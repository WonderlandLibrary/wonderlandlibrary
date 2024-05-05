package space.lunaclient.luna.impl.files;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import space.lunaclient.luna.Luna;
import space.lunaclient.luna.api.file.CustomFile;
import space.lunaclient.luna.api.waypoint.WayPoint;
import space.lunaclient.luna.impl.managers.WaypointManager;

public class WayPointsFile
  extends CustomFile
{
  public WayPointsFile(Gson gson, File file)
  {
    super(gson, file);
  }
  
  public void loadFile()
    throws IOException
  {
    FileReader inFile = new FileReader(getFile());
    Luna.INSTANCE.WAYPOINT_MANAGER.setContents((CopyOnWriteArrayList)getGson().fromJson(inFile, new TypeToken() {}.getType()));
    inFile.close();
    if (Luna.INSTANCE.WAYPOINT_MANAGER.getContents() == null) {
      Luna.INSTANCE.WAYPOINT_MANAGER.setContents(new CopyOnWriteArrayList());
    }
  }
  
  public void saveFile()
    throws IOException
  {
    FileWriter printWriter = new FileWriter(getFile());
    printWriter.write(getGson().toJson(Luna.INSTANCE.WAYPOINT_MANAGER.getContents()));
    printWriter.close();
  }
}
