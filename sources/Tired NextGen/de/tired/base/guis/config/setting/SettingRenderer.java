package de.tired.base.guis.config.setting;

import de.tired.util.render.shaderloader.ShaderManager;
import de.tired.util.render.shaderloader.list.RoundedRectShader;

import java.awt.*;

public class SettingRenderer {

    private int x, y, mouseX, mouseY, width, height;

    public void render(int x, int y, int mouseX, int mouseY, int width, int height) {
        this.x = x;
        this.y = y;
        this.mouseX = mouseX;
        this.mouseY = mouseY;

        ShaderManager.shaderBy(RoundedRectShader.class).drawRound(x, y, width, height, 3, new Color(248, 248, 248));



    }

}
