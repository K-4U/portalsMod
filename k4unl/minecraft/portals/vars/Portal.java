package k4unl.minecraft.portals.vars;

import java.util.Random;

import k4unl.minecraft.portals.lib.Functions;
import k4unl.minecraft.portals.lib.config.Constants;
import k4unl.minecraft.portals.tiles.TilePortalCore;
import k4unl.minecraft.portals.vars.Types.Location;
import k4unl.minecraft.portals.vars.Types.portalColor;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class Portal{
	private Location portalLocation;
	private Location portalSpwanLocation; // The location the player should teleport to, when going to this portal
	private Portal linkedPortal;
	private World worldObj;
	private boolean isLinked = false;
	private boolean isValid = true;
	private boolean isActive = false;
	
	/*
	 * List:
	 * 0 = no error
	 * 1 = more than 2 portals already with this color
	 * 2 = portal no longer valid multiblock
	 */
	private int cannotFormReason = 0;
	
	
	private portalColor portalColors;
	
	public Portal(){
		this.isValid = false;
	}
	
	public Portal(World worldObj, int x, int y, int z){
		this.portalLocation = new Location(x, y, z);
		portalColors = new portalColor(0, 0, 0);
		//Add it to the big list:
		PortalStorage.addToPortalList(this);
		
		this.worldObj = worldObj;
	}
	
	public void randomTick(World world, int x, int y, int z, Random random){
		
	}
	
	public void link(Portal portalToLink){
		this.linkedPortal = portalToLink;
		this.isLinked = true;
	}
	
	public void unlink(){
		//Make sure we don't get infinite loops:
		Portal tLink = linkedPortal;
		linkedPortal = null;
		tLink.unlink();
		this.isLinked = false;
	}
	
	
	public Location getLocation(){
		return this.portalLocation;
	}
	
	private void checkLink(){
		int sameColors = PortalStorage.getPortalCountByColor(this.portalColors);
		if(sameColors > 2 || sameColors == 1){
			isLinked = false;
		}else if(sameColors == 2){ // 2, this one counts as well
			//Link the portals together
			Portal linkedPortal = PortalStorage.getPortalByColor(this, 
					this.portalColors);
			linkedPortal.link(this);
			this.link(linkedPortal);
		}
	}
	
	public void setColors(portalColor newColors){
		portalColors = newColors;
		this.checkLink();
	}
	
	public portalColor getColors(){
		return this.portalColors;
	}
	
	public void setWorldObj(World worldObj){
		this.worldObj = worldObj;
	}
	
	public void readFromNBTData(NBTTagCompound data){
		if(portalLocation == null){
			portalLocation = new Location(data.getInteger("x"),
					data.getInteger("y"), data.getInteger("z"));
		}else{
			portalLocation.setLocation(data.getInteger("x"),
				data.getInteger("y"), data.getInteger("z"));
		}
		
		isActive = data.getBoolean("isActive");
		if(isActive){
			this.activatePortal();
		}
		portalColors = new portalColor(data.getIntArray("portalColors"));
		
		//Recheck link
		this.checkLink();
	}

	public void writeToNBT(NBTTagCompound data) {
		data.setInteger("x", portalLocation.getX());
		data.setInteger("y", portalLocation.getY());
		data.setInteger("z", portalLocation.getZ());
		data.setBoolean("isActive", isActive);
		
		data.setIntArray("portalColors", portalColors.getColors());
	}

	public void setInvalid() {
		this.isValid = false;
		this.cannotFormReason = 2;
		PortalStorage.removeFromPortalList(this);
	}
	
	public boolean getIsValid(){
		return (this.isLinked && this.isValid);
	}
	
	public void activatePortal(){
		if(this.isLinked){
			//Get both tiles, tell them to activate
			TilePortalCore ownCore = (TilePortalCore)
					worldObj.getBlockTileEntity(portalLocation.getX(), 
							portalLocation.getY(), portalLocation.getZ());
			
			Location linkedLocation = linkedPortal.getLocation();
			TilePortalCore linkedCore = (TilePortalCore)
					worldObj.getBlockTileEntity(linkedLocation.getX(), 
							linkedLocation.getY(), linkedLocation.getZ());
			
			ownCore.activatePortal();
			linkedCore.activatePortal();
			isActive = true;
		}
	}
	
	public void deactivatePortal(){
		if(this.isLinked){
			//Get both tiles, tell them to activate
			TilePortalCore ownCore = (TilePortalCore)
					worldObj.getBlockTileEntity(portalLocation.getX(), 
							portalLocation.getY(), portalLocation.getZ());
			
			Location linkedLocation = linkedPortal.getLocation();
			TilePortalCore linkedCore = (TilePortalCore)
					worldObj.getBlockTileEntity(linkedLocation.getX(), 
							linkedLocation.getY(), linkedLocation.getZ());
			
			ownCore.deactivatePortal();
			linkedCore.deactivatePortal();
			
			isActive = false;
		}
	}

	public void close() {
		PortalStorage.removeFromPortalList(this);
		//Let this class whim away.. 
		//I'm not going to do that catchy tune here, you already have it
		//in your head by now
	}

	public void teleport(Entity colEntity) {
		//Check everything
		//It is almost impossible for this portal not to be linked,
		//But, please do check
		if(getIsValid()){
			Location linkedLocation = linkedPortal.getLocation();
			//For now, we can only teleport players, so:
			//Okay, we should do another location here
			//Else, the entity will end up in the collision of the portal on the other end.
			//Which will cause it to teleport again..
			//NBTTagCompound playerData = colEntity.getEntityData();
			//Location teleportedTo = new Location(playerData.getIntArray(Constants.NBTTags.entTeleportedTo));
			if(!PortalStorage.isTeleported(colEntity.entityId)){
				colEntity.setPosition(linkedLocation.getX(), 
						linkedLocation.getY()+1, linkedLocation.getZ());
				/*Functions.teleportPlayer(colEntity, linkedLocation.getX(), 
						linkedLocation.getY()+1, linkedLocation.getZ());*/
				//Add to the list:
				PortalStorage.addToTeleportedList(colEntity.entityId);
			}
			//colEntity.setPosition(linkedLocation.getX(), 
			//		linkedLocation.getY(), linkedLocation.getZ());
			//Play some sounds
			//do fancy stuff
			//show particles!
		}
	}
}