// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.modules.render;

import Lavish.ClickGUI.settings.Setting;
import Lavish.Client;
import java.util.ArrayList;
import Lavish.modules.Category;
import Lavish.modules.Module;

public class Animation extends Module
{
    public Animation() {
        super("Animation", 0, true, Category.Render, "Animate");
        final ArrayList<String> options = new ArrayList<String>();
        options.add("Exhibition");
        options.add("Slide");
        options.add("Remix");
        options.add("Tap");
        options.add("Astolfo");
        options.add("Sink");
        options.add("Lavish");
        Client.instance.setmgr.rSetting(new Setting("Animation Mode", this, "Slide", options));
    }
}
