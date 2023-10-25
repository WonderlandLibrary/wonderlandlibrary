package net.java.games.input;

final class LinuxInputID {
  private final int bustype;
  
  private final int vendor;
  
  private final int product;
  
  private final int version;
  
  public LinuxInputID(int bustype, int vendor, int product, int version) {
    this.bustype = bustype;
    this.vendor = vendor;
    this.product = product;
    this.version = version;
  }
  
  public final Controller.PortType getPortType() {
    return LinuxNativeTypesMap.getPortType(this.bustype);
  }
  
  public final String toString() {
    return "LinuxInputID: bustype = 0x" + Integer.toHexString(this.bustype) + " | vendor = 0x" + Integer.toHexString(this.vendor) + " | product = 0x" + Integer.toHexString(this.product) + " | version = 0x" + Integer.toHexString(this.version);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\java\games\input\LinuxInputID.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */