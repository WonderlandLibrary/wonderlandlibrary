package org.apache.logging.log4j.core.async;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

@Plugin(name = "ArrayBlockingQueue", category = "Core", elementType = "BlockingQueueFactory")
public class ArrayBlockingQueueFactory<E> implements BlockingQueueFactory<E> {
  public BlockingQueue<E> create(int capacity) {
    return new ArrayBlockingQueue<>(capacity);
  }
  
  @PluginFactory
  public static <E> ArrayBlockingQueueFactory<E> createFactory() {
    return new ArrayBlockingQueueFactory<>();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\async\ArrayBlockingQueueFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */