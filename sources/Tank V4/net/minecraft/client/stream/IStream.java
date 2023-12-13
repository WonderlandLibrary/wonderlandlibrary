package net.minecraft.client.stream;

import tv.twitch.ErrorCode;
import tv.twitch.broadcast.IngestServer;
import tv.twitch.chat.ChatUserInfo;

public interface IStream {
   void func_152930_t();

   void stopBroadcasting();

   boolean func_152927_B();

   void func_152917_b(String var1);

   ErrorCode func_152912_E();

   boolean func_152913_F();

   IngestServer[] func_152925_v();

   boolean isBroadcasting();

   boolean func_152929_G();

   IStream.AuthFailureReason func_152918_H();

   void pause();

   void shutdownStream();

   void func_152909_x();

   void func_176026_a(Metadata var1, long var2, long var4);

   IngestServerTester func_152932_y();

   void func_152922_k();

   String func_152921_C();

   boolean isPaused();

   void func_152911_a(Metadata var1, long var2);

   void unpause();

   boolean func_152928_D();

   int func_152920_A();

   boolean func_152908_z();

   void func_152935_j();

   void muteMicrophone(boolean var1);

   boolean isReadyToBroadcast();

   void updateStreamVolume();

   boolean func_152936_l();

   void requestCommercial();

   ChatUserInfo func_152926_a(String var1);

   public static enum AuthFailureReason {
      INVALID_TOKEN,
      ERROR;

      private static final IStream.AuthFailureReason[] ENUM$VALUES = new IStream.AuthFailureReason[]{ERROR, INVALID_TOKEN};
   }
}
