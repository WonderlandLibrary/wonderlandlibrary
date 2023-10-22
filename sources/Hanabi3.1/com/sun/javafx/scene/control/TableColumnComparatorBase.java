package com.sun.javafx.scene.control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TreeTableColumn;

public abstract class TableColumnComparatorBase implements Comparator {
   private final List columns;

   public TableColumnComparatorBase(TableColumnBase... var1) {
      this(Arrays.asList(var1));
   }

   public TableColumnComparatorBase(List var1) {
      this.columns = new ArrayList(var1);
   }

   public List getColumns() {
      return Collections.unmodifiableList(this.columns);
   }

   public int compare(Object var1, Object var2) {
      Iterator var3 = this.columns.iterator();

      while(var3.hasNext()) {
         TableColumnBase var4 = (TableColumnBase)var3.next();
         if (this.isSortable(var4)) {
            Object var5 = var4.getCellData(var1);
            Object var6 = var4.getCellData(var2);
            int var7 = this.doCompare(var4, var5, var6);
            if (var7 != 0) {
               return var7;
            }
         }
      }

      return 0;
   }

   public int hashCode() {
      int var1 = 7;
      var1 = 59 * var1 + (this.columns != null ? this.columns.hashCode() : 0);
      return var1;
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (this.getClass() != var1.getClass()) {
         return false;
      } else {
         TableColumnComparatorBase var2 = (TableColumnComparatorBase)var1;
         return this.columns == var2.columns || this.columns != null && this.columns.equals(var2.columns);
      }
   }

   public String toString() {
      return "TableColumnComparatorBase [ columns: " + this.getColumns() + "] ";
   }

   public abstract boolean isSortable(TableColumnBase var1);

   public abstract int doCompare(TableColumnBase var1, Object var2, Object var3);

   public static final class TreeTableColumnComparator extends TableColumnComparatorBase {
      public TreeTableColumnComparator(TreeTableColumn... var1) {
         super(Arrays.asList(var1));
      }

      public TreeTableColumnComparator(List var1) {
         super(var1);
      }

      public boolean isSortable(TableColumnBase var1) {
         TreeTableColumn var2 = (TreeTableColumn)var1;
         return var2.getSortType() != null && var2.isSortable();
      }

      public int doCompare(TableColumnBase var1, Object var2, Object var3) {
         TreeTableColumn var4 = (TreeTableColumn)var1;
         Comparator var5 = var4.getComparator();
         switch (var4.getSortType()) {
            case ASCENDING:
               return var5.compare(var2, var3);
            case DESCENDING:
               return var5.compare(var3, var2);
            default:
               return 0;
         }
      }
   }

   public static final class TableColumnComparator extends TableColumnComparatorBase {
      public TableColumnComparator(TableColumn... var1) {
         super(Arrays.asList(var1));
      }

      public TableColumnComparator(List var1) {
         super(var1);
      }

      public boolean isSortable(TableColumnBase var1) {
         TableColumn var2 = (TableColumn)var1;
         return var2.getSortType() != null && var2.isSortable();
      }

      public int doCompare(TableColumnBase var1, Object var2, Object var3) {
         TableColumn var4 = (TableColumn)var1;
         Comparator var5 = var4.getComparator();
         switch (var4.getSortType()) {
            case ASCENDING:
               return var5.compare(var2, var3);
            case DESCENDING:
               return var5.compare(var3, var2);
            default:
               return 0;
         }
      }
   }
}
