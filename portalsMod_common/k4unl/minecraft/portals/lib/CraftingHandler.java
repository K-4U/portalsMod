package k4unl.minecraft.portals.lib;

import java.util.Random;

import k4unl.minecraft.portals.items.PortalTunerItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.ICraftingHandler;

public class CraftingHandler implements ICraftingHandler {
	
	@Override
	public void onCrafting(EntityPlayer player, ItemStack itemStack,
			IInventory craftMatrix) {
		
		if(itemStack.getItem() instanceof PortalTunerItem){
			NBTTagCompound tagCompound = new NBTTagCompound();
			tagCompound.setBoolean("isLinking", false);
			tagCompound.setInteger("linkingId", (new Random()).nextInt(1337));
			
			itemStack.setTagCompound(tagCompound);
			
		}
		
	}

	@Override
	public void onSmelting(EntityPlayer player, ItemStack item) {
		// TODO Auto-generated method stub
		
	}
	
}
