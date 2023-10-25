package net.java.games.input;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

final class IDirectInput {
  private final List devices = new ArrayList();
  
  private final long idirectinput_address;
  
  private final DummyWindow window;
  
  public IDirectInput(DummyWindow window) throws IOException {
    this.window = window;
    this.idirectinput_address = createIDirectInput();
    try {
      enumDevices();
    } catch (IOException e) {
      releaseDevices();
      release();
      throw e;
    } 
  }
  
  private static final native long createIDirectInput() throws IOException;
  
  public final List getDevices() {
    return this.devices;
  }
  
  private final void enumDevices() throws IOException {
    nEnumDevices(this.idirectinput_address);
  }
  
  private final native void nEnumDevices(long paramLong) throws IOException;
  
  private final void addDevice(long address, byte[] instance_guid, byte[] product_guid, int dev_type, int dev_subtype, String instance_name, String product_name) throws IOException {
    try {
      IDirectInputDevice device = new IDirectInputDevice(this.window, address, instance_guid, product_guid, dev_type, dev_subtype, instance_name, product_name);
      this.devices.add(device);
    } catch (IOException e) {
      DirectInputEnvironmentPlugin.logln("Failed to initialize device " + product_name + " because of: " + e);
    } 
  }
  
  public final void releaseDevices() {
    for (int i = 0; i < this.devices.size(); i++) {
      IDirectInputDevice device = this.devices.get(i);
      device.release();
    } 
  }
  
  public final void release() {
    nRelease(this.idirectinput_address);
  }
  
  private static final native void nRelease(long paramLong);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\java\games\input\IDirectInput.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */