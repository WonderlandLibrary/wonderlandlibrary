// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.scripting.api;

import dev.tenacity.config.DragManager;
import dev.tenacity.module.Module;
import dev.tenacity.Tenacity;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiChat;
import dev.tenacity.utils.objects.Dragging;
import java.util.function.Function;
import dev.tenacity.module.settings.impl.MultipleBoolSetting;
import dev.tenacity.module.settings.impl.StringSetting;
import dev.tenacity.module.settings.impl.ColorSetting;
import java.awt.Color;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.settings.impl.BooleanSetting;
import store.intent.intentguard.annotation.Strategy;
import store.intent.intentguard.annotation.Exclude;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import dev.tenacity.utils.misc.FileUtils;
import dev.tenacity.scripting.api.bindings.StatsBinding;
import dev.tenacity.scripting.api.bindings.ActionBinding;
import dev.tenacity.scripting.api.bindings.EnumFacingBinding;
import dev.tenacity.scripting.api.bindings.PotionBinding;
import dev.tenacity.scripting.api.bindings.FontBinding;
import dev.tenacity.scripting.api.bindings.UserBinding;
import dev.tenacity.scripting.api.bindings.WorldBinding;
import dev.tenacity.scripting.api.bindings.RenderBinding;
import dev.tenacity.scripting.api.bindings.PacketBinding;
import dev.tenacity.scripting.api.bindings.PlayerBinding;
import dev.tenacity.scripting.api.bindings.ClientBinding;
import dev.tenacity.scripting.api.bindings.NotificationBinding;
import javax.script.SimpleBindings;
import jdk.nashorn.api.scripting.ClassFilter;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import jdk.nashorn.api.scripting.JSObject;
import java.util.HashMap;
import java.io.File;
import dev.tenacity.module.settings.Setting;
import java.util.ArrayList;
import dev.tenacity.utils.Utils;

public class Script implements Utils
{
    private final ArrayList<Setting> settings;
    private String name;
    private String spacedName;
    private String author;
    private String description;
    private final File file;
    private final HashMap<String, JSObject> eventHashMap;
    private ScriptModule scriptModule;
    private boolean initializedSettings;
    private boolean reloadable;
    
    public Script(final File file) throws ScriptException {
        this.settings = new ArrayList<Setting>();
        this.eventHashMap = new HashMap<String, JSObject>();
        this.initializedSettings = false;
        this.reloadable = true;
        this.file = file;
        final NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
        final ScriptEngine scriptEngine = factory.getScriptEngine(new ScriptFilter());
        final Bindings manager = new SimpleBindings();
        manager.put("notification", (Object)new NotificationBinding());
        manager.put("client", (Object)new ClientBinding());
        manager.put("player", (Object)new PlayerBinding());
        manager.put("packet", (Object)new PacketBinding());
        manager.put("render", (Object)new RenderBinding());
        manager.put("world", (Object)new WorldBinding());
        manager.put("user", (Object)new UserBinding());
        manager.put("font", (Object)new FontBinding());
        manager.put("potion", (Object)new PotionBinding());
        manager.put("initScript", (Object)new InitializeScript());
        manager.put("color", (Object)new ColorConstructor());
        manager.put("facing", (Object)new EnumFacingBinding());
        manager.put("action", (Object)new ActionBinding());
        manager.put("stats", (Object)new StatsBinding());
        manager.put("createDrag", (Object)new CreateDrag());
        scriptEngine.setBindings(manager, 200);
        final String scriptContent = FileUtils.readFile(file);
        scriptEngine.eval(scriptContent);
        if (this.name == null || this.spacedName == null || this.author == null || this.description == null) {
            throw new ScriptException("Script is missing name, spaced name, author, or description");
        }
        this.registerModule(this.name, this.spacedName, this.description, this.author, file);
        scriptEngine.eval(scriptContent);
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void overrideReload() {
        this.reloadable = false;
        this.scriptModule.setReloadable(false);
    }
    
    private void registerModule(final String name, final String spacedName, final String description, final String author, final File file) {
        this.scriptModule = new ScriptModule(name, spacedName, description, this.eventHashMap, author, file);
        this.settings.forEach(setting -> this.scriptModule.addSettings(setting));
        this.initializedSettings = true;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public BooleanSetting booleanSetting(final String name, final boolean initialValue) {
        if (this.initializedSettings) {
            return this.scriptModule.getSettingsList().stream().filter(setting -> setting instanceof BooleanSetting).map(setting -> setting).filter(setting -> setting.name.equals(name)).findFirst().orElse(null);
        }
        final BooleanSetting booleanSetting = new BooleanSetting(name, initialValue);
        this.settings.add(booleanSetting);
        return booleanSetting;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public ModeSetting modeSetting(final String name, final String startMode, final String... modes) {
        if (this.initializedSettings) {
            return this.scriptModule.getSettingsList().stream().filter(setting -> setting instanceof ModeSetting).map(setting -> setting).filter(setting -> setting.name.equals(name)).findFirst().orElse(null);
        }
        final ModeSetting modeSetting = new ModeSetting(name, startMode, modes);
        this.settings.add(modeSetting);
        return modeSetting;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public NumberSetting numberSetting(final String name, final float defaultValue, final float minValue, final float maxValue, final float increment) {
        if (this.initializedSettings) {
            return this.scriptModule.getSettingsList().stream().filter(setting -> setting instanceof NumberSetting).map(setting -> setting).filter(setting -> setting.name.equals(name)).findFirst().orElse(null);
        }
        final NumberSetting numberSetting = new NumberSetting(name, defaultValue, maxValue, minValue, increment);
        this.settings.add(numberSetting);
        return numberSetting;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public ColorSetting colorSetting(final String name, final Color color) {
        if (this.initializedSettings) {
            return this.scriptModule.getSettingsList().stream().filter(setting -> setting instanceof ColorSetting).map(setting -> setting).filter(setting -> setting.name.equals(name)).findFirst().orElse(null);
        }
        final ColorSetting colorSetting = new ColorSetting(name, color);
        this.settings.add(colorSetting);
        return colorSetting;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public StringSetting stringSetting(final String name, final String string) {
        if (this.initializedSettings) {
            return this.scriptModule.getSettingsList().stream().filter(setting -> setting instanceof StringSetting).map(setting -> setting).filter(setting -> setting.name.equals(name)).findFirst().orElse(null);
        }
        final StringSetting stringSetting = new StringSetting(name, string);
        this.settings.add(stringSetting);
        return stringSetting;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public MultipleBoolSetting multiBoolSetting(final String name, final String... options) {
        if (this.initializedSettings) {
            return this.scriptModule.getSettingsList().stream().filter(setting -> setting instanceof MultipleBoolSetting).map(setting -> setting).filter(setting -> setting.name.equals(name)).findFirst().orElse(null);
        }
        final MultipleBoolSetting multiBoolSetting = new MultipleBoolSetting(name, options);
        this.settings.add(multiBoolSetting);
        return multiBoolSetting;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void onAttack(final JSObject handle) {
        this.eventHashMap.put("attack", handle);
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void onRenderModel(final JSObject handle) {
        this.eventHashMap.put("renderModel", handle);
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void onRender2D(final JSObject handle) {
        this.eventHashMap.put("render", handle);
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void onRender3D(final JSObject handle) {
        this.eventHashMap.put("render3D", handle);
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void onMotion(final JSObject handle) {
        this.eventHashMap.put("motion", handle);
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void onTick(final JSObject handle) {
        this.eventHashMap.put("tick", handle);
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void onMove(final JSObject handle) {
        this.eventHashMap.put("move", handle);
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void onPacketSend(final JSObject handle) {
        this.eventHashMap.put("packetSend", handle);
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void onPacketReceive(final JSObject handle) {
        this.eventHashMap.put("packetReceive", handle);
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void onShader(final JSObject handle) {
        this.eventHashMap.put("shader", handle);
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void onChatReceived(final JSObject handle) {
        this.eventHashMap.put("chat", handle);
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void onPlayerSendMessage(final JSObject handle) {
        this.eventHashMap.put("playerMessage", handle);
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void onWorldLoad(final JSObject handle) {
        this.eventHashMap.put("worldLoad", handle);
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void onSafeWalk(final JSObject handle) {
        this.eventHashMap.put("safewalk", handle);
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void onCustomBlockRender(final JSObject handle) {
        this.eventHashMap.put("customBlockRender", handle);
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void onEnable(final JSObject handle) {
        this.eventHashMap.put("enable", handle);
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void onDisable(final JSObject handle) {
        this.eventHashMap.put("disable", handle);
    }
    
    public ArrayList<Setting> getSettings() {
        return this.settings;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getSpacedName() {
        return this.spacedName;
    }
    
    public String getAuthor() {
        return this.author;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public File getFile() {
        return this.file;
    }
    
    public HashMap<String, JSObject> getEventHashMap() {
        return this.eventHashMap;
    }
    
    public ScriptModule getScriptModule() {
        return this.scriptModule;
    }
    
    public boolean isInitializedSettings() {
        return this.initializedSettings;
    }
    
    public boolean isReloadable() {
        return this.reloadable;
    }
    
    public void setReloadable(final boolean reloadable) {
        this.reloadable = reloadable;
    }
    
    private class CreateDrag implements Function<JSObject, Dragging>
    {
        @Override
        public Dragging apply(final JSObject jsObject) {
            final int initialX = (int)jsObject.getMember("initialX");
            final int initialY = (int)jsObject.getMember("initialY");
            if (Utils.mc.currentScreen instanceof GuiChat) {
                Utils.mc.displayGuiScreen(null);
            }
            final Dragging dragging = Tenacity.INSTANCE.createDrag(Script.this.scriptModule, "script" + Script.this.description, (float)initialX, (float)initialY);
            DragManager.loadDragData();
            return dragging;
        }
    }
    
    private static class ColorConstructor implements Function<JSObject, Color>
    {
        @Override
        public Color apply(final JSObject jsObject) {
            if (jsObject.hasMember("hex")) {
                return new Color((int)jsObject.getMember("hex"));
            }
            final int red = (int)jsObject.getMember("red");
            final int green = (int)jsObject.getMember("green");
            final int blue = (int)jsObject.getMember("blue");
            int alpha = 255;
            if (jsObject.hasMember("alpha")) {
                alpha = (int)jsObject.getMember("alpha");
            }
            return new Color(red, green, blue, alpha);
        }
    }
    
    private class InitializeScript implements Function<JSObject, Script>
    {
        @Override
        public Script apply(final JSObject jsObject) {
            Script.this.name = (String)jsObject.getMember("name");
            Script.this.description = (String)jsObject.getMember("description");
            Script.this.author = (String)jsObject.getMember("author");
            return Script.this;
        }
    }
}
