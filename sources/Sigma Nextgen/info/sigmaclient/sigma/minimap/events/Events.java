package info.sigmaclient.sigma.minimap.events;

import info.sigmaclient.sigma.minimap.animation.*;
import info.sigmaclient.sigma.minimap.interfaces.*;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class Events
{
    
    public Events() {
    }


    public static void drawMiniMap() {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        InterfaceHandler.drawInterfaces(Minecraft.getInstance().timer.renderPartialTicks);
        Animation.tick();
    }
}
