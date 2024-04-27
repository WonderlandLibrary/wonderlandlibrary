/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.server.S0BPacketAnimation
 *  net.minecraft.network.play.server.S14PacketEntity
 *  net.minecraft.network.play.server.S18PacketEntityTeleport
 *  net.minecraft.network.play.server.S19PacketEntityHeadLook
 *  net.minecraft.network.play.server.S19PacketEntityStatus
 *  net.minecraft.network.play.server.S1DPacketEntityEffect
 *  net.minecraft.network.play.server.S20PacketEntityProperties
 *  net.minecraft.network.play.server.S49PacketUpdateEntityNBT
 *  net.minecraft.world.World
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module.modules.misc;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.concurrent.ThreadsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.CharsKt;
import kotlin.text.StringsKt;
import net.dev.important.Client;
import net.dev.important.event.EventTarget;
import net.dev.important.event.PacketEvent;
import net.dev.important.event.WorldEvent;
import net.dev.important.gui.client.hud.element.elements.Notification;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.misc.HttpUtils;
import net.dev.important.utils.timer.MSTimer;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.network.play.server.S19PacketEntityHeadLook;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S20PacketEntityProperties;
import net.minecraft.network.play.server.S49PacketUpdateEntityNBT;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

@Info(name="AntiBan", spacedName="Anti Ban", description="Anti staff on BlocksMC. Automatically leaves a map if detected known staffs.", category=Category.MISC, cnName="\u53cd\u5c01\u7981")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0011\u001a\u00020\u0012H\u0016J\b\u0010\u0013\u001a\u00020\u0012H\u0016J\u0010\u0010\u0014\u001a\u00020\u00122\u0006\u0010\u0015\u001a\u00020\u0016H\u0007J\u0010\u0010\u0017\u001a\u00020\u00122\u0006\u0010\u0018\u001a\u00020\u0019H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\u00020\u00078VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2={"Lnet/dev/important/modules/module/modules/misc/AntiBan;", "Lnet/dev/important/modules/module/Module;", "()V", "detected", "", "finishedCheck", "obStaffs", "", "staff_fallback", "staff_main", "tag", "getTag", "()Ljava/lang/String;", "totalCount", "", "updater", "Lnet/dev/important/utils/timer/MSTimer;", "onEnable", "", "onInitialize", "onPacket", "event", "Lnet/dev/important/event/PacketEvent;", "onWorld", "e", "Lnet/dev/important/event/WorldEvent;", "LiquidBounce"})
public final class AntiBan
extends Module {
    @NotNull
    private String obStaffs = "_";
    private boolean detected;
    private int totalCount;
    private boolean finishedCheck;
    @NotNull
    private MSTimer updater = new MSTimer();
    private String staff_main = "https://add-my-brain.exit-scammed.repl.co/staff/";
    private String staff_fallback = "https://wysi-foundation.github.io/LiquidCloud/LiquidBounce/staffs.txt";

    @Override
    public void onInitialize() {
        ThreadsKt.thread$default(false, false, null, null, 0, new Function0<Unit>(this){
            final /* synthetic */ AntiBan this$0;
            {
                this.this$0 = $receiver;
                super(0);
            }

            /*
             * WARNING - void declaration
             */
            public final void invoke() {
                void $this$filterTo$iv$iv;
                void $this$filter$iv;
                String string = AntiBan.access$getStaff_fallback$p(this.this$0);
                if (string == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("staff_fallback");
                    string = null;
                }
                AntiBan.access$setObStaffs$p(this.this$0, HttpUtils.get(string));
                String string2 = AntiBan.access$getObStaffs$p(this.this$0);
                AntiBan antiBan = this.this$0;
                boolean $i$f$filter = false;
                CharSequence charSequence = (CharSequence)$this$filter$iv;
                Appendable destination$iv$iv = new StringBuilder();
                boolean $i$f$filterTo = false;
                int n = 0;
                int n2 = $this$filterTo$iv$iv.length();
                while (n < n2) {
                    char element$iv$iv;
                    int index$iv$iv = n++;
                    char it = element$iv$iv = $this$filterTo$iv$iv.charAt(index$iv$iv);
                    boolean bl = false;
                    if (!CharsKt.isWhitespace(it)) continue;
                    destination$iv$iv.append(element$iv$iv);
                }
                String string3 = ((StringBuilder)destination$iv$iv).toString();
                Intrinsics.checkNotNullExpressionValue(string3, "filterTo(StringBuilder(), predicate).toString()");
                AntiBan.access$setTotalCount$p(antiBan, ((CharSequence)string3).length());
                System.out.println((Object)Intrinsics.stringPlus("[Staff/fallback] ", AntiBan.access$getObStaffs$p(this.this$0)));
            }
        }, 31, null);
    }

    @Override
    public void onEnable() {
        this.detected = false;
    }

    @EventTarget
    public final void onWorld(@NotNull WorldEvent e) {
        Intrinsics.checkNotNullParameter(e, "e");
        this.detected = false;
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        block47: {
            Entity entity;
            block48: {
                String string;
                Packet<?> packet;
                block45: {
                    block46: {
                        block43: {
                            block44: {
                                block41: {
                                    block42: {
                                        block39: {
                                            block40: {
                                                block37: {
                                                    block38: {
                                                        block35: {
                                                            block36: {
                                                                block33: {
                                                                    block34: {
                                                                        Intrinsics.checkNotNullParameter(event, "event");
                                                                        if (MinecraftInstance.mc.field_71441_e == null || MinecraftInstance.mc.field_71439_g == null) {
                                                                            return;
                                                                        }
                                                                        packet = event.getPacket();
                                                                        if (!(packet instanceof S1DPacketEntityEffect) || (entity = MinecraftInstance.mc.field_71441_e.func_73045_a(((S1DPacketEntityEffect)packet).func_149426_d())) == null) break block33;
                                                                        CharSequence charSequence = this.obStaffs;
                                                                        string = entity.func_70005_c_();
                                                                        Intrinsics.checkNotNullExpressionValue(string, "entity.name");
                                                                        if (StringsKt.contains$default(charSequence, string, false, 2, null)) break block34;
                                                                        CharSequence charSequence2 = this.obStaffs;
                                                                        string = entity.func_145748_c_().func_150260_c();
                                                                        Intrinsics.checkNotNullExpressionValue(string, "entity.displayName.unformattedText");
                                                                        if (!StringsKt.contains$default(charSequence2, string, false, 2, null)) break block33;
                                                                    }
                                                                    if (!this.detected) {
                                                                        Client.INSTANCE.getHud().addNotification(new Notification(Intrinsics.stringPlus(entity.func_70005_c_(), " / effect."), Notification.Type.ERROR));
                                                                        MinecraftInstance.mc.field_71439_g.func_71165_d("/leave");
                                                                        this.detected = true;
                                                                    }
                                                                }
                                                                if (!(packet instanceof S18PacketEntityTeleport) || (entity = MinecraftInstance.mc.field_71441_e.func_73045_a(((S18PacketEntityTeleport)packet).func_149451_c())) == null) break block35;
                                                                CharSequence charSequence = this.obStaffs;
                                                                string = entity.func_70005_c_();
                                                                Intrinsics.checkNotNullExpressionValue(string, "entity.name");
                                                                if (StringsKt.contains$default(charSequence, string, false, 2, null)) break block36;
                                                                CharSequence charSequence3 = this.obStaffs;
                                                                string = entity.func_145748_c_().func_150260_c();
                                                                Intrinsics.checkNotNullExpressionValue(string, "entity.displayName.unformattedText");
                                                                if (!StringsKt.contains$default(charSequence3, string, false, 2, null)) break block35;
                                                            }
                                                            if (!this.detected) {
                                                                Client.INSTANCE.getHud().addNotification(new Notification(Intrinsics.stringPlus(entity.func_70005_c_(), " / teleport."), Notification.Type.ERROR));
                                                                MinecraftInstance.mc.field_71439_g.func_71165_d("/leave");
                                                                this.detected = true;
                                                            }
                                                        }
                                                        if (!(packet instanceof S20PacketEntityProperties) || (entity = MinecraftInstance.mc.field_71441_e.func_73045_a(((S20PacketEntityProperties)packet).func_149442_c())) == null) break block37;
                                                        CharSequence charSequence = this.obStaffs;
                                                        string = entity.func_70005_c_();
                                                        Intrinsics.checkNotNullExpressionValue(string, "entity.name");
                                                        if (StringsKt.contains$default(charSequence, string, false, 2, null)) break block38;
                                                        CharSequence charSequence4 = this.obStaffs;
                                                        string = entity.func_145748_c_().func_150260_c();
                                                        Intrinsics.checkNotNullExpressionValue(string, "entity.displayName.unformattedText");
                                                        if (!StringsKt.contains$default(charSequence4, string, false, 2, null)) break block37;
                                                    }
                                                    if (!this.detected) {
                                                        Client.INSTANCE.getHud().addNotification(new Notification(Intrinsics.stringPlus(entity.func_70005_c_(), " / properties."), Notification.Type.ERROR));
                                                        MinecraftInstance.mc.field_71439_g.func_71165_d("/leave");
                                                        this.detected = true;
                                                    }
                                                }
                                                if (!(packet instanceof S0BPacketAnimation) || (entity = MinecraftInstance.mc.field_71441_e.func_73045_a(((S0BPacketAnimation)packet).func_148978_c())) == null) break block39;
                                                CharSequence charSequence = this.obStaffs;
                                                string = entity.func_70005_c_();
                                                Intrinsics.checkNotNullExpressionValue(string, "entity.name");
                                                if (StringsKt.contains$default(charSequence, string, false, 2, null)) break block40;
                                                CharSequence charSequence5 = this.obStaffs;
                                                string = entity.func_145748_c_().func_150260_c();
                                                Intrinsics.checkNotNullExpressionValue(string, "entity.displayName.unformattedText");
                                                if (!StringsKt.contains$default(charSequence5, string, false, 2, null)) break block39;
                                            }
                                            if (!this.detected) {
                                                Client.INSTANCE.getHud().addNotification(new Notification(Intrinsics.stringPlus(entity.func_70005_c_(), " / animation."), Notification.Type.ERROR));
                                                MinecraftInstance.mc.field_71439_g.func_71165_d("/leave");
                                                this.detected = true;
                                            }
                                        }
                                        if (!(packet instanceof S14PacketEntity) || (entity = ((S14PacketEntity)packet).func_149065_a((World)MinecraftInstance.mc.field_71441_e)) == null) break block41;
                                        CharSequence charSequence = this.obStaffs;
                                        string = entity.func_70005_c_();
                                        Intrinsics.checkNotNullExpressionValue(string, "entity.name");
                                        if (StringsKt.contains$default(charSequence, string, false, 2, null)) break block42;
                                        CharSequence charSequence6 = this.obStaffs;
                                        string = entity.func_145748_c_().func_150260_c();
                                        Intrinsics.checkNotNullExpressionValue(string, "entity.displayName.unformattedText");
                                        if (!StringsKt.contains$default(charSequence6, string, false, 2, null)) break block41;
                                    }
                                    if (!this.detected) {
                                        Client.INSTANCE.getHud().addNotification(new Notification(Intrinsics.stringPlus(entity.func_70005_c_(), " / update."), Notification.Type.ERROR));
                                        MinecraftInstance.mc.field_71439_g.func_71165_d("/leave");
                                        this.detected = true;
                                    }
                                }
                                if (!(packet instanceof S19PacketEntityStatus) || (entity = ((S19PacketEntityStatus)packet).func_149161_a((World)MinecraftInstance.mc.field_71441_e)) == null) break block43;
                                CharSequence charSequence = this.obStaffs;
                                string = entity.func_70005_c_();
                                Intrinsics.checkNotNullExpressionValue(string, "entity.name");
                                if (StringsKt.contains$default(charSequence, string, false, 2, null)) break block44;
                                CharSequence charSequence7 = this.obStaffs;
                                string = entity.func_145748_c_().func_150260_c();
                                Intrinsics.checkNotNullExpressionValue(string, "entity.displayName.unformattedText");
                                if (!StringsKt.contains$default(charSequence7, string, false, 2, null)) break block43;
                            }
                            if (!this.detected) {
                                Client.INSTANCE.getHud().addNotification(new Notification(Intrinsics.stringPlus(entity.func_70005_c_(), " / status."), Notification.Type.ERROR));
                                MinecraftInstance.mc.field_71439_g.func_71165_d("/leave");
                                this.detected = true;
                            }
                        }
                        if (!(packet instanceof S19PacketEntityHeadLook) || (entity = ((S19PacketEntityHeadLook)packet).func_149381_a((World)MinecraftInstance.mc.field_71441_e)) == null) break block45;
                        CharSequence charSequence = this.obStaffs;
                        string = entity.func_70005_c_();
                        Intrinsics.checkNotNullExpressionValue(string, "entity.name");
                        if (StringsKt.contains$default(charSequence, string, false, 2, null)) break block46;
                        CharSequence charSequence8 = this.obStaffs;
                        string = entity.func_145748_c_().func_150260_c();
                        Intrinsics.checkNotNullExpressionValue(string, "entity.displayName.unformattedText");
                        if (!StringsKt.contains$default(charSequence8, string, false, 2, null)) break block45;
                    }
                    if (!this.detected) {
                        Client.INSTANCE.getHud().addNotification(new Notification(Intrinsics.stringPlus(entity.func_70005_c_(), " / head."), Notification.Type.ERROR));
                        MinecraftInstance.mc.field_71439_g.func_71165_d("/leave");
                        this.detected = true;
                    }
                }
                if (!(packet instanceof S49PacketUpdateEntityNBT) || (entity = ((S49PacketUpdateEntityNBT)packet).func_179764_a((World)MinecraftInstance.mc.field_71441_e)) == null) break block47;
                CharSequence charSequence = this.obStaffs;
                string = entity.func_70005_c_();
                Intrinsics.checkNotNullExpressionValue(string, "entity.name");
                if (StringsKt.contains$default(charSequence, string, false, 2, null)) break block48;
                CharSequence charSequence9 = this.obStaffs;
                string = entity.func_145748_c_().func_150260_c();
                Intrinsics.checkNotNullExpressionValue(string, "entity.displayName.unformattedText");
                if (!StringsKt.contains$default(charSequence9, string, false, 2, null)) break block47;
            }
            if (!this.detected) {
                Client.INSTANCE.getHud().addNotification(new Notification(Intrinsics.stringPlus(entity.func_70005_c_(), " / nbt."), Notification.Type.ERROR));
                MinecraftInstance.mc.field_71439_g.func_71165_d("/leave");
                this.detected = true;
            }
        }
    }

    @Override
    @NotNull
    public String getTag() {
        return String.valueOf(this.totalCount);
    }

    public static final /* synthetic */ void access$setObStaffs$p(AntiBan $this, String string) {
        $this.obStaffs = string;
    }

    public static final /* synthetic */ String access$getStaff_fallback$p(AntiBan $this) {
        return $this.staff_fallback;
    }

    public static final /* synthetic */ void access$setTotalCount$p(AntiBan $this, int n) {
        $this.totalCount = n;
    }

    public static final /* synthetic */ String access$getObStaffs$p(AntiBan $this) {
        return $this.obStaffs;
    }
}

