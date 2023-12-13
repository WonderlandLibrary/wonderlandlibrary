package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.util.Builder;
import javafx.util.Callback;

/** @deprecated */
@Deprecated
public class TableColumnBuilder implements Builder {
   private int __set;
   private Callback cellFactory;
   private Callback cellValueFactory;
   private Collection columns;
   private Comparator comparator;
   private ContextMenu contextMenu;
   private boolean editable;
   private Node graphic;
   private String id;
   private double maxWidth;
   private double minWidth;
   private EventHandler onEditCancel;
   private EventHandler onEditCommit;
   private EventHandler onEditStart;
   private double prefWidth;
   private boolean resizable;
   private boolean sortable;
   private Node sortNode;
   private TableColumn.SortType sortType;
   private String style;
   private Collection styleClass;
   private String text;
   private Object userData;
   private boolean visible;

   protected TableColumnBuilder() {
   }

   public static TableColumnBuilder create() {
      return new TableColumnBuilder();
   }

   private void __set(int var1) {
      this.__set |= 1 << var1;
   }

   public void applyTo(TableColumn var1) {
      int var2 = this.__set;

      while(var2 != 0) {
         int var3 = Integer.numberOfTrailingZeros(var2);
         var2 &= ~(1 << var3);
         switch (var3) {
            case 0:
               var1.setCellFactory(this.cellFactory);
               break;
            case 1:
               var1.setCellValueFactory(this.cellValueFactory);
               break;
            case 2:
               var1.getColumns().addAll(this.columns);
               break;
            case 3:
               var1.setComparator(this.comparator);
               break;
            case 4:
               var1.setContextMenu(this.contextMenu);
               break;
            case 5:
               var1.setEditable(this.editable);
               break;
            case 6:
               var1.setGraphic(this.graphic);
               break;
            case 7:
               var1.setId(this.id);
               break;
            case 8:
               var1.setMaxWidth(this.maxWidth);
               break;
            case 9:
               var1.setMinWidth(this.minWidth);
               break;
            case 10:
               var1.setOnEditCancel(this.onEditCancel);
               break;
            case 11:
               var1.setOnEditCommit(this.onEditCommit);
               break;
            case 12:
               var1.setOnEditStart(this.onEditStart);
               break;
            case 13:
               var1.setPrefWidth(this.prefWidth);
               break;
            case 14:
               var1.setResizable(this.resizable);
               break;
            case 15:
               var1.setSortable(this.sortable);
               break;
            case 16:
               var1.setSortNode(this.sortNode);
               break;
            case 17:
               var1.setSortType(this.sortType);
               break;
            case 18:
               var1.setStyle(this.style);
               break;
            case 19:
               var1.getStyleClass().addAll(this.styleClass);
               break;
            case 20:
               var1.setText(this.text);
               break;
            case 21:
               var1.setUserData(this.userData);
               break;
            case 22:
               var1.setVisible(this.visible);
         }
      }

   }

   public TableColumnBuilder cellFactory(Callback var1) {
      this.cellFactory = var1;
      this.__set(0);
      return this;
   }

   public TableColumnBuilder cellValueFactory(Callback var1) {
      this.cellValueFactory = var1;
      this.__set(1);
      return this;
   }

   public TableColumnBuilder columns(Collection var1) {
      this.columns = var1;
      this.__set(2);
      return this;
   }

   public TableColumnBuilder columns(TableColumn... var1) {
      return this.columns((Collection)Arrays.asList(var1));
   }

   public TableColumnBuilder comparator(Comparator var1) {
      this.comparator = var1;
      this.__set(3);
      return this;
   }

   public TableColumnBuilder contextMenu(ContextMenu var1) {
      this.contextMenu = var1;
      this.__set(4);
      return this;
   }

   public TableColumnBuilder editable(boolean var1) {
      this.editable = var1;
      this.__set(5);
      return this;
   }

   public TableColumnBuilder graphic(Node var1) {
      this.graphic = var1;
      this.__set(6);
      return this;
   }

   public TableColumnBuilder id(String var1) {
      this.id = var1;
      this.__set(7);
      return this;
   }

   public TableColumnBuilder maxWidth(double var1) {
      this.maxWidth = var1;
      this.__set(8);
      return this;
   }

   public TableColumnBuilder minWidth(double var1) {
      this.minWidth = var1;
      this.__set(9);
      return this;
   }

   public TableColumnBuilder onEditCancel(EventHandler var1) {
      this.onEditCancel = var1;
      this.__set(10);
      return this;
   }

   public TableColumnBuilder onEditCommit(EventHandler var1) {
      this.onEditCommit = var1;
      this.__set(11);
      return this;
   }

   public TableColumnBuilder onEditStart(EventHandler var1) {
      this.onEditStart = var1;
      this.__set(12);
      return this;
   }

   public TableColumnBuilder prefWidth(double var1) {
      this.prefWidth = var1;
      this.__set(13);
      return this;
   }

   public TableColumnBuilder resizable(boolean var1) {
      this.resizable = var1;
      this.__set(14);
      return this;
   }

   public TableColumnBuilder sortable(boolean var1) {
      this.sortable = var1;
      this.__set(15);
      return this;
   }

   public TableColumnBuilder sortNode(Node var1) {
      this.sortNode = var1;
      this.__set(16);
      return this;
   }

   public TableColumnBuilder sortType(TableColumn.SortType var1) {
      this.sortType = var1;
      this.__set(17);
      return this;
   }

   public TableColumnBuilder style(String var1) {
      this.style = var1;
      this.__set(18);
      return this;
   }

   public TableColumnBuilder styleClass(Collection var1) {
      this.styleClass = var1;
      this.__set(19);
      return this;
   }

   public TableColumnBuilder styleClass(String... var1) {
      return this.styleClass((Collection)Arrays.asList(var1));
   }

   public TableColumnBuilder text(String var1) {
      this.text = var1;
      this.__set(20);
      return this;
   }

   public TableColumnBuilder userData(Object var1) {
      this.userData = var1;
      this.__set(21);
      return this;
   }

   public TableColumnBuilder visible(boolean var1) {
      this.visible = var1;
      this.__set(22);
      return this;
   }

   public TableColumn build() {
      TableColumn var1 = new TableColumn();
      this.applyTo(var1);
      return var1;
   }
}
