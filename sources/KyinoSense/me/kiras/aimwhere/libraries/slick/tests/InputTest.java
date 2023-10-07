/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.FMLCommonHandler
 */
package me.kiras.aimwhere.libraries.slick.tests;

import java.util.ArrayList;
import me.kiras.aimwhere.libraries.slick.AppGameContainer;
import me.kiras.aimwhere.libraries.slick.BasicGame;
import me.kiras.aimwhere.libraries.slick.Color;
import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.Input;
import me.kiras.aimwhere.libraries.slick.SlickException;
import me.kiras.aimwhere.libraries.slick.util.Log;
import net.minecraftforge.fml.common.FMLCommonHandler;

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

    @Override
    public void init(GameContainer container) throws SlickException {
        if (container instanceof AppGameContainer) {
            this.app = (AppGameContainer)container;
        }
        this.input = container.getInput();
        this.x = 300.0f;
        this.y = 300.0f;
    }

    @Override
    public void render(GameContainer container, Graphics g) {
        g.drawString("left shift down: " + this.lshift, 100.0f, 240.0f);
        g.drawString("right shift down: " + this.rshift, 100.0f, 260.0f);
        g.drawString("space down: " + this.space, 100.0f, 280.0f);
        g.setColor(Color.white);
        g.drawString(this.message, 10.0f, 50.0f);
        g.drawString("" + container.getInput().getMouseY(), 10.0f, 400.0f);
        g.drawString("Use the primary gamepad to control the blob, and hit a gamepad button to change the color", 10.0f, 90.0f);
        for (int i = 0; i < this.lines.size(); ++i) {
            Line line = (Line)this.lines.get(i);
            line.draw(g);
        }
        g.setColor(this.cols[this.index]);
        g.fillOval((int)this.x, (int)this.y, 50.0f, 50.0f);
        g.setColor(Color.yellow);
        g.fillRect(50.0f, 200 + this.ypos, 40.0f, 40.0f);
    }

    @Override
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

    @Override
    public void keyPressed(int key, char c) {
        if (key == 1) {
            FMLCommonHandler.instance().exitJava(0, true);
        }
        if (key == 59 && this.app != null) {
            try {
                this.app.setDisplayMode(600, 600, false);
                this.app.reinit();
            }
            catch (Exception e) {
                Log.error(e);
            }
        }
    }

    @Override
    public void keyReleased(int key, char c) {
        this.message = "You pressed key code " + key + " (character = " + c + ")";
    }

    @Override
    public void mousePressed(int button, int x, int y) {
        if (button == 0) {
            this.buttonDown = true;
        }
        this.message = "Mouse pressed " + button + " " + x + "," + y;
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
        if (button == 0) {
            this.buttonDown = false;
        }
        this.message = "Mouse released " + button + " " + x + "," + y;
    }

    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {
        System.out.println("CLICKED:" + x + "," + y + " " + clickCount);
    }

    @Override
    public void mouseWheelMoved(int change) {
        this.message = "Mouse wheel moved: " + change;
        if (change < 0) {
            this.ypos -= 10;
        }
        if (change > 0) {
            this.ypos += 10;
        }
    }

    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        if (this.buttonDown) {
            this.lines.add(new Line(oldx, oldy, newx, newy));
        }
    }

    @Override
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
        catch (SlickException e) {
            e.printStackTrace();
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

        public void draw(Graphics g) {
            g.drawLine(this.oldx, this.oldy, this.newx, this.newy);
        }
    }
}

