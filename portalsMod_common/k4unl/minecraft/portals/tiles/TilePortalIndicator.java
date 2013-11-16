package k4unl.minecraft.portals.tiles;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.logging.Level;

import k4unl.minecraft.portals.lib.Functions;
import k4unl.minecraft.portals.lib.LogHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.network.PacketDispatcher;


public class TilePortalIndicator extends TileEntity{
	TilePortalCore tileCore;
	int coreX;
	int coreY;
	int coreZ;
	boolean isRedstonePowered = false;
	private boolean isRotated = false;
	
	public void setCore(TilePortalCore core){
		coreX = core.xCoord;
		coreY = core.yCoord;
		coreZ = core.zCoord;
		tileCore = core;
	}
	
	public TilePortalCore getCore(){
		if(tileCore == null)
			tileCore = (TilePortalCore)worldObj.getBlockTileEntity(coreX, coreY, coreZ);
		
		return tileCore;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound){
		super.readFromNBT(tagCompound);
		
		coreX = tagCompound.getInteger("CoreX");
		coreY = tagCompound.getInteger("CoreY");
		coreZ = tagCompound.getInteger("CoreZ");
		this.isRotated = tagCompound.getBoolean("isRotated");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound){
		super.writeToNBT(tagCompound);
		
		tagCompound.setInteger("CoreX", coreX);
		tagCompound.setInteger("CoreY", coreY);
		tagCompound.setInteger("CoreZ", coreZ);
		tagCompound.setBoolean("isRotated", isRotated);
	}
	
	public void checkRedstonePower() {
		boolean isIndirectlyPowered = worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
		if(isIndirectlyPowered && !isRedstonePowered){
			LogHelper.log(Level.INFO, "Redstone change");
			isRedstonePowered = true;
			TilePortalCore core = this.getCore();
			core.redstoneChanged(isRedstonePowered);
		}else if(isRedstonePowered && !isIndirectlyPowered){
			LogHelper.log(Level.INFO, "Redstone change");
			isRedstonePowered = false;
			TilePortalCore core = this.getCore();
			core.redstoneChanged(isRedstonePowered);
		}
	}
	
	private void updateServerRotation(){
		ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
	    DataOutputStream outputStream = new DataOutputStream(bos);
	    try {
	        outputStream.writeInt(xCoord);
	        outputStream.writeInt(yCoord);
	        outputStream.writeInt(zCoord);
	        //write the relevant information here... exemple:
	        outputStream.writeBoolean(this.isRotated);
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	               
	    Packet250CustomPayload packet = new Packet250CustomPayload();
	    packet.channel = "GenericRandom";
	    packet.data = bos.toByteArray();
	    packet.length = bos.size();

		if(worldObj.isRemote){ //Client
			PacketDispatcher.sendPacketToServer(packet);
		}else{ //Server
			//PacketDispatcher.sendPacketToAllPlayers(packet);
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
	    
	    
	    
	}

	public Color getColor() {
		return Functions.dye2Rgb(Functions.getDyeFromBlock(worldObj.getBlockMetadata(xCoord, yCoord, zCoord)));
	}
	
	public boolean getIsRotated(){
			return this.isRotated;
	}

	public void setRotated(boolean b) {
		this.isRotated = b;
		updateServerRotation();
	}
	
	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData packet){
		NBTTagCompound tag = packet.data;
		this.readFromNBT(tag);

	}
	
	@Override
	public Packet getDescriptionPacket(){
	     NBTTagCompound var1 = new NBTTagCompound();
	     this.writeToNBT(var1);
	     return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 4, var1);
	}

}
