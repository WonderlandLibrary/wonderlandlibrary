// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.stream;

import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import java.util.Arrays;
import net.minecraft.crash.CrashReport;
import tv.twitch.broadcast.StartFlags;
import tv.twitch.broadcast.EncodingCpuUsage;
import tv.twitch.broadcast.StreamInfoForSetting;
import tv.twitch.MessageLevel;
import tv.twitch.broadcast.PixelFormat;
import tv.twitch.broadcast.StreamAPI;
import tv.twitch.broadcast.DesktopStreamAPI;
import tv.twitch.CoreAPI;
import tv.twitch.StandardCoreAPI;
import tv.twitch.broadcast.StatType;
import tv.twitch.broadcast.GameInfo;
import tv.twitch.broadcast.GameInfoList;
import com.google.common.collect.Lists;
import tv.twitch.broadcast.AudioDeviceType;
import org.apache.logging.log4j.LogManager;
import tv.twitch.broadcast.IStatCallbacks;
import tv.twitch.broadcast.IStreamCallbacks;
import tv.twitch.ErrorCode;
import tv.twitch.broadcast.ArchivingState;
import tv.twitch.broadcast.StreamInfo;
import tv.twitch.broadcast.UserInfo;
import tv.twitch.broadcast.ChannelInfo;
import tv.twitch.AuthToken;
import tv.twitch.broadcast.IngestServer;
import tv.twitch.broadcast.IngestList;
import tv.twitch.broadcast.AudioParams;
import tv.twitch.broadcast.VideoParams;
import tv.twitch.broadcast.FrameBuffer;
import java.util.List;
import tv.twitch.broadcast.Stream;
import tv.twitch.Core;
import net.minecraft.util.ThreadSafeBoundList;
import org.apache.logging.log4j.Logger;

public class BroadcastController
{
    private static final Logger logger;
    protected final int field_152865_a = 30;
    protected final int field_152866_b = 3;
    private static final ThreadSafeBoundList<String> field_152862_C;
    private String lastError;
    protected BroadcastListener broadcastListener;
    protected String field_152868_d;
    protected String field_152869_e;
    protected String field_152870_f;
    protected boolean field_152871_g;
    protected Core streamCore;
    protected Stream theStream;
    protected List<FrameBuffer> field_152874_j;
    protected List<FrameBuffer> field_152875_k;
    protected boolean field_152876_l;
    protected boolean field_152877_m;
    protected boolean field_152878_n;
    protected BroadcastState broadcastState;
    protected String field_152880_p;
    protected VideoParams videoParamaters;
    protected AudioParams audioParamaters;
    protected IngestList ingestList;
    protected IngestServer ingestServ;
    protected AuthToken authenticationToken;
    protected ChannelInfo channelInfo;
    protected UserInfo userInfo;
    protected StreamInfo streamInfo;
    protected ArchivingState archivingState;
    protected long field_152890_z;
    protected IngestServerTester ingestServTester;
    private ErrorCode errorCode;
    protected IStreamCallbacks streamCallback;
    protected IStatCallbacks field_177949_C;
    
    static {
        logger = LogManager.getLogger();
        field_152862_C = new ThreadSafeBoundList<String>(String.class, 50);
    }
    
    public void setBroadcastListener(final BroadcastListener broadcastListenerIn) {
        this.broadcastListener = broadcastListenerIn;
    }
    
    public boolean func_152858_b() {
        return this.field_152876_l;
    }
    
    public void func_152842_a(final String p_152842_1_) {
        this.field_152868_d = p_152842_1_;
    }
    
    public StreamInfo getStreamInfo() {
        return this.streamInfo;
    }
    
    public ChannelInfo getChannelInfo() {
        return this.channelInfo;
    }
    
    public boolean isBroadcasting() {
        return this.broadcastState == BroadcastState.Broadcasting || this.broadcastState == BroadcastState.Paused;
    }
    
    public boolean isReadyToBroadcast() {
        return this.broadcastState == BroadcastState.ReadyToBroadcast;
    }
    
    public boolean isIngestTesting() {
        return this.broadcastState == BroadcastState.IngestTesting;
    }
    
    public boolean isBroadcastPaused() {
        return this.broadcastState == BroadcastState.Paused;
    }
    
    public boolean func_152849_q() {
        return this.field_152877_m;
    }
    
    public IngestServer getIngestServer() {
        return this.ingestServ;
    }
    
    public void setIngestServer(final IngestServer ingestServerSet) {
        this.ingestServ = ingestServerSet;
    }
    
    public IngestList getIngestList() {
        return this.ingestList;
    }
    
    public void setRecordingDeviceVolume(final float volume) {
        this.theStream.setVolume(AudioDeviceType.TTV_RECORDER_DEVICE, volume);
    }
    
    public void setPlaybackDeviceVolume(final float volume) {
        this.theStream.setVolume(AudioDeviceType.TTV_PLAYBACK_DEVICE, volume);
    }
    
    public IngestServerTester isReady() {
        return this.ingestServTester;
    }
    
    public long getStreamTime() {
        return this.theStream.getStreamTime();
    }
    
    protected boolean func_152848_y() {
        return true;
    }
    
    public ErrorCode getErrorCode() {
        return this.errorCode;
    }
    
    public BroadcastController() {
        this.lastError = null;
        this.broadcastListener = null;
        this.field_152868_d = "";
        this.field_152869_e = "";
        this.field_152870_f = "";
        this.field_152871_g = true;
        this.streamCore = null;
        this.theStream = null;
        this.field_152874_j = Lists.newArrayList();
        this.field_152875_k = Lists.newArrayList();
        this.field_152876_l = false;
        this.field_152877_m = false;
        this.field_152878_n = false;
        this.broadcastState = BroadcastState.Uninitialized;
        this.field_152880_p = null;
        this.videoParamaters = null;
        this.audioParamaters = null;
        this.ingestList = new IngestList(new IngestServer[0]);
        this.ingestServ = null;
        this.authenticationToken = new AuthToken();
        this.channelInfo = new ChannelInfo();
        this.userInfo = new UserInfo();
        this.streamInfo = new StreamInfo();
        this.archivingState = new ArchivingState();
        this.field_152890_z = 0L;
        this.ingestServTester = null;
        this.streamCallback = (IStreamCallbacks)new IStreamCallbacks() {
            public void requestAuthTokenCallback(final ErrorCode p_requestAuthTokenCallback_1_, final AuthToken p_requestAuthTokenCallback_2_) {
                if (ErrorCode.succeeded(p_requestAuthTokenCallback_1_)) {
                    BroadcastController.this.authenticationToken = p_requestAuthTokenCallback_2_;
                    BroadcastController.this.func_152827_a(BroadcastState.Authenticated);
                }
                else {
                    BroadcastController.this.authenticationToken.data = "";
                    BroadcastController.this.func_152827_a(BroadcastState.Initialized);
                    final String s = ErrorCode.getString(p_requestAuthTokenCallback_1_);
                    BroadcastController.this.logError(String.format("RequestAuthTokenDoneCallback got failure: %s", s));
                }
                try {
                    if (BroadcastController.this.broadcastListener != null) {
                        BroadcastController.this.broadcastListener.func_152900_a(p_requestAuthTokenCallback_1_, p_requestAuthTokenCallback_2_);
                    }
                }
                catch (final Exception exception) {
                    BroadcastController.this.logError(exception.toString());
                }
            }
            
            public void loginCallback(final ErrorCode p_loginCallback_1_, final ChannelInfo p_loginCallback_2_) {
                if (ErrorCode.succeeded(p_loginCallback_1_)) {
                    BroadcastController.this.channelInfo = p_loginCallback_2_;
                    BroadcastController.this.func_152827_a(BroadcastState.LoggedIn);
                    BroadcastController.this.field_152877_m = true;
                }
                else {
                    BroadcastController.this.func_152827_a(BroadcastState.Initialized);
                    BroadcastController.this.field_152877_m = false;
                    final String s = ErrorCode.getString(p_loginCallback_1_);
                    BroadcastController.this.logError(String.format("LoginCallback got failure: %s", s));
                }
                try {
                    if (BroadcastController.this.broadcastListener != null) {
                        BroadcastController.this.broadcastListener.func_152897_a(p_loginCallback_1_);
                    }
                }
                catch (final Exception exception) {
                    BroadcastController.this.logError(exception.toString());
                }
            }
            
            public void getIngestServersCallback(final ErrorCode p_getIngestServersCallback_1_, final IngestList p_getIngestServersCallback_2_) {
                if (ErrorCode.succeeded(p_getIngestServersCallback_1_)) {
                    BroadcastController.this.ingestList = p_getIngestServersCallback_2_;
                    BroadcastController.this.ingestServ = BroadcastController.this.ingestList.getDefaultServer();
                    BroadcastController.this.func_152827_a(BroadcastState.ReceivedIngestServers);
                    try {
                        if (BroadcastController.this.broadcastListener != null) {
                            BroadcastController.this.broadcastListener.func_152896_a(p_getIngestServersCallback_2_);
                        }
                    }
                    catch (final Exception exception) {
                        BroadcastController.this.logError(exception.toString());
                    }
                }
                else {
                    final String s = ErrorCode.getString(p_getIngestServersCallback_1_);
                    BroadcastController.this.logError(String.format("IngestListCallback got failure: %s", s));
                    BroadcastController.this.func_152827_a(BroadcastState.LoggingIn);
                }
            }
            
            public void getUserInfoCallback(final ErrorCode p_getUserInfoCallback_1_, final UserInfo p_getUserInfoCallback_2_) {
                BroadcastController.this.userInfo = p_getUserInfoCallback_2_;
                if (ErrorCode.failed(p_getUserInfoCallback_1_)) {
                    final String s = ErrorCode.getString(p_getUserInfoCallback_1_);
                    BroadcastController.this.logError(String.format("UserInfoDoneCallback got failure: %s", s));
                }
            }
            
            public void getStreamInfoCallback(final ErrorCode p_getStreamInfoCallback_1_, final StreamInfo p_getStreamInfoCallback_2_) {
                if (ErrorCode.succeeded(p_getStreamInfoCallback_1_)) {
                    BroadcastController.this.streamInfo = p_getStreamInfoCallback_2_;
                    try {
                        if (BroadcastController.this.broadcastListener != null) {
                            BroadcastController.this.broadcastListener.func_152894_a(p_getStreamInfoCallback_2_);
                        }
                    }
                    catch (final Exception exception) {
                        BroadcastController.this.logError(exception.toString());
                    }
                }
                else {
                    final String s = ErrorCode.getString(p_getStreamInfoCallback_1_);
                    BroadcastController.this.logWarning(String.format("StreamInfoDoneCallback got failure: %s", s));
                }
            }
            
            public void getArchivingStateCallback(final ErrorCode p_getArchivingStateCallback_1_, final ArchivingState p_getArchivingStateCallback_2_) {
                BroadcastController.this.archivingState = p_getArchivingStateCallback_2_;
                if (ErrorCode.failed(p_getArchivingStateCallback_1_)) {}
            }
            
            public void runCommercialCallback(final ErrorCode p_runCommercialCallback_1_) {
                if (ErrorCode.failed(p_runCommercialCallback_1_)) {
                    final String s = ErrorCode.getString(p_runCommercialCallback_1_);
                    BroadcastController.this.logWarning(String.format("RunCommercialCallback got failure: %s", s));
                }
            }
            
            public void setStreamInfoCallback(final ErrorCode p_setStreamInfoCallback_1_) {
                if (ErrorCode.failed(p_setStreamInfoCallback_1_)) {
                    final String s = ErrorCode.getString(p_setStreamInfoCallback_1_);
                    BroadcastController.this.logWarning(String.format("SetStreamInfoCallback got failure: %s", s));
                }
            }
            
            public void getGameNameListCallback(final ErrorCode p_getGameNameListCallback_1_, final GameInfoList p_getGameNameListCallback_2_) {
                if (ErrorCode.failed(p_getGameNameListCallback_1_)) {
                    final String s = ErrorCode.getString(p_getGameNameListCallback_1_);
                    BroadcastController.this.logError(String.format("GameNameListCallback got failure: %s", s));
                }
                try {
                    if (BroadcastController.this.broadcastListener != null) {
                        BroadcastController.this.broadcastListener.func_152898_a(p_getGameNameListCallback_1_, (p_getGameNameListCallback_2_ == null) ? new GameInfo[0] : p_getGameNameListCallback_2_.list);
                    }
                }
                catch (final Exception exception) {
                    BroadcastController.this.logError(exception.toString());
                }
            }
            
            public void bufferUnlockCallback(final long p_bufferUnlockCallback_1_) {
                final FrameBuffer framebuffer = FrameBuffer.lookupBuffer(p_bufferUnlockCallback_1_);
                BroadcastController.this.field_152875_k.add(framebuffer);
            }
            
            public void startCallback(final ErrorCode p_startCallback_1_) {
                if (ErrorCode.succeeded(p_startCallback_1_)) {
                    try {
                        if (BroadcastController.this.broadcastListener != null) {
                            BroadcastController.this.broadcastListener.func_152899_b();
                        }
                    }
                    catch (final Exception exception1) {
                        BroadcastController.this.logError(exception1.toString());
                    }
                    BroadcastController.this.func_152827_a(BroadcastState.Broadcasting);
                }
                else {
                    BroadcastController.this.videoParamaters = null;
                    BroadcastController.this.audioParamaters = null;
                    BroadcastController.this.func_152827_a(BroadcastState.ReadyToBroadcast);
                    try {
                        if (BroadcastController.this.broadcastListener != null) {
                            BroadcastController.this.broadcastListener.func_152892_c(p_startCallback_1_);
                        }
                    }
                    catch (final Exception exception2) {
                        BroadcastController.this.logError(exception2.toString());
                    }
                    final String s = ErrorCode.getString(p_startCallback_1_);
                    BroadcastController.this.logError(String.format("startCallback got failure: %s", s));
                }
            }
            
            public void stopCallback(final ErrorCode p_stopCallback_1_) {
                if (ErrorCode.succeeded(p_stopCallback_1_)) {
                    BroadcastController.this.videoParamaters = null;
                    BroadcastController.this.audioParamaters = null;
                    BroadcastController.this.func_152831_M();
                    try {
                        if (BroadcastController.this.broadcastListener != null) {
                            BroadcastController.this.broadcastListener.func_152901_c();
                        }
                    }
                    catch (final Exception exception) {
                        BroadcastController.this.logError(exception.toString());
                    }
                    if (BroadcastController.this.field_152877_m) {
                        BroadcastController.this.func_152827_a(BroadcastState.ReadyToBroadcast);
                    }
                    else {
                        BroadcastController.this.func_152827_a(BroadcastState.Initialized);
                    }
                }
                else {
                    BroadcastController.this.func_152827_a(BroadcastState.ReadyToBroadcast);
                    final String s = ErrorCode.getString(p_stopCallback_1_);
                    BroadcastController.this.logError(String.format("stopCallback got failure: %s", s));
                }
            }
            
            public void sendActionMetaDataCallback(final ErrorCode p_sendActionMetaDataCallback_1_) {
                if (ErrorCode.failed(p_sendActionMetaDataCallback_1_)) {
                    final String s = ErrorCode.getString(p_sendActionMetaDataCallback_1_);
                    BroadcastController.this.logError(String.format("sendActionMetaDataCallback got failure: %s", s));
                }
            }
            
            public void sendStartSpanMetaDataCallback(final ErrorCode p_sendStartSpanMetaDataCallback_1_) {
                if (ErrorCode.failed(p_sendStartSpanMetaDataCallback_1_)) {
                    final String s = ErrorCode.getString(p_sendStartSpanMetaDataCallback_1_);
                    BroadcastController.this.logError(String.format("sendStartSpanMetaDataCallback got failure: %s", s));
                }
            }
            
            public void sendEndSpanMetaDataCallback(final ErrorCode p_sendEndSpanMetaDataCallback_1_) {
                if (ErrorCode.failed(p_sendEndSpanMetaDataCallback_1_)) {
                    final String s = ErrorCode.getString(p_sendEndSpanMetaDataCallback_1_);
                    BroadcastController.this.logError(String.format("sendEndSpanMetaDataCallback got failure: %s", s));
                }
            }
        };
        this.field_177949_C = (IStatCallbacks)new IStatCallbacks() {
            public void statCallback(final StatType p_statCallback_1_, final long p_statCallback_2_) {
            }
        };
        this.streamCore = Core.getInstance();
        if (Core.getInstance() == null) {
            this.streamCore = new Core((CoreAPI)new StandardCoreAPI());
        }
        this.theStream = new Stream((StreamAPI)new DesktopStreamAPI());
    }
    
    protected PixelFormat getPixelFormat() {
        return PixelFormat.TTV_PF_RGBA;
    }
    
    public boolean func_152817_A() {
        if (this.field_152876_l) {
            return false;
        }
        this.theStream.setStreamCallbacks(this.streamCallback);
        ErrorCode errorcode = this.streamCore.initialize(this.field_152868_d, System.getProperty("java.library.path"));
        if (!this.func_152853_a(errorcode)) {
            this.theStream.setStreamCallbacks((IStreamCallbacks)null);
            this.errorCode = errorcode;
            return false;
        }
        errorcode = this.streamCore.setTraceLevel(MessageLevel.TTV_ML_ERROR);
        if (!this.func_152853_a(errorcode)) {
            this.theStream.setStreamCallbacks((IStreamCallbacks)null);
            this.streamCore.shutdown();
            this.errorCode = errorcode;
            return false;
        }
        if (ErrorCode.succeeded(errorcode)) {
            this.field_152876_l = true;
            this.func_152827_a(BroadcastState.Initialized);
            return true;
        }
        this.errorCode = errorcode;
        this.streamCore.shutdown();
        return false;
    }
    
    public boolean func_152851_B() {
        if (!this.field_152876_l) {
            return true;
        }
        if (this.isIngestTesting()) {
            return false;
        }
        this.field_152878_n = true;
        this.func_152845_C();
        this.theStream.setStreamCallbacks((IStreamCallbacks)null);
        this.theStream.setStatCallbacks((IStatCallbacks)null);
        final ErrorCode errorcode = this.streamCore.shutdown();
        this.func_152853_a(errorcode);
        this.field_152876_l = false;
        this.field_152878_n = false;
        this.func_152827_a(BroadcastState.Uninitialized);
        return true;
    }
    
    public void statCallback() {
        if (this.broadcastState != BroadcastState.Uninitialized) {
            if (this.ingestServTester != null) {
                this.ingestServTester.func_153039_l();
            }
            while (this.ingestServTester != null) {
                try {
                    Thread.sleep(200L);
                }
                catch (final Exception exception) {
                    this.logError(exception.toString());
                }
                this.func_152821_H();
            }
            this.func_152851_B();
        }
    }
    
    public boolean func_152818_a(final String p_152818_1_, final AuthToken p_152818_2_) {
        if (this.isIngestTesting()) {
            return false;
        }
        this.func_152845_C();
        if (p_152818_1_ == null || p_152818_1_.isEmpty()) {
            this.logError("Username must be valid");
            return false;
        }
        if (p_152818_2_ != null && p_152818_2_.data != null && !p_152818_2_.data.isEmpty()) {
            this.field_152880_p = p_152818_1_;
            this.authenticationToken = p_152818_2_;
            if (this.func_152858_b()) {
                this.func_152827_a(BroadcastState.Authenticated);
            }
            return true;
        }
        this.logError("Auth token must be valid");
        return false;
    }
    
    public boolean func_152845_C() {
        if (this.isIngestTesting()) {
            return false;
        }
        if (this.isBroadcasting()) {
            this.theStream.stop(false);
        }
        this.field_152880_p = "";
        this.authenticationToken = new AuthToken();
        if (!this.field_152877_m) {
            return false;
        }
        this.field_152877_m = false;
        if (!this.field_152878_n) {
            try {
                if (this.broadcastListener != null) {
                    this.broadcastListener.func_152895_a();
                }
            }
            catch (final Exception exception) {
                this.logError(exception.toString());
            }
        }
        this.func_152827_a(BroadcastState.Initialized);
        return true;
    }
    
    public boolean func_152828_a(String p_152828_1_, String p_152828_2_, String p_152828_3_) {
        if (!this.field_152877_m) {
            return false;
        }
        if (p_152828_1_ == null || p_152828_1_.equals("")) {
            p_152828_1_ = this.field_152880_p;
        }
        if (p_152828_2_ == null) {
            p_152828_2_ = "";
        }
        if (p_152828_3_ == null) {
            p_152828_3_ = "";
        }
        final StreamInfoForSetting streaminfoforsetting = new StreamInfoForSetting();
        streaminfoforsetting.streamTitle = p_152828_3_;
        streaminfoforsetting.gameName = p_152828_2_;
        final ErrorCode errorcode = this.theStream.setStreamInfo(this.authenticationToken, p_152828_1_, streaminfoforsetting);
        this.func_152853_a(errorcode);
        return ErrorCode.succeeded(errorcode);
    }
    
    public boolean requestCommercial() {
        if (!this.isBroadcasting()) {
            return false;
        }
        final ErrorCode errorcode = this.theStream.runCommercial(this.authenticationToken);
        this.func_152853_a(errorcode);
        return ErrorCode.succeeded(errorcode);
    }
    
    public VideoParams func_152834_a(final int maxKbps, final int p_152834_2_, final float p_152834_3_, final float p_152834_4_) {
        final int[] aint = this.theStream.getMaxResolution(maxKbps, p_152834_2_, p_152834_3_, p_152834_4_);
        final VideoParams videoparams = new VideoParams();
        videoparams.maxKbps = maxKbps;
        videoparams.encodingCpuUsage = EncodingCpuUsage.TTV_ECU_HIGH;
        videoparams.pixelFormat = this.getPixelFormat();
        videoparams.targetFps = p_152834_2_;
        videoparams.outputWidth = aint[0];
        videoparams.outputHeight = aint[1];
        videoparams.disableAdaptiveBitrate = false;
        videoparams.verticalFlip = false;
        return videoparams;
    }
    
    public boolean func_152836_a(final VideoParams p_152836_1_) {
        if (p_152836_1_ == null || !this.isReadyToBroadcast()) {
            return false;
        }
        this.videoParamaters = p_152836_1_.clone();
        this.audioParamaters = new AudioParams();
        this.audioParamaters.audioEnabled = (this.field_152871_g && this.func_152848_y());
        this.audioParamaters.enableMicCapture = this.audioParamaters.audioEnabled;
        this.audioParamaters.enablePlaybackCapture = this.audioParamaters.audioEnabled;
        this.audioParamaters.enablePassthroughAudio = false;
        if (!this.func_152823_L()) {
            this.videoParamaters = null;
            this.audioParamaters = null;
            return false;
        }
        final ErrorCode errorcode = this.theStream.start(p_152836_1_, this.audioParamaters, this.ingestServ, StartFlags.None, true);
        if (ErrorCode.failed(errorcode)) {
            this.func_152831_M();
            final String s = ErrorCode.getString(errorcode);
            this.logError(String.format("Error while starting to broadcast: %s", s));
            this.videoParamaters = null;
            this.audioParamaters = null;
            return false;
        }
        this.func_152827_a(BroadcastState.Starting);
        return true;
    }
    
    public boolean stopBroadcasting() {
        if (!this.isBroadcasting()) {
            return false;
        }
        final ErrorCode errorcode = this.theStream.stop(true);
        if (ErrorCode.failed(errorcode)) {
            final String s = ErrorCode.getString(errorcode);
            this.logError(String.format("Error while stopping the broadcast: %s", s));
            return false;
        }
        this.func_152827_a(BroadcastState.Stopping);
        return ErrorCode.succeeded(errorcode);
    }
    
    public boolean func_152847_F() {
        if (!this.isBroadcasting()) {
            return false;
        }
        final ErrorCode errorcode = this.theStream.pauseVideo();
        if (ErrorCode.failed(errorcode)) {
            this.stopBroadcasting();
            final String s = ErrorCode.getString(errorcode);
            this.logError(String.format("Error pausing stream: %s\n", s));
        }
        else {
            this.func_152827_a(BroadcastState.Paused);
        }
        return ErrorCode.succeeded(errorcode);
    }
    
    public boolean func_152854_G() {
        if (!this.isBroadcastPaused()) {
            return false;
        }
        this.func_152827_a(BroadcastState.Broadcasting);
        return true;
    }
    
    public boolean func_152840_a(final String p_152840_1_, final long p_152840_2_, final String p_152840_4_, final String p_152840_5_) {
        final ErrorCode errorcode = this.theStream.sendActionMetaData(this.authenticationToken, p_152840_1_, p_152840_2_, p_152840_4_, p_152840_5_);
        if (ErrorCode.failed(errorcode)) {
            final String s = ErrorCode.getString(errorcode);
            this.logError(String.format("Error while sending meta data: %s\n", s));
            return false;
        }
        return true;
    }
    
    public long func_177946_b(final String p_177946_1_, final long p_177946_2_, final String p_177946_4_, final String p_177946_5_) {
        final long i = this.theStream.sendStartSpanMetaData(this.authenticationToken, p_177946_1_, p_177946_2_, p_177946_4_, p_177946_5_);
        if (i == -1L) {
            this.logError(String.format("Error in SendStartSpanMetaData\n", new Object[0]));
        }
        return i;
    }
    
    public boolean func_177947_a(final String p_177947_1_, final long p_177947_2_, final long p_177947_4_, final String p_177947_6_, final String p_177947_7_) {
        if (p_177947_4_ == -1L) {
            this.logError(String.format("Invalid sequence id: %d\n", p_177947_4_));
            return false;
        }
        final ErrorCode errorcode = this.theStream.sendEndSpanMetaData(this.authenticationToken, p_177947_1_, p_177947_2_, p_177947_4_, p_177947_6_, p_177947_7_);
        if (ErrorCode.failed(errorcode)) {
            final String s = ErrorCode.getString(errorcode);
            this.logError(String.format("Error in SendStopSpanMetaData: %s\n", s));
            return false;
        }
        return true;
    }
    
    protected void func_152827_a(final BroadcastState p_152827_1_) {
        if (p_152827_1_ != this.broadcastState) {
            this.broadcastState = p_152827_1_;
            try {
                if (this.broadcastListener != null) {
                    this.broadcastListener.func_152891_a(p_152827_1_);
                }
            }
            catch (final Exception exception) {
                this.logError(exception.toString());
            }
        }
    }
    
    public void func_152821_H() {
        if (this.theStream != null && this.field_152876_l) {
            ErrorCode errorcode = this.theStream.pollTasks();
            this.func_152853_a(errorcode);
            if (this.isIngestTesting()) {
                this.ingestServTester.func_153041_j();
                if (this.ingestServTester.func_153032_e()) {
                    this.ingestServTester = null;
                    this.func_152827_a(BroadcastState.ReadyToBroadcast);
                }
            }
            switch (this.broadcastState) {
                case Authenticated: {
                    this.func_152827_a(BroadcastState.LoggingIn);
                    errorcode = this.theStream.login(this.authenticationToken);
                    if (ErrorCode.failed(errorcode)) {
                        final String s3 = ErrorCode.getString(errorcode);
                        this.logError(String.format("Error in TTV_Login: %s\n", s3));
                        break;
                    }
                    break;
                }
                case LoggedIn: {
                    this.func_152827_a(BroadcastState.FindingIngestServer);
                    errorcode = this.theStream.getIngestServers(this.authenticationToken);
                    if (ErrorCode.failed(errorcode)) {
                        this.func_152827_a(BroadcastState.LoggedIn);
                        final String s4 = ErrorCode.getString(errorcode);
                        this.logError(String.format("Error in TTV_GetIngestServers: %s\n", s4));
                        break;
                    }
                    break;
                }
                case ReceivedIngestServers: {
                    this.func_152827_a(BroadcastState.ReadyToBroadcast);
                    errorcode = this.theStream.getUserInfo(this.authenticationToken);
                    if (ErrorCode.failed(errorcode)) {
                        final String s5 = ErrorCode.getString(errorcode);
                        this.logError(String.format("Error in TTV_GetUserInfo: %s\n", s5));
                    }
                    this.func_152835_I();
                    errorcode = this.theStream.getArchivingState(this.authenticationToken);
                    if (ErrorCode.failed(errorcode)) {
                        final String s6 = ErrorCode.getString(errorcode);
                        this.logError(String.format("Error in TTV_GetArchivingState: %s\n", s6));
                        break;
                    }
                    break;
                }
                case Broadcasting:
                case Paused: {
                    this.func_152835_I();
                    break;
                }
            }
        }
    }
    
    protected void func_152835_I() {
        final long i = System.nanoTime();
        final long j = (i - this.field_152890_z) / 1000000000L;
        if (j >= 30L) {
            this.field_152890_z = i;
            final ErrorCode errorcode = this.theStream.getStreamInfo(this.authenticationToken, this.field_152880_p);
            if (ErrorCode.failed(errorcode)) {
                final String s = ErrorCode.getString(errorcode);
                this.logError(String.format("Error in TTV_GetStreamInfo: %s", s));
            }
        }
    }
    
    public IngestServerTester func_152838_J() {
        if (!this.isReadyToBroadcast() || this.ingestList == null) {
            return null;
        }
        if (this.isIngestTesting()) {
            return null;
        }
        (this.ingestServTester = new IngestServerTester(this.theStream, this.ingestList)).func_176004_j();
        this.func_152827_a(BroadcastState.IngestTesting);
        return this.ingestServTester;
    }
    
    protected boolean func_152823_L() {
        for (int i = 0; i < 3; ++i) {
            final FrameBuffer framebuffer = this.theStream.allocateFrameBuffer(this.videoParamaters.outputWidth * this.videoParamaters.outputHeight * 4);
            if (!framebuffer.getIsValid()) {
                this.logError(String.format("Error while allocating frame buffer", new Object[0]));
                return false;
            }
            this.field_152874_j.add(framebuffer);
            this.field_152875_k.add(framebuffer);
        }
        return true;
    }
    
    protected void func_152831_M() {
        for (int i = 0; i < this.field_152874_j.size(); ++i) {
            final FrameBuffer framebuffer = this.field_152874_j.get(i);
            framebuffer.free();
        }
        this.field_152875_k.clear();
        this.field_152874_j.clear();
    }
    
    public FrameBuffer func_152822_N() {
        if (this.field_152875_k.size() == 0) {
            this.logError(String.format("Out of free buffers, this should never happen", new Object[0]));
            return null;
        }
        final FrameBuffer framebuffer = this.field_152875_k.get(this.field_152875_k.size() - 1);
        this.field_152875_k.remove(this.field_152875_k.size() - 1);
        return framebuffer;
    }
    
    public void captureFramebuffer(final FrameBuffer p_152846_1_) {
        try {
            this.theStream.captureFrameBuffer_ReadPixels(p_152846_1_);
        }
        catch (final Throwable throwable) {
            final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Trying to submit a frame to Twitch");
            final CrashReportCategory crashreportcategory = crashreport.makeCategory("Broadcast State");
            crashreportcategory.addCrashSection("Last reported errors", Arrays.toString(BroadcastController.field_152862_C.func_152756_c()));
            crashreportcategory.addCrashSection("Buffer", p_152846_1_);
            crashreportcategory.addCrashSection("Free buffer count", this.field_152875_k.size());
            crashreportcategory.addCrashSection("Capture buffer count", this.field_152874_j.size());
            throw new ReportedException(crashreport);
        }
    }
    
    public ErrorCode submitStreamFrame(final FrameBuffer frame) {
        if (this.isBroadcastPaused()) {
            this.func_152854_G();
        }
        else if (!this.isBroadcasting()) {
            return ErrorCode.TTV_EC_STREAM_NOT_STARTED;
        }
        final ErrorCode errorcode = this.theStream.submitVideoFrame(frame);
        if (errorcode != ErrorCode.TTV_EC_SUCCESS) {
            final String s = ErrorCode.getString(errorcode);
            if (ErrorCode.succeeded(errorcode)) {
                this.logWarning(String.format("Warning in SubmitTexturePointer: %s\n", s));
            }
            else {
                this.logError(String.format("Error in SubmitTexturePointer: %s\n", s));
                this.stopBroadcasting();
            }
            if (this.broadcastListener != null) {
                this.broadcastListener.func_152893_b(errorcode);
            }
        }
        return errorcode;
    }
    
    protected boolean func_152853_a(final ErrorCode p_152853_1_) {
        if (ErrorCode.failed(p_152853_1_)) {
            this.logError(ErrorCode.getString(p_152853_1_));
            return false;
        }
        return true;
    }
    
    protected void logError(final String error) {
        this.lastError = error;
        BroadcastController.field_152862_C.func_152757_a("<Error> " + error);
        BroadcastController.logger.error(TwitchStream.STREAM_MARKER, "[Broadcast controller] {}", new Object[] { error });
    }
    
    protected void logWarning(final String warning) {
        BroadcastController.field_152862_C.func_152757_a("<Warning> " + warning);
        BroadcastController.logger.warn(TwitchStream.STREAM_MARKER, "[Broadcast controller] {}", new Object[] { warning });
    }
    
    public enum BroadcastState
    {
        Uninitialized("Uninitialized", 0), 
        Initialized("Initialized", 1), 
        Authenticating("Authenticating", 2), 
        Authenticated("Authenticated", 3), 
        LoggingIn("LoggingIn", 4), 
        LoggedIn("LoggedIn", 5), 
        FindingIngestServer("FindingIngestServer", 6), 
        ReceivedIngestServers("ReceivedIngestServers", 7), 
        ReadyToBroadcast("ReadyToBroadcast", 8), 
        Starting("Starting", 9), 
        Broadcasting("Broadcasting", 10), 
        Stopping("Stopping", 11), 
        Paused("Paused", 12), 
        IngestTesting("IngestTesting", 13);
        
        private BroadcastState(final String s, final int n) {
        }
    }
    
    public interface BroadcastListener
    {
        void func_152900_a(final ErrorCode p0, final AuthToken p1);
        
        void func_152897_a(final ErrorCode p0);
        
        void func_152898_a(final ErrorCode p0, final GameInfo[] p1);
        
        void func_152891_a(final BroadcastState p0);
        
        void func_152895_a();
        
        void func_152894_a(final StreamInfo p0);
        
        void func_152896_a(final IngestList p0);
        
        void func_152893_b(final ErrorCode p0);
        
        void func_152899_b();
        
        void func_152901_c();
        
        void func_152892_c(final ErrorCode p0);
    }
}
