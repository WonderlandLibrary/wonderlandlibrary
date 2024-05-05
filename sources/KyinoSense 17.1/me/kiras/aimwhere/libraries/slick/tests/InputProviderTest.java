/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.tests;

import me.kiras.aimwhere.libraries.slick.AppGameContainer;
import me.kiras.aimwhere.libraries.slick.BasicGame;
import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.SlickException;
import me.kiras.aimwhere.libraries.slick.command.BasicCommand;
import me.kiras.aimwhere.libraries.slick.command.Command;
import me.kiras.aimwhere.libraries.slick.command.ControllerButtonControl;
import me.kiras.aimwhere.libraries.slick.command.ControllerDirectionControl;
import me.kiras.aimwhere.libraries.slick.command.InputProvider;
import me.kiras.aimwhere.libraries.slick.command.InputProviderListener;
import me.kiras.aimwhere.libraries.slick.command.KeyControl;
import me.kiras.aimwhere.libraries.slick.command.MouseButtonControl;

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

    @Override
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

    @Override
    public void render(GameContainer container, Graphics g) {
        g.drawString("Press A, W, Left, Up, space, mouse button 1,and gamepad controls", 10.0f, 50.0f);
        g.drawString(this.message, 100.0f, 150.0f);
    }

    @Override
    public void update(GameContainer container, int delta) {
    }

    @Override
    public void controlPressed(Command command) {
        this.message = "Pressed: " + command;
    }

    @Override
    public void controlReleased(Command command) {
        this.message = "Released: " + command;
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new InputProviderTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}

