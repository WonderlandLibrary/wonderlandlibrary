package viaversion.viarewind.protocol.protocol1_7_6_10to1_8;

import viaversion.viarewind.netty.EmptyChannelHandler;
import viaversion.viarewind.netty.ForwardMessageToByteEncoder;
import viaversion.viarewind.protocol.protocol1_7_6_10to1_8.packets.EntityPackets;
import viaversion.viarewind.protocol.protocol1_7_6_10to1_8.packets.InventoryPackets;
import viaversion.viarewind.protocol.protocol1_7_6_10to1_8.packets.PlayerPackets;
import viaversion.viarewind.protocol.protocol1_7_6_10to1_8.packets.ScoreboardPackets;
import viaversion.viarewind.protocol.protocol1_7_6_10to1_8.packets.SpawnPackets;
import viaversion.viarewind.protocol.protocol1_7_6_10to1_8.packets.WorldPackets;
import viaversion.viarewind.protocol.protocol1_7_6_10to1_8.storage.CompressionSendStorage;
import viaversion.viarewind.protocol.protocol1_7_6_10to1_8.storage.EntityTracker;
import viaversion.viarewind.protocol.protocol1_7_6_10to1_8.storage.GameProfileStorage;
import viaversion.viarewind.protocol.protocol1_7_6_10to1_8.storage.PlayerAbilities;
import viaversion.viarewind.protocol.protocol1_7_6_10to1_8.storage.PlayerPosition;
import viaversion.viarewind.protocol.protocol1_7_6_10to1_8.storage.Scoreboard;
import viaversion.viarewind.protocol.protocol1_7_6_10to1_8.storage.Windows;
import viaversion.viarewind.protocol.protocol1_7_6_10to1_8.storage.WorldBorder;
import viaversion.viarewind.utils.Ticker;
import io.netty.channel.Channel;
import viaversion.viaversion.api.PacketWrapper;
import viaversion.viaversion.api.data.UserConnection;
import viaversion.viaversion.api.protocol.Protocol;
import viaversion.viaversion.api.remapper.PacketRemapper;
import viaversion.viaversion.api.type.Type;
import viaversion.viaversion.api.type.types.CustomByteType;
import viaversion.viaversion.packets.Direction;
import viaversion.viaversion.packets.State;
import viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import viaversion.viaversion.protocols.protocol1_9to1_8.storage.ClientChunks;

public class Protocol1_7_6_10TO1_8 extends Protocol {

	@Override
	protected void registerPackets() {
		EntityPackets.register(this);
		InventoryPackets.register(this);
		PlayerPackets.register(this);
		ScoreboardPackets.register(this);
		SpawnPackets.register(this);
		WorldPackets.register(this);

		//Keep Alive
		this.registerOutgoing(State.PLAY, 0x00, 0x00, new PacketRemapper() {
			@Override
			public void registerMap() {
				map(Type.VAR_INT, Type.INT);
			}
		});

		//Set Compression
		this.registerOutgoing(State.PLAY, 0x46, -1, new PacketRemapper() {
			@Override
			public void registerMap() {
				handler(packetWrapper -> packetWrapper.cancel());
			}
		});

		//Keep Alive
		this.registerIncoming(State.PLAY, 0x00, 0x00, new PacketRemapper() {
			@Override
			public void registerMap() {
				map(Type.INT, Type.VAR_INT);
			}
		});

		//Encryption Request
		this.registerOutgoing(State.LOGIN, 0x01, 0x01, new PacketRemapper() {
			@Override
			public void registerMap() {
				map(Type.STRING);  //Server ID
				handler(packetWrapper -> {
					int publicKeyLength = packetWrapper.read(Type.VAR_INT);
					packetWrapper.write(Type.SHORT, (short) publicKeyLength);
					packetWrapper.passthrough(new CustomByteType(publicKeyLength));

					int verifyTokenLength = packetWrapper.read(Type.VAR_INT);
					packetWrapper.write(Type.SHORT, (short) verifyTokenLength);
					packetWrapper.passthrough(new CustomByteType(verifyTokenLength));
				});
			}
		});

		//Set Compression
		this.registerOutgoing(State.LOGIN, 0x03, 0x03, new PacketRemapper() {
			@Override
			public void registerMap() {
				handler(packetWrapper -> {
					packetWrapper.cancel();
					packetWrapper.user().get(CompressionSendStorage.class).setCompressionSend(true);
				});
			}
		});

		//Encryption Response
		this.registerIncoming(State.LOGIN, 0x01, 0x01, new PacketRemapper() {
			@Override
			public void registerMap() {
				handler(packetWrapper -> {
					int sharedSecretLength = packetWrapper.read(Type.SHORT);
					packetWrapper.write(Type.VAR_INT, sharedSecretLength);
					packetWrapper.passthrough(new CustomByteType(sharedSecretLength));

					int verifyTokenLength = packetWrapper.read(Type.SHORT);
					packetWrapper.write(Type.VAR_INT, verifyTokenLength);
					packetWrapper.passthrough(new CustomByteType(verifyTokenLength));
				});
			}
		});
	}

	@Override
	public void transform(Direction direction, State state, PacketWrapper packetWrapper) throws Exception {
		CompressionSendStorage compressionSendStorage = packetWrapper.user().get(CompressionSendStorage.class);
		if (compressionSendStorage.isCompressionSend()) {
			Channel channel = packetWrapper.user().getChannel();
			if (channel.pipeline().get("compress") != null) {
				channel.pipeline().replace("decompress", "decompress", new EmptyChannelHandler());
				channel.pipeline().replace("compress", "compress", new ForwardMessageToByteEncoder());
			} else if (channel.pipeline().get("compression-encoder") != null) { // Velocity
				channel.pipeline().replace("compression-decoder", "compression-decoder", new EmptyChannelHandler());
				channel.pipeline().replace("compression-encoder", "compression-encoder", new ForwardMessageToByteEncoder());
			}

			compressionSendStorage.setCompressionSend(false);
		}

		super.transform(direction, state, packetWrapper);
	}

	@Override
	public void init(UserConnection userConnection) {
		Ticker.init();

		userConnection.put(new Windows(userConnection));
		userConnection.put(new EntityTracker(userConnection));
		userConnection.put(new PlayerPosition(userConnection));
		userConnection.put(new GameProfileStorage(userConnection));
		userConnection.put(new ClientChunks(userConnection));
		userConnection.put(new Scoreboard(userConnection));
		userConnection.put(new CompressionSendStorage(userConnection));
		userConnection.put(new WorldBorder(userConnection));
		userConnection.put(new PlayerAbilities(userConnection));
		userConnection.put(new ClientWorld(userConnection));
	}
}
