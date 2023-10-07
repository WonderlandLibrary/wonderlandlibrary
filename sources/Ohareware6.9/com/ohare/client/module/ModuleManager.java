package com.ohare.client.module;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ohare.client.Client;
import com.ohare.client.event.events.input.KeyPressEvent;
import com.ohare.client.gui.GuiHud;
import com.ohare.client.gui.click.GuiClick;
import com.ohare.client.module.modules.combat.*;
import com.ohare.client.module.modules.exploits.Blink;
import com.ohare.client.module.modules.exploits.Phase;
import com.ohare.client.module.modules.movement.*;
import com.ohare.client.module.modules.other.*;
import com.ohare.client.module.modules.player.*;
import com.ohare.client.module.modules.visuals.*;
import dorkbox.messageBus.annotations.Subscribe;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import java.io.*;
import java.util.*;

/**
 * made by oHare for Client
 *
 * @since 5/25/2019
 **/
public class ModuleManager {
    public Map<String,Module> map = new HashMap<>();
    private File directory;
    private GuiHud hudgui;
    private GuiClick clickgui;
    public Map<String, Module> getModuleMap() {
        return map;
    }

    public void initialize() {
        register(HUD.class);
        register(Trails.class);
        register(AntiVelocity.class);
        register(Criticals.class);
        register(MurderMystery.class);
        register(AutoMatchJoin.class);
        register(ShowAngles.class);
        register(Regen.class);
        register(AntiHurtCam.class);
        register(AntiFreeze.class);
        register(Speed.class);
        register(SkeletonESP.class);
        register(ESP.class);
        register(AntiStrike.class);
        register(AutoGG.class);
        register(Sprint.class);
        register(MCF.class);
        register(AutoArmor.class);
        register(OutlineESP.class);
        register(ChestESP.class);
        register(Jesus.class);
        register(MemeMachine.class);
        register(LongJump.class);
        register(Step.class);
        register(AutoSword.class);
        register(Nametags.class);
        register(KillAura.class);
        register(Sneak.class);
        register(Flight.class);
        register(AntiBot.class);
        register(NoSlowdown.class);
        register(Blink.class);
        register(AutoHeal.class);
        register(Phase.class);
        register(NoFall.class);
        register(ChestStealer.class);
        register(Scaffold.class);
        register(TimeChanger.class);
        register(InventoryCleaner.class);
        register(InvWalk.class);
        register(FullBright.class);
        register(OffScreenESP.class);
        register(AutoApple.class);
        register(Disabler.class);
        register(AntiVoid.class);
        register(NoRender.class);
        register(NoRotate.class);
        register(Tracers.class);
        register(Waypoints.class);
        register(Chams.class);
        register(Freecam.class);
        register(AntiTabComplete.class);
        register(Crosshair.class);
        register(Trajectories.class);
        register(AntiHoldItem.class);
        register(AutoTool.class);
        register(BowAimbot.class);
        register(AutoBackStab.class);
        register(ChestDumper.class);
        register(AutoBard.class);
        register(Capes.class);
        register(Emote.class);
        register(TrashTalker.class);
        register(ChatBypass.class);
        register(Animate.class);
        register(VanishDetector.class);
        register(Detector.class);

        Client.INSTANCE.getBus().subscribe(this);
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }

    private void register(Class<? extends Module> moduleClass) {
        try {
            Module createdModule = moduleClass.newInstance();
            map.put(createdModule.getLabel().toLowerCase(), createdModule);
        } catch (Exception e) {}
    }

    @Subscribe
    public void onKeyPress(KeyPressEvent e) {
        getModuleMap().values().forEach(m -> {
            if (m.getKeybind() == e.getKey()) {
                m.toggle();
            }
        });
        if (e.getKey() == Keyboard.KEY_INSERT) {
            if (hudgui == null) hudgui = new GuiHud();
            Minecraft.getMinecraft().displayGuiScreen(hudgui);
        }
        if (e.getKey() == Keyboard.KEY_RSHIFT) {
            if (clickgui == null) {
                clickgui = new GuiClick();
                clickgui.init();
            }
            Minecraft.getMinecraft().displayGuiScreen(clickgui);
        }
    }

    public ArrayList<Module> getCategoryCheats(Module.Category cat) {
        final ArrayList<Module> modsInCategory = new ArrayList<Module>();
        for (Module mod : Client.INSTANCE.getModuleManager().getModuleMap().values()) {
            if (mod.getCategory() == cat) {
                modsInCategory.add(mod);
            }
        }
        return modsInCategory;
    }

    public boolean isModule(final String modulename) {
        for (Module mod : getModuleMap().values()) {
            if (mod.getLabel().equalsIgnoreCase(modulename)) {
                return true;
            }
        }
        return false;
    }

    public Module getModuleClass(final Class<?> clazz) {
        for (Module mod : getModuleMap().values()) {
            if (mod.getClass().equals(clazz)) {
                return mod;
            }
        }

        return null;
    }

    public Module getModule(String name) {
        return getModuleMap().get(name.toLowerCase());
    }

    public void saveModules() {
        if (getModuleMap().values().isEmpty()) {
            directory.delete();
        }
        File[] files = directory.listFiles();
        if (!directory.exists()) {
            directory.mkdir();
        } else if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
        getModuleMap().values().forEach(module -> {
            File file = new File(directory, module.getLabel() + ".json");
            JsonObject node = new JsonObject();
            module.save(node,true);
            if (node.entrySet().isEmpty()) {
                return;
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                return;
            }
            try (Writer writer = new FileWriter(file)) {
                writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(node));
            } catch (IOException e) {
                file.delete();
            }
        });
        files = directory.listFiles();
        if (files == null || files.length == 0) {
            directory.delete();
        }
    }

    public void loadModules() {
        getModuleMap().values().forEach(module -> {
            final File file = new File(directory, module.getLabel() + ".json");
            if (!file.exists()) {
                return;
            }
            try (Reader reader = new FileReader(file)) {
                JsonElement node = new JsonParser().parse(reader);
                if (!node.isJsonObject()) {
                    return;
                }
                module.load(node.getAsJsonObject());
            } catch (IOException e) {
            }
        });
    }
}
