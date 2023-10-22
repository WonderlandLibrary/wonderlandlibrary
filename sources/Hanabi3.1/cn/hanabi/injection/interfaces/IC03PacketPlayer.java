package cn.hanabi.injection.interfaces;

public interface IC03PacketPlayer {
   boolean isOnGround();

   void setOnGround(boolean var1);

   boolean ismoving();

   void setmoving(boolean var1);

   double getPosX();

   void setPosX(double var1);

   double getPosY();

   void setPosY(double var1);

   double getPosZ();

   void setPosZ(double var1);

   float getYaw();

   void setYaw(float var1);

   float getPitch();

   void setPitch(float var1);

   boolean getRotate();

   void setRotate(boolean var1);
}
