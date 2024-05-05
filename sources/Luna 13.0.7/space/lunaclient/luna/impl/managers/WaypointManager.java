package space.lunaclient.luna.impl.managers;

import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.BlockPos;
import space.lunaclient.luna.api.manager.Manager;
import space.lunaclient.luna.api.waypoint.WayPoint;

public class WaypointManager
  extends Manager<WayPoint>
{
  public WaypointManager() {}
  
  public void addWaypoint(String name, ServerData serverData, BlockPos blockPos)
  {
    if (serverData == null) {
      getContents().add(new WayPoint(name, null, blockPos.getX(), blockPos.getY(), blockPos.getZ()));
    } else {
      getContents().add(new WayPoint(name, serverData.serverIP, blockPos.getX(), blockPos.getY(), blockPos.getZ()));
    }
  }
  
  public WayPoint getWayPoint(String name, ServerData serverData)
  {
    for (WayPoint waypoint : getContents()) {
      if ((serverData != null) && (waypoint.getServerIP() != null))
      {
        if ((waypoint.getServerIP().equalsIgnoreCase(serverData.serverIP)) && (waypoint.getName().equalsIgnoreCase(name))) {
          return waypoint;
        }
      }
      else if (waypoint.getName().equalsIgnoreCase(name)) {
        return waypoint;
      }
    }
    return null;
  }
}
