package k4unl.minecraft.portals.renderers;

import k4unl.minecraft.portals.lib.config.ModInfo;
import k4unl.minecraft.portals.models.ModelPortalFrame;
import k4unl.minecraft.portals.tiles.TilePortalFrame;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class RendererPortalFrame extends TileEntitySpecialRenderer {
	private static final ResourceLocation resLoc = new ResourceLocation(ModInfo.ID.toLowerCase(),"textures/model/PortalFrame_tmap.png");
	private ModelPortalFrame portalFrame;
	
	
	public RendererPortalFrame(){
		this.portalFrame = new ModelPortalFrame();
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f) {
		//Open the GL matrix
		GL11.glPushMatrix();
		//The halve floats are added because else the model would be offsetted.
		GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
		
		//Rotate model
		//Todo: Check the 180
		GL11.glRotatef(180, 0F, 0F, 1F);
		
		//Bind texture
		this.bindTexture(resLoc);
		
		
		GL11.glPushMatrix();
		
		TilePortalFrame frame = (TilePortalFrame) tileEntity;
		portalFrame.setConnectedSides(frame.getConnectedSides());
		
		
		//it' s 1/16.
		//There's 16 pixel in 1 block.
		//portalFrame.render(tileEntity, tileEntity.worldObj, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, 1F/16F);
		this.portalFrame.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}

}
