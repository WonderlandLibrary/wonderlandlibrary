package org.apache.logging.log4j.core.net;

import java.net.Socket;
import java.net.SocketException;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.util.Builder;

@Plugin(name = "SocketOptions", category = "Core", printObject = true)
public class SocketOptions implements Builder<SocketOptions>, Cloneable {
  @PluginBuilderAttribute
  private Boolean keepAlive;
  
  @PluginBuilderAttribute
  private Boolean oobInline;
  
  @PluginElement("PerformancePreferences")
  private SocketPerformancePreferences performancePreferences;
  
  @PluginBuilderAttribute
  private Integer receiveBufferSize;
  
  @PluginBuilderAttribute
  private Boolean reuseAddress;
  
  @PluginBuilderAttribute
  private Rfc1349TrafficClass rfc1349TrafficClass;
  
  @PluginBuilderAttribute
  private Integer sendBufferSize;
  
  @PluginBuilderAttribute
  private Integer soLinger;
  
  @PluginBuilderAttribute
  private Integer soTimeout;
  
  @PluginBuilderAttribute
  private Boolean tcpNoDelay;
  
  @PluginBuilderAttribute
  private Integer trafficClass;
  
  @PluginBuilderFactory
  public static SocketOptions newBuilder() {
    return new SocketOptions();
  }
  
  public void apply(Socket socket) throws SocketException {
    if (this.keepAlive != null)
      socket.setKeepAlive(this.keepAlive.booleanValue()); 
    if (this.oobInline != null)
      socket.setOOBInline(this.oobInline.booleanValue()); 
    if (this.reuseAddress != null)
      socket.setReuseAddress(this.reuseAddress.booleanValue()); 
    if (this.performancePreferences != null)
      this.performancePreferences.apply(socket); 
    if (this.receiveBufferSize != null)
      socket.setReceiveBufferSize(this.receiveBufferSize.intValue()); 
    if (this.soLinger != null)
      socket.setSoLinger(true, this.soLinger.intValue()); 
    if (this.soTimeout != null)
      socket.setSoTimeout(this.soTimeout.intValue()); 
    if (this.tcpNoDelay != null)
      socket.setTcpNoDelay(this.tcpNoDelay.booleanValue()); 
    Integer actualTrafficClass = getActualTrafficClass();
    if (actualTrafficClass != null)
      socket.setTrafficClass(actualTrafficClass.intValue()); 
  }
  
  public SocketOptions build() {
    try {
      return (SocketOptions)clone();
    } catch (CloneNotSupportedException e) {
      throw new IllegalStateException(e);
    } 
  }
  
  public Integer getActualTrafficClass() {
    if (this.trafficClass != null && this.rfc1349TrafficClass != null)
      throw new IllegalStateException("You MUST not set both customTrafficClass and trafficClass."); 
    if (this.trafficClass != null)
      return this.trafficClass; 
    if (this.rfc1349TrafficClass != null)
      return Integer.valueOf(this.rfc1349TrafficClass.value()); 
    return null;
  }
  
  public SocketPerformancePreferences getPerformancePreferences() {
    return this.performancePreferences;
  }
  
  public Integer getReceiveBufferSize() {
    return this.receiveBufferSize;
  }
  
  public Rfc1349TrafficClass getRfc1349TrafficClass() {
    return this.rfc1349TrafficClass;
  }
  
  public Integer getSendBufferSize() {
    return this.sendBufferSize;
  }
  
  public Integer getSoLinger() {
    return this.soLinger;
  }
  
  public Integer getSoTimeout() {
    return this.soTimeout;
  }
  
  public Integer getTrafficClass() {
    return this.trafficClass;
  }
  
  public Boolean isKeepAlive() {
    return this.keepAlive;
  }
  
  public Boolean isOobInline() {
    return this.oobInline;
  }
  
  public Boolean isReuseAddress() {
    return this.reuseAddress;
  }
  
  public Boolean isTcpNoDelay() {
    return this.tcpNoDelay;
  }
  
  public SocketOptions setKeepAlive(boolean keepAlive) {
    this.keepAlive = Boolean.valueOf(keepAlive);
    return this;
  }
  
  public SocketOptions setOobInline(boolean oobInline) {
    this.oobInline = Boolean.valueOf(oobInline);
    return this;
  }
  
  public SocketOptions setPerformancePreferences(SocketPerformancePreferences performancePreferences) {
    this.performancePreferences = performancePreferences;
    return this;
  }
  
  public SocketOptions setReceiveBufferSize(int receiveBufferSize) {
    this.receiveBufferSize = Integer.valueOf(receiveBufferSize);
    return this;
  }
  
  public SocketOptions setReuseAddress(boolean reuseAddress) {
    this.reuseAddress = Boolean.valueOf(reuseAddress);
    return this;
  }
  
  public SocketOptions setRfc1349TrafficClass(Rfc1349TrafficClass trafficClass) {
    this.rfc1349TrafficClass = trafficClass;
    return this;
  }
  
  public SocketOptions setSendBufferSize(int sendBufferSize) {
    this.sendBufferSize = Integer.valueOf(sendBufferSize);
    return this;
  }
  
  public SocketOptions setSoLinger(int soLinger) {
    this.soLinger = Integer.valueOf(soLinger);
    return this;
  }
  
  public SocketOptions setSoTimeout(int soTimeout) {
    this.soTimeout = Integer.valueOf(soTimeout);
    return this;
  }
  
  public SocketOptions setTcpNoDelay(boolean tcpNoDelay) {
    this.tcpNoDelay = Boolean.valueOf(tcpNoDelay);
    return this;
  }
  
  public SocketOptions setTrafficClass(int trafficClass) {
    this.trafficClass = Integer.valueOf(trafficClass);
    return this;
  }
  
  public String toString() {
    return "SocketOptions [keepAlive=" + this.keepAlive + ", oobInline=" + this.oobInline + ", performancePreferences=" + this.performancePreferences + ", receiveBufferSize=" + this.receiveBufferSize + ", reuseAddress=" + this.reuseAddress + ", rfc1349TrafficClass=" + this.rfc1349TrafficClass + ", sendBufferSize=" + this.sendBufferSize + ", soLinger=" + this.soLinger + ", soTimeout=" + this.soTimeout + ", tcpNoDelay=" + this.tcpNoDelay + ", trafficClass=" + this.trafficClass + "]";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\net\SocketOptions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */