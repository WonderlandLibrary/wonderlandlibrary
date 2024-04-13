package com.canon.majik.api.core;

import com.canon.majik.api.utils.Globals;
import com.canon.majik.impl.modules.api.Category;
import com.canon.majik.impl.modules.api.Module;
import com.canon.majik.impl.modules.impl.TestModule;
import com.canon.majik.impl.modules.impl.client.ClickGui;
import com.canon.majik.impl.modules.impl.client.HudEditor;
import com.canon.majik.impl.modules.impl.combat.*;
import com.canon.majik.impl.modules.impl.exploit.FakeLatency;
import com.canon.majik.impl.modules.impl.hud.TargetHud;
import com.canon.majik.impl.modules.impl.hud.Watermark;
import com.canon.majik.impl.modules.impl.misc.FakePlayer;
import com.canon.majik.impl.modules.impl.misc.FastUse;
import com.canon.majik.impl.modules.impl.misc.MultiTask;
import com.canon.majik.impl.modules.impl.movement.*;
import com.canon.majik.impl.modules.impl.render.*;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;

public class ModuleManager implements Globals {

    ArrayList<Module> modules;

    public ModuleManager(){
        (modules = new ArrayList<Module>()).clear();
        MinecraftForge.EVENT_BUS.register(this);
        initModule();
    }

    public void initModule(){
        modules.addAll(Arrays.asList(
                //Client
                new TestModule(),
                new ClickGui("ClickGui", Keyboard.KEY_RSHIFT, Category.CLIENT),
                new HudEditor("HudEditor", Category.CLIENT),

                //Hud
                new Watermark("Watermark"),
                new TargetHud("TargetHud"),

                //Combat
                new KillAura("KillAura", Category.COMBAT),
                new AutoCrystal("AutoCrystal", Category.COMBAT),
                new HoleFill("HoleFill", Category.COMBAT),
                new BowRelease("BowRelease", Category.COMBAT),
                new AutoTotem("AutoTotem", Category.COMBAT),
                new AutoArmor("AutoArmor", Category.COMBAT),

                //Misc
                new MultiTask("MultiTask", Category.MISC),
                new FastUse("FastUse", Category.MISC),
                new FakePlayer("FakePlayer", Category.MISC),

                //Render
                new ViewModel("ViewModel", Category.RENDER),
                new FullBright("FullBright", Category.RENDER),
                new TimeChanger("TimeChanger", Category.RENDER),
                new HoleESP("HoleESP", Category.RENDER),
                new FogColor("FogColor", Category.RENDER),
                new NoHurtCam("NoHurtCam", Category.RENDER),

                //Exploit
                new FakeLatency("Fake latency", Category.EXPLOITS),

                //Movement
                new Flight("Flight", Category.MOVEMENT),
                new BlockFly("BlockFly", Category.MOVEMENT),
                new Velocity("Velocity", Category.MOVEMENT),
                new Sprint("Sprint", Category.MOVEMENT),
                new Step("Step", Category.MOVEMENT),
                new Speed("Speed", Category.MOVEMENT),
                new FastStop("FastStop", Category.MOVEMENT),
                new NoSlow("NoSlow", Category.MOVEMENT)
        ));
    }

    public ArrayList<Module> getModules() {
        return modules;
    }

}
