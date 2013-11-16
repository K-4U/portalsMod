package k4unl.minecraft.portals.tiles;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.common.network.PacketDispatcher;
import joptsimple.util.KeyValuePair;
import k4unl.minecraft.portals.lib.config.Ids;
import k4unl.minecraft.portals.lib.config.ModInfo;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;


public class TilePortalSpawner extends TileEntity{
	TilePortalCore tileCore;
	int coreX;
	int coreY;
	int coreZ;
	boolean isRedstonePowered = false;
	float rotation = 0F;
	
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
		rotation = tagCompound.getFloat("rotation");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound){
		super.writeToNBT(tagCompound);
		
		tagCompound.setInteger("CoreX", coreX);
		tagCompound.setInteger("CoreY", coreY);
		tagCompound.setInteger("CoreZ", coreZ);
		tagCompound.setFloat("rotation", rotation);
	}
	
	public void checkRedstonePower() {
		boolean isIndirectlyPowered = worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
		if(isIndirectlyPowered && !isRedstonePowered){
			//LogHelper.log(Level.INFO, "Redstone change");
			isRedstonePowered = true;
			TilePortalCore core = this.getCore();
			core.redstoneChanged(isRedstonePowered);
		}else if(isRedstonePowered && !isIndirectlyPowered){
			//LogHelper.log(Level.INFO, "Redstone change");
			isRedstonePowered = false;
			TilePortalCore core = this.getCore();
			core.redstoneChanged(isRedstonePowered);
		}
	}
	
	private int getBlockId(int x, int y, int z){
		int bId = worldObj.getBlockId(x, y, z);
		return bId;
	}
	
	private void updateServerRotation(){
		ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
	    DataOutputStream outputStream = new DataOutputStream(bos);
	    try {
	        outputStream.writeInt(xCoord);
	        outputStream.writeInt(yCoord);
	        outputStream.writeInt(zCoord);
	        outputStream.writeFloat(this.rotation);
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	               
	    Packet250CustomPayload packet = new Packet250CustomPayload();
	    packet.channel = ModInfo.CHANNEL;
	    packet.data = bos.toByteArray();
	    packet.length = bos.size();

		if(worldObj.isRemote){ //Client
			//PacketDispatcher.sendPacketToServer(packet);
		}else{ //Server
			//PacketDispatcher.sendPacketToAllPlayers(packet);
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
	}
	
	public float getRotation(){
		return this.rotation;
	}
	
	public void setRotation(float f) {
		this.rotation = f;
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
