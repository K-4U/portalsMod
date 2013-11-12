package k4unl.minecraft.portals.blocks;

import java.util.Map;

import k4unl.minecraft.portals.lib.config.Ids;
import k4unl.minecraft.portals.lib.config.ModInfo;
import k4unl.minecraft.portals.lib.config.Names;
import k4unl.minecraft.portals.tiles.TilePortalFrame;
import k4unl.minecraft.portals.tiles.TilePortalSpawner;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class PortalSpawnerBlock extends BlockContainer {
	public static Icon blockIcon;
	private Map<ForgeDirection, Integer> connectedSides;
	
	public PortalSpawnerBlock(int id) {
		super(id, Material.iron);
		
		setUnlocalizedName(Names.portalSpawnerBlock_unlocalized);
		setStepSound(Block.soundStoneFootstep);
		setHardness(3.5f);
		setCreativeTab(CreativeTabs.tabDecorations); //For now
		
		
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TilePortalSpawner();
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
	public void breakBlock(World world, int x, int y, int z, int par5, int par6){
		TilePortalSpawner frame = (TilePortalSpawner)world.getBlockTileEntity(x, y, z);
		
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
	
	private boolean isDir(ForgeDirection dir){
    	return this.connectedSides.containsKey(dir);
    }
    
    private boolean isExtra(ForgeDirection dir){
    	if(this.connectedSides.containsKey(dir)){
	    	return (this.connectedSides.get(dir) == Ids.portalCoreBlock_actual 
	    			|| this.connectedSides.get(dir) == Ids.portalIndicatorBlock_actual);
    	}else{
    		return false;
    	}
    }
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y,
				int z, int blockId) {
		super.onNeighborBlockChange(world, x, y, z, blockId);
		
		/*TilePortalFrame frame = (TilePortalFrame) world.getBlockTileEntity(x, y, z);
		if(frame != null){
			/*
			this.connectedSides = frame.getConnectedSides();
			float minX = 0.37F;
			float minY = 0.37F;
			float minZ = 0.37F;
			float maxX = 0.63F;
			float maxY = 0.63F;
			float maxZ = 0.63F;
			
			//xPos+ = east
			//xPos- = West
			//ZPos+ = North
			//ZPos- = South
			
			this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);*/			
		//}
        
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
