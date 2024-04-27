/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.NoWhenBranchMatchedException
 *  kotlin.NotImplementedError
 *  net.minecraft.entity.player.EnumPlayerModelParts
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.inventory.EntityEquipmentSlot
 *  net.minecraft.network.play.client.CPacketClientStatus$State
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.network.play.client.CPacketResourcePackStatus$Action
 *  net.minecraft.network.play.client.CPacketUseEntity$Action
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 *  net.minecraft.util.text.TextFormatting
 *  net.minecraft.util.text.event.ClickEvent$Action
 *  net.minecraft.world.GameType
 */
package net.ccbluex.liquidbounce.injection.backend.utils;

import kotlin.NoWhenBranchMatchedException;
import kotlin.NotImplementedError;
import net.ccbluex.liquidbounce.api.enums.WEnumHand;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.player.WEnumPlayerModelParts;
import net.ccbluex.liquidbounce.api.minecraft.event.IClickEvent;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketClientStatus;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketEntityAction;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerDigging;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketResourcePackStatus;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketUseEntity;
import net.ccbluex.liquidbounce.api.minecraft.util.IMovingObjectPosition;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.util.WEnumChatFormatting;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3i;
import net.ccbluex.liquidbounce.api.minecraft.world.IWorldSettings;
import net.ccbluex.liquidbounce.injection.backend.Backend;
import net.ccbluex.liquidbounce.injection.backend.utils.BackendExtentionsKt$WhenMappings;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketResourcePackStatus;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.world.GameType;

public final class BackendExtentionsKt {
    public static final WEnumPlayerModelParts wrap(EnumPlayerModelParts enumPlayerModelParts) {
        WEnumPlayerModelParts wEnumPlayerModelParts;
        boolean bl = false;
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$2[enumPlayerModelParts.ordinal()]) {
            case 1: {
                wEnumPlayerModelParts = WEnumPlayerModelParts.CAPE;
                break;
            }
            case 2: {
                wEnumPlayerModelParts = WEnumPlayerModelParts.JACKET;
                break;
            }
            case 3: {
                wEnumPlayerModelParts = WEnumPlayerModelParts.LEFT_SLEEVE;
                break;
            }
            case 4: {
                wEnumPlayerModelParts = WEnumPlayerModelParts.RIGHT_SLEEVE;
                break;
            }
            case 5: {
                wEnumPlayerModelParts = WEnumPlayerModelParts.LEFT_PANTS_LEG;
                break;
            }
            case 6: {
                wEnumPlayerModelParts = WEnumPlayerModelParts.RIGHT_PANTS_LEG;
                break;
            }
            case 7: {
                wEnumPlayerModelParts = WEnumPlayerModelParts.HAT;
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return wEnumPlayerModelParts;
    }

    public static final ICPacketPlayerDigging.WAction wrap(CPacketPlayerDigging.Action action) {
        ICPacketPlayerDigging.WAction wAction;
        boolean bl = false;
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$9[action.ordinal()]) {
            case 1: {
                wAction = ICPacketPlayerDigging.WAction.ABORT_DESTROY_BLOCK;
                break;
            }
            case 2: {
                wAction = ICPacketPlayerDigging.WAction.DROP_ALL_ITEMS;
                break;
            }
            case 3: {
                wAction = ICPacketPlayerDigging.WAction.DROP_ITEM;
                break;
            }
            case 4: {
                wAction = ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM;
                break;
            }
            case 5: {
                wAction = ICPacketPlayerDigging.WAction.START_DESTROY_BLOCK;
                break;
            }
            case 6: {
                wAction = ICPacketPlayerDigging.WAction.STOP_DESTROY_BLOCK;
                break;
            }
            case 7: {
                wAction = ICPacketPlayerDigging.WAction.SWAP_HELD_ITEMS;
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return wAction;
    }

    public static final int toInt(ClickType clickType) {
        int n;
        boolean bl = false;
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$15[clickType.ordinal()]) {
            case 1: {
                n = 0;
                break;
            }
            case 2: {
                n = 1;
                break;
            }
            case 3: {
                n = 2;
                break;
            }
            case 4: {
                n = 3;
                break;
            }
            case 5: {
                n = 4;
                break;
            }
            case 6: {
                n = 5;
                break;
            }
            case 7: {
                n = 6;
                break;
            }
            default: {
                throw (Throwable)new IllegalArgumentException("Invalid mode " + clickType);
            }
        }
        return n;
    }

    public static final WEnumChatFormatting wrap(TextFormatting textFormatting) {
        WEnumChatFormatting wEnumChatFormatting;
        boolean bl = false;
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$3[textFormatting.ordinal()]) {
            case 1: {
                wEnumChatFormatting = WEnumChatFormatting.BLACK;
                break;
            }
            case 2: {
                wEnumChatFormatting = WEnumChatFormatting.DARK_BLUE;
                break;
            }
            case 3: {
                wEnumChatFormatting = WEnumChatFormatting.DARK_GREEN;
                break;
            }
            case 4: {
                wEnumChatFormatting = WEnumChatFormatting.DARK_AQUA;
                break;
            }
            case 5: {
                wEnumChatFormatting = WEnumChatFormatting.DARK_RED;
                break;
            }
            case 6: {
                wEnumChatFormatting = WEnumChatFormatting.DARK_PURPLE;
                break;
            }
            case 7: {
                wEnumChatFormatting = WEnumChatFormatting.GOLD;
                break;
            }
            case 8: {
                wEnumChatFormatting = WEnumChatFormatting.GRAY;
                break;
            }
            case 9: {
                wEnumChatFormatting = WEnumChatFormatting.DARK_GRAY;
                break;
            }
            case 10: {
                wEnumChatFormatting = WEnumChatFormatting.BLUE;
                break;
            }
            case 11: {
                wEnumChatFormatting = WEnumChatFormatting.GREEN;
                break;
            }
            case 12: {
                wEnumChatFormatting = WEnumChatFormatting.AQUA;
                break;
            }
            case 13: {
                wEnumChatFormatting = WEnumChatFormatting.RED;
                break;
            }
            case 14: {
                wEnumChatFormatting = WEnumChatFormatting.LIGHT_PURPLE;
                break;
            }
            case 15: {
                wEnumChatFormatting = WEnumChatFormatting.YELLOW;
                break;
            }
            case 16: {
                wEnumChatFormatting = WEnumChatFormatting.WHITE;
                break;
            }
            case 17: {
                wEnumChatFormatting = WEnumChatFormatting.OBFUSCATED;
                break;
            }
            case 18: {
                wEnumChatFormatting = WEnumChatFormatting.BOLD;
                break;
            }
            case 19: {
                wEnumChatFormatting = WEnumChatFormatting.STRIKETHROUGH;
                break;
            }
            case 20: {
                wEnumChatFormatting = WEnumChatFormatting.UNDERLINE;
                break;
            }
            case 21: {
                wEnumChatFormatting = WEnumChatFormatting.ITALIC;
                break;
            }
            case 22: {
                wEnumChatFormatting = WEnumChatFormatting.RESET;
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return wEnumChatFormatting;
    }

    public static final CPacketClientStatus.State unwrap(ICPacketClientStatus.WEnumState wEnumState) {
        CPacketClientStatus.State state;
        boolean bl = false;
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$12[wEnumState.ordinal()]) {
            case 1: {
                state = CPacketClientStatus.State.PERFORM_RESPAWN;
                break;
            }
            case 2: {
                state = CPacketClientStatus.State.REQUEST_STATS;
                break;
            }
            case 3: {
                Backend backend = Backend.INSTANCE;
                boolean bl2 = false;
                throw (Throwable)new NotImplementedError("1.12.2 doesn't support this feature'");
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return state;
    }

    public static final CPacketPlayerDigging.Action unwrap(ICPacketPlayerDigging.WAction wAction) {
        CPacketPlayerDigging.Action action;
        boolean bl = false;
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$10[wAction.ordinal()]) {
            case 1: {
                action = CPacketPlayerDigging.Action.START_DESTROY_BLOCK;
                break;
            }
            case 2: {
                action = CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK;
                break;
            }
            case 3: {
                action = CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK;
                break;
            }
            case 4: {
                action = CPacketPlayerDigging.Action.DROP_ALL_ITEMS;
                break;
            }
            case 5: {
                action = CPacketPlayerDigging.Action.DROP_ITEM;
                break;
            }
            case 6: {
                action = CPacketPlayerDigging.Action.RELEASE_USE_ITEM;
                break;
            }
            case 7: {
                action = CPacketPlayerDigging.Action.SWAP_HELD_ITEMS;
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return action;
    }

    public static final IMovingObjectPosition.WMovingObjectType wrap(RayTraceResult.Type type) {
        IMovingObjectPosition.WMovingObjectType wMovingObjectType;
        boolean bl = false;
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$0[type.ordinal()]) {
            case 1: {
                wMovingObjectType = IMovingObjectPosition.WMovingObjectType.MISS;
                break;
            }
            case 2: {
                wMovingObjectType = IMovingObjectPosition.WMovingObjectType.BLOCK;
                break;
            }
            case 3: {
                wMovingObjectType = IMovingObjectPosition.WMovingObjectType.ENTITY;
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return wMovingObjectType;
    }

    public static final GameType unwrap(IWorldSettings.WGameType wGameType) {
        GameType gameType;
        boolean bl = false;
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$5[wGameType.ordinal()]) {
            case 1: {
                gameType = GameType.NOT_SET;
                break;
            }
            case 2: {
                gameType = GameType.SURVIVAL;
                break;
            }
            case 3: {
                gameType = GameType.CREATIVE;
                break;
            }
            case 4: {
                gameType = GameType.ADVENTURE;
                break;
            }
            case 5: {
                gameType = GameType.SPECTATOR;
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return gameType;
    }

    public static final EnumHand unwrap(WEnumHand wEnumHand) {
        EnumHand enumHand;
        boolean bl = false;
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$16[wEnumHand.ordinal()]) {
            case 1: {
                enumHand = EnumHand.MAIN_HAND;
                break;
            }
            case 2: {
                enumHand = EnumHand.OFF_HAND;
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return enumHand;
    }

    public static final CPacketResourcePackStatus.Action unwrap(ICPacketResourcePackStatus.WAction wAction) {
        CPacketResourcePackStatus.Action action;
        boolean bl = false;
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$13[wAction.ordinal()]) {
            case 1: {
                action = CPacketResourcePackStatus.Action.SUCCESSFULLY_LOADED;
                break;
            }
            case 2: {
                action = CPacketResourcePackStatus.Action.DECLINED;
                break;
            }
            case 3: {
                action = CPacketResourcePackStatus.Action.FAILED_DOWNLOAD;
                break;
            }
            case 4: {
                action = CPacketResourcePackStatus.Action.ACCEPTED;
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return action;
    }

    public static final WBlockPos wrap(BlockPos blockPos) {
        boolean bl = false;
        return new WBlockPos(blockPos.func_177958_n(), blockPos.func_177956_o(), blockPos.func_177952_p());
    }

    public static final TextFormatting unwrap(WEnumChatFormatting wEnumChatFormatting) {
        TextFormatting textFormatting;
        boolean bl = false;
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$4[wEnumChatFormatting.ordinal()]) {
            case 1: {
                textFormatting = TextFormatting.BLACK;
                break;
            }
            case 2: {
                textFormatting = TextFormatting.DARK_BLUE;
                break;
            }
            case 3: {
                textFormatting = TextFormatting.DARK_GREEN;
                break;
            }
            case 4: {
                textFormatting = TextFormatting.DARK_AQUA;
                break;
            }
            case 5: {
                textFormatting = TextFormatting.DARK_RED;
                break;
            }
            case 6: {
                textFormatting = TextFormatting.DARK_PURPLE;
                break;
            }
            case 7: {
                textFormatting = TextFormatting.GOLD;
                break;
            }
            case 8: {
                textFormatting = TextFormatting.GRAY;
                break;
            }
            case 9: {
                textFormatting = TextFormatting.DARK_GRAY;
                break;
            }
            case 10: {
                textFormatting = TextFormatting.BLUE;
                break;
            }
            case 11: {
                textFormatting = TextFormatting.GREEN;
                break;
            }
            case 12: {
                textFormatting = TextFormatting.AQUA;
                break;
            }
            case 13: {
                textFormatting = TextFormatting.RED;
                break;
            }
            case 14: {
                textFormatting = TextFormatting.LIGHT_PURPLE;
                break;
            }
            case 15: {
                textFormatting = TextFormatting.YELLOW;
                break;
            }
            case 16: {
                textFormatting = TextFormatting.WHITE;
                break;
            }
            case 17: {
                textFormatting = TextFormatting.OBFUSCATED;
                break;
            }
            case 18: {
                textFormatting = TextFormatting.BOLD;
                break;
            }
            case 19: {
                textFormatting = TextFormatting.STRIKETHROUGH;
                break;
            }
            case 20: {
                textFormatting = TextFormatting.UNDERLINE;
                break;
            }
            case 21: {
                textFormatting = TextFormatting.ITALIC;
                break;
            }
            case 22: {
                textFormatting = TextFormatting.RESET;
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return textFormatting;
    }

    public static final CPacketUseEntity.Action unwrap(ICPacketUseEntity.WAction wAction) {
        CPacketUseEntity.Action action;
        boolean bl = false;
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$8[wAction.ordinal()]) {
            case 1: {
                action = CPacketUseEntity.Action.INTERACT;
                break;
            }
            case 2: {
                action = CPacketUseEntity.Action.ATTACK;
                break;
            }
            case 3: {
                action = CPacketUseEntity.Action.INTERACT_AT;
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return action;
    }

    public static final EnumPlayerModelParts unwrap(WEnumPlayerModelParts wEnumPlayerModelParts) {
        EnumPlayerModelParts enumPlayerModelParts;
        boolean bl = false;
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$1[wEnumPlayerModelParts.ordinal()]) {
            case 1: {
                enumPlayerModelParts = EnumPlayerModelParts.CAPE;
                break;
            }
            case 2: {
                enumPlayerModelParts = EnumPlayerModelParts.JACKET;
                break;
            }
            case 3: {
                enumPlayerModelParts = EnumPlayerModelParts.LEFT_SLEEVE;
                break;
            }
            case 4: {
                enumPlayerModelParts = EnumPlayerModelParts.RIGHT_SLEEVE;
                break;
            }
            case 5: {
                enumPlayerModelParts = EnumPlayerModelParts.LEFT_PANTS_LEG;
                break;
            }
            case 6: {
                enumPlayerModelParts = EnumPlayerModelParts.RIGHT_PANTS_LEG;
                break;
            }
            case 7: {
                enumPlayerModelParts = EnumPlayerModelParts.HAT;
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return enumPlayerModelParts;
    }

    public static final ICPacketUseEntity.WAction wrap(CPacketUseEntity.Action action) {
        ICPacketUseEntity.WAction wAction;
        boolean bl = false;
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$7[action.ordinal()]) {
            case 1: {
                wAction = ICPacketUseEntity.WAction.INTERACT;
                break;
            }
            case 2: {
                wAction = ICPacketUseEntity.WAction.ATTACK;
                break;
            }
            case 3: {
                wAction = ICPacketUseEntity.WAction.INTERACT_AT;
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return wAction;
    }

    public static final ClickType toClickType(int n) {
        ClickType clickType;
        boolean bl = false;
        switch (n) {
            case 0: {
                clickType = ClickType.PICKUP;
                break;
            }
            case 1: {
                clickType = ClickType.QUICK_MOVE;
                break;
            }
            case 2: {
                clickType = ClickType.SWAP;
                break;
            }
            case 3: {
                clickType = ClickType.CLONE;
                break;
            }
            case 4: {
                clickType = ClickType.THROW;
                break;
            }
            case 5: {
                clickType = ClickType.QUICK_CRAFT;
                break;
            }
            case 6: {
                clickType = ClickType.PICKUP_ALL;
                break;
            }
            default: {
                throw (Throwable)new IllegalArgumentException("Invalid mode " + n);
            }
        }
        return clickType;
    }

    public static final CPacketEntityAction.Action unwrap(ICPacketEntityAction.WAction wAction) {
        CPacketEntityAction.Action action;
        boolean bl = false;
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$14[wAction.ordinal()]) {
            case 1: {
                action = CPacketEntityAction.Action.START_SNEAKING;
                break;
            }
            case 2: {
                action = CPacketEntityAction.Action.STOP_SNEAKING;
                break;
            }
            case 3: {
                action = CPacketEntityAction.Action.STOP_SLEEPING;
                break;
            }
            case 4: {
                action = CPacketEntityAction.Action.START_SPRINTING;
                break;
            }
            case 5: {
                action = CPacketEntityAction.Action.STOP_SPRINTING;
                break;
            }
            case 6: {
                action = CPacketEntityAction.Action.OPEN_INVENTORY;
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return action;
    }

    public static final Vec3i unwrap(WVec3i wVec3i) {
        boolean bl = false;
        return new Vec3i(wVec3i.getX(), wVec3i.getY(), wVec3i.getZ());
    }

    public static final BlockPos unwrap(WBlockPos wBlockPos) {
        boolean bl = false;
        return new BlockPos(wBlockPos.getX(), wBlockPos.getY(), wBlockPos.getZ());
    }

    public static final EntityEquipmentSlot toEntityEquipmentSlot(int n) {
        EntityEquipmentSlot entityEquipmentSlot;
        boolean bl = false;
        switch (n) {
            case 0: {
                entityEquipmentSlot = EntityEquipmentSlot.FEET;
                break;
            }
            case 1: {
                entityEquipmentSlot = EntityEquipmentSlot.LEGS;
                break;
            }
            case 2: {
                entityEquipmentSlot = EntityEquipmentSlot.CHEST;
                break;
            }
            case 3: {
                entityEquipmentSlot = EntityEquipmentSlot.HEAD;
                break;
            }
            case 4: {
                entityEquipmentSlot = EntityEquipmentSlot.MAINHAND;
                break;
            }
            case 5: {
                entityEquipmentSlot = EntityEquipmentSlot.OFFHAND;
                break;
            }
            default: {
                throw (Throwable)new IllegalArgumentException("Invalid armorType " + n);
            }
        }
        return entityEquipmentSlot;
    }

    public static final WVec3 wrap(Vec3d vec3d) {
        boolean bl = false;
        return new WVec3(vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c);
    }

    public static final IWorldSettings.WGameType wrap(GameType gameType) {
        IWorldSettings.WGameType wGameType;
        boolean bl = false;
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$6[gameType.ordinal()]) {
            case 1: {
                wGameType = IWorldSettings.WGameType.NOT_SET;
                break;
            }
            case 2: {
                wGameType = IWorldSettings.WGameType.SURVIVAL;
                break;
            }
            case 3: {
                wGameType = IWorldSettings.WGameType.CREATIVE;
                break;
            }
            case 4: {
                wGameType = IWorldSettings.WGameType.ADVENTUR;
                break;
            }
            case 5: {
                wGameType = IWorldSettings.WGameType.SPECTATOR;
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return wGameType;
    }

    public static final Vec3d unwrap(WVec3 wVec3) {
        boolean bl = false;
        return new Vec3d(wVec3.getXCoord(), wVec3.getYCoord(), wVec3.getZCoord());
    }

    public static final ClickEvent.Action unwrap(IClickEvent.WAction wAction) {
        boolean bl = false;
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$11[wAction.ordinal()]) {
            case 1: {
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return ClickEvent.Action.OPEN_URL;
    }

    public static final WVec3i wrap(Vec3i vec3i) {
        boolean bl = false;
        return new WVec3i(vec3i.func_177958_n(), vec3i.func_177956_o(), vec3i.func_177952_p());
    }
}

