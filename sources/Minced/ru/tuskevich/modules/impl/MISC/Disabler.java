// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.MISC;

import ru.tuskevich.event.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.Minecraft;
import ru.tuskevich.event.events.impl.EventMotion;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "Disabler", type = Type.MISC)
public class Disabler extends Module
{
    @EventTarget
    public void onMotion(final EventMotion event) {
        final Minecraft mc = Disabler.mc;
        if (Minecraft.player.isInWater()) {
            final float tp = 6.0f;
            for (int i = 0; i < 1; ++i) {
                final Minecraft mc2 = Disabler.mc;
                final EntityPlayerSP player = Minecraft.player;
                final Minecraft mc3 = Disabler.mc;
                player.setEntityBoundingBox(Minecraft.player.getEntityBoundingBox().offset(0.0, tp, 0.0));
                this.toggle();
            }
        }
        else {
            this.sendMessage("(Jesus Disabler) \u0417\u0430\u0439\u0434\u0438\u0442\u0435 \u0432 \u0432\u043e\u0434\u0443, \u0447\u0442\u043e\u0431\u044b \u0430\u043a\u0442\u0438\u0432\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u0431\u0430\u0439\u043f\u0430\u0441");
            this.toggle();
        }
    }
}
