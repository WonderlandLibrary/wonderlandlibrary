/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Ellipse;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.RoundedRectangle;

public class GeomAccuracyTest
extends BasicGame {
    private GameContainer container;
    private Color geomColor;
    private Color overlayColor;
    private boolean hideOverlay;
    private int colorIndex;
    private int curTest;
    private static final int NUMTESTS = 3;
    private Image magImage;

    public GeomAccuracyTest() {
        super("Geometry Accuracy Tests");
    }

    public void init(GameContainer container) throws SlickException {
        this.container = container;
        this.geomColor = Color.magenta;
        this.overlayColor = Color.white;
        this.magImage = new Image(21, 21);
    }

    public void render(GameContainer container, Graphics g2) {
        String text = new String();
        switch (this.curTest) {
            case 0: {
                text = "Rectangles";
                this.rectTest(g2);
                break;
            }
            case 1: {
                text = "Ovals";
                this.ovalTest(g2);
                break;
            }
            case 2: {
                text = "Arcs";
                this.arcTest(g2);
            }
        }
        g2.setColor(Color.white);
        g2.drawString("Press T to toggle overlay", 200.0f, 55.0f);
        g2.drawString("Press N to switch tests", 200.0f, 35.0f);
        g2.drawString("Press C to cycle drawing colors", 200.0f, 15.0f);
        g2.drawString("Current Test:", 400.0f, 35.0f);
        g2.setColor(Color.blue);
        g2.drawString(text, 485.0f, 35.0f);
        g2.setColor(Color.white);
        g2.drawString("Normal:", 10.0f, 150.0f);
        g2.drawString("Filled:", 10.0f, 300.0f);
        g2.drawString("Drawn with Graphics context", 125.0f, 400.0f);
        g2.drawString("Drawn using Shapes", 450.0f, 400.0f);
        g2.copyArea(this.magImage, container.getInput().getMouseX() - 10, container.getInput().getMouseY() - 10);
        this.magImage.draw(351.0f, 451.0f, 5.0f);
        g2.drawString("Mag Area -", 250.0f, 475.0f);
        g2.setColor(Color.darkGray);
        g2.drawRect(350.0f, 450.0f, 106.0f, 106.0f);
        g2.setColor(Color.white);
        g2.drawString("NOTE:", 500.0f, 450.0f);
        g2.drawString("lines should be flush with edges", 525.0f, 470.0f);
        g2.drawString("corners should be symetric", 525.0f, 490.0f);
    }

    void arcTest(Graphics g2) {
        if (!this.hideOverlay) {
            g2.setColor(this.overlayColor);
            g2.drawLine(198.0f, 100.0f, 198.0f, 198.0f);
            g2.drawLine(100.0f, 198.0f, 198.0f, 198.0f);
        }
        g2.setColor(this.geomColor);
        g2.drawArc(100.0f, 100.0f, 99.0f, 99.0f, 0.0f, 90.0f);
    }

    void ovalTest(Graphics g2) {
        g2.setColor(this.geomColor);
        g2.drawOval(100.0f, 100.0f, 99.0f, 99.0f);
        g2.fillOval(100.0f, 250.0f, 99.0f, 99.0f);
        Ellipse elip = new Ellipse(449.0f, 149.0f, 49.0f, 49.0f);
        g2.draw(elip);
        elip = new Ellipse(449.0f, 299.0f, 49.0f, 49.0f);
        g2.fill(elip);
        if (!this.hideOverlay) {
            g2.setColor(this.overlayColor);
            g2.drawLine(100.0f, 149.0f, 198.0f, 149.0f);
            g2.drawLine(149.0f, 100.0f, 149.0f, 198.0f);
            g2.drawLine(100.0f, 299.0f, 198.0f, 299.0f);
            g2.drawLine(149.0f, 250.0f, 149.0f, 348.0f);
            g2.drawLine(400.0f, 149.0f, 498.0f, 149.0f);
            g2.drawLine(449.0f, 100.0f, 449.0f, 198.0f);
            g2.drawLine(400.0f, 299.0f, 498.0f, 299.0f);
            g2.drawLine(449.0f, 250.0f, 449.0f, 348.0f);
        }
    }

    void rectTest(Graphics g2) {
        g2.setColor(this.geomColor);
        g2.drawRect(100.0f, 100.0f, 99.0f, 99.0f);
        g2.fillRect(100.0f, 250.0f, 99.0f, 99.0f);
        g2.drawRoundRect(250.0f, 100.0f, 99.0f, 99.0f, 10);
        g2.fillRoundRect(250.0f, 250.0f, 99.0f, 99.0f, 10);
        Rectangle rect = new Rectangle(400.0f, 100.0f, 99.0f, 99.0f);
        g2.draw(rect);
        rect = new Rectangle(400.0f, 250.0f, 99.0f, 99.0f);
        g2.fill(rect);
        RoundedRectangle rrect = new RoundedRectangle(550.0f, 100.0f, 99.0f, 99.0f, 10.0f);
        g2.draw(rrect);
        rrect = new RoundedRectangle(550.0f, 250.0f, 99.0f, 99.0f, 10.0f);
        g2.fill(rrect);
        if (!this.hideOverlay) {
            g2.setColor(this.overlayColor);
            g2.drawLine(100.0f, 149.0f, 198.0f, 149.0f);
            g2.drawLine(149.0f, 100.0f, 149.0f, 198.0f);
            g2.drawLine(250.0f, 149.0f, 348.0f, 149.0f);
            g2.drawLine(299.0f, 100.0f, 299.0f, 198.0f);
            g2.drawLine(400.0f, 149.0f, 498.0f, 149.0f);
            g2.drawLine(449.0f, 100.0f, 449.0f, 198.0f);
            g2.drawLine(550.0f, 149.0f, 648.0f, 149.0f);
            g2.drawLine(599.0f, 100.0f, 599.0f, 198.0f);
            g2.drawLine(100.0f, 299.0f, 198.0f, 299.0f);
            g2.drawLine(149.0f, 250.0f, 149.0f, 348.0f);
            g2.drawLine(250.0f, 299.0f, 348.0f, 299.0f);
            g2.drawLine(299.0f, 250.0f, 299.0f, 348.0f);
            g2.drawLine(400.0f, 299.0f, 498.0f, 299.0f);
            g2.drawLine(449.0f, 250.0f, 449.0f, 348.0f);
            g2.drawLine(550.0f, 299.0f, 648.0f, 299.0f);
            g2.drawLine(599.0f, 250.0f, 599.0f, 348.0f);
        }
    }

    public void update(GameContainer container, int delta) {
    }

    public void keyPressed(int key, char c2) {
        if (key == 1) {
            System.exit(0);
        }
        if (key == 49) {
            ++this.curTest;
            this.curTest %= 3;
        }
        if (key == 46) {
            ++this.colorIndex;
            this.colorIndex %= 4;
            this.setColors();
        }
        if (key == 20) {
            this.hideOverlay = !this.hideOverlay;
        }
    }

    private void setColors() {
        switch (this.colorIndex) {
            case 0: {
                this.overlayColor = Color.white;
                this.geomColor = Color.magenta;
                break;
            }
            case 1: {
                this.overlayColor = Color.magenta;
                this.geomColor = Color.white;
                break;
            }
            case 2: {
                this.overlayColor = Color.red;
                this.geomColor = Color.green;
                break;
            }
            case 3: {
                this.overlayColor = Color.red;
                this.geomColor = Color.white;
            }
        }
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new GeomAccuracyTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e2) {
            e2.printStackTrace();
        }
    }
}

