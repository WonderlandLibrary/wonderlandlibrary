/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.random.Random
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.Random;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiScreen;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.inventory.IGuiChest;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.inventory.IIInventory;
import net.ccbluex.liquidbounce.api.minecraft.inventory.IContainer;
import net.ccbluex.liquidbounce.api.minecraft.inventory.ISlot;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.player.InventoryCleaner;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.item.ItemUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="ChestStealer", description="Automatically steals all items from a chest.", category=ModuleCategory.WORLD)
public final class ChestStealer
extends Module {
    private final IntegerValue maxDelayValue = new IntegerValue(this, "MaxDelay", 200, 0, 400){
        final /* synthetic */ ChestStealer this$0;

        protected void onChanged(int oldValue, int newValue) {
            int i = ((Number)ChestStealer.access$getMinDelayValue$p(this.this$0).get()).intValue();
            if (i > newValue) {
                this.set(i);
            }
            ChestStealer.access$setNextDelay$p(this.this$0, TimeUtils.randomDelay(((Number)ChestStealer.access$getMinDelayValue$p(this.this$0).get()).intValue(), ((Number)this.get()).intValue()));
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4);
        }
    };
    private final IntegerValue minDelayValue = new IntegerValue(this, "MinDelay", 150, 0, 400){
        final /* synthetic */ ChestStealer this$0;

        protected void onChanged(int oldValue, int newValue) {
            int i = ((Number)ChestStealer.access$getMaxDelayValue$p(this.this$0).get()).intValue();
            if (i < newValue) {
                this.set(i);
            }
            ChestStealer.access$setNextDelay$p(this.this$0, TimeUtils.randomDelay(((Number)this.get()).intValue(), ((Number)ChestStealer.access$getMaxDelayValue$p(this.this$0).get()).intValue()));
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4);
        }
    };
    private final BoolValue takeRandomizedValue = new BoolValue("TakeRandomized", false);
    private final BoolValue onlyItemsValue = new BoolValue("OnlyItems", false);
    private final BoolValue noCompassValue = new BoolValue("NoCompass", false);
    private final BoolValue autoCloseValue = new BoolValue("AutoClose", true);
    private final IntegerValue autoCloseMaxDelayValue = new IntegerValue(this, "AutoCloseMaxDelay", 0, 0, 400){
        final /* synthetic */ ChestStealer this$0;

        protected void onChanged(int oldValue, int newValue) {
            int i = ((Number)ChestStealer.access$getAutoCloseMinDelayValue$p(this.this$0).get()).intValue();
            if (i > newValue) {
                this.set(i);
            }
            ChestStealer.access$setNextCloseDelay$p(this.this$0, TimeUtils.randomDelay(((Number)ChestStealer.access$getAutoCloseMinDelayValue$p(this.this$0).get()).intValue(), ((Number)this.get()).intValue()));
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4);
        }
    };
    private final IntegerValue autoCloseMinDelayValue = new IntegerValue(this, "AutoCloseMinDelay", 0, 0, 400){
        final /* synthetic */ ChestStealer this$0;

        protected void onChanged(int oldValue, int newValue) {
            int i = ((Number)ChestStealer.access$getAutoCloseMaxDelayValue$p(this.this$0).get()).intValue();
            if (i < newValue) {
                this.set(i);
            }
            ChestStealer.access$setNextCloseDelay$p(this.this$0, TimeUtils.randomDelay(((Number)this.get()).intValue(), ((Number)ChestStealer.access$getAutoCloseMaxDelayValue$p(this.this$0).get()).intValue()));
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4);
        }
    };
    private final BoolValue closeOnFullValue = new BoolValue("CloseOnFull", true);
    private final BoolValue chestTitleValue = new BoolValue("ChestTitle", false);
    private final MSTimer delayTimer = new MSTimer();
    private long nextDelay = TimeUtils.randomDelay(((Number)this.minDelayValue.get()).intValue(), ((Number)this.maxDelayValue.get()).intValue());
    private final MSTimer autoCloseTimer = new MSTimer();
    private long nextCloseDelay = TimeUtils.randomDelay(((Number)this.autoCloseMinDelayValue.get()).intValue(), ((Number)this.autoCloseMaxDelayValue.get()).intValue());
    private int contentReceived;

    /*
     * WARNING - void declaration
     */
    @EventTarget
    public final void onRender3D(@Nullable Render3DEvent event) {
        IGuiChest screen;
        IEntityPlayerSP thePlayer;
        block24: {
            block25: {
                Object object;
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                thePlayer = iEntityPlayerSP;
                if (!MinecraftInstance.classProvider.isGuiChest(MinecraftInstance.mc.getCurrentScreen()) || MinecraftInstance.mc.getCurrentScreen() == null || !this.delayTimer.hasTimePassed(this.nextDelay)) {
                    this.autoCloseTimer.reset();
                    return;
                }
                IGuiScreen iGuiScreen = MinecraftInstance.mc.getCurrentScreen();
                if (iGuiScreen == null) {
                    Intrinsics.throwNpe();
                }
                screen = iGuiScreen.asGuiChest();
                if (((Boolean)this.noCompassValue.get()).booleanValue() && ((object = thePlayer.getInventory().getCurrentItemInHand()) != null && (object = object.getItem()) != null ? object.getUnlocalizedName() : null).equals("item.compass")) {
                    return;
                }
                if (!((Boolean)this.chestTitleValue.get()).booleanValue()) break block24;
                if (screen.getLowerChestInventory() == null) break block25;
                IIInventory iIInventory = screen.getLowerChestInventory();
                if (iIInventory == null) {
                    Intrinsics.throwNpe();
                }
                String string = iIInventory.getName();
                IItem iItem = MinecraftInstance.functions.getObjectFromItemRegistry(MinecraftInstance.classProvider.createResourceLocation("minecraft:chest"));
                if (iItem == null) {
                    Intrinsics.throwNpe();
                }
                if (string.equals(MinecraftInstance.classProvider.createItemStack(iItem).getDisplayName())) break block24;
            }
            return;
        }
        Module module = LiquidBounce.INSTANCE.getModuleManager().get(InventoryCleaner.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.player.InventoryCleaner");
        }
        InventoryCleaner inventoryCleaner = (InventoryCleaner)module;
        if (!(this.isEmpty(screen) || ((Boolean)this.closeOnFullValue.get()).booleanValue() && this.getFullInventory())) {
            this.autoCloseTimer.reset();
            if (((Boolean)this.takeRandomizedValue.get()).booleanValue()) {
                Object slot;
                do {
                    int n = 0;
                    List items = new ArrayList();
                    n = 0;
                    int n2 = screen.getInventoryRows() * 9;
                    while (n < n2) {
                        void slotIndex;
                        IItemStack stack;
                        IContainer iContainer = screen.getInventorySlots();
                        if (iContainer == null) {
                            Intrinsics.throwNpe();
                        }
                        if (!((stack = (slot = iContainer.getSlot((int)slotIndex)).getStack()) == null || ((Boolean)this.onlyItemsValue.get()).booleanValue() && MinecraftInstance.classProvider.isItemBlock(stack.getItem()) || inventoryCleaner.getState() && !inventoryCleaner.isUseful(stack, -1))) {
                            items.add(slot);
                        }
                        ++slotIndex;
                    }
                    int randomSlot = Random.Default.nextInt(items.size());
                    ISlot slot2 = (ISlot)items.get(randomSlot);
                    this.move(screen, slot2);
                    if (!this.delayTimer.hasTimePassed(this.nextDelay)) break;
                    slot = items;
                    boolean stack = false;
                } while (!slot.isEmpty());
                return;
            }
            int items = 0;
            int n = screen.getInventoryRows() * 9;
            while (items < n) {
                void slotIndex;
                IContainer iContainer = screen.getInventorySlots();
                if (iContainer == null) {
                    Intrinsics.throwNpe();
                }
                ISlot slot = iContainer.getSlot((int)slotIndex);
                IItemStack stack = slot.getStack();
                if (this.delayTimer.hasTimePassed(this.nextDelay)) {
                    ChestStealer this_$iv = this;
                    boolean $i$f$shouldTake = false;
                    if (!(stack == null || ItemUtils.isStackEmpty(stack) || (Boolean)this_$iv.onlyItemsValue.get() != false && MinecraftInstance.classProvider.isItemBlock(stack.getItem()) || inventoryCleaner.getState() && !inventoryCleaner.isUseful(stack, -1))) {
                        this.move(screen, slot);
                    }
                }
                ++slotIndex;
            }
        } else if (((Boolean)this.autoCloseValue.get()).booleanValue()) {
            IContainer iContainer = screen.getInventorySlots();
            if (iContainer == null) {
                Intrinsics.throwNpe();
            }
            if (iContainer.getWindowId() == this.contentReceived && this.autoCloseTimer.hasTimePassed(this.nextCloseDelay)) {
                thePlayer.closeScreen();
                this.nextCloseDelay = TimeUtils.randomDelay(((Number)this.autoCloseMinDelayValue.get()).intValue(), ((Number)this.autoCloseMaxDelayValue.get()).intValue());
            }
        }
    }

    @EventTarget
    private final void onPacket(PacketEvent event) {
        IPacket packet = event.getPacket();
        if (MinecraftInstance.classProvider.isSPacketWindowItems(packet)) {
            this.contentReceived = packet.asSPacketWindowItems().getWindowId();
        }
    }

    private final boolean shouldTake(IItemStack stack, InventoryCleaner inventoryCleaner) {
        int $i$f$shouldTake = 0;
        return !(stack == null || ItemUtils.isStackEmpty(stack) || (Boolean)this.onlyItemsValue.get() != false && MinecraftInstance.classProvider.isItemBlock(stack.getItem()) || inventoryCleaner.getState() && !inventoryCleaner.isUseful(stack, -1));
    }

    private final void move(IGuiChest screen, ISlot slot) {
        screen.handleMouseClick(slot, slot.getSlotNumber(), 0, 1);
        this.delayTimer.reset();
        this.nextDelay = TimeUtils.randomDelay(((Number)this.minDelayValue.get()).intValue(), ((Number)this.maxDelayValue.get()).intValue());
    }

    /*
     * WARNING - void declaration
     */
    private final boolean isEmpty(IGuiChest chest) {
        Module module = LiquidBounce.INSTANCE.getModuleManager().get(InventoryCleaner.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.player.InventoryCleaner");
        }
        InventoryCleaner inventoryCleaner = (InventoryCleaner)module;
        int n = 0;
        int n2 = chest.getInventoryRows() * 9;
        while (n < n2) {
            void i;
            IContainer iContainer = chest.getInventorySlots();
            if (iContainer == null) {
                Intrinsics.throwNpe();
            }
            ISlot slot = iContainer.getSlot((int)i);
            IItemStack stack = slot.getStack();
            ChestStealer this_$iv = this;
            boolean $i$f$shouldTake = false;
            if (!(stack == null || ItemUtils.isStackEmpty(stack) || (Boolean)this_$iv.onlyItemsValue.get() != false && MinecraftInstance.classProvider.isItemBlock(stack.getItem()) || inventoryCleaner.getState() && !inventoryCleaner.isUseful(stack, -1))) {
                return false;
            }
            ++i;
        }
        return true;
    }

    private final boolean getFullInventory() {
        boolean bl;
        block5: {
            Object object = MinecraftInstance.mc.getThePlayer();
            if (object != null && (object = object.getInventory()) != null && (object = object.getMainInventory()) != null) {
                Iterable $this$none$iv = (Iterable)object;
                boolean $i$f$none = false;
                if ($this$none$iv instanceof Collection && ((Collection)$this$none$iv).isEmpty()) {
                    bl = true;
                } else {
                    for (Object element$iv : $this$none$iv) {
                        IItemStack p1 = (IItemStack)element$iv;
                        boolean bl2 = false;
                        if (!ItemUtils.isStackEmpty(p1)) continue;
                        bl = false;
                        break block5;
                    }
                    bl = true;
                }
            } else {
                bl = false;
            }
        }
        return bl;
    }

    public static final /* synthetic */ IntegerValue access$getMinDelayValue$p(ChestStealer $this) {
        return $this.minDelayValue;
    }

    public static final /* synthetic */ long access$getNextDelay$p(ChestStealer $this) {
        return $this.nextDelay;
    }

    public static final /* synthetic */ void access$setNextDelay$p(ChestStealer $this, long l) {
        $this.nextDelay = l;
    }

    public static final /* synthetic */ IntegerValue access$getMaxDelayValue$p(ChestStealer $this) {
        return $this.maxDelayValue;
    }

    public static final /* synthetic */ IntegerValue access$getAutoCloseMinDelayValue$p(ChestStealer $this) {
        return $this.autoCloseMinDelayValue;
    }

    public static final /* synthetic */ long access$getNextCloseDelay$p(ChestStealer $this) {
        return $this.nextCloseDelay;
    }

    public static final /* synthetic */ void access$setNextCloseDelay$p(ChestStealer $this, long l) {
        $this.nextCloseDelay = l;
    }

    public static final /* synthetic */ IntegerValue access$getAutoCloseMaxDelayValue$p(ChestStealer $this) {
        return $this.autoCloseMaxDelayValue;
    }
}

