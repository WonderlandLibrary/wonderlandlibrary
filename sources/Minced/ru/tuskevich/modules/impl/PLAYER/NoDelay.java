// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.PLAYER;

import ru.tuskevich.event.EventTarget;
import net.minecraft.client.Minecraft;
import ru.tuskevich.event.events.impl.EventUpdate;
import ru.tuskevich.ui.dropui.setting.Setting;
import ru.tuskevich.ui.dropui.setting.imp.BooleanSetting;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "NoDelay", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd", type = Type.PLAYER)
public class NoDelay extends Module
{
    public BooleanSetting noJumpDelay;
    public BooleanSetting noRightClickDelay;
    
    public NoDelay() {
        this.noJumpDelay = new BooleanSetting("Jump", true);
        this.noRightClickDelay = new BooleanSetting("Right Click", false);
        this.add(this.noJumpDelay, this.noRightClickDelay);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate e) {
        if (this.noJumpDelay.get()) {
            final Minecraft mc = NoDelay.mc;
            Minecraft.player.jumpTicks = 0;
        }
        if (this.noRightClickDelay.get()) {
            NoDelay.mc.rightClickDelayTimer = 0;
        }
    }
    
    @Override
    public void onDisable() {
        final Minecraft mc = NoDelay.mc;
        Minecraft.player.jumpTicks = 10;
        NoDelay.mc.rightClickDelayTimer = 6;
        super.onDisable();
    }
}
