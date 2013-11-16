package k4unl.minecraft.portals.proxy;
import java.util.logging.Level;

import k4unl.minecraft.portals.lib.LogHelper;
import k4unl.minecraft.portals.lib.config.ModInfo;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler {

        @Override
        public void onPacketData(INetworkManager manager,
                        Packet250CustomPayload packet, Player playerEntity) {
    		LogHelper.log(Level.INFO, "Packet: "+packet.data.toString());
            if(packet.channel.equals(ModInfo.CHANNEL)){
            	
            }
        }

}