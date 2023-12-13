package net.minecraft.client.stream;

import net.minecraft.client.stream.IStream;
import net.minecraft.client.stream.IngestServerTester;
import net.minecraft.client.stream.Metadata;
import tv.twitch.ErrorCode;
import tv.twitch.broadcast.IngestServer;
import tv.twitch.chat.ChatUserInfo;

public class NullStream implements IStream {
   private final Throwable field_152938_a;

   public NullStream(Throwable p_i1006_1_) {
      this.field_152938_a = p_i1006_1_;
   }

   public boolean func_152908_z() {
      return false;
   }

   public IngestServerTester func_152932_y() {
      return null;
   }

   public int func_152920_A() {
      return 0;
   }

   public String func_152921_C() {
      return null;
   }

   public void func_152917_b(String p_152917_1_) {
   }

   public boolean func_152927_B() {
      return false;
   }

   public IngestServer[] func_152925_v() {
      return new IngestServer[0];
   }

   public boolean func_152913_F() {
      return false;
   }

   public ErrorCode func_152912_E() {
      return null;
   }

   public void func_152930_t() {
   }

   public boolean func_152929_G() {
      return false;
   }

   public void func_152909_x() {
   }

   public void func_152911_a(Metadata p_152911_1_, long p_152911_2_) {
   }

   public IStream.AuthFailureReason func_152918_H() {
      return IStream.AuthFailureReason.ERROR;
   }

   public void func_176026_a(Metadata p_176026_1_, long p_176026_2_, long p_176026_4_) {
   }

   public boolean isReadyToBroadcast() {
      return false;
   }

   public void requestCommercial() {
   }

   public boolean isPaused() {
      return false;
   }

   public void muteMicrophone(boolean p_152910_1_) {
   }

   public boolean func_152928_D() {
      return false;
   }

   public void pause() {
   }

   public boolean isBroadcasting() {
      return false;
   }

   public void unpause() {
   }

   public boolean func_152936_l() {
      return false;
   }

   public void stopBroadcasting() {
   }

   public void func_152922_k() {
   }

   public void func_152935_j() {
   }

   public void shutdownStream() {
   }

   public ChatUserInfo func_152926_a(String p_152926_1_) {
      return null;
   }

   public void updateStreamVolume() {
   }

   public Throwable func_152937_a() {
      return this.field_152938_a;
   }
}
