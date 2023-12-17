// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.RENDER;

import ru.tuskevich.event.EventTarget;
import net.minecraft.potion.PotionEffect;
import net.minecraft.init.MobEffects;
import net.minecraft.client.Minecraft;
import ru.tuskevich.event.events.impl.EventUpdate;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "FullBright", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd", type = Type.RENDER)
public class FullBright extends Module
{
    @EventTarget
    public void onUpdate(final EventUpdate eventUpdate) {
        final Minecraft mc = FullBright.mc;
        Minecraft.player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 10000, 1));
    }
    
    @Override
    public void onDisable() {
        final Minecraft mc = FullBright.mc;
        Minecraft.player.removeActivePotionEffect(MobEffects.NIGHT_VISION);
        super.onDisable();
    }
}
