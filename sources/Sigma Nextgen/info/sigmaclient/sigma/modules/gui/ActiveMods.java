package info.sigmaclient.sigma.modules.gui;

import info.sigmaclient.sigma.gui.hud.AnotherArrayList;
import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.player.WindowUpdateEvent;
import info.sigmaclient.sigma.event.render.RenderEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.gui.hud.JelloArrayList;
import info.sigmaclient.sigma.modules.RenderModule;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class ActiveMods extends RenderModule {
    public static NumberValue x = new NumberValue("X", 0, 0, 10000, NumberValue.NUMBER_TYPE.INT){
        @Override
        public boolean isHidden() {
            return true;
        }
    };
    public static NumberValue y = new NumberValue("Y", 0, 0, 10000, NumberValue.NUMBER_TYPE.INT){
        @Override
        public boolean isHidden() {
            return true;
        }
    };
    public void setX(float v){
        x.setValue(v);
    }
    public void setY(float v){
        y.setValue(v);
    }
    public float getX(){
        return x.getValue().floatValue();
    }
    public float getY(){
        return y.getValue().floatValue();
    }
    public static BooleanValue anim = new BooleanValue("Animations", true);
    public static BooleanValue sounds = new BooleanValue("Sounds", true);
    public static BooleanValue push = new BooleanValue("Push SB", true);

    public boolean isHover(double mx, double my) {
        if(SigmaNG.gameMode == SigmaNG.GAME_MODE.dest) {
            return jelloArrayList.hover(mx, my);
        }
        return false;
    }
    public ActiveMods() {
        super("ActiveMods", Category.Gui, "Draw enable modules");
        registerValue(anim);
        registerValue(sounds);
        registerValue(anim);
    }

    public JelloArrayList jelloArrayList = SigmaNG.gameMode == SigmaNG.GAME_MODE.SIGMA ? new JelloArrayList() : new AnotherArrayList();
    @Override
    public void onRenderEvent(RenderEvent event) {
        jelloArrayList.drawObject(SigmaNG.SigmaNG.moduleManager.modules);
        if(!push.isEnable()){
            JelloArrayList.lasty = 0;
        }
    }
    @Override
    public void onWindowUpdateEvent(WindowUpdateEvent event) {
        jelloArrayList.tickEvent(SigmaNG.SigmaNG.moduleManager.modules);
    }
}
