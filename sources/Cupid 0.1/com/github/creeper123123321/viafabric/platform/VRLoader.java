package com.github.creeper123123321.viafabric.platform;

import com.github.creeper123123321.viafabric.providers.VRVersionProvider;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.platform.ViaPlatformLoader;
import us.myles.ViaVersion.api.platform.providers.Provider;
import us.myles.ViaVersion.bungee.providers.BungeeMovementTransmitter;
import us.myles.ViaVersion.protocols.base.VersionProvider;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.providers.MovementTransmitterProvider;

public class VRLoader implements ViaPlatformLoader {
  public void load() {
    Via.getManager().getProviders().use(MovementTransmitterProvider.class, (Provider)new BungeeMovementTransmitter());
    Via.getManager().getProviders().use(VersionProvider.class, (Provider)new VRVersionProvider());
  }
  
  public void unload() {}
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\github\creeper123123321\viafabric\platform\VRLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */