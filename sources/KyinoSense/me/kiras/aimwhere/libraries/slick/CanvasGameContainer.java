/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.FMLCommonHandler
 *  org.lwjgl.LWJGLException
 *  org.lwjgl.opengl.Display
 */
package me.kiras.aimwhere.libraries.slick;

import java.awt.Canvas;
import javax.swing.SwingUtilities;
import me.kiras.aimwhere.libraries.slick.AppGameContainer;
import me.kiras.aimwhere.libraries.slick.Game;
import me.kiras.aimwhere.libraries.slick.Input;
import me.kiras.aimwhere.libraries.slick.SlickException;
import me.kiras.aimwhere.libraries.slick.util.Log;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

public class CanvasGameContainer
extends Canvas {
    protected Container container;
    protected Game game;

    public CanvasGameContainer(Game game) throws SlickException {
        this(game, false);
    }

    public CanvasGameContainer(Game game, boolean shared) throws SlickException {
        this.game = game;
        this.setIgnoreRepaint(true);
        this.requestFocus();
        this.setSize(500, 500);
        this.container = new Container(game, shared);
        this.container.setForceExit(false);
    }

    public void start() throws SlickException {
        SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run() {
                try {
                    Input.disableControllers();
                    try {
                        Display.setParent((Canvas)CanvasGameContainer.this);
                    }
                    catch (LWJGLException e) {
                        throw new SlickException("Failed to setParent of canvas", e);
                    }
                    CanvasGameContainer.this.container.setup();
                    CanvasGameContainer.this.scheduleUpdate();
                }
                catch (SlickException e) {
                    e.printStackTrace();
                    FMLCommonHandler.instance().exitJava(0, true);
                }
            }
        });
    }

    private void scheduleUpdate() {
        if (!this.isVisible()) {
            return;
        }
        SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run() {
                try {
                    CanvasGameContainer.this.container.gameLoop();
                }
                catch (SlickException e) {
                    e.printStackTrace();
                }
                CanvasGameContainer.this.container.checkDimensions();
                CanvasGameContainer.this.scheduleUpdate();
            }
        });
    }

    public void dispose() {
    }

    private class Container
    extends AppGameContainer {
        public Container(Game game, boolean shared) throws SlickException {
            super(game, CanvasGameContainer.this.getWidth(), CanvasGameContainer.this.getHeight(), false);
            this.width = CanvasGameContainer.this.getWidth();
            this.height = CanvasGameContainer.this.getHeight();
            if (shared) {
                Container.enableSharedContext();
            }
        }

        @Override
        protected void updateFPS() {
            super.updateFPS();
        }

        @Override
        protected boolean running() {
            return super.running() && CanvasGameContainer.this.isDisplayable();
        }

        @Override
        public int getHeight() {
            return CanvasGameContainer.this.getHeight();
        }

        @Override
        public int getWidth() {
            return CanvasGameContainer.this.getWidth();
        }

        public void checkDimensions() {
            if (this.width != CanvasGameContainer.this.getWidth() || this.height != CanvasGameContainer.this.getHeight()) {
                try {
                    this.setDisplayMode(CanvasGameContainer.this.getWidth(), CanvasGameContainer.this.getHeight(), false);
                }
                catch (SlickException e) {
                    Log.error(e);
                }
            }
        }
    }
}

