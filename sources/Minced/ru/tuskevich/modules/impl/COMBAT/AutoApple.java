// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.COMBAT;

import ru.tuskevich.event.EventTarget;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.client.Minecraft;
import ru.tuskevich.event.events.impl.EventUpdate;
import ru.tuskevich.ui.dropui.setting.Setting;
import ru.tuskevich.ui.dropui.setting.imp.SliderSetting;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "AutoGApple", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd", type = Type.COMBAT)
public class AutoApple extends Module
{
    public SliderSetting health;
    boolean isEating;
    
    public AutoApple() {
        this.health = new SliderSetting("Health", 16.0f, 1.0f, 20.0f, 1.0f);
        this.add(this.health);
    }
    
    @EventTarget
    public void onUpdateEvent(final EventUpdate eventUpdate) {
        final Minecraft mc = AutoApple.mc;
        final float health;
        float hp = health = Minecraft.player.getHealth();
        final Minecraft mc2 = AutoApple.mc;
        hp = health + Minecraft.player.getAbsorptionAmount();
        final Minecraft mc3 = AutoApple.mc;
        if (Minecraft.player.getHeldItemOffhand().getItem() instanceof ItemAppleGold && hp <= this.health.getFloatValue()) {
            this.isEating = true;
            AutoApple.mc.gameSettings.keyBindUseItem.pressed = true;
        }
        else if (this.isEating) {
            AutoApple.mc.gameSettings.keyBindUseItem.pressed = false;
            this.isEating = false;
        }
    }
}
