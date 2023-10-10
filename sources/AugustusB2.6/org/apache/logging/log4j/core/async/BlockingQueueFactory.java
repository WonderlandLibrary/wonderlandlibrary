// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.async;

import java.util.concurrent.BlockingQueue;

public interface BlockingQueueFactory<E>
{
    public static final String ELEMENT_TYPE = "BlockingQueueFactory";
    
    BlockingQueue<E> create(final int capacity);
}
