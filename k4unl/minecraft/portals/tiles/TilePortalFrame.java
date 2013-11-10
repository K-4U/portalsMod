package k4unl.minecraft.portals.tiles;

import java.util.ArrayList;
import java.util.List;

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
	
	public boolean isCorner(){
		return false;
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
	
	private boolean isBlockAPortalBlock(int x, int y, int z){
		int bId = worldObj.getBlockId(x, y, z);
		return (bId == Ids.portalCoreBlock_actual || bId == Ids.portalFrameBlock_actual || bId == Ids.portalIndicatorBlock_actual);
	}
	
	public List<ForgeDirection> getConnectedSides() {
		List<ForgeDirection> retList = new ArrayList<ForgeDirection>();
		//Find other blocks:
		if(isBlockAPortalBlock(this.xCoord+1, this.yCoord, this.zCoord)) retList.add(ForgeDirection.WEST);
		if(isBlockAPortalBlock(this.xCoord-1, this.yCoord, this.zCoord)) retList.add(ForgeDirection.EAST);
		if(isBlockAPortalBlock(this.xCoord, this.yCoord+1, this.zCoord)) retList.add(ForgeDirection.UP);
		if(isBlockAPortalBlock(this.xCoord, this.yCoord-1, this.zCoord)) retList.add(ForgeDirection.DOWN);
		if(isBlockAPortalBlock(this.xCoord, this.yCoord, this.zCoord+1)) retList.add(ForgeDirection.SOUTH);
		if(isBlockAPortalBlock(this.xCoord, this.yCoord, this.zCoord-1)) retList.add(ForgeDirection.NORTH);
		return retList;
	}
}
