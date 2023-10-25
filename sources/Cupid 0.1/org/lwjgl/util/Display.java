package org.lwjgl.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.opengl.DisplayMode;

public final class Display {
  private static final boolean DEBUG = false;
  
  public static DisplayMode[] getAvailableDisplayModes(int minWidth, int minHeight, int maxWidth, int maxHeight, int minBPP, int maxBPP, int minFreq, int maxFreq) throws LWJGLException {
    DisplayMode[] modes = org.lwjgl.opengl.Display.getAvailableDisplayModes();
    if (LWJGLUtil.DEBUG) {
      System.out.println("Available screen modes:");
      for (DisplayMode mode : modes)
        System.out.println(mode); 
    } 
    ArrayList<DisplayMode> matches = new ArrayList<DisplayMode>(modes.length);
    for (int i = 0; i < modes.length; i++) {
      assert modes[i] != null : "" + i + " " + modes.length;
      if (minWidth != -1 && modes[i].getWidth() < minWidth)
        continue; 
      if (maxWidth != -1 && modes[i].getWidth() > maxWidth)
        continue; 
      if (minHeight != -1 && modes[i].getHeight() < minHeight)
        continue; 
      if (maxHeight != -1 && modes[i].getHeight() > maxHeight)
        continue; 
      if (minBPP != -1 && modes[i].getBitsPerPixel() < minBPP)
        continue; 
      if (maxBPP != -1 && modes[i].getBitsPerPixel() > maxBPP)
        continue; 
      if (modes[i].getFrequency() != 0) {
        if (minFreq != -1 && modes[i].getFrequency() < minFreq)
          continue; 
        if (maxFreq != -1 && modes[i].getFrequency() > maxFreq)
          continue; 
      } 
      matches.add(modes[i]);
      continue;
    } 
    DisplayMode[] ret = new DisplayMode[matches.size()];
    matches.toArray(ret);
    if (LWJGLUtil.DEBUG);
    return ret;
  }
  
  public static DisplayMode setDisplayMode(DisplayMode[] dm, final String[] param) throws Exception {
    class Sorter implements Comparator<DisplayMode> {
      final Display.FieldAccessor[] accessors = new Display.FieldAccessor[param.length];
      
      Sorter() {
        for (int i = 0; i < this.accessors.length; i++) {
          int idx = param[i].indexOf('=');
          if (idx > 0) {
            this.accessors[i] = new Display.FieldAccessor(param[i].substring(0, idx), 0, Integer.parseInt(param[i].substring(idx + 1, param[i].length())), true);
          } else if (param[i].charAt(0) == '-') {
            this.accessors[i] = new Display.FieldAccessor(param[i].substring(1), -1, 0, false);
          } else {
            this.accessors[i] = new Display.FieldAccessor(param[i], 1, 0, false);
          } 
        } 
      }
      
      public int compare(DisplayMode dm1, DisplayMode dm2) {
        for (Display.FieldAccessor accessor : this.accessors) {
          int f1 = accessor.getInt(dm1);
          int f2 = accessor.getInt(dm2);
          if (accessor.usePreferred && f1 != f2) {
            if (f1 == accessor.preferred)
              return -1; 
            if (f2 == accessor.preferred)
              return 1; 
            int absf1 = Math.abs(f1 - accessor.preferred);
            int absf2 = Math.abs(f2 - accessor.preferred);
            if (absf1 < absf2)
              return -1; 
            if (absf1 > absf2)
              return 1; 
          } else {
            if (f1 < f2)
              return accessor.order; 
            if (f1 != f2)
              return -accessor.order; 
          } 
        } 
        return 0;
      }
    };
    Arrays.sort(dm, new Sorter());
    if (LWJGLUtil.DEBUG) {
      System.out.println("Sorted display modes:");
      for (DisplayMode aDm : dm)
        System.out.println(aDm); 
    } 
    for (DisplayMode aDm : dm) {
      try {
        if (LWJGLUtil.DEBUG)
          System.out.println("Attempting to set displaymode: " + aDm); 
        org.lwjgl.opengl.Display.setDisplayMode(aDm);
        return aDm;
      } catch (Exception e) {
        if (LWJGLUtil.DEBUG) {
          System.out.println("Failed to set display mode to " + aDm);
          e.printStackTrace();
        } 
      } 
    } 
    throw new Exception("Failed to set display mode.");
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjg\\util\Display.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */