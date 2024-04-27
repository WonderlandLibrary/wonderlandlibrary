/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.collections.CollectionsKt
 *  kotlin.jvm.functions.Function1
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.player;

import java.util.Iterator;
import java.util.Map;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.api.enums.BlockType;
import net.ccbluex.liquidbounce.api.enums.EnchantmentType;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.entity.ai.attributes.IAttributeModifier;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.player.InventoryCleaner;
import net.ccbluex.liquidbounce.utils.InventoryUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.item.ItemUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="InventoryCleaner", description="Automatically throws away useless items.", category=ModuleCategory.PLAYER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J!\u0010\u001f\u001a\u0004\u0018\u00010 2\u0006\u0010!\u001a\u00020 2\b\u0010\"\u001a\u0004\u0018\u00010#H\u0002\u00a2\u0006\u0002\u0010$J\u0016\u0010%\u001a\u00020&2\u0006\u0010'\u001a\u00020#2\u0006\u0010(\u001a\u00020 J(\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020 \u0012\u0004\u0012\u00020#0)2\b\b\u0002\u0010*\u001a\u00020 2\b\b\u0002\u0010+\u001a\u00020 H\u0002J\u0010\u0010,\u001a\u00020-2\u0006\u0010.\u001a\u00020/H\u0007J\b\u00100\u001a\u00020-H\u0002J\u0010\u00101\u001a\u00020\r2\u0006\u0010!\u001a\u00020 H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u000eR\u000e\u0010\u000f\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u0015X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00062"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/player/InventoryCleaner;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "delay", "", "hotbarValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "ignoreVehiclesValue", "invOpenValue", "itemDelayValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "items", "", "", "[Ljava/lang/String;", "maxDelayValue", "minDelayValue", "noMoveValue", "randomSlotValue", "simulateInventory", "sortSlot1Value", "Lnet/ccbluex/liquidbounce/value/ListValue;", "sortSlot2Value", "sortSlot3Value", "sortSlot4Value", "sortSlot5Value", "sortSlot6Value", "sortSlot7Value", "sortSlot8Value", "sortSlot9Value", "sortValue", "findBetterItem", "", "targetSlot", "slotStack", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItemStack;", "(ILnet/ccbluex/liquidbounce/api/minecraft/item/IItemStack;)Ljava/lang/Integer;", "isUseful", "", "itemStack", "slot", "", "start", "end", "onUpdate", "", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "sortHotbar", "type", "LiKingSense"})
public final class InventoryCleaner
extends Module {
    public final IntegerValue maxDelayValue;
    public final IntegerValue minDelayValue;
    public final BoolValue invOpenValue;
    public final BoolValue simulateInventory;
    public final BoolValue noMoveValue;
    public final BoolValue ignoreVehiclesValue;
    public final BoolValue hotbarValue;
    public final BoolValue randomSlotValue;
    public final BoolValue sortValue;
    public final IntegerValue itemDelayValue;
    public final String[] items;
    public final ListValue sortSlot1Value;
    public final ListValue sortSlot2Value;
    public final ListValue sortSlot3Value;
    public final ListValue sortSlot4Value;
    public final ListValue sortSlot5Value;
    public final ListValue sortSlot6Value;
    public final ListValue sortSlot7Value;
    public final ListValue sortSlot8Value;
    public final ListValue sortSlot9Value;
    public long delay;

    /*
     * Exception decompiling
     */
    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl196 : INVOKEINTERFACE - null : Stack underflow
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Exception decompiling
     */
    public final boolean isUseful(@NotNull IItemStack itemStack, int slot) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl125 : INVOKESPECIAL - null : Stack underflow
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Exception decompiling
     */
    public final void sortHotbar() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * java.lang.IllegalStateException: Invisible function parameters on a non-constructor (or reads of uninitialised local variables).
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.assignSSAIdentifiers(Op02WithProcessedDataAndRefs.java:1631)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.discoverStorageLiveness(Op02WithProcessedDataAndRefs.java:1871)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:461)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * WARNING - void declaration
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public final Integer findBetterItem(int targetSlot, IItemStack slotStack) {
        String string;
        String type = this.type(targetSlot);
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) return null;
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        String string2 = string = type;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull((Object)string3, (String)"(this as java.lang.String).toLowerCase()");
        switch (string3) {
            case "sword": 
            case "pickaxe": 
            case "axe": {
                Function1 function1;
                if (StringsKt.equals((String)type, (String)"Sword", (boolean)true)) {
                    function1 = findBetterItem.currentTypeChecker.1.INSTANCE;
                } else if (StringsKt.equals((String)type, (String)"Pickaxe", (boolean)true)) {
                    function1 = findBetterItem.currentTypeChecker.2.INSTANCE;
                } else {
                    if (!StringsKt.equals((String)type, (String)"Axe", (boolean)true)) return null;
                    function1 = findBetterItem.currentTypeChecker.3.INSTANCE;
                }
                Function1 currentTypeChecker2 = function1;
                IItemStack iItemStack = slotStack;
                int bestWeapon = (Boolean)currentTypeChecker2.invoke((Object)(iItemStack != null ? iItemStack.getItem() : null)) != false ? targetSlot : -1;
                Iterable $this$forEachIndexed$iv = thePlayer.getInventory().getMainInventory();
                for (Object item$iv : $this$forEachIndexed$iv) {
                    IItemStack bestStack;
                    void itemStack;
                    void index$iv;
                    void var13_38;
                    if ((var13_38 = ++index$iv) < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    void var15_45 = var13_38;
                    IItemStack iItemStack2 = (IItemStack)item$iv;
                    void index = var15_45;
                    if (itemStack == null || !((Boolean)currentTypeChecker2.invoke((Object)itemStack.getItem())).booleanValue() || StringsKt.equals((String)this.type((int)index), (String)type, (boolean)true)) continue;
                    if (bestWeapon == -1) {
                        bestWeapon = index;
                        continue;
                    }
                    IAttributeModifier iAttributeModifier = (IAttributeModifier)CollectionsKt.firstOrNull((Iterable)itemStack.getAttributeModifier("generic.attackDamage"));
                    double currDamage = (iAttributeModifier != null ? iAttributeModifier.getAmount() : 0.0) + 1.25 * (double)ItemUtils.getEnchantment((IItemStack)itemStack, MinecraftInstance.classProvider.getEnchantmentEnum(EnchantmentType.SHARPNESS));
                    if (thePlayer.getInventory().getStackInSlot(bestWeapon) == null) {
                        continue;
                    }
                    IAttributeModifier iAttributeModifier2 = (IAttributeModifier)CollectionsKt.firstOrNull((Iterable)bestStack.getAttributeModifier("generic.attackDamage"));
                    double d = iAttributeModifier2 != null ? iAttributeModifier2.getAmount() : 0.0;
                    double bestDamage = d + 1.25 * (double)ItemUtils.getEnchantment(bestStack, MinecraftInstance.classProvider.getEnchantmentEnum(EnchantmentType.SHARPNESS));
                    if (!(bestDamage < currDamage)) continue;
                    bestWeapon = index;
                }
                if (bestWeapon == -1) {
                    if (bestWeapon != targetSlot) return null;
                }
                Integer n = bestWeapon;
                return n;
            }
            case "bow": {
                IItemStack iItemStack = slotStack;
                int bestBow = MinecraftInstance.classProvider.isItemBow(iItemStack != null ? iItemStack.getItem() : null) ? targetSlot : -1;
                int bestPower = bestBow != -1 ? ItemUtils.getEnchantment(slotStack, MinecraftInstance.classProvider.getEnchantmentEnum(EnchantmentType.POWER)) : 0;
                Iterable $this$forEachIndexed$iv = thePlayer.getInventory().getMainInventory();
                for (Object item$iv : $this$forEachIndexed$iv) {
                    void index$iv;
                    void var13_39;
                    if ((var13_39 = ++index$iv) < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    void var15_46 = var13_39;
                    IItemStack itemStack = (IItemStack)item$iv;
                    void index = var15_46;
                    IItemStack iItemStack3 = itemStack;
                    if (!MinecraftInstance.classProvider.isItemBow(iItemStack3 != null ? iItemStack3.getItem() : null) || StringsKt.equals((String)this.type((int)index), (String)type, (boolean)true)) continue;
                    if (bestBow == -1) {
                        bestBow = index;
                        continue;
                    }
                    int power = ItemUtils.getEnchantment(itemStack, MinecraftInstance.classProvider.getEnchantmentEnum(EnchantmentType.POWER));
                    if (ItemUtils.getEnchantment(itemStack, MinecraftInstance.classProvider.getEnchantmentEnum(EnchantmentType.POWER)) <= bestPower) continue;
                    bestBow = index;
                    bestPower = power;
                }
                if (bestBow == -1) return null;
                Integer n = bestBow;
                return n;
            }
            case "food": {
                void index;
                IItem item;
                void stack;
                Iterable $this$forEachIndexed$iv = thePlayer.getInventory().getMainInventory();
                Iterator iterator = $this$forEachIndexed$iv.iterator();
                do {
                    void index$iv;
                    void var11_24;
                    if (!iterator.hasNext()) return null;
                    Object item$iv = iterator.next();
                    if ((var11_24 = ++index$iv) < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    void var13_40 = var11_24;
                    IItemStack iItemStack = (IItemStack)item$iv;
                    index = var13_40;
                } while (stack == null || !MinecraftInstance.classProvider.isItemFood(item = stack.getItem()) || MinecraftInstance.classProvider.isItemAppleGold(item) || StringsKt.equals((String)this.type((int)index), (String)"Food", (boolean)true));
                if (!ItemUtils.isStackEmpty(slotStack)) {
                    if (MinecraftInstance.classProvider.isItemFood(item)) return null;
                }
                int n = 1;
                int replaceCurr = n;
                if (replaceCurr == 0) return null;
                Integer n2 = (int)index;
                return n2;
            }
            case "block": {
                void index;
                IItem item;
                IItemStack stack;
                Iterable $this$forEachIndexed$iv = thePlayer.getInventory().getMainInventory();
                Iterator iterator = $this$forEachIndexed$iv.iterator();
                do {
                    void index$iv;
                    void var11_25;
                    if (!iterator.hasNext()) return null;
                    Object item$iv = iterator.next();
                    if ((var11_25 = ++index$iv) < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    void var13_41 = var11_25;
                    stack = (IItemStack)item$iv;
                    index = var13_41;
                } while (stack == null || !MinecraftInstance.classProvider.isItemBlock(item = stack.getItem()) || InventoryUtils.BLOCK_BLACKLIST.contains(item.asItemBlock().getBlock()) || StringsKt.equals((String)this.type((int)index), (String)"Block", (boolean)true));
                if (!ItemUtils.isStackEmpty(slotStack)) {
                    if (MinecraftInstance.classProvider.isItemBlock(item)) return null;
                }
                int n = 1;
                int replaceCurr = n;
                if (replaceCurr == 0) return null;
                Integer n3 = (int)index;
                return n3;
            }
            case "water": {
                void index;
                IItem item;
                IItemStack stack;
                Iterable $this$forEachIndexed$iv = thePlayer.getInventory().getMainInventory();
                Iterator iterator = $this$forEachIndexed$iv.iterator();
                do {
                    void index$iv;
                    void var11_26;
                    if (!iterator.hasNext()) return null;
                    Object item$iv = iterator.next();
                    if ((var11_26 = ++index$iv) < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    void var13_42 = var11_26;
                    stack = (IItemStack)item$iv;
                    index = var13_42;
                } while (stack == null || !MinecraftInstance.classProvider.isItemBucket(item = stack.getItem()) || !Intrinsics.areEqual((Object)item.asItemBucket().isFull(), (Object)MinecraftInstance.classProvider.getBlockEnum(BlockType.FLOWING_WATER)) || StringsKt.equals((String)this.type((int)index), (String)"Water", (boolean)true));
                if (!ItemUtils.isStackEmpty(slotStack) && MinecraftInstance.classProvider.isItemBucket(item)) {
                    if ((Intrinsics.areEqual((Object)item.asItemBucket().isFull(), (Object)MinecraftInstance.classProvider.getBlockEnum(BlockType.FLOWING_WATER)) ^ 1) == 0) return null;
                }
                int n = 1;
                int replaceCurr = n;
                if (replaceCurr == 0) return null;
                Integer n4 = (int)index;
                return n4;
            }
            case "gapple": {
                void index;
                IItem item;
                IItemStack stack;
                Iterable $this$forEachIndexed$iv = thePlayer.getInventory().getMainInventory();
                Iterator iterator = $this$forEachIndexed$iv.iterator();
                do {
                    void index$iv;
                    void var11_27;
                    if (!iterator.hasNext()) return null;
                    Object item$iv = iterator.next();
                    if ((var11_27 = ++index$iv) < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    void var13_43 = var11_27;
                    stack = (IItemStack)item$iv;
                    index = var13_43;
                } while (stack == null || !MinecraftInstance.classProvider.isItemAppleGold(item = stack.getItem()) || StringsKt.equals((String)this.type((int)index), (String)"Gapple", (boolean)true));
                if (!ItemUtils.isStackEmpty(slotStack)) {
                    IItemStack iItemStack = slotStack;
                    if (MinecraftInstance.classProvider.isItemAppleGold(iItemStack != null ? iItemStack.getItem() : null)) return null;
                }
                int n = 1;
                int replaceCurr = n;
                if (replaceCurr == 0) return null;
                Integer n5 = (int)index;
                return n5;
            }
            case "pearl": {
                void index;
                IItem item;
                IItemStack stack;
                Iterable $this$forEachIndexed$iv = thePlayer.getInventory().getMainInventory();
                Iterator iterator = $this$forEachIndexed$iv.iterator();
                do {
                    void index$iv;
                    void var11_28;
                    if (!iterator.hasNext()) return null;
                    Object item$iv = iterator.next();
                    if ((var11_28 = ++index$iv) < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    void var13_44 = var11_28;
                    stack = (IItemStack)item$iv;
                    index = var13_44;
                } while (stack == null || !MinecraftInstance.classProvider.isItemEnderPearl(item = stack.getItem()) || StringsKt.equals((String)this.type((int)index), (String)"Pearl", (boolean)true));
                if (!ItemUtils.isStackEmpty(slotStack)) {
                    IItemStack iItemStack = slotStack;
                    if (MinecraftInstance.classProvider.isItemEnderPearl(iItemStack != null ? iItemStack.getItem() : null)) return null;
                }
                int n = 1;
                int replaceCurr = n;
                if (replaceCurr == 0) return null;
                Integer n6 = (int)index;
                return n6;
            }
        }
        return null;
    }

    /*
     * Exception decompiling
     */
    public final Map<Integer, IItemStack> items(int start, int end) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl38 : IADD - null : Stack underflow
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public static /* synthetic */ Map items$default(InventoryCleaner inventoryCleaner, int n, int n2, int n3, Object object) {
        if ((n3 & 1) != 0) {
            // empty if block
        }
        if ((n3 & 2) != 0) {
            // empty if block
        }
        return inventoryCleaner.items(n, n2);
    }

    public final String type(int targetSlot) {
        String string;
        switch (targetSlot) {
            case 0: {
                string = (String)this.sortSlot1Value.get();
                break;
            }
            case 1: {
                string = (String)this.sortSlot2Value.get();
                break;
            }
            case 2: {
                string = (String)this.sortSlot3Value.get();
                break;
            }
            case 3: {
                string = (String)this.sortSlot4Value.get();
                break;
            }
            case 4: {
                string = (String)this.sortSlot5Value.get();
                break;
            }
            case 5: {
                string = (String)this.sortSlot6Value.get();
                break;
            }
            case 6: {
                string = (String)this.sortSlot7Value.get();
                break;
            }
            case 7: {
                string = (String)this.sortSlot8Value.get();
                break;
            }
            case 8: {
                string = (String)this.sortSlot9Value.get();
                break;
            }
            default: {
                string = "";
            }
        }
        return string;
    }

    /*
     * Exception decompiling
     */
    public InventoryCleaner() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl10 : CHECKCAST - null : Stack underflow
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public static final /* synthetic */ IntegerValue access$getMinDelayValue$p(InventoryCleaner $this) {
        return $this.minDelayValue;
    }

    public static final /* synthetic */ IntegerValue access$getMaxDelayValue$p(InventoryCleaner $this) {
        return $this.maxDelayValue;
    }
}

