/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.visual;

import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventTransformSideFirstPerson;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;

public class ViewModel
extends Module {
    public static Setting rightx;
    public static Setting righty;
    public static Setting rightz;
    public static Setting leftx;
    public static Setting lefty;
    public static Setting leftz;

    public ViewModel() {
        super("ViewModel", Category.Visuals);
        rightx = new Setting("RightX", this, 0.0, -2.0, 2.0, false);
        Main.instance.setmgr.rSetting(rightx);
        righty = new Setting("RightY", this, 0.2, -2.0, 2.0, false);
        Main.instance.setmgr.rSetting(righty);
        rightz = new Setting("RightZ", this, 0.2, -2.0, 2.0, false);
        Main.instance.setmgr.rSetting(rightz);
        leftx = new Setting("LeftX", this, 0.0, -2.0, 2.0, false);
        Main.instance.setmgr.rSetting(leftx);
        lefty = new Setting("LeftY", this, 0.2, -2.0, 2.0, false);
        Main.instance.setmgr.rSetting(lefty);
        leftz = new Setting("LeftZ", this, 0.2, -2.0, 2.0, false);
        Main.instance.setmgr.rSetting(leftz);
    }

    @EventTarget
    public void onSidePerson(EventTransformSideFirstPerson event) {
        if (event.getEnumHandSide() == EnumHandSide.RIGHT) {
            GlStateManager.translate(rightx.getValDouble(), righty.getValDouble(), rightz.getValDouble());
        }
        if (event.getEnumHandSide() == EnumHandSide.LEFT) {
            GlStateManager.translate(-leftx.getValDouble(), lefty.getValDouble(), leftz.getValDouble());
        }
    }
}

