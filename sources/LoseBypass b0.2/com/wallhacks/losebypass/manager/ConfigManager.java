/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.manager;

import com.wallhacks.losebypass.LoseBypass;
import com.wallhacks.losebypass.manager.SystemManager;
import com.wallhacks.losebypass.systems.SettingsHolder;
import com.wallhacks.losebypass.systems.clientsetting.ClientSetting;
import com.wallhacks.losebypass.systems.hud.HudComponent;
import com.wallhacks.losebypass.systems.hud.HudSettings;
import com.wallhacks.losebypass.systems.module.Module;
import com.wallhacks.losebypass.systems.setting.Setting;
import com.wallhacks.losebypass.systems.setting.SettingGroup;
import com.wallhacks.losebypass.utils.FileUtil;
import com.wallhacks.losebypass.utils.MC;
import java.util.ArrayList;
import java.util.Iterator;

public class ConfigManager
implements MC {
    String getConfigPath() {
        return LoseBypass.ParentPath.getAbsolutePath() + System.getProperty("file.separator") + "configs";
    }

    public void Load() {
        this.LoadFromConfig();
    }

    public void Save() {
        this.SaveFromConfig(false);
    }

    public void LoadFromConfig() {
        this.loadSystems();
    }

    public void SaveFromConfig(boolean saveDefaults) {
        this.saveSystems(saveDefaults);
    }

    String getSystemSettingFile(SettingsHolder holder) {
        String base = this.getConfigPath() + System.getProperty("file.separator") + "systems" + System.getProperty("file.separator");
        if (holder instanceof Module) {
            base = base + "modules" + System.getProperty("file.separator");
        }
        if (holder instanceof ClientSetting || holder instanceof HudSettings) {
            base = base + "clientSettings" + System.getProperty("file.separator");
        }
        if (!(holder instanceof HudComponent)) return base + holder.getName() + ".cfg";
        base = base + "hudComponents" + System.getProperty("file.separator");
        return base + holder.getName() + ".cfg";
    }

    private void loadSystems() {
        Iterator<SettingsHolder> iterator = SystemManager.getSystems().iterator();
        while (iterator.hasNext()) {
            SettingsHolder system = iterator.next();
            this.loadSettingHolder(system, this.getSystemSettingFile(system));
        }
    }

    void loadSettingHolder(SettingsHolder holder, String file) {
        try {
            String[] List2;
            String s = FileUtil.read(file);
            if (s == null) return;
            String[] stringArray = List2 = s.split("\n");
            int n = stringArray.length;
            int n2 = 0;
            while (true) {
                if (n2 >= n) {
                    holder.onConfigLoad();
                    return;
                }
                String var = stringArray[n2];
                String n3 = var.split(":")[0];
                if (var.split(":").length > 1) {
                    String v = var.split(":")[1];
                    if (holder instanceof Module) {
                        if (n3.equalsIgnoreCase("Toggled")) {
                            ((Module)holder).setEnabled(Boolean.parseBoolean(v));
                        }
                        if (n3.equalsIgnoreCase("Hold")) {
                            ((Module)holder).setHold(Boolean.parseBoolean(v));
                        }
                        if (n3.equalsIgnoreCase("Visible")) {
                            ((Module)holder).setVisible(Boolean.parseBoolean(v));
                        }
                        if (n3.equalsIgnoreCase("Muted")) {
                            ((Module)holder).setMuted(Boolean.parseBoolean(v));
                        }
                        if (n3.equalsIgnoreCase("KeyBind")) {
                            ((Module)holder).setBind(Integer.parseInt(v));
                        }
                    }
                    if (holder instanceof HudComponent) {
                        if (n3.equalsIgnoreCase("Enabled")) {
                            ((HudComponent)holder).setEnabled(Boolean.parseBoolean(v));
                        }
                        if (n3.equalsIgnoreCase("PosY")) {
                            ((HudComponent)holder).posY = Integer.parseInt(v);
                        }
                        if (n3.equalsIgnoreCase("PosX")) {
                            ((HudComponent)holder).posX = Integer.parseInt(v);
                        }
                    }
                    for (Setting<?> setting : holder.getSettings()) {
                        if (!n3.equalsIgnoreCase(setting.getName()) && !n3.equalsIgnoreCase(setting.getsettingsHolder().getName() + "/" + setting.getName())) continue;
                        setting.setValueString(v);
                    }
                }
                ++n2;
            }
        }
        catch (Exception e) {
            LoseBypass.logger.info("Failed to load config for " + holder.getName());
            e.printStackTrace();
        }
    }

    private void saveSystems(boolean saveDefaults) {
        Iterator<SettingsHolder> iterator = SystemManager.getSystems().iterator();
        while (iterator.hasNext()) {
            SettingsHolder system = iterator.next();
            if (system instanceof SettingGroup) continue;
            this.saveSettingHolder(system, this.getSystemSettingFile(system), saveDefaults);
        }
    }

    void saveSettingHolder(SettingsHolder holder, String file, boolean saveDefaults) {
        try {
            ArrayList<String> lines = new ArrayList<String>();
            if (holder instanceof Module) {
                lines.add("Toggled:" + (saveDefaults ? ((Module)holder).getMod().enabled() : ((Module)holder).isEnabled()));
                lines.add("KeyBind:" + (saveDefaults ? ((Module)holder).getMod().bind() : ((Module)holder).getBind()));
                lines.add("Hold:" + (saveDefaults ? ((Module)holder).getMod().hold() : ((Module)holder).isHold()));
                lines.add("Visible:" + (saveDefaults ? ((Module)holder).getMod().visible() : ((Module)holder).isVisible()));
                lines.add("Muted:" + (saveDefaults ? ((Module)holder).getMod().muted() : ((Module)holder).isMuted()));
            }
            if (holder instanceof HudComponent) {
                lines.add("Enabled:" + (saveDefaults ? ((HudComponent)holder).getMod().enabled() : ((HudComponent)holder).isEnabled()));
                lines.add("PosX:" + (saveDefaults ? 0 : ((HudComponent)holder).posX));
                lines.add("PosY:" + (saveDefaults ? 0 : ((HudComponent)holder).posY));
            }
            for (Setting<?> setting : holder.getSettings()) {
                lines.add(setting.getsettingsHolder().getName() + "/" + setting.getName() + ":" + (saveDefaults ? setting.getDefaultValueString() : setting.getValueString()));
            }
            StringBuilder content = new StringBuilder();
            Iterator iterator = lines.iterator();
            while (true) {
                if (!iterator.hasNext()) {
                    FileUtil.write(file, content.toString());
                    holder.onConfigSave();
                    return;
                }
                String e = (String)iterator.next();
                content.append(e).append("\n");
            }
        }
        catch (Exception e) {
            LoseBypass.logger.info("Failed to save config for " + holder.getName());
            e.printStackTrace();
        }
    }
}

