package me.offeex.bloomware.api.gui.screen;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.gui.screen.frame.Frame;
import me.offeex.bloomware.api.gui.screen.frame.component.Component;
import me.offeex.bloomware.api.gui.screen.particle.ParticleManager;
import me.offeex.bloomware.api.manager.managers.FontManagerr;
import me.offeex.bloomware.api.structure.ColorMutable;
import me.offeex.bloomware.api.util.ClientUtil;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.module.client.Colors;
import me.offeex.bloomware.client.module.client.Gui;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.Platform;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class ClickGUI extends Screen {
    public static Object dragging = null;
    public TextFieldWidget searchField;
    private static List<Frame> frames;
    protected final ParticleManager particleManager;

    public ClickGUI() throws IOException {
        super(Text.literal("Bloomware Gui"));
        frames = new ArrayList<>();
        int frameX = 8;
        int frameY = 12;

//        Bloomware.INSTANCE.setCurrentScreen(new LoginScreen());

        particleManager = new ParticleManager();
        searchField = new TextFieldWidget(Bloomware.INSTANCE.getMc().textRenderer, 35 + 50, Bloomware.INSTANCE.getMc().getWindow().getScaledHeight() - 20, 150, 17, Text.of(""));
        searchField.setSuggestion("Search...");
        searchField.setMaxLength(30);
        addDrawableChild(searchField);
        /*
            Описание алгоритма сортировки окон
            getStrCount() - рассчитывает количество "этажей" для фреймов.
            calculateMaxElements() - рассчитывает максимальное количество фреймов на одной строке, чтобы они не вылезали за экран.
        */

        for (int i = 0; i < (getStrCount() + 1) * 2; i++) {      // Начинаем итерироваться по "этажам" ((getStrCount() + 1) * 2 не трогать, так надо)
            for (int j = 0; j < calculateMaxElements(); j++) {      // Итерируемся по каждому этажу
                int index = j + (i * calculateMaxElements());     // Получаем индекс относительно "матрицы"
                if (Module.Category.values().length > index
                        && Module.Category.values()[index] != Module.Category.HUD) { // Избегаем лишних итераций
                    Frame frame = new Frame(Module.Category.values()[index]);     // Создаем фрейм с определенным индексом
                    frames.add(frame);     // Добавляем его в аррейлист
                    /*
                        Установка координаты X
                        Смотрим, есть ли этажи выше или нет (i == 0)
                        Если нет, то мы начинаем выставлять оффсет (frameX + ((frame.getWidth() + 12) * j))
                        Если есть, то мы берем X с фрейма, который находится над нашим.
                    */
                    frames.get(index).setX(i == 0 ? frameX + (frameX + frame.getWidth()) * j : frames.get(j).getX());
                    /*
                        Установка координаты Y
                        Смотрим, есть ли этажи выше или нет (i == 0)
                        Если нет, то мы ставим дефолтный Y (frameY)
                        Если есть, то нам надо найти высоту, и координату Y фрейма, который находится над нашим
                        Это можно сделать с помощью формулы (frames.get(index - calculateMaxElements()))
                    */
                    int shouldOffsetY = i == 0 ? frameY : frames.get(index - calculateMaxElements()).getY() + frames.get(index - calculateMaxElements()).getHeight();
                    frames.get(index).setY(shouldOffsetY + 3);
                }
            }
        }
    }

    public List<Frame> getFrames() {
        return frames;
    }

    private short calculateMaxElements() {
        if (ClientUtil.INSTANCE.getSystem() == Platform.MACOSX) {
            return 1440;
        }
        return (short) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / (24 + 110) / 2);
    }

    private int getStrCount() {
        return Module.Category.values().length / calculateMaxElements() + Module.Category.values().length % calculateMaxElements() == 0 ? 0 : 1;
    }

    @Override
    public void init() {
        frames.forEach(f -> f.getButtons().forEach(m -> m.setPressed(false)));
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    public Frame getFrameByCategory(String category) {
        return frames.stream().filter(frame -> frame.category.getTitle().equalsIgnoreCase(category)).findAny().orElse(null);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float tickDelta) {
        Gui module = Gui.INSTANCE;
        DrawableHelper.fill(matrices, 0, 0, Bloomware.INSTANCE.getMc().getWindow().getScaledWidth(), Bloomware.INSTANCE.getMc().getWindow().getScaledHeight(), Colors.INSTANCE.getBg().getColor().getArgb());
        if (module.getParticles().getToggled()) particleManager.render(matrices);

        fillGradient(matrices, 0, 0, Bloomware.INSTANCE.getMc().getWindow().getScaledWidth(), Bloomware.INSTANCE.getMc().getWindow().getScaledHeight(), module.getHueUp().getColor().getArgb(), ColorMutable.Companion.getEMPTY().getArgb());
        fillGradient(matrices, Bloomware.INSTANCE.getMc().getWindow().getScaledWidth(), Bloomware.INSTANCE.getMc().getWindow().getScaledHeight(), 0, 0, module.getHueDown().getColor().getArgb(), ColorMutable.Companion.getEMPTY().getArgb());

        frames.forEach(frame -> {
            frame.renderFrame(matrices);
            frame.updatePosition(mouseX, mouseY);
            frame.getButtons().forEach(c -> c.updateComponent(mouseX, mouseY));
        });
        Bloomware.INSTANCE.getHud().getToolBar().updateProperties(0, Bloomware.INSTANCE.getMc().getWindow().getScaledHeight() - 25, Bloomware.INSTANCE.getMc().getWindow().getScaledWidth(), Bloomware.INSTANCE.getMc().getWindow().getScaledHeight());
        Bloomware.INSTANCE.getHud().getToolBar().render(matrices);
        Bloomware.INSTANCE.getHud().getToolBar().getComponents().forEach(component -> {
            component.render(matrices);
            component.updateComponent(mouseX, mouseY);
        });
        searchField.setX(35 + (int) FontManagerr.INSTANCE.width(Bloomware.NAME));
        searchField.setY(Bloomware.INSTANCE.getMc().getWindow().getScaledHeight() - 20);
        searchField.render(matrices, mouseX, mouseY, tickDelta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        for (int i = frames.size() - 1; i >= 0; i--) {
            Frame frame = frames.get(i);
            if (frame.isHovered(mouseX, mouseY) && mouseButton == 0) {
                frame.setDrag(true);
                frames.remove(i);
                frames.add(frame);
                frame.dragX = (int) (mouseX - frame.getX());
                frame.dragY = (int) (mouseY - frame.getY());
            }
            if (frame.isHovered(mouseX, mouseY) && mouseButton == 1) frame.setOpen(!frame.isOpen());
            if (frame.isOpen() && !frame.getButtons().isEmpty())
                frame.getButtons().forEach(button -> button.mouseClicked(mouseX, mouseY, mouseButton));
        }
        Bloomware.INSTANCE.getHud().getToolBar().getComponents().forEach(component -> component.mouseClicked(mouseX, mouseY, mouseButton));
        searchField.mouseClicked(mouseX, mouseY, mouseButton);

//        FontManagerr.INSTANCE.setFont(new NewFontRenderer(new Font(Font.MONOSPACED, Font.PLAIN, 48), new Integer[]{0}));

        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        for (int i = frames.size() - 1; i >= 0; i--) {
            Frame frame = frames.get(i);
            frame.setDrag(false);
        }
        for (int i = frames.size() - 1; i >= 0; i--) {
            Frame frame = frames.get(i);
            frame.getButtons().stream().filter(b -> frame.isOpen() && !frame.getButtons().isEmpty()).forEach(b -> b.mouseReleased(mouseX, mouseY, mouseButton));
        }
        Bloomware.INSTANCE.getHud().getToolBar().getComponents().forEach(component -> component.mouseReleased(mouseX, mouseY, mouseButton));
        searchField.mouseReleased(mouseX, mouseY, mouseButton);
        return super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (Frame frame : frames) {
            if (!frame.isOpen() || keyCode == 1 || frame.getButtons().isEmpty()) continue;
            for (Component button : frame.getButtons()) button.keyTyped(keyCode);
        }
        Bloomware.INSTANCE.getHud().getToolBar().getComponents().forEach(c -> c.keyTyped(keyCode));
        if (keyCode != GLFW.GLFW_KEY_UP && keyCode != GLFW.GLFW_KEY_DOWN && keyCode != GLFW.GLFW_KEY_LEFT && keyCode != GLFW.GLFW_KEY_RIGHT) {
            searchField.keyPressed(keyCode, scanCode, modifiers);
        }
        searchField.setSuggestion(searchField.getText().equals("") ? "Search..." : "");
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char c, int i) {
        searchField.setSuggestion(searchField.getText().equals("") ? "Search..." : "");
        return super.charTyped(c, i);
    }

    public static void fillGradient(MatrixStack matrix, int x, int y, int x1, int y1, int color, int color1) {
        DrawableHelper.fillGradient(matrix, x, y, x1, y1, color, color1);
    }
}
