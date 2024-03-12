// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.player;

import dev.tenacity.event.impl.game.WorldEvent;
import net.minecraft.client.gui.inventory.GuiChest;
import dev.tenacity.utils.font.AbstractFontRenderer;
import java.awt.Color;
import dev.tenacity.module.impl.render.HUDMod;
import net.minecraft.client.gui.ScaledResolution;
import dev.tenacity.event.impl.render.Render2DEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.IInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import java.util.Collections;
import net.minecraft.client.player.inventory.ContainerLocalMenu;
import net.minecraft.inventory.ContainerChest;
import dev.tenacity.utils.player.RotationUtils;
import net.minecraft.util.Vec3i;
import net.minecraft.util.Vec3;
import net.minecraft.util.EnumFacing;
import net.minecraft.init.Blocks;
import dev.tenacity.Tenacity;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.settings.ParentAttribute;
import java.util.ArrayList;
import dev.tenacity.module.Category;
import dev.tenacity.utils.time.TimerUtil;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import java.util.List;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.Module;

public class ChestStealer extends Module
{
    private final NumberSetting delay;
    private final BooleanSetting aura;
    private final NumberSetting auraRange;
    public static BooleanSetting stealingIndicator;
    public static BooleanSetting titleCheck;
    public static BooleanSetting freeLook;
    private final BooleanSetting reverse;
    public static final BooleanSetting silent;
    private final BooleanSetting smart;
    private final List<BlockPos> openedChests;
    private final List<Item> items;
    private final TimerUtil timer;
    public static boolean stealing;
    private InvManager invManager;
    private boolean clear;
    
    public ChestStealer() {
        super("ChestStealer", "Chest Stealer", Category.PLAYER, "auto loot chests");
        this.delay = new NumberSetting("Delay", 80.0, 300.0, 0.0, 10.0);
        this.aura = new BooleanSetting("Aura", false);
        this.auraRange = new NumberSetting("Aura Range", 3.0, 6.0, 1.0, 1.0);
        this.reverse = new BooleanSetting("Reverse", false);
        this.smart = new BooleanSetting("Smart", false);
        this.openedChests = new ArrayList<BlockPos>();
        this.items = new ArrayList<Item>();
        this.timer = new TimerUtil();
        this.auraRange.addParent(this.aura, ParentAttribute.BOOLEAN_CONDITION);
        ChestStealer.stealingIndicator.addParent(ChestStealer.silent, ParentAttribute.BOOLEAN_CONDITION);
        this.addSettings(this.delay, this.aura, this.auraRange, ChestStealer.stealingIndicator, ChestStealer.titleCheck, ChestStealer.freeLook, this.reverse, ChestStealer.silent, this.smart);
    }
    
    @Override
    public void onMotionEvent(final MotionEvent e) {
        if (e.isPre()) {
            this.setSuffix(this.smart.isEnabled() ? "Smart" : null);
            if (this.invManager == null) {
                this.invManager = Tenacity.INSTANCE.getModuleCollection().getModule(InvManager.class);
            }
            if (this.aura.isEnabled()) {
                for (int radius = this.auraRange.getValue().intValue(), x = -radius; x < radius; ++x) {
                    for (int y = -radius; y < radius; ++y) {
                        for (int z = -radius; z < radius; ++z) {
                            final BlockPos pos = new BlockPos(ChestStealer.mc.thePlayer.posX + x, ChestStealer.mc.thePlayer.posY + y, ChestStealer.mc.thePlayer.posZ + z);
                            if (pos.getBlock() == Blocks.chest && !this.openedChests.contains(pos) && ChestStealer.mc.playerController.onPlayerRightClick(ChestStealer.mc.thePlayer, ChestStealer.mc.theWorld, ChestStealer.mc.thePlayer.getHeldItem(), pos, EnumFacing.UP, new Vec3(pos))) {
                                ChestStealer.mc.thePlayer.swingItem();
                                final float[] rotations = RotationUtils.getFacingRotations2(pos.getX(), pos.getY(), pos.getZ());
                                e.setRotations(rotations[0], rotations[1]);
                                RotationUtils.setVisualRotations(rotations[0], rotations[1]);
                                this.openedChests.add(pos);
                            }
                        }
                    }
                }
            }
            if (ChestStealer.mc.thePlayer.openContainer instanceof ContainerChest) {
                final ContainerChest chest = (ContainerChest)ChestStealer.mc.thePlayer.openContainer;
                final IInventory chestInv = chest.getLowerChestInventory();
                if (ChestStealer.titleCheck.isEnabled() && (!(chestInv instanceof ContainerLocalMenu) || !((ContainerLocalMenu)chestInv).realChest)) {
                    return;
                }
                this.clear = true;
                final List<Integer> slots = new ArrayList<Integer>();
                for (int i = 0; i < chestInv.getSizeInventory(); ++i) {
                    final ItemStack is = chestInv.getStackInSlot(i);
                    if (is != null && (!this.smart.isEnabled() || (!this.invManager.isBadItem(is, -1, true) && !this.items.contains(is.getItem())))) {
                        slots.add(i);
                    }
                }
                if (this.reverse.isEnabled()) {
                    Collections.reverse(slots);
                }
                final IInventory inventory;
                final ItemStack is2;
                final Item item;
                final ContainerChest containerChest;
                slots.forEach(s -> {
                    is2 = inventory.getStackInSlot(s);
                    item = ((is2 != null) ? is2.getItem() : null);
                    if (item != null && !this.items.contains(item) && (this.delay.getValue() == 0.0 || this.timer.hasTimeElapsed(this.delay.getValue().longValue(), true))) {
                        if (this.smart.isEnabled() && !(item instanceof ItemBlock)) {
                            this.items.add(is2.getItem());
                        }
                        ChestStealer.mc.playerController.windowClick(containerChest.windowId, s, 0, 1, ChestStealer.mc.thePlayer);
                    }
                    return;
                });
                if (slots.isEmpty() || this.isInventoryFull()) {
                    this.items.clear();
                    this.clear = false;
                    ChestStealer.stealing = false;
                    ChestStealer.mc.thePlayer.closeScreen();
                }
            }
            else if (this.clear) {
                this.items.clear();
                this.clear = false;
            }
        }
    }
    
    @Override
    public void onRender2DEvent(final Render2DEvent event) {
        if (ChestStealer.stealingIndicator.isEnabled() && ChestStealer.stealing) {
            final ScaledResolution sr = new ScaledResolution(ChestStealer.mc);
            final AbstractFontRenderer fr = HUDMod.customFont.isEnabled() ? ChestStealer.tenacityFont20 : ChestStealer.mc.fontRendererObj;
            fr.drawStringWithShadow("§lStealing...", sr.getScaledWidth() / 2.0f - fr.getStringWidth("§lStealing...") / 2.0f, sr.getScaledHeight() / 2.0f + 10.0f, HUDMod.getClientColors().getFirst());
        }
    }
    
    @Override
    public void onEnable() {
        this.openedChests.clear();
        super.onEnable();
    }
    
    private boolean isInventoryFull() {
        for (int i = 9; i < 45; ++i) {
            if (ChestStealer.mc.thePlayer.inventoryContainer.getSlot(i).getStack() == null) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean canSteal() {
        if (Tenacity.INSTANCE.isEnabled(ChestStealer.class) && ChestStealer.mc.currentScreen instanceof GuiChest) {
            final ContainerChest chest = (ContainerChest)ChestStealer.mc.thePlayer.openContainer;
            final IInventory chestInv = chest.getLowerChestInventory();
            return !ChestStealer.titleCheck.isEnabled() || (chestInv instanceof ContainerLocalMenu && ((ContainerLocalMenu)chestInv).realChest);
        }
        return false;
    }
    
    @Override
    public void onWorldEvent(final WorldEvent event) {
        if (event instanceof WorldEvent.Load) {
            this.openedChests.clear();
        }
    }
    
    static {
        ChestStealer.stealingIndicator = new BooleanSetting("Stealing Indicator", false);
        ChestStealer.titleCheck = new BooleanSetting("Title Check", true);
        ChestStealer.freeLook = new BooleanSetting("Free Look", true);
        silent = new BooleanSetting("Silent", false);
    }
}
