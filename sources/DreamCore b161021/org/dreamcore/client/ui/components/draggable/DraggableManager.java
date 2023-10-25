package org.dreamcore.client.ui.components.draggable;

import org.dreamcore.client.ui.components.draggable.impl.*;

import java.util.ArrayList;

public class DraggableManager {

    public ArrayList<DraggableModule> mods = new ArrayList<>();

    public DraggableManager() {
        mods.add(new ClientInfoComponent());
        mods.add(new InfoComponent());
        mods.add(new WaterMarkComponent());
        mods.add(new PotionComponent());
        mods.add(new ArmorComponent());
        mods.add(new TargetHUDComponent());
    }

    public ArrayList<DraggableModule> getMods() {
        return mods;
    }

    public void setMods(ArrayList<DraggableModule> mods) {
        this.mods = mods;
    }
}