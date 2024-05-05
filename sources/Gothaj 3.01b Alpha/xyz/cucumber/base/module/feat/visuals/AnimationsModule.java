package xyz.cucumber.base.module.feat.visuals;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.util.MathHelper;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.ext.EventRenderItem;
import xyz.cucumber.base.events.ext.EventTick;
import xyz.cucumber.base.module.ArrayPriority;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.settings.BooleanSettings;
import xyz.cucumber.base.module.settings.ModeSettings;
import xyz.cucumber.base.module.settings.NumberSettings;

@ModuleInfo(category = Category.VISUALS, description = "Changes swing animation", name = "Animations", priority = ArrayPriority.LOW)
public class AnimationsModule extends Mod {
	public ModeSettings mode = new ModeSettings("Mode", new String[] {"Vanilla", "Sigma", "Stab", "Fan", "Sigma 2", "Old", "Exhibition", "Gothaj", "Swong", "Chill", "Basic", "Fast", "Fast 2"});
    
	public BooleanSettings skidSwing = new BooleanSettings("Skid Swing", false);
    
    public BooleanSettings onlySword = new BooleanSettings("Only Sword", true);
    
    public NumberSettings x = new NumberSettings("X", 0.0F, -2.0F, 2.0F, 0.1F);
    public NumberSettings y = new NumberSettings("Y", 0.0F, -2.0F, 2.0F, 0.1F);
    public NumberSettings z = new NumberSettings("Z", 0.0F, -2.0F, 2.0F, 0.1F);

    public AnimationsModule()
    {
        this.addSettings(mode, onlySword, skidSwing, x, y, z);
    }
    
    @EventListener
    public void onTick(EventTick e) {
    	setInfo(mode.getMode());
    }
    
    @EventListener
    public void onRender(EventRenderItem e) {
    	final ItemRenderer itemRenderer = mc.getItemRenderer();
        final float animationProgression = e.getF();
        final float swingProgress = e.getF1();
        final float convertedProgress = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float) Math.PI);
        
        GlStateManager.translate(x.getValue(), y.getValue(), z.getValue());
        

        switch (mode.getMode().toLowerCase())
        {
            case "vanilla": {
                itemRenderer.transformFirstPersonItem(animationProgression, swingProgress);
                itemRenderer.doBlockTransformations();

                break;
            }

            case "sigma": {
                itemRenderer.transformFirstPersonItem(animationProgression, 0.0F);
                final float y = -convertedProgress * 2.0F;
                GlStateManager.translate(0.0F, y / 10.0F + 0.1F, 0.0F);
                GlStateManager.rotate(y * 10.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(250, 0.2F, 1.0F, -0.6F);
                GlStateManager.rotate(-10.0F, 1.0F, 0.5F, 1.0F);
                GlStateManager.rotate(-y * 20.0F, 1.0F, 0.5F, 1.0F);

                break;
            }

            case "stab": {
                final float spin = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float) Math.PI);

                GlStateManager.translate(0.6f, 0.3f, -0.6f + -spin * 0.7);
                GlStateManager.rotate(6090, 0.0f, 0.0f, 0.1f);
                GlStateManager.rotate(6085, 0.0f, 0.1f, 0.0f);
                GlStateManager.rotate(6110, 0.1f, 0.0f, 0.0f);
                itemRenderer.transformFirstPersonItem(0.0F, 0.0f);
                itemRenderer.doBlockTransformations();
                break;
            }

            case "fan": {
                itemRenderer.transformFirstPersonItem(animationProgression, 0.0F);
                GlStateManager.translate(0, 0.2F, -1);
                GlStateManager.rotate(-59, -1, 0, 3);
                // Don't make the /2 a float it causes the animation to break
                GlStateManager.rotate(-(System.currentTimeMillis() / 2 % 360), 1, 0, 0.0F);
                GlStateManager.rotate(60.0F, 0.0F, 1.0F, 0.0F);
                break;
            }

            case "sigma 2": {
                itemRenderer.transformFirstPersonItem(animationProgression, 0.0F);
                GlStateManager.scale(0.8F, 0.8F, 0.8F);
                GlStateManager.translate(0.0f, 0.1F, 0.0F);
                itemRenderer.doBlockTransformations();
                GlStateManager.rotate(convertedProgress * 35.0F / 2.0F, 0.0F, 1.0F, 1.5F);
                GlStateManager.rotate(-convertedProgress * 135.0F / 4.0F, 1.0f, 1.0F, 0.0F);

                break;
            }

            case "old": {
                GlStateManager.translate(0.0F, 0.18F, 0.0F);
                itemRenderer.transformFirstPersonItem(animationProgression / 2.0F, swingProgress);
                itemRenderer.doBlockTransformations();

                break;
            }

            case "exhibition": {
                itemRenderer.transformFirstPersonItem(animationProgression / 2.0F, 0.0F);
                GlStateManager.translate(0.0F, 0.3F, -0.0F);
                GlStateManager.rotate(-convertedProgress * 31.0F, 1.0F, 0.0F, 2.0F);
                GlStateManager.rotate(-convertedProgress * 33.0F, 1.5F, (convertedProgress / 1.1F), 0.0F);
                itemRenderer.doBlockTransformations();

                break;
            }

            case "gothaj": {
                itemRenderer.transformFirstPersonItem(animationProgression / 2.0F, 0.0F);
                GlStateManager.translate(0.0F, 0.3F, -0.0F);
                GlStateManager.rotate(-convertedProgress * 30.0F, 1.0F, 0.0F, 2.0F);
                GlStateManager.rotate(-convertedProgress * 44.0F, 1.5F, (convertedProgress / 1.2F), 0.0F);
                itemRenderer.doBlockTransformations();

                break;
            }

            case "swong": {
                itemRenderer.transformFirstPersonItem(animationProgression / 2.0F, swingProgress);
                GlStateManager.rotate(convertedProgress * 30.0F / 2.0F, -convertedProgress, -0.0F, 9.0F);
                GlStateManager.rotate(convertedProgress * 40.0F, 1.0F, -convertedProgress / 2.0F, -0.0F);
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
                itemRenderer.doBlockTransformations();

                break;
            }

            case "chill": {
                itemRenderer.transformFirstPersonItem(animationProgression / 1.5F, 0.0F);
                itemRenderer.doBlockTransformations();
                GlStateManager.translate(-0.05F, 0.3F, 0.3F);
                GlStateManager.rotate(-convertedProgress * 140.0F, 8.0F, 0.0F, 8.0F);
                GlStateManager.rotate(convertedProgress * 90.0F, 8.0F, 0.0F, 8.0F);

                break;
            }

            case "basic": {
                itemRenderer.transformFirstPersonItem(-0.25F, 1.0F + convertedProgress / 10.0F);
                GL11.glRotated(-convertedProgress * 25.0F, 1.0F, 0.0F, 0.0F);
                itemRenderer.doBlockTransformations();

                break;
            }

            case "fast 2": {
                GlStateManager.translate(0.41F, -0.25F, -0.5555557F);
                GlStateManager.translate(0.0F, 0, 0.0F);
                GlStateManager.rotate(35.0F, 0f, 1.5F, 0.0F);

                final float racism = MathHelper.sin(swingProgress * swingProgress / 64 * (float) Math.PI);

                GlStateManager.rotate(racism * -5.0F, 0.0F, 0.0F, 0.0F);
                GlStateManager.rotate(convertedProgress * -12.0F, 0.0F, 0.0F, 1.0F);
                GlStateManager.rotate(convertedProgress * -65.0F, 1.0F, 0.0F, 0.0F);

                GlStateManager.scale(0.3F, 0.3F, 0.3F);
                itemRenderer.doBlockTransformations();

                break;
            }

            case "fast": {
                itemRenderer.transformFirstPersonItem(animationProgression, swingProgress);
                itemRenderer.doBlockTransformations();
                GlStateManager.translate(-0.3F, -0.1F, -0.0F);

                break;
            }
        }
    }
}
