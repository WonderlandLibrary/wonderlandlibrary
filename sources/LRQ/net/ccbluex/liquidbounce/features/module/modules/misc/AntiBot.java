/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.JvmStatic
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.player.IEntityPlayer;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.client.network.INetworkPlayerInfo;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketAnimation;
import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketEntity;
import net.ccbluex.liquidbounce.api.minecraft.util.IIChatComponent;
import net.ccbluex.liquidbounce.event.AttackEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.WorldEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.misc.AntiBot;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="AntiBot", description="Prevents KillAura from attacking AntiCheat bots.", category=ModuleCategory.MISC)
public final class AntiBot
extends Module {
    private final BoolValue tabValue = new BoolValue("Tab", true);
    private final ListValue tabModeValue = new ListValue("TabMode", new String[]{"Equals", "Contains"}, "Contains");
    private final BoolValue entityIDValue = new BoolValue("EntityID", true);
    private final BoolValue colorValue = new BoolValue("Color", false);
    private final BoolValue livingTimeValue = new BoolValue("LivingTime", false);
    private final IntegerValue livingTimeTicksValue = new IntegerValue("LivingTimeTicks", 40, 1, 200);
    private final BoolValue groundValue = new BoolValue("Ground", true);
    private final BoolValue airValue = new BoolValue("Air", false);
    private final BoolValue invalidGroundValue = new BoolValue("InvalidGround", true);
    private final BoolValue swingValue = new BoolValue("Swing", false);
    private final BoolValue healthValue = new BoolValue("Health", false);
    private final BoolValue derpValue = new BoolValue("Derp", true);
    private final BoolValue wasInvisibleValue = new BoolValue("WasInvisible", false);
    private final BoolValue armorValue = new BoolValue("Armor", false);
    private final BoolValue pingValue = new BoolValue("Ping", false);
    private final BoolValue needHitValue = new BoolValue("NeedHit", false);
    private final BoolValue duplicateInWorldValue = new BoolValue("DuplicateInWorld", false);
    private final BoolValue duplicateInTabValue = new BoolValue("DuplicateInTab", false);
    private final List<Integer> ground = new ArrayList();
    private final List<Integer> air = new ArrayList();
    private final Map<Integer, Integer> invalidGround = new HashMap();
    private final List<Integer> swing = new ArrayList();
    private final List<Integer> invisible = new ArrayList();
    private final List<Integer> hitted = new ArrayList();
    private final List<String> playerName = new ArrayList();
    public static final Companion Companion = new Companion(null);

    public final BoolValue getGroundValue() {
        return this.groundValue;
    }

    public final List<Integer> getGround() {
        return this.ground;
    }

    @Override
    public void onDisable() {
        this.clearAll();
        super.onDisable();
    }

    @EventTarget
    public final void onPacket(PacketEvent event) {
        IEntity entity;
        AntiBot antiBot = (AntiBot)LiquidBounce.INSTANCE.getModuleManager().getModule(AntiBot.class);
        if (MinecraftInstance.mc.getThePlayer() == null || MinecraftInstance.mc.getTheWorld() == null) {
            return;
        }
        IPacket packet = event.getPacket();
        if (MinecraftInstance.classProvider.isSPacketEntity(packet)) {
            ISPacketEntity packetEntity = packet.asSPacketEntity();
            IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
            if (iWorldClient == null) {
                Intrinsics.throwNpe();
            }
            if (MinecraftInstance.classProvider.isEntityPlayer(entity = packetEntity.getEntity(iWorldClient)) && entity != null) {
                if (packetEntity.getOnGround() && !this.ground.contains(entity.getEntityId())) {
                    this.ground.add(entity.getEntityId());
                }
                if (!packetEntity.getOnGround() && !this.air.contains(entity.getEntityId())) {
                    this.air.add(entity.getEntityId());
                }
                if (packetEntity.getOnGround()) {
                    if (entity.getPrevPosY() != entity.getPosY()) {
                        this.invalidGround.put(entity.getEntityId(), ((Number)this.invalidGround.getOrDefault(entity.getEntityId(), 0)).intValue() + 1);
                    }
                } else {
                    int currentVL = ((Number)this.invalidGround.getOrDefault(entity.getEntityId(), 0)).intValue() / 2;
                    if (currentVL <= 0) {
                        this.invalidGround.remove(entity.getEntityId());
                    } else {
                        this.invalidGround.put(entity.getEntityId(), currentVL);
                    }
                }
                if (entity.isInvisible() && !this.invisible.contains(entity.getEntityId())) {
                    this.invisible.add(entity.getEntityId());
                }
            }
        }
        if (MinecraftInstance.classProvider.isSPacketAnimation(packet)) {
            ISPacketAnimation packetAnimation = packet.asSPacketAnimation();
            IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
            if (iWorldClient == null) {
                Intrinsics.throwNpe();
            }
            if ((entity = iWorldClient.getEntityByID(packetAnimation.getEntityID())) != null && MinecraftInstance.classProvider.isEntityLivingBase(entity) && packetAnimation.getAnimationType() == 0 && !this.swing.contains(entity.getEntityId())) {
                this.swing.add(entity.getEntityId());
            }
        }
    }

    @EventTarget
    public final void onAttack(AttackEvent e) {
        IEntity entity = e.getTargetEntity();
        if (entity != null && MinecraftInstance.classProvider.isEntityLivingBase(entity) && !this.hitted.contains(entity.getEntityId())) {
            this.hitted.add(entity.getEntityId());
        }
    }

    @EventTarget
    public final void onWorld(@Nullable WorldEvent event) {
        this.clearAll();
    }

    private final void clearAll() {
        this.hitted.clear();
        this.swing.clear();
        this.ground.clear();
        this.invalidGround.clear();
        this.invisible.clear();
    }

    @JvmStatic
    public static final boolean isBot(IEntityLivingBase entity) {
        return Companion.isBot(entity);
    }

    public static final class Companion {
        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @JvmStatic
        public final boolean isBot(IEntityLivingBase entity) {
            IEntityPlayer player;
            if (!MinecraftInstance.classProvider.isEntityPlayer(entity)) {
                return false;
            }
            AntiBot antiBot = (AntiBot)LiquidBounce.INSTANCE.getModuleManager().getModule(AntiBot.class);
            if (antiBot == null) return false;
            if (!antiBot.getState()) {
                return false;
            }
            if (((Boolean)antiBot.colorValue.get()).booleanValue()) {
                IIChatComponent iIChatComponent = entity.getDisplayName();
                if (iIChatComponent == null) {
                    Intrinsics.throwNpe();
                }
                if (!StringsKt.replace$default((String)iIChatComponent.getFormattedText(), (String)"\u00a7r", (String)"", (boolean)false, (int)4, null).equals("\u00a7")) {
                    return true;
                }
            }
            if (((Boolean)antiBot.livingTimeValue.get()).booleanValue() && entity.getTicksExisted() < ((Number)antiBot.livingTimeTicksValue.get()).intValue()) {
                return true;
            }
            if (((Boolean)antiBot.getGroundValue().get()).booleanValue() && !antiBot.getGround().contains(entity.getEntityId())) {
                return true;
            }
            if (((Boolean)antiBot.airValue.get()).booleanValue() && !antiBot.air.contains(entity.getEntityId())) {
                return true;
            }
            if (((Boolean)antiBot.swingValue.get()).booleanValue() && !antiBot.swing.contains(entity.getEntityId())) {
                return true;
            }
            if (((Boolean)antiBot.healthValue.get()).booleanValue() && entity.getHealth() > 20.0f) {
                return true;
            }
            if (((Boolean)antiBot.entityIDValue.get()).booleanValue()) {
                if (entity.getEntityId() >= 1000000000) return true;
                if (entity.getEntityId() <= -1) {
                    return true;
                }
            }
            if (((Boolean)antiBot.derpValue.get()).booleanValue()) {
                if (entity.getRotationPitch() > 90.0f) return true;
                if (entity.getRotationPitch() < -90.0f) {
                    return true;
                }
            }
            if (((Boolean)antiBot.wasInvisibleValue.get()).booleanValue() && antiBot.invisible.contains(entity.getEntityId())) {
                return true;
            }
            if (((Boolean)antiBot.armorValue.get()).booleanValue() && (player = entity.asEntityPlayer()).getInventory().getArmorInventory().get(0) == null && player.getInventory().getArmorInventory().get(1) == null && player.getInventory().getArmorInventory().get(2) == null && player.getInventory().getArmorInventory().get(3) == null) {
                return true;
            }
            if (((Boolean)antiBot.pingValue.get()).booleanValue()) {
                player = entity.asEntityPlayer();
                INetworkPlayerInfo iNetworkPlayerInfo = MinecraftInstance.mc.getNetHandler().getPlayerInfo(player.getUniqueID());
                if (iNetworkPlayerInfo == null) {
                    Intrinsics.throwNpe();
                }
                if (iNetworkPlayerInfo.getResponseTime() == 0) {
                    return true;
                }
            }
            if (((Boolean)antiBot.needHitValue.get()).booleanValue() && !antiBot.hitted.contains(entity.getEntityId())) {
                return true;
            }
            if (((Boolean)antiBot.invalidGroundValue.get()).booleanValue() && ((Number)antiBot.invalidGround.getOrDefault(entity.getEntityId(), 0)).intValue() >= 10) {
                return true;
            }
            if (((Boolean)antiBot.tabValue.get()).booleanValue()) {
                String targetName;
                boolean equals = StringsKt.equals((String)((String)antiBot.tabModeValue.get()), (String)"Equals", (boolean)true);
                IIChatComponent iIChatComponent = entity.getDisplayName();
                if (iIChatComponent == null) {
                    Intrinsics.throwNpe();
                }
                if ((targetName = ColorUtils.stripColor(iIChatComponent.getFormattedText())) != null) {
                    Iterator<INetworkPlayerInfo> iterator = MinecraftInstance.mc.getNetHandler().getPlayerInfoMap().iterator();
                    while (iterator.hasNext()) {
                        String networkName;
                        INetworkPlayerInfo networkPlayerInfo = iterator.next();
                        if (ColorUtils.stripColor(EntityUtils.getName(networkPlayerInfo)) == null) {
                            continue;
                        }
                        if (equals ? targetName.equals(networkName) : targetName.equals(networkName)) return false;
                    }
                    return true;
                }
            }
            if (((Boolean)antiBot.duplicateInWorldValue.get()).booleanValue()) {
                IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
                if (iWorldClient == null) {
                    Intrinsics.throwNpe();
                }
                if (iWorldClient.getLoadedEntityList().stream().filter(isBot.1.INSTANCE).count() > 1L) {
                    return true;
                }
            }
            if (((Boolean)antiBot.duplicateInTabValue.get()).booleanValue() && MinecraftInstance.mc.getNetHandler().getPlayerInfoMap().stream().filter(new Predicate<INetworkPlayerInfo>(entity){
                final /* synthetic */ IEntityLivingBase $entity;

                public final boolean test(@Nullable INetworkPlayerInfo networkPlayer) {
                    return this.$entity.getName().equals(ColorUtils.stripColor(EntityUtils.getName(networkPlayer)));
                }
                {
                    this.$entity = iEntityLivingBase;
                }
            }).count() > 1L) {
                return true;
            }
            String string = entity.getName();
            if (string == null) {
                Intrinsics.throwNpe();
            }
            CharSequence charSequence = string;
            boolean bl = false;
            if (charSequence.length() == 0) {
                return true;
            }
            boolean bl2 = false;
            if (bl2) return true;
            String string2 = entity.getName();
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if (!string2.equals(iEntityPlayerSP.getName())) return false;
            return true;
        }

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

