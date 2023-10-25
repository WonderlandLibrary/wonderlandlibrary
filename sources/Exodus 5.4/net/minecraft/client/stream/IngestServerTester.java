/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  tv.twitch.AuthToken
 *  tv.twitch.ErrorCode
 *  tv.twitch.broadcast.ArchivingState
 *  tv.twitch.broadcast.AudioParams
 *  tv.twitch.broadcast.ChannelInfo
 *  tv.twitch.broadcast.EncodingCpuUsage
 *  tv.twitch.broadcast.FrameBuffer
 *  tv.twitch.broadcast.GameInfoList
 *  tv.twitch.broadcast.IStatCallbacks
 *  tv.twitch.broadcast.IStreamCallbacks
 *  tv.twitch.broadcast.IngestList
 *  tv.twitch.broadcast.IngestServer
 *  tv.twitch.broadcast.PixelFormat
 *  tv.twitch.broadcast.RTMPState
 *  tv.twitch.broadcast.StartFlags
 *  tv.twitch.broadcast.StatType
 *  tv.twitch.broadcast.Stream
 *  tv.twitch.broadcast.StreamInfo
 *  tv.twitch.broadcast.UserInfo
 *  tv.twitch.broadcast.VideoParams
 */
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
    protected boolean field_176008_y = false;
    protected int field_153063_u = 0;
    protected RTMPState field_153051_i;
    protected AudioParams audioParameters = null;
    protected Stream field_153045_c = null;
    protected IngestServer field_153059_q = null;
    protected float field_153065_w = 0.0f;
    protected IngestTestState field_153047_e = IngestTestState.Uninitalized;
    protected IStatCallbacks field_176006_B;
    protected long field_153064_v = 0L;
    protected IStreamCallbacks field_153057_o = null;
    protected long field_153049_g = 2000L;
    protected long field_153050_h = 0L;
    protected IStatCallbacks field_153058_p = null;
    protected int field_153062_t = -1;
    protected long field_153048_f = 8000L;
    protected IngestTestListener field_153044_b = null;
    protected long field_153054_l = 0L;
    protected IStreamCallbacks field_176005_A;
    protected IngestList field_153046_d = null;
    protected float field_153066_x = 0.0f;
    protected boolean field_176007_z = false;
    protected List<FrameBuffer> field_153055_m = null;
    protected boolean field_176009_x = false;
    protected VideoParams field_153052_j = null;
    protected boolean field_153056_n = false;
    protected boolean field_153061_s = false;
    protected boolean field_153060_r = false;

    public void func_153039_l() {
        if (!this.func_153032_e() && !this.field_153060_r) {
            this.field_153060_r = true;
            if (this.field_153059_q != null) {
                this.field_153059_q.bitrateKbps = 0.0f;
            }
        }
    }

    protected void func_153038_n() {
        float f = this.func_153037_m();
        switch (this.field_153047_e) {
            case Uninitalized: 
            case Starting: 
            case ConnectingToServer: 
            case Finished: 
            case Cancelled: 
            case Failed: {
                this.field_153066_x = 0.0f;
                break;
            }
            case DoneTestingServer: {
                this.field_153066_x = 1.0f;
                break;
            }
            default: {
                this.field_153066_x = f / (float)this.field_153048_f;
            }
        }
        switch (this.field_153047_e) {
            case Finished: 
            case Cancelled: 
            case Failed: {
                this.field_153065_w = 1.0f;
                break;
            }
            default: {
                this.field_153065_w = (float)this.field_153062_t / (float)this.field_153046_d.getServers().length;
                this.field_153065_w += this.field_153066_x / (float)this.field_153046_d.getServers().length;
            }
        }
    }

    protected boolean func_153029_c(IngestServer ingestServer) {
        if (!this.field_153061_s && !this.field_153060_r && this.func_153037_m() < this.field_153048_f) {
            if (!this.field_176008_y && !this.field_176007_z) {
                ErrorCode errorCode = this.field_153045_c.submitVideoFrame(this.field_153055_m.get(this.field_153063_u));
                if (ErrorCode.failed((ErrorCode)errorCode)) {
                    this.field_153056_n = false;
                    this.func_153034_a(IngestTestState.DoneTestingServer);
                    return false;
                }
                this.field_153063_u = (this.field_153063_u + 1) % this.field_153055_m.size();
                this.field_153045_c.pollStats();
                if (this.field_153051_i == RTMPState.SendVideo) {
                    this.func_153034_a(IngestTestState.TestingServer);
                    long l = this.func_153037_m();
                    if (l > 0L && this.field_153050_h > this.field_153064_v) {
                        ingestServer.bitrateKbps = (float)(this.field_153050_h * 8L) / (float)this.func_153037_m();
                        this.field_153064_v = this.field_153050_h;
                    }
                }
                return true;
            }
            return true;
        }
        this.func_153034_a(IngestTestState.DoneTestingServer);
        return true;
    }

    public void func_176004_j() {
        if (this.field_153047_e == IngestTestState.Uninitalized) {
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
            int n = 3;
            int n2 = 0;
            while (n2 < n) {
                FrameBuffer frameBuffer = this.field_153045_c.allocateFrameBuffer(this.field_153052_j.outputWidth * this.field_153052_j.outputHeight * 4);
                if (!frameBuffer.getIsValid()) {
                    this.func_153031_o();
                    this.func_153034_a(IngestTestState.Failed);
                    return;
                }
                this.field_153055_m.add(frameBuffer);
                this.field_153045_c.randomizeFrameBuffer(frameBuffer);
                ++n2;
            }
            this.func_153034_a(IngestTestState.Starting);
            this.field_153054_l = System.currentTimeMillis();
        }
    }

    protected long func_153037_m() {
        return System.currentTimeMillis() - this.field_153054_l;
    }

    protected void func_153031_o() {
        this.field_153059_q = null;
        if (this.field_153055_m != null) {
            int n = 0;
            while (n < this.field_153055_m.size()) {
                this.field_153055_m.get(n).free();
                ++n;
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

    public float func_153030_h() {
        return this.field_153066_x;
    }

    public IngestServerTester(Stream stream, IngestList ingestList) {
        this.field_153051_i = RTMPState.Invalid;
        this.field_176005_A = new IStreamCallbacks(){

            public void bufferUnlockCallback(long l) {
            }

            public void setStreamInfoCallback(ErrorCode errorCode) {
            }

            public void getArchivingStateCallback(ErrorCode errorCode, ArchivingState archivingState) {
            }

            public void getStreamInfoCallback(ErrorCode errorCode, StreamInfo streamInfo) {
            }

            public void sendEndSpanMetaDataCallback(ErrorCode errorCode) {
            }

            public void runCommercialCallback(ErrorCode errorCode) {
            }

            public void sendStartSpanMetaDataCallback(ErrorCode errorCode) {
            }

            public void getGameNameListCallback(ErrorCode errorCode, GameInfoList gameInfoList) {
            }

            public void loginCallback(ErrorCode errorCode, ChannelInfo channelInfo) {
            }

            public void sendActionMetaDataCallback(ErrorCode errorCode) {
            }

            public void getIngestServersCallback(ErrorCode errorCode, IngestList ingestList) {
            }

            public void requestAuthTokenCallback(ErrorCode errorCode, AuthToken authToken) {
            }

            public void getUserInfoCallback(ErrorCode errorCode, UserInfo userInfo) {
            }

            public void stopCallback(ErrorCode errorCode) {
                if (ErrorCode.failed((ErrorCode)errorCode)) {
                    System.out.println("IngestTester.stopCallback failed to stop - " + IngestServerTester.this.field_153059_q.serverName + ": " + errorCode.toString());
                }
                IngestServerTester.this.field_176007_z = false;
                IngestServerTester.this.field_176009_x = false;
                IngestServerTester.this.func_153034_a(IngestTestState.DoneTestingServer);
                IngestServerTester.this.field_153059_q = null;
                if (IngestServerTester.this.field_153060_r) {
                    IngestServerTester.this.func_153034_a(IngestTestState.Cancelling);
                }
            }

            public void startCallback(ErrorCode errorCode) {
                IngestServerTester.this.field_176008_y = false;
                if (ErrorCode.succeeded((ErrorCode)errorCode)) {
                    IngestServerTester.this.field_176009_x = true;
                    IngestServerTester.this.field_153054_l = System.currentTimeMillis();
                    IngestServerTester.this.func_153034_a(IngestTestState.ConnectingToServer);
                } else {
                    IngestServerTester.this.field_153056_n = false;
                    IngestServerTester.this.func_153034_a(IngestTestState.DoneTestingServer);
                }
            }
        };
        this.field_176006_B = new IStatCallbacks(){

            public void statCallback(StatType statType, long l) {
                switch (statType) {
                    case TTV_ST_RTMPSTATE: {
                        IngestServerTester.this.field_153051_i = RTMPState.lookupValue((int)((int)l));
                        break;
                    }
                    case TTV_ST_RTMPDATASENT: {
                        IngestServerTester.this.field_153050_h = l;
                    }
                }
            }
        };
        this.field_153045_c = stream;
        this.field_153046_d = ingestList;
    }

    public boolean func_153032_e() {
        return this.field_153047_e == IngestTestState.Finished || this.field_153047_e == IngestTestState.Cancelled || this.field_153047_e == IngestTestState.Failed;
    }

    protected boolean func_153036_a(IngestServer ingestServer) {
        this.field_153056_n = true;
        this.field_153050_h = 0L;
        this.field_153051_i = RTMPState.Idle;
        this.field_153059_q = ingestServer;
        this.field_176008_y = true;
        this.func_153034_a(IngestTestState.ConnectingToServer);
        ErrorCode errorCode = this.field_153045_c.start(this.field_153052_j, this.audioParameters, ingestServer, StartFlags.TTV_Start_BandwidthTest, true);
        if (ErrorCode.failed((ErrorCode)errorCode)) {
            this.field_176008_y = false;
            this.field_153056_n = false;
            this.func_153034_a(IngestTestState.DoneTestingServer);
            return false;
        }
        this.field_153064_v = this.field_153050_h;
        ingestServer.bitrateKbps = 0.0f;
        this.field_153063_u = 0;
        return true;
    }

    protected void func_153034_a(IngestTestState ingestTestState) {
        if (ingestTestState != this.field_153047_e) {
            this.field_153047_e = ingestTestState;
            if (this.field_153044_b != null) {
                this.field_153044_b.func_152907_a(this, ingestTestState);
            }
        }
    }

    public void func_153041_j() {
        if (!(this.func_153032_e() || this.field_153047_e == IngestTestState.Uninitalized || this.field_176008_y || this.field_176007_z)) {
            switch (this.field_153047_e) {
                case Starting: 
                case DoneTestingServer: {
                    if (this.field_153059_q != null) {
                        if (this.field_153061_s || !this.field_153056_n) {
                            this.field_153059_q.bitrateKbps = 0.0f;
                        }
                        this.func_153035_b(this.field_153059_q);
                        break;
                    }
                    this.field_153054_l = 0L;
                    this.field_153061_s = false;
                    this.field_153056_n = true;
                    if (this.field_153047_e != IngestTestState.Starting) {
                        ++this.field_153062_t;
                    }
                    if (this.field_153062_t < this.field_153046_d.getServers().length) {
                        this.field_153059_q = this.field_153046_d.getServers()[this.field_153062_t];
                        this.func_153036_a(this.field_153059_q);
                        break;
                    }
                    this.func_153034_a(IngestTestState.Finished);
                    break;
                }
                case ConnectingToServer: 
                case TestingServer: {
                    this.func_153029_c(this.field_153059_q);
                    break;
                }
                case Cancelling: {
                    this.func_153034_a(IngestTestState.Cancelled);
                }
            }
            this.func_153038_n();
            if (this.field_153047_e == IngestTestState.Cancelled || this.field_153047_e == IngestTestState.Finished) {
                this.func_153031_o();
            }
        }
    }

    protected void func_153035_b(IngestServer ingestServer) {
        if (this.field_176008_y) {
            this.field_153061_s = true;
        } else if (this.field_176009_x) {
            this.field_176007_z = true;
            ErrorCode errorCode = this.field_153045_c.stop(true);
            if (ErrorCode.failed((ErrorCode)errorCode)) {
                this.field_176005_A.stopCallback(ErrorCode.TTV_EC_SUCCESS);
                System.out.println("Stop failed: " + errorCode.toString());
            }
            this.field_153045_c.pollStats();
        } else {
            this.field_176005_A.stopCallback(ErrorCode.TTV_EC_SUCCESS);
        }
    }

    public IngestServer func_153040_c() {
        return this.field_153059_q;
    }

    public void func_153042_a(IngestTestListener ingestTestListener) {
        this.field_153044_b = ingestTestListener;
    }

    public int func_153028_p() {
        return this.field_153062_t;
    }

    public static enum IngestTestState {
        Uninitalized,
        Starting,
        ConnectingToServer,
        TestingServer,
        DoneTestingServer,
        Finished,
        Cancelling,
        Cancelled,
        Failed;

    }

    public static interface IngestTestListener {
        public void func_152907_a(IngestServerTester var1, IngestTestState var2);
    }
}

