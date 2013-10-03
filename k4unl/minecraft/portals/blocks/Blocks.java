package k4unl.minecraft.portals.blocks;

import k4unl.minecraft.portals.lib.config.Ids;
import k4unl.minecraft.portals.lib.config.Names;
import net.minecraft.block.Block;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class Blocks {
	public static Block portalCoreBlockInst;
	public static Block portalDummyBlockInst;
	public static Block portalPortalBlockInst;

	public static void init() {
		portalCoreBlockInst = new PortalCoreBlock(Ids.portalCoreBlock_actual);
		portalDummyBlockInst = new PortalDummyBlock(Ids.portalDummyBlock_actual);
		portalPortalBlockInst = new PortalPortalBlock(Ids.portalPortalBlock_actual);
		
		registerBlocks();
		addNames();
	}

	public static void registerBlocks(){
		GameRegistry.registerBlock(portalCoreBlockInst, Names.portalCoreBlock_unlocalized);
		GameRegistry.registerBlock(portalDummyBlockInst, Names.portalDummyBlock_unlocalized);
		GameRegistry.registerBlock(portalPortalBlockInst, Names.portalPortalBlock_unlocalized);
	}
	
	public static void addNames() {
		LanguageRegistry.addName(portalCoreBlockInst, Names.portalCoreBlock_name);
		LanguageRegistry.addName(portalDummyBlockInst, Names.portalDummyBlock_name);
		LanguageRegistry.addName(portalPortalBlockInst, Names.portalPortalBlock_name);
	}

}
