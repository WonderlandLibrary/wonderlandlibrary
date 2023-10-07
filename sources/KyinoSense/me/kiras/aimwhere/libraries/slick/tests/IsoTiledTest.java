/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.tests;

import me.kiras.aimwhere.libraries.slick.BasicGame;
import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.SlickException;
import me.kiras.aimwhere.libraries.slick.tiled.TiledMap;
import me.kiras.aimwhere.libraries.slick.util.Bootstrap;

public class IsoTiledTest
extends BasicGame {
    private TiledMap tilemap;

    public IsoTiledTest() {
        super("Isometric Tiled Map Test");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        this.tilemap = new TiledMap("testdata/isoexample.tmx", "testdata/");
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        this.tilemap.render(350, 150);
    }

    public static void main(String[] argv) {
        Bootstrap.runAsApplication(new IsoTiledTest(), 800, 600, false);
    }
}

