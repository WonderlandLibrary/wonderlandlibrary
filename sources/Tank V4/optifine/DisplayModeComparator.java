package optifine;

import java.util.Comparator;
import org.lwjgl.opengl.DisplayMode;

public class DisplayModeComparator implements Comparator {
   public int compare(Object var1, Object var2) {
      DisplayMode var3 = (DisplayMode)var1;
      DisplayMode var4 = (DisplayMode)var2;
      return var3.getWidth() != var4.getWidth() ? var3.getWidth() - var4.getWidth() : (var3.getHeight() != var4.getHeight() ? var3.getHeight() - var4.getHeight() : (var3.getBitsPerPixel() != var4.getBitsPerPixel() ? var3.getBitsPerPixel() - var4.getBitsPerPixel() : (var3.getFrequency() != var4.getFrequency() ? var3.getFrequency() - var4.getFrequency() : 0)));
   }
}
