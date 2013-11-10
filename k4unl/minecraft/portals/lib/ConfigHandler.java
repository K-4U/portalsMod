package k4unl.minecraft.portals.lib;

import java.io.File;

import k4unl.minecraft.portals.lib.config.Ids;
import k4unl.minecraft.portals.lib.config.Names;
import net.minecraftforge.common.Configuration;

public class ConfigHandler {
	private static Configuration config;
	
	public static void init(File configFile){
		config = new Configuration(configFile);
		config.load();
		
		handleIDs();
		
		if(config.hasChanged()){
			config.save();
		}
	}
	
	
	private static void handleIDs(){
		Ids.portalCoreBlock_actual = config.getBlock(Names.portalCoreBlock_unlocalized, Ids.portalCoreBlock_default).getInt();
		Ids.portalFrameBlock_actual = config.getBlock(Names.portalFrameBlock_unlocalized, Ids.portalFrameBlock_default).getInt();
		Ids.portalPortalBlock_actual = config.getBlock(Names.portalPortalBlock_unlocalized, Ids.portalPortalBlock_default).getInt();
		Ids.portalIndicatorBlock_actual = config.getBlock(Names.portalIndicatorBlock_unlocalized, Ids.portalIndicatorBlock_default).getInt();
		
		//Note! For items, do -256 on the value!
	}
}
