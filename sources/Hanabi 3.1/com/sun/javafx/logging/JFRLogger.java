package com.sun.javafx.logging;

import com.oracle.jrockit.jfr.EventToken;
import com.oracle.jrockit.jfr.FlightRecorder;
import com.oracle.jrockit.jfr.Producer;

class JFRLogger extends Logger {
   private static final String PRODUCER_URI = "http://www.oracle.com/technetwork/java/javafx/index.html";
   private static JFRLogger jfrLogger;
   private final Producer producer = new Producer("JavaFX producer", "JavaFX producer.", "http://www.oracle.com/technetwork/java/javafx/index.html");
   private final EventToken pulseEventToken;
   private final EventToken inputEventToken;
   private final ThreadLocal curPhaseEvent;
   private final ThreadLocal curInputEvent;
   private int pulseNumber;
   private int fxPulseNumber;
   private int renderPulseNumber;
   private Thread fxThread;

   private JFRLogger() throws Exception {
      this.pulseEventToken = this.producer.addEvent(JFRPulseEvent.class);
      this.inputEventToken = this.producer.addEvent(JFRInputEvent.class);
      this.producer.register();
      this.curPhaseEvent = new ThreadLocal() {
         public JFRPulseEvent initialValue() {
            return new JFRPulseEvent(JFRLogger.this.pulseEventToken);
         }
      };
      this.curInputEvent = new ThreadLocal() {
         public JFRInputEvent initialValue() {
            return new JFRInputEvent(JFRLogger.this.inputEventToken);
         }
      };
   }

   public static JFRLogger getInstance() {
      if (jfrLogger == null) {
         try {
            Class var0 = Class.forName("com.oracle.jrockit.jfr.FlightRecorder");
            if (var0 != null && FlightRecorder.isActive()) {
               jfrLogger = new JFRLogger();
            }
         } catch (Exception var1) {
            jfrLogger = null;
         }
      }

      return jfrLogger;
   }

   public void pulseStart() {
      ++this.pulseNumber;
      this.fxPulseNumber = this.pulseNumber;
      if (this.fxThread == null) {
         this.fxThread = Thread.currentThread();
      }

      this.newPhase("Pulse start");
   }

   public void pulseEnd() {
      this.newPhase((String)null);
      this.fxPulseNumber = 0;
   }

   public void renderStart() {
      this.renderPulseNumber = this.fxPulseNumber;
   }

   public void renderEnd() {
      this.newPhase((String)null);
      this.renderPulseNumber = 0;
   }

   public void newPhase(String var1) {
      if (this.pulseEventToken != null) {
         JFRPulseEvent var2 = (JFRPulseEvent)this.curPhaseEvent.get();
         if (!this.pulseEventToken.isEnabled()) {
            var2.setPhase((String)null);
         } else {
            if (var2.getPhase() != null) {
               var2.end();
               var2.commit();
            }

            if (var1 == null) {
               var2.setPhase((String)null);
            } else {
               var2.reset();
               var2.begin();
               var2.setPhase(var1);
               var2.setPulseNumber(Thread.currentThread() == this.fxThread ? this.fxPulseNumber : this.renderPulseNumber);
            }
         }
      }
   }

   public void newInput(String var1) {
      if (this.inputEventToken != null) {
         JFRInputEvent var2 = (JFRInputEvent)this.curInputEvent.get();
         if (!this.inputEventToken.isEnabled()) {
            var2.setInput((String)null);
         } else {
            if (var2.getInput() != null) {
               var2.end();
               var2.commit();
            }

            if (var1 == null) {
               var2.setInput((String)null);
            } else {
               var2.reset();
               var2.begin();
               var2.setInput(var1);
            }
         }
      }
   }
}
