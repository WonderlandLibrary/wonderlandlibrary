/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemPotion
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
 *  net.minecraft.network.play.client.C09PacketHeldItemChange
 *  net.minecraft.potion.Potion
 *  net.minecraft.potion.PotionEffect
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module.modules.combat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.Client;
import net.dev.important.event.EventState;
import net.dev.important.event.EventTarget;
import net.dev.important.event.MotionEvent;
import net.dev.important.event.WorldEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.modules.module.modules.combat.KillAura;
import net.dev.important.modules.module.modules.world.Scaffold;
import net.dev.important.utils.ClientUtils;
import net.dev.important.utils.InventoryHelper;
import net.dev.important.utils.InventoryUtils;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.timer.MSTimer;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

@Info(name="AutoPot", spacedName="Auto Pot", description="Automatically throw pots for you.", category=Category.COMBAT, cnName="\u81ea\u52a8\u55b7\u836f")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000~\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020\u001bH\u0002J\u0018\u0010'\u001a\u00020\u00102\u0006\u0010(\u001a\u00020\u00102\u0006\u0010)\u001a\u00020\u0010H\u0002J\u0010\u0010*\u001a\u00020\u00142\u0006\u0010+\u001a\u00020\u0010H\u0002J\u0018\u0010,\u001a\n\u0012\u0004\u0012\u00020.\u0018\u00010-2\u0006\u0010+\u001a\u00020\u0010H\u0002J\u0010\u0010/\u001a\u00020\u00142\u0006\u00100\u001a\u00020\u0010H\u0002J\b\u00101\u001a\u00020%H\u0016J\b\u00102\u001a\u00020%H\u0016J\u0010\u00103\u001a\u00020%2\u0006\u00104\u001a\u000205H\u0007J\u0010\u00106\u001a\u00020%2\u0006\u00104\u001a\u000205H\u0007J\u0010\u00107\u001a\u00020%2\u0006\u00104\u001a\u000208H\u0007J\b\u00109\u001a\u00020%H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u001a\u001a\u00020\u001b8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001c\u0010\u001dR\u001e\u0010\u001e\u001a\u0012\u0012\u0004\u0012\u00020\u00100\u001fj\b\u0012\u0004\u0012\u00020\u0010` X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020\u0014X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006:"}, d2={"Lnet/dev/important/modules/module/modules/combat/AutoPot;", "Lnet/dev/important/modules/module/Module;", "()V", "debugValue", "Lnet/dev/important/value/BoolValue;", "delayValue", "Lnet/dev/important/value/IntegerValue;", "healthValue", "Lnet/dev/important/value/FloatValue;", "invTimer", "Lnet/dev/important/utils/timer/MSTimer;", "killAura", "Lnet/dev/important/modules/module/modules/combat/KillAura;", "modeValue", "Lnet/dev/important/value/ListValue;", "potIndex", "", "regenValue", "resetTimer", "rotated", "", "scaffold", "Lnet/dev/important/modules/module/modules/world/Scaffold;", "smartValue", "spoofDelayValue", "spoofInvValue", "tag", "", "getTag", "()Ljava/lang/String;", "throwQueue", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "throwTimer", "throwing", "utilityValue", "debug", "", "s", "findPotion", "startSlot", "endSlot", "findSinglePotion", "slot", "getPotionFromSlot", "", "Lnet/minecraft/potion/PotionEffect;", "isUsefulPotion", "id", "onEnable", "onInitialize", "onMotion", "event", "Lnet/dev/important/event/MotionEvent;", "onMotionPost", "onWorld", "Lnet/dev/important/event/WorldEvent;", "resetAll", "LiquidBounce"})
public final class AutoPot
extends Module {
    @NotNull
    private final ListValue modeValue;
    @NotNull
    private final FloatValue healthValue;
    @NotNull
    private final IntegerValue delayValue;
    @NotNull
    private final BoolValue regenValue;
    @NotNull
    private final BoolValue utilityValue;
    @NotNull
    private final BoolValue smartValue;
    @NotNull
    private final BoolValue spoofInvValue;
    @NotNull
    private final IntegerValue spoofDelayValue;
    @NotNull
    private final BoolValue debugValue;
    private boolean throwing;
    private boolean rotated;
    private int potIndex;
    @NotNull
    private MSTimer throwTimer;
    @NotNull
    private MSTimer resetTimer;
    @NotNull
    private MSTimer invTimer;
    @NotNull
    private final ArrayList<Integer> throwQueue;
    private KillAura killAura;
    private Scaffold scaffold;

    public AutoPot() {
        String[] stringArray = new String[]{"Jump", "Floor"};
        this.modeValue = new ListValue("Mode", stringArray, "Floor");
        this.healthValue = new FloatValue("Health", 75.0f, 0.0f, 100.0f, "%");
        this.delayValue = new IntegerValue("Delay", 500, 500, 5000, "ms");
        this.regenValue = new BoolValue("Heal", true);
        this.utilityValue = new BoolValue("Utility", true);
        this.smartValue = new BoolValue("Smart", true);
        this.spoofInvValue = new BoolValue("InvSpoof", false);
        this.spoofDelayValue = new IntegerValue("InvDelay", 500, 500, 5000, "ms", new Function0<Boolean>(this){
            final /* synthetic */ AutoPot this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return (Boolean)AutoPot.access$getSpoofInvValue$p(this.this$0).get();
            }
        });
        this.debugValue = new BoolValue("Debug", false);
        this.potIndex = -1;
        this.throwTimer = new MSTimer();
        this.resetTimer = new MSTimer();
        this.invTimer = new MSTimer();
        this.throwQueue = new ArrayList();
    }

    @Override
    public void onInitialize() {
        Module module2 = Client.INSTANCE.getModuleManager().getModule(KillAura.class);
        Intrinsics.checkNotNull(module2);
        this.killAura = (KillAura)module2;
        Module module3 = Client.INSTANCE.getModuleManager().getModule(Scaffold.class);
        Intrinsics.checkNotNull(module3);
        this.scaffold = (Scaffold)module3;
    }

    private final void resetAll() {
        this.throwing = false;
        this.rotated = false;
        this.throwTimer.reset();
        this.resetTimer.reset();
        this.invTimer.reset();
        this.throwQueue.clear();
    }

    @Override
    public void onEnable() {
        this.resetAll();
    }

    @EventTarget
    public final void onWorld(@NotNull WorldEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        this.resetAll();
    }

    private final void debug(String s) {
        if (((Boolean)this.debugValue.get()).booleanValue()) {
            ClientUtils.displayChatMessage(Intrinsics.stringPlus("[AutoPot] ", s));
        }
    }

    @EventTarget(priority=2)
    public final void onMotion(@NotNull MotionEvent event) {
        block18: {
            Scaffold scaffold;
            block19: {
                int potion;
                int n;
                Intrinsics.checkNotNullParameter(event, "event");
                if (event.getEventState() != EventState.PRE) break block18;
                if (((Boolean)this.smartValue.get()).booleanValue() && !this.throwQueue.isEmpty() && 0 <= (n = this.throwQueue.size() + -1)) {
                    do {
                        int k = n--;
                        EntityPlayerSP entityPlayerSP = MinecraftInstance.mc.field_71439_g;
                        Integer n2 = this.throwQueue.get(k);
                        Intrinsics.checkNotNullExpressionValue(n2, "throwQueue[k]");
                        if (!entityPlayerSP.func_82165_m(((Number)n2).intValue())) continue;
                        this.throwQueue.remove(k);
                    } while (0 <= n);
                }
                if (((Boolean)this.spoofInvValue.get()).booleanValue() && !(MinecraftInstance.mc.field_71462_r instanceof GuiContainer) && !this.throwing) {
                    int invPotion;
                    if (this.invTimer.hasTimePassed(((Number)this.spoofDelayValue.get()).intValue()) && (invPotion = this.findPotion(9, 36)) != -1) {
                        if (InventoryUtils.hasSpaceHotbar()) {
                            InventoryHelper.INSTANCE.openPacket();
                            MinecraftInstance.mc.field_71442_b.func_78753_a(0, invPotion, 0, 1, (EntityPlayer)MinecraftInstance.mc.field_71439_g);
                            InventoryHelper.INSTANCE.closePacket();
                        } else {
                            int n3 = 36;
                            while (n3 < 45) {
                                int i;
                                ItemStack stack;
                                if ((stack = MinecraftInstance.mc.field_71439_g.field_71069_bz.func_75139_a(i = n3++).func_75211_c()) == null || !(stack.func_77973_b() instanceof ItemPotion) || !ItemPotion.func_77831_g((int)stack.func_77952_i())) continue;
                                InventoryHelper.INSTANCE.openPacket();
                                MinecraftInstance.mc.field_71442_b.func_78753_a(0, invPotion, 0, 0, (EntityPlayer)MinecraftInstance.mc.field_71439_g);
                                MinecraftInstance.mc.field_71442_b.func_78753_a(0, i, 0, 0, (EntityPlayer)MinecraftInstance.mc.field_71439_g);
                                InventoryHelper.INSTANCE.closePacket();
                                break;
                            }
                        }
                        this.invTimer.reset();
                        this.debug("moved pot");
                        return;
                    }
                } else {
                    this.invTimer.reset();
                }
                if (!(MinecraftInstance.mc.field_71462_r instanceof GuiContainer) && !this.throwing && this.throwTimer.hasTimePassed(((Number)this.delayValue.get()).intValue()) && (potion = this.findPotion(36, 45)) != -1) {
                    this.potIndex = potion;
                    this.throwing = true;
                    this.debug("found pot, queueing");
                }
                if (!this.throwing || MinecraftInstance.mc.field_71462_r instanceof GuiContainer) break block18;
                KillAura killAura = this.killAura;
                if (killAura == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("killAura");
                    killAura = null;
                }
                if (!killAura.getState()) break block19;
                KillAura killAura2 = this.killAura;
                if (killAura2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("killAura");
                    killAura2 = null;
                }
                if (killAura2.getTarget() != null) break block18;
            }
            if ((scaffold = this.scaffold) == null) {
                Intrinsics.throwUninitializedPropertyAccessException("scaffold");
                scaffold = null;
            }
            if (!scaffold.getState()) {
                if (MinecraftInstance.mc.field_71439_g.field_70122_E && StringsKt.equals((String)this.modeValue.get(), "jump", true)) {
                    MinecraftInstance.mc.field_71439_g.func_70664_aZ();
                    this.debug("jumped");
                }
                event.setPitch(90.0f);
                this.debug("silent rotation");
            }
        }
    }

    /*
     * WARNING - void declaration
     */
    @EventTarget(priority=-1)
    public final void onMotionPost(@NotNull MotionEvent event) {
        block13: {
            Scaffold scaffold;
            block14: {
                Intrinsics.checkNotNullParameter(event, "event");
                if (event.getEventState() != EventState.POST || !this.throwing || MinecraftInstance.mc.field_71462_r instanceof GuiContainer || (!MinecraftInstance.mc.field_71439_g.field_70122_E || !StringsKt.equals((String)this.modeValue.get(), "floor", true)) && (MinecraftInstance.mc.field_71439_g.field_70122_E || !StringsKt.equals((String)this.modeValue.get(), "jump", true))) break block13;
                KillAura killAura = this.killAura;
                if (killAura == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("killAura");
                    killAura = null;
                }
                if (!killAura.getState()) break block14;
                KillAura killAura2 = this.killAura;
                if (killAura2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("killAura");
                    killAura2 = null;
                }
                if (killAura2.getTarget() != null) break block13;
            }
            if ((scaffold = this.scaffold) == null) {
                Intrinsics.throwUninitializedPropertyAccessException("scaffold");
                scaffold = null;
            }
            if (!scaffold.getState()) {
                List<PotionEffect> potionEffects = this.getPotionFromSlot(this.potIndex);
                if (potionEffects != null) {
                    Iterable $this$mapTo$iv$iv;
                    Iterable $this$map$iv = potionEffects;
                    boolean $i$f$map = false;
                    Iterable iterable = $this$map$iv;
                    Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
                    boolean $i$f$mapTo = false;
                    for (Object item$iv$iv : $this$mapTo$iv$iv) {
                        void it;
                        PotionEffect potionEffect = (PotionEffect)item$iv$iv;
                        Collection collection = destination$iv$iv;
                        boolean bl = false;
                        collection.add(it.func_76456_a());
                    }
                    List potionIds = (List)destination$iv$iv;
                    if (((Boolean)this.smartValue.get()).booleanValue()) {
                        void $this$forEach$iv;
                        void $this$filterTo$iv$iv;
                        Iterable $this$filter$iv = potionIds;
                        boolean $i$f$filter = false;
                        $this$mapTo$iv$iv = $this$filter$iv;
                        destination$iv$iv = new ArrayList();
                        boolean $i$f$filterTo = false;
                        for (Object element$iv$iv : $this$filterTo$iv$iv) {
                            int it = ((Number)element$iv$iv).intValue();
                            boolean bl = false;
                            if (!(!this.throwQueue.contains(it))) continue;
                            destination$iv$iv.add(element$iv$iv);
                        }
                        $this$filter$iv = (List)destination$iv$iv;
                        boolean $i$f$forEach = false;
                        for (Object element$iv : $this$forEach$iv) {
                            int it = ((Number)element$iv).intValue();
                            boolean bl = false;
                            this.throwQueue.add(it);
                        }
                    }
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(this.potIndex - 36));
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C08PacketPlayerBlockPlacement(MinecraftInstance.mc.field_71439_g.func_70694_bm()));
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(MinecraftInstance.mc.field_71439_g.field_71071_by.field_70461_c));
                    this.potIndex = -1;
                    this.throwing = false;
                    this.throwTimer.reset();
                    this.debug("thrown");
                } else {
                    this.potIndex = -1;
                    this.throwing = false;
                    this.debug("failed to retrieve potion info, retrying...");
                }
            }
        }
    }

    private final int findPotion(int startSlot, int endSlot) {
        int n = startSlot;
        while (n < endSlot) {
            int i;
            if (!this.findSinglePotion(i = n++)) continue;
            return i;
        }
        return -1;
    }

    private final List<PotionEffect> getPotionFromSlot(int slot) {
        ItemStack stack = MinecraftInstance.mc.field_71439_g.field_71069_bz.func_75139_a(slot).func_75211_c();
        if (stack == null || !(stack.func_77973_b() instanceof ItemPotion) || !ItemPotion.func_77831_g((int)stack.func_77952_i())) {
            return null;
        }
        Item item = stack.func_77973_b();
        if (item == null) {
            throw new NullPointerException("null cannot be cast to non-null type net.minecraft.item.ItemPotion");
        }
        ItemPotion itemPotion = (ItemPotion)item;
        return itemPotion.func_77832_l(stack);
    }

    private final boolean findSinglePotion(int slot) {
        block7: {
            ItemPotion itemPotion;
            ItemStack stack;
            block6: {
                stack = MinecraftInstance.mc.field_71439_g.field_71069_bz.func_75139_a(slot).func_75211_c();
                if (stack == null || !(stack.func_77973_b() instanceof ItemPotion) || !ItemPotion.func_77831_g((int)stack.func_77952_i())) {
                    return false;
                }
                Item item = stack.func_77973_b();
                if (item == null) {
                    throw new NullPointerException("null cannot be cast to non-null type net.minecraft.item.ItemPotion");
                }
                itemPotion = (ItemPotion)item;
                if (!(MinecraftInstance.mc.field_71439_g.func_110143_aJ() / MinecraftInstance.mc.field_71439_g.func_110138_aP() * 100.0f < ((Number)this.healthValue.get()).floatValue()) || !((Boolean)this.regenValue.get()).booleanValue()) break block6;
                for (PotionEffect potionEffect : itemPotion.func_77832_l(stack)) {
                    if (potionEffect.func_76456_a() != Potion.field_76432_h.field_76415_H) continue;
                    return true;
                }
                if (MinecraftInstance.mc.field_71439_g.func_70644_a(Potion.field_76428_l) || ((Boolean)this.smartValue.get()).booleanValue() && this.throwQueue.contains(Potion.field_76428_l.field_76415_H)) break block7;
                for (PotionEffect potionEffect : itemPotion.func_77832_l(stack)) {
                    if (potionEffect.func_76456_a() != Potion.field_76428_l.field_76415_H) continue;
                    return true;
                }
                break block7;
            }
            if (((Boolean)this.utilityValue.get()).booleanValue()) {
                for (PotionEffect potionEffect : itemPotion.func_77832_l(stack)) {
                    if (!this.isUsefulPotion(potionEffect.func_76456_a())) continue;
                    return true;
                }
            }
        }
        return false;
    }

    private final boolean isUsefulPotion(int id) {
        if (id == Potion.field_76428_l.field_76415_H || id == Potion.field_76432_h.field_76415_H || id == Potion.field_76436_u.field_76415_H || id == Potion.field_76440_q.field_76415_H || id == Potion.field_76433_i.field_76415_H || id == Potion.field_82731_v.field_76415_H || id == Potion.field_76419_f.field_76415_H || id == Potion.field_76421_d.field_76415_H || id == Potion.field_76437_t.field_76415_H) {
            return false;
        }
        return !MinecraftInstance.mc.field_71439_g.func_82165_m(id) && ((Boolean)this.smartValue.get() == false || !this.throwQueue.contains(id));
    }

    @Override
    @NotNull
    public String getTag() {
        return String.valueOf(this.modeValue.get());
    }

    public static final /* synthetic */ BoolValue access$getSpoofInvValue$p(AutoPot $this) {
        return $this.spoofInvValue;
    }
}

