package space.lunaclient.luna.util;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class ApplicationUtilities
{
  public ApplicationUtilities() {}
  
  public static void runApplication(String applicationFilePath)
    throws IOException, InterruptedException
  {
    File application = new File(applicationFilePath);
    String applicationName = application.getName();
    if (!isProcessRunning(applicationName)) {
      Desktop.getDesktop().open(application);
    }
  }
  
  public static boolean isProcessRunning(String processName)
    throws IOException
  {
    ProcessBuilder processBuilder = new ProcessBuilder(new String[] { "tasklist.exe" });
    Process process = processBuilder.start();
    String tasksList = toString(process.getInputStream());
    
    return tasksList.contains(processName);
  }
  
  private static String toString(InputStream inputStream)
  {
    Scanner scanner = new Scanner(inputStream, "UTF-8").useDelimiter("\\A");
    String string = scanner.hasNext() ? scanner.next() : "";
    scanner.close();
    
    return string;
  }
}
