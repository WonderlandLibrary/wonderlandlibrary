// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.visuals;

import ru.fluger.client.event.EventTarget;
import ru.fluger.client.event.events.impl.player.EventTransformSideFirstPerson;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.NumberSetting;
import ru.fluger.client.feature.Feature;

public class ViewModel extends Feature
{
    public static NumberSetting rightx;
    public static NumberSetting righty;
    public static NumberSetting rightz;
    public static NumberSetting leftx;
    public static NumberSetting lefty;
    public static NumberSetting leftz;
    
    public ViewModel() {
        super("ViewModel", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0440\u0435\u0434\u0430\u043a\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u043f\u043e\u0437\u0438\u0446\u0438\u044e \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u043e\u0432 \u0432 \u0440\u0443\u043a\u0435", Type.Visuals);
        this.addSettings(ViewModel.rightx, ViewModel.righty, ViewModel.rightz, ViewModel.leftx, ViewModel.lefty, ViewModel.leftz);
    }
    
    @EventTarget
    public void onSidePerson(final EventTransformSideFirstPerson event) {
        if (event.getEnumHandSide() == vo.b) {
            bus.c(ViewModel.rightx.getCurrentValue(), ViewModel.righty.getCurrentValue(), ViewModel.rightz.getCurrentValue());
        }
        if (event.getEnumHandSide() == vo.a) {
            bus.c(-ViewModel.leftx.getCurrentValue(), ViewModel.lefty.getCurrentValue(), ViewModel.leftz.getCurrentValue());
        }
    }
    
    static {
        ViewModel.rightx = new NumberSetting("RightX", 0.0f, -2.0f, 2.0f, 0.1f, () -> true);
        ViewModel.righty = new NumberSetting("RightY", 0.2f, -2.0f, 2.0f, 0.1f, () -> true);
        ViewModel.rightz = new NumberSetting("RightZ", 0.2f, -2.0f, 2.0f, 0.1f, () -> true);
        ViewModel.leftx = new NumberSetting("LeftX", 0.0f, -2.0f, 2.0f, 0.1f, () -> true);
        ViewModel.lefty = new NumberSetting("LeftY", 0.2f, -2.0f, 2.0f, 0.1f, () -> true);
        ViewModel.leftz = new NumberSetting("LeftZ", 0.2f, -2.0f, 2.0f, 0.1f, () -> true);
    }
}
