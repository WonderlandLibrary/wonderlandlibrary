package com.sun.webkit;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class EventLoop {
   private static final Logger logger = Logger.getLogger(EventLoop.class.getName());
   private static EventLoop instance;

   public static void setEventLoop(EventLoop var0) {
      instance = var0;
   }

   private static void fwkCycle() {
      logger.log(Level.FINE, "Executing event loop cycle");
      instance.cycle();
   }

   protected abstract void cycle();
}
