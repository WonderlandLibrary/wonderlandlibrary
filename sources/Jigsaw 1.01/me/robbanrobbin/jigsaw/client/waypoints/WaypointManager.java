package me.robbanrobbin.jigsaw.client.waypoints;

import java.util.ArrayList;

public class WaypointManager {
	
	private ArrayList<Waypoint> waypoints = new ArrayList<Waypoint>();
	
	public WaypointManager() {
		
		
		
	}
	
	public void addWaypoint(Waypoint waypoint) {
		waypoints.add(waypoint);
	}
	
	public ArrayList<Waypoint> getWaypoints() {
		return waypoints;
	}
	
	public int getWaypointCount() {
		return waypoints.size();
	}
	
}
