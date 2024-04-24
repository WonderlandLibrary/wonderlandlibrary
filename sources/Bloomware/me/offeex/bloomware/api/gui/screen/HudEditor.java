package me.offeex.bloomware.api.gui.screen;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.gui.screen.frame.Frame;
import me.offeex.bloomware.api.gui.screen.frame.component.Component;
import me.offeex.bloomware.api.gui.screen.frame.component.components.ToolBar;
import me.offeex.bloomware.api.gui.screen.frame.component.components.toolbar.ScreenSwitchButton;
import me.offeex.bloomware.api.manager.managers.ModuleManager;
import me.offeex.bloomware.api.structure.ColorMutable;
import me.offeex.bloomware.client.module.HudModule;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.module.client.Gui;
import me.offeex.bloomware.client.module.client.Hud;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.awt.*;



public class HudEditor extends Screen {
    private final Frame frame;
    private final ToolBar toolBar;
    private final ColorMutable hitboxColor = new ColorMutable(0, 0, 0, 80);

    public HudEditor() {
        super(Text.literal("Bloomware Gui"));
        int frameX = 10;
        frame = new Frame(Module.Category.HUD);
        frame.setX(frameX);
        toolBar = new ToolBar(0, 0, 0, 0);
        toolBar.addComponent(new ScreenSwitchButton(25, 25, 25, getIcon("elements/gui/toolbar/hudeditor.png"), Hud.INSTANCE));
        toolBar.addComponent(new ScreenSwitchButton(25, 25, 50, getIcon("elements/gui/toolbar/clickgui.png"), Gui.INSTANCE));
//        toolBar.addComponent(new ScreenSwitchButton(25, 25, 75, getIcon("elements/gui/toolbar/accounts.png"), ModuleManager.INSTANCE.getModule(Accounts.class)));
    }

    @Override
    protected void init() {
        super.init();
        frame.getButtons().forEach(m -> m.setPressed(false));
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    public static Identifier getIcon(String path) {
        return new Identifier("bloomware", path);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float tickDelta) {
        Gui module = Gui.INSTANCE;
        if (module.getParticles().getToggled()) Bloomware.INSTANCE.getGui().particleManager.render(matrices);
        DrawableHelper.fill(matrices, 0, 0, Bloomware.INSTANCE.getMc().getWindow().getScaledWidth(), Bloomware.INSTANCE.getMc().getWindow().getScaledHeight(), new Color(0, 0, 0, 100).getRGB());
        frame.renderFrame(matrices);
        frame.updatePosition(mouseX, mouseY);
        frame.getButtons().forEach(c -> c.updateComponent(mouseX, mouseY));
        ModuleManager.INSTANCE.getModules().stream().filter(HudModule.class::isInstance).map(HudModule.class::cast).forEach(m -> {
            if (m.getEnabled() && Bloomware.INSTANCE.getMc().player != null) {
                DrawableHelper.fill(matrices, m.getX() - 3, m.getY() - 3, m.getX() + m.getWidth() + 3, m.getY() + m.getHeight(), hitboxColor.getArgb());
                m.draw(matrices, tickDelta);
                m.updatePosition(mouseX, mouseY);
            }
        });
        toolBar.updateProperties(0, Bloomware.INSTANCE.getMc().getWindow().getScaledHeight() - 25, Bloomware.INSTANCE.getMc().getWindow().getScaledWidth(), Bloomware.INSTANCE.getMc().getWindow().getScaledHeight());
        toolBar.render(matrices);
        toolBar.getComponents().forEach(component -> {
            component.render(matrices);
            component.updateComponent(mouseX, mouseY);
        });
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (frame.isHovered(mouseX, mouseY) && mouseButton == 0) {
            frame.setDrag(true);
            frame.dragX = (int) (mouseX - frame.getX());
            frame.dragY = (int) (mouseY - frame.getY());
        }

        ModuleManager.INSTANCE.getModules().stream().filter(HudModule.class::isInstance).map(HudModule.class::cast).forEach(m -> {
            if (m.isHovered(mouseX, mouseY) && mouseButton == 0 && m.getEnabled()) {
                m.setDragging(true);
                m.setDragX((int) (mouseX - m.getX()));
                m.setDragY((int) (mouseY - m.getY()));
            }
        });
        if (frame.isHovered(mouseX, mouseY) && mouseButton == 1) frame.setOpen(!frame.isOpen());
        if (frame.isOpen() && !frame.getButtons().isEmpty())
            frame.getButtons().forEach(button -> button.mouseClicked(mouseX, mouseY, mouseButton));
        toolBar.getComponents().forEach(component -> component.mouseClicked(mouseX, mouseY, mouseButton));
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        ModuleManager.INSTANCE.getModules().stream().filter(HudModule.class::isInstance).map(HudModule.class::cast).forEach(m -> m.setDragging(false));
        frame.setDrag(false);
        frame.getButtons().stream().filter(b -> frame.isOpen() && !frame.getButtons().isEmpty()).forEach(b -> b.mouseReleased(mouseX, mouseY, mouseButton));
        toolBar.getComponents().forEach(component -> component.mouseReleased(mouseX, mouseY, mouseButton));
        return super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (frame.isOpen() && keyCode != 1 && !frame.getButtons().isEmpty()) {
            for (Component button : frame.getButtons()) button.keyTyped(keyCode);
        }
        toolBar.getComponents().forEach(component -> component.keyTyped(keyCode));
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public Frame getFrame() {
        return frame;
    }

    public ToolBar getToolBar() {
        return toolBar;
    }
}
