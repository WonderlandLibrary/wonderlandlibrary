/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.tests;

import java.util.ArrayList;
import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class FontPerformanceTest
extends BasicGame {
    private AngelCodeFont font;
    private String text = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Proin bibendum. Aliquam ac sapien a elit congue iaculis. Quisque et justo quis mi mattis euismod. Donec elementum, mi quis aliquet varius, nisi leo volutpat magna, quis ultricies eros augue at risus. Integer non magna at lorem sodales molestie. Integer diam nulla, ornare sit amet, mattis quis, euismod et, mauris. Proin eget tellus non nisl mattis laoreet. Nunc at nunc id elit pretium tempor. Duis vulputate, nibh eget rhoncus eleifend, tellus lectus sollicitudin mi, rhoncus tincidunt nisi massa vitae ipsum. Praesent tellus diam, luctus ut, eleifend nec, auctor et, orci. Praesent eu elit. Pellentesque ante orci, volutpat placerat, ornare eget, cursus sit amet, eros. Duis pede sapien, euismod a, volutpat pellentesque, convallis eu, mauris. Nunc eros. Ut eu risus et felis laoreet viverra. Curabitur a metus.";
    private ArrayList lines = new ArrayList();
    private boolean visible = true;

    public FontPerformanceTest() {
        super("Font Performance Test");
    }

    public void init(GameContainer container) throws SlickException {
        this.font = new AngelCodeFont("testdata/perffont.fnt", "testdata/perffont.png");
        for (int j2 = 0; j2 < 2; ++j2) {
            int lineLen = 90;
            for (int i2 = 0; i2 < this.text.length(); i2 += lineLen) {
                if (i2 + lineLen > this.text.length()) {
                    lineLen = this.text.length() - i2;
                }
                this.lines.add(this.text.substring(i2, i2 + lineLen));
            }
            this.lines.add("");
        }
    }

    public void render(GameContainer container, Graphics g2) {
        g2.setFont(this.font);
        if (this.visible) {
            for (int i2 = 0; i2 < this.lines.size(); ++i2) {
                this.font.drawString(10.0f, 50 + i2 * 20, (String)this.lines.get(i2), i2 > 10 ? Color.red : Color.green);
            }
        }
    }

    public void update(GameContainer container, int delta) throws SlickException {
    }

    public void keyPressed(int key, char c2) {
        if (key == 1) {
            System.exit(0);
        }
        if (key == 57) {
            this.visible = !this.visible;
        }
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new FontPerformanceTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e2) {
            e2.printStackTrace();
        }
    }
}

