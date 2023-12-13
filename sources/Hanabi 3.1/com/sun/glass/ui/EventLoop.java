package com.sun.glass.ui;

import java.util.ArrayDeque;
import java.util.Deque;

public final class EventLoop {
   private static final Deque stack = new ArrayDeque();
   private State state;
   private Object returnValue;

   EventLoop() {
      this.state = EventLoop.State.IDLE;
      Application.checkEventThread();
   }

   public State getState() {
      Application.checkEventThread();
      return this.state;
   }

   public Object enter() {
      Application.checkEventThread();
      if (!this.state.equals(EventLoop.State.IDLE)) {
         throw new IllegalStateException("The event loop object isn't idle");
      } else {
         this.state = EventLoop.State.ACTIVE;
         stack.push(this);

         Object var2;
         try {
            Object var1 = Application.enterNestedEventLoop();

            assert var1 == this : "Internal inconsistency - wrong EventLoop";

            assert stack.peek() == this : "Internal inconsistency - corrupted event loops stack";

            assert this.state.equals(EventLoop.State.LEAVING) : "The event loop isn't leaving";

            var2 = this.returnValue;
         } finally {
            this.returnValue = null;
            this.state = EventLoop.State.IDLE;
            stack.pop();
            if (!stack.isEmpty() && ((EventLoop)stack.peek()).state.equals(EventLoop.State.LEAVING)) {
               Application.invokeLater(() -> {
                  EventLoop var0 = (EventLoop)stack.peek();
                  if (var0 != null && var0.state.equals(EventLoop.State.LEAVING)) {
                     Application.leaveNestedEventLoop(var0);
                  }

               });
            }

         }

         return var2;
      }
   }

   public void leave(Object var1) {
      Application.checkEventThread();
      if (!this.state.equals(EventLoop.State.ACTIVE)) {
         throw new IllegalStateException("The event loop object isn't active");
      } else {
         this.state = EventLoop.State.LEAVING;
         this.returnValue = var1;
         if (stack.peek() == this) {
            Application.leaveNestedEventLoop(this);
         }

      }
   }

   public static enum State {
      IDLE,
      ACTIVE,
      LEAVING;
   }
}
