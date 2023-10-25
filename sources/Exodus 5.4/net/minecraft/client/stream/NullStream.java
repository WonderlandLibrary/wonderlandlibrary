/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  tv.twitch.ErrorCode
 *  tv.twitch.broadcast.IngestServer
 *  tv.twitch.chat.ChatUserInfo
 */
package net.minecraft.client.stream;

import net.minecraft.client.stream.IStream;
import net.minecraft.client.stream.IngestServerTester;
import net.minecraft.client.stream.Metadata;
import tv.twitch.ErrorCode;
import tv.twitch.broadcast.IngestServer;
import tv.twitch.chat.ChatUserInfo;

public class NullStream
implements IStream {
    private final Throwable field_152938_a;

    @Override
    public void updateStreamVolume() {
    }

    @Override
    public void func_152911_a(Metadata metadata, long l) {
    }

    @Override
    public IngestServer[] func_152925_v() {
        return new IngestServer[0];
    }

    @Override
    public void func_152917_b(String string) {
    }

    @Override
    public ChatUserInfo func_152926_a(String string) {
        return null;
    }

    public NullStream(Throwable throwable) {
        this.field_152938_a = throwable;
    }

    @Override
    public IStream.AuthFailureReason func_152918_H() {
        return IStream.AuthFailureReason.ERROR;
    }

    @Override
    public int func_152920_A() {
        return 0;
    }

    @Override
    public ErrorCode func_152912_E() {
        return null;
    }

    @Override
    public IngestServerTester func_152932_y() {
        return null;
    }

    public Throwable func_152937_a() {
        return this.field_152938_a;
    }

    @Override
    public String func_152921_C() {
        return null;
    }

    @Override
    public boolean func_152929_G() {
        return false;
    }

    @Override
    public void func_176026_a(Metadata metadata, long l, long l2) {
    }

    @Override
    public void stopBroadcasting() {
    }

    @Override
    public boolean func_152936_l() {
        return false;
    }

    @Override
    public boolean isReadyToBroadcast() {
        return false;
    }

    @Override
    public boolean func_152928_D() {
        return false;
    }

    @Override
    public void unpause() {
    }

    @Override
    public void func_152930_t() {
    }

    @Override
    public void requestCommercial() {
    }

    @Override
    public boolean isBroadcasting() {
        return false;
    }

    @Override
    public boolean func_152908_z() {
        return false;
    }

    @Override
    public void shutdownStream() {
    }

    @Override
    public void func_152909_x() {
    }

    @Override
    public void muteMicrophone(boolean bl) {
    }

    @Override
    public void func_152922_k() {
    }

    @Override
    public void func_152935_j() {
    }

    @Override
    public void pause() {
    }

    @Override
    public boolean isPaused() {
        return false;
    }

    @Override
    public boolean func_152913_F() {
        return false;
    }

    @Override
    public boolean func_152927_B() {
        return false;
    }
}

