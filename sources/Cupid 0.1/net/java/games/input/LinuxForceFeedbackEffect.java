package net.java.games.input;

import java.io.IOException;

abstract class LinuxForceFeedbackEffect implements Rumbler {
  private final LinuxEventDevice device;
  
  private final int ff_id;
  
  private final WriteTask write_task = new WriteTask();
  
  private final UploadTask upload_task = new UploadTask();
  
  public LinuxForceFeedbackEffect(LinuxEventDevice device) throws IOException {
    this.device = device;
    this.ff_id = this.upload_task.doUpload(-1, 0.0F);
  }
  
  protected final LinuxEventDevice getDevice() {
    return this.device;
  }
  
  public final synchronized void rumble(float intensity) {
    try {
      if (intensity > 0.0F) {
        this.upload_task.doUpload(this.ff_id, intensity);
        this.write_task.write(1);
      } else {
        this.write_task.write(0);
      } 
    } catch (IOException e) {
      LinuxEnvironmentPlugin.logln("Failed to rumble: " + e);
    } 
  }
  
  public final String getAxisName() {
    return null;
  }
  
  public final Component.Identifier getAxisIdentifier() {
    return null;
  }
  
  protected abstract int upload(int paramInt, float paramFloat) throws IOException;
  
  private final class UploadTask extends LinuxDeviceTask {
    private int id;
    
    private float intensity;
    
    private final LinuxForceFeedbackEffect this$0;
    
    private UploadTask() {}
    
    public final int doUpload(int id, float intensity) throws IOException {
      this.id = id;
      this.intensity = intensity;
      LinuxEnvironmentPlugin.execute(this);
      return this.id;
    }
    
    protected final Object execute() throws IOException {
      this.id = LinuxForceFeedbackEffect.this.upload(this.id, this.intensity);
      return null;
    }
  }
  
  private final class WriteTask extends LinuxDeviceTask {
    private int value;
    
    private final LinuxForceFeedbackEffect this$0;
    
    private WriteTask() {}
    
    public final void write(int value) throws IOException {
      this.value = value;
      LinuxEnvironmentPlugin.execute(this);
    }
    
    protected final Object execute() throws IOException {
      LinuxForceFeedbackEffect.this.device.writeEvent(21, LinuxForceFeedbackEffect.this.ff_id, this.value);
      return null;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\java\games\input\LinuxForceFeedbackEffect.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */