package k4unl.minecraft.portals.items;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import k4unl.minecraft.portals.lib.config.Ids;
import k4unl.minecraft.portals.lib.config.Names;
import net.minecraft.item.Item;

public class Items {

	public static Item PortalTunerInstance;
	
	public static void init() {
		PortalTunerInstance = new PortalTunerItem(Ids.portalTunerItem_actual);
		
		registerItems();
		addNames();
	}
	
	public static void registerItems(){
		GameRegistry.registerItem(PortalTunerInstance, Names.portalTunerItem_unlocalized);
		
	}
	
	public static void addNames(){
		LanguageRegistry.addName(PortalTunerInstance, Names.portalTunerItem_name);
	}
	
	
	
}
