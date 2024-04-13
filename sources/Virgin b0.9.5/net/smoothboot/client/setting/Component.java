package net.smoothboot.client.setting;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.smoothboot.client.hud.modbutton;
import net.smoothboot.client.module.settings.Setting;

;

public class Component {

    public Setting setting;
    public modbutton parent;
    public int offset;

    protected MinecraftClient mc = MinecraftClient.getInstance();

    public Component(Setting setting, modbutton parent, int offset) {
        this.setting = setting;
        this.parent = parent;
        this.offset = offset;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {

    }

    public void mouseClicked(double mouseX, double mouseY, int button) {

    }

    public void mouseReleased(double mouseX, double mouseY, int button) {

    }

    public boolean isHovered(double mouseX, double mouseY) {
        return mouseX > parent.parent.x && mouseX < parent.parent.x + parent.parent.width && mouseY > parent.parent.y + parent.offset + offset && mouseY < parent.parent.y + parent.offset + offset + parent.parent.height;
    }

    public void keyPressed(int key) {

    }
}