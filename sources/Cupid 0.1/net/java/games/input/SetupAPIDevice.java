package net.java.games.input;

final class SetupAPIDevice {
  private final String device_instance_id;
  
  private final String device_name;
  
  public SetupAPIDevice(String device_instance_id, String device_name) {
    this.device_instance_id = device_instance_id;
    this.device_name = device_name;
  }
  
  public final String getName() {
    return this.device_name;
  }
  
  public final String getInstanceId() {
    return this.device_instance_id;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\java\games\input\SetupAPIDevice.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */