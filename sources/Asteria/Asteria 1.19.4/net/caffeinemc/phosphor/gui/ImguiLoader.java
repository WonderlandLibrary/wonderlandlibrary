package net.caffeinemc.phosphor.gui;

import imgui.*;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import lombok.Getter;
import net.caffeinemc.phosphor.common.Phosphor;
import net.caffeinemc.phosphor.module.modules.client.AsteriaSettingsModule;
import net.minecraft.client.MinecraftClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.glfwGetCurrentContext;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;

public class ImguiLoader {
    private static final Set<Renderable> renderstack = new HashSet<>();
    private static final Set<Renderable> toRemove = new HashSet<>();

    private static final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private static final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    @Getter
    private static ImFont customFont;
    @Getter
    private static ImFont bigCustomFont;
    @Getter
    private static ImFont biggerCustomFont;

    @Getter
    private static ImFont normalDosisFont;
    @Getter
    private static ImFont dosisFont;
    @Getter
    private static ImFont bigDosisFont;
    @Getter
    private static ImFont biggerDosisFont;
    @Getter
    private static ImFont fontAwesome;
    @Getter
    private static ImFont normalFontAwesome;
    @Getter
    private static ImFont bigFontAwesome;
    @Getter
    private static ImFont biggerFontAwesome;

    public static void onGlfwInit(long handle) {
        initializeImGui();
        imGuiGlfw.init(handle,true);
        imGuiGl3.init();
    }

    public static void onFrameRender() {
        imGuiGlfw.newFrame();
        ImGui.newFrame();

        if (Phosphor.INSTANCE != null && AsteriaMenu.isClientEnabled()) {
            AsteriaSettingsModule asteria = Phosphor.moduleManager().getModule(AsteriaSettingsModule.class);
            if (asteria != null) asteria.updateMode();
        }

        // User render code
        for (Renderable renderable : renderstack) {
            MinecraftClient.getInstance().getProfiler().push("ImGui Render " + renderable.getName());
            renderable.getTheme().preRender();
            renderable.render();
            renderable.getTheme().postRender();
            MinecraftClient.getInstance().getProfiler().pop();
        }
        // End of user code

        ImGui.render();
        endFrame();
    }

    private static void initializeImGui() {
        ImGui.createContext();

        final ImGuiIO io = ImGui.getIO();

        io.setIniFilename(null);                               // We don't want to save .ini file
        io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard); // Enable Keyboard Controls
        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);     // Enable Docking
        //io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);   // Enable Multi-Viewport / Platform Windows
        //io.setConfigViewportsNoTaskBarIcon(true);

        final ImFontGlyphRangesBuilder rangesBuilder = new ImFontGlyphRangesBuilder(); // Glyphs ranges provide

        final short iconRangeMin = (short) 0xe005;
        final short iconRangeMax = (short) 0xf8ff;
        final short[] iconRange = new short[]{iconRangeMin, iconRangeMax, 0};

        rangesBuilder.addRanges(iconRange);

        final short[] glyphRanges = rangesBuilder.buildRanges();

        ImFontConfig iconsConfig = new ImFontConfig();

        iconsConfig.setMergeMode(true);
        iconsConfig.setPixelSnapH(true);
        iconsConfig.setOversampleH(3);
        iconsConfig.setOversampleV(3);

        final ImFontAtlas fontAtlas = io.getFonts();
        final ImFontConfig fontConfig = new ImFontConfig(); // Natively allocated object, should be explicitly destroyed

        fontAtlas.addFontDefault();
        fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesCyrillic());
        byte[] fontAwesomeData = null;
        try (InputStream is = ImguiLoader.class.getClassLoader().getResourceAsStream("assets/FontAwesome6-Solid.otf")) {
            if (is != null) {
                fontAwesomeData = is.readAllBytes();
            }
        } catch (IOException ignored) {
            // do nothing, we already have font :3
        }

        try (InputStream is = ImguiLoader.class.getClassLoader().getResourceAsStream("assets/JetBrainsMono-Regular.ttf")) {
            if (is != null) {
                byte[] fontData = is.readAllBytes();

                customFont = fontAtlas.addFontFromMemoryTTF(fontData, 18);
                bigCustomFont = fontAtlas.addFontFromMemoryTTF(fontData, 24);
                biggerCustomFont = fontAtlas.addFontFromMemoryTTF(fontData, 32);
            }
        } catch (IOException ignored) {
            // do nothing, we already have font :3
        }

        byte[] dosisFontData = null;
        try (InputStream is = ImguiLoader.class.getClassLoader().getResourceAsStream("assets/Dosis-Medium.ttf")) {
            if (is != null) {
                dosisFontData = is.readAllBytes();

                normalDosisFont = fontAtlas.addFontFromMemoryTTF(dosisFontData, 20);
                bigDosisFont = fontAtlas.addFontFromMemoryTTF(dosisFontData, 24);
                biggerDosisFont = fontAtlas.addFontFromMemoryTTF(dosisFontData, 32);
                dosisFont = fontAtlas.addFontFromMemoryTTF(dosisFontData, 18);
            }
        } catch (IOException ignored) {
            // do nothing, we already have font :3
        }
        fontAwesome = fontAtlas.addFontFromMemoryTTF(fontAwesomeData, 20, iconsConfig, iconRange);


        fontConfig.setMergeMode(true); // When enabled, all fonts added with this config would be merged with the previously added font
        dosisFont = fontAtlas.addFontFromMemoryTTF(dosisFontData, 18);
        fontAwesome = fontAtlas.addFontFromMemoryTTF(fontAwesomeData, 18, iconsConfig, iconRange);
        bigDosisFont = fontAtlas.addFontFromMemoryTTF(dosisFontData, 24);
        bigFontAwesome = fontAtlas.addFontFromMemoryTTF(fontAwesomeData, 24, iconsConfig, iconRange);
        biggerDosisFont = fontAtlas.addFontFromMemoryTTF(dosisFontData, 32);
        biggerFontAwesome = fontAtlas.addFontFromMemoryTTF(fontAwesomeData, 32, iconsConfig, iconRange);
        normalDosisFont = fontAtlas.addFontFromMemoryTTF(dosisFontData, 20);
        normalFontAwesome = fontAtlas.addFontFromMemoryTTF(fontAwesomeData, 20, iconsConfig, iconRange);
        fontConfig.destroy();
        fontAtlas.build();


        if (io.hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final ImGuiStyle style = ImGui.getStyle();
            style.setWindowRounding(0.0f);
            style.setColor(ImGuiCol.WindowBg, ImGui.getColorU32(ImGuiCol.WindowBg, 1));
        }
    }

    private static void endFrame() {
        // After Dear ImGui prepared a draw data, we use it in the LWJGL3 renderer.
        // At that moment ImGui will be rendered to the current OpenGL context.
        imGuiGl3.renderDrawData(ImGui.getDrawData());

        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupWindowPtr = glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            glfwMakeContextCurrent(backupWindowPtr);
        }

        if (!toRemove.isEmpty()) {
            toRemove.forEach(renderstack::remove);
            toRemove.clear();
        }
    }

    public static void addRenderable(Renderable renderable) {
        renderstack.add(renderable);
    }

    public static void queueRemove(Renderable renderable) {
        toRemove.add(renderable);
    }

    public static boolean isRendered(Renderable renderable) {
        return renderstack.contains(renderable);
    }

    private ImguiLoader() {}
}