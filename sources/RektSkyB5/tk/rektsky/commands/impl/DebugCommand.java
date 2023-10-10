/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.commands.impl;

import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.network.play.client.C02PacketUseEntity;
import tk.rektsky.Client;
import tk.rektsky.commands.Command;
import tk.rektsky.utils.inventory.InventoryUtils;

public class DebugCommand
extends Command {
    public DebugCommand() {
        super("debug", "<help | attribute>", "A command that helps developers develop stuff");
    }

    @Override
    public void onCommand(String label, String[] args) {
        if (args.length <= 0) {
            Client.addClientChat("I'm lazy to write help command. You won't need this command, and it doesn't help you at all (You can't access any hidden feature or something except something like get item attribute id that you can get from google). Give up");
            return;
        }
        if (args[0].equalsIgnoreCase("help")) {
            Client.addClientChat("I'm lazy to write help command. You won't need this command, and it doesn't help you at all (You can't access any hidden feature or something except something like get item attribute id that you can get from google). Give up");
        }
        if (args[0].equalsIgnoreCase("attribute")) {
            if (this.mc.thePlayer.getHeldItem() != null) {
                Multimap<String, AttributeModifier> attributeModifiers = this.mc.thePlayer.getHeldItem().getAttributeModifiers();
                for (String key : attributeModifiers.keySet()) {
                    Client.addClientChat(key);
                    for (AttributeModifier attributeModifier : attributeModifiers.get(key)) {
                        Client.addClientChat(" - " + attributeModifier.getName() + ": " + attributeModifier.getAmount());
                    }
                }
            } else {
                Client.addClientChat("Try hold something with attribute (Attack Damage etc.)");
            }
        }
        if (args[0].equalsIgnoreCase("damage")) {
            if (this.mc.thePlayer.getHeldItem() != null) {
                Client.addClientChat(InventoryUtils.getAttackDamage(this.mc.thePlayer.getHeldItem()) + "");
            } else {
                Client.addClientChat("Try holding a weapon");
            }
        }
        if (args[0].equalsIgnoreCase("C03")) {
            C02PacketUseEntity packet = new C02PacketUseEntity(this.mc.theWorld.getEntityByID(325193), C02PacketUseEntity.Action.INTERACT);
            this.mc.thePlayer.sendQueue.addToSendQueueSilent(packet);
        }
    }
}

