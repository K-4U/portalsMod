package k4unl.minecraft.portals.models;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.ForgeDirection;

public class ModelPortalFrame extends ModelBase{
  //fields
    ModelRenderer barLeft;
    ModelRenderer barDown;
    ModelRenderer barUp;
    ModelRenderer barRight;
    ModelRenderer inner;
    ModelRenderer barBack;
    ModelRenderer barFront;
    ModelRenderer cornerBlock;
	public boolean isCorner = false;
	private List<ForgeDirection> connectedSides = new ArrayList<ForgeDirection>();
  
    public ModelPortalFrame(){
		textureWidth = 64;
		textureHeight = 32;
		
		barLeft = new ModelRenderer(this, 16, 0);
	      barLeft.addBox(0F, -2F, -2F, 6, 4, 4);
	      barLeft.setRotationPoint(-8F, 16F, 0F);
	      barLeft.setTextureSize(64, 32);
	      barLeft.mirror = true;
	      setRotation(barLeft, 0F, 0F, 0F);
	      barDown = new ModelRenderer(this, 0, 0);
	      barDown.addBox(-2F, 0F, -2F, 4, 6, 4);
	      barDown.setRotationPoint(0F, 18F, 0F);
	      barDown.setTextureSize(64, 32);
	      barDown.mirror = true;
	      setRotation(barDown, 0F, 0F, 0F);
	      barUp = new ModelRenderer(this, 0, 10);
	      barUp.addBox(-2F, 0F, -2F, 4, 6, 4);
	      barUp.setRotationPoint(0F, 8F, 0F);
	      barUp.setTextureSize(64, 32);
	      barUp.mirror = true;
	      setRotation(barUp, 0F, 0F, 0F);
	      barRight = new ModelRenderer(this, 16, 8);
	      barRight.addBox(0F, -2F, -2F, 6, 4, 4);
	      barRight.setRotationPoint(2F, 16F, 0F);
	      barRight.setTextureSize(64, 32);
	      barRight.mirror = true;
	      setRotation(barRight, 0F, 0F, 0F);
	      inner = new ModelRenderer(this, 36, 10);
	      inner.addBox(-2F, -2F, -2F, 4, 4, 4);
	      inner.setRotationPoint(0F, 16F, 0F);
	      inner.setTextureSize(64, 32);
	      inner.mirror = true;
	      setRotation(inner, 0F, 0F, 0F);
	      barBack = new ModelRenderer(this, 16, 16);
	      barBack.addBox(-2F, -2F, 0F, 4, 4, 6);
	      barBack.setRotationPoint(0F, 16F, 2F);
	      barBack.setTextureSize(64, 32);
	      barBack.mirror = true;
	      setRotation(barBack, 0F, 0F, 0F);
	      barFront = new ModelRenderer(this, 36, 0);
	      barFront.addBox(-2F, -2F, -6F, 4, 4, 6);
	      barFront.setRotationPoint(0F, 16F, -2F);
	      barFront.setTextureSize(64, 32);
	      barFront.mirror = true;
	      setRotation(barFront, 0F, 0F, 0F);
	      
	      cornerBlock = new ModelRenderer(this, 36, 18);
	      cornerBlock.addBox(-3F, -3F, -3F, 6, 6, 6);
	      cornerBlock.setRotationPoint(0F, 16F, 0F);
	      cornerBlock.setTextureSize(64, 32);
	      cornerBlock.mirror = true;
	      setRotation(cornerBlock, 0F, 0F, 0F);
  }
  
    private boolean isDir(ForgeDirection dir){
    	return this.connectedSides.contains(dir);
    }
    
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		
	    if(isDir(ForgeDirection.NORTH)) barFront.render(f5);
	    if(isDir(ForgeDirection.SOUTH)) barBack.render(f5);
	    
	    if(isDir(ForgeDirection.EAST)) barRight.render(f5);
	    if(isDir(ForgeDirection.WEST)) barLeft.render(f5);
	    
	    if(isDir(ForgeDirection.DOWN)) barDown.render(f5);
	    if(isDir(ForgeDirection.UP)) barUp.render(f5);
	    
	    if((isDir(ForgeDirection.UP) || isDir(ForgeDirection.DOWN)) && 
	    		(isDir(ForgeDirection.EAST) || isDir(ForgeDirection.WEST) || 
	    				isDir(ForgeDirection.NORTH) || isDir(ForgeDirection.SOUTH))){
	    	this.cornerBlock.render(f5);
	    }
	    		
	    inner.render(f5);
	}
  
	private void setRotation(ModelRenderer model, float x, float y, float z){
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
  
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity ent){
		super.setRotationAngles(f, f1, f2, f3, f4, f5, ent);
	}

	public void setConnectedSides(List<ForgeDirection> connectedSides) {
		this.connectedSides = connectedSides;
	}

}
