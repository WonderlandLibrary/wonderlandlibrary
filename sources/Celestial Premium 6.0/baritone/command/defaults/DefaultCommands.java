/*
 * Decompiled with CFR 0.150.
 */
package baritone.command.defaults;

import baritone.api.IBaritone;
import baritone.api.command.Command;
import baritone.api.command.ICommand;
import baritone.command.defaults.AxisCommand;
import baritone.command.defaults.BlacklistCommand;
import baritone.command.defaults.BuildCommand;
import baritone.command.defaults.ChestsCommand;
import baritone.command.defaults.ClickCommand;
import baritone.command.defaults.ComeCommand;
import baritone.command.defaults.CommandAlias;
import baritone.command.defaults.ETACommand;
import baritone.command.defaults.ExecutionControlCommands;
import baritone.command.defaults.ExploreCommand;
import baritone.command.defaults.ExploreFilterCommand;
import baritone.command.defaults.FarmCommand;
import baritone.command.defaults.FindCommand;
import baritone.command.defaults.FollowCommand;
import baritone.command.defaults.ForceCancelCommand;
import baritone.command.defaults.GcCommand;
import baritone.command.defaults.GoalCommand;
import baritone.command.defaults.GotoCommand;
import baritone.command.defaults.HelpCommand;
import baritone.command.defaults.InvertCommand;
import baritone.command.defaults.MineCommand;
import baritone.command.defaults.PathCommand;
import baritone.command.defaults.ProcCommand;
import baritone.command.defaults.ReloadAllCommand;
import baritone.command.defaults.RenderCommand;
import baritone.command.defaults.RepackCommand;
import baritone.command.defaults.SaveAllCommand;
import baritone.command.defaults.SchematicaCommand;
import baritone.command.defaults.SelCommand;
import baritone.command.defaults.SetCommand;
import baritone.command.defaults.SurfaceCommand;
import baritone.command.defaults.ThisWayCommand;
import baritone.command.defaults.TunnelCommand;
import baritone.command.defaults.VersionCommand;
import baritone.command.defaults.WaypointsCommand;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class DefaultCommands {
    private DefaultCommands() {
    }

    public static List<ICommand> createAll(IBaritone baritone) {
        Objects.requireNonNull(baritone);
        ArrayList<Command> commands = new ArrayList<Command>(Arrays.asList(new HelpCommand(baritone), new SetCommand(baritone), new CommandAlias(baritone, Arrays.asList("modified", "mod", "baritone", "modifiedsettings"), "List modified settings", "set modified"), new CommandAlias(baritone, "reset", "Reset all settings or just one", "set reset"), new GoalCommand(baritone), new GotoCommand(baritone), new PathCommand(baritone), new ProcCommand(baritone), new ETACommand(baritone), new VersionCommand(baritone), new RepackCommand(baritone), new BuildCommand(baritone), new SchematicaCommand(baritone), new ComeCommand(baritone), new AxisCommand(baritone), new ForceCancelCommand(baritone), new GcCommand(baritone), new InvertCommand(baritone), new TunnelCommand(baritone), new RenderCommand(baritone), new FarmCommand(baritone), new ChestsCommand(baritone), new FollowCommand(baritone), new ExploreFilterCommand(baritone), new ReloadAllCommand(baritone), new SaveAllCommand(baritone), new ExploreCommand(baritone), new BlacklistCommand(baritone), new FindCommand(baritone), new MineCommand(baritone), new ClickCommand(baritone), new SurfaceCommand(baritone), new ThisWayCommand(baritone), new WaypointsCommand(baritone), new CommandAlias(baritone, "sethome", "Sets your home waypoint", "waypoints save home"), new CommandAlias(baritone, "home", "Path to your home waypoint", "waypoints goto home"), new SelCommand(baritone)));
        ExecutionControlCommands prc = new ExecutionControlCommands(baritone);
        commands.add(prc.pauseCommand);
        commands.add(prc.resumeCommand);
        commands.add(prc.pausedCommand);
        commands.add(prc.cancelCommand);
        return Collections.unmodifiableList(commands);
    }
}

