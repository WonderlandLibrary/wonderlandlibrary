package cc.novoline.viaversion.platform;

import net.minecraft.client.Minecraft;
import viaversion.viabackwards.ViaBackwards;
import viaversion.viabackwards.api.ViaBackwardsConfig;
import viaversion.viabackwards.api.ViaBackwardsPlatform;
import viaversion.viaversion.api.Via;

import java.io.File;
import java.util.logging.Logger;

public class ViaBackwardsPlatformImplementation implements ViaBackwardsPlatform {

    public ViaBackwardsPlatformImplementation() {
        ViaBackwards.init(this, new ViaBackwardsConfig() {
            @Override
            public boolean addCustomEnchantsToLore() {
                return true;
            }

            @Override
            public boolean addTeamColorTo1_13Prefix() {
                return true;
            }

            @Override
            public boolean isFix1_13FacePlayer() {
                return true;
            }

            @Override
            public boolean alwaysShowOriginalMobName() {
                return true;
            }
        });
        init(Minecraft.getInstance().mcDataDir);
    }

    @Override
    public Logger getLogger() {
        return Via.getPlatform().getLogger();
    }

    @Override
    public void disable() {

    }

    @Override
    public boolean isOutdated() {
        return false;
    }

    @Override
    public File getDataFolder() {
        return Minecraft.getInstance().mcDataDir;
    }
}
