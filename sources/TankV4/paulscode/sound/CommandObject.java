package paulscode.sound;

public class CommandObject {
   public static final int INITIALIZE = 1;
   public static final int LOAD_SOUND = 2;
   public static final int LOAD_DATA = 3;
   public static final int UNLOAD_SOUND = 4;
   public static final int QUEUE_SOUND = 5;
   public static final int DEQUEUE_SOUND = 6;
   public static final int FADE_OUT = 7;
   public static final int FADE_OUT_IN = 8;
   public static final int CHECK_FADE_VOLUMES = 9;
   public static final int NEW_SOURCE = 10;
   public static final int RAW_DATA_STREAM = 11;
   public static final int QUICK_PLAY = 12;
   public static final int SET_POSITION = 13;
   public static final int SET_VOLUME = 14;
   public static final int SET_PITCH = 15;
   public static final int SET_PRIORITY = 16;
   public static final int SET_LOOPING = 17;
   public static final int SET_ATTENUATION = 18;
   public static final int SET_DIST_OR_ROLL = 19;
   public static final int CHANGE_DOPPLER_FACTOR = 20;
   public static final int CHANGE_DOPPLER_VELOCITY = 21;
   public static final int SET_VELOCITY = 22;
   public static final int SET_LISTENER_VELOCITY = 23;
   public static final int PLAY = 24;
   public static final int FEED_RAW_AUDIO_DATA = 25;
   public static final int PAUSE = 26;
   public static final int STOP = 27;
   public static final int REWIND = 28;
   public static final int FLUSH = 29;
   public static final int CULL = 30;
   public static final int ACTIVATE = 31;
   public static final int SET_TEMPORARY = 32;
   public static final int REMOVE_SOURCE = 33;
   public static final int MOVE_LISTENER = 34;
   public static final int SET_LISTENER_POSITION = 35;
   public static final int TURN_LISTENER = 36;
   public static final int SET_LISTENER_ANGLE = 37;
   public static final int SET_LISTENER_ORIENTATION = 38;
   public static final int SET_MASTER_VOLUME = 39;
   public static final int NEW_LIBRARY = 40;
   public byte[] buffer;
   public int[] intArgs;
   public float[] floatArgs;
   public long[] longArgs;
   public boolean[] boolArgs;
   public String[] stringArgs;
   public Class[] classArgs;
   public Object[] objectArgs;
   public int Command;

   public CommandObject(int var1) {
      this.Command = var1;
   }

   public CommandObject(int var1, int var2) {
      this.Command = var1;
      this.intArgs = new int[1];
      this.intArgs[0] = var2;
   }

   public CommandObject(int var1, Class var2) {
      this.Command = var1;
      this.classArgs = new Class[1];
      this.classArgs[0] = var2;
   }

   public CommandObject(int var1, float var2) {
      this.Command = var1;
      this.floatArgs = new float[1];
      this.floatArgs[0] = var2;
   }

   public CommandObject(int var1, String var2) {
      this.Command = var1;
      this.stringArgs = new String[1];
      this.stringArgs[0] = var2;
   }

   public CommandObject(int var1, Object var2) {
      this.Command = var1;
      this.objectArgs = new Object[1];
      this.objectArgs[0] = var2;
   }

   public CommandObject(int var1, String var2, Object var3) {
      this.Command = var1;
      this.stringArgs = new String[1];
      this.stringArgs[0] = var2;
      this.objectArgs = new Object[1];
      this.objectArgs[0] = var3;
   }

   public CommandObject(int var1, String var2, byte[] var3) {
      this.Command = var1;
      this.stringArgs = new String[1];
      this.stringArgs[0] = var2;
      this.buffer = var3;
   }

   public CommandObject(int var1, String var2, Object var3, long var4) {
      this.Command = var1;
      this.stringArgs = new String[1];
      this.stringArgs[0] = var2;
      this.objectArgs = new Object[1];
      this.objectArgs[0] = var3;
      this.longArgs = new long[1];
      this.longArgs[0] = var4;
   }

   public CommandObject(int var1, String var2, Object var3, long var4, long var6) {
      this.Command = var1;
      this.stringArgs = new String[1];
      this.stringArgs[0] = var2;
      this.objectArgs = new Object[1];
      this.objectArgs[0] = var3;
      this.longArgs = new long[2];
      this.longArgs[0] = var4;
      this.longArgs[1] = var6;
   }

   public CommandObject(int var1, String var2, String var3) {
      this.Command = var1;
      this.stringArgs = new String[2];
      this.stringArgs[0] = var2;
      this.stringArgs[1] = var3;
   }

   public CommandObject(int var1, String var2, int var3) {
      this.Command = var1;
      this.intArgs = new int[1];
      this.stringArgs = new String[1];
      this.intArgs[0] = var3;
      this.stringArgs[0] = var2;
   }

   public CommandObject(int var1, String var2, float var3) {
      this.Command = var1;
      this.floatArgs = new float[1];
      this.stringArgs = new String[1];
      this.floatArgs[0] = var3;
      this.stringArgs[0] = var2;
   }

   public CommandObject(int var1, String var2, boolean var3) {
      this.Command = var1;
      this.boolArgs = new boolean[1];
      this.stringArgs = new String[1];
      this.boolArgs[0] = var3;
      this.stringArgs[0] = var2;
   }

   public CommandObject(int var1, float var2, float var3, float var4) {
      this.Command = var1;
      this.floatArgs = new float[3];
      this.floatArgs[0] = var2;
      this.floatArgs[1] = var3;
      this.floatArgs[2] = var4;
   }

   public CommandObject(int var1, String var2, float var3, float var4, float var5) {
      this.Command = var1;
      this.floatArgs = new float[3];
      this.stringArgs = new String[1];
      this.floatArgs[0] = var3;
      this.floatArgs[1] = var4;
      this.floatArgs[2] = var5;
      this.stringArgs[0] = var2;
   }

   public CommandObject(int var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      this.Command = var1;
      this.floatArgs = new float[6];
      this.floatArgs[0] = var2;
      this.floatArgs[1] = var3;
      this.floatArgs[2] = var4;
      this.floatArgs[3] = var5;
      this.floatArgs[4] = var6;
      this.floatArgs[5] = var7;
   }

   public CommandObject(int var1, boolean var2, boolean var3, boolean var4, String var5, Object var6, float var7, float var8, float var9, int var10, float var11) {
      this.Command = var1;
      this.intArgs = new int[1];
      this.floatArgs = new float[4];
      this.boolArgs = new boolean[3];
      this.stringArgs = new String[1];
      this.objectArgs = new Object[1];
      this.intArgs[0] = var10;
      this.floatArgs[0] = var7;
      this.floatArgs[1] = var8;
      this.floatArgs[2] = var9;
      this.floatArgs[3] = var11;
      this.boolArgs[0] = var2;
      this.boolArgs[1] = var3;
      this.boolArgs[2] = var4;
      this.stringArgs[0] = var5;
      this.objectArgs[0] = var6;
   }

   public CommandObject(int var1, boolean var2, boolean var3, boolean var4, String var5, Object var6, float var7, float var8, float var9, int var10, float var11, boolean var12) {
      this.Command = var1;
      this.intArgs = new int[1];
      this.floatArgs = new float[4];
      this.boolArgs = new boolean[4];
      this.stringArgs = new String[1];
      this.objectArgs = new Object[1];
      this.intArgs[0] = var10;
      this.floatArgs[0] = var7;
      this.floatArgs[1] = var8;
      this.floatArgs[2] = var9;
      this.floatArgs[3] = var11;
      this.boolArgs[0] = var2;
      this.boolArgs[1] = var3;
      this.boolArgs[2] = var4;
      this.boolArgs[3] = var12;
      this.stringArgs[0] = var5;
      this.objectArgs[0] = var6;
   }

   public CommandObject(int var1, Object var2, boolean var3, String var4, float var5, float var6, float var7, int var8, float var9) {
      this.Command = var1;
      this.intArgs = new int[1];
      this.floatArgs = new float[4];
      this.boolArgs = new boolean[1];
      this.stringArgs = new String[1];
      this.objectArgs = new Object[1];
      this.intArgs[0] = var8;
      this.floatArgs[0] = var5;
      this.floatArgs[1] = var6;
      this.floatArgs[2] = var7;
      this.floatArgs[3] = var9;
      this.boolArgs[0] = var3;
      this.stringArgs[0] = var4;
      this.objectArgs[0] = var2;
   }
}
