package io.netty.channel.rxtx;

import java.net.SocketAddress;

public class RxtxDeviceAddress extends SocketAddress {
  private static final long serialVersionUID = -2907820090993709523L;
  
  private final String value;
  
  public RxtxDeviceAddress(String value) {
    this.value = value;
  }
  
  public String value() {
    return this.value;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\channel\rxtx\RxtxDeviceAddress.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */