package cn.hanabi.gui.font;

import cn.hanabi.events.EventTick;
import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import java.util.Iterator;

public class FontsGC {
   private static final int GC_TICKS = 200;
   private static int ticks = 0;
   public static final int REMOVE_TIME = 30000;
   private static final ArrayList arr = new ArrayList();

   @EventTarget
   public void onTick(EventTick event) {
      if (ticks++ > 200) {
         ticks = 0;
         Iterator var2 = arr.iterator();

         while(var2.hasNext()) {
            VertexFontRenderer fontRenderer = (VertexFontRenderer)var2.next();
            fontRenderer.gcTick();
         }
      }

   }

   public void add(VertexFontRenderer fontRenderer) {
      if (arr.contains(fontRenderer)) {
         throw new IllegalArgumentException("FontRenderer already added!");
      } else {
         arr.add(fontRenderer);
      }
   }

   public void remove(VertexFontRenderer fontRenderer) {
      if (!arr.contains(fontRenderer)) {
         throw new IllegalArgumentException("FontRenderer not added!");
      } else {
         arr.remove(fontRenderer);
      }
   }

   public static void removeAll() {
      arr.clear();
   }

   static {
      EventManager.register(FontsGC.class);
   }
}
