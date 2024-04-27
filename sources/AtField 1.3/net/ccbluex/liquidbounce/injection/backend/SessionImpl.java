/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  net.minecraft.util.Session
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import com.mojang.authlib.GameProfile;
import net.ccbluex.liquidbounce.api.minecraft.util.ISession;
import net.minecraft.util.Session;
import org.jetbrains.annotations.Nullable;

public final class SessionImpl
implements ISession {
    private final Session wrapped;

    @Override
    public String getUsername() {
        return this.wrapped.func_111285_a();
    }

    @Override
    public String getToken() {
        return this.wrapped.func_148254_d();
    }

    @Override
    public String getSessionType() {
        return this.wrapped.field_152429_d.name();
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof SessionImpl && ((SessionImpl)object).wrapped.equals(this.wrapped);
    }

    public SessionImpl(Session session) {
        this.wrapped = session;
    }

    @Override
    public String getPlayerId() {
        return this.wrapped.func_148255_b();
    }

    public final Session getWrapped() {
        return this.wrapped;
    }

    @Override
    public GameProfile getProfile() {
        return this.wrapped.func_148256_e();
    }
}

