package net.caffeinemc.phosphor.module.setting.settings;

import imgui.ImGui;
import imgui.flag.ImGuiDataType;
import imgui.type.ImInt;
import net.caffeinemc.phosphor.api.font.JColor;
import net.caffeinemc.phosphor.module.Module;
import net.caffeinemc.phosphor.module.setting.RenderableSetting;
import net.caffeinemc.phosphor.module.setting.Setting;

import java.util.ArrayList;
import java.util.List;

public class ColorSetting extends Setting implements RenderableSetting {
    private JColor color;
    private ArrayList<RenderableSetting> renderableSettings = new ArrayList<>();
    private boolean alpha;
    private boolean showSliders;
    private boolean rainbow;

    public ColorSetting(String name, Module parent, JColor color, boolean alpha) {
        this.name = name;
        this.parent = parent;
        this.color = color;
        this.alpha = alpha;
        this.showSliders = false;
        this.rainbow = false;

        renderableSettings.addAll(List.of(
                new BooleanRainbowSetting("Rainbow", parent.getName()+"/"+this.getName()+"/Rainbow", rainbow),
                new ColorSliderSetting("Red", parent.getName()+"/"+this.getName()+"/Red", color.getRed()),
                new ColorSliderSetting("Green", parent.getName()+"/"+this.getName()+"/Green", color.getGreen()),
                new ColorSliderSetting("Blue", parent.getName()+"/"+this.getName()+"/Blue", color.getBlue())));

        if (alpha) renderableSettings.add(new ColorSliderSetting("Alpha", parent.getName()+"/"+this.getName()+"/Alpha", color.getAlpha()));

        parent.addSettings(this);
    }

    private class ColorSliderSetting extends NumberSetting {
        private String imGuiID;

        public ColorSliderSetting(String name, String imGuiID, int value) {
            super(name, null, value, 0, 255, 1);
            this.imGuiID = imGuiID;
        }

        @Override
        public void render() {
            ImGui.pushID(imGuiID);

            ImGui.text(this.name);

            ImInt val = new ImInt((int) this.value);

            ImGui.pushItemWidth(160f);
            boolean changed = ImGui.sliderScalar("", ImGuiDataType.S32, val, (int) minimum, (int) maximum);
            ImGui.popItemWidth();

            if (changed) this.value = val.doubleValue();

            ImGui.popID();
        }
    }

    private class BooleanRainbowSetting extends BooleanSetting {
        private String imGuiID;

        public BooleanRainbowSetting(String name, String imGuiID, boolean enabled) {
            super(name, null, enabled);
            this.imGuiID = imGuiID;
        }

        @Override
        public void render() {
            ImGui.pushID(imGuiID);

            ImGui.text(this.name);
            if (ImGui.checkbox("", this.enabled)) {
                toggle();
            }

            ImGui.popID();
        }
    }

    public boolean isRainbow() {
        return rainbow;
    }

    public JColor getValue() {
        return color;
    }

    public JColor getColor() {
        if (rainbow) return getRainbow(0, this.getValue().getAlpha());
        return color;
    }

    public static JColor getRainbow(int incr, int alpha) {
        JColor color =  JColor.fromHSB(((System.currentTimeMillis() + incr * 200) % (360 * 20)) / (360f * 20),0.5f,1f);
        return new JColor(color.getRed(), color.getBlue(), color.getGreen(), alpha);
    }

    public void setColor(JColor color, boolean rainbow) {
        this.color = color;
        this.rainbow = rainbow;

        ((BooleanSetting) renderableSettings.get(0)).setEnabled(rainbow);
        ((ColorSliderSetting) renderableSettings.get(1)).setValue(color.getRed());
        ((ColorSliderSetting) renderableSettings.get(2)).setValue(color.getGreen());
        ((ColorSliderSetting) renderableSettings.get(3)).setValue(color.getBlue());
        if (alpha) ((ColorSliderSetting) renderableSettings.get(4)).setValue(color.getAlpha());
    }

    @Override
    public void render() {
        ImGui.pushID(parent.getName()+"/"+this.getName());

        float[] color = getColor().getFloatColorWAlpha();

        ImGui.text(this.getName());

        if (ImGui.colorButton(this.getName(), color)) showSliders = !showSliders;

        if (showSliders) {
            ImGui.indent(10f);

            for (RenderableSetting renderableSetting : renderableSettings) {
                renderableSetting.render();
            }

            ImGui.unindent(10f);

            if (alpha) {
                this.setColor(new JColor(
                        ((ColorSliderSetting) renderableSettings.get(1)).getIValue(),
                        ((ColorSliderSetting) renderableSettings.get(2)).getIValue(),
                        ((ColorSliderSetting) renderableSettings.get(3)).getIValue(),
                        ((ColorSliderSetting) renderableSettings.get(4)).getIValue()
                        ),
                        ((BooleanSetting) renderableSettings.get(0)).isEnabled());
            } else {
                this.setColor(new JColor(
                        ((ColorSliderSetting) renderableSettings.get(1)).getIValue(),
                        ((ColorSliderSetting) renderableSettings.get(2)).getIValue(),
                        ((ColorSliderSetting) renderableSettings.get(3)).getIValue()
                        ),
                        ((BooleanSetting) renderableSettings.get(0)).isEnabled());
            }

            ImGui.spacing();
        }

        ImGui.popID();
    }
}
