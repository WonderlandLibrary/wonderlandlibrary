package com.masterof13fps.features.commands.impl;

import com.masterof13fps.Client;
import com.masterof13fps.Wrapper;
import com.masterof13fps.features.commands.Command;
import com.masterof13fps.utils.NotifyUtil;
import com.masterof13fps.manager.notificationmanager.NotificationType;
import net.minecraft.item.ItemStack;

public class Rename extends Command {

    String syntax = Client.main().getClientPrefix() + "rename <name>";

    @Override
    public void execute(String[] args) {
        if (!Wrapper.mc.thePlayer.capabilities.isCreativeMode) {
            notify.chat("Nur im Kreativmodus verfügbar!");
        }
        if (args.length == 0) {
            notify.notification("Falscher Syntax!", "Nutze §c" + syntax + "§r!", NotificationType.ERROR, 5);
        } else {
            if (Wrapper.mc.thePlayer.getHeldItem() == null) {
                notify.chat("Halte ein Item in der Hand!");
                return;
            }
            String message = args[0];
            for (int i = 1; i < args.length; i++)
                message += " " + args[i];
            message = message.replace("&", "§").replace("&", "§");
            ItemStack item = Wrapper.mc.thePlayer.inventory.getCurrentItem();
            item.setStackDisplayName(message);
            notify.chat("Item umbenannt zu: \"" + message + "\".");
        }
    }

    public Rename() {
        super("rename", "ren");
    }

}
