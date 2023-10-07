package viaversion.viafabric.platform;

import viaversion.viafabric.providers.VRVersionProvider;
import viaversion.viaversion.api.Via;
import viaversion.viaversion.api.platform.ViaPlatformLoader;
import viaversion.viaversion.bungee.providers.BungeeMovementTransmitter;
import viaversion.viaversion.protocols.base.VersionProvider;
import viaversion.viaversion.protocols.protocol1_9to1_8.providers.MovementTransmitterProvider;

public class VRLoader implements ViaPlatformLoader {

    @Override
    public void load() {
        Via.getManager().getProviders().use(MovementTransmitterProvider.class, new BungeeMovementTransmitter());
        Via.getManager().getProviders().use(VersionProvider.class, new VRVersionProvider());
    }

    @Override
    public void unload() {
        // Nothing to do
    }
}
