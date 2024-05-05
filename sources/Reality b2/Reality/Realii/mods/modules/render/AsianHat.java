
package Reality.Realii.mods.modules.render;

import libraries.javax.vecmath.Vector3d;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.*;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

import java.awt.Color;

import javax.swing.text.html.parser.Entity;

import org.lwjgl.opengl.GL11;
import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.rendering.EventRender3D;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.mods.modules.ClientSettings;
import Reality.Realii.utils.math.Vec4f;


public class AsianHat
extends Module {
	
    public AsianHat(){
        super("AsianHat", ModuleType.Render);
        
    }
    
    
    @EventHandler
    public void onRender(EventRender3D eventRender) {
    
    	Color c = new Color(ClientSettings.r.getValue().intValue(), ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue());
    	  if (mc.gameSettings.thirdPersonView != 0 && !mc.thePlayer.isInvisible()) {
              for (int i = 0; i < 165; ++i) {
                  drawHat(mc.thePlayer, 0.001 + (double)((float)i * 0.0045f), eventRender.getPartialTicks(), 20, 2.0f, (mc.thePlayer.isSneaking() ? 2.0f : 2.15f) - (float)i * 0.002f, 0x800080);
              }
          }
    }
    
    public static Color getColorInt() {
        return new Color(ClientSettings.r.getValue().intValue(), ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue());
    }
   
    
    

    public void drawHat(EntityPlayerSP thePlayer, double radius, float partialTicks, int points, float width, float yAdd, int color) {
        GL11.glPushMatrix();
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glDisable((int)2929);
        GL11.glLineWidth((float)width);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)2929);
        GL11.glBegin((int)3);
        double d = mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * (double)mc.timer.renderPartialTicks;
        mc.getRenderManager();
        double x = d - RenderManager.viewerPosX;
        double d2 = mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * (double)mc.timer.renderPartialTicks;
        mc.getRenderManager();
        double y = d2 - RenderManager.viewerPosY + (double)yAdd + (mc.thePlayer.isSneaking() ? -0.2 : 0.0);
        double d3 = mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * (double)mc.timer.renderPartialTicks;
        mc.getRenderManager();
        double z = d3 - RenderManager.viewerPosZ;
        GL11.glColor4f((float)((float)new Color(color).getRed() / 255.0f), (float)((float)new Color(color).getGreen() / 255.0f), (float)((float)new Color(color).getBlue() / 255.0f), (float)0.15f);
        for (int i = 0; i <= points; ++i) {
            GL11.glVertex3d((double)(x + radius * Math.cos((double)i * (Math.PI * 2) / (double)points)), (double)y, (double)(z + radius * Math.sin((double)i * (Math.PI * 2) / (double)points)));
        }
        GL11.glEnd();
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)2929);
        GL11.glEnable((int)3553);
        GlStateManager.enableDepth();
        GL11.glPopMatrix();
    }
}