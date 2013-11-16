package k4unl.minecraft.portals.models;

import java.awt.Color;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

import org.lwjgl.opengl.GL11;

public class ModelPortalIndicator extends ModelBase
{
  //fields
    ModelRenderer indicator;
    ModelRenderer connectorLeft;
    ModelRenderer connectorRight;
    ModelRenderer frameRight;
    ModelRenderer frameLeft;
    ModelRenderer frameTop;
    ModelRenderer frameBottom;
    
    private float indicatorRed = 255;
    private float indicatorGreen = 255;
    private float indicatorBlue = 255;
  
  public ModelPortalIndicator()
  {
    textureWidth = 128;
    textureHeight = 32;
    
      indicator = new ModelRenderer(this, 0, 16);
      indicator.addBox(0F, -2F, -3.5F, 10, 4, 7);
      indicator.setRotationPoint(-5F, 16F, 0F);
      indicator.setTextureSize(128, 32);
      indicator.mirror = true;
      setRotation(indicator, 0F, 0F, 0F);
      connectorLeft = new ModelRenderer(this, 0, 0);
      connectorLeft.addBox(0F, -3F, -3F, 1, 6, 6);
      connectorLeft.setRotationPoint(-8F, 16F, 0F);
      connectorLeft.setTextureSize(128, 32);
      connectorLeft.mirror = true;
      setRotation(connectorLeft, 0F, 0F, 0F);
      connectorRight = new ModelRenderer(this, 0, 0);
      connectorRight.addBox(0F, -3F, -3F, 1, 6, 6);
      connectorRight.setRotationPoint(8F, 16F, 0F);
      connectorRight.setTextureSize(128, 32);
      connectorRight.mirror = true;
      setRotation(connectorRight, 0F, 3.141593F, 0F);
      frameRight = new ModelRenderer(this, 14, 0);
      frameRight.addBox(0F, -4F, -4F, 2, 8, 8);
      frameRight.setRotationPoint(7F, 16F, 0F);
      frameRight.setTextureSize(128, 32);
      frameRight.mirror = true;
      setRotation(frameRight, 0F, 3.141593F, 0F);
      frameLeft = new ModelRenderer(this, 14, 0);
      frameLeft.addBox(0F, -4F, -4F, 2, 8, 8);
      frameLeft.setRotationPoint(-7F, 16F, 0F);
      frameLeft.setTextureSize(128, 32);
      frameLeft.mirror = true;
      setRotation(frameLeft, 0F, 0F, 0F);
      frameTop = new ModelRenderer(this, 34, 0);
      frameTop.addBox(-5F, -1F, 0F, 10, 2, 8);
      frameTop.setRotationPoint(0F, 13F, -4F);
      frameTop.setTextureSize(128, 32);
      frameTop.mirror = true;
      setRotation(frameTop, 0F, 0F, 0F);
      frameBottom = new ModelRenderer(this, 34, 10);
      frameBottom.addBox(-5F, -1F, -4F, 10, 2, 8);
      frameBottom.setRotationPoint(0F, 19F, 0F);
      frameBottom.setTextureSize(128, 32);
      frameBottom.mirror = true;
      setRotation(frameBottom, 0F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    //GL11.glPushAttrib(GL11.GL_CURRENT_BIT);
    
    
    //GL11.glPopAttrib();
    connectorLeft.render(f5);
    connectorRight.render(f5);
    frameRight.render(f5);
    frameLeft.render(f5);
    frameTop.render(f5);
    frameBottom.render(f5);
    
    GL11.glColor3f(1/(255/this.indicatorRed), 1/(255/this.indicatorGreen), 1/(255/this.indicatorBlue));
    indicator.render(f5);
  }
  
	private void setRotation(ModelRenderer model, float x, float y, float z){
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
  
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity ent){
		super.setRotationAngles(f, f1, f2, f3, f4, f5, ent);
	}

	public void setColor(Color clr) {
		this.indicatorRed = clr.getRed();
		this.indicatorGreen = clr.getGreen();
		this.indicatorBlue = clr.getBlue();
	}


}
