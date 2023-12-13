package javafx.scene.control;

import javafx.collections.ObservableList;

class TreeUtil {
   static int getExpandedDescendantCount(TreeItem var0, boolean var1) {
      if (var0 == null) {
         return 0;
      } else {
         return var0.isLeaf() ? 1 : var0.getExpandedDescendentCount(var1);
      }
   }

   static int updateExpandedItemCount(TreeItem var0, boolean var1, boolean var2) {
      if (var0 == null) {
         return 0;
      } else if (!var0.isExpanded()) {
         return 1;
      } else {
         int var3 = getExpandedDescendantCount(var0, var1);
         if (!var2) {
            --var3;
         }

         return var3;
      }
   }

   static TreeItem getItem(TreeItem var0, int var1, boolean var2) {
      if (var0 == null) {
         return null;
      } else if (var1 == 0) {
         return var0;
      } else if (var1 >= getExpandedDescendantCount(var0, var2)) {
         return null;
      } else {
         ObservableList var3 = var0.getChildren();
         if (var3 == null) {
            return null;
         } else {
            int var4 = var1 - 1;
            int var6 = 0;

            for(int var7 = var3.size(); var6 < var7; ++var6) {
               TreeItem var5 = (TreeItem)var3.get(var6);
               if (var4 == 0) {
                  return var5;
               }

               if (!var5.isLeaf() && var5.isExpanded()) {
                  int var8 = getExpandedDescendantCount(var5, var2);
                  if (var4 >= var8) {
                     var4 -= var8;
                  } else {
                     TreeItem var9 = getItem(var5, var4, var2);
                     if (var9 != null) {
                        return var9;
                     }

                     --var4;
                  }
               } else {
                  --var4;
               }
            }

            return null;
         }
      }
   }

   static int getRow(TreeItem var0, TreeItem var1, boolean var2, boolean var3) {
      if (var0 == null) {
         return -1;
      } else if (var3 && var0.equals(var1)) {
         return 0;
      } else {
         int var4 = 0;
         TreeItem var5 = var0;
         TreeItem var6 = var0.getParent();

         boolean var9;
         for(var9 = false; !var5.equals(var1) && var6 != null; ++var4) {
            if (!var6.isExpanded()) {
               var9 = true;
               break;
            }

            ObservableList var8 = var6.children;
            int var10 = var8.indexOf(var5);

            for(int var11 = var10 - 1; var11 > -1; --var11) {
               TreeItem var7 = (TreeItem)var8.get(var11);
               if (var7 != null) {
                  var4 += getExpandedDescendantCount(var7, var2);
                  if (var7.equals(var1)) {
                     if (!var3) {
                        return -1;
                     }

                     return var4;
                  }
               }
            }

            var5 = var6;
            var6 = var6.getParent();
         }

         return (var6 != null || var4 != 0) && !var9 ? (var3 ? var4 : var4 - 1) : -1;
      }
   }
}
