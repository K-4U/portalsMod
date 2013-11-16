package k4unl.minecraft.portals.tiles;

import java.util.logging.Level;

import k4unl.minecraft.portals.lib.LogHelper;
import k4unl.minecraft.portals.lib.config.Constants;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TilePortalPortal extends TileEntity{
	
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
		if(tileCore == null){
			tileCore = (TilePortalCore)worldObj.getBlockTileEntity(coreX, coreY, coreZ);
			if(tileCore == null){
				TileEntity tEnt = worldObj.getBlockTileEntity(xCoord, yCoord, zCoord);
				LogHelper.log(Level.INFO, "Derp message");
			}
		}
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
}
