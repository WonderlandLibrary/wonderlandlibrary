/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Strings
 *  com.google.common.collect.Maps
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  com.mojang.authlib.properties.Property
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.apache.logging.log4j.Marker
 *  org.apache.logging.log4j.MarkerManager
 *  org.lwjgl.opengl.GL11
 *  tv.twitch.AuthToken
 *  tv.twitch.ErrorCode
 *  tv.twitch.broadcast.EncodingCpuUsage
 *  tv.twitch.broadcast.FrameBuffer
 *  tv.twitch.broadcast.GameInfo
 *  tv.twitch.broadcast.IngestList
 *  tv.twitch.broadcast.IngestServer
 *  tv.twitch.broadcast.StreamInfo
 *  tv.twitch.broadcast.VideoParams
 *  tv.twitch.chat.ChatRawMessage
 *  tv.twitch.chat.ChatTokenizedMessage
 *  tv.twitch.chat.ChatUserInfo
 *  tv.twitch.chat.ChatUserMode
 *  tv.twitch.chat.ChatUserSubscription
 */
package net.minecraft.client.stream;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.properties.Property;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.stream.GuiTwitchUserMode;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.stream.BroadcastController;
import net.minecraft.client.stream.ChatController;
import net.minecraft.client.stream.IStream;
import net.minecraft.client.stream.IngestServerTester;
import net.minecraft.client.stream.Metadata;
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
import tv.twitch.chat.ChatUserMode;
import tv.twitch.chat.ChatUserSubscription;

public class TwitchStream
implements BroadcastController.BroadcastListener,
IngestServerTester.IngestTestListener,
IStream,
ChatController.ChatListener {
    private boolean field_152962_n;
    private final IChatComponent twitchComponent = new ChatComponentText("Twitch");
    private static final Logger LOGGER = LogManager.getLogger();
    private IStream.AuthFailureReason authFailureReason;
    private boolean field_152960_l = false;
    private final Minecraft mc;
    private static boolean field_152965_q;
    public static final Marker STREAM_MARKER;
    private Framebuffer framebuffer;
    private boolean field_152957_i;
    private boolean field_152963_o;
    private final Map<String, ChatUserInfo> field_152955_g = Maps.newHashMap();
    private boolean loggedIn;
    private final BroadcastController broadcastController;
    private final ChatController chatController;
    private long field_152959_k = 0L;
    private int targetFPS = 30;
    private String field_176029_e;

    @Override
    public IngestServer[] func_152925_v() {
        return this.broadcastController.func_152855_t().getServers();
    }

    @Override
    public void func_152907_a(IngestServerTester ingestServerTester, IngestServerTester.IngestTestState ingestTestState) {
        LOGGER.debug(STREAM_MARKER, "Ingest test state changed to {}", new Object[]{ingestTestState});
        if (ingestTestState == IngestServerTester.IngestTestState.Finished) {
            this.field_152960_l = true;
        }
    }

    @Override
    public ChatUserInfo func_152926_a(String string) {
        return this.field_152955_g.get(string);
    }

    @Override
    public void func_152894_a(StreamInfo streamInfo) {
        LOGGER.debug(STREAM_MARKER, "Stream info updated; {} viewers on stream ID {}", new Object[]{streamInfo.viewers, streamInfo.streamId});
    }

    @Override
    public void func_176020_d(String string) {
    }

    @Override
    public void func_176024_e() {
    }

    public static float formatStreamBps(float f) {
        return 0.1f + f * 0.1f;
    }

    @Override
    public void func_152930_t() {
        GameSettings gameSettings = Minecraft.gameSettings;
        VideoParams videoParams = this.broadcastController.func_152834_a(TwitchStream.formatStreamKbps(gameSettings.streamKbps), TwitchStream.formatStreamFps(gameSettings.streamFps), TwitchStream.formatStreamBps(gameSettings.streamBytesPerPixel), (float)this.mc.displayWidth / (float)Minecraft.displayHeight);
        switch (gameSettings.streamCompression) {
            case 0: {
                videoParams.encodingCpuUsage = EncodingCpuUsage.TTV_ECU_LOW;
                break;
            }
            case 1: {
                videoParams.encodingCpuUsage = EncodingCpuUsage.TTV_ECU_MEDIUM;
                break;
            }
            case 2: {
                videoParams.encodingCpuUsage = EncodingCpuUsage.TTV_ECU_HIGH;
            }
        }
        if (this.framebuffer == null) {
            this.framebuffer = new Framebuffer(videoParams.outputWidth, videoParams.outputHeight, false);
        } else {
            this.framebuffer.createBindFramebuffer(videoParams.outputWidth, videoParams.outputHeight);
        }
        if (gameSettings.streamPreferredServer != null && gameSettings.streamPreferredServer.length() > 0) {
            IngestServer[] ingestServerArray = this.func_152925_v();
            int n = ingestServerArray.length;
            int n2 = 0;
            while (n2 < n) {
                IngestServer ingestServer = ingestServerArray[n2];
                if (ingestServer.serverUrl.equals(gameSettings.streamPreferredServer)) {
                    this.broadcastController.func_152824_a(ingestServer);
                    break;
                }
                ++n2;
            }
        }
        this.targetFPS = videoParams.targetFps;
        this.field_152957_i = gameSettings.streamSendMetadata;
        this.broadcastController.func_152836_a(videoParams);
        LOGGER.info(STREAM_MARKER, "Streaming at {}/{} at {} kbps to {}", new Object[]{videoParams.outputWidth, videoParams.outputHeight, videoParams.maxKbps, this.broadcastController.func_152833_s().serverUrl});
        this.broadcastController.func_152828_a(null, "Minecraft", null);
    }

    @Override
    public String func_152921_C() {
        return this.field_176029_e;
    }

    @Override
    public void func_152911_a(Metadata metadata, long l) {
        if (this.isBroadcasting() && this.field_152957_i) {
            long l2 = this.broadcastController.func_152844_x();
            if (!this.broadcastController.func_152840_a(metadata.func_152810_c(), l2 + l, metadata.func_152809_a(), metadata.func_152806_b())) {
                LOGGER.warn(STREAM_MARKER, "Couldn't send stream metadata action at {}: {}", new Object[]{l2 + l, metadata});
            } else {
                LOGGER.debug(STREAM_MARKER, "Sent stream metadata action at {}: {}", new Object[]{l2 + l, metadata});
            }
        }
    }

    @Override
    public void requestCommercial() {
        if (this.broadcastController.requestCommercial()) {
            LOGGER.debug(STREAM_MARKER, "Requested commercial from Twitch");
        } else {
            LOGGER.warn(STREAM_MARKER, "Could not request commercial from Twitch");
        }
    }

    static {
        STREAM_MARKER = MarkerManager.getMarker((String)"STREAM");
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
        }
        catch (Throwable throwable) {
            field_152965_q = false;
        }
    }

    @Override
    public void func_152893_b(ErrorCode errorCode) {
        LOGGER.warn(STREAM_MARKER, "Issue submitting frame: {} (Error code {})", new Object[]{ErrorCode.getString((ErrorCode)errorCode), errorCode.getValue()});
        this.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new ChatComponentText("Issue streaming frame: " + errorCode + " (" + ErrorCode.getString((ErrorCode)errorCode) + ")"), 2);
    }

    @Override
    public void pause() {
        this.broadcastController.func_152847_F();
        this.field_152962_n = true;
        this.updateStreamVolume();
    }

    @Override
    public boolean func_152927_B() {
        return this.field_176029_e != null && this.field_176029_e.equals(this.broadcastController.getChannelInfo().name);
    }

    @Override
    public void func_176023_d(ErrorCode errorCode) {
        if (ErrorCode.failed((ErrorCode)errorCode)) {
            LOGGER.error(STREAM_MARKER, "Chat failed to initialize");
        }
    }

    @Override
    public void func_152897_a(ErrorCode errorCode) {
        if (ErrorCode.succeeded((ErrorCode)errorCode)) {
            LOGGER.debug(STREAM_MARKER, "Login attempt successful");
            this.loggedIn = true;
        } else {
            LOGGER.warn(STREAM_MARKER, "Login attempt unsuccessful: {} (error code {})", new Object[]{ErrorCode.getString((ErrorCode)errorCode), errorCode.getValue()});
            this.loggedIn = false;
        }
    }

    @Override
    public void unpause() {
        this.broadcastController.func_152854_G();
        this.field_152962_n = false;
        this.updateStreamVolume();
    }

    @Override
    public void func_176022_e(ErrorCode errorCode) {
        if (ErrorCode.failed((ErrorCode)errorCode)) {
            LOGGER.error(STREAM_MARKER, "Chat failed to shutdown");
        }
    }

    @Override
    public void muteMicrophone(boolean bl) {
        this.field_152963_o = bl;
        this.updateStreamVolume();
    }

    @Override
    public boolean func_152908_z() {
        return this.broadcastController.isIngestTesting();
    }

    @Override
    public IngestServerTester func_152932_y() {
        return this.broadcastController.isReady();
    }

    @Override
    public void func_152895_a() {
        LOGGER.info(STREAM_MARKER, "Logged out of twitch");
    }

    @Override
    public void func_176025_a(String string, ChatTokenizedMessage[] chatTokenizedMessageArray) {
    }

    @Override
    public void shutdownStream() {
        LOGGER.debug(STREAM_MARKER, "Shutdown streaming");
        this.broadcastController.statCallback();
        this.chatController.func_175988_p();
    }

    @Override
    public void func_180607_b(String string) {
        LOGGER.debug(STREAM_MARKER, "Chat disconnected");
        this.field_152955_g.clear();
    }

    @Override
    public void stopBroadcasting() {
        if (this.broadcastController.stopBroadcasting()) {
            LOGGER.info(STREAM_MARKER, "Stopped streaming to Twitch");
        } else {
            LOGGER.warn(STREAM_MARKER, "Could not stop streaming to Twitch");
        }
    }

    @Override
    public boolean isReadyToBroadcast() {
        return this.broadcastController.isReadyToBroadcast();
    }

    @Override
    public void func_152901_c() {
        LOGGER.info(STREAM_MARKER, "Broadcast to Twitch has stopped");
    }

    @Override
    public void func_152898_a(ErrorCode errorCode, GameInfo[] gameInfoArray) {
    }

    public TwitchStream(Minecraft minecraft, final Property property) {
        this.authFailureReason = IStream.AuthFailureReason.ERROR;
        this.mc = minecraft;
        this.broadcastController = new BroadcastController();
        this.chatController = new ChatController();
        this.broadcastController.func_152841_a(this);
        this.chatController.func_152990_a(this);
        this.broadcastController.func_152842_a("nmt37qblda36pvonovdkbopzfzw3wlq");
        this.chatController.func_152984_a("nmt37qblda36pvonovdkbopzfzw3wlq");
        this.twitchComponent.getChatStyle().setColor(EnumChatFormatting.DARK_PURPLE);
        if (property != null && !Strings.isNullOrEmpty((String)property.getValue()) && OpenGlHelper.framebufferSupported) {
            Thread thread = new Thread("Twitch authenticator"){

                @Override
                public void run() {
                    try {
                        URL uRL = new URL("https://api.twitch.tv/kraken?oauth_token=" + URLEncoder.encode(property.getValue(), "UTF-8"));
                        String string = HttpUtil.get(uRL);
                        JsonObject jsonObject = JsonUtils.getJsonObject(new JsonParser().parse(string), "Response");
                        JsonObject jsonObject2 = JsonUtils.getJsonObject(jsonObject, "token");
                        if (JsonUtils.getBoolean(jsonObject2, "valid")) {
                            String string2 = JsonUtils.getString(jsonObject2, "user_name");
                            LOGGER.debug(STREAM_MARKER, "Authenticated with twitch; username is {}", new Object[]{string2});
                            AuthToken authToken = new AuthToken();
                            authToken.data = property.getValue();
                            TwitchStream.this.broadcastController.func_152818_a(string2, authToken);
                            TwitchStream.this.chatController.func_152998_c(string2);
                            TwitchStream.this.chatController.func_152994_a(authToken);
                            Runtime.getRuntime().addShutdownHook(new Thread("Twitch shutdown hook"){

                                @Override
                                public void run() {
                                    TwitchStream.this.shutdownStream();
                                }
                            });
                            TwitchStream.this.broadcastController.func_152817_A();
                            TwitchStream.this.chatController.func_175984_n();
                        } else {
                            TwitchStream.this.authFailureReason = IStream.AuthFailureReason.INVALID_TOKEN;
                            LOGGER.error(STREAM_MARKER, "Given twitch access token is invalid");
                        }
                    }
                    catch (IOException iOException) {
                        TwitchStream.this.authFailureReason = IStream.AuthFailureReason.ERROR;
                        LOGGER.error(STREAM_MARKER, "Could not authenticate with twitch", (Throwable)iOException);
                    }
                }
            };
            thread.setDaemon(true);
            thread.start();
        }
    }

    private boolean func_176028_a(Set<ChatUserMode> set, Set<ChatUserSubscription> set2, int n) {
        return set.contains(ChatUserMode.TTV_CHAT_USERMODE_BANNED) ? false : (set.contains(ChatUserMode.TTV_CHAT_USERMODE_ADMINSTRATOR) ? true : (set.contains(ChatUserMode.TTV_CHAT_USERMODE_MODERATOR) ? true : (set.contains(ChatUserMode.TTV_CHAT_USERMODE_STAFF) ? true : (n == 0 ? true : (n == 1 ? set2.contains(ChatUserSubscription.TTV_CHAT_USERSUB_SUBSCRIBER) : false)))));
    }

    @Override
    public int func_152920_A() {
        return this.isBroadcasting() ? this.broadcastController.getStreamInfo().viewers : 0;
    }

    protected void func_152942_I() {
        String string;
        ChatController.ChatState chatState = this.chatController.func_153000_j();
        this.field_176029_e = string = this.broadcastController.getChannelInfo().name;
        if (chatState != ChatController.ChatState.Initialized) {
            LOGGER.warn("Invalid twitch chat state {}", new Object[]{chatState});
        } else if (this.chatController.func_175989_e(this.field_176029_e) == ChatController.EnumChannelState.Disconnected) {
            this.chatController.func_152986_d(string);
        } else {
            LOGGER.warn("Invalid twitch chat state {}", new Object[]{chatState});
        }
    }

    @Override
    public void func_152899_b() {
        this.updateStreamVolume();
        LOGGER.info(STREAM_MARKER, "Broadcast to Twitch has started");
    }

    @Override
    public void func_176019_a(String string, String string2) {
    }

    @Override
    public void func_152917_b(String string) {
        this.chatController.func_175986_a(this.field_176029_e, string);
    }

    @Override
    public void func_152891_a(BroadcastController.BroadcastState broadcastState) {
        LOGGER.debug(STREAM_MARKER, "Broadcast state changed to {}", new Object[]{broadcastState});
        if (broadcastState == BroadcastController.BroadcastState.Initialized) {
            this.broadcastController.func_152827_a(BroadcastController.BroadcastState.Authenticated);
        }
    }

    @Override
    public boolean func_152913_F() {
        return this.loggedIn;
    }

    @Override
    public boolean func_152928_D() {
        return field_152965_q && this.broadcastController.func_152858_b();
    }

    @Override
    public void func_176016_c(String string) {
    }

    @Override
    public void func_152896_a(IngestList ingestList) {
    }

    @Override
    public void func_176017_a(ChatController.ChatState chatState) {
    }

    @Override
    public void func_180605_a(String string, ChatRawMessage[] chatRawMessageArray) {
        ChatRawMessage[] chatRawMessageArray2 = chatRawMessageArray;
        int n = chatRawMessageArray.length;
        int n2 = 0;
        while (n2 < n) {
            ChatRawMessage chatRawMessage = chatRawMessageArray2[n2];
            this.func_176027_a(chatRawMessage.userName, chatRawMessage);
            if (this.func_176028_a(chatRawMessage.modes, chatRawMessage.subscriptions, Minecraft.gameSettings.streamChatUserFilter)) {
                ChatComponentText chatComponentText = new ChatComponentText(chatRawMessage.userName);
                ChatComponentTranslation chatComponentTranslation = new ChatComponentTranslation("chat.stream." + (chatRawMessage.action ? "emote" : "text"), this.twitchComponent, chatComponentText, EnumChatFormatting.getTextWithoutFormattingCodes(chatRawMessage.message));
                if (chatRawMessage.action) {
                    chatComponentTranslation.getChatStyle().setItalic(true);
                }
                ChatComponentText chatComponentText2 = new ChatComponentText("");
                chatComponentText2.appendSibling(new ChatComponentTranslation("stream.userinfo.chatTooltip", new Object[0]));
                for (IChatComponent iChatComponent : GuiTwitchUserMode.func_152328_a(chatRawMessage.modes, chatRawMessage.subscriptions, null)) {
                    chatComponentText2.appendText("\n");
                    chatComponentText2.appendSibling(iChatComponent);
                }
                chatComponentText.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, chatComponentText2));
                chatComponentText.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.TWITCH_USER_INFO, chatRawMessage.userName));
                this.mc.ingameGUI.getChatGUI().printChatMessage(chatComponentTranslation);
            }
            ++n2;
        }
    }

    public static int formatStreamFps(float f) {
        return MathHelper.floor_float(10.0f + f * 50.0f);
    }

    @Override
    public void func_176021_d() {
    }

    @Override
    public void func_152900_a(ErrorCode errorCode, AuthToken authToken) {
    }

    @Override
    public void func_176026_a(Metadata metadata, long l, long l2) {
        if (this.isBroadcasting() && this.field_152957_i) {
            long l3 = this.broadcastController.func_152844_x();
            String string = metadata.func_152809_a();
            String string2 = metadata.func_152806_b();
            long l4 = this.broadcastController.func_177946_b(metadata.func_152810_c(), l3 + l, string, string2);
            if (l4 < 0L) {
                LOGGER.warn(STREAM_MARKER, "Could not send stream metadata sequence from {} to {}: {}", new Object[]{l3 + l, l3 + l2, metadata});
            } else if (this.broadcastController.func_177947_a(metadata.func_152810_c(), l3 + l2, l4, string, string2)) {
                LOGGER.debug(STREAM_MARKER, "Sent stream metadata sequence from {} to {}: {}", new Object[]{l3 + l, l3 + l2, metadata});
            } else {
                LOGGER.warn(STREAM_MARKER, "Half-sent stream metadata sequence from {} to {}: {}", new Object[]{l3 + l, l3 + l2, metadata});
            }
        }
    }

    @Override
    public void func_152909_x() {
        IngestServerTester ingestServerTester = this.broadcastController.func_152838_J();
        if (ingestServerTester != null) {
            ingestServerTester.func_153042_a(this);
        }
    }

    @Override
    public boolean isBroadcasting() {
        return this.broadcastController.isBroadcasting();
    }

    @Override
    public void func_180606_a(String string) {
        LOGGER.debug(STREAM_MARKER, "Chat connected");
    }

    @Override
    public boolean isPaused() {
        return this.broadcastController.isBroadcastPaused();
    }

    @Override
    public boolean func_152929_G() {
        boolean bl;
        boolean bl2 = bl = Minecraft.gameSettings.streamMicToggleBehavior == 1;
        return this.field_152962_n || Minecraft.gameSettings.streamMicVolume <= 0.0f || bl != this.field_152963_o;
    }

    @Override
    public ErrorCode func_152912_E() {
        return !field_152965_q ? ErrorCode.TTV_EC_OS_TOO_OLD : this.broadcastController.getErrorCode();
    }

    @Override
    public void func_176018_a(String string, ChatUserInfo[] chatUserInfoArray, ChatUserInfo[] chatUserInfoArray2, ChatUserInfo[] chatUserInfoArray3) {
        ChatUserInfo chatUserInfo;
        ChatUserInfo[] chatUserInfoArray4 = chatUserInfoArray2;
        int n = chatUserInfoArray2.length;
        int n2 = 0;
        while (n2 < n) {
            chatUserInfo = chatUserInfoArray4[n2];
            this.field_152955_g.remove(chatUserInfo.displayName);
            ++n2;
        }
        chatUserInfoArray4 = chatUserInfoArray3;
        n = chatUserInfoArray3.length;
        n2 = 0;
        while (n2 < n) {
            chatUserInfo = chatUserInfoArray4[n2];
            this.field_152955_g.put(chatUserInfo.displayName, chatUserInfo);
            ++n2;
        }
        chatUserInfoArray4 = chatUserInfoArray;
        n = chatUserInfoArray.length;
        n2 = 0;
        while (n2 < n) {
            chatUserInfo = chatUserInfoArray4[n2];
            this.field_152955_g.put(chatUserInfo.displayName, chatUserInfo);
            ++n2;
        }
    }

    @Override
    public boolean func_152936_l() {
        return this.broadcastController.func_152849_q();
    }

    @Override
    public void func_152922_k() {
        if (this.broadcastController.isBroadcasting() && !this.broadcastController.isBroadcastPaused()) {
            long l;
            boolean bl;
            long l2 = System.nanoTime();
            long l3 = l2 - this.field_152959_k;
            boolean bl2 = bl = l3 >= (l = (long)(1000000000 / this.targetFPS));
            if (bl) {
                FrameBuffer frameBuffer = this.broadcastController.func_152822_N();
                Framebuffer framebuffer = this.mc.getFramebuffer();
                this.framebuffer.bindFramebuffer(true);
                GlStateManager.matrixMode(5889);
                GlStateManager.pushMatrix();
                GlStateManager.loadIdentity();
                GlStateManager.ortho(0.0, this.framebuffer.framebufferWidth, this.framebuffer.framebufferHeight, 0.0, 1000.0, 3000.0);
                GlStateManager.matrixMode(5888);
                GlStateManager.pushMatrix();
                GlStateManager.loadIdentity();
                GlStateManager.translate(0.0f, 0.0f, -2000.0f);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.viewport(0, 0, this.framebuffer.framebufferWidth, this.framebuffer.framebufferHeight);
                GlStateManager.enableTexture2D();
                GlStateManager.disableAlpha();
                GlStateManager.disableBlend();
                float f = this.framebuffer.framebufferWidth;
                float f2 = this.framebuffer.framebufferHeight;
                float f3 = (float)framebuffer.framebufferWidth / (float)framebuffer.framebufferTextureWidth;
                float f4 = (float)framebuffer.framebufferHeight / (float)framebuffer.framebufferTextureHeight;
                framebuffer.bindFramebufferTexture();
                GL11.glTexParameterf((int)3553, (int)10241, (float)9729.0f);
                GL11.glTexParameterf((int)3553, (int)10240, (float)9729.0f);
                Tessellator tessellator = Tessellator.getInstance();
                WorldRenderer worldRenderer = tessellator.getWorldRenderer();
                worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
                worldRenderer.pos(0.0, f2, 0.0).tex(0.0, f4).endVertex();
                worldRenderer.pos(f, f2, 0.0).tex(f3, f4).endVertex();
                worldRenderer.pos(f, 0.0, 0.0).tex(f3, 0.0).endVertex();
                worldRenderer.pos(0.0, 0.0, 0.0).tex(0.0, 0.0).endVertex();
                tessellator.draw();
                framebuffer.unbindFramebufferTexture();
                GlStateManager.popMatrix();
                GlStateManager.matrixMode(5889);
                GlStateManager.popMatrix();
                GlStateManager.matrixMode(5888);
                this.broadcastController.captureFramebuffer(frameBuffer);
                this.framebuffer.unbindFramebuffer();
                this.broadcastController.submitStreamFrame(frameBuffer);
                this.field_152959_k = l2;
            }
        }
    }

    @Override
    public void func_152935_j() {
        boolean bl;
        int n = Minecraft.gameSettings.streamChatEnabled;
        boolean bl2 = this.field_176029_e != null && this.chatController.func_175990_d(this.field_176029_e);
        boolean bl3 = bl = this.chatController.func_153000_j() == ChatController.ChatState.Initialized && (this.field_176029_e == null || this.chatController.func_175989_e(this.field_176029_e) == ChatController.EnumChannelState.Disconnected);
        if (n == 2) {
            if (bl2) {
                LOGGER.debug(STREAM_MARKER, "Disconnecting from twitch chat per user options");
                this.chatController.func_175991_l(this.field_176029_e);
            }
        } else if (n == 1) {
            if (bl && this.broadcastController.func_152849_q()) {
                LOGGER.debug(STREAM_MARKER, "Connecting to twitch chat per user options");
                this.func_152942_I();
            }
        } else if (n == 0) {
            if (bl2 && !this.isBroadcasting()) {
                LOGGER.debug(STREAM_MARKER, "Disconnecting from twitch chat as user is no longer streaming");
                this.chatController.func_175991_l(this.field_176029_e);
            } else if (bl && this.isBroadcasting()) {
                LOGGER.debug(STREAM_MARKER, "Connecting to twitch chat as user is streaming");
                this.func_152942_I();
            }
        }
        this.broadcastController.func_152821_H();
        this.chatController.func_152997_n();
    }

    private void func_176027_a(String string, ChatRawMessage chatRawMessage) {
        ChatUserInfo chatUserInfo = this.field_152955_g.get(string);
        if (chatUserInfo == null) {
            chatUserInfo = new ChatUserInfo();
            chatUserInfo.displayName = string;
            this.field_152955_g.put(string, chatUserInfo);
        }
        chatUserInfo.subscriptions = chatRawMessage.subscriptions;
        chatUserInfo.modes = chatRawMessage.modes;
        chatUserInfo.nameColorARGB = chatRawMessage.nameColorARGB;
    }

    public static int formatStreamKbps(float f) {
        return MathHelper.floor_float(230.0f + f * 3270.0f);
    }

    @Override
    public void func_152892_c(ErrorCode errorCode) {
        if (errorCode == ErrorCode.TTV_EC_SOUNDFLOWER_NOT_INSTALLED) {
            ChatComponentTranslation chatComponentTranslation = new ChatComponentTranslation("stream.unavailable.soundflower.chat.link", new Object[0]);
            chatComponentTranslation.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://help.mojang.com/customer/portal/articles/1374877-configuring-soundflower-for-streaming-on-apple-computers"));
            chatComponentTranslation.getChatStyle().setUnderlined(true);
            ChatComponentTranslation chatComponentTranslation2 = new ChatComponentTranslation("stream.unavailable.soundflower.chat", chatComponentTranslation);
            chatComponentTranslation2.getChatStyle().setColor(EnumChatFormatting.DARK_RED);
            this.mc.ingameGUI.getChatGUI().printChatMessage(chatComponentTranslation2);
        } else {
            ChatComponentTranslation chatComponentTranslation = new ChatComponentTranslation("stream.unavailable.unknown.chat", ErrorCode.getString((ErrorCode)errorCode));
            chatComponentTranslation.getChatStyle().setColor(EnumChatFormatting.DARK_RED);
            this.mc.ingameGUI.getChatGUI().printChatMessage(chatComponentTranslation);
        }
    }

    @Override
    public IStream.AuthFailureReason func_152918_H() {
        return this.authFailureReason;
    }

    @Override
    public void updateStreamVolume() {
        if (this.isBroadcasting()) {
            float f = Minecraft.gameSettings.streamGameVolume;
            boolean bl = this.field_152962_n || f <= 0.0f;
            this.broadcastController.setPlaybackDeviceVolume(bl ? 0.0f : f);
            this.broadcastController.setRecordingDeviceVolume(this.func_152929_G() ? 0.0f : Minecraft.gameSettings.streamMicVolume);
        }
    }
}

