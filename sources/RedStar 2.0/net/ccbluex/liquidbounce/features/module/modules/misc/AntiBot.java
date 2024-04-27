package net.ccbluex.liquidbounce.features.module.modules.misc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
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
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.extensions.NetworkExtensionKt;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="AntiBot", description="Prevents KillAura from attacking AntiCheat bots.", category=ModuleCategory.MISC)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000f\n\n\n\b\n!\n\b\n\u0000\n\n\b\n\n\b\n%\n\b\n\n\b\n\n\b\n\n\u0000\n\n\u0000\n\n\b\n\n\b\n\n\u0000\n\n\u0000\bÇ\u000020B\b¢J\b%0&HJ'0(2)0*HJ+0&2,0-HJ\b.0&HJ/0&2001HJ20&2\b003HR\b00X¢\n\u0000R0X¢\n\u0000R\b0X¢\n\u0000R\t0\nX¢\n\u0000R0X¢\n\u0000R\f0X¢\n\u0000R\r0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R\b00X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R\b00X¢\n\u0000R000X¢\n\u0000R0X¢\n\u0000R\b00X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R\b00X¢\n\u0000R0X¢\n\u0000R\b00X¢\n\u0000R 0X¢\n\u0000R!0\"X¢\n\u0000R#0X¢\n\u0000R$0X¢\n\u0000¨4"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/misc/AntiBot;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "air", "", "", "airValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "allwaysInRadiusValue", "allwaysRadiusValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "armorValue", "colorValue", "derpValue", "duplicateInTabValue", "duplicateInWorldValue", "entityIDValue", "ground", "groundValue", "healthValue", "hitted", "invalidGround", "", "invalidGroundValue", "invisible", "livingTimeTicksValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "livingTimeValue", "needHitValue", "notAlwaysInRadius", "pingValue", "swing", "swingValue", "tabModeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "tabValue", "wasInvisibleValue", "clearAll", "", "isBot", "", "entity", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;", "onAttack", "e", "Lnet/ccbluex/liquidbounce/event/AttackEvent;", "onDisable", "onPacket", "event", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onWorld", "Lnet/ccbluex/liquidbounce/event/WorldEvent;", "Pride"})
public final class AntiBot
extends Module {
    private static final BoolValue tabValue;
    private static final ListValue tabModeValue;
    private static final BoolValue entityIDValue;
    private static final BoolValue colorValue;
    private static final BoolValue livingTimeValue;
    private static final IntegerValue livingTimeTicksValue;
    private static final BoolValue groundValue;
    private static final BoolValue airValue;
    private static final BoolValue invalidGroundValue;
    private static final BoolValue swingValue;
    private static final BoolValue healthValue;
    private static final BoolValue derpValue;
    private static final BoolValue wasInvisibleValue;
    private static final BoolValue armorValue;
    private static final BoolValue pingValue;
    private static final BoolValue needHitValue;
    private static final BoolValue duplicateInWorldValue;
    private static final BoolValue duplicateInTabValue;
    private static final BoolValue allwaysInRadiusValue;
    private static final FloatValue allwaysRadiusValue;
    private static final List<Integer> ground;
    private static final List<Integer> air;
    private static final Map<Integer, Integer> invalidGround;
    private static final List<Integer> swing;
    private static final List<Integer> invisible;
    private static final List<Integer> hitted;
    private static final List<Integer> notAlwaysInRadius;
    public static final AntiBot INSTANCE;

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @JvmStatic
    public static final boolean isBot(@NotNull IEntityLivingBase entity) {
        Object object;
        Object it;
        Iterable $this$filterTo$iv$iv;
        boolean $i$f$filterTo;
        Collection destination$iv$iv;
        Iterable $this$filter$iv;
        IEntityPlayer player;
        Intrinsics.checkParameterIsNotNull(entity, "entity");
        if (!MinecraftInstance.classProvider.isEntityPlayer(entity)) {
            return false;
        }
        if (!INSTANCE.getState()) {
            return false;
        }
        if (((Boolean)colorValue.get()).booleanValue()) {
            IIChatComponent iIChatComponent = entity.getDisplayName();
            if (iIChatComponent == null) {
                Intrinsics.throwNpe();
            }
            if (!StringsKt.contains$default((CharSequence)StringsKt.replace$default(iIChatComponent.getFormattedText(), "§r", "", false, 4, null), "§", false, 2, null)) {
                return true;
            }
        }
        if (((Boolean)livingTimeValue.get()).booleanValue() && entity.getTicksExisted() < ((Number)livingTimeTicksValue.get()).intValue()) {
            return true;
        }
        if (((Boolean)groundValue.get()).booleanValue() && !ground.contains(entity.getEntityId())) {
            return true;
        }
        if (((Boolean)airValue.get()).booleanValue() && !air.contains(entity.getEntityId())) {
            return true;
        }
        if (((Boolean)swingValue.get()).booleanValue() && !swing.contains(entity.getEntityId())) {
            return true;
        }
        if (((Boolean)healthValue.get()).booleanValue() && entity.getHealth() > 20.0f) {
            return true;
        }
        if (((Boolean)entityIDValue.get()).booleanValue()) {
            if (entity.getEntityId() >= 1000000000) return true;
            if (entity.getEntityId() <= -1) {
                return true;
            }
        }
        if (((Boolean)derpValue.get()).booleanValue()) {
            if (entity.getRotationPitch() > 90.0f) return true;
            if (entity.getRotationPitch() < -90.0f) {
                return true;
            }
        }
        if (((Boolean)wasInvisibleValue.get()).booleanValue() && invisible.contains(entity.getEntityId())) {
            return true;
        }
        if (((Boolean)armorValue.get()).booleanValue() && (player = entity.asEntityPlayer()).getInventory().getArmorInventory().get(0) == null && player.getInventory().getArmorInventory().get(1) == null && player.getInventory().getArmorInventory().get(2) == null && player.getInventory().getArmorInventory().get(3) == null) {
            return true;
        }
        if (((Boolean)pingValue.get()).booleanValue()) {
            INetworkPlayerInfo iNetworkPlayerInfo = MinecraftInstance.mc.getNetHandler().getPlayerInfo(entity.asEntityPlayer().getUniqueID());
            if (iNetworkPlayerInfo != null) {
                if (iNetworkPlayerInfo.getResponseTime() == 0) {
                    return true;
                }
            }
        }
        if (((Boolean)needHitValue.get()).booleanValue() && !hitted.contains(entity.getEntityId())) {
            return true;
        }
        if (((Boolean)invalidGroundValue.get()).booleanValue() && ((Number)invalidGround.getOrDefault(entity.getEntityId(), 0)).intValue() >= 10) {
            return true;
        }
        if (((Boolean)tabValue.get()).booleanValue()) {
            String targetName;
            boolean equals = StringsKt.equals((String)tabModeValue.get(), "Equals", true);
            IIChatComponent iIChatComponent = entity.getDisplayName();
            if (iIChatComponent == null) {
                Intrinsics.throwNpe();
            }
            if ((targetName = ColorUtils.stripColor(iIChatComponent.getFormattedText())) != null) {
                Iterator<INetworkPlayerInfo> iterator2 = MinecraftInstance.mc.getNetHandler().getPlayerInfoMap().iterator();
                while (iterator2.hasNext()) {
                    String networkName;
                    INetworkPlayerInfo networkPlayerInfo = iterator2.next();
                    if (ColorUtils.stripColor(NetworkExtensionKt.getFullName(networkPlayerInfo)) == null) {
                        continue;
                    }
                    if (equals ? Intrinsics.areEqual(targetName, networkName) : StringsKt.contains$default((CharSequence)targetName, networkName, false, 2, null)) return false;
                }
                return true;
            }
        }
        if (((Boolean)duplicateInWorldValue.get()).booleanValue()) {
            IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
            if (iWorldClient == null) {
                Intrinsics.throwNpe();
            }
            $this$filter$iv = iWorldClient.getLoadedEntityList();
            boolean $i$f$filter = false;
            Iterable networkPlayerInfo = $this$filter$iv;
            destination$iv$iv = new ArrayList();
            $i$f$filterTo = false;
            for (Object element$iv$iv : $this$filterTo$iv$iv) {
                it = (IEntity)element$iv$iv;
                boolean bl = false;
                if (!(MinecraftInstance.classProvider.isEntityPlayer(it) && Intrinsics.areEqual(it.asEntityPlayer().getDisplayNameString(), it.asEntityPlayer().getDisplayNameString()))) continue;
                destination$iv$iv.add(element$iv$iv);
            }
            $this$filter$iv = (List)destination$iv$iv;
            $i$f$filter = false;
            if ($this$filter$iv.size() > 1) {
                return true;
            }
        }
        if (((Boolean)duplicateInTabValue.get()).booleanValue()) {
            $this$filter$iv = MinecraftInstance.mc.getNetHandler().getPlayerInfoMap();
            boolean $i$f$filter = false;
            $this$filterTo$iv$iv = $this$filter$iv;
            destination$iv$iv = new ArrayList();
            $i$f$filterTo = false;
            for (Object element$iv$iv : $this$filterTo$iv$iv) {
                it = (INetworkPlayerInfo)element$iv$iv;
                boolean bl = false;
                if (!Intrinsics.areEqual(entity.getName(), ColorUtils.stripColor(NetworkExtensionKt.getFullName((INetworkPlayerInfo)it)))) continue;
                destination$iv$iv.add(element$iv$iv);
            }
            object = (List)destination$iv$iv;
            boolean bl = false;
            if (object.size() > 1) {
                return true;
            }
        }
        if (((Boolean)allwaysInRadiusValue.get()).booleanValue() && !notAlwaysInRadius.contains(entity.getEntityId())) {
            return true;
        }
        String string = entity.getName();
        if (string == null) {
            Intrinsics.throwNpe();
        }
        object = string;
        boolean bl = false;
        if (object.length() == 0) {
            return true;
        }
        boolean bl2 = false;
        if (bl2) return true;
        String string2 = entity.getName();
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (!Intrinsics.areEqual(string2, iEntityPlayerSP.getName())) return false;
        return true;
    }

    @Override
    public void onDisable() {
        this.clearAll();
        super.onDisable();
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        IEntity entity;
        Intrinsics.checkParameterIsNotNull(event, "event");
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
                if (packetEntity.getOnGround() && !ground.contains(entity.getEntityId())) {
                    ground.add(entity.getEntityId());
                }
                if (!packetEntity.getOnGround() && !air.contains(entity.getEntityId())) {
                    air.add(entity.getEntityId());
                }
                if (packetEntity.getOnGround()) {
                    if (entity.getPrevPosY() != entity.getPosY()) {
                        invalidGround.put(entity.getEntityId(), ((Number)invalidGround.getOrDefault(entity.getEntityId(), 0)).intValue() + 1);
                    }
                } else {
                    int currentVL = ((Number)invalidGround.getOrDefault(entity.getEntityId(), 0)).intValue() / 2;
                    if (currentVL <= 0) {
                        invalidGround.remove(entity.getEntityId());
                    } else {
                        invalidGround.put(entity.getEntityId(), currentVL);
                    }
                }
                if (entity.isInvisible() && !invisible.contains(entity.getEntityId())) {
                    invisible.add(entity.getEntityId());
                }
                if (!notAlwaysInRadius.contains(entity.getEntityId())) {
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iEntityPlayerSP.getDistanceToEntity(entity) > ((Number)allwaysRadiusValue.get()).floatValue()) {
                        notAlwaysInRadius.add(entity.getEntityId());
                    }
                }
            }
        }
        if (MinecraftInstance.classProvider.isSPacketAnimation(packet)) {
            ISPacketAnimation packetAnimation = packet.asSPacketAnimation();
            IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
            if (iWorldClient == null) {
                Intrinsics.throwNpe();
            }
            if ((entity = iWorldClient.getEntityByID(packetAnimation.getEntityID())) != null && MinecraftInstance.classProvider.isEntityLivingBase(entity) && packetAnimation.getAnimationType() == 0 && !swing.contains(entity.getEntityId())) {
                swing.add(entity.getEntityId());
            }
        }
    }

    @EventTarget
    public final void onAttack(@NotNull AttackEvent e) {
        Intrinsics.checkParameterIsNotNull(e, "e");
        IEntity entity = e.getTargetEntity();
        if (entity != null && MinecraftInstance.classProvider.isEntityLivingBase(entity) && !hitted.contains(entity.getEntityId())) {
            hitted.add(entity.getEntityId());
        }
    }

    @EventTarget
    public final void onWorld(@Nullable WorldEvent event) {
        this.clearAll();
    }

    private final void clearAll() {
        hitted.clear();
        swing.clear();
        ground.clear();
        invalidGround.clear();
        invisible.clear();
        notAlwaysInRadius.clear();
    }

    private AntiBot() {
    }

    static {
        AntiBot antiBot;
        INSTANCE = antiBot = new AntiBot();
        tabValue = new BoolValue("Tab", true);
        tabModeValue = new ListValue("TabMode", new String[]{"Equals", "Contains"}, "Contains");
        entityIDValue = new BoolValue("EntityID", true);
        colorValue = new BoolValue("Color", false);
        livingTimeValue = new BoolValue("LivingTime", false);
        livingTimeTicksValue = new IntegerValue("LivingTimeTicks", 40, 1, 200);
        groundValue = new BoolValue("Ground", true);
        airValue = new BoolValue("Air", false);
        invalidGroundValue = new BoolValue("InvalidGround", true);
        swingValue = new BoolValue("Swing", false);
        healthValue = new BoolValue("Health", false);
        derpValue = new BoolValue("Derp", true);
        wasInvisibleValue = new BoolValue("WasInvisible", false);
        armorValue = new BoolValue("Armor", false);
        pingValue = new BoolValue("Ping", false);
        needHitValue = new BoolValue("NeedHit", false);
        duplicateInWorldValue = new BoolValue("DuplicateInWorld", false);
        duplicateInTabValue = new BoolValue("DuplicateInTab", false);
        allwaysInRadiusValue = new BoolValue("AlwaysInRadius", false);
        allwaysRadiusValue = new FloatValue("AlwaysInRadiusBlocks", 20.0f, 5.0f, 30.0f);
        boolean bl = false;
        ground = new ArrayList();
        bl = false;
        air = new ArrayList();
        bl = false;
        invalidGround = new LinkedHashMap();
        bl = false;
        swing = new ArrayList();
        bl = false;
        invisible = new ArrayList();
        bl = false;
        hitted = new ArrayList();
        bl = false;
        notAlwaysInRadius = new ArrayList();
    }
}
