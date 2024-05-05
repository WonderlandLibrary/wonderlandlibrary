package club.cloverhook.cheat;

import club.cloverhook.cheat.impl.combat.AntiBot;
import club.cloverhook.cheat.impl.combat.Criticals;
import club.cloverhook.utils.Mafs;
import me.hippo.systems.lwjeb.annotation.Collect;

import java.util.ArrayList;
import java.util.HashMap;

import club.cloverhook.Cloverhook;
import club.cloverhook.cheat.impl.combat.AutoPot;
import club.cloverhook.cheat.impl.combat.aura.Aura;
import club.cloverhook.cheat.impl.misc.*;
import club.cloverhook.cheat.impl.misc.cheststealer.ChestStealer;
import club.cloverhook.cheat.impl.movement.*;
import club.cloverhook.cheat.impl.player.*;
import club.cloverhook.cheat.impl.visual.*;
import club.cloverhook.event.minecraft.KeyPressEvent;
import net.minecraft.entity.EntityLivingBase;

/**
 * @author antja03
 */
public class CheatManager {

    private final HashMap<String, Cheat> cheatRegistry;

    public CheatManager() {
        cheatRegistry = new HashMap<>();
        Cloverhook.eventBus.register(this);
    }

    public void registerCheats() {
        registerCheat(new BlockAnimation());
        registerCheat(new Hud());
        registerCheat(new NameProtect());
        registerCheat(new AutoPot());
        registerCheat(new Regen());
        registerCheat(new Phase());
        registerCheat(new AntiKnockback());
        registerCheat(new AutoInventory());
        registerCheat(new ChestStealer());
        registerCheat(new AutoSprint());
        registerCheat(new Flight());
        registerCheat(new GuiMove());
        registerCheat(new NoFall());
        registerCheat(new Scaffold());
        registerCheat(new Speed());
        registerCheat(new Interface());
        registerCheat(new ChestStealer());
        registerCheat(new Brightness());
        registerCheat(new NoSlowdown());
        registerCheat(new ESP());
        registerCheat(new Xray());
        registerCheat(new Longjump());
        registerCheat(new AntiVoid());
        registerCheat(new AntiBot());
        registerCheat(new Criticals());
        registerCheat(new Chams());
        registerCheat(new KillSults());
        registerCheat(new Aura());
        registerCheat(new ItemPhysics());
        registerCheat(new NoHurtCam());
        registerCheat(new Crosshair());
        registerCheat(new Step());
        registerCheat(new Jesus());
        registerCheat(new NoRotate());
        registerCheat(new AutoPlay());
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
