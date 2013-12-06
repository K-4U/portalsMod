package k4unl.minecraft.portals.tiles;

import java.util.Random;
import java.util.logging.Level;

import k4unl.minecraft.portals.blocks.PortalCoreBlock;
import k4unl.minecraft.portals.items.Items;
import k4unl.minecraft.portals.items.PortalTunerItem;
import k4unl.minecraft.portals.lib.Functions;
import k4unl.minecraft.portals.lib.LogHelper;
import k4unl.minecraft.portals.lib.config.Constants;
import k4unl.minecraft.portals.lib.config.Ids;
import k4unl.minecraft.portals.vars.Portal;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class TilePortalCore extends TileEntity implements IInventory {
	private boolean isValidMultiblock;

	private static final int cornerBlockId = Ids.portalFrameBlock_actual;
	private static final int frameBlockId = Ids.portalFrameBlock_actual;
	private boolean isRedstonePowered = false;
	
	public int portalLinkId;
	private int direction = 0;
	private boolean isActive = false;
	
	private Portal ownPortal;
	
	public boolean isLinked(){
		return (portalLinkId != 0);
	}

	
	public TilePortalCore(){
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
		boolean spawnerConnected = false;
		boolean spawnerPresent = false;
		
		for(int horiz = portalX_neg; horiz <= portalX_pos; horiz++){	// Horizontal (X or Z)
			LogHelper.log(Level.INFO, "Starting new row");
			for(int vert = portalY_neg; vert <= portalY_pos; vert++){	// Vertical (Y)
				int x = xCoord + (direction == 1 ? horiz : 0);
				int y = yCoord + vert;
				int z = zCoord + (direction == 0 ? horiz : 0);
				boolean isCorner = false;
				boolean isTopRow = false;
				
				if(horiz == 0 && vert == 0){
					continue; //Looking at core block, continue on
				}
				
				int blockId = worldObj.getBlockId(x, y, z);
				LogHelper.log(Level.INFO, "X: " + x + 
										  " Y: " + y + 
										  " ID: " + blockId);
				
				//Check in the other direction to see if there's a spawner
				if(vert == 0){
					int x2 = xCoord + (direction == 0 ? horiz : 0);
					int z2 = zCoord + (direction == 1 ? horiz : 0);
					int blockId2 = worldObj.getBlockId(x2, y, z2);
					if(blockId2 == frameBlockId && (horiz == -1 || horiz == 1)){
						spawnerConnected = true;
					}
					if(blockId2 == Ids.portalSpawnerBlock_actual){
						spawnerPresent = true;
						if(horiz == 1 || horiz == -1){
							spawnerConnected = true;
						}
					}
				}
				
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
				
				/*if(isTopRow && blockId == Ids.portalIndicatorBlock_actual){
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
				}*/
				
				
				if((horiz >= (portalX_neg + 1)) && (horiz <= (portalX_pos - 1)) && 
						(vert >= (portalY_neg + 1)) && (vert <= (portalY_pos - 1))){
					if(blockId != 0){ //Center
						return false;
					}
				}else{
					if(blockId != frameBlockId){
						return false;
					}
				}
			}
		}
		if(spawnerConnected && spawnerPresent){
			return true;
		}else{
			return false;
		}
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
	
	public void setLink(int newLink){
		if(this.isActive){
			//Deactivate portal
			if(this.ownPortal.getIsValid()){
				//it SHOULD be valid, but you'll never know for sure
				this.ownPortal.deactivatePortal();
			}
		}
		this.portalLinkId = newLink;
		this.ownPortal.setLink(this.portalLinkId);
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
		boolean spawnerConnected = false;
		for(int horiz = portalX_neg; horiz <= portalX_pos; horiz++){	// Horizontal (X or Z)
			for(int vert = portalY_neg; vert <= portalY_pos; vert++){	// Vertical (Y)
				int x = xCoord + (direction == 1 ? horiz : 0);
				int y = yCoord + vert;
				int z = zCoord + (direction == 0 ? horiz : 0);
				int blockIdToSet = Ids.portalFrameBlock_actual;
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
				
				//if(blockId == Ids.portalIndicatorBlock_actual){
				//	TilePortalIndicator dummyTE = (TilePortalIndicator)worldObj.getBlockTileEntity(x, y, z);
				//	dummyTE.setCore(this);
				if(blockId == Ids.portalFrameBlock_actual){
					TilePortalFrame dummyTE = (TilePortalFrame)worldObj.getBlockTileEntity(x, y, z);
					dummyTE.setCore(this);	
				}
				
				if(vert == 0){
					//We are at core level. Find the spawner!
					int x2 = xCoord + (direction == 0 ? horiz : 0);
					int z2 = zCoord + (direction == 1 ? horiz : 0);
					blockId = worldObj.getBlockId(x2, y, z2);
					Functions.showMessageInChat(blockId + " found at (" + x2 + "," + y + "," + z2 + ")");
				}
			}
		}
		this.isValidMultiblock = true;
		//Create portal class
		if(this.ownPortal != null){
			this.ownPortal.close();
		}
		this.ownPortal = new Portal(worldObj, xCoord, yCoord, zCoord);
		this.ownPortal.setLink(this.portalLinkId);
	}
	
	
	private void revertDummies(){
		this.isValidMultiblock = false;
		if(this.ownPortal != null){
			this.ownPortal.setInvalid();
		}
		Minecraft.getMinecraft().thePlayer.addChatMessage("Portal destroyed");
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound){
		super.readFromNBT(tagCompound);
		
		isValidMultiblock = tagCompound.getBoolean("isMultiBlock");
		portalLinkId = tagCompound.getInteger("linkingId");
		
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
		tagCompound.setInteger("linkingId", portalLinkId);
		
		
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
		return 0;
	}
	

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		if(portalLinkId > 0){
			ItemStack portalItem = new ItemStack(Items.PortalTunerInstance, 1);
			NBTTagCompound tagCompound = new NBTTagCompound();
			tagCompound.setBoolean("isLinking", false);
			tagCompound.setInteger("linkingId", (new Random()).nextInt(1337));
			
			portalItem.setTagCompound(tagCompound);
			portalLinkId = 0;
			return portalItem;
		}else{
			return null;
		}
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemStack) {
		if(i > 0){
			return;
		}else{
			if(itemStack != null){
				if(itemStack.getItem() instanceof PortalTunerItem){
					NBTTagCompound tagCompound = itemStack.getTagCompound();
					portalLinkId = tagCompound.getInteger("linkingId");
				}
			}
		}

        this.onInventoryChanged();
	}

	@Override
	public String getInvName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isInvNameLocalized() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public void onInventoryChanged() {
		
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void openChest() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeChest() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemStack) {
		// TODO Auto-generated method stub
		if(itemStack.getItem() instanceof PortalTunerItem && i == 0){
			return true;
		}
		return false;
	}

	/*
	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		ForgeDirection s = ForgeDirection.getOrientation(side);
		if(s.equals(ForgeDirection.UP)){
			return null;
		}
		int ret[] = new int[1];
		ret[0] = 1;
		return ret;
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemstack, int side) {
		ForgeDirection s = ForgeDirection.getOrientation(side);
		if(s.equals(ForgeDirection.UP)){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int side) {
		ForgeDirection s = ForgeDirection.getOrientation(side);
		if(s.equals(ForgeDirection.UP)){
			return false;
		}else{
			return true;
		}
	}	*/


}
