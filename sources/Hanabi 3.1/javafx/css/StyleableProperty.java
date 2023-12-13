package javafx.css;

import javafx.beans.value.WritableValue;

public interface StyleableProperty extends WritableValue {
   void applyStyle(StyleOrigin var1, Object var2);

   StyleOrigin getStyleOrigin();

   CssMetaData getCssMetaData();
}
