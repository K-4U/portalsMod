package k4unl.minecraft.portals.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelPortalSpawner extends ModelBase
{
  //fields
    ModelRenderer arm;
    ModelRenderer bottomArm;
    ModelRenderer bottomLayer;
    ModelRenderer topShape;
  
  public ModelPortalSpawner()
  {
    textureWidth = 128;
    textureHeight = 32;
    
    arm = new ModelRenderer(this, 0, 0);
    arm.addBox(-2F, -2F, 0F, 4, 4, 10);
    arm.setRotationPoint(0F, 16F, -2F);
    arm.setTextureSize(128, 32);
    arm.mirror = true;
    setRotation(arm, 0F, 0F, 0F);
    bottomArm = new ModelRenderer(this, 0, 14);
    bottomArm.addBox(-2F, -2F, -2F, 4, 2, 4);
    bottomArm.setRotationPoint(0F, 14F, 0F);
    bottomArm.setTextureSize(128, 32);
    bottomArm.mirror = true;
    setRotation(bottomArm, 0F, 0F, 0F);
    bottomLayer = new ModelRenderer(this, 28, 0);
    bottomLayer.addBox(-6F, 0F, -6F, 12, 2, 12);
    bottomLayer.setRotationPoint(0F, 10F, 0F);
    bottomLayer.setTextureSize(128, 32);
    bottomLayer.mirror = true;
    setRotation(bottomLayer, 0F, 0F, 0F);
    topShape = new ModelRenderer(this, 0, 14);
    topShape.addBox(-8F, 0F, -8F, 16, 2, 16);
    topShape.setRotationPoint(0F, 8F, 0F);
    topShape.setTextureSize(128, 32);
    topShape.mirror = true;
    setRotation(topShape, 0F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    //setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    
    arm.render(f5);
    bottomArm.render(f5);
    bottomLayer.render(f5);
    topShape.render(f5);
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
  
  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity ent){
		super.setRotationAngles(f, f1, f2, f3, f4, f5, ent);
	}


}
