/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.event;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.event.Event;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\t\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0005R\u001a\u0010\u0004\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u0007\"\u0004\b\u000b\u0010\t\u00a8\u0006\f"}, d2={"Lnet/ccbluex/liquidbounce/event/ActionEvent;", "Lnet/ccbluex/liquidbounce/event/Event;", "sprinting", "", "sneaking", "(ZZ)V", "getSneaking", "()Z", "setSneaking", "(Z)V", "getSprinting", "setSprinting", "KyinoClient"})
public final class ActionEvent
extends Event {
    private boolean sprinting;
    private boolean sneaking;

    public final boolean getSprinting() {
        return this.sprinting;
    }

    public final void setSprinting(boolean bl) {
        this.sprinting = bl;
    }

    public final boolean getSneaking() {
        return this.sneaking;
    }

    public final void setSneaking(boolean bl) {
        this.sneaking = bl;
    }

    public ActionEvent(boolean sprinting, boolean sneaking) {
        this.sprinting = sprinting;
        this.sneaking = sneaking;
    }
}

