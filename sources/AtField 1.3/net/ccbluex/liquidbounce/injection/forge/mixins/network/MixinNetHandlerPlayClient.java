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
 *  net.minecraft.network.play.client.CPacketCustomPayload
 *  net.minecraft.network.play.client.CPacketResourcePackStatus
 *  net.minecraft.network.play.client.CPacketResourcePackStatus$Action
 *  net.minecraft.network.play.server.SPacketEntity
 *  net.minecraft.network.play.server.SPacketJoinGame
 *  net.minecraft.network.play.server.SPacketResourcePackSend
 *  net.minecraft.util.IThreadListener
 *  net.minecraft.world.World
 *  net.minecraft.world.WorldSettings
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.network;

import io.netty.buffer.Unpooled;
import java.net.URI;
import java.net.URISyntaxException;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EntityMovementEvent;
import net.ccbluex.liquidbounce.features.special.AntiForge;
import net.ccbluex.liquidbounce.injection.backend.EntityImplKt;
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
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.client.CPacketResourcePackStatus;
import net.minecraft.network.play.server.SPacketEntity;
import net.minecraft.network.play.server.SPacketJoinGame;
import net.minecraft.network.play.server.SPacketResourcePackSend;
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
    public int field_147304_c;
    @Shadow
    private Minecraft field_147299_f;
    @Shadow
    @Final
    private NetworkManager field_147302_e;
    @Shadow
    private WorldClient field_147300_g;

    @Inject(method={"handleEntityMovement"}, at={@At(value="FIELD", target="Lnet/minecraft/entity/Entity;onGround:Z")})
    private void handleEntityMovementEvent(SPacketEntity sPacketEntity, CallbackInfo callbackInfo) {
        Entity entity = sPacketEntity.func_149065_a((World)this.field_147300_g);
        if (entity != null) {
            LiquidBounce.eventManager.callEvent(new EntityMovementEvent(EntityImplKt.wrap(entity)));
        }
    }

    @Inject(method={"handleResourcePack"}, at={@At(value="HEAD")}, cancellable=true)
    private void handleResourcePack(SPacketResourcePackSend sPacketResourcePackSend, CallbackInfo callbackInfo) {
        String string = sPacketResourcePackSend.func_179783_a();
        String string2 = sPacketResourcePackSend.func_179784_b();
        try {
            String string3 = new URI(string).getScheme();
            boolean bl = "level".equals(string3);
            if (!("http".equals(string3) || "https".equals(string3) || bl)) {
                throw new URISyntaxException(string, "Wrong protocol");
            }
            if (bl && (string.contains("..") || !string.endsWith("/resources.zip"))) {
                throw new URISyntaxException(string, "Invalid levelstorage resourcepack path");
            }
        }
        catch (URISyntaxException uRISyntaxException) {
            ClientUtils.getLogger().error("Failed to handle resource pack", (Throwable)uRISyntaxException);
            this.field_147302_e.func_179290_a((Packet)new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.FAILED_DOWNLOAD));
            callbackInfo.cancel();
        }
    }

    @Inject(method={"handleJoinGame"}, at={@At(value="HEAD")}, cancellable=true)
    private void handleJoinGameWithAntiForge(SPacketJoinGame sPacketJoinGame, CallbackInfo callbackInfo) {
        if (!AntiForge.enabled || !AntiForge.blockFML || Minecraft.func_71410_x().func_71387_A()) {
            return;
        }
        PacketThreadUtil.func_180031_a((Packet)sPacketJoinGame, (INetHandler)((NetHandlerPlayClient)this), (IThreadListener)this.field_147299_f);
        this.field_147299_f.field_71442_b = new PlayerControllerMP(this.field_147299_f, (NetHandlerPlayClient)this);
        this.field_147300_g = new WorldClient((NetHandlerPlayClient)this, new WorldSettings(0L, sPacketJoinGame.func_149198_e(), false, sPacketJoinGame.func_149195_d(), sPacketJoinGame.func_149196_i()), sPacketJoinGame.func_149194_f(), sPacketJoinGame.func_149192_g(), this.field_147299_f.field_71424_I);
        this.field_147299_f.field_71474_y.field_74318_M = sPacketJoinGame.func_149192_g();
        this.field_147299_f.func_71403_a(this.field_147300_g);
        this.field_147299_f.field_71439_g.field_71093_bK = sPacketJoinGame.func_149194_f();
        this.field_147299_f.func_147108_a((GuiScreen)new GuiDownloadTerrain());
        this.field_147299_f.field_71439_g.func_145769_d(sPacketJoinGame.func_149197_c());
        this.field_147304_c = sPacketJoinGame.func_149193_h();
        this.field_147299_f.field_71439_g.func_175150_k(sPacketJoinGame.func_179744_h());
        this.field_147299_f.field_71442_b.func_78746_a(sPacketJoinGame.func_149198_e());
        this.field_147299_f.field_71474_y.func_82879_c();
        this.field_147302_e.func_179290_a((Packet)new CPacketCustomPayload("MC|Brand", new PacketBuffer(Unpooled.buffer()).func_180714_a(ClientBrandRetriever.getClientModName())));
        callbackInfo.cancel();
    }
}

