package javafx.collections;

import java.util.Set;
import javafx.beans.Observable;

public interface ObservableSet extends Set, Observable {
   void addListener(SetChangeListener var1);

   void removeListener(SetChangeListener var1);
}
