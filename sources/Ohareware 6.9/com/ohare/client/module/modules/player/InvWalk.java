package com.ohare.client.module.modules.player;

import java.awt.Color;
import java.util.Objects;
import java.util.Set;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.ImmutableSet;
import com.ohare.client.event.events.player.UpdateEvent;
import com.ohare.client.module.Module;
import com.ohare.client.utils.value.impl.BooleanValue;

import dorkbox.messageBus.annotations.Subscribe;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiCommandBlock;
import net.minecraft.client.gui.GuiControls;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.gui.GuiSleepMP;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

public class InvWalk extends Module {
    private BooleanValue rotate = new BooleanValue("Rotate", false);
    private final Set<Class<? extends GuiScreen>> blacklisted = new ImmutableSet.Builder<Class<? extends GuiScreen>>().add(GuiChat.class).add(GuiCommandBlock.class).add(GuiControls.class).add(GuiEditSign.class).add(GuiRepair.class).add(GuiScreenBook.class).add(GuiSleepMP.class).build();
    private KeyBinding[] movementKeys;

    public InvWalk() {
        super("InvWalk", Category.PLAYER, new Color(154, 168, 255, 255).getRGB());
        setDescription("Walk while having guis open");
        setRenderlabel("Inv Walk");
        addValues(rotate);
        GameSettings settings = mc.gameSettings;
        this.movementKeys = new KeyBinding[]{settings.keyBindForward, settings.keyBindRight, settings.keyBindBack, settings.keyBindLeft, settings.keyBindJump, settings.keyBindSprint};
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        KeyBinding[] moveKeys = {mc.gameSettings.keyBindRight, mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindForward, mc.gameSettings.keyBindJump, mc.gameSettings.keyBindSprint,};
        if ((mc.currentScreen instanceof GuiScreen) && !(mc.currentScreen instanceof GuiMainMenu) && !(mc.currentScreen instanceof GuiChat)) {
            KeyBinding[] array;
            int length = (array = moveKeys).length;
            for (int i = 0; i < length; i++) {
                KeyBinding key = array[i];
                key.pressed = Keyboard.isKeyDown(key.getKeyCode());
            }
            if (rotate.isEnabled()) {
                if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
                    mc.thePlayer.rotationYaw += 4;
                } else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
                    mc.thePlayer.rotationYaw -= 4;
                } else if (Keyboard.isKeyDown(Keyboard.KEY_UP) && mc.thePlayer.rotationPitch - 4 > -90) {
                    mc.thePlayer.rotationPitch -= 4;
                } else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) && mc.thePlayer.rotationPitch + 4 < 90) {
                    mc.thePlayer.rotationPitch += 4;
                }
            }
        } else if (Objects.isNull(mc.currentScreen)) {
            KeyBinding[] array2;
            int length2 = (array2 = moveKeys).length;
            for (int j = 0; j < length2; j++) {
                KeyBinding bind = array2[j];
                if (!Keyboard.isKeyDown(bind.getKeyCode())) {
                    KeyBinding.setKeyBindState(bind.getKeyCode(), false);
                }
            }
        }
    }
}