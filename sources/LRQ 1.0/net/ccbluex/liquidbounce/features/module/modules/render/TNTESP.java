/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.IClassProvider;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;

@ModuleInfo(name="TNTESP", description="Allows you to see ignited TNT blocks through walls.", category=ModuleCategory.RENDER)
public final class TNTESP
extends Module {
    /*
     * WARNING - void declaration
     */
    @EventTarget
    public final void onRender3D(Render3DEvent event) {
        void $this$filterTo$iv$iv;
        void $this$filter$iv;
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        Iterable iterable = iWorldClient.getLoadedEntityList();
        IClassProvider iClassProvider = MinecraftInstance.classProvider;
        boolean $i$f$filter = false;
        void var5_7 = $this$filter$iv;
        Collection destination$iv$iv = new ArrayList();
        boolean $i$f$filterTo = false;
        Iterator iterator = $this$filterTo$iv$iv.iterator();
        while (iterator.hasNext()) {
            Object element$iv$iv;
            Object p1 = element$iv$iv = iterator.next();
            boolean bl = false;
            if (!iClassProvider.isEntityTNTPrimed(p1)) continue;
            destination$iv$iv.add(element$iv$iv);
        }
        Iterable $this$forEach$iv = (List)destination$iv$iv;
        boolean $i$f$forEach = false;
        for (Object element$iv : $this$forEach$iv) {
            IEntity it = (IEntity)element$iv;
            boolean bl = false;
            RenderUtils.drawEntityBox(it, Color.RED, false);
        }
    }
}

