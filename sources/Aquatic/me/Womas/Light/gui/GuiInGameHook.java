package me.Womas.Light.gui;

import javafx.scene.effect.Light;
import me.Womas.Light.Core.Aquatic;
import me.Womas.Light.Module.Template;
import me.Womas.Light.utils.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;

public class GuiInGameHook extends GuiIngame {

    public GuiInGameHook(Minecraft mcIn) {
        super(mcIn);
    }

    public void renderGameOverlay(float p_175100_1_) {
        super.renderGameOverlay(p_175100_1_);
        Wrapper.fr.drawString(Aquatic.FULLNAME, 1, 1, 0x102a45);
        renderArray();
    }

    public void renderArray() {
        int distance = 10;
        for (Template m : Aquatic.getInstance().getModuleManager().moduleList) {
            m.onRender();
            if(m.isToggled())
                Wrapper.fr.drawString(m.getName(), 2, distance, 0x00ff7f);

            distance += 10;
        }

    }
}