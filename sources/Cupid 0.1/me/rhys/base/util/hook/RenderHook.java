package me.rhys.base.util.hook;

import me.rhys.base.Lite;
import me.rhys.base.event.Event;
import me.rhys.base.event.impl.render.RenderGameOverlayEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class RenderHook extends GuiIngame {
  public RenderHook(Minecraft mcIn) {
    super(mcIn);
  }
  
  public void renderGameOverlay(float partialTicks) {
    super.renderGameOverlay(partialTicks);
    GlStateManager.pushMatrix();
    ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
    Lite.EVENT_BUS.call((Event)new RenderGameOverlayEvent(partialTicks, resolution.getScaledWidth(), resolution.getScaledHeight()));
    GlStateManager.popMatrix();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\bas\\util\hook\RenderHook.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */