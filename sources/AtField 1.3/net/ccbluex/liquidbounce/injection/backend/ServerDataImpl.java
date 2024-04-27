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
    public String getServerIP() {
        return this.wrapped.field_78845_b;
    }

    @Override
    public String getServerName() {
        return this.wrapped.field_78847_a;
    }

    public ServerDataImpl(ServerData serverData) {
        this.wrapped = serverData;
    }

    @Override
    public String getGameVersion() {
        return this.wrapped.field_82822_g;
    }

    public final ServerData getWrapped() {
        return this.wrapped;
    }

    @Override
    public String getPopulationInfo() {
        return this.wrapped.field_78846_c;
    }

    @Override
    public long getPingToServer() {
        return this.wrapped.field_78844_e;
    }

    @Override
    public int getVersion() {
        return this.wrapped.field_82821_f;
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof ServerDataImpl && ((ServerDataImpl)object).wrapped.equals(this.wrapped);
    }

    @Override
    public String getServerMOTD() {
        return this.wrapped.field_78843_d;
    }
}

