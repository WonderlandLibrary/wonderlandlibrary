package javafx.scene.media;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public abstract class Track {
   private String name;
   private long trackID;
   private Locale locale;
   private Map metadata;
   private String description;

   public final String getName() {
      return this.name;
   }

   public final Locale getLocale() {
      return this.locale;
   }

   public final long getTrackID() {
      return this.trackID;
   }

   public final Map getMetadata() {
      return this.metadata;
   }

   Track(long var1, Map var3) {
      this.trackID = var1;
      Object var4 = var3.get("name");
      if (null != var4 && var4 instanceof String) {
         this.name = (String)var4;
      }

      var4 = var3.get("locale");
      if (null != var4 && var4 instanceof Locale) {
         this.locale = (Locale)var4;
      }

      this.metadata = Collections.unmodifiableMap(var3);
   }

   public final String toString() {
      synchronized(this) {
         if (null == this.description) {
            StringBuilder var2 = new StringBuilder();
            Map var3 = this.getMetadata();
            var2.append(this.getClass().getName());
            var2.append("[ track id = ");
            var2.append(this.trackID);
            Iterator var4 = var3.entrySet().iterator();

            while(var4.hasNext()) {
               Map.Entry var5 = (Map.Entry)var4.next();
               Object var6 = var5.getValue();
               if (null != var6) {
                  var2.append(", ");
                  var2.append((String)var5.getKey());
                  var2.append(" = ");
                  var2.append(var6.toString());
               }
            }

            var2.append("]");
            this.description = var2.toString();
         }
      }

      return this.description;
   }
}
