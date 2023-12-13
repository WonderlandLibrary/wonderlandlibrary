package vestige.api.module;

import lombok.Getter;
import lombok.Setter;
import vestige.Vestige;
import vestige.api.setting.Setting;
import vestige.api.setting.impl.KeybindSetting;
import vestige.util.base.IMinecraft;
import vestige.util.misc.LogUtil;
import vestige.util.misc.TimerUtil;

import java.util.ArrayList;
import java.util.Arrays;

import com.mojang.realmsclient.gui.ChatFormatting;

@Getter
public abstract class Module implements IMinecraft {

    private final ModuleInfo info = getClass().getAnnotation(ModuleInfo.class);

    private final String name = info.name();
    
    @Setter
    private String suffix = "";
    private final String description = info.description();
    private final Category category = info.category();
    private final EventListenType listenType = info.listenType();
    private final KeybindSetting keybind = new KeybindSetting("Key", this, info.key());

    protected final ArrayList<Setting> settings = new ArrayList<>();
    
    private final TimerUtil toggleTimer = new TimerUtil();

    private boolean enabled;
    private boolean listening;

    public Module() {
    	settings.add(keybind);
    }

    public void onEnable() {

    }

    public void onDisable() {

    }

    public final void setEnabled(boolean enabled) {
        this.enabled = enabled;

        if(enabled) {
        	toggleTimer.reset();
            onEnable();
            if(listenType == EventListenType.AUTOMATIC) {
                startListening();
            }
        } else {
        	toggleTimer.reset();
            if(listenType == EventListenType.AUTOMATIC) {
                stopListening();
            }
            onDisable();
        }
    }

    public final void setEnabledSilently(boolean enabled) {
        if(this.enabled != enabled) {
            this.enabled = enabled;

            if(listenType == EventListenType.AUTOMATIC) {
                if(enabled) {
                    startListening();
                } else {
                    stopListening();
                }
            }
        }
    }

    public final void toggle() {
        this.enabled = !this.enabled;

        if(this.enabled) {
        	toggleTimer.reset();
            onEnable();
            if(listenType == EventListenType.AUTOMATIC) {
                startListening();
            }
        } else {
        	toggleTimer.reset();
            if(listenType == EventListenType.AUTOMATIC) {
                stopListening();
            }
            onDisable();
        }
    }

    public final void toggleSilently() {
        this.enabled = !this.enabled;

        if(this.enabled) {
            if(listenType == EventListenType.AUTOMATIC) {
                startListening();
            }
        } else {
            if(listenType == EventListenType.AUTOMATIC) {
                stopListening();
            }
        }
    }

    protected final void startListening() {
        if(!listening) {
            Vestige.getInstance().getEventManager().register(this);
            listening = true;
        }
    }

    protected final void stopListening() {
        if(listening) {
            Vestige.getInstance().getEventManager().unregister(this);
            listening = false;
        }
    }

    protected final void registerSettings(Setting... settings) {
        this.settings.addAll(Arrays.asList(settings));
    }

    public String getDisplayName() {
        return hasSuffix() ? getName() + ChatFormatting.GRAY + " " + getSuffix() : getName();
    }

    private boolean hasSuffix() {
        return !getSuffix().equalsIgnoreCase("");
    }

}
