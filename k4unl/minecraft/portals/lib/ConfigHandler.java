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
		Ids.portalDummyBlock_actual = config.getBlock(Names.portalDummyBlock_unlocalized, Ids.portalDummyBlock_default).getInt();
		
		//Note! For items, do -256 on the value!
	}
}
