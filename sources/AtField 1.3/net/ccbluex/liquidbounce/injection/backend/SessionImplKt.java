/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Session
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.util.ISession;
import net.ccbluex.liquidbounce.injection.backend.SessionImpl;
import net.minecraft.util.Session;

public final class SessionImplKt {
    public static final ISession wrap(Session session) {
        boolean bl = false;
        return new SessionImpl(session);
    }

    public static final Session unwrap(ISession iSession) {
        boolean bl = false;
        return ((SessionImpl)iSession).getWrapped();
    }
}

