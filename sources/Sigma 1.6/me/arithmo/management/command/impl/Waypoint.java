/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.management.command.impl;

import java.util.List;
import me.arithmo.event.Event;
import me.arithmo.management.command.Command;
import me.arithmo.management.waypoints.WaypointManager;
import me.arithmo.util.misc.ChatUtil;
import me.arithmo.util.render.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.Vec3;

public class Waypoint
extends Command {
    public Waypoint(String[] names, String description) {
        super(names, description);
    }

    @Override
    public void fire(String[] args) {
        if (args == null) {
            this.printUsage();
            return;
        }
        if (args.length > 1) {
            if (args[0].equalsIgnoreCase("d") || args[0].equalsIgnoreCase("del")) {
                if (args.length == 2) {
                    for (me.arithmo.management.waypoints.Waypoint waypoint : WaypointManager.getManager().getWaypoints()) {
                        if (!waypoint.getName().equalsIgnoreCase(args[1])) continue;
                        WaypointManager.getManager().deleteWaypoint(waypoint);
                        ChatUtil.printChat("\u00a7c[\u00a7fS\u00a7c]\u00a77 \u00a77Waypoint \u00a7c" + args[1] + "\u00a77 has been removed.");
                        return;
                    }
                    ChatUtil.printChat("\u00a7c[\u00a7fS\u00a7c]\u00a77 \u00a77No Waypoint under the name \u00a7c" + args[1] + "\u00a77 was found.");
                    return;
                }
                this.printUsage();
                return;
            }
            if (args[0].equalsIgnoreCase("a") || args[0].equalsIgnoreCase("add")) {
                if (args.length == 2) {
                    if (!WaypointManager.getManager().containsName(args[1])) {
                        int color = Colors.getColor((int)(255.0 * Math.random()), (int)(255.0 * Math.random()), (int)(255.0 * Math.random()));
                        WaypointManager.getManager().createWaypoint(args[1], new Vec3(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 1.0, this.mc.thePlayer.posZ), color, this.mc.getCurrentServerData().serverIP);
                        ChatUtil.printChat("\u00a7c[\u00a7fS\u00a7c]\u00a77 \u00a77Waypoint \u00a7c" + args[1] + "\u00a77 has been successfully created.");
                        return;
                    }
                    ChatUtil.printChat("\u00a7c[\u00a7fS\u00a7c]\u00a77 \u00a77Waypoint \u00a7c" + args[1] + "\u00a77already exists.");
                    this.printUsage();
                    return;
                }
                if (args.length == 5) {
                    if (!WaypointManager.getManager().containsName(args[1])) {
                        int color = Colors.getColor((int)(255.0 * Math.random()), (int)(255.0 * Math.random()), (int)(255.0 * Math.random()));
                        WaypointManager.getManager().createWaypoint(args[1], new Vec3(Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4])), color, this.mc.getCurrentServerData().serverIP);
                        ChatUtil.printChat("\u00a7c[\u00a7fS\u00a7c]\u00a77 \u00a77Waypoint \u00a7c" + args[1] + " \u00a77has been successfully created.");
                        return;
                    }
                    ChatUtil.printChat("\u00a7c[\u00a7fS\u00a7c]\u00a77 \u00a77Waypoint \u00a7c" + args[1] + " \u00a77already exists.");
                    this.printUsage();
                    return;
                }
                this.printUsage();
                return;
            }
        } else {
            this.printUsage();
            return;
        }
    }

    public void onEvent(Event event) {
    }

    @Override
    public String getUsage() {
        return "add/del <name> (add only <x> <y> <z>)";
    }
}

