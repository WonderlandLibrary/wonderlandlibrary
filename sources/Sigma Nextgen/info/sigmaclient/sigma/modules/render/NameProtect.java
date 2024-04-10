package info.sigmaclient.sigma.modules.render;

import info.sigmaclient.sigma.event.render.RenderEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class NameProtect extends Module {
    public static boolean enable = false;
    public NameProtect() {
        super("NameProtect", Category.Render, "Dont wdr me.");
    }
    public static String getName(){
        return "me";
    }
    public static String place(String str){
        if(enable){
            return str.replace(mc.session.getUsername(), "Me");
        }
        return str;
    }

    @Override
    public void onEnable() {
        enable = true;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        enable = false;
        super.onDisable();
    }
}
