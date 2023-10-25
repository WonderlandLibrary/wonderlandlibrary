package optifine;

import java.util.Comparator;
import org.lwjgl.opengl.DisplayMode;

public class DisplayModeComparator implements Comparator {
  public int compare(Object p_compare_1_, Object p_compare_2_) {
    DisplayMode displaymode = (DisplayMode)p_compare_1_;
    DisplayMode displaymode1 = (DisplayMode)p_compare_2_;
    return (displaymode.getWidth() != displaymode1.getWidth()) ? (displaymode.getWidth() - displaymode1.getWidth()) : ((displaymode.getHeight() != displaymode1.getHeight()) ? (displaymode.getHeight() - displaymode1.getHeight()) : ((displaymode.getBitsPerPixel() != displaymode1.getBitsPerPixel()) ? (displaymode.getBitsPerPixel() - displaymode1.getBitsPerPixel()) : ((displaymode.getFrequency() != displaymode1.getFrequency()) ? (displaymode.getFrequency() - displaymode1.getFrequency()) : 0)));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\optifine\DisplayModeComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */