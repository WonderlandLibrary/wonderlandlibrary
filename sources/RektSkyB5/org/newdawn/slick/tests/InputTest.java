/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.tests;

import java.util.ArrayList;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

public class InputTest
extends BasicGame {
    private String message = "Press any key, mouse button, or drag the mouse";
    private ArrayList lines = new ArrayList();
    private boolean buttonDown;
    private float x;
    private float y;
    private Color[] cols = new Color[]{Color.red, Color.green, Color.blue, Color.white, Color.magenta, Color.cyan};
    private int index;
    private Input input;
    private int ypos;
    private AppGameContainer app;
    private boolean space;
    private boolean lshift;
    private boolean rshift;

    public InputTest() {
        super("Input Test");
    }

    public void init(GameContainer container) throws SlickException {
        if (container instanceof AppGameContainer) {
            this.app = (AppGameContainer)container;
        }
        this.input = container.getInput();
        this.x = 300.0f;
        this.y = 300.0f;
    }

    public void render(GameContainer container, Graphics g2) {
        g2.drawString("left shift down: " + this.lshift, 100.0f, 240.0f);
        g2.drawString("right shift down: " + this.rshift, 100.0f, 260.0f);
        g2.drawString("space down: " + this.space, 100.0f, 280.0f);
        g2.setColor(Color.white);
        g2.drawString(this.message, 10.0f, 50.0f);
        g2.drawString("" + container.getInput().getMouseY(), 10.0f, 400.0f);
        g2.drawString("Use the primary gamepad to control the blob, and hit a gamepad button to change the color", 10.0f, 90.0f);
        for (int i2 = 0; i2 < this.lines.size(); ++i2) {
            Line line = (Line)this.lines.get(i2);
            line.draw(g2);
        }
        g2.setColor(this.cols[this.index]);
        g2.fillOval((int)this.x, (int)this.y, 50.0f, 50.0f);
        g2.setColor(Color.yellow);
        g2.fillRect(50.0f, 200 + this.ypos, 40.0f, 40.0f);
    }

    public void update(GameContainer container, int delta) {
        this.lshift = container.getInput().isKeyDown(42);
        this.rshift = container.getInput().isKeyDown(54);
        this.space = container.getInput().isKeyDown(57);
        if (this.controllerLeft[0]) {
            this.x -= (float)delta * 0.1f;
        }
        if (this.controllerRight[0]) {
            this.x += (float)delta * 0.1f;
        }
        if (this.controllerUp[0]) {
            this.y -= (float)delta * 0.1f;
        }
        if (this.controllerDown[0]) {
            this.y += (float)delta * 0.1f;
        }
    }

    public void keyPressed(int key, char c2) {
        if (key == 1) {
            System.exit(0);
        }
        if (key == 59 && this.app != null) {
            try {
                this.app.setDisplayMode(600, 600, false);
                this.app.reinit();
            }
            catch (Exception e2) {
                Log.error(e2);
            }
        }
    }

    public void keyReleased(int key, char c2) {
        this.message = "You pressed key code " + key + " (character = " + c2 + ")";
    }

    public void mousePressed(int button, int x2, int y2) {
        if (button == 0) {
            this.buttonDown = true;
        }
        this.message = "Mouse pressed " + button + " " + x2 + "," + y2;
    }

    public void mouseReleased(int button, int x2, int y2) {
        if (button == 0) {
            this.buttonDown = false;
        }
        this.message = "Mouse released " + button + " " + x2 + "," + y2;
    }

    public void mouseClicked(int button, int x2, int y2, int clickCount) {
        System.out.println("CLICKED:" + x2 + "," + y2 + " " + clickCount);
    }

    public void mouseWheelMoved(int change) {
        this.message = "Mouse wheel moved: " + change;
        if (change < 0) {
            this.ypos -= 10;
        }
        if (change > 0) {
            this.ypos += 10;
        }
    }

    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        if (this.buttonDown) {
            this.lines.add(new Line(oldx, oldy, newx, newy));
        }
    }

    public void controllerButtonPressed(int controller, int button) {
        super.controllerButtonPressed(controller, button);
        ++this.index;
        this.index %= this.cols.length;
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new InputTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e2) {
            e2.printStackTrace();
        }
    }

    private class Line {
        private int oldx;
        private int oldy;
        private int newx;
        private int newy;

        public Line(int oldx, int oldy, int newx, int newy) {
            this.oldx = oldx;
            this.oldy = oldy;
            this.newx = newx;
            this.newy = newy;
        }

        public void draw(Graphics g2) {
            g2.drawLine(this.oldx, this.oldy, this.newx, this.newy);
        }
    }
}

