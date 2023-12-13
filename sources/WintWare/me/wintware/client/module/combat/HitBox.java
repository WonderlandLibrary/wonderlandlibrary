/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.combat;

import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.utils.other.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class HitBox
extends Module {
    public static Setting expand;

    public HitBox() {
        super("HitBox", Category.Combat);
        expand = new Setting("Expand", this, 0.0, 0.0, 2.0, false);
        Main.instance.setmgr.rSetting(expand);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setSuffix("" + MathUtils.round(expand.getValFloat(), 1));
    }

    public static float expand(Entity entity) {
        if (!entity.equals(Minecraft.player) && Main.instance.moduleManager.getModuleByClass(HitBox.class).getState()) {
            return (float)expand.getValDouble();
        }
        return 0.0f;
    }
}

