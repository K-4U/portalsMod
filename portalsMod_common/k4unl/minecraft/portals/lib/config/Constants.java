package k4unl.minecraft.portals.lib.config;

public class Constants {
	public static final int portalWidth = 5;
	public static final int portalHeight = 5;
	public static final int portalTimeout = 25; // 50 ticks
	
	
	public static final class NBTTags{
		public static final String entTeleportedAgo = ModInfo.ID + ":teleportedAgo";
		public static final String entTeleportedTo = ModInfo.ID + ":teleportedTo";
	}
	 
}
