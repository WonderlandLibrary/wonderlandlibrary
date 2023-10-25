package io.netty.channel.group;

import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

public class ChannelGroupException extends ChannelException implements Iterable<Map.Entry<Channel, Throwable>> {
  private static final long serialVersionUID = -4093064295562629453L;
  
  private final Collection<Map.Entry<Channel, Throwable>> failed;
  
  public ChannelGroupException(Collection<Map.Entry<Channel, Throwable>> causes) {
    if (causes == null)
      throw new NullPointerException("causes"); 
    if (causes.isEmpty())
      throw new IllegalArgumentException("causes must be non empty"); 
    this.failed = Collections.unmodifiableCollection(causes);
  }
  
  public Iterator<Map.Entry<Channel, Throwable>> iterator() {
    return this.failed.iterator();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\group\ChannelGroupException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */