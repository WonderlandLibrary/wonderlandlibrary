/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.command.datatypes;

import baritone.api.IBaritone;
import baritone.api.cache.IWaypoint;
import baritone.api.cache.IWaypointCollection;
import baritone.api.command.datatypes.IDatatypeContext;
import baritone.api.command.datatypes.IDatatypeFor;
import baritone.api.command.exception.CommandException;
import baritone.api.command.helpers.TabCompleteHelper;
import java.util.Comparator;
import java.util.stream.Stream;

public enum ForWaypoints implements IDatatypeFor<IWaypoint[]>
{
    INSTANCE;


    @Override
    public IWaypoint[] get(IDatatypeContext ctx) throws CommandException {
        String input = ctx.getConsumer().getString();
        IWaypoint.Tag tag = IWaypoint.Tag.getByName(input);
        return tag == null ? ForWaypoints.getWaypointsByName(ctx.getBaritone(), input) : ForWaypoints.getWaypointsByTag(ctx.getBaritone(), tag);
    }

    @Override
    public Stream<String> tabComplete(IDatatypeContext ctx) throws CommandException {
        return new TabCompleteHelper().append(ForWaypoints.getWaypointNames(ctx.getBaritone())).sortAlphabetically().prepend(IWaypoint.Tag.getAllNames()).filterPrefix(ctx.getConsumer().getString()).stream();
    }

    public static IWaypointCollection waypoints(IBaritone baritone) {
        return baritone.getWorldProvider().getCurrentWorld().getWaypoints();
    }

    public static IWaypoint[] getWaypoints(IBaritone baritone) {
        return (IWaypoint[])ForWaypoints.waypoints(baritone).getAllWaypoints().stream().sorted(Comparator.comparingLong(IWaypoint::getCreationTimestamp).reversed()).toArray(IWaypoint[]::new);
    }

    public static String[] getWaypointNames(IBaritone baritone) {
        return (String[])Stream.of(ForWaypoints.getWaypoints(baritone)).map(IWaypoint::getName).filter(name -> !name.isEmpty()).toArray(String[]::new);
    }

    public static IWaypoint[] getWaypointsByTag(IBaritone baritone, IWaypoint.Tag tag) {
        return (IWaypoint[])ForWaypoints.waypoints(baritone).getByTag(tag).stream().sorted(Comparator.comparingLong(IWaypoint::getCreationTimestamp).reversed()).toArray(IWaypoint[]::new);
    }

    public static IWaypoint[] getWaypointsByName(IBaritone baritone, String name) {
        return (IWaypoint[])Stream.of(ForWaypoints.getWaypoints(baritone)).filter(waypoint -> waypoint.getName().equalsIgnoreCase(name)).toArray(IWaypoint[]::new);
    }
}

