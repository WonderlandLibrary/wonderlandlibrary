/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.Unpooled
 *  net.minecraft.client.ClientBrandRetriever
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.client.gui.GuiDownloadTerrain
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.multiplayer.PlayerControllerMP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.client.network.NetworkPlayerInfo
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.inventory.Container
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.INetHandler
 *  net.minecraft.network.NetworkManager
 *  net.minecraft.network.Packet
 *  net.minecraft.network.PacketBuffer
 *  net.minecraft.network.PacketThreadUtil
 *  net.minecraft.network.play.client.C17PacketCustomPayload
 *  net.minecraft.network.play.client.C19PacketResourcePackStatus
 *  net.minecraft.network.play.client.C19PacketResourcePackStatus$Action
 *  net.minecraft.network.play.server.S01PacketJoinGame
 *  net.minecraft.network.play.server.S03PacketTimeUpdate
 *  net.minecraft.network.play.server.S04PacketEntityEquipment
 *  net.minecraft.network.play.server.S0BPacketAnimation
 *  net.minecraft.network.play.server.S0CPacketSpawnPlayer
 *  net.minecraft.network.play.server.S13PacketDestroyEntities
 *  net.minecraft.network.play.server.S14PacketEntity
 *  net.minecraft.network.play.server.S18PacketEntityTeleport
 *  net.minecraft.network.play.server.S19PacketEntityHeadLook
 *  net.minecraft.network.play.server.S19PacketEntityStatus
 *  net.minecraft.network.play.server.S1CPacketEntityMetadata
 *  net.minecraft.network.play.server.S20PacketEntityProperties
 *  net.minecraft.network.play.server.S29PacketSoundEffect
 *  net.minecraft.network.play.server.S32PacketConfirmTransaction
 *  net.minecraft.network.play.server.S3BPacketScoreboardObjective
 *  net.minecraft.network.play.server.S48PacketResourcePackSend
 *  net.minecraft.util.IThreadListener
 *  net.minecraft.world.World
 *  net.minecraft.world.WorldSettings
 */
package net.dev.important.injection.forge.mixins.network;

import io.netty.buffer.Unpooled;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;
import net.dev.important.Client;
import net.dev.important.event.EntityMovementEvent;
import net.dev.important.modules.command.other.AntiForge;
import net.dev.important.modules.module.modules.misc.Patcher;
import net.dev.important.utils.ClientUtils;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.network.play.server.S04PacketEntityEquipment;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.network.play.server.S13PacketDestroyEntities;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.network.play.server.S19PacketEntityHeadLook;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;
import net.minecraft.network.play.server.S20PacketEntityProperties;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.network.play.server.S3BPacketScoreboardObjective;
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
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

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

    @Shadow
    public abstract NetworkPlayerInfo func_175102_a(UUID var1);

    @Inject(method={"handleSpawnPlayer"}, at={@At(value="HEAD")}, cancellable=true)
    private void handleSpawnPlayer(S0CPacketSpawnPlayer packetIn, CallbackInfo callbackInfo) {
        if (((Boolean)Patcher.silentNPESP.get()).booleanValue()) {
            try {
                PacketThreadUtil.func_180031_a((Packet)packetIn, (INetHandler)((NetHandlerPlayClient)this), (IThreadListener)this.field_147299_f);
                double d0 = (double)packetIn.func_148942_f() / 32.0;
                double d1 = (double)packetIn.func_148949_g() / 32.0;
                double d2 = (double)packetIn.func_148946_h() / 32.0;
                float f = (float)(packetIn.func_148941_i() * 360) / 256.0f;
                float f1 = (float)(packetIn.func_148945_j() * 360) / 256.0f;
                EntityOtherPlayerMP entityotherplayermp = new EntityOtherPlayerMP((World)this.field_147299_f.field_71441_e, this.func_175102_a(packetIn.func_179819_c()).func_178845_a());
                entityotherplayermp.field_70118_ct = packetIn.func_148942_f();
                entityotherplayermp.field_70169_q = entityotherplayermp.field_70142_S = (double)entityotherplayermp.field_70118_ct;
                entityotherplayermp.field_70117_cu = packetIn.func_148949_g();
                entityotherplayermp.field_70167_r = entityotherplayermp.field_70137_T = (double)entityotherplayermp.field_70117_cu;
                entityotherplayermp.field_70116_cv = packetIn.func_148946_h();
                entityotherplayermp.field_70166_s = entityotherplayermp.field_70136_U = (double)entityotherplayermp.field_70116_cv;
                int i = packetIn.func_148947_k();
                entityotherplayermp.field_71071_by.field_70462_a[entityotherplayermp.field_71071_by.field_70461_c] = i == 0 ? null : new ItemStack(Item.func_150899_d((int)i), 1, 0);
                entityotherplayermp.func_70080_a(d0, d1, d2, f, f1);
                this.field_147300_g.func_73027_a(packetIn.func_148943_d(), (Entity)entityotherplayermp);
                List list = packetIn.func_148944_c();
                if (list != null) {
                    entityotherplayermp.func_70096_w().func_75687_a(list);
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
            callbackInfo.cancel();
        }
    }

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
            if (isLevelProtocol && (url.contains("..") || !url.endsWith(".zip"))) {
                File file1 = new File(this.field_147299_f.field_71412_D, "saves");
                String s2 = url.substring("level://".length());
                File file2 = new File(file1, s2);
                if (file2.isFile() && !url.toLowerCase().contains("liquidbounce")) {
                    this.field_147302_e.func_179290_a((Packet)new C19PacketResourcePackStatus(hash, C19PacketResourcePackStatus.Action.ACCEPTED));
                    this.field_147302_e.func_179290_a((Packet)new C19PacketResourcePackStatus(hash, C19PacketResourcePackStatus.Action.SUCCESSFULLY_LOADED));
                } else {
                    this.field_147302_e.func_179290_a((Packet)new C19PacketResourcePackStatus(hash, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
                }
                ClientUtils.displayChatMessage("\u00a78[\u00a79\u00a7lLiquidBounce+\u00a78] \u00a76Resourcepack exploit detected.");
                ClientUtils.displayChatMessage("\u00a78[\u00a79\u00a7lLiquidBounce+\u00a78] \u00a77Exploit target directory: \u00a7r" + url);
                throw new IllegalStateException("Invalid levelstorage resourcepack path");
            }
        }
        catch (URISyntaxException e) {
            ClientUtils.getLogger().error("Failed to handle resource pack", (Throwable)e);
            this.field_147302_e.func_179290_a((Packet)new C19PacketResourcePackStatus(hash, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
            callbackInfo.cancel();
        }
        catch (IllegalStateException e) {
            ClientUtils.getLogger().error("Failed to handle resource pack", (Throwable)e);
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
            Client.eventManager.callEvent(new EntityMovementEvent(entity));
        }
    }

    @Inject(method={"handleEntityStatus"}, at={@At(value="HEAD")})
    public void handleDamagePacket(S19PacketEntityStatus packetIn, CallbackInfo callbackInfo) {
        Entity entity;
        if (packetIn.func_149160_c() == 2 && (entity = packetIn.func_149161_a((World)this.field_147300_g)) != null && entity instanceof EntityPlayer) {
            Client.hud.handleDamage((EntityPlayer)entity);
        }
    }

    @Inject(method={"handleAnimation"}, at={@At(value="INVOKE", target="Lnet/minecraft/network/PacketThreadUtil;checkThreadAndEnqueue(Lnet/minecraft/network/Packet;Lnet/minecraft/network/INetHandler;Lnet/minecraft/util/IThreadListener;)V", shift=At.Shift.AFTER)}, cancellable=true)
    private void handleAnimation(S0BPacketAnimation s0BPacketAnimation, CallbackInfo callbackInfo) {
        this.cancelIfNull(this.field_147300_g, callbackInfo);
    }

    @Inject(method={"handleEntityTeleport"}, at={@At(value="INVOKE", target="Lnet/minecraft/network/PacketThreadUtil;checkThreadAndEnqueue(Lnet/minecraft/network/Packet;Lnet/minecraft/network/INetHandler;Lnet/minecraft/util/IThreadListener;)V", shift=At.Shift.AFTER)}, cancellable=true)
    private void handleEntityTeleport(S18PacketEntityTeleport s18PacketEntityTeleport, CallbackInfo callbackInfo) {
        this.cancelIfNull(this.field_147300_g, callbackInfo);
    }

    @Inject(method={"handleEntityMovement"}, at={@At(value="INVOKE", target="Lnet/minecraft/network/PacketThreadUtil;checkThreadAndEnqueue(Lnet/minecraft/network/Packet;Lnet/minecraft/network/INetHandler;Lnet/minecraft/util/IThreadListener;)V", shift=At.Shift.AFTER)}, cancellable=true)
    private void handleEntityMovement(S14PacketEntity s14PacketEntity, CallbackInfo callbackInfo) {
        this.cancelIfNull(this.field_147300_g, callbackInfo);
    }

    @Inject(method={"handleEntityHeadLook"}, at={@At(value="INVOKE", target="Lnet/minecraft/network/PacketThreadUtil;checkThreadAndEnqueue(Lnet/minecraft/network/Packet;Lnet/minecraft/network/INetHandler;Lnet/minecraft/util/IThreadListener;)V", shift=At.Shift.AFTER)}, cancellable=true)
    private void handleEntityHeadLook(S19PacketEntityHeadLook s19PacketEntityHeadLook, CallbackInfo callbackInfo) {
        this.cancelIfNull(this.field_147300_g, callbackInfo);
    }

    @Inject(method={"handleEntityProperties"}, at={@At(value="INVOKE", target="Lnet/minecraft/network/PacketThreadUtil;checkThreadAndEnqueue(Lnet/minecraft/network/Packet;Lnet/minecraft/network/INetHandler;Lnet/minecraft/util/IThreadListener;)V", shift=At.Shift.AFTER)}, cancellable=true)
    private void handleEntityProperties(S20PacketEntityProperties s20PacketEntityProperties, CallbackInfo callbackInfo) {
        this.cancelIfNull(this.field_147300_g, callbackInfo);
    }

    @Inject(method={"handleEntityMetadata"}, at={@At(value="INVOKE", target="Lnet/minecraft/network/PacketThreadUtil;checkThreadAndEnqueue(Lnet/minecraft/network/Packet;Lnet/minecraft/network/INetHandler;Lnet/minecraft/util/IThreadListener;)V", shift=At.Shift.AFTER)}, cancellable=true)
    private void handleEntityMetadata(S1CPacketEntityMetadata s1CPacketEntityMetadata, CallbackInfo callbackInfo) {
        this.cancelIfNull(this.field_147300_g, callbackInfo);
    }

    @Inject(method={"handleEntityEquipment"}, at={@At(value="INVOKE", target="Lnet/minecraft/network/PacketThreadUtil;checkThreadAndEnqueue(Lnet/minecraft/network/Packet;Lnet/minecraft/network/INetHandler;Lnet/minecraft/util/IThreadListener;)V", shift=At.Shift.AFTER)}, cancellable=true)
    private void handleEntityEquipment(S04PacketEntityEquipment s04PacketEntityEquipment, CallbackInfo callbackInfo) {
        this.cancelIfNull(this.field_147300_g, callbackInfo);
    }

    @Inject(method={"handleDestroyEntities"}, at={@At(value="INVOKE", target="Lnet/minecraft/network/PacketThreadUtil;checkThreadAndEnqueue(Lnet/minecraft/network/Packet;Lnet/minecraft/network/INetHandler;Lnet/minecraft/util/IThreadListener;)V", shift=At.Shift.AFTER)}, cancellable=true)
    private void handleDestroyEntities(S13PacketDestroyEntities s13PacketDestroyEntities, CallbackInfo callbackInfo) {
        this.cancelIfNull(this.field_147300_g, callbackInfo);
    }

    @Inject(method={"handleScoreboardObjective"}, at={@At(value="INVOKE", target="Lnet/minecraft/network/PacketThreadUtil;checkThreadAndEnqueue(Lnet/minecraft/network/Packet;Lnet/minecraft/network/INetHandler;Lnet/minecraft/util/IThreadListener;)V", shift=At.Shift.AFTER)}, cancellable=true)
    private void handleScoreboardObjective(S3BPacketScoreboardObjective s3BPacketScoreboardObjective, CallbackInfo callbackInfo) {
        this.cancelIfNull(this.field_147300_g, callbackInfo);
    }

    @Inject(method={"handleConfirmTransaction"}, at={@At(value="INVOKE", target="Lnet/minecraft/network/play/server/S32PacketConfirmTransaction;getWindowId()I", ordinal=0)}, cancellable=true, locals=LocalCapture.CAPTURE_FAILEXCEPTION)
    private void handleConfirmTransaction(S32PacketConfirmTransaction s32PacketConfirmTransaction, CallbackInfo callbackInfo, Container container, EntityPlayer entityPlayer) {
        this.cancelIfNull(entityPlayer, callbackInfo);
    }

    @Inject(method={"handleSoundEffect"}, at={@At(value="INVOKE", target="Lnet/minecraft/network/PacketThreadUtil;checkThreadAndEnqueue(Lnet/minecraft/network/Packet;Lnet/minecraft/network/INetHandler;Lnet/minecraft/util/IThreadListener;)V")}, cancellable=true)
    private void handleSoundEffect(S29PacketSoundEffect s29PacketSoundEffect, CallbackInfo callbackInfo) {
        this.cancelIfNull(this.field_147299_f.field_71441_e, callbackInfo);
    }

    @Inject(method={"handleTimeUpdate"}, at={@At(value="INVOKE", target="Lnet/minecraft/network/PacketThreadUtil;checkThreadAndEnqueue(Lnet/minecraft/network/Packet;Lnet/minecraft/network/INetHandler;Lnet/minecraft/util/IThreadListener;)V")}, cancellable=true)
    private void handleTimeUpdate(S03PacketTimeUpdate s03PacketTimeUpdate, CallbackInfo callbackInfo) {
        this.cancelIfNull(this.field_147299_f.field_71441_e, callbackInfo);
    }

    private <T> void cancelIfNull(T t, CallbackInfo callbackInfo) {
        if (t == null) {
            callbackInfo.cancel();
        }
    }
}

