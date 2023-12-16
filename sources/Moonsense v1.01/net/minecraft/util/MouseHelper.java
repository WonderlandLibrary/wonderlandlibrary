// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import moonsense.features.SCModule;
import moonsense.features.modules.type.mechanic.FreelookModule;
import moonsense.config.ModuleConfig;
import org.lwjgl.opengl.Display;
import org.lwjgl.input.Mouse;

public class MouseHelper
{
    public int deltaX;
    public int deltaY;
    private static final String __OBFID = "CL_00000648";
    
    public void grabMouseCursor() {
        Mouse.setGrabbed(true);
        this.deltaX = 0;
        this.deltaY = 0;
    }
    
    public void ungrabMouseCursor() {
        Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
        Mouse.setGrabbed(false);
    }
    
    public void mouseXYChange() {
        if (ModuleConfig.INSTANCE.isEnabled(FreelookModule.INSTANCE)) {
            if (FreelookModule.INSTANCE.isHeld()) {
                if (FreelookModule.INSTANCE.invertX.getBoolean()) {
                    this.deltaX = -Mouse.getDX();
                }
                else {
                    this.deltaX = Mouse.getDX();
                }
                if (FreelookModule.INSTANCE.invertY.getBoolean()) {
                    this.deltaY = Mouse.getDY();
                }
                else {
                    this.deltaY = -Mouse.getDY();
                }
            }
            else {
                this.deltaX = Mouse.getDX();
                this.deltaY = Mouse.getDY();
            }
        }
        else {
            this.deltaX = Mouse.getDX();
            this.deltaY = Mouse.getDY();
        }
    }
}
