package me.nyan.flush.module;

import me.nyan.flush.Flush;
import me.nyan.flush.event.EventManager;
import me.nyan.flush.module.settings.Setting;
import me.nyan.flush.utils.render.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Module {
    protected final String name;
    protected final ArrayList<Integer> keys = new ArrayList<>();
    private final Category category;
    private final int color;
    private boolean enabled;
    private boolean hidden;
    private float slidingLevel;
    public boolean extended;
    protected final Minecraft mc = Minecraft.getMinecraft();
    protected final Flush flush = Flush.getInstance();
    private final ArrayList<Setting> settings = new ArrayList<>();

    public Module(String name, Category category) {
        this(name, category, false);
    }

    public Module(String name, Category category, boolean hidden) {
        this.name = name;
        this.category = category;
        enabled = false;
        this.hidden = hidden;
        slidingLevel = 0;
        extended = false;
        color = ColorUtils.getRandomColor();
        onLoad();
    }

    public String getName() {
        return name;
    }

    public String getSuffix() {
        return null;
    }

    public void onEnable() {

    }

    public void onDisable() {

    }

    public void onToggle() {

    }

    public void onLoad() {

    }

    public void enable() {
        setEnabled(true);
    }

    public void disable() {
        setEnabled(false);
    }

    public void toggle() {
        onToggle();
        setEnabled(!enabled);
    }

    public void addSetting(Setting setting) {
        settings.add(setting);
    }

    public void addSettings(Setting... settings) {
        for (Setting setting : settings) addSetting(setting);
    }

    public ArrayList<Setting> getSettings() {
        return settings;
    }

    public Setting getSetting(String name) {
        return settings.stream().filter(setting -> setting.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
    public ArrayList<Integer> getKeys() {
        return keys;
    }

    public void addKey(int key) {
        this.keys.add(key);
    }

    public void clearKeys() {
        this.keys.clear();
    }

    public String getKeysString() {
        if (keys.isEmpty()) {
            return "NONE";
        }
        return keys.stream().map(Keyboard::getKeyName).collect(Collectors.joining(", "));
    }

    public Category getCategory() {
        return category;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isHidden() {
        return hidden;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        if (enabled == this.enabled)
            return;

        this.enabled = enabled;

        try {
            if (enabled) {
                EventManager.register(this);
                onEnable();
            } else {
                EventManager.unregister(this);
                onDisable();
            }
        } catch (Exception ignored) {
        }
    }

    public Module getModule(String name) {
        return flush.getModuleManager().getModule(name);
    }

    public boolean isEnabled(String name) {
        return getModule(name).isEnabled();
    }

    public boolean isEnabled(Class<? extends Module> clazz) {
        return getModule(clazz).isEnabled();
    }

    public <T extends Module> T getModule(Class<T> clazz) {
        return flush.getModuleManager().getModule(clazz);
    }

    public float getSlidingLevel() {
        return slidingLevel;
    }

    public void setSlidingLevel(float slidingLevel) {
        slidingLevel = Math.max(Math.min(slidingLevel, 1), 0);
        this.slidingLevel = slidingLevel;
    }

    public int getColor() {
        return color;
    }

    public enum Category {
        COMBAT("Combat", 0xFFF8536F, new ResourceLocation("flush/icons/remix/combat.png"), 0xffE64D3A),
        MOVEMENT("Movement", 0xFF5ACDD1, new ResourceLocation("flush/icons/remix/movement.png"), 0xff2ECD6F),
        PLAYER("Player", 0xFFD0E853, new ResourceLocation("flush/icons/remix/player.png"), 0xff8E45AE),
        RENDER("Render", 0xFF9B5DEC, new ResourceLocation("flush/icons/remix/render.png"), 0xff3601CE),
        WORLD("World", 0xFFF79A3E, new ResourceLocation("flush/icons/remix/world.png"), 0xffE65F00),
        MISC("Misc", 0xFF48E6A7, new ResourceLocation("flush/icons/remix/misc.png"), 0xffF29D11);

        public boolean expanded = false;
        public String name;
        public int remixColor;
        public int astolfoColor;
        public ResourceLocation remixIcon;
        public int moduleIndex;

        Category(String name, int remixColor, ResourceLocation remixIcon, int astolfoColor) {
            this.name = name;
            this.remixColor = remixColor;
            this.remixIcon = remixIcon;
            this.astolfoColor = astolfoColor;
        }
    }
}
