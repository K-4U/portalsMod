package k4unl.minecraft.portals.renderers;

import java.util.logging.Level;

import k4unl.minecraft.portals.lib.LogHelper;
import k4unl.minecraft.portals.lib.config.ModInfo;
import k4unl.minecraft.portals.models.ModelPortalIndicator;
import k4unl.minecraft.portals.models.ModelPortalSpawner;
import k4unl.minecraft.portals.tiles.TilePortalIndicator;
import k4unl.minecraft.portals.tiles.TilePortalSpawner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

public class RendererPortalSpawner extends TileEntitySpecialRenderer {
	private static final ResourceLocation resLoc = new ResourceLocation(ModInfo.ID.toLowerCase(),"textures/model/portalSpawner_tmap.png");
	private ModelPortalSpawner portalSpawner;
	
	
	public RendererPortalSpawner(){
		this.portalSpawner = new ModelPortalSpawner();
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
		
		TilePortalSpawner spawner = (TilePortalSpawner) tileEntity;
		
		//GL11.glRotatef(180, spawner.getRotation(), 0F, .5F);
		this.bindTexture(resLoc);
		
		GL11.glPushMatrix();
		
		
		//TilePortalFrame frame = (TilePortalFrame) tileEntity;
		//portalIndicator.setConnectedSides(frame.getConnectedSides());
		
		
		//it' s 1/16.
		//There's 16 pixel in 1 block.
		//portalFrame.render(tileEntity, tileEntity.worldObj, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, 1F/16F);
		this.portalSpawner.render((Entity)null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}

}
