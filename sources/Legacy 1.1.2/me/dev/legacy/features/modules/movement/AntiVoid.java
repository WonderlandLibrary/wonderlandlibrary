package me.dev.legacy.features.modules.movement;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.dev.legacy.features.command.Command;
import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;

public class AntiVoid extends Module {
    public Setting<Mode> mode = register(new Setting<Mode>("Mode", Mode.BOUNCE));
    public Setting<Boolean> display = register(new Setting<Boolean>("Display", true));

    public AntiVoid() {
        super("AntiVoid", "Glitches you up from void.", Module.Category.MOVEMENT, false, false, false);
    }

    @Override
    public void onUpdate() {
        double yLevel = mc.player.posY;
        if (yLevel <= .5) {
            Command.sendMessage(ChatFormatting.RED + "Player " + ChatFormatting.GREEN + mc.player.getName() + ChatFormatting.RED + " is in the void!");
            if (mode.getValue().equals(Mode.BOUNCE)) {
                mc.player.moveVertical = 10;
                mc.player.jump();
            }
            if (mode.getValue().equals(Mode.LAUNCH)) {
                mc.player.moveVertical = 100;
                mc.player.jump();
            }
        } else {
            mc.player.moveVertical = 0;
        }
    }

    @Override
    public void onDisable() {
        mc.player.moveVertical = 0;
    }

    @Override
    public String getDisplayInfo() {
        if (display.getValue()) {
            if (mode.getValue().equals(Mode.BOUNCE)) {
                return "Bounce";
            }
            if (mode.getValue().equals(Mode.LAUNCH)) {
                return "Launch";
            }
        }
        return null;
    }

        public enum Mode {
            BOUNCE,
            LAUNCH,
        }
    }