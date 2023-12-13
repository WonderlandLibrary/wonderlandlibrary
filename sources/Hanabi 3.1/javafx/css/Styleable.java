package javafx.css;

import java.util.List;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;

public interface Styleable {
   String getTypeSelector();

   String getId();

   ObservableList getStyleClass();

   String getStyle();

   List getCssMetaData();

   Styleable getStyleableParent();

   ObservableSet getPseudoClassStates();
}
