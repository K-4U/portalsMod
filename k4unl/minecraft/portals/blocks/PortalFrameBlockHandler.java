package k4unl.minecraft.portals.blocks;

import k4unl.minecraft.portals.lib.config.Names;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;


public class PortalFrameBlockHandler extends ItemBlock {

	public PortalFrameBlockHandler(int id) {
		super(id);
		setHasSubtypes(true);
	}

	
	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		if(itemStack.getItemDamage() == 0){
			return Names.portalFrameBlock_unlocalized;
		}else if(itemStack.getItemDamage() == 1){
			return Names.portalFrameCornerBlock_unlocalized;
		}else{
			return "ERROR: " + itemStack.getItemDamage();
		}
	}
	
	@Override
	public int getMetadata(int damage) {
		return damage;
	}
	
}
