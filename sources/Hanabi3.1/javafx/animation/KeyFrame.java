package javafx.animation;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javafx.beans.NamedArg;
import javafx.event.EventHandler;
import javafx.util.Duration;

public final class KeyFrame {
   private static final EventHandler DEFAULT_ON_FINISHED = null;
   private static final String DEFAULT_NAME = null;
   private final Duration time;
   private final Set values;
   private final EventHandler onFinished;
   private final String name;

   public Duration getTime() {
      return this.time;
   }

   public Set getValues() {
      return this.values;
   }

   public EventHandler getOnFinished() {
      return this.onFinished;
   }

   public String getName() {
      return this.name;
   }

   public KeyFrame(@NamedArg("time") Duration var1, @NamedArg("name") String var2, @NamedArg("onFinished") EventHandler var3, @NamedArg("values") Collection var4) {
      if (var1 == null) {
         throw new NullPointerException("The time has to be specified");
      } else if (!var1.lessThan(Duration.ZERO) && !var1.equals(Duration.UNKNOWN)) {
         this.time = var1;
         this.name = var2;
         if (var4 != null) {
            CopyOnWriteArraySet var5 = new CopyOnWriteArraySet(var4);
            var5.remove((Object)null);
            this.values = var5.size() == 0 ? Collections.emptySet() : (var5.size() == 1 ? Collections.singleton(var5.iterator().next()) : Collections.unmodifiableSet(var5));
         } else {
            this.values = Collections.emptySet();
         }

         this.onFinished = var3;
      } else {
         throw new IllegalArgumentException("The time is invalid.");
      }
   }

   public KeyFrame(@NamedArg("time") Duration var1, @NamedArg("name") String var2, @NamedArg("onFinished") EventHandler var3, @NamedArg("values") KeyValue... var4) {
      if (var1 == null) {
         throw new NullPointerException("The time has to be specified");
      } else if (!var1.lessThan(Duration.ZERO) && !var1.equals(Duration.UNKNOWN)) {
         this.time = var1;
         this.name = var2;
         if (var4 != null) {
            CopyOnWriteArraySet var5 = new CopyOnWriteArraySet();
            KeyValue[] var6 = var4;
            int var7 = var4.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               KeyValue var9 = var6[var8];
               if (var9 != null) {
                  var5.add(var9);
               }
            }

            this.values = var5.size() == 0 ? Collections.emptySet() : (var5.size() == 1 ? Collections.singleton(var5.iterator().next()) : Collections.unmodifiableSet(var5));
         } else {
            this.values = Collections.emptySet();
         }

         this.onFinished = var3;
      } else {
         throw new IllegalArgumentException("The time is invalid.");
      }
   }

   public KeyFrame(@NamedArg("time") Duration var1, @NamedArg("onFinished") EventHandler var2, @NamedArg("values") KeyValue... var3) {
      this(var1, DEFAULT_NAME, var2, var3);
   }

   public KeyFrame(@NamedArg("time") Duration var1, @NamedArg("name") String var2, @NamedArg("values") KeyValue... var3) {
      this(var1, var2, DEFAULT_ON_FINISHED, var3);
   }

   public KeyFrame(@NamedArg("time") Duration var1, @NamedArg("values") KeyValue... var2) {
      this(var1, DEFAULT_NAME, DEFAULT_ON_FINISHED, var2);
   }

   public String toString() {
      return "KeyFrame [time=" + this.time + ", values=" + this.values + ", onFinished=" + this.onFinished + ", name=" + this.name + "]";
   }

   public int hashCode() {
      assert this.time != null && this.values != null;

      int var2 = 1;
      var2 = 31 * var2 + this.time.hashCode();
      var2 = 31 * var2 + (this.name == null ? 0 : this.name.hashCode());
      var2 = 31 * var2 + (this.onFinished == null ? 0 : this.onFinished.hashCode());
      var2 = 31 * var2 + this.values.hashCode();
      return var2;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof KeyFrame)) {
         return false;
      } else {
         KeyFrame var2 = (KeyFrame)var1;

         assert this.time != null && this.values != null && var2.time != null && var2.values != null;

         boolean var10000;
         if (this.time.equals(var2.time)) {
            label61: {
               if (this.name == null) {
                  if (var2.name != null) {
                     break label61;
                  }
               } else if (!this.name.equals(var2.name)) {
                  break label61;
               }

               if (this.onFinished == null) {
                  if (var2.onFinished != null) {
                     break label61;
                  }
               } else if (!this.onFinished.equals(var2.onFinished)) {
                  break label61;
               }

               if (this.values.equals(var2.values)) {
                  var10000 = true;
                  return var10000;
               }
            }
         }

         var10000 = false;
         return var10000;
      }
   }
}
