package space.lunaclient.luna.impl.orders;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import space.lunaclient.luna.Luna;
import space.lunaclient.luna.api.file.CustomFile;
import space.lunaclient.luna.api.order.Order;
import space.lunaclient.luna.api.waypoint.WayPoint;
import space.lunaclient.luna.impl.files.WayPointsFile;
import space.lunaclient.luna.impl.managers.CustomFileManager;
import space.lunaclient.luna.impl.managers.WaypointManager;
import space.lunaclient.luna.util.PlayerUtils;

public class WayPointOrder
  implements Order
{
  public WayPointOrder() {}
  
  public boolean run(String[] args)
  {
    if (args.length == 2)
    {
      if (args[1].equalsIgnoreCase("clear"))
      {
        Luna.INSTANCE.WAYPOINT_MANAGER.getContents().clear();
        PlayerUtils.tellPlayer("All waypoints have been removed.", false);
        saveFile();
        return true;
      }
      if (args[1].equalsIgnoreCase("list"))
      {
        if (Luna.INSTANCE.WAYPOINT_MANAGER.getContents().isEmpty())
        {
          PlayerUtils.tellPlayer("There are no waypoints.", false);
        }
        else
        {
          PlayerUtils.tellPlayer("Here is a list of all the waypoints:", false);
          for (WayPoint waypoint : Luna.INSTANCE.WAYPOINT_MANAGER.getContents()) {
            PlayerUtils.tellPlayer(
            
              "Name: " + ChatFormatting.WHITE + waypoint.getName() + ChatFormatting.GRAY + " Server: " + ChatFormatting.WHITE + (waypoint.getServerIP() != null ? waypoint.getServerIP() : "SinglePlayer"), false);
          }
        }
        return true;
      }
    }
    else if (args.length == 3)
    {
      if (args[1].equalsIgnoreCase("add"))
      {
        Luna.INSTANCE.WAYPOINT_MANAGER.addWaypoint(args[2], Minecraft.getMinecraft().getCurrentServerData(), Minecraft.thePlayer.getPosition());
        PlayerUtils.tellPlayer("A waypoint with the name " + args[2] + " has been added.", false);
        saveFile();
        return true;
      }
      if (args[1].equalsIgnoreCase("remove"))
      {
        WayPoint waypoint = Luna.INSTANCE.WAYPOINT_MANAGER.getWayPoint(args[2], Minecraft.getMinecraft().getCurrentServerData());
        if (waypoint != null)
        {
          Luna.INSTANCE.WAYPOINT_MANAGER.getContents().remove(waypoint);
          PlayerUtils.tellPlayer("A waypoint with the name " + args[2] + " has been removed.", false);
          saveFile();
        }
        else
        {
          PlayerUtils.tellPlayer("A waypoint with the name " + args[2] + " can not be found.", false);
        }
        return true;
      }
    }
    return false;
  }
  
  private void saveFile()
  {
    try
    {
      Luna.INSTANCE.FILE_MANAGER.getFile(WayPointsFile.class).saveFile();
    }
    catch (Exception localException) {}
  }
  
  public String usage()
  {
    return "USAGE: " + ChatFormatting.GRAY + "[ " + ChatFormatting.WHITE + "waypoint <list/add/remove/clear> <name>" + ChatFormatting.GRAY + " ]";
  }
}
