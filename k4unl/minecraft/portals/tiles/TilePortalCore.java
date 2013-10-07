package k4unl.minecraft.portals.tiles;

import java.util.logging.Level;

import k4unl.minecraft.portals.blocks.PortalCoreBlock;
import k4unl.minecraft.portals.lib.LogHelper;
import k4unl.minecraft.portals.lib.config.Ids;
import k4unl.minecraft.portals.vars.PortalStorage.Portal;
import k4unl.minecraft.portals.lib.config.Constants;
import k4unl.minecraft.portals.vars.Types.portalColor;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TilePortalCore extends TileEntity {
	private boolean isValidMultiblock;

	private static final int cornerBlockId = Block.blockGold.blockID;
	private static final int frameBlockId = Block.brick.blockID;
	private boolean isRedstonePowered = false;
	
	private portalColor portalColors;
	private int direction = 0;
	private boolean isActive = false;
	
	private Portal ownPortal;
	
	public TilePortalCore(){
		this.portalColors = new portalColor(0,0,0);
		this.ownPortal = new Portal();
	}
	
	public boolean getIsValid(){
		return isValidMultiblock;
	}
	
	public boolean getIsActive(){
		return isActive;
	}
	
	public void invalidateMultiblock(){
		if(isValidMultiblock){ //To prevent infinite loops
			isValidMultiblock = false;
			
			int metadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
			metadata = metadata & PortalCoreBlock.MASK_DIR;
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, metadata, 2);
			
			revertDummies();
		}
	}
	
	/*
	 * Checks for a complete portal in the direction of "direction"
	 */
	private boolean checkForPortal(int direction){
		int portalX_neg = 0-(Constants.portalWidth-1)/2;
		int portalX_pos = (Constants.portalWidth-1)/2;
		int portalY_neg = 0;
		int portalY_pos = (Constants.portalHeight-1);
		
		for(int horiz = portalX_neg; horiz <= portalX_pos; horiz++){	// Horizontal (X or Z)
			LogHelper.log(Level.INFO, "Starting new row");
			for(int vert = portalY_neg; vert <= portalY_pos; vert++){	// Vertical (Y)
				int x = xCoord + (direction == 1 ? horiz : 0);
				int y = yCoord + vert;
				int z = zCoord + (direction == 0 ? horiz : 0);
				boolean isCorner = false;
				boolean isTopRow = false;
				
				int blockId = worldObj.getBlockId(x, y, z);
				LogHelper.log(Level.INFO, "X: " + x + 
										  " Y: " + y + 
										  " ID: " + blockId);
				
				//Check if we are at a corner
				isCorner = ((horiz == portalX_neg || horiz == portalX_pos)
						&& (vert == portalY_neg || vert == portalY_pos));
				
				if(isCorner && blockId != cornerBlockId){
					return false;
				}else if(isCorner){
					continue;
				}
				
				//If we are at top row, and not in a corner, we are looking at the wool
				isTopRow = (!isCorner && (vert == portalY_pos));
				
				if(isTopRow && blockId == Block.cloth.blockID){
					//Fetch the color of the wool.
					if(horiz == -1){
						this.portalColors.setColor(0, 
								worldObj.getBlockMetadata(x, y, z)); 
					}else if(horiz == 0){
						this.portalColors.setColor(1, 
								worldObj.getBlockMetadata(x, y, z)); 
					}else if(horiz == 1){
						this.portalColors.setColor(2, 
								worldObj.getBlockMetadata(x, y, z)); 
					}
					continue;
				}else if(isTopRow && blockId != Block.cloth.blockID){
					return false;
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
	
	public void setColor(int index, int newColor){
		if(this.isActive){
			//Deactivate portal
			if(this.ownPortal.getIsValid()){
				//it SHOULD be valid, but you'll never know for sure
				this.ownPortal.deactivatePortal();
			}
		}
		this.portalColors.setColor(index, newColor);
		this.ownPortal.setColors(this.portalColors);
		//Also, update the child up there.
		int horiz = index - 1;
		int vert = Constants.portalHeight-1;
		int x = xCoord + (direction == 1 ? horiz : 0);
		int y = yCoord + vert;
		int z = zCoord + (direction == 0 ? horiz : 0);
		
		worldObj.setBlockMetadataWithNotify(x, y, z, newColor, 2); //Curious to see if this works
	}
	
	public void activatePortal(){
		int portalX_neg = (0-(Constants.portalWidth-1)/2)+1;
		int portalX_pos = ((Constants.portalWidth-1)/2)-1;
		int portalY_neg = 1;
		int portalY_pos = ((Constants.portalHeight-1)-1);
		
		for(int horiz = portalX_neg; horiz <= portalX_pos; horiz++){	// Horizontal (X or Z)
			for(int vert = portalY_neg; vert <= portalY_pos; vert++){	// Vertical (Y)
				int x = xCoord + (direction == 1 ? horiz : 0);
				int y = yCoord + vert;
				int z = zCoord + (direction == 0 ? horiz : 0);
				int blockIdToSet = Ids.portalPortalBlock_actual;
				
				
				worldObj.setBlock(x, y, z, blockIdToSet);
				worldObj.markBlockForUpdate(x, y, z);
				TilePortalPortal dummyTE = (TilePortalPortal)worldObj.getBlockTileEntity(x, y, z);
				if(dummyTE instanceof TilePortalPortal){
					dummyTE.setCore(this);
				}
			}
		}
		this.isActive = true;
	}
	

	public void deactivatePortal() {
		this.isActive = false;
		int portalX_neg = (0-(Constants.portalWidth-1)/2)+1;
		int portalX_pos = ((Constants.portalWidth-1)/2)-1;
		int portalY_neg = 1;
		int portalY_pos = ((Constants.portalHeight-1)-1);
		
		for(int horiz = portalX_neg; horiz <= portalX_pos; horiz++){	// Horizontal (X or Z)
			for(int vert = portalY_neg; vert <= portalY_pos; vert++){	// Vertical (Y)
				int x = xCoord + (direction == 1 ? horiz : 0);
				int y = yCoord + vert;
				int z = zCoord + (direction == 0 ? horiz : 0);
				int blockIdToSet = 0;
				
				if(horiz == 0 && vert == 0){
					continue; //Looking at core block, continue on
				}
				
				worldObj.setBlock(x, y, z, blockIdToSet);
				worldObj.markBlockForUpdate(x, y, z);
			}
		}
	}
	
	public void convertDummies(){
		int portalX_neg = 0-(Constants.portalWidth-1)/2;
		int portalX_pos = (Constants.portalWidth-1)/2;
		int portalY_neg = 0;
		int portalY_pos = (Constants.portalHeight-1);
		
		for(int horiz = portalX_neg; horiz <= portalX_pos; horiz++){	// Horizontal (X or Z)
			for(int vert = portalY_neg; vert <= portalY_pos; vert++){	// Vertical (Y)
				int x = xCoord + (direction == 1 ? horiz : 0);
				int y = yCoord + vert;
				int z = zCoord + (direction == 0 ? horiz : 0);
				int blockIdToSet = Ids.portalDummyBlock_actual;
				int blockId = worldObj.getBlockId(x, y, z);
				int metaDataToSet = 0;
				
				if(horiz == 0 && vert == 0){
					continue; //Looking at core block, continue on
				}
				if((horiz >= (portalX_neg + 1)) && (horiz <= (portalX_pos - 1))){
					//Do leave the edge standing please
					if((vert >= (portalY_neg + 1)) && (vert <= (portalY_pos - 1))){
						//blockIdToSet = Ids.portalPortalBlock_actual;
						blockIdToSet = 0;
						//Only do this if the portal is active!
					}
				}
				//Check if current ID is wool, that should be reverted to the indicator:
				//If we are at top row, and not in a corner, we are looking at the wool
				if(blockId == Block.cloth.blockID){
					//Fetch the color of the wool.
					metaDataToSet = worldObj.getBlockMetadata(x, y, z);
					blockIdToSet = Ids.portalIndicatorBlock_actual;
				}
				
				worldObj.setBlock(x, y, z, blockIdToSet);
				if(blockIdToSet == Ids.portalIndicatorBlock_actual){
					worldObj.setBlockMetadataWithNotify(x, y, z, metaDataToSet, 2);
				}
				worldObj.markBlockForUpdate(x, y, z);
				if(blockIdToSet == Ids.portalIndicatorBlock_actual){
					TilePortalIndicator dummyTE = (TilePortalIndicator)worldObj.getBlockTileEntity(x, y, z);
					dummyTE.setCore(this);
				}else if(blockIdToSet == Ids.portalDummyBlock_actual){
					TilePortalDummy dummyTE = (TilePortalDummy)worldObj.getBlockTileEntity(x, y, z);
					dummyTE.setCore(this);	
				}
			}
		}
		
		isValidMultiblock = true;
		//Create portal class
		if(this.ownPortal != null){
			this.ownPortal.close();
		}
		this.ownPortal = new Portal(worldObj, xCoord, yCoord, zCoord);
		this.ownPortal.setColors(this.portalColors);
	}
	
	
	private void revertDummies(){
		int portalX_neg = 0-(Constants.portalWidth-1)/2;
		int portalX_pos = (Constants.portalWidth-1)/2;
		int portalY_neg = 0;
		int portalY_pos = (Constants.portalHeight-1);
		
		portalColors = ownPortal.getColors();
		
		for(int horiz = portalX_neg; horiz <= portalX_pos; horiz++){	// Horizontal (X or Z)
			for(int vert = portalY_neg; vert <= portalY_pos; vert++){	// Vertical (Y)
				int x = xCoord + (direction == 1 ? horiz : 0);
				int y = yCoord + vert;
				int z = zCoord + (direction == 0 ? horiz : 0);
				boolean isCorner = false;
				boolean isTopRow = false;
				
				int blockId = worldObj.getBlockId(x, y, z);
				isCorner = ((horiz == portalX_neg || horiz == portalX_pos) && (vert == portalY_neg || vert == portalY_pos));
				
				isTopRow = (!isCorner && (vert == portalY_pos));
				
				if(horiz == 0 && vert == 0)
					continue;
				
				if(blockId == Ids.portalDummyBlock_actual || 
						blockId == Ids.portalIndicatorBlock_actual){
					if(isCorner){
						worldObj.setBlock(x, y, z, cornerBlockId);
					}else if(isTopRow){
						worldObj.setBlock(x, y, z, Block.cloth.blockID);
						if(horiz == -1){
							worldObj.setBlockMetadataWithNotify(x, y, z,
								portalColors.getColor(0), 2);
						}else if(horiz == 0){
							worldObj.setBlockMetadataWithNotify(x, y, z,
									portalColors.getColor(1), 2);
						}else if(horiz == 1){
							worldObj.setBlockMetadataWithNotify(x, y, z,
									portalColors.getColor(2), 2);
						}
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
		if(this.ownPortal != null){
			this.ownPortal.setInvalid();
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound){
		super.readFromNBT(tagCompound);
		
		isValidMultiblock = tagCompound.getBoolean("isMultiBlock");
		
		if(isValidMultiblock){
			if(this.ownPortal.getIsValid() == false){
				this.ownPortal = new Portal(worldObj, xCoord, yCoord, zCoord);
			}
			this.ownPortal.setWorldObj(worldObj);
			this.ownPortal.readFromNBTData(tagCompound);
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound){
		super.writeToNBT(tagCompound);
		
		tagCompound.setBoolean("isMultiBlock", isValidMultiblock);
		
		if(isValidMultiblock){
			this.ownPortal.writeToNBT(tagCompound);
		}
	}
	
	public void redstoneChanged(boolean redstoneActive){
		this.isActive = redstoneActive;
		this.ownPortal.setWorldObj(worldObj);
		if(redstoneActive){
			if(this.ownPortal.getIsValid()){
				this.ownPortal.activatePortal();
			}
		}else{
			if(this.ownPortal.getIsValid()){
				//it SHOULD be valid, but you'll never know for sure
				this.ownPortal.deactivatePortal();
			}
		}
	}

	public Portal getMasterClass() {
		return this.ownPortal;
	}

	public void checkRedstonePower() {
		boolean isIndirectlyPowered = worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
		if(isIndirectlyPowered && !isRedstonePowered){
			LogHelper.log(Level.INFO, "Redstone change");
			isRedstonePowered = true;
			this.redstoneChanged(isRedstonePowered);
		}else if(isRedstonePowered && !isIndirectlyPowered){
			LogHelper.log(Level.INFO, "Redstone change");
			isRedstonePowered = false;
			this.redstoneChanged(isRedstonePowered);
		}
	}

	public int getIndicatorNumber(int x, int y, int z) {
		if(y == (yCoord + Constants.portalHeight)-1){
			if(direction == 1){
				int diff = x - xCoord;
				return diff + 1;
			}else{
				int diff = z - zCoord;
				return diff + 1;
			}
		}
		//int x = xCoord + (direction == 1 ? horiz : 0);
		//int y = yCoord + vert;
		//int z = zCoord + (direction == 0 ? horiz : 0);
		return 0;
	}

}
