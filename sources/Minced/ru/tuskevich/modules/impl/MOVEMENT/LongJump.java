// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.MOVEMENT;

import ru.tuskevich.event.EventTarget;
import net.minecraft.client.Minecraft;
import ru.tuskevich.event.events.impl.EventUpdate;
import ru.tuskevich.ui.dropui.setting.Setting;
import ru.tuskevich.ui.dropui.setting.imp.SliderSetting;
import ru.tuskevich.ui.dropui.setting.imp.BooleanSetting;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "LongJump", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd", type = Type.MOVEMENT)
public class LongJump extends Module
{
    public int ticks2;
    public boolean a;
    public BooleanSetting autoJump;
    private final SliderSetting boost;
    
    public LongJump() {
        this.autoJump = new BooleanSetting("Auto Jump", true, () -> true);
        this.boost = new SliderSetting("Boost Multiplier", 0.25f, 0.1f, 0.32f, 0.01f, () -> true);
        this.add(this.autoJump, this.boost);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate eventUpdate) {
        final Minecraft mc = LongJump.mc;
        if (Minecraft.player.hurtTime > 0) {
            this.a = true;
        }
        if (this.a) {
            final Minecraft mc2 = LongJump.mc;
            if (Minecraft.player.onGround) {
                final Minecraft mc3 = LongJump.mc;
                Minecraft.player.jump();
            }
            final Minecraft mc4 = LongJump.mc;
            if (Minecraft.player.ticksExisted % 2 == 0) {
                this.ticks2 += (int)10.0f;
            }
            final Minecraft mc5 = LongJump.mc;
            Minecraft.player.speedInAir = this.boost.getFloatValue();
            final Minecraft mc6 = LongJump.mc;
            if (Minecraft.player.onGround) {
                this.a = false;
            }
        }
        else {
            final Minecraft mc7 = LongJump.mc;
            Minecraft.player.speedInAir = 0.02f;
        }
    }
    
    @Override
    public void onDisable() {
        this.a = false;
        this.ticks2 = 0;
        final Minecraft mc = LongJump.mc;
        Minecraft.player.speedInAir = 0.02f;
        LongJump.mc.timer.timerSpeed = 1.0f;
        super.onDisable();
    }
}
