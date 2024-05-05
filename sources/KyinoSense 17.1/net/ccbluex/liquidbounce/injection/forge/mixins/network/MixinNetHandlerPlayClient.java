/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.Unpooled
 *  net.minecraft.client.ClientBrandRetriever
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiDownloadTerrain
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.multiplayer.PlayerControllerMP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.INetHandler
 *  net.minecraft.network.NetworkManager
 *  net.minecraft.network.Packet
 *  net.minecraft.network.PacketBuffer
 *  net.minecraft.network.PacketThreadUtil
 *  net.minecraft.network.play.client.C17PacketCustomPayload
 *  net.minecraft.network.play.client.C19PacketResourcePackStatus
 *  net.minecraft.network.play.client.C19PacketResourcePackStatus$Action
 *  net.minecraft.network.play.server.S01PacketJoinGame
 *  net.minecraft.network.play.server.S14PacketEntity
 *  net.minecraft.network.play.server.S48PacketResourcePackSend
 *  net.minecraft.util.IThreadListener
 *  net.minecraft.world.World
 *  net.minecraft.world.WorldSettings
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.network;

import io.netty.buffer.Unpooled;
import java.net.URI;
import java.net.URISyntaxException;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EntityMovementEvent;
import net.ccbluex.liquidbounce.features.special.AntiForge;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S48PacketResourcePackSend;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={NetHandlerPlayClient.class})
public abstract class MixinNetHandlerPlayClient {
    @Shadow
    @Final
    private NetworkManager field_147302_e;
    @Shadow
    private Minecraft field_147299_f;
    @Shadow
    private WorldClient field_147300_g;
    @Shadow
    public int field_147304_c;

    @Inject(method={"handleResourcePack"}, at={@At(value="HEAD")}, cancellable=true)
    private void handleResourcePack(S48PacketResourcePackSend p_handleResourcePack_1_, CallbackInfo callbackInfo) {
        String url = p_handleResourcePack_1_.func_179783_a();
        String hash = p_handleResourcePack_1_.func_179784_b();
        try {
            String scheme = new URI(url).getScheme();
            boolean isLevelProtocol = "level".equals(scheme);
            if (!("http".equals(scheme) || "https".equals(scheme) || isLevelProtocol)) {
                throw new URISyntaxException(url, "Wrong protocol");
            }
            if (isLevelProtocol && (url.contains("..") || !url.endsWith("/resources.zip"))) {
                throw new URISyntaxException(url, "Invalid levelstorage resourcepack path");
            }
        }
        catch (URISyntaxException e) {
            ClientUtils.getLogger().error("Failed to handle resource pack", (Throwable)e);
            this.field_147302_e.func_179290_a((Packet)new C19PacketResourcePackStatus(hash, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
            callbackInfo.cancel();
        }
    }

    @Inject(method={"handleJoinGame"}, at={@At(value="HEAD")}, cancellable=true)
    private void handleJoinGameWithAntiForge(S01PacketJoinGame packetIn, CallbackInfo callbackInfo) {
        if (!AntiForge.enabled || !AntiForge.blockFML || Minecraft.func_71410_x().func_71387_A()) {
            return;
        }
        PacketThreadUtil.func_180031_a((Packet)packetIn, (INetHandler)((NetHandlerPlayClient)this), (IThreadListener)this.field_147299_f);
        this.field_147299_f.field_71442_b = new PlayerControllerMP(this.field_147299_f, (NetHandlerPlayClient)this);
        this.field_147300_g = new WorldClient((NetHandlerPlayClient)this, new WorldSettings(0L, packetIn.func_149198_e(), false, packetIn.func_149195_d(), packetIn.func_149196_i()), packetIn.func_149194_f(), packetIn.func_149192_g(), this.field_147299_f.field_71424_I);
        this.field_147299_f.field_71474_y.field_74318_M = packetIn.func_149192_g();
        this.field_147299_f.func_71403_a(this.field_147300_g);
        this.field_147299_f.field_71439_g.field_71093_bK = packetIn.func_149194_f();
        this.field_147299_f.func_147108_a((GuiScreen)new GuiDownloadTerrain((NetHandlerPlayClient)this));
        this.field_147299_f.field_71439_g.func_145769_d(packetIn.func_149197_c());
        this.field_147304_c = packetIn.func_149193_h();
        this.field_147299_f.field_71439_g.func_175150_k(packetIn.func_179744_h());
        this.field_147299_f.field_71442_b.func_78746_a(packetIn.func_149198_e());
        this.field_147299_f.field_71474_y.func_82879_c();
        this.field_147302_e.func_179290_a((Packet)new C17PacketCustomPayload("MC|Brand", new PacketBuffer(Unpooled.buffer()).func_180714_a(ClientBrandRetriever.getClientModName())));
        callbackInfo.cancel();
    }

    @Inject(method={"handleEntityMovement"}, at={@At(value="FIELD", target="Lnet/minecraft/entity/Entity;onGround:Z")})
    private void handleEntityMovementEvent(S14PacketEntity packetIn, CallbackInfo callbackInfo) {
        Entity entity = packetIn.func_149065_a((World)this.field_147300_g);
        if (entity != null) {
            LiquidBounce.eventManager.callEvent(new EntityMovementEvent(entity));
        }
    }
}

