package k4unl.minecraft.portals.vars;

import java.util.ArrayList;

import k4unl.minecraft.portals.vars.Types.Location;
import k4unl.minecraft.portals.vars.Types.portalColor;

public class PortalStorage {
	private static ArrayList<Portal> portalList;
	private static ArrayList<teleportedEntity> teleportedPlayers;
	
	public static class teleportedEntity{
		public int entityId;
		public int teleportedAgo;
		public teleportedEntity(int entityId){
			this.entityId = entityId;
			this.teleportedAgo = 0;
		}
	}
	public static void init(){
		portalList = new ArrayList<Portal>();
		teleportedPlayers = new ArrayList<teleportedEntity>();
	}
	
	public static void addToTeleportedList(int entityId){
		//Check if the player is already in there:
		if(getTeleportedCountById(entityId) == 0){
			teleportedPlayers.add(new teleportedEntity(entityId));
		}
	}
	
	private static int getTeleportedCountById(int idToCheck){
		int count = 0;
		for(teleportedEntity telEnt: teleportedPlayers){
			if(telEnt.entityId == idToCheck){
				count++;
			}
		}
		return count;
	}
	

	public static boolean isTeleported(int entityId) {
		return (getTeleportedCountById(entityId) > 0);
	}
	
	public static void removeFromTeleportedList(int idToRemove){
		for(teleportedEntity telEnt: teleportedPlayers){
			if(telEnt.entityId == idToRemove){
				teleportedPlayers.remove(telEnt);
				return;
			}
		}
	}
	public static ArrayList<teleportedEntity> getTeleportedList(){
		return teleportedPlayers;
	}
	
	public static void addToPortalList(Portal pToAdd){
		//Check if a portal already exists on that location
		Portal existing = getPortalByLocation(pToAdd.getLocation());
		if(existing != null){
			//Remove it first, then add again
			removeFromPortalList(existing);
		}
		portalList.add(pToAdd);
	}
	
	public static void removeFromPortalList(Portal pToRemove){
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

}
