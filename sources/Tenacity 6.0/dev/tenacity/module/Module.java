package dev.tenacity.module;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import dev.tenacity.Tenacity;
import dev.tenacity.config.ConfigSetting;
import dev.tenacity.setting.Setting;
import dev.tenacity.util.render.animation.AnimationDirection;
import dev.tenacity.util.render.animation.impl.DecelerateAnimation;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Module {

    @Expose
    @SerializedName("name")
    private final String name;
    private final String description;

    private final ModuleCategory category;

    @Expose
    @SerializedName("enabled")
    private boolean enabled;

    @Expose
    @SerializedName("keycode")
    private int keyCode;

    private boolean hidden, expanded;
    private String suffix;

    @Expose
    @SerializedName("settings")
    private ConfigSetting[] configSettings;
    private final List<Setting<?>> settingList = new ArrayList<>();

    protected final Minecraft mc = Minecraft.getMinecraft();

    private final DecelerateAnimation arrayListAnimation = new DecelerateAnimation(350, 1, AnimationDirection.FORWARDS);

    public Module(final String name, final String description, final ModuleCategory category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public final String getName() {
        return name;
    }

    public final String getDescription() {
        return description;
    }

    public final ModuleCategory getCategory() {
        return category;
    }

    public final List<Setting<?>> getSettingList() {
        return settingList;
    }

    public final int getKeyCode() {
        return keyCode;
    }

    public final String getSuffix() {
        return suffix;
    }

    public final ConfigSetting[] getConfigSettings() {
        return configSettings;
    }

    public final DecelerateAnimation getArrayListAnimation() {
        return arrayListAnimation;
    }

    public final boolean isHidden() {
        return hidden;
    }

    public final boolean isExpanded() {
        return expanded;
    }

    public final boolean isEnabled() {
        return enabled;
    }

    public final void setKeyCode(final int keyCode) {
        this.keyCode = keyCode;
    }

    public final void setHidden(final boolean hidden) {
        this.hidden = hidden;
    }

    public final void setExpanded(final boolean expanded) {
        this.expanded = expanded;
    }

    public final void setSuffix(final String suffix) {
        this.suffix = suffix;
    }

    public final void setConfigSettings(final ConfigSetting[] configSettings) {
        this.configSettings = configSettings;
    }

    public final void initializeSettings(final Setting<?>... settings) {
        settingList.addAll(Arrays.asList(settings));
    }

    public final void setEnabled(final boolean enabled) {
        this.enabled = enabled;
        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    public final void toggle() {
        setEnabled(!isEnabled());
    }

    public void onEnable() {
        Tenacity.getInstance().getEventBus().subscribe(this);
    }

    public void onDisable() {
        Tenacity.getInstance().getEventBus().unsubscribe(this);
    }

}
