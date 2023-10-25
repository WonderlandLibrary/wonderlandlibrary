package net.java.games.input;

import java.io.IOException;

final class IDirectInputEffect implements Rumbler {
  private final long address;
  
  private final DIEffectInfo info;
  
  private boolean released;
  
  public IDirectInputEffect(long address, DIEffectInfo info) {
    this.address = address;
    this.info = info;
  }
  
  public final synchronized void rumble(float intensity) {
    try {
      checkReleased();
      if (intensity > 0.0F) {
        int int_gain = Math.round(intensity * 10000.0F);
        setGain(int_gain);
        start(1, 0);
      } else {
        stop();
      } 
    } catch (IOException e) {
      DirectInputEnvironmentPlugin.logln("Failed to set rumbler gain: " + e.getMessage());
    } 
  }
  
  public final Component.Identifier getAxisIdentifier() {
    return null;
  }
  
  public final String getAxisName() {
    return null;
  }
  
  public final synchronized void release() {
    if (!this.released) {
      this.released = true;
      nRelease(this.address);
    } 
  }
  
  private static final native void nRelease(long paramLong);
  
  private final void checkReleased() throws IOException {
    if (this.released)
      throw new IOException(); 
  }
  
  private final void setGain(int gain) throws IOException {
    int res = nSetGain(this.address, gain);
    if (res != 3 && res != 4 && res != 0 && res != 8 && res != 12)
      throw new IOException("Failed to set effect gain (0x" + Integer.toHexString(res) + ")"); 
  }
  
  private static final native int nSetGain(long paramLong, int paramInt);
  
  private final void start(int iterations, int flags) throws IOException {
    int res = nStart(this.address, iterations, flags);
    if (res != 0)
      throw new IOException("Failed to start effect (0x" + Integer.toHexString(res) + ")"); 
  }
  
  private static final native int nStart(long paramLong, int paramInt1, int paramInt2);
  
  private final void stop() throws IOException {
    int res = nStop(this.address);
    if (res != 0)
      throw new IOException("Failed to stop effect (0x" + Integer.toHexString(res) + ")"); 
  }
  
  private static final native int nStop(long paramLong);
  
  protected void finalize() {
    release();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\java\games\input\IDirectInputEffect.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */