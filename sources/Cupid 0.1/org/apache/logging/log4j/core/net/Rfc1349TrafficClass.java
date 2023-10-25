package org.apache.logging.log4j.core.net;

public enum Rfc1349TrafficClass {
  IPTOS_NORMAL(0),
  IPTOS_LOWCOST(2),
  IPTOS_LOWDELAY(16),
  IPTOS_RELIABILITY(4),
  IPTOS_THROUGHPUT(8);
  
  private final int trafficClass;
  
  Rfc1349TrafficClass(int trafficClass) {
    this.trafficClass = trafficClass;
  }
  
  public int value() {
    return this.trafficClass;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\net\Rfc1349TrafficClass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */