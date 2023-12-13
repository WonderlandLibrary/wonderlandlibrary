package net.minecraft.client.stream;

import com.google.common.collect.Lists;
import java.util.List;
import tv.twitch.AuthToken;
import tv.twitch.ErrorCode;
import tv.twitch.broadcast.ArchivingState;
import tv.twitch.broadcast.AudioParams;
import tv.twitch.broadcast.ChannelInfo;
import tv.twitch.broadcast.EncodingCpuUsage;
import tv.twitch.broadcast.FrameBuffer;
import tv.twitch.broadcast.GameInfoList;
import tv.twitch.broadcast.IStatCallbacks;
import tv.twitch.broadcast.IStreamCallbacks;
import tv.twitch.broadcast.IngestList;
import tv.twitch.broadcast.IngestServer;
import tv.twitch.broadcast.PixelFormat;
import tv.twitch.broadcast.RTMPState;
import tv.twitch.broadcast.StartFlags;
import tv.twitch.broadcast.StatType;
import tv.twitch.broadcast.Stream;
import tv.twitch.broadcast.StreamInfo;
import tv.twitch.broadcast.UserInfo;
import tv.twitch.broadcast.VideoParams;

public class IngestServerTester {
   protected AudioParams audioParameters;
   protected int field_153062_t;
   protected IngestList field_153046_d = null;
   protected boolean field_153060_r;
   protected boolean field_176007_z;
   protected Stream field_153045_c = null;
   protected boolean field_153061_s;
   protected int field_153063_u;
   protected List field_153055_m;
   protected float field_153066_x;
   protected VideoParams field_153052_j;
   protected boolean field_176009_x;
   protected long field_153054_l;
   protected IStatCallbacks field_176006_B;
   protected long field_153064_v;
   protected IStreamCallbacks field_153057_o;
   protected float field_153065_w;
   protected long field_153048_f;
   protected IngestServerTester.IngestTestListener field_153044_b = null;
   protected IStatCallbacks field_153058_p;
   private static volatile int[] $SWITCH_TABLE$net$minecraft$client$stream$IngestServerTester$IngestTestState;
   protected long field_153049_g;
   protected IngestServerTester.IngestTestState field_153047_e;
   protected IStreamCallbacks field_176005_A;
   protected boolean field_153056_n;
   protected IngestServer field_153059_q;
   protected long field_153050_h;
   protected boolean field_176008_y;
   protected RTMPState field_153051_i;

   protected void func_153031_o() {
      this.field_153059_q = null;
      if (this.field_153055_m != null) {
         for(int var1 = 0; var1 < this.field_153055_m.size(); ++var1) {
            ((FrameBuffer)this.field_153055_m.get(var1)).free();
         }

         this.field_153055_m = null;
      }

      if (this.field_153045_c.getStatCallbacks() == this.field_176006_B) {
         this.field_153045_c.setStatCallbacks(this.field_153058_p);
         this.field_153058_p = null;
      }

      if (this.field_153045_c.getStreamCallbacks() == this.field_176005_A) {
         this.field_153045_c.setStreamCallbacks(this.field_153057_o);
         this.field_153057_o = null;
      }

   }

   public IngestServerTester(Stream var1, IngestList var2) {
      this.field_153047_e = IngestServerTester.IngestTestState.Uninitalized;
      this.field_153048_f = 8000L;
      this.field_153049_g = 2000L;
      this.field_153050_h = 0L;
      this.field_153051_i = RTMPState.Invalid;
      this.field_153052_j = null;
      this.audioParameters = null;
      this.field_153054_l = 0L;
      this.field_153055_m = null;
      this.field_153056_n = false;
      this.field_153057_o = null;
      this.field_153058_p = null;
      this.field_153059_q = null;
      this.field_153060_r = false;
      this.field_153061_s = false;
      this.field_153062_t = -1;
      this.field_153063_u = 0;
      this.field_153064_v = 0L;
      this.field_153065_w = 0.0F;
      this.field_153066_x = 0.0F;
      this.field_176009_x = false;
      this.field_176008_y = false;
      this.field_176007_z = false;
      this.field_176005_A = new IStreamCallbacks(this) {
         final IngestServerTester this$0;

         public void loginCallback(ErrorCode var1, ChannelInfo var2) {
         }

         public void getArchivingStateCallback(ErrorCode var1, ArchivingState var2) {
         }

         public void setStreamInfoCallback(ErrorCode var1) {
         }

         {
            this.this$0 = var1;
         }

         public void getIngestServersCallback(ErrorCode var1, IngestList var2) {
         }

         public void startCallback(ErrorCode var1) {
            this.this$0.field_176008_y = false;
            if (ErrorCode.succeeded(var1)) {
               this.this$0.field_176009_x = true;
               this.this$0.field_153054_l = System.currentTimeMillis();
               this.this$0.func_153034_a(IngestServerTester.IngestTestState.ConnectingToServer);
            } else {
               this.this$0.field_153056_n = false;
               this.this$0.func_153034_a(IngestServerTester.IngestTestState.DoneTestingServer);
            }

         }

         public void requestAuthTokenCallback(ErrorCode var1, AuthToken var2) {
         }

         public void stopCallback(ErrorCode var1) {
            if (ErrorCode.failed(var1)) {
               System.out.println("IngestTester.stopCallback failed to stop - " + this.this$0.field_153059_q.serverName + ": " + var1.toString());
            }

            this.this$0.field_176007_z = false;
            this.this$0.field_176009_x = false;
            this.this$0.func_153034_a(IngestServerTester.IngestTestState.DoneTestingServer);
            this.this$0.field_153059_q = null;
            if (this.this$0.field_153060_r) {
               this.this$0.func_153034_a(IngestServerTester.IngestTestState.Cancelling);
            }

         }

         public void getStreamInfoCallback(ErrorCode var1, StreamInfo var2) {
         }

         public void getGameNameListCallback(ErrorCode var1, GameInfoList var2) {
         }

         public void sendEndSpanMetaDataCallback(ErrorCode var1) {
         }

         public void bufferUnlockCallback(long var1) {
         }

         public void sendActionMetaDataCallback(ErrorCode var1) {
         }

         public void runCommercialCallback(ErrorCode var1) {
         }

         public void getUserInfoCallback(ErrorCode var1, UserInfo var2) {
         }

         public void sendStartSpanMetaDataCallback(ErrorCode var1) {
         }
      };
      this.field_176006_B = new IStatCallbacks(this) {
         private static volatile int[] $SWITCH_TABLE$tv$twitch$broadcast$StatType;
         final IngestServerTester this$0;

         public void statCallback(StatType var1, long var2) {
            switch($SWITCH_TABLE$tv$twitch$broadcast$StatType()[var1.ordinal()]) {
            case 1:
               this.this$0.field_153051_i = RTMPState.lookupValue((int)var2);
               break;
            case 2:
               this.this$0.field_153050_h = var2;
            }

         }

         static int[] $SWITCH_TABLE$tv$twitch$broadcast$StatType() {
            int[] var10000 = $SWITCH_TABLE$tv$twitch$broadcast$StatType;
            if (var10000 != null) {
               return var10000;
            } else {
               int[] var0 = new int[StatType.values().length];

               try {
                  var0[StatType.TTV_ST_RTMPDATASENT.ordinal()] = 2;
               } catch (NoSuchFieldError var3) {
               }

               try {
                  var0[StatType.TTV_ST_RTMPSTATE.ordinal()] = 1;
               } catch (NoSuchFieldError var2) {
               }

               $SWITCH_TABLE$tv$twitch$broadcast$StatType = var0;
               return var0;
            }
         }

         {
            this.this$0 = var1;
         }
      };
      this.field_153045_c = var1;
      this.field_153046_d = var2;
   }

   public float func_153030_h() {
      return this.field_153066_x;
   }

   public IngestServer func_153040_c() {
      return this.field_153059_q;
   }

   public int func_153028_p() {
      return this.field_153062_t;
   }

   protected long func_153037_m() {
      return System.currentTimeMillis() - this.field_153054_l;
   }

   public void func_153042_a(IngestServerTester.IngestTestListener var1) {
      this.field_153044_b = var1;
   }

   protected void func_153038_n() {
      float var1 = (float)this.func_153037_m();
      switch($SWITCH_TABLE$net$minecraft$client$stream$IngestServerTester$IngestTestState()[this.field_153047_e.ordinal()]) {
      case 1:
      case 2:
      case 3:
      case 6:
      case 8:
      case 9:
         this.field_153066_x = 0.0F;
         break;
      case 4:
      case 7:
      default:
         this.field_153066_x = var1 / (float)this.field_153048_f;
         break;
      case 5:
         this.field_153066_x = 1.0F;
      }

      switch($SWITCH_TABLE$net$minecraft$client$stream$IngestServerTester$IngestTestState()[this.field_153047_e.ordinal()]) {
      case 6:
      case 8:
      case 9:
         this.field_153065_w = 1.0F;
         break;
      case 7:
      default:
         this.field_153065_w = (float)this.field_153062_t / (float)this.field_153046_d.getServers().length;
         this.field_153065_w += this.field_153066_x / (float)this.field_153046_d.getServers().length;
      }

   }

   public void func_153039_l() {
      // $FF: Couldn't be decompiled
   }

   public void func_176004_j() {
      if (this.field_153047_e == IngestServerTester.IngestTestState.Uninitalized) {
         this.field_153062_t = 0;
         this.field_153060_r = false;
         this.field_153061_s = false;
         this.field_176009_x = false;
         this.field_176008_y = false;
         this.field_176007_z = false;
         this.field_153058_p = this.field_153045_c.getStatCallbacks();
         this.field_153045_c.setStatCallbacks(this.field_176006_B);
         this.field_153057_o = this.field_153045_c.getStreamCallbacks();
         this.field_153045_c.setStreamCallbacks(this.field_176005_A);
         this.field_153052_j = new VideoParams();
         this.field_153052_j.targetFps = 60;
         this.field_153052_j.maxKbps = 3500;
         this.field_153052_j.outputWidth = 1280;
         this.field_153052_j.outputHeight = 720;
         this.field_153052_j.pixelFormat = PixelFormat.TTV_PF_BGRA;
         this.field_153052_j.encodingCpuUsage = EncodingCpuUsage.TTV_ECU_HIGH;
         this.field_153052_j.disableAdaptiveBitrate = true;
         this.field_153052_j.verticalFlip = false;
         this.field_153045_c.getDefaultParams(this.field_153052_j);
         this.audioParameters = new AudioParams();
         this.audioParameters.audioEnabled = false;
         this.audioParameters.enableMicCapture = false;
         this.audioParameters.enablePlaybackCapture = false;
         this.audioParameters.enablePassthroughAudio = false;
         this.field_153055_m = Lists.newArrayList();
         byte var1 = 3;

         for(int var2 = 0; var2 < var1; ++var2) {
            FrameBuffer var3 = this.field_153045_c.allocateFrameBuffer(this.field_153052_j.outputWidth * this.field_153052_j.outputHeight * 4);
            if (!var3.getIsValid()) {
               this.func_153031_o();
               this.func_153034_a(IngestServerTester.IngestTestState.Failed);
               return;
            }

            this.field_153055_m.add(var3);
            this.field_153045_c.randomizeFrameBuffer(var3);
         }

         this.func_153034_a(IngestServerTester.IngestTestState.Starting);
         this.field_153054_l = System.currentTimeMillis();
      }

   }

   protected boolean func_153036_a(IngestServer var1) {
      this.field_153056_n = true;
      this.field_153050_h = 0L;
      this.field_153051_i = RTMPState.Idle;
      this.field_153059_q = var1;
      this.field_176008_y = true;
      this.func_153034_a(IngestServerTester.IngestTestState.ConnectingToServer);
      ErrorCode var2 = this.field_153045_c.start(this.field_153052_j, this.audioParameters, var1, StartFlags.TTV_Start_BandwidthTest, true);
      if (ErrorCode.failed(var2)) {
         this.field_176008_y = false;
         this.field_153056_n = false;
         this.func_153034_a(IngestServerTester.IngestTestState.DoneTestingServer);
         return false;
      } else {
         this.field_153064_v = this.field_153050_h;
         var1.bitrateKbps = 0.0F;
         this.field_153063_u = 0;
         return true;
      }
   }

   static int[] $SWITCH_TABLE$net$minecraft$client$stream$IngestServerTester$IngestTestState() {
      int[] var10000 = $SWITCH_TABLE$net$minecraft$client$stream$IngestServerTester$IngestTestState;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[IngestServerTester.IngestTestState.values().length];

         try {
            var0[IngestServerTester.IngestTestState.Cancelled.ordinal()] = 8;
         } catch (NoSuchFieldError var10) {
         }

         try {
            var0[IngestServerTester.IngestTestState.Cancelling.ordinal()] = 7;
         } catch (NoSuchFieldError var9) {
         }

         try {
            var0[IngestServerTester.IngestTestState.ConnectingToServer.ordinal()] = 3;
         } catch (NoSuchFieldError var8) {
         }

         try {
            var0[IngestServerTester.IngestTestState.DoneTestingServer.ordinal()] = 5;
         } catch (NoSuchFieldError var7) {
         }

         try {
            var0[IngestServerTester.IngestTestState.Failed.ordinal()] = 9;
         } catch (NoSuchFieldError var6) {
         }

         try {
            var0[IngestServerTester.IngestTestState.Finished.ordinal()] = 6;
         } catch (NoSuchFieldError var5) {
         }

         try {
            var0[IngestServerTester.IngestTestState.Starting.ordinal()] = 2;
         } catch (NoSuchFieldError var4) {
         }

         try {
            var0[IngestServerTester.IngestTestState.TestingServer.ordinal()] = 4;
         } catch (NoSuchFieldError var3) {
         }

         try {
            var0[IngestServerTester.IngestTestState.Uninitalized.ordinal()] = 1;
         } catch (NoSuchFieldError var2) {
         }

         $SWITCH_TABLE$net$minecraft$client$stream$IngestServerTester$IngestTestState = var0;
         return var0;
      }
   }

   protected void func_153035_b(IngestServer var1) {
      if (this.field_176008_y) {
         this.field_153061_s = true;
      } else if (this.field_176009_x) {
         this.field_176007_z = true;
         ErrorCode var2 = this.field_153045_c.stop(true);
         if (ErrorCode.failed(var2)) {
            this.field_176005_A.stopCallback(ErrorCode.TTV_EC_SUCCESS);
            System.out.println("Stop failed: " + var2.toString());
         }

         this.field_153045_c.pollStats();
      } else {
         this.field_176005_A.stopCallback(ErrorCode.TTV_EC_SUCCESS);
      }

   }

   public void func_153041_j() {
      // $FF: Couldn't be decompiled
   }

   protected boolean func_153029_c(IngestServer var1) {
      if (!this.field_153061_s && !this.field_153060_r && this.func_153037_m() < this.field_153048_f) {
         if (!this.field_176008_y && !this.field_176007_z) {
            ErrorCode var2 = this.field_153045_c.submitVideoFrame((FrameBuffer)this.field_153055_m.get(this.field_153063_u));
            if (ErrorCode.failed(var2)) {
               this.field_153056_n = false;
               this.func_153034_a(IngestServerTester.IngestTestState.DoneTestingServer);
               return false;
            } else {
               this.field_153063_u = (this.field_153063_u + 1) % this.field_153055_m.size();
               this.field_153045_c.pollStats();
               if (this.field_153051_i == RTMPState.SendVideo) {
                  this.func_153034_a(IngestServerTester.IngestTestState.TestingServer);
                  long var3 = this.func_153037_m();
                  if (var3 > 0L && this.field_153050_h > this.field_153064_v) {
                     var1.bitrateKbps = (float)(this.field_153050_h * 8L) / (float)this.func_153037_m();
                     this.field_153064_v = this.field_153050_h;
                  }
               }

               return true;
            }
         } else {
            return true;
         }
      } else {
         this.func_153034_a(IngestServerTester.IngestTestState.DoneTestingServer);
         return true;
      }
   }

   protected void func_153034_a(IngestServerTester.IngestTestState var1) {
      if (var1 != this.field_153047_e) {
         this.field_153047_e = var1;
         if (this.field_153044_b != null) {
            this.field_153044_b.func_152907_a(this, var1);
         }
      }

   }

   public interface IngestTestListener {
      void func_152907_a(IngestServerTester var1, IngestServerTester.IngestTestState var2);
   }

   public static enum IngestTestState {
      ConnectingToServer,
      Starting,
      Finished,
      DoneTestingServer;

      private static final IngestServerTester.IngestTestState[] ENUM$VALUES = new IngestServerTester.IngestTestState[]{Uninitalized, Starting, ConnectingToServer, TestingServer, DoneTestingServer, Finished, Cancelling, Cancelled, Failed};
      Cancelled,
      Failed,
      Cancelling,
      TestingServer,
      Uninitalized;
   }
}
