/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  tv.twitch.AuthToken
 *  tv.twitch.Core
 *  tv.twitch.CoreAPI
 *  tv.twitch.ErrorCode
 *  tv.twitch.MessageLevel
 *  tv.twitch.StandardCoreAPI
 *  tv.twitch.broadcast.ArchivingState
 *  tv.twitch.broadcast.AudioDeviceType
 *  tv.twitch.broadcast.AudioParams
 *  tv.twitch.broadcast.ChannelInfo
 *  tv.twitch.broadcast.DesktopStreamAPI
 *  tv.twitch.broadcast.EncodingCpuUsage
 *  tv.twitch.broadcast.FrameBuffer
 *  tv.twitch.broadcast.GameInfo
 *  tv.twitch.broadcast.GameInfoList
 *  tv.twitch.broadcast.IStatCallbacks
 *  tv.twitch.broadcast.IStreamCallbacks
 *  tv.twitch.broadcast.IngestList
 *  tv.twitch.broadcast.IngestServer
 *  tv.twitch.broadcast.PixelFormat
 *  tv.twitch.broadcast.StartFlags
 *  tv.twitch.broadcast.StatType
 *  tv.twitch.broadcast.Stream
 *  tv.twitch.broadcast.StreamAPI
 *  tv.twitch.broadcast.StreamInfo
 *  tv.twitch.broadcast.StreamInfoForSetting
 *  tv.twitch.broadcast.UserInfo
 *  tv.twitch.broadcast.VideoParams
 */
package net.minecraft.client.stream;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.stream.IngestServerTester;
import net.minecraft.client.stream.TwitchStream;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ThreadSafeBoundList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tv.twitch.AuthToken;
import tv.twitch.Core;
import tv.twitch.CoreAPI;
import tv.twitch.ErrorCode;
import tv.twitch.MessageLevel;
import tv.twitch.StandardCoreAPI;
import tv.twitch.broadcast.ArchivingState;
import tv.twitch.broadcast.AudioDeviceType;
import tv.twitch.broadcast.AudioParams;
import tv.twitch.broadcast.ChannelInfo;
import tv.twitch.broadcast.DesktopStreamAPI;
import tv.twitch.broadcast.EncodingCpuUsage;
import tv.twitch.broadcast.FrameBuffer;
import tv.twitch.broadcast.GameInfo;
import tv.twitch.broadcast.GameInfoList;
import tv.twitch.broadcast.IStatCallbacks;
import tv.twitch.broadcast.IStreamCallbacks;
import tv.twitch.broadcast.IngestList;
import tv.twitch.broadcast.IngestServer;
import tv.twitch.broadcast.PixelFormat;
import tv.twitch.broadcast.StartFlags;
import tv.twitch.broadcast.StatType;
import tv.twitch.broadcast.Stream;
import tv.twitch.broadcast.StreamAPI;
import tv.twitch.broadcast.StreamInfo;
import tv.twitch.broadcast.StreamInfoForSetting;
import tv.twitch.broadcast.UserInfo;
import tv.twitch.broadcast.VideoParams;

public class BroadcastController {
    private static final ThreadSafeBoundList<String> field_152862_C;
    protected VideoParams videoParamaters = null;
    protected String field_152868_d = "";
    protected boolean field_152878_n = false;
    protected UserInfo userInfo;
    protected IngestList ingestList;
    protected AudioParams audioParamaters = null;
    protected boolean field_152871_g = true;
    protected boolean field_152877_m = false;
    protected String field_152880_p = null;
    protected IStreamCallbacks field_177948_B;
    protected ChannelInfo channelInfo;
    protected String field_152870_f = "";
    protected ArchivingState field_152889_y;
    protected List<FrameBuffer> field_152874_j = Lists.newArrayList();
    protected IngestServer field_152884_t = null;
    private ErrorCode errorCode;
    protected String field_152869_e = "";
    private static final Logger logger;
    protected Core field_152872_h = null;
    protected BroadcastState broadcastState;
    protected List<FrameBuffer> field_152875_k = Lists.newArrayList();
    protected StreamInfo streamInfo;
    protected BroadcastListener broadcastListener = null;
    protected final int field_152866_b = 3;
    protected boolean field_152876_l = false;
    protected IngestServerTester field_152860_A = null;
    private String field_152863_D = null;
    protected IStatCallbacks field_177949_C;
    protected long field_152890_z = 0L;
    protected final int field_152865_a = 30;
    protected AuthToken authenticationToken;
    protected Stream field_152873_i = null;

    public StreamInfo getStreamInfo() {
        return this.streamInfo;
    }

    static {
        logger = LogManager.getLogger();
        field_152862_C = new ThreadSafeBoundList<String>(String.class, 50);
    }

    protected boolean func_152848_y() {
        return true;
    }

    public boolean func_152840_a(String string, long l, String string2, String string3) {
        ErrorCode errorCode = this.field_152873_i.sendActionMetaData(this.authenticationToken, string, l, string2, string3);
        if (ErrorCode.failed((ErrorCode)errorCode)) {
            String string4 = ErrorCode.getString((ErrorCode)errorCode);
            this.func_152820_d(String.format("Error while sending meta data: %s\n", string4));
            return false;
        }
        return true;
    }

    public long func_152844_x() {
        return this.field_152873_i.getStreamTime();
    }

    public boolean func_177947_a(String string, long l, long l2, String string2, String string3) {
        if (l2 == -1L) {
            this.func_152820_d(String.format("Invalid sequence id: %d\n", l2));
            return false;
        }
        ErrorCode errorCode = this.field_152873_i.sendEndSpanMetaData(this.authenticationToken, string, l, l2, string2, string3);
        if (ErrorCode.failed((ErrorCode)errorCode)) {
            String string4 = ErrorCode.getString((ErrorCode)errorCode);
            this.func_152820_d(String.format("Error in SendStopSpanMetaData: %s\n", string4));
            return false;
        }
        return true;
    }

    protected void func_152832_e(String string) {
        field_152862_C.func_152757_a("<Warning> " + string);
        logger.warn(TwitchStream.STREAM_MARKER, "[Broadcast controller] {}", new Object[]{string});
    }

    public boolean isReadyToBroadcast() {
        return this.broadcastState == BroadcastState.ReadyToBroadcast;
    }

    public void captureFramebuffer(FrameBuffer frameBuffer) {
        try {
            this.field_152873_i.captureFrameBuffer_ReadPixels(frameBuffer);
        }
        catch (Throwable throwable) {
            CrashReport crashReport = CrashReport.makeCrashReport(throwable, "Trying to submit a frame to Twitch");
            CrashReportCategory crashReportCategory = crashReport.makeCategory("Broadcast State");
            crashReportCategory.addCrashSection("Last reported errors", Arrays.toString(field_152862_C.func_152756_c()));
            crashReportCategory.addCrashSection("Buffer", frameBuffer);
            crashReportCategory.addCrashSection("Free buffer count", this.field_152875_k.size());
            crashReportCategory.addCrashSection("Capture buffer count", this.field_152874_j.size());
            throw new ReportedException(crashReport);
        }
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
        this.field_152873_i.setStreamCallbacks(null);
        this.field_152873_i.setStatCallbacks(null);
        ErrorCode errorCode = this.field_152872_h.shutdown();
        this.func_152853_a(errorCode);
        this.field_152876_l = false;
        this.field_152878_n = false;
        this.func_152827_a(BroadcastState.Uninitialized);
        return true;
    }

    public boolean func_152854_G() {
        if (!this.isBroadcastPaused()) {
            return false;
        }
        this.func_152827_a(BroadcastState.Broadcasting);
        return true;
    }

    public VideoParams func_152834_a(int n, int n2, float f, float f2) {
        int[] nArray = this.field_152873_i.getMaxResolution(n, n2, f, f2);
        VideoParams videoParams = new VideoParams();
        videoParams.maxKbps = n;
        videoParams.encodingCpuUsage = EncodingCpuUsage.TTV_ECU_HIGH;
        videoParams.pixelFormat = this.func_152826_z();
        videoParams.targetFps = n2;
        videoParams.outputWidth = nArray[0];
        videoParams.outputHeight = nArray[1];
        videoParams.disableAdaptiveBitrate = false;
        videoParams.verticalFlip = false;
        return videoParams;
    }

    public boolean isBroadcasting() {
        return this.broadcastState == BroadcastState.Broadcasting || this.broadcastState == BroadcastState.Paused;
    }

    public boolean requestCommercial() {
        if (!this.isBroadcasting()) {
            return false;
        }
        ErrorCode errorCode = this.field_152873_i.runCommercial(this.authenticationToken);
        this.func_152853_a(errorCode);
        return ErrorCode.succeeded((ErrorCode)errorCode);
    }

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }

    protected void func_152827_a(BroadcastState broadcastState) {
        if (broadcastState != this.broadcastState) {
            this.broadcastState = broadcastState;
            try {
                if (this.broadcastListener != null) {
                    this.broadcastListener.func_152891_a(broadcastState);
                }
            }
            catch (Exception exception) {
                this.func_152820_d(exception.toString());
            }
        }
    }

    protected void func_152831_M() {
        int n = 0;
        while (n < this.field_152874_j.size()) {
            FrameBuffer frameBuffer = this.field_152874_j.get(n);
            frameBuffer.free();
            ++n;
        }
        this.field_152875_k.clear();
        this.field_152874_j.clear();
    }

    public boolean func_152847_F() {
        if (!this.isBroadcasting()) {
            return false;
        }
        ErrorCode errorCode = this.field_152873_i.pauseVideo();
        if (ErrorCode.failed((ErrorCode)errorCode)) {
            this.stopBroadcasting();
            String string = ErrorCode.getString((ErrorCode)errorCode);
            this.func_152820_d(String.format("Error pausing stream: %s\n", string));
        } else {
            this.func_152827_a(BroadcastState.Paused);
        }
        return ErrorCode.succeeded((ErrorCode)errorCode);
    }

    public IngestServer func_152833_s() {
        return this.field_152884_t;
    }

    public long func_177946_b(String string, long l, String string2, String string3) {
        long l2 = this.field_152873_i.sendStartSpanMetaData(this.authenticationToken, string, l, string2, string3);
        if (l2 == -1L) {
            this.func_152820_d(String.format("Error in SendStartSpanMetaData\n", new Object[0]));
        }
        return l2;
    }

    protected void func_152835_I() {
        long l = System.nanoTime();
        long l2 = (l - this.field_152890_z) / 1000000000L;
        if (l2 >= 30L) {
            this.field_152890_z = l;
            ErrorCode errorCode = this.field_152873_i.getStreamInfo(this.authenticationToken, this.field_152880_p);
            if (ErrorCode.failed((ErrorCode)errorCode)) {
                String string = ErrorCode.getString((ErrorCode)errorCode);
                this.func_152820_d(String.format("Error in TTV_GetStreamInfo: %s", string));
            }
        }
    }

    public FrameBuffer func_152822_N() {
        if (this.field_152875_k.size() == 0) {
            this.func_152820_d(String.format("Out of free buffers, this should never happen", new Object[0]));
            return null;
        }
        FrameBuffer frameBuffer = this.field_152875_k.get(this.field_152875_k.size() - 1);
        this.field_152875_k.remove(this.field_152875_k.size() - 1);
        return frameBuffer;
    }

    public boolean func_152818_a(String string, AuthToken authToken) {
        if (this.isIngestTesting()) {
            return false;
        }
        this.func_152845_C();
        if (string != null && !string.isEmpty()) {
            if (authToken != null && authToken.data != null && !authToken.data.isEmpty()) {
                this.field_152880_p = string;
                this.authenticationToken = authToken;
                if (this.func_152858_b()) {
                    this.func_152827_a(BroadcastState.Authenticated);
                }
                return true;
            }
            this.func_152820_d("Auth token must be valid");
            return false;
        }
        this.func_152820_d("Username must be valid");
        return false;
    }

    public boolean isIngestTesting() {
        return this.broadcastState == BroadcastState.IngestTesting;
    }

    public boolean stopBroadcasting() {
        if (!this.isBroadcasting()) {
            return false;
        }
        ErrorCode errorCode = this.field_152873_i.stop(true);
        if (ErrorCode.failed((ErrorCode)errorCode)) {
            String string = ErrorCode.getString((ErrorCode)errorCode);
            this.func_152820_d(String.format("Error while stopping the broadcast: %s", string));
            return false;
        }
        this.func_152827_a(BroadcastState.Stopping);
        return ErrorCode.succeeded((ErrorCode)errorCode);
    }

    protected boolean func_152823_L() {
        int n = 0;
        while (n < 3) {
            FrameBuffer frameBuffer = this.field_152873_i.allocateFrameBuffer(this.videoParamaters.outputWidth * this.videoParamaters.outputHeight * 4);
            if (!frameBuffer.getIsValid()) {
                this.func_152820_d(String.format("Error while allocating frame buffer", new Object[0]));
                return false;
            }
            this.field_152874_j.add(frameBuffer);
            this.field_152875_k.add(frameBuffer);
            ++n;
        }
        return true;
    }

    protected void func_152820_d(String string) {
        this.field_152863_D = string;
        field_152862_C.func_152757_a("<Error> " + string);
        logger.error(TwitchStream.STREAM_MARKER, "[Broadcast controller] {}", new Object[]{string});
    }

    public ChannelInfo getChannelInfo() {
        return this.channelInfo;
    }

    public IngestServerTester isReady() {
        return this.field_152860_A;
    }

    protected boolean func_152853_a(ErrorCode errorCode) {
        if (ErrorCode.failed((ErrorCode)errorCode)) {
            this.func_152820_d(ErrorCode.getString((ErrorCode)errorCode));
            return false;
        }
        return true;
    }

    public void setRecordingDeviceVolume(float f) {
        this.field_152873_i.setVolume(AudioDeviceType.TTV_RECORDER_DEVICE, f);
    }

    public boolean func_152828_a(String string, String string2, String string3) {
        if (!this.field_152877_m) {
            return false;
        }
        if (string == null || string.equals("")) {
            string = this.field_152880_p;
        }
        if (string2 == null) {
            string2 = "";
        }
        if (string3 == null) {
            string3 = "";
        }
        StreamInfoForSetting streamInfoForSetting = new StreamInfoForSetting();
        streamInfoForSetting.streamTitle = string3;
        streamInfoForSetting.gameName = string2;
        ErrorCode errorCode = this.field_152873_i.setStreamInfo(this.authenticationToken, string, streamInfoForSetting);
        this.func_152853_a(errorCode);
        return ErrorCode.succeeded((ErrorCode)errorCode);
    }

    public void statCallback() {
        if (this.broadcastState != BroadcastState.Uninitialized) {
            if (this.field_152860_A != null) {
                this.field_152860_A.func_153039_l();
            }
            while (this.field_152860_A != null) {
                try {
                    Thread.sleep(200L);
                }
                catch (Exception exception) {
                    this.func_152820_d(exception.toString());
                }
                this.func_152821_H();
            }
            this.func_152851_B();
        }
    }

    protected PixelFormat func_152826_z() {
        return PixelFormat.TTV_PF_RGBA;
    }

    public IngestList func_152855_t() {
        return this.ingestList;
    }

    public BroadcastController() {
        this.broadcastState = BroadcastState.Uninitialized;
        this.ingestList = new IngestList(new IngestServer[0]);
        this.authenticationToken = new AuthToken();
        this.channelInfo = new ChannelInfo();
        this.userInfo = new UserInfo();
        this.streamInfo = new StreamInfo();
        this.field_152889_y = new ArchivingState();
        this.field_177948_B = new IStreamCallbacks(){

            public void sendStartSpanMetaDataCallback(ErrorCode errorCode) {
                if (ErrorCode.failed((ErrorCode)errorCode)) {
                    String string = ErrorCode.getString((ErrorCode)errorCode);
                    BroadcastController.this.func_152820_d(String.format("sendStartSpanMetaDataCallback got failure: %s", string));
                }
            }

            public void runCommercialCallback(ErrorCode errorCode) {
                if (ErrorCode.failed((ErrorCode)errorCode)) {
                    String string = ErrorCode.getString((ErrorCode)errorCode);
                    BroadcastController.this.func_152832_e(String.format("RunCommercialCallback got failure: %s", string));
                }
            }

            public void startCallback(ErrorCode errorCode) {
                if (ErrorCode.succeeded((ErrorCode)errorCode)) {
                    try {
                        if (BroadcastController.this.broadcastListener != null) {
                            BroadcastController.this.broadcastListener.func_152899_b();
                        }
                    }
                    catch (Exception exception) {
                        BroadcastController.this.func_152820_d(exception.toString());
                    }
                    BroadcastController.this.func_152827_a(BroadcastState.Broadcasting);
                } else {
                    BroadcastController.this.videoParamaters = null;
                    BroadcastController.this.audioParamaters = null;
                    BroadcastController.this.func_152827_a(BroadcastState.ReadyToBroadcast);
                    try {
                        if (BroadcastController.this.broadcastListener != null) {
                            BroadcastController.this.broadcastListener.func_152892_c(errorCode);
                        }
                    }
                    catch (Exception exception) {
                        BroadcastController.this.func_152820_d(exception.toString());
                    }
                    String string = ErrorCode.getString((ErrorCode)errorCode);
                    BroadcastController.this.func_152820_d(String.format("startCallback got failure: %s", string));
                }
            }

            public void sendEndSpanMetaDataCallback(ErrorCode errorCode) {
                if (ErrorCode.failed((ErrorCode)errorCode)) {
                    String string = ErrorCode.getString((ErrorCode)errorCode);
                    BroadcastController.this.func_152820_d(String.format("sendEndSpanMetaDataCallback got failure: %s", string));
                }
            }

            public void sendActionMetaDataCallback(ErrorCode errorCode) {
                if (ErrorCode.failed((ErrorCode)errorCode)) {
                    String string = ErrorCode.getString((ErrorCode)errorCode);
                    BroadcastController.this.func_152820_d(String.format("sendActionMetaDataCallback got failure: %s", string));
                }
            }

            public void getUserInfoCallback(ErrorCode errorCode, UserInfo userInfo) {
                BroadcastController.this.userInfo = userInfo;
                if (ErrorCode.failed((ErrorCode)errorCode)) {
                    String string = ErrorCode.getString((ErrorCode)errorCode);
                    BroadcastController.this.func_152820_d(String.format("UserInfoDoneCallback got failure: %s", string));
                }
            }

            public void getIngestServersCallback(ErrorCode errorCode, IngestList ingestList) {
                if (ErrorCode.succeeded((ErrorCode)errorCode)) {
                    BroadcastController.this.ingestList = ingestList;
                    BroadcastController.this.field_152884_t = BroadcastController.this.ingestList.getDefaultServer();
                    BroadcastController.this.func_152827_a(BroadcastState.ReceivedIngestServers);
                    try {
                        if (BroadcastController.this.broadcastListener != null) {
                            BroadcastController.this.broadcastListener.func_152896_a(ingestList);
                        }
                    }
                    catch (Exception exception) {
                        BroadcastController.this.func_152820_d(exception.toString());
                    }
                } else {
                    String string = ErrorCode.getString((ErrorCode)errorCode);
                    BroadcastController.this.func_152820_d(String.format("IngestListCallback got failure: %s", string));
                    BroadcastController.this.func_152827_a(BroadcastState.LoggingIn);
                }
            }

            public void getStreamInfoCallback(ErrorCode errorCode, StreamInfo streamInfo) {
                if (ErrorCode.succeeded((ErrorCode)errorCode)) {
                    BroadcastController.this.streamInfo = streamInfo;
                    try {
                        if (BroadcastController.this.broadcastListener != null) {
                            BroadcastController.this.broadcastListener.func_152894_a(streamInfo);
                        }
                    }
                    catch (Exception exception) {
                        BroadcastController.this.func_152820_d(exception.toString());
                    }
                } else {
                    String string = ErrorCode.getString((ErrorCode)errorCode);
                    BroadcastController.this.func_152832_e(String.format("StreamInfoDoneCallback got failure: %s", string));
                }
            }

            public void getArchivingStateCallback(ErrorCode errorCode, ArchivingState archivingState) {
                BroadcastController.this.field_152889_y = archivingState;
                if (ErrorCode.failed((ErrorCode)errorCode)) {
                    // empty if block
                }
            }

            public void getGameNameListCallback(ErrorCode errorCode, GameInfoList gameInfoList) {
                if (ErrorCode.failed((ErrorCode)errorCode)) {
                    String string = ErrorCode.getString((ErrorCode)errorCode);
                    BroadcastController.this.func_152820_d(String.format("GameNameListCallback got failure: %s", string));
                }
                try {
                    if (BroadcastController.this.broadcastListener != null) {
                        BroadcastController.this.broadcastListener.func_152898_a(errorCode, gameInfoList == null ? new GameInfo[]{} : gameInfoList.list);
                    }
                }
                catch (Exception exception) {
                    BroadcastController.this.func_152820_d(exception.toString());
                }
            }

            public void setStreamInfoCallback(ErrorCode errorCode) {
                if (ErrorCode.failed((ErrorCode)errorCode)) {
                    String string = ErrorCode.getString((ErrorCode)errorCode);
                    BroadcastController.this.func_152832_e(String.format("SetStreamInfoCallback got failure: %s", string));
                }
            }

            public void requestAuthTokenCallback(ErrorCode errorCode, AuthToken authToken) {
                if (ErrorCode.succeeded((ErrorCode)errorCode)) {
                    BroadcastController.this.authenticationToken = authToken;
                    BroadcastController.this.func_152827_a(BroadcastState.Authenticated);
                } else {
                    BroadcastController.this.authenticationToken.data = "";
                    BroadcastController.this.func_152827_a(BroadcastState.Initialized);
                    String string = ErrorCode.getString((ErrorCode)errorCode);
                    BroadcastController.this.func_152820_d(String.format("RequestAuthTokenDoneCallback got failure: %s", string));
                }
                try {
                    if (BroadcastController.this.broadcastListener != null) {
                        BroadcastController.this.broadcastListener.func_152900_a(errorCode, authToken);
                    }
                }
                catch (Exception exception) {
                    BroadcastController.this.func_152820_d(exception.toString());
                }
            }

            public void bufferUnlockCallback(long l) {
                FrameBuffer frameBuffer = FrameBuffer.lookupBuffer((long)l);
                BroadcastController.this.field_152875_k.add(frameBuffer);
            }

            public void loginCallback(ErrorCode errorCode, ChannelInfo channelInfo) {
                if (ErrorCode.succeeded((ErrorCode)errorCode)) {
                    BroadcastController.this.channelInfo = channelInfo;
                    BroadcastController.this.func_152827_a(BroadcastState.LoggedIn);
                    BroadcastController.this.field_152877_m = true;
                } else {
                    BroadcastController.this.func_152827_a(BroadcastState.Initialized);
                    BroadcastController.this.field_152877_m = false;
                    String string = ErrorCode.getString((ErrorCode)errorCode);
                    BroadcastController.this.func_152820_d(String.format("LoginCallback got failure: %s", string));
                }
                try {
                    if (BroadcastController.this.broadcastListener != null) {
                        BroadcastController.this.broadcastListener.func_152897_a(errorCode);
                    }
                }
                catch (Exception exception) {
                    BroadcastController.this.func_152820_d(exception.toString());
                }
            }

            public void stopCallback(ErrorCode errorCode) {
                if (ErrorCode.succeeded((ErrorCode)errorCode)) {
                    BroadcastController.this.videoParamaters = null;
                    BroadcastController.this.audioParamaters = null;
                    BroadcastController.this.func_152831_M();
                    try {
                        if (BroadcastController.this.broadcastListener != null) {
                            BroadcastController.this.broadcastListener.func_152901_c();
                        }
                    }
                    catch (Exception exception) {
                        BroadcastController.this.func_152820_d(exception.toString());
                    }
                    if (BroadcastController.this.field_152877_m) {
                        BroadcastController.this.func_152827_a(BroadcastState.ReadyToBroadcast);
                    } else {
                        BroadcastController.this.func_152827_a(BroadcastState.Initialized);
                    }
                } else {
                    BroadcastController.this.func_152827_a(BroadcastState.ReadyToBroadcast);
                    String string = ErrorCode.getString((ErrorCode)errorCode);
                    BroadcastController.this.func_152820_d(String.format("stopCallback got failure: %s", string));
                }
            }
        };
        this.field_177949_C = new IStatCallbacks(){

            public void statCallback(StatType statType, long l) {
            }
        };
        this.field_152872_h = Core.getInstance();
        if (Core.getInstance() == null) {
            this.field_152872_h = new Core((CoreAPI)new StandardCoreAPI());
        }
        this.field_152873_i = new Stream((StreamAPI)new DesktopStreamAPI());
    }

    public void func_152824_a(IngestServer ingestServer) {
        this.field_152884_t = ingestServer;
    }

    public void func_152841_a(BroadcastListener broadcastListener) {
        this.broadcastListener = broadcastListener;
    }

    public void func_152821_H() {
        if (this.field_152873_i != null && this.field_152876_l) {
            ErrorCode errorCode = this.field_152873_i.pollTasks();
            this.func_152853_a(errorCode);
            if (this.isIngestTesting()) {
                this.field_152860_A.func_153041_j();
                if (this.field_152860_A.func_153032_e()) {
                    this.field_152860_A = null;
                    this.func_152827_a(BroadcastState.ReadyToBroadcast);
                }
            }
            switch (this.broadcastState) {
                case Authenticated: {
                    this.func_152827_a(BroadcastState.LoggingIn);
                    errorCode = this.field_152873_i.login(this.authenticationToken);
                    if (!ErrorCode.failed((ErrorCode)errorCode)) break;
                    String string = ErrorCode.getString((ErrorCode)errorCode);
                    this.func_152820_d(String.format("Error in TTV_Login: %s\n", string));
                    break;
                }
                case LoggedIn: {
                    this.func_152827_a(BroadcastState.FindingIngestServer);
                    errorCode = this.field_152873_i.getIngestServers(this.authenticationToken);
                    if (!ErrorCode.failed((ErrorCode)errorCode)) break;
                    this.func_152827_a(BroadcastState.LoggedIn);
                    String string = ErrorCode.getString((ErrorCode)errorCode);
                    this.func_152820_d(String.format("Error in TTV_GetIngestServers: %s\n", string));
                    break;
                }
                case ReceivedIngestServers: {
                    String string;
                    this.func_152827_a(BroadcastState.ReadyToBroadcast);
                    errorCode = this.field_152873_i.getUserInfo(this.authenticationToken);
                    if (ErrorCode.failed((ErrorCode)errorCode)) {
                        string = ErrorCode.getString((ErrorCode)errorCode);
                        this.func_152820_d(String.format("Error in TTV_GetUserInfo: %s\n", string));
                    }
                    this.func_152835_I();
                    errorCode = this.field_152873_i.getArchivingState(this.authenticationToken);
                    if (!ErrorCode.failed((ErrorCode)errorCode)) break;
                    string = ErrorCode.getString((ErrorCode)errorCode);
                    this.func_152820_d(String.format("Error in TTV_GetArchivingState: %s\n", string));
                }
                default: {
                    break;
                }
                case Broadcasting: 
                case Paused: {
                    this.func_152835_I();
                }
            }
        }
    }

    public boolean func_152849_q() {
        return this.field_152877_m;
    }

    public boolean func_152817_A() {
        if (this.field_152876_l) {
            return false;
        }
        this.field_152873_i.setStreamCallbacks(this.field_177948_B);
        ErrorCode errorCode = this.field_152872_h.initialize(this.field_152868_d, System.getProperty("java.library.path"));
        if (!this.func_152853_a(errorCode)) {
            this.field_152873_i.setStreamCallbacks(null);
            this.errorCode = errorCode;
            return false;
        }
        errorCode = this.field_152872_h.setTraceLevel(MessageLevel.TTV_ML_ERROR);
        if (!this.func_152853_a(errorCode)) {
            this.field_152873_i.setStreamCallbacks(null);
            this.field_152872_h.shutdown();
            this.errorCode = errorCode;
            return false;
        }
        if (ErrorCode.succeeded((ErrorCode)errorCode)) {
            this.field_152876_l = true;
            this.func_152827_a(BroadcastState.Initialized);
            return true;
        }
        this.errorCode = errorCode;
        this.field_152872_h.shutdown();
        return false;
    }

    public IngestServerTester func_152838_J() {
        if (this.isReadyToBroadcast() && this.ingestList != null) {
            if (this.isIngestTesting()) {
                return null;
            }
            this.field_152860_A = new IngestServerTester(this.field_152873_i, this.ingestList);
            this.field_152860_A.func_176004_j();
            this.func_152827_a(BroadcastState.IngestTesting);
            return this.field_152860_A;
        }
        return null;
    }

    public boolean func_152858_b() {
        return this.field_152876_l;
    }

    public boolean func_152836_a(VideoParams videoParams) {
        if (videoParams != null && this.isReadyToBroadcast()) {
            this.videoParamaters = videoParams.clone();
            this.audioParamaters = new AudioParams();
            this.audioParamaters.enableMicCapture = this.audioParamaters.audioEnabled = this.field_152871_g && this.func_152848_y();
            this.audioParamaters.enablePlaybackCapture = this.audioParamaters.audioEnabled;
            this.audioParamaters.enablePassthroughAudio = false;
            if (!this.func_152823_L()) {
                this.videoParamaters = null;
                this.audioParamaters = null;
                return false;
            }
            ErrorCode errorCode = this.field_152873_i.start(videoParams, this.audioParamaters, this.field_152884_t, StartFlags.None, true);
            if (ErrorCode.failed((ErrorCode)errorCode)) {
                this.func_152831_M();
                String string = ErrorCode.getString((ErrorCode)errorCode);
                this.func_152820_d(String.format("Error while starting to broadcast: %s", string));
                this.videoParamaters = null;
                this.audioParamaters = null;
                return false;
            }
            this.func_152827_a(BroadcastState.Starting);
            return true;
        }
        return false;
    }

    public ErrorCode submitStreamFrame(FrameBuffer frameBuffer) {
        if (this.isBroadcastPaused()) {
            this.func_152854_G();
        } else if (!this.isBroadcasting()) {
            return ErrorCode.TTV_EC_STREAM_NOT_STARTED;
        }
        ErrorCode errorCode = this.field_152873_i.submitVideoFrame(frameBuffer);
        if (errorCode != ErrorCode.TTV_EC_SUCCESS) {
            String string = ErrorCode.getString((ErrorCode)errorCode);
            if (ErrorCode.succeeded((ErrorCode)errorCode)) {
                this.func_152832_e(String.format("Warning in SubmitTexturePointer: %s\n", string));
            } else {
                this.func_152820_d(String.format("Error in SubmitTexturePointer: %s\n", string));
                this.stopBroadcasting();
            }
            if (this.broadcastListener != null) {
                this.broadcastListener.func_152893_b(errorCode);
            }
        }
        return errorCode;
    }

    public void func_152842_a(String string) {
        this.field_152868_d = string;
    }

    public boolean isBroadcastPaused() {
        return this.broadcastState == BroadcastState.Paused;
    }

    public void setPlaybackDeviceVolume(float f) {
        this.field_152873_i.setVolume(AudioDeviceType.TTV_PLAYBACK_DEVICE, f);
    }

    public boolean func_152845_C() {
        if (this.isIngestTesting()) {
            return false;
        }
        if (this.isBroadcasting()) {
            this.field_152873_i.stop(false);
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
            catch (Exception exception) {
                this.func_152820_d(exception.toString());
            }
        }
        this.func_152827_a(BroadcastState.Initialized);
        return true;
    }

    public static enum BroadcastState {
        Uninitialized,
        Initialized,
        Authenticating,
        Authenticated,
        LoggingIn,
        LoggedIn,
        FindingIngestServer,
        ReceivedIngestServers,
        ReadyToBroadcast,
        Starting,
        Broadcasting,
        Stopping,
        Paused,
        IngestTesting;

    }

    public static interface BroadcastListener {
        public void func_152895_a();

        public void func_152897_a(ErrorCode var1);

        public void func_152900_a(ErrorCode var1, AuthToken var2);

        public void func_152898_a(ErrorCode var1, GameInfo[] var2);

        public void func_152891_a(BroadcastState var1);

        public void func_152901_c();

        public void func_152892_c(ErrorCode var1);

        public void func_152896_a(IngestList var1);

        public void func_152894_a(StreamInfo var1);

        public void func_152899_b();

        public void func_152893_b(ErrorCode var1);
    }
}

