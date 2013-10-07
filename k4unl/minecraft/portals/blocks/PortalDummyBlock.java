package k4unl.minecraft.portals.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import k4unl.minecraft.portals.lib.config.Ids;
import k4unl.minecraft.portals.lib.config.ModInfo;
import k4unl.minecraft.portals.lib.config.Names;
import k4unl.minecraft.portals.lib.Functions;
import k4unl.minecraft.portals.tiles.TilePortalCore;
import k4unl.minecraft.portals.tiles.TilePortalDummy;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;


public class PortalDummyBlock extends BlockContainer {
	public static Icon blockIcon;
	
	public PortalDummyBlock(int id) {
		super(id, Material.iron);
		
		setUnlocalizedName(Names.portalDummyBlock_unlocalized);
		setStepSound(Block.soundStoneFootstep);
		setHardness(3.5f);
		setCreativeTab(CreativeTabs.tabDecorations); //For now
		
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TilePortalDummy();
	}

	@Override
	public void registerIcons(IconRegister icon) {
		blockIcon = icon.registerIcon(ModInfo.ID.toLowerCase() + ":" + Names.portalDummyBlock_unlocalized);
	}
	
	@Override
	public Icon getIcon(int side, int metadata) {
		return blockIcon;
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6)
	{
		TilePortalDummy dummy = (TilePortalDummy)world.getBlockTileEntity(x, y, z);
		
		if(dummy != null && dummy.getCore() != null)
			dummy.getCore().invalidateMultiblock();
		
		super.breakBlock(world, x, y, z, par5, par6);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
	{
		/*if(player.isSneaking())
			return false;
		
		TilePortalDummy dummy = (TilePortalDummy)world.getBlockTileEntity(x, y, z);
		
		if(dummy != null && dummy.getCore() != null)
		{
			TilePortalCore core = dummy.getCore();
			//return core.getBlockType().onBlockActivated(world, core.xCoord, core.yCoord, core.zCoord, player, par6, par7, par8, par9);
		}*/
		
		return false;
	}
	
	@SideOnly(Side.CLIENT)
    public int idPicked(World par1World, int par2, int par3, int par4){
        return Ids.portalCoreBlock_actual;
    }
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y,
				int z, int blockId) {
		super.onNeighborBlockChange(world, x, y, z, blockId);
		
		TilePortalDummy tile = (TilePortalDummy) world.getBlockTileEntity(x, y, z);
		tile.checkRedstonePower();
	}
}
