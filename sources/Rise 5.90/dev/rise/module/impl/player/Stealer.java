/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.player;

import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.packet.PacketReceiveEvent;
import dev.rise.event.impl.render.Render2DEvent;
import dev.rise.font.CustomFont;
import dev.rise.font.fontrenderer.TTFFontRenderer;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.util.math.TimeUtil;
import dev.rise.util.render.theme.ThemeType;
import dev.rise.util.render.theme.ThemeUtil;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockSlime;
import net.minecraft.block.BlockTNT;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.*;
import net.minecraft.network.play.server.S30PacketWindowItems;
import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import java.util.ArrayList;

@ModuleInfo(name = "Stealer", description = "Steals items from chests", category = Category.PLAYER)
public final class Stealer extends Module {

    private final TimeUtil timer = new TimeUtil();
    private final TimeUtil startTimer = new TimeUtil();

    private final NumberSetting minDelay = new NumberSetting("Min Delay", this, 5, 0, 1000, 25);
    private final NumberSetting maxDelay = new NumberSetting("Max Delay", this, 5, 0, 1000, 25);
    private final BooleanSetting stealTrashItems = new BooleanSetting("Steal trash items", this, false);
    private final BooleanSetting autoClose = new BooleanSetting("Auto Close", this, true);
    private final BooleanSetting hideGui = new BooleanSetting("Hide Gui", this, true);
    private final BooleanSetting mouseInput = new BooleanSetting("Move head in chest", this, true);
    private final BooleanSetting chestName = new BooleanSetting("Check chest name", this, false);

    private int decidedTimer = 0;

    public static boolean hideChestGui, allowMouseInput, closeAfterContainer;

    private boolean gotItems;
    private final static TTFFontRenderer comfortaa = CustomFont.FONT_MANAGER.getFont("Comfortaa 18");
    private int ticksInChest;

    private boolean lastInChest;

    @Override
    protected void onDisable() {
        closeAfterContainer = false;
        allowMouseInput = false;
        hideChestGui = false;
        gotItems = false;
    }

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        if (mc.thePlayer.ticksExisted <= 60) return;

        if (event.getPacket() instanceof S30PacketWindowItems) {
            gotItems = true;
        }
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (mc.thePlayer.ticksExisted <= 60) return;
        if (mc.currentScreen instanceof GuiChest && Display.isVisible() && mouseInput.isEnabled() && hideGui.isEnabled() && (!chestName.isEnabled() || (((GuiChest) mc.currentScreen).lowerChestInventory.getDisplayName().getUnformattedText().contains("chest")))) {
            mc.mouseHelper.centerMouse();
            mc.mouseHelper.mouseGrab(false);
            mc.mouseHelper.mouseGrab(true);
        }

        if (mc.currentScreen instanceof GuiChest) {
            ticksInChest++;

            if (ticksInChest * 50 > 255) {
                ticksInChest = 10;
            }
        } else {
            ticksInChest--;
            gotItems = false;
            if (ticksInChest < 0) {
                ticksInChest = 0;
            }
        }
    }

    @Override
    public void onRender2DEvent(final Render2DEvent event) {
        if (mc.thePlayer.ticksExisted <= 60) return;
        if (!lastInChest) startTimer.reset();
        lastInChest = mc.currentScreen instanceof GuiChest;

        if (mc.currentScreen instanceof GuiChest) {
            if (hideGui.isEnabled()) {
                hideChestGui = true;
            }

            if (mouseInput.isEnabled()) {
                allowMouseInput = true;
            }

            if (chestName.isEnabled()) {
                final String name = ((GuiChest) mc.currentScreen).lowerChestInventory.getDisplayName().getUnformattedText();

                if (!name.toLowerCase().contains("chest")) return;
            }

            if (hideGui.isEnabled()) {
                final ScaledResolution SR = new ScaledResolution(mc);
                final String t = "Stealing chest... Press " + Keyboard.getKeyName(mc.gameSettings.keyBindInventory.getKeyCode()) + " to close the chest";
                int o;

                o = ticksInChest * 50;

                if (o > 255)
                    o = 255;

                comfortaa.drawStringWithShadow(t, SR.getScaledWidth() / 2f - comfortaa.getWidth(t) / 2f, SR.getScaledHeight() - 80, ThemeUtil.getThemeColorInt(ThemeType.GENERAL));
            }

            if (decidedTimer == 0) {
                final int delayFirst = (int) Math.floor(Math.min(minDelay.getValue(), maxDelay.getValue()));
                final int delaySecond = (int) Math.ceil(Math.max(minDelay.getValue(), maxDelay.getValue()));
                decidedTimer = RandomUtils.nextInt(delayFirst, delaySecond);
            }

            if (timer.hasReached(decidedTimer)) {
                final ContainerChest chest = (ContainerChest) mc.thePlayer.openContainer;

                for (int i = 0; i < chest.inventorySlots.size(); i++) {
                    final ItemStack stack = chest.getLowerChestInventory().getStackInSlot(i);
                    if (stack != null && (itemWhitelisted(stack) && !stealTrashItems.isEnabled())) {
                        mc.playerController.windowClick(chest.windowId, i, 0, 1, mc.thePlayer);
                        timer.reset();

                        final int delayFirst = (int) Math.floor(Math.min(minDelay.getValue(), maxDelay.getValue()));
                        final int delaySecond = (int) Math.ceil(Math.max(minDelay.getValue(), maxDelay.getValue()));

                        decidedTimer = RandomUtils.nextInt(delayFirst, delaySecond);

                        gotItems = true;
                        return;
                    }
                }

                if (autoClose.isEnabled() && (gotItems || ticksInChest > 10)) {
                    mc.thePlayer.closeScreen();
                }
            }
        } else {
            hideChestGui = false;
            allowMouseInput = false;
        }
    }

    private boolean itemWhitelisted(final ItemStack itemStack) {
        final ArrayList<Item> whitelistedItems = new ArrayList<Item>() {{
            add(Items.ender_pearl);
            add(Items.iron_ingot);
            add(Items.gold_ingot);
            add(Items.redstone);
            add(Items.diamond);
            add(Items.emerald);
            add(Items.quartz);
            add(Items.bow);
            add(Items.arrow);
            add(Items.fishing_rod);
        }};

        final Item item = itemStack.getItem();
        final String itemName = itemStack.getDisplayName();

        if (itemName.contains("Right Click") || itemName.contains("Click to Use") || itemName.contains("Players Finder"))
            return true;

        final ArrayList<Integer> whitelistedPotions = new ArrayList<Integer>() {{
            add(6);
            add(1);
            add(5);
            add(8);
            add(14);
            add(12);
            add(10);
            add(16);
        }};

        if (item instanceof ItemPotion) {
            final int potionID = getPotionId(itemStack);
            return whitelistedPotions.contains(potionID);
        }

        return (item instanceof ItemBlock
                && !(((ItemBlock) item).getBlock() instanceof BlockTNT)
                && !(((ItemBlock) item).getBlock() instanceof BlockSlime)
                && !(((ItemBlock) item).getBlock() instanceof BlockFalling))
                || item instanceof ItemAnvilBlock
                || item instanceof ItemSword
                || item instanceof ItemArmor
                || item instanceof ItemTool
                || item instanceof ItemFood
                || item instanceof ItemSkull
                || itemName.contains("\247")
                || whitelistedItems.contains(item)
                && !item.equals(Items.spider_eye);
    }

    private int getPotionId(final ItemStack potion) {
        final Item item = potion.getItem();

        try {
            if (item instanceof ItemPotion) {
                final ItemPotion p = (ItemPotion) item;
                return p.getEffects(potion.getMetadata()).get(0).getPotionID();
            }
        } catch (final NullPointerException ignored) {
        }

        return 0;
    }
}