package k4unl.minecraft.portals.lib;

import javax.swing.text.html.parser.Entity;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class Functions {
	public static void teleportPlayer(EntityPlayer player, int targetX, int targetY, int targetZ){
		player.setPositionAndUpdate(targetX, targetY, targetZ);
		//player.playerNetServerHandler.setPlayerLocation(targetX, targetY, targetZ, player.rotationYaw, player.rotationPitch);
	}
	
	/**
	 * Takes a block damage value and returns the dye damage value to match
	 */
	public static int getDyeFromBlock(int par0) {
		return ~par0 & 15;
	}
}
