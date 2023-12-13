package javafx.scene;

import java.time.LocalDate;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.scene.input.KeyCombination;
import javafx.scene.text.Font;

public enum AccessibleAttribute {
   ACCELERATOR(KeyCombination.class),
   BOUNDS(Bounds.class),
   BOUNDS_FOR_RANGE(Bounds[].class),
   CARET_OFFSET(Integer.class),
   CHILDREN(ObservableList.class),
   COLUMN_AT_INDEX(Node.class),
   CELL_AT_ROW_COLUMN(Node.class),
   COLUMN_COUNT(Integer.class),
   COLUMN_INDEX(Integer.class),
   CONTENTS(Node.class),
   DISABLED(Boolean.class),
   DISCLOSURE_LEVEL(Integer.class),
   DATE(LocalDate.class),
   EDITABLE(Boolean.class),
   EXPANDED(Boolean.class),
   FOCUS_ITEM(Node.class),
   FOCUS_NODE(Node.class),
   FOCUSED(Boolean.class),
   FONT(Font.class),
   HEADER(Node.class),
   HELP(String.class),
   HORIZONTAL_SCROLLBAR(Node.class),
   INDETERMINATE(Boolean.class),
   ITEM_AT_INDEX(Node.class),
   ITEM_COUNT(Integer.class),
   INDEX(Integer.class),
   LABELED_BY(Node.class),
   LEAF(Boolean.class),
   LINE_END(Integer.class),
   LINE_FOR_OFFSET(Integer.class),
   LINE_START(Integer.class),
   MIN_VALUE(Double.class),
   MAX_VALUE(Double.class),
   MNEMONIC(String.class),
   MULTIPLE_SELECTION(Boolean.class),
   NODE_AT_POINT(Node.class),
   OFFSET_AT_POINT(Integer.class),
   ORIENTATION(Orientation.class),
   OVERFLOW_BUTTON(Node.class),
   PARENT(Parent.class),
   PARENT_MENU(Node.class),
   ROLE(AccessibleRole.class),
   ROLE_DESCRIPTION(String.class),
   ROW_AT_INDEX(Node.class),
   ROW_COUNT(Integer.class),
   ROW_INDEX(Integer.class),
   SCENE(Scene.class),
   SELECTED(Boolean.class),
   SELECTED_ITEMS(ObservableList.class),
   SELECTION_END(Integer.class),
   SELECTION_START(Integer.class),
   SUBMENU(Node.class),
   TEXT(String.class),
   TREE_ITEM_AT_INDEX(Node.class),
   TREE_ITEM_COUNT(Integer.class),
   TREE_ITEM_PARENT(Node.class),
   VALUE(Double.class),
   VERTICAL_SCROLLBAR(Node.class),
   VISIBLE(Boolean.class),
   VISITED(Boolean.class);

   private Class returnClass;

   private AccessibleAttribute(Class var3) {
      this.returnClass = var3;
   }

   public Class getReturnType() {
      return this.returnClass;
   }
}
