package javafx.beans.value;

import javafx.beans.Observable;

public interface ObservableValue extends Observable {
   void addListener(ChangeListener var1);

   void removeListener(ChangeListener var1);

   Object getValue();
}
