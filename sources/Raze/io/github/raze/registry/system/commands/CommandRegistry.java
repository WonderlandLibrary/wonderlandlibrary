package io.github.raze.registry.system.commands;

import de.florianmichael.rclasses.storage.Storage;
import io.github.raze.Raze;
import io.github.raze.commands.collection.*;
import io.github.raze.commands.system.Command;
import io.github.raze.events.collection.game.EventChatMessage;
import io.github.raze.events.collection.game.EventKeyboard;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.utilities.collection.visual.ChatUtil;
import io.github.raze.utilities.system.Methods;
import net.minecraft.client.gui.GuiChat;
import org.lwjgl.input.Keyboard;

public class CommandRegistry extends Storage<Command> implements Methods {

    public void init() {
        add(
                // Commands
                new HelpCommand(),
                new PrefixCommand(),
                new ConfigCommand(),
                new ClipCommand(),
                new BindCommand(),
                new SayCommand(),
                new IGNCommand(),
                new IPCommand(),
                new ToggleCommand(),
                new NameProtectCommand(),
                new WaterMarkCommand()

        );
    }

    public Command getCommandByName(String input) {
        return this.getList().stream().filter(command -> command.getName().equalsIgnoreCase(input)).findFirst().orElse(null);
    }

    @Listen
    public void onChatMessage(EventChatMessage event) {

        String prefix = Raze.INSTANCE.getPrefix();

        if (event.isCancelled()) {
            return;
        }

        String message = event.getMessage();
        String[] arguments = message.split(" ");

        if (message.startsWith(prefix)) {
            event.setCancelled(true);
        } else {
            return;
        }

        String name = arguments[0].substring(1);

        Command command = getCommandByName(name);

        String result = String.format("Could not find command %s.", name);

        if (command != null) {
            result = command.onCommand(arguments, message);
        }

        if (!result.isEmpty()) {
            ChatUtil.addChatMessage(result);
        }
    }

    @Listen
    public void onKeyboard(EventKeyboard event) {

        String prefix = Raze.INSTANCE.getPrefix();

        int keyCode = event.getKeyCode();

        if (keyCode == Keyboard.getKeyIndex(prefix)) {
            mc.displayGuiScreen(new GuiChat(prefix));
        }

    }

}
