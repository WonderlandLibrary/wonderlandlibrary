package info.sigmaclient.sigma.modules.movement;

import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.event.net.PacketEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.config.values.ModeValue;
import info.sigmaclient.sigma.modules.combat.Killaura;
import info.sigmaclient.sigma.utils.player.MovementUtils;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.*;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.network.play.server.SWindowItemsPacket;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.optifine.util.BlockUtils;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class NoSlow extends Module {
    public static ModeValue mode = new ModeValue("Mode", "Intave", new String[]{
            "GrimAC",
            "Intave",
            "Vanilla",
            "OnlySword",
            "Vulcan",
            "LNCP",
            "ONCP",
            "Advance",
            "Blocksmc",
            "Hypixel",
            "Grim2",
            "Hypixel2"
    });
    public static BooleanValue sword = new BooleanValue("Sword", true);
    public static BooleanValue food = new BooleanValue("Food", true);
    public static BooleanValue bow = new BooleanValue("Bow", true);
    public static BooleanValue potion = new BooleanValue("Potion", true);
    public static BooleanValue shield = new BooleanValue("Shield", true);
    public NoSlow() {
        super("NoSlow", Category.Movement, "No slow for use items");
        registerValue(mode);
        registerValue(sword);
        registerValue(food);
        registerValue(bow);
        registerValue(potion);
        registerValue(shield);
    }
    public static boolean isNeedNoslow(){
        if(mode.is("ReallyWorld")){
            if(mc.player.onGround || mc.player.fallDistance == 0)
                return false;
        }
        return SigmaNG.SigmaNG.moduleManager.getModule(NoSlow.class).enabled &&
                ((!mode.is("OnlySword") && !mode.is("Advance")) || mc.player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof SwordItem) && (mc.player.isHandActive())
                && (!mode.is("Intave") || !(mc.player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof PotionItem))
                && (sword.isEnable() || !(mc.player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof SwordItem))
                && (food.isEnable() || !(mc.player.getHeldItem(Hand.MAIN_HAND).isFood()))
                && (bow.isEnable() || !(mc.player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof BowItem))
                && (shield.isEnable() || !(mc.player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof ShieldItem || mc.player.getHeldItem(Hand.OFF_HAND).getItem() instanceof ShieldItem))
                && (potion.isEnable() || !(mc.player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof PotionItem));
    }
    boolean noSlowing = false;
    boolean reset = false, slow = false;

    @Override
    public void onPacketEvent(PacketEvent event) {
        //hypixel modez
        if (!mc.player.isRidingHorse() && mc.gameSettings.keyBindUseItem.pressed || Killaura.attackTarget != null) {
            IPacket<?> packet = event.packet;
            switch (mode.getValue()) {
                case "Advance":
                    if (packet instanceof CUseEntityPacket &&
                            ((CUseEntityPacket) packet).getAction() ==
                                    CUseEntityPacket.Action.ATTACK &&
                            mc.player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof SwordItem) {
                        noSlowing = true;
                    }
                    break;
            }
            if(packet instanceof SWindowItemsPacket) {
                if(!noSlowing) {
                    event.cancelable = true;
                }
            }
        }
        if(mode.is("Grim2")){
            IPacket<?> packet = event.getPacket();
            if (packet instanceof SWindowItemsPacket && ((SWindowItemsPacket) packet).getWindowId() == 0) {
                if ((mc.player != null ? mc.player.getActiveItemStack() : ItemStack.EMPTY) != ItemStack.EMPTY) {
//                    packetBuf.add(packet);
//                    event.cancelEvent();
                    event.cancelable = true;
                }
                slow = false;
            } else if (packet instanceof SSetSlotPacket && (((SSetSlotPacket) packet).getWindowId() == 0 || ((SSetSlotPacket) packet).getWindowId() == -1 || ((SSetSlotPacket) packet).getWindowId() == -2) && (mc.player != null ? mc.player.getActiveItemStack() : ItemStack.EMPTY) != ItemStack.EMPTY) {
//                packetBuf.add(packet);
//                event.cancelEvent();
                event.cancelable = true;
            } else if (packet instanceof CConfirmTransactionPacket && ((CConfirmTransactionPacket) packet).getWindowId() == 0 && ((CConfirmTransactionPacket) packet).getUid() != (short) 11451 && slow) {
                mc.getConnection().sendPacketNOEvent(new CConfirmTransactionPacket(0, (short) 11451, true));
            }else if (packet instanceof CPlayerTryUseItemPacket) {
                Item stack = mc.player != null ? mc.player.getHeldItem(((CPlayerTryUseItemPacket) packet).getHand()).getItem() : null;
                if (stack instanceof SwordItem || stack.isFood() ||
                        stack instanceof PotionItem || stack instanceof MilkBucketItem || stack instanceof BowItem || stack instanceof ShieldItem) {
                    slow = true;
                    mc.getConnection().sendPacket(new CClickWindowPacket(0, 36, 0, ClickType.SWAP, new ItemStack(Blocks.CHEST), (short) 11451));
                }
            }
        }
        super.onPacketEvent(event);
    }
    @Override
    public void onUpdateEvent(UpdateEvent event) {
        suffix = mode.getValue();
        if (mc.player.isHandActive() && !mc.player.isRidingHorse()) {
            if (!isNeedNoslow()) {
                if (reset) {
                    mc.getConnection().sendPacket(new CEntityActionPacket(mc.player, CEntityActionPacket.Action.RELEASE_SHIFT_KEY));
                }
                return;
            }
            switch (mode.getValue()) {
                case "Hypixel":
                    if (event.isPre()) {
                        if (mc.gameSettings.keyBindUseItem.pressed && mc.player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof SwordItem) {
                            mc.getConnection().sendPacket(new CEntityActionPacket(mc.player, CEntityActionPacket.Action.PRESS_SHIFT_KEY));
                            reset = true;
                        }else if(reset){
                            mc.getConnection().sendPacket(new CEntityActionPacket(mc.player, CEntityActionPacket.Action.RELEASE_SHIFT_KEY));
                        }
                    }
                    break;
                case "Hypixel3":
                    if (MovementUtils.isMoving()) {
                        if (event.isPre()) {
                            if (!(mc.player.getActiveItemStack().isFood() && mc.player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof BowItem) && mc.player.ticksExisted % 3 == 0) {
                                mc.getConnection().sendPacket(new CPlayerTryUseItemOnBlockPacket(Hand.MAIN_HAND, new BlockRayTraceResult(new Vector3d(-1, -1, -1), Direction.UP, new BlockPos(-1, -1, -1), false)));
                            }
                        }
                    }
                    break;
                case "Grim2":
                    if (event.isPre()) {
//                        if (MovementUtils.isMoving() && (mc.player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof SwordItem || mc.player.getHeldItem(Hand.OFF_HAND).getItem() instanceof SwordItem)) {
//                            mc.getConnection().sendPacket(new CHeldItemChangePacket((mc.player.inventory.currentItem + 1) % 9));
//                            mc.getConnection().sendPacket(new CHeldItemChangePacket(mc.player.inventory.currentItem));
//                        }
                    }
                    break;
                case "Advance":
                    if (mc.gameSettings.keyBindUseItem.pressed || Killaura.attackTarget!= null && mc.player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof SwordItem) {
                        if (noSlowing) {
                            noSlowing = false;
                            return;
                        }
                        if(event.isPre()) {
                            if (mc.player.ticksExisted % 3 == 0)
                                mc.getConnection().sendPacket(new CPlayerTryUseItemOnBlockPacket(
                                        Hand.MAIN_HAND,
                                        new BlockRayTraceResult(
                                                new Vector3d(-1, -1, -1),
                                                Direction.UP,
                                                new BlockPos(-1, -1, -1),
                                                false
                                        )
                                ));
                        }
                    }
                    break;
                case "Blocksmc":
                    if (event.isPre()) {
                        if (!(mc.player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof SwordItem)) {
                            mc.getConnection().sendPacket(new CHeldItemChangePacket(mc.player.inventory.currentItem % 8 + 1));
                            mc.getConnection().sendPacket(new CHeldItemChangePacket(mc.player.inventory.currentItem));
                            if (mc.player.ticksExisted % 3 == 0) {
                                mc.getConnection().sendPacket(new CPlayerTryUseItemPacket(Hand.MAIN_HAND));
                            }
                        }
                    }
                    break;
                case "Intave":
                    if (mc.player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof BowItem) {
                        if(event.isPre()) {
                            mc.getConnection().sendPacket(new CHeldItemChangePacket(mc.player.inventory.currentItem % 8 + 1));
                            mc.getConnection().sendPacket(new CHeldItemChangePacket(mc.player.inventory.currentItem));
                        }
                    }else
                        mc.getConnection().sendPacket(new CPlayerDiggingPacket(CPlayerDiggingPacket.Action.RELEASE_USE_ITEM, BlockPos.ZERO, Direction.DOWN));

                    break;
                case "Vulcan":
                    if (event.isPre()) {
                        if (mc.player.ticksExisted % 3 == 0) {
                            mc.getConnection().sendPacket(new CPlayerTryUseItemPacket(Hand.MAIN_HAND));
                        }
                    }
                    break;
                case "ReallyWorld":
                    if (!event.isPre()) {
                        if (mc.player.ticksExisted % 2 == 0) {
                            mc.getConnection().sendPacket(new CPlayerDiggingPacket(CPlayerDiggingPacket.Action.RELEASE_USE_ITEM, BlockPos.ZERO, Direction.DOWN));
//                            mc.getConnection().sendPacket(new CPlayerTryUseItemPacket(Hand.MAIN_HAND));
                        }
                    }
                    break;
                case "GrimAC":
                    if (event.isPre()) {
                        mc.getConnection().sendPacket(new CHeldItemChangePacket(mc.player.inventory.currentItem % 8 + 1));
                        mc.getConnection().sendPacket(new CHeldItemChangePacket(mc.player.inventory.currentItem));
                    }
                    break;
                case "LNCP":
                    if (event.isPre()) {
                        mc.getConnection().sendPacket(new CHeldItemChangePacket(mc.player.inventory.currentItem % 8 + 1));
                        mc.getConnection().sendPacket(new CHeldItemChangePacket(mc.player.inventory.currentItem));
                        if (mc.player.ticksExisted % 3 == 0) {
                            mc.getConnection().sendPacket(new CPlayerTryUseItemPacket(Hand.MAIN_HAND));
                        }
                    }
                    break;
                case "ONCP":
                    if (event.isPre()) {
                        mc.getConnection().sendPacket(new CPlayerDiggingPacket(CPlayerDiggingPacket.Action.RELEASE_USE_ITEM, BlockPos.ZERO, Direction.DOWN));
                        mc.getConnection().sendPacket(new CPlayerTryUseItemPacket(Hand.MAIN_HAND));
                    }
                    break;
            }
        }
        super.onUpdateEvent(event);
    }
}