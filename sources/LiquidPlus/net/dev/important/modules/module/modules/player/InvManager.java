/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.inventory.GuiInventory
 *  net.minecraft.enchantment.Enchantment
 *  net.minecraft.entity.ai.attributes.AttributeModifier
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemAppleGold
 *  net.minecraft.item.ItemArmor
 *  net.minecraft.item.ItemAxe
 *  net.minecraft.item.ItemBed
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemBoat
 *  net.minecraft.item.ItemBow
 *  net.minecraft.item.ItemBucket
 *  net.minecraft.item.ItemEnderPearl
 *  net.minecraft.item.ItemFood
 *  net.minecraft.item.ItemMinecart
 *  net.minecraft.item.ItemPickaxe
 *  net.minecraft.item.ItemPotion
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.item.ItemTool
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C07PacketPlayerDigging
 *  net.minecraft.network.play.client.C07PacketPlayerDigging$Action
 *  net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
 *  net.minecraft.network.play.client.C09PacketHeldItemChange
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module.modules.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.event.EventTarget;
import net.dev.important.event.MotionEvent;
import net.dev.important.event.UpdateEvent;
import net.dev.important.event.WorldEvent;
import net.dev.important.injection.implementations.IItemStack;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.ClientUtils;
import net.dev.important.utils.InventoryHelper;
import net.dev.important.utils.InventoryUtils;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.MovementUtils;
import net.dev.important.utils.item.ArmorPart;
import net.dev.important.utils.item.ItemHelper;
import net.dev.important.utils.timer.TimeUtils;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBed;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBoat;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemMinecart;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.jetbrains.annotations.NotNull;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Info(name="InvManager", spacedName="Inv Manager", description="Automatically throws away useless items, and also equips armors for you.", category=Category.PLAYER, cnName="\u81ea\u52a8\u6574\u7406")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010%\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0010\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0015\u00108\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00050\u0004H\u0002\u00a2\u0006\u0002\u00109J!\u0010:\u001a\u0004\u0018\u00010\u000f2\u0006\u0010;\u001a\u00020\u000f2\b\u0010<\u001a\u0004\u0018\u00010\u0010H\u0002\u00a2\u0006\u0002\u0010=J\b\u0010>\u001a\u00020?H\u0002J\u0016\u0010@\u001a\u0002042\u0006\u0010A\u001a\u00020\u00102\u0006\u0010B\u001a\u00020\u000fJ(\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00100\u000e2\b\b\u0002\u0010C\u001a\u00020\u000f2\b\b\u0002\u0010D\u001a\u00020\u000fH\u0002J\u0016\u0010E\u001a\u0002042\u0006\u0010F\u001a\u00020\u000f2\u0006\u0010G\u001a\u000204J\b\u0010H\u001a\u00020?H\u0016J\u0010\u0010I\u001a\u00020?2\u0006\u0010J\u001a\u00020KH\u0007J\u0010\u0010L\u001a\u00020?2\u0006\u0010J\u001a\u00020MH\u0007J\u0010\u0010N\u001a\u00020?2\u0006\u0010J\u001a\u00020OH\u0007J\u0006\u0010P\u001a\u00020?J\b\u0010Q\u001a\u00020?H\u0002J\u0010\u0010R\u001a\u00020\u001d2\u0006\u0010;\u001a\u00020\u000fH\u0002R\u0018\u0010\u0003\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00050\u0004X\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u0006R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00100\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0011\u001a\u00020\u00128BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0013\u0010\u0014R\u000e\u0010\u0015\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u001bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u001d0\u0004X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u001eR\u000e\u0010\u001f\u001a\u00020\u001bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u001bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\"X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020\"X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010.\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010/\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00100\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00101\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00102\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u00105\u001a\u0002042\u0006\u00103\u001a\u000204@BX\u0082\u000e\u00a2\u0006\b\n\u0000\"\u0004\b6\u00107\u00a8\u0006S"}, d2={"Lnet/dev/important/modules/module/modules/player/InvManager;", "Lnet/dev/important/modules/module/Module;", "()V", "armorQueue", "", "Lnet/dev/important/utils/item/ArmorPart;", "[Lnet/dev/important/utils/item/ArmorPart;", "armorsValue", "Lnet/dev/important/value/BoolValue;", "delay", "", "eventModeValue", "Lnet/dev/important/value/ListValue;", "garbageQueue", "", "", "Lnet/minecraft/item/ItemStack;", "goal", "Lnet/dev/important/utils/item/ItemHelper$EnumNBTPriorityType;", "getGoal", "()Lnet/dev/important/utils/item/ItemHelper$EnumNBTPriorityType;", "hotbarValue", "ignoreVehiclesValue", "invOpenValue", "invSpoof", "invSpoofOld", "itemDelayValue", "Lnet/dev/important/value/IntegerValue;", "items", "", "[Ljava/lang/String;", "maxDelayValue", "minDelayValue", "nbtArmorPriority", "Lnet/dev/important/value/FloatValue;", "nbtGoalValue", "nbtItemNotGarbage", "nbtWeaponPriority", "noMoveValue", "onlyPositivePotionValue", "randomSlotValue", "sortSlot1Value", "sortSlot2Value", "sortSlot3Value", "sortSlot4Value", "sortSlot5Value", "sortSlot6Value", "sortSlot7Value", "sortSlot8Value", "sortSlot9Value", "sortValue", "value", "", "spoofInventory", "setSpoofInventory", "(Z)V", "findBestArmor", "()[Lnet/dev/important/utils/item/ArmorPart;", "findBetterItem", "targetSlot", "slotStack", "(ILnet/minecraft/item/ItemStack;)Ljava/lang/Integer;", "findQueueItems", "", "isUseful", "itemStack", "slot", "start", "end", "move", "item", "isArmorSlot", "onEnable", "onMotion", "event", "Lnet/dev/important/event/MotionEvent;", "onUpdate", "Lnet/dev/important/event/UpdateEvent;", "onWorld", "Lnet/dev/important/event/WorldEvent;", "performManager", "sortHotbar", "type", "LiquidBounce"})
public final class InvManager
extends Module {
    @NotNull
    private final IntegerValue maxDelayValue = new IntegerValue(this){
        final /* synthetic */ InvManager this$0;
        {
            this.this$0 = $receiver;
            super("MaxDelay", 600, 0, 1000, "ms");
        }

        protected void onChanged(int oldValue, int newValue) {
            int minCPS2 = ((Number)InvManager.access$getMinDelayValue$p(this.this$0).get()).intValue();
            if (minCPS2 > newValue) {
                this.set(minCPS2);
            }
        }
    };
    @NotNull
    private final IntegerValue minDelayValue = new IntegerValue(this){
        final /* synthetic */ InvManager this$0;
        {
            this.this$0 = $receiver;
            super("MinDelay", 400, 0, 1000, "ms");
        }

        protected void onChanged(int oldValue, int newValue) {
            int maxDelay = ((Number)InvManager.access$getMaxDelayValue$p(this.this$0).get()).intValue();
            if (maxDelay < newValue) {
                this.set(maxDelay);
            }
        }
    };
    @NotNull
    private final ListValue eventModeValue;
    @NotNull
    private final BoolValue invOpenValue;
    @NotNull
    private final BoolValue invSpoof;
    @NotNull
    private final BoolValue invSpoofOld;
    @NotNull
    private final BoolValue armorsValue;
    @NotNull
    private final BoolValue noMoveValue;
    @NotNull
    private final BoolValue hotbarValue;
    @NotNull
    private final BoolValue randomSlotValue;
    @NotNull
    private final BoolValue sortValue;
    @NotNull
    private final IntegerValue itemDelayValue;
    @NotNull
    private final BoolValue ignoreVehiclesValue;
    @NotNull
    private final BoolValue onlyPositivePotionValue;
    @NotNull
    private final ListValue nbtGoalValue;
    @NotNull
    private final BoolValue nbtItemNotGarbage;
    @NotNull
    private final FloatValue nbtArmorPriority;
    @NotNull
    private final FloatValue nbtWeaponPriority;
    @NotNull
    private final String[] items;
    @NotNull
    private final ListValue sortSlot1Value;
    @NotNull
    private final ListValue sortSlot2Value;
    @NotNull
    private final ListValue sortSlot3Value;
    @NotNull
    private final ListValue sortSlot4Value;
    @NotNull
    private final ListValue sortSlot5Value;
    @NotNull
    private final ListValue sortSlot6Value;
    @NotNull
    private final ListValue sortSlot7Value;
    @NotNull
    private final ListValue sortSlot8Value;
    @NotNull
    private final ListValue sortSlot9Value;
    @NotNull
    private Map<Integer, ItemStack> garbageQueue;
    @NotNull
    private ArmorPart[] armorQueue;
    private boolean spoofInventory;
    private long delay;

    /*
     * WARNING - void declaration
     */
    public InvManager() {
        Collection<String> collection;
        void $this$mapTo$iv$iv;
        void $this$map$iv;
        Object[] objectArray = new String[]{"Update", "MotionPre", "MotionPost"};
        this.eventModeValue = new ListValue("OnEvent", (String[])objectArray, "Update");
        this.invOpenValue = new BoolValue("InvOpen", false);
        this.invSpoof = new BoolValue("InvSpoof", true);
        this.invSpoofOld = new BoolValue("InvSpoof-Old", false, new Function0<Boolean>(this){
            final /* synthetic */ InvManager this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return (Boolean)InvManager.access$getInvSpoof$p(this.this$0).get();
            }
        });
        this.armorsValue = new BoolValue("WearArmors", true);
        this.noMoveValue = new BoolValue("NoMove", false);
        this.hotbarValue = new BoolValue("Hotbar", true);
        this.randomSlotValue = new BoolValue("RandomSlot", false);
        this.sortValue = new BoolValue("Sort", true);
        this.itemDelayValue = new IntegerValue("ItemDelay", 0, 0, 5000, "ms");
        this.ignoreVehiclesValue = new BoolValue("IgnoreVehicles", false);
        this.onlyPositivePotionValue = new BoolValue("OnlyPositivePotion", false);
        objectArray = ItemHelper.EnumNBTPriorityType.values();
        String string = "NBTGoal";
        InvManager invManager = this;
        boolean $i$f$map = false;
        void var3_5 = $this$map$iv;
        Collection destination$iv$iv = new ArrayList(((void)$this$map$iv).length);
        boolean $i$f$mapTo = false;
        for (void item$iv$iv : $this$mapTo$iv$iv) {
            void it;
            void var10_12 = item$iv$iv;
            collection = destination$iv$iv;
            boolean bl = false;
            collection.add(it.toString());
        }
        collection = (List)destination$iv$iv;
        Collection $this$toTypedArray$iv = collection;
        boolean $i$f$toTypedArray = false;
        Collection thisCollection$iv = $this$toTypedArray$iv;
        String[] stringArray = thisCollection$iv.toArray(new String[0]);
        if (stringArray == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
        }
        String string2 = "NONE";
        String[] stringArray2 = stringArray;
        String string3 = string;
        invManager.nbtGoalValue = new ListValue(string3, stringArray2, string2);
        this.nbtItemNotGarbage = new BoolValue("NBTItemNotGarbage", true, new Function0<Boolean>(this){
            final /* synthetic */ InvManager this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return !InvManager.access$getNbtGoalValue$p(this.this$0).equals("NONE");
            }
        });
        this.nbtArmorPriority = new FloatValue("NBTArmorPriority", 0.0f, 0.0f, 5.0f, new Function0<Boolean>(this){
            final /* synthetic */ InvManager this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return !InvManager.access$getNbtGoalValue$p(this.this$0).equals("NONE");
            }
        });
        this.nbtWeaponPriority = new FloatValue("NBTWeaponPriority", 0.0f, 0.0f, 5.0f, new Function0<Boolean>(this){
            final /* synthetic */ InvManager this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return !InvManager.access$getNbtGoalValue$p(this.this$0).equals("NONE");
            }
        });
        objectArray = new String[]{"None", "Ignore", "Sword", "Bow", "Pickaxe", "Axe", "Food", "Block", "Water", "Gapple", "Pearl", "Potion"};
        this.items = objectArray;
        this.sortSlot1Value = new ListValue("SortSlot-1", this.items, "Sword", new Function0<Boolean>(this){
            final /* synthetic */ InvManager this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return (Boolean)InvManager.access$getSortValue$p(this.this$0).get();
            }
        });
        this.sortSlot2Value = new ListValue("SortSlot-2", this.items, "Gapple", new Function0<Boolean>(this){
            final /* synthetic */ InvManager this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return (Boolean)InvManager.access$getSortValue$p(this.this$0).get();
            }
        });
        this.sortSlot3Value = new ListValue("SortSlot-3", this.items, "Potion", new Function0<Boolean>(this){
            final /* synthetic */ InvManager this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return (Boolean)InvManager.access$getSortValue$p(this.this$0).get();
            }
        });
        this.sortSlot4Value = new ListValue("SortSlot-4", this.items, "Pickaxe", new Function0<Boolean>(this){
            final /* synthetic */ InvManager this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return (Boolean)InvManager.access$getSortValue$p(this.this$0).get();
            }
        });
        this.sortSlot5Value = new ListValue("SortSlot-5", this.items, "Axe", new Function0<Boolean>(this){
            final /* synthetic */ InvManager this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return (Boolean)InvManager.access$getSortValue$p(this.this$0).get();
            }
        });
        this.sortSlot6Value = new ListValue("SortSlot-6", this.items, "None", new Function0<Boolean>(this){
            final /* synthetic */ InvManager this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return (Boolean)InvManager.access$getSortValue$p(this.this$0).get();
            }
        });
        this.sortSlot7Value = new ListValue("SortSlot-7", this.items, "Block", new Function0<Boolean>(this){
            final /* synthetic */ InvManager this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return (Boolean)InvManager.access$getSortValue$p(this.this$0).get();
            }
        });
        this.sortSlot8Value = new ListValue("SortSlot-8", this.items, "Pearl", new Function0<Boolean>(this){
            final /* synthetic */ InvManager this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return (Boolean)InvManager.access$getSortValue$p(this.this$0).get();
            }
        });
        this.sortSlot9Value = new ListValue("SortSlot-9", this.items, "Food", new Function0<Boolean>(this){
            final /* synthetic */ InvManager this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return (Boolean)InvManager.access$getSortValue$p(this.this$0).get();
            }
        });
        this.garbageQueue = new LinkedHashMap();
        this.armorQueue = new ArmorPart[0];
    }

    private final ItemHelper.EnumNBTPriorityType getGoal() {
        return ItemHelper.EnumNBTPriorityType.valueOf((String)this.nbtGoalValue.get());
    }

    private final void setSpoofInventory(boolean value) {
        if (value != this.spoofInventory && !((Boolean)this.invOpenValue.get()).booleanValue()) {
            if (value) {
                InventoryHelper.INSTANCE.openPacket();
            } else {
                InventoryHelper.INSTANCE.closePacket();
            }
        }
        this.spoofInventory = value;
    }

    @Override
    public void onEnable() {
        if (((Boolean)this.invSpoof.get()).booleanValue() && !((Boolean)this.invSpoofOld.get()).booleanValue()) {
            this.setSpoofInventory(false);
        }
        this.garbageQueue.clear();
        this.armorQueue = new ArmorPart[0];
    }

    @EventTarget
    public final void onWorld(@NotNull WorldEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (((Boolean)this.invSpoof.get()).booleanValue() && !((Boolean)this.invSpoofOld.get()).booleanValue()) {
            this.setSpoofInventory(false);
        }
        this.garbageQueue.clear();
        this.armorQueue = new ArmorPart[0];
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (StringsKt.equals((String)this.eventModeValue.get(), "update", true)) {
            this.performManager();
        }
    }

    @EventTarget
    public final void onMotion(@NotNull MotionEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (StringsKt.equals((String)this.eventModeValue.get(), Intrinsics.stringPlus("motion", event.getEventState().getStateName()), true)) {
            this.performManager();
        }
    }

    public final void performManager() {
        int n;
        if (!InventoryUtils.CLICK_TIMER.hasTimePassed(this.delay) || ((Boolean)this.noMoveValue.get()).booleanValue() && MovementUtils.isMoving() || MinecraftInstance.mc.field_71439_g.field_71070_bA != null && MinecraftInstance.mc.field_71439_g.field_71070_bA.field_75152_c != 0) {
            return;
        }
        this.findQueueItems();
        if (((Boolean)this.hotbarValue.get()).booleanValue() && !this.spoofInventory && !(MinecraftInstance.mc.field_71462_r instanceof GuiInventory)) {
            n = 0;
            while (n < 9) {
                ItemStack bestItem;
                int index;
                if (MinecraftInstance.mc.field_71439_g.field_71071_by.func_70301_a(index = n++) == null || bestItem.func_77973_b() == null || !this.garbageQueue.containsValue(bestItem)) continue;
                if (index != MinecraftInstance.mc.field_71439_g.field_71071_by.field_70461_c) {
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(index));
                }
                MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.DROP_ALL_ITEMS, BlockPos.field_177992_a, EnumFacing.DOWN));
                if (index != MinecraftInstance.mc.field_71439_g.field_71071_by.field_70461_c) {
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(MinecraftInstance.mc.field_71439_g.field_71071_by.field_70461_c));
                }
                this.garbageQueue.remove(index, bestItem);
            }
        }
        if (!(MinecraftInstance.mc.field_71462_r instanceof GuiInventory) && ((Boolean)this.invOpenValue.get()).booleanValue() && !((Boolean)this.invSpoof.get()).booleanValue()) {
            return;
        }
        if (this.garbageQueue.isEmpty() && this.armorQueue.length <= 0) {
            if (((Boolean)this.invSpoof.get()).booleanValue() && !((Boolean)this.invSpoofOld.get()).booleanValue()) {
                this.setSpoofInventory(false);
            }
            return;
        }
        if (((Boolean)this.sortValue.get()).booleanValue()) {
            this.sortHotbar();
        }
        if (((Boolean)this.invSpoof.get()).booleanValue() && !((Boolean)this.invSpoofOld.get()).booleanValue()) {
            this.setSpoofInventory(true);
        }
        if (((Boolean)this.armorsValue.get()).booleanValue()) {
            n = 0;
            while (n < 4) {
                ArmorPart ArmorPart2;
                int armorSlot;
                ItemStack oldArmor;
                int i;
                if (this.armorQueue[i = n++] == null || (oldArmor = MinecraftInstance.mc.field_71439_g.field_71071_by.func_70440_f(armorSlot = 3 - i)) != null && oldArmor.func_77973_b() instanceof ItemArmor && ItemHelper.INSTANCE.compareArmor(new ArmorPart(oldArmor, -1), ArmorPart2, ((Number)this.nbtArmorPriority.get()).floatValue(), this.getGoal()) >= 0) continue;
                if (oldArmor != null && this.move(8 - armorSlot, true)) {
                    return;
                }
                if (MinecraftInstance.mc.field_71439_g.field_71071_by.func_70440_f(armorSlot) != null || !this.move(ArmorPart2.getSlot(), false)) continue;
                return;
            }
        }
        while (InventoryUtils.CLICK_TIMER.hasTimePassed(this.delay)) {
            boolean openInventory;
            List garbageItems = CollectionsKt.toMutableList((Collection)this.garbageQueue.keySet());
            if (((Boolean)this.randomSlotValue.get()).booleanValue()) {
                Collections.shuffle(garbageItems);
            }
            Integer n2 = (Integer)CollectionsKt.firstOrNull(garbageItems);
            if (n2 == null) break;
            int garbageItem = n2;
            boolean bl = openInventory = !(MinecraftInstance.mc.field_71462_r instanceof GuiInventory) && (Boolean)this.invSpoof.get() != false && (Boolean)this.invSpoofOld.get() != false;
            if (openInventory) {
                InventoryHelper.INSTANCE.openPacket();
            }
            MinecraftInstance.mc.field_71442_b.func_78753_a(MinecraftInstance.mc.field_71439_g.field_71070_bA.field_75152_c, garbageItem, 1, 4, (EntityPlayer)MinecraftInstance.mc.field_71439_g);
            if (openInventory) {
                InventoryHelper.INSTANCE.closePacket();
            }
            this.delay = TimeUtils.randomDelay(((Number)this.minDelayValue.get()).intValue(), ((Number)this.maxDelayValue.get()).intValue());
            if (this.delay != 0L && !InventoryUtils.CLICK_TIMER.hasTimePassed(this.delay)) continue;
            break;
        }
    }

    private final void sortHotbar() {
        int n = 0;
        while (n < 9) {
            boolean openInventory;
            int bestItem;
            int index = n++;
            Integer n2 = this.findBetterItem(index, MinecraftInstance.mc.field_71439_g.field_71071_by.func_70301_a(index));
            if (n2 == null || (bestItem = n2.intValue()) == index) continue;
            boolean bl = openInventory = !(MinecraftInstance.mc.field_71462_r instanceof GuiInventory) && (Boolean)this.invSpoof.get() != false && (Boolean)this.invSpoofOld.get() != false;
            if (openInventory) {
                InventoryHelper.INSTANCE.openPacket();
            }
            MinecraftInstance.mc.field_71442_b.func_78753_a(0, bestItem < 9 ? bestItem + 36 : bestItem, index, 2, (EntityPlayer)MinecraftInstance.mc.field_71439_g);
            if (openInventory) {
                InventoryHelper.INSTANCE.closePacket();
            }
            this.delay = TimeUtils.randomDelay(((Number)this.minDelayValue.get()).intValue(), ((Number)this.maxDelayValue.get()).intValue());
            break;
        }
    }

    /*
     * WARNING - void declaration
     */
    private final void findQueueItems() {
        void $this$filterTo$iv$iv;
        void $this$filter$iv;
        this.garbageQueue.clear();
        Map<Integer, ItemStack> map = this.items(9, 45);
        InvManager invManager = this;
        boolean $i$f$filter = false;
        void var3_4 = $this$filter$iv;
        Map destination$iv$iv = new LinkedHashMap();
        boolean $i$f$filterTo = false;
        Iterator iterator2 = $this$filterTo$iv$iv.entrySet().iterator();
        while (iterator2.hasNext()) {
            Map.Entry element$iv$iv;
            Map.Entry it = element$iv$iv = iterator2.next();
            boolean bl = false;
            if (!(!this.isUseful((ItemStack)it.getValue(), ((Number)it.getKey()).intValue()))) continue;
            destination$iv$iv.put(element$iv$iv.getKey(), element$iv$iv.getValue());
        }
        invManager.garbageQueue = MapsKt.toMutableMap(destination$iv$iv);
        if (((Boolean)this.armorsValue.get()).booleanValue()) {
            this.armorQueue = this.findBestArmor();
        }
    }

    /*
     * WARNING - void declaration
     */
    private final ArmorPart[] findBestArmor() {
        Map<Integer, List<ArmorPart>> ArmorParts = IntStream.range(0, 36).filter(arg_0 -> InvManager.findBestArmor$lambda-2(this, arg_0)).mapToObj(InvManager::findBestArmor$lambda-3).collect(Collectors.groupingBy(InvManager::findBestArmor$lambda-4));
        ArmorPart[] bestArmor = new ArmorPart[4];
        Intrinsics.checkNotNullExpressionValue(ArmorParts, "ArmorParts");
        for (Map.Entry<Integer, List<ArmorPart>> entry : ArmorParts.entrySet()) {
            void it;
            List<ArmorPart> list;
            Integer key = entry.getKey();
            List<ArmorPart> value = entry.getValue();
            Integer n = key;
            Intrinsics.checkNotNull(n);
            List<ArmorPart> list2 = list = value;
            int n2 = n;
            ArmorPart[] armorPartArray = bestArmor;
            boolean bl = false;
            Intrinsics.checkNotNullExpressionValue(it, "it");
            CollectionsKt.sortWith(it, (arg_0, arg_1) -> InvManager.findBestArmor$lambda-6$lambda-5(this, arg_0, arg_1));
            Unit unit = Unit.INSTANCE;
            List<ArmorPart> list3 = list;
            Intrinsics.checkNotNullExpressionValue(list3, "value.also { it.sortWith\u2026Priority.get(), goal) } }");
            armorPartArray[n2] = CollectionsKt.lastOrNull(list3);
        }
        return bestArmor;
    }

    public final boolean move(int item, boolean isArmorSlot) {
        if (!isArmorSlot && item < 9 && ((Boolean)this.hotbarValue.get()).booleanValue() && !(MinecraftInstance.mc.field_71462_r instanceof GuiInventory) && !this.spoofInventory) {
            if (item != MinecraftInstance.mc.field_71439_g.field_71071_by.field_70461_c) {
                MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(item));
            }
            MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C08PacketPlayerBlockPlacement(MinecraftInstance.mc.field_71439_g.field_71069_bz.func_75139_a(item).func_75211_c()));
            if (item != MinecraftInstance.mc.field_71439_g.field_71071_by.field_70461_c) {
                MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(MinecraftInstance.mc.field_71439_g.field_71071_by.field_70461_c));
            }
            this.delay = TimeUtils.randomDelay(((Number)this.minDelayValue.get()).intValue(), ((Number)this.maxDelayValue.get()).intValue());
            return true;
        }
        if (!(((Boolean)this.noMoveValue.get()).booleanValue() && MovementUtils.isMoving() || ((Boolean)this.invOpenValue.get()).booleanValue() && !(MinecraftInstance.mc.field_71462_r instanceof GuiInventory) || item == -1)) {
            boolean openInventory;
            boolean bl = openInventory = (Boolean)this.invSpoof.get() != false && !(MinecraftInstance.mc.field_71462_r instanceof GuiInventory) && (Boolean)this.invSpoofOld.get() == false;
            if (openInventory) {
                InventoryHelper.INSTANCE.openPacket();
            }
            MinecraftInstance.mc.field_71442_b.func_78753_a(MinecraftInstance.mc.field_71439_g.field_71069_bz.field_75152_c, isArmorSlot ? item : (item < 9 ? item + 36 : item), 0, 1, (EntityPlayer)MinecraftInstance.mc.field_71439_g);
            this.delay = TimeUtils.randomDelay(((Number)this.minDelayValue.get()).intValue(), ((Number)this.maxDelayValue.get()).intValue());
            if (openInventory) {
                InventoryHelper.INSTANCE.closePacket();
            }
            return true;
        }
        return false;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public final boolean isUseful(@NotNull ItemStack itemStack, int slot) {
        Intrinsics.checkNotNullParameter(itemStack, "itemStack");
        try {
            Map.Entry element$iv;
            ItemStack stack;
            Item item = itemStack.func_77973_b();
            if (item instanceof ItemSword || item instanceof ItemTool) {
                boolean bl;
                double d;
                int n;
                if (slot >= 36) {
                    Integer n2 = this.findBetterItem(slot - 36, MinecraftInstance.mc.field_71439_g.field_71071_by.func_70301_a(slot - 36));
                    n = slot - 36;
                    if (n2 != null && n2 == n) {
                        return true;
                    }
                }
                n = 0;
                while (n < 9) {
                    int i;
                    if (!(StringsKt.equals(this.type(i = n++), "sword", true) && item instanceof ItemSword || StringsKt.equals(this.type(i), "pickaxe", true) && item instanceof ItemPickaxe) && (!StringsKt.equals(this.type(i), "axe", true) || !(item instanceof ItemAxe)) || this.findBetterItem(i, MinecraftInstance.mc.field_71439_g.field_71071_by.func_70301_a(i)) != null) continue;
                    return true;
                }
                Collection collection = itemStack.func_111283_C().get((Object)"generic.attackDamage");
                Intrinsics.checkNotNullExpressionValue(collection, "itemStack.attributeModif\u2026s[\"generic.attackDamage\"]");
                AttributeModifier attributeModifier = (AttributeModifier)CollectionsKt.firstOrNull(collection);
                double damage = (attributeModifier == null ? 0.0 : (d = attributeModifier.func_111164_d())) + ItemHelper.INSTANCE.getWeaponEnchantFactor(itemStack, ((Number)this.nbtWeaponPriority.get()).floatValue(), this.getGoal());
                Map<Integer, ItemStack> $this$none$iv = this.items(0, 45);
                boolean $i$f$none2 = false;
                if ($this$none$iv.isEmpty()) {
                    return true;
                }
                Iterator<Map.Entry<Integer, ItemStack>> iterator2 = $this$none$iv.entrySet().iterator();
                do {
                    Map.Entry<Integer, ItemStack> element$iv2;
                    if (!iterator2.hasNext()) return true;
                    Map.Entry<Integer, ItemStack> $dstr$_u24__u24$stack = element$iv2 = iterator2.next();
                    boolean bl2 = false;
                    stack = $dstr$_u24__u24$stack.getValue();
                    if (!Intrinsics.areEqual(stack, itemStack) && Intrinsics.areEqual(stack.getClass(), itemStack.getClass())) {
                        double d2;
                        Collection collection2 = stack.func_111283_C().get((Object)"generic.attackDamage");
                        Intrinsics.checkNotNullExpressionValue(collection2, "stack.attributeModifiers[\"generic.attackDamage\"]");
                        AttributeModifier attributeModifier2 = (AttributeModifier)CollectionsKt.firstOrNull(collection2);
                        double d3 = attributeModifier2 == null ? 0.0 : (d2 = attributeModifier2.func_111164_d());
                        if (damage <= d3 + ItemHelper.INSTANCE.getWeaponEnchantFactor(stack, ((Number)this.nbtWeaponPriority.get()).floatValue(), this.getGoal())) {
                            return false;
                        }
                    }
                    bl = false;
                } while (!bl);
                return false;
            }
            if (item instanceof ItemBow) {
                boolean bl;
                Enchantment i = Enchantment.field_77345_t;
                Intrinsics.checkNotNullExpressionValue(i, "power");
                int currPower = ItemHelper.INSTANCE.getEnchantment(itemStack, i);
                Map $this$none$iv = InvManager.items$default(this, 0, 0, 3, null);
                boolean $i$f$none = false;
                if ($this$none$iv.isEmpty()) {
                    return true;
                }
                Iterator $i$f$none2 = $this$none$iv.entrySet().iterator();
                do {
                    if (!$i$f$none2.hasNext()) return true;
                    Map.Entry $dstr$_u24__u24$stack = element$iv = $i$f$none2.next();
                    boolean bl3 = false;
                    ItemStack stack2 = (ItemStack)$dstr$_u24__u24$stack.getValue();
                    if (!Intrinsics.areEqual(itemStack, stack2) && stack2.func_77973_b() instanceof ItemBow) {
                        stack = Enchantment.field_77345_t;
                        Intrinsics.checkNotNullExpressionValue(stack, "power");
                        if (currPower <= ItemHelper.INSTANCE.getEnchantment(stack2, (Enchantment)stack)) {
                            return false;
                        }
                    }
                    bl = false;
                } while (!bl);
                return false;
            }
            if (item instanceof ItemArmor) {
                boolean bl;
                ArmorPart currArmor = new ArmorPart(itemStack, slot);
                Map $this$none$iv = InvManager.items$default(this, 0, 0, 3, null);
                boolean $i$f$none = false;
                if ($this$none$iv.isEmpty()) {
                    return true;
                }
                Iterator $i$f$none2 = $this$none$iv.entrySet().iterator();
                do {
                    if (!$i$f$none2.hasNext()) return true;
                    Map.Entry $dstr$slot$stack = element$iv = $i$f$none2.next();
                    boolean bl4 = false;
                    int slot2 = ((Number)$dstr$slot$stack.getKey()).intValue();
                    stack = (ItemStack)$dstr$slot$stack.getValue();
                    if (!Intrinsics.areEqual(stack, itemStack) && stack.func_77973_b() instanceof ItemArmor) {
                        ArmorPart armor = new ArmorPart(stack, slot2);
                        if (armor.getArmorType() != currArmor.getArmorType()) {
                            bl = false;
                            continue;
                        }
                        if (ItemHelper.INSTANCE.compareArmor(currArmor, armor, ((Number)this.nbtArmorPriority.get()).floatValue(), this.getGoal()) <= 0) {
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
                Iterator<Map.Entry<Integer, ItemStack>> iterator3 = $this$none$iv.entrySet().iterator();
                do {
                    Map.Entry<Integer, ItemStack> element$iv3;
                    if (!iterator3.hasNext()) return true;
                    Map.Entry<Integer, ItemStack> $dstr$_u24__u24$stack = element$iv3 = iterator3.next();
                    boolean bl5 = false;
                    ItemStack stack3 = $dstr$_u24__u24$stack.getValue();
                    if (!Intrinsics.areEqual(itemStack, stack3) && Intrinsics.areEqual(stack3.func_77977_a(), "item.compass")) {
                        return false;
                    }
                    bl = false;
                } while (!bl);
                return false;
            }
            if (((Boolean)this.nbtItemNotGarbage.get()).booleanValue()) {
                if (ItemHelper.INSTANCE.hasNBTGoal(itemStack, this.getGoal())) return true;
            }
            if (item instanceof ItemFood) return true;
            if (Intrinsics.areEqual(itemStack.func_77977_a(), "item.arrow")) return true;
            if (item instanceof ItemBlock) {
                if (!InventoryHelper.INSTANCE.isBlockListBlock((ItemBlock)item)) return true;
            }
            if (item instanceof ItemBed) return true;
            if (item instanceof ItemPotion) {
                if ((Boolean)this.onlyPositivePotionValue.get() == false) return true;
                if (InventoryHelper.INSTANCE.isPositivePotion((ItemPotion)item, itemStack)) return true;
            }
            if (item instanceof ItemEnderPearl) return true;
            if (item instanceof ItemBucket) return true;
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
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private final Integer findBetterItem(int targetSlot, ItemStack slotStack) {
        String type = this.type(targetSlot);
        String string = type.toLowerCase();
        Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase()");
        switch (string) {
            case "sword": 
            case "pickaxe": 
            case "axe": {
                Class<Object> clazz;
                Class<ItemSword> clazz2;
                if (StringsKt.equals(type, "Sword", true)) {
                    clazz2 = ItemSword.class;
                } else if (StringsKt.equals(type, "Pickaxe", true)) {
                    clazz2 = ItemPickaxe.class;
                } else {
                    if (!StringsKt.equals(type, "Axe", true)) return null;
                    clazz2 = ItemAxe.class;
                }
                Class<ItemSword> clazz3 = clazz2;
                int bestWeapon = 0;
                ItemStack itemStack = slotStack;
                if (itemStack == null) {
                    clazz = null;
                } else {
                    Item item = itemStack.func_77973_b();
                    clazz = item == null ? null : item.getClass();
                }
                bestWeapon = Intrinsics.areEqual(clazz, clazz3) ? targetSlot : -1;
                ItemStack[] itemStackArray = MinecraftInstance.mc.field_71439_g.field_71071_by.field_70462_a;
                Intrinsics.checkNotNullExpressionValue(itemStackArray, "mc.thePlayer.inventory.mainInventory");
                Object[] objectArray = itemStackArray;
                boolean bl = false;
                int index$iv = 0;
                for (Object item$iv : objectArray) {
                    double d;
                    ItemStack bestStack;
                    double d2;
                    void itemStack2;
                    int n = index$iv;
                    index$iv = n + 1;
                    ItemStack itemStack3 = (ItemStack)item$iv;
                    int index = n;
                    boolean bl2 = false;
                    if (itemStack2 == null || !Intrinsics.areEqual(itemStack2.func_77973_b().getClass(), clazz3) || StringsKt.equals(this.type(index), type, true)) continue;
                    if (bestWeapon == -1) {
                        bestWeapon = index;
                        continue;
                    }
                    Collection collection = itemStack2.func_111283_C().get((Object)"generic.attackDamage");
                    Intrinsics.checkNotNullExpressionValue(collection, "itemStack.attributeModif\u2026s[\"generic.attackDamage\"]");
                    AttributeModifier attributeModifier = (AttributeModifier)CollectionsKt.firstOrNull(collection);
                    double currDamage = (attributeModifier == null ? 0.0 : (d2 = attributeModifier.func_111164_d())) + ItemHelper.INSTANCE.getWeaponEnchantFactor((ItemStack)itemStack2, ((Number)this.nbtWeaponPriority.get()).floatValue(), this.getGoal());
                    if (MinecraftInstance.mc.field_71439_g.field_71071_by.func_70301_a(bestWeapon) == null) continue;
                    Collection collection2 = bestStack.func_111283_C().get((Object)"generic.attackDamage");
                    Intrinsics.checkNotNullExpressionValue(collection2, "bestStack.attributeModif\u2026s[\"generic.attackDamage\"]");
                    AttributeModifier attributeModifier2 = (AttributeModifier)CollectionsKt.firstOrNull(collection2);
                    double d3 = attributeModifier2 == null ? 0.0 : (d = attributeModifier2.func_111164_d());
                    double bestDamage = d3 + ItemHelper.INSTANCE.getWeaponEnchantFactor(bestStack, ((Number)this.nbtWeaponPriority.get()).floatValue(), this.getGoal());
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
                int n;
                int n2;
                boolean bl = false;
                ItemStack itemStack = slotStack;
                int n3 = (itemStack == null ? null : itemStack.func_77973_b()) instanceof ItemBow ? targetSlot : -1;
                int bestPower = 0;
                if (n3 != -1) {
                    ItemStack itemStack4 = slotStack;
                    Intrinsics.checkNotNull(itemStack4);
                    Enchantment enchantment = Enchantment.field_77345_t;
                    Intrinsics.checkNotNullExpressionValue(enchantment, "power");
                    n2 = ItemHelper.INSTANCE.getEnchantment(itemStack4, enchantment);
                } else {
                    n2 = 0;
                }
                bestPower = n2;
                ItemStack[] itemStackArray = MinecraftInstance.mc.field_71439_g.field_71071_by.field_70462_a;
                Intrinsics.checkNotNullExpressionValue(itemStackArray, "mc.thePlayer.inventory.mainInventory");
                Object[] objectArray = itemStackArray;
                boolean bl3 = false;
                int index$iv = 0;
                for (Object item$iv2 : objectArray) {
                    int n4 = index$iv;
                    index$iv = n4 + 1;
                    ItemStack itemStack2 = (ItemStack)item$iv2;
                    int index = n4;
                    boolean bl4 = false;
                    ItemStack itemStack5 = itemStack2;
                    if (!((itemStack5 == null ? null : itemStack5.func_77973_b()) instanceof ItemBow) || StringsKt.equals(this.type(index), type, true)) continue;
                    if (n == -1) {
                        n = index;
                        continue;
                    }
                    Intrinsics.checkNotNullExpressionValue(itemStack2, "itemStack");
                    Enchantment enchantment = Enchantment.field_77345_t;
                    Intrinsics.checkNotNullExpressionValue(enchantment, "power");
                    int power = ItemHelper.INSTANCE.getEnchantment(itemStack2, enchantment);
                    enchantment = Enchantment.field_77345_t;
                    Intrinsics.checkNotNullExpressionValue(enchantment, "power");
                    if (ItemHelper.INSTANCE.getEnchantment(itemStack2, enchantment) <= bestPower) continue;
                    n = index;
                    bestPower = power;
                }
                if (n == -1) return null;
                Integer n5 = n;
                return n5;
            }
            case "food": {
                void index;
                void v13;
                Item item;
                ItemStack[] itemStackArray = MinecraftInstance.mc.field_71439_g.field_71071_by.field_70462_a;
                Intrinsics.checkNotNullExpressionValue(itemStackArray, "mc.thePlayer.inventory.mainInventory");
                Object[] objectArray = itemStackArray;
                boolean $i$f$forEachIndexed = false;
                boolean bl = false;
                Object[] objectArray2 = objectArray;
                int n = 0;
                int n6 = objectArray2.length;
                do {
                    void stack;
                    void var7_36;
                    if (n >= n6) return null;
                    Object item$iv = objectArray2[n];
                    ++n;
                    void var12_81 = var7_36;
                    var7_36 = var12_81 + true;
                    ItemStack item$iv2 = (ItemStack)item$iv;
                    index = var12_81;
                    boolean bl5 = false;
                    v13 = stack;
                } while (!((item = v13 == null ? null : v13.func_77973_b()) instanceof ItemFood) || item instanceof ItemAppleGold || StringsKt.equals(this.type((int)index), "Food", true));
                if (slotStack != null) {
                    if (slotStack.func_77973_b() instanceof ItemFood) return null;
                }
                boolean bl6 = true;
                boolean replaceCurr = bl6;
                if (!replaceCurr) return null;
                Integer n7 = (int)index;
                return n7;
            }
            case "block": {
                void index;
                ItemStack itemStack;
                Item item;
                ItemStack[] itemStackArray = MinecraftInstance.mc.field_71439_g.field_71071_by.field_70462_a;
                Intrinsics.checkNotNullExpressionValue(itemStackArray, "mc.thePlayer.inventory.mainInventory");
                Object[] objectArray = itemStackArray;
                boolean $i$f$forEachIndexed = false;
                boolean bl = false;
                Object[] objectArray3 = objectArray;
                int n = 0;
                int n8 = objectArray3.length;
                do {
                    void var7_38;
                    if (n >= n8) return null;
                    Object item$iv = objectArray3[n];
                    ++n;
                    void var12_82 = var7_38;
                    var7_38 = var12_82 + true;
                    ItemStack stack = (ItemStack)item$iv;
                    index = var12_82;
                    boolean bl7 = false;
                    itemStack = stack;
                } while (!((item = itemStack == null ? null : itemStack.func_77973_b()) instanceof ItemBlock) || InventoryHelper.INSTANCE.isBlockListBlock((ItemBlock)item) || StringsKt.equals(this.type((int)index), "Block", true));
                if (slotStack != null) {
                    if (slotStack.func_77973_b() instanceof ItemBlock) return null;
                }
                boolean bl8 = true;
                boolean replaceCurr = bl8;
                if (!replaceCurr) return null;
                Integer n9 = (int)index;
                return n9;
            }
            case "water": {
                void index;
                ItemStack itemStack;
                Item item;
                ItemStack[] itemStackArray = MinecraftInstance.mc.field_71439_g.field_71071_by.field_70462_a;
                Intrinsics.checkNotNullExpressionValue(itemStackArray, "mc.thePlayer.inventory.mainInventory");
                Object[] objectArray = itemStackArray;
                boolean $i$f$forEachIndexed = false;
                boolean bl = false;
                Object[] objectArray4 = objectArray;
                int n = 0;
                int n10 = objectArray4.length;
                do {
                    void var7_40;
                    if (n >= n10) return null;
                    Object item$iv = objectArray4[n];
                    ++n;
                    void var12_83 = var7_40;
                    var7_40 = var12_83 + true;
                    ItemStack stack = (ItemStack)item$iv;
                    index = var12_83;
                    boolean bl9 = false;
                    itemStack = stack;
                } while (!((item = itemStack == null ? null : itemStack.func_77973_b()) instanceof ItemBucket) || !Intrinsics.areEqual(((ItemBucket)item).field_77876_a, Blocks.field_150358_i) || StringsKt.equals(this.type((int)index), "Water", true));
                if (slotStack != null && slotStack.func_77973_b() instanceof ItemBucket) {
                    Item item2 = slotStack.func_77973_b();
                    if (item2 == null) {
                        throw new NullPointerException("null cannot be cast to non-null type net.minecraft.item.ItemBucket");
                    }
                    if (Intrinsics.areEqual(((ItemBucket)item2).field_77876_a, Blocks.field_150358_i)) return null;
                }
                boolean bl10 = true;
                boolean replaceCurr = bl10;
                if (!replaceCurr) return null;
                Integer n11 = (int)index;
                return n11;
            }
            case "gapple": {
                void index;
                ItemStack itemStack;
                Item item;
                ItemStack[] itemStackArray = MinecraftInstance.mc.field_71439_g.field_71071_by.field_70462_a;
                Intrinsics.checkNotNullExpressionValue(itemStackArray, "mc.thePlayer.inventory.mainInventory");
                Object[] objectArray = itemStackArray;
                boolean $i$f$forEachIndexed = false;
                boolean bl = false;
                Object[] objectArray5 = objectArray;
                int n = 0;
                int n12 = objectArray5.length;
                do {
                    void var7_42;
                    if (n >= n12) return null;
                    Object item$iv = objectArray5[n];
                    ++n;
                    void var12_84 = var7_42;
                    var7_42 = var12_84 + true;
                    ItemStack stack = (ItemStack)item$iv;
                    index = var12_84;
                    boolean bl11 = false;
                    itemStack = stack;
                } while (!((item = itemStack == null ? null : itemStack.func_77973_b()) instanceof ItemAppleGold) || StringsKt.equals(this.type((int)index), "Gapple", true));
                if (slotStack != null) {
                    if (slotStack.func_77973_b() instanceof ItemAppleGold) return null;
                }
                boolean bl12 = true;
                boolean replaceCurr = bl12;
                if (!replaceCurr) return null;
                Integer n13 = (int)index;
                return n13;
            }
            case "pearl": {
                void index;
                ItemStack itemStack;
                Item item;
                ItemStack[] itemStackArray = MinecraftInstance.mc.field_71439_g.field_71071_by.field_70462_a;
                Intrinsics.checkNotNullExpressionValue(itemStackArray, "mc.thePlayer.inventory.mainInventory");
                Object[] objectArray = itemStackArray;
                boolean $i$f$forEachIndexed = false;
                boolean bl = false;
                Object[] objectArray6 = objectArray;
                int n = 0;
                int n14 = objectArray6.length;
                do {
                    void var7_44;
                    if (n >= n14) return null;
                    Object item$iv = objectArray6[n];
                    ++n;
                    void var12_85 = var7_44;
                    var7_44 = var12_85 + true;
                    ItemStack stack = (ItemStack)item$iv;
                    index = var12_85;
                    boolean bl13 = false;
                    itemStack = stack;
                } while (!((item = itemStack == null ? null : itemStack.func_77973_b()) instanceof ItemEnderPearl) || StringsKt.equals(this.type((int)index), "Pearl", true));
                if (slotStack != null) {
                    if (slotStack.func_77973_b() instanceof ItemEnderPearl) return null;
                }
                boolean bl14 = true;
                boolean replaceCurr = bl14;
                if (!replaceCurr) return null;
                Integer n15 = (int)index;
                return n15;
            }
            case "potion": {
                void index;
                ItemStack stack;
                ItemStack itemStack;
                Item item;
                ItemStack[] itemStackArray = MinecraftInstance.mc.field_71439_g.field_71071_by.field_70462_a;
                Intrinsics.checkNotNullExpressionValue(itemStackArray, "mc.thePlayer.inventory.mainInventory");
                Object[] objectArray = itemStackArray;
                boolean $i$f$forEachIndexed = false;
                boolean bl = false;
                Object[] objectArray7 = objectArray;
                int n = 0;
                int n16 = objectArray7.length;
                do {
                    void var7_46;
                    if (n >= n16) return null;
                    Object item$iv = objectArray7[n];
                    ++n;
                    void var12_86 = var7_46;
                    var7_46 = var12_86 + true;
                    stack = (ItemStack)item$iv;
                    index = var12_86;
                    boolean bl15 = false;
                    itemStack = stack;
                } while (!((item = itemStack == null ? null : itemStack.func_77973_b()) instanceof ItemPotion) || !ItemPotion.func_77831_g((int)stack.func_77952_i()) || StringsKt.equals(this.type((int)index), "Potion", true));
                if (slotStack != null && slotStack.func_77973_b() instanceof ItemPotion) {
                    if (ItemPotion.func_77831_g((int)slotStack.func_77952_i())) return null;
                }
                boolean bl16 = true;
                boolean replaceCurr = bl16;
                if (!replaceCurr) return null;
                Integer n17 = (int)index;
                return n17;
            }
        }
        return null;
    }

    private final Map<Integer, ItemStack> items(int start, int end) {
        Map items = new LinkedHashMap();
        int n = end - 1;
        if (start <= n) {
            int i;
            do {
                ItemStack itemStack;
                if (MinecraftInstance.mc.field_71439_g.field_71069_bz.func_75139_a(i = n--).func_75211_c() == null || itemStack.func_77973_b() == null) continue;
                if ((36 <= i ? i < 45 : false) && StringsKt.equals(this.type(i), "Ignore", true) || System.currentTimeMillis() - ((IItemStack)itemStack).getItemDelay() < (long)((Number)this.itemDelayValue.get()).intValue()) continue;
                Map map = items;
                Integer n2 = i;
                map.put(n2, itemStack);
            } while (i != start);
        }
        return items;
    }

    static /* synthetic */ Map items$default(InvManager invManager, int n, int n2, int n3, Object object) {
        if ((n3 & 1) != 0) {
            n = 0;
        }
        if ((n3 & 2) != 0) {
            n2 = 45;
        }
        return invManager.items(n, n2);
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

    private static final boolean findBestArmor$lambda-2(InvManager this$0, int i) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        ItemStack itemStack = MinecraftInstance.mc.field_71439_g.field_71071_by.func_70301_a(i);
        return itemStack != null && itemStack.func_77973_b() instanceof ItemArmor && (i < 9 || System.currentTimeMillis() - ((IItemStack)itemStack).getItemDelay() >= (long)((Number)this$0.itemDelayValue.get()).intValue());
    }

    private static final ArmorPart findBestArmor$lambda-3(int i) {
        ItemStack itemStack = MinecraftInstance.mc.field_71439_g.field_71071_by.func_70301_a(i);
        Intrinsics.checkNotNullExpressionValue(itemStack, "mc.thePlayer.inventory.getStackInSlot(i)");
        return new ArmorPart(itemStack, i);
    }

    private static final Integer findBestArmor$lambda-4(ArmorPart obj) {
        Intrinsics.checkNotNullParameter(obj, "obj");
        return obj.getArmorType();
    }

    private static final int findBestArmor$lambda-6$lambda-5(InvManager this$0, ArmorPart ArmorPart2, ArmorPart ArmorPart22) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullExpressionValue(ArmorPart2, "ArmorPart");
        Intrinsics.checkNotNullExpressionValue(ArmorPart22, "ArmorPart2");
        return ItemHelper.INSTANCE.compareArmor(ArmorPart2, ArmorPart22, ((Number)this$0.nbtArmorPriority.get()).floatValue(), this$0.getGoal());
    }

    public static final /* synthetic */ IntegerValue access$getMinDelayValue$p(InvManager $this) {
        return $this.minDelayValue;
    }

    public static final /* synthetic */ IntegerValue access$getMaxDelayValue$p(InvManager $this) {
        return $this.maxDelayValue;
    }

    public static final /* synthetic */ BoolValue access$getInvSpoof$p(InvManager $this) {
        return $this.invSpoof;
    }

    public static final /* synthetic */ ListValue access$getNbtGoalValue$p(InvManager $this) {
        return $this.nbtGoalValue;
    }

    public static final /* synthetic */ BoolValue access$getSortValue$p(InvManager $this) {
        return $this.sortValue;
    }
}

