// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.util.render;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;

public class Screen
{
    public static int getMouseX() {
        return Mouse.getX() * Minecraft.getMinecraft().currentScreen.width / Minecraft.getMinecraft().displayWidth;
    }
    
    public static int getMouseY() {
        return Minecraft.getMinecraft().currentScreen.height - Mouse.getY() * Minecraft.getMinecraft().currentScreen.height / Minecraft.getMinecraft().displayHeight - 1;
    }
    
    public static ScaledResolution getResolution() {
        return new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
    }
}
