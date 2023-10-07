/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.event;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\b\u0086\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\f\u001a\u00020\u0005H\u00c6\u0003J\u001d\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u00d6\u0003J\t\u0010\u0012\u001a\u00020\u0005H\u00d6\u0001J\t\u0010\u0013\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0014"}, d2={"Lnet/ccbluex/liquidbounce/event/ConnectingEvent;", "Lnet/ccbluex/liquidbounce/event/Event;", "ip", "", "port", "", "(Ljava/lang/String;I)V", "getIp", "()Ljava/lang/String;", "getPort", "()I", "component1", "component2", "copy", "equals", "", "other", "", "hashCode", "toString", "KyinoClient"})
public final class ConnectingEvent
extends Event {
    @NotNull
    private final String ip;
    private final int port;

    @NotNull
    public final String getIp() {
        return this.ip;
    }

    public final int getPort() {
        return this.port;
    }

    public ConnectingEvent(@NotNull String ip, int port) {
        Intrinsics.checkParameterIsNotNull(ip, "ip");
        this.ip = ip;
        this.port = port;
    }

    @NotNull
    public final String component1() {
        return this.ip;
    }

    public final int component2() {
        return this.port;
    }

    @NotNull
    public final ConnectingEvent copy(@NotNull String ip, int port) {
        Intrinsics.checkParameterIsNotNull(ip, "ip");
        return new ConnectingEvent(ip, port);
    }

    public static /* synthetic */ ConnectingEvent copy$default(ConnectingEvent connectingEvent, String string, int n, int n2, Object object) {
        if ((n2 & 1) != 0) {
            string = connectingEvent.ip;
        }
        if ((n2 & 2) != 0) {
            n = connectingEvent.port;
        }
        return connectingEvent.copy(string, n);
    }

    @NotNull
    public String toString() {
        return "ConnectingEvent(ip=" + this.ip + ", port=" + this.port + ")";
    }

    public int hashCode() {
        String string = this.ip;
        return (string != null ? string.hashCode() : 0) * 31 + Integer.hashCode(this.port);
    }

    public boolean equals(@Nullable Object object) {
        block3: {
            block2: {
                if (this == object) break block2;
                if (!(object instanceof ConnectingEvent)) break block3;
                ConnectingEvent connectingEvent = (ConnectingEvent)object;
                if (!Intrinsics.areEqual(this.ip, connectingEvent.ip) || this.port != connectingEvent.port) break block3;
            }
            return true;
        }
        return false;
    }
}

