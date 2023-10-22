package cn.hanabi.injection.interfaces;

import cn.hanabi.events.EventMove;

public interface IEntityPlayerSP {
   boolean moving();

   float getSpeed();

   void setSpeed(double var1);

   void setMoveSpeed(EventMove var1, double var2);

   void setYaw(double var1);

   void setPitch(double var1);

   float getDirection();

   void setLastReportedPosY(double var1);
}
