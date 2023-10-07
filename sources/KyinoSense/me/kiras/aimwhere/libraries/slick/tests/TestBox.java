/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.tests;

import java.util.ArrayList;
import me.kiras.aimwhere.libraries.slick.AppGameContainer;
import me.kiras.aimwhere.libraries.slick.BasicGame;
import me.kiras.aimwhere.libraries.slick.Color;
import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.SlickException;
import me.kiras.aimwhere.libraries.slick.opengl.SlickCallable;
import me.kiras.aimwhere.libraries.slick.tests.AnimationTest;
import me.kiras.aimwhere.libraries.slick.tests.AntiAliasTest;
import me.kiras.aimwhere.libraries.slick.tests.BigImageTest;
import me.kiras.aimwhere.libraries.slick.tests.ClipTest;
import me.kiras.aimwhere.libraries.slick.tests.DuplicateEmitterTest;
import me.kiras.aimwhere.libraries.slick.tests.FlashTest;
import me.kiras.aimwhere.libraries.slick.tests.FontPerformanceTest;
import me.kiras.aimwhere.libraries.slick.tests.FontTest;
import me.kiras.aimwhere.libraries.slick.tests.GeomTest;
import me.kiras.aimwhere.libraries.slick.tests.GradientTest;
import me.kiras.aimwhere.libraries.slick.tests.GraphicsTest;
import me.kiras.aimwhere.libraries.slick.tests.ImageBufferTest;
import me.kiras.aimwhere.libraries.slick.tests.ImageReadTest;
import me.kiras.aimwhere.libraries.slick.tests.ImageTest;
import me.kiras.aimwhere.libraries.slick.tests.KeyRepeatTest;
import me.kiras.aimwhere.libraries.slick.tests.MusicListenerTest;
import me.kiras.aimwhere.libraries.slick.tests.PackedSheetTest;
import me.kiras.aimwhere.libraries.slick.tests.PedigreeTest;
import me.kiras.aimwhere.libraries.slick.tests.PureFontTest;
import me.kiras.aimwhere.libraries.slick.tests.ShapeTest;
import me.kiras.aimwhere.libraries.slick.tests.SoundTest;
import me.kiras.aimwhere.libraries.slick.tests.SpriteSheetFontTest;
import me.kiras.aimwhere.libraries.slick.tests.TransparentColorTest;
import me.kiras.aimwhere.libraries.slick.util.Log;

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
        catch (Exception e) {
            Log.error(e);
        }
        this.container.setTitle(this.currentGame.getTitle());
    }

    @Override
    public void init(GameContainer c) throws SlickException {
        if (this.games.size() == 0) {
            this.currentGame = new BasicGame("NULL"){

                @Override
                public void init(GameContainer container) throws SlickException {
                }

                @Override
                public void update(GameContainer container, int delta) throws SlickException {
                }

                @Override
                public void render(GameContainer container, Graphics g) throws SlickException {
                }
            };
            this.currentGame.init(c);
            this.index = -1;
        } else {
            this.index = 0;
            this.container = (AppGameContainer)c;
            this.startGame();
        }
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        this.currentGame.update(container, delta);
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        SlickCallable.enterSafeBlock();
        this.currentGame.render(container, g);
        SlickCallable.leaveSafeBlock();
    }

    @Override
    public void controllerButtonPressed(int controller, int button) {
        this.currentGame.controllerButtonPressed(controller, button);
    }

    @Override
    public void controllerButtonReleased(int controller, int button) {
        this.currentGame.controllerButtonReleased(controller, button);
    }

    @Override
    public void controllerDownPressed(int controller) {
        this.currentGame.controllerDownPressed(controller);
    }

    @Override
    public void controllerDownReleased(int controller) {
        this.currentGame.controllerDownReleased(controller);
    }

    @Override
    public void controllerLeftPressed(int controller) {
        this.currentGame.controllerLeftPressed(controller);
    }

    @Override
    public void controllerLeftReleased(int controller) {
        this.currentGame.controllerLeftReleased(controller);
    }

    @Override
    public void controllerRightPressed(int controller) {
        this.currentGame.controllerRightPressed(controller);
    }

    @Override
    public void controllerRightReleased(int controller) {
        this.currentGame.controllerRightReleased(controller);
    }

    @Override
    public void controllerUpPressed(int controller) {
        this.currentGame.controllerUpPressed(controller);
    }

    @Override
    public void controllerUpReleased(int controller) {
        this.currentGame.controllerUpReleased(controller);
    }

    @Override
    public void keyPressed(int key, char c) {
        this.currentGame.keyPressed(key, c);
        if (key == 28) {
            this.nextGame();
        }
    }

    @Override
    public void keyReleased(int key, char c) {
        this.currentGame.keyReleased(key, c);
    }

    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        this.currentGame.mouseMoved(oldx, oldy, newx, newy);
    }

    @Override
    public void mousePressed(int button, int x, int y) {
        this.currentGame.mousePressed(button, x, y);
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
        this.currentGame.mouseReleased(button, x, y);
    }

    @Override
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
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}

