package k4unl.minecraft.portals.lib.config;

import k4unl.minecraft.portals.blocks.Blocks;
import k4unl.minecraft.portals.items.Items;
import k4unl.minecraft.portals.lib.CraftingHandler;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.registry.GameRegistry;

public class Recipes {
	public static void init() {
		
		/*GameRegistry.addRecipe(new ItemStack(Blocks.switcherBlock, 1),
				new Object [] {
			"DDD",
			"RDR",
			"DDD",
			'D', Block.dirt,
			'R', Item.redstone
		});*/
		
		
		GameRegistry.registerCraftingHandler(new CraftingHandler());
		GameRegistry.addRecipe(new ItemStack(Items.PortalTunerInstance, 2),
				new Object [] {
					"GGG",
					"GDG",
					"GGG",
					'D', Item.diamond,
					'G', Item.ingotGold
		});
	}
}
