package k4unl.minecraft.portals.blocks;

import k4unl.minecraft.portals.lib.config.ModInfo;
import k4unl.minecraft.portals.lib.config.Names;
import k4unl.minecraft.portals.lib.Functions;
import k4unl.minecraft.portals.tiles.TilePortalCore;
import mods.multifurnace.MultiFurnaceMod;
import mods.multifurnace.common.ModConfig;
import mods.multifurnace.tileentity.TileEntityMultiFurnaceCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;


public class PortalCoreBlock extends BlockContainer {
	public static final int META_ISACTIVE = 0x00000008;
	public static final int MASK_DIR = 0x00000007;
	public static final int META_DIR_NORTH = 0x00000001;
	public static final int META_DIR_SOUTH = 0x00000002;
	public static final int META_DIR_EAST = 0x00000003;
	public static final int META_DIR_WEST = 0x00000000;
	
	public static Icon blockIcon;
	
	public PortalCoreBlock(int id) {
		super(id, Material.iron);
		
		setUnlocalizedName(Names.portalCoreBlock_unlocalized);
		setStepSound(Block.soundStoneFootstep);
		setHardness(3.5f);
		setCreativeTab(CreativeTabs.tabDecorations); //For now
		
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		// TODO Auto-generated method stub
		return new TilePortalCore();
	}

	@Override
	public void registerIcons(IconRegister icon) {
		blockIcon = icon.registerIcon(ModInfo.ID.toLowerCase() + ":" + Names.portalCoreBlock_unlocalized);
	}
	
	@Override
	public Icon getIcon(int side, int metadata) {
		return blockIcon;
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9){
		if(player.isSneaking())
			return false;
		
		TilePortalCore tileEntity = (TilePortalCore)world.getBlockTileEntity(x, y, z);
		
		if(tileEntity != null)
		{
			// Determine if the Multiblock is currently known to be valid
			if(!tileEntity.getIsValid())
			{
				if(tileEntity.checkIfProperlyFormed())
				{
					tileEntity.convertDummies();
					if(world.isRemote)
						player.addChatMessage("Multi-Block Furnace Created!");
				}
			}
			
			// Check if the multi-block structure has been formed.
			if(tileEntity.getIsValid()){
			}
				//player.openGui(MultiFurnaceMod.instance, ModConfig.GUIIDs.multiFurnace, world, x, y, z);
		}
		
		
		//TODO: DEBUG!
		//Functions.teleportPlayer(player, 45, 18, 574);
		
		return true;
	}
	
}
