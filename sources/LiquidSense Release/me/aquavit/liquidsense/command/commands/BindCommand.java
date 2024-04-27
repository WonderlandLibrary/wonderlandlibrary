package me.aquavit.liquidsense.command.commands;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.command.Command;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.ui.client.hud.element.elements.extend.ColorType;
import me.aquavit.liquidsense.ui.client.hud.element.elements.extend.Notification;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BindCommand extends Command {
    public BindCommand() {
        super("bind");
    }

    @Override
    public void execute(final String[] args) {
        if (args.length <= 2) {
            chatSyntax(new String[] { "<module> <key>", "<module> none" });
            return;
        }
        final Module module = LiquidSense.moduleManager.getModule(args[1]);
        if (module == null) {
            chat("Module §a§l" + args[1] + "§3 not found.");
            return;
        }
        final int key = Keyboard.getKeyIndex(args[2].toUpperCase());
        module.setKeyBind(key);
        chat("Bound module §a§l" + module.getName() + "§3 to key §a§l" + Keyboard.getKeyName(key) + "§3.");
        LiquidSense.hud.addNotification(new Notification("Bound " + module.getName() + " to " + Keyboard.getKeyName(key),"", ColorType.INFO,1500,500));
        playEdit();
    }

    @Override
    public List<String> tabComplete(String[] args) {
        if (args.length == 0) return new ArrayList<>();

        String moduleName = args[0];

        if (args.length == 1) {
            return LiquidSense.moduleManager.getModules().stream()
                    .map(Module::getName)
                    .filter(module -> module.toLowerCase().startsWith(moduleName.toLowerCase()))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
