package wtf.expensive.command;

import net.minecraft.util.text.TextFormatting;
import wtf.expensive.command.impl.*;
import wtf.expensive.managment.Managment;
import wtf.expensive.util.ClientUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager {
    public List<Command> commandList = new ArrayList<>();

    public XRayCommand xRayCommand;
    public boolean isMessage;


    public void init() {
        commandList.addAll(Arrays.asList(
                this.xRayCommand = new XRayCommand(),
                new HClipCommand(),
                new VClipCommand(),
                new HelpCommand(),
                new MacroCommand(),
                new BindCommand(),
                new ConfigCommand(),
                new FriendCommand(),
                new PanicCommand(),
                new TPCommand(),
                new LoginCommand(),
                new StaffCommand(),
                new GPSCommand(),
                new ParseCommand(),
                new ReloadCommand(),
                new ObfuscatorCommand(),
                new ToggleCommand()
        ));
    }

    public void runCommands(String message) {
        if (ClientUtil.legitMode || Managment.FUNCTION_MANAGER.noCommands.state) {
            isMessage = false;
            return;
        }

        if (message.startsWith(".")) {
            for (Command command : Managment.COMMAND_MANAGER.getCommands()) {
                if (message.startsWith("." + command.command)) {
                    try {
                        command.run(message.split(" "));
                    } catch (Exception ex) {
                        command.error();
                        ex.printStackTrace();
                    }
                    isMessage = true;
                    return;
                }
            }
            ClientUtil.sendMesage(TextFormatting.RED + "Команда не наейдена!");
            ClientUtil.sendMesage(TextFormatting.GRAY + "Список всех команд: .help");
            isMessage = true;

        } else {
            isMessage = false;
        }
    }

    public List<Command> getCommands() {
        return commandList;
    }
}
