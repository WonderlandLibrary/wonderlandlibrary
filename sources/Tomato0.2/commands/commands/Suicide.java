/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.commands.commands;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.Cheese;
import cow.milkgod.cheese.commands.Command;
import cow.milkgod.cheese.commands.CommandManager;
import cow.milkgod.cheese.events.EventTick;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class Suicide
extends Command {
    private double damage;

    public Suicide() {
        super("Suicide", null, new String[]{"suc"}, null, "Kills you.");
    }

    @EventTarget
    public void onChatSend(EventTick event) {
        if (this.getState()) {
            if (this.mc.thePlayer != null) {
                int i2 = 0;
                while (i2 <= 25) {
                    Cheese.getInstance();
                    Cheese.commandManager.getCommandbyName("Damage").Toggle();
                    ++i2;
                }
            }
            this.Toggle();
        }
    }
}

