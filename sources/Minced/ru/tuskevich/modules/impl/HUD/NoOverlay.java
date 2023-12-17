// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.HUD;

import java.util.Iterator;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.Entity;
import net.minecraft.init.MobEffects;
import net.minecraft.client.Minecraft;
import ru.tuskevich.event.events.impl.EventUpdate;
import ru.tuskevich.event.EventTarget;
import ru.tuskevich.event.events.impl.EventOverlay;
import ru.tuskevich.ui.dropui.setting.Setting;
import ru.tuskevich.ui.dropui.setting.imp.MultiBoxSetting;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "NoOverlay", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd \ufffd\ufffd\ufffd\ufffd", type = Type.HUD)
public class NoOverlay extends Module
{
    public static MultiBoxSetting element;
    
    public NoOverlay() {
        this.add(NoOverlay.element);
    }
    
    @EventTarget
    public void onOverlayEvent(final EventOverlay eventOverlay) {
        if ((NoOverlay.element.get(1) && eventOverlay.getOverlayType() == EventOverlay.OverlayType.TotemAnimation) || (NoOverlay.element.get(2) && eventOverlay.getOverlayType() == EventOverlay.OverlayType.Fire) || (NoOverlay.element.get(4) && eventOverlay.getOverlayType() == EventOverlay.OverlayType.BossBar) || (NoOverlay.element.get(7) && eventOverlay.getOverlayType() == EventOverlay.OverlayType.Light) || (NoOverlay.element.get(8) && eventOverlay.getOverlayType() == EventOverlay.OverlayType.Fog)) {
            eventOverlay.cancel();
        }
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate eventUpdate) {
        if (NoOverlay.element.get(6) && NoOverlay.mc.world.isRaining()) {
            NoOverlay.mc.world.setRainStrength(0.0f);
            NoOverlay.mc.world.setThunderStrength(0.0f);
        }
        Label_0111: {
            Label_0085: {
                if (NoOverlay.element.get(5)) {
                    final Minecraft mc = NoOverlay.mc;
                    if (Minecraft.player.isPotionActive(MobEffects.BLINDNESS)) {
                        break Label_0085;
                    }
                }
                final Minecraft mc2 = NoOverlay.mc;
                if (!Minecraft.player.isPotionActive(MobEffects.NAUSEA)) {
                    break Label_0111;
                }
            }
            final Minecraft mc3 = NoOverlay.mc;
            Minecraft.player.removePotionEffect(MobEffects.NAUSEA);
            final Minecraft mc4 = NoOverlay.mc;
            Minecraft.player.removePotionEffect(MobEffects.BLINDNESS);
        }
        if (NoOverlay.element.get(3)) {
            final Minecraft mc5 = NoOverlay.mc;
            if (Minecraft.player == null || NoOverlay.mc.world == null) {
                return;
            }
            for (final Entity entity : NoOverlay.mc.world.loadedEntityList) {
                if (!(entity instanceof EntityArmorStand)) {
                    continue;
                }
                NoOverlay.mc.world.removeEntity(entity);
            }
        }
    }
    
    static {
        NoOverlay.element = new MultiBoxSetting("Element", new String[] { "Scoreboard", "Totem", "Fire", "Armor Stand", "Boss Bar", "Bad Effects", "Rain", "Light", "Fog" });
    }
}
