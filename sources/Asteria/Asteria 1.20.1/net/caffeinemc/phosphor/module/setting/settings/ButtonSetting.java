package net.caffeinemc.phosphor.module.setting.settings;

import imgui.ImGui;
import net.caffeinemc.phosphor.module.Module;
import net.caffeinemc.phosphor.module.setting.RenderableSetting;
import net.caffeinemc.phosphor.module.setting.Setting;

public class ButtonSetting extends Setting implements RenderableSetting {
    public Runnable runnable;
    public boolean sameLine;

    public ButtonSetting(String name, Module parent, Runnable runnable) {
        this.name = name;
        this.parent = parent;
        this.runnable = runnable;
        this.sameLine = false;

        parent.addSettings(this);
    }

    public ButtonSetting(String name, Module parent, Runnable runnable, boolean sameLine) {
        this.name = name;
        this.parent = parent;
        this.runnable = runnable;
        this.sameLine = sameLine;

        parent.addSettings(this);
    }

    @Override
    public void render() {
        ImGui.pushID(parent.getName()+"/"+this.getName());

        if (ImGui.button(name))
            runnable.run();

        if (sameLine)
            ImGui.sameLine();

        ImGui.popID();
    }
}
