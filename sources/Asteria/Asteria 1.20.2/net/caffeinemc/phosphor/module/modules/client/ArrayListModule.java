package net.caffeinemc.phosphor.module.modules.client;

import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiWindowFlags;
import net.caffeinemc.phosphor.api.font.JColor;
import net.caffeinemc.phosphor.common.Phosphor;
import net.caffeinemc.phosphor.gui.ImguiLoader;
import net.caffeinemc.phosphor.gui.Renderable;
import net.caffeinemc.phosphor.gui.Theme;
import net.caffeinemc.phosphor.module.Module;
import net.caffeinemc.phosphor.module.setting.settings.BooleanSetting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ArrayListModule extends Module implements Renderable {
    public final BooleanSetting radiumText = new BooleanSetting("Asteria Text", this, false);

    public Comparator<Module> nameLengthComparator;
    public ArrayList<Module> modules;
    private boolean firstFrame;

    public ArrayListModule() {
        super("ArrayList", "Shows array list", Category.CLIENT);
        nameLengthComparator = new Comparator<Module>() {
            @Override
            public int compare(Module o1, Module o2) {
                return Integer.compare(o2.getName().length(), o1.getName().length());
            }
        };

        firstFrame = true;
    }

    @Override
    public void onEnable() {
        ImguiLoader.addRenderable(this);
        modules = Phosphor.moduleManager().getModules();
        Collections.sort(modules, nameLengthComparator);
    }

    @Override
    public void onDisable() {
        ImguiLoader.queueRemove(this);
    }

    @Override
    public void render() {
        int imGuiWindowFlags = 0;
        imGuiWindowFlags |= ImGuiWindowFlags.NoBackground;
        imGuiWindowFlags |= ImGuiWindowFlags.NoTitleBar;
        imGuiWindowFlags |= ImGuiWindowFlags.NoDocking;
        imGuiWindowFlags |= ImGuiWindowFlags.AlwaysAutoResize;
        imGuiWindowFlags |= ImGuiWindowFlags.NoFocusOnAppearing;
        if (!Phosphor.moduleManager().isModuleEnabled(Phosphor.name)) imGuiWindowFlags |= ImGuiWindowFlags.NoMove;
        ImGui.begin(this.getName(), imGuiWindowFlags);

        if (firstFrame) {
            ImGui.setWindowPos(0, 0);
            firstFrame = false;
        }

        if (radiumText.isEnabled()) {
            float[] color = JColor.getGuiColor().getFloatColor();

            ImGui.pushFont(ImguiLoader.getBiggerDosisFont());
            ImGui.pushStyleColor(ImGuiCol.Text, color[0], color[1], color[2], 1f);
            ImGui.text("Asteria");
            ImGui.popFont();
            ImGui.popStyleColor();
        }

        for (Module module : modules) {
            if (module.isEnabled())
                ImGui.text(module.getName());
        }

        ImGui.end();
    }

    @Override
    public String getName() {
        return "ArrayList";
    }

    @Override
    public Theme getTheme() {
        return new Theme() {
            @Override
            public void preRender() {
                float[][] colors = ImGui.getStyle().getColors();
                colors[ImGuiCol.Text] = new float[]{0.80f, 0.84f, 0.96f, 1.00f};
                colors[ImGuiCol.TextDisabled] = new float[]{0.58f, 0.60f, 0.70f, 1.00f};
                colors[ImGuiCol.WindowBg] = new float[]{0.09f, 0.09f, 0.15f, 1.00f};
                colors[ImGuiCol.ChildBg] = new float[]{0.00f, 0.00f, 0.00f, 0.00f};
                colors[ImGuiCol.PopupBg] = new float[]{0.08f, 0.08f, 0.08f, 0.94f};
                colors[ImGuiCol.Border] = new float[]{0.43f, 0.43f, 0.50f, 0.50f};
                colors[ImGuiCol.BorderShadow] = new float[]{0.00f, 0.00f, 0.00f, 0.00f};
                colors[ImGuiCol.FrameBg] = new float[]{0.90f, 0.27f, 0.33f, 0.59f};
                colors[ImGuiCol.FrameBgHovered] = new float[]{0.90f, 0.27f, 0.33f, 0.85f};
                colors[ImGuiCol.FrameBgActive] = new float[]{0.90f, 0.27f, 0.33f, 1.00f};
                colors[ImGuiCol.TitleBg] = new float[]{0.04f, 0.04f, 0.04f, 1.00f};
                colors[ImGuiCol.TitleBgActive] = new float[]{0.46f, 0.15f, 0.18f, 1.00f};
                colors[ImGuiCol.TitleBgCollapsed] = new float[]{0.00f, 0.00f, 0.00f, 0.51f};
                colors[ImGuiCol.MenuBarBg] = new float[]{0.14f, 0.14f, 0.14f, 1.00f};
                colors[ImGuiCol.ScrollbarBg] = new float[]{0.02f, 0.02f, 0.02f, 0.53f};
                colors[ImGuiCol.ScrollbarGrab] = new float[]{0.31f, 0.31f, 0.31f, 1.00f};
                colors[ImGuiCol.ScrollbarGrabHovered] = new float[]{0.41f, 0.41f, 0.41f, 1.00f};
                colors[ImGuiCol.ScrollbarGrabActive] = new float[]{0.51f, 0.51f, 0.51f, 1.00f};
                colors[ImGuiCol.CheckMark] = new float[]{0.90f, 0.27f, 0.33f, 1.00f};
                colors[ImGuiCol.SliderGrab] = new float[]{0.77f, 0.23f, 0.27f, 1.00f};
                colors[ImGuiCol.SliderGrabActive] = new float[]{0.90f, 0.27f, 0.33f, 1.00f};
                colors[ImGuiCol.Button] = new float[]{0.90f, 0.27f, 0.33f, 0.45f};
                colors[ImGuiCol.ButtonHovered] = new float[]{0.90f, 0.27f, 0.33f, 1.00f};
                colors[ImGuiCol.ButtonActive] = new float[]{0.75f, 0.21f, 0.25f, 1.00f};
                colors[ImGuiCol.Header] = new float[]{0.90f, 0.27f, 0.33f, 0.32f};
                colors[ImGuiCol.HeaderHovered] = new float[]{0.90f, 0.27f, 0.33f, 0.73f};
                colors[ImGuiCol.HeaderActive] = new float[]{0.90f, 0.27f, 0.33f, 1.00f};
                colors[ImGuiCol.Separator] = new float[]{0.42f, 0.44f, 0.52f, 1.00f};
                colors[ImGuiCol.SeparatorHovered] = new float[]{0.81f, 0.25f, 0.30f, 0.78f};
                colors[ImGuiCol.SeparatorActive] = new float[]{0.76f, 0.22f, 0.26f, 1.00f};
                colors[ImGuiCol.ResizeGrip] = new float[]{0.90f, 0.27f, 0.33f, 0.21f};
                colors[ImGuiCol.ResizeGripHovered] = new float[]{0.90f, 0.27f, 0.33f, 0.79f};
                colors[ImGuiCol.ResizeGripActive] = new float[]{0.90f, 0.27f, 0.33f, 1.00f};
                colors[ImGuiCol.Tab] = new float[]{0.56f, 0.17f, 0.21f, 0.85f};
                colors[ImGuiCol.TabHovered] = new float[]{0.90f, 0.27f, 0.33f, 0.85f};
                colors[ImGuiCol.TabActive] = new float[]{0.70f, 0.22f, 0.26f, 1.00f};
                colors[ImGuiCol.TabUnfocused] = new float[]{0.15f, 0.07f, 0.07f, 0.97f};
                colors[ImGuiCol.TabUnfocusedActive] = new float[]{0.42f, 0.14f, 0.14f, 1.00f};
                colors[ImGuiCol.DockingPreview] = new float[]{0.90f, 0.27f, 0.33f, 0.70f};
                colors[ImGuiCol.DockingEmptyBg] = new float[]{0.20f, 0.20f, 0.20f, 1.00f};
                colors[ImGuiCol.PlotLines] = new float[]{0.61f, 0.61f, 0.61f, 1.00f};
                colors[ImGuiCol.PlotLinesHovered] = new float[]{1.00f, 0.43f, 0.35f, 1.00f};
                colors[ImGuiCol.PlotHistogram] = new float[]{0.90f, 0.70f, 0.00f, 1.00f};
                colors[ImGuiCol.PlotHistogramHovered] = new float[]{1.00f, 0.60f, 0.00f, 1.00f};
                colors[ImGuiCol.TableHeaderBg] = new float[]{0.19f, 0.19f, 0.20f, 1.00f};
                colors[ImGuiCol.TableBorderStrong] = new float[]{0.31f, 0.31f, 0.35f, 1.00f};
                colors[ImGuiCol.TableBorderLight] = new float[]{0.23f, 0.23f, 0.25f, 1.00f};
                colors[ImGuiCol.TableRowBg] = new float[]{0.00f, 0.00f, 0.00f, 0.00f};
                colors[ImGuiCol.TableRowBgAlt] = new float[]{1.00f, 1.00f, 1.00f, 0.06f};
                colors[ImGuiCol.TextSelectedBg] = new float[]{0.90f, 0.27f, 0.33f, 0.35f};
                colors[ImGuiCol.DragDropTarget] = new float[]{1.00f, 1.00f, 0.00f, 0.90f};
                colors[ImGuiCol.NavHighlight] = new float[]{0.90f, 0.27f, 0.33f, 1.00f};
                colors[ImGuiCol.NavWindowingHighlight] = new float[]{1.00f, 1.00f, 1.00f, 0.70f};
                colors[ImGuiCol.NavWindowingDimBg] = new float[]{0.80f, 0.80f, 0.80f, 0.20f};
                colors[ImGuiCol.ModalWindowDimBg] = new float[]{0.80f, 0.80f, 0.80f, 0.35f};
                ImGui.getStyle().setColors(colors);

                ImGui.getStyle().setWindowRounding(8);
                ImGui.getStyle().setFrameRounding(4);
                ImGui.getStyle().setGrabRounding(4);
                ImGui.getStyle().setPopupRounding(4);
                ImGui.getStyle().setScrollbarRounding(4);
                ImGui.getStyle().setTabRounding(4);

                if (ImguiLoader.getBigCustomFont() != null) {
                    ImGui.pushFont(ImguiLoader.getBigCustomFont());
                }
            }

            @Override
            public void postRender() {
                if (ImguiLoader.getBigCustomFont() != null) {
                    ImGui.popFont();
                }
            }
        };
    }
}
