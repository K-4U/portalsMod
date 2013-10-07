package k4unl.minecraft.portals.blocks;

import java.util.logging.Level;

import k4unl.minecraft.portals.lib.LogHelper;
import k4unl.minecraft.portals.lib.config.ModInfo;
import k4unl.minecraft.portals.lib.config.Names;
import k4unl.minecraft.portals.tiles.TilePortalCore;
import k4unl.minecraft.portals.tiles.TilePortalPortal;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PortalPortalBlock extends BlockContainer {
	private Icon blockIcon;
	
	public PortalPortalBlock(int id) {
		super(id, Material.portal);
		
		setUnlocalizedName(Names.portalCoreBlock_unlocalized);
		setLightValue(0.6F);
		setHardness(99999F);
	}

	@Override
	public void registerIcons(IconRegister icon) {
		blockIcon = icon.registerIcon(ModInfo.ID.toLowerCase() + ":" + Names.portalPortalBlock_unlocalized);
	}
	
	
	@Override
	public Icon getIcon(int side, int metadata) {
		return blockIcon;
	}
	
	/**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4){
        return null;
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
        float f;
        float f1;

        if (par1IBlockAccess.getBlockId(par2 - 1, par3, par4) != this.blockID && par1IBlockAccess.getBlockId(par2 + 1, par3, par4) != this.blockID)
        {
            f = 0.02F;
            f1 = 0.5F;
            this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f1, 0.5F + f, 1.0F, 0.5F + f1);
        }
        else
        {
            f = 0.5F;
            f1 = 0.02F;
            this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f1, 0.5F + f, 1.0F, 0.5F + f1);
        }
    }

    /**
     * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
     */
    public int getRenderBlockPass(){
        return 1;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube(){
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock(){
        return false;
    }
    
    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity colEntity){
    	if(colEntity instanceof EntityPlayerMP || colEntity instanceof EntityPlayerSP){
    		//For now, just players plox
    		TilePortalPortal dummy = (TilePortalPortal)world.getBlockTileEntity(x, y, z);
    		if(dummy != null && dummy.getCore() != null)
    			dummy.getCore().getMasterClass().teleport((EntityPlayer)colEntity); //Yay for infinite loops! :D
    	}
    	
    	super.onEntityCollidedWithBlock(world, x, y, z, colEntity);
		
    }
    
	@Override
	public void breakBlock(World world, int x, int y, int z, int oldBlockId, int par6){
		TilePortalPortal dummy = (TilePortalPortal)world.getBlockTileEntity(x, y, z);
		if(dummy != null && dummy.getCore() != null){
			if(dummy.getCore().getIsActive()){
				dummy.getCore().getMasterClass().deactivatePortal();
			}
		}
			
		
		super.breakBlock(world, x, y, z, oldBlockId, par6);
	}
    
    @SideOnly(Side.CLIENT)
    public int idPicked(World par1World, int par2, int par3, int par4){
        return 0;
    }


	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TilePortalPortal();
	}

}
