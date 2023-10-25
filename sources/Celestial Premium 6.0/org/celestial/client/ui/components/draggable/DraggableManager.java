/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.ui.components.draggable;

import java.util.ArrayList;
import org.celestial.client.ui.components.draggable.DraggableModule;
import org.celestial.client.ui.components.draggable.impl.ArmorComponent;
import org.celestial.client.ui.components.draggable.impl.ClientInfoComponent;
import org.celestial.client.ui.components.draggable.impl.HudInfoComponent;
import org.celestial.client.ui.components.draggable.impl.IndicatorsComponent;
import org.celestial.client.ui.components.draggable.impl.InfoComponent;
import org.celestial.client.ui.components.draggable.impl.InvPreviewComponent;
import org.celestial.client.ui.components.draggable.impl.LogoComponent;
import org.celestial.client.ui.components.draggable.impl.PotionComponent;
import org.celestial.client.ui.components.draggable.impl.ScoreboardComponent;
import org.celestial.client.ui.components.draggable.impl.TargetHUDComponent;

public class DraggableManager {
    public ArrayList<DraggableModule> mods = new ArrayList();

    public DraggableManager() {
        this.mods.add(new ClientInfoComponent());
        this.mods.add(new InfoComponent());
        this.mods.add(new LogoComponent());
        this.mods.add(new PotionComponent());
        this.mods.add(new ArmorComponent());
        this.mods.add(new TargetHUDComponent());
        this.mods.add(new HudInfoComponent());
        this.mods.add(new InvPreviewComponent());
        this.mods.add(new IndicatorsComponent());
        this.mods.add(new ScoreboardComponent());
    }

    public ArrayList<DraggableModule> getMods() {
        return this.mods;
    }

    public void setMods(ArrayList<DraggableModule> mods) {
        this.mods = mods;
    }
}

