// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.MISC;

import ru.tuskevich.util.world.SoundUtility;
import ru.tuskevich.ui.dropui.setting.Setting;
import ru.tuskevich.ui.dropui.setting.imp.SliderSetting;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "ClientSounds", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd/\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd", type = Type.MISC)
public class ClientSound extends Module
{
    public static SliderSetting volume;
    
    public ClientSound() {
        this.add(ClientSound.volume);
    }
    
    @Override
    public void onDisable() {
        SoundUtility.playSound(1.5f, ClientSound.volume.getFloatValue());
        super.onDisable();
    }
    
    static {
        ClientSound.volume = new SliderSetting("Volume", 0.5f, 0.1f, 1.0f, 0.01f);
    }
}
