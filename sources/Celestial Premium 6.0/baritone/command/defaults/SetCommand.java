/*
 * Decompiled with CFR 0.150.
 */
package baritone.command.defaults;

import baritone.Baritone;
import baritone.api.IBaritone;
import baritone.api.Settings;
import baritone.api.command.Command;
import baritone.api.command.IBaritoneChatControl;
import baritone.api.command.argument.IArgConsumer;
import baritone.api.command.exception.CommandException;
import baritone.api.command.exception.CommandInvalidTypeException;
import baritone.api.command.helpers.Paginator;
import baritone.api.command.helpers.TabCompleteHelper;
import baritone.api.utils.SettingsUtil;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class SetCommand
extends Command {
    public SetCommand(IBaritone baritone) {
        super(baritone, "set", "setting", "settings");
    }

    @Override
    public void execute(String label, IArgConsumer args) throws CommandException {
        boolean doingSomething;
        boolean paginate;
        String arg = args.hasAny() ? args.getString().toLowerCase(Locale.US) : "list";
        if (Arrays.asList("s", "save").contains(arg)) {
            SettingsUtil.save(Baritone.settings());
            this.logDirect("Settings saved");
            return;
        }
        boolean viewModified = Arrays.asList("m", "mod", "modified").contains(arg);
        boolean viewAll = Arrays.asList("all", "l", "list").contains(arg);
        boolean bl = paginate = viewModified || viewAll;
        if (paginate) {
            String search = args.hasAny() && args.peekAsOrNull(Integer.class) == null ? args.getString() : "";
            args.requireMax(1);
            List toPaginate = (viewModified ? SettingsUtil.modifiedSettings(Baritone.settings()) : Baritone.settings().allSettings).stream().filter(s -> !s.getName().equals("logger")).filter(s -> s.getName().toLowerCase(Locale.US).contains(search.toLowerCase(Locale.US))).sorted((s1, s2) -> String.CASE_INSENSITIVE_ORDER.compare(s1.getName(), s2.getName())).collect(Collectors.toList());
            Paginator.paginate(args, new Paginator(toPaginate), () -> this.logDirect(!search.isEmpty() ? String.format("All %ssettings containing the string '%s':", viewModified ? "modified " : "", search) : String.format("All %ssettings:", viewModified ? "modified " : "")), setting -> {
                TextComponentString typeComponent = new TextComponentString(String.format(" (%s)", SettingsUtil.settingTypeToString(setting)));
                typeComponent.getStyle().setColor(TextFormatting.DARK_GRAY);
                TextComponentString hoverComponent = new TextComponentString("");
                hoverComponent.getStyle().setColor(TextFormatting.GRAY);
                hoverComponent.appendText(setting.getName());
                hoverComponent.appendText(String.format("\nType: %s", SettingsUtil.settingTypeToString(setting)));
                hoverComponent.appendText(String.format("\n\nValue:\n%s", SettingsUtil.settingValueToString(setting)));
                hoverComponent.appendText(String.format("\n\nDefault Value:\n%s", SettingsUtil.settingDefaultToString(setting)));
                String commandSuggestion = (String)Baritone.settings().prefix.value + String.format("set %s ", setting.getName());
                TextComponentString component = new TextComponentString(setting.getName());
                component.getStyle().setColor(TextFormatting.GRAY);
                component.appendSibling(typeComponent);
                component.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverComponent)).setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, commandSuggestion));
                return component;
            }, IBaritoneChatControl.FORCE_COMMAND_PREFIX + "set " + arg + " " + search);
            return;
        }
        args.requireMax(1);
        boolean resetting = arg.equalsIgnoreCase("reset");
        boolean toggling = arg.equalsIgnoreCase("toggle");
        boolean bl2 = doingSomething = resetting || toggling;
        if (resetting) {
            if (!args.hasAny()) {
                this.logDirect("Please specify 'all' as an argument to reset to confirm you'd really like to do this");
                this.logDirect("ALL settings will be reset. Use the 'set modified' or 'modified' commands to see what will be reset.");
                this.logDirect("Specify a setting name instead of 'all' to only reset one setting");
            } else if (args.peekString().equalsIgnoreCase("all")) {
                SettingsUtil.modifiedSettings(Baritone.settings()).forEach(Settings.Setting::reset);
                this.logDirect("All settings have been reset to their default values");
                SettingsUtil.save(Baritone.settings());
                return;
            }
        }
        if (toggling) {
            args.requireMin(1);
        }
        String settingName = doingSomething ? args.getString() : arg;
        Settings.Setting setting2 = Baritone.settings().allSettings.stream().filter(s -> s.getName().equalsIgnoreCase(settingName)).findFirst().orElse(null);
        if (setting2 == null) {
            throw new CommandInvalidTypeException(args.consumed(), "a valid setting");
        }
        if (!doingSomething && !args.hasAny()) {
            this.logDirect(String.format("Value of setting %s:", setting2.getName()));
            this.logDirect(SettingsUtil.settingValueToString(setting2));
        } else {
            String oldValue = SettingsUtil.settingValueToString(setting2);
            if (resetting) {
                setting2.reset();
            } else if (toggling) {
                if (setting2.getValueClass() != Boolean.class) {
                    throw new CommandInvalidTypeException(args.consumed(), "a toggleable setting", "some other setting");
                }
                Settings.Setting setting3 = setting2;
                setting3.value = (Boolean)setting3.value ^ true;
                this.logDirect(String.format("Toggled setting %s to %s", setting2.getName(), Boolean.toString((Boolean)setting2.value)));
            } else {
                String newValue = args.getString();
                try {
                    SettingsUtil.parseAndApply(Baritone.settings(), arg, newValue);
                }
                catch (Throwable t) {
                    t.printStackTrace();
                    throw new CommandInvalidTypeException(args.consumed(), "a valid value", t);
                }
            }
            if (!toggling) {
                this.logDirect(String.format("Successfully %s %s to %s", resetting ? "reset" : "set", setting2.getName(), SettingsUtil.settingValueToString(setting2)));
            }
            TextComponentString oldValueComponent = new TextComponentString(String.format("Old value: %s", oldValue));
            oldValueComponent.getStyle().setColor(TextFormatting.GRAY).setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("Click to set the setting back to this value"))).setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, IBaritoneChatControl.FORCE_COMMAND_PREFIX + String.format("set %s %s", setting2.getName(), oldValue)));
            this.logDirect(oldValueComponent);
            if (setting2.getName().equals("chatControl") && !((Boolean)setting2.value).booleanValue() && !((Boolean)Baritone.settings().chatControlAnyway.value).booleanValue() || setting2.getName().equals("chatControlAnyway") && !((Boolean)setting2.value).booleanValue() && !((Boolean)Baritone.settings().chatControl.value).booleanValue()) {
                this.logDirect("Warning: Chat commands will no longer work. If you want to revert this change, use prefix control (if enabled) or click the old value listed above.", TextFormatting.RED);
            } else if (setting2.getName().equals("prefixControl") && !((Boolean)setting2.value).booleanValue()) {
                this.logDirect("Warning: Prefixed commands will no longer work. If you want to revert this change, use chat control (if enabled) or click the old value listed above.", TextFormatting.RED);
            }
        }
        SettingsUtil.save(Baritone.settings());
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public Stream<String> tabComplete(String label, IArgConsumer args) throws CommandException {
        if (!args.hasAny()) return Stream.empty();
        String arg = args.getString();
        if (args.hasExactlyOne()) {
            if (!Arrays.asList("s", "save").contains(args.peekString().toLowerCase(Locale.US))) {
                if (arg.equalsIgnoreCase("reset")) {
                    return new TabCompleteHelper().addModifiedSettings().prepend("all").filterPrefix(args.getString()).stream();
                }
                if (arg.equalsIgnoreCase("toggle")) {
                    return new TabCompleteHelper().addToggleableSettings().filterPrefix(args.getString()).stream();
                }
                Settings.Setting<?> setting = Baritone.settings().byLowerName.get(arg.toLowerCase(Locale.US));
                if (setting == null) return Stream.empty();
                if (setting.getType() != Boolean.class) return Stream.of(SettingsUtil.settingValueToString(setting));
                TabCompleteHelper helper = new TabCompleteHelper();
                if (((Boolean)setting.value).booleanValue()) {
                    helper.append("true", "false");
                    return helper.filterPrefix(args.getString()).stream();
                } else {
                    helper.append("false", "true");
                }
                return helper.filterPrefix(args.getString()).stream();
            }
        }
        if (args.hasAny()) return Stream.empty();
        return new TabCompleteHelper().addSettings().sortAlphabetically().prepend("list", "modified", "reset", "toggle", "save").filterPrefix(arg).stream();
    }

    @Override
    public String getShortDesc() {
        return "View or change settings";
    }

    @Override
    public List<String> getLongDesc() {
        return Arrays.asList("Using the set command, you can manage all of Baritone's settings. Almost every aspect is controlled by these settings - go wild!", "", "Usage:", "> set - Same as `set list`", "> set list [page] - View all settings", "> set modified [page] - View modified settings", "> set <setting> - View the current value of a setting", "> set <setting> <value> - Set the value of a setting", "> set reset all - Reset ALL SETTINGS to their defaults", "> set reset <setting> - Reset a setting to its default", "> set toggle <setting> - Toggle a boolean setting", "> set save - Save all settings (this is automatic tho)");
    }
}

