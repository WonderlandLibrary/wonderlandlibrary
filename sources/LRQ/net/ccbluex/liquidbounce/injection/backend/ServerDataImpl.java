/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.multiplayer.ServerData
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IServerData;
import net.minecraft.client.multiplayer.ServerData;
import org.jetbrains.annotations.Nullable;

public final class ServerDataImpl
implements IServerData {
    private final ServerData wrapped;

    @Override
    public long getPingToServer() {
        return this.wrapped.field_78844_e;
    }

    @Override
    public int getVersion() {
        return this.wrapped.field_82821_f;
    }

    @Override
    public String getGameVersion() {
        return this.wrapped.field_82822_g;
    }

    @Override
    public String getServerMOTD() {
        return this.wrapped.field_78843_d;
    }

    @Override
    public String getPopulationInfo() {
        return this.wrapped.field_78846_c;
    }

    @Override
    public String getServerName() {
        return this.wrapped.field_78847_a;
    }

    @Override
    public String getServerIP() {
        return this.wrapped.field_78845_b;
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof ServerDataImpl && ((ServerDataImpl)other).wrapped.equals(this.wrapped);
    }

    public final ServerData getWrapped() {
        return this.wrapped;
    }

    public ServerDataImpl(ServerData wrapped) {
        this.wrapped = wrapped;
    }
}

