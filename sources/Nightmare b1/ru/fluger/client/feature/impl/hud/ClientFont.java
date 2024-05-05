// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.hud;

import ru.fluger.client.settings.Setting;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.ListSetting;
import ru.fluger.client.feature.Feature;

public class ClientFont extends Feature
{
    public static ListSetting font;
    
    public ClientFont() {
        super("Client Font", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0432\u044b\u0431\u0440\u0430\u0442\u044c \u043d\u0430\u0441\u0442\u0440\u043e\u0439\u043a\u0443 \u0448\u0440\u0438\u0444\u0442\u0430 \u0443 \u0447\u0438\u0442\u0430", Type.Hud);
        ClientFont.font = new ListSetting("Font Type", "Ubuntu", new String[] { "RobotoRegular", "Rubik", "SF UI", "Luxora", "Calibri", "Verdana", "Comfortaa", "LucidaConsole", "Lato", "RaleWay", "Product Sans", "Open Sans", "Kollektif", "Ubuntu", "Bebas Book" });
        this.addSettings(ClientFont.font);
    }
    
    @Override
    public void onEnable() {
        this.toggle();
        super.onEnable();
    }
}
