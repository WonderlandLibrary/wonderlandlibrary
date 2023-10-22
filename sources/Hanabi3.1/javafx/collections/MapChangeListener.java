package javafx.collections;

@FunctionalInterface
public interface MapChangeListener {
   void onChanged(Change var1);

   public abstract static class Change {
      private final ObservableMap map;

      public Change(ObservableMap var1) {
         this.map = var1;
      }

      public ObservableMap getMap() {
         return this.map;
      }

      public abstract boolean wasAdded();

      public abstract boolean wasRemoved();

      public abstract Object getKey();

      public abstract Object getValueAdded();

      public abstract Object getValueRemoved();
   }
}
