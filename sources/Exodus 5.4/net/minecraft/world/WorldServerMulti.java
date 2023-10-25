/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world;

import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.village.VillageCollection;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.border.IBorderListener;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.storage.DerivedWorldInfo;
import net.minecraft.world.storage.ISaveHandler;

public class WorldServerMulti
extends WorldServer {
    private WorldServer delegate;

    @Override
    protected void saveLevel() throws MinecraftException {
    }

    public WorldServerMulti(MinecraftServer minecraftServer, ISaveHandler iSaveHandler, int n, WorldServer worldServer, Profiler profiler) {
        super(minecraftServer, iSaveHandler, new DerivedWorldInfo(worldServer.getWorldInfo()), n, profiler);
        this.delegate = worldServer;
        worldServer.getWorldBorder().addListener(new IBorderListener(){

            @Override
            public void onTransitionStarted(WorldBorder worldBorder, double d, double d2, long l) {
                WorldServerMulti.this.getWorldBorder().setTransition(d, d2, l);
            }

            @Override
            public void onWarningTimeChanged(WorldBorder worldBorder, int n) {
                WorldServerMulti.this.getWorldBorder().setWarningTime(n);
            }

            @Override
            public void onCenterChanged(WorldBorder worldBorder, double d, double d2) {
                WorldServerMulti.this.getWorldBorder().setCenter(d, d2);
            }

            @Override
            public void onDamageAmountChanged(WorldBorder worldBorder, double d) {
                WorldServerMulti.this.getWorldBorder().setDamageAmount(d);
            }

            @Override
            public void onDamageBufferChanged(WorldBorder worldBorder, double d) {
                WorldServerMulti.this.getWorldBorder().setDamageBuffer(d);
            }

            @Override
            public void onSizeChanged(WorldBorder worldBorder, double d) {
                WorldServerMulti.this.getWorldBorder().setTransition(d);
            }

            @Override
            public void onWarningDistanceChanged(WorldBorder worldBorder, int n) {
                WorldServerMulti.this.getWorldBorder().setWarningDistance(n);
            }
        });
    }

    @Override
    public World init() {
        this.mapStorage = this.delegate.getMapStorage();
        this.worldScoreboard = this.delegate.getScoreboard();
        String string = VillageCollection.fileNameForProvider(this.provider);
        VillageCollection villageCollection = (VillageCollection)this.mapStorage.loadData(VillageCollection.class, string);
        if (villageCollection == null) {
            this.villageCollectionObj = new VillageCollection(this);
            this.mapStorage.setData(string, this.villageCollectionObj);
        } else {
            this.villageCollectionObj = villageCollection;
            this.villageCollectionObj.setWorldsForAll(this);
        }
        return this;
    }
}

