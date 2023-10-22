package javafx.scene.control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

class TableUtil {
   private TableUtil() {
   }

   static void removeTableColumnListener(List var0, InvalidationListener var1, InvalidationListener var2, InvalidationListener var3, InvalidationListener var4) {
      if (var0 != null) {
         TableColumnBase var6;
         for(Iterator var5 = var0.iterator(); var5.hasNext(); removeTableColumnListener(var6.getColumns(), var1, var2, var3, var4)) {
            var6 = (TableColumnBase)var5.next();
            var6.visibleProperty().removeListener(var1);
            var6.sortableProperty().removeListener(var2);
            var6.comparatorProperty().removeListener(var4);
            if (var6 instanceof TableColumn) {
               ((TableColumn)var6).sortTypeProperty().removeListener(var3);
            } else if (var6 instanceof TreeTableColumn) {
               ((TreeTableColumn)var6).sortTypeProperty().removeListener(var3);
            }
         }

      }
   }

   static void addTableColumnListener(List var0, InvalidationListener var1, InvalidationListener var2, InvalidationListener var3, InvalidationListener var4) {
      if (var0 != null) {
         TableColumnBase var6;
         for(Iterator var5 = var0.iterator(); var5.hasNext(); addTableColumnListener(var6.getColumns(), var1, var2, var3, var4)) {
            var6 = (TableColumnBase)var5.next();
            var6.visibleProperty().addListener(var1);
            var6.sortableProperty().addListener(var2);
            var6.comparatorProperty().addListener(var4);
            if (var6 instanceof TableColumn) {
               ((TableColumn)var6).sortTypeProperty().addListener(var3);
            } else if (var6 instanceof TreeTableColumn) {
               ((TreeTableColumn)var6).sortTypeProperty().addListener(var3);
            }
         }

      }
   }

   static void removeColumnsListener(List var0, ListChangeListener var1) {
      if (var0 != null) {
         Iterator var2 = var0.iterator();

         while(var2.hasNext()) {
            TableColumnBase var3 = (TableColumnBase)var2.next();
            var3.getColumns().removeListener(var1);
            removeColumnsListener(var3.getColumns(), var1);
         }

      }
   }

   static void addColumnsListener(List var0, ListChangeListener var1) {
      if (var0 != null) {
         Iterator var2 = var0.iterator();

         while(var2.hasNext()) {
            TableColumnBase var3 = (TableColumnBase)var2.next();
            var3.getColumns().addListener(var1);
            addColumnsListener(var3.getColumns(), var1);
         }

      }
   }

   static void handleSortFailure(ObservableList var0, SortEventType var1, Object... var2) {
      if (var1 == TableUtil.SortEventType.COLUMN_SORT_TYPE_CHANGE) {
         TableColumnBase var3 = (TableColumnBase)var2[0];
         revertSortType(var3);
      } else if (var1 == TableUtil.SortEventType.SORT_ORDER_CHANGE) {
         ListChangeListener.Change var6 = (ListChangeListener.Change)var2[0];
         ArrayList var4 = new ArrayList();
         ArrayList var5 = new ArrayList();

         while(var6.next()) {
            if (var6.wasAdded()) {
               var4.addAll(var6.getAddedSubList());
            }

            if (var6.wasRemoved()) {
               var5.addAll(var6.getRemoved());
            }
         }

         var0.removeAll(var4);
         var0.addAll(var5);
      } else if (var1 != TableUtil.SortEventType.COLUMN_SORTABLE_CHANGE && var1 == TableUtil.SortEventType.COLUMN_COMPARATOR_CHANGE) {
      }

   }

   private static void revertSortType(TableColumnBase var0) {
      if (var0 instanceof TableColumn) {
         TableColumn var1 = (TableColumn)var0;
         TableColumn.SortType var2 = var1.getSortType();
         if (var2 == TableColumn.SortType.ASCENDING) {
            var1.setSortType((TableColumn.SortType)null);
         } else if (var2 == TableColumn.SortType.DESCENDING) {
            var1.setSortType(TableColumn.SortType.ASCENDING);
         } else if (var2 == null) {
            var1.setSortType(TableColumn.SortType.DESCENDING);
         }
      } else if (var0 instanceof TreeTableColumn) {
         TreeTableColumn var3 = (TreeTableColumn)var0;
         TreeTableColumn.SortType var4 = var3.getSortType();
         if (var4 == TreeTableColumn.SortType.ASCENDING) {
            var3.setSortType((TreeTableColumn.SortType)null);
         } else if (var4 == TreeTableColumn.SortType.DESCENDING) {
            var3.setSortType(TreeTableColumn.SortType.ASCENDING);
         } else if (var4 == null) {
            var3.setSortType(TreeTableColumn.SortType.DESCENDING);
         }
      }

   }

   static boolean constrainedResize(ResizeFeaturesBase var0, boolean var1, double var2, List var4) {
      TableColumnBase var5 = var0.getColumn();
      double var6 = var0.getDelta();
      double var11 = 0.0;
      double var13 = 0.0;
      if (var2 == 0.0) {
         return false;
      } else {
         double var15 = 0.0;

         Iterator var17;
         TableColumnBase var18;
         for(var17 = var4.iterator(); var17.hasNext(); var15 += var18.getWidth()) {
            var18 = (TableColumnBase)var17.next();
         }

         boolean var8;
         double var25;
         if (Math.abs(var15 - var2) > 1.0) {
            var8 = var15 > var2;
            double var9 = var2;
            if (var1) {
               for(var17 = var4.iterator(); var17.hasNext(); var13 += var18.getMaxWidth()) {
                  var18 = (TableColumnBase)var17.next();
                  var11 += var18.getMinWidth();
               }

               var13 = var13 == Double.POSITIVE_INFINITY ? Double.MAX_VALUE : (var13 == Double.NEGATIVE_INFINITY ? Double.MIN_VALUE : var13);

               double var21;
               for(var17 = var4.iterator(); var17.hasNext(); var13 -= var21) {
                  var18 = (TableColumnBase)var17.next();
                  double var19 = var18.getMinWidth();
                  var21 = var18.getMaxWidth();
                  double var23;
                  if (Math.abs(var11 - var13) < 1.0E-7) {
                     var23 = var19;
                  } else {
                     var25 = (var9 - var11) / (var13 - var11);
                     var23 = (double)Math.round(var19 + var25 * (var21 - var19));
                  }

                  var25 = resize(var18, var23 - var18.getWidth());
                  var9 -= var23 + var25;
                  var11 -= var19;
               }

               var1 = false;
            } else {
               double var31 = var2 - var15;
               resizeColumns(var4, var31);
            }
         }

         if (var5 == null) {
            return false;
         } else {
            var8 = var6 < 0.0;

            TableColumnBase var32;
            for(var32 = var5; var32.getColumns().size() > 0; var32 = (TableColumnBase)var32.getColumns().get(var32.getColumns().size() - 1)) {
            }

            int var33 = var4.indexOf(var32);
            int var34 = var4.size() - 1;
            double var20 = var6;

            while(var34 > var33 && var20 != 0.0) {
               TableColumnBase var22 = (TableColumnBase)var4.get(var34);
               --var34;
               if (var22.isResizable()) {
                  TableColumnBase var35 = var8 ? var32 : var22;
                  TableColumnBase var24 = !var8 ? var32 : var22;
                  if (var24.getWidth() > var24.getPrefWidth()) {
                     List var36 = var4.subList(var33 + 1, var34 + 1);

                     for(int var26 = var36.size() - 1; var26 >= 0; --var26) {
                        TableColumnBase var27 = (TableColumnBase)var36.get(var26);
                        if (var27.getWidth() < var27.getPrefWidth()) {
                           var24 = var27;
                           break;
                        }
                     }
                  }

                  var25 = Math.min(Math.abs(var20), var35.getWidth() - var35.getMinWidth());
                  resize(var35, -var25);
                  resize(var24, var25);
                  var20 += var8 ? var25 : -var25;
               }
            }

            return var20 == 0.0;
         }
      }
   }

   static double resize(TableColumnBase var0, double var1) {
      if (var1 == 0.0) {
         return 0.0;
      } else if (!var0.isResizable()) {
         return var1;
      } else {
         boolean var3 = var1 < 0.0;
         List var4 = getResizableChildren(var0, var3);
         if (var4.size() > 0) {
            return resizeColumns(var4, var1);
         } else {
            double var5 = var0.getWidth() + var1;
            if (var5 > var0.getMaxWidth()) {
               var0.impl_setWidth(var0.getMaxWidth());
               return var5 - var0.getMaxWidth();
            } else if (var5 < var0.getMinWidth()) {
               var0.impl_setWidth(var0.getMinWidth());
               return var5 - var0.getMinWidth();
            } else {
               var0.impl_setWidth(var5);
               return 0.0;
            }
         }
      }
   }

   private static List getResizableChildren(TableColumnBase var0, boolean var1) {
      if (var0 != null && !var0.getColumns().isEmpty()) {
         ArrayList var2 = new ArrayList();
         Iterator var3 = var0.getColumns().iterator();

         while(true) {
            while(true) {
               TableColumnBase var4;
               do {
                  do {
                     if (!var3.hasNext()) {
                        return var2;
                     }

                     var4 = (TableColumnBase)var3.next();
                  } while(!var4.isVisible());
               } while(!var4.isResizable());

               if (var1 && var4.getWidth() > var4.getMinWidth()) {
                  var2.add(var4);
               } else if (!var1 && var4.getWidth() < var4.getMaxWidth()) {
                  var2.add(var4);
               }
            }
         }
      } else {
         return Collections.emptyList();
      }
   }

   private static double resizeColumns(List var0, double var1) {
      int var3 = var0.size();
      double var4 = var1 / (double)var3;
      double var6 = var1;
      int var8 = 0;
      boolean var9 = true;
      Iterator var10 = var0.iterator();

      while(var10.hasNext()) {
         TableColumnBase var11 = (TableColumnBase)var10.next();
         ++var8;
         double var12 = resize(var11, var4);
         var6 = var6 - var4 + var12;
         if (var12 != 0.0) {
            var9 = false;
            var4 = var6 / (double)(var3 - var8);
         }
      }

      return var9 ? 0.0 : var6;
   }

   static enum SortEventType {
      SORT_ORDER_CHANGE,
      COLUMN_SORT_TYPE_CHANGE,
      COLUMN_SORTABLE_CHANGE,
      COLUMN_COMPARATOR_CHANGE;
   }
}
