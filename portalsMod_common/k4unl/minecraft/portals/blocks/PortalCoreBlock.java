package k4unl.minecraft.portals.blocks;

import java.util.Random;

import k4unl.minecraft.portals.items.PortalTunerItem;
import k4unl.minecraft.portals.lib.config.ModInfo;
import k4unl.minecraft.portals.lib.config.Names;
import k4unl.minecraft.portals.tiles.TilePortalCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;


public class PortalCoreBlock extends BlockContainer {
	public static final int META_ISACTIVE = 0x00000008;
	public static final int MASK_DIR = 0x00000007;
	public static final int META_DIR_NORTH = 0x00000001;
	public static final int META_DIR_SOUTH = 0x00000002;
	public static final int META_DIR_EAST = 0x00000003;
	public static final int META_DIR_WEST = 0x00000000;
	
	public static Icon capIcon;
	public static Icon capIconEnabled;
	public static Icon capIconActive;
	public static Icon sideIcon;
	public static Icon sideIconEnabled;
	public static Icon sideIconActive;
	
	public PortalCoreBlock(int id) {
		super(id, Material.iron);
		
		setUnlocalizedName(Names.portalCoreBlock_unlocalized);
		setStepSound(Block.soundStoneFootstep);
		setHardness(3.5f);
		setCreativeTab(CreativeTabs.tabDecorations); //For now
		
		setTickRandomly(true);
		
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		//Sent it to the tile, which sends it to the class
		TilePortalCore tile = (TilePortalCore) world.getBlockTileEntity(x, y, z);
		tile.getMasterClass().randomTick(world, x, y, z, random);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TilePortalCore();
	}

	@Override
	public void registerIcons(IconRegister icon) {
		sideIcon = icon.registerIcon(ModInfo.ID.toLowerCase() + ":" + Names.portalCoreBlock_unlocalized + "_sides");
		sideIconEnabled = icon.registerIcon(ModInfo.ID.toLowerCase() + ":" + Names.portalCoreBlock_unlocalized + "_side_enabled");
		sideIconActive = icon.registerIcon(ModInfo.ID.toLowerCase() + ":" + Names.portalCoreBlock_unlocalized + "_side_active");
		capIcon = icon.registerIcon(ModInfo.ID.toLowerCase() + ":" + Names.portalCoreBlock_unlocalized + "_top");
		capIconEnabled = icon.registerIcon(ModInfo.ID.toLowerCase() + ":" + Names.portalCoreBlock_unlocalized + "_top_enabled");
		capIconActive = icon.registerIcon(ModInfo.ID.toLowerCase() + ":" + Names.portalCoreBlock_unlocalized + "_top_active");
	}
	
	@Override
	public Icon getIcon(int side, int metadata) {
		ForgeDirection s = ForgeDirection.getOrientation(side);
		if(s == ForgeDirection.UP || s == ForgeDirection.DOWN){
			return capIcon;
		}
		return sideIcon;
	}
	
	@Override
	public boolean canConnectRedstone(IBlockAccess iba, int i, int j, int k, int dir){
		return true;
    }
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y,
				int z, int blockId) {
		super.onNeighborBlockChange(world, x, y, z, blockId);
		
		TilePortalCore tile = (TilePortalCore) world.getBlockTileEntity(x, y, z);
		tile.checkRedstonePower();
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9){
		if(player.isSneaking())
			return false;
		
		if (player.getCurrentEquippedItem() != null) {
			if(player.getCurrentEquippedItem().getItem() instanceof PortalTunerItem){
				return false;
			}
		}else{
			return false;
		}
		
		TilePortalCore tileEntity = (TilePortalCore)world.getBlockTileEntity(x, y, z);
		
		if(tileEntity != null){
			if(player.isSneaking()){
				if (player.getCurrentEquippedItem() != null) {
					if(player.getCurrentEquippedItem().getItem() instanceof PortalTunerItem){
						
					}else{
						return false;
					}
				}else{
					return false;
				}
			}
			
			
			// Determine if the Multiblock is currently known to be valid
			if(!tileEntity.getIsValid()){
				if(tileEntity.checkIfProperlyFormed()){
					tileEntity.convertDummies();
					player.addChatMessage("Portal created!");
				}
			}
			
			// Check if the multi-block structure has been formed.
			if(tileEntity.getIsValid()){
			}
				//player.openGui(MultiFurnaceMod.instance, ModConfig.GUIIDs.multiFurnace, world, x, y, z);
		}
		
		return true;
	}
	
}
