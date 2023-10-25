package wtf.evolution.module.impl.Combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.minecraft.block.BlockObsidian;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventUpdate;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.BooleanSetting;
import wtf.evolution.settings.options.SliderSetting;

@ModuleInfo(name = "AutoTotem", type = Category.Combat)
public class AutoTotem extends Module {
    public static SliderSetting health = new SliderSetting("Health", 6, 0, 20, 0.5f);
    public static BooleanSetting checkCrystal = new BooleanSetting("Check Crystal", true);
    public static SliderSetting CrystalDistance = new SliderSetting("CrystalDistance", 8, 0, 15, 0.5f).setHidden(() -> !checkCrystal.get());
    public static BooleanSetting fall = new BooleanSetting("Check Falling", true);
    public static SliderSetting fallDistance = new SliderSetting("FallDistance", 8, 1, 30, 0.5f).setHidden(() -> !fall.get());
    public static BooleanSetting obsidian = new BooleanSetting("Check Obsidian", false);
    public static SliderSetting obsidianDistance = new SliderSetting("ObsidianDistance", 8, 1, 15, 0.5f).setHidden(() -> !obsidian.get());
    public static BooleanSetting checkTnt = new BooleanSetting("Check TNT", false);
    public static SliderSetting TnTDistance = new SliderSetting("TnTDistance", 8, 0, 15, 0.5f).setHidden(() -> !checkTnt.get());
    public static BooleanSetting absorptionHP = new BooleanSetting("+ Absorption", true);
    public static int swapBack = -1;
    public static long delay;

    public AutoTotem() {
        addSettings(health, checkCrystal, CrystalDistance, fall, fallDistance, obsidian, obsidianDistance,  checkTnt, TnTDistance, absorptionHP);
    }

    @EventTarget
    public void onEvent(EventUpdate event) {
        float hp = mc.player.getHealth();
        if (absorptionHP.get())
            hp += mc.player.getAbsorptionAmount();
        int totem = getSlotIDFromItem(Items.TOTEM_OF_UNDYING);
        int stackSizeHand = mc.player.getHeldItemOffhand().getCount();
        boolean handNotNull = !(mc.player.getHeldItemOffhand().getItem() instanceof ItemAir);
        boolean handTotem = mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING;
        boolean totemCheck = ((hp <= health.get() || checkTNT() || checkCrystal() || checkFall()
                || checkObsidian()));
        if (System.currentTimeMillis() < delay) {
            return;
        }
        if (totemCheck) {
            if (totem >= 0) {
                if (!handTotem) {
                    mc.playerController.windowClick(0, totem, 1, ClickType.PICKUP, mc.player);
                    mc.playerController.windowClick(0, 45, 1, ClickType.PICKUP, mc.player);
                    if (handNotNull) {
                        mc.playerController.windowClick(0, totem, 0, ClickType.PICKUP, mc.player);
                        if (swapBack == -1)
                            swapBack = totem;
                    }
                    delay = System.currentTimeMillis() + 300;
                }
            }
            return;
        }
        if (swapBack >= 0) {
            mc.playerController.windowClick(0, swapBack, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
            if (handNotNull)
                mc.playerController.windowClick(0, swapBack, 0, ClickType.PICKUP, mc.player);
            swapBack = -1;
            delay = System.currentTimeMillis() + 300;
            return;
        }
        {

            int totemSlot = getTotemSlot();
            if (totemSlot < 9 && totemSlot != -1) {
                totemSlot += 36;
            }
            if (absorptionHP.get()) {
                hp += mc.player.getAbsorptionAmount();
            }
            int prevCurrentItem = mc.player.inventory.currentItem;
            int currentItem = findNearestCurrentItem();
            ItemStack prevHeldItem = mc.player.getHeldItemOffhand();
            boolean totemInHand = mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING;
            if (totemCheck) {
                if (totemSlot >= 0 && !totemInHand) {
                    mc.playerController.windowClick(0, totemSlot, currentItem, ClickType.SWAP, mc.player);
                    mc.getConnection().sendPacket(new CPacketHeldItemChange(currentItem));
                    mc.player.inventory.currentItem = currentItem;
                    ItemStack itemstack = mc.player.getHeldItem(EnumHand.OFF_HAND);
                    mc.player.setHeldItem(EnumHand.OFF_HAND, mc.player.getHeldItem(EnumHand.MAIN_HAND));
                    mc.player.setHeldItem(EnumHand.MAIN_HAND, itemstack);
                    mc.getConnection().sendPacket(
                            new CPacketPlayerDigging(Action.SWAP_HELD_ITEMS, BlockPos.ORIGIN, EnumFacing.DOWN));
                    mc.getConnection().sendPacket(new CPacketHeldItemChange(prevCurrentItem));
                    mc.player.inventory.currentItem = prevCurrentItem;
                    mc.playerController.windowClick(0, totemSlot, currentItem, ClickType.SWAP, mc.player);
                    if (swapBack == -1)
                        swapBack = totemSlot;
                    return;
                }
                if (totemInHand) {
                    return;
                }
            }
            if (swapBack >= 0) {
                mc.getConnection().sendPacket(new CPacketHeldItemChange(currentItem));
                mc.player.inventory.currentItem = currentItem;
                ItemStack itemstack = mc.player.getHeldItem(EnumHand.OFF_HAND);
                mc.player.setHeldItem(EnumHand.OFF_HAND, mc.player.getHeldItem(EnumHand.MAIN_HAND));
                mc.player.setHeldItem(EnumHand.MAIN_HAND, itemstack);
                mc.getConnection().sendPacket(
                        new CPacketPlayerDigging(Action.SWAP_HELD_ITEMS, BlockPos.ORIGIN, EnumFacing.DOWN));
                mc.playerController.windowClick(0, swapBack, currentItem, ClickType.SWAP, mc.player);
                mc.getConnection().sendPacket(
                        new CPacketPlayerDigging(Action.SWAP_HELD_ITEMS, BlockPos.ORIGIN, EnumFacing.DOWN));
                itemstack = mc.player.getHeldItem(EnumHand.OFF_HAND);
                mc.player.setHeldItem(EnumHand.OFF_HAND, mc.player.getHeldItem(EnumHand.MAIN_HAND));
                mc.player.setHeldItem(EnumHand.MAIN_HAND, itemstack);
                mc.getConnection().sendPacket(new CPacketHeldItemChange(prevCurrentItem));
                mc.player.inventory.currentItem = prevCurrentItem;
                swapBack = -1;
            }
        }
    }

    public int getSlotIDFromItem(Item item) {
        int slot = -1;
        for (int i = 0; i < 36; i++) {
            ItemStack s = mc.player.inventory.getStackInSlot(i);
            if (s.getItem() == item) {
                slot = i;
                break;
            }
        }
        if (slot < 9 && slot != -1) {
            slot = slot + 36;
        }
        return slot;
    }

    public int getTotemSlot() {
        for (int i = 0; i < 36; i++) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
                return i;
            }
        }
        return -1;
    }

    public int findNearestCurrentItem() {
        int currentItem = mc.player.inventory.currentItem;
        if (currentItem == 8) {
            return 7;
        }
        if (currentItem == 0) {
            return 1;
        }
        return currentItem - 1;
    }



    private boolean checkCrystal() {
        if (!checkCrystal.get()) {
            return false;
        }
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityEnderCrystal
                    && mc.player.getDistance(entity) <= CrystalDistance.get()) {
                return true;
            }
        }
        return false;
    }

    private boolean checkTNT() {
        if (!checkTnt.get()) {
            return false;
        }
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityTNTPrimed && mc.player.getDistance(entity) <= TnTDistance.get()) {
                return true;
            }
            if (entity instanceof EntityMinecartTNT
                    && mc.player.getDistance(entity) <= TnTDistance.get()) {
                return true;
            }
        }
        return false;
    }

    private boolean IsValidBlockPos(BlockPos pos) {
        IBlockState state = mc.world.getBlockState(pos);
        return state.getBlock() instanceof BlockObsidian;
    }

    private boolean checkObsidian() {
        if (!obsidian.get()) {
            return false;
        }
        BlockPos pos = getSphere(getPlayerPosLocal(), obsidianDistance.get(), 6, false, true, 0).stream()
                .filter(this::IsValidBlockPos)
                .min(Comparator.comparing(blockPos -> getDistanceOfEntityToBlock(mc.player, blockPos))).orElse(null);
        return pos != null;
    }

    public static double getDistanceOfEntityToBlock(final Entity entity, final BlockPos blockPos) {
        return getDistance(entity.posX, entity.posY, entity.posZ, blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public static double getDistance(final double n, final double n2, final double n3, final double n4, final double n5,
                                     final double n6) {
        final double n7 = n - n4;
        final double n8 = n2 - n5;
        final double n9 = n3 - n6;
        return MathHelper.sqrt(n7 * n7 + n8 * n8 + n9 * n9);
    }

    public BlockPos getPlayerPosLocal() {
        if (mc.player == null) {
            return BlockPos.ORIGIN;
        }
        return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
    }

    public static List<BlockPos> getSphere(final BlockPos blockPos, final float n, final int n2, final boolean b,
                                           final boolean b2, final int n3) {
        final ArrayList<BlockPos> list = new ArrayList<BlockPos>();
        final int x = blockPos.getX();
        final int y = blockPos.getY();
        final int z = blockPos.getZ();
        for (int n4 = x - (int) n; n4 <= x + n; ++n4) {
            for (int n5 = z - (int) n; n5 <= z + n; ++n5) {
                for (int n6 = b2 ? (y - (int) n) : y; n6 < (b2 ? (y + n) : ((float) (y + n2))); ++n6) {
                    final double n7 = (x - n4) * (x - n4) + (z - n5) * (z - n5) + (b2 ? ((y - n6) * (y - n6)) : 0);
                    if (n7 < n * n && (!b || n7 >= (n - 1.0f) * (n - 1.0f))) {
                        list.add(new BlockPos(n4, n6 + n3, n5));
                    }
                }
            }
        }
        return list;
    }

    private boolean checkFall() {
        if (!fall.get()) {
            return false;
        }
        return mc.player.fallDistance > fallDistance.get();
    }
}