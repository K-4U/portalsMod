package k4unl.minecraft.portals.blocks;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import k4unl.minecraft.portals.lib.config.Ids;
import k4unl.minecraft.portals.lib.config.ModInfo;
import k4unl.minecraft.portals.lib.config.Names;
import k4unl.minecraft.portals.lib.Functions;
import k4unl.minecraft.portals.tiles.TilePortalCore;
import k4unl.minecraft.portals.tiles.TilePortalFrame;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;


public class PortalFrameBlock extends BlockContainer {
	public static Icon blockIcon;
	
	public PortalFrameBlock(int id) {
		super(id, Material.iron);
		
		setUnlocalizedName(Names.portalFrameBlock_unlocalized);
		setStepSound(Block.soundStoneFootstep);
		setHardness(3.5f);
		setCreativeTab(CreativeTabs.tabDecorations); //For now
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TilePortalFrame();
	}

	@Override
	public void registerIcons(IconRegister icon) {
		blockIcon = icon.registerIcon(ModInfo.ID.toLowerCase() + ":" + Names.portalFrameBlock_unlocalized);
	}
	
	@Override
	public Icon getIcon(int side, int metadata) {
		return blockIcon;
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6)
	{
		TilePortalFrame frame = (TilePortalFrame)world.getBlockTileEntity(x, y, z);
		
		if(frame != null && frame.getCore() != null)
			frame.getCore().invalidateMultiblock();
		
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
		
		TilePortalFrame frame = (TilePortalFrame) world.getBlockTileEntity(x, y, z);
		frame.checkRedstonePower();
	}
	
	@Override
	public void getSubBlocks(int id, CreativeTabs tab, List list) {
		list.add(new ItemStack(this, 1, 0));
		list.add(new ItemStack(this, 1, 1));
	}
	
	@Override
	public int damageDropped(int meta) {
		return meta;
	}
	
	@Override
	public int getRenderType(){
		return -1;
	}
	
	public boolean isOpaqueCube(){
		return false;
	}
	
	public boolean renderAsNormalBlock(){
		return false;
	}
}
