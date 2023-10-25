package net.java.games.input;

import java.util.ArrayList;
import java.util.List;

public class WinTabContext {
  private DummyWindow window;
  
  private long hCTX;
  
  private Controller[] controllers;
  
  public WinTabContext(DummyWindow window) {
    this.window = window;
  }
  
  public Controller[] getControllers() {
    if (this.hCTX == 0L)
      throw new IllegalStateException("Context must be open before getting the controllers"); 
    return this.controllers;
  }
  
  public synchronized void open() {
    this.hCTX = nOpen(this.window.getHwnd());
    List devices = new ArrayList();
    int numSupportedDevices = nGetNumberOfSupportedDevices();
    for (int i = 0; i < numSupportedDevices; i++) {
      WinTabDevice newDevice = WinTabDevice.createDevice(this, i);
      if (newDevice != null)
        devices.add(newDevice); 
    } 
    this.controllers = devices.<Controller>toArray(new Controller[0]);
  }
  
  public synchronized void close() {
    nClose(this.hCTX);
  }
  
  public synchronized void processEvents() {
    WinTabPacket[] packets = nGetPackets(this.hCTX);
    for (int i = 0; i < packets.length; i++)
      ((WinTabDevice)getControllers()[0]).processPacket(packets[i]); 
  }
  
  private static final native int nGetNumberOfSupportedDevices();
  
  private static final native long nOpen(long paramLong);
  
  private static final native void nClose(long paramLong);
  
  private static final native WinTabPacket[] nGetPackets(long paramLong);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\java\games\input\WinTabContext.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */