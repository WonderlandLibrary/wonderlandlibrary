// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.ui.newui;

import com.google.common.collect.Lists;
import java.awt.Rectangle;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import java.util.List;

public final class SmartScissor
{
    private static State state;
    private static List<State> stateStack;
    
    public static void push() {
        SmartScissor.stateStack.add(SmartScissor.state.clone());
        GL11.glPushAttrib(524288);
    }
    
    public static void pop() {
        SmartScissor.state = SmartScissor.stateStack.remove(SmartScissor.stateStack.size() - 1);
        GL11.glPopAttrib();
    }
    
    public static void unset() {
        GL11.glDisable(3089);
        SmartScissor.state.enabled = false;
    }
    
    public static void setFromComponentCoordinates(final int x, final int y, final int width, final int height) {
        final ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        final int scaleFactor = ScaledResolution.getScaleFactor();
        final int screenX = x * scaleFactor;
        int screenY = y * scaleFactor;
        final int screenWidth = width * scaleFactor;
        final int screenHeight = height * scaleFactor;
        screenY = Minecraft.getMinecraft().displayHeight - screenY - screenHeight;
        set(screenX, screenY, screenWidth, screenHeight);
    }
    
    public static void set(final int x, final int y, final int width, final int height) {
        final Rectangle screen = new Rectangle(0, 0, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        Rectangle current;
        if (SmartScissor.state.enabled) {
            current = new Rectangle(SmartScissor.state.x, SmartScissor.state.y, SmartScissor.state.width, SmartScissor.state.height);
        }
        else {
            current = screen;
        }
        final Rectangle target = new Rectangle(x + SmartScissor.state.transX, y + SmartScissor.state.transY, width, height);
        Rectangle result = current.intersection(target);
        result = result.intersection(screen);
        if (result.width < 0) {
            result.width = 0;
        }
        if (result.height < 0) {
            result.height = 0;
        }
        SmartScissor.state.enabled = true;
        SmartScissor.state.x = result.x;
        SmartScissor.state.y = result.y;
        SmartScissor.state.width = result.width;
        SmartScissor.state.height = result.height;
        GL11.glEnable(3089);
        GL11.glScissor(result.x, result.y, result.width, result.height);
    }
    
    public static void translate(final int x, final int y) {
        SmartScissor.state.transX = x;
        SmartScissor.state.transY = y;
    }
    
    public static void translateFromComponentCoordinates(final int x, final int y) {
        final ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        final int totalHeight = res.getScaledHeight();
        final int scaleFactor = ScaledResolution.getScaleFactor();
        final int screenX = x * scaleFactor;
        int screenY = y * scaleFactor;
        screenY = totalHeight * scaleFactor - screenY;
        translate(screenX, screenY);
    }
    
    private SmartScissor() {
    }
    
    static {
        SmartScissor.state = new State();
        SmartScissor.stateStack = (List<State>)Lists.newArrayList();
    }
    
    private static class State implements Cloneable
    {
        public boolean enabled;
        public int transX;
        public int transY;
        public int x;
        public int y;
        public int width;
        public int height;
        
        public State clone() {
            try {
                return (State)super.clone();
            }
            catch (CloneNotSupportedException e) {
                throw new AssertionError((Object)e);
            }
        }
    }
}
