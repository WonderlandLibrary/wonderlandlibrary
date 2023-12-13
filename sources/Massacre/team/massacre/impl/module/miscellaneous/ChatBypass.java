package team.massacre.impl.module.miscellaneous;

import org.apache.commons.lang3.RandomUtils;
import team.massacre.api.event.api.annotations.Handler;
import team.massacre.api.module.Category;
import team.massacre.api.module.Module;
import team.massacre.api.property.Property;
import team.massacre.api.property.impl.EnumProperty;
import team.massacre.impl.event.EventSendMessage;

public class ChatBypass extends Module {
   private static final String[] INVIS_CHARS = new String[]{"â›�", "â›˜", "â›œ", "â› ", "â›Ÿ", "â›�", "â›�", "â›¡", "â›‹", "â›Œ", "â›—", "â›©", "â›‰"};
   private final EnumProperty<ChatBypass.BypassMode> bypassModeProperty;

   public ChatBypass() {
      super("Chat Bypass", 0, Category.MISCELLANEOUS);
      this.bypassModeProperty = new EnumProperty("Mode", ChatBypass.BypassMode.INVIS);
      this.addValues(new Property[]{this.bypassModeProperty});
   }

   @Handler
   public void a(EventSendMessage event) {
      if (!event.getMessage().startsWith("/")) {
         switch((ChatBypass.BypassMode)this.bypassModeProperty.getValue()) {
         case INVIS:
            StringBuilder stringBuilder = new StringBuilder();
            char[] var3 = event.getMessage().toCharArray();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               char character = var3[var5];
               stringBuilder.append(character).append(INVIS_CHARS[RandomUtils.nextInt(0, INVIS_CHARS.length)]);
            }

            event.setMessage(stringBuilder.toString());
         case FONT:
         default:
         }
      }
   }

   private static enum BypassMode {
      INVIS,
      FONT;
   }
}
