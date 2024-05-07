package com.masterof13fps.features.commands.impl;

import com.masterof13fps.Client;
import com.masterof13fps.Wrapper;
import com.masterof13fps.features.commands.Command;
import com.masterof13fps.utils.NotifyUtil;
import com.masterof13fps.manager.notificationmanager.NotificationType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;

public class Enchant extends Command {

    String syntax = Client.main().getClientPrefix() + "enchant <all/hand>";

    @Override
    public void execute(String[] args) {

        if (!Wrapper.mc.thePlayer.capabilities.isCreativeMode)
            notify.chat("Nur im Kreativmodus verfügbar!");

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("hand")) {
                ItemStack currentItem = Wrapper.mc.thePlayer.inventory.getCurrentItem();
                if (currentItem == null)
                    notify.chat("Halte ein Item in der Hand!");
                for (Enchantment enchantment : Enchantment.enchantmentsList)
                    try {
                        if (enchantment == Enchantment.silkTouch)
                            continue;
                        currentItem.addEnchantment(enchantment, 127);
                    } catch (Exception e) {

                    }
            } else if (args[0].equalsIgnoreCase("all")) {
                int items = 0;
                for (int i = 0; i < 40; i++) {
                    ItemStack currentItem =
                            Wrapper.mc.thePlayer.inventory.getStackInSlot(i);
                    if (currentItem == null)
                        continue;
                    items++;
                    for (Enchantment enchantment : Enchantment.enchantmentsList)
                        try {
                            if (enchantment == Enchantment.silkTouch)
                                continue;
                            currentItem.addEnchantment(enchantment, 127);
                        } catch (Exception e) {

                        }
                }
                notify.chat("Erfolgreich alle Items verzaubert!");
            } else {
                notify.notification("Falscher Syntax!", "Nutze §c" + syntax + "§r!", NotificationType.ERROR, 5);
            }
        } else {
            notify.notification("Falscher Syntax!", "Nutze §c" + syntax + "§r!", NotificationType.ERROR, 5);
        }
    }

    public Enchant() {
        super("enchant", "ench");
    }

}
