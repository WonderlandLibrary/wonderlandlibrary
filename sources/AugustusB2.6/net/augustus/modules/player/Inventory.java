// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.player;

import net.lenni0451.eventapi.reflection.EventTarget;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.GuiChat;
import net.augustus.events.EventSilentMove;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.modules.Module;

public class Inventory extends Module
{
    public Inventory() {
        super("Inventory", new Color(11, 143, 125), Categorys.PLAYER);
    }
    
    @EventTarget
    public void onEventSilentMove(final EventSilentMove eventSilentMove) {
        if (!(Inventory.mc.currentScreen instanceof GuiChat)) {
            KeyBinding.setKeyBindState(Inventory.mc.gameSettings.keyBindForward.getKeyCode(), Keyboard.isKeyDown(Inventory.mc.gameSettings.keyBindForward.getKeyCode()));
            KeyBinding.setKeyBindState(Inventory.mc.gameSettings.keyBindBack.getKeyCode(), Keyboard.isKeyDown(Inventory.mc.gameSettings.keyBindBack.getKeyCode()));
            KeyBinding.setKeyBindState(Inventory.mc.gameSettings.keyBindRight.getKeyCode(), Keyboard.isKeyDown(Inventory.mc.gameSettings.keyBindRight.getKeyCode()));
            KeyBinding.setKeyBindState(Inventory.mc.gameSettings.keyBindLeft.getKeyCode(), Keyboard.isKeyDown(Inventory.mc.gameSettings.keyBindLeft.getKeyCode()));
            KeyBinding.setKeyBindState(Inventory.mc.gameSettings.keyBindSneak.getKeyCode(), Keyboard.isKeyDown(Inventory.mc.gameSettings.keyBindSneak.getKeyCode()));
            KeyBinding.setKeyBindState(Inventory.mc.gameSettings.keyBindJump.getKeyCode(), Keyboard.isKeyDown(Inventory.mc.gameSettings.keyBindJump.getKeyCode()));
            KeyBinding.setKeyBindState(Inventory.mc.gameSettings.keyBindSprint.getKeyCode(), Keyboard.isKeyDown(Inventory.mc.gameSettings.keyBindSprint.getKeyCode()));
            if (Inventory.mm.sprint.isToggled() && !Inventory.mm.scaffoldWalk.isToggled() && !Inventory.mm.newScaffold.isToggled() && (!Inventory.mm.blockFly.isToggled() || Inventory.mm.blockFly.sprint.getBoolean())) {
                KeyBinding.setKeyBindState(Inventory.mc.gameSettings.keyBindSprint.getKeyCode(), true);
            }
        }
    }
}
