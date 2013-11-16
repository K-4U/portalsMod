package k4unl.minecraft.portals.models;

import java.util.HashMap;
import java.util.Map;

import k4unl.minecraft.portals.lib.config.Ids;
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
    ModelRenderer connectedLeft;
    ModelRenderer connectedRight;
    ModelRenderer connectedBack;
    ModelRenderer connectedFront;
    
	public boolean isCorner = false;
	private Map<ForgeDirection, Integer> connectedSides = new HashMap<ForgeDirection, Integer>();
  
    public ModelPortalFrame(){
		textureWidth = 64;
		textureHeight = 32;
		
	      barLeft = new ModelRenderer(this, 0, 0);
	      barLeft.addBox(-2F, 0F, -2F, 4, 6, 4);
	      barLeft.setRotationPoint(-2F, 16F, 0F);
	      barLeft.setTextureSize(64, 32);
	      barLeft.mirror = true;
	      setRotation(barLeft, 0F, 0F, 1.570796F);
	      barDown = new ModelRenderer(this, 0, 0);
	      barDown.addBox(-2F, 0F, -2F, 4, 6, 4);
	      barDown.setRotationPoint(0F, 18F, 0F);
	      barDown.setTextureSize(64, 32);
	      barDown.mirror = true;
	      setRotation(barDown, 0F, 0F, 0F);
	      barUp = new ModelRenderer(this, 0, 0);
	      barUp.addBox(-2F, 0F, -2F, 4, 6, 4);
	      barUp.setRotationPoint(0F, 8F, 0F);
	      barUp.setTextureSize(64, 32);
	      barUp.mirror = true;
	      setRotation(barUp, 0F, 0F, 0F);
	      barRight = new ModelRenderer(this, 0, 0);
	      barRight.addBox(-2F, 0F, -2F, 4, 6, 4);
	      barRight.setRotationPoint(2F, 16F, 0F);
	      barRight.setTextureSize(64, 32);
	      barRight.mirror = true;
	      setRotation(barRight, 0F, 0F, -1.570796F);
	      inner = new ModelRenderer(this, 16, 0);
	      inner.addBox(-2F, -2F, -2F, 4, 4, 4);
	      inner.setRotationPoint(0F, 16F, 0F);
	      inner.setTextureSize(64, 32);
	      inner.mirror = true;
	      setRotation(inner, 0F, 0F, 0F);
	      barBack = new ModelRenderer(this, 0, 0);
	      barBack.addBox(-2F, 0F, -2F, 4, 6, 4);
	      barBack.setRotationPoint(0F, 16F, 2F);
	      barBack.setTextureSize(64, 32);
	      barBack.mirror = true;
	      setRotation(barBack, 1.570796F, 0F, 0F);
	      barFront = new ModelRenderer(this, 0, 0);
	      barFront.addBox(-2F, 0F, -2F, 4, 6, 4);
	      barFront.setRotationPoint(0F, 16F, -2F);
	      barFront.setTextureSize(64, 32);
	      barFront.mirror = true;
	      setRotation(barFront, -1.570796F, 0F, 0F);
	      cornerBlock = new ModelRenderer(this, 0, 10);
	      cornerBlock.addBox(-3F, -3F, -3F, 6, 6, 6);
	      cornerBlock.setRotationPoint(0F, 16F, 0F);
	      cornerBlock.setTextureSize(64, 32);
	      cornerBlock.mirror = true;
	      setRotation(cornerBlock, 0F, 0F, 0F);
	      connectedLeft = new ModelRenderer(this, 32, 0);
	      connectedLeft.addBox(0F, -3F, -3F, 1, 6, 6);
	      connectedLeft.setRotationPoint(-7F, 16F, 0F);
	      connectedLeft.setTextureSize(64, 32);
	      connectedLeft.mirror = true;
	      setRotation(connectedLeft, 0F, 3.141593F, 0F);
	      connectedRight = new ModelRenderer(this, 32, 0);
	      connectedRight.addBox(0F, -3F, -3F, 1, 6, 6);
	      connectedRight.setRotationPoint(7F, 16F, 0F);
	      connectedRight.setTextureSize(64, 32);
	      connectedRight.mirror = true;
	      setRotation(connectedRight, 0F, 0F, 0F);
	      connectedBack = new ModelRenderer(this, 32, 0);
	      connectedBack.addBox(0F, -3F, -3F, 1, 6, 6);
	      connectedBack.setRotationPoint(0F, 16F, 7F);
	      connectedBack.setTextureSize(64, 32);
	      connectedBack.mirror = true;
	      setRotation(connectedBack, 0F, -1.570796F, 0F);
	      connectedFront = new ModelRenderer(this, 32, 0);
	      connectedFront.addBox(0F, -3F, -3F, 1, 6, 6);
	      connectedFront.setRotationPoint(0F, 16F, -7F);
	      connectedFront.setTextureSize(64, 32);
	      connectedFront.mirror = true;
	      setRotation(connectedFront, 0F, 1.570796F, 0F);
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
	    
	    if(isExtra(ForgeDirection.NORTH)) connectedFront.render(f5);
	    if(isExtra(ForgeDirection.SOUTH)) connectedBack.render(f5);
	    
	    if(isExtra(ForgeDirection.EAST)) connectedRight.render(f5);
	    if(isExtra(ForgeDirection.WEST)) connectedLeft.render(f5);

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

	public void setConnectedSides(Map<ForgeDirection, Integer> map) {
		this.connectedSides = map;
	}

}
