/*
 * This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package kevin.utils;

import kotlin.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public final class InventoryUtils extends MinecraftInstance {

    public static final MSTimer CLICK_TIMER = new MSTimer();
    public static final List<Block> BLOCK_BLACKLIST = Arrays.asList(
            Blocks.chest, Blocks.ender_chest, Blocks.trapped_chest, Blocks.anvil, Blocks.sand, Blocks.web, Blocks.torch,
            Blocks.crafting_table, Blocks.furnace, Blocks.waterlily, Blocks.dispenser, Blocks.stone_pressure_plate, Blocks.wooden_pressure_plate,
            Blocks.noteblock, Blocks.dropper, Blocks.tnt, Blocks.standing_banner,Blocks.wall_banner, Blocks.redstone_torch
    );

    public static int findItem(final int startSlot, final int endSlot, final Item item) {
        for (int i = startSlot; i < endSlot; i++) {
            final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

            if (stack != null && stack.getItem().equals(item))
                return i;
        }

        return -1;
    }

    public static boolean hasSpaceHotbar() {
        for (int i = 36; i < 45; i++) {
            final ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);

            if (stack == null)
                return true;
        }

        return false;
    }

    public static int findBlockInHotbar() {
        EntityPlayerSP player = mc.thePlayer;
        if (player == null) return -1;
        Container inventory = player.openContainer;

        return IntStream.range(36, 44).filter(it -> {
            ItemStack stack = inventory.getSlot(it).getStack();
            if (stack == null) return false;

            Block block;
            if (stack.getItem() instanceof ItemBlock)
                block = ((ItemBlock) stack.getItem()).getBlock();
            else return false;

            return stack.getItem() instanceof ItemBlock && stack.stackSize > 0 && BLOCK_BLACKLIST.contains(block) && !(block instanceof BlockBush);
        }).min().orElse(-1);
    }

    public static int  findLargestBlockStackInHotbar() {
        EntityPlayerSP player = mc.thePlayer;
        if (player == null) return -1;
        Container inventory = player.openContainer;

        return IntStream.range(36, 44).filter(it -> {
            ItemStack stack = inventory.getSlot(it).getStack();
            if (stack == null) return false;

            Block block;
            if (stack.getItem() instanceof ItemBlock)
                block = ((ItemBlock) stack.getItem()).getBlock();
            else return false;

            return stack.getItem() instanceof ItemBlock && stack.stackSize > 0 && BLOCK_BLACKLIST.contains(block) && !(block instanceof BlockBush);
        }).mapToObj(it -> new Pair<>(it, inventory.getSlot(it).getStack().stackSize)).max(Comparator.comparingInt(Pair::getSecond)).orElse(new Pair<>(-1, 0)).getFirst();
    }

    public static int findAutoBlockBlock() {
        for (int i = 36; i < 45; i++) {
            final ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

            if (itemStack != null && itemStack.getItem() instanceof ItemBlock && itemStack.stackSize > 0) {
                final ItemBlock itemBlock = (ItemBlock) itemStack.getItem();
                final Block block = itemBlock.getBlock();

                if (block.isFullCube() && !BLOCK_BLACKLIST.contains(block)
                        && !(block instanceof BlockBush))
                    return i;
            }
        }

        for (int i = 36; i < 45; i++) {
            final ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

            if (itemStack != null && itemStack.getItem() instanceof ItemBlock && itemStack.stackSize > 0) {
                final ItemBlock itemBlock = (ItemBlock) itemStack.getItem();
                final Block block = itemBlock.getBlock();

                if (!BLOCK_BLACKLIST.contains(block) && !(block instanceof BlockBush))
                    return i;
            }
        }

        return -1;
    }

    public static boolean isBlockListBlock(ItemBlock itemBlock) {
        Block block = itemBlock.getBlock();
        return BLOCK_BLACKLIST.contains(block) || !block.isFullCube();
    }

    public static boolean canPlaceBlock(Block block) {
        return block.isFullCube() && !BLOCK_BLACKLIST.contains(block);
    }

    public static class MouseSimulator {
        public static final MouseSimulator SHARED = new MouseSimulator();
        public double mouseX, mouseY;

        public void moveTo(int x, int y, double speed) {
            moveTo0(x, y, speed);
        }

        public void moveTo(int x, int y, float speed) {
            moveTo0(x, y, speed);
        }

        public void moveTo(double x, double y, float speed) {
            moveTo0(x, y, speed);
        }

        public void moveTo(double x, double y, double speed) {
            moveTo0(x, y, speed);
        }

        private void moveTo0(double x, double y, double speed) {
            Vec3 current = new Vec3(mouseX, 0.0, mouseY);
            Vec3 target = new Vec3(x, 0.0, y);
            Vec3 diff = target.subtract(current);
            speed = Math.min(speed, diff.lengthVector()) + RandomUtils.nextDouble(-0.01, 0.01);
            Vec3 dir = new Vec3(0.0, 0.0, Math.toRadians(90.0) * 0.5);
            double direction = Math.acos(MathHelper.clamp_double(dir.dotProduct(diff) / (dir.lengthVector() * diff.lengthVector()), -1, 1));
            mouseX += Math.sin(direction) * speed * Math.signum(x - mouseX) + RandomUtils.nextDouble(-0.01, 0.01);
            mouseY += Math.cos(direction) * speed + RandomUtils.nextDouble(-0.01, 0.01);
        }

        public boolean ableToClick(double x, double y, double tolerance) {
            return Math.abs(x - mouseX) <= tolerance && Math.abs(y - mouseY) <= tolerance;
        }

        public boolean ableToClick(int x, int y, int tolerance) {
            return Math.abs(x - mouseX) <= tolerance && Math.abs(y - mouseY) <= tolerance;
        }

        public void set(double x, double y) {
            mouseX = x;
            mouseY = y;
        }

        public void reset() {
            mouseX = mouseY = 0;
        }
    }
}