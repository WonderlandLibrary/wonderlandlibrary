/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityTNTPrimed
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.event.EventTarget;
import net.dev.important.event.Render3DEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.render.RenderUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import org.jetbrains.annotations.NotNull;

@Info(name="TNTESP", spacedName="TNT ESP", description="Allows you to see ignited TNT blocks through walls.", category=Category.RENDER, cnName="\u70b8\u836f\u900f\u89c6")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\u00a8\u0006\u0007"}, d2={"Lnet/dev/important/modules/module/modules/render/TNTESP;", "Lnet/dev/important/modules/module/Module;", "()V", "onRender3D", "", "event", "Lnet/dev/important/event/Render3DEvent;", "LiquidBounce"})
public final class TNTESP
extends Module {
    /*
     * WARNING - void declaration
     */
    @EventTarget
    public final void onRender3D(@NotNull Render3DEvent event) {
        void $this$filterIsInstanceTo$iv$iv;
        Intrinsics.checkNotNullParameter(event, "event");
        List list = MinecraftInstance.mc.field_71441_e.field_72996_f;
        Intrinsics.checkNotNullExpressionValue(list, "mc.theWorld.loadedEntityList");
        Iterable $this$filterIsInstance$iv = list;
        boolean $i$f$filterIsInstance = false;
        Iterable iterable = $this$filterIsInstance$iv;
        Collection destination$iv$iv = new ArrayList();
        boolean $i$f$filterIsInstanceTo = false;
        for (Object element$iv$iv : $this$filterIsInstanceTo$iv$iv) {
            if (!(element$iv$iv instanceof EntityTNTPrimed)) continue;
            destination$iv$iv.add(element$iv$iv);
        }
        Iterable $this$forEach$iv = (List)destination$iv$iv;
        boolean $i$f$forEach = false;
        for (Object element$iv : $this$forEach$iv) {
            EntityTNTPrimed it = (EntityTNTPrimed)element$iv;
            boolean bl = false;
            RenderUtils.drawEntityBox((Entity)it, Color.RED, false);
        }
    }
}

