package me.aquavit.liquidsense.command.commands;

import me.aquavit.liquidsense.module.modules.client.Target;
import me.aquavit.liquidsense.command.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TargetCommand extends Command {
    public TargetCommand() {
        super("targetsettings", "ts");
    }

    @Override
    public void execute(final String[] args) {
        if (args.length > 1) {
            String arg = args[1].toLowerCase();
            switch (arg) {
                case "players":
                    Target.player.set(!Target.player.get());
                    chat("§7Target players toggled " + (Target.player.get() ? "on" : "off") + '.');
                    break;
                case "mobs":
                    Target.mob.set(!Target.mob.get());
                    chat("§7Target mobs toggled " + (Target.mob.get() ? "on" : "off") + '.');
                    break;
                case "animals":
                    Target.animal.set(!Target.animal.get());
                    chat("§7Target animals toggled " + (Target.animal.get() ? "on" : "off") + '.');
                    break;
                case "invisible":
                    Target.invisible.set(!Target.invisible.get());
                    chat("§7Target Invisible toggled " + (Target.invisible.get() ? "on" : "off") + '.');
                    break;
                case "dead":
                    Target.dead.set(!Target.dead.get());
                    chat("§7Target dead toggled " + (Target.dead.get() ? "on" : "off") + '.');
                    break;
                default:
                    chatSyntax("targetsettings <players/mobs/animals/invisible/dead>");
                    break;
            }
            playEdit();
            return;

        }
        chatSyntax("targetsettings <players/mobs/animals/invisible/dead>");

    }

    @Override
    public List<String> tabComplete(String[] args) {
        if (args.length == 0) return new ArrayList<>();

        if (args.length == 1) {
            return Arrays.stream(new String[]{"players", "mobs", "animals", "invisible", "dead"})
                    .filter(it -> it.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
