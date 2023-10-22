package com.sun.javafx.scene.control;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TreeTableColumn;

public class TableColumnSortTypeWrapper {
   public static boolean isAscending(TableColumnBase var0) {
      String var1 = getSortTypeName(var0);
      return "ASCENDING".equals(var1);
   }

   public static boolean isDescending(TableColumnBase var0) {
      String var1 = getSortTypeName(var0);
      return "DESCENDING".equals(var1);
   }

   public static void setSortType(TableColumnBase var0, TableColumn.SortType var1) {
      if (var0 instanceof TableColumn) {
         TableColumn var2 = (TableColumn)var0;
         var2.setSortType(var1);
      } else if (var0 instanceof TreeTableColumn) {
         TreeTableColumn var3 = (TreeTableColumn)var0;
         if (var1 == TableColumn.SortType.ASCENDING) {
            var3.setSortType(TreeTableColumn.SortType.ASCENDING);
         } else if (var1 == TableColumn.SortType.DESCENDING) {
            var3.setSortType(TreeTableColumn.SortType.DESCENDING);
         } else if (var1 == null) {
            var3.setSortType((TreeTableColumn.SortType)null);
         }
      }

   }

   public static String getSortTypeName(TableColumnBase var0) {
      if (var0 instanceof TableColumn) {
         TableColumn var3 = (TableColumn)var0;
         TableColumn.SortType var4 = var3.getSortType();
         return var4 == null ? null : var4.name();
      } else if (var0 instanceof TreeTableColumn) {
         TreeTableColumn var1 = (TreeTableColumn)var0;
         TreeTableColumn.SortType var2 = var1.getSortType();
         return var2 == null ? null : var2.name();
      } else {
         return null;
      }
   }

   public static ObservableValue getSortTypeProperty(TableColumnBase var0) {
      if (var0 instanceof TableColumn) {
         return ((TableColumn)var0).sortTypeProperty();
      } else {
         return var0 instanceof TreeTableColumn ? ((TreeTableColumn)var0).sortTypeProperty() : null;
      }
   }
}
