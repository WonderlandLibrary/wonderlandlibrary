package dev.monsoon.ui.clickgui.dropdown1.api;


import dev.monsoon.Monsoon;
import dev.monsoon.module.base.Module;
import dev.monsoon.module.enums.Category;
import dev.monsoon.module.setting.Setting;
import dev.monsoon.util.render.Particles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;

public class CGUI extends GuiScreen
{

    public static class ModuleComparator implements Comparator<Module> {
        @Override
        public int compare(Module arg0, Module arg1) {
            if(Minecraft.getMinecraft().fontRendererObj.getStringWidth(arg0.getDisplayname()) > Minecraft.getMinecraft().fontRendererObj.getStringWidth(arg1.getName())) {
                return -1;
            }
            if(Minecraft.getMinecraft().fontRendererObj.getStringWidth(arg0.getDisplayname()) < Minecraft.getMinecraft().fontRendererObj.getStringWidth(arg1.getName())) {
                return 1;
            }
            return 0;
        }
    }

    private final int width;
    private final int height;

    public CGUI(int width, int height)
    {
        Collections.sort(Monsoon.modules, new CGUI.ModuleComparator());
        this.width = width;
        this.height = height;

        int xOffset = 3;
        for (Category category : Category.values())
        {
            category.setOpen(true);
            category.setX(xOffset);
            category.setY(3);
            xOffset += width + 3;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        Monsoon.manager.modules.sort(Comparator.comparingDouble(m -> {
                    String modulesText = ((Module) m).getName();

                    return mc.fontRendererObj.getStringWidth(modulesText);

                })
                        .reversed()
        );
        Monsoon.getClickGUI().drawScreen(mouseX, mouseY, partialTicks);

        Particles particles = new Particles(100);
        particles.render();
        for (Category category : Category.values())
        {
            if (category == Category.HIDDEN) continue;

            int x = category.getX();
            int y = category.getY();

           // Monsoon.getClickGUI().drawCategory(x, y, width, height, mouseX, mouseY, category);

            if (!category.isOpen()) continue;

            for (Module module : Monsoon.getModulesByCategory(category))
            {
                y += height;

                Monsoon.getClickGUI().drawModule(x, y, width, height, mouseX, mouseY, module);

                if (!module.isOpen()) continue;

                for (Setting setting : module.settings)
                {
                    if(setting.shouldRender) {
                        y += height;

                        Monsoon.getClickGUI().drawSetting(x, y, width, height, mouseX, mouseY, setting);
                    }
                }
            }
            Monsoon.getClickGUI().drawCategory(x, category.getY(), width, height, mouseX, mouseY, category);
        }

    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
    {
        for (Category category : Category.values())
        {
            int x = category.getX();
            int y = category.getY();
            if (isHover(x, y, width, height, mouseX, mouseY)) Monsoon.getClickGUI().clickCategory(mouseX, mouseY, mouseButton, category);

            if (!category.isOpen()) continue;

            for (Module module : Monsoon.getModulesByCategory(category))
            {
                y += height;

                if (isHover(x, y, width, height, mouseX, mouseY)) Monsoon.getClickGUI().clickModule(mouseX, mouseY, mouseButton, module);

                if (!module.isOpen()) continue;

                for (Setting setting : module.settings)
                {
                    if(setting.shouldRender) {
                        y += height;

                        if (isHover(x, y, width, height, mouseX, mouseY))
                            Monsoon.getClickGUI().clickSetting(mouseX, mouseY, mouseButton, setting);
                    }
                }
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state)
    {
        Monsoon.getClickGUI().mouseReleased(mouseX, mouseY, state);
    }


    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        super.keyTyped(typedChar, keyCode);

        Monsoon.getClickGUI().keyTyped(typedChar, keyCode);
    }

    @Override
    public void initGui()
    {
        Monsoon.getClickGUI().onOpen();
    }

    @Override
    public void onGuiClosed()
    {
        Monsoon.getClickGUI().onClose();
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return Monsoon.getClickGUI().doesPauseGame();
    }

    public static boolean isHover(int X, int Y, int W, int H, int mX, int mY)
    {
        return mX >= X && mX <= X + W && mY >= Y && mY <= Y + H;
    }
}
