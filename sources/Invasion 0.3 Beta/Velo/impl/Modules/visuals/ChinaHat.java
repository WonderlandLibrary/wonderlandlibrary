package Velo.impl.Modules.visuals;

import java.awt.Color;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import Velo.api.Module.Module;
import Velo.api.Module.Module.Category;
import Velo.api.Util.Other.ColorUtil;
import Velo.api.Util.Render.RenderUtil;
import Velo.impl.Event.EventMovement;
import Velo.impl.Event.EventRender3D;
import Velo.impl.Event.EventUpdate;
import Velo.impl.Event.EventUpdateModel;
import Velo.impl.Modules.combat.TargetStrafe;
import Velo.impl.Modules.visuals.hud.HUD;
import Velo.impl.Settings.BooleanSetting;
import Velo.impl.Settings.NumberSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;


public class ChinaHat extends Module {

    public BooleanSetting rainbow = new BooleanSetting("Rainbow", false);
  
    public NumberSetting red = new NumberSetting("Red", 255, 1, 255, 1);
    public NumberSetting green = new NumberSetting("Green", 255, 1, 255, 1);
    public NumberSetting blue = new NumberSetting("Blue", 255, 1, 255, 1);

    public NumberSetting points = new NumberSetting("Connectors", 12, 4, 64, 1);

    Color hatColor;
    
    public ChinaHat() {
        super("ChinaHat", "ChinaHat", Keyboard.KEY_NONE, Category.VISUALS);
        this.loadSettings(red, green, blue, points, rainbow);
    }
    
    
    @Override
    public void onUpdate(EventUpdate event) {

    	super.onUpdate(event);
    }
    @Override
    public void onModelUpdate(EventUpdateModel event) {
       
    	super.onModelUpdate(event);
    }
    @Override
    public void onMovementUpdate(EventMovement event) {

    	super.onMovementUpdate(event);
    }
    @Override
    public void onRender3DUpdate(EventRender3D event) {
        if(rainbow.enabled) {
        //    hatColor = new Color(ColorUtil.getRainbow(12, 0.75f, 1f, 5));
        }else {
         //   hatColor = new Color((float) red.getValue() / 255, (float) green.getValue() / 255, (float) blue.getValue() / 255);
        }
        
        float hatColor = 0;
        

        if(mc.gameSettings.thirdPersonView != 0) {
            for(int i = 0; i < 400; i++) {
                if(HUD.colormode.equalsIgnorecase("Fade")) {
                	hatColor = ColorUtil.getGradientOffset(new Color((int) HUD.red2.getValue(), (int) HUD.green2.getValue(), (int) HUD.blue2.getValue(), 255), new Color((int) HUD.red.getValue(), (int) HUD.green.getValue(), (int) HUD.blue.getValue(), 255), (Math.abs(((System.currentTimeMillis()) / 7 - i/2)) / 120D)).getRGB();
                }
                if(HUD.colormode.equalsIgnorecase("Rainbow")) {
                	hatColor = ColorUtil.getRainbow(12, 0.5F, 1, i*5);
                }
                if(HUD.colormode.equalsIgnorecase("Astolfo")) {
                	hatColor = ColorUtil.astolfoColors(i/10, 1000);
                }
                if(HUD.colormode.equalsIgnorecase("Custom")) {
                	hatColor = new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue(), 255).getRGB();
                }
                
                drawHat(mc.thePlayer, 0.009 + i * 0.0014, mc.timer.elapsedPartialTicks , (int) points.getValue(), (float) 2, 2.20f - i * 0.000785f - (mc.thePlayer.isSneaking() ? 0.03f : 0.03f), (int) hatColor);
               
            }
        }

        
    }
    
    
    


    
    
    public static void drawHat(Entity entity, double radius, float partialTicks, int points, float width, float yAdd, int color) {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GL11.glDepthMask(false);
        GL11.glLineWidth(width);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        
        float yaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * Minecraft.getMinecraft().timer.renderPartialTicks;
float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * Minecraft.getMinecraft().timer.renderPartialTicks;

        final double x = RenderUtil.interpolate(entity.prevPosX, entity.posX, partialTicks) - RenderManager.viewerPosX;
        final double y = RenderUtil.interpolate(entity.prevPosY + yAdd, entity.posY + yAdd, partialTicks) - RenderManager.viewerPosY;
        final double z = RenderUtil.interpolate(entity.prevPosZ, entity.posZ, partialTicks) - RenderManager.viewerPosZ;
    //    
        GL11.glColor4f(new Color(color).getRed() / 255f, new Color(color).getGreen() / 255f, new Color(color).getBlue() / 255f, 0.15f);
        for (int i = 0; i <= points; i++) GL11.glVertex3d(x + radius * Math.cos(i * Math.PI * 2 / points), y, z  + radius * Math.sin(i * Math.PI * 2 / points));

        GL11.glEnd();
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }
    
   
}