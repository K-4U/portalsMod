package k4unl.minecraft.portals.items;

import java.util.List;
import k4unl.minecraft.portals.lib.config.ModInfo;
import k4unl.minecraft.portals.lib.config.Names;
import k4unl.minecraft.portals.tiles.TilePortalCore;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PortalTunerItem extends Item {
	private Icon itemIcon;
	
	public PortalTunerItem(int id) {
		super(id);
		
		setMaxStackSize(2); //We only want this on one.
		setCreativeTab(CreativeTabs.tabMisc);
		setUnlocalizedName(Names.portalTunerItem_unlocalized);
		setTextureName(ModInfo.ID.toLowerCase() + ":" + Names.portalTunerItem_unlocalized);
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister icon) {
		itemIcon = icon.registerIcon(ModInfo.ID.toLowerCase() + ":" + Names.portalTunerItem_unlocalized);
	}
	
	@Override
	public Icon getIcon(ItemStack stack, int pass){
    	return itemIcon;
    }
	
	
	@Override
	public Icon getIconFromDamage(int dmg){
		return itemIcon;
	}
	
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4){
		NBTTagCompound tagCompound = itemStack.getTagCompound();
		if(tagCompound == null){
			tagCompound = new NBTTagCompound();
		}
		list.add("Linking ID of " + tagCompound.getInteger("linkingId"));
	}
	
	//Determines if the item has that fancy "enchanted" effect.
	@Override
	public boolean hasEffect(ItemStack itemStack){
		NBTTagCompound tagCompound = itemStack.getTagCompound();
		if(tagCompound != null){
			if(tagCompound.getBoolean("isLinking")){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	//Called if the item is being used to click on a block.
	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float par7, float par8, float par9){
		if(!world.isRemote){
			if(player.isSneaking()){
				//Maybe change modes here?
			}else{
				TileEntity target = world.getBlockTileEntity(x, y, z);
				if(target != null){
					if(target instanceof TilePortalCore){
						TilePortalCore core = (TilePortalCore)target;
						
						//Write to NBT
						NBTTagCompound tagCompound = itemStack.getTagCompound();
						if(tagCompound == null){
							tagCompound = new NBTTagCompound();
						}
						if(tagCompound.getBoolean("isLinking")){
							int lx = tagCompound.getInteger("linkingX");
							int ly = tagCompound.getInteger("linkingY");
							int lz = tagCompound.getInteger("linkingZ");
							
							TileEntity linkingTarget = world.getBlockTileEntity(lx, ly, lz);
							if(linkingTarget instanceof TilePortalCore){
								TilePortalCore linkingCore = (TilePortalCore) linkingTarget;
								player.addChatMessage("Linked portal at (" + lx + ","+ ly +"," + lz + ")!");
								tagCompound.setBoolean("isLinking", false);
								//Put it in the core
								if(!core.isLinked()){
									ItemStack singleTuner = itemStack.splitStack(1);
									core.setInventorySlotContents(0, singleTuner);
								}
							}
						}else{
							if(!core.isLinked()){
								//Create tag compound
								//Put the portal coordinates in there.
								tagCompound.setBoolean("isLinking", true);
								tagCompound.setInteger("linkingX", x);
								tagCompound.setInteger("linkingY", y);
								tagCompound.setInteger("linkingZ", z);
								
								itemStack.setTagCompound(tagCompound);
								player.addChatMessage("Started linking a portal!");
								core.setLink(0);
								//Put it in the core.
								if(!core.isLinked()){
									ItemStack singleTuner = itemStack.splitStack(1);
									core.setInventorySlotContents(0, singleTuner);
								}
							}
						}
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	//Called if the item is being right clicked in the air.
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		if(!world.isRemote){
			if(player.isSneaking()){
				//Maybe change modes here?
				NBTTagCompound tagCompound = itemStack.getTagCompound();
				if(tagCompound == null){
					tagCompound = new NBTTagCompound();
				}
				player.addChatMessage("Linking of ID" + tagCompound.getInteger("linkingId"));
			}
		}
		return itemStack;
	}

}
