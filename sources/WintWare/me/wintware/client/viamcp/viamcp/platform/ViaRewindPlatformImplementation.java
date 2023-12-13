/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.viamcp.viamcp.platform;

import de.gerrygames.viarewind.api.ViaRewindConfig;
import de.gerrygames.viarewind.api.ViaRewindPlatform;
import java.util.logging.Logger;

public class ViaRewindPlatformImplementation
implements ViaRewindPlatform {
    public ViaRewindPlatformImplementation() {
        this.init(new ViaRewindConfig(){

            @Override
            public ViaRewindConfig.CooldownIndicator getCooldownIndicator() {
                return ViaRewindConfig.CooldownIndicator.TITLE;
            }

            @Override
            public boolean isReplaceAdventureMode() {
                return true;
            }

            @Override
            public boolean isReplaceParticles() {
                return true;
            }
        });
    }

    @Override
    public Logger getLogger() {
        return null;
    }
}

