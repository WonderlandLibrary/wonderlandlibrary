package org.lwjgl.openal;

public final class Util {
  public static void checkALCError(ALCdevice device) {
    int err = ALC10.alcGetError(device);
    if (err != 0)
      throw new OpenALException(ALC10.alcGetString(AL.getDevice(), err)); 
  }
  
  public static void checkALError() {
    int err = AL10.alGetError();
    if (err != 0)
      throw new OpenALException(err); 
  }
  
  public static void checkALCValidDevice(ALCdevice device) {
    if (!device.isValid())
      throw new OpenALException("Invalid device: " + device); 
  }
  
  public static void checkALCValidContext(ALCcontext context) {
    if (!context.isValid())
      throw new OpenALException("Invalid context: " + context); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\openal\Util.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */