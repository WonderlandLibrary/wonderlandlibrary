// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.COMBAT;

import ru.tuskevich.event.EventTarget;
import net.minecraft.client.Minecraft;
import ru.tuskevich.event.events.impl.EventUpdate;
import ru.tuskevich.ui.dropui.setting.imp.SliderSetting;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "MultiActions", desc = "", type = Type.COMBAT)
public class MultiActions extends Module
{
    private final SliderSetting clickCoolDown;
    
    public MultiActions() {
        this.clickCoolDown = new SliderSetting("Cooldown", 1.0f, 0.5f, 1.0f, 0.1f, () -> true);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        final Minecraft mc = MultiActions.mc;
        if (Minecraft.player.getCooledAttackStrength(0.0f) == this.clickCoolDown.getFloatValue() && MultiActions.mc.gameSettings.keyBindAttack.pressed) {
            MultiActions.mc.clickMouse();
        }
    }
}
