package net.minecraft.client.stream;

import net.minecraft.client.stream.IngestServerTester;
import net.minecraft.client.stream.Metadata;
import tv.twitch.ErrorCode;
import tv.twitch.broadcast.IngestServer;
import tv.twitch.chat.ChatUserInfo;

public interface IStream {
   boolean func_152908_z();

   IngestServerTester func_152932_y();

   int func_152920_A();

   String func_152921_C();

   void func_152917_b(String var1);

   boolean func_152927_B();

   IngestServer[] func_152925_v();

   boolean func_152913_F();

   ErrorCode func_152912_E();

   void func_152930_t();

   boolean func_152929_G();

   void func_152909_x();

   void func_152911_a(Metadata var1, long var2);

   IStream.AuthFailureReason func_152918_H();

   void func_176026_a(Metadata var1, long var2, long var4);

   boolean isReadyToBroadcast();

   void requestCommercial();

   boolean isPaused();

   void muteMicrophone(boolean var1);

   boolean func_152928_D();

   void pause();

   boolean isBroadcasting();

   void unpause();

   boolean func_152936_l();

   void stopBroadcasting();

   void func_152922_k();

   void func_152935_j();

   void shutdownStream();

   ChatUserInfo func_152926_a(String var1);

   void updateStreamVolume();

   public static enum AuthFailureReason {
      ERROR,
      INVALID_TOKEN;
   }
}
