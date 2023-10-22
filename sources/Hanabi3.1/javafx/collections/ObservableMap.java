package javafx.collections;

import java.util.Map;
import javafx.beans.Observable;

public interface ObservableMap extends Map, Observable {
   void addListener(MapChangeListener var1);

   void removeListener(MapChangeListener var1);
}
