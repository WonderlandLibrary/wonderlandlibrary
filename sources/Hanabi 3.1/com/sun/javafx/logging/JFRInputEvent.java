package com.sun.javafx.logging;

import com.oracle.jrockit.jfr.ContentType;
import com.oracle.jrockit.jfr.EventDefinition;
import com.oracle.jrockit.jfr.EventToken;
import com.oracle.jrockit.jfr.TimedEvent;
import com.oracle.jrockit.jfr.ValueDefinition;

@EventDefinition(
   path = "javafx/input",
   name = "JavaFX Input",
   description = "JavaFX input event",
   stacktrace = false,
   thread = true
)
public class JFRInputEvent extends TimedEvent {
   @ValueDefinition(
      name = "inputType",
      description = "Input event type",
      contentType = ContentType.None
   )
   private String input;

   public JFRInputEvent(EventToken var1) {
      super(var1);
   }

   public String getInput() {
      return this.input;
   }

   public void setInput(String var1) {
      this.input = var1;
   }
}
