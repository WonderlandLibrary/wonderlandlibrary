// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.stream;

import tv.twitch.ErrorCode;
import tv.twitch.chat.ChatUserInfo;
import tv.twitch.broadcast.IngestServer;

public interface IStream
{
    void shutdownStream();
    
    void func_152935_j();
    
    void func_152922_k();
    
    boolean func_152936_l();
    
    boolean func_152924_m();
    
    boolean func_152934_n();
    
    void func_152911_a(final Metadata p0, final long p1);
    
    void func_176026_a(final Metadata p0, final long p1, final long p2);
    
    boolean isPaused();
    
    void func_152931_p();
    
    void func_152916_q();
    
    void func_152933_r();
    
    void func_152915_s();
    
    void func_152930_t();
    
    void func_152914_u();
    
    IngestServer[] func_152925_v();
    
    void func_152909_x();
    
    IngestServerTester func_152932_y();
    
    boolean func_152908_z();
    
    int func_152920_A();
    
    boolean func_152927_B();
    
    String func_152921_C();
    
    ChatUserInfo func_152926_a(final String p0);
    
    void func_152917_b(final String p0);
    
    boolean func_152928_D();
    
    ErrorCode func_152912_E();
    
    boolean func_152913_F();
    
    void func_152910_a(final boolean p0);
    
    boolean func_152929_G();
    
    AuthFailureReason func_152918_H();
    
    public enum AuthFailureReason
    {
        ERROR("ERROR", 0), 
        INVALID_TOKEN("INVALID_TOKEN", 1);
        
        private static final AuthFailureReason[] $VALUES;
        private static final String __OBFID = "CL_00001813";
        
        private AuthFailureReason(final String p_i1014_1_, final int p_i1014_2_) {
        }
        
        static {
            $VALUES = new AuthFailureReason[] { AuthFailureReason.ERROR, AuthFailureReason.INVALID_TOKEN };
        }
    }
}
