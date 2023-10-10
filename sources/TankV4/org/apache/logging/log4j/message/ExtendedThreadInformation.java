package org.apache.logging.log4j.message;

import java.lang.Thread.State;
import java.lang.management.LockInfo;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;

class ExtendedThreadInformation implements ThreadInformation {
   private final ThreadInfo info;

   public ExtendedThreadInformation(ThreadInfo var1) {
      this.info = var1;
   }

   public void printThreadInfo(StringBuilder var1) {
      var1.append("\"").append(this.info.getThreadName()).append("\"");
      var1.append(" Id=").append(this.info.getThreadId()).append(" ");
      this.formatState(var1, this.info);
      if (this.info.isSuspended()) {
         var1.append(" (suspended)");
      }

      if (this.info.isInNative()) {
         var1.append(" (in native)");
      }

      var1.append('\n');
   }

   public void printStack(StringBuilder var1, StackTraceElement[] var2) {
      int var3 = 0;
      StackTraceElement[] var4 = var2;
      int var5 = var2.length;

      int var6;
      for(var6 = 0; var6 < var5; ++var6) {
         StackTraceElement var7 = var4[var6];
         var1.append("\tat ").append(var7.toString());
         var1.append('\n');
         if (var3 == 0 && this.info.getLockInfo() != null) {
            State var8 = this.info.getThreadState();
            switch(var8) {
            case BLOCKED:
               var1.append("\t-  blocked on ");
               this.formatLock(var1, this.info.getLockInfo());
               var1.append('\n');
               break;
            case WAITING:
               var1.append("\t-  waiting on ");
               this.formatLock(var1, this.info.getLockInfo());
               var1.append('\n');
               break;
            case TIMED_WAITING:
               var1.append("\t-  waiting on ");
               this.formatLock(var1, this.info.getLockInfo());
               var1.append('\n');
            }
         }

         MonitorInfo[] var15 = this.info.getLockedMonitors();
         int var9 = var15.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            MonitorInfo var11 = var15[var10];
            if (var11.getLockedStackDepth() == var3) {
               var1.append("\t-  locked ");
               this.formatLock(var1, var11);
               var1.append('\n');
            }
         }

         ++var3;
      }

      LockInfo[] var12 = this.info.getLockedSynchronizers();
      if (var12.length > 0) {
         var1.append("\n\tNumber of locked synchronizers = ").append(var12.length).append('\n');
         LockInfo[] var13 = var12;
         var6 = var12.length;

         for(int var14 = 0; var14 < var6; ++var14) {
            LockInfo var16 = var13[var14];
            var1.append("\t- ");
            this.formatLock(var1, var16);
            var1.append('\n');
         }
      }

   }

   private void formatLock(StringBuilder var1, LockInfo var2) {
      var1.append("<").append(var2.getIdentityHashCode()).append("> (a ");
      var1.append(var2.getClassName()).append(")");
   }

   private void formatState(StringBuilder var1, ThreadInfo var2) {
      State var3 = var2.getThreadState();
      var1.append(var3);
      StackTraceElement var4;
      String var5;
      String var6;
      switch(var3) {
      case BLOCKED:
         var1.append(" (on object monitor owned by \"");
         var1.append(var2.getLockOwnerName()).append("\" Id=").append(var2.getLockOwnerId()).append(")");
         break;
      case WAITING:
         var4 = var2.getStackTrace()[0];
         var5 = var4.getClassName();
         var6 = var4.getMethodName();
         if (var5.equals("java.lang.Object") && var6.equals("wait")) {
            var1.append(" (on object monitor");
            if (var2.getLockOwnerName() != null) {
               var1.append(" owned by \"");
               var1.append(var2.getLockOwnerName()).append("\" Id=").append(var2.getLockOwnerId());
            }

            var1.append(")");
         } else if (var5.equals("java.lang.Thread") && var6.equals("join")) {
            var1.append(" (on completion of thread ").append(var2.getLockOwnerId()).append(")");
         } else {
            var1.append(" (parking for lock");
            if (var2.getLockOwnerName() != null) {
               var1.append(" owned by \"");
               var1.append(var2.getLockOwnerName()).append("\" Id=").append(var2.getLockOwnerId());
            }

            var1.append(")");
         }
         break;
      case TIMED_WAITING:
         var4 = var2.getStackTrace()[0];
         var5 = var4.getClassName();
         var6 = var4.getMethodName();
         if (var5.equals("java.lang.Object") && var6.equals("wait")) {
            var1.append(" (on object monitor");
            if (var2.getLockOwnerName() != null) {
               var1.append(" owned by \"");
               var1.append(var2.getLockOwnerName()).append("\" Id=").append(var2.getLockOwnerId());
            }

            var1.append(")");
         } else if (var5.equals("java.lang.Thread") && var6.equals("sleep")) {
            var1.append(" (sleeping)");
         } else if (var5.equals("java.lang.Thread") && var6.equals("join")) {
            var1.append(" (on completion of thread ").append(var2.getLockOwnerId()).append(")");
         } else {
            var1.append(" (parking for lock");
            if (var2.getLockOwnerName() != null) {
               var1.append(" owned by \"");
               var1.append(var2.getLockOwnerName()).append("\" Id=").append(var2.getLockOwnerId());
            }

            var1.append(")");
         }
      }

   }
}
