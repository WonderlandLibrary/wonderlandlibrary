package xyz.northclient.draggable.impl;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
import xyz.northclient.NorthSingleton;
import xyz.northclient.draggable.AbstractDraggable;
import xyz.northclient.features.AbstractModule;
import xyz.northclient.theme.ColorUtil;
import xyz.northclient.util.font.FontUtil;
import xyz.northclient.util.shader.RectUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Keybinds extends AbstractDraggable {
    public Keybinds() {
        super(false);
    }

    @Override
    public void Init() {
        super.Init();
        X = 5;
        Y = 100;
    }

    public int calc() {
        int cal = 0;
        List<AbstractModule> moduleList = new ArrayList<>(NorthSingleton.INSTANCE.getModules().getModules());
        for(AbstractModule mod : moduleList) {
            if(mod.getKeyCode() != 0) {
                cal+=11;
            }
        }
        return cal;
    }

    @Override
    public Vector2f Render() {
        int height = calc()+17;

        RectUtil.drawRoundedBloom(X,Y,100,height,8,16,new Color(0,0,0,190));
        RectUtil.drawRoundedRect(X,Y,100,height,8, new Color(0,0,0,190));

        FontUtil.DefaultSmallBold.drawString("Keybinds",X+5,Y+7,-1);

        int off = 15;
        List<AbstractModule> moduleList = new ArrayList<>(NorthSingleton.INSTANCE.getModules().getModules());
        for(AbstractModule mod : moduleList) {
            if(mod.getKeyCode() != 0) {
                FontUtil.DefaultSmall.drawString(mod.getName(),X+5,Y+7+off,mod.isEnabled() ? ColorUtil.GetColor((off/10)*150) : -1);
                FontUtil.DefaultSmallBold.drawString(Keyboard.getKeyName(mod.getKeyCode()),X+5 + 90 - FontUtil.DefaultSmallBold.getWidth(Keyboard.getKeyName(mod.getKeyCode())),Y+7+off,mod.isEnabled() ? ColorUtil.GetColor((off/10)*150) : -1);
                off+=10;
            }
        }
        return new Vector2f(100,height);
    }

    @Override
    public String getDraggableName() {
        return "Keybinds";
    }
}
