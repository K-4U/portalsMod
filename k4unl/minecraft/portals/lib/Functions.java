package k4unl.minecraft.portals.lib;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.PacketDispatcher;

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
	
	public static Color dye2Rgb(int dmgValue){
		String hex = "";
		List<String> lookup = new ArrayList<String>();
		lookup.add("#191919");
		lookup.add("#993333");
		lookup.add("#667F33");
		lookup.add("#664C33");
		lookup.add("#334CB2");
		lookup.add("#7F3FB2");
		lookup.add("#4C7F99");
		lookup.add("#999999");
		lookup.add("#4C4C4C");
		lookup.add("#F27FA5");
		lookup.add("#7FCC19");
		lookup.add("#E5E533");
		lookup.add("#6699D8");
		lookup.add("#B24CD8");
		lookup.add("#D87F33");
		lookup.add("#FFFFFF");
		
		return hex2Rgb(lookup.get(dmgValue));
	}
	
	public static Color hex2Rgb(String colorStr) {
	    return new Color(
	            Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
	            Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
	            Integer.valueOf( colorStr.substring( 5, 7 ), 16 ) );
	}
	
	public static void showMessageInChat(String message){
		EntityClientPlayerMP pl = Minecraft.getMinecraft().thePlayer;
		pl.addChatMessage(message);
		
	}
	
}
