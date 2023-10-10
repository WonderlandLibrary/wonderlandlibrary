package today.flux.module;

import com.darkmagician6.eventapi.EventManager;
import com.soterdev.SoterObfuscator;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;
import today.flux.Flux;
import today.flux.addon.api.module.AddonModule;
import today.flux.gui.hud.notification.NotificationManager;
import today.flux.module.implement.Render.Hud;
import today.flux.module.value.ModeValue;
import today.flux.utility.ChatUtils;
import today.flux.utility.SmoothAnimationTimer;

import java.util.ArrayList;
import java.util.List;

public class Module {
    // ClickGUI
    public SmoothAnimationTimer ySmooth = new SmoothAnimationTimer(100);

    @Getter @Setter
    AddonModule addonModule;

    final Category category;

    @Getter @Setter
    boolean isHide, isSilent;
    @Getter
    public boolean isEnabled;
    private String name, tag = "";

    private ModeValue mode;
    private final List<SubModule> subModuleList;
    private final boolean hasSubModule;

    private int bind;
    public static Minecraft mc = Minecraft.getMinecraft();

    public boolean cantToggle = false;
    public float toggleButtonAnimation = 218f;
    public float animationY = 0f;

    // Verify
    public static int a = 0;

    public Module(String name, Category cat, ModeValue mode) {
        this(name, cat, false);
        this.mode = mode;
    }

    public Module(String name, Category cat, boolean hasSubModule, SubModule... subModules) {
        this.subModuleList = new ArrayList<>();
        this.bind = Keyboard.KEY_NONE;
        this.category = cat;
        this.name = name;
        this.hasSubModule = hasSubModule;

        if (!hasSubModule)
            return;

        SubModule defaultSubModule = null;
        for (SubModule item : subModules) {
            if (defaultSubModule == null)
                defaultSubModule = item;

            addSubModule(item);
        }

        List<String> submodnames = new ArrayList<>();
        if (!this.subModuleList.isEmpty()) {
            for (SubModule mod : this.subModuleList)
                submodnames.add(mod.getName());
        }

        if (defaultSubModule != null) {
            this.mode = new ModeValue(this.name, "Mode", defaultSubModule.getName(), submodnames.toArray(new String[submodnames.size()]));
        }
    }

    public void toggle() {

        if (this.cantToggle) return;

        this.setEnabled(!this.isEnabled);
        //notification
        NotificationManager.show("Module", this.getName() + (this.isEnabled ? " \2472Enabled" : " \247cDisabled"), this);
    }

    public void cooldown(final int time) {
        onDisable();

        new Thread(() -> {
            try {
                Thread.sleep(time);
            } catch (InterruptedException var2) {

            }

            if (isEnabled())
                onEnable();
        }
        ).start();
    }

    public void onEnable() {
        if (a == 1) EventManager.register(this);

        if (addonModule != null) {
            try {
                addonModule.onEnable();
            } catch (Throwable exception) {
                exception.printStackTrace();
                ChatUtils.sendErrorToPlayer(String.format("[Flux API] [%s] Error: %s", getName(), exception.getMessage()));
            }
        }

        if (hasSubModule)
            this.updateSubModule(true);
    }

    public void onDisable() {
        if (a == 1) EventManager.unregister(this);

        if (addonModule != null) {
            try {
                addonModule.onDisable();
            } catch (Throwable exception) {
                exception.printStackTrace();
                ChatUtils.sendErrorToPlayer(String.format("[Flux API] [%s] Error: %s", getName(), exception.getMessage()));
            }
        }

        if (hasSubModule)
            this.updateSubModule(false);
    }

    public void update() {
        if (a == 1) {
            EventManager.unregister(this);
            if (isEnabled) {
                EventManager.register(this);
            }
        }

        if (addonModule != null) {
            try {
                addonModule.onDisable();
            } catch (Throwable exception) {
                exception.printStackTrace();
                ChatUtils.sendErrorToPlayer(String.format("[Flux API] [%s] Error: %s", getName(), exception.getMessage()));
            }
        }

        if (hasSubModule)
            this.updateSubModule(isEnabled);
    }

    @SoterObfuscator.Obfuscation(flags = "+native")
    public void setEnabled(boolean action) {

        if (this.cantToggle) return;

        if (action) {
            this.isEnabled = true;
            this.onEnable();

            if (mc.theWorld != null && Flux.ToggleSound.getValue() && !isSilent) {
                mc.thePlayer.playSound("random.click", 0.5F, 1.0F);
            }

        } else {
            this.isEnabled = false;
            this.onDisable();

            if (mc.theWorld != null && Flux.ToggleSound.getValue() && !isSilent) {
                mc.thePlayer.playSound("random.click", 0.4F, 0.8F);
            }

        }
    }

    public void enable() {
        this.setEnabled(true);
    }

    public void disable() {
        this.setEnabled(false);
    }

    public void setEnabledSilent(boolean enabled) {
        this.setEnabled(enabled);
    }

    /**
     * SubModule Manager
     **/
    private void addSubModule(SubModule subMod) {
        if (!doesSubModuleExist(subMod.getName())) {
            this.subModuleList.add(subMod);
        }
    }

    private boolean doesSubModuleExist(String submoduleName) {
        for (SubModule subModule : subModuleList) {
            if (subModule.getName().equalsIgnoreCase(submoduleName)) {
                return true;
            }
        }
        return false;
    }

    public boolean setSubModule(String subMod) {
        if (!doesSubModuleExist(subMod))
            return false;

        for (SubModule subModule : subModuleList) {
            if (subModule.getName().equalsIgnoreCase(subMod)) {
                this.mode.setValue(subModule.getName());
            }
        }

        this.updateSubModule(this.isEnabled);

        return true;
    }

    public void updateSubModule(boolean action) {
        if (!hasSubModule)
            return;

        for (SubModule subModule : this.subModuleList) {
            if (subModule.isEnabled()) {
                try {
                    subModule.setEnabled(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (!doesSubModuleExist(this.mode.getValue()))
            return;

        if (!action)
            return;

        for (SubModule subModule : subModuleList) {
            if (subModule.getName().equalsIgnoreCase(this.mode.getValue())) {
                subModule.setEnabled(true);
                break;
            }
        }
    }

    public String getCurrentSubModule() {
        if (mode == null)
            return "";
        return this.mode.getValue();
    }

    public String getDisplayText() {
        if (this.getTag().length() > 0) {
            if (Hud.subModuleMode.isCurrentMode("White")) {
                return this.getName() + " " + EnumChatFormatting.WHITE + this.getTag();
            } else if (Hud.subModuleMode.isCurrentMode("Nostalgia")) {
                return this.getName() + EnumChatFormatting.GRAY + " [" + EnumChatFormatting.WHITE + this.getTag() + EnumChatFormatting.GRAY + "]";
            } else {
                return this.getName() + " " + EnumChatFormatting.GRAY + this.getTag();
            }
        }
        return this.getName();
    }

    public boolean isHasSubModule() {
        return hasSubModule;
    }

    public ModeValue getMode() {
        return mode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        if (tag.length() == 0) {
            return getCurrentSubModule();
        }
        return tag;
    }

    public int getBind() {
        return bind;
    }

    public void setBind(int bind) {
        this.bind = bind;
    }

    public Category getCategory() {
        return category;
    }

}
