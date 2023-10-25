package org.lwjgl.opengl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.lwjgl.LWJGLUtil;

public class XRandR {
  private static Screen[] current;
  
  private static String primaryScreenIdentifier;
  
  private static Screen[] savedConfiguration;
  
  private static Map<String, Screen[]> screens;
  
  private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");
  
  private static void populate() {
    if (screens != null)
      return; 
    screens = (Map)new HashMap<String, Screen>();
    try {
      Process p = Runtime.getRuntime().exec(new String[] { "xrandr", "-q" });
      List<Screen> currentList = new ArrayList<Screen>();
      List<Screen> possibles = new ArrayList<Screen>();
      String name = null;
      int[] currentScreenPosition = new int[2];
      BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
      String line;
      while ((line = br.readLine()) != null) {
        line = line.trim();
        String[] sa = WHITESPACE_PATTERN.split(line);
        if ("connected".equals(sa[1])) {
          if (name != null) {
            screens.put(name, possibles.toArray(new Screen[possibles.size()]));
            possibles.clear();
          } 
          name = sa[0];
          if ("primary".equals(sa[2])) {
            parseScreenHeader(currentScreenPosition, sa[3]);
            primaryScreenIdentifier = name;
            continue;
          } 
          parseScreenHeader(currentScreenPosition, sa[2]);
          continue;
        } 
        Matcher m = SCREEN_MODELINE_PATTERN.matcher(sa[0]);
        if (m.matches())
          parseScreenModeline(possibles, currentList, name, Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)), sa, currentScreenPosition); 
      } 
      screens.put(name, possibles.toArray(new Screen[possibles.size()]));
      current = currentList.<Screen>toArray(new Screen[currentList.size()]);
      if (primaryScreenIdentifier == null) {
        long totalPixels = Long.MIN_VALUE;
        for (Screen screen : current) {
          if (1L * screen.width * screen.height > totalPixels) {
            primaryScreenIdentifier = screen.name;
            totalPixels = 1L * screen.width * screen.height;
          } 
        } 
      } 
    } catch (Throwable e) {
      LWJGLUtil.log("Exception in XRandR.populate(): " + e.getMessage());
      screens.clear();
      current = new Screen[0];
    } 
  }
  
  public static Screen[] getConfiguration() {
    populate();
    for (Screen screen : current) {
      if (screen.name.equals(primaryScreenIdentifier))
        return new Screen[] { screen }; 
    } 
    return (Screen[])current.clone();
  }
  
  public static void setConfiguration(boolean disableOthers, Screen... screens) {
    if (screens.length == 0)
      throw new IllegalArgumentException("Must specify at least one screen"); 
    List<String> cmd = new ArrayList<String>();
    cmd.add("xrandr");
    if (disableOthers)
      for (Screen screen : current) {
        boolean disable = true;
        for (Screen screen1 : screens) {
          if (screen1.name.equals(screen.name)) {
            disable = false;
            break;
          } 
        } 
        if (disable) {
          cmd.add("--output");
          cmd.add(screen.name);
          cmd.add("--off");
        } 
      }  
    for (Screen screen : screens)
      screen.getArgs(cmd); 
    try {
      Process p = Runtime.getRuntime().exec(cmd.<String>toArray(new String[cmd.size()]));
      BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
      String line;
      while ((line = br.readLine()) != null)
        LWJGLUtil.log("Unexpected output from xrandr process: " + line); 
      current = screens;
    } catch (IOException e) {
      LWJGLUtil.log("XRandR exception in setConfiguration(): " + e.getMessage());
    } 
  }
  
  public static void saveConfiguration() {
    populate();
    savedConfiguration = (Screen[])current.clone();
  }
  
  public static void restoreConfiguration() {
    if (savedConfiguration != null)
      setConfiguration(true, savedConfiguration); 
  }
  
  public static String[] getScreenNames() {
    populate();
    return (String[])screens.keySet().toArray((Object[])new String[screens.size()]);
  }
  
  public static Screen[] getResolutions(String name) {
    populate();
    return (Screen[])((Screen[])screens.get(name)).clone();
  }
  
  private static final Pattern SCREEN_HEADER_PATTERN = Pattern.compile("^(\\d+)x(\\d+)[+](\\d+)[+](\\d+)$");
  
  private static final Pattern SCREEN_MODELINE_PATTERN = Pattern.compile("^(\\d+)x(\\d+)$");
  
  private static final Pattern FREQ_PATTERN = Pattern.compile("^(\\d+)[.](\\d+)(?:\\s*[*])?(?:\\s*[+])?$");
  
  private static void parseScreenModeline(List<Screen> allModes, List<Screen> current, String name, int width, int height, String[] modeLine, int[] screenPosition) {
    for (int i = 1; i < modeLine.length; i++) {
      String freqS = modeLine[i];
      if (!"+".equals(freqS)) {
        Matcher m = FREQ_PATTERN.matcher(freqS);
        if (!m.matches()) {
          LWJGLUtil.log("Frequency match failed: " + Arrays.toString((Object[])modeLine));
          return;
        } 
        int freq = Integer.parseInt(m.group(1));
        Screen s = new Screen(name, width, height, freq, 0, 0);
        if (freqS.contains("*")) {
          current.add(new Screen(name, width, height, freq, screenPosition[0], screenPosition[1]));
          allModes.add(0, s);
        } else {
          allModes.add(s);
        } 
      } 
    } 
  }
  
  private static void parseScreenHeader(int[] screenPosition, String resPos) {
    Matcher m = SCREEN_HEADER_PATTERN.matcher(resPos);
    if (!m.matches()) {
      screenPosition[0] = 0;
      screenPosition[1] = 0;
      return;
    } 
    screenPosition[0] = Integer.parseInt(m.group(3));
    screenPosition[1] = Integer.parseInt(m.group(4));
  }
  
  static Screen DisplayModetoScreen(DisplayMode mode) {
    populate();
    Screen primary = findPrimary(current);
    return new Screen(primary.name, mode.getWidth(), mode.getHeight(), mode.getFrequency(), primary.xPos, primary.yPos);
  }
  
  static DisplayMode ScreentoDisplayMode(Screen... screens) {
    populate();
    Screen primary = findPrimary(screens);
    return new DisplayMode(primary.width, primary.height, 24, primary.freq);
  }
  
  private static Screen findPrimary(Screen... screens) {
    for (Screen screen : screens) {
      if (screen.name.equals(primaryScreenIdentifier))
        return screen; 
    } 
    return screens[0];
  }
  
  public static class Screen implements Cloneable {
    public final String name;
    
    public final int width;
    
    public final int height;
    
    public final int freq;
    
    public int xPos;
    
    public int yPos;
    
    Screen(String name, int width, int height, int freq, int xPos, int yPos) {
      this.name = name;
      this.width = width;
      this.height = height;
      this.freq = freq;
      this.xPos = xPos;
      this.yPos = yPos;
    }
    
    private void getArgs(List<String> argList) {
      argList.add("--output");
      argList.add(this.name);
      argList.add("--mode");
      argList.add(this.width + "x" + this.height);
      argList.add("--rate");
      argList.add(Integer.toString(this.freq));
      argList.add("--pos");
      argList.add(this.xPos + "x" + this.yPos);
    }
    
    public String toString() {
      return this.name + " " + this.width + "x" + this.height + " @ " + this.xPos + "x" + this.yPos + " with " + this.freq + "Hz";
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\XRandR.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */