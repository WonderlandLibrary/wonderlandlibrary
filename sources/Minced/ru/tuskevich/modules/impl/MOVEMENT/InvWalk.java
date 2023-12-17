// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.MOVEMENT;

import ru.tuskevich.event.EventTarget;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import ru.tuskevich.event.events.impl.EventUpdate;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "GuiWalk", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd", type = Type.MOVEMENT)
public class InvWalk extends Module
{
    @EventTarget
    public void onUpdateEvent(final EventUpdate eventUpdate) {
        final KeyBinding[] array;
        final KeyBinding[] buttons = array = new KeyBinding[] { InvWalk.mc.gameSettings.keyBindForward, InvWalk.mc.gameSettings.keyBindJump, InvWalk.mc.gameSettings.keyBindBack, InvWalk.mc.gameSettings.keyBindRight, InvWalk.mc.gameSettings.keyBindLeft, InvWalk.mc.gameSettings.keyBindSprint };
        for (final KeyBinding button : array) {
            if (InvWalk.mc.currentScreen instanceof GuiChat || InvWalk.mc.currentScreen instanceof GuiEditSign) {
                return;
            }
            button.pressed = Keyboard.isKeyDown(button.getKeyCode());
        }
    }
}
