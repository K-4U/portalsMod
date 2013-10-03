package k4unl.minecraft.portals.lib;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class Functions {
	public static void teleportPlayer(EntityPlayer player, int targetX, int targetY, int targetZ){
		player.setPositionAndUpdate(targetX, targetY, targetZ);
		//player.playerNetServerHandler.setPlayerLocation(targetX, targetY, targetZ, player.rotationYaw, player.rotationPitch);
	}
}
