/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.mod.mods.MOVEMENT;

import com.darkmagician6.eventapi.EventTarget;
import me.AveReborn.Value;
import me.AveReborn.events.EventUpdate;
import me.AveReborn.mod.Category;
import me.AveReborn.mod.Mod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class InvMove
extends Mod {
    public Value<Boolean> aacp = new Value<Boolean>("InvMove_AACP", false);

    public InvMove() {
        super("InvMove", Category.MOVEMENT);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (this.mc.currentScreen != null && !(this.mc.currentScreen instanceof GuiChat)) {
            KeyBinding[] key;
            KeyBinding[] array = key = new KeyBinding[]{this.mc.gameSettings.keyBindForward, this.mc.gameSettings.keyBindBack, this.mc.gameSettings.keyBindLeft, this.mc.gameSettings.keyBindRight, this.mc.gameSettings.keyBindSprint, this.mc.gameSettings.keyBindJump};
            int length = array.length;
            int i2 = 0;
            while (i2 < length) {
                KeyBinding b2 = array[i2];
                KeyBinding.setKeyBindState(b2.getKeyCode(), Keyboard.isKeyDown(b2.getKeyCode()));
                ++i2;
            }
            if (this.aacp.getValueState().booleanValue()) {
                Minecraft.thePlayer.setSprinting(false);
            }
        }
    }
}

