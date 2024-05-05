package space.lunaclient.luna.impl.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.Minecraft;
import space.lunaclient.luna.Luna;
import space.lunaclient.luna.api.file.CustomFile;
import space.lunaclient.luna.api.manager.Manager;
import space.lunaclient.luna.impl.files.ConfigsFile;
import space.lunaclient.luna.impl.files.FriendsFile;
import space.lunaclient.luna.impl.files.ModulesFile;
import space.lunaclient.luna.impl.files.OrdersFile;
import space.lunaclient.luna.impl.files.WayPointsFile;
import space.lunaclient.luna.impl.gui.alt.Alt;
import space.lunaclient.luna.impl.gui.alt.AltManager;

public class CustomFileManager
  extends Manager<CustomFile>
{
  private static final File ALT = getConfigFile("Alts");
  private static final File LAST_ALT = getConfigFile("LastAlt");
  private Gson gson;
  private File directory;
  
  public CustomFileManager()
  {
    this.gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    this.directory = new File(Minecraft.getMinecraft().mcDataDir.toString() + "/" + Luna.INSTANCE.NAME);
    makeDirectory();
    registerFiles();
    loadLastAlt();
    loadAlts();
  }
  
  private void makeDirectory()
  {
    if (!this.directory.exists()) {
      this.directory.mkdir();
    }
  }
  
  private static void loadLastAlt()
  {
    try
    {
      if (!LAST_ALT.exists())
      {
        PrintWriter printWriter = new PrintWriter(new FileWriter(LAST_ALT));
        printWriter.println();
        printWriter.close();
      }
      else if (LAST_ALT.exists())
      {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(LAST_ALT));
        String s;
        while ((s = bufferedReader.readLine()) != null)
        {
          if (s.contains("\t")) {
            s = s.replace("\t", "    ");
          }
          if (s.contains("    "))
          {
            String[] parts = s.split(" {4}");
            String[] account = parts[1].split(":");
            if (account.length == 2)
            {
              Luna.INSTANCE.ALT_MANAGER.setLastAlt(new Alt(account[0], account[1], parts[0]));
            }
            else
            {
              StringBuilder pw = new StringBuilder(account[1]);
              for (int i = 2; i < account.length; i++) {
                pw.append(":").append(account[i]);
              }
              Luna.INSTANCE.ALT_MANAGER.setLastAlt(new Alt(account[0], pw.toString(), parts[0]));
            }
          }
          else
          {
            String[] account = s.split(":");
            if (account.length == 1)
            {
              Luna.INSTANCE.ALT_MANAGER.setLastAlt(new Alt(account[0], ""));
            }
            else if (account.length == 2)
            {
              Luna.INSTANCE.ALT_MANAGER.setLastAlt(new Alt(account[0], account[1]));
            }
            else
            {
              StringBuilder pw = new StringBuilder(account[1]);
              for (int i = 2; i < account.length; i++) {
                pw.append(":").append(account[i]);
              }
              Luna.INSTANCE.ALT_MANAGER.setLastAlt(new Alt(account[0], pw.toString()));
            }
          }
        }
        bufferedReader.close();
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  public static void saveLastAlt()
  {
    try
    {
      PrintWriter printWriter = new PrintWriter(LAST_ALT);
      Alt alt = Luna.INSTANCE.ALT_MANAGER.getLastAlt();
      if (alt != null) {
        printWriter.println(alt.getMask() + "    " + alt.getUsername() + ":" + alt.getPassword());
      }
      printWriter.close();
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
  }
  
  private static void loadAlts()
  {
    try
    {
      BufferedReader bufferedReader = new BufferedReader(new FileReader(ALT));
      if (!ALT.exists())
      {
        PrintWriter printWriter = new PrintWriter(new FileWriter(ALT));
        printWriter.println();
        printWriter.close();
      }
      else if (ALT.exists())
      {
        String s;
        while ((s = bufferedReader.readLine()) != null)
        {
          if (s.contains("\t")) {
            s = s.replace("\t", "    ");
          }
          if (s.contains("    "))
          {
            String[] parts = s.split(" {4}");
            String[] account = parts[1].split(":");
            if (account.length == 2)
            {
              Luna.INSTANCE.ALT_MANAGER.getAlts().add(new Alt(account[0], account[1], parts[0]));
            }
            else
            {
              StringBuilder pw = new StringBuilder(account[1]);
              for (int i = 2; i < account.length; i++) {
                pw.append(":").append(account[i]);
              }
              Luna.INSTANCE.ALT_MANAGER.getAlts().add(new Alt(account[0], pw.toString(), parts[0]));
            }
          }
          else
          {
            String[] account = s.split(":");
            if (account.length == 1)
            {
              Luna.INSTANCE.ALT_MANAGER.getAlts().add(new Alt(account[0], ""));
            }
            else if (account.length == 2)
            {
              try
              {
                Luna.INSTANCE.ALT_MANAGER.getAlts().add(new Alt(account[0], account[1]));
              }
              catch (Exception e)
              {
                e.printStackTrace();
              }
            }
            else
            {
              StringBuilder pw = new StringBuilder(account[1]);
              for (int i = 2; i < account.length; i++) {
                pw.append(":").append(account[i]);
              }
              Luna.INSTANCE.ALT_MANAGER.getAlts().add(new Alt(account[0], pw.toString()));
            }
          }
        }
      }
      bufferedReader.close();
    }
    catch (Exception localException1) {}
  }
  
  public static void saveAlts()
  {
    try
    {
      PrintWriter printWriter = new PrintWriter(ALT);
      for (Alt alt : Luna.INSTANCE.ALT_MANAGER.getAlts()) {
        if (alt.getMask().equals("")) {
          printWriter.println(alt.getUsername() + ":" + alt.getPassword());
        } else {
          printWriter.println(alt.getMask() + "    " + alt.getUsername() + ":" + alt.getPassword());
        }
      }
      printWriter.close();
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
  }
  
  private static File getConfigFile(String name)
  {
    File file = new File(getConfigDir(), String.format("%s.txt", new Object[] { name }));
    if (!file.exists()) {
      try
      {
        file.createNewFile();
      }
      catch (IOException localIOException) {}
    }
    return file;
  }
  
  private static File getConfigDir()
  {
    File file = new File(Minecraft.getMinecraft().mcDataDir, Luna.INSTANCE.NAME);
    if (!file.exists()) {
      file.mkdir();
    }
    return file;
  }
  
  private void registerFiles()
  {
    getContents().add(new FriendsFile(this.gson, new File(this.directory, "Friends.json")));
    getContents().add(new WayPointsFile(this.gson, new File(this.directory, "Waypoints.json")));
    getContents().add(new ConfigsFile(this.gson, new File(this.directory.toString() + "/Configs", "Configs.json")));
    getContents().add(new ModulesFile(this.gson, null));
    getContents().add(new OrdersFile(this.gson, null));
  }
  
  public void loadFiles()
  {
    for (CustomFile file : getContents()) {
      try
      {
        System.out.println("Loading files");
        file.loadFile();
      }
      catch (IOException e)
      {
        System.out.println("Failed files");
        e.printStackTrace();
      }
    }
  }
  
  public void saveFiles()
  {
    for (CustomFile file : getContents()) {
      try
      {
        file.saveFile();
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
  }
  
  public CustomFile getFile(Class<? extends CustomFile> clazz)
  {
    for (CustomFile file : getContents()) {
      if (file.getClass() == clazz) {
        return file;
      }
    }
    return null;
  }
}
