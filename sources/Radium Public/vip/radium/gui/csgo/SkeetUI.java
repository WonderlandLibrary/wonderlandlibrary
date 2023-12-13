package vip.radium.gui.csgo;

import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import vip.radium.RadiumClient;
import vip.radium.event.impl.KeyPressEvent;
import vip.radium.gui.csgo.component.Component;
import vip.radium.gui.csgo.component.TabComponent;
import vip.radium.gui.csgo.component.impl.GroupBoxComponent;
import vip.radium.gui.csgo.component.impl.sub.button.ButtonComponentImpl;
import vip.radium.gui.csgo.component.impl.sub.checkBox.CheckBoxTextComponent;
import vip.radium.gui.csgo.component.impl.sub.color.ColorPickerTextComponent;
import vip.radium.gui.csgo.component.impl.sub.comboBox.ComboBoxTextComponent;
import vip.radium.gui.csgo.component.impl.sub.key.KeyBindComponent;
import vip.radium.gui.csgo.component.impl.sub.slider.SliderTextComponent;
import vip.radium.gui.font.FontManager;
import vip.radium.gui.font.TrueTypeFontRenderer;
import vip.radium.module.Module;
import vip.radium.module.ModuleCategory;
import vip.radium.property.Property;
import vip.radium.property.impl.DoubleProperty;
import vip.radium.property.impl.EnumProperty;
import vip.radium.property.impl.MultiSelectEnumProperty;
import vip.radium.utils.StringUtils;
import vip.radium.utils.Wrapper;
import vip.radium.utils.render.*;

import java.awt.*;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public final class SkeetUI extends GuiScreen {

    public static final int GROUP_BOX_MARGIN = 8;
    public static final TrueTypeFontRenderer ICONS_RENDERER;
    public static final TrueTypeFontRenderer GROUP_BOX_HEADER_RENDERER = FontManager.CSGO_FR;
    public static final TrueTypeFontRenderer FONT_RENDERER;
    public static final TrueTypeFontRenderer KEYBIND_FONT_RENDERER;
    public static final int GROUP_BOX_LEFT_MARGIN = 3;
    public static final int ENABLE_BUTTON_Y_OFFSET = 6;
    public static final int ENABLE_BUTTON_Y_GAP = 4;
    private static final int WIDTH = 370;
    private static final int HEIGHT = 350;
    private static final float TOTAL_BORDER_WIDTH = 3.5F;
    private static final float RAINBOW_BAR_WIDTH = 1.5F;
    private static final int TAB_SELECTOR_WIDTH = 48;
    public static final float USABLE_AREA_WIDTH = WIDTH - TAB_SELECTOR_WIDTH - TOTAL_BORDER_WIDTH * 2;
    public static final float GROUP_BOX_WIDTH = (USABLE_AREA_WIDTH - GROUP_BOX_MARGIN * 4) / 3;
    public static final float HALF_GROUP_BOX = (GROUP_BOX_WIDTH - 2.0F) / 2 - GROUP_BOX_LEFT_MARGIN * 2;
    private static final SkeetUI INSTANCE;
    private static final ResourceLocation BACKGROUND_IMAGE = new ResourceLocation("radium/gui/skeetchainmail.png");
    private static final char[] ICONS = {'E', 'G', 'F', 'I', 'D', 'J', 'H'};
    private static final float USABLE_AREA_HEIGHT = HEIGHT - TOTAL_BORDER_WIDTH * 2 - RAINBOW_BAR_WIDTH;
    private static final int TAB_SELECTOR_HEIGHT = (int) (USABLE_AREA_HEIGHT - 20) / ICONS.length;
    public static final Property<Integer> colorProperty = new Property<>("GUI Color", Colors.PURPLE);
    private static double alpha;
    private static boolean open;

    static {
        ICONS_RENDERER = new TrueTypeFontRenderer(
                TTFUtils.getFontFromLocation("icons.ttf", 40), true, true);

        ICONS_RENDERER.generateTextures();

        FONT_RENDERER = new TrueTypeFontRenderer(new Font(
                "Tahoma", Font.PLAIN, 11), false, true);

        FONT_RENDERER.generateTextures();

        KEYBIND_FONT_RENDERER = new TrueTypeFontRenderer(new Font(
                "Tahoma", Font.PLAIN, 9), false, false);

        KEYBIND_FONT_RENDERER.generateTextures();

        INSTANCE = new SkeetUI();
    }

    private final Component rootComponent;
    private final Component tabSelectorComponent;
    private double targetAlpha;
    private boolean closed;
    @EventLink
    private final Listener<KeyPressEvent> onKeyPressEvent = event -> {
        if (event.getKey() == Keyboard.KEY_INSERT || event.getKey() == Keyboard.KEY_RSHIFT) {
            this.open();
        }
    };
    private boolean dragging;
    private float prevX;
    private float prevY;
    private int selectorIndex;
    private TabComponent selectedTab;

    private SkeetUI() {
        rootComponent = new Component(null, 0, 0, WIDTH, HEIGHT) {
            @Override
            public void drawComponent(LockedResolution lockedResolution, int mouseX, int mouseY) {
                if (dragging) {
                    setX(Math.max(0, Math.min(lockedResolution.getWidth() - getWidth(), mouseX - prevX)));
                    setY(Math.max(0, Math.min(lockedResolution.getHeight() - getHeight(), mouseY - prevY)));
                }
                final float borderX = getX();
                final float borderY = getY();
                final float width = getWidth();
                final float height = getHeight();
                // Border
                Gui.drawRect(borderX, borderY, borderX + width, borderY + height, getColor(0x10110E));
                Gui.drawRect(borderX + 0.5F, borderY + 0.5F, borderX + width - 0.5F, borderY + height - 0.5F, getColor(0x373A3A));
                Gui.drawRect(borderX + 1.0F, borderY + 1.0F, borderX + width - 1.0F, borderY + height - 1.0F, getColor(0x232323));
                Gui.drawRect(borderX + 3.0F, borderY + 3.0F, borderX + width - 3.0F, borderY + height - 3.0F, getColor(0x2F2F2F));
                float left = borderX + TOTAL_BORDER_WIDTH;
                float top = borderY + TOTAL_BORDER_WIDTH;
                float right = borderX + width - TOTAL_BORDER_WIDTH;
                float bottom = borderY + height - TOTAL_BORDER_WIDTH;
                // Background
                Gui.drawRect(left, top, right, bottom, getColor(0x151515));
                if (alpha > 20) {
                    GL11.glEnable(GL11.GL_SCISSOR_TEST);
                    OGLUtils.startScissorBox(lockedResolution, (int) left, (int) top, (int) (right - left), (int) (bottom - top));
                    // Retard code
                    RenderingUtils.drawImage(left, top, 325, 275, 1.0F, 1.0F, 1.0F, BACKGROUND_IMAGE);
                    RenderingUtils.drawImage(left + 325, top + 1, 325, 275, 1.0F, 1.0F, 1.0F, BACKGROUND_IMAGE);
                    RenderingUtils.drawImage(left + 1, top + 275, 325, 275, 1.0F, 1.0F, 1.0F, BACKGROUND_IMAGE);
                    RenderingUtils.drawImage(left + 326, top + 276, 325, 275, 1.0F, 1.0F, 1.0F, BACKGROUND_IMAGE);
                    GL11.glDisable(GL11.GL_SCISSOR_TEST);
                }
                //Rainbow bar
                final float xDif = (right - left) / 2;
                top += 0.5F;
                left += 0.5F;
                right -= 0.5F;
                RenderingUtils.drawGradientRect(left, top, left + xDif, top + RAINBOW_BAR_WIDTH - 0.5F, true,
                        getColor(RenderingUtils.darker(0x3C646A, 1.5F)),
                        getColor(RenderingUtils.darker(0x70326F, 1.5F)));
                RenderingUtils.drawGradientRect(left + xDif, top, right, top + RAINBOW_BAR_WIDTH - 0.5F, true,
                        getColor(RenderingUtils.darker(0x70326F, 1.5F)),
                        getColor(RenderingUtils.darker(0x7B8334, 1.5F)));

                if (alpha >= 0x70)
                    Gui.drawRect(left, top + RAINBOW_BAR_WIDTH - 1.0F, right,
                            top + RAINBOW_BAR_WIDTH - 0.5F, 0x70000000);


                for (Component child : children) {
                    if (child instanceof TabComponent && selectedTab != child)
                        continue;
                    child.drawComponent(lockedResolution, mouseX, mouseY);
                }
            }

            @Override
            public void onKeyPress(int keyCode) {
                for (Component child : children) {
                    if (child instanceof TabComponent && selectedTab != child)
                        continue;
                    child.onKeyPress(keyCode);
                }
            }

            @Override
            public void onMouseClick(int mouseX, int mouseY, int button) {
                for (Component child : children) {
                    if (child instanceof TabComponent) {
                        if (selectedTab != child) {
                            continue;
                        }

                        if (child.isHovered(mouseX, mouseY)) {
                            child.onMouseClick(mouseX, mouseY, button);
                            break;
                        }
                    }

                    child.onMouseClick(mouseX, mouseY, button);
                }

                // Dragging
                if (button == 0 && isHovered(mouseX, mouseY)) {
                    for (Component tabOrSideBar : getChildren()) {
                        if (tabOrSideBar instanceof TabComponent) {
                            if (selectedTab != tabOrSideBar)
                                continue;
                            for (Component groupBox : tabOrSideBar.getChildren()) {
                                if (groupBox instanceof GroupBoxComponent) {
                                    GroupBoxComponent groupBoxComponent = (GroupBoxComponent) groupBox;

                                    if (groupBoxComponent.isHoveredEntire(mouseX, mouseY)) {
                                        return;
                                    }
                                }
                            }
                        } else if (tabOrSideBar.isHovered(mouseX, mouseY))
                            return;
                    }

                    dragging = true;
                    prevX = mouseX - getX();
                    prevY = mouseY - getY();
                }
            }

            @Override
            public void onMouseRelease(int button) {
                super.onMouseRelease(button);

                dragging = false;
            }
        };

        for (ModuleCategory category : ModuleCategory.values()) {
            TabComponent categoryTab = new TabComponent(rootComponent,
                    StringUtils.upperSnakeCaseToPascal(category.name()),
                    TOTAL_BORDER_WIDTH + TAB_SELECTOR_WIDTH,
                    TOTAL_BORDER_WIDTH + RAINBOW_BAR_WIDTH,
                    USABLE_AREA_WIDTH,
                    USABLE_AREA_HEIGHT) {
                @Override
                public void setupChildren() {
                    final List<Module> modulesInCategory = RadiumClient.getInstance().getModuleManager().getModulesForCategory(category);
                    for (final Module module : modulesInCategory) {
                        GroupBoxComponent groupBoxComponent = new GroupBoxComponent(this, module.getLabel(),
                                0,
                                0,
                                GROUP_BOX_WIDTH,
                                ENABLE_BUTTON_Y_OFFSET);
                        CheckBoxTextComponent enabledButton = new CheckBoxTextComponent(groupBoxComponent, "Enabled",
                                module::isEnabled,
                                module::setEnabled);
                        enabledButton.addChild(new KeyBindComponent(enabledButton,
                                module::getKey,
                                module::setKey,
                                2, 1));
                        groupBoxComponent.addChild(enabledButton);
                        groupBoxComponent.addChild(new CheckBoxTextComponent(groupBoxComponent, "Hidden",
                                module::isHidden,
                                module::setHidden));

                        addChild(groupBoxComponent);

                        for (Property<?> property : module.getElements()) {
                            Component component = null;
                            if (property.getType() == Boolean.class) {
                                Property<Boolean> booleanProperty = (Property<Boolean>) property;
                                component = new CheckBoxTextComponent(groupBoxComponent, property.getLabel(),
                                        booleanProperty::getValue,
                                        booleanProperty::setValue,
                                        booleanProperty::isAvailable);
                            } else if (property.getType() == Integer.class) {
                                Property<Integer> colorProperty = (Property<Integer>) property;
                                component = new ColorPickerTextComponent(groupBoxComponent, property.getLabel(),
                                        colorProperty::getValue,
                                        colorProperty::setValue,
                                        colorProperty::addValueChangeListener,
                                        colorProperty::isAvailable);
                            } else if (property instanceof DoubleProperty) {
                                DoubleProperty doubleProperty = (DoubleProperty) property;
                                component = new SliderTextComponent(groupBoxComponent,
                                        property.getLabel(),
                                        doubleProperty::getValue,
                                        doubleProperty::setValue,
                                        doubleProperty::getMin,
                                        doubleProperty::getMax,
                                        doubleProperty::getIncrement,
                                        doubleProperty::getRepresentation,
                                        doubleProperty::isAvailable);
                            } else if (property instanceof EnumProperty) {
                                EnumProperty<?> enumProperty = (EnumProperty<?>) property;
                                component = new ComboBoxTextComponent(groupBoxComponent,
                                        property.getLabel(),
                                        enumProperty::getValues,
                                        enumProperty::setValue,
                                        enumProperty::getValue,
                                        () -> null,
                                        enumProperty::isAvailable,
                                        false);
                            } else if (property instanceof MultiSelectEnumProperty) {
                                MultiSelectEnumProperty<?> enumProperty = (MultiSelectEnumProperty<?>) property;
                                component = new ComboBoxTextComponent(groupBoxComponent,
                                        property.getLabel(),
                                        enumProperty::getValues,
                                        enumProperty::setValue,
                                        () -> null,
                                        // Dodgy cast
                                        () -> (List<Enum<?>>) enumProperty.getValue(),
                                        enumProperty::isAvailable,
                                        true);
                            }

                            if (component != null)
                                groupBoxComponent.addChild(component);
                        }
                    }
                    getChildren().sort(Comparator.comparingDouble(Component::getHeight).reversed());
                }
            };

            rootComponent.addChild(categoryTab);
        }

        TabComponent configTab = new TabComponent(rootComponent, "Settings",
                TOTAL_BORDER_WIDTH + TAB_SELECTOR_WIDTH,
                TOTAL_BORDER_WIDTH + RAINBOW_BAR_WIDTH,
                USABLE_AREA_WIDTH,
                USABLE_AREA_HEIGHT) {
            @Override
            public void setupChildren() {
                GroupBoxComponent configsGroupBox = new GroupBoxComponent(this, "Configs", GROUP_BOX_MARGIN,
                        GROUP_BOX_MARGIN,
                        GROUP_BOX_WIDTH,
                        140);
                final int buttonHeight = 15;
                // TODO: Configs box in GUI
                final Consumer<Integer> onPress = button -> {

                };
                configsGroupBox.addChild(new ButtonComponentImpl(configsGroupBox, "Load",
                        onPress,
                        GROUP_BOX_WIDTH - GROUP_BOX_LEFT_MARGIN * 2,
                        buttonHeight));
                configsGroupBox.addChild(new ButtonComponentImpl(configsGroupBox, "Save",
                        onPress,
                        GROUP_BOX_WIDTH - GROUP_BOX_LEFT_MARGIN * 2,
                        buttonHeight));
                configsGroupBox.addChild(new ButtonComponentImpl(configsGroupBox, "Refresh",
                        onPress,
                        GROUP_BOX_WIDTH - GROUP_BOX_LEFT_MARGIN * 2,
                        buttonHeight));
                configsGroupBox.addChild(new ButtonComponentImpl(configsGroupBox, "Delete",
                        onPress,
                        GROUP_BOX_WIDTH - GROUP_BOX_LEFT_MARGIN * 2,
                        buttonHeight));
                addChild(configsGroupBox);

                GroupBoxComponent guiSettingsGroupBox = new GroupBoxComponent(this, "GUI Settings",
                        GROUP_BOX_MARGIN,
                        GROUP_BOX_MARGIN,
                        GROUP_BOX_WIDTH,
                        100);

                guiSettingsGroupBox.addChild(new ColorPickerTextComponent(guiSettingsGroupBox,
                        colorProperty.getLabel(), colorProperty::getValue,
                        colorProperty::setValue, colorProperty::addValueChangeListener));

                addChild(guiSettingsGroupBox);
            }
        };

        rootComponent.addChild(configTab);

        selectedTab = (TabComponent) rootComponent.getChildren().get(selectorIndex);

        tabSelectorComponent = new Component(rootComponent,
                TOTAL_BORDER_WIDTH,
                TOTAL_BORDER_WIDTH + RAINBOW_BAR_WIDTH,
                TAB_SELECTOR_WIDTH,
                USABLE_AREA_HEIGHT) {
            private double selectorY;

            @Override
            public void onMouseClick(int mouseX, int mouseY, int button) {
                if (isHovered(mouseX, mouseY)) {
                    final float mouseYOffset = mouseY - tabSelectorComponent.getY() - 10;
                    if (mouseYOffset > 0 && mouseYOffset < tabSelectorComponent.getHeight() - 10) {
                        selectorIndex = Math.min(ICONS.length - 1, (int) (mouseYOffset / TAB_SELECTOR_HEIGHT));
                        selectedTab = (TabComponent) rootComponent.getChildren().get(selectorIndex);
                    }
                }
            }

            @Override
            public void drawComponent(LockedResolution resolution, int mouseX, int mouseY) {
                selectorY = RenderingUtils.progressiveAnimation(selectorY, selectorIndex * TAB_SELECTOR_HEIGHT + 10, 1.0D);

                final float x = getX();
                final float y = getY();
                final float width = getWidth();
                final float height = getHeight();
                final int innerColor = getColor(0x060606);
                final int outerColor = getColor(0x202020);

                // Top
                Gui.drawRect(x, y, x + width, y + selectorY, getColor(0x0C0C0C));

                Gui.drawRect(x + width - 1, y, x + width, y + selectorY, innerColor);
                Gui.drawRect(x + width - 0.5F, y, x + width, y + selectorY, outerColor);
                Gui.drawRect(x, y + selectorY - 1.0F, x + width - 0.5F, y + selectorY, innerColor);
                Gui.drawRect(x, y + selectorY - 0.5F, x + width, y + selectorY, outerColor);

                // Bottom
                Gui.drawRect(x, y + selectorY + TAB_SELECTOR_HEIGHT, x + width, y + height, getColor(0x0C0C0C));

                Gui.drawRect(x + width - 1, y + selectorY + TAB_SELECTOR_HEIGHT,
                        x + width, y + height, innerColor);
                Gui.drawRect(x + width - 0.5F, y + selectorY + TAB_SELECTOR_HEIGHT,
                        x + width, y + height, outerColor);
                Gui.drawRect(x,
                        y + selectorY + TAB_SELECTOR_HEIGHT,
                        x + width - 0.5F,
                        y + selectorY + TAB_SELECTOR_HEIGHT + 1.0F,
                        innerColor);
                Gui.drawRect(x,
                        y + selectorY + TAB_SELECTOR_HEIGHT,
                        x + width,
                        y + selectorY + TAB_SELECTOR_HEIGHT + 0.5F,
                        outerColor);

                if (shouldRenderText()) {
                    for (int i = 0; i < ICONS.length; i++) {
                        final String c = String.valueOf(ICONS[i]);
                        ICONS_RENDERER.drawString(c,
                                x + (TAB_SELECTOR_WIDTH / 2.0F) - (ICONS_RENDERER.getWidth(c) / 2.0F) - 1,
                                y + 10 + i * TAB_SELECTOR_HEIGHT + (TAB_SELECTOR_HEIGHT / 2.0F) - (ICONS_RENDERER.getHeight(c) / 2.0F),
                                getColor(i == selectorIndex ? 0xFFFFFF : 0x808080));
                    }
                }
            }
        };

        rootComponent.addChild(tabSelectorComponent);
    }

    public static double getAlpha() {
        return alpha;
    }

    public static int getColor() {
        return getColor(colorProperty.getValue());
    }

    public static boolean shouldRenderText() {
        return alpha > 20;
    }

    private static boolean isVisible() {
        return open || alpha > 0;
    }

    public static int getColor(int color) {
        int r = (color >> 16 & 0xFF);
        int g = (color >> 8 & 0xFF);
        int b = (color & 0xFF);
        int a = (int) alpha;

        return ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8) |
                (b & 0xFF) |
                ((a & 0xFF) << 24);
    }

    public static void init() {
        RadiumClient.getInstance().getEventBus().subscribe(INSTANCE);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            this.close();
        } else {
            rootComponent.onKeyPress(keyCode);
        }
    }

    private void close() {
        if (open) {
            targetAlpha = 0;
            open = false;
            dragging = false;
        }
    }

    private void open() {
        Wrapper.getMinecraft().displayGuiScreen(this);
        alpha = 0;
        targetAlpha = 255;
        open = true;
        closed = false;
    }

    private boolean finishedClosing() {
        return !open && alpha == 0 && !closed;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (this.finishedClosing()) {
            Wrapper.getMinecraft().displayGuiScreen(null);
            return;
        }

        if (isVisible()) {
            alpha = RenderingUtils.linearAnimation(alpha, targetAlpha, 15);

            rootComponent.drawComponent(RenderingUtils.getLockedResolution(), mouseX, mouseY);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (isVisible())
            rootComponent.onMouseClick(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (isVisible())
            rootComponent.onMouseRelease(state);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
