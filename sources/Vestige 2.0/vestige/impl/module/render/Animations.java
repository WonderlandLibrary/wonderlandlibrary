package vestige.impl.module.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import vestige.api.module.Category;
import vestige.api.module.Module;
import vestige.api.module.ModuleInfo;
import vestige.api.setting.impl.ModeSetting;
import vestige.api.setting.impl.NumberSetting;

@ModuleInfo(name = "Animations", category = Category.RENDER)
public class Animations extends Module {
	
	public final ModeSetting mode = new ModeSetting("Mode", this, "1.7", "1.7", "Old 1.7", "Exhibition", "Tap", "Scale", "Monsoon", "Fan", "None");
	public final NumberSetting swingDuration = new NumberSetting("Swing duration", this, 1, 0.2, 5, 0.1, false);

	public Animations() {
		this.registerSettings(mode, swingDuration);
	}
	
	public void animate(ItemRenderer itemRenderer, float partialTicks) {
		switch(mode.getMode()) {
			case "1.7":
				NormalAnimations(itemRenderer, partialTicks);
				break;
			case "Old 1.7":
				OldNormalAnimations(itemRenderer, partialTicks);
				break;
			case "Exhibition":
				Exhibition(itemRenderer, partialTicks);
				break;
			case "Tap":
				Tap(itemRenderer, partialTicks);
				break;
			case "Scale":
				Scale(itemRenderer, partialTicks);
				break;
			case "Monsoon":
				Monsoon(itemRenderer, partialTicks);
				break;
			case "Fan":
				Fan(itemRenderer, partialTicks);
				break;
			case "None":
				None(itemRenderer, partialTicks);
				break;
		}
	}
	
	private void NormalAnimations(ItemRenderer itemRenderer, float partialTicks) {
		AbstractClientPlayer abstractclientplayer = this.mc.thePlayer;
		float f = 1.0F - (itemRenderer.prevEquippedProgress + (itemRenderer.equippedProgress - itemRenderer.prevEquippedProgress) * partialTicks);
		float f1 = abstractclientplayer.getSwingProgress(partialTicks);
		
		if(mc.thePlayer.isSwingInProgress) {
			itemRenderer.transformFirstPersonItem(f, f1);
			itemRenderer.func_178103_d();
			GlStateManager.translate(-0.15F, 0.2F, 0.2F);
        } else {
        	itemRenderer.transformFirstPersonItem(f, f1);
        	itemRenderer.func_178103_d();
        	GlStateManager.translate(-0.15F, 0.2F, 0.2F);
        }
	}
	
	private void OldNormalAnimations(ItemRenderer itemRenderer, float partialTicks) {
		AbstractClientPlayer abstractclientplayer = this.mc.thePlayer;
		float f = 1.0F - (itemRenderer.prevEquippedProgress + (itemRenderer.equippedProgress - itemRenderer.prevEquippedProgress) * partialTicks);
		float f1 = abstractclientplayer.getSwingProgress(partialTicks);
		
		if(mc.thePlayer.isSwingInProgress) {
			itemRenderer.transformFirstPersonItem(f, f1);
			itemRenderer.func_178103_d();
            GlStateManager.translate(-0.3F, 0.2F, 0.2F);
        } else {
        	itemRenderer.transformFirstPersonItem(f, f1);
        	itemRenderer.func_178103_d();
            GlStateManager.translate(-0.15F, 0.2F, 0.2F);
        }
	}
	
	private void Exhibition(ItemRenderer itemRenderer, float partialTicks) {
		AbstractClientPlayer abstractclientplayer = this.mc.thePlayer;
		float f = 1.0F - (itemRenderer.prevEquippedProgress + (itemRenderer.equippedProgress - itemRenderer.prevEquippedProgress) * partialTicks);
		float f1 = abstractclientplayer.getSwingProgress(partialTicks);
		float f8 = (float) Math.sin(( (Math.sqrt(f1) * Math.PI)));
		
		itemRenderer.transformFirstPersonItem(f, 0.0F);
        GlStateManager.translate(0.1F, 0.135F, -0.15F);
        GL11.glRotated(-f8 * 26.0F, f8 / 2, 0.0F, 9.0F);
        GL11.glRotated(-f8 * 29.0F, 0.8F, f8 / 2, 0F);
        itemRenderer.func_178103_d();
	}
	
	private void Tap(ItemRenderer itemRenderer, float partialTicks) {
		AbstractClientPlayer abstractclientplayer = this.mc.thePlayer;
		float f = 1.0F - (itemRenderer.prevEquippedProgress + (itemRenderer.equippedProgress - itemRenderer.prevEquippedProgress) * partialTicks);
		float f1 = abstractclientplayer.getSwingProgress(partialTicks);
		float f8 = (float) Math.sin(( (Math.sqrt(f1) * Math.PI)));
		
		itemRenderer.transformFirstPersonItem(f, 0.0F);
        GlStateManager.translate(0.1F, 0.135F, -0.15F);
        GL11.glRotated(-f8 * 25.0F, f8 / 2, 0.0F, 9.0F);
        GL11.glRotated(-f8 * 55.0F, 0.8F, f8 / 2, 0F);
        itemRenderer.func_178103_d();
	}
	
	private void Scale(ItemRenderer itemRenderer, float partialTicks) {
		AbstractClientPlayer abstractclientplayer = this.mc.thePlayer;
		float f = 1.0F - (itemRenderer.prevEquippedProgress + (itemRenderer.equippedProgress - itemRenderer.prevEquippedProgress) * partialTicks);
		float f1 = abstractclientplayer.getSwingProgress(partialTicks);
		float f8 = (float) Math.sin(( (Math.sqrt(f1) * Math.PI)));
		
		itemRenderer.transformFirstPersonItem(f, 0.0F);
		GlStateManager.translate(0.2F, 0.25F, -0.15F);
        GL11.glRotated(-f8 * -40.0F, 0, 0F, 0F);
        itemRenderer.func_178103_d();
	}
	
	private void Monsoon(ItemRenderer itemRenderer, float partialTicks) {
		AbstractClientPlayer abstractclientplayer = this.mc.thePlayer;
		float f = 1.0F - (itemRenderer.prevEquippedProgress + (itemRenderer.equippedProgress - itemRenderer.prevEquippedProgress) * partialTicks);
		float f1 = abstractclientplayer.getSwingProgress(partialTicks);
		float f8 = (float) Math.sin(( (Math.sqrt(f1) * Math.PI)));
		
		f8 = (float) Math.sin(((Math.sqrt(f1) * Math.PI)));
		itemRenderer.transformFirstPersonItem(f, 0.0F);
        GlStateManager.translate(0.1F, 0.535F, -0.15F);
        GlStateManager.scale(0.8f,0.8f,0.8f);
        GL11.glRotated(-f8 * 70.0F, f8 / 2, 0.0F, 0F);
        GL11.glRotated(0, 0F,f8 / 2, 0F);
        itemRenderer.func_178103_d();
	}
	
	private void Fan(ItemRenderer itemRenderer, float partialTicks) {
		float f = 1.0F - (itemRenderer.prevEquippedProgress + (itemRenderer.equippedProgress - itemRenderer.prevEquippedProgress) * partialTicks);
		itemRenderer.transformFirstPersonItem(f, 0.0F);
		
    	float angle = (int) ((System.currentTimeMillis() / 1.5) % 360);
        angle =  (angle > 180 ? 360 - angle : angle) * 2;
        angle /= 180f;
        
        float angle2 = (int) ((System.currentTimeMillis() / 3.5) % 120);
        angle2 =  (angle2 > 30 ? 120 - angle2 : angle2) * 2;
        angle2 /= 1f;
        
        
        float angle3 = (int) ((System.currentTimeMillis() / 3.5) % 110);
        angle3 =  (angle3 > 30 ? 110 - angle3 : angle3) * 2;
        angle3 /= 1f;
        
    	int random1 = 0;
    	int random2 = 0;
    	int random3 = 0;
    	int random4 = 0;
    	float random5 = 0;
    	int random6 = 0;
    	int random7 = 0;
    	float random8 = 0;
    	int random10 = 0;
    	
    	
    	
    		random4 = (int) 0.2f;
    		random1 = (int) (System.currentTimeMillis() / 2 % 360);
    		random2 = 1;
    		random5 = 1;
    		random4 = -59;
    		random6 = -1;
    		random7 = 0;
    		random10 = 3;
    	
    	//BLOCK ANIMATION STUFF
        GlStateManager.translate(random8, 0.2f, -random5);
        GlStateManager.rotate(random4, random6, random7, random10);
        GlStateManager.rotate(-random1, random2, random3, 0.0F);
        GlStateManager.rotate(60.0F, 0.0F, 1.0F, 0.0F);
    }
	
	private void None(ItemRenderer itemRenderer, float partialTicks) {
		float f = 1.0F - (itemRenderer.prevEquippedProgress + (itemRenderer.equippedProgress - itemRenderer.prevEquippedProgress) * partialTicks);
		
		itemRenderer.transformFirstPersonItem(f, 0.0F);
        GlStateManager.translate(0.1F, 0.135F, -0.15F);
        itemRenderer.func_178103_d();
	}
	
}