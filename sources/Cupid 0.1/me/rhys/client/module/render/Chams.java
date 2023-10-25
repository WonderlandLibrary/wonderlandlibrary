package me.rhys.client.module.render;

import java.awt.Color;
import me.rhys.base.Lite;
import me.rhys.base.event.Event;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.render.RenderEntityEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class Chams extends Module {
  public Chams(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
  }
  
  @EventTarget
  public void onRender(RenderEntityEvent event) {
    if (event.getEntity().isEntityEqual((Entity)player()) || 
      !(event.getEntity() instanceof net.minecraft.entity.player.EntityPlayer))
      return; 
    HUD hud = (HUD)Lite.MODULE_FACTORY.findByClass(HUD.class);
    if (event.getType() == Event.Type.PRE) {
      if (!event.isLayers()) {
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 772);
        float[] color = RenderUtil.getColors((hud.colorMode == HUD.ColorMode.RAINBOW) ? 
            Color.getHSBColor((float)(Minecraft.getSystemTime() + (10 * (Minecraft.getMinecraft()).thePlayer.ticksExisted)) % 5000.0F / 5000.0F, 1.0F, 0.2F)
            .getRGB() : (new Color(52, 189, 235)).getRGB());
        if (this.mc.thePlayer.canEntityBeSeen((Entity)event.getEntity())) {
          if (hud.colorMode != HUD.ColorMode.RAINBOW)
            GL11.glBlendFunc(770, 771); 
          GlStateManager.color(color[0], color[1], color[2]);
        } else {
          GlStateManager.color(1.0F, 0.0F, 0.0F, 1.0F);
        } 
      } 
      GlStateManager.doPolygonOffset(event.isLayers() ? -2.0E9F : -2.0E7F, -3.0F);
      GlStateManager.enablePolygonOffset();
    } else {
      GlStateManager.doPolygonOffset(0.0F, 0.0F);
      GlStateManager.disablePolygonOffset();
      if (!event.isLayers()) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
      } 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\render\Chams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */