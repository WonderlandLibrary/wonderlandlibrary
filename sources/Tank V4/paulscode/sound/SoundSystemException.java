package paulscode.sound;

public class SoundSystemException extends Exception {
   public static final int ERROR_NONE = 0;
   public static final int UNKNOWN_ERROR = 1;
   public static final int NULL_PARAMETER = 2;
   public static final int CLASS_TYPE_MISMATCH = 3;
   public static final int LIBRARY_NULL = 4;
   public static final int LIBRARY_TYPE = 5;
   private int myType = 1;

   public SoundSystemException(String var1) {
      super(var1);
   }

   public SoundSystemException(String var1, int var2) {
      super(var1);
      this.myType = var2;
   }

   public int getType() {
      return this.myType;
   }
}
