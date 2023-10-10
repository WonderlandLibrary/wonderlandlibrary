/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.ui.partcles;

import java.util.Random;
import me.AveReborn.ui.partcles.Particle;
import me.AveReborn.util.ClientUtil;
import me.AveReborn.util.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.util.vector.Vector2f;

public class ParticleSnow
extends Particle {
    private Random random = new Random();
    private ScaledResolution res;

    @Override
    public void draw(int xAdd) {
        this.prepare();
        this.move();
        this.drawPixel(xAdd);
        this.resetPos();
    }

    private void prepare() {
        this.res = new ScaledResolution(Minecraft.getMinecraft());
    }

    private void drawPixel(int xAdd) {
        float size = 10.0f;
        int i2 = 0;
        while (i2 < 10) {
            int alpha = Math.min(0, 1 - i2 / 10);
            Gui.drawFilledCircle(this.vector.x, this.vector.y, size + (1.0f + (float)i2 * 0.2f), ClientUtil.reAlpha(Colors.WHITE.c, alpha));
            ++i2;
        }
        Gui.drawFilledCircle(this.vector.x + (float)xAdd, this.vector.y, 1.1f, ClientUtil.reAlpha(-1, 0.2f));
        Gui.drawFilledCircle(this.vector.x + (float)xAdd, this.vector.y, 0.8f, ClientUtil.reAlpha(-1, 0.4f));
        Gui.drawFilledCircle(this.vector.x + (float)xAdd, this.vector.y, 0.5f, ClientUtil.reAlpha(-1, 0.6f));
        Gui.drawFilledCircle(this.vector.x + (float)xAdd, this.vector.y, 0.3f, ClientUtil.reAlpha(Colors.WHITE.c, 1.0f));
    }

    private void move() {
        float speed = 100.0f;
        this.vector.y += this.random.nextFloat() * 0.25f;
        this.vector.x -= this.random.nextFloat();
    }

    private void resetPos() {
        if (this.vector.x < 0.0f) {
            this.vector.x = this.res.getScaledWidth();
        }
        if (this.vector.y > (float)this.res.getScaledHeight()) {
            this.vector.y = 0.0f;
        }
    }
}

