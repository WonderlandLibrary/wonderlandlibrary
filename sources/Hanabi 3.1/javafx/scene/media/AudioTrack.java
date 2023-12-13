package javafx.scene.media;

import java.util.Locale;
import java.util.Map;

public final class AudioTrack extends Track {
   /** @deprecated */
   @Deprecated
   public final String getLanguage() {
      Locale var1 = this.getLocale();
      return null == var1 ? null : var1.getLanguage();
   }

   AudioTrack(long var1, Map var3) {
      super(var1, var3);
   }
}
