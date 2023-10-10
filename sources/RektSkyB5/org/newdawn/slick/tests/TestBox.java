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
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.SlickCallable;
import org.newdawn.slick.tests.AnimationTest;
import org.newdawn.slick.tests.AntiAliasTest;
import org.newdawn.slick.tests.BigImageTest;
import org.newdawn.slick.tests.ClipTest;
import org.newdawn.slick.tests.DuplicateEmitterTest;
import org.newdawn.slick.tests.FlashTest;
import org.newdawn.slick.tests.FontPerformanceTest;
import org.newdawn.slick.tests.FontTest;
import org.newdawn.slick.tests.GeomTest;
import org.newdawn.slick.tests.GradientTest;
import org.newdawn.slick.tests.GraphicsTest;
import org.newdawn.slick.tests.ImageBufferTest;
import org.newdawn.slick.tests.ImageReadTest;
import org.newdawn.slick.tests.ImageTest;
import org.newdawn.slick.tests.KeyRepeatTest;
import org.newdawn.slick.tests.MusicListenerTest;
import org.newdawn.slick.tests.PackedSheetTest;
import org.newdawn.slick.tests.PedigreeTest;
import org.newdawn.slick.tests.PureFontTest;
import org.newdawn.slick.tests.ShapeTest;
import org.newdawn.slick.tests.SoundTest;
import org.newdawn.slick.tests.SpriteSheetFontTest;
import org.newdawn.slick.tests.TransparentColorTest;
import org.newdawn.slick.util.Log;

public class TestBox
extends BasicGame {
    private ArrayList games = new ArrayList();
    private BasicGame currentGame;
    private int index;
    private AppGameContainer container;

    public TestBox() {
        super("Test Box");
    }

    public void addGame(Class game) {
        this.games.add(game);
    }

    private void nextGame() {
        if (this.index == -1) {
            return;
        }
        ++this.index;
        if (this.index >= this.games.size()) {
            this.index = 0;
        }
        this.startGame();
    }

    private void startGame() {
        try {
            this.currentGame = (BasicGame)((Class)this.games.get(this.index)).newInstance();
            this.container.getGraphics().setBackground(Color.black);
            this.currentGame.init(this.container);
            this.currentGame.render(this.container, this.container.getGraphics());
        }
        catch (Exception e2) {
            Log.error(e2);
        }
        this.container.setTitle(this.currentGame.getTitle());
    }

    public void init(GameContainer c2) throws SlickException {
        if (this.games.size() == 0) {
            this.currentGame = new BasicGame("NULL"){

                public void init(GameContainer container) throws SlickException {
                }

                public void update(GameContainer container, int delta) throws SlickException {
                }

                public void render(GameContainer container, Graphics g2) throws SlickException {
                }
            };
            this.currentGame.init(c2);
            this.index = -1;
        } else {
            this.index = 0;
            this.container = (AppGameContainer)c2;
            this.startGame();
        }
    }

    public void update(GameContainer container, int delta) throws SlickException {
        this.currentGame.update(container, delta);
    }

    public void render(GameContainer container, Graphics g2) throws SlickException {
        SlickCallable.enterSafeBlock();
        this.currentGame.render(container, g2);
        SlickCallable.leaveSafeBlock();
    }

    public void controllerButtonPressed(int controller, int button) {
        this.currentGame.controllerButtonPressed(controller, button);
    }

    public void controllerButtonReleased(int controller, int button) {
        this.currentGame.controllerButtonReleased(controller, button);
    }

    public void controllerDownPressed(int controller) {
        this.currentGame.controllerDownPressed(controller);
    }

    public void controllerDownReleased(int controller) {
        this.currentGame.controllerDownReleased(controller);
    }

    public void controllerLeftPressed(int controller) {
        this.currentGame.controllerLeftPressed(controller);
    }

    public void controllerLeftReleased(int controller) {
        this.currentGame.controllerLeftReleased(controller);
    }

    public void controllerRightPressed(int controller) {
        this.currentGame.controllerRightPressed(controller);
    }

    public void controllerRightReleased(int controller) {
        this.currentGame.controllerRightReleased(controller);
    }

    public void controllerUpPressed(int controller) {
        this.currentGame.controllerUpPressed(controller);
    }

    public void controllerUpReleased(int controller) {
        this.currentGame.controllerUpReleased(controller);
    }

    public void keyPressed(int key, char c2) {
        this.currentGame.keyPressed(key, c2);
        if (key == 28) {
            this.nextGame();
        }
    }

    public void keyReleased(int key, char c2) {
        this.currentGame.keyReleased(key, c2);
    }

    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        this.currentGame.mouseMoved(oldx, oldy, newx, newy);
    }

    public void mousePressed(int button, int x2, int y2) {
        this.currentGame.mousePressed(button, x2, y2);
    }

    public void mouseReleased(int button, int x2, int y2) {
        this.currentGame.mouseReleased(button, x2, y2);
    }

    public void mouseWheelMoved(int change) {
        this.currentGame.mouseWheelMoved(change);
    }

    public static void main(String[] argv) {
        try {
            TestBox box = new TestBox();
            box.addGame(AnimationTest.class);
            box.addGame(AntiAliasTest.class);
            box.addGame(BigImageTest.class);
            box.addGame(ClipTest.class);
            box.addGame(DuplicateEmitterTest.class);
            box.addGame(FlashTest.class);
            box.addGame(FontPerformanceTest.class);
            box.addGame(FontTest.class);
            box.addGame(GeomTest.class);
            box.addGame(GradientTest.class);
            box.addGame(GraphicsTest.class);
            box.addGame(ImageBufferTest.class);
            box.addGame(ImageReadTest.class);
            box.addGame(ImageTest.class);
            box.addGame(KeyRepeatTest.class);
            box.addGame(MusicListenerTest.class);
            box.addGame(PackedSheetTest.class);
            box.addGame(PedigreeTest.class);
            box.addGame(PureFontTest.class);
            box.addGame(ShapeTest.class);
            box.addGame(SoundTest.class);
            box.addGame(SpriteSheetFontTest.class);
            box.addGame(TransparentColorTest.class);
            AppGameContainer container = new AppGameContainer(box);
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e2) {
            e2.printStackTrace();
        }
    }
}

