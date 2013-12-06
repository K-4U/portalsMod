package k4unl.minecraft.portals.blocks;

import k4unl.minecraft.portals.lib.Functions;
import k4unl.minecraft.portals.lib.config.Names;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;


public class PortalIndicatorBlockHandler extends ItemBlock {
	public PortalIndicatorBlockHandler(int id) {
		super(id);
		setHasSubtypes(true);
	}

	
	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		int metaData = itemStack.getItemDamage();
		String dyeName = ItemDye.dyeItemNames[Functions.getDyeFromBlock(metaData)];
		
		return Names.portalIndicatorBlock_unlocalized + "_" + dyeName;
	}
	
	@Override
	public int getMetadata(int damage) {
		return damage;
	}
}
