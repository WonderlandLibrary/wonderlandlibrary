// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.message;

import java.lang.management.MonitorInfo;
import java.lang.management.LockInfo;
import org.apache.logging.log4j.util.StringBuilders;
import java.lang.management.ThreadInfo;
import org.apache.logging.log4j.message.ThreadInformation;

class ExtendedThreadInformation implements ThreadInformation
{
    private final ThreadInfo threadInfo;
    
    ExtendedThreadInformation(final ThreadInfo thread) {
        this.threadInfo = thread;
    }
    
    @Override
    public void printThreadInfo(final StringBuilder sb) {
        StringBuilders.appendDqValue(sb, this.threadInfo.getThreadName());
        sb.append(" Id=").append(this.threadInfo.getThreadId()).append(' ');
        this.formatState(sb, this.threadInfo);
        if (this.threadInfo.isSuspended()) {
            sb.append(" (suspended)");
        }
        if (this.threadInfo.isInNative()) {
            sb.append(" (in native)");
        }
        sb.append('\n');
    }
    
    @Override
    public void printStack(final StringBuilder sb, final StackTraceElement[] stack) {
        int i = 0;
        for (final StackTraceElement element : stack) {
            sb.append("\tat ").append(element.toString());
            sb.append('\n');
            if (i == 0 && this.threadInfo.getLockInfo() != null) {
                final Thread.State ts = this.threadInfo.getThreadState();
                switch (ts) {
                    case BLOCKED: {
                        sb.append("\t-  blocked on ");
                        this.formatLock(sb, this.threadInfo.getLockInfo());
                        sb.append('\n');
                        break;
                    }
                    case WAITING: {
                        sb.append("\t-  waiting on ");
                        this.formatLock(sb, this.threadInfo.getLockInfo());
                        sb.append('\n');
                        break;
                    }
                    case TIMED_WAITING: {
                        sb.append("\t-  waiting on ");
                        this.formatLock(sb, this.threadInfo.getLockInfo());
                        sb.append('\n');
                        break;
                    }
                }
            }
            for (final MonitorInfo mi : this.threadInfo.getLockedMonitors()) {
                if (mi.getLockedStackDepth() == i) {
                    sb.append("\t-  locked ");
                    this.formatLock(sb, mi);
                    sb.append('\n');
                }
            }
            ++i;
        }
        final LockInfo[] locks = this.threadInfo.getLockedSynchronizers();
        if (locks.length > 0) {
            sb.append("\n\tNumber of locked synchronizers = ").append(locks.length).append('\n');
            for (final LockInfo li : locks) {
                sb.append("\t- ");
                this.formatLock(sb, li);
                sb.append('\n');
            }
        }
    }
    
    private void formatLock(final StringBuilder sb, final LockInfo lock) {
        sb.append('<').append(lock.getIdentityHashCode()).append("> (a ");
        sb.append(lock.getClassName()).append(')');
    }
    
    private void formatState(final StringBuilder sb, final ThreadInfo info) {
        final Thread.State state = info.getThreadState();
        sb.append(state);
        switch (state) {
            case BLOCKED: {
                sb.append(" (on object monitor owned by \"");
                sb.append(info.getLockOwnerName()).append("\" Id=").append(info.getLockOwnerId()).append(')');
                break;
            }
            case WAITING: {
                final StackTraceElement element = info.getStackTrace()[0];
                final String className = element.getClassName();
                final String method = element.getMethodName();
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
            }
            case TIMED_WAITING: {
                final StackTraceElement element = info.getStackTrace()[0];
                final String className = element.getClassName();
                final String method = element.getMethodName();
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
}
