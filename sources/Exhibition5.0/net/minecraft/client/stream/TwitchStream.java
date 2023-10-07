// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.stream;

import net.minecraft.util.Util;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.LogManager;
import tv.twitch.chat.ChatUserSubscription;
import tv.twitch.chat.ChatUserMode;
import tv.twitch.chat.ChatUserInfo;
import tv.twitch.chat.ChatTokenizedMessage;
import java.util.Iterator;
import net.minecraft.event.HoverEvent;
import net.minecraft.client.gui.stream.GuiTwitchUserMode;
import java.util.Set;
import tv.twitch.chat.ChatRawMessage;
import net.minecraft.util.MathHelper;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentTranslation;
import tv.twitch.broadcast.IngestList;
import tv.twitch.broadcast.StreamInfo;
import tv.twitch.broadcast.GameInfo;
import tv.twitch.ErrorCode;
import tv.twitch.broadcast.IngestServer;
import tv.twitch.broadcast.VideoParams;
import net.minecraft.client.settings.GameSettings;
import tv.twitch.broadcast.EncodingCpuUsage;
import net.minecraft.client.renderer.WorldRenderer;
import tv.twitch.broadcast.FrameBuffer;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import com.google.gson.JsonObject;
import java.io.IOException;
import tv.twitch.AuthToken;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonParser;
import net.minecraft.util.HttpUtil;
import java.net.URL;
import java.net.URLEncoder;
import net.minecraft.client.renderer.OpenGlHelper;
import com.google.common.base.Strings;
import net.minecraft.util.EnumChatFormatting;
import com.google.common.collect.Maps;
import net.minecraft.util.ChatComponentText;
import com.mojang.authlib.properties.Property;
import net.minecraft.client.shader.Framebuffer;
import java.util.Map;
import net.minecraft.util.IChatComponent;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Logger;

public class TwitchStream implements BroadcastController.BroadcastListener, ChatController.ChatListener, IngestServerTester.IngestTestListener, IStream
{
    private static final Logger field_152950_b;
    public static final Marker field_152949_a;
    private final BroadcastController broadcastController;
    private final ChatController field_152952_d;
    private String field_176029_e;
    private final Minecraft field_152953_e;
    private final IChatComponent field_152954_f;
    private final Map field_152955_g;
    private Framebuffer field_152956_h;
    private boolean field_152957_i;
    private int field_152958_j;
    private long field_152959_k;
    private boolean field_152960_l;
    private boolean field_152961_m;
    private boolean field_152962_n;
    private boolean field_152963_o;
    private AuthFailureReason field_152964_p;
    private static boolean field_152965_q;
    private static final String __OBFID = "CL_00001812";
    
    public TwitchStream(final Minecraft mcIn, final Property p_i46057_2_) {
        this.field_152954_f = new ChatComponentText("Twitch");
        this.field_152955_g = Maps.newHashMap();
        this.field_152958_j = 30;
        this.field_152959_k = 0L;
        this.field_152960_l = false;
        this.field_152964_p = AuthFailureReason.ERROR;
        this.field_152953_e = mcIn;
        this.broadcastController = new BroadcastController();
        this.field_152952_d = new ChatController();
        this.broadcastController.func_152841_a(this);
        this.field_152952_d.func_152990_a(this);
        this.broadcastController.func_152842_a("nmt37qblda36pvonovdkbopzfzw3wlq");
        this.field_152952_d.func_152984_a("nmt37qblda36pvonovdkbopzfzw3wlq");
        this.field_152954_f.getChatStyle().setColor(EnumChatFormatting.DARK_PURPLE);
        if (p_i46057_2_ != null && !Strings.isNullOrEmpty(p_i46057_2_.getValue()) && OpenGlHelper.framebufferSupported) {
            final Thread var3 = new Thread("Twitch authenticator") {
                private static final String __OBFID = "CL_00001811";
                
                @Override
                public void run() {
                    try {
                        final URL var1 = new URL("https://api.twitch.tv/kraken?oauth_token=" + URLEncoder.encode(p_i46057_2_.getValue(), "UTF-8"));
                        final String var2 = HttpUtil.get(var1);
                        final JsonObject var3 = JsonUtils.getElementAsJsonObject(new JsonParser().parse(var2), "Response");
                        final JsonObject var4 = JsonUtils.getJsonObject(var3, "token");
                        if (JsonUtils.getJsonObjectBooleanFieldValue(var4, "valid")) {
                            final String var5 = JsonUtils.getJsonObjectStringFieldValue(var4, "user_name");
                            TwitchStream.field_152950_b.debug(TwitchStream.field_152949_a, "Authenticated with twitch; username is {}", new Object[] { var5 });
                            final AuthToken var6 = new AuthToken();
                            var6.data = p_i46057_2_.getValue();
                            TwitchStream.this.broadcastController.func_152818_a(var5, var6);
                            TwitchStream.this.field_152952_d.func_152998_c(var5);
                            TwitchStream.this.field_152952_d.func_152994_a(var6);
                            Runtime.getRuntime().addShutdownHook(new Thread("Twitch shutdown hook") {
                                private static final String __OBFID = "CL_00001810";
                                
                                @Override
                                public void run() {
                                    TwitchStream.this.shutdownStream();
                                }
                            });
                            TwitchStream.this.broadcastController.func_152817_A();
                            TwitchStream.this.field_152952_d.func_175984_n();
                        }
                        else {
                            TwitchStream.this.field_152964_p = AuthFailureReason.INVALID_TOKEN;
                            TwitchStream.field_152950_b.error(TwitchStream.field_152949_a, "Given twitch access token is invalid");
                        }
                    }
                    catch (IOException var7) {
                        TwitchStream.this.field_152964_p = AuthFailureReason.ERROR;
                        TwitchStream.field_152950_b.error(TwitchStream.field_152949_a, "Could not authenticate with twitch", (Throwable)var7);
                    }
                }
            };
            var3.setDaemon(true);
            var3.start();
        }
    }
    
    @Override
    public void shutdownStream() {
        TwitchStream.field_152950_b.debug(TwitchStream.field_152949_a, "Shutdown streaming");
        this.broadcastController.statCallback();
        this.field_152952_d.func_175988_p();
    }
    
    @Override
    public void func_152935_j() {
        final int var1 = this.field_152953_e.gameSettings.streamChatEnabled;
        final boolean var2 = this.field_176029_e != null && this.field_152952_d.func_175990_d(this.field_176029_e);
        final boolean var3 = this.field_152952_d.func_153000_j() == ChatController.ChatState.Initialized && (this.field_176029_e == null || this.field_152952_d.func_175989_e(this.field_176029_e) == ChatController.EnumChannelState.Disconnected);
        if (var1 == 2) {
            if (var2) {
                TwitchStream.field_152950_b.debug(TwitchStream.field_152949_a, "Disconnecting from twitch chat per user options");
                this.field_152952_d.func_175991_l(this.field_176029_e);
            }
        }
        else if (var1 == 1) {
            if (var3 && this.broadcastController.func_152849_q()) {
                TwitchStream.field_152950_b.debug(TwitchStream.field_152949_a, "Connecting to twitch chat per user options");
                this.func_152942_I();
            }
        }
        else if (var1 == 0) {
            if (var2 && !this.func_152934_n()) {
                TwitchStream.field_152950_b.debug(TwitchStream.field_152949_a, "Disconnecting from twitch chat as user is no longer streaming");
                this.field_152952_d.func_175991_l(this.field_176029_e);
            }
            else if (var3 && this.func_152934_n()) {
                TwitchStream.field_152950_b.debug(TwitchStream.field_152949_a, "Connecting to twitch chat as user is streaming");
                this.func_152942_I();
            }
        }
        this.broadcastController.func_152821_H();
        this.field_152952_d.func_152997_n();
    }
    
    protected void func_152942_I() {
        final ChatController.ChatState var1 = this.field_152952_d.func_153000_j();
        final String var2 = this.broadcastController.func_152843_l().name;
        this.field_176029_e = var2;
        if (var1 != ChatController.ChatState.Initialized) {
            TwitchStream.field_152950_b.warn("Invalid twitch chat state {}", new Object[] { var1 });
        }
        else if (this.field_152952_d.func_175989_e(this.field_176029_e) == ChatController.EnumChannelState.Disconnected) {
            this.field_152952_d.func_152986_d(var2);
        }
        else {
            TwitchStream.field_152950_b.warn("Invalid twitch chat state {}", new Object[] { var1 });
        }
    }
    
    @Override
    public void func_152922_k() {
        if (this.broadcastController.isBroadcasting() && !this.broadcastController.isBroadcastPaused()) {
            final long var1 = System.nanoTime();
            final long var2 = 1000000000 / this.field_152958_j;
            final long var3 = var1 - this.field_152959_k;
            final boolean var4 = var3 >= var2;
            if (var4) {
                final FrameBuffer var5 = this.broadcastController.func_152822_N();
                final Framebuffer var6 = this.field_152953_e.getFramebuffer();
                this.field_152956_h.bindFramebuffer(true);
                GlStateManager.matrixMode(5889);
                GlStateManager.pushMatrix();
                GlStateManager.loadIdentity();
                GlStateManager.ortho(0.0, this.field_152956_h.framebufferWidth, this.field_152956_h.framebufferHeight, 0.0, 1000.0, 3000.0);
                GlStateManager.matrixMode(5888);
                GlStateManager.pushMatrix();
                GlStateManager.loadIdentity();
                GlStateManager.translate(0.0f, 0.0f, -2000.0f);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.viewport(0, 0, this.field_152956_h.framebufferWidth, this.field_152956_h.framebufferHeight);
                GlStateManager.enableTextures();
                GlStateManager.disableAlpha();
                GlStateManager.disableBlend();
                final float var7 = this.field_152956_h.framebufferWidth;
                final float var8 = this.field_152956_h.framebufferHeight;
                final float var9 = var6.framebufferWidth / var6.framebufferTextureWidth;
                final float var10 = var6.framebufferHeight / var6.framebufferTextureHeight;
                var6.bindFramebufferTexture();
                GL11.glTexParameterf(3553, 10241, 9729.0f);
                GL11.glTexParameterf(3553, 10240, 9729.0f);
                final Tessellator var11 = Tessellator.getInstance();
                final WorldRenderer var12 = var11.getWorldRenderer();
                var12.startDrawingQuads();
                var12.addVertexWithUV(0.0, var8, 0.0, 0.0, var10);
                var12.addVertexWithUV(var7, var8, 0.0, var9, var10);
                var12.addVertexWithUV(var7, 0.0, 0.0, var9, 0.0);
                var12.addVertexWithUV(0.0, 0.0, 0.0, 0.0, 0.0);
                var11.draw();
                var6.unbindFramebufferTexture();
                GlStateManager.popMatrix();
                GlStateManager.matrixMode(5889);
                GlStateManager.popMatrix();
                GlStateManager.matrixMode(5888);
                this.broadcastController.func_152846_a(var5);
                this.field_152956_h.unbindFramebuffer();
                this.broadcastController.func_152859_b(var5);
                this.field_152959_k = var1;
            }
        }
    }
    
    @Override
    public boolean func_152936_l() {
        return this.broadcastController.func_152849_q();
    }
    
    @Override
    public boolean func_152924_m() {
        return this.broadcastController.func_152857_n();
    }
    
    @Override
    public boolean func_152934_n() {
        return this.broadcastController.isBroadcasting();
    }
    
    @Override
    public void func_152911_a(final Metadata p_152911_1_, final long p_152911_2_) {
        if (this.func_152934_n() && this.field_152957_i) {
            final long var4 = this.broadcastController.func_152844_x();
            if (!this.broadcastController.func_152840_a(p_152911_1_.func_152810_c(), var4 + p_152911_2_, p_152911_1_.func_152809_a(), p_152911_1_.func_152806_b())) {
                TwitchStream.field_152950_b.warn(TwitchStream.field_152949_a, "Couldn't send stream metadata action at {}: {}", new Object[] { var4 + p_152911_2_, p_152911_1_ });
            }
            else {
                TwitchStream.field_152950_b.debug(TwitchStream.field_152949_a, "Sent stream metadata action at {}: {}", new Object[] { var4 + p_152911_2_, p_152911_1_ });
            }
        }
    }
    
    @Override
    public void func_176026_a(final Metadata p_176026_1_, final long p_176026_2_, final long p_176026_4_) {
        if (this.func_152934_n() && this.field_152957_i) {
            final long var6 = this.broadcastController.func_152844_x();
            final String var7 = p_176026_1_.func_152809_a();
            final String var8 = p_176026_1_.func_152806_b();
            final long var9 = this.broadcastController.func_177946_b(p_176026_1_.func_152810_c(), var6 + p_176026_2_, var7, var8);
            if (var9 < 0L) {
                TwitchStream.field_152950_b.warn(TwitchStream.field_152949_a, "Could not send stream metadata sequence from {} to {}: {}", new Object[] { var6 + p_176026_2_, var6 + p_176026_4_, p_176026_1_ });
            }
            else if (this.broadcastController.func_177947_a(p_176026_1_.func_152810_c(), var6 + p_176026_4_, var9, var7, var8)) {
                TwitchStream.field_152950_b.debug(TwitchStream.field_152949_a, "Sent stream metadata sequence from {} to {}: {}", new Object[] { var6 + p_176026_2_, var6 + p_176026_4_, p_176026_1_ });
            }
            else {
                TwitchStream.field_152950_b.warn(TwitchStream.field_152949_a, "Half-sent stream metadata sequence from {} to {}: {}", new Object[] { var6 + p_176026_2_, var6 + p_176026_4_, p_176026_1_ });
            }
        }
    }
    
    @Override
    public boolean isPaused() {
        return this.broadcastController.isBroadcastPaused();
    }
    
    @Override
    public void func_152931_p() {
        if (this.broadcastController.func_152830_D()) {
            TwitchStream.field_152950_b.debug(TwitchStream.field_152949_a, "Requested commercial from Twitch");
        }
        else {
            TwitchStream.field_152950_b.warn(TwitchStream.field_152949_a, "Could not request commercial from Twitch");
        }
    }
    
    @Override
    public void func_152916_q() {
        this.broadcastController.func_152847_F();
        this.field_152962_n = true;
        this.func_152915_s();
    }
    
    @Override
    public void func_152933_r() {
        this.broadcastController.func_152854_G();
        this.field_152962_n = false;
        this.func_152915_s();
    }
    
    @Override
    public void func_152915_s() {
        if (this.func_152934_n()) {
            final float var1 = this.field_152953_e.gameSettings.streamGameVolume;
            final boolean var2 = this.field_152962_n || var1 <= 0.0f;
            this.broadcastController.func_152837_b(var2 ? 0.0f : var1);
            this.broadcastController.func_152829_a(this.func_152929_G() ? 0.0f : this.field_152953_e.gameSettings.streamMicVolume);
        }
    }
    
    @Override
    public void func_152930_t() {
        final GameSettings var1 = this.field_152953_e.gameSettings;
        final VideoParams var2 = this.broadcastController.func_152834_a(func_152946_b(var1.streamKbps), func_152948_a(var1.streamFps), func_152947_c(var1.streamBytesPerPixel), this.field_152953_e.displayWidth / this.field_152953_e.displayHeight);
        switch (var1.streamCompression) {
            case 0: {
                var2.encodingCpuUsage = EncodingCpuUsage.TTV_ECU_LOW;
                break;
            }
            case 1: {
                var2.encodingCpuUsage = EncodingCpuUsage.TTV_ECU_MEDIUM;
                break;
            }
            case 2: {
                var2.encodingCpuUsage = EncodingCpuUsage.TTV_ECU_HIGH;
                break;
            }
        }
        if (this.field_152956_h == null) {
            this.field_152956_h = new Framebuffer(var2.outputWidth, var2.outputHeight, false);
        }
        else {
            this.field_152956_h.createBindFramebuffer(var2.outputWidth, var2.outputHeight);
        }
        if (var1.streamPreferredServer != null && var1.streamPreferredServer.length() > 0) {
            for (final IngestServer var6 : this.func_152925_v()) {
                if (var6.serverUrl.equals(var1.streamPreferredServer)) {
                    this.broadcastController.func_152824_a(var6);
                    break;
                }
            }
        }
        this.field_152958_j = var2.targetFps;
        this.field_152957_i = var1.streamSendMetadata;
        this.broadcastController.func_152836_a(var2);
        TwitchStream.field_152950_b.info(TwitchStream.field_152949_a, "Streaming at {}/{} at {} kbps to {}", new Object[] { var2.outputWidth, var2.outputHeight, var2.maxKbps, this.broadcastController.func_152833_s().serverUrl });
        this.broadcastController.func_152828_a(null, "Minecraft", null);
    }
    
    @Override
    public void func_152914_u() {
        if (this.broadcastController.func_152819_E()) {
            TwitchStream.field_152950_b.info(TwitchStream.field_152949_a, "Stopped streaming to Twitch");
        }
        else {
            TwitchStream.field_152950_b.warn(TwitchStream.field_152949_a, "Could not stop streaming to Twitch");
        }
    }
    
    @Override
    public void func_152900_a(final ErrorCode p_152900_1_, final AuthToken p_152900_2_) {
    }
    
    @Override
    public void func_152897_a(final ErrorCode p_152897_1_) {
        if (ErrorCode.succeeded(p_152897_1_)) {
            TwitchStream.field_152950_b.debug(TwitchStream.field_152949_a, "Login attempt successful");
            this.field_152961_m = true;
        }
        else {
            TwitchStream.field_152950_b.warn(TwitchStream.field_152949_a, "Login attempt unsuccessful: {} (error code {})", new Object[] { ErrorCode.getString(p_152897_1_), p_152897_1_.getValue() });
            this.field_152961_m = false;
        }
    }
    
    @Override
    public void func_152898_a(final ErrorCode p_152898_1_, final GameInfo[] p_152898_2_) {
    }
    
    @Override
    public void func_152891_a(final BroadcastController.BroadcastState p_152891_1_) {
        TwitchStream.field_152950_b.debug(TwitchStream.field_152949_a, "Broadcast state changed to {}", new Object[] { p_152891_1_ });
        if (p_152891_1_ == BroadcastController.BroadcastState.Initialized) {
            this.broadcastController.func_152827_a(BroadcastController.BroadcastState.Authenticated);
        }
    }
    
    @Override
    public void func_152895_a() {
        TwitchStream.field_152950_b.info(TwitchStream.field_152949_a, "Logged out of twitch");
    }
    
    @Override
    public void func_152894_a(final StreamInfo p_152894_1_) {
        TwitchStream.field_152950_b.debug(TwitchStream.field_152949_a, "Stream info updated; {} viewers on stream ID {}", new Object[] { p_152894_1_.viewers, p_152894_1_.streamId });
    }
    
    @Override
    public void func_152896_a(final IngestList p_152896_1_) {
    }
    
    @Override
    public void func_152893_b(final ErrorCode p_152893_1_) {
        TwitchStream.field_152950_b.warn(TwitchStream.field_152949_a, "Issue submitting frame: {} (Error code {})", new Object[] { ErrorCode.getString(p_152893_1_), p_152893_1_.getValue() });
        this.field_152953_e.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new ChatComponentText("Issue streaming frame: " + p_152893_1_ + " (" + ErrorCode.getString(p_152893_1_) + ")"), 2);
    }
    
    @Override
    public void func_152899_b() {
        this.func_152915_s();
        TwitchStream.field_152950_b.info(TwitchStream.field_152949_a, "Broadcast to Twitch has started");
    }
    
    @Override
    public void func_152901_c() {
        TwitchStream.field_152950_b.info(TwitchStream.field_152949_a, "Broadcast to Twitch has stopped");
    }
    
    @Override
    public void func_152892_c(final ErrorCode p_152892_1_) {
        if (p_152892_1_ == ErrorCode.TTV_EC_SOUNDFLOWER_NOT_INSTALLED) {
            final ChatComponentTranslation var2 = new ChatComponentTranslation("stream.unavailable.soundflower.chat.link", new Object[0]);
            var2.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://help.mojang.com/customer/portal/articles/1374877-configuring-soundflower-for-streaming-on-apple-computers"));
            var2.getChatStyle().setUnderlined(true);
            final ChatComponentTranslation var3 = new ChatComponentTranslation("stream.unavailable.soundflower.chat", new Object[] { var2 });
            var3.getChatStyle().setColor(EnumChatFormatting.DARK_RED);
            this.field_152953_e.ingameGUI.getChatGUI().printChatMessage(var3);
        }
        else {
            final ChatComponentTranslation var2 = new ChatComponentTranslation("stream.unavailable.unknown.chat", new Object[] { ErrorCode.getString(p_152892_1_) });
            var2.getChatStyle().setColor(EnumChatFormatting.DARK_RED);
            this.field_152953_e.ingameGUI.getChatGUI().printChatMessage(var2);
        }
    }
    
    @Override
    public void func_152907_a(final IngestServerTester p_152907_1_, final IngestServerTester.IngestTestState p_152907_2_) {
        TwitchStream.field_152950_b.debug(TwitchStream.field_152949_a, "Ingest test state changed to {}", new Object[] { p_152907_2_ });
        if (p_152907_2_ == IngestServerTester.IngestTestState.Finished) {
            this.field_152960_l = true;
        }
    }
    
    public static int func_152948_a(final float p_152948_0_) {
        return MathHelper.floor_float(10.0f + p_152948_0_ * 50.0f);
    }
    
    public static int func_152946_b(final float p_152946_0_) {
        return MathHelper.floor_float(230.0f + p_152946_0_ * 3270.0f);
    }
    
    public static float func_152947_c(final float p_152947_0_) {
        return 0.1f + p_152947_0_ * 0.1f;
    }
    
    @Override
    public IngestServer[] func_152925_v() {
        return this.broadcastController.func_152855_t().getServers();
    }
    
    @Override
    public void func_152909_x() {
        final IngestServerTester var1 = this.broadcastController.func_152838_J();
        if (var1 != null) {
            var1.func_153042_a(this);
        }
    }
    
    @Override
    public IngestServerTester func_152932_y() {
        return this.broadcastController.isReady();
    }
    
    @Override
    public boolean func_152908_z() {
        return this.broadcastController.isIngestTesting();
    }
    
    @Override
    public int func_152920_A() {
        return this.func_152934_n() ? this.broadcastController.func_152816_j().viewers : 0;
    }
    
    @Override
    public void func_176023_d(final ErrorCode p_176023_1_) {
        if (ErrorCode.failed(p_176023_1_)) {
            TwitchStream.field_152950_b.error(TwitchStream.field_152949_a, "Chat failed to initialize");
        }
    }
    
    @Override
    public void func_176022_e(final ErrorCode p_176022_1_) {
        if (ErrorCode.failed(p_176022_1_)) {
            TwitchStream.field_152950_b.error(TwitchStream.field_152949_a, "Chat failed to shutdown");
        }
    }
    
    @Override
    public void func_176017_a(final ChatController.ChatState p_176017_1_) {
    }
    
    @Override
    public void func_180605_a(final String p_180605_1_, final ChatRawMessage[] p_180605_2_) {
        for (final ChatRawMessage var6 : p_180605_2_) {
            this.func_176027_a(var6.userName, var6);
            if (this.func_176028_a(var6.modes, var6.subscriptions, this.field_152953_e.gameSettings.streamChatUserFilter)) {
                final ChatComponentText var7 = new ChatComponentText(var6.userName);
                final ChatComponentTranslation var8 = new ChatComponentTranslation("chat.stream." + (var6.action ? "emote" : "text"), new Object[] { this.field_152954_f, var7, EnumChatFormatting.getTextWithoutFormattingCodes(var6.message) });
                if (var6.action) {
                    var8.getChatStyle().setItalic(true);
                }
                final ChatComponentText var9 = new ChatComponentText("");
                var9.appendSibling(new ChatComponentTranslation("stream.userinfo.chatTooltip", new Object[0]));
                for (final IChatComponent var11 : GuiTwitchUserMode.func_152328_a(var6.modes, var6.subscriptions, null)) {
                    var9.appendText("\n");
                    var9.appendSibling(var11);
                }
                var7.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, var9));
                var7.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.TWITCH_USER_INFO, var6.userName));
                this.field_152953_e.ingameGUI.getChatGUI().printChatMessage(var8);
            }
        }
    }
    
    @Override
    public void func_176025_a(final String p_176025_1_, final ChatTokenizedMessage[] p_176025_2_) {
    }
    
    private void func_176027_a(final String p_176027_1_, final ChatRawMessage p_176027_2_) {
        ChatUserInfo var3 = this.field_152955_g.get(p_176027_1_);
        if (var3 == null) {
            var3 = new ChatUserInfo();
            var3.displayName = p_176027_1_;
            this.field_152955_g.put(p_176027_1_, var3);
        }
        var3.subscriptions = p_176027_2_.subscriptions;
        var3.modes = p_176027_2_.modes;
        var3.nameColorARGB = p_176027_2_.nameColorARGB;
    }
    
    private boolean func_176028_a(final Set p_176028_1_, final Set p_176028_2_, final int p_176028_3_) {
        return !p_176028_1_.contains(ChatUserMode.TTV_CHAT_USERMODE_BANNED) && (p_176028_1_.contains(ChatUserMode.TTV_CHAT_USERMODE_ADMINSTRATOR) || p_176028_1_.contains(ChatUserMode.TTV_CHAT_USERMODE_MODERATOR) || p_176028_1_.contains(ChatUserMode.TTV_CHAT_USERMODE_STAFF) || p_176028_3_ == 0 || (p_176028_3_ == 1 && p_176028_2_.contains(ChatUserSubscription.TTV_CHAT_USERSUB_SUBSCRIBER)));
    }
    
    @Override
    public void func_176018_a(final String p_176018_1_, final ChatUserInfo[] p_176018_2_, final ChatUserInfo[] p_176018_3_, final ChatUserInfo[] p_176018_4_) {
        for (final ChatUserInfo var8 : p_176018_3_) {
            this.field_152955_g.remove(var8.displayName);
        }
        for (final ChatUserInfo var8 : p_176018_4_) {
            this.field_152955_g.put(var8.displayName, var8);
        }
        for (final ChatUserInfo var8 : p_176018_2_) {
            this.field_152955_g.put(var8.displayName, var8);
        }
    }
    
    @Override
    public void func_180606_a(final String p_180606_1_) {
        TwitchStream.field_152950_b.debug(TwitchStream.field_152949_a, "Chat connected");
    }
    
    @Override
    public void func_180607_b(final String p_180607_1_) {
        TwitchStream.field_152950_b.debug(TwitchStream.field_152949_a, "Chat disconnected");
        this.field_152955_g.clear();
    }
    
    @Override
    public void func_176019_a(final String p_176019_1_, final String p_176019_2_) {
    }
    
    @Override
    public void func_176021_d() {
    }
    
    @Override
    public void func_176024_e() {
    }
    
    @Override
    public void func_176016_c(final String p_176016_1_) {
    }
    
    @Override
    public void func_176020_d(final String p_176020_1_) {
    }
    
    @Override
    public boolean func_152927_B() {
        return this.field_176029_e != null && this.field_176029_e.equals(this.broadcastController.func_152843_l().name);
    }
    
    @Override
    public String func_152921_C() {
        return this.field_176029_e;
    }
    
    @Override
    public ChatUserInfo func_152926_a(final String p_152926_1_) {
        return this.field_152955_g.get(p_152926_1_);
    }
    
    @Override
    public void func_152917_b(final String p_152917_1_) {
        this.field_152952_d.func_175986_a(this.field_176029_e, p_152917_1_);
    }
    
    @Override
    public boolean func_152928_D() {
        return TwitchStream.field_152965_q && this.broadcastController.func_152858_b();
    }
    
    @Override
    public ErrorCode func_152912_E() {
        return TwitchStream.field_152965_q ? this.broadcastController.func_152852_P() : ErrorCode.TTV_EC_OS_TOO_OLD;
    }
    
    @Override
    public boolean func_152913_F() {
        return this.field_152961_m;
    }
    
    @Override
    public void func_152910_a(final boolean p_152910_1_) {
        this.field_152963_o = p_152910_1_;
        this.func_152915_s();
    }
    
    @Override
    public boolean func_152929_G() {
        final boolean var1 = this.field_152953_e.gameSettings.streamMicToggleBehavior == 1;
        return this.field_152962_n || this.field_152953_e.gameSettings.streamMicVolume <= 0.0f || var1 != this.field_152963_o;
    }
    
    @Override
    public AuthFailureReason func_152918_H() {
        return this.field_152964_p;
    }
    
    static {
        field_152950_b = LogManager.getLogger();
        field_152949_a = MarkerManager.getMarker("STREAM");
        try {
            if (Util.getOSType() == Util.EnumOS.WINDOWS) {
                System.loadLibrary("avutil-ttv-51");
                System.loadLibrary("swresample-ttv-0");
                System.loadLibrary("libmp3lame-ttv");
                if (System.getProperty("os.arch").contains("64")) {
                    System.loadLibrary("libmfxsw64");
                }
                else {
                    System.loadLibrary("libmfxsw32");
                }
            }
            TwitchStream.field_152965_q = true;
        }
        catch (Throwable var1) {
            TwitchStream.field_152965_q = false;
        }
    }
}
