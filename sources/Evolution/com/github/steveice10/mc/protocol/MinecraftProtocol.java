/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.protocol;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.auth.exception.request.RequestException;
import com.github.steveice10.mc.auth.service.AuthenticationService;
import com.github.steveice10.mc.protocol.ClientListener;
import com.github.steveice10.mc.protocol.ServerListener;
import com.github.steveice10.mc.protocol.data.SubProtocol;
import com.github.steveice10.mc.protocol.packet.handshake.client.HandshakePacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientKeepAlivePacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientPluginMessagePacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientRequestPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientResourcePackStatusPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientSettingsPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientTabCompletePacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerAbilitiesPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerActionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerChangeHeldItemPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerInteractEntityPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerMovementPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPlaceBlockPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerStatePacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerSwingArmPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerUseItemPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientAdvancementTabPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientCloseWindowPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientConfirmTransactionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientCraftingBookDataPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientCreativeInventoryActionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientEnchantItemPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientPrepareCraftingGridPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientWindowActionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.world.ClientSpectatePacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.world.ClientSteerBoatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.world.ClientSteerVehiclePacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.world.ClientTeleportConfirmPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.world.ClientUpdateSignPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.world.ClientVehicleMovePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerAdvancementTabPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerAdvancementsPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerBossBarPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerCombatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerDifficultyPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerDisconnectPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerKeepAlivePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerPlayerListDataPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerPlayerListEntryPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerPluginMessagePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerResourcePackSendPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerRespawnPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerSetCooldownPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerStatisticsPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerSwitchCameraPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerTabCompletePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerTitlePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerUnlockRecipesPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityAnimationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityAttachPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityCollectItemPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityDestroyPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityEffectPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityEquipmentPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityHeadLookPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityMetadataPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityMovementPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityPositionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityPropertiesPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityRemoveEffectPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntitySetPassengersPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityStatusPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityTeleportPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityVelocityPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerVehicleMovePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerAbilitiesPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerChangeHeldItemPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerHealthPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerSetExperiencePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerUseBedPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnExpOrbPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnGlobalEntityPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPaintingPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.scoreboard.ServerDisplayScoreboardPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.scoreboard.ServerScoreboardObjectivePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.scoreboard.ServerTeamPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.scoreboard.ServerUpdateScorePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerCloseWindowPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerConfirmTransactionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerOpenWindowPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerPreparedCraftingGridPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerSetSlotPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerWindowItemsPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerWindowPropertyPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerBlockBreakAnimPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerBlockChangePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerBlockValuePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerExplosionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerMapDataPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerMultiBlockChangePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerNotifyClientPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerOpenTileEntityEditorPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerPlayBuiltinSoundPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerPlayEffectPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerPlaySoundPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerSpawnParticlePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerSpawnPositionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUnloadChunkPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUpdateTileEntityPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUpdateTimePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerWorldBorderPacket;
import com.github.steveice10.mc.protocol.packet.login.client.EncryptionResponsePacket;
import com.github.steveice10.mc.protocol.packet.login.client.LoginStartPacket;
import com.github.steveice10.mc.protocol.packet.login.server.EncryptionRequestPacket;
import com.github.steveice10.mc.protocol.packet.login.server.LoginDisconnectPacket;
import com.github.steveice10.mc.protocol.packet.login.server.LoginSetCompressionPacket;
import com.github.steveice10.mc.protocol.packet.login.server.LoginSuccessPacket;
import com.github.steveice10.mc.protocol.packet.status.client.StatusPingPacket;
import com.github.steveice10.mc.protocol.packet.status.client.StatusQueryPacket;
import com.github.steveice10.mc.protocol.packet.status.server.StatusPongPacket;
import com.github.steveice10.mc.protocol.packet.status.server.StatusResponsePacket;
import com.github.steveice10.packetlib.Client;
import com.github.steveice10.packetlib.Server;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.crypt.AESEncryption;
import com.github.steveice10.packetlib.crypt.PacketEncryption;
import com.github.steveice10.packetlib.packet.DefaultPacketHeader;
import com.github.steveice10.packetlib.packet.PacketHeader;
import com.github.steveice10.packetlib.packet.PacketProtocol;
import java.net.Proxy;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.UUID;

public class MinecraftProtocol
extends PacketProtocol {
    private SubProtocol subProtocol = SubProtocol.HANDSHAKE;
    private PacketHeader header = new DefaultPacketHeader();
    private AESEncryption encrypt;
    private GameProfile profile;
    private String accessToken = "";

    private MinecraftProtocol() {
    }

    public MinecraftProtocol(SubProtocol subProtocol) {
        if (subProtocol != SubProtocol.LOGIN && subProtocol != SubProtocol.STATUS) {
            throw new IllegalArgumentException("Only login and status modes are permitted.");
        }
        this.subProtocol = subProtocol;
        if (subProtocol == SubProtocol.LOGIN) {
            this.profile = new GameProfile("", "Player");
        }
    }

    public MinecraftProtocol(String username) {
        this(SubProtocol.LOGIN);
        this.profile = new GameProfile("", username);
    }

    public MinecraftProtocol(String username, String password) throws RequestException {
        this(username, password, false);
    }

    public MinecraftProtocol(String username, String using, boolean token) throws RequestException {
        this(username, using, token, Proxy.NO_PROXY);
    }

    public MinecraftProtocol(String username, String using, boolean token, Proxy authProxy) throws RequestException {
        this(SubProtocol.LOGIN);
        String clientToken = UUID.randomUUID().toString();
        AuthenticationService auth = new AuthenticationService(clientToken, authProxy);
        auth.setUsername(username);
        if (token) {
            auth.setAccessToken(using);
        } else {
            auth.setPassword(using);
        }
        auth.login();
        this.profile = auth.getSelectedProfile();
        this.accessToken = auth.getAccessToken();
    }

    public MinecraftProtocol(GameProfile profile, String accessToken) {
        this(SubProtocol.LOGIN);
        this.profile = profile;
        this.accessToken = accessToken;
    }

    public GameProfile getProfile() {
        return this.profile;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    @Override
    public String getSRVRecordPrefix() {
        return "_minecraft";
    }

    @Override
    public PacketHeader getPacketHeader() {
        return this.header;
    }

    @Override
    public PacketEncryption getEncryption() {
        return this.encrypt;
    }

    @Override
    public void newClientSession(Client client, Session session) {
        if (this.profile != null) {
            session.setFlag("profile", this.profile);
            session.setFlag("access-token", this.accessToken);
        }
        this.setSubProtocol(this.subProtocol, true, session);
        session.addListener(new ClientListener());
    }

    @Override
    public void newServerSession(Server server, Session session) {
        this.setSubProtocol(SubProtocol.HANDSHAKE, false, session);
        session.addListener(new ServerListener());
    }

    protected void enableEncryption(Key key) {
        try {
            this.encrypt = new AESEncryption(key);
        }
        catch (GeneralSecurityException e) {
            throw new Error("Failed to enable protocol encryption.", e);
        }
    }

    public SubProtocol getSubProtocol() {
        return this.subProtocol;
    }

    protected void setSubProtocol(SubProtocol subProtocol, boolean client, Session session) {
        this.clearPackets();
        switch (subProtocol) {
            case HANDSHAKE: {
                if (client) {
                    this.initClientHandshake(session);
                    break;
                }
                this.initServerHandshake(session);
                break;
            }
            case LOGIN: {
                if (client) {
                    this.initClientLogin(session);
                    break;
                }
                this.initServerLogin(session);
                break;
            }
            case GAME: {
                if (client) {
                    this.initClientGame(session);
                    break;
                }
                this.initServerGame(session);
                break;
            }
            case STATUS: {
                if (client) {
                    this.initClientStatus(session);
                    break;
                }
                this.initServerStatus(session);
            }
        }
        this.subProtocol = subProtocol;
    }

    private void initClientHandshake(Session session) {
        this.registerOutgoing(0, HandshakePacket.class);
    }

    private void initServerHandshake(Session session) {
        this.registerIncoming(0, HandshakePacket.class);
    }

    private void initClientLogin(Session session) {
        this.registerIncoming(0, LoginDisconnectPacket.class);
        this.registerIncoming(1, EncryptionRequestPacket.class);
        this.registerIncoming(2, LoginSuccessPacket.class);
        this.registerIncoming(3, LoginSetCompressionPacket.class);
        this.registerOutgoing(0, LoginStartPacket.class);
        this.registerOutgoing(1, EncryptionResponsePacket.class);
    }

    private void initServerLogin(Session session) {
        this.registerIncoming(0, LoginStartPacket.class);
        this.registerIncoming(1, EncryptionResponsePacket.class);
        this.registerOutgoing(0, LoginDisconnectPacket.class);
        this.registerOutgoing(1, EncryptionRequestPacket.class);
        this.registerOutgoing(2, LoginSuccessPacket.class);
        this.registerOutgoing(3, LoginSetCompressionPacket.class);
    }

    private void initClientGame(Session session) {
        this.registerIncoming(0, ServerSpawnObjectPacket.class);
        this.registerIncoming(1, ServerSpawnExpOrbPacket.class);
        this.registerIncoming(2, ServerSpawnGlobalEntityPacket.class);
        this.registerIncoming(3, ServerSpawnMobPacket.class);
        this.registerIncoming(4, ServerSpawnPaintingPacket.class);
        this.registerIncoming(5, ServerSpawnPlayerPacket.class);
        this.registerIncoming(6, ServerEntityAnimationPacket.class);
        this.registerIncoming(7, ServerStatisticsPacket.class);
        this.registerIncoming(8, ServerBlockBreakAnimPacket.class);
        this.registerIncoming(9, ServerUpdateTileEntityPacket.class);
        this.registerIncoming(10, ServerBlockValuePacket.class);
        this.registerIncoming(11, ServerBlockChangePacket.class);
        this.registerIncoming(12, ServerBossBarPacket.class);
        this.registerIncoming(13, ServerDifficultyPacket.class);
        this.registerIncoming(14, ServerTabCompletePacket.class);
        this.registerIncoming(15, ServerChatPacket.class);
        this.registerIncoming(16, ServerMultiBlockChangePacket.class);
        this.registerIncoming(17, ServerConfirmTransactionPacket.class);
        this.registerIncoming(18, ServerCloseWindowPacket.class);
        this.registerIncoming(19, ServerOpenWindowPacket.class);
        this.registerIncoming(20, ServerWindowItemsPacket.class);
        this.registerIncoming(21, ServerWindowPropertyPacket.class);
        this.registerIncoming(22, ServerSetSlotPacket.class);
        this.registerIncoming(23, ServerSetCooldownPacket.class);
        this.registerIncoming(24, ServerPluginMessagePacket.class);
        this.registerIncoming(25, ServerPlaySoundPacket.class);
        this.registerIncoming(26, ServerDisconnectPacket.class);
        this.registerIncoming(27, ServerEntityStatusPacket.class);
        this.registerIncoming(28, ServerExplosionPacket.class);
        this.registerIncoming(29, ServerUnloadChunkPacket.class);
        this.registerIncoming(30, ServerNotifyClientPacket.class);
        this.registerIncoming(31, ServerKeepAlivePacket.class);
        this.registerIncoming(32, ServerChunkDataPacket.class);
        this.registerIncoming(33, ServerPlayEffectPacket.class);
        this.registerIncoming(34, ServerSpawnParticlePacket.class);
        this.registerIncoming(35, ServerJoinGamePacket.class);
        this.registerIncoming(36, ServerMapDataPacket.class);
        this.registerIncoming(37, ServerEntityMovementPacket.class);
        this.registerIncoming(38, ServerEntityPositionPacket.class);
        this.registerIncoming(39, ServerEntityPositionRotationPacket.class);
        this.registerIncoming(40, ServerEntityRotationPacket.class);
        this.registerIncoming(41, ServerVehicleMovePacket.class);
        this.registerIncoming(42, ServerOpenTileEntityEditorPacket.class);
        this.registerIncoming(43, ServerPreparedCraftingGridPacket.class);
        this.registerIncoming(44, ServerPlayerAbilitiesPacket.class);
        this.registerIncoming(45, ServerCombatPacket.class);
        this.registerIncoming(46, ServerPlayerListEntryPacket.class);
        this.registerIncoming(47, ServerPlayerPositionRotationPacket.class);
        this.registerIncoming(48, ServerPlayerUseBedPacket.class);
        this.registerIncoming(49, ServerUnlockRecipesPacket.class);
        this.registerIncoming(50, ServerEntityDestroyPacket.class);
        this.registerIncoming(51, ServerEntityRemoveEffectPacket.class);
        this.registerIncoming(52, ServerResourcePackSendPacket.class);
        this.registerIncoming(53, ServerRespawnPacket.class);
        this.registerIncoming(54, ServerEntityHeadLookPacket.class);
        this.registerIncoming(55, ServerAdvancementTabPacket.class);
        this.registerIncoming(56, ServerWorldBorderPacket.class);
        this.registerIncoming(57, ServerSwitchCameraPacket.class);
        this.registerIncoming(58, ServerPlayerChangeHeldItemPacket.class);
        this.registerIncoming(59, ServerDisplayScoreboardPacket.class);
        this.registerIncoming(60, ServerEntityMetadataPacket.class);
        this.registerIncoming(61, ServerEntityAttachPacket.class);
        this.registerIncoming(62, ServerEntityVelocityPacket.class);
        this.registerIncoming(63, ServerEntityEquipmentPacket.class);
        this.registerIncoming(64, ServerPlayerSetExperiencePacket.class);
        this.registerIncoming(65, ServerPlayerHealthPacket.class);
        this.registerIncoming(66, ServerScoreboardObjectivePacket.class);
        this.registerIncoming(67, ServerEntitySetPassengersPacket.class);
        this.registerIncoming(68, ServerTeamPacket.class);
        this.registerIncoming(69, ServerUpdateScorePacket.class);
        this.registerIncoming(70, ServerSpawnPositionPacket.class);
        this.registerIncoming(71, ServerUpdateTimePacket.class);
        this.registerIncoming(72, ServerTitlePacket.class);
        this.registerIncoming(73, ServerPlayBuiltinSoundPacket.class);
        this.registerIncoming(74, ServerPlayerListDataPacket.class);
        this.registerIncoming(75, ServerEntityCollectItemPacket.class);
        this.registerIncoming(76, ServerEntityTeleportPacket.class);
        this.registerIncoming(77, ServerAdvancementsPacket.class);
        this.registerIncoming(78, ServerEntityPropertiesPacket.class);
        this.registerIncoming(79, ServerEntityEffectPacket.class);
        this.registerOutgoing(0, ClientTeleportConfirmPacket.class);
        this.registerOutgoing(1, ClientTabCompletePacket.class);
        this.registerOutgoing(2, ClientChatPacket.class);
        this.registerOutgoing(3, ClientRequestPacket.class);
        this.registerOutgoing(4, ClientSettingsPacket.class);
        this.registerOutgoing(5, ClientConfirmTransactionPacket.class);
        this.registerOutgoing(6, ClientEnchantItemPacket.class);
        this.registerOutgoing(7, ClientWindowActionPacket.class);
        this.registerOutgoing(8, ClientCloseWindowPacket.class);
        this.registerOutgoing(9, ClientPluginMessagePacket.class);
        this.registerOutgoing(10, ClientPlayerInteractEntityPacket.class);
        this.registerOutgoing(11, ClientKeepAlivePacket.class);
        this.registerOutgoing(12, ClientPlayerMovementPacket.class);
        this.registerOutgoing(13, ClientPlayerPositionPacket.class);
        this.registerOutgoing(14, ClientPlayerPositionRotationPacket.class);
        this.registerOutgoing(15, ClientPlayerRotationPacket.class);
        this.registerOutgoing(16, ClientVehicleMovePacket.class);
        this.registerOutgoing(17, ClientSteerBoatPacket.class);
        this.registerOutgoing(18, ClientPrepareCraftingGridPacket.class);
        this.registerOutgoing(19, ClientPlayerAbilitiesPacket.class);
        this.registerOutgoing(20, ClientPlayerActionPacket.class);
        this.registerOutgoing(21, ClientPlayerStatePacket.class);
        this.registerOutgoing(22, ClientSteerVehiclePacket.class);
        this.registerOutgoing(23, ClientCraftingBookDataPacket.class);
        this.registerOutgoing(24, ClientResourcePackStatusPacket.class);
        this.registerOutgoing(25, ClientAdvancementTabPacket.class);
        this.registerOutgoing(26, ClientPlayerChangeHeldItemPacket.class);
        this.registerOutgoing(27, ClientCreativeInventoryActionPacket.class);
        this.registerOutgoing(28, ClientUpdateSignPacket.class);
        this.registerOutgoing(29, ClientPlayerSwingArmPacket.class);
        this.registerOutgoing(30, ClientSpectatePacket.class);
        this.registerOutgoing(31, ClientPlayerPlaceBlockPacket.class);
        this.registerOutgoing(32, ClientPlayerUseItemPacket.class);
    }

    private void initServerGame(Session session) {
        this.registerIncoming(0, ClientTeleportConfirmPacket.class);
        this.registerIncoming(1, ClientTabCompletePacket.class);
        this.registerIncoming(2, ClientChatPacket.class);
        this.registerIncoming(3, ClientRequestPacket.class);
        this.registerIncoming(4, ClientSettingsPacket.class);
        this.registerIncoming(5, ClientConfirmTransactionPacket.class);
        this.registerIncoming(6, ClientEnchantItemPacket.class);
        this.registerIncoming(7, ClientWindowActionPacket.class);
        this.registerIncoming(8, ClientCloseWindowPacket.class);
        this.registerIncoming(9, ClientPluginMessagePacket.class);
        this.registerIncoming(10, ClientPlayerInteractEntityPacket.class);
        this.registerIncoming(11, ClientKeepAlivePacket.class);
        this.registerIncoming(12, ClientPlayerMovementPacket.class);
        this.registerIncoming(13, ClientPlayerPositionPacket.class);
        this.registerIncoming(14, ClientPlayerPositionRotationPacket.class);
        this.registerIncoming(15, ClientPlayerRotationPacket.class);
        this.registerIncoming(16, ClientVehicleMovePacket.class);
        this.registerIncoming(17, ClientSteerBoatPacket.class);
        this.registerIncoming(18, ClientPrepareCraftingGridPacket.class);
        this.registerIncoming(19, ClientPlayerAbilitiesPacket.class);
        this.registerIncoming(20, ClientPlayerActionPacket.class);
        this.registerIncoming(21, ClientPlayerStatePacket.class);
        this.registerIncoming(22, ClientSteerVehiclePacket.class);
        this.registerIncoming(23, ClientCraftingBookDataPacket.class);
        this.registerIncoming(24, ClientResourcePackStatusPacket.class);
        this.registerIncoming(25, ClientAdvancementTabPacket.class);
        this.registerIncoming(26, ClientPlayerChangeHeldItemPacket.class);
        this.registerIncoming(27, ClientCreativeInventoryActionPacket.class);
        this.registerIncoming(28, ClientUpdateSignPacket.class);
        this.registerIncoming(29, ClientPlayerSwingArmPacket.class);
        this.registerIncoming(30, ClientSpectatePacket.class);
        this.registerIncoming(31, ClientPlayerPlaceBlockPacket.class);
        this.registerIncoming(32, ClientPlayerUseItemPacket.class);
        this.registerOutgoing(0, ServerSpawnObjectPacket.class);
        this.registerOutgoing(1, ServerSpawnExpOrbPacket.class);
        this.registerOutgoing(2, ServerSpawnGlobalEntityPacket.class);
        this.registerOutgoing(3, ServerSpawnMobPacket.class);
        this.registerOutgoing(4, ServerSpawnPaintingPacket.class);
        this.registerOutgoing(5, ServerSpawnPlayerPacket.class);
        this.registerOutgoing(6, ServerEntityAnimationPacket.class);
        this.registerOutgoing(7, ServerStatisticsPacket.class);
        this.registerOutgoing(8, ServerBlockBreakAnimPacket.class);
        this.registerOutgoing(9, ServerUpdateTileEntityPacket.class);
        this.registerOutgoing(10, ServerBlockValuePacket.class);
        this.registerOutgoing(11, ServerBlockChangePacket.class);
        this.registerOutgoing(12, ServerBossBarPacket.class);
        this.registerOutgoing(13, ServerDifficultyPacket.class);
        this.registerOutgoing(14, ServerTabCompletePacket.class);
        this.registerOutgoing(15, ServerChatPacket.class);
        this.registerOutgoing(16, ServerMultiBlockChangePacket.class);
        this.registerOutgoing(17, ServerConfirmTransactionPacket.class);
        this.registerOutgoing(18, ServerCloseWindowPacket.class);
        this.registerOutgoing(19, ServerOpenWindowPacket.class);
        this.registerOutgoing(20, ServerWindowItemsPacket.class);
        this.registerOutgoing(21, ServerWindowPropertyPacket.class);
        this.registerOutgoing(22, ServerSetSlotPacket.class);
        this.registerOutgoing(23, ServerSetCooldownPacket.class);
        this.registerOutgoing(24, ServerPluginMessagePacket.class);
        this.registerOutgoing(25, ServerPlaySoundPacket.class);
        this.registerOutgoing(26, ServerDisconnectPacket.class);
        this.registerOutgoing(27, ServerEntityStatusPacket.class);
        this.registerOutgoing(28, ServerExplosionPacket.class);
        this.registerOutgoing(29, ServerUnloadChunkPacket.class);
        this.registerOutgoing(30, ServerNotifyClientPacket.class);
        this.registerOutgoing(31, ServerKeepAlivePacket.class);
        this.registerOutgoing(32, ServerChunkDataPacket.class);
        this.registerOutgoing(33, ServerPlayEffectPacket.class);
        this.registerOutgoing(34, ServerSpawnParticlePacket.class);
        this.registerOutgoing(35, ServerJoinGamePacket.class);
        this.registerOutgoing(36, ServerMapDataPacket.class);
        this.registerOutgoing(37, ServerEntityMovementPacket.class);
        this.registerOutgoing(38, ServerEntityPositionPacket.class);
        this.registerOutgoing(39, ServerEntityPositionRotationPacket.class);
        this.registerOutgoing(40, ServerEntityRotationPacket.class);
        this.registerOutgoing(41, ServerVehicleMovePacket.class);
        this.registerOutgoing(42, ServerOpenTileEntityEditorPacket.class);
        this.registerOutgoing(43, ServerPreparedCraftingGridPacket.class);
        this.registerOutgoing(44, ServerPlayerAbilitiesPacket.class);
        this.registerOutgoing(45, ServerCombatPacket.class);
        this.registerOutgoing(46, ServerPlayerListEntryPacket.class);
        this.registerOutgoing(47, ServerPlayerPositionRotationPacket.class);
        this.registerOutgoing(48, ServerPlayerUseBedPacket.class);
        this.registerOutgoing(49, ServerUnlockRecipesPacket.class);
        this.registerOutgoing(50, ServerEntityDestroyPacket.class);
        this.registerOutgoing(51, ServerEntityRemoveEffectPacket.class);
        this.registerOutgoing(52, ServerResourcePackSendPacket.class);
        this.registerOutgoing(53, ServerRespawnPacket.class);
        this.registerOutgoing(54, ServerEntityHeadLookPacket.class);
        this.registerOutgoing(55, ServerAdvancementTabPacket.class);
        this.registerOutgoing(56, ServerWorldBorderPacket.class);
        this.registerOutgoing(57, ServerSwitchCameraPacket.class);
        this.registerOutgoing(58, ServerPlayerChangeHeldItemPacket.class);
        this.registerOutgoing(59, ServerDisplayScoreboardPacket.class);
        this.registerOutgoing(60, ServerEntityMetadataPacket.class);
        this.registerOutgoing(61, ServerEntityAttachPacket.class);
        this.registerOutgoing(62, ServerEntityVelocityPacket.class);
        this.registerOutgoing(63, ServerEntityEquipmentPacket.class);
        this.registerOutgoing(64, ServerPlayerSetExperiencePacket.class);
        this.registerOutgoing(65, ServerPlayerHealthPacket.class);
        this.registerOutgoing(66, ServerScoreboardObjectivePacket.class);
        this.registerOutgoing(67, ServerEntitySetPassengersPacket.class);
        this.registerOutgoing(68, ServerTeamPacket.class);
        this.registerOutgoing(69, ServerUpdateScorePacket.class);
        this.registerOutgoing(70, ServerSpawnPositionPacket.class);
        this.registerOutgoing(71, ServerUpdateTimePacket.class);
        this.registerOutgoing(72, ServerTitlePacket.class);
        this.registerOutgoing(73, ServerPlayBuiltinSoundPacket.class);
        this.registerOutgoing(74, ServerPlayerListDataPacket.class);
        this.registerOutgoing(75, ServerEntityCollectItemPacket.class);
        this.registerOutgoing(76, ServerEntityTeleportPacket.class);
        this.registerOutgoing(77, ServerAdvancementsPacket.class);
        this.registerOutgoing(78, ServerEntityPropertiesPacket.class);
        this.registerOutgoing(79, ServerEntityEffectPacket.class);
    }

    private void initClientStatus(Session session) {
        this.registerIncoming(0, StatusResponsePacket.class);
        this.registerIncoming(1, StatusPongPacket.class);
        this.registerOutgoing(0, StatusQueryPacket.class);
        this.registerOutgoing(1, StatusPingPacket.class);
    }

    private void initServerStatus(Session session) {
        this.registerIncoming(0, StatusQueryPacket.class);
        this.registerIncoming(1, StatusPingPacket.class);
        this.registerOutgoing(0, StatusResponsePacket.class);
        this.registerOutgoing(1, StatusPongPacket.class);
    }
}

