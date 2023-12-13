package org.apache.logging.log4j.message;

import java.lang.Thread.State;

class BasicThreadInformation implements ThreadInformation {
   private static final int HASH_SHIFT = 32;
   private static final int HASH_MULTIPLIER = 31;
   private final long id;
   private final String name;
   private final String longName;
   private final State state;
   private final int priority;
   private final boolean isAlive;
   private final boolean isDaemon;
   private final String threadGroupName;

   public BasicThreadInformation(Thread var1) {
      this.id = var1.getId();
      this.name = var1.getName();
      this.longName = var1.toString();
      this.state = var1.getState();
      this.priority = var1.getPriority();
      this.isAlive = var1.isAlive();
      this.isDaemon = var1.isDaemon();
      ThreadGroup var2 = var1.getThreadGroup();
      this.threadGroupName = var2 == null ? null : var2.getName();
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         BasicThreadInformation var2 = (BasicThreadInformation)var1;
         if (this.id != var2.id) {
            return false;
         } else {
            if (this.name != null) {
               if (!this.name.equals(var2.name)) {
                  return false;
               }
            } else if (var2.name != null) {
               return false;
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int var1 = (int)(this.id ^ this.id >>> 32);
      var1 = 31 * var1 + (this.name != null ? this.name.hashCode() : 0);
      return var1;
   }

   public void printThreadInfo(StringBuilder var1) {
      var1.append("\"").append(this.name).append("\" ");
      if (this.isDaemon) {
         var1.append("daemon ");
      }

      var1.append("prio=").append(this.priority).append(" tid=").append(this.id).append(" ");
      if (this.threadGroupName != null) {
         var1.append("group=\"").append(this.threadGroupName).append("\"");
      }

      var1.append("\n");
      var1.append("\tThread state: ").append(this.state.name()).append("\n");
   }

   public void printStack(StringBuilder var1, StackTraceElement[] var2) {
      StackTraceElement[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         StackTraceElement var6 = var3[var5];
         var1.append("\tat ").append(var6).append("\n");
      }

   }
}
