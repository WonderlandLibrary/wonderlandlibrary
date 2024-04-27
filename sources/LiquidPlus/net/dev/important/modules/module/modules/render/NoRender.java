/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityArmorStand
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.player.EntityPlayer
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module.modules.render;

import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.event.EventTarget;
import net.dev.important.event.MotionEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.EntityUtils;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.extensions.PlayerExtensionKt;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import org.jetbrains.annotations.NotNull;

@Info(name="NoRender", spacedName="No Render", description="Increase FPS by decreasing or stop rendering visible entities.", category=Category.RENDER, cnName="\u4e0d\u663e\u793a")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0012\u001a\u00020\u0013H\u0016J\u0010\u0010\u0014\u001a\u00020\u00132\u0006\u0010\u0015\u001a\u00020\u0016H\u0007J\u000e\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001aR\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\b\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\u0006R\u000e\u0010\n\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u000f\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0006R\u000e\u0010\u0011\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2={"Lnet/dev/important/modules/module/modules/render/NoRender;", "Lnet/dev/important/modules/module/Module;", "()V", "allValue", "Lnet/dev/important/value/BoolValue;", "getAllValue", "()Lnet/dev/important/value/BoolValue;", "animalsValue", "armorStandValue", "getArmorStandValue", "autoResetValue", "itemsValue", "maxRenderRange", "Lnet/dev/important/value/FloatValue;", "mobsValue", "nameTagsValue", "getNameTagsValue", "playersValue", "onDisable", "", "onMotion", "event", "Lnet/dev/important/event/MotionEvent;", "shouldStopRender", "", "entity", "Lnet/minecraft/entity/Entity;", "LiquidBounce"})
public final class NoRender
extends Module {
    @NotNull
    private final BoolValue allValue = new BoolValue("All", true);
    @NotNull
    private final BoolValue nameTagsValue = new BoolValue("NameTags", true);
    @NotNull
    private final BoolValue itemsValue = new BoolValue("Items", true, new Function0<Boolean>(this){
        final /* synthetic */ NoRender this$0;
        {
            this.this$0 = $receiver;
            super(0);
        }

        @NotNull
        public final Boolean invoke() {
            return (Boolean)this.this$0.getAllValue().get() == false;
        }
    });
    @NotNull
    private final BoolValue playersValue = new BoolValue("Players", true, new Function0<Boolean>(this){
        final /* synthetic */ NoRender this$0;
        {
            this.this$0 = $receiver;
            super(0);
        }

        @NotNull
        public final Boolean invoke() {
            return (Boolean)this.this$0.getAllValue().get() == false;
        }
    });
    @NotNull
    private final BoolValue mobsValue = new BoolValue("Mobs", true, new Function0<Boolean>(this){
        final /* synthetic */ NoRender this$0;
        {
            this.this$0 = $receiver;
            super(0);
        }

        @NotNull
        public final Boolean invoke() {
            return (Boolean)this.this$0.getAllValue().get() == false;
        }
    });
    @NotNull
    private final BoolValue animalsValue = new BoolValue("Animals", true, new Function0<Boolean>(this){
        final /* synthetic */ NoRender this$0;
        {
            this.this$0 = $receiver;
            super(0);
        }

        @NotNull
        public final Boolean invoke() {
            return (Boolean)this.this$0.getAllValue().get() == false;
        }
    });
    @NotNull
    private final BoolValue armorStandValue = new BoolValue("ArmorStand", true, new Function0<Boolean>(this){
        final /* synthetic */ NoRender this$0;
        {
            this.this$0 = $receiver;
            super(0);
        }

        @NotNull
        public final Boolean invoke() {
            return (Boolean)this.this$0.getAllValue().get() == false;
        }
    });
    @NotNull
    private final BoolValue autoResetValue = new BoolValue("AutoReset", true);
    @NotNull
    private final FloatValue maxRenderRange = new FloatValue("MaxRenderRange", 4.0f, 0.0f, 16.0f, "m");

    @NotNull
    public final BoolValue getAllValue() {
        return this.allValue;
    }

    @NotNull
    public final BoolValue getNameTagsValue() {
        return this.nameTagsValue;
    }

    @NotNull
    public final BoolValue getArmorStandValue() {
        return this.armorStandValue;
    }

    @EventTarget
    public final void onMotion(@NotNull MotionEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        for (Entity en : MinecraftInstance.mc.field_71441_e.field_72996_f) {
            Entity entity;
            Intrinsics.checkNotNull(en);
            if (this.shouldStopRender(entity)) {
                entity.field_70155_l = 0.0;
                continue;
            }
            if (!((Boolean)this.autoResetValue.get()).booleanValue()) continue;
            entity.field_70155_l = 1.0;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public final boolean shouldStopRender(@NotNull Entity entity) {
        Intrinsics.checkNotNullParameter(entity, "entity");
        if (!(((Boolean)this.allValue.get()).booleanValue() || ((Boolean)this.itemsValue.get()).booleanValue() && entity instanceof EntityItem || ((Boolean)this.playersValue.get()).booleanValue() && entity instanceof EntityPlayer || ((Boolean)this.mobsValue.get()).booleanValue() && EntityUtils.isMob(entity) || ((Boolean)this.animalsValue.get()).booleanValue() && EntityUtils.isAnimal(entity))) {
            if ((Boolean)this.armorStandValue.get() == false) return false;
            if (!(entity instanceof EntityArmorStand)) return false;
        }
        EntityPlayerSP entityPlayerSP = MinecraftInstance.mc.field_71439_g;
        Intrinsics.checkNotNull(entityPlayerSP);
        if (Intrinsics.areEqual(entity, entityPlayerSP)) return false;
        EntityPlayerSP entityPlayerSP2 = MinecraftInstance.mc.field_71439_g;
        Intrinsics.checkNotNull(entityPlayerSP2);
        if (!((float)PlayerExtensionKt.getDistanceToEntityBox((Entity)entityPlayerSP2, entity) > ((Number)this.maxRenderRange.get()).floatValue())) return false;
        return true;
    }

    @Override
    public void onDisable() {
        for (Entity en : MinecraftInstance.mc.field_71441_e.field_72996_f) {
            Entity entity;
            Intrinsics.checkNotNull(en);
            EntityPlayerSP entityPlayerSP = MinecraftInstance.mc.field_71439_g;
            Intrinsics.checkNotNull(entityPlayerSP);
            if (Intrinsics.areEqual(entity, entityPlayerSP) || !(entity.field_70155_l <= 0.0)) continue;
            entity.field_70155_l = 1.0;
        }
    }
}

