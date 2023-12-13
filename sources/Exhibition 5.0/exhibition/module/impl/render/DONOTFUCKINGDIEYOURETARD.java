// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.render;

import exhibition.event.RegisterEvent;
import java.awt.Color;
import exhibition.event.impl.EventRenderGui;
import exhibition.event.Event;
import exhibition.module.data.ModuleData;
import exhibition.module.Module;

public class DONOTFUCKINGDIEYOURETARD extends Module
{
    public DONOTFUCKINGDIEYOURETARD(final ModuleData data) {
        super(data);
    }
    
    @RegisterEvent(events = { EventRenderGui.class })
    @Override
    public void onEvent(final Event event) {
        if (event instanceof EventRenderGui) {
            final EventRenderGui er = (EventRenderGui)event;
            final int width = er.getResolution().getScaledWidth() / 2;
            final int height = er.getResolution().getScaledHeight() / 2;
            final String XD = "" + (int)DONOTFUCKINGDIEYOURETARD.mc.thePlayer.getHealth();
            final int XDDD = DONOTFUCKINGDIEYOURETARD.mc.fontRendererObj.getStringWidth(XD);
            float health = DONOTFUCKINGDIEYOURETARD.mc.thePlayer.getHealth();
            if (health > 20.0f) {
                health = 20.0f;
            }
            final int red = (int)Math.abs(health * 5.0f * 0.01f * 0.0f + (1.0f - health * 5.0f * 0.01f) * 255.0f);
            final int green = (int)Math.abs(health * 5.0f * 0.01f * 255.0f + (1.0f - health * 5.0f * 0.01f) * 0.0f);
            final Color customColor = new Color(red, green, 0).brighter();
            DONOTFUCKINGDIEYOURETARD.mc.fontRendererObj.drawStringWithShadow(XD, -XDDD / 2 + width, height - 17, customColor.getRGB());
        }
    }
}
