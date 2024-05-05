package me.earth.earthhack.impl.services.chat;

import me.earth.earthhack.api.command.Command;
import me.earth.earthhack.api.command.Completer;
import me.earth.earthhack.api.event.bus.EventListener;
import me.earth.earthhack.api.event.bus.SubscriberImpl;
import me.earth.earthhack.impl.commands.ConfigCommand;
import me.earth.earthhack.impl.commands.FriendCommand;
import me.earth.earthhack.impl.commands.HistoryCommand;
import me.earth.earthhack.impl.commands.ModuleCommand;
import me.earth.earthhack.impl.commands.ModuleListCommand;
import me.earth.earthhack.impl.commands.PrefixCommand;
import me.earth.earthhack.impl.commands.hidden.FailCommand;
import me.earth.earthhack.impl.commands.hidden.HiddenModule;
import me.earth.earthhack.impl.event.events.network.PacketEvent;
import me.earth.earthhack.impl.modules.client.commands.Commands;
import me.earth.earthhack.impl.services.render.TextRenderer;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.network.play.client.CPacketChatMessage;

import java.util.HashSet;
import java.util.Set;

public class CommandManager extends SubscriberImpl
{
    private static final CommandManager INSTANCE = new CommandManager();
    private static final Command FAIL_COMMAND    = new FailCommand();

    private final Set<Command> commands = new HashSet<>();
    private final Set<Command> hidden   = new HashSet<>();
    private String concatenated_cache;

    private CommandManager()
    {
        this.listeners.add(new EventListener<PacketEvent.Send<CPacketChatMessage>>(PacketEvent.Send.class, CPacketChatMessage.class)
        {
            @Override
            public void invoke(PacketEvent.Send<CPacketChatMessage> event)
            {
                if (event.getPacket().getMessage().startsWith(Commands.getInstance().getPrefix()))
                {
                    applyCommand(event.getPacket().getMessage());
                    event.setCancelled(true);
                }
            }
        });
    }

    public static CommandManager getInstance()
    {
        return INSTANCE;
    }

    public void init()
    {
        commands.add(new ConfigCommand());
        commands.add(new FriendCommand());
        commands.add(new HistoryCommand());
        commands.add(new ModuleListCommand());
        commands.add(new PrefixCommand());

        commands.add(new ModuleCommand()); //always add this last

        hidden.add(new HiddenModule());

        concatenated_cache = concatenateCommands();
    }

    public void renderCommandGui(String message, int x, int y)
    {
        if (message != null && message.startsWith(Commands.getInstance().getPrefix()))
        {
            String[] array = createArray(message);
            String possible = getCommandForMessage(array).getPossibleInputs(array);
            int width = x + TextRenderer.getInstance().getStringWidth(message.trim());
            TextRenderer.getInstance().drawString(possible, width, y, 0xffffffff, true);
        }
    }

    public boolean onTabComplete(GuiTextField inputField)
    {
        if (inputField.getText().startsWith(Commands.getInstance().getPrefix()))
        {
            String[] array = createArray(inputField.getText());
            Completer completer = getCommandForMessage(array).onTabComplete(new Completer(inputField.getText(), array));
            inputField.setText(completer.getResult());
            return completer.shouldMcComplete();
        }

        return true;
    }

    public void applyCommand(String message)
    {
        if (message != null)
        {
            String[] array = createArray(message);
            Command command = getCommandForMessage(array);
            if (command.equals(FAIL_COMMAND) && !message.isEmpty())
            {
                command = getHiddenCommand(array);
            }

            command.execute(array);
        }
    }

    public String getConcatenatedCommands()
    {
        return concatenated_cache;
    }

    private Command getCommandForMessage(String[] array)
    {
        if (array == null || array.length == 0)
        {
            return FAIL_COMMAND;
        }

        for (Command command : commands)
        {
            if (command.fits(array)) //TODO: hmmm
            {
                return command;
            }
        }

        return FAIL_COMMAND;
    }

    private Command getHiddenCommand(String[] array)
    {
        for (Command command : hidden)
        {
            if (command.fits(array))
            {
                return command;
            }
        }

        return FAIL_COMMAND;
    }

    private String[] createArray(String message)
    {
        String noPrefix = message.substring(Commands.getInstance().getPrefix().length());
        return noPrefix.split(" ");
    }

    private String concatenateCommands()
    {
        StringBuilder builder = new StringBuilder("<");

        for (Command command : commands)
        {
            builder.append(command.getName()).append(", ");
        }

        builder.replace(builder.length() - 2, builder.length(), ">");
        return builder.toString();
    }

}
