package net.ccbluex.liquidbounce.features.module.modules.player;

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
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.IClassProvider;
import net.ccbluex.liquidbounce.api.enums.BlockType;
import net.ccbluex.liquidbounce.api.enums.EnchantmentType;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IPlayerControllerMP;
import net.ccbluex.liquidbounce.api.minecraft.client.network.IINetHandlerPlayClient;
import net.ccbluex.liquidbounce.api.minecraft.entity.ai.attributes.IAttributeModifier;
import net.ccbluex.liquidbounce.api.minecraft.inventory.IContainer;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketEntityAction;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.AutoArmor;
import net.ccbluex.liquidbounce.features.module.modules.player.InventoryCleaner;
import net.ccbluex.liquidbounce.injection.backend.WrapperImpl;
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
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="InvCleaner", description="Automatically throws away useless items.", category=ModuleCategory.PLAYER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000b\n\n\n\b\n\t\n\u0000\n\n\b\n\n\u0000\n\n\n\b\n\n\b\n\n\b\n\b\n\n\b\n\n\b\n$\n\b\n\n\u0000\n\n\b\b\u000020B¢J!0 2!0 2\b\"0#H¢$J%0&2'0#2(0 J(0 0#0)2\b\b*0 2\b\b+0 HJ,0-2.0/HJ\b00-HJ10\r2!0 HR0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R\b0X¢\n\u0000R\t0\nX¢\n\u0000R\b0\r0\fX¢\nR0\nX¢\n\u0000R0\nX¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000¨2"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/player/InventoryCleaner;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "delay", "", "hotbarValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "ignoreVehiclesValue", "invOpenValue", "itemDelayValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "items", "", "", "[Ljava/lang/String;", "maxDelayValue", "minDelayValue", "noMoveValue", "randomSlotValue", "simulateInventory", "sortSlot1Value", "Lnet/ccbluex/liquidbounce/value/ListValue;", "sortSlot2Value", "sortSlot3Value", "sortSlot4Value", "sortSlot5Value", "sortSlot6Value", "sortSlot7Value", "sortSlot8Value", "sortSlot9Value", "sortValue", "findBetterItem", "", "targetSlot", "slotStack", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItemStack;", "(ILnet/ccbluex/liquidbounce/api/minecraft/item/IItemStack;)Ljava/lang/Integer;", "isUseful", "", "itemStack", "slot", "", "start", "end", "onUpdate", "", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "sortHotbar", "type", "Pride"})
public final class InventoryCleaner
extends Module {
    private final IntegerValue maxDelayValue = new IntegerValue(this, "MaxDelay", 600, 0, 1000){
        final InventoryCleaner this$0;

        protected void onChanged(int oldValue, int newValue) {
            int minCPS = ((Number)InventoryCleaner.access$getMinDelayValue$p(this.this$0).get()).intValue();
            if (minCPS > newValue) {
                this.set(minCPS);
            }
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4);
        }
    };
    private final IntegerValue minDelayValue = new IntegerValue(this, "MinDelay", 400, 0, 1000){
        final InventoryCleaner this$0;

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
    private final IntegerValue itemDelayValue = new IntegerValue("ItemDelay", 0, 0, 5000);
    private final String[] items = new String[]{"None", "Ignore", "Sword", "Bow", "Pickaxe", "Axe", "Food", "Block", "Water", "Gapple", "Pearl"};
    private final ListValue sortSlot1Value = new ListValue("SortSlot-1", this.items, "Sword");
    private final ListValue sortSlot2Value = new ListValue("SortSlot-2", this.items, "Bow");
    private final ListValue sortSlot3Value = new ListValue("SortSlot-3", this.items, "Pickaxe");
    private final ListValue sortSlot4Value = new ListValue("SortSlot-4", this.items, "Axe");
    private final ListValue sortSlot5Value = new ListValue("SortSlot-5", this.items, "None");
    private final ListValue sortSlot6Value = new ListValue("SortSlot-6", this.items, "None");
    private final ListValue sortSlot7Value = new ListValue("SortSlot-7", this.items, "Food");
    private final ListValue sortSlot8Value = new ListValue("SortSlot-8", this.items, "Block");
    private final ListValue sortSlot9Value = new ListValue("SortSlot-9", this.items, "Block");
    private long delay;

    /*
     * WARNING - void declaration
     */
    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        IEntityPlayerSP thePlayer;
        block15: {
            block14: {
                Intrinsics.checkParameterIsNotNull(event, "event");
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    return;
                }
                thePlayer = iEntityPlayerSP;
                if (!InventoryUtils.CLICK_TIMER.hasTimePassed(this.delay) || !MinecraftInstance.classProvider.isGuiInventory(MinecraftInstance.mc.getCurrentScreen()) && ((Boolean)this.invOpenValue.get()).booleanValue() || ((Boolean)this.noMoveValue.get()).booleanValue() && MovementUtils.isMoving()) break block14;
                if (thePlayer.getOpenContainer() == null) break block15;
                IContainer iContainer = thePlayer.getOpenContainer();
                if (iContainer == null) {
                    Intrinsics.throwNpe();
                }
                if (iContainer.getWindowId() == 0) break block15;
            }
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
            Map<Integer, IItemStack> map = $this$filter$iv;
            Map destination$iv$iv = new LinkedHashMap();
            boolean $i$f$filterTo = false;
            void var9_11 = $this$filterTo$iv$iv;
            boolean bl = false;
            Iterator iterator2 = var9_11.entrySet().iterator();
            while (iterator2.hasNext()) {
                Map.Entry element$iv$iv;
                Map.Entry it = element$iv$iv = iterator2.next();
                boolean bl2 = false;
                if (!(!this.isUseful((IItemStack)it.getValue(), ((Number)it.getKey()).intValue()))) continue;
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
            boolean bl3 = openInventory = !MinecraftInstance.classProvider.isGuiInventory(MinecraftInstance.mc.getCurrentScreen()) && (Boolean)this.simulateInventory.get() != false;
            if (openInventory) {
                IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                boolean $i$f$createOpenInventoryPacket = false;
                IClassProvider iClassProvider = WrapperImpl.INSTANCE.getClassProvider();
                IEntityPlayerSP iEntityPlayerSP = LiquidBounce.INSTANCE.getWrapper().getMinecraft().getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                IPacket iPacket = iClassProvider.createCPacketEntityAction(iEntityPlayerSP, ICPacketEntityAction.WAction.OPEN_INVENTORY);
                iINetHandlerPlayClient.addToSendQueue(iPacket);
            }
            IPlayerControllerMP iPlayerControllerMP = MinecraftInstance.mc.getPlayerController();
            IContainer iContainer = thePlayer.getOpenContainer();
            if (iContainer == null) {
                Intrinsics.throwNpe();
            }
            iPlayerControllerMP.windowClick(iContainer.getWindowId(), garbageItem, 1, 4, thePlayer);
            if (openInventory) {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketCloseWindow());
            }
            this.delay = TimeUtils.randomDelay(((Number)this.minDelayValue.get()).intValue(), ((Number)this.maxDelayValue.get()).intValue());
        }
    }

    /*
     * WARNING - void declaration
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public final boolean isUseful(@NotNull IItemStack itemStack, int slot) {
        Intrinsics.checkParameterIsNotNull(itemStack, "itemStack");
        try {
            Object object;
            IItem item = itemStack.getItem();
            if (MinecraftInstance.classProvider.isItemSword(item) || MinecraftInstance.classProvider.isItemTool(item)) {
                boolean bl;
                int n;
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) return true;
                IEntityPlayerSP thePlayer = iEntityPlayerSP;
                if (slot >= 36) {
                    Integer n2 = this.findBetterItem(slot - 36, thePlayer.getInventory().getStackInSlot(slot - 36));
                    n = slot - 36;
                    if (n2 != null && n2 == n) {
                        return true;
                    }
                }
                n = 0;
                int n3 = 8;
                while (n <= n3) {
                    void i;
                    if ((StringsKt.equals(this.type((int)i), "sword", true) && MinecraftInstance.classProvider.isItemSword(item) || StringsKt.equals(this.type((int)i), "pickaxe", true) && MinecraftInstance.classProvider.isItemPickaxe(item) || StringsKt.equals(this.type((int)i), "axe", true) && MinecraftInstance.classProvider.isItemAxe(item)) && this.findBetterItem((int)i, thePlayer.getInventory().getStackInSlot((int)i)) == null) {
                        return true;
                    }
                    ++i;
                }
                IAttributeModifier iAttributeModifier = (IAttributeModifier)CollectionsKt.firstOrNull((Iterable)itemStack.getAttributeModifier("generic.attackDamage"));
                double damage = (iAttributeModifier != null ? iAttributeModifier.getAmount() : 0.0) + 1.25 * (double)ItemUtils.getEnchantment(itemStack, MinecraftInstance.classProvider.getEnchantmentEnum(EnchantmentType.SHARPNESS));
                Map<Integer, IItemStack> $this$none$iv = this.items(0, 45);
                boolean $i$f$none = false;
                if ($this$none$iv.isEmpty()) {
                    return true;
                }
                object = $this$none$iv;
                boolean bl2 = false;
                Iterator iterator2 = object.entrySet().iterator();
                do {
                    Map.Entry element$iv;
                    if (!iterator2.hasNext()) return true;
                    Map.Entry $dstr$_u24__u24$stack2 = element$iv = iterator2.next();
                    boolean bl3 = false;
                    Map.Entry entry = $dstr$_u24__u24$stack2;
                    boolean bl4 = false;
                    IItemStack stack = (IItemStack)entry.getValue();
                    if (Intrinsics.areEqual(stack, itemStack) ^ true && Intrinsics.areEqual(stack.getClass(), itemStack.getClass())) {
                        IAttributeModifier iAttributeModifier2 = (IAttributeModifier)CollectionsKt.firstOrNull((Iterable)stack.getAttributeModifier("generic.attackDamage"));
                        double d = iAttributeModifier2 != null ? iAttributeModifier2.getAmount() : 0.0;
                        if (damage < d + 1.25 * (double)ItemUtils.getEnchantment(stack, MinecraftInstance.classProvider.getEnchantmentEnum(EnchantmentType.SHARPNESS))) {
                            return false;
                        }
                    }
                    bl = false;
                } while (!bl);
                return false;
            }
            if (MinecraftInstance.classProvider.isItemBow(item)) {
                boolean bl;
                int currPower = ItemUtils.getEnchantment(itemStack, MinecraftInstance.classProvider.getEnchantmentEnum(EnchantmentType.POWER));
                Map $this$none$iv = InventoryCleaner.items$default(this, 0, 0, 3, null);
                boolean $i$f$none = false;
                if ($this$none$iv.isEmpty()) {
                    return true;
                }
                Map map = $this$none$iv;
                boolean bl5 = false;
                object = map.entrySet().iterator();
                do {
                    Map.Entry element$iv;
                    if (!object.hasNext()) return true;
                    Map.Entry $dstr$_u24__u24$stack = element$iv = object.next();
                    boolean bl6 = false;
                    Map.Entry $dstr$_u24__u24$stack2 = $dstr$_u24__u24$stack;
                    boolean bl3 = false;
                    IItemStack stack = (IItemStack)$dstr$_u24__u24$stack2.getValue();
                    if (Intrinsics.areEqual(itemStack, stack) ^ true && MinecraftInstance.classProvider.isItemBow(stack.getItem()) && currPower < ItemUtils.getEnchantment(stack, MinecraftInstance.classProvider.getEnchantmentEnum(EnchantmentType.POWER))) {
                        return false;
                    }
                    bl = false;
                } while (!bl);
                return false;
            }
            if (MinecraftInstance.classProvider.isItemArmor(item)) {
                boolean bl;
                ArmorPiece currArmor = new ArmorPiece(itemStack, slot);
                Map $this$none$iv = InventoryCleaner.items$default(this, 0, 0, 3, null);
                boolean $i$f$none = false;
                if ($this$none$iv.isEmpty()) {
                    return true;
                }
                Map map = $this$none$iv;
                boolean bl7 = false;
                object = map.entrySet().iterator();
                do {
                    Map.Entry element$iv;
                    if (!object.hasNext()) return true;
                    Map.Entry $dstr$slot$stack = element$iv = object.next();
                    boolean bl8 = false;
                    Map.Entry $dstr$_u24__u24$stack2 = $dstr$slot$stack;
                    boolean bl3 = false;
                    int stack = ((Number)$dstr$_u24__u24$stack2.getKey()).intValue();
                    $dstr$_u24__u24$stack2 = $dstr$slot$stack;
                    bl3 = false;
                    IItemStack stack2 = (IItemStack)$dstr$_u24__u24$stack2.getValue();
                    if (Intrinsics.areEqual(stack2, itemStack) ^ true && MinecraftInstance.classProvider.isItemArmor(stack2.getItem())) {
                        void slot2;
                        ArmorPiece armor = new ArmorPiece(stack2, (int)slot2);
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
            if (Intrinsics.areEqual(itemStack.getUnlocalizedName(), "item.compass")) {
                boolean bl;
                Map<Integer, IItemStack> $this$none$iv = this.items(0, 45);
                boolean $i$f$none = false;
                if ($this$none$iv.isEmpty()) {
                    return true;
                }
                Map<Integer, IItemStack> map = $this$none$iv;
                boolean bl9 = false;
                Iterator<Map.Entry<Integer, IItemStack>> iterator3 = map.entrySet().iterator();
                do {
                    Map.Entry<Integer, IItemStack> element$iv;
                    if (!iterator3.hasNext()) return true;
                    Map.Entry<Integer, IItemStack> $dstr$_u24__u24$stack = element$iv = iterator3.next();
                    boolean bl10 = false;
                    Map.Entry<Integer, IItemStack> entry = $dstr$_u24__u24$stack;
                    boolean bl11 = false;
                    IItemStack stack = entry.getValue();
                    if (Intrinsics.areEqual(itemStack, stack) ^ true && Intrinsics.areEqual(stack.getUnlocalizedName(), "item.compass")) {
                        return false;
                    }
                    bl = false;
                } while (!bl);
                return false;
            }
            if (MinecraftInstance.classProvider.isItemFood(item)) return true;
            if (Intrinsics.areEqual(itemStack.getUnlocalizedName(), "item.arrow")) return true;
            if (MinecraftInstance.classProvider.isItemBlock(item)) {
                IItem iItem = item;
                if (!MinecraftInstance.classProvider.isBlockBush(iItem != null && (iItem = iItem.asItemBlock()) != null ? iItem.getBlock() : null)) return true;
            }
            if (MinecraftInstance.classProvider.isItemBed(item)) return true;
            if (Intrinsics.areEqual(itemStack.getUnlocalizedName(), "item.diamond")) return true;
            if (Intrinsics.areEqual(itemStack.getUnlocalizedName(), "item.ingotIron")) return true;
            if (MinecraftInstance.classProvider.isItemPotion(item)) return true;
            if (MinecraftInstance.classProvider.isItemEnderPearl(item)) return true;
            if (MinecraftInstance.classProvider.isItemEnchantedBook(item)) return true;
            if (MinecraftInstance.classProvider.isItemBucket(item)) return true;
            if (Intrinsics.areEqual(itemStack.getUnlocalizedName(), "item.stick")) return true;
            if ((Boolean)this.ignoreVehiclesValue.get() == false) return false;
            if (MinecraftInstance.classProvider.isItemBoat(item)) return true;
            if (!MinecraftInstance.classProvider.isItemMinecart(item)) return false;
            return true;
        }
        catch (Exception ex) {
            ClientUtils.getLogger().error("(InventoryCleaner) Failed to check item: " + itemStack.getUnlocalizedName() + '.', (Throwable)ex);
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
            IEntityPlayerSP thePlayer;
            void index;
            if (MinecraftInstance.mc.getThePlayer() == null) {
                return;
            }
            Integer n3 = this.findBetterItem((int)index, thePlayer.getInventory().getStackInSlot((int)index));
            if (n3 == null) {
            } else {
                int bestItem = n3;
                if (bestItem != index) {
                    boolean openInventory;
                    boolean bl = openInventory = !MinecraftInstance.classProvider.isGuiInventory(MinecraftInstance.mc.getCurrentScreen()) && (Boolean)this.simulateInventory.get() != false;
                    if (openInventory) {
                        IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                        boolean $i$f$createOpenInventoryPacket = false;
                        IClassProvider iClassProvider = WrapperImpl.INSTANCE.getClassProvider();
                        IEntityPlayerSP iEntityPlayerSP = LiquidBounce.INSTANCE.getWrapper().getMinecraft().getThePlayer();
                        if (iEntityPlayerSP == null) {
                            Intrinsics.throwNpe();
                        }
                        IPacket iPacket = iClassProvider.createCPacketEntityAction(iEntityPlayerSP, ICPacketEntityAction.WAction.OPEN_INVENTORY);
                        iINetHandlerPlayClient.addToSendQueue(iPacket);
                    }
                    MinecraftInstance.mc.getPlayerController().windowClick(0, bestItem < 9 ? bestItem + 36 : bestItem, (int)index, 2, thePlayer);
                    if (openInventory) {
                        MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketCloseWindow());
                    }
                    this.delay = TimeUtils.randomDelay(((Number)this.minDelayValue.get()).intValue(), ((Number)this.maxDelayValue.get()).intValue());
                    break;
                }
            }
            ++index;
        }
    }

    /*
     * Unable to fully structure code
     */
    private final Integer findBetterItem(int targetSlot, IItemStack slotStack) {
        type = this.type(targetSlot);
        v0 = MinecraftInstance.mc.getThePlayer();
        if (v0 == null) {
            return null;
        }
        thePlayer = v0;
        var5_5 = type;
        var6_6 = false;
        v1 = var5_5;
        if (v1 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        v2 = v1.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(v2, "(this as java.lang.String).toLowerCase()");
        var5_5 = v2;
        tmp = -1;
        switch (var5_5.hashCode()) {
            case 109860349: {
                if (!var5_5.equals("sword")) break;
                tmp = 1;
                break;
            }
            case -578028723: {
                if (!var5_5.equals("pickaxe")) break;
                tmp = 1;
                break;
            }
            case 93832333: {
                if (!var5_5.equals("block")) break;
                tmp = 2;
                break;
            }
            case 97738: {
                if (!var5_5.equals("bow")) break;
                tmp = 3;
                break;
            }
            case 112903447: {
                if (!var5_5.equals("water")) break;
                tmp = 4;
                break;
            }
            case -1253135533: {
                if (!var5_5.equals("gapple")) break;
                tmp = 5;
                break;
            }
            case 3148894: {
                if (!var5_5.equals("food")) break;
                tmp = 6;
                break;
            }
            case 97038: {
                if (!var5_5.equals("axe")) break;
                tmp = 1;
                break;
            }
            case 106540102: {
                if (!var5_5.equals("pearl")) break;
                tmp = 7;
                break;
            }
        }
        switch (tmp) {
            case 1: {
                if (StringsKt.equals(type, "Sword", true)) {
                    v3 = findBetterItem.currentTypeChecker.1.INSTANCE;
                } else if (StringsKt.equals(type, "Pickaxe", true)) {
                    v3 = findBetterItem.currentTypeChecker.2.INSTANCE;
                } else if (StringsKt.equals(type, "Axe", true)) {
                    v3 = findBetterItem.currentTypeChecker.3.INSTANCE;
                } else {
                    return null;
                }
                currentTypeChecker = v3;
                v4 = slotStack;
                bestWeapon = (Boolean)currentTypeChecker.invoke(v4 != null ? v4.getItem() : null) != false ? targetSlot : -1;
                $this$forEachIndexed$iv = thePlayer.getInventory().getMainInventory();
                $i$f$forEachIndexed = false;
                index$iv = 0;
                for (T item$iv : $this$forEachIndexed$iv) {
                    var13_55 = index$iv++;
                    var14_62 = false;
                    if (var13_55 < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    var15_69 = var13_55;
                    var16_76 = (IItemStack)item$iv;
                    index = var15_69;
                    $i$a$-forEachIndexed-InventoryCleaner$findBetterItem$1 = false;
                    if (itemStack == null || !((Boolean)currentTypeChecker.invoke(itemStack.getItem())).booleanValue() || StringsKt.equals(this.type(index), type, true)) continue;
                    if (bestWeapon == -1) {
                        bestWeapon = index;
                        continue;
                    }
                    v5 = (IAttributeModifier)CollectionsKt.firstOrNull((Iterable)itemStack.getAttributeModifier("generic.attackDamage"));
                    currDamage = (v5 != null ? v5.getAmount() : 0.0) + 1.25 * (double)ItemUtils.getEnchantment((IItemStack)itemStack, MinecraftInstance.classProvider.getEnchantmentEnum(EnchantmentType.SHARPNESS));
                    if (thePlayer.getInventory().getStackInSlot(bestWeapon) == null) {
                        continue;
                    }
                    v6 = (IAttributeModifier)CollectionsKt.firstOrNull((Iterable)bestStack.getAttributeModifier("generic.attackDamage"));
                    v7 = v6 != null ? v6.getAmount() : 0.0;
                    bestDamage = v7 + 1.25 * (double)ItemUtils.getEnchantment(bestStack, MinecraftInstance.classProvider.getEnchantmentEnum(EnchantmentType.SHARPNESS));
                    if (!(bestDamage < currDamage)) continue;
                    bestWeapon = index;
                }
                return bestWeapon != -1 || bestWeapon == targetSlot ? Integer.valueOf(bestWeapon) : null;
            }
            case 3: {
                v8 = slotStack;
                bestBow = MinecraftInstance.classProvider.isItemBow(v8 != null ? v8.getItem() : null) != false ? targetSlot : -1;
                bestPower = bestBow != -1 ? ItemUtils.getEnchantment(slotStack, MinecraftInstance.classProvider.getEnchantmentEnum(EnchantmentType.POWER)) : 0;
                $this$forEachIndexed$iv = thePlayer.getInventory().getMainInventory();
                $i$f$forEachIndexed = false;
                index$iv = 0;
                for (T item$iv : $this$forEachIndexed$iv) {
                    var13_56 = index$iv++;
                    var14_63 = false;
                    if (var13_56 < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    var15_70 = var13_56;
                    itemStack = (IItemStack)item$iv;
                    index = var15_70;
                    $i$a$-forEachIndexed-InventoryCleaner$findBetterItem$2 = false;
                    v9 = itemStack;
                    if (!MinecraftInstance.classProvider.isItemBow(v9 != null ? v9.getItem() : null) || StringsKt.equals(this.type(index), type, true)) continue;
                    if (bestBow == -1) {
                        bestBow = index;
                        continue;
                    }
                    power = ItemUtils.getEnchantment(itemStack, MinecraftInstance.classProvider.getEnchantmentEnum(EnchantmentType.POWER));
                    if (ItemUtils.getEnchantment(itemStack, MinecraftInstance.classProvider.getEnchantmentEnum(EnchantmentType.POWER)) <= bestPower) continue;
                    bestBow = index;
                    bestPower = power;
                }
                return bestBow != -1 ? Integer.valueOf(bestBow) : null;
            }
            case 6: {
                $this$forEachIndexed$iv = thePlayer.getInventory().getMainInventory();
                $i$f$forEachIndexed = false;
                index$iv = 0;
                for (T item$iv : $this$forEachIndexed$iv) {
                    var11_43 = index$iv++;
                    var12_50 = false;
                    if (var11_43 < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    var13_57 = var11_43;
                    var14_64 = (IItemStack)item$iv;
                    index = var13_57;
                    $i$a$-forEachIndexed-InventoryCleaner$findBetterItem$3 = false;
                    if (stack == null || !MinecraftInstance.classProvider.isItemFood(item = stack.getItem()) || MinecraftInstance.classProvider.isItemAppleGold(item) || StringsKt.equals(this.type(index), "Food", true)) continue;
                    replaceCurr = ItemUtils.isStackEmpty(slotStack) != false || MinecraftInstance.classProvider.isItemFood(item) == false;
                    return replaceCurr != false ? Integer.valueOf(index) : null;
                }
                break;
            }
            case 2: {
                $this$forEachIndexed$iv = thePlayer.getInventory().getMainInventory();
                $i$f$forEachIndexed = false;
                index$iv = 0;
                for (T item$iv : $this$forEachIndexed$iv) {
                    var11_44 = index$iv++;
                    var12_51 = false;
                    if (var11_44 < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    var13_58 = var11_44;
                    stack = (IItemStack)item$iv;
                    index = var13_58;
                    $i$a$-forEachIndexed-InventoryCleaner$findBetterItem$4 = false;
                    if (stack == null) continue;
                    if (stack.getItem() == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!MinecraftInstance.classProvider.isItemBlock(item) || InventoryUtils.BLOCK_BLACKLIST.contains(item.asItemBlock().getBlock()) || StringsKt.equals(this.type(index), "Block", true)) continue;
                    replaceCurr = ItemUtils.isStackEmpty(slotStack) != false || MinecraftInstance.classProvider.isItemBlock(item) == false;
                    return replaceCurr != false ? Integer.valueOf(index) : null;
                }
                break;
            }
            case 4: {
                $this$forEachIndexed$iv = thePlayer.getInventory().getMainInventory();
                $i$f$forEachIndexed = false;
                index$iv = 0;
                for (T item$iv : $this$forEachIndexed$iv) {
                    var11_45 = index$iv++;
                    var12_52 = false;
                    if (var11_45 < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    var13_59 = var11_45;
                    stack = (IItemStack)item$iv;
                    index = var13_59;
                    $i$a$-forEachIndexed-InventoryCleaner$findBetterItem$5 = false;
                    if (stack == null) continue;
                    if (stack.getItem() == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!MinecraftInstance.classProvider.isItemBucket(item) || !Intrinsics.areEqual(item.asItemBucket().isFull(), MinecraftInstance.classProvider.getBlockEnum(BlockType.FLOWING_WATER)) || StringsKt.equals(this.type(index), "Water", true)) continue;
                    replaceCurr = ItemUtils.isStackEmpty(slotStack) != false || MinecraftInstance.classProvider.isItemBucket(item) == false || (Intrinsics.areEqual(item.asItemBucket().isFull(), MinecraftInstance.classProvider.getBlockEnum(BlockType.FLOWING_WATER)) ^ true) != false;
                    return replaceCurr != false ? Integer.valueOf(index) : null;
                }
                break;
            }
            case 5: {
                $this$forEachIndexed$iv = thePlayer.getInventory().getMainInventory();
                $i$f$forEachIndexed = false;
                index$iv = 0;
                for (T item$iv : $this$forEachIndexed$iv) {
                    var11_46 = index$iv++;
                    var12_53 = false;
                    if (var11_46 < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    var13_60 = var11_46;
                    stack = (IItemStack)item$iv;
                    index = var13_60;
                    $i$a$-forEachIndexed-InventoryCleaner$findBetterItem$6 = false;
                    if (stack == null) continue;
                    if (stack.getItem() == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!MinecraftInstance.classProvider.isItemAppleGold(item) || StringsKt.equals(this.type(index), "Gapple", true)) continue;
                    if (ItemUtils.isStackEmpty(slotStack)) ** GOTO lbl-1000
                    v10 = slotStack;
                    if (!MinecraftInstance.classProvider.isItemAppleGold(v10 != null ? v10.getItem() : null)) lbl-1000:
                    // 2 sources

                    {
                        v11 = true;
                    } else {
                        v11 = false;
                    }
                    replaceCurr = v11;
                    return replaceCurr != false ? Integer.valueOf(index) : null;
                }
                break;
            }
            case 7: {
                $this$forEachIndexed$iv = thePlayer.getInventory().getMainInventory();
                $i$f$forEachIndexed = false;
                index$iv = 0;
                for (T item$iv : $this$forEachIndexed$iv) {
                    var11_47 = index$iv++;
                    var12_54 = false;
                    if (var11_47 < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    var13_61 = var11_47;
                    stack = (IItemStack)item$iv;
                    index = var13_61;
                    $i$a$-forEachIndexed-InventoryCleaner$findBetterItem$7 = false;
                    if (stack == null || !MinecraftInstance.classProvider.isItemEnderPearl(item = stack.getItem()) || StringsKt.equals(this.type(index), "Pearl", true)) continue;
                    if (ItemUtils.isStackEmpty(slotStack)) ** GOTO lbl-1000
                    v12 = slotStack;
                    if (!MinecraftInstance.classProvider.isItemEnderPearl(v12 != null ? v12.getItem() : null)) lbl-1000:
                    // 2 sources

                    {
                        v13 = true;
                    } else {
                        v13 = false;
                    }
                    replaceCurr = v13;
                    return replaceCurr != false ? Integer.valueOf(index) : null;
                }
                break;
            }
        }
        return null;
    }

    /*
     * WARNING - void declaration
     */
    private final Map<Integer, IItemStack> items(int start, int end) {
        int n = 0;
        Map items = new LinkedHashMap();
        n = end - 1;
        int n2 = start;
        if (n >= n2) {
            while (true) {
                void i;
                Object object;
                if ((object = MinecraftInstance.mc.getThePlayer()) == null || (object = object.getInventoryContainer()) == null || (object = object.getSlot((int)i)) == null || (object = object.getStack()) == null) {
                } else {
                    void var7_7;
                    Object itemStack = object;
                    if (!(ItemUtils.isStackEmpty((IItemStack)itemStack) || 36 <= (var7_7 = i) && 44 >= var7_7 && StringsKt.equals(this.type((int)i), "Ignore", true) || System.currentTimeMillis() - itemStack.getItemDelay() < ((Number)this.itemDelayValue.get()).longValue())) {
                        items.put((int)i, itemStack);
                    }
                }
                if (i == n2) break;
                --i;
            }
        }
        return items;
    }

    static Map items$default(InventoryCleaner inventoryCleaner, int n, int n2, int n3, Object object) {
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

    public static final IntegerValue access$getMinDelayValue$p(InventoryCleaner $this) {
        return $this.minDelayValue;
    }

    public static final IntegerValue access$getMaxDelayValue$p(InventoryCleaner $this) {
        return $this.maxDelayValue;
    }
}
