/*
 * Decompiled with CFR 0.150.
 */
package baritone.behavior;

import baritone.Baritone;
import baritone.api.event.events.TickEvent;
import baritone.behavior.Behavior;
import baritone.utils.ToolSet;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.Random;
import java.util.function.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;

public final class InventoryBehavior
extends Behavior {
    public InventoryBehavior(Baritone baritone) {
        super(baritone);
    }

    @Override
    public void onTick(TickEvent event) {
        int pick;
        if (!((Boolean)Baritone.settings().allowInventory.value).booleanValue()) {
            return;
        }
        if (event.getType() == TickEvent.Type.OUT) {
            return;
        }
        if (this.ctx.player().openContainer != this.ctx.player().inventoryContainer) {
            return;
        }
        if (this.firstValidThrowaway() >= 9) {
            this.swapWithHotBar(this.firstValidThrowaway(), 8);
        }
        if ((pick = this.bestToolAgainst(Blocks.STONE, ItemPickaxe.class)) >= 9) {
            this.swapWithHotBar(pick, 0);
        }
    }

    public void attemptToPutOnHotbar(int inMainInvy, Predicate<Integer> disallowedHotbar) {
        OptionalInt destination = this.getTempHotbarSlot(disallowedHotbar);
        if (destination.isPresent()) {
            this.swapWithHotBar(inMainInvy, destination.getAsInt());
        }
    }

    public OptionalInt getTempHotbarSlot(Predicate<Integer> disallowedHotbar) {
        int i;
        ArrayList<Integer> candidates = new ArrayList<Integer>();
        for (i = 1; i < 8; ++i) {
            if (!this.ctx.player().inventory.mainInventory.get(i).isEmpty() || disallowedHotbar.test(i)) continue;
            candidates.add(i);
        }
        if (candidates.isEmpty()) {
            for (i = 1; i < 8; ++i) {
                if (disallowedHotbar.test(i)) continue;
                candidates.add(i);
            }
        }
        if (candidates.isEmpty()) {
            return OptionalInt.empty();
        }
        return OptionalInt.of((Integer)candidates.get(new Random().nextInt(candidates.size())));
    }

    private void swapWithHotBar(int inInventory, int inHotbar) {
        this.ctx.playerController().windowClick(this.ctx.player().inventoryContainer.windowId, inInventory < 9 ? inInventory + 36 : inInventory, inHotbar, ClickType.SWAP, this.ctx.player());
    }

    private int firstValidThrowaway() {
        NonNullList<ItemStack> invy = this.ctx.player().inventory.mainInventory;
        for (int i = 0; i < invy.size(); ++i) {
            if (!((List)Baritone.settings().acceptableThrowawayItems.value).contains(invy.get(i).getItem())) continue;
            return i;
        }
        return -1;
    }

    private int bestToolAgainst(Block against, Class<? extends ItemTool> cla$$) {
        NonNullList<ItemStack> invy = this.ctx.player().inventory.mainInventory;
        int bestInd = -1;
        double bestSpeed = -1.0;
        for (int i = 0; i < invy.size(); ++i) {
            double speed;
            ItemStack stack = invy.get(i);
            if (stack.isEmpty() || ((Boolean)Baritone.settings().itemSaver.value).booleanValue() && stack.getItemDamage() >= stack.getMaxDamage() && stack.getMaxDamage() > 1 || !cla$$.isInstance(stack.getItem()) || !((speed = ToolSet.calculateSpeedVsBlock(stack, against.getDefaultState())) > bestSpeed)) continue;
            bestSpeed = speed;
            bestInd = i;
        }
        return bestInd;
    }

    public boolean hasGenericThrowaway() {
        for (Item item : (List)Baritone.settings().acceptableThrowawayItems.value) {
            if (!this.throwaway(false, stack -> item.equals(stack.getItem()))) continue;
            return true;
        }
        return false;
    }

    public boolean selectThrowawayForLocation(boolean select, int x, int y, int z) {
        IBlockState maybe = this.baritone.getBuilderProcess().placeAt(x, y, z, this.baritone.bsi.get0(x, y, z));
        if (maybe != null && this.throwaway(select, stack -> stack.getItem() instanceof ItemBlock && maybe.equals(((ItemBlock)stack.getItem()).getBlock().getStateForPlacement(this.ctx.world(), this.ctx.playerFeet(), EnumFacing.UP, (float)this.ctx.player().posX, (float)this.ctx.player().posY, (float)this.ctx.player().posZ, stack.getItem().getMetadata(stack.getMetadata()), this.ctx.player())))) {
            return true;
        }
        if (maybe != null && this.throwaway(select, stack -> stack.getItem() instanceof ItemBlock && ((ItemBlock)stack.getItem()).getBlock().equals(maybe.getBlock()))) {
            return true;
        }
        for (Item item : (List)Baritone.settings().acceptableThrowawayItems.value) {
            if (!this.throwaway(select, stack -> item.equals(stack.getItem()))) continue;
            return true;
        }
        return false;
    }

    public boolean throwaway(boolean select, Predicate<? super ItemStack> desired) {
        ItemStack item;
        int i;
        EntityPlayerSP p = this.ctx.player();
        NonNullList<ItemStack> inv = p.inventory.mainInventory;
        for (i = 0; i < 9; ++i) {
            item = inv.get(i);
            if (!desired.test(item)) continue;
            if (select) {
                p.inventory.currentItem = i;
            }
            return true;
        }
        if (desired.test(p.inventory.offHandInventory.get(0))) {
            for (i = 0; i < 9; ++i) {
                item = inv.get(i);
                if (!item.isEmpty() && !(item.getItem() instanceof ItemPickaxe)) continue;
                if (select) {
                    p.inventory.currentItem = i;
                }
                return true;
            }
        }
        return false;
    }
}

