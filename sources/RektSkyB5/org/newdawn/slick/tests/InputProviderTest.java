/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.command.BasicCommand;
import org.newdawn.slick.command.Command;
import org.newdawn.slick.command.ControllerButtonControl;
import org.newdawn.slick.command.ControllerDirectionControl;
import org.newdawn.slick.command.InputProvider;
import org.newdawn.slick.command.InputProviderListener;
import org.newdawn.slick.command.KeyControl;
import org.newdawn.slick.command.MouseButtonControl;

public class InputProviderTest
extends BasicGame
implements InputProviderListener {
    private Command attack = new BasicCommand("attack");
    private Command jump = new BasicCommand("jump");
    private Command run = new BasicCommand("run");
    private InputProvider provider;
    private String message = "";

    public InputProviderTest() {
        super("InputProvider Test");
    }

    public void init(GameContainer container) throws SlickException {
        this.provider = new InputProvider(container.getInput());
        this.provider.addListener(this);
        this.provider.bindCommand(new KeyControl(203), this.run);
        this.provider.bindCommand(new KeyControl(30), this.run);
        this.provider.bindCommand(new ControllerDirectionControl(0, ControllerDirectionControl.LEFT), this.run);
        this.provider.bindCommand(new KeyControl(200), this.jump);
        this.provider.bindCommand(new KeyControl(17), this.jump);
        this.provider.bindCommand(new ControllerDirectionControl(0, ControllerDirectionControl.UP), this.jump);
        this.provider.bindCommand(new KeyControl(57), this.attack);
        this.provider.bindCommand(new MouseButtonControl(0), this.attack);
        this.provider.bindCommand(new ControllerButtonControl(0, 1), this.attack);
    }

    public void render(GameContainer container, Graphics g2) {
        g2.drawString("Press A, W, Left, Up, space, mouse button 1,and gamepad controls", 10.0f, 50.0f);
        g2.drawString(this.message, 100.0f, 150.0f);
    }

    public void update(GameContainer container, int delta) {
    }

    public void controlPressed(Command command) {
        this.message = "Pressed: " + command;
    }

    public void controlReleased(Command command) {
        this.message = "Released: " + command;
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new InputProviderTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e2) {
            e2.printStackTrace();
        }
    }
}

