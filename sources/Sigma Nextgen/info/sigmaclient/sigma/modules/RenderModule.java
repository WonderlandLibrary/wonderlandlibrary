package info.sigmaclient.sigma.modules;

import info.sigmaclient.sigma.config.values.NumberValue;
import top.fl0wowp4rty.phantomshield.annotations.Native;

public class RenderModule extends Module {
    public float getX(){
        return 0;
    }
    public float getY(){
        return 0;
    }
    public void setX(float v){
    }
    public void setY(float v){
    }
    public RenderModule(String name, Category category, String desc, boolean notJello) {
        super(name, category, desc, notJello);
    }
    public RenderModule(String name, Category category, String desc) {
        super(name, category, desc);
    }
}
