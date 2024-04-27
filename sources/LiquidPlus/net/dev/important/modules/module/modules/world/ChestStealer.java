/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.inventory.GuiChest
 *  net.minecraft.inventory.Slot
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.server.S30PacketWindowItems
 *  net.minecraft.util.ResourceLocation
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.dev.important.modules.module.modules.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.Random;
import kotlin.text.StringsKt;
import net.dev.important.Client;
import net.dev.important.event.EventTarget;
import net.dev.important.event.MotionEvent;
import net.dev.important.event.PacketEvent;
import net.dev.important.event.Render3DEvent;
import net.dev.important.event.UpdateEvent;
import net.dev.important.gui.client.hud.element.elements.Notification;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.modules.module.modules.player.InvManager;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.timer.MSTimer;
import net.dev.important.utils.timer.TimeUtils;
import net.dev.important.value.BoolValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Info(name="ChestStealer", spacedName="Chest Stealer", description="Automatically steals all items from a chest.", category=Category.WORLD, cnName="\u62ff\u53d6\u7bb1\u5b50\u7269\u54c1")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010,\u001a\u00020\u00162\u0006\u0010-\u001a\u00020.H\u0002J\u0018\u0010/\u001a\u0002002\u0006\u00101\u001a\u00020.2\u0006\u00102\u001a\u000203H\u0002J\b\u00104\u001a\u000200H\u0016J\u0010\u00105\u001a\u0002002\u0006\u00106\u001a\u000207H\u0007J\u0010\u00108\u001a\u0002002\u0006\u00106\u001a\u000209H\u0003J\u0012\u0010:\u001a\u0002002\b\u00106\u001a\u0004\u0018\u00010;H\u0007J\u0010\u0010<\u001a\u0002002\u0006\u00106\u001a\u00020=H\u0007J\u000e\u0010>\u001a\u0002002\u0006\u00101\u001a\u00020?R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\f\u001a\u00020\rX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u000e\u0010\u0012\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0015\u001a\u00020\u00168BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0017\u0010\u0018R\u000e\u0010\u0019\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u001cX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u001cX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u001f\u001a\u00020\u0016X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b \u0010\u0018\"\u0004\b!\u0010\"R\u000e\u0010#\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010$\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010&R\u0011\u0010'\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010&R\u0011\u0010)\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010&R\u000e\u0010+\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006@"}, d2={"Lnet/dev/important/modules/module/modules/world/ChestStealer;", "Lnet/dev/important/modules/module/Module;", "()V", "autoCloseMaxDelayValue", "Lnet/dev/important/value/IntegerValue;", "autoCloseMinDelayValue", "autoCloseTimer", "Lnet/dev/important/utils/timer/MSTimer;", "autoCloseValue", "Lnet/dev/important/value/BoolValue;", "chestTitleValue", "closeOnFullValue", "contentReceived", "", "getContentReceived", "()I", "setContentReceived", "(I)V", "delayTimer", "eventModeValue", "Lnet/dev/important/value/ListValue;", "fullInventory", "", "getFullInventory", "()Z", "maxDelayValue", "minDelayValue", "nextCloseDelay", "", "nextDelay", "noCompassValue", "once", "getOnce", "setOnce", "(Z)V", "onlyItemsValue", "showStringValue", "getShowStringValue", "()Lnet/dev/important/value/BoolValue;", "silenceValue", "getSilenceValue", "stillDisplayValue", "getStillDisplayValue", "takeRandomizedValue", "isEmpty", "chest", "Lnet/minecraft/client/gui/inventory/GuiChest;", "move", "", "screen", "slot", "Lnet/minecraft/inventory/Slot;", "onDisable", "onMotion", "event", "Lnet/dev/important/event/MotionEvent;", "onPacket", "Lnet/dev/important/event/PacketEvent;", "onRender3D", "Lnet/dev/important/event/Render3DEvent;", "onUpdate", "Lnet/dev/important/event/UpdateEvent;", "performStealer", "Lnet/minecraft/client/gui/GuiScreen;", "LiquidBounce"})
public final class ChestStealer
extends Module {
    @NotNull
    private final IntegerValue maxDelayValue = new IntegerValue(this){
        final /* synthetic */ ChestStealer this$0;
        {
            this.this$0 = $receiver;
            super("MaxDelay", 200, 0, 400, "ms");
        }

        protected void onChanged(int oldValue, int newValue) {
            int i = ((Number)ChestStealer.access$getMinDelayValue$p(this.this$0).get()).intValue();
            if (i > newValue) {
                this.set(i);
            }
            ChestStealer.access$setNextDelay$p(this.this$0, TimeUtils.randomDelay(((Number)ChestStealer.access$getMinDelayValue$p(this.this$0).get()).intValue(), ((Number)this.get()).intValue()));
        }
    };
    @NotNull
    private final IntegerValue minDelayValue = new IntegerValue(this){
        final /* synthetic */ ChestStealer this$0;
        {
            this.this$0 = $receiver;
            super("MinDelay", 150, 0, 400, "ms");
        }

        protected void onChanged(int oldValue, int newValue) {
            int i = ((Number)ChestStealer.access$getMaxDelayValue$p(this.this$0).get()).intValue();
            if (i < newValue) {
                this.set(i);
            }
            ChestStealer.access$setNextDelay$p(this.this$0, TimeUtils.randomDelay(((Number)this.get()).intValue(), ((Number)ChestStealer.access$getMaxDelayValue$p(this.this$0).get()).intValue()));
        }
    };
    @NotNull
    private final ListValue eventModeValue;
    @NotNull
    private final BoolValue takeRandomizedValue;
    @NotNull
    private final BoolValue onlyItemsValue;
    @NotNull
    private final BoolValue noCompassValue;
    @NotNull
    private final BoolValue autoCloseValue;
    @NotNull
    private final BoolValue silenceValue;
    @NotNull
    private final BoolValue showStringValue;
    @NotNull
    private final BoolValue stillDisplayValue;
    @NotNull
    private final IntegerValue autoCloseMaxDelayValue;
    @NotNull
    private final IntegerValue autoCloseMinDelayValue;
    @NotNull
    private final BoolValue closeOnFullValue;
    @NotNull
    private final BoolValue chestTitleValue;
    @NotNull
    private final MSTimer delayTimer;
    private long nextDelay;
    @NotNull
    private final MSTimer autoCloseTimer;
    private long nextCloseDelay;
    private int contentReceived;
    private boolean once;

    public ChestStealer() {
        String[] stringArray = new String[]{"Render3D", "Update", "MotionPre", "MotionPost"};
        this.eventModeValue = new ListValue("OnEvent", stringArray, "Render3D");
        this.takeRandomizedValue = new BoolValue("TakeRandomized", false);
        this.onlyItemsValue = new BoolValue("OnlyItems", false);
        this.noCompassValue = new BoolValue("NoCompass", false);
        this.autoCloseValue = new BoolValue("AutoClose", true);
        this.silenceValue = new BoolValue("SilentMode", true);
        this.showStringValue = new BoolValue("Silent-ShowString", false, new Function0<Boolean>(this){
            final /* synthetic */ ChestStealer this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return (Boolean)this.this$0.getSilenceValue().get();
            }
        });
        this.stillDisplayValue = new BoolValue("Silent-StillDisplay", false, new Function0<Boolean>(this){
            final /* synthetic */ ChestStealer this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return (Boolean)this.this$0.getSilenceValue().get();
            }
        });
        this.autoCloseMaxDelayValue = new IntegerValue(this){
            final /* synthetic */ ChestStealer this$0;
            {
                this.this$0 = $receiver;
                super("AutoCloseMaxDelay", 0, 0, 400, "ms");
            }

            protected void onChanged(int oldValue, int newValue) {
                int i = ((Number)ChestStealer.access$getAutoCloseMinDelayValue$p(this.this$0).get()).intValue();
                if (i > newValue) {
                    this.set(i);
                }
                ChestStealer.access$setNextCloseDelay$p(this.this$0, TimeUtils.randomDelay(((Number)ChestStealer.access$getAutoCloseMinDelayValue$p(this.this$0).get()).intValue(), ((Number)this.get()).intValue()));
            }
        };
        this.autoCloseMinDelayValue = new IntegerValue(this){
            final /* synthetic */ ChestStealer this$0;
            {
                this.this$0 = $receiver;
                super("AutoCloseMinDelay", 0, 0, 400, "ms");
            }

            protected void onChanged(int oldValue, int newValue) {
                int i = ((Number)ChestStealer.access$getAutoCloseMaxDelayValue$p(this.this$0).get()).intValue();
                if (i < newValue) {
                    this.set(i);
                }
                ChestStealer.access$setNextCloseDelay$p(this.this$0, TimeUtils.randomDelay(((Number)this.get()).intValue(), ((Number)ChestStealer.access$getAutoCloseMaxDelayValue$p(this.this$0).get()).intValue()));
            }
        };
        this.closeOnFullValue = new BoolValue("CloseOnFull", true);
        this.chestTitleValue = new BoolValue("ChestTitle", false);
        this.delayTimer = new MSTimer();
        this.nextDelay = TimeUtils.randomDelay(((Number)this.minDelayValue.get()).intValue(), ((Number)this.maxDelayValue.get()).intValue());
        this.autoCloseTimer = new MSTimer();
        this.nextCloseDelay = TimeUtils.randomDelay(((Number)this.autoCloseMinDelayValue.get()).intValue(), ((Number)this.autoCloseMaxDelayValue.get()).intValue());
    }

    @NotNull
    public final BoolValue getSilenceValue() {
        return this.silenceValue;
    }

    @NotNull
    public final BoolValue getShowStringValue() {
        return this.showStringValue;
    }

    @NotNull
    public final BoolValue getStillDisplayValue() {
        return this.stillDisplayValue;
    }

    public final int getContentReceived() {
        return this.contentReceived;
    }

    public final void setContentReceived(int n) {
        this.contentReceived = n;
    }

    public final boolean getOnce() {
        return this.once;
    }

    public final void setOnce(boolean bl) {
        this.once = bl;
    }

    @Override
    public void onDisable() {
        this.once = false;
    }

    @EventTarget
    public final void onRender3D(@Nullable Render3DEvent event) {
        GuiScreen guiScreen = MinecraftInstance.mc.field_71462_r;
        if (guiScreen == null) {
            return;
        }
        GuiScreen screen = guiScreen;
        if (StringsKt.equals((String)this.eventModeValue.get(), "render3d", true)) {
            this.performStealer(screen);
        }
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        GuiScreen guiScreen = MinecraftInstance.mc.field_71462_r;
        if (guiScreen == null) {
            return;
        }
        GuiScreen screen = guiScreen;
        if (StringsKt.equals((String)this.eventModeValue.get(), "update", true)) {
            this.performStealer(screen);
        }
    }

    @EventTarget
    public final void onMotion(@NotNull MotionEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        GuiScreen guiScreen = MinecraftInstance.mc.field_71462_r;
        if (guiScreen == null) {
            return;
        }
        GuiScreen screen = guiScreen;
        if (StringsKt.equals((String)this.eventModeValue.get(), Intrinsics.stringPlus("motion", event.getEventState().getStateName()), true)) {
            this.performStealer(screen);
        }
    }

    public final void performStealer(@NotNull GuiScreen screen) {
        block21: {
            block22: {
                Intrinsics.checkNotNullParameter(screen, "screen");
                if (this.once && !(screen instanceof GuiChest)) {
                    this.setState(false);
                    return;
                }
                if (!(screen instanceof GuiChest) || !this.delayTimer.hasTimePassed(this.nextDelay)) {
                    this.autoCloseTimer.reset();
                    return;
                }
                if (!this.once && ((Boolean)this.noCompassValue.get()).booleanValue()) {
                    String string;
                    ItemStack itemStack = MinecraftInstance.mc.field_71439_g.field_71071_by.func_70448_g();
                    if (itemStack == null) {
                        string = null;
                    } else {
                        Item item = itemStack.func_77973_b();
                        string = item == null ? null : item.func_77658_a();
                    }
                    if (Intrinsics.areEqual(string, "item.compass")) {
                        return;
                    }
                }
                if (this.once || !((Boolean)this.chestTitleValue.get()).booleanValue()) break block21;
                if (((GuiChest)screen).field_147015_w == null) break block22;
                String string = ((GuiChest)screen).field_147015_w.func_70005_c_();
                Intrinsics.checkNotNullExpressionValue(string, "screen.lowerChestInventory.name");
                CharSequence charSequence = string;
                string = new ItemStack((Item)Item.field_150901_e.func_82594_a((Object)new ResourceLocation("minecraft:chest"))).func_82833_r();
                Intrinsics.checkNotNullExpressionValue(string, "ItemStack(Item.itemRegis\u2026aft:chest\"))).displayName");
                if (StringsKt.contains$default(charSequence, string, false, 2, null)) break block21;
            }
            return;
        }
        Module module2 = Client.INSTANCE.getModuleManager().get(InvManager.class);
        if (module2 == null) {
            throw new NullPointerException("null cannot be cast to non-null type net.dev.important.modules.module.modules.player.InvManager");
        }
        InvManager inventoryCleaner = (InvManager)module2;
        if (!(this.isEmpty((GuiChest)screen) || ((Boolean)this.closeOnFullValue.get()).booleanValue() && this.getFullInventory())) {
            this.autoCloseTimer.reset();
            if (((Boolean)this.takeRandomizedValue.get()).booleanValue()) {
                List items;
                boolean noLoop = false;
                do {
                    items = new ArrayList();
                    int n = 0;
                    int n2 = ((GuiChest)screen).field_147018_x * 9;
                    while (n < n2) {
                        int slotIndex;
                        Slot slot;
                        if ((slot = (Slot)((GuiChest)screen).field_147002_h.field_75151_b.get(slotIndex = n++)).func_75211_c() == null || ((Boolean)this.onlyItemsValue.get()).booleanValue() && slot.func_75211_c().func_77973_b() instanceof ItemBlock) continue;
                        if (inventoryCleaner.getState()) {
                            ItemStack itemStack = slot.func_75211_c();
                            Intrinsics.checkNotNullExpressionValue(itemStack, "slot.stack");
                            if (!inventoryCleaner.isUseful(itemStack, -1)) continue;
                        }
                        Intrinsics.checkNotNullExpressionValue(slot, "slot");
                        items.add(slot);
                    }
                    int randomSlot = Random.Default.nextInt(items.size());
                    Slot slot = (Slot)items.get(randomSlot);
                    this.move((GuiChest)screen, slot);
                    if (this.nextDelay != 0L && !this.delayTimer.hasTimePassed(this.nextDelay)) continue;
                    noLoop = true;
                } while (this.delayTimer.hasTimePassed(this.nextDelay) && !((Collection)items).isEmpty() && !noLoop);
                return;
            }
            int n = 0;
            int n3 = ((GuiChest)screen).field_147018_x * 9;
            while (n < n3) {
                int slotIndex = n++;
                Slot slot = (Slot)((GuiChest)screen).field_147002_h.field_75151_b.get(slotIndex);
                if (!this.delayTimer.hasTimePassed(this.nextDelay) || slot.func_75211_c() == null || ((Boolean)this.onlyItemsValue.get()).booleanValue() && slot.func_75211_c().func_77973_b() instanceof ItemBlock) continue;
                if (inventoryCleaner.getState()) {
                    ItemStack itemStack = slot.func_75211_c();
                    Intrinsics.checkNotNullExpressionValue(itemStack, "slot.stack");
                    if (!inventoryCleaner.isUseful(itemStack, -1)) continue;
                }
                GuiChest guiChest = (GuiChest)screen;
                Intrinsics.checkNotNullExpressionValue(slot, "slot");
                this.move(guiChest, slot);
            }
        } else if (((Boolean)this.autoCloseValue.get()).booleanValue() && ((GuiChest)screen).field_147002_h.field_75152_c == this.contentReceived && this.autoCloseTimer.hasTimePassed(this.nextCloseDelay)) {
            MinecraftInstance.mc.field_71439_g.func_71053_j();
            if (((Boolean)this.silenceValue.get()).booleanValue() && !((Boolean)this.stillDisplayValue.get()).booleanValue()) {
                Client.INSTANCE.getHud().addNotification(new Notification("Closed chest.", Notification.Type.INFO));
            }
            this.nextCloseDelay = TimeUtils.randomDelay(((Number)this.autoCloseMinDelayValue.get()).intValue(), ((Number)this.autoCloseMaxDelayValue.get()).intValue());
            if (this.once) {
                this.once = false;
                this.setState(false);
                return;
            }
        }
    }

    @EventTarget
    private final void onPacket(PacketEvent event) {
        Packet<?> packet = event.getPacket();
        if (packet instanceof S30PacketWindowItems) {
            this.contentReceived = ((S30PacketWindowItems)packet).func_148911_c();
        }
    }

    private final void move(GuiChest screen, Slot slot) {
        screen.func_146984_a(slot, slot.field_75222_d, 0, 1);
        this.delayTimer.reset();
        this.nextDelay = TimeUtils.randomDelay(((Number)this.minDelayValue.get()).intValue(), ((Number)this.maxDelayValue.get()).intValue());
    }

    private final boolean isEmpty(GuiChest chest) {
        Module module2 = Client.INSTANCE.getModuleManager().get(InvManager.class);
        if (module2 == null) {
            throw new NullPointerException("null cannot be cast to non-null type net.dev.important.modules.module.modules.player.InvManager");
        }
        InvManager inventoryCleaner = (InvManager)module2;
        int n = 0;
        int n2 = chest.field_147018_x * 9;
        while (n < n2) {
            int i;
            Slot slot;
            if ((slot = (Slot)chest.field_147002_h.field_75151_b.get(i = n++)).func_75211_c() == null || ((Boolean)this.onlyItemsValue.get()).booleanValue() && slot.func_75211_c().func_77973_b() instanceof ItemBlock) continue;
            if (inventoryCleaner.getState()) {
                ItemStack itemStack = slot.func_75211_c();
                Intrinsics.checkNotNullExpressionValue(itemStack, "slot.stack");
                if (!inventoryCleaner.isUseful(itemStack, -1)) continue;
            }
            return false;
        }
        return true;
    }

    private final boolean getFullInventory() {
        boolean bl;
        block1: {
            ItemStack[] itemStackArray = MinecraftInstance.mc.field_71439_g.field_71071_by.field_70462_a;
            Intrinsics.checkNotNullExpressionValue(itemStackArray, "mc.thePlayer.inventory.mainInventory");
            Object[] $this$none$iv = itemStackArray;
            boolean $i$f$none = false;
            for (Object element$iv : $this$none$iv) {
                ItemStack it = (ItemStack)element$iv;
                boolean bl2 = false;
                if (!(it == null)) continue;
                bl = false;
                break block1;
            }
            bl = true;
        }
        return bl;
    }

    public static final /* synthetic */ IntegerValue access$getMinDelayValue$p(ChestStealer $this) {
        return $this.minDelayValue;
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

    public static final /* synthetic */ void access$setNextCloseDelay$p(ChestStealer $this, long l) {
        $this.nextCloseDelay = l;
    }

    public static final /* synthetic */ IntegerValue access$getAutoCloseMaxDelayValue$p(ChestStealer $this) {
        return $this.autoCloseMaxDelayValue;
    }
}

