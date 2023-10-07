// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.stream;

import tv.twitch.broadcast.StartFlags;
import tv.twitch.broadcast.FrameBuffer;
import com.google.common.collect.Lists;
import tv.twitch.broadcast.EncodingCpuUsage;
import tv.twitch.broadcast.PixelFormat;
import tv.twitch.broadcast.StatType;
import tv.twitch.broadcast.GameInfoList;
import tv.twitch.broadcast.ArchivingState;
import tv.twitch.broadcast.StreamInfo;
import tv.twitch.broadcast.UserInfo;
import tv.twitch.broadcast.ChannelInfo;
import tv.twitch.AuthToken;
import tv.twitch.ErrorCode;
import tv.twitch.broadcast.IngestServer;
import tv.twitch.broadcast.IStatCallbacks;
import tv.twitch.broadcast.IStreamCallbacks;
import java.util.List;
import tv.twitch.broadcast.AudioParams;
import tv.twitch.broadcast.VideoParams;
import tv.twitch.broadcast.RTMPState;
import tv.twitch.broadcast.IngestList;
import tv.twitch.broadcast.Stream;

public class IngestServerTester
{
    protected IngestTestListener field_153044_b;
    protected Stream field_153045_c;
    protected IngestList field_153046_d;
    protected IngestTestState field_153047_e;
    protected long field_153048_f;
    protected long field_153049_g;
    protected long field_153050_h;
    protected RTMPState field_153051_i;
    protected VideoParams field_153052_j;
    protected AudioParams field_153053_k;
    protected long field_153054_l;
    protected List field_153055_m;
    protected boolean field_153056_n;
    protected IStreamCallbacks field_153057_o;
    protected IStatCallbacks field_153058_p;
    protected IngestServer field_153059_q;
    protected boolean field_153060_r;
    protected boolean field_153061_s;
    protected int field_153062_t;
    protected int field_153063_u;
    protected long field_153064_v;
    protected float field_153065_w;
    protected float field_153066_x;
    protected boolean field_176009_x;
    protected boolean field_176008_y;
    protected boolean field_176007_z;
    protected IStreamCallbacks field_176005_A;
    protected IStatCallbacks field_176006_B;
    private static final String __OBFID = "CL_00001816";
    
    public void func_153042_a(final IngestTestListener p_153042_1_) {
        this.field_153044_b = p_153042_1_;
    }
    
    public IngestServer func_153040_c() {
        return this.field_153059_q;
    }
    
    public int func_153028_p() {
        return this.field_153062_t;
    }
    
    public boolean func_153032_e() {
        return this.field_153047_e == IngestTestState.Finished || this.field_153047_e == IngestTestState.Cancelled || this.field_153047_e == IngestTestState.Failed;
    }
    
    public float func_153030_h() {
        return this.field_153066_x;
    }
    
    public IngestServerTester(final Stream p_i1019_1_, final IngestList p_i1019_2_) {
        this.field_153044_b = null;
        this.field_153045_c = null;
        this.field_153046_d = null;
        this.field_153047_e = IngestTestState.Uninitalized;
        this.field_153048_f = 8000L;
        this.field_153049_g = 2000L;
        this.field_153050_h = 0L;
        this.field_153051_i = RTMPState.Invalid;
        this.field_153052_j = null;
        this.field_153053_k = null;
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
        this.field_153065_w = 0.0f;
        this.field_153066_x = 0.0f;
        this.field_176009_x = false;
        this.field_176008_y = false;
        this.field_176007_z = false;
        this.field_176005_A = (IStreamCallbacks)new IStreamCallbacks() {
            private static final String __OBFID = "CL_00002368";
            
            public void requestAuthTokenCallback(final ErrorCode p_requestAuthTokenCallback_1_, final AuthToken p_requestAuthTokenCallback_2_) {
            }
            
            public void loginCallback(final ErrorCode p_loginCallback_1_, final ChannelInfo p_loginCallback_2_) {
            }
            
            public void getIngestServersCallback(final ErrorCode p_getIngestServersCallback_1_, final IngestList p_getIngestServersCallback_2_) {
            }
            
            public void getUserInfoCallback(final ErrorCode p_getUserInfoCallback_1_, final UserInfo p_getUserInfoCallback_2_) {
            }
            
            public void getStreamInfoCallback(final ErrorCode p_getStreamInfoCallback_1_, final StreamInfo p_getStreamInfoCallback_2_) {
            }
            
            public void getArchivingStateCallback(final ErrorCode p_getArchivingStateCallback_1_, final ArchivingState p_getArchivingStateCallback_2_) {
            }
            
            public void runCommercialCallback(final ErrorCode p_runCommercialCallback_1_) {
            }
            
            public void setStreamInfoCallback(final ErrorCode p_setStreamInfoCallback_1_) {
            }
            
            public void getGameNameListCallback(final ErrorCode p_getGameNameListCallback_1_, final GameInfoList p_getGameNameListCallback_2_) {
            }
            
            public void bufferUnlockCallback(final long p_bufferUnlockCallback_1_) {
            }
            
            public void startCallback(final ErrorCode p_startCallback_1_) {
                IngestServerTester.this.field_176008_y = false;
                if (ErrorCode.succeeded(p_startCallback_1_)) {
                    IngestServerTester.this.field_176009_x = true;
                    IngestServerTester.this.field_153054_l = System.currentTimeMillis();
                    IngestServerTester.this.func_153034_a(IngestTestState.ConnectingToServer);
                }
                else {
                    IngestServerTester.this.field_153056_n = false;
                    IngestServerTester.this.func_153034_a(IngestTestState.DoneTestingServer);
                }
            }
            
            public void stopCallback(final ErrorCode p_stopCallback_1_) {
                if (ErrorCode.failed(p_stopCallback_1_)) {
                    System.out.println("IngestTester.stopCallback failed to stop - " + IngestServerTester.this.field_153059_q.serverName + ": " + p_stopCallback_1_.toString());
                }
                IngestServerTester.this.field_176007_z = false;
                IngestServerTester.this.field_176009_x = false;
                IngestServerTester.this.func_153034_a(IngestTestState.DoneTestingServer);
                IngestServerTester.this.field_153059_q = null;
                if (IngestServerTester.this.field_153060_r) {
                    IngestServerTester.this.func_153034_a(IngestTestState.Cancelling);
                }
            }
            
            public void sendActionMetaDataCallback(final ErrorCode p_sendActionMetaDataCallback_1_) {
            }
            
            public void sendStartSpanMetaDataCallback(final ErrorCode p_sendStartSpanMetaDataCallback_1_) {
            }
            
            public void sendEndSpanMetaDataCallback(final ErrorCode p_sendEndSpanMetaDataCallback_1_) {
            }
        };
        this.field_176006_B = (IStatCallbacks)new IStatCallbacks() {
            private static final String __OBFID = "CL_00002367";
            
            public void statCallback(final StatType p_statCallback_1_, final long p_statCallback_2_) {
                switch (SwitchStatType.field_176003_a[p_statCallback_1_.ordinal()]) {
                    case 1: {
                        IngestServerTester.this.field_153051_i = RTMPState.lookupValue((int)p_statCallback_2_);
                        break;
                    }
                    case 2: {
                        IngestServerTester.this.field_153050_h = p_statCallback_2_;
                        break;
                    }
                }
            }
        };
        this.field_153045_c = p_i1019_1_;
        this.field_153046_d = p_i1019_2_;
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
            this.field_153053_k = new AudioParams();
            this.field_153053_k.audioEnabled = false;
            this.field_153053_k.enableMicCapture = false;
            this.field_153053_k.enablePlaybackCapture = false;
            this.field_153053_k.enablePassthroughAudio = false;
            this.field_153055_m = Lists.newArrayList();
            final byte var1 = 3;
            for (int var2 = 0; var2 < var1; ++var2) {
                final FrameBuffer var3 = this.field_153045_c.allocateFrameBuffer(this.field_153052_j.outputWidth * this.field_153052_j.outputHeight * 4);
                if (!var3.getIsValid()) {
                    this.func_153031_o();
                    this.func_153034_a(IngestTestState.Failed);
                    return;
                }
                this.field_153055_m.add(var3);
                this.field_153045_c.randomizeFrameBuffer(var3);
            }
            this.func_153034_a(IngestTestState.Starting);
            this.field_153054_l = System.currentTimeMillis();
        }
    }
    
    public void func_153041_j() {
        if (!this.func_153032_e() && this.field_153047_e != IngestTestState.Uninitalized && !this.field_176008_y && !this.field_176007_z) {
            switch (SwitchStatType.field_176002_b[this.field_153047_e.ordinal()]) {
                case 1:
                case 2: {
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
                        this.func_153036_a(this.field_153059_q = this.field_153046_d.getServers()[this.field_153062_t]);
                        break;
                    }
                    this.func_153034_a(IngestTestState.Finished);
                    break;
                }
                case 3:
                case 4: {
                    this.func_153029_c(this.field_153059_q);
                    break;
                }
                case 5: {
                    this.func_153034_a(IngestTestState.Cancelled);
                    break;
                }
            }
            this.func_153038_n();
            if (this.field_153047_e == IngestTestState.Cancelled || this.field_153047_e == IngestTestState.Finished) {
                this.func_153031_o();
            }
        }
    }
    
    public void func_153039_l() {
        if (!this.func_153032_e() && !this.field_153060_r) {
            this.field_153060_r = true;
            if (this.field_153059_q != null) {
                this.field_153059_q.bitrateKbps = 0.0f;
            }
        }
    }
    
    protected boolean func_153036_a(final IngestServer p_153036_1_) {
        this.field_153056_n = true;
        this.field_153050_h = 0L;
        this.field_153051_i = RTMPState.Idle;
        this.field_153059_q = p_153036_1_;
        this.field_176008_y = true;
        this.func_153034_a(IngestTestState.ConnectingToServer);
        final ErrorCode var2 = this.field_153045_c.start(this.field_153052_j, this.field_153053_k, p_153036_1_, StartFlags.TTV_Start_BandwidthTest, true);
        if (ErrorCode.failed(var2)) {
            this.field_176008_y = false;
            this.field_153056_n = false;
            this.func_153034_a(IngestTestState.DoneTestingServer);
            return false;
        }
        this.field_153064_v = this.field_153050_h;
        p_153036_1_.bitrateKbps = 0.0f;
        this.field_153063_u = 0;
        return true;
    }
    
    protected void func_153035_b(final IngestServer p_153035_1_) {
        if (this.field_176008_y) {
            this.field_153061_s = true;
        }
        else if (this.field_176009_x) {
            this.field_176007_z = true;
            final ErrorCode var2 = this.field_153045_c.stop(true);
            if (ErrorCode.failed(var2)) {
                this.field_176005_A.stopCallback(ErrorCode.TTV_EC_SUCCESS);
                System.out.println("Stop failed: " + var2.toString());
            }
            this.field_153045_c.pollStats();
        }
        else {
            this.field_176005_A.stopCallback(ErrorCode.TTV_EC_SUCCESS);
        }
    }
    
    protected long func_153037_m() {
        return System.currentTimeMillis() - this.field_153054_l;
    }
    
    protected void func_153038_n() {
        final float var1 = this.func_153037_m();
        switch (SwitchStatType.field_176002_b[this.field_153047_e.ordinal()]) {
            case 1:
            case 3:
            case 6:
            case 7:
            case 8:
            case 9: {
                this.field_153066_x = 0.0f;
                break;
            }
            case 2: {
                this.field_153066_x = 1.0f;
                break;
            }
            default: {
                this.field_153066_x = var1 / this.field_153048_f;
                break;
            }
        }
        switch (SwitchStatType.field_176002_b[this.field_153047_e.ordinal()]) {
            case 7:
            case 8:
            case 9: {
                this.field_153065_w = 1.0f;
                break;
            }
            default: {
                this.field_153065_w = this.field_153062_t / this.field_153046_d.getServers().length;
                this.field_153065_w += this.field_153066_x / this.field_153046_d.getServers().length;
                break;
            }
        }
    }
    
    protected boolean func_153029_c(final IngestServer p_153029_1_) {
        if (this.field_153061_s || this.field_153060_r || this.func_153037_m() >= this.field_153048_f) {
            this.func_153034_a(IngestTestState.DoneTestingServer);
            return true;
        }
        if (this.field_176008_y || this.field_176007_z) {
            return true;
        }
        final ErrorCode var2 = this.field_153045_c.submitVideoFrame((FrameBuffer)this.field_153055_m.get(this.field_153063_u));
        if (ErrorCode.failed(var2)) {
            this.field_153056_n = false;
            this.func_153034_a(IngestTestState.DoneTestingServer);
            return false;
        }
        this.field_153063_u = (this.field_153063_u + 1) % this.field_153055_m.size();
        this.field_153045_c.pollStats();
        if (this.field_153051_i == RTMPState.SendVideo) {
            this.func_153034_a(IngestTestState.TestingServer);
            final long var3 = this.func_153037_m();
            if (var3 > 0L && this.field_153050_h > this.field_153064_v) {
                p_153029_1_.bitrateKbps = this.field_153050_h * 8L / this.func_153037_m();
                this.field_153064_v = this.field_153050_h;
            }
        }
        return true;
    }
    
    protected void func_153031_o() {
        this.field_153059_q = null;
        if (this.field_153055_m != null) {
            for (int var1 = 0; var1 < this.field_153055_m.size(); ++var1) {
                this.field_153055_m.get(var1).free();
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
    
    protected void func_153034_a(final IngestTestState p_153034_1_) {
        if (p_153034_1_ != this.field_153047_e) {
            this.field_153047_e = p_153034_1_;
            if (this.field_153044_b != null) {
                this.field_153044_b.func_152907_a(this, p_153034_1_);
            }
        }
    }
    
    public enum IngestTestState
    {
        Uninitalized("Uninitalized", 0), 
        Starting("Starting", 1), 
        ConnectingToServer("ConnectingToServer", 2), 
        TestingServer("TestingServer", 3), 
        DoneTestingServer("DoneTestingServer", 4), 
        Finished("Finished", 5), 
        Cancelling("Cancelling", 6), 
        Cancelled("Cancelled", 7), 
        Failed("Failed", 8);
        
        private static final IngestTestState[] $VALUES;
        private static final String __OBFID = "CL_00001814";
        
        private IngestTestState(final String p_i1016_1_, final int p_i1016_2_) {
        }
        
        static {
            $VALUES = new IngestTestState[] { IngestTestState.Uninitalized, IngestTestState.Starting, IngestTestState.ConnectingToServer, IngestTestState.TestingServer, IngestTestState.DoneTestingServer, IngestTestState.Finished, IngestTestState.Cancelling, IngestTestState.Cancelled, IngestTestState.Failed };
        }
    }
    
    static final class SwitchStatType
    {
        static final int[] field_176003_a;
        static final int[] field_176002_b;
        private static final String __OBFID = "CL_00001815";
        
        static {
            field_176002_b = new int[IngestTestState.values().length];
            try {
                SwitchStatType.field_176002_b[IngestTestState.Starting.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchStatType.field_176002_b[IngestTestState.DoneTestingServer.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                SwitchStatType.field_176002_b[IngestTestState.ConnectingToServer.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                SwitchStatType.field_176002_b[IngestTestState.TestingServer.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
            try {
                SwitchStatType.field_176002_b[IngestTestState.Cancelling.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError5) {}
            try {
                SwitchStatType.field_176002_b[IngestTestState.Uninitalized.ordinal()] = 6;
            }
            catch (NoSuchFieldError noSuchFieldError6) {}
            try {
                SwitchStatType.field_176002_b[IngestTestState.Finished.ordinal()] = 7;
            }
            catch (NoSuchFieldError noSuchFieldError7) {}
            try {
                SwitchStatType.field_176002_b[IngestTestState.Cancelled.ordinal()] = 8;
            }
            catch (NoSuchFieldError noSuchFieldError8) {}
            try {
                SwitchStatType.field_176002_b[IngestTestState.Failed.ordinal()] = 9;
            }
            catch (NoSuchFieldError noSuchFieldError9) {}
            field_176003_a = new int[StatType.values().length];
            try {
                SwitchStatType.field_176003_a[StatType.TTV_ST_RTMPSTATE.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError10) {}
            try {
                SwitchStatType.field_176003_a[StatType.TTV_ST_RTMPDATASENT.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError11) {}
        }
    }
    
    public interface IngestTestListener
    {
        void func_152907_a(final IngestServerTester p0, final IngestTestState p1);
    }
}
