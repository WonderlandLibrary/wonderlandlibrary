// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.MISC;

import ru.tuskevich.ui.dropui.setting.imp.BooleanSetting;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "Optimization", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd \ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd \ufffd\ufffd\ufffd\ufffd", type = Type.MISC)
public class Optimization extends Module
{
    public BooleanSetting grass;
    public BooleanSetting shadow;
    public BooleanSetting particles;
    
    public Optimization() {
        this.grass = new BooleanSetting("Grass", true);
        this.shadow = new BooleanSetting("Shadow", true);
        this.particles = new BooleanSetting("Particles", true);
    }
    
    @Override
    public void onEnable() {
        Optimization.mc.renderGlobal.loadRenderers();
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        Optimization.mc.renderGlobal.loadRenderers();
        super.onDisable();
    }
}
