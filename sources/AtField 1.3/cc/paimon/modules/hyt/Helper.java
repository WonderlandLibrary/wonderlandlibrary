/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.functions.Function0
 */
package cc.paimon.modules.hyt;

import kotlin.TypeCastException;
import kotlin.jvm.functions.Function0;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.Value;

@ModuleInfo(name="AutoRange", description="Fix Killaura AirBan", category=ModuleCategory.COMBAT)
public final class Helper
extends Module {
    private final BoolValue betterAuraValue = new BoolValue("BetterAura", false);
    private final Value AirRange;
    private final Value hurttime = new IntegerValue("BetterAura-HurtTime1", 9, 1, 10).displayable(new Function0(this){
        final Helper this$0;

        public final boolean invoke() {
            return (Boolean)Helper.access$getBetterAuraValue$p(this.this$0).get();
        }
        {
            this.this$0 = helper;
            super(0);
        }

        static {
        }

        public Object invoke() {
            return this.invoke();
        }
    });
    private final BoolValue Debug;
    private final Value GroundRange;
    private final Value hurttime2 = new IntegerValue("BetterAura-HurtTime2", 10, 1, 10).displayable(new Function0(this){
        final Helper this$0;
        {
            this.this$0 = helper;
            super(0);
        }

        public Object invoke() {
            return this.invoke();
        }

        static {
        }

        public final boolean invoke() {
            return (Boolean)Helper.access$getBetterAuraValue$p(this.this$0).get();
        }
    });
    private int ticks;

    public static final BoolValue access$getBetterAuraValue$p(Helper helper) {
        return helper.betterAuraValue;
    }

    @EventTarget
    public final void onUpdate(UpdateEvent updateEvent) {
        Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(KillAura.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.combat.KillAura");
        }
        KillAura killAura = (KillAura)module;
        if (MinecraftInstance.mc.getThePlayer() == null) {
            return;
        }
        if (((Boolean)this.betterAuraValue.get()).booleanValue() && killAura.getState()) {
            if (MinecraftInstance.mc2.field_71439_g.field_70122_E && ((Number)killAura.getRangeValue().get()).floatValue() != ((Number)this.GroundRange.get()).floatValue()) {
                killAura.getRangeValue().set(this.GroundRange.get());
                Helper.debug$default(this, "NewRange: " + ((Number)this.GroundRange.get()).floatValue(), false, 2, null);
            } else if (!MinecraftInstance.mc2.field_71439_g.field_70122_E && ((Number)killAura.getRangeValue().get()).floatValue() != ((Number)this.AirRange.get()).floatValue()) {
                killAura.getRangeValue().set(this.AirRange.get());
                Helper.debug$default(this, "NewRange: " + ((Number)this.AirRange.get()).floatValue(), false, 2, null);
            }
            int n = this.ticks;
            this.ticks = n + 1;
            if (this.ticks == 1 && ((Number)killAura.getHurtTimeValue().get()).intValue() != ((Number)this.hurttime.get()).intValue()) {
                killAura.getHurtTimeValue().set(this.hurttime.get());
                Helper.debug$default(this, "NewHurtTime: " + ((Number)this.hurttime.get()).intValue(), false, 2, null);
            }
            if (this.ticks == 2 && ((Number)killAura.getHurtTimeValue().get()).intValue() != ((Number)this.hurttime2.get()).intValue()) {
                killAura.getHurtTimeValue().set(this.hurttime2.get());
                Helper.debug$default(this, "NewHurtTime: " + ((Number)this.hurttime2.get()).intValue(), false, 2, null);
            }
            if (this.ticks != 3 || this.ticks == 0) {
                // empty if block
            }
        }
    }

    public final void debug(String string, boolean bl) {
        if (((Boolean)this.Debug.get()).booleanValue() || bl) {
            ClientUtils.displayChatMessage("\u00a77[\u00a73\u00a76Helper\u00a77]\u00a7f " + string);
        }
    }

    public Helper() {
        this.AirRange = new FloatValue("BetterAura-AirRange", 3.0f, 0.0f, 5.0f).displayable(new Function0(this){
            final Helper this$0;

            static {
            }

            public Object invoke() {
                return this.invoke();
            }

            public final boolean invoke() {
                return (Boolean)Helper.access$getBetterAuraValue$p(this.this$0).get();
            }
            {
                this.this$0 = helper;
                super(0);
            }
        });
        this.GroundRange = new FloatValue("BetterAura-GroundRange", 3.5f, 0.0f, 5.0f).displayable(new Function0(this){
            final Helper this$0;

            static {
            }

            public Object invoke() {
                return this.invoke();
            }

            public final boolean invoke() {
                return (Boolean)Helper.access$getBetterAuraValue$p(this.this$0).get();
            }
            {
                this.this$0 = helper;
                super(0);
            }
        });
        this.Debug = new BoolValue("Debug", true);
    }

    public static void debug$default(Helper helper, String string, boolean bl, int n, Object object) {
        if ((n & 2) != 0) {
            bl = false;
        }
        helper.debug(string, bl);
    }
}

