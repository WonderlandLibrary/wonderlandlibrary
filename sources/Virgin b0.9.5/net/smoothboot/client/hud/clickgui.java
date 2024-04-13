package net.smoothboot.client.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.smoothboot.client.module.Mod;

import java.util.ArrayList;
import java.util.List;

public class clickgui extends Screen {

    public static final clickgui INSTANCE = new clickgui();
    private final List<frame> frame;

    protected MinecraftClient mc = MinecraftClient.getInstance();

    private clickgui() {
        super(Text.literal("Click GUI"));
        frame = new ArrayList<>();
        int offset= 15;
        for (Mod.Category category : Mod.Category.values()) {
                frame.add(new frame(category, offset, 15, 105, 15));
                offset += 110;
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        for (frame frame : frame) {
            frame.render(context,mouseX,mouseY,delta);
            frame.updatePosition(mouseX, mouseY);
        }
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (frame frame : frame){
            frame.mouseClicked(mouseX, mouseY, button);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (frame frame : frame){
            frame.mouseReleased(mouseX, mouseY, button);
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (frame frame : frame) {
            frame.keyPressed(keyCode);
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}