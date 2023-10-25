/*
 * Decompiled with CFR 0.150.
 */
package baritone.command.defaults;

import baritone.api.IBaritone;
import baritone.api.cache.IWaypoint;
import baritone.api.cache.Waypoint;
import baritone.api.command.Command;
import baritone.api.command.IBaritoneChatControl;
import baritone.api.command.argument.IArgConsumer;
import baritone.api.command.datatypes.ForWaypoints;
import baritone.api.command.datatypes.RelativeBlockPos;
import baritone.api.command.exception.CommandException;
import baritone.api.command.exception.CommandInvalidStateException;
import baritone.api.command.exception.CommandInvalidTypeException;
import baritone.api.command.helpers.Paginator;
import baritone.api.command.helpers.TabCompleteHelper;
import baritone.api.pathing.goals.GoalBlock;
import baritone.api.utils.BetterBlockPos;
import java.lang.invoke.LambdaMetafactory;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class WaypointsCommand
extends Command {
    public WaypointsCommand(IBaritone baritone) {
        super(baritone, "waypoints", "waypoint", "wp");
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    @Override
    public void execute(String label, IArgConsumer args) throws CommandException {
        v0 = action = args.hasAny() != false ? Action.getByName(args.getString()) : Action.LIST;
        if (action == null) {
            throw new CommandInvalidTypeException(args.consumed(), "an action");
        }
        toComponent = (BiFunction<IWaypoint, Action, ITextComponent>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;, lambda$execute$0(java.lang.String baritone.api.cache.IWaypoint baritone.command.defaults.WaypointsCommand$Action ), (Lbaritone/api/cache/IWaypoint;Lbaritone/command/defaults/WaypointsCommand$Action;)Lnet/minecraft/util/text/ITextComponent;)((String)label);
        transform = (Function<IWaypoint, ITextComponent>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Ljava/lang/Object;, lambda$execute$1(java.util.function.BiFunction baritone.command.defaults.WaypointsCommand$Action baritone.api.cache.IWaypoint ), (Lbaritone/api/cache/IWaypoint;)Lnet/minecraft/util/text/ITextComponent;)(toComponent, (Action)action);
        if (action == Action.LIST) {
            v1 = tag = args.hasAny() != false ? IWaypoint.Tag.getByName(args.peekString()) : null;
            if (tag != null) {
                args.get();
            }
            v2 = waypoints = tag != null ? ForWaypoints.getWaypointsByTag(this.baritone, tag) : ForWaypoints.getWaypoints(this.baritone);
            if (waypoints.length > 0) {
                args.requireMax(1);
                Paginator.paginate(args, waypoints, (Runnable)LambdaMetafactory.metafactory(null, null, null, ()V, lambda$execute$2(baritone.api.cache.IWaypoint$Tag ), ()V)((WaypointsCommand)this, (IWaypoint.Tag)tag), transform, String.format("%s%s %s%s", new Object[]{IBaritoneChatControl.FORCE_COMMAND_PREFIX, label, Action.access$000(action)[0], tag != null ? " " + tag.getName() : ""}));
                return;
            }
            args.requireMax(0);
            if (tag != null) {
                v3 = "No waypoints found by that tag";
                throw new CommandInvalidStateException(v3);
            }
            v3 = "No waypoints found";
            throw new CommandInvalidStateException(v3);
        }
        if (action == Action.SAVE) {
            tag = IWaypoint.Tag.getByName(args.getString());
            if (tag == null) {
                throw new CommandInvalidStateException(String.format("'%s' is not a tag ", new Object[]{args.consumedString()}));
            }
            name = args.hasAny() != false ? args.getString() : "";
            pos = args.hasAny() != false ? (BetterBlockPos)args.getDatatypePost(RelativeBlockPos.INSTANCE, this.ctx.playerFeet()) : this.ctx.playerFeet();
            args.requireMax(0);
            waypoint = new Waypoint(name, tag, pos);
            ForWaypoints.waypoints(this.baritone).addWaypoint(waypoint);
            component = new TextComponentString("Waypoint added: ");
            component.getStyle().setColor(TextFormatting.GRAY);
            component.appendSibling(toComponent.apply(waypoint, Action.INFO));
            this.logDirect(new ITextComponent[]{component});
            return;
        }
        if (action == Action.CLEAR) {
            args.requireMax(1);
            tag = IWaypoint.Tag.getByName(args.getString());
            pos = waypoints = ForWaypoints.getWaypointsByTag(this.baritone, tag);
            waypoint = pos.length;
            component = 0;
            while (true) {
                if (component >= waypoint) {
                    this.logDirect(String.format("Cleared %d waypoints", new Object[]{waypoints.length}));
                    return;
                }
                waypoint = pos[component];
                ForWaypoints.waypoints(this.baritone).removeWaypoint(waypoint);
                ++component;
            }
        }
        waypoints = (IWaypoint[])args.getDatatypeFor(ForWaypoints.INSTANCE);
        waypoint = null;
        if (!args.hasAny() || !args.peekString().equals("@")) {
            switch (waypoints.length) {
                case 0: {
                    throw new CommandInvalidStateException("No waypoints found");
                }
                case 1: {
                    waypoint = waypoints[0];
                    ** break;
                }
            }
        } else {
            args.requireExactly(2);
            args.get();
            timestamp = args.getAs(Long.class);
            for (IWaypoint iWaypoint : waypoints) {
                if (iWaypoint.getCreationTimestamp() != timestamp) continue;
                waypoint = iWaypoint;
                break;
            }
            if (waypoint == null) {
                throw new CommandInvalidStateException("Timestamp was specified but no waypoint was found");
            }
        }
lbl72:
        // 4 sources

        if (waypoint == null) {
            args.requireMax(1);
            Paginator.paginate(args, waypoints, (Runnable)LambdaMetafactory.metafactory(null, null, null, ()V, lambda$execute$3(), ()V)((WaypointsCommand)this), transform, String.format("%s%s %s %s", new Object[]{IBaritoneChatControl.FORCE_COMMAND_PREFIX, label, Action.access$000(action)[0], args.consumedString()}));
            return;
        }
        if (action == Action.INFO) {
            this.logDirect(new ITextComponent[]{transform.apply(waypoint)});
            this.logDirect(String.format("Position: %s", new Object[]{waypoint.getLocation()}));
            deleteComponent = new TextComponentString("Click to delete this waypoint");
            deleteComponent.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("%s%s delete %s @ %d", new Object[]{IBaritoneChatControl.FORCE_COMMAND_PREFIX, label, waypoint.getTag().getName(), waypoint.getCreationTimestamp()})));
            goalComponent = new TextComponentString("Click to set goal to this waypoint");
            goalComponent.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("%s%s goal %s @ %d", new Object[]{IBaritoneChatControl.FORCE_COMMAND_PREFIX, label, waypoint.getTag().getName(), waypoint.getCreationTimestamp()})));
            backComponent = new TextComponentString("Click to return to the waypoints list");
            backComponent.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("%s%s list", new Object[]{IBaritoneChatControl.FORCE_COMMAND_PREFIX, label})));
            this.logDirect(new ITextComponent[]{deleteComponent});
            this.logDirect(new ITextComponent[]{goalComponent});
            this.logDirect(new ITextComponent[]{backComponent});
            return;
        }
        if (action == Action.DELETE) {
            ForWaypoints.waypoints(this.baritone).removeWaypoint(waypoint);
            this.logDirect("That waypoint has successfully been deleted");
            return;
        }
        if (action == Action.GOAL) {
            goal = new GoalBlock(waypoint.getLocation());
            this.baritone.getCustomGoalProcess().setGoal(goal);
            this.logDirect(String.format("Goal: %s", new Object[]{goal}));
            return;
        }
        if (action != Action.GOTO) return;
        goal = new GoalBlock(waypoint.getLocation());
        this.baritone.getCustomGoalProcess().setGoalAndPath(goal);
        this.logDirect(String.format("Going to: %s", new Object[]{goal}));
    }

    @Override
    public Stream<String> tabComplete(String label, IArgConsumer args) throws CommandException {
        if (args.hasAny()) {
            if (args.hasExactlyOne()) {
                return new TabCompleteHelper().append(Action.getAllNames()).sortAlphabetically().filterPrefix(args.getString()).stream();
            }
            Action action = Action.getByName(args.getString());
            if (args.hasExactlyOne()) {
                if (action == Action.LIST || action == Action.SAVE || action == Action.CLEAR) {
                    return new TabCompleteHelper().append(IWaypoint.Tag.getAllNames()).sortAlphabetically().filterPrefix(args.getString()).stream();
                }
                return args.tabCompleteDatatype(ForWaypoints.INSTANCE);
            }
            if (args.has(3) && action == Action.SAVE) {
                args.get();
                args.get();
                return args.tabCompleteDatatype(RelativeBlockPos.INSTANCE);
            }
        }
        return Stream.empty();
    }

    @Override
    public String getShortDesc() {
        return "Manage waypoints";
    }

    @Override
    public List<String> getLongDesc() {
        return Arrays.asList("The waypoint command allows you to manage Baritone's waypoints.", "", "Waypoints can be used to mark positions for later. Waypoints are each given a tag and an optional name.", "", "Note that the info, delete, and goal commands let you specify a waypoint by tag. If there is more than one waypoint with a certain tag, then they will let you select which waypoint you mean.", "", "Usage:", "> wp [l/list] - List all waypoints.", "> wp <s/save> <tag> - Save your current position as an unnamed waypoint with the specified tag.", "> wp <s/save> <tag> <name> - Save the waypoint with the specified name.", "> wp <s/save> <tag> <name> <pos> - Save the waypoint with the specified name and position.", "> wp <i/info/show> <tag> - Show info on a waypoint by tag.", "> wp <d/delete> <tag> - Delete a waypoint by tag.", "> wp <g/goal> <tag> - Set a goal to a waypoint by tag.", "> wp <goto> <tag> - Set a goal to a waypoint by tag and start pathing.");
    }

    private /* synthetic */ void lambda$execute$3() {
        this.logDirect("Multiple waypoints were found:");
    }

    private /* synthetic */ void lambda$execute$2(IWaypoint.Tag tag) {
        this.logDirect(tag != null ? String.format("All waypoints by tag %s:", tag.name()) : "All waypoints:");
    }

    private static /* synthetic */ ITextComponent lambda$execute$1(BiFunction toComponent, Action action, IWaypoint waypoint) {
        return (ITextComponent)toComponent.apply(waypoint, action == Action.LIST ? Action.INFO : action);
    }

    private static /* synthetic */ ITextComponent lambda$execute$0(String label, IWaypoint waypoint, Action _action) {
        TextComponentString component = new TextComponentString("");
        TextComponentString tagComponent = new TextComponentString(waypoint.getTag().name() + " ");
        tagComponent.getStyle().setColor(TextFormatting.GRAY);
        String name = waypoint.getName();
        TextComponentString nameComponent = new TextComponentString(!name.isEmpty() ? name : "<empty>");
        nameComponent.getStyle().setColor(!name.isEmpty() ? TextFormatting.GRAY : TextFormatting.DARK_GRAY);
        TextComponentString timestamp = new TextComponentString(" @ " + new Date(waypoint.getCreationTimestamp()));
        timestamp.getStyle().setColor(TextFormatting.DARK_GRAY);
        component.appendSibling(tagComponent);
        component.appendSibling(nameComponent);
        component.appendSibling(timestamp);
        component.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("Click to select"))).setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("%s%s %s %s @ %d", IBaritoneChatControl.FORCE_COMMAND_PREFIX, label, _action.names[0], waypoint.getTag().getName(), waypoint.getCreationTimestamp())));
        return component;
    }

    private static enum Action {
        LIST("list", "get", "l"),
        CLEAR("clear", "c"),
        SAVE("save", "s"),
        INFO("info", "show", "i"),
        DELETE("delete", "d"),
        GOAL("goal", "g"),
        GOTO("goto");

        private final String[] names;

        private Action(String ... names) {
            this.names = names;
        }

        public static Action getByName(String name) {
            for (Action action : Action.values()) {
                for (String alias : action.names) {
                    if (!alias.equalsIgnoreCase(name)) continue;
                    return action;
                }
            }
            return null;
        }

        public static String[] getAllNames() {
            HashSet<String> names = new HashSet<String>();
            for (Action action : Action.values()) {
                names.addAll(Arrays.asList(action.names));
            }
            return names.toArray(new String[0]);
        }
    }
}

