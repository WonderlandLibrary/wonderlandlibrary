/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.NoWhenBranchMatchedException
 *  kotlin.NotImplementedError
 *  kotlin.jvm.internal.Intrinsics
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
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.injection.backend.utils;

import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.NotImplementedError;
import kotlin.jvm.internal.Intrinsics;
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
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=2, d1={"\u0000\u0088\u0001\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u0086\b\u001a\r\u0010\u0003\u001a\u00020\u0004*\u00020\u0002H\u0086\b\u001a\r\u0010\u0005\u001a\u00020\u0002*\u00020\u0001H\u0086\b\u001a\r\u0010\u0006\u001a\u00020\u0007*\u00020\bH\u0086\b\u001a\r\u0010\u0006\u001a\u00020\t*\u00020\nH\u0086\b\u001a\r\u0010\u0006\u001a\u00020\u000b*\u00020\fH\u0086\b\u001a\r\u0010\u0006\u001a\u00020\r*\u00020\u000eH\u0086\b\u001a\r\u0010\u0006\u001a\u00020\u000f*\u00020\u0010H\u0086\b\u001a\r\u0010\u0006\u001a\u00020\u0011*\u00020\u0012H\u0086\b\u001a\r\u0010\u0006\u001a\u00020\u0013*\u00020\u0014H\u0086\b\u001a\r\u0010\u0006\u001a\u00020\u0015*\u00020\u0016H\u0086\b\u001a\r\u0010\u0006\u001a\u00020\u0017*\u00020\u0018H\u0086\b\u001a\r\u0010\u0006\u001a\u00020\u0019*\u00020\u001aH\u0086\b\u001a\r\u0010\u0006\u001a\u00020\u001b*\u00020\u001cH\u0086\b\u001a\r\u0010\u0006\u001a\u00020\u001d*\u00020\u001eH\u0086\b\u001a\r\u0010\u0006\u001a\u00020\u001f*\u00020 H\u0086\b\u001a\r\u0010!\u001a\u00020\n*\u00020\tH\u0086\b\u001a\r\u0010!\u001a\u00020\u0012*\u00020\u0011H\u0086\b\u001a\r\u0010!\u001a\u00020\u0016*\u00020\u0015H\u0086\b\u001a\r\u0010!\u001a\u00020\u0018*\u00020\u0017H\u0086\b\u001a\r\u0010!\u001a\u00020\"*\u00020#H\u0086\b\u001a\r\u0010!\u001a\u00020\u001c*\u00020\u001bH\u0086\b\u001a\r\u0010!\u001a\u00020\u001e*\u00020\u001dH\u0086\b\u001a\r\u0010!\u001a\u00020\u001a*\u00020\u0019H\u0086\b\u001a\r\u0010!\u001a\u00020 *\u00020\u001fH\u0086\b\u00a8\u0006$"}, d2={"toClickType", "Lnet/minecraft/inventory/ClickType;", "", "toEntityEquipmentSlot", "Lnet/minecraft/inventory/EntityEquipmentSlot;", "toInt", "unwrap", "Lnet/minecraft/util/EnumHand;", "Lnet/ccbluex/liquidbounce/api/enums/WEnumHand;", "Lnet/minecraft/entity/player/EnumPlayerModelParts;", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/player/WEnumPlayerModelParts;", "Lnet/minecraft/util/text/event/ClickEvent$Action;", "Lnet/ccbluex/liquidbounce/api/minecraft/event/IClickEvent$WAction;", "Lnet/minecraft/network/play/client/CPacketClientStatus$State;", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketClientStatus$WEnumState;", "Lnet/minecraft/network/play/client/CPacketEntityAction$Action;", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketEntityAction$WAction;", "Lnet/minecraft/network/play/client/CPacketPlayerDigging$Action;", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketPlayerDigging$WAction;", "Lnet/minecraft/network/play/client/CPacketResourcePackStatus$Action;", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketResourcePackStatus$WAction;", "Lnet/minecraft/network/play/client/CPacketUseEntity$Action;", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketUseEntity$WAction;", "Lnet/minecraft/util/math/BlockPos;", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "Lnet/minecraft/util/text/TextFormatting;", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WEnumChatFormatting;", "Lnet/minecraft/util/math/Vec3d;", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WVec3;", "Lnet/minecraft/util/math/Vec3i;", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WVec3i;", "Lnet/minecraft/world/GameType;", "Lnet/ccbluex/liquidbounce/api/minecraft/world/IWorldSettings$WGameType;", "wrap", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IMovingObjectPosition$WMovingObjectType;", "Lnet/minecraft/util/math/RayTraceResult$Type;", "LiKingSense"})
public final class BackendExtentionsKt {
    @NotNull
    public static final Vec3d unwrap(@NotNull WVec3 $this$unwrap) {
        int $i$f$unwrap = 0;
        Intrinsics.checkParameterIsNotNull((Object)$this$unwrap, (String)"$this$unwrap");
        return new Vec3d($this$unwrap.getXCoord(), $this$unwrap.getYCoord(), $this$unwrap.getZCoord());
    }

    @NotNull
    public static final Vec3i unwrap(@NotNull WVec3i $this$unwrap) {
        int $i$f$unwrap = 0;
        Intrinsics.checkParameterIsNotNull((Object)$this$unwrap, (String)"$this$unwrap");
        return new Vec3i($this$unwrap.getX(), $this$unwrap.getY(), $this$unwrap.getZ());
    }

    @NotNull
    public static final BlockPos unwrap(@NotNull WBlockPos $this$unwrap) {
        int $i$f$unwrap = 0;
        Intrinsics.checkParameterIsNotNull((Object)$this$unwrap, (String)"$this$unwrap");
        return new BlockPos($this$unwrap.getX(), $this$unwrap.getY(), $this$unwrap.getZ());
    }

    @NotNull
    public static final WBlockPos wrap(@NotNull BlockPos $this$wrap) {
        int $i$f$wrap = 0;
        Intrinsics.checkParameterIsNotNull((Object)$this$wrap, (String)"$this$wrap");
        return new WBlockPos($this$wrap.func_177958_n(), $this$wrap.func_177956_o(), $this$wrap.func_177952_p());
    }

    @NotNull
    public static final WVec3 wrap(@NotNull Vec3d $this$wrap) {
        int $i$f$wrap = 0;
        Intrinsics.checkParameterIsNotNull((Object)$this$wrap, (String)"$this$wrap");
        return new WVec3($this$wrap.field_72450_a, $this$wrap.field_72448_b, $this$wrap.field_72449_c);
    }

    @NotNull
    public static final WVec3i wrap(@NotNull Vec3i $this$wrap) {
        int $i$f$wrap = 0;
        Intrinsics.checkParameterIsNotNull((Object)$this$wrap, (String)"$this$wrap");
        return new WVec3i($this$wrap.func_177958_n(), $this$wrap.func_177956_o(), $this$wrap.func_177952_p());
    }

    @NotNull
    public static final IMovingObjectPosition.WMovingObjectType wrap(@NotNull RayTraceResult.Type $this$wrap) {
        IMovingObjectPosition.WMovingObjectType wMovingObjectType;
        int $i$f$wrap = 0;
        Intrinsics.checkParameterIsNotNull((Object)$this$wrap, (String)"$this$wrap");
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$0[$this$wrap.ordinal()]) {
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

    @NotNull
    public static final EnumPlayerModelParts unwrap(@NotNull WEnumPlayerModelParts $this$unwrap) {
        EnumPlayerModelParts enumPlayerModelParts;
        int $i$f$unwrap = 0;
        Intrinsics.checkParameterIsNotNull((Object)((Object)$this$unwrap), (String)"$this$unwrap");
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$1[$this$unwrap.ordinal()]) {
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

    @NotNull
    public static final WEnumPlayerModelParts wrap(@NotNull EnumPlayerModelParts $this$wrap) {
        WEnumPlayerModelParts wEnumPlayerModelParts;
        int $i$f$wrap = 0;
        Intrinsics.checkParameterIsNotNull((Object)$this$wrap, (String)"$this$wrap");
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$2[$this$wrap.ordinal()]) {
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

    @NotNull
    public static final WEnumChatFormatting wrap(@NotNull TextFormatting $this$wrap) {
        WEnumChatFormatting wEnumChatFormatting;
        int $i$f$wrap = 0;
        Intrinsics.checkParameterIsNotNull((Object)$this$wrap, (String)"$this$wrap");
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$3[$this$wrap.ordinal()]) {
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

    @NotNull
    public static final TextFormatting unwrap(@NotNull WEnumChatFormatting $this$unwrap) {
        TextFormatting textFormatting;
        int $i$f$unwrap = 0;
        Intrinsics.checkParameterIsNotNull((Object)((Object)$this$unwrap), (String)"$this$unwrap");
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$4[$this$unwrap.ordinal()]) {
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

    @NotNull
    public static final GameType unwrap(@NotNull IWorldSettings.WGameType $this$unwrap) {
        GameType gameType;
        int $i$f$unwrap = 0;
        Intrinsics.checkParameterIsNotNull((Object)((Object)$this$unwrap), (String)"$this$unwrap");
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$5[$this$unwrap.ordinal()]) {
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

    @NotNull
    public static final IWorldSettings.WGameType wrap(@NotNull GameType $this$wrap) {
        IWorldSettings.WGameType wGameType;
        int $i$f$wrap = 0;
        Intrinsics.checkParameterIsNotNull((Object)$this$wrap, (String)"$this$wrap");
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$6[$this$wrap.ordinal()]) {
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

    @NotNull
    public static final ICPacketUseEntity.WAction wrap(@NotNull CPacketUseEntity.Action $this$wrap) {
        ICPacketUseEntity.WAction wAction;
        int $i$f$wrap = 0;
        Intrinsics.checkParameterIsNotNull((Object)$this$wrap, (String)"$this$wrap");
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$7[$this$wrap.ordinal()]) {
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

    @NotNull
    public static final CPacketUseEntity.Action unwrap(@NotNull ICPacketUseEntity.WAction $this$unwrap) {
        CPacketUseEntity.Action action;
        int $i$f$unwrap = 0;
        Intrinsics.checkParameterIsNotNull((Object)((Object)$this$unwrap), (String)"$this$unwrap");
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$8[$this$unwrap.ordinal()]) {
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

    @NotNull
    public static final ICPacketPlayerDigging.WAction wrap(@NotNull CPacketPlayerDigging.Action $this$wrap) {
        ICPacketPlayerDigging.WAction wAction;
        int $i$f$wrap = 0;
        Intrinsics.checkParameterIsNotNull((Object)$this$wrap, (String)"$this$wrap");
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$9[$this$wrap.ordinal()]) {
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

    @NotNull
    public static final CPacketPlayerDigging.Action unwrap(@NotNull ICPacketPlayerDigging.WAction $this$unwrap) {
        CPacketPlayerDigging.Action action;
        int $i$f$unwrap = 0;
        Intrinsics.checkParameterIsNotNull((Object)((Object)$this$unwrap), (String)"$this$unwrap");
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$10[$this$unwrap.ordinal()]) {
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

    @NotNull
    public static final ClickEvent.Action unwrap(@NotNull IClickEvent.WAction $this$unwrap) {
        int $i$f$unwrap = 0;
        Intrinsics.checkParameterIsNotNull((Object)((Object)$this$unwrap), (String)"$this$unwrap");
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$11[$this$unwrap.ordinal()]) {
            case 1: {
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return ClickEvent.Action.OPEN_URL;
    }

    @NotNull
    public static final CPacketClientStatus.State unwrap(@NotNull ICPacketClientStatus.WEnumState $this$unwrap) {
        CPacketClientStatus.State state;
        int $i$f$unwrap = 0;
        Intrinsics.checkParameterIsNotNull((Object)((Object)$this$unwrap), (String)"$this$unwrap");
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$12[$this$unwrap.ordinal()]) {
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
                boolean $i$f$BACKEND_UNSUPPORTED = false;
                throw (Throwable)new NotImplementedError("1.12.2 doesn't support this feature'");
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return state;
    }

    @NotNull
    public static final CPacketResourcePackStatus.Action unwrap(@NotNull ICPacketResourcePackStatus.WAction $this$unwrap) {
        CPacketResourcePackStatus.Action action;
        int $i$f$unwrap = 0;
        Intrinsics.checkParameterIsNotNull((Object)((Object)$this$unwrap), (String)"$this$unwrap");
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$13[$this$unwrap.ordinal()]) {
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

    @NotNull
    public static final CPacketEntityAction.Action unwrap(@NotNull ICPacketEntityAction.WAction $this$unwrap) {
        CPacketEntityAction.Action action;
        int $i$f$unwrap = 0;
        Intrinsics.checkParameterIsNotNull((Object)((Object)$this$unwrap), (String)"$this$unwrap");
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$14[$this$unwrap.ordinal()]) {
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

    @NotNull
    public static final EntityEquipmentSlot toEntityEquipmentSlot(int $this$toEntityEquipmentSlot) {
        EntityEquipmentSlot entityEquipmentSlot;
        int $i$f$toEntityEquipmentSlot = 0;
        switch ($this$toEntityEquipmentSlot) {
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
                throw (Throwable)new IllegalArgumentException("Invalid armorType " + $this$toEntityEquipmentSlot);
            }
        }
        return entityEquipmentSlot;
    }

    @NotNull
    public static final ClickType toClickType(int $this$toClickType) {
        ClickType clickType;
        int $i$f$toClickType = 0;
        switch ($this$toClickType) {
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
                throw (Throwable)new IllegalArgumentException("Invalid mode " + $this$toClickType);
            }
        }
        return clickType;
    }

    public static final int toInt(@NotNull ClickType $this$toInt) {
        int n;
        int $i$f$toInt = 0;
        Intrinsics.checkParameterIsNotNull((Object)$this$toInt, (String)"$this$toInt");
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$15[$this$toInt.ordinal()]) {
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
                throw (Throwable)new IllegalArgumentException("Invalid mode " + $this$toInt);
            }
        }
        return n;
    }

    @NotNull
    public static final EnumHand unwrap(@NotNull WEnumHand $this$unwrap) {
        EnumHand enumHand;
        int $i$f$unwrap = 0;
        Intrinsics.checkParameterIsNotNull((Object)((Object)$this$unwrap), (String)"$this$unwrap");
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$16[$this$unwrap.ordinal()]) {
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
}

