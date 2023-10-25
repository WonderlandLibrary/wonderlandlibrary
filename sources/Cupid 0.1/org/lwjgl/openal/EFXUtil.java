package org.lwjgl.openal;

public final class EFXUtil {
  private static final int EFFECT = 1111;
  
  private static final int FILTER = 2222;
  
  public static boolean isEfxSupported() {
    if (!AL.isCreated())
      throw new OpenALException("OpenAL has not been created."); 
    return ALC10.alcIsExtensionPresent(AL.getDevice(), "ALC_EXT_EFX");
  }
  
  public static boolean isEffectSupported(int effectType) {
    switch (effectType) {
      case 0:
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
      case 9:
      case 10:
      case 11:
      case 12:
      case 32768:
        return testSupportGeneric(1111, effectType);
    } 
    throw new IllegalArgumentException("Unknown or invalid effect type: " + effectType);
  }
  
  public static boolean isFilterSupported(int filterType) {
    switch (filterType) {
      case 0:
      case 1:
      case 2:
      case 3:
        return testSupportGeneric(2222, filterType);
    } 
    throw new IllegalArgumentException("Unknown or invalid filter type: " + filterType);
  }
  
  private static boolean testSupportGeneric(int objectType, int typeValue) {
    switch (objectType) {
      case 1111:
      case 2222:
        break;
      default:
        throw new IllegalArgumentException("Invalid objectType: " + objectType);
    } 
    boolean supported = false;
    if (isEfxSupported()) {
      int genError;
      AL10.alGetError();
      int testObject = 0;
      try {
        switch (objectType) {
          case 1111:
            testObject = EFX10.alGenEffects();
            break;
          case 2222:
            testObject = EFX10.alGenFilters();
            break;
          default:
            throw new IllegalArgumentException("Invalid objectType: " + objectType);
        } 
        genError = AL10.alGetError();
      } catch (OpenALException debugBuildException) {
        if (debugBuildException.getMessage().contains("AL_OUT_OF_MEMORY")) {
          genError = 40965;
        } else {
          genError = 40964;
        } 
      } 
      if (genError == 0) {
        char c;
        AL10.alGetError();
        try {
          switch (objectType) {
            case 1111:
              EFX10.alEffecti(testObject, 32769, typeValue);
              break;
            case 2222:
              EFX10.alFilteri(testObject, 32769, typeValue);
              break;
            default:
              throw new IllegalArgumentException("Invalid objectType: " + objectType);
          } 
          c = AL10.alGetError();
        } catch (OpenALException debugBuildException) {
          c = 'ꀃ';
        } 
        if (c == '\000')
          supported = true; 
        try {
          switch (objectType) {
            case 1111:
              EFX10.alDeleteEffects(testObject);
              return supported;
            case 2222:
              EFX10.alDeleteFilters(testObject);
              return supported;
          } 
          throw new IllegalArgumentException("Invalid objectType: " + objectType);
        } catch (OpenALException debugBuildException) {}
      } else if (genError == 40965) {
        throw new OpenALException(genError);
      } 
    } 
    return supported;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\openal\EFXUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */