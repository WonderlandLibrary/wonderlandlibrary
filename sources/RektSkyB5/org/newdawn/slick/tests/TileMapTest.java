/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class TileMapTest
extends BasicGame {
    private TiledMap map;
    private String mapName;
    private String monsterDifficulty;
    private String nonExistingMapProperty;
    private String nonExistingLayerProperty;
    private int updateCounter = 0;
    private static int UPDATE_TIME = 1000;
    private int originalTileID = 0;

    public TileMapTest() {
        super("Tile Map Test");
    }

    public void init(GameContainer container) throws SlickException {
        this.map = new TiledMap("testdata/testmap.tmx", "testdata");
        this.mapName = this.map.getMapProperty("name", "Unknown map name");
        this.monsterDifficulty = this.map.getLayerProperty(0, "monsters", "easy peasy");
        this.nonExistingMapProperty = this.map.getMapProperty("zaphod", "Undefined map property");
        this.nonExistingLayerProperty = this.map.getLayerProperty(1, "beeblebrox", "Undefined layer property");
        this.originalTileID = this.map.getTileId(10, 10, 0);
    }

    public void render(GameContainer container, Graphics g2) {
        this.map.render(10, 10, 4, 4, 15, 15);
        g2.scale(0.35f, 0.35f);
        this.map.render(1400, 0);
        g2.resetTransform();
        g2.drawString("map name: " + this.mapName, 10.0f, 500.0f);
        g2.drawString("monster difficulty: " + this.monsterDifficulty, 10.0f, 550.0f);
        g2.drawString("non existing map property: " + this.nonExistingMapProperty, 10.0f, 525.0f);
        g2.drawString("non existing layer property: " + this.nonExistingLayerProperty, 10.0f, 575.0f);
    }

    public void update(GameContainer container, int delta) {
        this.updateCounter += delta;
        if (this.updateCounter > UPDATE_TIME) {
            this.updateCounter -= UPDATE_TIME;
            int currentTileID = this.map.getTileId(10, 10, 0);
            if (currentTileID != this.originalTileID) {
                this.map.setTileId(10, 10, 0, this.originalTileID);
            } else {
                this.map.setTileId(10, 10, 0, 1);
            }
        }
    }

    public void keyPressed(int key, char c2) {
        if (key == 1) {
            System.exit(0);
        }
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new TileMapTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e2) {
            e2.printStackTrace();
        }
    }
}

