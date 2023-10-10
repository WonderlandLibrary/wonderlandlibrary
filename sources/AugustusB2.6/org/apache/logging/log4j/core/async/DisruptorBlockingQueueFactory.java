// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.async;

import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import com.conversantmedia.util.concurrent.DisruptorBlockingQueue;
import java.util.concurrent.BlockingQueue;
import com.conversantmedia.util.concurrent.SpinPolicy;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "DisruptorBlockingQueue", category = "Core", elementType = "BlockingQueueFactory")
public class DisruptorBlockingQueueFactory<E> implements BlockingQueueFactory<E>
{
    private final SpinPolicy spinPolicy;
    
    private DisruptorBlockingQueueFactory(final SpinPolicy spinPolicy) {
        this.spinPolicy = spinPolicy;
    }
    
    @Override
    public BlockingQueue<E> create(final int capacity) {
        return (BlockingQueue<E>)new DisruptorBlockingQueue(capacity, this.spinPolicy);
    }
    
    @PluginFactory
    public static <E> DisruptorBlockingQueueFactory<E> createFactory(@PluginAttribute(value = "SpinPolicy", defaultString = "WAITING") final SpinPolicy spinPolicy) {
        return new DisruptorBlockingQueueFactory<E>(spinPolicy);
    }
}
