package nl.matsv.viabackwards.api;

import nl.matsv.viabackwards.api.data.BackwardsMappings;
import org.jetbrains.annotations.Nullable;
import us.myles.ViaVersion.api.data.MappingData;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.protocol.ProtocolRegistry;
import us.myles.ViaVersion.api.protocol.ServerboundPacketType;

public abstract class BackwardsProtocol<C1 extends ClientboundPacketType, C2 extends ClientboundPacketType, S1 extends ServerboundPacketType, S2 extends ServerboundPacketType> extends Protocol<C1, C2, S1, S2> {
  protected BackwardsProtocol() {}
  
  protected BackwardsProtocol(@Nullable Class<C1> oldClientboundPacketEnum, @Nullable Class<C2> clientboundPacketEnum, @Nullable Class<S1> oldServerboundPacketEnum, @Nullable Class<S2> serverboundPacketEnum) {
    super(oldClientboundPacketEnum, clientboundPacketEnum, oldServerboundPacketEnum, serverboundPacketEnum);
  }
  
  protected void executeAsyncAfterLoaded(Class<? extends Protocol> protocolClass, Runnable runnable) {
    ProtocolRegistry.addMappingLoaderFuture(getClass(), protocolClass, runnable);
  }
  
  public boolean hasMappingDataToLoad() {
    return false;
  }
  
  public BackwardsMappings getMappingData() {
    return null;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\api\BackwardsProtocol.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */