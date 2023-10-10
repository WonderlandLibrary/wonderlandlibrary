package Reality.Realii.managers;

import net.minecraft.client.Minecraft; 
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import Reality.Realii.event.EventBus;
import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.misc.EventKey;
import Reality.Realii.event.events.rendering.EventRender2D;
import Reality.Realii.event.events.rendering.EventRender3D;
import Reality.Realii.event.events.world.EventTick;
import Reality.Realii.event.value.Mode;
import Reality.Realii.event.value.Numbers;
import Reality.Realii.event.value.Option;
import Reality.Realii.event.value.Value;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.mods.modules.ClientSettings;
import Reality.Realii.mods.modules.combat.*;
import Reality.Realii.mods.modules.misc.MCF;
import Reality.Realii.mods.modules.misc.NoRotate;
import Reality.Realii.mods.modules.misc.Sneak;
import Reality.Realii.mods.modules.movement.*;
import Reality.Realii.mods.modules.player.*;
import Reality.Realii.mods.modules.render.*;
import Reality.Realii.mods.modules.world.*;
import Reality.Realii.utils.cheats.world.TimerUtil;
import Reality.Realii.utils.render.gl.GLUtils;
import Reality.server.packets.client.C04PacketData;

import java.nio.FloatBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager implements Manager {
    public static List<Module> modules = new ArrayList<Module>();
    public static List<Module> enabledModules = new ArrayList<Module>();

    private boolean enabledNeededMod = true;

    @Override
    public void init() {

        modules.add(new Killaura());
        modules.add(new Velocity());
        modules.add(new Regen());
        modules.add(new FlagDetector());
        modules.add(new WTap());
        modules.add(new AutoArmor());
        modules.add(new AutoPot());
        modules.add(new FastBow());
        modules.add(new AntiDesync());
        modules.add(new BowAimBot());
        modules.add(new AntiBots());
        modules.add(new TpAura());
        modules.add(new Criticals());
        modules.add(new AutoClicker());
        modules.add(new AutoClickerTest());
        modules.add(new HUD());
        modules.add(new Animations());
        modules.add(new Esp());
        modules.add(new ClickGui());
        modules.add(new FullBright());
        modules.add(new Tracers());
        modules.add(new NoRender());
        modules.add(new Chams());
        modules.add(new TargetHud());
        modules.add(new ItemPhysic());
        modules.add(new ArrowEsp());
        modules.add(new HitParticles());
        modules.add(new ChunkAnimator());
        modules.add(new Xray());
        modules.add(new ChestESP());
        modules.add(new JumpCircle());
        modules.add(new GlowEsp());
        modules.add(new DiscordRPC());
        
        modules.add(new NameTag());
        modules.add(new BlockOverlay());
        modules.add(new ArrayList2());
        modules.add(new TabUI());
        modules.add(new Information());
        modules.add(new NoHurtCam());
        modules.add(new  SesionInfo());
        modules.add(new ShaderEsp());
        modules.add(new Ambiance());
        modules.add(new AsianHat());
        modules.add(new NoScoreBoard());
        modules.add(new Radar());
        modules.add(new PlayerSize());
        modules.add(new BreadCrumbs());
        modules.add(new Chinahat());
        modules.add(new KeyStrokes());
        modules.add(new MotionGraph());
        modules.add(new OldNametags());
        modules.add(new Capes());
        modules.add(new FastUse());
        modules.add(new Spammer());
        modules.add(new InvManager());
        modules.add(new Teams());
        modules.add(new AutoTools());
        modules.add(new NoFall());
        modules.add(new SpinBot());
        modules.add(new AutoPlay());
        modules.add(new HighJump());
        modules.add(new TickBase());
        modules.add(new BedNuker());
        modules.add(new AutoQuitOnBan());
        modules.add(new AutoLogin());
        modules.add(new KillInsult());
        modules.add(new CheatDetector());
        modules.add(new TargetStrafe());
        modules.add(new LagBackChecker());
        modules.add(new Fly());
        modules.add(new Speed());
        modules.add(new Blink());
        modules.add(new Sprint());
        modules.add(new Step());
        modules.add(new InvMove());
        modules.add(new Jesus());
        modules.add(new AntiVoid());
        modules.add(new SafeWalk());
        modules.add(new NoSlow());
        modules.add(new Longjump());
        modules.add(new Bobbing());
        modules.add(new ClickTp());
        modules.add(new Speerder());
        modules.add(new ClientSpoofer());
        modules.add(new GodMode());
        modules.add(new ChestStealer());
        modules.add(new Scaffold());
        modules.add(new FastPlace());
        modules.add(new SpeedMine());
        modules.add(new FakePlayer());
        modules.add(new Spider());
        modules.add(new Phase());
        modules.add(new Timer());
        modules.add(new AutoAccept());
        modules.add(new Freecam());
        modules.add(new MCF());
        modules.add(new CustomRotations());
        modules.add(new NoRotate());
        modules.add(new ClientSettings());
        modules.add(new Optimizaions());
        modules.add(new Disabler());
        modules.add(new ImageEsp());
       
        
        

     
      
//		this.readSettings();
        EventBus.getInstance().register(this);
    }

   
    public static List<Module> getModules() {
        return modules;
    }

    public synchronized static Module getModuleByClass(Class<? extends Module> cls) {
        for (Module m : modules) {
            if (m.getClass() != cls)
                continue;
            return m;
        }

        System.out.println("no" + cls.getName());
        return modules.get(0);
    }
    
    

    public static Module getModuleByName(String name) {
        for (Module m : modules) {
            if (!m.getName().equalsIgnoreCase(name))
                continue;
            return m;
        }
        return null;
    }

    public List<Module> getModulesInType(ModuleType t) {
        ArrayList<Module> output = new ArrayList<Module>();
        for (Module m : modules) {
            if (m.getType() != t)
                continue;
            output.add(m);
        }
        return output;
    }

    @EventHandler
    private void onKeyPress(EventKey e) {
        for (Module m : modules) {
            if (m.getKey() != e.getKey())
                continue;
            m.setEnabled(!m.isEnabled());
        }
    }

    TimerUtil timerUtil = new TimerUtil();

   

    @EventHandler
    private void on2DRender(EventRender2D e) {
        if (this.enabledNeededMod) {
            this.enabledNeededMod = false;
            for (Module m : modules) {
                if (!m.enabledOnStartup)
                    continue;
                m.setEnabled(true);
            }
        }
    }

    public void readSettings() {
        List<String> binds = FileManager.read("Binds.txt");
        for (String v : binds) {
            String name = v.split(":")[0];
            String bind = v.split(":")[1];
            Module m = ModuleManager.getModuleByName(name);
            if (m == null)
                continue;
            m.setKey(Keyboard.getKeyIndex(bind.toUpperCase()));
        }
        List<String> enabled = FileManager.read("Enabled.txt");
        for (String v : enabled) {
            Module m = ModuleManager.getModuleByName(v);
            if (m == null)
                continue;
            m.enabledOnStartup = true;
        }
        List<String> vals = FileManager.read("Values.txt");
        for (String v : vals) {
            String name = v.split(":")[0];
            String values = v.split(":")[1];
            Module m = ModuleManager.getModuleByName(name);
            if (m == null)
                continue;
            for (Value value : m.getValues()) {
                if (!value.getName().equalsIgnoreCase(values))
                    continue;
                if (value instanceof Option) {
                    value.setValue(Boolean.parseBoolean(v.split(":")[2]));
                    continue;
                }
                if (value instanceof Numbers) {
                    value.setValue(Double.parseDouble(v.split(":")[2]));
                    continue;
                }
                ((Mode) value).setMode(v.split(":")[2]);
            }
        }
    }

    public void saveSettings() {
        StringBuilder content = new StringBuilder();

        for (Module m : modules) {
            content.append(
                    String.format("%s:%s%s", m.getName(), Keyboard.getKeyName(m.getKey()), System.lineSeparator()));
        }

        FileManager.save("Binds.txt", content.toString(), false);

        String values = "";
        for (Module m : ModuleManager.getModules()) {
            for (Value v : m.getValues()) {
                values = String.valueOf(values)
                        + String.format("%s:%s:%s%s", m.getName(), v.getName(), v.getValue(), System.lineSeparator());
            }
        }
        FileManager.save("Values.txt", values, false);
        String enabled = "";
        for (Module m : ModuleManager.getModules()) {
            if (!m.isEnabled())
                continue;
            enabled = String.valueOf(enabled) + String.format("%s%s", m.getName(), System.lineSeparator());
        }
        FileManager.save("Enabled.txt", enabled, false);
    }

    public void saveSettings(String text) {
        String values = "";
        for (Module m : ModuleManager.getModules()) {
            for (Value v : m.getValues()) {
                values = String.valueOf(values)
                        + String.format("%s:%s:%s%s", m.getName(), v.getName(), v.getValue(), System.lineSeparator());
            }
        }
        FileManager.save("Values.txt", values, false);
        String enabled = "";
        for (Module m : ModuleManager.getModules()) {
            if (!m.isEnabled())
                continue;
            enabled = String.valueOf(enabled) + String.format("%s%s", m.getName(), System.lineSeparator());
        }
        FileManager.save("Enabled.txt", enabled, false);
    }
}
