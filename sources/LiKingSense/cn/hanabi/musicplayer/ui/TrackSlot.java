/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package cn.hanabi.musicplayer.ui;

import cn.hanabi.musicplayer.MusicManager;
import cn.hanabi.musicplayer.impl.Track;
import java.awt.Color;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;

public class TrackSlot {
    public Track track;
    public float x;
    public float y;

    public TrackSlot(Track t2) {
        this.track = t2;
    }

    public void render(float a, float b, int mouseX, int mouseY) {
        this.x = a;
        this.y = b;
        float f = this.x;
        float f2 = this.y;
        float f3 = this.x + 137.0f;
        float f4 = this.y + 20.0f;
        int n = 0;
        Minecraft.func_71410_x().field_71466_p.func_78276_b(this.track.name, (int)(this.x + 2.0f), (int)(this.y + 1.0f), Color.WHITE.getRGB());
        Minecraft.func_71410_x().field_71466_p.func_78276_b(this.track.artists, (int)(this.x + 2.0f), (int)(this.y + 10.0f), Color.WHITE.getRGB());
        float f5 = this.x + 122.0f;
        float f6 = this.y;
        float f7 = this.x + 137.0f;
        float f8 = this.y + 20.0f;
        int n2 = 0;
        Minecraft.func_71410_x().field_71466_p.func_78276_b(">", (int)(this.x + 125.5f), (int)(this.y + 5.5f), Color.WHITE.getRGB());
    }

    public void click(int mouseX, int mouseY, int mouseButton) {
        if (RenderUtils.isHovering(mouseX, mouseY, this.x + 125.0f, this.y + 5.0f, this.x + 135.0f, this.y + 15.0f) && mouseButton == 0) {
            try {
                MusicManager.INSTANCE.play(this.track);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

