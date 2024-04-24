package me.crimp.claudius.mod.modules.text;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.crimp.claudius.event.events.DeathEvent;
import me.crimp.claudius.event.events.TotemPopEvent;
import me.crimp.claudius.mod.command.Command;
import me.crimp.claudius.mod.modules.Module;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


public class SelfPopCounter extends Module {
    public SelfPopCounter() {
        super("SelfPopCounter", "Counts self totem pops.", Category.Text, false, false);
    }

    public static SelfPopCounter INSTANCE = new SelfPopCounter();
    String totemSpelling;
    public int TotemCount;
    private Boolean hasdied = false;

    @Override
    public void onToggle() {
        TotemCount = 0;
    }


    @SubscribeEvent
    public void onPop(TotemPopEvent event) {
        if (event.getPlayer().getEntityId() == mc.player.getEntityId()) {
            if (hasdied.equals(true)) {
                TotemCount = 0;
                hasdied = false;
            }
            TotemCount++;
            if (TotemCount == 1) {
                totemSpelling = " totem";
            } else if (TotemCount > 1) {
                totemSpelling = " totems";
            }
            Command.sendOverwriteClientMessage(ChatFormatting.BLUE + "You" + ChatFormatting.RESET + " have popped " + ChatFormatting.BLUE + TotemCount + ChatFormatting.RESET + totemSpelling);
        }
    }


    @SubscribeEvent
    public void onDeath(DeathEvent event) {
        if (event.getPlayer().getEntityId() == mc.player.getEntityId()) {
            hasdied = true;
            if (TotemCount == 0) {
                Command.sendOverwriteClientMessage(ChatFormatting.BLUE + "You" + ChatFormatting.RESET + " just died after popping " + ChatFormatting.BLUE + "0 " + ChatFormatting.RESET + "totems");
            } else {
                Command.sendOverwriteClientMessage(ChatFormatting.BLUE + "You" + ChatFormatting.RESET + " just died after popping " + ChatFormatting.BLUE + TotemCount + ChatFormatting.RESET + totemSpelling);
            }
        }
    }
}


