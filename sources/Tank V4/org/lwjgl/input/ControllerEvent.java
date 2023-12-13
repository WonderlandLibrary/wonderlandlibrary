package org.lwjgl.input;

class ControllerEvent {
   public static final int BUTTON = 1;
   public static final int AXIS = 2;
   public static final int POVX = 3;
   public static final int POVY = 4;
   private Controller source;
   private int index;
   private int type;
   private boolean buttonState;
   private boolean xaxis;
   private boolean yaxis;
   private long timeStamp;
   private float xaxisValue;
   private float yaxisValue;

   ControllerEvent(Controller var1, long var2, int var4, int var5, boolean var6, boolean var7) {
      this(var1, var2, var4, var5, false, var6, var7, 0.0F, 0.0F);
   }

   ControllerEvent(Controller var1, long var2, int var4, int var5, boolean var6, boolean var7, boolean var8, float var9, float var10) {
      this.source = var1;
      this.timeStamp = var2;
      this.type = var4;
      this.index = var5;
      this.buttonState = var6;
      this.xaxis = var7;
      this.yaxis = var8;
      this.xaxisValue = var9;
      this.yaxisValue = var10;
   }

   public long getTimeStamp() {
      return this.timeStamp;
   }

   public Controller getSource() {
      return this.source;
   }

   public int getControlIndex() {
      return this.index;
   }

   public boolean isButton() {
      return this.type == 1;
   }

   public boolean getButtonState() {
      return this.buttonState;
   }

   public boolean isAxis() {
      return this.type == 2;
   }

   public boolean isPovY() {
      return this.type == 4;
   }

   public boolean isPovX() {
      return this.type == 3;
   }

   public boolean isXAxis() {
      return this.xaxis;
   }

   public boolean isYAxis() {
      return this.yaxis;
   }

   public float getXAxisValue() {
      return this.xaxisValue;
   }

   public float getYAxisValue() {
      return this.yaxisValue;
   }

   public String toString() {
      return "[" + this.source + " type=" + this.type + " xaxis=" + this.xaxis + " yaxis=" + this.yaxis + "]";
   }
}
