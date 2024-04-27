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
    public static final IGameSettings wrap(GameSettings gameSettings) {
        boolean bl = false;
        return new GameSettingsImpl(gameSettings);
    }

    public static final GameSettings unwrap(IGameSettings iGameSettings) {
        boolean bl = false;
        return ((GameSettingsImpl)iGameSettings).getWrapped();
    }
}

