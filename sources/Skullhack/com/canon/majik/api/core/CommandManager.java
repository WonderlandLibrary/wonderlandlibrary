package com.canon.majik.api.core;

import com.canon.majik.api.utils.Globals;
import com.canon.majik.api.utils.client.ChatUtils;
import com.canon.majik.impl.command.api.Command;
import com.canon.majik.impl.command.impl.BindCommand;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager implements Globals {
    public static String prefix = ".";
    private final ArrayList<Command> commands = new ArrayList<>();

    public CommandManager() {
        MinecraftForge.EVENT_BUS.register(this);
        init();
    }

    public void init(){
        commands.addAll(Arrays.asList(
                new BindCommand()
        ));
    }

    public static void setPrefix(String prefix) {
        CommandManager.prefix = prefix;
    }

    @SubscribeEvent
    public void chatEvent(ClientChatEvent event) {
        if (event.getMessage().startsWith(prefix)) {
            event.setCanceled(true);

            List<String> args = Arrays.asList(event.getMessage().substring(prefix.length()).trim().split(" "));
            if (args.isEmpty()) {
                return;
            }

            for (Command command : commands) {
                if (command.getName().equalsIgnoreCase(args.get(0))) {
                    command.runCommand(args.subList(1, args.size()));
                    return;
                }
            }

            ChatUtils.tempMessage("Invalid Command, Try " + prefix + "help", 1);
        }
    }

    public String getPrefix() {
        return prefix;
    }

    public ArrayList<Command> getCommands() {
        return commands;
    }
}
