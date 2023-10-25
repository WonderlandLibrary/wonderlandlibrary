/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.misc;

import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.util.ColorUtils;
import me.Tengoku.Terror.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;

public class SuperHeroFX
extends Module {
    private int color = ColorUtils.getRainbow(4.0f, 0.4f, 1.0f);
    FontRenderer fr = Minecraft.fontRendererObj;
    Timer timer;
    ScaledResolution scaledresolution = new ScaledResolution(mc);

    public SuperHeroFX() {
        super("SuperHeroFX", 0, Category.MISC, "Discontinued module.");
        this.timer = new Timer();
    }
}

