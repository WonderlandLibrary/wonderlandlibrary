// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.COMBAT;

import ru.tuskevich.event.EventTarget;
import ru.tuskevich.event.events.impl.EventEntityBoundingBox;
import ru.tuskevich.ui.dropui.setting.Setting;
import ru.tuskevich.ui.dropui.setting.imp.SliderSetting;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "HitBox", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd-\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd", type = Type.COMBAT)
public class HitBox extends Module
{
    public static SliderSetting hitboxSize;
    
    public HitBox() {
        this.add(HitBox.hitboxSize);
    }
    
    @EventTarget
    public void onEventEntityBoundingBox(final EventEntityBoundingBox eventUpdate) {
        eventUpdate.setSize(HitBox.hitboxSize.getFloatValue());
    }
    
    static {
        HitBox.hitboxSize = new SliderSetting("Size", 0.2f, 0.1f, 3.0f, 0.01f);
    }
}
