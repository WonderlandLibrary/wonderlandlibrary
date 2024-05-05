// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.render;

import java.awt.Color;
import net.minecraft.client.gui.ScaledResolution;
import vip.Resolute.events.impl.EventRender2D;
import vip.Resolute.events.Event;
import vip.Resolute.modules.Module;

public class HealthIndicator extends Module
{
    public HealthIndicator() {
        super("HealthIndicator", 0, "Displays player health", Category.RENDER);
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventRender2D) {
            final ScaledResolution sr = new ScaledResolution(HealthIndicator.mc);
            final int width = sr.getScaledWidth() / 2;
            final int height = sr.getScaledHeight() / 2;
            final String playerHealth = "" + (int)HealthIndicator.mc.thePlayer.getHealth();
            final int print = HealthIndicator.mc.fontRendererObj.getStringWidth(playerHealth);
            float health = HealthIndicator.mc.thePlayer.getHealth();
            if (health > 20.0f) {
                health = 20.0f;
            }
            final int red = (int)Math.abs(health * 5.0f * 0.01f * 0.0f + (1.0f - health * 5.0f * 0.01f) * 255.0f);
            final int green = (int)Math.abs(health * 5.0f * 0.01f * 255.0f + (1.0f - health * 5.0f * 0.01f) * 0.0f);
            final Color customColor = new Color(red, green, 0).brighter();
            HealthIndicator.mc.fontRendererObj.drawStringWithShadow(playerHealth, (float)(-print / 2 + width), (float)(height - 17), customColor.getRGB());
        }
    }
}
