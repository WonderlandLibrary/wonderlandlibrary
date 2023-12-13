// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.ui.click.skeet;

import vip.Resolute.util.font.FontUtil;
import java.io.IOException;
import vip.Resolute.ui.click.skeet.component.impl.sub.player.PlayerComponent;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.List;
import java.util.Comparator;
import vip.Resolute.ui.click.skeet.component.impl.sub.color.ColorPickerTextComponent;
import vip.Resolute.ui.click.skeet.component.impl.sub.slider.SliderTextComponent;
import vip.Resolute.settings.impl.NumberSetting;
import vip.Resolute.ui.click.skeet.component.impl.sub.comboBox.ComboBoxTextComponent;
import java.util.Collections;
import vip.Resolute.settings.impl.ModeSetting;
import vip.Resolute.settings.Setting;
import vip.Resolute.ui.click.skeet.component.impl.sub.key.KeyBindComponent;
import vip.Resolute.ui.click.skeet.component.impl.sub.checkbox.CheckBoxTextComponent;
import vip.Resolute.Resolute;
import net.minecraft.util.StringUtils;
import vip.Resolute.modules.Module;
import vip.Resolute.ui.click.skeet.component.impl.GroupBoxComponent;
import java.util.Iterator;
import vip.Resolute.modules.impl.render.ClickGUI;
import net.minecraft.client.Minecraft;
import vip.Resolute.util.render.RenderUtils;
import org.lwjgl.opengl.GL11;
import java.awt.Color;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import vip.Resolute.ui.click.skeet.component.TabComponent;
import vip.Resolute.ui.click.skeet.framework.Component;
import vip.Resolute.settings.impl.BooleanSetting;
import vip.Resolute.settings.impl.ColorSetting;
import net.minecraft.util.ResourceLocation;
import vip.Resolute.util.font.MinecraftFontRenderer;
import net.minecraft.client.gui.GuiScreen;

public final class SkeetUI extends GuiScreen
{
    public static final int GROUP_BOX_MARGIN = 8;
    public static final MinecraftFontRenderer ICONS_RENDERER;
    public static final MinecraftFontRenderer GROUP_BOX_HEADER_RENDERER;
    public static final MinecraftFontRenderer FONT_RENDERER;
    public static final MinecraftFontRenderer KEYBIND_FONT_RENDERER;
    private static final SkeetUI INSTANCE;
    private static final ResourceLocation BACKGROUND_IMAGE;
    private static final char[] ICONS;
    private static final float USABLE_AREA_HEIGHT = 341.5f;
    private static final int TAB_SELECTOR_HEIGHT;
    private static final ColorSetting GUI_COLOR;
    private static final BooleanSetting PLAYERS;
    private static final BooleanSetting ANIMALS;
    private static final BooleanSetting MOBS;
    private static final BooleanSetting INVISIBLES;
    private static final BooleanSetting VILLAGERS;
    private static final BooleanSetting TEAMS;
    float hue;
    private static double alpha;
    private static boolean open;
    private final Component rootComponent;
    private final Component tabSelectorComponent;
    private double targetAlpha;
    private boolean closed;
    private boolean dragging;
    private float prevX;
    private float prevY;
    private int selectorIndex;
    private TabComponent selectedTab;
    
    private SkeetUI() {
        this.rootComponent = new Component((Component)null, 230.0f, 30.0f, 430.0f, 400.0f) {
            @Override
            public boolean isHovered(final int mouseX, final int mouseY) {
                final float x;
                final float y;
                return mouseX >= (x = this.getX() + 3.0f) && mouseY >= (y = this.getY() + 3.0f) && mouseX <= x + this.getWidth() - 6.0f && mouseY <= y + this.getHeight() - 6.0f;
            }
            
            @Override
            public void drawComponent(final ScaledResolution lockedResolution, final int mouseX, final int mouseY) {
                if (SkeetUI.this.dragging) {
                    this.setX(Math.max(0.0f, Math.min(lockedResolution.getScaledWidth() - this.getWidth(), mouseX - SkeetUI.this.prevX)));
                    this.setY(Math.max(0.0f, Math.min(lockedResolution.getScaledHeight() - this.getHeight(), mouseY - SkeetUI.this.prevY)));
                }
                final float borderX = this.getX();
                final float borderY = this.getY();
                final float width = this.getWidth();
                final float height = this.getHeight();
                Gui.drawRect(borderX, borderY, borderX + width, borderY + height, SkeetUI.getColor(1052942));
                Gui.drawRect(borderX + 0.5f, borderY + 0.5f, borderX + width - 0.5f, borderY + height - 0.5f, SkeetUI.getColor(3619386));
                Gui.drawRect(borderX + 1.0f, borderY + 1.0f, borderX + width - 1.0f, borderY + height - 1.0f, SkeetUI.getColor(2302755));
                Gui.drawRect(borderX + 3.0f, borderY + 3.0f, borderX + width - 3.0f, borderY + height - 3.0f, SkeetUI.getColor(3092271));
                float left = borderX + 3.5f;
                float top = borderY + 3.5f;
                float right = borderX + width - 3.5f;
                final float bottom = borderY + height - 3.5f;
                float h = SkeetUI.this.hue;
                float h2 = SkeetUI.this.hue + 85.0f;
                float h3 = SkeetUI.this.hue + 170.0f;
                if (h > 255.0f) {
                    h = 0.0f;
                }
                if (h2 > 255.0f) {
                    h2 -= 255.0f;
                }
                if (h3 > 255.0f) {
                    h3 -= 255.0f;
                }
                final Color no = Color.getHSBColor(h / 255.0f, 0.55f, 1.0f);
                final Color yes = Color.getHSBColor(h2 / 255.0f, 0.55f, 1.0f);
                final Color bruh = Color.getHSBColor(h3 / 255.0f, 0.55f, 1.0f);
                final SkeetUI this$0 = SkeetUI.this;
                this$0.hue += 0.5f;
                if (SkeetUI.alpha > 20.0) {
                    GL11.glEnable(3089);
                    RenderUtils.startScissorBox(lockedResolution, (int)left, (int)top, (int)(right - left), (int)(bottom - top));
                    Minecraft.getMinecraft().getTextureManager().bindTexture(SkeetUI.BACKGROUND_IMAGE);
                    RenderUtils.drawImage(left, top, 325.0f, 275.0f, -1);
                    RenderUtils.drawImage(left + 325.0f, top + 1.0f, 325.0f, 275.0f, -1);
                    RenderUtils.drawImage(left + 1.0f, top + 275.0f, 325.0f, 275.0f, -1);
                    RenderUtils.drawImage(left + 326.0f, top + 276.0f, 325.0f, 275.0f, -1);
                    GL11.glDisable(3089);
                }
                final float xDif = (right - left) / 2.0f;
                top += 0.5f;
                left += 0.5f;
                right -= 0.5f;
                RenderUtils.drawGradientRect(left, top, left + xDif, top + 1.5f - 0.5f, true, SkeetUI.getColor(RenderUtils.darker(3957866, 1.5f)), SkeetUI.getColor(RenderUtils.darker(7352943, 1.5f)));
                RenderUtils.drawGradientRect(left + xDif, top, right, top + 1.5f - 0.5f, true, SkeetUI.getColor(RenderUtils.darker(7352943, 1.5f)), SkeetUI.getColor(RenderUtils.darker(8094516, 1.5f)));
                if (SkeetUI.alpha >= 112.0) {
                    Gui.drawRect(left, top + 1.5f - 1.0f, right, top + 1.5f - 0.5f, 1879048192);
                }
                if (ClickGUI.rainbow.isEnabled()) {
                    RenderUtils.drawGradientSideways(left, top, right / 2.0f, bottom, no.getRGB(), yes.getRGB());
                    RenderUtils.drawGradientSideways(left, top, right, bottom, yes.getRGB(), bruh.getRGB());
                    Gui.drawRect(left, top, right, bottom, new Color(21, 21, 21, 205).getRGB());
                }
                for (final Component child : this.children) {
                    if (!(child instanceof TabComponent) || SkeetUI.this.selectedTab == child) {
                        child.drawComponent(lockedResolution, mouseX, mouseY);
                    }
                }
            }
            
            @Override
            public void onKeyPress(final int keyCode) {
                for (final Component child : this.children) {
                    if (!(child instanceof TabComponent) || SkeetUI.this.selectedTab == child) {
                        child.onKeyPress(keyCode);
                    }
                }
            }
            
            @Override
            public void onMouseClick(final int mouseX, final int mouseY, final int button) {
                for (final Component tabOrSideBar : this.children) {
                    if (tabOrSideBar instanceof TabComponent) {
                        if (SkeetUI.this.selectedTab != tabOrSideBar) {
                            continue;
                        }
                        if (tabOrSideBar.isHovered(mouseX, mouseY)) {
                            tabOrSideBar.onMouseClick(mouseX, mouseY, button);
                            break;
                        }
                    }
                    tabOrSideBar.onMouseClick(mouseX, mouseY, button);
                }
                if (button == 0 && this.isHovered(mouseX, mouseY)) {
                    for (final Component tabOrSideBar : this.getChildren()) {
                        if (tabOrSideBar instanceof TabComponent) {
                            if (SkeetUI.this.selectedTab != tabOrSideBar) {
                                continue;
                            }
                            for (final Component groupBox : tabOrSideBar.getChildren()) {
                                if (groupBox instanceof GroupBoxComponent) {
                                    final GroupBoxComponent groupBoxComponent = (GroupBoxComponent)groupBox;
                                    if (groupBoxComponent.isHoveredEntire(mouseX, mouseY)) {
                                        return;
                                    }
                                    continue;
                                }
                            }
                        }
                        else {
                            if (tabOrSideBar.isHovered(mouseX, mouseY)) {
                                return;
                            }
                            continue;
                        }
                    }
                    SkeetUI.this.prevX = mouseX - this.getX();
                    SkeetUI.this.prevY = mouseY - this.getY();
                }
            }
            
            @Override
            public void onMouseRelease(final int button) {
                super.onMouseRelease(button);
                SkeetUI.this.dragging = false;
            }
        };
        for (final Module.Category category : Module.Category.values()) {
            final TabComponent categoryTab = new TabComponent(this.rootComponent, StringUtils.upperSnakeCaseToPascal(category.name()), 51.5f, 5.0f, 415.0f, 397.5f) {
                @Override
                public void setupChildren() {
                    final List<Module> modulesInCategory = Resolute.getModulesByCategory(category);
                    for (final Module module : modulesInCategory) {
                        final GroupBoxComponent groupBoxComponent = new GroupBoxComponent(this, module.getName(), 0.0f, 0.0f, 94.333336f, 6.0f);
                        final CheckBoxTextComponent enabledButton = new CheckBoxTextComponent(groupBoxComponent, "Enabled", module::isEnabled, module::setState);
                        enabledButton.addChild(new KeyBindComponent(enabledButton, module::getKey, module::setKey, 42.0f, 1.0f));
                        groupBoxComponent.addChild(enabledButton);
                        this.addChild(groupBoxComponent);
                        for (final Setting property : module.getSettings()) {
                            Component component = null;
                            if (property instanceof BooleanSetting) {
                                final BooleanSetting bool = (BooleanSetting)property;
                                component = new CheckBoxTextComponent(groupBoxComponent, bool.name, bool::isEnabled, bool::setEnabled, bool::isAvailable);
                            }
                            if (property instanceof ModeSetting) {
                                final ModeSetting mode = (ModeSetting)property;
                                component = new ComboBoxTextComponent(groupBoxComponent, mode.name, mode::getModes, mode::setSelected, Collections.singletonList(mode.getMode()), mode::isAvailable);
                            }
                            if (property instanceof NumberSetting) {
                                final NumberSetting number = (NumberSetting)property;
                                component = new SliderTextComponent(groupBoxComponent, number.name, number::getValue, number::setValue, number::getMinimum, number::getMaximum, number::getIncrement, number::isAvailable);
                            }
                            if (property instanceof ColorSetting) {
                                final ColorSetting color = (ColorSetting)property;
                                component = new ColorPickerTextComponent(groupBoxComponent, color.name, color::getColor, color::setColor, color::isAvailable);
                            }
                            if (component != null) {
                                groupBoxComponent.addChild(component);
                            }
                        }
                    }
                    this.getChildren().sort(Comparator.comparingDouble(Component::getHeight).reversed());
                }
            };
            this.rootComponent.addChild(categoryTab);
        }
        final TabComponent colorTab = new TabComponent(this.rootComponent, "Settings", 51.5f, 5.0f, 415.0f, 341.5f) {
            @Override
            public void setupChildren() {
                final GroupBoxComponent guiSettingsGroupBox = new GroupBoxComponent(this, "GUI Settings", 8.0f, 8.0f, 94.333336f, 100.0f);
                final Supplier var10005 = SkeetUI::getColor;
                final Consumer<Color> var10006 = SkeetUI::setColorValue;
                final Setting var10007 = SkeetUI.GUI_COLOR;
                var10007.getClass();
                guiSettingsGroupBox.addChild(new ColorPickerTextComponent(guiSettingsGroupBox, "Gui Color", var10005, var10006));
                this.addChild(guiSettingsGroupBox);
            }
        };
        this.rootComponent.addChild(colorTab);
        final TabComponent targetTab = new TabComponent(this.rootComponent, "Targets", 51.5f, 5.0f, 415.0f, 341.5f) {
            @Override
            public void setupChildren() {
                final GroupBoxComponent guiSettingsGroupBox = new GroupBoxComponent(this, "Targets", 8.0f, 8.0f, 94.333336f, 100.0f);
                final BooleanSetting var10007 = SkeetUI.PLAYERS;
                final BooleanSetting var10008 = SkeetUI.ANIMALS;
                final BooleanSetting var10009 = SkeetUI.MOBS;
                final BooleanSetting var10010 = SkeetUI.INVISIBLES;
                final BooleanSetting var10011 = SkeetUI.VILLAGERS;
                final BooleanSetting var10012 = SkeetUI.TEAMS;
                var10007.getClass();
                guiSettingsGroupBox.addChild(new CheckBoxTextComponent(guiSettingsGroupBox, var10007.name, var10007::isEnabled, var10007::setEnabled, var10007::isAvailable));
                guiSettingsGroupBox.addChild(new CheckBoxTextComponent(guiSettingsGroupBox, var10008.name, var10008::isEnabled, var10008::setEnabled, var10008::isAvailable));
                guiSettingsGroupBox.addChild(new CheckBoxTextComponent(guiSettingsGroupBox, var10009.name, var10009::isEnabled, var10009::setEnabled, var10009::isAvailable));
                guiSettingsGroupBox.addChild(new CheckBoxTextComponent(guiSettingsGroupBox, var10010.name, var10010::isEnabled, var10010::setEnabled, var10010::isAvailable));
                guiSettingsGroupBox.addChild(new CheckBoxTextComponent(guiSettingsGroupBox, var10011.name, var10011::isEnabled, var10011::setEnabled, var10011::isAvailable));
                guiSettingsGroupBox.addChild(new CheckBoxTextComponent(guiSettingsGroupBox, var10012.name, var10012::isEnabled, var10012::setEnabled, var10012::isAvailable));
                this.addChild(guiSettingsGroupBox);
            }
        };
        this.rootComponent.addChild(targetTab);
        final TabComponent configTab = new TabComponent(this.rootComponent, "Player", 51.5f, 5.0f, 415.0f, 341.5f) {
            @Override
            public void setupChildren() {
                final GroupBoxComponent configsGroupBox = new GroupBoxComponent(this, "Player", 8.0f, 8.0f, 94.333336f, 140.0f);
                configsGroupBox.addChild(new PlayerComponent(configsGroupBox, 8.0f, 8.0f));
                this.addChild(configsGroupBox);
            }
        };
        this.rootComponent.addChild(configTab);
        this.selectedTab = this.rootComponent.getChildren().get(this.selectorIndex);
        this.tabSelectorComponent = new Component(this.rootComponent, 3.5f, 5.0f, 48.0f, 391.5f) {
            private double selectorY;
            
            @Override
            public void onMouseClick(final int mouseX, final int mouseY, final int button) {
                if (this.isHovered(mouseX, mouseY)) {
                    final float mouseYOffset = mouseY - SkeetUI.this.tabSelectorComponent.getY() - 10.0f;
                    if (mouseYOffset > 0.0f && mouseYOffset < SkeetUI.this.tabSelectorComponent.getHeight() - 10.0f) {
                        SkeetUI.this.selectorIndex = Math.min(SkeetUI.ICONS.length - 1, (int)(mouseYOffset / SkeetUI.TAB_SELECTOR_HEIGHT));
                        SkeetUI.this.selectedTab = SkeetUI.this.rootComponent.getChildren().get(SkeetUI.this.selectorIndex);
                    }
                }
            }
            
            @Override
            public void drawComponent(final ScaledResolution resolution, final int mouseX, final int mouseY) {
                this.selectorY = RenderUtils.progressiveAnimation(this.selectorY, SkeetUI.this.selectorIndex * SkeetUI.TAB_SELECTOR_HEIGHT + 10, 1.0);
                final float x = this.getX();
                final float y = this.getY();
                final float width = this.getWidth();
                final float height = this.getHeight();
                final int innerColor = SkeetUI.getColor(394758);
                final int outerColor = SkeetUI.getColor(2105376);
                Gui.drawRect(x, y, x + width, y + this.selectorY, SkeetUI.getColor(789516));
                Gui.drawRect(x + width - 1.0f, y, x + width, y + this.selectorY, innerColor);
                Gui.drawRect(x + width - 0.5f, y, x + width, y + this.selectorY, outerColor);
                Gui.drawRect(x, y + this.selectorY - 1.0, x + width - 0.5f, y + this.selectorY, innerColor);
                Gui.drawRect(x, y + this.selectorY - 0.5, x + width, y + this.selectorY, outerColor);
                Gui.drawRect(x, y + this.selectorY + SkeetUI.TAB_SELECTOR_HEIGHT, x + width, y + height, SkeetUI.getColor(789516));
                Gui.drawRect(x + width - 1.0f, y + this.selectorY + SkeetUI.TAB_SELECTOR_HEIGHT, x + width, y + height, innerColor);
                Gui.drawRect(x + width - 0.5f, y + this.selectorY + SkeetUI.TAB_SELECTOR_HEIGHT, x + width, y + height, outerColor);
                Gui.drawRect(x, y + this.selectorY + SkeetUI.TAB_SELECTOR_HEIGHT, x + width - 0.5f, y + this.selectorY + SkeetUI.TAB_SELECTOR_HEIGHT + 1.0, innerColor);
                Gui.drawRect(x, y + this.selectorY + SkeetUI.TAB_SELECTOR_HEIGHT, x + width, y + this.selectorY + SkeetUI.TAB_SELECTOR_HEIGHT + 0.5, outerColor);
                if (SkeetUI.shouldRenderText()) {
                    for (int i = 0; i < SkeetUI.ICONS.length; ++i) {
                        final String c = String.valueOf(SkeetUI.ICONS[i]);
                        Gui.drawRect(0.0, 0.0, 0.0, 0.0, -1);
                        SkeetUI.ICONS_RENDERER.drawString(c, x + 24.0f - SkeetUI.ICONS_RENDERER.getStringWidth(c) / 2.0 - 1.0, y + 10.0f + i * SkeetUI.TAB_SELECTOR_HEIGHT + SkeetUI.TAB_SELECTOR_HEIGHT / 2.0f - SkeetUI.ICONS_RENDERER.getHeight() / 2.0f, SkeetUI.getColor((i == SkeetUI.this.selectorIndex) ? 16777215 : 8421504));
                    }
                }
            }
        };
        this.rootComponent.addChild(this.tabSelectorComponent);
    }
    
    public static double getAlpha() {
        return SkeetUI.alpha;
    }
    
    public static boolean shouldRenderText() {
        return SkeetUI.alpha > 20.0;
    }
    
    private static boolean isVisible() {
        return SkeetUI.open || SkeetUI.alpha > 0.0;
    }
    
    public static int getColor() {
        return getColor(SkeetUI.GUI_COLOR.getColor());
    }
    
    public static boolean isPlayers() {
        return SkeetUI.PLAYERS.isEnabled();
    }
    
    public static boolean isAnimals() {
        return SkeetUI.ANIMALS.isEnabled();
    }
    
    public static boolean isMobs() {
        return SkeetUI.MOBS.isEnabled();
    }
    
    public static boolean isInvisibles() {
        return SkeetUI.INVISIBLES.isEnabled();
    }
    
    public static boolean isVillagers() {
        return SkeetUI.VILLAGERS.isEnabled();
    }
    
    public static boolean isTeams() {
        return SkeetUI.TEAMS.isEnabled();
    }
    
    public static void setColorValue(final Color color) {
        SkeetUI.GUI_COLOR.setColor(color);
    }
    
    public static int getColor(final int color) {
        final int r = color >> 16 & 0xFF;
        final int g = color >> 8 & 0xFF;
        final int b = color & 0xFF;
        final int a = (int)SkeetUI.alpha;
        return (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF) | (a & 0xFF) << 24;
    }
    
    public static void init() {
        SkeetUI.INSTANCE.open();
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1) {
            this.close();
        }
        else {
            this.rootComponent.onKeyPress(keyCode);
        }
    }
    
    private void close() {
        if (SkeetUI.open) {
            this.targetAlpha = 0.0;
            SkeetUI.open = false;
            this.dragging = false;
        }
    }
    
    private void open() {
        Minecraft.getMinecraft().displayGuiScreen(this);
        SkeetUI.alpha = 0.0;
        this.targetAlpha = 255.0;
        SkeetUI.open = true;
        this.closed = false;
    }
    
    private boolean finishedClosing() {
        return !SkeetUI.open && SkeetUI.alpha == 0.0 && !this.closed;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if (this.finishedClosing()) {
            Minecraft.getMinecraft().displayGuiScreen(null);
        }
        else if (isVisible()) {
            SkeetUI.alpha = RenderUtils.linearAnimation(SkeetUI.alpha, this.targetAlpha, 10.0);
            this.rootComponent.drawComponent(new ScaledResolution(Minecraft.getMinecraft()), mouseX, mouseY);
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        if (isVisible()) {
            this.rootComponent.onMouseClick(mouseX, mouseY, mouseButton);
        }
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        if (isVisible()) {
            this.rootComponent.onMouseRelease(state);
        }
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    static {
        GROUP_BOX_HEADER_RENDERER = FontUtil.tahomaSmall;
        BACKGROUND_IMAGE = new ResourceLocation("resolute/skeetchainmail.png");
        ICONS = new char[] { 'E', 'G', 'F', 'D', 'A', 'H', 'J', 'I' };
        TAB_SELECTOR_HEIGHT = 381 / (SkeetUI.ICONS.length + 0);
        GUI_COLOR = new ColorSetting("Gui Color", new Color(255, 255, 255));
        PLAYERS = new BooleanSetting("Players", true);
        ANIMALS = new BooleanSetting("Animals", false);
        MOBS = new BooleanSetting("Mobs", false);
        INVISIBLES = new BooleanSetting("Invisibles", false);
        VILLAGERS = new BooleanSetting("Villagers", false);
        TEAMS = new BooleanSetting("Teams", false);
        ICONS_RENDERER = FontUtil.icons;
        FONT_RENDERER = FontUtil.tahomaSmall;
        KEYBIND_FONT_RENDERER = FontUtil.tahomaSmall;
        INSTANCE = new SkeetUI();
    }
}
