/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.management.waypoints;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import me.arithmo.management.waypoints.Waypoint;
import me.arithmo.util.FileUtils;
import net.minecraft.util.Vec3;

public class WaypointManager {
    private static WaypointManager waypointManager;
    private List<Waypoint> waypoints = new CopyOnWriteArrayList<Waypoint>();
    private final File WAYPOINT_DIR = FileUtils.getConfigFile("Waypoints");

    public WaypointManager() {
        waypointManager = this;
    }

    public void loadWaypoints() {
        try {
            List<String> fileContent = FileUtils.read(this.WAYPOINT_DIR);
            for (String line : fileContent) {
                String[] split = line.split(":");
                String waypointName = split[0];
                assert (split.length == 6);
                this.createWaypoint(waypointName, new Vec3(Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3])), Integer.parseInt(split[4]), split[5]);
            }
        }
        catch (Exception e) {
            System.out.println("[ERROR] Failed loading waypoints! Please check that waypoints.txt is in the valid format!");
            e.printStackTrace();
        }
    }

    public void saveWaypoints() {
        ArrayList<String> fileContent = new ArrayList<String>();
        for (Waypoint waypoint : this.getWaypoints()) {
            String waypointName = waypoint.getName();
            String x = String.valueOf(waypoint.getVec3().xCoord);
            String y = String.valueOf(waypoint.getVec3().yCoord);
            String z = String.valueOf(waypoint.getVec3().zCoord);
            fileContent.add(String.format("%s:%s:%s:%s:%s:%s", waypointName, x, y, z, waypoint.getColor(), waypoint.getAddress()));
        }
        FileUtils.write(this.WAYPOINT_DIR, fileContent, true);
    }

    public void deleteWaypoint(Waypoint waypoint) {
        this.waypoints.remove(waypoint);
    }

    public void createWaypoint(String name, Vec3 vec3, int color, String address) {
        this.waypoints.add(new Waypoint(name, vec3, color, address));
        this.saveWaypoints();
    }

    public List<Waypoint> getWaypoints() {
        return this.waypoints;
    }

    public static WaypointManager getManager() {
        return waypointManager;
    }

    public boolean containsName(String name) {
        for (Waypoint waypoint : this.getWaypoints()) {
            if (!waypoint.getName().equalsIgnoreCase(name)) continue;
            return true;
        }
        return false;
    }
}

