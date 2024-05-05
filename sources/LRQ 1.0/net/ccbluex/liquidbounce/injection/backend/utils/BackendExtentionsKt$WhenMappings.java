/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EnumPlayerModelParts
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.network.play.client.CPacketUseEntity$Action
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraft.util.text.TextFormatting
 *  net.minecraft.world.GameType
 */
package net.ccbluex.liquidbounce.injection.backend.utils;

import net.ccbluex.liquidbounce.api.enums.WEnumHand;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.player.WEnumPlayerModelParts;
import net.ccbluex.liquidbounce.api.minecraft.event.IClickEvent;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketClientStatus;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketEntityAction;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerDigging;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketResourcePackStatus;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketUseEntity;
import net.ccbluex.liquidbounce.api.minecraft.util.WEnumChatFormatting;
import net.ccbluex.liquidbounce.api.minecraft.world.IWorldSettings;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;

public final class BackendExtentionsKt$WhenMappings {
    public static final /* synthetic */ int[] $EnumSwitchMapping$0;
    public static final /* synthetic */ int[] $EnumSwitchMapping$1;
    public static final /* synthetic */ int[] $EnumSwitchMapping$2;
    public static final /* synthetic */ int[] $EnumSwitchMapping$3;
    public static final /* synthetic */ int[] $EnumSwitchMapping$4;
    public static final /* synthetic */ int[] $EnumSwitchMapping$5;
    public static final /* synthetic */ int[] $EnumSwitchMapping$6;
    public static final /* synthetic */ int[] $EnumSwitchMapping$7;
    public static final /* synthetic */ int[] $EnumSwitchMapping$8;
    public static final /* synthetic */ int[] $EnumSwitchMapping$9;
    public static final /* synthetic */ int[] $EnumSwitchMapping$10;
    public static final /* synthetic */ int[] $EnumSwitchMapping$11;
    public static final /* synthetic */ int[] $EnumSwitchMapping$12;
    public static final /* synthetic */ int[] $EnumSwitchMapping$13;
    public static final /* synthetic */ int[] $EnumSwitchMapping$14;
    public static final /* synthetic */ int[] $EnumSwitchMapping$15;
    public static final /* synthetic */ int[] $EnumSwitchMapping$16;

    static {
        $EnumSwitchMapping$0 = new int[RayTraceResult.Type.values().length];
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$0[RayTraceResult.Type.MISS.ordinal()] = 1;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$0[RayTraceResult.Type.BLOCK.ordinal()] = 2;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$0[RayTraceResult.Type.ENTITY.ordinal()] = 3;
        $EnumSwitchMapping$1 = new int[WEnumPlayerModelParts.values().length];
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$1[WEnumPlayerModelParts.CAPE.ordinal()] = 1;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$1[WEnumPlayerModelParts.JACKET.ordinal()] = 2;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$1[WEnumPlayerModelParts.LEFT_SLEEVE.ordinal()] = 3;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$1[WEnumPlayerModelParts.RIGHT_SLEEVE.ordinal()] = 4;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$1[WEnumPlayerModelParts.LEFT_PANTS_LEG.ordinal()] = 5;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$1[WEnumPlayerModelParts.RIGHT_PANTS_LEG.ordinal()] = 6;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$1[WEnumPlayerModelParts.HAT.ordinal()] = 7;
        $EnumSwitchMapping$2 = new int[EnumPlayerModelParts.values().length];
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$2[EnumPlayerModelParts.CAPE.ordinal()] = 1;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$2[EnumPlayerModelParts.JACKET.ordinal()] = 2;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$2[EnumPlayerModelParts.LEFT_SLEEVE.ordinal()] = 3;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$2[EnumPlayerModelParts.RIGHT_SLEEVE.ordinal()] = 4;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$2[EnumPlayerModelParts.LEFT_PANTS_LEG.ordinal()] = 5;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$2[EnumPlayerModelParts.RIGHT_PANTS_LEG.ordinal()] = 6;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$2[EnumPlayerModelParts.HAT.ordinal()] = 7;
        $EnumSwitchMapping$3 = new int[TextFormatting.values().length];
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$3[TextFormatting.BLACK.ordinal()] = 1;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$3[TextFormatting.DARK_BLUE.ordinal()] = 2;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$3[TextFormatting.DARK_GREEN.ordinal()] = 3;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$3[TextFormatting.DARK_AQUA.ordinal()] = 4;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$3[TextFormatting.DARK_RED.ordinal()] = 5;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$3[TextFormatting.DARK_PURPLE.ordinal()] = 6;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$3[TextFormatting.GOLD.ordinal()] = 7;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$3[TextFormatting.GRAY.ordinal()] = 8;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$3[TextFormatting.DARK_GRAY.ordinal()] = 9;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$3[TextFormatting.BLUE.ordinal()] = 10;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$3[TextFormatting.GREEN.ordinal()] = 11;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$3[TextFormatting.AQUA.ordinal()] = 12;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$3[TextFormatting.RED.ordinal()] = 13;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$3[TextFormatting.LIGHT_PURPLE.ordinal()] = 14;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$3[TextFormatting.YELLOW.ordinal()] = 15;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$3[TextFormatting.WHITE.ordinal()] = 16;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$3[TextFormatting.OBFUSCATED.ordinal()] = 17;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$3[TextFormatting.BOLD.ordinal()] = 18;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$3[TextFormatting.STRIKETHROUGH.ordinal()] = 19;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$3[TextFormatting.UNDERLINE.ordinal()] = 20;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$3[TextFormatting.ITALIC.ordinal()] = 21;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$3[TextFormatting.RESET.ordinal()] = 22;
        $EnumSwitchMapping$4 = new int[WEnumChatFormatting.values().length];
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$4[WEnumChatFormatting.BLACK.ordinal()] = 1;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$4[WEnumChatFormatting.DARK_BLUE.ordinal()] = 2;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$4[WEnumChatFormatting.DARK_GREEN.ordinal()] = 3;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$4[WEnumChatFormatting.DARK_AQUA.ordinal()] = 4;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$4[WEnumChatFormatting.DARK_RED.ordinal()] = 5;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$4[WEnumChatFormatting.DARK_PURPLE.ordinal()] = 6;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$4[WEnumChatFormatting.GOLD.ordinal()] = 7;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$4[WEnumChatFormatting.GRAY.ordinal()] = 8;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$4[WEnumChatFormatting.DARK_GRAY.ordinal()] = 9;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$4[WEnumChatFormatting.BLUE.ordinal()] = 10;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$4[WEnumChatFormatting.GREEN.ordinal()] = 11;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$4[WEnumChatFormatting.AQUA.ordinal()] = 12;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$4[WEnumChatFormatting.RED.ordinal()] = 13;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$4[WEnumChatFormatting.LIGHT_PURPLE.ordinal()] = 14;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$4[WEnumChatFormatting.YELLOW.ordinal()] = 15;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$4[WEnumChatFormatting.WHITE.ordinal()] = 16;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$4[WEnumChatFormatting.OBFUSCATED.ordinal()] = 17;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$4[WEnumChatFormatting.BOLD.ordinal()] = 18;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$4[WEnumChatFormatting.STRIKETHROUGH.ordinal()] = 19;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$4[WEnumChatFormatting.UNDERLINE.ordinal()] = 20;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$4[WEnumChatFormatting.ITALIC.ordinal()] = 21;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$4[WEnumChatFormatting.RESET.ordinal()] = 22;
        $EnumSwitchMapping$5 = new int[IWorldSettings.WGameType.values().length];
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$5[IWorldSettings.WGameType.NOT_SET.ordinal()] = 1;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$5[IWorldSettings.WGameType.SURVIVAL.ordinal()] = 2;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$5[IWorldSettings.WGameType.CREATIVE.ordinal()] = 3;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$5[IWorldSettings.WGameType.ADVENTUR.ordinal()] = 4;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$5[IWorldSettings.WGameType.SPECTATOR.ordinal()] = 5;
        $EnumSwitchMapping$6 = new int[GameType.values().length];
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$6[GameType.NOT_SET.ordinal()] = 1;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$6[GameType.SURVIVAL.ordinal()] = 2;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$6[GameType.CREATIVE.ordinal()] = 3;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$6[GameType.ADVENTURE.ordinal()] = 4;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$6[GameType.SPECTATOR.ordinal()] = 5;
        $EnumSwitchMapping$7 = new int[CPacketUseEntity.Action.values().length];
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$7[CPacketUseEntity.Action.INTERACT.ordinal()] = 1;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$7[CPacketUseEntity.Action.ATTACK.ordinal()] = 2;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$7[CPacketUseEntity.Action.INTERACT_AT.ordinal()] = 3;
        $EnumSwitchMapping$8 = new int[ICPacketUseEntity.WAction.values().length];
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$8[ICPacketUseEntity.WAction.INTERACT.ordinal()] = 1;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$8[ICPacketUseEntity.WAction.ATTACK.ordinal()] = 2;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$8[ICPacketUseEntity.WAction.INTERACT_AT.ordinal()] = 3;
        $EnumSwitchMapping$9 = new int[CPacketPlayerDigging.Action.values().length];
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$9[CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK.ordinal()] = 1;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$9[CPacketPlayerDigging.Action.DROP_ALL_ITEMS.ordinal()] = 2;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$9[CPacketPlayerDigging.Action.DROP_ITEM.ordinal()] = 3;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$9[CPacketPlayerDigging.Action.RELEASE_USE_ITEM.ordinal()] = 4;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$9[CPacketPlayerDigging.Action.START_DESTROY_BLOCK.ordinal()] = 5;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$9[CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK.ordinal()] = 6;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$9[CPacketPlayerDigging.Action.SWAP_HELD_ITEMS.ordinal()] = 7;
        $EnumSwitchMapping$10 = new int[ICPacketPlayerDigging.WAction.values().length];
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$10[ICPacketPlayerDigging.WAction.START_DESTROY_BLOCK.ordinal()] = 1;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$10[ICPacketPlayerDigging.WAction.ABORT_DESTROY_BLOCK.ordinal()] = 2;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$10[ICPacketPlayerDigging.WAction.STOP_DESTROY_BLOCK.ordinal()] = 3;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$10[ICPacketPlayerDigging.WAction.DROP_ALL_ITEMS.ordinal()] = 4;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$10[ICPacketPlayerDigging.WAction.DROP_ITEM.ordinal()] = 5;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$10[ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM.ordinal()] = 6;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$10[ICPacketPlayerDigging.WAction.SWAP_HELD_ITEMS.ordinal()] = 7;
        $EnumSwitchMapping$11 = new int[IClickEvent.WAction.values().length];
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$11[IClickEvent.WAction.OPEN_URL.ordinal()] = 1;
        $EnumSwitchMapping$12 = new int[ICPacketClientStatus.WEnumState.values().length];
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$12[ICPacketClientStatus.WEnumState.PERFORM_RESPAWN.ordinal()] = 1;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$12[ICPacketClientStatus.WEnumState.REQUEST_STATS.ordinal()] = 2;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$12[ICPacketClientStatus.WEnumState.OPEN_INVENTORY_ACHIEVEMENT.ordinal()] = 3;
        $EnumSwitchMapping$13 = new int[ICPacketResourcePackStatus.WAction.values().length];
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$13[ICPacketResourcePackStatus.WAction.SUCCESSFULLY_LOADED.ordinal()] = 1;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$13[ICPacketResourcePackStatus.WAction.DECLINED.ordinal()] = 2;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$13[ICPacketResourcePackStatus.WAction.FAILED_DOWNLOAD.ordinal()] = 3;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$13[ICPacketResourcePackStatus.WAction.ACCEPTED.ordinal()] = 4;
        $EnumSwitchMapping$14 = new int[ICPacketEntityAction.WAction.values().length];
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$14[ICPacketEntityAction.WAction.START_SNEAKING.ordinal()] = 1;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$14[ICPacketEntityAction.WAction.STOP_SNEAKING.ordinal()] = 2;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$14[ICPacketEntityAction.WAction.STOP_SLEEPING.ordinal()] = 3;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$14[ICPacketEntityAction.WAction.START_SPRINTING.ordinal()] = 4;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$14[ICPacketEntityAction.WAction.STOP_SPRINTING.ordinal()] = 5;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$14[ICPacketEntityAction.WAction.OPEN_INVENTORY.ordinal()] = 6;
        $EnumSwitchMapping$15 = new int[ClickType.values().length];
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$15[ClickType.PICKUP.ordinal()] = 1;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$15[ClickType.QUICK_MOVE.ordinal()] = 2;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$15[ClickType.SWAP.ordinal()] = 3;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$15[ClickType.CLONE.ordinal()] = 4;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$15[ClickType.THROW.ordinal()] = 5;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$15[ClickType.QUICK_CRAFT.ordinal()] = 6;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$15[ClickType.PICKUP_ALL.ordinal()] = 7;
        $EnumSwitchMapping$16 = new int[WEnumHand.values().length];
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$16[WEnumHand.MAIN_HAND.ordinal()] = 1;
        BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$16[WEnumHand.OFF_HAND.ordinal()] = 2;
    }
}

