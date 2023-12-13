package com.sun.javafx.logging;

import com.oracle.jrockit.jfr.ContentType;
import com.oracle.jrockit.jfr.EventDefinition;
import com.oracle.jrockit.jfr.EventToken;
import com.oracle.jrockit.jfr.TimedEvent;
import com.oracle.jrockit.jfr.ValueDefinition;

@EventDefinition(
   path = "javafx/pulse",
   name = "JavaFX Pulse Phase",
   description = "Describes a phase in JavaFX pulse processing",
   stacktrace = false,
   thread = true
)
public class JFRPulseEvent extends TimedEvent {
   @ValueDefinition(
      name = "pulseID",
      description = "Pulse number",
      contentType = ContentType.None,
      relationKey = "http://www.oracle.com/javafx/pulse/id"
   )
   private int pulseNumber;
   @ValueDefinition(
      name = "phaseName",
      description = "Pulse phase name",
      contentType = ContentType.None
   )
   private String phase;

   public JFRPulseEvent(EventToken var1) {
      super(var1);
   }

   public int getPulseNumber() {
      return this.pulseNumber;
   }

   public void setPulseNumber(int var1) {
      this.pulseNumber = var1;
   }

   public String getPhase() {
      return this.phase;
   }

   public void setPhase(String var1) {
      this.phase = var1;
   }
}
