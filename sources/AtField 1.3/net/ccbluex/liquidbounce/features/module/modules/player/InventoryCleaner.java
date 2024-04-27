/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.collections.CollectionsKt
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 */
package net.ccbluex.liquidbounce.features.module.modules.player;

import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.IClassProvider;
import net.ccbluex.liquidbounce.api.enums.BlockType;
import net.ccbluex.liquidbounce.api.enums.EnchantmentType;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.network.IINetHandlerPlayClient;
import net.ccbluex.liquidbounce.api.minecraft.entity.ai.attributes.IAttributeModifier;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketEntityAction;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.player.InventoryCleaner;
import net.ccbluex.liquidbounce.injection.backend.WrapperImpl;
import net.ccbluex.liquidbounce.utils.InventoryUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.item.ItemUtils;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;

@ModuleInfo(name="InventoryCleaner", description="Automatically throws away useless items.", category=ModuleCategory.PLAYER)
public final class InventoryCleaner
extends Module {
    private final IntegerValue minDelayValue;
    private final BoolValue noMoveValue;
    private long delay;
    private final BoolValue randomSlotValue;
    private final String[] items;
    private final IntegerValue itemDelayValue;
    private final ListValue sortSlot3Value;
    private final ListValue sortSlot4Value;
    private final BoolValue hotbarValue;
    private final ListValue sortSlot5Value;
    private final ListValue sortSlot9Value;
    private final BoolValue invOpenValue;
    private final BoolValue sortValue;
    private final ListValue sortSlot1Value;
    private final ListValue sortSlot8Value;
    private final BoolValue simulateInventory;
    private final ListValue sortSlot7Value;
    private final ListValue sortSlot2Value;
    private final IntegerValue maxDelayValue = new IntegerValue(this, "MaxDelay", 600, 0, 1000){
        final InventoryCleaner this$0;
        {
            this.this$0 = inventoryCleaner;
            super(string, n, n2, n3);
        }

        public void onChanged(Object object, Object object2) {
            this.onChanged(((Number)object).intValue(), ((Number)object2).intValue());
        }

        static {
        }

        protected void onChanged(int n, int n2) {
            int n3 = ((Number)InventoryCleaner.access$getMinDelayValue$p(this.this$0).get()).intValue();
            if (n3 > n2) {
                this.set((Object)n3);
            }
        }
    };
    private final ListValue sortSlot6Value;
    private final BoolValue ignoreVehiclesValue;

    public static final IntegerValue access$getMaxDelayValue$p(InventoryCleaner inventoryCleaner) {
        return inventoryCleaner.maxDelayValue;
    }

    public static final IntegerValue access$getMinDelayValue$p(InventoryCleaner inventoryCleaner) {
        return inventoryCleaner.minDelayValue;
    }

    private final Map items(int n, int n2) {
        int n3 = 0;
        Map map = new LinkedHashMap();
        n3 = n2 - 1;
        int n4 = n;
        if (n3 >= n4) {
            while (true) {
                Object object;
                if ((object = MinecraftInstance.mc.getThePlayer()) == null || (object = object.getInventoryContainer()) == null || (object = object.getSlot(n3)) == null || (object = object.getStack()) == null) {
                } else {
                    int n5;
                    Object object2 = object;
                    if (!(ItemUtils.isStackEmpty((IItemStack)object2) || 36 <= (n5 = n3) && 44 >= n5 && StringsKt.equals((String)this.type(n3), (String)"Ignore", (boolean)true) || System.currentTimeMillis() - object2.getItemDelay() < ((Number)this.itemDelayValue.get()).longValue())) {
                        map.put(n3, object2);
                    }
                }
                if (n3 == n4) break;
                --n3;
            }
        }
        return map;
    }

    private final String type(int n) {
        String string;
        switch (n) {
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

    public InventoryCleaner() {
        this.minDelayValue = new IntegerValue(this, "MinDelay", 400, 0, 1000){
            final InventoryCleaner this$0;

            protected void onChanged(int n, int n2) {
                int n3 = ((Number)InventoryCleaner.access$getMaxDelayValue$p(this.this$0).get()).intValue();
                if (n3 < n2) {
                    this.set((Object)n3);
                }
            }

            public void onChanged(Object object, Object object2) {
                this.onChanged(((Number)object).intValue(), ((Number)object2).intValue());
            }

            static {
            }
            {
                this.this$0 = inventoryCleaner;
                super(string, n, n2, n3);
            }
        };
        this.invOpenValue = new BoolValue("InvOpen", false);
        this.simulateInventory = new BoolValue("SimulateInventory", true);
        this.noMoveValue = new BoolValue("NoMove", false);
        this.ignoreVehiclesValue = new BoolValue("IgnoreVehicles", false);
        this.hotbarValue = new BoolValue("Hotbar", true);
        this.randomSlotValue = new BoolValue("RandomSlot", false);
        this.sortValue = new BoolValue("Sort", true);
        this.itemDelayValue = new IntegerValue("ItemDelay", 0, 0, 5000);
        this.items = new String[]{"None", "Ignore", "Sword", "Bow", "Pickaxe", "Axe", "Food", "Block", "Water", "Gapple", "Pearl"};
        this.sortSlot1Value = new ListValue("SortSlot-1", this.items, "Sword");
        this.sortSlot2Value = new ListValue("SortSlot-2", this.items, "Bow");
        this.sortSlot3Value = new ListValue("SortSlot-3", this.items, "Pickaxe");
        this.sortSlot4Value = new ListValue("SortSlot-4", this.items, "Axe");
        this.sortSlot5Value = new ListValue("SortSlot-5", this.items, "None");
        this.sortSlot6Value = new ListValue("SortSlot-6", this.items, "None");
        this.sortSlot7Value = new ListValue("SortSlot-7", this.items, "Food");
        this.sortSlot8Value = new ListValue("SortSlot-8", this.items, "Block");
        this.sortSlot9Value = new ListValue("SortSlot-9", this.items, "Block");
    }

    /*
     * Unable to fully structure code
     */
    private final Integer findBetterItem(int var1_1, IItemStack var2_2) {
        var3_3 = this.type(var1_1);
        v0 = MinecraftInstance.mc.getThePlayer();
        if (v0 == null) {
            return null;
        }
        var4_4 = v0;
        var5_5 = var3_3;
        var6_6 = 0;
        v1 = var5_5;
        if (v1 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        var5_5 = v1.toLowerCase();
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
                if (StringsKt.equals((String)var3_3, (String)"Sword", (boolean)true)) {
                    v2 = findBetterItem.currentTypeChecker.1.INSTANCE;
                } else if (StringsKt.equals((String)var3_3, (String)"Pickaxe", (boolean)true)) {
                    v2 = findBetterItem.currentTypeChecker.2.INSTANCE;
                } else if (StringsKt.equals((String)var3_3, (String)"Axe", (boolean)true)) {
                    v2 = findBetterItem.currentTypeChecker.3.INSTANCE;
                } else {
                    return null;
                }
                var6_7 = v2;
                v3 = var2_2;
                var7_13 = (Boolean)var6_7.invoke((Object)(v3 != null ? v3.getItem() : null)) != false ? var1_1 : -1;
                var8_20 = var4_4.getInventory().getMainInventory();
                var9_27 = false;
                var10_34 = 0;
                for (T var12_48 : var8_20) {
                    var13_55 = var10_34++;
                    var14_62 = false;
                    if (var13_55 < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    var15_69 = var13_55;
                    var16_76 = (IItemStack)var12_48;
                    var17_83 = var15_69;
                    var18_90 = false;
                    if (var16_76 == null || !((Boolean)var6_7.invoke((Object)var16_76.getItem())).booleanValue() || StringsKt.equals((String)this.type(var17_83), (String)var3_3, (boolean)true)) continue;
                    if (var7_13 == -1) {
                        var7_13 = var17_83;
                        continue;
                    }
                    v4 = (IAttributeModifier)CollectionsKt.firstOrNull((Iterable)var16_76.getAttributeModifier("generic.attackDamage"));
                    var19_97 = (v4 != null ? v4.getAmount() : 0.0) + 1.25 * (double)ItemUtils.getEnchantment(var16_76, MinecraftInstance.classProvider.getEnchantmentEnum(EnchantmentType.SHARPNESS));
                    if (var4_4.getInventory().getStackInSlot(var7_13) == null) {
                        continue;
                    }
                    v5 = (IAttributeModifier)CollectionsKt.firstOrNull((Iterable)var21_99.getAttributeModifier("generic.attackDamage"));
                    v6 = v5 != null ? v5.getAmount() : 0.0;
                    var22_100 = v6 + 1.25 * (double)ItemUtils.getEnchantment(var21_99, MinecraftInstance.classProvider.getEnchantmentEnum(EnchantmentType.SHARPNESS));
                    if (!(var22_100 < var19_97)) continue;
                    var7_13 = var17_83;
                }
                return var7_13 != -1 || var7_13 == var1_1 ? Integer.valueOf(var7_13) : null;
            }
            case 3: {
                v7 = var2_2;
                var6_6 = MinecraftInstance.classProvider.isItemBow(v7 != null ? v7.getItem() : null) != false ? var1_1 : -1;
                var7_14 = var6_6 != -1 ? ItemUtils.getEnchantment(var2_2, MinecraftInstance.classProvider.getEnchantmentEnum(EnchantmentType.POWER)) : 0;
                var8_21 = var4_4.getInventory().getMainInventory();
                var9_28 = false;
                var10_35 = 0;
                for (T var12_49 : var8_21) {
                    var13_56 = var10_35++;
                    var14_63 = false;
                    if (var13_56 < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    var15_70 = var13_56;
                    var16_77 = (IItemStack)var12_49;
                    var17_84 = var15_70;
                    var18_91 = false;
                    v8 = var16_77;
                    if (!MinecraftInstance.classProvider.isItemBow(v8 != null ? v8.getItem() : null) || StringsKt.equals((String)this.type(var17_84), (String)var3_3, (boolean)true)) continue;
                    if (var6_6 == -1) {
                        var6_6 = var17_84;
                        continue;
                    }
                    var19_98 = ItemUtils.getEnchantment(var16_77, MinecraftInstance.classProvider.getEnchantmentEnum(EnchantmentType.POWER));
                    if (ItemUtils.getEnchantment(var16_77, MinecraftInstance.classProvider.getEnchantmentEnum(EnchantmentType.POWER)) <= var7_14) continue;
                    var6_6 = var17_84;
                    var7_14 = var19_98;
                }
                return var6_6 != -1 ? Integer.valueOf(var6_6) : null;
            }
            case 6: {
                var6_8 = var4_4.getInventory().getMainInventory();
                var7_15 = false;
                var8_22 = 0;
                for (T var10_36 : var6_8) {
                    var11_43 = var8_22++;
                    var12_50 = false;
                    if (var11_43 < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    var13_57 = var11_43;
                    var14_64 = (IItemStack)var10_36;
                    var15_71 = var13_57;
                    var16_78 = false;
                    if (var14_64 == null || !MinecraftInstance.classProvider.isItemFood(var17_85 = var14_64.getItem()) || MinecraftInstance.classProvider.isItemAppleGold(var17_85) || StringsKt.equals((String)this.type(var15_71), (String)"Food", (boolean)true)) continue;
                    var18_92 = ItemUtils.isStackEmpty(var2_2) != false || MinecraftInstance.classProvider.isItemFood(var17_85) == false;
                    return var18_92 != false ? Integer.valueOf(var15_71) : null;
                }
                break;
            }
            case 2: {
                var6_9 = var4_4.getInventory().getMainInventory();
                var7_16 = false;
                var8_23 = 0;
                for (T var10_37 : var6_9) {
                    var11_44 = var8_23++;
                    var12_51 = false;
                    if (var11_44 < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    var13_58 = var11_44;
                    var14_65 = (IItemStack)var10_37;
                    var15_72 = var13_58;
                    var16_79 = false;
                    if (var14_65 == null) continue;
                    if (var14_65.getItem() == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!MinecraftInstance.classProvider.isItemBlock(var17_86) || InventoryUtils.BLOCK_BLACKLIST.contains(var17_86.asItemBlock().getBlock()) || StringsKt.equals((String)this.type(var15_72), (String)"Block", (boolean)true)) continue;
                    var18_93 = ItemUtils.isStackEmpty(var2_2) != false || MinecraftInstance.classProvider.isItemBlock(var17_86) == false;
                    return var18_93 != false ? Integer.valueOf(var15_72) : null;
                }
                break;
            }
            case 4: {
                var6_10 = var4_4.getInventory().getMainInventory();
                var7_17 = false;
                var8_24 = 0;
                for (T var10_38 : var6_10) {
                    var11_45 = var8_24++;
                    var12_52 = false;
                    if (var11_45 < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    var13_59 = var11_45;
                    var14_66 = (IItemStack)var10_38;
                    var15_73 = var13_59;
                    var16_80 = false;
                    if (var14_66 == null) continue;
                    if (var14_66.getItem() == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!MinecraftInstance.classProvider.isItemBucket(var17_87) || !var17_87.asItemBucket().isFull().equals(MinecraftInstance.classProvider.getBlockEnum(BlockType.FLOWING_WATER)) || StringsKt.equals((String)this.type(var15_73), (String)"Water", (boolean)true)) continue;
                    var18_94 = ItemUtils.isStackEmpty(var2_2) != false || MinecraftInstance.classProvider.isItemBucket(var17_87) == false || (var17_87.asItemBucket().isFull().equals(MinecraftInstance.classProvider.getBlockEnum(BlockType.FLOWING_WATER)) ^ true) != false;
                    return var18_94 != false ? Integer.valueOf(var15_73) : null;
                }
                break;
            }
            case 5: {
                var6_11 = var4_4.getInventory().getMainInventory();
                var7_18 = false;
                var8_25 = 0;
                for (T var10_39 : var6_11) {
                    var11_46 = var8_25++;
                    var12_53 = false;
                    if (var11_46 < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    var13_60 = var11_46;
                    var14_67 = (IItemStack)var10_39;
                    var15_74 = var13_60;
                    var16_81 = false;
                    if (var14_67 == null) continue;
                    if (var14_67.getItem() == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!MinecraftInstance.classProvider.isItemAppleGold(var17_88) || StringsKt.equals((String)this.type(var15_74), (String)"Gapple", (boolean)true)) continue;
                    if (ItemUtils.isStackEmpty(var2_2)) ** GOTO lbl-1000
                    v9 = var2_2;
                    if (!MinecraftInstance.classProvider.isItemAppleGold(v9 != null ? v9.getItem() : null)) lbl-1000:
                    // 2 sources

                    {
                        v10 = true;
                    } else {
                        v10 = false;
                    }
                    var18_95 = v10;
                    return var18_95 != false ? Integer.valueOf(var15_74) : null;
                }
                break;
            }
            case 7: {
                var6_12 = var4_4.getInventory().getMainInventory();
                var7_19 = false;
                var8_26 = 0;
                for (T var10_40 : var6_12) {
                    var11_47 = var8_26++;
                    var12_54 = false;
                    if (var11_47 < 0) {
                        CollectionsKt.throwIndexOverflow();
                    }
                    var13_61 = var11_47;
                    var14_68 = (IItemStack)var10_40;
                    var15_75 = var13_61;
                    var16_82 = false;
                    if (var14_68 == null || !MinecraftInstance.classProvider.isItemEnderPearl(var17_89 = var14_68.getItem()) || StringsKt.equals((String)this.type(var15_75), (String)"Pearl", (boolean)true)) continue;
                    if (ItemUtils.isStackEmpty(var2_2)) ** GOTO lbl-1000
                    v11 = var2_2;
                    if (!MinecraftInstance.classProvider.isItemEnderPearl(v11 != null ? v11.getItem() : null)) lbl-1000:
                    // 2 sources

                    {
                        v12 = true;
                    } else {
                        v12 = false;
                    }
                    var18_96 = v12;
                    return var18_96 != false ? Integer.valueOf(var15_75) : null;
                }
                break;
            }
        }
        return null;
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

    /*
     * Exception decompiling
     */
    @EventTarget
    public final void onUpdate(UpdateEvent var1) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Invalid stack depths @ lbl103 : ALOAD - null : trying to set 2 previously set to 0
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:207)
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

    private final void sortHotbar() {
        int n = 8;
        for (int i = 0; i <= n; ++i) {
            boolean bl;
            IEntityPlayerSP iEntityPlayerSP;
            if (MinecraftInstance.mc.getThePlayer() == null) {
                return;
            }
            Integer n2 = this.findBetterItem(i, iEntityPlayerSP.getInventory().getStackInSlot(i));
            if (n2 == null) {
                continue;
            }
            int n3 = n2;
            if (n3 == i) continue;
            boolean bl2 = bl = !MinecraftInstance.classProvider.isGuiInventory(MinecraftInstance.mc.getCurrentScreen()) && (Boolean)this.simulateInventory.get() != false;
            if (bl) {
                IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                boolean bl3 = false;
                IClassProvider iClassProvider = WrapperImpl.INSTANCE.getClassProvider();
                IEntityPlayerSP iEntityPlayerSP2 = LiquidBounce.INSTANCE.getWrapper().getMinecraft().getThePlayer();
                if (iEntityPlayerSP2 == null) {
                    Intrinsics.throwNpe();
                }
                IPacket iPacket = iClassProvider.createCPacketEntityAction(iEntityPlayerSP2, ICPacketEntityAction.WAction.OPEN_INVENTORY);
                iINetHandlerPlayClient.addToSendQueue(iPacket);
            }
            MinecraftInstance.mc.getPlayerController().windowClick(0, n3 < 9 ? n3 + 36 : n3, i, 2, iEntityPlayerSP);
            if (bl) {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketCloseWindow());
            }
            this.delay = TimeUtils.randomDelay(((Number)this.minDelayValue.get()).intValue(), ((Number)this.maxDelayValue.get()).intValue());
            break;
        }
    }
}

