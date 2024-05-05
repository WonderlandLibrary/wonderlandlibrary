/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.FMLCommonHandler
 */
package me.kiras.aimwhere.libraries.slick.tests;

import me.kiras.aimwhere.libraries.slick.AppGameContainer;
import me.kiras.aimwhere.libraries.slick.BasicGame;
import me.kiras.aimwhere.libraries.slick.GameContainer;
import me.kiras.aimwhere.libraries.slick.Graphics;
import me.kiras.aimwhere.libraries.slick.SlickException;
import me.kiras.aimwhere.libraries.slick.tiled.TiledMap;
import net.minecraftforge.fml.common.FMLCommonHandler;

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

    @Override
    public void init(GameContainer container) throws SlickException {
        this.map = new TiledMap("testdata/testmap.tmx", "testdata");
        this.mapName = this.map.getMapProperty("name", "Unknown map name");
        this.monsterDifficulty = this.map.getLayerProperty(0, "monsters", "easy peasy");
        this.nonExistingMapProperty = this.map.getMapProperty("zaphod", "Undefined map property");
        this.nonExistingLayerProperty = this.map.getLayerProperty(1, "beeblebrox", "Undefined layer property");
        this.originalTileID = this.map.getTileId(10, 10, 0);
    }

    @Override
    public void render(GameContainer container, Graphics g) {
        this.map.render(10, 10, 4, 4, 15, 15);
        g.scale(0.35f, 0.35f);
        this.map.render(1400, 0);
        g.resetTransform();
        g.drawString("map name: " + this.mapName, 10.0f, 500.0f);
        g.drawString("monster difficulty: " + this.monsterDifficulty, 10.0f, 550.0f);
        g.drawString("non existing map property: " + this.nonExistingMapProperty, 10.0f, 525.0f);
        g.drawString("non existing layer property: " + this.nonExistingLayerProperty, 10.0f, 575.0f);
    }

    @Override
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

    @Override
    public void keyPressed(int key, char c) {
        if (key == 1) {
            FMLCommonHandler.instance().exitJava(0, true);
        }
    }

    public static void main(String[] argv) {
        try {
            AppGameContainer container = new AppGameContainer(new TileMapTest());
            container.setDisplayMode(800, 600, false);
            container.start();
        }
        catch (SlickException e) {
            e.printStackTrace();
        }
    }
}

