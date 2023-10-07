package viaversion.viaversion.protocols.base;

import viaversion.viaversion.api.PacketWrapper;
import viaversion.viaversion.api.Pair;
import viaversion.viaversion.api.Via;
import viaversion.viaversion.api.data.UserConnection;
import viaversion.viaversion.api.platform.providers.ViaProviders;
import viaversion.viaversion.api.protocol.Protocol;
import viaversion.viaversion.api.protocol.ProtocolPipeline;
import viaversion.viaversion.api.protocol.ProtocolRegistry;
import viaversion.viaversion.api.protocol.ProtocolVersion;
import viaversion.viaversion.api.protocol.SimpleProtocol;
import viaversion.viaversion.api.remapper.PacketRemapper;
import viaversion.viaversion.api.type.Type;
import viaversion.viaversion.packets.Direction;
import viaversion.viaversion.packets.State;

import java.util.List;

public class BaseProtocol extends SimpleProtocol {

    @Override
    protected void registerPackets() {
        /* Incoming Packets */

        // Handshake Packet
        registerIncoming(State.HANDSHAKE, 0x00, 0x00, new PacketRemapper() {
            @Override
            public void registerMap() {
                handler(wrapper -> {
                    int protocolVersion = wrapper.passthrough(Type.VAR_INT);
                    wrapper.passthrough(Type.STRING); // Server Address
                    wrapper.passthrough(Type.UNSIGNED_SHORT); // Server Port
                    int state = wrapper.passthrough(Type.VAR_INT);

                    ProtocolInfo info = wrapper.user().getProtocolInfo();
                    info.setProtocolVersion(protocolVersion);
                    // Ensure the server has a version provider
                    if (Via.getManager().getProviders().get(VersionProvider.class) == null) {
                        wrapper.user().setActive(false);
                        return;
                    }

                    // Choose the pipe
                    int serverProtocol = Via.getManager().getProviders().get(VersionProvider.class).getServerProtocol(wrapper.user());
                    info.setServerProtocolVersion(serverProtocol);
                    List<Pair<Integer, Protocol>> protocols = null;

                    // Only allow newer clients or (1.9.2 on 1.9.4 server if the server supports it)
                    if (info.getProtocolVersion() >= serverProtocol || Via.getPlatform().isOldClientsAllowed()) {
                        protocols = ProtocolRegistry.getProtocolPath(info.getProtocolVersion(), serverProtocol);
                    }

                    ProtocolPipeline pipeline = wrapper.user().getProtocolInfo().getPipeline();
                    if (protocols != null) {
                        for (Pair<Integer, Protocol> prot : protocols) {
                            pipeline.add(prot.getValue());
                            // Ensure mapping data has already been loaded
                            ProtocolRegistry.completeMappingDataLoading(prot.getValue().getClass());
                        }

                        // Set the original snapshot version if present
                        ProtocolVersion protocol = ProtocolVersion.getProtocol(serverProtocol);
                        wrapper.set(Type.VAR_INT, 0, protocol.getOriginalVersion());
                    }

                    // Add Base Protocol
                    pipeline.add(ProtocolRegistry.getBaseProtocol(serverProtocol));

                    // Change state
                    if (state == 1) {
                        info.setState(State.STATUS);
                    }
                    if (state == 2) {
                        info.setState(State.LOGIN);
                    }
                });
            }
        });
    }

    @Override
    public void init(UserConnection userConnection) {
        // Nothing gets added, ProtocolPipeline handles ProtocolInfo
    }

    @Override
    protected void register(ViaProviders providers) {
        providers.register(VersionProvider.class, new VersionProvider());
    }

    @Override
    public void transform(Direction direction, State state, PacketWrapper packetWrapper) throws Exception {
        super.transform(direction, state, packetWrapper);
        if (direction == Direction.INCOMING && state == State.HANDSHAKE) {
            // Disable if it isn't a handshake packet.
            if (packetWrapper.getId() != 0) {
                packetWrapper.user().setActive(false);
            }
        }
    }
}
