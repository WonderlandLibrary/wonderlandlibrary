/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.inventory.GuiInventory
 *  net.minecraft.enchantment.Enchantment
 *  net.minecraft.entity.ai.attributes.AttributeModifier
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.inventory.Slot
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemAppleGold
 *  net.minecraft.item.ItemArmor
 *  net.minecraft.item.ItemAxe
 *  net.minecraft.item.ItemBed
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemBoat
 *  net.minecraft.item.ItemBow
 *  net.minecraft.item.ItemBucket
 *  net.minecraft.item.ItemEnchantedBook
 *  net.minecraft.item.ItemEnderPearl
 *  net.minecraft.item.ItemFood
 *  net.minecraft.item.ItemMinecart
 *  net.minecraft.item.ItemPickaxe
 *  net.minecraft.item.ItemPotion
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.item.ItemTool
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C0DPacketCloseWindow
 *  net.minecraft.network.play.client.C16PacketClientStatus
 *  net.minecraft.network.play.client.C16PacketClientStatus$EnumState
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.player;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoArmor;
import net.ccbluex.liquidbounce.injection.implementations.IItemStack;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.InventoryUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.item.ArmorPiece;
import net.ccbluex.liquidbounce.utils.item.ItemUtils;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBed;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBoat;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemMinecart;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="InvCleaner", description="Automatically throws away useless items.", category=ModuleCategory.PLAYER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J!\u0010#\u001a\u0004\u0018\u00010$2\u0006\u0010%\u001a\u00020$2\b\u0010&\u001a\u0004\u0018\u00010'H\u0002\u00a2\u0006\u0002\u0010(J\u0016\u0010)\u001a\u00020*2\u0006\u0010+\u001a\u00020'2\u0006\u0010,\u001a\u00020$J(\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020$\u0012\u0004\u0012\u00020'0-2\b\b\u0002\u0010.\u001a\u00020$2\b\b\u0002\u0010/\u001a\u00020$H\u0002J\u0010\u00100\u001a\u0002012\u0006\u00102\u001a\u000203H\u0007J\b\u00104\u001a\u000201H\u0002J\u0010\u00105\u001a\u00020\u000e2\u0006\u0010%\u001a\u00020$H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\rX\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u000fR\u000e\u0010\u0010\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010 \u001a\u0004\u0018\u00010\u000e8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b!\u0010\"\u00a8\u00066"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/player/InventoryCleaner;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "delay", "", "displayTag", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "hotbarValue", "ignoreVehiclesValue", "invOpenValue", "itemDelayValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "items", "", "", "[Ljava/lang/String;", "maxDelayValue", "minDelayValue", "noMoveValue", "randomSlotValue", "simulateInventory", "sortSlot1Value", "Lnet/ccbluex/liquidbounce/value/ListValue;", "sortSlot2Value", "sortSlot3Value", "sortSlot4Value", "sortSlot5Value", "sortSlot6Value", "sortSlot7Value", "sortSlot8Value", "sortSlot9Value", "sortValue", "tag", "getTag", "()Ljava/lang/String;", "findBetterItem", "", "targetSlot", "slotStack", "Lnet/minecraft/item/ItemStack;", "(ILnet/minecraft/item/ItemStack;)Ljava/lang/Integer;", "isUseful", "", "itemStack", "slot", "", "start", "end", "onUpdate", "", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "sortHotbar", "type", "KyinoClient"})
public final class InventoryCleaner
extends Module {
    private final IntegerValue maxDelayValue = new IntegerValue(this, "MaxDelay", 600, 0, 1000){
        final /* synthetic */ InventoryCleaner this$0;

        protected void onChanged(int oldValue, int newValue) {
            int minCPS2 = ((Number)InventoryCleaner.access$getMinDelayValue$p(this.this$0).get()).intValue();
            if (minCPS2 > newValue) {
                this.set(minCPS2);
            }
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4);
        }
    };
    private final IntegerValue minDelayValue = new IntegerValue(this, "MinDelay", 400, 0, 1000){
        final /* synthetic */ InventoryCleaner this$0;

        protected void onChanged(int oldValue, int newValue) {
            int maxDelay = ((Number)InventoryCleaner.access$getMaxDelayValue$p(this.this$0).get()).intValue();
            if (maxDelay < newValue) {
                this.set(maxDelay);
            }
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4);
        }
    };
    private final BoolValue invOpenValue = new BoolValue("InvOpen", false);
    private final BoolValue simulateInventory = new BoolValue("SimulateInventory", true);
    private final BoolValue noMoveValue = new BoolValue("NoMove", false);
    private final BoolValue ignoreVehiclesValue = new BoolValue("IgnoreVehicles", false);
    private final BoolValue hotbarValue = new BoolValue("Hotbar", true);
    private final BoolValue randomSlotValue = new BoolValue("RandomSlot", false);
    private final BoolValue sortValue = new BoolValue("Sort", true);
    private final IntegerValue itemDelayValue = new IntegerValue("Item-Delay", 0, 0, 5000);
    private final String[] items = new String[]{"None", "Ignore", "Sword", "Bow", "Pickaxe", "Axe", "Food", "Block", "Water", "Gapple", "Pearl"};
    private final ListValue sortSlot1Value = new ListValue("SortSlot-1", this.items, "Sword");
    private final ListValue sortSlot2Value = new ListValue("SortSlot-2", this.items, "Gapple");
    private final ListValue sortSlot3Value = new ListValue("SortSlot-3", this.items, "Bow");
    private final ListValue sortSlot4Value = new ListValue("SortSlot-4", this.items, "Food");
    private final ListValue sortSlot5Value = new ListValue("SortSlot-5", this.items, "Pearl");
    private final ListValue sortSlot6Value = new ListValue("SortSlot-6", this.items, "Pickaxe");
    private final ListValue sortSlot7Value = new ListValue("SortSlot-7", this.items, "Axe");
    private final ListValue sortSlot8Value = new ListValue("SortSlot-8", this.items, "Block");
    private final ListValue sortSlot9Value = new ListValue("SortSlot-9", this.items, "Block");
    private final BoolValue displayTag = new BoolValue("ArrayList-Tag", true);
    private long delay;

    @Override
    @Nullable
    public String getTag() {
        return (Boolean)this.displayTag.get() != false ? "" + ((Number)this.maxDelayValue.get()).intValue() + ' ' + ((Number)this.minDelayValue.get()).intValue() : null;
    }

    /*
     * WARNING - void declaration
     */
    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (!InventoryUtils.CLICK_TIMER.hasTimePassed(this.delay) || !(InventoryCleaner.access$getMc$p$s1046033730().field_71462_r instanceof GuiInventory) && (Boolean)this.invOpenValue.get() != false || (Boolean)this.noMoveValue.get() != false && MovementUtils.isMoving() || InventoryCleaner.access$getMc$p$s1046033730().field_71439_g.field_71070_bA != null && InventoryCleaner.access$getMc$p$s1046033730().field_71439_g.field_71070_bA.field_75152_c != 0) {
            return;
        }
        if (((Boolean)this.sortValue.get()).booleanValue()) {
            this.sortHotbar();
        }
        while (InventoryUtils.CLICK_TIMER.hasTimePassed(this.delay)) {
            boolean openInventory;
            void $this$filterTo$iv$iv;
            Object $this$filter$iv = this.items(9, (Boolean)this.hotbarValue.get() != false ? 45 : 36);
            boolean $i$f$filter = false;
            Map<Integer, ItemStack> map = $this$filter$iv;
            Map destination$iv$iv = new LinkedHashMap();
            boolean $i$f$filterTo = false;
            void var8_9 = $this$filterTo$iv$iv;
            boolean bl = false;
            Iterator iterator2 = var8_9.entrySet().iterator();
            while (iterator2.hasNext()) {
                Map.Entry element$iv$iv;
                Map.Entry it = element$iv$iv = iterator2.next();
                boolean bl2 = false;
                if (!(!this.isUseful((ItemStack)it.getValue(), ((Number)it.getKey()).intValue()))) continue;
                destination$iv$iv.put(element$iv$iv.getKey(), element$iv$iv.getValue());
            }
            List garbageItems = CollectionsKt.toMutableList(destination$iv$iv.keySet());
            if (((Boolean)this.randomSlotValue.get()).booleanValue()) {
                $this$filter$iv = garbageItems;
                $i$f$filter = false;
                Collections.shuffle($this$filter$iv);
            }
            Integer n = (Integer)CollectionsKt.firstOrNull(garbageItems);
            if (n == null) {
                break;
            }
            int garbageItem = n;
            boolean bl3 = openInventory = !(InventoryCleaner.access$getMc$p$s1046033730().field_71462_r instanceof GuiInventory) && (Boolean)this.simulateInventory.get() != false;
            if (openInventory) {
                Minecraft minecraft = InventoryCleaner.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
            }
            InventoryCleaner.access$getMc$p$s1046033730().field_71442_b.func_78753_a(InventoryCleaner.access$getMc$p$s1046033730().field_71439_g.field_71070_bA.field_75152_c, garbageItem, 4, 4, (EntityPlayer)InventoryCleaner.access$getMc$p$s1046033730().field_71439_g);
            if (openInventory) {
                Minecraft minecraft = InventoryCleaner.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)new C0DPacketCloseWindow());
            }
            this.delay = TimeUtils.randomDelay(((Number)this.minDelayValue.get()).intValue(), ((Number)this.maxDelayValue.get()).intValue());
        }
    }

    /*
     * WARNING - void declaration
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public final boolean isUseful(@NotNull ItemStack itemStack, int slot) {
        Intrinsics.checkParameterIsNotNull(itemStack, "itemStack");
        try {
            Map.Entry element$iv;
            ItemStack stack;
            Item item = itemStack.func_77973_b();
            if (item instanceof ItemSword || item instanceof ItemTool) {
                boolean bl;
                int n;
                if (slot >= 36) {
                    Integer n2 = this.findBetterItem(slot - 36, InventoryCleaner.access$getMc$p$s1046033730().field_71439_g.field_71071_by.func_70301_a(slot - 36));
                    n = slot - 36;
                    if (n2 != null && n2 == n) {
                        return true;
                    }
                }
                n = 0;
                int n3 = 8;
                while (n <= n3) {
                    void i;
                    if ((StringsKt.equals(this.type((int)i), "sword", true) && item instanceof ItemSword || StringsKt.equals(this.type((int)i), "pickaxe", true) && item instanceof ItemPickaxe || StringsKt.equals(this.type((int)i), "axe", true) && item instanceof ItemAxe) && this.findBetterItem((int)i, InventoryCleaner.access$getMc$p$s1046033730().field_71439_g.field_71071_by.func_70301_a((int)i)) == null) {
                        return true;
                    }
                    ++i;
                }
                Collection collection = itemStack.func_111283_C().get((Object)"generic.attackDamage");
                Intrinsics.checkExpressionValueIsNotNull(collection, "itemStack.attributeModif\u2026s[\"generic.attackDamage\"]");
                AttributeModifier attributeModifier = (AttributeModifier)CollectionsKt.firstOrNull(collection);
                double damage = (attributeModifier != null ? attributeModifier.func_111164_d() : 0.0) + 1.25 * (double)ItemUtils.getEnchantment(itemStack, Enchantment.field_180314_l);
                Map<Integer, ItemStack> $this$none$iv = this.items(0, 45);
                boolean $i$f$none = false;
                if ($this$none$iv.isEmpty()) {
                    return true;
                }
                Map<Integer, ItemStack> map = $this$none$iv;
                boolean bl2 = false;
                Iterator<Map.Entry<Integer, ItemStack>> iterator2 = map.entrySet().iterator();
                do {
                    Map.Entry<Integer, ItemStack> element$iv2;
                    if (!iterator2.hasNext()) return true;
                    Map.Entry<Integer, ItemStack> $dstr$_u24__u24$stack = element$iv2 = iterator2.next();
                    boolean bl3 = false;
                    Map.Entry<Integer, ItemStack> entry = $dstr$_u24__u24$stack;
                    boolean bl4 = false;
                    stack = entry.getValue();
                    if (Intrinsics.areEqual(stack, itemStack) ^ true && Intrinsics.areEqual(stack.getClass(), itemStack.getClass())) {
                        Collection collection2 = stack.func_111283_C().get((Object)"generic.attackDamage");
                        Intrinsics.checkExpressionValueIsNotNull(collection2, "stack.attributeModifiers[\"generic.attackDamage\"]");
                        AttributeModifier attributeModifier2 = (AttributeModifier)CollectionsKt.firstOrNull(collection2);
                        double d = attributeModifier2 != null ? attributeModifier2.func_111164_d() : 0.0;
                        if (damage <= d + 1.25 * (double)ItemUtils.getEnchantment(stack, Enchantment.field_180314_l)) {
                            return false;
                        }
                    }
                    bl = false;
                } while (!bl);
                return false;
            }
            if (item instanceof ItemBow) {
                boolean bl;
                int currPower = ItemUtils.getEnchantment(itemStack, Enchantment.field_77345_t);
                Map $this$none$iv = InventoryCleaner.items$default(this, 0, 0, 3, null);
                boolean $i$f$none = false;
                if ($this$none$iv.isEmpty()) {
                    return true;
                }
                Map map = $this$none$iv;
                boolean bl5 = false;
                Iterator iterator3 = map.entrySet().iterator();
                do {
                    if (!iterator3.hasNext()) return true;
                    Map.Entry $dstr$_u24__u24$stack = element$iv = iterator3.next();
                    boolean bl6 = false;
                    Map.Entry bl3 = $dstr$_u24__u24$stack;
                    boolean bl7 = false;
                    ItemStack stack2 = (ItemStack)bl3.getValue();
                    if (Intrinsics.areEqual(itemStack, stack2) ^ true && stack2.func_77973_b() instanceof ItemBow && currPower <= ItemUtils.getEnchantment(stack2, Enchantment.field_77345_t)) {
                        return false;
                    }
                    bl = false;
                } while (!bl);
                return false;
            }
            if (item instanceof ItemArmor) {
                boolean bl;
                ArmorPiece currArmor = new ArmorPiece(itemStack, slot);
                Map $this$none$iv = InventoryCleaner.items$default(this, 0, 0, 3, null);
                boolean $i$f$none = false;
                if ($this$none$iv.isEmpty()) {
                    return true;
                }
                Map map = $this$none$iv;
                boolean bl8 = false;
                Iterator iterator4 = map.entrySet().iterator();
                do {
                    if (!iterator4.hasNext()) return true;
                    Map.Entry $dstr$slot$stack = element$iv = iterator4.next();
                    boolean bl9 = false;
                    Map.Entry bl3 = $dstr$slot$stack;
                    boolean bl10 = false;
                    int slot2 = ((Number)bl3.getKey()).intValue();
                    bl3 = $dstr$slot$stack;
                    bl10 = false;
                    stack = (ItemStack)bl3.getValue();
                    if (Intrinsics.areEqual(stack, itemStack) ^ true && stack.func_77973_b() instanceof ItemArmor) {
                        ArmorPiece armor = new ArmorPiece(stack, slot2);
                        if (armor.getArmorType() != currArmor.getArmorType()) {
                            bl = false;
                            continue;
                        }
                        if (AutoArmor.ARMOR_COMPARATOR.compare(currArmor, armor) <= 0) {
                            return false;
                        }
                        bl = false;
                        continue;
                    }
                    bl = false;
                } while (!bl);
                return false;
            }
            if (Intrinsics.areEqual(itemStack.func_77977_a(), "item.compass")) {
                boolean bl;
                Map<Integer, ItemStack> $this$none$iv = this.items(0, 45);
                boolean $i$f$none = false;
                if ($this$none$iv.isEmpty()) {
                    return true;
                }
                Map<Integer, ItemStack> map = $this$none$iv;
                boolean bl11 = false;
                Iterator<Map.Entry<Integer, ItemStack>> iterator5 = map.entrySet().iterator();
                do {
                    Map.Entry<Integer, ItemStack> element$iv3;
                    if (!iterator5.hasNext()) return true;
                    Map.Entry<Integer, ItemStack> $dstr$_u24__u24$stack = element$iv3 = iterator5.next();
                    boolean bl12 = false;
                    Map.Entry<Integer, ItemStack> entry = $dstr$_u24__u24$stack;
                    boolean bl13 = false;
                    ItemStack stack3 = entry.getValue();
                    if (Intrinsics.areEqual(itemStack, stack3) ^ true && Intrinsics.areEqual(stack3.func_77977_a(), "item.compass")) {
                        return false;
                    }
                    bl = false;
                } while (!bl);
                return false;
            }
            if (item instanceof ItemFood) return true;
            if (Intrinsics.areEqual(itemStack.func_77977_a(), "item.arrow")) return true;
            if (item instanceof ItemBlock) {
                String string = itemStack.func_77977_a();
                Intrinsics.checkExpressionValueIsNotNull(string, "itemStack.unlocalizedName");
                if (!StringsKt.contains$default((CharSequence)string, "flower", false, 2, null)) return true;
            }
            if (item instanceof ItemBed) return true;
            if (Intrinsics.areEqual(itemStack.func_77977_a(), "item.diamond")) return true;
            if (Intrinsics.areEqual(itemStack.func_77977_a(), "item.ingotIron")) return true;
            if (item instanceof ItemPotion) return true;
            if (item instanceof ItemEnderPearl) return true;
            if (item instanceof ItemEnchantedBook) return true;
            if (item instanceof ItemBucket) return true;
            if (Intrinsics.areEqual(itemStack.func_77977_a(), "item.stick")) return true;
            if ((Boolean)this.ignoreVehiclesValue.get() == false) return false;
            if (item instanceof ItemBoat) return true;
            if (!(item instanceof ItemMinecart)) return false;
            return true;
        }
        catch (Exception ex) {
            ClientUtils.getLogger().error("(InventoryCleaner) Failed to check item: " + itemStack.func_77977_a() + '.', (Throwable)ex);
            return true;
        }
    }

    /*
     * WARNING - void declaration
     */
    private final void sortHotbar() {
        int n = 0;
        int n2 = 8;
        while (n <= n2) {
            void index;
            Integer n3 = this.findBetterItem((int)index, InventoryCleaner.access$getMc$p$s1046033730().field_71439_g.field_71071_by.func_70301_a((int)index));
            if (n3 == null) {
            } else {
                int bestItem = n3;
                if (bestItem != index) {
                    boolean openInventory;
                    boolean bl = openInventory = !(InventoryCleaner.access$getMc$p$s1046033730().field_71462_r instanceof GuiInventory) && (Boolean)this.simulateInventory.get() != false;
                    if (openInventory) {
                        Minecraft minecraft = InventoryCleaner.access$getMc$p$s1046033730();
                        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                        minecraft.func_147114_u().func_147297_a((Packet)new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
                    }
                    InventoryCleaner.access$getMc$p$s1046033730().field_71442_b.func_78753_a(0, bestItem < 9 ? bestItem + 36 : bestItem, (int)index, 2, (EntityPlayer)InventoryCleaner.access$getMc$p$s1046033730().field_71439_g);
                    if (openInventory) {
                        Minecraft minecraft = InventoryCleaner.access$getMc$p$s1046033730();
                        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                        minecraft.func_147114_u().func_147297_a((Packet)new C0DPacketCloseWindow());
                    }
                    this.delay = TimeUtils.randomDelay(((Number)this.minDelayValue.get()).intValue(), ((Number)this.maxDelayValue.get()).intValue());
                    break;
                }
            }
            ++index;
        }
    }

    /*
     * WARNING - void declaration
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private final Integer findBetterItem(int targetSlot, ItemStack slotStack) {
        String type;
        String string = type = this.type(targetSlot);
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
        switch (string3) {
            case "sword": 
            case "pickaxe": 
            case "axe": {
                Class<ItemSword> clazz;
                if (StringsKt.equals(type, "Sword", true)) {
                    clazz = ItemSword.class;
                } else if (StringsKt.equals(type, "Pickaxe", true)) {
                    clazz = ItemPickaxe.class;
                } else {
                    if (!StringsKt.equals(type, "Axe", true)) return null;
                    clazz = ItemAxe.class;
                }
                Class<ItemSword> currentType = clazz;
                ItemStack itemStack = slotStack;
                int bestWeapon = Intrinsics.areEqual(itemStack != null && (itemStack = itemStack.func_77973_b()) != null ? itemStack.getClass() : null, currentType) ? targetSlot : -1;
                Intrinsics.checkExpressionValueIsNotNull(InventoryCleaner.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70462_a, "mc.thePlayer.inventory.mainInventory");
                ItemStack[] $this$forEachIndexed$iv = InventoryCleaner.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70462_a;
                boolean $i$f$forEachIndexed = false;
                int index$iv = 0;
                for (ItemStack item$iv : $this$forEachIndexed$iv) {
                    ItemStack bestStack;
                    void itemStack2;
                    int n = index$iv++;
                    ItemStack itemStack3 = item$iv;
                    int index = n;
                    boolean bl2 = false;
                    Item item = itemStack2;
                    if (!Intrinsics.areEqual(item != null && (item = item.func_77973_b()) != null ? item.getClass() : null, currentType) || StringsKt.equals(this.type(index), type, true)) continue;
                    if (bestWeapon == -1) {
                        bestWeapon = index;
                        continue;
                    }
                    Collection collection = itemStack2.func_111283_C().get((Object)"generic.attackDamage");
                    Intrinsics.checkExpressionValueIsNotNull(collection, "itemStack.attributeModif\u2026s[\"generic.attackDamage\"]");
                    AttributeModifier attributeModifier = (AttributeModifier)CollectionsKt.firstOrNull(collection);
                    double currDamage = (attributeModifier != null ? attributeModifier.func_111164_d() : 0.0) + 1.25 * (double)ItemUtils.getEnchantment((ItemStack)itemStack2, Enchantment.field_180314_l);
                    if (InventoryCleaner.access$getMc$p$s1046033730().field_71439_g.field_71071_by.func_70301_a(bestWeapon) == null) {
                        continue;
                    }
                    Collection collection2 = bestStack.func_111283_C().get((Object)"generic.attackDamage");
                    Intrinsics.checkExpressionValueIsNotNull(collection2, "bestStack.attributeModif\u2026s[\"generic.attackDamage\"]");
                    AttributeModifier attributeModifier2 = (AttributeModifier)CollectionsKt.firstOrNull(collection2);
                    double bestDamage = (attributeModifier2 != null ? attributeModifier2.func_111164_d() : 0.0) + 1.25 * (double)ItemUtils.getEnchantment(bestStack, Enchantment.field_180314_l);
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
                ItemStack itemStack = slotStack;
                int bestBow = (itemStack != null ? itemStack.func_77973_b() : null) instanceof ItemBow ? targetSlot : -1;
                int bestPower = bestBow != -1 ? ItemUtils.getEnchantment(slotStack, Enchantment.field_77345_t) : 0;
                Intrinsics.checkExpressionValueIsNotNull(InventoryCleaner.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70462_a, "mc.thePlayer.inventory.mainInventory");
                ItemStack[] $this$forEachIndexed$iv = InventoryCleaner.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70462_a;
                boolean $i$f$forEachIndexed = false;
                int index$iv = 0;
                for (ItemStack item$iv : $this$forEachIndexed$iv) {
                    int n = index$iv++;
                    ItemStack itemStack2 = item$iv;
                    int index = n;
                    boolean bl3 = false;
                    ItemStack itemStack4 = itemStack2;
                    if (!((itemStack4 != null ? itemStack4.func_77973_b() : null) instanceof ItemBow) || StringsKt.equals(this.type(index), type, true)) continue;
                    if (bestBow == -1) {
                        bestBow = index;
                        continue;
                    }
                    int power = ItemUtils.getEnchantment(itemStack2, Enchantment.field_77345_t);
                    if (ItemUtils.getEnchantment(itemStack2, Enchantment.field_77345_t) <= bestPower) continue;
                    bestBow = index;
                    bestPower = power;
                }
                if (bestBow == -1) return null;
                Integer n = bestBow;
                return n;
            }
            case "food": {
                Intrinsics.checkExpressionValueIsNotNull(InventoryCleaner.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70462_a, "mc.thePlayer.inventory.mainInventory");
                ItemStack[] $this$forEachIndexed$iv = InventoryCleaner.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70462_a;
                boolean $i$f$forEachIndexed = false;
                int index$iv = 0;
                ItemStack[] itemStackArray = $this$forEachIndexed$iv;
                int n = itemStackArray.length;
                int n2 = 0;
                while (n2 < n) {
                    Item item;
                    void stack;
                    ItemStack item$iv = itemStackArray[n2];
                    int n3 = index$iv++;
                    ItemStack itemStack = item$iv;
                    int index = n3;
                    boolean bl4 = false;
                    void v16 = stack;
                    Object object = item = v16 != null ? v16.func_77973_b() : null;
                    if (item instanceof ItemFood && !(item instanceof ItemAppleGold) && !StringsKt.equals(this.type(index), "Food", true)) {
                        if (slotStack != null) {
                            if (slotStack.func_77973_b() instanceof ItemFood) return null;
                        }
                        boolean bl5 = true;
                        boolean replaceCurr = bl5;
                        if (!replaceCurr) return null;
                        Integer n4 = index;
                        return n4;
                    }
                    ++n2;
                }
                return null;
            }
            case "block": {
                Intrinsics.checkExpressionValueIsNotNull(InventoryCleaner.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70462_a, "mc.thePlayer.inventory.mainInventory");
                ItemStack[] $this$forEachIndexed$iv = InventoryCleaner.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70462_a;
                boolean $i$f$forEachIndexed = false;
                int index$iv = 0;
                ItemStack[] itemStackArray = $this$forEachIndexed$iv;
                int n = itemStackArray.length;
                int n5 = 0;
                while (n5 < n) {
                    Item item;
                    ItemStack item$iv = itemStackArray[n5];
                    int n6 = index$iv++;
                    ItemStack stack = item$iv;
                    int index = n6;
                    boolean bl6 = false;
                    ItemStack itemStack = stack;
                    Object object = item = itemStack != null ? itemStack.func_77973_b() : null;
                    if (item instanceof ItemBlock && !InventoryUtils.BLOCK_BLACKLIST.contains(((ItemBlock)item).field_150939_a) && !StringsKt.equals(this.type(index), "Block", true)) {
                        if (slotStack != null) {
                            if (slotStack.func_77973_b() instanceof ItemBlock) return null;
                        }
                        boolean bl7 = true;
                        boolean replaceCurr = bl7;
                        if (!replaceCurr) return null;
                        Integer n7 = index;
                        return n7;
                    }
                    ++n5;
                }
                return null;
            }
            case "water": {
                Intrinsics.checkExpressionValueIsNotNull(InventoryCleaner.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70462_a, "mc.thePlayer.inventory.mainInventory");
                ItemStack[] $this$forEachIndexed$iv = InventoryCleaner.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70462_a;
                boolean $i$f$forEachIndexed = false;
                int index$iv = 0;
                ItemStack[] itemStackArray = $this$forEachIndexed$iv;
                int n = itemStackArray.length;
                int n8 = 0;
                while (n8 < n) {
                    Item item;
                    ItemStack item$iv = itemStackArray[n8];
                    int n9 = index$iv++;
                    ItemStack stack = item$iv;
                    int index = n9;
                    boolean bl8 = false;
                    ItemStack itemStack = stack;
                    Object object = item = itemStack != null ? itemStack.func_77973_b() : null;
                    if (item instanceof ItemBucket && Intrinsics.areEqual(((ItemBucket)item).field_77876_a, Blocks.field_150358_i) && !StringsKt.equals(this.type(index), "Water", true)) {
                        if (slotStack != null && slotStack.func_77973_b() instanceof ItemBucket) {
                            Item item2 = slotStack.func_77973_b();
                            if (item2 == null) {
                                throw new TypeCastException("null cannot be cast to non-null type net.minecraft.item.ItemBucket");
                            }
                            if (!(Intrinsics.areEqual(((ItemBucket)item2).field_77876_a, Blocks.field_150358_i) ^ true)) return null;
                        }
                        boolean bl9 = true;
                        boolean replaceCurr = bl9;
                        if (!replaceCurr) return null;
                        Integer n10 = index;
                        return n10;
                    }
                    ++n8;
                }
                return null;
            }
            case "gapple": {
                Intrinsics.checkExpressionValueIsNotNull(InventoryCleaner.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70462_a, "mc.thePlayer.inventory.mainInventory");
                ItemStack[] $this$forEachIndexed$iv = InventoryCleaner.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70462_a;
                boolean $i$f$forEachIndexed = false;
                int index$iv = 0;
                ItemStack[] itemStackArray = $this$forEachIndexed$iv;
                int n = itemStackArray.length;
                int n11 = 0;
                while (n11 < n) {
                    Item item;
                    ItemStack item$iv = itemStackArray[n11];
                    int n12 = index$iv++;
                    ItemStack stack = item$iv;
                    int index = n12;
                    boolean bl10 = false;
                    ItemStack itemStack = stack;
                    Object object = item = itemStack != null ? itemStack.func_77973_b() : null;
                    if (item instanceof ItemAppleGold && !StringsKt.equals(this.type(index), "Gapple", true)) {
                        if (slotStack != null) {
                            if (slotStack.func_77973_b() instanceof ItemAppleGold) return null;
                        }
                        boolean bl11 = true;
                        boolean replaceCurr = bl11;
                        if (!replaceCurr) return null;
                        Integer n13 = index;
                        return n13;
                    }
                    ++n11;
                }
                return null;
            }
            case "pearl": {
                Intrinsics.checkExpressionValueIsNotNull(InventoryCleaner.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70462_a, "mc.thePlayer.inventory.mainInventory");
                ItemStack[] $this$forEachIndexed$iv = InventoryCleaner.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70462_a;
                boolean $i$f$forEachIndexed = false;
                int index$iv = 0;
                ItemStack[] itemStackArray = $this$forEachIndexed$iv;
                int n = itemStackArray.length;
                int n14 = 0;
                while (n14 < n) {
                    Item item;
                    ItemStack item$iv = itemStackArray[n14];
                    int n15 = index$iv++;
                    ItemStack stack = item$iv;
                    int index = n15;
                    boolean bl12 = false;
                    ItemStack itemStack = stack;
                    Object object = item = itemStack != null ? itemStack.func_77973_b() : null;
                    if (item instanceof ItemEnderPearl && !StringsKt.equals(this.type(index), "Pearl", true)) {
                        if (slotStack != null) {
                            if (slotStack.func_77973_b() instanceof ItemEnderPearl) return null;
                        }
                        boolean bl13 = true;
                        boolean replaceCurr = bl13;
                        if (!replaceCurr) return null;
                        Integer n16 = index;
                        return n16;
                    }
                    ++n14;
                }
                return null;
            }
        }
        return null;
    }

    /*
     * WARNING - void declaration
     */
    private final Map<Integer, ItemStack> items(int start, int end) {
        int n = 0;
        Map items = new LinkedHashMap();
        n = end - 1;
        int n2 = start;
        if (n >= n2) {
            while (true) {
                ItemStack itemStack;
                void i;
                Slot slot = InventoryCleaner.access$getMc$p$s1046033730().field_71439_g.field_71069_bz.func_75139_a((int)i);
                Intrinsics.checkExpressionValueIsNotNull(slot, "mc.thePlayer.inventoryContainer.getSlot(i)");
                if (slot.func_75211_c() == null) {
                } else if (itemStack.func_77973_b() == null) {
                } else {
                    void var7_7 = i;
                    if (36 > var7_7 || 44 < var7_7 || !StringsKt.equals(this.type((int)i), "Ignore", true)) {
                        ItemStack itemStack2 = itemStack;
                        if (itemStack2 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.implementations.IItemStack");
                        }
                        if (System.currentTimeMillis() - ((IItemStack)itemStack2).getItemDelay() >= ((Number)this.itemDelayValue.get()).longValue()) {
                            items.put((int)i, itemStack);
                        }
                    }
                }
                if (i == n2) break;
                --i;
            }
        }
        return items;
    }

    static /* synthetic */ Map items$default(InventoryCleaner inventoryCleaner, int n, int n2, int n3, Object object) {
        if ((n3 & 1) != 0) {
            n = 0;
        }
        if ((n3 & 2) != 0) {
            n2 = 45;
        }
        return inventoryCleaner.items(n, n2);
    }

    private final String type(int targetSlot) {
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

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }

    public static final /* synthetic */ IntegerValue access$getMinDelayValue$p(InventoryCleaner $this) {
        return $this.minDelayValue;
    }

    public static final /* synthetic */ IntegerValue access$getMaxDelayValue$p(InventoryCleaner $this) {
        return $this.maxDelayValue;
    }
}

