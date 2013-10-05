package k4unl.minecraft.portals.blocks;

import java.util.List;

import k4unl.minecraft.portals.lib.config.Ids;
import k4unl.minecraft.portals.lib.config.ModInfo;
import k4unl.minecraft.portals.lib.config.Names;
import k4unl.minecraft.portals.tiles.TilePortalCore;
import k4unl.minecraft.portals.tiles.TilePortalDummy;
import k4unl.minecraft.portals.tiles.TilePortalIndicator;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class PortalIndicatorBlock extends BlockContainer{
	private Icon[] iconArray;
	
	public PortalIndicatorBlock(int id) {
		super(id, Material.iron);
		
		setUnlocalizedName(Names.portalDummyBlock_unlocalized);
		setStepSound(Block.soundStoneFootstep);
		setHardness(3.5f);
		setCreativeTab(CreativeTabs.tabDecorations); //For now
		
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TilePortalIndicator();
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6)
	{
		TilePortalIndicator dummy = (TilePortalIndicator)world.getBlockTileEntity(x, y, z);
		
		if(dummy != null && dummy.getCore() != null)
			dummy.getCore().invalidateMultiblock();
		
		super.breakBlock(world, x, y, z, par5, par6);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
	{
		if(player.isSneaking())
			return false;
		
		TilePortalIndicator dummy = (TilePortalIndicator)world.getBlockTileEntity(x, y, z);
		
		if(dummy != null && dummy.getCore() != null)
		{
			TilePortalCore core = dummy.getCore();
			return core.getBlockType().onBlockActivated(world, core.xCoord, core.yCoord, core.zCoord, player, par6, par7, par8, par9);
		}
		
		return true;
	}
	
	@SideOnly(Side.CLIENT)
    public int idPicked(World par1World, int par2, int par3, int par4){
        return Ids.portalCoreBlock_actual;
    }
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y,
				int z, int blockId) {
		super.onNeighborBlockChange(world, x, y, z, blockId);
		
		TilePortalIndicator tile = (TilePortalIndicator) world.getBlockTileEntity(x, y, z);
		if(tile!= null){
			tile.checkRedstonePower();
		}
	}
	
	//Anddd, a lot of stuff copied from ColoredBlock
	@SideOnly(Side.CLIENT)
	/**
	 * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
	 */
	public Icon getIcon(int par1, int par2) {
		return this.iconArray[par2 % this.iconArray.length];
	}

	/**
	 * Determines the damage on the item the block drops. Used in cloth and
	 * wood.
	 */
	public int damageDropped(int par1) {
		return par1;
	}

	/**
	 * Takes a dye damage value and returns the block damage value to match
	 */
	public static int getBlockFromDye(int par0) {
		return ~par0 & 15;
	}

	/**
	 * Takes a block damage value and returns the dye damage value to match
	 */
	public static int getDyeFromBlock(int par0) {
		return ~par0 & 15;
	}

	@SideOnly(Side.CLIENT)
	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
	 */
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs,
			List par3List) {
		for (int j = 0; j < 16; ++j) {
			par3List.add(new ItemStack(par1, 1, j));
		}
	}

	@SideOnly(Side.CLIENT)
	/**
	 * When this method is called, your block should register all the icons it needs with the given IconRegister. This
	 * is the only chance you get to register icons.
	 */
	public void registerIcons(IconRegister par1IconRegister) {
		this.iconArray = new Icon[16];

		for (int i = 0; i < this.iconArray.length; ++i) {
			this.iconArray[i] = par1IconRegister.registerIcon(
					ModInfo.ID.toLowerCase() + ":" 
					+ Names.portalIndicatorBlock_unlocalized
					+ "_"
					+ ItemDye.dyeItemNames[getDyeFromBlock(i)]);
		}
	}
	
}
