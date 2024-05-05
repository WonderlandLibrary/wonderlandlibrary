// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.player;

import net.minecraft.util.Vec3;
import net.minecraft.util.BlockPos;
import java.util.List;
import vip.Resolute.util.player.InventoryUtils;
import org.lwjgl.input.Mouse;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.EnumFacing;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntity;
import vip.Resolute.modules.impl.combat.KillAura;
import vip.Resolute.events.impl.EventMotion;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import vip.Resolute.events.impl.EventPacket;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import java.util.HashSet;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.IInventory;
import net.minecraft.client.gui.inventory.GuiChest;
import java.util.Set;
import vip.Resolute.util.misc.TimerUtil;
import vip.Resolute.settings.impl.BooleanSetting;
import vip.Resolute.settings.impl.NumberSetting;
import vip.Resolute.modules.Module;

public class ChestStealer extends Module
{
    public NumberSetting clickDelay;
    public NumberSetting closeDelay;
    public static BooleanSetting silent;
    public BooleanSetting aura;
    public NumberSetting auraRange;
    public BooleanSetting check;
    public BooleanSetting mouseFix;
    TimerUtil timer;
    TimerUtil auraTimer;
    public final Set openedChests;
    GuiChest chest;
    IInventory lowerChestInv;
    int i;
    Slot slot;
    
    public boolean isBoolEnabled() {
        return this.aura.isEnabled();
    }
    
    public ChestStealer() {
        super("ChestStealer", 0, "Steals from chests on a delay", Category.PLAYER);
        this.clickDelay = new NumberSetting("Click Delay", 150.0, 0.0, 500.0, 10.0);
        this.closeDelay = new NumberSetting("Close Delay", 150.0, 0.0, 500.0, 10.0);
        this.aura = new BooleanSetting("Aura", false);
        this.auraRange = new NumberSetting("Aura Range", 5.0, this::isBoolEnabled, 1.0, 6.0, 0.1);
        this.check = new BooleanSetting("Check", true);
        this.mouseFix = new BooleanSetting("Mouse Fix", false);
        this.timer = new TimerUtil();
        this.auraTimer = new TimerUtil();
        this.openedChests = new HashSet();
        this.addSettings(this.clickDelay, this.closeDelay, ChestStealer.silent, this.aura, this.auraRange, this.check, this.mouseFix);
    }
    
    @Override
    public void onEvent(final Event e) {
        this.setSuffix("");
        if (e instanceof EventPacket && ((EventPacket)e).getPacket() instanceof S2DPacketOpenWindow) {
            this.timer.reset();
        }
        if (e instanceof EventMotion) {
            if (e.isPre() && ChestStealer.mc.currentScreen == null && this.aura.isEnabled() && KillAura.target == null) {
                final List loadedTileEntityList = ChestStealer.mc.theWorld.loadedTileEntityList;
                this.index = 0;
                final int loadedTileEntityListSize = loadedTileEntityList.size();
                while (this.index < loadedTileEntityListSize) {
                    final TileEntity tile = loadedTileEntityList.get(this.index);
                    final BlockPos pos = tile.getPos();
                    if (tile instanceof TileEntityChest && this.getDistToPos(pos) < this.auraRange.getValue() && !this.openedChests.contains(tile) && this.auraTimer.hasElapsed(500L) && ChestStealer.mc.playerController.onPlayerRightClick(ChestStealer.mc.thePlayer, ChestStealer.mc.theWorld, ChestStealer.mc.thePlayer.getHeldItem(), pos, EnumFacing.DOWN, this.getVec3(tile.getPos()))) {
                        ChestStealer.mc.getNetHandler().sendPacketNoEvent(new C0APacketAnimation());
                        this.set(this.openedChests, tile);
                        this.auraTimer.reset();
                        return;
                    }
                    ++this.index;
                }
            }
            if (e.isPre() && ChestStealer.mc.currentScreen instanceof GuiChest) {
                this.chest = (GuiChest)ChestStealer.mc.currentScreen;
                this.lowerChestInv = this.chest.getLowerChestInventory();
                if (this.lowerChestInv.getDisplayName().getUnformattedText().contains("Chest") || !this.check.isEnabled()) {
                    if (ChestStealer.silent.isEnabled() && !Mouse.isGrabbed()) {
                        ChestStealer.mc.inGameHasFocus = true;
                        ChestStealer.mc.mouseHelper.grabMouseCursor();
                    }
                    if (this.isInventoryFull() || InventoryUtils.isInventoryEmpty(this.lowerChestInv)) {
                        if (this.timer.hasElapsed((long)this.closeDelay.getValue())) {
                            ChestStealer.mc.thePlayer.closeScreen();
                        }
                    }
                    else {
                        this.i = 0;
                        while (this.i < this.lowerChestInv.getSizeInventory()) {
                            if (this.timer.hasElapsed((long)this.clickDelay.getValue()) && InventoryUtils.isValid(this.lowerChestInv.getStackInSlot(this.i))) {
                                this.slot = ChestStealer.mc.thePlayer.openContainer.getSlot(this.i);
                                if (this.mouseFix.isEnabled()) {
                                    InventoryUtils.legitClick(this.slot, this.chest.inventorySlots.windowId, this.i, 0, InventoryUtils.ClickType.SHIFT_CLICK);
                                }
                                else {
                                    InventoryUtils.windowClick(this.chest.inventorySlots.windowId, this.i, 0, InventoryUtils.ClickType.SHIFT_CLICK);
                                }
                                this.timer.reset();
                            }
                            else {
                                ++this.i;
                            }
                        }
                    }
                }
            }
        }
    }
    
    public void set(final Set set, final TileEntity chest) {
        if (set.size() > 128) {
            set.clear();
        }
        set.add(chest);
    }
    
    public Vec3 getVec3(final BlockPos pos) {
        return new Vec3(pos.getX(), pos.getY(), pos.getZ());
    }
    
    public double getDistToPos(final BlockPos pos) {
        return ChestStealer.mc.thePlayer.getDistance(pos.getX(), pos.getY(), pos.getZ());
    }
    
    private boolean isInventoryFull() {
        for (int i = 9; i < 45; ++i) {
            if (!ChestStealer.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                return false;
            }
        }
        return true;
    }
    
    static {
        ChestStealer.silent = new BooleanSetting("Silent", false);
    }
}
