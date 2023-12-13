// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.or;

import org.apache.log4j.Layout;

public class ThreadGroupRenderer implements ObjectRenderer
{
    @Override
    public String doRender(final Object obj) {
        if (obj instanceof ThreadGroup) {
            final StringBuilder sb = new StringBuilder();
            final ThreadGroup threadGroup = (ThreadGroup)obj;
            sb.append("java.lang.ThreadGroup[name=");
            sb.append(threadGroup.getName());
            sb.append(", maxpri=");
            sb.append(threadGroup.getMaxPriority());
            sb.append("]");
            final Thread[] threads = new Thread[threadGroup.activeCount()];
            threadGroup.enumerate(threads);
            for (final Thread thread : threads) {
                sb.append(Layout.LINE_SEP);
                sb.append("   Thread=[");
                sb.append(thread.getName());
                sb.append(",");
                sb.append(thread.getPriority());
                sb.append(",");
                sb.append(thread.isDaemon());
                sb.append("]");
            }
            return sb.toString();
        }
        try {
            return obj.toString();
        }
        catch (final Exception ex) {
            return ex.toString();
        }
    }
}
