/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.commands;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.commands.Command;
import cow.milkgod.cheese.commands.commands.Bind;
import cow.milkgod.cheese.commands.commands.Coords;
import cow.milkgod.cheese.commands.commands.Damage;
import cow.milkgod.cheese.commands.commands.Enemy;
import cow.milkgod.cheese.commands.commands.Friends;
import cow.milkgod.cheese.commands.commands.Help;
import cow.milkgod.cheese.commands.commands.Legit;
import cow.milkgod.cheese.commands.commands.ListStuff;
import cow.milkgod.cheese.commands.commands.Prefix;
import cow.milkgod.cheese.commands.commands.Staff;
import cow.milkgod.cheese.commands.commands.Suicide;
import cow.milkgod.cheese.commands.commands.TabGui;
import cow.milkgod.cheese.commands.commands.Teleport;
import cow.milkgod.cheese.commands.commands.Theme;
import cow.milkgod.cheese.commands.commands.Toggle;
import cow.milkgod.cheese.commands.commands.Visible;
import cow.milkgod.cheese.commands.commands.getIP;
import cow.milkgod.cheese.commands.commands.getName;
import cow.milkgod.cheese.commands.commands.vClip;
import cow.milkgod.cheese.events.EventRenderChat;
import cow.milkgod.cheese.utils.Utils;
import cow.milkgod.cheese.utils.Wrapper;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;

public class CommandManager {
    public static ArrayList<Command> activeCommands = new ArrayList();

    public static void addCommand(Command c2) {
        activeCommands.add(c2);
    }

    public CommandManager() {
        EventManager.register(this);
        activeCommands.add(new getName());
        activeCommands.add(new getIP());
        activeCommands.add(new Toggle());
        activeCommands.add(new Bind());
        activeCommands.add(new ListStuff());
        activeCommands.add(new Theme());
        activeCommands.add(new Damage());
        activeCommands.add(new Friends());
        activeCommands.add(new vClip());
        activeCommands.add(new Help());
        activeCommands.add(new Visible());
        activeCommands.add(new Legit());
        activeCommands.add(new Prefix());
        activeCommands.add(new Enemy());
        activeCommands.add(new TabGui());
        activeCommands.add(new Coords());
        activeCommands.add(new Teleport());
        activeCommands.add(new Staff());
        activeCommands.add(new Suicide());
    }

    public static ArrayList<Command> getCommands() {
        return activeCommands;
    }

    public Command getCommandbyName(String name) {
        for (Command mod : CommandManager.getCommands()) {
            if (mod.getName() != name && !mod.getName().equalsIgnoreCase(name)) continue;
            return mod;
        }
        return null;
    }

    public Command getCommandbyAlias(String alias) {
        for (Command cmd2 : CommandManager.getCommands()) {
            String[] alias2 = cmd2.getAlias();
            int length = alias2.length;
            int i2 = 0;
            while (i2 < length) {
                String aliases = alias2[i2];
                Utils.addChatMessage(aliases);
                if (aliases.equalsIgnoreCase(alias)) {
                    return cmd2;
                }
                ++i2;
            }
        }
        return null;
    }

    @EventTarget
    public void event(EventRenderChat event) {
        if (event.getString().startsWith("-")) {
            String message = event.getString();
            String predictedCommand = "";
            for (Command command : activeCommands) {
                if (("-" + command.getName()).toLowerCase().startsWith(message.split(" ")[0].toLowerCase())) {
                    predictedCommand = String.valueOf(command.getName().toLowerCase()) + " " + command.getArguments() + " | " + command.getDescription();
                }
                if (command.getAlias() == null) continue;
                String[] arrstring = command.getAlias();
                int n = arrstring.length;
                int n2 = 0;
                while (n2 < n) {
                    String alias = arrstring[n2];
                    if (("-" + alias).toLowerCase().startsWith(message.split(" ")[0].toLowerCase())) {
                        predictedCommand = String.valueOf(alias.toLowerCase()) + " " + command.getArguments() + " | " + command.getDescription();
                    }
                    ++n2;
                }
            }
            if (!(predictedCommand = "-" + predictedCommand).equalsIgnoreCase("") && !message.contains(" ")) {
                ScaledResolution scaledRes = new ScaledResolution(Wrapper.getMinecraft(), Wrapper.getMinecraft().displayWidth, Wrapper.getMinecraft().displayHeight);
                int y2 = scaledRes.getScaledHeight() - 12;
                Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(predictedCommand, 4.0f, y2, -8355712);
            }
        }
    }
}

