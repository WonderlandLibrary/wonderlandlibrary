package org.apache.logging.log4j.core.async;

import com.conversantmedia.util.concurrent.DisruptorBlockingQueue;
import com.conversantmedia.util.concurrent.SpinPolicy;
import java.util.concurrent.BlockingQueue;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

@Plugin(name = "DisruptorBlockingQueue", category = "Core", elementType = "BlockingQueueFactory")
public class DisruptorBlockingQueueFactory<E> implements BlockingQueueFactory<E> {
  private final SpinPolicy spinPolicy;
  
  private DisruptorBlockingQueueFactory(SpinPolicy spinPolicy) {
    this.spinPolicy = spinPolicy;
  }
  
  public BlockingQueue<E> create(int capacity) {
    return (BlockingQueue<E>)new DisruptorBlockingQueue(capacity, this.spinPolicy);
  }
  
  @PluginFactory
  public static <E> DisruptorBlockingQueueFactory<E> createFactory(@PluginAttribute(value = "SpinPolicy", defaultString = "WAITING") SpinPolicy spinPolicy) {
    return new DisruptorBlockingQueueFactory<>(spinPolicy);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\async\DisruptorBlockingQueueFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */