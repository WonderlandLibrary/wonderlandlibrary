package Method.Client.module.misc;

import Method.Client.Main;
import Method.Client.clickgui.ClickGui;
import Method.Client.clickgui.component.Component;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

import static Method.Client.Main.setmgr;

public class GuiModule extends Module {


    public ClickGui clickgui;
    public static Setting Frame;
    public static Setting Button;
    public static Setting Subcomponents;
    public static Setting Animations;
    static public Setting Framecolor;
    static public Setting Background;
    static public Setting innercolor;
    static public Setting Hover;
    static public Setting Anispeed;
    static public Setting ColorAni;
    static public Setting Highlight;
    static public Setting Blur;
    static public Setting border;

    public GuiModule() {
        super("ClickGUI", Keyboard.KEY_RSHIFT, Category.MISC, "Settings for the Clickgui");
        ArrayList<String> options = new ArrayList<>();
        options.add("Arial");
        options.add("Impact");
        options.add("Times");
        options.add("MC");
        setmgr.add(Frame = new Setting("Frame_Font", this, "Times", options));
        setmgr.add(Button = new Setting("Button_Font", this, "Times", options));
        setmgr.add(Subcomponents = new Setting("Sub_Font", this, "Times", options));
        setmgr.add(Blur = new Setting("Blur", this, false));
        setmgr.add(Framecolor = new Setting("Frame", this, 0, .7, .65, .7));
        setmgr.add(Background = new Setting("Background", this, 0, 1, .01, .22));
        setmgr.add(Hover = new Setting("Hover", this, 0, 1, .01, .10));
        setmgr.add(ColorAni = new Setting("ColorAni", this, 0, 1, .5, .40));
        setmgr.add(innercolor = new Setting("innercolor", this, 0.68, .35, .05, .3));
        setmgr.add(border = new Setting("border", this, false));
        setmgr.add(Highlight = new Setting("Border Color", this, 0, 1, 1, .88, border));
        setmgr.add(Animations = new Setting("Animations", this, true));
        setmgr.add(Anispeed = new Setting("ButtonSpeed", this, 1.8, 0, 3, false));

    }


    @Override
    public void settingChanged(Component.ClickType visual) {
        if (!Blur.getValBoolean())
            mc.entityRenderer.stopUseShader();

        Method.Client.clickgui.component.Frame.updateFont();
        Method.Client.clickgui.component.components.Button.updateFont();
    }

    @Override
    public void setup() {
        this.visible = false;
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(Main.ClickGui);
        this.toggle();
        super.onEnable();
    }


}
