// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.command.commands;

import net.minecraft.util.math.BlockPos;
import java.util.UUID;
import com.elementars.eclient.module.render.Waypoints;
import com.elementars.eclient.command.Command;

public class WaypointCommand extends Command
{
    public WaypointCommand() {
        super("waypoints", "Manages Waypoints", new String[] { "add", "remove" });
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length < 2) {
            Command.sendChatMessage("Use .waypoints help to see commands");
            return;
        }
        if (!args[1].equalsIgnoreCase("add")) {
            if (args[1].equalsIgnoreCase("remove")) {
                if (args.length < 3) {
                    Command.sendChatMessage("Please specify the name of the waypoint (.waypoints remove name)");
                    return;
                }
                Waypoints.WAYPOINTS.removeIf(waypoint -> waypoint.getName().equalsIgnoreCase(args[2]));
                Command.sendChatMessage("Removed waypoint(s) with the name: " + args[2]);
            }
            return;
        }
        if (args.length < 3) {
            Command.sendChatMessage("Please specify the name of the waypoint (.waypoints add name (X) (Y) (Z))");
            return;
        }
        if (args.length < 6) {
            Command.sendChatMessage("Please specify coordinates (.waypoints add name (X) (Y) (Z))");
            return;
        }
        final int x = (int)Double.parseDouble(args[3]);
        final int y = (int)Double.parseDouble(args[4]);
        final int z = (int)Double.parseDouble(args[5]);
        Waypoints.WAYPOINTS.add(new Waypoints.Waypoint(UUID.randomUUID(), args[2], new BlockPos(x, y, z), WaypointCommand.mc.player.getEntityBoundingBox(), WaypointCommand.mc.player.dimension));
        Command.sendChatMessage("Added waypoint with the name: " + args[2]);
    }
}
