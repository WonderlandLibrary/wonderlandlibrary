package javafx.scene.paint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javafx.beans.NamedArg;

public final class Stop {
   static final List NO_STOPS;
   private double offset;
   private Color color;
   private int hash = 0;

   static List normalize(Stop[] var0) {
      List var1 = var0 == null ? null : Arrays.asList(var0);
      return normalize(var1);
   }

   static List normalize(List var0) {
      if (var0 == null) {
         return NO_STOPS;
      } else {
         Stop var1 = null;
         Stop var2 = null;
         ArrayList var3 = new ArrayList(var0.size());
         Iterator var4 = var0.iterator();

         while(true) {
            Stop var5;
            double var6;
            do {
               while(true) {
                  do {
                     do {
                        if (!var4.hasNext()) {
                           if (var1 == null) {
                              Color var10;
                              if (var3.isEmpty()) {
                                 if (var2 == null) {
                                    return NO_STOPS;
                                 }

                                 var10 = var2.getColor();
                              } else {
                                 var10 = ((Stop)var3.get(0)).getColor();
                                 if (var2 == null && var3.size() == 1) {
                                    var3.clear();
                                 }
                              }

                              var1 = new Stop(0.0, var10);
                           } else if (var1.getOffset() < 0.0) {
                              var1 = new Stop(0.0, var1.getColor());
                           }

                           var3.add(0, var1);
                           if (var2 == null) {
                              var2 = new Stop(1.0, ((Stop)var3.get(var3.size() - 1)).getColor());
                           } else if (var2.getOffset() > 1.0) {
                              var2 = new Stop(1.0, var2.getColor());
                           }

                           var3.add(var2);
                           return Collections.unmodifiableList(var3);
                        }

                        var5 = (Stop)var4.next();
                     } while(var5 == null);
                  } while(var5.getColor() == null);

                  var6 = var5.getOffset();
                  if (var6 <= 0.0) {
                     break;
                  }

                  if (var6 >= 1.0) {
                     if (var2 == null || var6 < var2.getOffset()) {
                        var2 = var5;
                     }
                  } else if (var6 == var6) {
                     for(int var8 = var3.size() - 1; var8 >= 0; --var8) {
                        Stop var9 = (Stop)var3.get(var8);
                        if (var9.getOffset() <= var6) {
                           if (var9.getOffset() == var6) {
                              if (var8 > 0 && ((Stop)var3.get(var8 - 1)).getOffset() == var6) {
                                 var3.set(var8, var5);
                              } else {
                                 var3.add(var8 + 1, var5);
                              }
                           } else {
                              var3.add(var8 + 1, var5);
                           }

                           var5 = null;
                           break;
                        }
                     }

                     if (var5 != null) {
                        var3.add(0, var5);
                     }
                  }
               }
            } while(var1 != null && !(var6 >= var1.getOffset()));

            var1 = var5;
         }
      }
   }

   public final double getOffset() {
      return this.offset;
   }

   public final Color getColor() {
      return this.color;
   }

   public Stop(@NamedArg("offset") double var1, @NamedArg(value = "color",defaultValue = "BLACK") Color var3) {
      this.offset = var1;
      this.color = var3;
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (var1 == this) {
         return true;
      } else if (!(var1 instanceof Stop)) {
         return false;
      } else {
         boolean var10000;
         label41: {
            Stop var2 = (Stop)var1;
            if (this.offset == var2.offset) {
               if (this.color == null) {
                  if (var2.color == null) {
                     break label41;
                  }
               } else if (this.color.equals(var2.color)) {
                  break label41;
               }
            }

            var10000 = false;
            return var10000;
         }

         var10000 = true;
         return var10000;
      }
   }

   public int hashCode() {
      if (this.hash == 0) {
         long var1 = 17L;
         var1 = 37L * var1 + Double.doubleToLongBits(this.offset);
         var1 = 37L * var1 + (long)this.color.hashCode();
         this.hash = (int)(var1 ^ var1 >> 32);
      }

      return this.hash;
   }

   public String toString() {
      return this.color + " " + this.offset * 100.0 + "%";
   }

   static {
      NO_STOPS = Collections.unmodifiableList(Arrays.asList(new Stop(0.0, Color.TRANSPARENT), new Stop(1.0, Color.TRANSPARENT)));
   }
}
