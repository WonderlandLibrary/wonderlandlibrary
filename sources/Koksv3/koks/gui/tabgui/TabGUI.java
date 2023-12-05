package koks.gui.tabgui;

import koks.Koks;
import koks.api.interfaces.Wrapper;
import koks.manager.event.impl.EventKeyPress;
import koks.manager.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

/**
 * @author deleteboys | lmao | kroko
 * @created on 14.09.2020 : 12:40
 */
public class TabGUI implements Wrapper {

    private final ArrayList<DrawCategory> drawCategories = new ArrayList<>();
    private final Minecraft mc = Minecraft.getMinecraft();
    private final FontRenderer fr = mc.fontRendererObj;
    private int x, y, width, height, category;
    public Module.Category extendedCat;
    public int currentCategory;

    private double animateCat;

    public TabGUI() {
        for (Module.Category category : Module.Category.values()) {
            drawCategories.add(new DrawCategory(category));
        }
    }

    public void drawScreen(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        if (animateCat == 0)
            animateCat = y + currentCategory * height;

        int y2 = y;

        double shouldRender = y + currentCategory * height;

        if (animateCat < shouldRender)
            animateCat += 0.5;
        if (animateCat > shouldRender)
            animateCat -= 0.5;

        renderUtil.drawRect(x, y, x + width, y + height * drawCategories.size(), 0xBB000000);
        renderUtil.drawRect(x, animateCat, x + width, animateCat + height, Koks.getKoks().clientColor.getRGB());


        for (DrawCategory drawCategory : drawCategories) {
            drawCategory.drawScreen(x, y2, width, height);
            y2 += height;
        }
    }

    public void manageKeys(EventKeyPress eventKeyPress) {
        Keyboard.enableRepeatEvents(true);
        int key = eventKeyPress.getKey();

        for (DrawCategory drawCategory : drawCategories) {
            drawCategory.manageKeys(eventKeyPress);
        }
        if (extendedCat == null) {
            if (key == Keyboard.KEY_UP) {
                if (currentCategory > 0) {
                    currentCategory--;
                }
            } else if (key == Keyboard.KEY_DOWN) {
                if (currentCategory < Module.Category.values().length - 1) {
                    currentCategory++;
                }
            }
        }
    }

}