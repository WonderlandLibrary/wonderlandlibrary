package io.netty.channel;

public final class ChannelMetadata {
  private final boolean hasDisconnect;
  
  public ChannelMetadata(boolean hasDisconnect) {
    this.hasDisconnect = hasDisconnect;
  }
  
  public boolean hasDisconnect() {
    return this.hasDisconnect;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\ChannelMetadata.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */