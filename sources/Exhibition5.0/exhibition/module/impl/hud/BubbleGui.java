// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.hud;

import java.util.HashMap;
import java.util.Comparator;
import exhibition.util.RotationUtils;
import java.util.ArrayList;
import java.util.List;
import exhibition.event.impl.EventTick;
import exhibition.event.RegisterEvent;
import java.util.Iterator;
import exhibition.util.StringConversions;
import exhibition.util.MathUtils;
import exhibition.util.misc.ChatUtil;
import exhibition.Client;
import exhibition.event.impl.EventKeyPress;
import net.minecraft.client.renderer.GlStateManager;
import exhibition.util.RenderingUtil;
import exhibition.util.render.Colors;
import exhibition.module.data.Setting;
import java.awt.Color;
import exhibition.event.impl.EventRenderGui;
import exhibition.event.Event;
import exhibition.module.data.ModuleData;
import exhibition.util.Timer;
import exhibition.module.Module;

public class BubbleGui extends Module
{
    public int currentSetting;
    public int currentType;
    public int currentMod;
    public int typeRot;
    public int setRot;
    public int modRot;
    public int targetTypeRot;
    public int targetSetRot;
    public int targetModRot;
    public int hiddenX;
    public int targetX;
    public int targetModX;
    public int modX;
    public int targetModSetX;
    public int setModX;
    public int targetSetX;
    public int setX;
    public boolean inModules;
    public boolean inModSet;
    public boolean inSet;
    public Module selectedModule;
    Timer timer;
    int opacity;
    int targetOpacity;
    boolean isActive;
    
    public BubbleGui(final ModuleData data) {
        super(data);
        this.typeRot = -180;
        this.setRot = -180;
        this.modRot = -180;
        this.targetTypeRot = -180;
        this.targetSetRot = -180;
        this.targetModRot = -180;
        this.timer = new Timer();
        this.opacity = 45;
        this.targetOpacity = 45;
    }
    
    @Override
    public void onEnable() {
        this.inModules = false;
        this.inModSet = false;
        this.inSet = false;
        this.targetX = 0;
        this.typeRot = -180;
        this.targetTypeRot = -180;
    }
    
    @RegisterEvent(events = { EventRenderGui.class, EventTick.class, EventKeyPress.class })
    @Override
    public void onEvent(final Event event) {
        if (BubbleGui.mc.gameSettings.showDebugInfo) {
            return;
        }
        if (event instanceof EventRenderGui) {
            final EventRenderGui er = (EventRenderGui)event;
            if (this.timer.delay(4500.0f)) {
                this.targetX = -150;
                this.targetSetX = -150;
                this.targetModX = -150;
                this.inModules = false;
                this.inModSet = false;
                this.inSet = false;
                this.isActive = false;
            }
            this.animate();
            int rot = this.setRot;
            if (this.inModSet) {
                final Color[] color = new Color[this.getSettings(this.selectedModule).size()];
                int currentModVal = 0;
                for (final Setting mod : this.getSettings(this.selectedModule)) {
                    final int posX = 93;
                    final int posZ = 0;
                    final float cos = (float)Math.cos(Math.toRadians(rot));
                    final float sin = (float)Math.sin(Math.toRadians(rot));
                    final float rotY = -(posZ * cos - posX * sin);
                    final float rotX = -(posX * cos + posZ * sin);
                    final int posXT = 110;
                    final int posYT = -BubbleGui.mc.fontRendererObj.FONT_HEIGHT / 2;
                    final float rotY2 = -(posYT * cos - posXT * sin);
                    final float rotX2 = -(posXT * cos + posYT * sin);
                    color[currentModVal] = new Color((int)(Math.sin(currentModVal) * 127.0 + 128.0), (int)(Math.sin(currentModVal + 1.5707963267948966) * 127.0 + 128.0), (int)(Math.sin(currentModVal + 3.141592653589793) * 127.0 + 128.0));
                    RenderingUtil.drawBorderedCircle(this.setX + (int)rotX, (int)rotY + er.getResolution().getScaledHeight() / 2, 10.0, 1.0, Colors.getColor(color[currentModVal].getRed(), color[currentModVal].getGreen(), color[currentModVal].getBlue(), this.getAlpha(rot, 2.35f)), Colors.getColor(20, 20, 20, this.getAlpha(rot, 2.35f)));
                    final String xd = mod.getName().charAt(0) + mod.getName().toLowerCase().substring(1);
                    GlStateManager.pushMatrix();
                    GlStateManager.enableBlend();
                    BubbleGui.mc.fontRendererObj.drawStringWithShadow(xd + " " + (Object)((this.getSettings(this.selectedModule).get(this.currentSetting) == mod) ? mod.getValue() : ""), this.setX + (int)rotX2, (int)rotY2 + er.getResolution().getScaledHeight() / 2, Colors.getColor(255, 255, 255, this.getAlpha(rot, 2.35f)));
                    GlStateManager.disableBlend();
                    GlStateManager.popMatrix();
                    rot -= 19;
                    ++currentModVal;
                }
            }
            rot = this.modRot;
            if (this.inModules) {
                final Color[] color = new Color[this.getModules(ModuleData.Type.values()[this.currentType]).size()];
                int currentModVal = 0;
                for (final Module mod2 : this.getModules(ModuleData.Type.values()[this.currentType])) {
                    final int posX = 66;
                    final int posZ = 0;
                    final float cos = (float)Math.cos(Math.toRadians(rot));
                    final float sin = (float)Math.sin(Math.toRadians(rot));
                    final float rotY = -(posZ * cos - posX * sin);
                    final float rotX = -(posX * cos + posZ * sin);
                    final int posXT = 83;
                    final int posYT = -BubbleGui.mc.fontRendererObj.FONT_HEIGHT / 2;
                    final float rotY2 = -(posYT * cos - posXT * sin);
                    final float rotX2 = -(posXT * cos + posYT * sin);
                    color[currentModVal] = new Color((int)(Math.sin(currentModVal) * 127.0 + 128.0), (int)(Math.sin(currentModVal + 1.5707963267948966) * 127.0 + 128.0), (int)(Math.sin(currentModVal + 3.141592653589793) * 127.0 + 128.0));
                    RenderingUtil.drawBorderedCircle(this.modX + (int)rotX, (int)rotY + er.getResolution().getScaledHeight() / 2, 10.0, 1.0, Colors.getColor(color[currentModVal].getRed(), color[currentModVal].getGreen(), color[currentModVal].getBlue(), this.getAlpha(rot, 2.25f)), Colors.getColor(20, 20, 20, this.getAlpha(rot, 2.25f)));
                    if (!this.inModSet) {
                        GlStateManager.pushMatrix();
                        GlStateManager.enableBlend();
                        BubbleGui.mc.fontRendererObj.drawStringWithShadow(mod2.getName(), this.modX + (int)rotX2, (int)rotY2 + er.getResolution().getScaledHeight() / 2, mod2.isEnabled() ? Colors.getColor(255, 255, 255, this.getAlpha(rot, 2.25f)) : Colors.getColor(175, 175, 175, this.getAlpha(rot, 2.25f)));
                        GlStateManager.disableBlend();
                        GlStateManager.popMatrix();
                    }
                    rot -= 27;
                    ++currentModVal;
                }
            }
            rot = this.typeRot;
            for (final ModuleData.Type type : ModuleData.Type.values()) {
                final int posX = 40;
                final int posZ = 0;
                final float cos = (float)Math.cos(Math.toRadians(rot));
                final float sin = (float)Math.sin(Math.toRadians(rot));
                final float rotY = -(posZ * cos - posX * sin);
                final float rotX = -(posX * cos + posZ * sin);
                final int posXT = 58;
                final int posYT = -BubbleGui.mc.fontRendererObj.FONT_HEIGHT / 2;
                final float rotY2 = -(posYT * cos - posXT * sin);
                final float rotX2 = -(posXT * cos + posYT * sin);
                final Color color2 = new Color(Module.getColor(type));
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                RenderingUtil.drawBorderedCircle(this.hiddenX + (int)rotX, (int)rotY + er.getResolution().getScaledHeight() / 2, 10.0, 1.0, Colors.getColor(color2.getRed(), color2.getGreen(), color2.getBlue(), this.getAlpha(rot, 2.2f)), Colors.getColor(20, 20, 20, this.getAlpha(rot, 2.2f)));
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
                if (!this.inModules) {
                    GlStateManager.pushMatrix();
                    GlStateManager.enableBlend();
                    BubbleGui.mc.fontRendererObj.drawStringWithShadow(type.name(), this.hiddenX + (int)rotX2 + 5, (int)rotY2 + er.getResolution().getScaledHeight() / 2, Colors.getColor(255, 255, 255, this.getAlpha(rot, 2.2f)));
                    GlStateManager.disableBlend();
                    GlStateManager.popMatrix();
                }
                rot -= 45;
            }
            RenderingUtil.drawBorderedCircle(this.hiddenX / 8, er.getResolution().getScaledHeight() / 2, 24.0, 1.0, Colors.getColor(50, 255, 163), Colors.getColor(20, 20, 20));
        }
        if (event instanceof EventKeyPress) {
            final EventKeyPress ek = (EventKeyPress)event;
            if (this.isActive && this.keyCheck(ek.getKey())) {
                this.timer.reset();
            }
            if (!this.isActive && this.keyCheck(ek.getKey())) {
                this.isActive = true;
                this.inModules = false;
                this.targetX = 0;
                this.targetModX = -150;
                this.timer.reset();
                return;
            }
            if (!this.inModules && this.isActive) {
                if (ek.getKey() == 208) {
                    this.targetTypeRot += 45;
                    ++this.currentType;
                    if (this.currentType > ModuleData.Type.values().length - 1) {
                        this.targetTypeRot = -180;
                        this.currentType = 0;
                    }
                }
                else if (ek.getKey() == 200) {
                    this.targetTypeRot -= 45;
                    --this.currentType;
                    if (this.currentType < 0) {
                        this.currentType = ModuleData.Type.values().length - 1;
                        this.targetTypeRot = -180 + (ModuleData.Type.values().length - 1) * 45;
                    }
                }
                else if (ek.getKey() == 205) {
                    this.inModules = true;
                    this.targetModRot = -180;
                    this.modRot = -180;
                    this.currentMod = 0;
                    this.targetModX = 0;
                }
                else if (ek.getKey() == 203) {
                    this.isActive = false;
                    this.targetX = -150;
                    this.modX = -175;
                }
            }
            else if (this.inModules && !this.inModSet && this.isActive && !this.inSet) {
                if (ek.getKey() == 203) {
                    final Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                BubbleGui.this.targetModX = -170;
                                Thread.sleep(150L);
                            }
                            catch (InterruptedException ex) {}
                            BubbleGui.this.inModules = false;
                        }
                    });
                    thread.start();
                }
                if (ek.getKey() == 208) {
                    this.targetModRot += 27;
                    ++this.currentMod;
                    if (this.currentMod > this.getModules(ModuleData.Type.values()[this.currentType]).size() - 1) {
                        this.targetModRot = -180;
                        this.currentMod = 0;
                    }
                }
                else if (ek.getKey() == 200) {
                    this.targetModRot -= 27;
                    --this.currentMod;
                    if (this.currentMod < 0) {
                        this.targetModRot = -180 + (this.getModules(ModuleData.Type.values()[this.currentType]).size() - 1) * 27;
                        this.currentMod = this.getModules(ModuleData.Type.values()[this.currentType]).size() - 1;
                    }
                }
                else if (ek.getKey() == 28) {
                    try {
                        final Module mod3 = this.getModules(ModuleData.Type.values()[this.currentType]).get(this.currentMod);
                        if (mod3 != this || mod3 != Client.getModuleManager().get(TabGUI.class)) {
                            mod3.toggle();
                        }
                    }
                    catch (Exception e) {
                        ChatUtil.printChat(this.getModules(ModuleData.Type.values()[this.currentType]).size() + ", " + this.currentMod + ", ");
                    }
                }
                else if (ek.getKey() == 205) {
                    this.selectedModule = this.getModules(ModuleData.Type.values()[this.currentType]).get(this.currentMod);
                    if (this.getSettings(this.selectedModule) != null) {
                        this.inModSet = true;
                        this.targetSetX = 0;
                        this.currentSetting = 0;
                        this.setRot = -180;
                        this.targetSetRot = -180;
                    }
                    else if (this.selectedModule != Client.getModuleManager().get(BubbleGui.class) || this.selectedModule != Client.getModuleManager().get(TabGUI.class)) {
                        this.selectedModule.toggle();
                    }
                }
            }
            else if (this.inModSet && !this.inSet) {
                if (ek.getKey() == 203) {
                    final Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                BubbleGui.this.targetSetX = -200;
                                Thread.sleep(100L);
                            }
                            catch (InterruptedException ex) {}
                            BubbleGui.this.inModSet = false;
                        }
                    });
                    thread.start();
                }
                else if (ek.getKey() == 208) {
                    this.targetSetRot += 19;
                    ++this.currentSetting;
                    if (this.currentSetting > this.getSettings(this.selectedModule).size() - 1) {
                        this.targetSetRot = -180;
                        this.currentSetting = 0;
                    }
                }
                else if (ek.getKey() == 200) {
                    this.targetSetRot -= 19;
                    --this.currentSetting;
                    if (this.currentSetting < 0) {
                        this.targetSetRot = -180 + (this.getSettings(this.selectedModule).size() - 1) * 19;
                        this.currentSetting = this.getSettings(this.selectedModule).size() - 1;
                    }
                }
                else if (ek.getKey() == 205) {
                    final Setting set = this.getSettings(this.selectedModule).get(this.currentSetting);
                    if (set.getValue() instanceof Number) {
                        this.inSet = true;
                    }
                    else if (set.getValue().getClass().equals(Boolean.class)) {
                        final boolean xd2 = set.getValue();
                        set.setValue(!xd2);
                        Module.saveSettings();
                    }
                }
            }
            else if (this.inSet) {
                if (ek.getKey() == 203) {
                    this.inSet = false;
                }
                else if (ek.getKey() == 200) {
                    final Setting set = this.getSettings(this.selectedModule).get(this.currentSetting);
                    if (set.getValue() instanceof Number) {
                        final double increment = (set.getInc() != 0.0) ? set.getInc() : 0.5;
                        final String str = MathUtils.roundToPlace(set.getValue().doubleValue() + increment, 1) + "";
                        if (Double.parseDouble(str) > set.getMax() && set.getInc() != 0.0) {
                            return;
                        }
                        final Object newValue = StringConversions.castNumber(str, set.getValue());
                        if (newValue != null) {
                            set.setValue(newValue);
                            Module.saveSettings();
                        }
                    }
                }
                else if (ek.getKey() == 208) {
                    final Setting set = this.getSettings(this.selectedModule).get(this.currentSetting);
                    if (set.getValue() instanceof Number) {
                        final double increment = (set.getInc() != 0.0) ? set.getInc() : 0.5;
                        final String str = MathUtils.roundToPlace(set.getValue().doubleValue() - increment, 1) + "";
                        if (Double.parseDouble(str) < set.getMin() && set.getInc() != 0.0) {
                            return;
                        }
                        final Object newValue = StringConversions.castNumber(str, set.getValue());
                        if (newValue != null) {
                            set.setValue(newValue);
                            Module.saveSettings();
                        }
                    }
                }
            }
        }
    }
    
    private void animate() {
        final int diffType = this.targetTypeRot - this.typeRot;
        this.typeRot += (int)(diffType * 0.1);
        if (diffType > 0) {
            ++this.typeRot;
        }
        else if (diffType < 0) {
            --this.typeRot;
        }
        final int diffMod = this.targetModRot - this.modRot;
        this.modRot += (int)(diffMod * 0.15);
        if (diffMod > 0) {
            ++this.modRot;
        }
        else if (diffMod < 0) {
            --this.modRot;
        }
        final int diffSetMod = this.targetSetRot - this.setRot;
        this.setRot += (int)(diffSetMod * 0.2);
        if (diffSetMod > 0) {
            ++this.setRot;
        }
        else if (diffSetMod < 0) {
            --this.setRot;
        }
        final int diffHidden = this.targetX - this.hiddenX;
        this.hiddenX += diffHidden / 9;
        if (diffHidden > 0) {
            ++this.hiddenX;
        }
        else if (diffHidden < 0) {
            --this.hiddenX;
        }
        final int diffModX = this.targetModX - this.modX;
        this.modX += diffModX / 7;
        if (diffModX > 0) {
            ++this.modX;
        }
        else if (diffModX < 0) {
            --this.modX;
        }
        final int diffSetModX = this.targetModSetX - this.setModX;
        this.setModX += diffSetModX / 7;
        if (diffSetModX > 0) {
            ++this.setModX;
        }
        else if (diffSetModX < 0) {
            --this.setModX;
        }
        final int diffSetX = this.targetSetX - this.setX;
        this.setX += diffSetX / 7;
        if (diffSetX > 0) {
            ++this.setX;
        }
        else if (diffSetX < 0) {
            --this.setX;
        }
        final int opacityDiff = this.targetOpacity - this.opacity;
        this.opacity += (int)(opacityDiff * 0.25);
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
                if (!this.inModules) {
                    active = true;
                    break;
                }
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
        for (final Setting set : ((HashMap<K, Setting>)mod.getSettings()).values()) {
            settings.add(set);
        }
        if (settings.isEmpty()) {
            return null;
        }
        return settings;
    }
    
    private int getAlpha(final int rotation, final float multiplier) {
        final float dist = RotationUtils.getDistanceBetweenAngles(-180.0f, rotation);
        int alpha = (int)Math.abs(dist * multiplier / 2.5 * 0.009999999776482582 * 0.0 + (1.0 - dist * multiplier / 2.5 * 0.009999999776482582) * 255.0);
        if (dist > 90.0f) {
            alpha = 20;
        }
        return alpha;
    }
    
    private List<Module> getModules(final ModuleData.Type type) {
        final List<Module> modulesInType = new ArrayList<Module>();
        for (final Module mod : Client.getModuleManager().getArray()) {
            if (mod.getType() == type) {
                modulesInType.add(mod);
                int width = 0;
                if (BubbleGui.mc.fontRendererObj.getStringWidth(mod.getName()) > width) {
                    width = BubbleGui.mc.fontRendererObj.getStringWidth(mod.getName());
                }
            }
        }
        if (modulesInType.isEmpty()) {
            return null;
        }
        modulesInType.sort(new Comparator<Module>() {
            @Override
            public int compare(final Module m1, final Module m2) {
                return m1.getName().compareTo(m2.getName());
            }
        });
        return modulesInType;
    }
}
