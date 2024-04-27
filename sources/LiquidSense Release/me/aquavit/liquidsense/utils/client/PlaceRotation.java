package me.aquavit.liquidsense.utils.client;

import me.aquavit.liquidsense.utils.block.PlaceInfo;
import me.aquavit.liquidsense.utils.client.Rotation;
import me.aquavit.liquidsense.utils.mc.MinecraftInstance;

public final class PlaceRotation extends MinecraftInstance {

    private PlaceInfo placeInfo;
    private Rotation rotation;

    public PlaceRotation(PlaceInfo placeInfo,Rotation rotation){
        this.placeInfo = placeInfo;
        this.rotation = rotation;
    }

    public final PlaceInfo getPlaceInfo() {
        return this.placeInfo;
    }

    public final void setPlaceInfo(PlaceInfo info) {
        this.placeInfo = info;
    }

    public final Rotation getRotation() {
        return this.rotation;
    }

    public final void setRotation(Rotation rotation) {
        this.rotation = rotation;
    }
}
