// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.modules.render;

import Lavish.ClickGUI.settings.Setting;
import Lavish.Client;
import java.util.ArrayList;
import Lavish.modules.Category;
import Lavish.modules.Module;

public class Cape extends Module
{
    public Cape() {
        super("Cape", 0, true, Category.Render, "Shows capes on you");
        final ArrayList<String> options = new ArrayList<String>();
        options.add("Lavish");
        options.add("Astolfo");
        options.add("Astolfo2");
        options.add("Dortware");
        options.add("Anime");
        options.add("Moon");
        options.add("Flux");
        Client.instance.setmgr.rSetting(new Setting("Cape Mode", this, "Lavish", options));
    }
}
