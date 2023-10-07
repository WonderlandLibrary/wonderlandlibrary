package cc.novoline.viaversion.platform;

import viaversion.viarewind.api.ViaRewindConfig;
import viaversion.viarewind.api.ViaRewindPlatform;
import viaversion.viaversion.api.Via;

import java.util.logging.Logger;

public class ViaRewindPlatformImplementation implements ViaRewindPlatform {

    public ViaRewindPlatformImplementation() {
        init(new ViaRewindConfig() {
            @Override
            public CooldownIndicator getCooldownIndicator() {
                return CooldownIndicator.TITLE;
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
        return Via.getPlatform().getLogger();
    }
}
