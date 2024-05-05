/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.inventory.GuiChest
 *  net.minecraft.inventory.IInventory
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
package net.ccbluex.liquidbounce.features.module.modules.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.Random;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.player.InventoryCleaner;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.misc.SoundFxPlayer;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="Stealer", description="Open the chest to get items.", category=ModuleCategory.WORLD)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000~\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u000f\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u00103\u001a\u00020\u00192\u0006\u00104\u001a\u000205H\u0002J\u0018\u00106\u001a\u0002072\u0006\u00108\u001a\u0002052\u0006\u00109\u001a\u00020:H\u0002J\b\u0010;\u001a\u000207H\u0016J\u0010\u0010<\u001a\u0002072\u0006\u0010=\u001a\u00020>H\u0007J\u0010\u0010?\u001a\u0002072\u0006\u0010=\u001a\u00020@H\u0003J\u0012\u0010A\u001a\u0002072\b\u0010=\u001a\u0004\u0018\u00010BH\u0007J\u0010\u0010C\u001a\u0002072\u0006\u0010=\u001a\u00020DH\u0007J\u000e\u0010E\u001a\u0002072\u0006\u00108\u001a\u00020FR\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000f\u001a\u00020\u0010X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u000e\u0010\u0015\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0018\u001a\u00020\u00198BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001a\u0010\u001bR\u000e\u0010\u001c\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u001fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u001fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010#\u001a\u00020\u0019X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b$\u0010\u001b\"\u0004\b%\u0010&R\u000e\u0010'\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010(\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010\u0006R\u0011\u0010*\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010\u0006R\u0011\u0010,\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010\u0006R\u0016\u0010.\u001a\u0004\u0018\u00010/8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b0\u00101R\u000e\u00102\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006G"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/world/ChestStealer;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "ArrayListTag", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "getArrayListTag", "()Lnet/ccbluex/liquidbounce/value/BoolValue;", "autoCloseMaxDelayValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "autoCloseMinDelayValue", "autoCloseTimer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "autoCloseValue", "chestTitleValue", "closeOnFullValue", "contentReceived", "", "getContentReceived", "()I", "setContentReceived", "(I)V", "delayTimer", "eventModeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "fullInventory", "", "getFullInventory", "()Z", "maxDelayValue", "minDelayValue", "nextCloseDelay", "", "nextDelay", "noCompassValue", "noDuplicateValue", "once", "getOnce", "setOnce", "(Z)V", "onlyItemsValue", "showStringValue", "getShowStringValue", "silenceValue", "getSilenceValue", "stillDisplayValue", "getStillDisplayValue", "tag", "", "getTag", "()Ljava/lang/String;", "takeRandomizedValue", "isEmpty", "chest", "Lnet/minecraft/client/gui/inventory/GuiChest;", "move", "", "screen", "slot", "Lnet/minecraft/inventory/Slot;", "onDisable", "onMotion", "event", "Lnet/ccbluex/liquidbounce/event/MotionEvent;", "onPacket", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onRender3D", "Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "performStealer", "Lnet/minecraft/client/gui/GuiScreen;", "KyinoClient"})
public final class ChestStealer
extends Module {
    private final IntegerValue maxDelayValue = new IntegerValue(this, "MaxDelay", 80, 0, 400, "ms"){
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
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4, $super_call_param$5);
        }
    };
    private final IntegerValue minDelayValue = new IntegerValue(this, "MinDelay", 60, 0, 400, "ms"){
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
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4, $super_call_param$5);
        }
    };
    private final ListValue eventModeValue = new ListValue("OnEvent", new String[]{"Render3D", "Update", "MotionPre", "MotionPost"}, "Update");
    private final BoolValue takeRandomizedValue = new BoolValue("TakeRandomized", true);
    private final BoolValue onlyItemsValue = new BoolValue("OnlyItems", false);
    private final BoolValue noCompassValue = new BoolValue("NoCompass", false);
    private final BoolValue noDuplicateValue = new BoolValue("NoDuplicateNonStackable", false);
    private final BoolValue autoCloseValue = new BoolValue("AutoClose", true);
    @NotNull
    private final BoolValue silenceValue = new BoolValue("SilentMode", true);
    @NotNull
    private final BoolValue showStringValue = new BoolValue("Silent-ShowString", true, new Function0<Boolean>(this){
        final /* synthetic */ ChestStealer this$0;

        public final boolean invoke() {
            return (Boolean)this.this$0.getSilenceValue().get();
        }
        {
            this.this$0 = chestStealer;
            super(0);
        }
    });
    @NotNull
    private final BoolValue stillDisplayValue = new BoolValue("Silent-StillDisplay", true, new Function0<Boolean>(this){
        final /* synthetic */ ChestStealer this$0;

        public final boolean invoke() {
            return (Boolean)this.this$0.getSilenceValue().get();
        }
        {
            this.this$0 = chestStealer;
            super(0);
        }
    });
    private final IntegerValue autoCloseMaxDelayValue = new IntegerValue(this, "AutoCloseMaxDelay", 5, 0, 400, "ms"){
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
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4, $super_call_param$5);
        }
    };
    private final IntegerValue autoCloseMinDelayValue = new IntegerValue(this, "AutoCloseMinDelay", 5, 0, 400, "ms"){
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
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4, $super_call_param$5);
        }
    };
    private final BoolValue closeOnFullValue = new BoolValue("CloseOnFull", true);
    private final BoolValue chestTitleValue = new BoolValue("ChestTitle", false);
    @NotNull
    private final BoolValue ArrayListTag = new BoolValue("ArrayList-Tag", true);
    private final MSTimer delayTimer = new MSTimer();
    private long nextDelay = TimeUtils.randomDelay(((Number)this.minDelayValue.get()).intValue(), ((Number)this.maxDelayValue.get()).intValue());
    private final MSTimer autoCloseTimer = new MSTimer();
    private long nextCloseDelay = TimeUtils.randomDelay(((Number)this.autoCloseMinDelayValue.get()).intValue(), ((Number)this.autoCloseMaxDelayValue.get()).intValue());
    private int contentReceived;
    private boolean once;

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

    @NotNull
    public final BoolValue getArrayListTag() {
        return this.ArrayListTag;
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
        GuiScreen guiScreen = ChestStealer.access$getMc$p$s1046033730().field_71462_r;
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
        Intrinsics.checkParameterIsNotNull(event, "event");
        GuiScreen guiScreen = ChestStealer.access$getMc$p$s1046033730().field_71462_r;
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
        Intrinsics.checkParameterIsNotNull(event, "event");
        GuiScreen guiScreen = ChestStealer.access$getMc$p$s1046033730().field_71462_r;
        if (guiScreen == null) {
            return;
        }
        GuiScreen screen = guiScreen;
        if (StringsKt.equals((String)this.eventModeValue.get(), "motion" + event.getEventState().getStateName(), true)) {
            this.performStealer(screen);
        }
    }

    /*
     * WARNING - void declaration
     */
    public final void performStealer(@NotNull GuiScreen screen) {
        block38: {
            block28: {
                InventoryCleaner inventoryCleaner;
                block29: {
                    Object slot;
                    block26: {
                        block27: {
                            ItemStack itemStack;
                            Intrinsics.checkParameterIsNotNull(screen, "screen");
                            if (this.once && !(screen instanceof GuiChest)) {
                                this.setState(false);
                                return;
                            }
                            if (!(screen instanceof GuiChest) || !this.delayTimer.hasTimePassed(this.nextDelay)) {
                                this.autoCloseTimer.reset();
                                return;
                            }
                            if (!this.once && ((Boolean)this.noCompassValue.get()).booleanValue() && Intrinsics.areEqual((itemStack = ChestStealer.access$getMc$p$s1046033730().field_71439_g.field_71071_by.func_70448_g()) != null && (itemStack = itemStack.func_77973_b()) != null ? itemStack.func_77658_a() : null, "item.compass")) {
                                return;
                            }
                            if (this.once || !((Boolean)this.chestTitleValue.get()).booleanValue()) break block26;
                            if (((GuiChest)screen).field_147015_w == null) break block27;
                            IInventory iInventory = ((GuiChest)screen).field_147015_w;
                            Intrinsics.checkExpressionValueIsNotNull(iInventory, "screen.lowerChestInventory");
                            String string = iInventory.func_70005_c_();
                            Intrinsics.checkExpressionValueIsNotNull(string, "screen.lowerChestInventory.name");
                            CharSequence charSequence = string;
                            String string2 = new ItemStack((Item)Item.field_150901_e.func_82594_a((Object)new ResourceLocation("minecraft:chest"))).func_82833_r();
                            Intrinsics.checkExpressionValueIsNotNull(string2, "ItemStack(Item.itemRegis\u2026aft:chest\"))).displayName");
                            if (StringsKt.contains$default(charSequence, string2, false, 2, null)) break block26;
                        }
                        return;
                    }
                    Module module = LiquidBounce.INSTANCE.getModuleManager().get(InventoryCleaner.class);
                    if (module == null) {
                        throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.player.InventoryCleaner");
                    }
                    inventoryCleaner = (InventoryCleaner)module;
                    if (this.isEmpty((GuiChest)screen) || ((Boolean)this.closeOnFullValue.get()).booleanValue() && this.getFullInventory()) break block28;
                    this.autoCloseTimer.reset();
                    if (!((Boolean)this.takeRandomizedValue.get()).booleanValue()) break block29;
                    boolean noLoop = false;
                    do {
                        int n = 0;
                        List items = new ArrayList();
                        n = 0;
                        int n2 = ((GuiChest)screen).field_147018_x * 9;
                        while (n < n2) {
                            void slotIndex;
                            block30: {
                                block33: {
                                    block32: {
                                        void $this$mapTo$iv$iv;
                                        Iterable $this$filterTo$iv$iv;
                                        block31: {
                                            slot = (Slot)((GuiChest)screen).field_147002_h.field_75151_b.get((int)slotIndex);
                                            Slot slot2 = slot;
                                            Intrinsics.checkExpressionValueIsNotNull(slot2, "slot");
                                            if (slot2.func_75211_c() == null) break block30;
                                            if (!((Boolean)this.onlyItemsValue.get()).booleanValue()) break block31;
                                            ItemStack itemStack = slot.func_75211_c();
                                            Intrinsics.checkExpressionValueIsNotNull(itemStack, "slot.stack");
                                            if (itemStack.func_77973_b() instanceof ItemBlock) break block30;
                                        }
                                        if (!((Boolean)this.noDuplicateValue.get()).booleanValue()) break block32;
                                        ItemStack itemStack = slot.func_75211_c();
                                        Intrinsics.checkExpressionValueIsNotNull(itemStack, "slot.stack");
                                        if (itemStack.func_77976_d() > 1) break block32;
                                        Intrinsics.checkExpressionValueIsNotNull(ChestStealer.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70462_a, "mc.thePlayer.inventory.mainInventory");
                                        ItemStack[] $this$filter$iv = ChestStealer.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70462_a;
                                        boolean $i$f$filter = false;
                                        ItemStack[] itemStackArray = $this$filter$iv;
                                        Collection destination$iv$iv = new ArrayList();
                                        boolean $i$f$filterTo = false;
                                        Iterator iterator2 = $this$filterTo$iv$iv;
                                        int n3 = ((void)iterator2).length;
                                        for (int i = 0; i < n3; ++i) {
                                            void element$iv$iv;
                                            void it = element$iv$iv = iterator2[i];
                                            boolean bl = false;
                                            if (!(it != null && it.func_77973_b() != null)) continue;
                                            destination$iv$iv.add(element$iv$iv);
                                        }
                                        Iterable $this$map$iv = (List)destination$iv$iv;
                                        boolean $i$f$map = false;
                                        $this$filterTo$iv$iv = $this$map$iv;
                                        destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
                                        boolean $i$f$mapTo = false;
                                        for (Object item$iv$iv : $this$mapTo$iv$iv) {
                                            Item item;
                                            void it;
                                            ItemStack itemStack2 = (ItemStack)item$iv$iv;
                                            Collection collection = destination$iv$iv;
                                            boolean bl = false;
                                            void v9 = it;
                                            Intrinsics.checkExpressionValueIsNotNull(v9, "it");
                                            if (v9.func_77973_b() == null) {
                                                Intrinsics.throwNpe();
                                            }
                                            collection.add(item);
                                        }
                                        List list = (List)destination$iv$iv;
                                        ItemStack itemStack3 = slot.func_75211_c();
                                        Intrinsics.checkExpressionValueIsNotNull(itemStack3, "slot.stack");
                                        if (list.contains(itemStack3.func_77973_b())) break block30;
                                    }
                                    if (!inventoryCleaner.getState()) break block33;
                                    ItemStack itemStack = slot.func_75211_c();
                                    Intrinsics.checkExpressionValueIsNotNull(itemStack, "slot.stack");
                                    if (!inventoryCleaner.isUseful(itemStack, -1)) break block30;
                                }
                                items.add(slot);
                            }
                            ++slotIndex;
                        }
                        int randomSlot = Random.Default.nextInt(items.size());
                        Slot slot3 = (Slot)items.get(randomSlot);
                        this.move((GuiChest)screen, slot3);
                        if (this.nextDelay == 0L || this.delayTimer.hasTimePassed(this.nextDelay)) {
                            noLoop = true;
                        }
                        if (!this.delayTimer.hasTimePassed(this.nextDelay)) break;
                        slot = items;
                        boolean $this$map$iv = false;
                    } while (!slot.isEmpty() && !noLoop);
                    return;
                }
                int noLoop = 0;
                int n = ((GuiChest)screen).field_147018_x * 9;
                while (noLoop < n) {
                    void slotIndex;
                    block34: {
                        Slot slot;
                        block37: {
                            block36: {
                                void $this$mapTo$iv$iv;
                                Iterable $this$filterTo$iv$iv;
                                block35: {
                                    slot = (Slot)((GuiChest)screen).field_147002_h.field_75151_b.get((int)slotIndex);
                                    if (!this.delayTimer.hasTimePassed(this.nextDelay)) break block34;
                                    Slot slot4 = slot;
                                    Intrinsics.checkExpressionValueIsNotNull(slot4, "slot");
                                    if (slot4.func_75211_c() == null) break block34;
                                    if (!((Boolean)this.onlyItemsValue.get()).booleanValue()) break block35;
                                    ItemStack itemStack = slot.func_75211_c();
                                    Intrinsics.checkExpressionValueIsNotNull(itemStack, "slot.stack");
                                    if (itemStack.func_77973_b() instanceof ItemBlock) break block34;
                                }
                                if (!((Boolean)this.noDuplicateValue.get()).booleanValue()) break block36;
                                ItemStack itemStack = slot.func_75211_c();
                                Intrinsics.checkExpressionValueIsNotNull(itemStack, "slot.stack");
                                if (itemStack.func_77976_d() > 1) break block36;
                                Intrinsics.checkExpressionValueIsNotNull(ChestStealer.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70462_a, "mc.thePlayer.inventory.mainInventory");
                                ItemStack[] $this$filter$iv = ChestStealer.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70462_a;
                                boolean $i$f$filter = false;
                                ItemStack[] $this$map$iv = $this$filter$iv;
                                Collection destination$iv$iv = new ArrayList();
                                boolean $i$f$filterTo = false;
                                Iterator iterator3 = $this$filterTo$iv$iv;
                                int $i$f$mapTo = ((void)iterator3).length;
                                for (int i = 0; i < $i$f$mapTo; ++i) {
                                    void element$iv$iv;
                                    void it = element$iv$iv = iterator3[i];
                                    boolean bl = false;
                                    if (!(it != null && it.func_77973_b() != null)) continue;
                                    destination$iv$iv.add(element$iv$iv);
                                }
                                Iterable $this$map$iv2 = (List)destination$iv$iv;
                                boolean $i$f$map = false;
                                $this$filterTo$iv$iv = $this$map$iv2;
                                destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv2, 10));
                                boolean $i$f$mapTo2 = false;
                                for (Object item$iv$iv : $this$mapTo$iv$iv) {
                                    Item item;
                                    void it;
                                    ItemStack itemStack4 = (ItemStack)item$iv$iv;
                                    Collection collection = destination$iv$iv;
                                    boolean bl = false;
                                    void v16 = it;
                                    Intrinsics.checkExpressionValueIsNotNull(v16, "it");
                                    if (v16.func_77973_b() == null) {
                                        Intrinsics.throwNpe();
                                    }
                                    collection.add(item);
                                }
                                List list = (List)destination$iv$iv;
                                ItemStack itemStack5 = slot.func_75211_c();
                                Intrinsics.checkExpressionValueIsNotNull(itemStack5, "slot.stack");
                                if (list.contains(itemStack5.func_77973_b())) break block34;
                            }
                            if (!inventoryCleaner.getState()) break block37;
                            ItemStack itemStack = slot.func_75211_c();
                            Intrinsics.checkExpressionValueIsNotNull(itemStack, "slot.stack");
                            if (!inventoryCleaner.isUseful(itemStack, -1)) break block34;
                        }
                        this.move((GuiChest)screen, slot);
                    }
                    ++slotIndex;
                }
                break block38;
            }
            if (((Boolean)this.autoCloseValue.get()).booleanValue() && ((GuiChest)screen).field_147002_h.field_75152_c == this.contentReceived && this.autoCloseTimer.hasTimePassed(this.nextCloseDelay)) {
                ChestStealer.access$getMc$p$s1046033730().field_71439_g.func_71053_j();
                if (((Boolean)this.silenceValue.get()).booleanValue() && !((Boolean)this.stillDisplayValue.get()).booleanValue()) {
                    new SoundFxPlayer().playSound(SoundFxPlayer.SoundType.VICTORY, -8.0f);
                }
                LiquidBounce.INSTANCE.getHud().addNotification(new Notification(this.getName() + ": Closed chest.", Notification.Type.INFO));
                this.nextCloseDelay = TimeUtils.randomDelay(((Number)this.autoCloseMinDelayValue.get()).intValue(), ((Number)this.autoCloseMaxDelayValue.get()).intValue());
                if (this.once) {
                    this.once = false;
                    this.setState(false);
                    return;
                }
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

    /*
     * WARNING - void declaration
     */
    private final boolean isEmpty(GuiChest chest) {
        Module module = LiquidBounce.INSTANCE.getModuleManager().get(InventoryCleaner.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.player.InventoryCleaner");
        }
        InventoryCleaner inventoryCleaner = (InventoryCleaner)module;
        int n = 0;
        int n2 = chest.field_147018_x * 9;
        while (n < n2) {
            void i;
            block8: {
                block11: {
                    Slot slot;
                    block10: {
                        void $this$mapTo$iv$iv;
                        Iterable $this$filterTo$iv$iv;
                        block9: {
                            Slot slot2 = slot = (Slot)chest.field_147002_h.field_75151_b.get((int)i);
                            Intrinsics.checkExpressionValueIsNotNull(slot2, "slot");
                            if (slot2.func_75211_c() == null) break block8;
                            if (!((Boolean)this.onlyItemsValue.get()).booleanValue()) break block9;
                            ItemStack itemStack = slot.func_75211_c();
                            Intrinsics.checkExpressionValueIsNotNull(itemStack, "slot.stack");
                            if (itemStack.func_77973_b() instanceof ItemBlock) break block8;
                        }
                        if (!((Boolean)this.noDuplicateValue.get()).booleanValue()) break block10;
                        ItemStack itemStack = slot.func_75211_c();
                        Intrinsics.checkExpressionValueIsNotNull(itemStack, "slot.stack");
                        if (itemStack.func_77976_d() > 1) break block10;
                        Intrinsics.checkExpressionValueIsNotNull(ChestStealer.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70462_a, "mc.thePlayer.inventory.mainInventory");
                        ItemStack[] $this$filter$iv = ChestStealer.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70462_a;
                        boolean $i$f$filter = false;
                        ItemStack[] itemStackArray = $this$filter$iv;
                        Collection destination$iv$iv = new ArrayList();
                        boolean $i$f$filterTo = false;
                        Iterator iterator2 = $this$filterTo$iv$iv;
                        int n3 = ((void)iterator2).length;
                        for (int j = 0; j < n3; ++j) {
                            void element$iv$iv;
                            void it = element$iv$iv = iterator2[j];
                            boolean bl = false;
                            if (!(it != null && it.func_77973_b() != null)) continue;
                            destination$iv$iv.add(element$iv$iv);
                        }
                        Iterable $this$map$iv = (List)destination$iv$iv;
                        boolean $i$f$map = false;
                        $this$filterTo$iv$iv = $this$map$iv;
                        destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
                        boolean $i$f$mapTo = false;
                        for (Object item$iv$iv : $this$mapTo$iv$iv) {
                            Item item;
                            void it;
                            ItemStack itemStack2 = (ItemStack)item$iv$iv;
                            Collection collection = destination$iv$iv;
                            boolean bl = false;
                            void v4 = it;
                            Intrinsics.checkExpressionValueIsNotNull(v4, "it");
                            if (v4.func_77973_b() == null) {
                                Intrinsics.throwNpe();
                            }
                            collection.add(item);
                        }
                        List list = (List)destination$iv$iv;
                        ItemStack itemStack3 = slot.func_75211_c();
                        Intrinsics.checkExpressionValueIsNotNull(itemStack3, "slot.stack");
                        if (list.contains(itemStack3.func_77973_b())) break block8;
                    }
                    if (!inventoryCleaner.getState()) break block11;
                    ItemStack itemStack = slot.func_75211_c();
                    Intrinsics.checkExpressionValueIsNotNull(itemStack, "slot.stack");
                    if (!inventoryCleaner.isUseful(itemStack, -1)) break block8;
                }
                return false;
            }
            ++i;
        }
        return true;
    }

    private final boolean getFullInventory() {
        boolean bl;
        block1: {
            Intrinsics.checkExpressionValueIsNotNull(ChestStealer.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70462_a, "mc.thePlayer.inventory.mainInventory");
            ItemStack[] $this$none$iv = ChestStealer.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70462_a;
            boolean $i$f$none = false;
            ItemStack[] itemStackArray = $this$none$iv;
            int n = itemStackArray.length;
            for (int i = 0; i < n; ++i) {
                ItemStack element$iv;
                ItemStack it = element$iv = itemStackArray[i];
                boolean bl2 = false;
                if (!(it == null)) continue;
                bl = false;
                break block1;
            }
            bl = true;
        }
        return bl;
    }

    @Override
    @Nullable
    public String getTag() {
        return (Boolean)this.silenceValue.get() != false ? "Silent" : new Function0<String>(this){
            final /* synthetic */ ChestStealer this$0;

            @Nullable
            public final String invoke() {
                return (Boolean)this.this$0.getArrayListTag().get() != false ? "" + ((Number)ChestStealer.access$getMinDelayValue$p(this.this$0).get()).intValue() + ' ' + ((Number)ChestStealer.access$getMaxDelayValue$p(this.this$0).get()).intValue() : null;
            }
            {
                this.this$0 = chestStealer;
                super(0);
            }
        }.toString();
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }

    public static final /* synthetic */ IntegerValue access$getMinDelayValue$p(ChestStealer $this) {
        return $this.minDelayValue;
    }

    public static final /* synthetic */ IntegerValue access$getMaxDelayValue$p(ChestStealer $this) {
        return $this.maxDelayValue;
    }

    public static final /* synthetic */ long access$getNextDelay$p(ChestStealer $this) {
        return $this.nextDelay;
    }

    public static final /* synthetic */ void access$setNextDelay$p(ChestStealer $this, long l) {
        $this.nextDelay = l;
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

