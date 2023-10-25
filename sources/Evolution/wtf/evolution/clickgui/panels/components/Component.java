package wtf.evolution.clickgui.panels.components;

import wtf.evolution.module.Module;

public class Component {

    public Module module;

    public float x, y, width, height;
    public boolean opened;

    public Component(Module module, float x, float y, float width, float height) {
        this.module = module;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void render(int mouseX, int mouseY) {

    }

    public void click(int mouseX, int mouseY, int button) {

    }

}
