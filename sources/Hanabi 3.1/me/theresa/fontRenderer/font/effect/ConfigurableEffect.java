package me.theresa.fontRenderer.font.effect;

import java.util.List;

public interface ConfigurableEffect extends Effect {
   List getValues();

   void setValues(List var1);

   public interface Value {
      String getName();

      String getString();

      void setString(String var1);

      Object getObject();

      void showDialog();
   }
}
