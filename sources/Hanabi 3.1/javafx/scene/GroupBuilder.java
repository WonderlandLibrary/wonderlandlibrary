package javafx.scene;

import java.util.Arrays;
import java.util.Collection;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class GroupBuilder extends ParentBuilder implements Builder {
   private int __set;
   private boolean autoSizeChildren;
   private Collection children;

   protected GroupBuilder() {
   }

   public static GroupBuilder create() {
      return new GroupBuilder();
   }

   public void applyTo(Group var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setAutoSizeChildren(this.autoSizeChildren);
      }

      if ((var2 & 2) != 0) {
         var1.getChildren().addAll(this.children);
      }

   }

   public GroupBuilder autoSizeChildren(boolean var1) {
      this.autoSizeChildren = var1;
      this.__set |= 1;
      return this;
   }

   public GroupBuilder children(Collection var1) {
      this.children = var1;
      this.__set |= 2;
      return this;
   }

   public GroupBuilder children(Node... var1) {
      return this.children((Collection)Arrays.asList(var1));
   }

   public Group build() {
      Group var1 = new Group();
      this.applyTo(var1);
      return var1;
   }
}
