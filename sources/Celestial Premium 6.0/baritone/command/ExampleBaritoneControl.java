/*
 * Decompiled with CFR 0.150.
 */
package baritone.command;

import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import baritone.api.Settings;
import baritone.api.command.IBaritoneChatControl;
import baritone.api.command.argument.ICommandArgument;
import baritone.api.command.exception.CommandNotEnoughArgumentsException;
import baritone.api.command.exception.CommandNotFoundException;
import baritone.api.command.helpers.TabCompleteHelper;
import baritone.api.command.manager.ICommandManager;
import baritone.api.event.events.ChatEvent;
import baritone.api.event.events.TabCompleteEvent;
import baritone.api.event.listener.AbstractGameEventListener;
import baritone.api.utils.Helper;
import baritone.api.utils.SettingsUtil;
import baritone.command.argument.ArgConsumer;
import baritone.command.argument.CommandArguments;
import baritone.command.manager.CommandManager;
import baritone.utils.accessor.IGuiScreen;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class ExampleBaritoneControl
implements Helper,
AbstractGameEventListener {
    private static final Settings settings = BaritoneAPI.getSettings();
    private final ICommandManager manager;

    public ExampleBaritoneControl(IBaritone baritone) {
        this.manager = baritone.getCommandManager();
        baritone.getGameEventHandler().registerEventListener(this);
    }

    @Override
    public void onSendChatMessage(ChatEvent event) {
        String msg = event.getMessage();
        String prefix = (String)ExampleBaritoneControl.settings.prefix.value;
        boolean forceRun = msg.startsWith(IBaritoneChatControl.FORCE_COMMAND_PREFIX);
        if (((Boolean)ExampleBaritoneControl.settings.prefixControl.value).booleanValue() && msg.startsWith(prefix) || forceRun) {
            event.cancel();
            String commandStr = msg.substring(forceRun ? IBaritoneChatControl.FORCE_COMMAND_PREFIX.length() : prefix.length());
            if (!this.runCommand(commandStr) && !commandStr.trim().isEmpty()) {
                new CommandNotFoundException(CommandManager.expand(commandStr).getFirst()).handle(null, null);
            }
        } else if ((((Boolean)ExampleBaritoneControl.settings.chatControl.value).booleanValue() || ((Boolean)ExampleBaritoneControl.settings.chatControlAnyway.value).booleanValue()) && this.runCommand(msg)) {
            event.cancel();
        }
    }

    private void logRanCommand(String command, String rest) {
        if (((Boolean)ExampleBaritoneControl.settings.echoCommands.value).booleanValue()) {
            String msg = command + rest;
            String toDisplay = (Boolean)ExampleBaritoneControl.settings.censorRanCommands.value != false ? command + " ..." : msg;
            TextComponentString component = new TextComponentString(String.format("> %s", toDisplay));
            component.getStyle().setColor(TextFormatting.WHITE).setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("Click to rerun command"))).setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, IBaritoneChatControl.FORCE_COMMAND_PREFIX + msg));
            this.logDirect(component);
        }
    }

    public boolean runCommand(String msg) {
        if (msg.trim().equalsIgnoreCase("damn")) {
            this.logDirect("daniel");
            return false;
        }
        if (msg.trim().equalsIgnoreCase("orderpizza")) {
            try {
                ((IGuiScreen)((Object)ExampleBaritoneControl.mc.currentScreen)).openLink(new URI("https://www.dominos.com/en/pages/order/"));
            }
            catch (NullPointerException | URISyntaxException exception) {
                // empty catch block
            }
            return false;
        }
        if (msg.isEmpty()) {
            return this.runCommand("help");
        }
        Tuple<String, List<ICommandArgument>> pair = CommandManager.expand(msg);
        String command = pair.getFirst();
        String rest = msg.substring(pair.getFirst().length());
        ArgConsumer argc = new ArgConsumer(this.manager, pair.getSecond());
        if (!argc.hasAny()) {
            Settings.Setting<?> setting = ExampleBaritoneControl.settings.byLowerName.get(command.toLowerCase(Locale.US));
            if (setting != null) {
                this.logRanCommand(command, rest);
                if (setting.getValueClass() == Boolean.class) {
                    this.manager.execute(String.format("set toggle %s", setting.getName()));
                } else {
                    this.manager.execute(String.format("set %s", setting.getName()));
                }
                return true;
            }
        } else if (argc.hasExactlyOne()) {
            for (Settings.Setting<?> setting : ExampleBaritoneControl.settings.allSettings) {
                if (setting.getName().equals("logger") || !setting.getName().equalsIgnoreCase(pair.getFirst())) continue;
                this.logRanCommand(command, rest);
                try {
                    this.manager.execute(String.format("set %s %s", setting.getName(), argc.getString()));
                }
                catch (CommandNotEnoughArgumentsException commandNotEnoughArgumentsException) {
                    // empty catch block
                }
                return true;
            }
        }
        if (this.manager.getCommand(pair.getFirst()) != null) {
            this.logRanCommand(command, rest);
        }
        return this.manager.execute(pair);
    }

    @Override
    public void onPreTabComplete(TabCompleteEvent event) {
        if (!((Boolean)ExampleBaritoneControl.settings.prefixControl.value).booleanValue()) {
            return;
        }
        String prefix = event.prefix;
        String commandPrefix = (String)ExampleBaritoneControl.settings.prefix.value;
        if (!prefix.startsWith(commandPrefix)) {
            return;
        }
        String msg = prefix.substring(commandPrefix.length());
        List<ICommandArgument> args = CommandArguments.from(msg, true);
        Stream<String> stream = this.tabComplete(msg);
        if (args.size() == 1) {
            stream = stream.map(x -> commandPrefix + x);
        }
        event.completions = (String[])stream.toArray(String[]::new);
    }

    public Stream<String> tabComplete(String msg) {
        try {
            List<ICommandArgument> args = CommandArguments.from(msg, true);
            ArgConsumer argc = new ArgConsumer(this.manager, args);
            if (argc.hasAtMost(2)) {
                if (argc.hasExactly(1)) {
                    return new TabCompleteHelper().addCommands(this.manager).addSettings().filterPrefix(argc.getString()).stream();
                }
                Settings.Setting<?> setting = ExampleBaritoneControl.settings.byLowerName.get(argc.getString().toLowerCase(Locale.US));
                if (setting != null) {
                    if (setting.getValueClass() == Boolean.class) {
                        TabCompleteHelper helper = new TabCompleteHelper();
                        if (((Boolean)setting.value).booleanValue()) {
                            helper.append("true", "false");
                        } else {
                            helper.append("false", "true");
                        }
                        return helper.filterPrefix(argc.getString()).stream();
                    }
                    return Stream.of(SettingsUtil.settingValueToString(setting));
                }
            }
            return this.manager.tabComplete(msg);
        }
        catch (CommandNotEnoughArgumentsException ignored) {
            return Stream.empty();
        }
    }
}

