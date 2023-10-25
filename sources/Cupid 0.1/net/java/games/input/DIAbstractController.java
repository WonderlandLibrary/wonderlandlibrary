package net.java.games.input;

import java.io.IOException;

final class DIAbstractController extends AbstractController {
  private final IDirectInputDevice device;
  
  private final Controller.Type type;
  
  protected DIAbstractController(IDirectInputDevice device, Component[] components, Controller[] children, Rumbler[] rumblers, Controller.Type type) {
    super(device.getProductName(), components, children, rumblers);
    this.device = device;
    this.type = type;
  }
  
  public final void pollDevice() throws IOException {
    this.device.pollAll();
  }
  
  protected final boolean getNextDeviceEvent(Event event) throws IOException {
    return DIControllers.getNextDeviceEvent(event, this.device);
  }
  
  protected final void setDeviceEventQueueSize(int size) throws IOException {
    this.device.setBufferSize(size);
  }
  
  public final Controller.Type getType() {
    return this.type;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\java\games\input\DIAbstractController.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */