package me.aquavit.liquidsense.ui.client.hud.element;

import me.aquavit.liquidsense.utils.render.RenderUtils;

import java.awt.*;

public class Border {

	public float x;
	public float y;
	public float x2;
	public float y2;

    public void draw() {
        RenderUtils.drawRectBordered(this.x, this.y, this.x2, this.y2, 1.0F, new Color(15 , 15 , 15 , 40).getRGB(), Integer.MIN_VALUE);
    }

    public Border(float x, float y, float x2, float y2) {
        this.x = x;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;
    }
}
