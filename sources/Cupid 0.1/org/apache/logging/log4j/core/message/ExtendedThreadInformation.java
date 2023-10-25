package org.apache.logging.log4j.core.message;

import java.lang.management.LockInfo;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import org.apache.logging.log4j.message.ThreadInformation;
import org.apache.logging.log4j.util.StringBuilders;

class ExtendedThreadInformation implements ThreadInformation {
  private final ThreadInfo threadInfo;
  
  ExtendedThreadInformation(ThreadInfo thread) {
    this.threadInfo = thread;
  }
  
  public void printThreadInfo(StringBuilder sb) {
    StringBuilders.appendDqValue(sb, this.threadInfo.getThreadName());
    sb.append(" Id=").append(this.threadInfo.getThreadId()).append(' ');
    formatState(sb, this.threadInfo);
    if (this.threadInfo.isSuspended())
      sb.append(" (suspended)"); 
    if (this.threadInfo.isInNative())
      sb.append(" (in native)"); 
    sb.append('\n');
  }
  
  public void printStack(StringBuilder sb, StackTraceElement[] stack) {
    int i = 0;
    for (StackTraceElement element : stack) {
      sb.append("\tat ").append(element.toString());
      sb.append('\n');
      if (i == 0 && this.threadInfo.getLockInfo() != null) {
        Thread.State ts = this.threadInfo.getThreadState();
        switch (ts) {
          case BLOCKED:
            sb.append("\t-  blocked on ");
            formatLock(sb, this.threadInfo.getLockInfo());
            sb.append('\n');
            break;
          case WAITING:
            sb.append("\t-  waiting on ");
            formatLock(sb, this.threadInfo.getLockInfo());
            sb.append('\n');
            break;
          case TIMED_WAITING:
            sb.append("\t-  waiting on ");
            formatLock(sb, this.threadInfo.getLockInfo());
            sb.append('\n');
            break;
        } 
      } 
      for (MonitorInfo mi : this.threadInfo.getLockedMonitors()) {
        if (mi.getLockedStackDepth() == i) {
          sb.append("\t-  locked ");
          formatLock(sb, mi);
          sb.append('\n');
        } 
      } 
      i++;
    } 
    LockInfo[] locks = this.threadInfo.getLockedSynchronizers();
    if (locks.length > 0) {
      sb.append("\n\tNumber of locked synchronizers = ").append(locks.length).append('\n');
      for (LockInfo li : locks) {
        sb.append("\t- ");
        formatLock(sb, li);
        sb.append('\n');
      } 
    } 
  }
  
  private void formatLock(StringBuilder sb, LockInfo lock) {
    sb.append('<').append(lock.getIdentityHashCode()).append("> (a ");
    sb.append(lock.getClassName()).append(')');
  }
  
  private void formatState(StringBuilder sb, ThreadInfo info) {
    StackTraceElement element;
    String className, method;
    Thread.State state = info.getThreadState();
    sb.append(state);
    switch (state) {
      case BLOCKED:
        sb.append(" (on object monitor owned by \"");
        sb.append(info.getLockOwnerName()).append("\" Id=").append(info.getLockOwnerId()).append(')');
        break;
      case WAITING:
        element = info.getStackTrace()[0];
        className = element.getClassName();
        method = element.getMethodName();
        if (className.equals("java.lang.Object") && method.equals("wait")) {
          sb.append(" (on object monitor");
          if (info.getLockOwnerName() != null) {
            sb.append(" owned by \"");
            sb.append(info.getLockOwnerName()).append("\" Id=").append(info.getLockOwnerId());
          } 
          sb.append(')');
          break;
        } 
        if (className.equals("java.lang.Thread") && method.equals("join")) {
          sb.append(" (on completion of thread ").append(info.getLockOwnerId()).append(')');
          break;
        } 
        sb.append(" (parking for lock");
        if (info.getLockOwnerName() != null) {
          sb.append(" owned by \"");
          sb.append(info.getLockOwnerName()).append("\" Id=").append(info.getLockOwnerId());
        } 
        sb.append(')');
        break;
      case TIMED_WAITING:
        element = info.getStackTrace()[0];
        className = element.getClassName();
        method = element.getMethodName();
        if (className.equals("java.lang.Object") && method.equals("wait")) {
          sb.append(" (on object monitor");
          if (info.getLockOwnerName() != null) {
            sb.append(" owned by \"");
            sb.append(info.getLockOwnerName()).append("\" Id=").append(info.getLockOwnerId());
          } 
          sb.append(')');
          break;
        } 
        if (className.equals("java.lang.Thread") && method.equals("sleep")) {
          sb.append(" (sleeping)");
          break;
        } 
        if (className.equals("java.lang.Thread") && method.equals("join")) {
          sb.append(" (on completion of thread ").append(info.getLockOwnerId()).append(')');
          break;
        } 
        sb.append(" (parking for lock");
        if (info.getLockOwnerName() != null) {
          sb.append(" owned by \"");
          sb.append(info.getLockOwnerName()).append("\" Id=").append(info.getLockOwnerId());
        } 
        sb.append(')');
        break;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\message\ExtendedThreadInformation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */