package viaversion.viabackwards.api;

import viaversion.viabackwards.api.data.BackwardsMappings;
import org.jetbrains.annotations.Nullable;
import viaversion.viaversion.api.protocol.ClientboundPacketType;
import viaversion.viaversion.api.protocol.Protocol;
import viaversion.viaversion.api.protocol.ProtocolRegistry;
import viaversion.viaversion.api.protocol.ServerboundPacketType;

public abstract class BackwardsProtocol<C1 extends ClientboundPacketType, C2 extends ClientboundPacketType, S1 extends ServerboundPacketType, S2 extends ServerboundPacketType>
        extends Protocol<C1, C2, S1, S2> {

    protected BackwardsProtocol() {
    }

    protected BackwardsProtocol(@Nullable Class<C1> oldClientboundPacketEnum, @Nullable Class<C2> clientboundPacketEnum,
                                @Nullable Class<S1> oldServerboundPacketEnum, @Nullable Class<S2> serverboundPacketEnum) {
        super(oldClientboundPacketEnum, clientboundPacketEnum, oldServerboundPacketEnum, serverboundPacketEnum);
    }

    /**
     * Waits for the given protocol to be loaded to then asynchronously execute the runnable for this protocol.
     */
    protected void executeAsyncAfterLoaded(Class<? extends Protocol> protocolClass, Runnable runnable) {
        ProtocolRegistry.addMappingLoaderFuture(getClass(), protocolClass, runnable);
    }

    @Override
    public boolean hasMappingDataToLoad() {
        // Manually load them later, since they depend on VV's mappings
        return false;
    }

    @Override
    public BackwardsMappings getMappingData() {
        return null;
    }
}
