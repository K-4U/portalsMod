package k4unl.minecraft.portals.tiles;

import java.util.HashMap;
import java.util.Map;

import joptsimple.util.KeyValuePair;
import k4unl.minecraft.portals.lib.config.Ids;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;


public class TilePortalFrame extends TileEntity{
	TilePortalCore tileCore;
	int coreX;
	int coreY;
	int coreZ;
	boolean isRedstonePowered = false;
	
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
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound){
		super.writeToNBT(tagCompound);
		
		tagCompound.setInteger("CoreX", coreX);
		tagCompound.setInteger("CoreY", coreY);
		tagCompound.setInteger("CoreZ", coreZ);
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
	
	private boolean shouldConnectTo(int bId){
		return (bId == Ids.portalCoreBlock_actual || bId == Ids.portalFrameBlock_actual || bId == Ids.portalIndicatorBlock_actual || bId == Ids.portalSpawnerBlock_actual);
	}
	
	public Map<ForgeDirection, Integer> getConnectedSides() {
		Map<ForgeDirection, Integer> retList = new HashMap<ForgeDirection, Integer>();
		//Find other blocks:
		retList.put(ForgeDirection.WEST, getBlockId(this.xCoord+1, this.yCoord, this.zCoord));
		retList.put(ForgeDirection.EAST, getBlockId(this.xCoord-1, this.yCoord, this.zCoord));
		retList.put(ForgeDirection.UP, getBlockId(this.xCoord, this.yCoord+1, this.zCoord));
		retList.put(ForgeDirection.DOWN, getBlockId(this.xCoord, this.yCoord-1, this.zCoord));
		retList.put(ForgeDirection.SOUTH, getBlockId(this.xCoord, this.yCoord, this.zCoord+1));
		retList.put(ForgeDirection.NORTH, getBlockId(this.xCoord, this.yCoord, this.zCoord-1));
		
		Map<ForgeDirection, Integer> retMap = new HashMap<ForgeDirection, Integer>();
		
		for (Map.Entry<ForgeDirection, Integer> entry : retList.entrySet()) {
		    if(shouldConnectTo(entry.getValue())){
		    	retMap.put(entry.getKey(), entry.getValue());
		    }
		}
		return retMap;
	}
}
