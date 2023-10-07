/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.server.S14PacketEntity
 *  net.minecraft.network.play.server.S1DPacketEntityEffect
 *  net.minecraft.util.IChatComponent
 *  net.minecraft.world.World
 *  org.jetbrains.annotations.NotNull
 */
package me.report.liquidware.modules.world;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.WorldEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.misc.HttpUtils;
import net.ccbluex.liquidbounce.utils.misc.SoundFxPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="AntiStaff", category=ModuleCategory.WORLD, description="skid")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0007\u001a\u00020\bH\u0016J\u0010\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\u000bH\u0007J\u0010\u0010\f\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\u000eH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2={"Lme/report/liquidware/modules/world/AntiStaff;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "detected", "", "obStaffs", "", "onEnable", "", "onPacket", "event", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onWorld", "e", "Lnet/ccbluex/liquidbounce/event/WorldEvent;", "KyinoClient"})
public final class AntiStaff
extends Module {
    private String obStaffs = "none";
    private boolean detected;

    @Override
    public void onEnable() {
        try {
            this.obStaffs = HttpUtils.get("https://wysi-foundation.github.io/LiquidCloud/LiquidBounce/staffs.txt");
            String string = "[Staff list] " + this.obStaffs;
            boolean bl = false;
            System.out.println((Object)string);
        }
        catch (Exception exception) {
            // empty catch block
        }
        this.detected = false;
    }

    @EventTarget
    public final void onWorld(@NotNull WorldEvent e) {
        Intrinsics.checkParameterIsNotNull(e, "e");
        this.detected = false;
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        block11: {
            block12: {
                Entity entity;
                Packet<?> packet;
                block9: {
                    block10: {
                        Intrinsics.checkParameterIsNotNull(event, "event");
                        if (AntiStaff.access$getMc$p$s1046033730().field_71441_e == null || AntiStaff.access$getMc$p$s1046033730().field_71439_g == null) {
                            return;
                        }
                        packet = event.getPacket();
                        if (!(packet instanceof S1DPacketEntityEffect) || (entity = AntiStaff.access$getMc$p$s1046033730().field_71441_e.func_73045_a(((S1DPacketEntityEffect)packet).func_149426_d())) == null) break block9;
                        CharSequence charSequence = this.obStaffs;
                        String string = entity.func_70005_c_();
                        Intrinsics.checkExpressionValueIsNotNull(string, "entity.name");
                        if (StringsKt.contains$default(charSequence, string, false, 2, null)) break block10;
                        CharSequence charSequence2 = this.obStaffs;
                        IChatComponent iChatComponent = entity.func_145748_c_();
                        Intrinsics.checkExpressionValueIsNotNull(iChatComponent, "entity.displayName");
                        String string2 = iChatComponent.func_150260_c();
                        Intrinsics.checkExpressionValueIsNotNull(string2, "entity.displayName.unformattedText");
                        if (!StringsKt.contains$default(charSequence2, string2, false, 2, null)) break block9;
                    }
                    if (!this.detected) {
                        new SoundFxPlayer().playSound(SoundFxPlayer.SoundType.VICTORY, -8.0f);
                        LiquidBounce.INSTANCE.getHud().addNotification(new Notification("Detected BlocksMC staff members with invis. You should quit ASAP.", Notification.Type.INFO, 8000L, 0, 0, 24, null));
                        AntiStaff.access$getMc$p$s1046033730().field_71439_g.func_71165_d("/leave");
                        this.detected = true;
                    }
                }
                if (!(packet instanceof S14PacketEntity) || (entity = ((S14PacketEntity)packet).func_149065_a((World)AntiStaff.access$getMc$p$s1046033730().field_71441_e)) == null) break block11;
                CharSequence charSequence = this.obStaffs;
                String string = entity.func_70005_c_();
                Intrinsics.checkExpressionValueIsNotNull(string, "entity.name");
                if (StringsKt.contains$default(charSequence, string, false, 2, null)) break block12;
                CharSequence charSequence3 = this.obStaffs;
                IChatComponent iChatComponent = entity.func_145748_c_();
                Intrinsics.checkExpressionValueIsNotNull(iChatComponent, "entity.displayName");
                String string3 = iChatComponent.func_150260_c();
                Intrinsics.checkExpressionValueIsNotNull(string3, "entity.displayName.unformattedText");
                if (!StringsKt.contains$default(charSequence3, string3, false, 2, null)) break block11;
            }
            if (!this.detected) {
                new SoundFxPlayer().playSound(SoundFxPlayer.SoundType.VICTORY, -8.0f);
                LiquidBounce.INSTANCE.getHud().addNotification(new Notification("Detected BlocksMC staff members. You should quit ASAP.", Notification.Type.INFO, 8000L, 0, 0, 24, null));
                AntiStaff.access$getMc$p$s1046033730().field_71439_g.func_71165_d("/leave");
                this.detected = true;
            }
        }
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

