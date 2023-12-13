package javafx.collections;

import com.sun.javafx.collections.ListListenerHelper;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javafx.beans.InvalidationListener;

public abstract class ObservableListBase extends AbstractList implements ObservableList {
   private ListListenerHelper listenerHelper;
   private final ListChangeBuilder changeBuilder = new ListChangeBuilder(this);

   protected final void nextUpdate(int var1) {
      this.changeBuilder.nextUpdate(var1);
   }

   protected final void nextSet(int var1, Object var2) {
      this.changeBuilder.nextSet(var1, var2);
   }

   protected final void nextReplace(int var1, int var2, List var3) {
      this.changeBuilder.nextReplace(var1, var2, var3);
   }

   protected final void nextRemove(int var1, List var2) {
      this.changeBuilder.nextRemove(var1, var2);
   }

   protected final void nextRemove(int var1, Object var2) {
      this.changeBuilder.nextRemove(var1, var2);
   }

   protected final void nextPermutation(int var1, int var2, int[] var3) {
      this.changeBuilder.nextPermutation(var1, var2, var3);
   }

   protected final void nextAdd(int var1, int var2) {
      this.changeBuilder.nextAdd(var1, var2);
   }

   protected final void beginChange() {
      this.changeBuilder.beginChange();
   }

   protected final void endChange() {
      this.changeBuilder.endChange();
   }

   public final void addListener(InvalidationListener var1) {
      this.listenerHelper = ListListenerHelper.addListener(this.listenerHelper, var1);
   }

   public final void removeListener(InvalidationListener var1) {
      this.listenerHelper = ListListenerHelper.removeListener(this.listenerHelper, var1);
   }

   public final void addListener(ListChangeListener var1) {
      this.listenerHelper = ListListenerHelper.addListener(this.listenerHelper, var1);
   }

   public final void removeListener(ListChangeListener var1) {
      this.listenerHelper = ListListenerHelper.removeListener(this.listenerHelper, var1);
   }

   protected final void fireChange(ListChangeListener.Change var1) {
      ListListenerHelper.fireValueChangedEvent(this.listenerHelper, var1);
   }

   protected final boolean hasListeners() {
      return ListListenerHelper.hasListeners(this.listenerHelper);
   }

   public boolean addAll(Object... var1) {
      return this.addAll(Arrays.asList(var1));
   }

   public boolean setAll(Object... var1) {
      return this.setAll((Collection)Arrays.asList(var1));
   }

   public boolean setAll(Collection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean removeAll(Object... var1) {
      return this.removeAll(Arrays.asList(var1));
   }

   public boolean retainAll(Object... var1) {
      return this.retainAll(Arrays.asList(var1));
   }

   public void remove(int var1, int var2) {
      this.removeRange(var1, var2);
   }
}
