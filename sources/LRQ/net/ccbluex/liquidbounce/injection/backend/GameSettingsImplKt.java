/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.settings.GameSettings
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.settings.IGameSettings;
import net.ccbluex.liquidbounce.injection.backend.GameSettingsImpl;
import net.minecraft.client.settings.GameSettings;

public final class GameSettingsImplKt {
    public static final GameSettings unwrap(IGameSettings $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((GameSettingsImpl)$this$unwrap).getWrapped();
    }

    public static final IGameSettings wrap(GameSettings $this$wrap) {
        int $i$f$wrap = 0;
        return new GameSettingsImpl($this$wrap);
    }
}

