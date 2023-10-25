/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.player;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.settings.KeyBinding;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.settings.impl.BooleanSetting;
import org.lwjgl.input.Keyboard;

public class GuiWalk
extends Feature {
    private final BooleanSetting noShiftPress = new BooleanSetting("No Shift Press", true, () -> true);

    public GuiWalk() {
        super("GuiWalk", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0445\u043e\u0434\u0438\u0442\u044c \u0432 \u043e\u043a\u0440\u044b\u0442\u044b\u0445 \u043a\u043e\u043d\u0442\u0435\u0439\u043d\u0435\u0440\u0430\u0445", Type.Player);
        this.addSettings(this.noShiftPress);
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        KeyBinding[] keys = new KeyBinding[]{GuiWalk.mc.gameSettings.keyBindSneak, GuiWalk.mc.gameSettings.keyBindForward, GuiWalk.mc.gameSettings.keyBindBack, GuiWalk.mc.gameSettings.keyBindLeft, GuiWalk.mc.gameSettings.keyBindRight, GuiWalk.mc.gameSettings.keyBindJump, GuiWalk.mc.gameSettings.keyBindSprint};
        if (GuiWalk.mc.currentScreen instanceof GuiChat || GuiWalk.mc.currentScreen instanceof GuiEditSign) {
            return;
        }
        for (KeyBinding keyBinding : keys) {
            if (this.noShiftPress.getCurrentValue() && keyBinding == GuiWalk.mc.gameSettings.keyBindSneak) continue;
            keyBinding.pressed = Keyboard.isKeyDown(keyBinding.getKeyCode());
        }
    }
}

