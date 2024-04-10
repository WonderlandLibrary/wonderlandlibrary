package info.sigmaclient.sigma.modules;

import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.config.values.Value;
import info.sigmaclient.sigma.modules.gui.ActiveMods;
import info.sigmaclient.sigma.utils.render.anims.AnimationUtils;
import info.sigmaclient.sigma.utils.render.anims.PartialTicksAnim;
import info.sigmaclient.sigma.gui.hud.notification.NotificationManager;
import info.sigmaclient.sigma.music.SoundPlayer;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import top.fl0wowp4rty.phantomshield.annotations.Native;

public class Module extends EventBase {
    public PartialTicksAnim getTranslate(){
        return this.translate;
    }
    public void setScaleTranslate(float y, float speed){
        this.translate.interpolate(y, speed);
    }
    public PartialTicksAnim getAlphaTranslate(){
        return this.translate2;
    }
    public void setAlphaTranslate(int y, float speed){
        this.translate2.interpolate(y, speed);
    }
    public void setScaleTranslate(float y){
        this.translate.setValue(y);
    }
    public void setAlphaTranslate(int y){
        this.translate2.setValue(y);
    }
    public void setPremium(){
        premium = true;
    }
    public static Minecraft mc = Minecraft.getInstance();
    PartialTicksAnim translate2 = new PartialTicksAnim(0);
    PartialTicksAnim translate = new PartialTicksAnim(0);
    public PartialTicksAnim hoverAnimationUtil = new PartialTicksAnim(0);
    public PartialTicksAnim sAnim = new PartialTicksAnim(0);
    public AnimationUtils animationUtils = new AnimationUtils();
    public double anim = 0;
    public int hoverAnimation = 0;
    public boolean hove = false;
    public String suffix;
    public double animX = 0;
    public double animY = 0;

    public String name;
    public String remapName;
    public Category category;
    public String describe;

    public int key = -1;

    public ArrayList<Value<?>> values;
    public ArrayList<String> premiumValues;
    public boolean enabled = false;
    public boolean notJello = false;
    public boolean premium = false;
    public Module(String name, String desc){
        this.name = name;
        this.category = null;
        this.describe = desc.replace(".", "");
        this.values = new ArrayList<>();
        this.premiumValues = new ArrayList<>();
        this.suffix = "";
    }
    public Module(String name, Category category, String desc, boolean notJello){
        this.name = name;
        this.category = category;
        this.describe = desc.replace(".", "");
        this.values = new ArrayList<>();
        this.premiumValues = new ArrayList<>();
        this.suffix = "";
        this.notJello = notJello;
    }
    public Module(String name, Category category, String desc){
        this.name = name;
        this.category = category;
        this.describe = desc.replace(".", "");
        this.values = new ArrayList<>();
        this.premiumValues = new ArrayList<>();
        this.suffix = "";
    }
    public Module(String name, Category category, String desc, int key){
        this.name = name;
        this.category = category;
        this.describe = desc.replace(".", "");
        this.key = key;
        this.values = new ArrayList<>();
        this.premiumValues = new ArrayList<>();
        this.suffix = "";
    }
    public void onChangeStatus() {}
    public void onEnable() {}
    public void onDisable() {}
    public void flagDisable() {
        if(enabled) {
            enabled = false;
            onDisable();
            NotificationManager.notify(remapName, "Disabled "+remapName+" due to lagback.");
        }
        onChangeStatus();
    }
    public void silentDisable() {
        if(enabled) {
            enabled = false;
        }
        onChangeStatus();
    }
    public void toggle(){
        enabled = !enabled;
        if(enabled){
            if(ActiveMods.sounds.getValue())
                SoundPlayer.MusicPlay("enable.wav");
            if(SigmaNG.gameMode == SigmaNG.GAME_MODE.dest){
                NotificationManager.notify("Module", remapName + " was enabled.", 1500);
            }
            onEnable();
        }else{
            if(ActiveMods.sounds.getValue())
                SoundPlayer.MusicPlay("disable.wav");
            if(SigmaNG.gameMode == SigmaNG.GAME_MODE.dest){
                NotificationManager.notify("Module", remapName + " was disabled.", 1500);
            }
            onDisable();
        }
        onChangeStatus();
    }
    protected void registerValue(Value<?> value){
        value.id = value.name + value.id;
        this.values.add(value);
    }
    public void toggleSilent() {
        enabled = !enabled;
        onChangeStatus();
    }
}
