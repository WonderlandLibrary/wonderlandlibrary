package rip.helium.cheat;

import me.hippo.systems.lwjeb.annotation.Collect;
import rip.helium.Helium;
import rip.helium.cheat.impl.combat.AntiBot;
import rip.helium.cheat.impl.combat.AutoPot;
import rip.helium.cheat.impl.combat.Criticals;
import rip.helium.cheat.impl.combat.SchoolShooter;
import rip.helium.cheat.impl.combat.aura.Aura;
import rip.helium.cheat.impl.misc.*;
import rip.helium.cheat.impl.misc.cheststealer.ChestStealer;
import rip.helium.cheat.impl.movement.*;
import rip.helium.cheat.impl.player.*;
import rip.helium.cheat.impl.visual.*;
import rip.helium.event.minecraft.KeyPressEvent;
import rip.helium.ui.main.tab.TabGui;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author antja03
 */
public class CheatManager {

    private final HashMap<String, Cheat> cheatRegistry;

    public CheatManager() {
        cheatRegistry = new HashMap<>();
        Helium.eventBus.register(this);
    }

    public void registerCheats() {
        registerCheat(new BlockAnimation());
        registerCheat(new Hud());
        registerCheat(new SchoolShooter());
        registerCheat(new AutoPot());
        registerCheat(new Regen());
        registerCheat(new FastUse());
        registerCheat(new Phase());
        registerCheat(new AntiKnockback());
        registerCheat(new AutoInventory());
        registerCheat(new Spammer());
        registerCheat(new ChestStealer());
        registerCheat(new AutoSprint());
        registerCheat(new SkeletonESP());
        registerCheat(new Flight());
        registerCheat(new GuiMove());
        registerCheat(new BookExploit());
        registerCheat(new NoFall());
        registerCheat(new Scaffold());
        registerCheat(new Speed());
        registerCheat(new ChestAura());
        registerCheat(new Interface());
        registerCheat(new ChestStealer());
        registerCheat(new Brightness());
        registerCheat(new NoSlowdown());
        registerCheat(new Xray());
        registerCheat(new Longjump());
        registerCheat(new FastUse());
        registerCheat(new AntiVoid());
        registerCheat(new Disabler());
        registerCheat(new AntiBot());
        registerCheat(new Criticals());
        registerCheat(new Flag());
        registerCheat(new Chams());
        registerCheat(new Console());
        registerCheat(new KillSults());
        registerCheat(new VClip());
        registerCheat(new Aura());
        registerCheat(new WaterSpeed());
        registerCheat(new ItemESP());
        registerCheat(new Fucker());
        registerCheat(new PearlFinder());
        registerCheat(new ItemPhysics());
        registerCheat(new Sneak());
        registerCheat(new IceSpeed());
        registerCheat(new Protect());
        registerCheat(new TargetStrafe());
        registerCheat(new NoHurtCam());
        registerCheat(new Crosshair());
        registerCheat(new Step());
        registerCheat(new Jesus());
        registerCheat(new VClip());
        registerCheat(new NoRotate());
        registerCheat(new AutoPlay());
        registerCheat(new NoStrike());
        registerCheat(new NameTags());
        registerCheat(new InventoryCleaner());
        registerCheat(new ESP());
        registerCheat(new Tracers());
        registerCheat(new Longjump());
        registerCheat(new TabGui());
        registerCheat(new Freecam());
        registerCheat(new Teleport());
        if (Helium.instance.is18Mode) registerCheat(new MemeESP());

    }

    public void registerCheat(Object object) {
        if (object instanceof Cheat) {
            Cheat cheat = (Cheat) object;
            if (!cheatRegistry.containsValue(cheat)) {
                cheatRegistry.put(cheat.getId(), cheat);
            }
        }
    }


    @Collect
    public void onKeyPress(KeyPressEvent event) {

        //NotificationManager.postInfo("key press", RandomStringUtils.random(20, true, true));
        for (Cheat cheat : cheatRegistry.values()) {
            if (event.getKeyCode() == cheat.getBind()) {
                cheat.setState(!cheat.getState(), true);
            }
        }
    }

    public ArrayList<Cheat> searchRegistry(CheatCategory category) {
        ArrayList<Cheat> cheats = new ArrayList<>();
        cheatRegistry.values().forEach(cheat -> {
            if (cheat.getCategory().equals(category)) {
                cheats.add(cheat);
            }
        });
        return cheats;
    }

    public ArrayList<Cheat> searchRegistry(String term) {
        ArrayList<Cheat> cheats = new ArrayList<>();
        cheatRegistry.values().forEach(cheat -> {
            if (cheat.getId().toLowerCase().contains(term.toLowerCase())) {
                cheats.add(cheat);
            }
        });
        return cheats;
    }

    public HashMap<String, Cheat> getCheatRegistry() {
        return cheatRegistry;
    }

    public boolean isCheatEnabled(String id) {
        return getCheatRegistry().get(id).getState();
    }

}
