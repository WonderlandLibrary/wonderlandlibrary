// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.movement;

import net.minecraft.client.entity.EntityPlayerSP;
import com.elementars.eclient.util.Wrapper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.GuiChat;
import com.elementars.eclient.module.Category;
import com.elementars.eclient.module.Module;

public class GuiMove extends Module
{
    public GuiMove() {
        super("GuiMove", "Move in gui menus", 0, Category.MOVEMENT, true);
    }
    
    @Override
    public void onUpdate() {
        if (GuiMove.mc.currentScreen instanceof GuiChat || GuiMove.mc.currentScreen == null) {
            return;
        }
        final int[] array;
        final int[] keys = array = new int[] { GuiMove.mc.gameSettings.keyBindForward.getKeyCode(), GuiMove.mc.gameSettings.keyBindLeft.getKeyCode(), GuiMove.mc.gameSettings.keyBindRight.getKeyCode(), GuiMove.mc.gameSettings.keyBindBack.getKeyCode(), GuiMove.mc.gameSettings.keyBindJump.getKeyCode() };
        for (final int keyCode : array) {
            if (Keyboard.isKeyDown(keyCode)) {
                KeyBinding.setKeyBindState(keyCode, true);
            }
            else {
                KeyBinding.setKeyBindState(keyCode, false);
            }
        }
        if (Wrapper.getMinecraft().currentScreen instanceof GuiContainer) {
            if (Keyboard.isKeyDown((int)200)) {
                final EntityPlayerSP player = Wrapper.getMinecraft().player;
                player.rotationPitch -= 7.0f;
            }
            if (Keyboard.isKeyDown((int)208)) {
                final EntityPlayerSP player2 = Wrapper.getMinecraft().player;
                player2.rotationPitch += 7.0f;
            }
            if (Keyboard.isKeyDown((int)205)) {
                final EntityPlayerSP player3 = Wrapper.getMinecraft().player;
                player3.rotationYaw += 7.0f;
            }
            if (Keyboard.isKeyDown((int)203)) {
                final EntityPlayerSP player4 = Wrapper.getMinecraft().player;
                player4.rotationYaw -= 7.0f;
            }
            if (Keyboard.isKeyDown(Wrapper.getMinecraft().gameSettings.keyBindSprint.getKeyCode())) {
                Wrapper.getMinecraft().player.setSprinting(true);
            }
        }
    }
}
