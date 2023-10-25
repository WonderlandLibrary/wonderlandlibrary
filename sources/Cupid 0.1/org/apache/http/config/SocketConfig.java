package org.apache.http.config;

import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;

@Immutable
public class SocketConfig implements Cloneable {
  public static final SocketConfig DEFAULT = (new Builder()).build();
  
  private final int soTimeout;
  
  private final boolean soReuseAddress;
  
  private final int soLinger;
  
  private final boolean soKeepAlive;
  
  private final boolean tcpNoDelay;
  
  SocketConfig(int soTimeout, boolean soReuseAddress, int soLinger, boolean soKeepAlive, boolean tcpNoDelay) {
    this.soTimeout = soTimeout;
    this.soReuseAddress = soReuseAddress;
    this.soLinger = soLinger;
    this.soKeepAlive = soKeepAlive;
    this.tcpNoDelay = tcpNoDelay;
  }
  
  public int getSoTimeout() {
    return this.soTimeout;
  }
  
  public boolean isSoReuseAddress() {
    return this.soReuseAddress;
  }
  
  public int getSoLinger() {
    return this.soLinger;
  }
  
  public boolean isSoKeepAlive() {
    return this.soKeepAlive;
  }
  
  public boolean isTcpNoDelay() {
    return this.tcpNoDelay;
  }
  
  protected SocketConfig clone() throws CloneNotSupportedException {
    return (SocketConfig)super.clone();
  }
  
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("[soTimeout=").append(this.soTimeout).append(", soReuseAddress=").append(this.soReuseAddress).append(", soLinger=").append(this.soLinger).append(", soKeepAlive=").append(this.soKeepAlive).append(", tcpNoDelay=").append(this.tcpNoDelay).append("]");
    return builder.toString();
  }
  
  public static Builder custom() {
    return new Builder();
  }
  
  public static Builder copy(SocketConfig config) {
    Args.notNull(config, "Socket config");
    return (new Builder()).setSoTimeout(config.getSoTimeout()).setSoReuseAddress(config.isSoReuseAddress()).setSoLinger(config.getSoLinger()).setSoKeepAlive(config.isSoKeepAlive()).setTcpNoDelay(config.isTcpNoDelay());
  }
  
  public static class Builder {
    private int soTimeout;
    
    private boolean soReuseAddress;
    
    private int soLinger = -1;
    
    private boolean soKeepAlive;
    
    private boolean tcpNoDelay = true;
    
    public Builder setSoTimeout(int soTimeout) {
      this.soTimeout = soTimeout;
      return this;
    }
    
    public Builder setSoReuseAddress(boolean soReuseAddress) {
      this.soReuseAddress = soReuseAddress;
      return this;
    }
    
    public Builder setSoLinger(int soLinger) {
      this.soLinger = soLinger;
      return this;
    }
    
    public Builder setSoKeepAlive(boolean soKeepAlive) {
      this.soKeepAlive = soKeepAlive;
      return this;
    }
    
    public Builder setTcpNoDelay(boolean tcpNoDelay) {
      this.tcpNoDelay = tcpNoDelay;
      return this;
    }
    
    public SocketConfig build() {
      return new SocketConfig(this.soTimeout, this.soReuseAddress, this.soLinger, this.soKeepAlive, this.tcpNoDelay);
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\config\SocketConfig.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */