package cn.hanabi.gui.font.noway.ttfr;

import cn.hanabi.Wrapper;
import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;

public class FontRendererCallback {
   public static boolean betterFontsEnabled = true;

   public static void constructor(IBFFontRenderer font, ResourceLocation location) {
      if (((FontRenderer)font).getClass() == FontRenderer.class) {
         if (location.getResourcePath().equalsIgnoreCase("textures/font/ascii.png") && font.getStringCache() == null) {
            font.setDropShadowEnabled(true);
            int[] colorCode = Wrapper.getFontRender().getColorCode();
            font.setStringCache(new HStringCache(colorCode));
            font.getStringCache().setDefaultFont((String)"Lucida Sans Regular", 18, false);
         }

      }
   }

   public static String bidiReorder(IBFFontRenderer font, String text) {
      if (betterFontsEnabled && font.getStringCache() != null) {
         return text;
      } else {
         try {
            Bidi bidi = new Bidi((new ArabicShaping(8)).shape(text), 127);
            bidi.setReorderingMode(0);
            return bidi.writeReordered(2);
         } catch (ArabicShapingException var3) {
            return text;
         }
      }
   }
}
