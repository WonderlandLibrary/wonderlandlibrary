// 
// Decompiled by Procyon v0.6.0
// 

package fluid.client.mods;

import java.util.Iterator;
import java.util.ArrayList;
import fluid.client.mods.impl.MinimalViewBobbing;
import fluid.client.mods.impl.HitDelayFix;
import fluid.client.mods.impl.Animations;
import fluid.client.mods.impl.Reach;
import fluid.client.mods.impl.Crosshair;
import fluid.client.mods.impl.CPS;
import fluid.client.mods.impl.ToggleSprint;
import fluid.client.mods.impl.TestMod;
import fluid.client.mods.impl.FPS;
import java.util.List;

public class ModManager
{
    public List<Mod> modList;
    public List<GuiMod> guiModList;
    public FPS fps;
    public TestMod testMod;
    public ToggleSprint toggleSprint;
    public CPS cps;
    public Crosshair crosshair;
    public Reach reach;
    public Animations animations;
    public HitDelayFix hitDelayFix;
    public MinimalViewBobbing minimalViewBobbing;
    
    public ModManager() {
        this.modList = new ArrayList<Mod>();
        (this.guiModList = new ArrayList<GuiMod>()).add(this.fps = new FPS());
        this.guiModList.add(this.testMod = new TestMod());
        this.guiModList.add(this.toggleSprint = new ToggleSprint());
        this.guiModList.add(this.cps = new CPS());
        this.guiModList.add(this.crosshair = new Crosshair());
        this.guiModList.add(this.reach = new Reach());
        this.guiModList.add(this.animations = new Animations());
        this.guiModList.add(this.hitDelayFix = new HitDelayFix());
        this.guiModList.add(this.minimalViewBobbing = new MinimalViewBobbing());
    }
    
    public Mod getModByName(final String name) {
        for (final Mod mod : this.modList) {
            if (mod.name.equalsIgnoreCase(name)) {
                return mod;
            }
        }
        return null;
    }
    
    public GuiMod getGuiModByName(final String name) {
        for (final GuiMod mod : this.guiModList) {
            if (mod.name.equalsIgnoreCase(name)) {
                return mod;
            }
        }
        return null;
    }
}
