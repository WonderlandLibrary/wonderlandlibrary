// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.other;

import java.util.HashMap;
import exhibition.event.RegisterEvent;
import java.util.Arrays;
import exhibition.management.command.CommandManager;
import exhibition.management.command.Command;
import exhibition.event.impl.EventChat;
import exhibition.event.Event;
import exhibition.module.data.Setting;
import exhibition.module.data.ModuleData;
import exhibition.module.Module;

public class ChatCommands extends Module
{
    private static final String KEY_PREFIX = "CHAT-PREFIX";
    
    public ChatCommands(final ModuleData data) {
        super(data);
        ((HashMap<String, Setting<String>>)this.settings).put("CHAT-PREFIX", new Setting<String>("CHAT-PREFIX", ".", "Command prefix."));
    }
    
    @RegisterEvent(events = { EventChat.class })
    @Override
    public void onEvent(final Event event) {
        final EventChat ec = (EventChat)event;
        final String prefix = ((HashMap<K, Setting<String>>)this.settings).get("CHAT-PREFIX").getValue();
        if (!ec.getText().startsWith(prefix)) {
            return;
        }
        event.setCancelled(true);
        final String[] commandBits = ec.getText().substring(prefix.length()).split(" ");
        final String commandName = commandBits[0];
        final Command command = CommandManager.commandMap.get(commandName);
        if (command == null) {
            return;
        }
        if (commandBits.length > 1) {
            final String[] commandArguments = Arrays.copyOfRange(commandBits, 1, commandBits.length);
            command.fire(commandArguments);
        }
        else {
            command.fire(null);
        }
    }
}
