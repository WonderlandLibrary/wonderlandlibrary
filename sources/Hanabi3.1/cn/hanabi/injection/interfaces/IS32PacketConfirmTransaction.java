package cn.hanabi.injection.interfaces;

public interface IS32PacketConfirmTransaction {
   void setwindowId(int var1);

   int getwindowID();

   short getUid();

   void setUid(short var1);

   boolean getAccepted();

   void setAccepted(boolean var1);
}
