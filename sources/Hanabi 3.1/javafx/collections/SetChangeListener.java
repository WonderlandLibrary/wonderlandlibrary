package javafx.collections;

@FunctionalInterface
public interface SetChangeListener {
   void onChanged(Change var1);

   public abstract static class Change {
      private ObservableSet set;

      public Change(ObservableSet var1) {
         this.set = var1;
      }

      public ObservableSet getSet() {
         return this.set;
      }

      public abstract boolean wasAdded();

      public abstract boolean wasRemoved();

      public abstract Object getElementAdded();

      public abstract Object getElementRemoved();
   }
}
