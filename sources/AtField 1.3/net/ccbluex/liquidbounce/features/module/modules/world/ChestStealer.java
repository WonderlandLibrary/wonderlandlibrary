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
import java.util.List;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.Random;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.IExtractedFunctions;
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
    private final MSTimer delayTimer;
    private long nextDelay;
    private long nextCloseDelay;
    private final IntegerValue autoCloseMinDelayValue;
    private final BoolValue takeRandomizedValue;
    private final BoolValue noCompassValue;
    private final BoolValue chestTitleValue;
    private final BoolValue closeOnFullValue;
    private final IntegerValue minDelayValue;
    private final IntegerValue autoCloseMaxDelayValue;
    private final BoolValue autoCloseValue;
    private final MSTimer autoCloseTimer;
    private final BoolValue onlyItemsValue;
    private int contentReceived;
    private final IntegerValue maxDelayValue = new IntegerValue(this, "MaxDelay", 200, 0, 400){
        final ChestStealer this$0;

        static {
        }
        {
            this.this$0 = chestStealer;
            super(string, n, n2, n3);
        }

        protected void onChanged(int n, int n2) {
            int n3 = ((Number)ChestStealer.access$getMinDelayValue$p(this.this$0).get()).intValue();
            if (n3 > n2) {
                this.set((Object)n3);
            }
            ChestStealer.access$setNextDelay$p(this.this$0, TimeUtils.randomDelay(((Number)ChestStealer.access$getMinDelayValue$p(this.this$0).get()).intValue(), ((Number)this.get()).intValue()));
        }

        public void onChanged(Object object, Object object2) {
            this.onChanged(((Number)object).intValue(), ((Number)object2).intValue());
        }
    };

    public static final IntegerValue access$getMinDelayValue$p(ChestStealer chestStealer) {
        return chestStealer.minDelayValue;
    }

    public static final long access$getNextDelay$p(ChestStealer chestStealer) {
        return chestStealer.nextDelay;
    }

    public static final void access$setNextDelay$p(ChestStealer chestStealer, long l) {
        chestStealer.nextDelay = l;
    }

    public static final IntegerValue access$getMaxDelayValue$p(ChestStealer chestStealer) {
        return chestStealer.maxDelayValue;
    }

    public static final IExtractedFunctions access$getFunctions$p$s1046033730() {
        return MinecraftInstance.functions;
    }

    public static final long access$getNextCloseDelay$p(ChestStealer chestStealer) {
        return chestStealer.nextCloseDelay;
    }

    public static final IntegerValue access$getAutoCloseMaxDelayValue$p(ChestStealer chestStealer) {
        return chestStealer.autoCloseMaxDelayValue;
    }

    @EventTarget
    private final void onPacket(PacketEvent packetEvent) {
        IPacket iPacket = packetEvent.getPacket();
        if (MinecraftInstance.classProvider.isSPacketWindowItems(iPacket)) {
            this.contentReceived = iPacket.asSPacketWindowItems().getWindowId();
        }
    }

    private final void move(IGuiChest iGuiChest, ISlot iSlot) {
        iGuiChest.handleMouseClick(iSlot, iSlot.getSlotNumber(), 0, 1);
        this.delayTimer.reset();
        this.nextDelay = TimeUtils.randomDelay(((Number)this.minDelayValue.get()).intValue(), ((Number)this.maxDelayValue.get()).intValue());
    }

    public static final void access$setNextCloseDelay$p(ChestStealer chestStealer, long l) {
        chestStealer.nextCloseDelay = l;
    }

    public static final IntegerValue access$getAutoCloseMinDelayValue$p(ChestStealer chestStealer) {
        return chestStealer.autoCloseMinDelayValue;
    }

    public ChestStealer() {
        this.minDelayValue = new IntegerValue(this, "MinDelay", 150, 0, 400){
            final ChestStealer this$0;

            protected void onChanged(int n, int n2) {
                int n3 = ((Number)ChestStealer.access$getMaxDelayValue$p(this.this$0).get()).intValue();
                if (n3 < n2) {
                    this.set((Object)n3);
                }
                ChestStealer.access$setNextDelay$p(this.this$0, TimeUtils.randomDelay(((Number)this.get()).intValue(), ((Number)ChestStealer.access$getMaxDelayValue$p(this.this$0).get()).intValue()));
            }
            {
                this.this$0 = chestStealer;
                super(string, n, n2, n3);
            }

            public void onChanged(Object object, Object object2) {
                this.onChanged(((Number)object).intValue(), ((Number)object2).intValue());
            }

            static {
            }
        };
        this.takeRandomizedValue = new BoolValue("TakeRandomized", false);
        this.onlyItemsValue = new BoolValue("OnlyItems", false);
        this.noCompassValue = new BoolValue("NoCompass", false);
        this.autoCloseValue = new BoolValue("AutoClose", true);
        this.autoCloseMaxDelayValue = new IntegerValue(this, "AutoCloseMaxDelay", 0, 0, 400){
            final ChestStealer this$0;

            protected void onChanged(int n, int n2) {
                int n3 = ((Number)ChestStealer.access$getAutoCloseMinDelayValue$p(this.this$0).get()).intValue();
                if (n3 > n2) {
                    this.set((Object)n3);
                }
                ChestStealer.access$setNextCloseDelay$p(this.this$0, TimeUtils.randomDelay(((Number)ChestStealer.access$getAutoCloseMinDelayValue$p(this.this$0).get()).intValue(), ((Number)this.get()).intValue()));
            }
            {
                this.this$0 = chestStealer;
                super(string, n, n2, n3);
            }

            static {
            }

            public void onChanged(Object object, Object object2) {
                this.onChanged(((Number)object).intValue(), ((Number)object2).intValue());
            }
        };
        this.autoCloseMinDelayValue = new IntegerValue(this, "AutoCloseMinDelay", 0, 0, 400){
            final ChestStealer this$0;
            {
                this.this$0 = chestStealer;
                super(string, n, n2, n3);
            }

            protected void onChanged(int n, int n2) {
                int n3 = ((Number)ChestStealer.access$getAutoCloseMaxDelayValue$p(this.this$0).get()).intValue();
                if (n3 < n2) {
                    this.set((Object)n3);
                }
                ChestStealer.access$setNextCloseDelay$p(this.this$0, TimeUtils.randomDelay(((Number)this.get()).intValue(), ((Number)ChestStealer.access$getAutoCloseMaxDelayValue$p(this.this$0).get()).intValue()));
            }

            public void onChanged(Object object, Object object2) {
                this.onChanged(((Number)object).intValue(), ((Number)object2).intValue());
            }

            static {
            }
        };
        this.closeOnFullValue = new BoolValue("CloseOnFull", true);
        this.chestTitleValue = new BoolValue("ChestTitle", false);
        this.delayTimer = new MSTimer();
        this.nextDelay = TimeUtils.randomDelay(((Number)this.minDelayValue.get()).intValue(), ((Number)this.maxDelayValue.get()).intValue());
        this.autoCloseTimer = new MSTimer();
        this.nextCloseDelay = TimeUtils.randomDelay(((Number)this.autoCloseMinDelayValue.get()).intValue(), ((Number)this.autoCloseMaxDelayValue.get()).intValue());
    }

    @EventTarget
    public final void onRender3D(@Nullable Render3DEvent render3DEvent) {
        IGuiChest iGuiChest;
        IEntityPlayerSP iEntityPlayerSP;
        block21: {
            block22: {
                Object object;
                IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                iEntityPlayerSP = iEntityPlayerSP2;
                if (!MinecraftInstance.classProvider.isGuiChest(MinecraftInstance.mc.getCurrentScreen()) || MinecraftInstance.mc.getCurrentScreen() == null || !this.delayTimer.hasTimePassed(this.nextDelay)) {
                    this.autoCloseTimer.reset();
                    return;
                }
                IGuiScreen iGuiScreen = MinecraftInstance.mc.getCurrentScreen();
                if (iGuiScreen == null) {
                    Intrinsics.throwNpe();
                }
                iGuiChest = iGuiScreen.asGuiChest();
                if (((Boolean)this.noCompassValue.get()).booleanValue() && ((object = iEntityPlayerSP.getInventory().getCurrentItemInHand()) != null && (object = object.getItem()) != null ? object.getUnlocalizedName() : null).equals("item.compass")) {
                    return;
                }
                if (!((Boolean)this.chestTitleValue.get()).booleanValue()) break block21;
                if (iGuiChest.getLowerChestInventory() == null) break block22;
                IIInventory iIInventory = iGuiChest.getLowerChestInventory();
                if (iIInventory == null) {
                    Intrinsics.throwNpe();
                }
                String string = iIInventory.getName();
                IItem iItem = ChestStealer.access$getFunctions$p$s1046033730().getObjectFromItemRegistry(MinecraftInstance.classProvider.createResourceLocation("minecraft:chest"));
                if (iItem == null) {
                    Intrinsics.throwNpe();
                }
                if (string.equals(MinecraftInstance.classProvider.createItemStack(iItem).getDisplayName())) break block21;
            }
            return;
        }
        Module module = LiquidBounce.INSTANCE.getModuleManager().get(InventoryCleaner.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.player.InventoryCleaner");
        }
        InventoryCleaner inventoryCleaner = (InventoryCleaner)module;
        ChestStealer chestStealer = this;
        if (!(iGuiChest != null || ((Boolean)this.closeOnFullValue.get()).booleanValue() && this == null)) {
            this.autoCloseTimer.reset();
            if (((Boolean)this.takeRandomizedValue.get()).booleanValue()) {
                Object object;
                do {
                    int n = 0;
                    List list = new ArrayList();
                    int n2 = iGuiChest.getInventoryRows() * 9;
                    for (n = 0; n < n2; ++n) {
                        IItemStack iItemStack;
                        IContainer iContainer = iGuiChest.getInventorySlots();
                        if (iContainer == null) {
                            Intrinsics.throwNpe();
                        }
                        if ((iItemStack = (object = iContainer.getSlot(n)).getStack()) == null || ((Boolean)this.onlyItemsValue.get()).booleanValue() && MinecraftInstance.classProvider.isItemBlock(iItemStack.getItem()) || inventoryCleaner.getState() && !inventoryCleaner.isUseful(iItemStack, -1)) continue;
                        list.add(object);
                    }
                    n = Random.Default.nextInt(list.size());
                    ISlot iSlot = (ISlot)list.get(n);
                    this.move(iGuiChest, iSlot);
                    if (!this.delayTimer.hasTimePassed(this.nextDelay)) break;
                    object = list;
                    boolean bl = false;
                } while (!object.isEmpty());
                return;
            }
            int n = iGuiChest.getInventoryRows() * 9;
            for (int i = 0; i < n; ++i) {
                IContainer iContainer = iGuiChest.getInventorySlots();
                if (iContainer == null) {
                    Intrinsics.throwNpe();
                }
                ISlot iSlot = iContainer.getSlot(i);
                IItemStack iItemStack = iSlot.getStack();
                if (!this.delayTimer.hasTimePassed(this.nextDelay)) continue;
                ChestStealer chestStealer2 = this;
                boolean bl = false;
                if (!(!(iItemStack == null || ItemUtils.isStackEmpty(iItemStack) || (Boolean)ChestStealer.access$getOnlyItemsValue$p(chestStealer2).get() != false && MinecraftInstance.classProvider.isItemBlock(iItemStack.getItem()) || inventoryCleaner.getState() && !inventoryCleaner.isUseful(iItemStack, -1)))) continue;
                this.move(iGuiChest, iSlot);
            }
        } else if (((Boolean)this.autoCloseValue.get()).booleanValue()) {
            IContainer iContainer = iGuiChest.getInventorySlots();
            if (iContainer == null) {
                Intrinsics.throwNpe();
            }
            if (iContainer.getWindowId() == this.contentReceived && this.autoCloseTimer.hasTimePassed(this.nextCloseDelay)) {
                iEntityPlayerSP.closeScreen();
                this.nextCloseDelay = TimeUtils.randomDelay(((Number)this.autoCloseMinDelayValue.get()).intValue(), ((Number)this.autoCloseMaxDelayValue.get()).intValue());
            }
        }
    }

    public static final BoolValue access$getOnlyItemsValue$p(ChestStealer chestStealer) {
        return chestStealer.onlyItemsValue;
    }

    private final boolean shouldTake(IItemStack iItemStack, InventoryCleaner inventoryCleaner) {
        boolean bl = false;
        return !(iItemStack == null || ItemUtils.isStackEmpty(iItemStack) || (Boolean)ChestStealer.access$getOnlyItemsValue$p(this).get() != false && MinecraftInstance.classProvider.isItemBlock(iItemStack.getItem()) || inventoryCleaner.getState() && !inventoryCleaner.isUseful(iItemStack, -1));
    }
}

