package vestige.module;

import com.mojang.realmsclient.gui.ChatFormatting;
import lombok.Getter;
import lombok.Setter;
import vestige.Vestige;
import vestige.setting.AbstractSetting;
import vestige.util.IMinecraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@Getter
public abstract class Module implements IMinecraft {

    private String name;
    private Category category;

    @Setter
    private int key;

    private boolean enabled;

    private boolean listening;
    protected EventListenType listenType;

    private ArrayList<AbstractSetting> settings = new ArrayList<>();

    public Module(String name, Category category) {
        this.name = name;
        this.category = category;
        this.listenType = EventListenType.AUTOMATIC;
    }

    protected void onEnable() {

    }

    protected void onDisable() {

    }

    public void onClientStarted() {

    }

    public final void setEnabled(boolean enabled) {
        if (this.enabled != enabled) {
            this.enabled = enabled;

            if (enabled) {
                onEnable();
                if(listenType == EventListenType.AUTOMATIC) {
                    startListening();
                }
            } else {
                if(listenType == EventListenType.AUTOMATIC) {
                    stopListening();
                }
                onDisable();
            }
        }
    }

    public final void setEnabledSilently(boolean enabled) {
        if (this.enabled != enabled) {
            this.enabled = enabled;

            if (enabled) {
                if(listenType == EventListenType.AUTOMATIC) {
                    startListening();
                }
            } else {
                if(listenType == EventListenType.AUTOMATIC) {
                    stopListening();
                }
            }
        }
    }

    public final void toggle() {
        this.enabled = !enabled;

        if (enabled) {
            onEnable();
            if(listenType == EventListenType.AUTOMATIC) {
                startListening();
            }
        } else {
            if(listenType == EventListenType.AUTOMATIC) {
                stopListening();
            }
            onDisable();
        }
    }

    public final void toggleSilently() {
        this.enabled = !enabled;

        if (enabled) {
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
        if (!listening) {
            Vestige.instance.getEventManager().register(this);
            listening = true;
        }
    }

    protected final void stopListening() {
        if (listening) {
            Vestige.instance.getEventManager().unregister(this);
            listening = false;
        }
    }

    public void addSettings(AbstractSetting... settings) {
        this.settings.addAll(Arrays.asList(settings));
    }

    public <T extends AbstractSetting> T getSettingByName(String name) {
        Optional<AbstractSetting> optional = settings.stream().filter(m -> m.getName().equals(name)).findFirst();

        if(optional.isPresent()) {
            return (T) optional.get();
        }

        return null;
    }

    public String getSuffix() {
        return null;
    }

    public final String getDisplayName() {
        return getDisplayName(ChatFormatting.GRAY);
    }

    public final String getDisplayName(ChatFormatting formatting) {
        String tag = getSuffix();

        if(tag == null || tag.equals("")) {
            return name;
        }

        return name + formatting + " " + tag;
    }

}