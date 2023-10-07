package cc.novoline.modules.misc;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.LoadWorldEvent;
import cc.novoline.events.events.PlayerUpdateEvent;
import cc.novoline.gui.screen.setting.Setting;
import cc.novoline.gui.screen.setting.SettingType;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.modules.configurations.annotation.Property;
import cc.novoline.modules.configurations.property.object.BooleanProperty;
import cc.novoline.modules.configurations.property.object.PropertyFactory;
import cc.novoline.modules.configurations.property.object.StringProperty;
import net.minecraft.client.network.NetworkPlayerInfo;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static cc.novoline.gui.screen.setting.Manager.put;

public final class Streamer extends AbstractModule {

    /* fields */
    public List<String> name_data = new CopyOnWriteArrayList<>();

    /* properties @off */
    @Property("local-player-name")
    private final StringProperty your_name = PropertyFactory.createString("Team Khonsari");
    @Property("local-player")
    private final BooleanProperty hide_yourself = PropertyFactory.booleanFalse();
    @Property("scrambled-enemies")
    private final BooleanProperty hide_others = PropertyFactory.booleanFalse();
    @Property("hide-server-id")
    private final BooleanProperty hide_server_id = PropertyFactory.booleanFalse();

    /* constructors @on */
    public Streamer(@NonNull ModuleManager moduleManager) {
        super(moduleManager, "Streamer", EnumModuleType.MISC, "");
        put(new Setting("LOCALSCRAMBLE", "Change your name", SettingType.CHECKBOX, this, this.hide_yourself));
        put(new Setting("LOCAL", "Local name", SettingType.TEXTBOX, this, "New name", this.your_name, hide_yourself::get));
        put(new Setting("SCRAMBLEENEMIES", "Hide others", SettingType.CHECKBOX, this, this.hide_others));
        put(new Setting("HIDEGAMEID", "Hide server ID", SettingType.CHECKBOX, this, this.hide_server_id));
    }

    @EventTarget
    public void onUpdate(PlayerUpdateEvent event) {
        for (NetworkPlayerInfo playerInfo : mc.getNetHandler().getPlayerInfoMap()) {
            String name = playerInfo.getGameProfile().getName();

            if (!name_data.contains(name)) {
                name_data.add(name);
            }
        }
    }

    @EventTarget
    public void onLoadWorld(LoadWorldEvent event) {
        if (!name_data.isEmpty()) {
            name_data.clear();
        }
    }

    @Override
    public void onDisable() {
        if (!name_data.isEmpty()) {
            name_data.clear();
        }
    }

    //region Lombok
    public StringProperty getYourName() {
        return this.your_name;
    }

    public BooleanProperty isChangeYourName() {
        return this.hide_yourself;
    }

    public BooleanProperty isHideOthers() {
        return this.hide_others;
    }

    public BooleanProperty isHideServerId() {
        return this.hide_server_id;
    }
    //endregion

}
