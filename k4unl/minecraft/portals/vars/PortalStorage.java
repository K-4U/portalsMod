package k4unl.minecraft.portals.vars;

import java.lang.reflect.Array;
import java.util.ArrayList;

import k4unl.minecraft.portals.tiles.TilePortalCore;
import k4unl.minecraft.portals.vars.Types.Location;
import k4unl.minecraft.portals.vars.Types.portalColor;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class PortalStorage {
	private static ArrayList<Portal> portalList;
	
	public static void init(){
		portalList = new ArrayList<Portal>();
	}
	
	public static void addToList(Portal pToAdd){
		//Check if a portal already exists on that location
		Portal existing = getPortalByLocation(pToAdd.getLocation());
		if(existing != null){
			//Remove it first, then add again
			removeFromList(existing);
		}
		portalList.add(pToAdd);
	}
	
	public static void removeFromList(Portal pToRemove){
		portalList.remove(pToRemove);
	}

	/*
	 * Returns first portal with that color
	 * 
	 */
	public static Portal getPortalByColor(Portal ignore, portalColor theColor){
		for(Portal mPortal: portalList){
			if(!mPortal.equals(ignore)){
				if(mPortal.getColors().equals(theColor)){
					return mPortal;
				}
			}
		}
		return null;
	}
	
	public static ArrayList<Portal> getPortalByColor(portalColor theColor){
		ArrayList<Portal> coloredPortals = new ArrayList<Portal>();
		for(Portal mPortal: portalList){
			if(mPortal.getColors().equals(theColor)){
				coloredPortals.add(mPortal);
			}
		}
		return coloredPortals;
	}
	
	public static int getPortalCountByColor(portalColor theColor){
		int count = 0;
		for(Portal mPortal: portalList){
			if(mPortal.getColors().equals(theColor)){
				count++;
			}
		}
		return count;
	}
	
	public static Portal getPortalByLocation(Location theLocation){
		int count = 0;
		for(Portal mPortal: portalList){
			if(mPortal.getLocation().equals(theLocation)){
				return mPortal;
			}
		}
		return null;
	}
	
	
	
	public static class Portal{
		private Location portalLocation;
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
			PortalStorage.addToList(this);
			
			this.worldObj = worldObj;
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
		
		public void setColors(portalColor newColors){
			portalColors = newColors;
			int sameColors = PortalStorage.getPortalCountByColor(this.portalColors); 
			if(sameColors > 2){
				//Error! Cannot form!
				isValid = false;
				cannotFormReason = 1;
			}else if(sameColors == 2){ // 2, this one counts as well
				//Link the portals together
				Portal linkedPortal = PortalStorage.getPortalByColor(this, 
						this.portalColors);
				linkedPortal.link(this);
				this.link(linkedPortal);
			}
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
			PortalStorage.removeFromList(this);
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
			removeFromList(this);
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
				colEntity.setPosition(linkedLocation.getX(), 
						linkedLocation.getY(), linkedLocation.getZ());
				//Play some sounds
				//do fancy stuff
				//show particles!
			}
		}
	}
}
