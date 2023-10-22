package javafx.collections.transformation;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.collections.WeakListChangeListener;

public abstract class TransformationList extends ObservableListBase implements ObservableList {
   private ObservableList source;
   private ListChangeListener sourceListener;

   protected TransformationList(ObservableList var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         this.source = var1;
         var1.addListener(new WeakListChangeListener(this.getListener()));
      }
   }

   public final ObservableList getSource() {
      return this.source;
   }

   public final boolean isInTransformationChain(ObservableList var1) {
      if (this.source == var1) {
         return true;
      } else {
         ObservableList var2 = this.source;

         do {
            if (!(var2 instanceof TransformationList)) {
               return false;
            }

            var2 = ((TransformationList)var2).source;
         } while(var2 != var1);

         return true;
      }
   }

   private ListChangeListener getListener() {
      if (this.sourceListener == null) {
         this.sourceListener = (var1) -> {
            this.sourceChanged(var1);
         };
      }

      return this.sourceListener;
   }

   protected abstract void sourceChanged(ListChangeListener.Change var1);

   public abstract int getSourceIndex(int var1);

   public final int getSourceIndexFor(ObservableList var1, int var2) {
      if (!this.isInTransformationChain(var1)) {
         throw new IllegalArgumentException("Provided list is not in the transformation chain of thistransformation list");
      } else {
         ObservableList var3 = this.source;

         int var4;
         TransformationList var5;
         for(var4 = this.getSourceIndex(var2); var3 != var1 && var3 instanceof TransformationList; var3 = var5.source) {
            var5 = (TransformationList)var3;
            var4 = var5.getSourceIndex(var4);
         }

         return var4;
      }
   }
}
