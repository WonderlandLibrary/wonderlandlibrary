/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.network.play.client.C03PacketPlayer$C04PacketPlayerPosition
 *  net.minecraft.network.play.client.C07PacketPlayerDigging
 *  net.minecraft.network.play.client.C07PacketPlayerDigging$Action
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module.modules.combat;

import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.Client;
import net.dev.important.event.AttackEvent;
import net.dev.important.event.EventTarget;
import net.dev.important.event.PacketEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.modules.module.modules.combat.KillAura;
import net.dev.important.modules.module.modules.combat.TeleportAura;
import net.dev.important.modules.module.modules.movement.Fly;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.timer.MSTimer;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.ListValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import oh.yalan.NativeClass;
import org.jetbrains.annotations.NotNull;

@NativeClass
@Info(name="Criticals", description="Automatically deals critical hits.", category=Category.COMBAT, cnName="\u66b4\u51fb")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020&H\u0007J\b\u0010'\u001a\u00020$H\u0016J\u0010\u0010(\u001a\u00020$2\u0006\u0010%\u001a\u00020)H\u0007R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0011\u0010\f\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0014\u001a\u00020\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0011\u0010\u0018\u001a\u00020\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u000e\u0010\u001c\u001a\u00020\u001dX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u001f\u001a\u00020 8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b!\u0010\"\u00a8\u0006*"}, d2={"Lnet/dev/important/modules/module/modules/combat/Criticals;", "Lnet/dev/important/modules/module/Module;", "()V", "attacks", "", "getAttacks", "()I", "setAttacks", "(I)V", "canCrits", "", "counter", "delayValue", "Lnet/dev/important/value/IntegerValue;", "getDelayValue", "()Lnet/dev/important/value/IntegerValue;", "downYValue", "Lnet/dev/important/value/FloatValue;", "hurtTimeValue", "jumpHeightValue", "modeValue", "Lnet/dev/important/value/ListValue;", "getModeValue", "()Lnet/dev/important/value/ListValue;", "msTimer", "Lnet/dev/important/utils/timer/MSTimer;", "getMsTimer", "()Lnet/dev/important/utils/timer/MSTimer;", "onlyAuraValue", "Lnet/dev/important/value/BoolValue;", "readyCrits", "tag", "", "getTag", "()Ljava/lang/String;", "onAttack", "", "event", "Lnet/dev/important/event/AttackEvent;", "onEnable", "onPacket", "Lnet/dev/important/event/PacketEvent;", "LiquidBounce"})
public final class Criticals
extends Module {
    @NotNull
    private final ListValue modeValue;
    @NotNull
    private final IntegerValue delayValue;
    @NotNull
    private final FloatValue jumpHeightValue;
    @NotNull
    private final FloatValue downYValue;
    @NotNull
    private final IntegerValue hurtTimeValue;
    @NotNull
    private final BoolValue onlyAuraValue;
    @NotNull
    private final MSTimer msTimer;
    private boolean readyCrits;
    private boolean canCrits;
    private int counter;
    private int attacks;

    public Criticals() {
        String[] stringArray = new String[]{"NewPacket", "Packet", "NCPPacket", "NoGround", "Hop", "TPHop", "Jump", "Visual", "VerusSmart", "Vulcan", "Hypixel"};
        this.modeValue = new ListValue("Mode", stringArray, "Packet");
        this.delayValue = new IntegerValue("Delay", 300, 0, 500, "ms");
        this.jumpHeightValue = new FloatValue("JumpHeight", 0.42f, 0.1f, 0.42f, new Function0<Boolean>(this){
            final /* synthetic */ Criticals this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)this.this$0.getModeValue().get(), "jump", true);
            }
        });
        this.downYValue = new FloatValue("DownY", 0.0f, 0.0f, 0.1f, new Function0<Boolean>(this){
            final /* synthetic */ Criticals this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            @NotNull
            public final Boolean invoke() {
                return StringsKt.equals((String)this.this$0.getModeValue().get(), "jump", true);
            }
        });
        this.hurtTimeValue = new IntegerValue("HurtTime", 10, 0, 10);
        this.onlyAuraValue = new BoolValue("OnlyAura", false);
        this.msTimer = new MSTimer();
        this.canCrits = true;
    }

    @NotNull
    public final ListValue getModeValue() {
        return this.modeValue;
    }

    @NotNull
    public final IntegerValue getDelayValue() {
        return this.delayValue;
    }

    @NotNull
    public final MSTimer getMsTimer() {
        return this.msTimer;
    }

    public final int getAttacks() {
        return this.attacks;
    }

    public final void setAttacks(int n) {
        this.attacks = n;
    }

    @Override
    public void onEnable() {
        if (StringsKt.equals((String)this.modeValue.get(), "NoGround", true)) {
            MinecraftInstance.mc.field_71439_g.func_70664_aZ();
        }
        this.canCrits = true;
        this.counter = 0;
    }

    @EventTarget
    public final void onAttack(@NotNull AttackEvent event) {
        block59: {
            Entity entity;
            block61: {
                block60: {
                    Intrinsics.checkNotNullParameter(event, "event");
                    if (((Boolean)this.onlyAuraValue.get()).booleanValue()) {
                        Module module2 = Client.INSTANCE.getModuleManager().get(KillAura.class);
                        Intrinsics.checkNotNull(module2);
                        if (!module2.getState()) {
                            Module module3 = Client.INSTANCE.getModuleManager().get(TeleportAura.class);
                            Intrinsics.checkNotNull(module3);
                            if (!module3.getState()) {
                                return;
                            }
                        }
                    }
                    if (!(event.getTargetEntity() instanceof EntityLivingBase)) break block59;
                    entity = event.getTargetEntity();
                    if (!MinecraftInstance.mc.field_71439_g.field_70122_E || MinecraftInstance.mc.field_71439_g.func_70617_f_() || MinecraftInstance.mc.field_71439_g.field_70134_J || MinecraftInstance.mc.field_71439_g.func_70090_H() || MinecraftInstance.mc.field_71439_g.func_180799_ab() || MinecraftInstance.mc.field_71439_g.field_70154_o != null || ((EntityLivingBase)entity).field_70737_aN > ((Number)this.hurtTimeValue.get()).intValue()) break block60;
                    Module module4 = Client.INSTANCE.getModuleManager().get(Fly.class);
                    Intrinsics.checkNotNull(module4);
                    if (!module4.getState() && this.msTimer.hasTimePassed(((Number)this.delayValue.get()).intValue())) break block61;
                }
                return;
            }
            double x = MinecraftInstance.mc.field_71439_g.field_70165_t;
            double y = MinecraftInstance.mc.field_71439_g.field_70163_u;
            double z = MinecraftInstance.mc.field_71439_g.field_70161_v;
            Object object = ((String)this.modeValue.get()).toLowerCase();
            Intrinsics.checkNotNullExpressionValue(object, "this as java.lang.String).toLowerCase()");
            switch (object) {
                case "newpacket": {
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.05250000001304, z, true));
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.00150000001304, z, false));
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.01400000001304, z, false));
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.00150000001304, z, false));
                    MinecraftInstance.mc.field_71439_g.func_71009_b(entity);
                    break;
                }
                case "packet": {
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.0625, z, true));
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1.1E-5, z, false));
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                    MinecraftInstance.mc.field_71439_g.func_71009_b(entity);
                    break;
                }
                case "ncppacket": {
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.11, z, false));
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.1100013579, z, false));
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1.3579E-6, z, false));
                    MinecraftInstance.mc.field_71439_g.func_71009_b(entity);
                    break;
                }
                case "aacv4": {
                    EntityPlayerSP entityPlayerSP = MinecraftInstance.mc.field_71439_g;
                    entityPlayerSP.field_70179_y *= 0.0;
                    entityPlayerSP = MinecraftInstance.mc.field_71439_g;
                    entityPlayerSP.field_70159_w *= 0.0;
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(MinecraftInstance.mc.field_71439_g.field_70165_t, MinecraftInstance.mc.field_71439_g.field_70163_u + 3.0E-14, MinecraftInstance.mc.field_71439_g.field_70161_v, true));
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(MinecraftInstance.mc.field_71439_g.field_70165_t, MinecraftInstance.mc.field_71439_g.field_70163_u + 8.0E-15, MinecraftInstance.mc.field_71439_g.field_70161_v, true));
                    break;
                }
                case "vulcan": {
                    int n = this.attacks;
                    this.attacks = n + 1;
                    if (this.attacks <= 6) break;
                    Criticals.onAttack$sendPacket$default(0.0, 0.2, 0.0, false, 5, null);
                    Criticals.onAttack$sendPacket$default(0.0, 0.1216, 0.0, false, 5, null);
                    this.attacks = 0;
                    break;
                }
                case "hypixel": {
                    object = new double[7];
                    object[0] = 0.05;
                    object[1] = 0.0016;
                    object[2] = 0.0018;
                    object[3] = 0.0016;
                    object[4] = 0.002;
                    object[5] = 0.04;
                    object[6] = 0.0011;
                    for (Object offset : object) {
                        MinecraftInstance.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(MinecraftInstance.mc.field_71439_g.field_70165_t, MinecraftInstance.mc.field_71439_g.field_70163_u + offset, MinecraftInstance.mc.field_71439_g.field_70161_v, false));
                    }
                    break;
                }
                case "hop": {
                    MinecraftInstance.mc.field_71439_g.field_70181_x = 0.1;
                    MinecraftInstance.mc.field_71439_g.field_70143_R = 0.1f;
                    MinecraftInstance.mc.field_71439_g.field_70122_E = false;
                    break;
                }
                case "tphop": {
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.02, z, false));
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.01, z, false));
                    MinecraftInstance.mc.field_71439_g.func_70107_b(x, y + 0.01, z);
                    break;
                }
                case "jump": {
                    if (MinecraftInstance.mc.field_71439_g.field_70122_E) {
                        MinecraftInstance.mc.field_71439_g.field_70181_x = ((Number)this.jumpHeightValue.get()).floatValue();
                        break;
                    }
                    EntityPlayerSP entityPlayerSP = MinecraftInstance.mc.field_71439_g;
                    entityPlayerSP.field_70181_x -= ((Number)this.downYValue.get()).doubleValue();
                    break;
                }
                case "miniphase": {
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y - 0.0125, z, false));
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.01275, z, false));
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y - 2.5E-4, z, true));
                    break;
                }
                case "nanopacket": {
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.00973333333333, z, false));
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.001, z, false));
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y - 0.01200000000007, z, false));
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y - 5.0E-4, z, false));
                    break;
                }
                case "non-calculable": {
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1.0E-5, z, false));
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1.0E-7, z, false));
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y - 1.0E-6, z, false));
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y - 1.0E-4, z, false));
                    break;
                }
                case "invalid": {
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1.0E27, z, false));
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y - 1.0E68, z, false));
                    MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1.0E41, z, false));
                    break;
                }
                case "verussmart": {
                    int n = this.counter;
                    this.counter = n + 1;
                    if (this.counter == 1) {
                        MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.001, z, true));
                        MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                    }
                    if (this.counter < 5) break;
                    this.counter = 0;
                    break;
                }
                case "visual": {
                    MinecraftInstance.mc.field_71439_g.func_71009_b(entity);
                }
            }
            this.readyCrits = true;
            this.msTimer.reset();
        }
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (((Boolean)this.onlyAuraValue.get()).booleanValue()) {
            Module module2 = Client.INSTANCE.getModuleManager().get(KillAura.class);
            Intrinsics.checkNotNull(module2);
            if (!module2.getState()) {
                return;
            }
        }
        Packet<?> packet = event.getPacket();
        String string = ((String)this.modeValue.get()).toLowerCase();
        Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).toLowerCase()");
        switch (string) {
            case "redesky": {
                if (packet instanceof C03PacketPlayer) {
                    C03PacketPlayer packetPlayer = (C03PacketPlayer)packet;
                    if (MinecraftInstance.mc.field_71439_g.field_70122_E && this.canCrits) {
                        packetPlayer.field_149477_b += 1.0E-6;
                        packetPlayer.field_149474_g = false;
                    }
                    if (MinecraftInstance.mc.field_71441_e.func_72945_a((Entity)MinecraftInstance.mc.field_71439_g, MinecraftInstance.mc.field_71439_g.func_174813_aQ().func_72317_d(0.0, (MinecraftInstance.mc.field_71439_g.field_70181_x - 0.08) * 0.98, 0.0).func_72314_b(0.0, 0.0, 0.0)).isEmpty()) {
                        packetPlayer.field_149474_g = true;
                    }
                }
                if (!(packet instanceof C07PacketPlayerDigging)) break;
                if (((C07PacketPlayerDigging)packet).func_180762_c() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
                    this.canCrits = false;
                    break;
                }
                if (((C07PacketPlayerDigging)packet).func_180762_c() != C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK && ((C07PacketPlayerDigging)packet).func_180762_c() != C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK) break;
                this.canCrits = true;
                break;
            }
            case "noground": {
                if (!(packet instanceof C03PacketPlayer)) break;
                ((C03PacketPlayer)packet).field_149474_g = false;
                break;
            }
            case "edit": {
                if (!this.readyCrits) break;
                if (packet instanceof C03PacketPlayer) {
                    ((C03PacketPlayer)packet).field_149474_g = false;
                }
                this.readyCrits = false;
            }
        }
    }

    @Override
    @NotNull
    public String getTag() {
        return (String)this.modeValue.get();
    }

    private static final void onAttack$sendPacket(double xOffset, double yOffset, double zOffset, boolean ground) {
        double x = MinecraftInstance.mc.field_71439_g.field_70165_t + xOffset;
        double y = MinecraftInstance.mc.field_71439_g.field_70163_u + yOffset;
        double z = MinecraftInstance.mc.field_71439_g.field_70161_v + zOffset;
        MinecraftInstance.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, ground));
    }

    static /* synthetic */ void onAttack$sendPacket$default(double d, double d2, double d3, boolean bl, int n, Object object) {
        if ((n & 1) != 0) {
            d = 0.0;
        }
        if ((n & 2) != 0) {
            d2 = 0.0;
        }
        if ((n & 4) != 0) {
            d3 = 0.0;
        }
        Criticals.onAttack$sendPacket(d, d2, d3, bl);
    }
}

