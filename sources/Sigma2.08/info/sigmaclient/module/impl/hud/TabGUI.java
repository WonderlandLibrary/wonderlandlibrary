package info.sigmaclient.module.impl.hud;

import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventKeyPress;
import info.sigmaclient.event.impl.EventRenderGui;
import info.sigmaclient.event.impl.EventTick;
import info.sigmaclient.management.ColorManager;
import info.sigmaclient.management.animate.Translate;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.module.impl.other.ClickGui;
import info.sigmaclient.util.MathUtils;
import info.sigmaclient.util.RenderingUtil;
import info.sigmaclient.util.StringConversions;
import info.sigmaclient.util.Timer;
import info.sigmaclient.util.render.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TabGUI extends Module {

    private int selectedTypeX, moduleBoxY, currentSetting,
            settingBoxX, categoryBoxY, categoryBoxX, currentCategory, targetY, targetModY, targetSetY, moduleBoxX,
            targetX, targetSetX;
    private boolean inModules, inModSet, inSet;
    private Module selectedModule;
    private Timer timer = new Timer();
    public static int opacity = 45;
    private int targetOpacity = 45;
    private boolean isActive;

    private final String TOOLTIPS = "TOOLTIPS";

    public TabGUI(ModuleData data) {
        super(data);
        settings.put(TOOLTIPS, new Setting<>(TOOLTIPS, true, "Shows descriptions of features."));
    }

    private Translate selectedType = new Translate(0, 14);
    private Translate selectedModuleT = new Translate(0, 14);
    private Translate selectedSettingT = new Translate(0, 14);

    @Override
    public void onEnable() {
        targetY = 12;
        categoryBoxY = 0;
        currentCategory = 0;
        inModules = false;
        inModSet = false;
        inSet = false;
    }

    @RegisterEvent(events = {EventRenderGui.class, EventTick.class, EventKeyPress.class})
    public void onEvent(Event event) {
        if (mc.gameSettings.showDebugInfo) {
            return;
        }
        if (event instanceof EventKeyPress) {
            EventKeyPress ek = (EventKeyPress) event;
            if (!isActive && this.keyCheck(ek.getKey())) {
                isActive = true;
                targetOpacity = 200;
                timer.reset();
            }
            if (isActive && this.keyCheck(ek.getKey())) {
                timer.reset();
            }
            if (!inModules) {
                if (ek.getKey() == Keyboard.KEY_DOWN) {
                    targetY += 12;
                    currentCategory++;
                    if (currentCategory > ModuleData.Type.values().length - 1) {
                        targetY = 14;
                        currentCategory = 0;
                    }
                } else if (ek.getKey() == Keyboard.KEY_UP) {
                    targetY -= 12;
                    currentCategory--;
                    if (currentCategory < 0) {
                        targetY = categoryBoxY - 11;
                        currentCategory = ModuleData.Type.values().length - 1;
                    }
                } else if (ek.getKey() == Keyboard.KEY_RIGHT) {
                    inModules = true;
                    moduleBoxY = 0;
                    selectedModuleT.setY(14);
                    targetModY = 14;
                    int longestString = 0;
                    for (Module modxd : Client.getModuleManager().getArray()) {
                        if (modxd.getType() == ModuleData.Type.values()[currentCategory]) {
                            if (longestString < Client.fm.getFont("SFB 8").getWidth(modxd.getName())) {
                                longestString = (int) Client.fm.getFont("SFB 8").getWidth(modxd.getName());
                            }
                        }
                    }
                    targetX = categoryBoxX + 5 + longestString + 15;
                    moduleBoxX = categoryBoxX + 5;
                }
            } else if (!inModSet) {
                if (ek.getKey() == Keyboard.KEY_LEFT) {
                    targetX = categoryBoxX + 6;
                    Thread thread = new Thread(() -> {
                        try {
                            Thread.sleep(110);
                        } catch (InterruptedException e) {
                        }
                        inModules = false;
                    });
                    thread.start();
                }
                if (ek.getKey() == Keyboard.KEY_DOWN) {
                    targetModY += 12;
                    moduleBoxY++;
                    if (moduleBoxY > getModules(ModuleData.Type.values()[currentCategory]).size() - 1) {
                        targetModY = 14;
                        moduleBoxY = 0;
                    }
                } else if (ek.getKey() == Keyboard.KEY_UP) {
                    targetModY -= 12;
                    moduleBoxY--;
                    if (moduleBoxY < 0) {
                        targetModY = (((getModules(ModuleData.Type.values()[currentCategory]).size() - 1) * 12) + 14);
                        moduleBoxY = getModules(ModuleData.Type.values()[currentCategory]).size() - 1;
                    }
                } else if (ek.getKey() == Keyboard.KEY_RETURN) {
                    Module mod = getModules(ModuleData.Type.values()[currentCategory]).get(moduleBoxY);
                    if (!mod.getName().contains("TabGUI") && mod != Client.getModuleManager().get(BubbleGui.class)) {
                        mod.toggle();
                    }
                } else if (ek.getKey() == Keyboard.KEY_RIGHT) {
                    selectedModule = getModules(ModuleData.Type.values()[currentCategory]).get(moduleBoxY);

                    if (!(getSettings(selectedModule) == null)) {
                        inModSet = true;
                        selectedSettingT.setY(14);
                        targetSetY = 14;
                        currentSetting = 0;
                        int longestString = 0;
                        for (Setting modxd : getSettings(selectedModule)) {
                            String faggotXD = modxd.getValue() instanceof Options ? ((Options) modxd.getValue()).getSelected() + "XD" : modxd.getValue().toString();
                            if (longestString < Client.fm.getFont("SFB 8").getWidth(modxd.getName() + " " + faggotXD)) {
                                longestString = (int) Client.fm.getFont("SFB 8").getWidth(modxd.getName() + " " + faggotXD);
                            }
                        }
                        targetSetX = moduleBoxX + longestString + 16;
                        settingBoxX = moduleBoxX + 4;
                    }
                }
            } else if (!inSet) {
                if (ek.getKey() == Keyboard.KEY_LEFT) {
                    targetSetX = moduleBoxX + 4;
                    Thread thread = new Thread(() -> {
                        try {
                            Thread.sleep(110);
                        } catch (InterruptedException e) {
                        }
                        inModSet = false;
                        selectedModule = null;
                    });
                    thread.start();
                } else if (ek.getKey() == Keyboard.KEY_DOWN) {
                    targetSetY += 12;
                    currentSetting++;
                    if (currentSetting > getSettings(selectedModule).size() - 1) {
                        currentSetting = 0;
                        targetSetY = 14;
                    }
                } else if (ek.getKey() == Keyboard.KEY_UP) {
                    targetSetY -= 12;
                    currentSetting--;
                    if (currentSetting < 0) {
                        targetSetY = ((getSettings(selectedModule).size() - 1) * 12) + 14;
                        currentSetting = getSettings(selectedModule).size() - 1;
                    }
                } else if (ek.getKey() == Keyboard.KEY_RIGHT) {
                    inSet = true;
                }
            } else if (inSet) {
                if (ek.getKey() == Keyboard.KEY_LEFT) {
                    inSet = !inSet;
                } else if (ek.getKey() == Keyboard.KEY_UP) {
                    Setting set = getSettings(selectedModule).get(currentSetting);
                    if (set.getValue() instanceof Number) {
                        double increment = (set.getInc());
                        String str = MathUtils.isInteger(MathUtils.getIncremental((((Number) (set.getValue())).doubleValue() + increment), increment)) ?
                                (MathUtils.getIncremental((((Number) (set.getValue())).doubleValue() + increment), increment) + "").replace(".0", "") : MathUtils.getIncremental((((Number) (set.getValue())).doubleValue() + increment), increment) + "";
                        if (Double.parseDouble(str) > set.getMax() && set.getInc() != 0) {
                            return;
                        }
                        Object newValue = (StringConversions.castNumber(str, increment));
                        if (newValue != null) {
                            set.setValue(newValue);
                            Module.saveSettings();
                            return;
                        }
                    } else if (set.getValue().getClass().equals(Boolean.class)) {
                        boolean xd = ((Boolean) set.getValue());
                        set.setValue(!xd);
                        Module.saveSettings();
                    } else if (set.getValue() instanceof Options) {
                        List<String> options = new CopyOnWriteArrayList<>();
                        Collections.addAll(options, ((Options) set.getValue()).getOptions());
                        for (int i = 0; i <= options.size() - 1; i++) {
                            if (options.get(i).equalsIgnoreCase(((Options) set.getValue()).getSelected())) {
                                if (i + 1 > options.size() - 1) {
                                    ((Options) set.getValue()).setSelected(options.get(0));
                                } else {
                                    ((Options) set.getValue()).setSelected(options.get(i + 1));
                                }
                                break;
                            }
                        }
                    }
                } else if (ek.getKey() == Keyboard.KEY_DOWN) {
                    Setting set = getSettings(selectedModule).get(currentSetting);
                    if (set.getValue() instanceof Number) {
                        double increment = (set.getInc());

                        String str = MathUtils.isInteger(MathUtils.getIncremental((((Number) (set.getValue())).doubleValue() - increment), increment)) ?
                                (MathUtils.getIncremental((((Number) (set.getValue())).doubleValue() - increment), increment) + "").replace(".0", "") : MathUtils.getIncremental((((Number) (set.getValue())).doubleValue() - increment), increment) + "";
                        if (Double.parseDouble(str) < set.getMin() && increment != 0) {
                            return;
                        }
                        Object newValue = (StringConversions.castNumber(str, increment));
                        if (newValue != null) {
                            set.setValue(newValue);
                            Module.saveSettings();
                            return;
                        }
                    } else if (set.getValue().getClass().equals(Boolean.class)) {
                        boolean xd = ((Boolean) set.getValue()).booleanValue();
                        set.setValue(!xd);
                        Module.saveSettings();
                    } else if (set.getValue() instanceof Options) {
                        List<String> options = new CopyOnWriteArrayList<>();
                        Collections.addAll(options, ((Options) set.getValue()).getOptions());
                        for (int i = options.size() - 1; i >= 0; i--) {
                            if (options.get(i).equalsIgnoreCase(((Options) set.getValue()).getSelected())) {
                                if (i - 1 < 0) {
                                    ((Options) set.getValue()).setSelected(options.get(options.size() - 1));
                                } else {
                                    ((Options) set.getValue()).setSelected(options.get(i - 1));
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (event instanceof EventTick) {
            if (categoryBoxY == 0) {
                int y = 13;
                int largestString = -1;
                for (ModuleData.Type type : ModuleData.Type.values()) {
                    y += 12;
                    if (Minecraft.getMinecraft().fontRendererObj.getStringWidth(type.name()) > largestString) {
                        largestString = Minecraft.getMinecraft().fontRendererObj.getStringWidth(type.name());
                    }
                }
                categoryBoxY = y;
                categoryBoxX = 53;
                selectedTypeX = 2;
                targetY = 14;
            }
        }
        if (event instanceof EventRenderGui) {
            EventRenderGui er = (EventRenderGui) event;
            if (timer.delay(4500)) {
                targetOpacity = 100;
                isActive = false;
            }
            int diff3 = (targetX) - (moduleBoxX);
            int diff5 = (targetSetX) - settingBoxX;
            int opacityDiff = (targetOpacity) - (opacity);
            opacity += opacityDiff * 0.1;

            selectedType.interpolate(selectedTypeX, targetY, (Math.abs(selectedType.getY() - targetY) > 12) ? 12 : 4);
            selectedModuleT.interpolate(categoryBoxX + 3, targetModY, (Math.abs(selectedModuleT.getY() - targetModY) > 12) ? 12 : 4);
            selectedSettingT.interpolate(0, targetSetY, (Math.abs(selectedSettingT.getY() - targetSetY) > 12) ? 12 : 4);
            moduleBoxX += MathUtils.roundToPlace(diff3 * 0.25, 0);
            if (diff3 == 1) {
                moduleBoxX++;
            } else if (diff3 == -1) {
                moduleBoxX--;
            }
            settingBoxX += MathUtils.roundToPlace(diff5 * 0.25, 0);
            if (diff5 == 1) {
                settingBoxX++;
            } else if (diff5 == -1) {
                settingBoxX--;
            }


            RenderingUtil.rectangle(2, 14, categoryBoxX + 2, categoryBoxY + 1, Colors.getColor(0, opacity));
            RenderingUtil.rectangle(selectedTypeX + 0.3, selectedType.getY() + 0.3, categoryBoxX + 1.5, selectedType.getY() + 11.5,
                    Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, opacity + 64));
            int y = 18;
            for (ModuleData.Type type : ModuleData.Type.values()) {
                boolean isSelected = Math.abs(y - selectedType.getY()) < 6 || y - selectedType.getY() == 6;
                // Client.cf.drawString(type.name(), isSelected ? 7 : 5, y + 1,
                // Colors.getColor, 200));
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                Client.fm.getFont("SFB 8").drawStringWithShadow(type.name(), isSelected ? 5 : 3, y,
                        Colors.getColor(255, 255, 255, opacity + 64));
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
                y += 12;
            }
            if (inModules) {
                List<Module> xd = getModules(ModuleData.Type.values()[currentCategory]);
                y = 18;
                RenderingUtil.rectangle(categoryBoxX + 6, 14, moduleBoxX, ((xd.size()) * 12) + 14, Colors.getColor(0, opacity));

                if (diff3 == 0 && moduleBoxX > categoryBoxX + 6) {
                    RenderingUtil.rectangle(categoryBoxX + 6.3, selectedModuleT.getY() + 0.3, moduleBoxX - 0.3, selectedModuleT.getY() + 11.6,
                            Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, opacity + 64));
                    for (Module mod : getModules(ModuleData.Type.values()[currentCategory])) {
                        if (!(getSettings(mod) == null) && !(selectedModule == mod)) {
                            RenderingUtil.rectangle(moduleBoxX - 2, y - 3.5, moduleBoxX - 0.3, y + 7.6,
                                    Colors.getColor(255, opacity + 64));
                        }
                        boolean isSelected = Math.abs(y - selectedModuleT.getY()) < 6 || y - selectedModuleT.getY() == 6;
                        if((Boolean)settings.get(TOOLTIPS).getValue() && !inModSet) {
                            if(isSelected) {
                                float width = Client.fm.getFont("SFM 8").getWidth(mod.getDescription());
                                RenderingUtil.rectangle(moduleBoxX + 2, selectedModuleT.getY(), moduleBoxX + width + 2, selectedModuleT.getY() + 12,
                                        Colors.getColor(0, opacity));
                                Client.fm.getFont("SFM 8").drawStringWithShadow(mod.getDescription(), moduleBoxX + 3, selectedModuleT.getY() + 4, Colors.getColor(255, opacity + 64));
                            }
                        }
                        // Client.cf.drawString(mod.getName(), categoryBoxX +
                        // (isSelected ? 9 : 7), y + 1,Colors.getColor,
                        // 200));
                        GlStateManager.pushMatrix();
                        GlStateManager.enableAlpha();
                        Client.fm.getFont("SFB 8").drawStringWithShadow(mod.getName(), categoryBoxX + (isSelected ? 9 : 7), y,
                                mod.isEnabled() ? Colors.getColor(255, opacity + 64)
                                        : Colors.getColor(175, opacity + 64));
                        GlStateManager.disableAlpha();
                        GlStateManager.popMatrix();
                        y += 12;
                    }
                }
            }
            if (inModSet) {
                RenderingUtil.rectangle(moduleBoxX + 4, 14, settingBoxX, 14 + getSettings(selectedModule).size() * 12,
                        Colors.getColor(0, opacity));

                if (inSet) {
                    RenderingUtil.rectangleBordered(settingBoxX + 1, selectedSettingT.getY(), settingBoxX + 3.5f, selectedSettingT.getY() + 12, 0.5f,
                            Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, opacity + 64), Colors.getColor(0, opacity));
                }
                int y1 = 18;
                try {
                    RenderingUtil.rectangle(0, 0, 0, 0, 0);
                    RenderingUtil.rectangle(moduleBoxX + 4.3, selectedSettingT.getY() + 0.3, settingBoxX - 0.3, selectedSettingT.getY() + 11.3,
                            Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, opacity + 64));
                    for (Setting setting : getSettings(selectedModule)) {
                        if (setting != null && diff5 == 0 && settingBoxX > moduleBoxX + 4) {

                            boolean isSelected = Math.abs(y1 - selectedSettingT.getY()) < 6 || y1 - selectedSettingT.getY() == 6;
                            if((Boolean)settings.get(TOOLTIPS).getValue() && !inSet) {
                                if(isSelected) {
                                    float width = Client.fm.getFont("SFM 8").getWidth(setting.getDesc());
                                    RenderingUtil.rectangle(settingBoxX + 2, selectedSettingT.getY(), settingBoxX + width + 2, selectedSettingT.getY() + 12,
                                            Colors.getColor(0, opacity));
                                    Client.fm.getFont("SFM 8").drawStringWithShadow(setting.getDesc(), settingBoxX + 3, selectedSettingT.getY() + 4, Colors.getColor(255));
                                }
                            }
                            String xd = setting.getName().charAt(0) + setting.getName().toLowerCase().substring(1);
                            GlStateManager.pushMatrix();
                            String fagniger = setting.getValue() instanceof Options ? ((Options) setting.getValue()).getSelected() : setting.getValue().toString();
                            Client.fm.getFont("SFB 8").drawStringWithShadow(xd + " " + fagniger, moduleBoxX + (isSelected ? 7 : 5), y1, Colors.getColor(255, opacity + 64));
                            GlStateManager.popMatrix();
                            // Client.cf.drawString(xd + " " +
                            // setting.getValue(), moduleBoxX + 3, y1, -1);
                            y1 += 12;
                        }
                    }
                } catch (Exception e) {

                }
            }

        }
    }

    private boolean keyCheck(int key) {
        boolean active = false;
        switch (key) {
            case Keyboard.KEY_DOWN:
                active = true;
                break;
            case Keyboard.KEY_UP:
                active = true;
                break;
            case Keyboard.KEY_RETURN:
                active = true;
                break;
            case Keyboard.KEY_LEFT:
                active = true;
                break;
            case Keyboard.KEY_RIGHT:
                active = true;
                break;
            default:
                break;
        }
        return active;
    }

    private List<Setting> getSettings(Module mod) {
        List<Setting> settings = new CopyOnWriteArrayList<>();
        settings.addAll(mod.getSettings().values());
        for (Setting setting : settings) {
            if (setting.getValue().getClass().equals(String.class)) {
                settings.remove(setting);
            }
        }
        if (settings.isEmpty()) {
            return null;
        }
        settings.sort(Comparator.comparing(Setting::getName));
        return settings;
    }

    private List<Module> getModules(ModuleData.Type type) {
        List<Module> modulesInType = new ArrayList<>();
        for (Module mod : Client.getModuleManager().getArray()) {
            if (mod.getType() == type && !(mod instanceof ClickGui)) {
                modulesInType.add(mod);
            }
        }
        if (modulesInType.isEmpty()) {
            return null;
        }
        modulesInType.sort(Comparator.comparing(Module::getName));
        return modulesInType;
    }
}
