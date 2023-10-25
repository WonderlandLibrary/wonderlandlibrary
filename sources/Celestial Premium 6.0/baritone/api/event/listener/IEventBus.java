/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.event.listener;

import baritone.api.event.listener.IGameEventListener;

public interface IEventBus
extends IGameEventListener {
    public void registerEventListener(IGameEventListener var1);
}

