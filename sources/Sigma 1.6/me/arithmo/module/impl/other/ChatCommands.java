/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.module.impl.other;

import java.util.Arrays;
import java.util.HashMap;
import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventChat;
import me.arithmo.management.command.Command;
import me.arithmo.management.command.CommandManager;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.module.data.Setting;
import me.arithmo.module.data.SettingsMap;
import me.arithmo.util.misc.ChatUtil;

public class ChatCommands
extends Module {
    private static final String KEY_PREFIX = "CHAT-PREFIX";

    public ChatCommands(ModuleData data) {
        super(data);
        this.settings.put("CHAT-PREFIX", new Setting<String>("CHAT-PREFIX", ".", "Command prefix."));
    }

    @RegisterEvent(events={EventChat.class})
    public void onEvent(Event event) {
        EventChat ec = (EventChat)event;
        String prefix = (String)((Setting)this.settings.get("CHAT-PREFIX")).getValue();
        if (!ec.getText().startsWith(prefix)) {
            return;
        }
        event.setCancelled(true);
        String[] commandBits = ec.getText().substring(prefix.length()).split(" ");
        String commandName = commandBits[0];
        Command command = CommandManager.commandMap.get(commandName);
        if (command == null) {
            ChatUtil.printChat("\u00a7c[\u00a7fS\u00a7c]\u00a77 \u00a77\"\u00a7f" + commandName + "\u00a77\"\u00a78 is not a valid command.");
            return;
        }
        if (commandBits.length > 1) {
            String[] commandArguments = Arrays.copyOfRange(commandBits, 1, commandBits.length);
            command.fire(commandArguments);
        } else {
            command.fire(null);
        }
    }
}

