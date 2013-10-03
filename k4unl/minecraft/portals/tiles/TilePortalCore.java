package k4unl.minecraft.portals.tiles;

import java.util.logging.Level;

import k4unl.minecraft.portals.blocks.PortalCoreBlock;
import k4unl.minecraft.portals.lib.LogHelper;
import k4unl.minecraft.portals.lib.config.Ids;
import mods.multifurnace.block.BlockMultiFurnaceCore;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

public class TilePortalCore extends TileEntity {
	private boolean isValidMultiblock;
	private static final int portalWidth = 5;
	private static final int portalHeight = 5;
	/*
	private static final int portalX_neg = -2;
	private static final int portalX_pos = 2;
	private static final int portalY_neg = 0;
	private static final int portalY_pos = 4;
	private static final int portalZ_neg = 0;
	private static final int portalZ_pos = 0;*/
	private static final int cornerBlockId = Block.blockGold.blockID;
	private static final int frameBlockId = Block.brick.blockID;
	private int direction = 0;
	
	
	public TilePortalCore()
	{
	}
	
	public boolean getIsValid()
	{
		return isValidMultiblock;
	}
	
	public void invalidateMultiblock()
	{
		isValidMultiblock = false;
		
		int metadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		metadata = metadata & BlockMultiFurnaceCore.MASK_DIR;
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, metadata, 2);
		
		revertDummies();
	}
	
	/*
	 * Checks for a complete portal in the direction of "direction"
	 */
	private boolean checkForPortal(int direction){
		int portalX_neg = 0-(portalWidth-1)/2;
		int portalX_pos = (portalWidth-1)/2;
		int portalY_neg = 0;
		int portalY_pos = (portalHeight-1);
		
		for(int horiz = portalX_neg; horiz <= portalX_pos; horiz++){	// Horizontal (X or Z)
			for(int vert = portalY_neg; vert <= portalY_pos; vert++){	// Vertical (Y)
				int x = xCoord + (direction == 1 ? horiz : 0);
				int y = yCoord + vert;
				int z = zCoord + (direction == 0 ? horiz : 0);
				boolean isCorner = false;
				
				int blockId = worldObj.getBlockId(x, y, z);
				LogHelper.log(Level.INFO, "X: " + x + " Y: " + y + " ID: " + blockId);
				
				//Check if we are at a corner
				isCorner = ((horiz == portalX_neg || horiz == portalX_pos) && (vert == portalY_neg || vert == portalY_pos));
				
				if(isCorner && blockId != cornerBlockId){
					return false;
				}else if(isCorner){
					continue;
				}
				
				if(horiz == 0 && vert == 0){
					continue; //Looking at core block, continue on
				}
				if((horiz >= (portalX_neg + 1)) && (horiz <= (portalX_pos - 1)) && 
						(vert >= (portalY_neg + 1)) && (vert <= (portalY_pos - 1))){
					if(blockId != 0){
						return false;
					}
				}else{
					if(blockId != frameBlockId){
						return false;
					}
				}
			}
		}
		return true;
	}
	
	public boolean checkIfProperlyFormed(){
		//Check if blocks are surrounding him:
		if(checkForPortal(0)){
			LogHelper.log(Level.INFO, "Found in 0");
			direction = 0;
			return true;
		}
		if(checkForPortal(1)){
			LogHelper.log(Level.INFO, "Found in 1");
			direction = 1;
			return true;			
		}
		return false;
	}
	
	public void convertDummies(){
		int portalX_neg = 0-(portalWidth-1)/2;
		int portalX_pos = (portalWidth-1)/2;
		int portalY_neg = 0;
		int portalY_pos = (portalHeight-1);
		
		for(int horiz = portalX_neg; horiz <= portalX_pos; horiz++){	// Horizontal (X or Z)
			for(int vert = portalY_neg; vert <= portalY_pos; vert++){	// Vertical (Y)
				int x = xCoord + (direction == 1 ? horiz : 0);
				int y = yCoord + vert;
				int z = zCoord + (direction == 0 ? horiz : 0);
				int blockIdToSet = Ids.portalDummyBlock_actual;
				
				if(horiz == 0 && vert == 0){
					continue; //Looking at core block, continue on
				}
				if((horiz >= (portalX_neg + 1)) && (horiz <= (portalX_pos - 1))){
					//Do leave the edge standing please
					if((vert >= (portalY_neg + 1)) && (vert <= (portalY_pos - 1))){
						blockIdToSet = Ids.portalPortalBlock_actual;
					}
				}
				
				worldObj.setBlock(x, y, z, blockIdToSet);
				worldObj.markBlockForUpdate(x, y, z);
				//if(blockIdToSet == Ids.portalDummyBlock_actual){
				TilePortalDummy dummyTE = (TilePortalDummy)worldObj.getBlockTileEntity(x, y, z);
				dummyTE.setCore(this);
				//}
			}
		}
		
		isValidMultiblock = true;
	}
	
	private void revertDummies(){
		int portalX_neg = 0-(portalWidth-1)/2;
		int portalX_pos = (portalWidth-1)/2;
		int portalY_neg = 0;
		int portalY_pos = (portalHeight-1);
		
		for(int horiz = portalX_neg; horiz <= portalX_pos; horiz++){	// Horizontal (X or Z)
			for(int vert = portalY_neg; vert <= portalY_pos; vert++){	// Vertical (Y)
				int x = xCoord + (direction == 1 ? horiz : 0);
				int y = yCoord + vert;
				int z = zCoord + (direction == 0 ? horiz : 0);
				boolean isCorner = false;
				int blockIdToSet = Block.brick.blockID;
				
				int blockId = worldObj.getBlockId(x, y, z);
				isCorner = ((horiz == portalX_neg || horiz == portalX_pos) && (vert == portalY_neg || vert == portalY_pos));
				
				if(horiz == 0 && vert == 0)
					continue;
				
				if(blockId == Ids.portalDummyBlock_actual){
					if(isCorner){
						worldObj.setBlock(x, y, z, cornerBlockId);
					}else{
						worldObj.setBlock(x, y, z, frameBlockId);
					}
				}else if(blockId == Ids.portalPortalBlock_actual){
					worldObj.setBlock(x, y, z, 0);
				}
				
				worldObj.markBlockForUpdate(x, y, z);
			}
		}
		isValidMultiblock = false;
	}
}
