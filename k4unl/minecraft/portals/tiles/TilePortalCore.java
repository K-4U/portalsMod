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
	private static final int portalX_neg = -2;
	private static final int portalX_pos = 2;
	private static final int portalY_neg = 0;
	private static final int portalY_pos = 4;
	private static final int portalZ_neg = 0;
	private static final int portalZ_pos = 0;
	
	
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
	
	public boolean checkIfProperlyFormed()
	{
		int dir = (getBlockMetadata() & BlockMultiFurnaceCore.MASK_DIR);
		
		int depthMultiplier = ((dir == BlockMultiFurnaceCore.META_DIR_NORTH || dir == BlockMultiFurnaceCore.META_DIR_WEST) ? 1 : -1);
		boolean forwardZ = ((dir == BlockMultiFurnaceCore.META_DIR_NORTH) || (dir == BlockMultiFurnaceCore.META_DIR_SOUTH));
		
		/*
		 * 			FORWARD		BACKWARD
		 * North:	-z				+z
		 * South:	+z				-z
		 * East:	+x				-x
		 * West:	-x				+x
		 * 
		 * Should move BACKWARD for depth (facing = direction of block face, not direction of player looking at face)
		 */
		
		for(int horiz = portalX_neg; horiz <= portalX_pos; horiz++)	// Horizontal (X or Z)
		{
			for(int vert = portalY_neg; vert <= portalY_pos; vert++)	// Vertical (Y)
			{
				for(int depth = portalZ_neg; depth <= portalZ_pos; depth++)	// Depth (Z or X)
				{
					int x = xCoord + (forwardZ ? horiz : (depth * depthMultiplier));
					int y = yCoord + vert;
					int z = zCoord + (forwardZ ? (depth * depthMultiplier) : horiz);
					
					int blockId = worldObj.getBlockId(x, y, z);
					LogHelper.log(Level.INFO, "X: " + horiz + " Y: " + vert + " Z: " + depth + " ID: " + blockId);
					
					if(horiz == 0 && vert == 0 && depth == 0){
						continue; //Looking at core block, continue on
					}
					if((horiz >= (portalX_neg + 1)) && (horiz <= (portalX_pos - 1))){
						//Do leave the edge standing please
						if((vert >= (portalY_neg + 1)) && (vert <= (portalY_pos - 1))){
							if(blockId != 0){
								return false;
							}
						}
					}
					
					
					if(blockId != Block.brick.blockID && blockId != 0)
						return false;
				}
			}
		}
		
		return true;
	}
	
	public void convertDummies()
	{
		int dir = (getBlockMetadata() & PortalCoreBlock.MASK_DIR);
		
		int depthMultiplier = ((dir == PortalCoreBlock.META_DIR_NORTH || dir == PortalCoreBlock.META_DIR_WEST) ? 1 : -1);
		boolean forwardZ = ((dir == PortalCoreBlock.META_DIR_NORTH) || (dir == PortalCoreBlock.META_DIR_SOUTH));
		
		/*
		 * 			FORWARD		BACKWARD
		 * North:	-z				+z
		 * South:	+z				-z
		 * East:	+x				-x
		 * West:	-x				+x
		 * 
		 * Should move BACKWARD for depth (facing = direction of block face, not direction of player looking at face)
		 */
		
		for(int horiz = portalX_neg; horiz <= portalX_pos; horiz++)	// Horizontal (X or Z)
		{
			for(int vert = portalY_neg; vert <= portalY_pos; vert++)	// Vertical (Y)
			{
				for(int depth = portalZ_neg; depth <= portalZ_pos; depth++)	// Depth (Z or X)
				{
					int x = xCoord + (forwardZ ? horiz : (depth * depthMultiplier));
					int y = yCoord + vert;
					int z = zCoord + (forwardZ ? (depth * depthMultiplier) : horiz);
					int blockIdToSet = Ids.portalDummyBlock_actual;
					
					if(horiz == 0 && vert == 0 && depth == 0){
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
					if(blockIdToSet == Ids.portalDummyBlock_actual){
						TilePortalDummy dummyTE = (TilePortalDummy)worldObj.getBlockTileEntity(x, y, z);
						dummyTE.setCore(this);
					}
				}
			}
		}
		
		isValidMultiblock = true;
	}
	
	private void revertDummies()
	{
		int dir = (getBlockMetadata() & PortalCoreBlock.MASK_DIR);
		
		int depthMultiplier = ((dir == PortalCoreBlock.META_DIR_NORTH || dir == PortalCoreBlock.META_DIR_WEST) ? 1 : -1);
		boolean forwardZ = ((dir == PortalCoreBlock.META_DIR_NORTH) || (dir == PortalCoreBlock.META_DIR_SOUTH));
		
		/*
		 * 			FORWARD		BACKWARD
		 * North:	-z				+z
		 * South:	+z				-z
		 * East:	+x				-x
		 * West:	-x				+x
		 * 
		 * Should move BACKWARD for depth (facing = direction of block face, not direction of player looking at face)
		 */
		
		for(int horiz = portalX_neg; horiz <= portalX_pos; horiz++)	// Horizontal (X or Z)
		{
			for(int vert = portalY_neg; vert <= portalY_pos; vert++)	// Vertical (Y)
			{
				for(int depth = portalZ_neg; depth <= portalZ_pos; depth++)	// Depth (Z or X)
				{
					int x = xCoord + (forwardZ ? horiz : (depth * depthMultiplier));
					int y = yCoord + vert;
					int z = zCoord + (forwardZ ? (depth * depthMultiplier) : horiz);
					int blockIdToSet = Block.brick.blockID;
					
					int blockId = worldObj.getBlockId(x, y, z);
					
					if(horiz == 0 && vert == 0 && (depth == 0 || depth == 1))
						continue;
					
					if(blockId == Ids.portalDummyBlock_actual){
						worldObj.setBlock(x, y, z, Block.brick.blockID);
					}else if(blockId == Ids.portalPortalBlock_actual){
						worldObj.setBlock(x, y, z, 0);
					}
					
					worldObj.markBlockForUpdate(x, y, z);
				}
			}
		}
		
		isValidMultiblock = false;
	}
}
