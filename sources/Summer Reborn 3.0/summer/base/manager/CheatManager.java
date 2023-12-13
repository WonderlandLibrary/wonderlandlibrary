package summer.base.manager;

import com.google.common.collect.ImmutableClassToInstanceMap;
import summer.Summer;
import summer.base.manager.config.Cheats;
import summer.cheat.cheats.combat.*;
import summer.cheat.cheats.misc.AntiBan;
import summer.cheat.cheats.misc.FastChat;
import summer.cheat.cheats.movement.*;
import summer.cheat.cheats.player.*;
import summer.cheat.cheats.render.*;
import summer.cheat.eventsystem.EventTarget;
import summer.cheat.eventsystem.events.client.EventKey;

import java.util.*;
import java.util.stream.Collectors;

public class CheatManager {
    private final ImmutableClassToInstanceMap<Cheats> instanceMap;

    public CheatManager() {
        instanceMap = putInInstanceMap(Collections.unmodifiableList(Arrays.asList(
                new DamageParticles(),
                new Animations(),
                new AntiDesync(),
                new AntiVoid(),
                new AutoPot(),
                new ChestStealer(),
                new HUD(),
                new Speed(),
                new AutoArmor(),
                new NoFall(),
                new TimeChanger(),
                new NoRotate(),
                new Sprint(),
                new Flight(),
                new Velocity(),
                new KillAura(),
                new AntiBot(),
                new NoSlowDown(),
                new ESP(),
                new InvManager(),
                new ChestESP(),
                new TargetHUD(),
                new InvMove(),
                new NoHurtcam(),
                new FastChat(),
                new TargetStrafe(),
                new Step(),
                new Crosshair(),
                new Chams(),
                new Criticals(),
                new Wings(),
                new ViewClip(),
                new ClickGUI(),
                new CustomHitColor(),
                new CustomGlint(),
                new Scoreboard(),
                new MemoryFix(),
                new MCF(),
                new Keystrokes(),
                new AntiBan(),
                new Scaffold(),
                new ItemPhysics(),
                new AutoTool(),
                new AutoJoin(),
                new Phase(),
                new Radar()
        )));
    }

    private <T extends Cheats> ImmutableClassToInstanceMap<Cheats> putInInstanceMap(List<T> modules) {
        ImmutableClassToInstanceMap.Builder<Cheats> moduleBuilder = ImmutableClassToInstanceMap.builder();

        for (T module : modules)
            moduleBuilder.put((Class<Cheats>) module.getClass(), module);

        return moduleBuilder.build();
    }

    public Collection<Cheats> getModuleList() {
        return instanceMap.values();
    }

    public List<Cheats> getCheats(Selection cheatType) {
        return getModuleList().stream()
                .filter(cheat -> cheat.getCategory() == cheatType)
                .collect(Collectors.toList());
    }

    public <T extends Cheats> T getModule(Class<T> clazz) {
        return instanceMap.getInstance(clazz);
    }

    public static <T extends Cheats> T getInstance(Class<T> clazz) {
        return Summer.INSTANCE.cheatManager.getModule(clazz);
    }

    public Cheats getModuleByName(String name) {
        for (Cheats m : getModuleList()) {
            if (m.getName().equalsIgnoreCase(name)) {
                return m;
            }
        }
        return null;
    }

    @EventTarget
    public void onKey(EventKey ek) {
        for (Cheats m : getModuleList()) {
            if (ek.getKey() == m.getKey()) {
                m.toggle();
            }
        }
    }

    public ArrayList getEnabledVisibleMods() {
        ArrayList<Cheats> enabledMods = new ArrayList<Cheats>();
        for (Cheats m : getModuleList()) {
            if (m.isToggled() && !m.getDisplayName().equalsIgnoreCase("hud")) {
                enabledMods.add(m);
            }
        }
        return enabledMods;
    }

}
