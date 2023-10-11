package com.initial.modules.impl.movement;

import com.initial.modules.*;
import net.minecraft.client.gui.*;
import org.lwjgl.input.*;
import net.minecraft.client.entity.*;
import com.initial.events.*;

public class InvMove extends Module
{
    public InvMove() {
        super("InvMove", 0, Category.PLAYER);
    }
    
    @EventTarget
    @Override
    public void onUpdate() {
        if (this.mc.currentScreen != null && !(this.mc.currentScreen instanceof GuiChat)) {
            if (Keyboard.isKeyDown(200)) {
                final EntityPlayerSP thePlayer5;
                final EntityPlayerSP thePlayer = thePlayer5 = this.mc.thePlayer;
                thePlayer5.rotationPitch -= 5.0f;
            }
            if (Keyboard.isKeyDown(208)) {
                final EntityPlayerSP thePlayer6;
                final EntityPlayerSP thePlayer2 = thePlayer6 = this.mc.thePlayer;
                thePlayer6.rotationPitch += 5.0f;
            }
            if (Keyboard.isKeyDown(203)) {
                final EntityPlayerSP thePlayer7;
                final EntityPlayerSP thePlayer3 = thePlayer7 = this.mc.thePlayer;
                thePlayer7.rotationYaw -= 7.0f;
            }
            if (Keyboard.isKeyDown(205)) {
                final EntityPlayerSP thePlayer8;
                final EntityPlayerSP thePlayer4 = thePlayer8 = this.mc.thePlayer;
                thePlayer8.rotationYaw += 7.0f;
            }
        }
    }
}
