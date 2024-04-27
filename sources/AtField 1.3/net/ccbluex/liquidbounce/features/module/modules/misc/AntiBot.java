/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.JvmStatic
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.misc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.client.network.INetworkPlayerInfo;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.util.IIChatComponent;
import net.ccbluex.liquidbounce.event.AttackEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.WorldEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.extensions.NetworkPlayerInfoKt;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="AntiBot", description="Prevents KillAura from attacking AntiCheat bots.", category=ModuleCategory.MISC)
public final class AntiBot
extends Module {
    private static final List hitted;
    private static final IntegerValue livingTimeTicksValue;
    private static final BoolValue colorValue;
    private static final FloatValue allwaysRadiusValue;
    private static final BoolValue airValue;
    private static final BoolValue derpValue;
    private static final ListValue tabModeValue;
    private static final BoolValue wasInvisibleValue;
    public static final AntiBot INSTANCE;
    private static final List air;
    private static final List invisible;
    private static final BoolValue healthValue;
    private static final BoolValue allwaysInRadiusValue;
    private static final BoolValue duplicateInTabValue;
    private static final BoolValue armorValue;
    private static final List swing;
    private static final List ground;
    private static final BoolValue duplicateInWorldValue;
    private static final BoolValue livingTimeValue;
    private static final List notAlwaysInRadius;
    private static final BoolValue groundValue;
    private static final BoolValue needHitValue;
    private static final BoolValue entityIDValue;
    private static final Map invalidGround;
    private static final BoolValue invalidGroundValue;
    private static final BoolValue tabValue;
    private static final BoolValue swingValue;
    private static final BoolValue pingValue;

    @EventTarget
    public final void onPacket(PacketEvent packetEvent) {
        IEntity iEntity;
        IPacket iPacket;
        if (MinecraftInstance.mc.getThePlayer() == null || MinecraftInstance.mc.getTheWorld() == null) {
            return;
        }
        IPacket iPacket2 = packetEvent.getPacket();
        if (MinecraftInstance.classProvider.isSPacketEntity(iPacket2)) {
            iPacket = iPacket2.asSPacketEntity();
            IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
            if (iWorldClient == null) {
                Intrinsics.throwNpe();
            }
            if (MinecraftInstance.classProvider.isEntityPlayer(iEntity = iPacket.getEntity(iWorldClient)) && iEntity != null) {
                if (iPacket.getOnGround() && !ground.contains(iEntity.getEntityId())) {
                    ground.add(iEntity.getEntityId());
                }
                if (!iPacket.getOnGround() && !air.contains(iEntity.getEntityId())) {
                    air.add(iEntity.getEntityId());
                }
                if (iPacket.getOnGround()) {
                    if (iEntity.getPrevPosY() != iEntity.getPosY()) {
                        invalidGround.put(iEntity.getEntityId(), ((Number)invalidGround.getOrDefault(iEntity.getEntityId(), 0)).intValue() + 1);
                    }
                } else {
                    int n = ((Number)invalidGround.getOrDefault(iEntity.getEntityId(), 0)).intValue() / 2;
                    if (n <= 0) {
                        invalidGround.remove(iEntity.getEntityId());
                    } else {
                        invalidGround.put(iEntity.getEntityId(), n);
                    }
                }
                if (iEntity.isInvisible() && !invisible.contains(iEntity.getEntityId())) {
                    invisible.add(iEntity.getEntityId());
                }
                if (!notAlwaysInRadius.contains(iEntity.getEntityId())) {
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iEntityPlayerSP.getDistanceToEntity(iEntity) > ((Number)allwaysRadiusValue.get()).floatValue()) {
                        notAlwaysInRadius.add(iEntity.getEntityId());
                    }
                }
            }
        }
        if (MinecraftInstance.classProvider.isSPacketAnimation(iPacket2)) {
            iPacket = iPacket2.asSPacketAnimation();
            IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
            if (iWorldClient == null) {
                Intrinsics.throwNpe();
            }
            if ((iEntity = iWorldClient.getEntityByID(iPacket.getEntityID())) != null && MinecraftInstance.classProvider.isEntityLivingBase(iEntity) && iPacket.getAnimationType() == 0 && !swing.contains(iEntity.getEntityId())) {
                swing.add(iEntity.getEntityId());
            }
        }
    }

    @EventTarget
    public final void onAttack(AttackEvent attackEvent) {
        IEntity iEntity = attackEvent.getTargetEntity();
        if (iEntity != null && MinecraftInstance.classProvider.isEntityLivingBase(iEntity) && !hitted.contains(iEntity.getEntityId())) {
            hitted.add(iEntity.getEntityId());
        }
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

    @Override
    public void onDisable() {
        this.clearAll();
        super.onDisable();
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

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @JvmStatic
    public static final boolean isBot(IEntityLivingBase iEntityLivingBase) {
        boolean bl;
        Object object;
        Object t;
        Iterator iterator2;
        boolean bl2;
        Collection collection;
        Object object2;
        Object object3;
        if (!MinecraftInstance.classProvider.isEntityPlayer(iEntityLivingBase)) {
            return false;
        }
        if (!INSTANCE.getState()) {
            return false;
        }
        if (((Boolean)colorValue.get()).booleanValue()) {
            IIChatComponent iIChatComponent = iEntityLivingBase.getDisplayName();
            if (iIChatComponent == null) {
                Intrinsics.throwNpe();
            }
            if (!StringsKt.replace$default((String)iIChatComponent.getFormattedText(), (String)"\u00a7r", (String)"", (boolean)false, (int)4, null).equals("\u00a7")) {
                return true;
            }
        }
        if (((Boolean)livingTimeValue.get()).booleanValue() && iEntityLivingBase.getTicksExisted() < ((Number)livingTimeTicksValue.get()).intValue()) {
            return true;
        }
        if (((Boolean)groundValue.get()).booleanValue() && !ground.contains(iEntityLivingBase.getEntityId())) {
            return true;
        }
        if (((Boolean)airValue.get()).booleanValue() && !air.contains(iEntityLivingBase.getEntityId())) {
            return true;
        }
        if (((Boolean)swingValue.get()).booleanValue() && !swing.contains(iEntityLivingBase.getEntityId())) {
            return true;
        }
        if (((Boolean)healthValue.get()).booleanValue() && iEntityLivingBase.getHealth() > 20.0f) {
            return true;
        }
        if (((Boolean)entityIDValue.get()).booleanValue()) {
            if (iEntityLivingBase.getEntityId() >= 1000000000) return true;
            if (iEntityLivingBase.getEntityId() <= -1) {
                return true;
            }
        }
        if (((Boolean)derpValue.get()).booleanValue()) {
            if (iEntityLivingBase.getRotationPitch() > 90.0f) return true;
            if (iEntityLivingBase.getRotationPitch() < -90.0f) {
                return true;
            }
        }
        if (((Boolean)wasInvisibleValue.get()).booleanValue() && invisible.contains(iEntityLivingBase.getEntityId())) {
            return true;
        }
        if (((Boolean)armorValue.get()).booleanValue() && (object3 = iEntityLivingBase.asEntityPlayer()).getInventory().getArmorInventory().get(0) == null && object3.getInventory().getArmorInventory().get(1) == null && object3.getInventory().getArmorInventory().get(2) == null && object3.getInventory().getArmorInventory().get(3) == null) {
            return true;
        }
        if (((Boolean)pingValue.get()).booleanValue()) {
            INetworkPlayerInfo iNetworkPlayerInfo = MinecraftInstance.mc.getNetHandler().getPlayerInfo(iEntityLivingBase.asEntityPlayer().getUniqueID());
            if (iNetworkPlayerInfo != null) {
                if (iNetworkPlayerInfo.getResponseTime() == 0) {
                    return true;
                }
            }
        }
        if (((Boolean)needHitValue.get()).booleanValue() && !hitted.contains(iEntityLivingBase.getEntityId())) {
            return true;
        }
        if (((Boolean)invalidGroundValue.get()).booleanValue() && ((Number)invalidGround.getOrDefault(iEntityLivingBase.getEntityId(), 0)).intValue() >= 10) {
            return true;
        }
        if (((Boolean)tabValue.get()).booleanValue()) {
            String string;
            boolean bl3 = StringsKt.equals((String)((String)tabModeValue.get()), (String)"Equals", (boolean)true);
            IIChatComponent iIChatComponent = iEntityLivingBase.getDisplayName();
            if (iIChatComponent == null) {
                Intrinsics.throwNpe();
            }
            if ((string = ColorUtils.stripColor(iIChatComponent.getFormattedText())) != null) {
                Iterator iterator3 = MinecraftInstance.mc.getNetHandler().getPlayerInfoMap().iterator();
                while (iterator3.hasNext()) {
                    String string2;
                    INetworkPlayerInfo iNetworkPlayerInfo = (INetworkPlayerInfo)iterator3.next();
                    if (ColorUtils.stripColor(NetworkPlayerInfoKt.getFullName(iNetworkPlayerInfo)) == null) {
                        continue;
                    }
                    if (bl3 ? string.equals(string2) : string.equals(string2)) return false;
                }
                return true;
            }
        }
        if (((Boolean)duplicateInWorldValue.get()).booleanValue()) {
            IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
            if (iWorldClient == null) {
                Intrinsics.throwNpe();
            }
            object3 = iWorldClient.getLoadedEntityList();
            boolean bl4 = false;
            object2 = object3;
            collection = new ArrayList();
            bl2 = false;
            iterator2 = object2.iterator();
            while (iterator2.hasNext()) {
                t = iterator2.next();
                object = (IEntity)t;
                bl = false;
                if (!(MinecraftInstance.classProvider.isEntityPlayer(object) && object.asEntityPlayer().getDisplayNameString().equals(object.asEntityPlayer().getDisplayNameString()))) continue;
                collection.add(t);
            }
            object3 = (List)collection;
            bl4 = false;
            if (object3.size() > 1) {
                return true;
            }
        }
        if (((Boolean)duplicateInTabValue.get()).booleanValue()) {
            object3 = MinecraftInstance.mc.getNetHandler().getPlayerInfoMap();
            boolean bl5 = false;
            object2 = object3;
            collection = new ArrayList();
            bl2 = false;
            iterator2 = object2.iterator();
            while (iterator2.hasNext()) {
                t = iterator2.next();
                object = (INetworkPlayerInfo)t;
                bl = false;
                if (!iEntityLivingBase.getName().equals(ColorUtils.stripColor(NetworkPlayerInfoKt.getFullName((INetworkPlayerInfo)object)))) continue;
                collection.add(t);
            }
            object3 = (List)collection;
            bl5 = false;
            if (object3.size() > 1) {
                return true;
            }
        }
        if (((Boolean)allwaysInRadiusValue.get()).booleanValue() && !notAlwaysInRadius.contains(iEntityLivingBase.getEntityId())) {
            return true;
        }
        String string = iEntityLivingBase.getName();
        if (string == null) {
            Intrinsics.throwNpe();
        }
        object3 = string;
        boolean bl6 = false;
        if (object3.length() == 0) {
            return true;
        }
        boolean bl7 = false;
        if (bl7) return true;
        String string3 = iEntityLivingBase.getName();
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (!string3.equals(iEntityPlayerSP.getName())) return false;
        return true;
    }

    @EventTarget
    public final void onWorld(@Nullable WorldEvent worldEvent) {
        this.clearAll();
    }
}

