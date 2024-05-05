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
    public static final Session unwrap(ISession $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((SessionImpl)$this$unwrap).getWrapped();
    }

    public static final ISession wrap(Session $this$wrap) {
        int $i$f$wrap = 0;
        return new SessionImpl($this$wrap);
    }
}

