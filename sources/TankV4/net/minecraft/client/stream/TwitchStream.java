package net.minecraft.client.stream;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.properties.Property;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.stream.GuiTwitchUserMode;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.HttpUtil;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.lwjgl.opengl.GL11;
import tv.twitch.AuthToken;
import tv.twitch.ErrorCode;
import tv.twitch.broadcast.EncodingCpuUsage;
import tv.twitch.broadcast.FrameBuffer;
import tv.twitch.broadcast.GameInfo;
import tv.twitch.broadcast.IngestList;
import tv.twitch.broadcast.IngestServer;
import tv.twitch.broadcast.StreamInfo;
import tv.twitch.broadcast.VideoParams;
import tv.twitch.chat.ChatRawMessage;
import tv.twitch.chat.ChatTokenizedMessage;
import tv.twitch.chat.ChatUserInfo;

public class TwitchStream implements IngestServerTester.IngestTestListener, BroadcastController.BroadcastListener, ChatController.ChatListener, IStream {
   private boolean field_152962_n;
   private boolean field_152963_o;
   public static final Marker STREAM_MARKER = MarkerManager.getMarker("STREAM");
   private final BroadcastController broadcastController;
   private final IChatComponent twitchComponent = new ChatComponentText("Twitch");
   private String field_176029_e;
   private Framebuffer framebuffer;
   private boolean loggedIn;
   private boolean field_152957_i;
   private final ChatController chatController;
   private IStream.AuthFailureReason authFailureReason;
   private int targetFPS = 30;
   private final Minecraft mc;
   private final Map field_152955_g = Maps.newHashMap();
   private static boolean field_152965_q;
   private boolean field_152960_l = false;
   private static final Logger LOGGER = LogManager.getLogger();
   private long field_152959_k = 0L;

   public void func_152930_t() {
      GameSettings var1 = this.mc.gameSettings;
      VideoParams var2 = this.broadcastController.func_152834_a(formatStreamKbps(var1.streamKbps), formatStreamFps(var1.streamFps), formatStreamBps(var1.streamBytesPerPixel), (float)this.mc.displayWidth / (float)this.mc.displayHeight);
      switch(var1.streamCompression) {
      case 0:
         var2.encodingCpuUsage = EncodingCpuUsage.TTV_ECU_LOW;
         break;
      case 1:
         var2.encodingCpuUsage = EncodingCpuUsage.TTV_ECU_MEDIUM;
         break;
      case 2:
         var2.encodingCpuUsage = EncodingCpuUsage.TTV_ECU_HIGH;
      }

      if (this.framebuffer == null) {
         this.framebuffer = new Framebuffer(var2.outputWidth, var2.outputHeight, false);
      } else {
         this.framebuffer.createBindFramebuffer(var2.outputWidth, var2.outputHeight);
      }

      if (var1.streamPreferredServer != null && var1.streamPreferredServer.length() > 0) {
         IngestServer[] var6;
         int var5 = (var6 = this.func_152925_v()).length;

         for(int var4 = 0; var4 < var5; ++var4) {
            IngestServer var3 = var6[var4];
            if (var3.serverUrl.equals(var1.streamPreferredServer)) {
               this.broadcastController.func_152824_a(var3);
               break;
            }
         }
      }

      this.targetFPS = var2.targetFps;
      this.field_152957_i = var1.streamSendMetadata;
      this.broadcastController.func_152836_a(var2);
      LOGGER.info(STREAM_MARKER, "Streaming at {}/{} at {} kbps to {}", var2.outputWidth, var2.outputHeight, var2.maxKbps, this.broadcastController.func_152833_s().serverUrl);
      this.broadcastController.func_152828_a((String)null, "Minecraft", (String)null);
   }

   public void func_152893_b(ErrorCode var1) {
      LOGGER.warn(STREAM_MARKER, "Issue submitting frame: {} (Error code {})", ErrorCode.getString(var1), var1.getValue());
      this.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new ChatComponentText("Issue streaming frame: " + var1 + " (" + ErrorCode.getString(var1) + ")"), 2);
   }

   public static int formatStreamKbps(float var0) {
      return MathHelper.floor_float(230.0F + var0 * 3270.0F);
   }

   public boolean func_152927_B() {
      return this.field_176029_e != null && this.field_176029_e.equals(this.broadcastController.getChannelInfo().name);
   }

   public void func_152922_k() {
      if (this.broadcastController.isBroadcasting() && !this.broadcastController.isBroadcastPaused()) {
         long var1 = System.nanoTime();
         long var3 = (long)(1000000000 / this.targetFPS);
         long var5 = var1 - this.field_152959_k;
         boolean var7 = var5 >= var3;
         if (var7) {
            FrameBuffer var8 = this.broadcastController.func_152822_N();
            Framebuffer var9 = this.mc.getFramebuffer();
            this.framebuffer.bindFramebuffer(true);
            GlStateManager.matrixMode(5889);
            GlStateManager.pushMatrix();
            GlStateManager.loadIdentity();
            GlStateManager.ortho(0.0D, (double)this.framebuffer.framebufferWidth, (double)this.framebuffer.framebufferHeight, 0.0D, 1000.0D, 3000.0D);
            GlStateManager.matrixMode(5888);
            GlStateManager.pushMatrix();
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0F, 0.0F, -2000.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.viewport(0, 0, this.framebuffer.framebufferWidth, this.framebuffer.framebufferHeight);
            GlStateManager.enableTexture2D();
            GlStateManager.disableAlpha();
            GlStateManager.disableBlend();
            float var10 = (float)this.framebuffer.framebufferWidth;
            float var11 = (float)this.framebuffer.framebufferHeight;
            float var12 = (float)var9.framebufferWidth / (float)var9.framebufferTextureWidth;
            float var13 = (float)var9.framebufferHeight / (float)var9.framebufferTextureHeight;
            var9.bindFramebufferTexture();
            GL11.glTexParameterf(3553, 10241, 9729.0F);
            GL11.glTexParameterf(3553, 10240, 9729.0F);
            Tessellator var14 = Tessellator.getInstance();
            WorldRenderer var15 = var14.getWorldRenderer();
            var15.begin(7, DefaultVertexFormats.POSITION_TEX);
            var15.pos(0.0D, (double)var11, 0.0D).tex(0.0D, (double)var13).endVertex();
            var15.pos((double)var10, (double)var11, 0.0D).tex((double)var12, (double)var13).endVertex();
            var15.pos((double)var10, 0.0D, 0.0D).tex((double)var12, 0.0D).endVertex();
            var15.pos(0.0D, 0.0D, 0.0D).tex(0.0D, 0.0D).endVertex();
            var14.draw();
            var9.unbindFramebufferTexture();
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5889);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
            this.broadcastController.captureFramebuffer(var8);
            this.framebuffer.unbindFramebuffer();
            this.broadcastController.submitStreamFrame(var8);
            this.field_152959_k = var1;
         }
      }

   }

   public void func_152898_a(ErrorCode var1, GameInfo[] var2) {
   }

   public void func_152911_a(Metadata var1, long var2) {
      if (this.isBroadcasting() && this.field_152957_i) {
         long var4 = this.broadcastController.func_152844_x();
         if (!this.broadcastController.func_152840_a(var1.func_152810_c(), var4 + var2, var1.func_152809_a(), var1.func_152806_b())) {
            LOGGER.warn(STREAM_MARKER, "Couldn't send stream metadata action at {}: {}", var4 + var2, var1);
         } else {
            LOGGER.debug(STREAM_MARKER, "Sent stream metadata action at {}: {}", var4 + var2, var1);
         }
      }

   }

   public void func_152935_j() {
      int var1 = this.mc.gameSettings.streamChatEnabled;
      boolean var2 = this.field_176029_e != null && this.chatController.func_175990_d(this.field_176029_e);
      boolean var3 = this.chatController.func_153000_j() == ChatController.ChatState.Initialized && (this.field_176029_e == null || this.chatController.func_175989_e(this.field_176029_e) == ChatController.EnumChannelState.Disconnected);
      if (var1 == 2) {
         if (var2) {
            LOGGER.debug(STREAM_MARKER, "Disconnecting from twitch chat per user options");
            this.chatController.func_175991_l(this.field_176029_e);
         }
      } else if (var1 == 1) {
         if (var3 && this.broadcastController.func_152849_q()) {
            LOGGER.debug(STREAM_MARKER, "Connecting to twitch chat per user options");
            this.func_152942_I();
         }
      } else if (var1 == 0) {
         if (var2 && !this.isBroadcasting()) {
            LOGGER.debug(STREAM_MARKER, "Disconnecting from twitch chat as user is no longer streaming");
            this.chatController.func_175991_l(this.field_176029_e);
         } else if (var3 && this.isBroadcasting()) {
            LOGGER.debug(STREAM_MARKER, "Connecting to twitch chat as user is streaming");
            this.func_152942_I();
         }
      }

      this.broadcastController.func_152821_H();
      this.chatController.func_152997_n();
   }

   public void func_152894_a(StreamInfo var1) {
      LOGGER.debug(STREAM_MARKER, "Stream info updated; {} viewers on stream ID {}", var1.viewers, var1.streamId);
   }

   public void func_176026_a(Metadata var1, long var2, long var4) {
      if (this.isBroadcasting() && this.field_152957_i) {
         long var6 = this.broadcastController.func_152844_x();
         String var8 = var1.func_152809_a();
         String var9 = var1.func_152806_b();
         long var10 = this.broadcastController.func_177946_b(var1.func_152810_c(), var6 + var2, var8, var9);
         if (var10 < 0L) {
            LOGGER.warn(STREAM_MARKER, "Could not send stream metadata sequence from {} to {}: {}", var6 + var2, var6 + var4, var1);
         } else if (this.broadcastController.func_177947_a(var1.func_152810_c(), var6 + var4, var10, var8, var9)) {
            LOGGER.debug(STREAM_MARKER, "Sent stream metadata sequence from {} to {}: {}", var6 + var2, var6 + var4, var1);
         } else {
            LOGGER.warn(STREAM_MARKER, "Half-sent stream metadata sequence from {} to {}: {}", var6 + var2, var6 + var4, var1);
         }
      }

   }

   public ErrorCode func_152912_E() {
      return !field_152965_q ? ErrorCode.TTV_EC_OS_TOO_OLD : this.broadcastController.getErrorCode();
   }

   public void func_152909_x() {
      IngestServerTester var1 = this.broadcastController.func_152838_J();
      if (var1 != null) {
         var1.func_153042_a(this);
      }

   }

   public int func_152920_A() {
      return this.isBroadcasting() ? this.broadcastController.getStreamInfo().viewers : 0;
   }

   public void func_152896_a(IngestList var1) {
   }

   public void requestCommercial() {
      if (this.broadcastController.requestCommercial()) {
         LOGGER.debug(STREAM_MARKER, "Requested commercial from Twitch");
      } else {
         LOGGER.warn(STREAM_MARKER, "Could not request commercial from Twitch");
      }

   }

   static {
      try {
         if (Util.getOSType() == Util.EnumOS.WINDOWS) {
            System.loadLibrary("avutil-ttv-51");
            System.loadLibrary("swresample-ttv-0");
            System.loadLibrary("libmp3lame-ttv");
            if (System.getProperty("os.arch").contains("64")) {
               System.loadLibrary("libmfxsw64");
            } else {
               System.loadLibrary("libmfxsw32");
            }
         }

         field_152965_q = true;
      } catch (Throwable var1) {
         field_152965_q = false;
      }

   }

   public boolean func_152913_F() {
      return this.loggedIn;
   }

   public String func_152921_C() {
      return this.field_176029_e;
   }

   public boolean isBroadcasting() {
      return this.broadcastController.isBroadcasting();
   }

   public ChatUserInfo func_152926_a(String var1) {
      return (ChatUserInfo)this.field_152955_g.get(var1);
   }

   public static int formatStreamFps(float var0) {
      return MathHelper.floor_float(10.0F + var0 * 50.0F);
   }

   public void func_152901_c() {
      LOGGER.info(STREAM_MARKER, "Broadcast to Twitch has stopped");
   }

   static void access$3(TwitchStream var0, IStream.AuthFailureReason var1) {
      var0.authFailureReason = var1;
   }

   public boolean isReadyToBroadcast() {
      return this.broadcastController.isReadyToBroadcast();
   }

   public void func_152907_a(IngestServerTester var1, IngestServerTester.IngestTestState var2) {
      LOGGER.debug(STREAM_MARKER, "Ingest test state changed to {}", var2);
      if (var2 == IngestServerTester.IngestTestState.Finished) {
         this.field_152960_l = true;
      }

   }

   public void func_152899_b() {
      this.updateStreamVolume();
      LOGGER.info(STREAM_MARKER, "Broadcast to Twitch has started");
   }

   public void muteMicrophone(boolean var1) {
      this.field_152963_o = var1;
      this.updateStreamVolume();
   }

   static ChatController access$2(TwitchStream var0) {
      return var0.chatController;
   }

   public void func_152892_c(ErrorCode var1) {
      ChatComponentTranslation var2;
      if (var1 == ErrorCode.TTV_EC_SOUNDFLOWER_NOT_INSTALLED) {
         var2 = new ChatComponentTranslation("stream.unavailable.soundflower.chat.link", new Object[0]);
         var2.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://help.mojang.com/customer/portal/articles/1374877-configuring-soundflower-for-streaming-on-apple-computers"));
         var2.getChatStyle().setUnderlined(true);
         ChatComponentTranslation var3 = new ChatComponentTranslation("stream.unavailable.soundflower.chat", new Object[]{var2});
         var3.getChatStyle().setColor(EnumChatFormatting.DARK_RED);
         this.mc.ingameGUI.getChatGUI().printChatMessage(var3);
      } else {
         var2 = new ChatComponentTranslation("stream.unavailable.unknown.chat", new Object[]{ErrorCode.getString(var1)});
         var2.getChatStyle().setColor(EnumChatFormatting.DARK_RED);
         this.mc.ingameGUI.getChatGUI().printChatMessage(var2);
      }

   }

   protected void func_152942_I() {
      ChatController.ChatState var1 = this.chatController.func_153000_j();
      String var2 = this.broadcastController.getChannelInfo().name;
      this.field_176029_e = var2;
      if (var1 != ChatController.ChatState.Initialized) {
         LOGGER.warn("Invalid twitch chat state {}", var1);
      } else if (this.chatController.func_175989_e(this.field_176029_e) == ChatController.EnumChannelState.Disconnected) {
         this.chatController.func_152986_d(var2);
      } else {
         LOGGER.warn("Invalid twitch chat state {}", var1);
      }

   }

   public void func_180607_b(String var1) {
      LOGGER.debug(STREAM_MARKER, "Chat disconnected");
      this.field_152955_g.clear();
   }

   public void func_180605_a(String var1, ChatRawMessage[] var2) {
      ChatRawMessage[] var6 = var2;
      int var5 = var2.length;

      for(int var4 = 0; var4 < var5; ++var4) {
         ChatRawMessage var3 = var6[var4];
         this.func_176027_a(var3.userName, var3);
         HashSet var10001 = var3.modes;
         HashSet var10002 = var3.subscriptions;
         if (this.mc.gameSettings.streamChatUserFilter != 0) {
            ChatComponentText var7 = new ChatComponentText(var3.userName);
            ChatComponentTranslation var8 = new ChatComponentTranslation("chat.stream." + (var3.action ? "emote" : "text"), new Object[]{this.twitchComponent, var7, EnumChatFormatting.getTextWithoutFormattingCodes(var3.message)});
            if (var3.action) {
               var8.getChatStyle().setItalic(true);
            }

            ChatComponentText var9 = new ChatComponentText("");
            var9.appendSibling(new ChatComponentTranslation("stream.userinfo.chatTooltip", new Object[0]));
            Iterator var11 = GuiTwitchUserMode.func_152328_a(var3.modes, var3.subscriptions, (IStream)null).iterator();

            while(var11.hasNext()) {
               IChatComponent var10 = (IChatComponent)var11.next();
               var9.appendText("\n");
               var9.appendSibling(var10);
            }

            var7.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, var9));
            var7.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.TWITCH_USER_INFO, var3.userName));
            this.mc.ingameGUI.getChatGUI().printChatMessage(var8);
         }
      }

   }

   public IngestServer[] func_152925_v() {
      return this.broadcastController.func_152855_t().getServers();
   }

   public void updateStreamVolume() {
      if (this.isBroadcasting()) {
         float var1 = this.mc.gameSettings.streamGameVolume;
         boolean var2 = this.field_152962_n || var1 <= 0.0F;
         this.broadcastController.setPlaybackDeviceVolume(var2 ? 0.0F : var1);
         this.broadcastController.setRecordingDeviceVolume(this != false ? 0.0F : this.mc.gameSettings.streamMicVolume);
      }

   }

   public void unpause() {
      this.broadcastController.func_152854_G();
      this.field_152962_n = false;
      this.updateStreamVolume();
   }

   public void stopBroadcasting() {
      if (this.broadcastController.stopBroadcasting()) {
         LOGGER.info(STREAM_MARKER, "Stopped streaming to Twitch");
      } else {
         LOGGER.warn(STREAM_MARKER, "Could not stop streaming to Twitch");
      }

   }

   public void func_152895_a() {
      LOGGER.info(STREAM_MARKER, "Logged out of twitch");
   }

   public void func_152897_a(ErrorCode var1) {
      if (ErrorCode.succeeded(var1)) {
         LOGGER.debug(STREAM_MARKER, "Login attempt successful");
         this.loggedIn = true;
      } else {
         LOGGER.warn(STREAM_MARKER, "Login attempt unsuccessful: {} (error code {})", ErrorCode.getString(var1), var1.getValue());
         this.loggedIn = false;
      }

   }

   public void func_176017_a(ChatController.ChatState var1) {
   }

   public void func_176019_a(String var1, String var2) {
   }

   public void func_176018_a(String var1, ChatUserInfo[] var2, ChatUserInfo[] var3, ChatUserInfo[] var4) {
      ChatUserInfo[] var8 = var3;
      int var7 = var3.length;

      ChatUserInfo var5;
      int var6;
      for(var6 = 0; var6 < var7; ++var6) {
         var5 = var8[var6];
         this.field_152955_g.remove(var5.displayName);
      }

      var8 = var4;
      var7 = var4.length;

      for(var6 = 0; var6 < var7; ++var6) {
         var5 = var8[var6];
         this.field_152955_g.put(var5.displayName, var5);
      }

      var8 = var2;
      var7 = var2.length;

      for(var6 = 0; var6 < var7; ++var6) {
         var5 = var8[var6];
         this.field_152955_g.put(var5.displayName, var5);
      }

   }

   public void func_176025_a(String var1, ChatTokenizedMessage[] var2) {
   }

   public boolean func_152936_l() {
      return this.broadcastController.func_152849_q();
   }

   public void shutdownStream() {
      LOGGER.debug(STREAM_MARKER, "Shutdown streaming");
      this.broadcastController.statCallback();
      this.chatController.func_175988_p();
   }

   public void func_176021_d() {
   }

   static Logger access$0() {
      return LOGGER;
   }

   public void func_176023_d(ErrorCode var1) {
      if (ErrorCode.failed(var1)) {
         LOGGER.error(STREAM_MARKER, "Chat failed to initialize");
      }

   }

   public static float formatStreamBps(float var0) {
      return 0.1F + var0 * 0.1F;
   }

   public boolean func_152908_z() {
      return this.broadcastController.isIngestTesting();
   }

   public void func_152900_a(ErrorCode var1, AuthToken var2) {
   }

   public void func_176020_d(String var1) {
   }

   public void func_152891_a(BroadcastController.BroadcastState var1) {
      LOGGER.debug(STREAM_MARKER, "Broadcast state changed to {}", var1);
      if (var1 == BroadcastController.BroadcastState.Initialized) {
         this.broadcastController.func_152827_a(BroadcastController.BroadcastState.Authenticated);
      }

   }

   public IStream.AuthFailureReason func_152918_H() {
      return this.authFailureReason;
   }

   private void func_176027_a(String var1, ChatRawMessage var2) {
      ChatUserInfo var3 = (ChatUserInfo)this.field_152955_g.get(var1);
      if (var3 == null) {
         var3 = new ChatUserInfo();
         var3.displayName = var1;
         this.field_152955_g.put(var1, var3);
      }

      var3.subscriptions = var2.subscriptions;
      var3.modes = var2.modes;
      var3.nameColorARGB = var2.nameColorARGB;
   }

   public void func_176024_e() {
   }

   public boolean isPaused() {
      return this.broadcastController.isBroadcastPaused();
   }

   public TwitchStream(Minecraft var1, Property var2) {
      this.authFailureReason = IStream.AuthFailureReason.ERROR;
      this.mc = var1;
      this.broadcastController = new BroadcastController();
      this.chatController = new ChatController();
      this.broadcastController.func_152841_a(this);
      this.chatController.func_152990_a(this);
      this.broadcastController.func_152842_a("nmt37qblda36pvonovdkbopzfzw3wlq");
      this.chatController.func_152984_a("nmt37qblda36pvonovdkbopzfzw3wlq");
      this.twitchComponent.getChatStyle().setColor(EnumChatFormatting.DARK_PURPLE);
      if (var2 != null && !Strings.isNullOrEmpty(var2.getValue()) && OpenGlHelper.framebufferSupported) {
         Thread var3 = new Thread(this, "Twitch authenticator", var2) {
            private final Property val$streamProperty;
            final TwitchStream this$0;

            static TwitchStream access$0(Object var0) {
               return var0.this$0;
            }

            public void run() {
               try {
                  URL var1 = new URL("https://api.twitch.tv/kraken?oauth_token=" + URLEncoder.encode(this.val$streamProperty.getValue(), "UTF-8"));
                  String var2 = HttpUtil.get(var1);
                  JsonObject var3 = JsonUtils.getJsonObject((new JsonParser()).parse(var2), "Response");
                  JsonObject var4 = JsonUtils.getJsonObject(var3, "token");
                  if (JsonUtils.getBoolean(var4, "valid")) {
                     String var5 = JsonUtils.getString(var4, "user_name");
                     TwitchStream.access$0().debug(TwitchStream.STREAM_MARKER, "Authenticated with twitch; username is {}", var5);
                     AuthToken var6 = new AuthToken();
                     var6.data = this.val$streamProperty.getValue();
                     TwitchStream.access$1(this.this$0).func_152818_a(var5, var6);
                     TwitchStream.access$2(this.this$0).func_152998_c(var5);
                     TwitchStream.access$2(this.this$0).func_152994_a(var6);
                     Runtime.getRuntime().addShutdownHook(new Thread(this, "Twitch shutdown hook") {
                        final <undefinedtype> this$1;

                        {
                           this.this$1 = var1;
                        }

                        public void run() {
                           null.access$0(this.this$1).shutdownStream();
                        }
                     });
                     TwitchStream.access$1(this.this$0).func_152817_A();
                     TwitchStream.access$2(this.this$0).func_175984_n();
                  } else {
                     TwitchStream.access$3(this.this$0, IStream.AuthFailureReason.INVALID_TOKEN);
                     TwitchStream.access$0().error(TwitchStream.STREAM_MARKER, "Given twitch access token is invalid");
                  }
               } catch (IOException var8) {
                  TwitchStream.access$3(this.this$0, IStream.AuthFailureReason.ERROR);
                  TwitchStream.access$0().error(TwitchStream.STREAM_MARKER, (String)"Could not authenticate with twitch", (Throwable)var8);
               }

            }

            {
               this.this$0 = var1;
               this.val$streamProperty = var3;
            }
         };
         var3.setDaemon(true);
         var3.start();
      }

   }

   static BroadcastController access$1(TwitchStream var0) {
      return var0.broadcastController;
   }

   public void func_152917_b(String var1) {
      this.chatController.func_175986_a(this.field_176029_e, var1);
   }

   public boolean func_152928_D() {
      return field_152965_q && this.broadcastController.func_152858_b();
   }

   public void func_180606_a(String var1) {
      LOGGER.debug(STREAM_MARKER, "Chat connected");
   }

   public void pause() {
      this.broadcastController.func_152847_F();
      this.field_152962_n = true;
      this.updateStreamVolume();
   }

   public void func_176022_e(ErrorCode var1) {
      if (ErrorCode.failed(var1)) {
         LOGGER.error(STREAM_MARKER, "Chat failed to shutdown");
      }

   }

   public void func_176016_c(String var1) {
   }

   public IngestServerTester func_152932_y() {
      return this.broadcastController.isReady();
   }
}
