package javafx.scene.control;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public final class IndexRangeBuilder implements Builder {
   private int end;
   private int start;

   protected IndexRangeBuilder() {
   }

   public static IndexRangeBuilder create() {
      return new IndexRangeBuilder();
   }

   public IndexRangeBuilder end(int var1) {
      this.end = var1;
      return this;
   }

   public IndexRangeBuilder start(int var1) {
      this.start = var1;
      return this;
   }

   public IndexRange build() {
      IndexRange var1 = new IndexRange(this.start, this.end);
      return var1;
   }
}
