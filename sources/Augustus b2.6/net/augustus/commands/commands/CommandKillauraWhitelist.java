// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.commands.commands;

import org.apache.commons.lang3.StringUtils;
import net.augustus.utils.interfaces.MM;
import net.augustus.commands.Command;

public class CommandKillauraWhitelist extends Command implements MM
{
    public CommandKillauraWhitelist() {
        super(".killauraslot");
    }
    
    @Override
    public void commandAction(final String[] message) {
        super.commandAction(message);
        boolean errorMessage = true;
        if (message.length == 2 && message[1].equalsIgnoreCase("show")) {
            String string = "";
            if (CommandKillauraWhitelist.mm.killAura.slot1.getBoolean()) {
                string += " 1,";
            }
            if (CommandKillauraWhitelist.mm.killAura.slot2.getBoolean()) {
                string += " 2,";
            }
            if (CommandKillauraWhitelist.mm.killAura.slot3.getBoolean()) {
                string += " 3,";
            }
            if (CommandKillauraWhitelist.mm.killAura.slot4.getBoolean()) {
                string += " 4,";
            }
            if (CommandKillauraWhitelist.mm.killAura.slot5.getBoolean()) {
                string += " 5,";
            }
            if (CommandKillauraWhitelist.mm.killAura.slot6.getBoolean()) {
                string += " 6,";
            }
            if (CommandKillauraWhitelist.mm.killAura.slot7.getBoolean()) {
                string += " 7,";
            }
            if (CommandKillauraWhitelist.mm.killAura.slot8.getBoolean()) {
                string += " 8,";
            }
            if (CommandKillauraWhitelist.mm.killAura.slot9.getBoolean()) {
                string += " 9,";
            }
            string = StringUtils.substring(string, 0, -1);
            this.sendChat("§7KillAuraSlots:" + string);
            errorMessage = false;
        }
        if (message.length == 3 && message[1].equalsIgnoreCase("add")) {
            final int id = Integer.parseInt(message[2]);
            switch (id) {
                case 1: {
                    CommandKillauraWhitelist.mm.killAura.slot1.setBoolean(true);
                    this.sendChat("§7Slot 1 added");
                    errorMessage = false;
                    break;
                }
                case 2: {
                    CommandKillauraWhitelist.mm.killAura.slot2.setBoolean(true);
                    this.sendChat("§7Slot 2 added");
                    errorMessage = false;
                    break;
                }
                case 3: {
                    CommandKillauraWhitelist.mm.killAura.slot3.setBoolean(true);
                    this.sendChat("§7Slot 3 added");
                    errorMessage = false;
                    break;
                }
                case 4: {
                    CommandKillauraWhitelist.mm.killAura.slot4.setBoolean(true);
                    this.sendChat("§7Slot 4 added");
                    errorMessage = false;
                    break;
                }
                case 5: {
                    CommandKillauraWhitelist.mm.killAura.slot5.setBoolean(true);
                    this.sendChat("§7Slot 5 added");
                    errorMessage = false;
                    break;
                }
                case 6: {
                    CommandKillauraWhitelist.mm.killAura.slot6.setBoolean(true);
                    this.sendChat("§7Slot 6 added");
                    errorMessage = false;
                    break;
                }
                case 7: {
                    CommandKillauraWhitelist.mm.killAura.slot7.setBoolean(true);
                    this.sendChat("§7Slot 7 added");
                    errorMessage = false;
                    break;
                }
                case 8: {
                    CommandKillauraWhitelist.mm.killAura.slot8.setBoolean(true);
                    this.sendChat("§7Slot 8 added");
                    errorMessage = false;
                    break;
                }
                case 9: {
                    CommandKillauraWhitelist.mm.killAura.slot9.setBoolean(true);
                    this.sendChat("§7Slot 9 added");
                    errorMessage = false;
                    break;
                }
            }
        }
        if (message.length == 3 && message[1].equalsIgnoreCase("remove")) {
            final int id = Integer.parseInt(message[2]);
            switch (id) {
                case 1: {
                    CommandKillauraWhitelist.mm.killAura.slot1.setBoolean(false);
                    this.sendChat("§7Slot 1 removed");
                    errorMessage = false;
                    break;
                }
                case 2: {
                    CommandKillauraWhitelist.mm.killAura.slot2.setBoolean(false);
                    this.sendChat("§7Slot 2 removed");
                    errorMessage = false;
                    break;
                }
                case 3: {
                    CommandKillauraWhitelist.mm.killAura.slot3.setBoolean(false);
                    this.sendChat("§7Slot 3 removed");
                    errorMessage = false;
                    break;
                }
                case 4: {
                    CommandKillauraWhitelist.mm.killAura.slot4.setBoolean(false);
                    this.sendChat("§7Slot 4 removed");
                    errorMessage = false;
                    break;
                }
                case 5: {
                    CommandKillauraWhitelist.mm.killAura.slot5.setBoolean(false);
                    this.sendChat("§7Slot 5 removed");
                    errorMessage = false;
                    break;
                }
                case 6: {
                    CommandKillauraWhitelist.mm.killAura.slot6.setBoolean(false);
                    this.sendChat("§7Slot 6 removed");
                    errorMessage = false;
                    break;
                }
                case 7: {
                    CommandKillauraWhitelist.mm.killAura.slot7.setBoolean(false);
                    this.sendChat("§7Slot 7 removed");
                    errorMessage = false;
                    break;
                }
                case 8: {
                    CommandKillauraWhitelist.mm.killAura.slot8.setBoolean(false);
                    this.sendChat("§7Slot 8 removed");
                    errorMessage = false;
                    break;
                }
                case 9: {
                    CommandKillauraWhitelist.mm.killAura.slot9.setBoolean(false);
                    this.sendChat("§7Slot 9 removed");
                    errorMessage = false;
                    break;
                }
            }
        }
        if (errorMessage) {
            this.errorMessage();
        }
    }
    
    private void errorMessage() {
        this.sendChat("§7.killauraslot [§cadd/remove/show§7] [§2SlotNumber§7]");
    }
    
    @Override
    public void helpMessage() {
        this.sendChat("§c.killauraslot§7 (set the whitelist for KillAura Slots)");
    }
}
