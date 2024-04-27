/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import java.awt.Color;
import java.util.ArrayList;
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
    @EventTarget
    public final void onRender3D(Render3DEvent render3DEvent) {
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        Iterable iterable = iWorldClient.getLoadedEntityList();
        IClassProvider iClassProvider = MinecraftInstance.classProvider;
        boolean bl = false;
        Iterable iterable22 = iterable;
        Object object = new ArrayList();
        boolean bl2 = false;
        Iterator iterator2 = iterable22.iterator();
        while (iterator2.hasNext()) {
            Object t;
            Object t2 = t = iterator2.next();
            boolean bl3 = false;
            if (!iClassProvider.isEntityTNTPrimed(t2)) continue;
            object.add(t);
        }
        iterable = (List)object;
        boolean bl4 = false;
        for (Iterable iterable22 : iterable) {
            object = (IEntity)((Object)iterable22);
            bl2 = false;
            RenderUtils.drawEntityBox((IEntity)object, Color.RED, false);
        }
    }
}

