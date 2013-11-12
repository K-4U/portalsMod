package k4unl.minecraft.portals.blocks;

import k4unl.minecraft.portals.lib.Functions;
import k4unl.minecraft.portals.lib.config.Ids;
import k4unl.minecraft.portals.lib.config.ModInfo;
import k4unl.minecraft.portals.lib.config.Names;
import net.minecraft.block.Block;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class Blocks {
	public static Block portalCoreBlockInst;
	public static Block portalFrameBlockInst;
	public static Block portalPortalBlockInst;
	public static Block portalIndicatorBlockInst;
	
	public static Block portalSpawnerBlockInst;
	
	public static void init() {
		portalCoreBlockInst = new PortalCoreBlock(Ids.portalCoreBlock_actual);
		portalFrameBlockInst = new PortalFrameBlock(Ids.portalFrameBlock_actual);
		portalPortalBlockInst = new PortalPortalBlock(Ids.portalPortalBlock_actual);
		portalIndicatorBlockInst = new PortalIndicatorBlock(Ids.portalIndicatorBlock_actual);
		portalSpawnerBlockInst = new PortalSpawnerBlock(Ids.portalSpawnerBlock_actual);
		
		registerBlocks();
		addNames();
	}

	public static void registerBlocks(){
		GameRegistry.registerBlock(portalCoreBlockInst, Names.portalCoreBlock_unlocalized);
		GameRegistry.registerBlock(portalPortalBlockInst, Names.portalPortalBlock_unlocalized);
		GameRegistry.registerBlock(portalFrameBlockInst, Names.portalFrameBlock_unlocalized);
		GameRegistry.registerBlock(portalIndicatorBlockInst, PortalIndicatorBlockHandler.class);
		GameRegistry.registerBlock(portalSpawnerBlockInst, Names.portalSpawnerBlock_name);
		
		
	}
	
	public static void addNames() {
		LanguageRegistry.addName(portalCoreBlockInst, Names.portalCoreBlock_name);
		
		LanguageRegistry.addName(portalFrameBlockInst, Names.portalFrameBlock_name);
		
		for (int i = 0; i < 16; ++i) {
			String dyeName = ItemDye.dyeItemNames[Functions.getDyeFromBlock(i)];
			dyeName = dyeName.replace('_', ' ');
			dyeName = Character.toUpperCase(dyeName.charAt(0)) + dyeName.substring(1);
			LanguageRegistry.addName(new ItemStack(portalIndicatorBlockInst, 1, i), 
					 dyeName + " " +Names.portalIndicatorBlock_name);
		}
		
		LanguageRegistry.addName(portalPortalBlockInst, Names.portalPortalBlock_name);
		LanguageRegistry.addName(portalSpawnerBlockInst, Names.portalSpawnerBlock_name);
	}

}
