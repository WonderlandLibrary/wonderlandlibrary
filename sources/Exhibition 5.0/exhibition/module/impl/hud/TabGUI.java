// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.hud;

import java.util.HashMap;
import java.util.function.Function;
import java.util.Comparator;
import exhibition.event.RegisterEvent;
import java.util.List;
import java.util.Iterator;
import net.minecraft.client.renderer.GlStateManager;
import exhibition.management.ColorManager;
import exhibition.util.RenderingUtil;
import exhibition.util.render.Colors;
import exhibition.event.impl.EventRenderGui;
import net.minecraft.client.Minecraft;
import exhibition.event.impl.EventTick;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import exhibition.util.StringConversions;
import exhibition.util.MathUtils;
import exhibition.module.data.Options;
import exhibition.module.data.Setting;
import exhibition.util.misc.ChatUtil;
import exhibition.Client;
import exhibition.event.impl.EventKeyPress;
import exhibition.event.Event;
import exhibition.module.data.ModuleData;
import exhibition.management.animate.Expand;
import exhibition.management.animate.Translate;
import exhibition.util.Timer;
import exhibition.module.Module;

public class TabGUI extends Module
{
    private int selectedTypeX;
    private int selectedModuleY;
    private int selectedSetY;
    private int moduleBoxY;
    private int currentSetting;
    private int settingBoxX;
    private int categoryBoxY;
    private int categoryBoxX;
    private int currentCategory;
    private int targetY;
    private int targetModY;
    private int targetSetY;
    private int moduleBoxX;
    private int targetX;
    private int targetSetX;
    private boolean inModules;
    private boolean inModSet;
    private boolean inSet;
    private Module selectedModule;
    private Timer timer;
    private int opacity;
    private int targetOpacity;
    private boolean isActive;
    private Translate selectedType;
    private Translate selectedModuleT;
    private Translate selectedSettingT;
    private Expand moduleExpand;
    
    public TabGUI(final ModuleData data) {
        super(data);
        this.timer = new Timer();
        this.opacity = 45;
        this.targetOpacity = 45;
        this.selectedType = new Translate(0.0f, 14.0f);
        this.selectedModuleT = new Translate(0.0f, 14.0f);
        this.selectedSettingT = new Translate(0.0f, 14.0f);
    }
    
    @Override
    public void onEnable() {
        this.targetY = 12;
        this.categoryBoxY = 0;
        this.currentCategory = 0;
        this.inModules = false;
        this.inModSet = false;
        this.inSet = false;
    }
    
    @RegisterEvent(events = { EventRenderGui.class, EventTick.class, EventKeyPress.class })
    @Override
    public void onEvent(final Event event) {
        if (TabGUI.mc.gameSettings.showDebugInfo) {
            return;
        }
        if (event instanceof EventKeyPress) {
            final EventKeyPress ek = (EventKeyPress)event;
            if (!this.isActive && this.keyCheck(ek.getKey())) {
                this.isActive = true;
                this.targetOpacity = 200;
                this.timer.reset();
            }
            if (this.isActive && this.keyCheck(ek.getKey())) {
                this.timer.reset();
            }
            if (!this.inModules) {
                if (ek.getKey() == 208) {
                    this.targetY += 12;
                    ++this.currentCategory;
                    if (this.currentCategory > ModuleData.Type.values().length - 1) {
                        this.targetY = 14;
                        this.currentCategory = 0;
                    }
                }
                else if (ek.getKey() == 200) {
                    this.targetY -= 12;
                    --this.currentCategory;
                    if (this.currentCategory < 0) {
                        this.targetY = this.categoryBoxY - 11;
                        this.currentCategory = ModuleData.Type.values().length - 1;
                    }
                }
                else if (ek.getKey() == 205) {
                    this.inModules = true;
                    this.moduleBoxY = 0;
                    this.selectedModuleT.setY(14.0f);
                    this.targetModY = 14;
                    int longestString = 0;
                    for (final Module modxd : Client.getModuleManager().getArray()) {
                        if (modxd.getType() == ModuleData.Type.values()[this.currentCategory] && longestString < TabGUI.mc.fontRendererObj.getStringWidth(modxd.getName())) {
                            longestString = TabGUI.mc.fontRendererObj.getStringWidth(modxd.getName());
                        }
                    }
                    this.targetX = this.categoryBoxX + 3 + longestString + 7;
                    this.moduleBoxX = this.categoryBoxX + 3;
                }
            }
            else if (!this.inModSet) {
                if (ek.getKey() == 203) {
                    this.targetX = this.categoryBoxX + 3;
                    final Thread thread = new Thread(() -> {
                        try {
                            Thread.sleep(110L);
                        }
                        catch (InterruptedException ex) {}
                        this.inModules = false;
                        return;
                    });
                    thread.start();
                }
                if (ek.getKey() == 208) {
                    this.targetModY += 12;
                    ++this.moduleBoxY;
                    if (this.moduleBoxY > this.getModules(ModuleData.Type.values()[this.currentCategory]).size() - 1) {
                        this.targetModY = 14;
                        this.moduleBoxY = 0;
                    }
                }
                else if (ek.getKey() == 200) {
                    this.targetModY -= 12;
                    --this.moduleBoxY;
                    if (this.moduleBoxY < 0) {
                        this.targetModY = (this.getModules(ModuleData.Type.values()[this.currentCategory]).size() - 1) * 12 + 14;
                        this.moduleBoxY = this.getModules(ModuleData.Type.values()[this.currentCategory]).size() - 1;
                    }
                }
                else if (ek.getKey() == 28) {
                    try {
                        final Module mod = this.getModules(ModuleData.Type.values()[this.currentCategory]).get(this.moduleBoxY);
                        mod.toggle();
                    }
                    catch (Exception e) {
                        ChatUtil.printChat(this.getModules(ModuleData.Type.values()[this.currentCategory]).size() + ", " + this.moduleBoxY + ", ");
                    }
                }
                else if (ek.getKey() == 205) {
                    this.selectedModule = this.getModules(ModuleData.Type.values()[this.currentCategory]).get(this.moduleBoxY);
                    if (this.getSettings(this.selectedModule) != null) {
                        this.inModSet = true;
                        this.selectedSettingT.setY(14.0f);
                        this.targetSetY = 14;
                        this.currentSetting = 0;
                        int longestString = 0;
                        for (final Setting modxd2 : this.getSettings(this.selectedModule)) {
                            final String faggotXD = (modxd2.getValue() instanceof Options) ? modxd2.getValue().getSelected() : modxd2.getValue().toString();
                            if (longestString < TabGUI.mc.fontRendererObj.getStringWidth(modxd2.getName() + " " + faggotXD)) {
                                longestString = TabGUI.mc.fontRendererObj.getStringWidth(modxd2.getName() + " " + faggotXD);
                            }
                        }
                        this.targetSetX = this.moduleBoxX + longestString + 5;
                        this.settingBoxX = this.moduleBoxX + 1;
                    }
                }
            }
            else if (!this.inSet) {
                if (ek.getKey() == 203) {
                    this.targetSetX = this.moduleBoxX + 1;
                    final Thread thread = new Thread(() -> {
                        try {
                            Thread.sleep(110L);
                        }
                        catch (InterruptedException ex2) {}
                        this.inModSet = false;
                        this.selectedModule = null;
                        return;
                    });
                    thread.start();
                }
                else if (ek.getKey() == 208) {
                    this.targetSetY += 12;
                    ++this.currentSetting;
                    if (this.currentSetting > this.getSettings(this.selectedModule).size() - 1) {
                        this.currentSetting = 0;
                        this.targetSetY = 14;
                    }
                }
                else if (ek.getKey() == 200) {
                    this.targetSetY -= 12;
                    --this.currentSetting;
                    if (this.currentSetting < 0) {
                        this.targetSetY = (this.getSettings(this.selectedModule).size() - 1) * 12 + 14;
                        this.currentSetting = this.getSettings(this.selectedModule).size() - 1;
                    }
                }
                else if (ek.getKey() == 205) {
                    this.inSet = true;
                }
            }
            else if (this.inSet) {
                if (ek.getKey() == 203) {
                    this.inSet = !this.inSet;
                }
                else if (ek.getKey() == 200) {
                    final Setting set = this.getSettings(this.selectedModule).get(this.currentSetting);
                    if (set.getValue() instanceof Number) {
                        final double increment = set.getInc();
                        final String str = MathUtils.isInteger(MathUtils.getIncremental(set.getValue().doubleValue() + increment, increment)) ? (MathUtils.getIncremental(set.getValue().doubleValue() + increment, increment) + "").replace(".0", "") : (MathUtils.getIncremental(set.getValue().doubleValue() + increment, increment) + "");
                        if (Double.parseDouble(str) > set.getMax() && set.getInc() != 0.0) {
                            return;
                        }
                        final Object newValue = StringConversions.castNumber(str, increment);
                        if (newValue != null) {
                            set.setValue(newValue);
                            Module.saveSettings();
                            return;
                        }
                    }
                    else if (set.getValue().getClass().equals(Boolean.class)) {
                        final boolean xd = set.getValue();
                        set.setValue(!xd);
                        Module.saveSettings();
                    }
                    else if (set.getValue() instanceof Options) {
                        final List<String> options = new ArrayList<String>();
                        Collections.addAll(options, set.getValue().getOptions());
                        int i = 0;
                        while (i <= options.size() - 1) {
                            if (options.get(i).equalsIgnoreCase(set.getValue().getSelected())) {
                                if (i + 1 > options.size() - 1) {
                                    set.getValue().setSelected(options.get(0));
                                    break;
                                }
                                set.getValue().setSelected(options.get(i + 1));
                                break;
                            }
                            else {
                                ++i;
                            }
                        }
                    }
                }
                else if (ek.getKey() == 208) {
                    final Setting set = this.getSettings(this.selectedModule).get(this.currentSetting);
                    if (set.getValue() instanceof Number) {
                        final double increment = set.getInc();
                        final String str = MathUtils.isInteger(MathUtils.getIncremental(set.getValue().doubleValue() - increment, increment)) ? (MathUtils.getIncremental(set.getValue().doubleValue() - increment, increment) + "").replace(".0", "") : (MathUtils.getIncremental(set.getValue().doubleValue() - increment, increment) + "");
                        if (Double.parseDouble(str) < set.getMin() && increment != 0.0) {
                            return;
                        }
                        final Object newValue = StringConversions.castNumber(str, increment);
                        if (newValue != null) {
                            set.setValue(newValue);
                            Module.saveSettings();
                            return;
                        }
                    }
                    else if (set.getValue().getClass().equals(Boolean.class)) {
                        final boolean xd = set.getValue();
                        set.setValue(!xd);
                        Module.saveSettings();
                    }
                    else if (set.getValue() instanceof Options) {
                        final List<String> options = new ArrayList<String>();
                        Collections.addAll(options, set.getValue().getOptions());
                        int i = options.size() - 1;
                        while (i >= 0) {
                            if (options.get(i).equalsIgnoreCase(set.getValue().getSelected())) {
                                if (i - 1 < 0) {
                                    set.getValue().setSelected(options.get(options.size() - 1));
                                    break;
                                }
                                set.getValue().setSelected(options.get(i - 1));
                                break;
                            }
                            else {
                                --i;
                            }
                        }
                    }
                }
            }
        }
        if (event instanceof EventTick && this.categoryBoxY == 0) {
            int y = 13;
            int largestString = -1;
            for (final ModuleData.Type type : ModuleData.Type.values()) {
                y += 12;
                if (Minecraft.getMinecraft().fontRendererObj.getStringWidth(type.name()) > largestString) {
                    largestString = Minecraft.getMinecraft().fontRendererObj.getStringWidth(type.name());
                }
            }
            this.categoryBoxY = y;
            this.categoryBoxX = largestString + 7;
            this.selectedTypeX = 2;
            this.targetY = 14;
        }
        if (event instanceof EventRenderGui) {
            final EventRenderGui er = (EventRenderGui)event;
            if (this.timer.delay(4500.0f)) {
                this.targetOpacity = 100;
                this.isActive = false;
            }
            final int diff3 = this.targetX - this.moduleBoxX;
            final int diff4 = this.targetSetX - this.settingBoxX;
            final int opacityDiff = this.targetOpacity - this.opacity;
            this.opacity += (int)(opacityDiff * 0.1);
            this.selectedType.interpolate(this.selectedTypeX, this.targetY, (Math.abs(this.selectedType.getY() - this.targetY) > 12.0f) ? 12 : 4);
            this.selectedModuleT.interpolate(this.categoryBoxX + 3, this.targetModY, (Math.abs(this.selectedModuleT.getY() - this.targetModY) > 12.0f) ? 12 : 4);
            this.selectedSettingT.interpolate(0.0f, this.targetSetY, (Math.abs(this.selectedSettingT.getY() - this.targetSetY) > 12.0f) ? 12 : 4);
            this.moduleBoxX += (int)MathUtils.roundToPlace(diff3 * 0.25, 0);
            if (diff3 == 1) {
                ++this.moduleBoxX;
            }
            else if (diff3 == -1) {
                --this.moduleBoxX;
            }
            this.settingBoxX += (int)MathUtils.roundToPlace(diff4 * 0.25, 0);
            if (diff4 == 1) {
                ++this.settingBoxX;
            }
            else if (diff4 == -1) {
                --this.settingBoxX;
            }
            RenderingUtil.rectangle(2.0, 14.0, this.categoryBoxX + 2, this.categoryBoxY + 1, Colors.getColor(0, 0, 0, this.opacity));
            RenderingUtil.rectangle(this.selectedTypeX + 0.3, this.selectedType.getY() + 0.3, this.categoryBoxX + 2 - 0.3, this.selectedType.getY() + 12.0f - 0.3, Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, this.opacity + 64));
            int y2 = 16;
            for (final ModuleData.Type type2 : ModuleData.Type.values()) {
                final boolean isSelected = type2 == ModuleData.Type.values()[this.currentCategory];
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                TabGUI.mc.fontRendererObj.drawStringWithShadow(type2.name(), isSelected ? 6.0f : 4.0f, y2, Colors.getColor(255, 255, 255, this.opacity + 64));
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
                y2 += 12;
            }
            if (this.inModules) {
                final List<Module> xd2 = this.getModules(ModuleData.Type.values()[this.currentCategory]);
                y2 = 16;
                RenderingUtil.rectangle(this.categoryBoxX + 3, 14.0, this.moduleBoxX, xd2.size() * 12 + 14, Colors.getColor(0, 0, 0, this.opacity));
                RenderingUtil.rectangle(this.categoryBoxX + 3 + 0.3, this.selectedModuleT.getY() + 0.3, this.moduleBoxX - 0.3, this.selectedModuleT.getY() + 12.0f - 0.3, Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, this.opacity + 64));
                if (diff3 == 0 && this.moduleBoxX > this.categoryBoxX + 3) {
                    for (final Module mod2 : this.getModules(ModuleData.Type.values()[this.currentCategory])) {
                        if (this.getSettings(mod2) != null && !this.inModSet) {
                            RenderingUtil.rectangle(this.moduleBoxX + 1, y2 - 1, this.moduleBoxX + 11, y2 + 9, Colors.getColor(0, 0, 0, this.opacity));
                            GlStateManager.pushMatrix();
                            GlStateManager.enableBlend();
                            TabGUI.mc.fontRendererObj.drawStringWithShadow("+", this.moduleBoxX + 3.0f, y2 + 0.5f, Colors.getColor(255, this.opacity + 64));
                            GlStateManager.disableBlend();
                            GlStateManager.popMatrix();
                        }
                        final boolean isSelected2 = this.getModules(ModuleData.Type.values()[this.currentCategory]).get(this.moduleBoxY) == mod2;
                        GlStateManager.pushMatrix();
                        GlStateManager.enableBlend();
                        TabGUI.mc.fontRendererObj.drawStringWithShadow(mod2.getName(), this.categoryBoxX + (isSelected2 ? 8 : 6), y2, mod2.isEnabled() ? Colors.getColor(255, this.opacity + 64) : Colors.getColor(175, this.opacity + 64));
                        GlStateManager.disableBlend();
                        GlStateManager.popMatrix();
                        y2 += 12;
                    }
                }
            }
            if (this.inModSet) {
                RenderingUtil.rectangle(this.moduleBoxX + 1, 14.0, this.settingBoxX, 14 + this.selectedModule.getSettings().size() * 12, Colors.getColor(0, 0, 0, this.opacity));
                RenderingUtil.rectangle(this.moduleBoxX + 1 + 0.3, this.selectedSettingT.getY() + 0.3, this.settingBoxX - 0.3, this.selectedSettingT.getY() + 12.0f - 0.3, Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, this.opacity + 64));
                int y3 = 16;
                try {
                    for (final Setting setting : ((HashMap<K, Setting>)this.selectedModule.getSettings()).values()) {
                        if (setting != null && diff4 == 0 && this.settingBoxX > this.moduleBoxX + 3) {
                            final String xd3 = setting.getName().charAt(0) + setting.getName().toLowerCase().substring(1);
                            GlStateManager.pushMatrix();
                            GlStateManager.enableBlend();
                            final String fagniger = (setting.getValue() instanceof Options) ? setting.getValue().getSelected() : setting.getValue().toString();
                            TabGUI.mc.fontRendererObj.drawStringWithShadow(xd3 + " " + fagniger, this.moduleBoxX + 4, y3, Colors.getColor(255, this.opacity + 64));
                            GlStateManager.disableBlend();
                            GlStateManager.popMatrix();
                            y3 += 12;
                        }
                    }
                }
                catch (Exception ex3) {}
            }
            if (this.inSet) {
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                RenderingUtil.drawCircle(this.settingBoxX + 6, this.selectedSettingT.getY() + 6.0f, 4.6f, 32, Colors.getColor(0, this.opacity));
                RenderingUtil.drawCircle(this.settingBoxX + 6, this.selectedSettingT.getY() + 6.0f, 4.0f, 32, Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, this.opacity + 64));
                RenderingUtil.drawCircle(this.settingBoxX + 6, this.selectedSettingT.getY() + 6.0f, 3.0f, 32, Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, this.opacity + 64));
                RenderingUtil.drawCircle(this.settingBoxX + 6, this.selectedSettingT.getY() + 6.0f, 2.0f, 32, Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, this.opacity + 64));
                RenderingUtil.drawCircle(this.settingBoxX + 6, this.selectedSettingT.getY() + 6.0f, 1.0f, 32, Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, this.opacity + 64));
                RenderingUtil.drawCircle(this.settingBoxX + 6, this.selectedSettingT.getY() + 6.0f, 0.0f, 32, Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, this.opacity + 64));
                TabGUI.mc.fontRendererObj.drawStringWithShadow("<", this.settingBoxX + 3, this.selectedSettingT.getY() + 2.5f, Colors.getColor(255, 255, 255, this.opacity + 64));
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
        }
    }
    
    private boolean keyCheck(final int key) {
        boolean active = false;
        switch (key) {
            case 208: {
                active = true;
                break;
            }
            case 200: {
                active = true;
                break;
            }
            case 28: {
                active = true;
                break;
            }
            case 203: {
                active = true;
                break;
            }
            case 205: {
                active = true;
                break;
            }
        }
        return active;
    }
    
    private List<Setting> getSettings(final Module mod) {
        final List<Setting> settings = new ArrayList<Setting>();
        settings.addAll(((HashMap<K, ? extends Setting>)mod.getSettings()).values());
        if (settings.isEmpty()) {
            return null;
        }
        return settings;
    }
    
    private List<Module> getModules(final ModuleData.Type type) {
        final List<Module> modulesInType = new ArrayList<Module>();
        int width = 0;
        for (final Module mod : Client.getModuleManager().getArray()) {
            if (mod.getType() == type) {
                modulesInType.add(mod);
                if (TabGUI.mc.fontRendererObj.getStringWidth(mod.getName()) > width) {
                    width = TabGUI.mc.fontRendererObj.getStringWidth(mod.getName());
                    this.selectedModuleT.setX(TabGUI.mc.fontRendererObj.getStringWidth(mod.getName()));
                }
            }
        }
        if (modulesInType.isEmpty()) {
            return null;
        }
        modulesInType.sort(Comparator.comparing((Function<? super Module, ? extends Comparable>)Module::getName));
        return modulesInType;
    }
}
