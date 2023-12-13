package io.github.nevalackin.client.ui.nl.components;

import io.github.nevalackin.client.module.Category;
import io.github.nevalackin.client.property.Property;
import io.github.nevalackin.client.ui.cfont.CustomFontRenderer;
import io.github.nevalackin.client.ui.framework.Component;
import io.github.nevalackin.client.core.KetamineClient;
import io.github.nevalackin.client.property.BooleanProperty;
import io.github.nevalackin.client.property.DoubleProperty;
import io.github.nevalackin.client.property.EnumProperty;
import io.github.nevalackin.client.property.MultiSelectionEnumProperty;
import io.github.nevalackin.client.ui.nl.components.page.PageComponent;
import io.github.nevalackin.client.ui.nl.components.page.groupBox.GroupBoxComponent;
import io.github.nevalackin.client.ui.nl.components.page.groupBox.components.CheckBoxComponent;
import io.github.nevalackin.client.ui.nl.components.page.groupBox.components.DropDownComponent;
import io.github.nevalackin.client.ui.nl.components.page.groupBox.components.GroupBoxHeaderComponent;
import io.github.nevalackin.client.ui.nl.components.page.groupBox.components.SliderComponent;
import io.github.nevalackin.client.ui.nl.components.selector.PageSelector;
import io.github.nevalackin.client.ui.nl.components.selector.PageSelectorComponent;
import io.github.nevalackin.client.util.render.BlurUtil;
import io.github.nevalackin.client.util.render.DrawUtil;
import net.minecraft.client.gui.ScaledResolution;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.glScaled;
import static org.lwjgl.opengl.GL11.glTranslated;

public final class RootComponent extends WindowComponent implements PageSelector {

    private final PageSelectorComponent pageSelector;
    private final HeaderComponent header;

    private PageComponent currentPage;
    private int currentIdx = -1;
    private final List<PageComponent> pageComponents = new ArrayList<>();

    private final Theme theme = Theme.BLUE;

    private static final double WIDTH = 840.0;
    private static final double HEIGHT = 740.0;

    public RootComponent(final ScaledResolution scaledResolution) {
        super(null, 0, 0,
              WIDTH / scaledResolution.getScaleFactor(),
              HEIGHT / scaledResolution.getScaleFactor());

        this.setResizable(true);
        this.setDraggable(true);

        // Centre the UI
        this.setX(scaledResolution.getScaledWidth() / 2.0 - this.getWidth() / 2.0);
        this.setY(scaledResolution.getScaledHeight() / 2.0 - this.getHeight() / 2.0);

        // Initialize the header
        this.header = new HeaderComponent(this);
        this.addChild(this.header);

        // Initialize page selector on left
        this.pageSelector = new PageSelectorComponent(this);
        this.addChild(this.pageSelector);

        // Initial all page components
        for (final Category category : Category.values()) {
            for (final Category.SubCategory subCategory : category.getSubCategories()) {
                this.pageComponents.add(new PageComponent(this) {
                    @Override
                    public void onInit() {
                        KetamineClient.getInstance().getModuleManager().getModules().stream()
                            .filter(module -> module.getSubCategory() == subCategory)
                            .forEach(module -> this.addChild(new GroupBoxComponent(this) {
                                @Override
                                public void onInit() {
                                    this.addChild(new GroupBoxHeaderComponent(this, module.getName(), module::setEnabled, module::isEnabled,
                                                                              module::setBind, module::getBind, () -> module.setBind(null), 20));

                                    // TODO :: Add more settings
                                    for (final Property<?> property : module.getProperties()) {
                                        if (property instanceof BooleanProperty) {
                                            final BooleanProperty booleanProperty = (BooleanProperty) property;
                                            this.addChild(new CheckBoxComponent(this, property.getName(),
                                                                                booleanProperty::getValue, booleanProperty::setValue,
                                                                                booleanProperty::check, 14));
                                        } else if (property instanceof DoubleProperty) {
                                            final DoubleProperty doubleProperty = (DoubleProperty) property;
                                            this.addChild(new SliderComponent(this, property.getName(),
                                                                              doubleProperty::getDisplayString,
                                                                              doubleProperty::setValue, doubleProperty::getValue, doubleProperty::getMin,
                                                                              doubleProperty::getMax, doubleProperty::getInc,
                                                                              doubleProperty::check, 14));
                                        } else if (property instanceof EnumProperty) {
                                            final EnumProperty<?> enumProperty = (EnumProperty<?>) property;
                                            this.addChild(new DropDownComponent(this, property.getName(), enumProperty::setValue, null,
                                                                                null, () -> enumProperty.getValue().ordinal(), enumProperty::getValueNames,
                                                                                enumProperty::check, false, 14));
                                        } else if (property instanceof MultiSelectionEnumProperty) {
                                            final MultiSelectionEnumProperty<?> enumProperty = (MultiSelectionEnumProperty<?>) property;
                                            this.addChild(new DropDownComponent(this, property.getName(), enumProperty::select, enumProperty::unselect,
                                                                                enumProperty::getValueIndices, null, enumProperty::getValueNames,
                                                                                enumProperty::check, true, 14));
                                        }
                                    }
                                }
                            }));
                    }
                });
            }
        }
    }

    @Override
    public Theme getTheme() {
        return this.theme;
    }

    @Override
    public void onPageSelect(int idx, double y) {
        if (this.pageComponents.isEmpty()) return;
        final int currentIdx = Math.min(this.pageComponents.size() - 1, Math.max(0, idx));
        if (this.currentIdx != currentIdx) {
            this.currentIdx = currentIdx;
            this.currentPage = this.pageComponents.get(currentIdx);
            this.currentPage.pageSelectorButtonY = y;
            this.currentPage.resetAnimationState();
        }
    }

    @Override
    public int getSelectedIdx() {
        return this.currentIdx;
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        final double x = this.getX();
        final double y = this.getY();
        final double width = this.getWidth();
        final double height = this.getHeight();

        this.setWidth(WIDTH / scaledResolution.getScaleFactor());
        this.setHeight(HEIGHT / scaledResolution.getScaleFactor());

        this.pageSelector.setY(this.header.getHeight());
        this.pageSelector.setHeight(this.getHeight() - this.header.getHeight());

        this.header.setX(this.pageSelector.getWidth());
        this.header.setWidth(this.getWidth() - this.pageSelector.getWidth());

        this.fadeInProgress = DrawUtil.animateProgress(this.fadeInProgress, 1.0, 5);
        final double smoothFadeIn = DrawUtil.bezierBlendAnimation(this.fadeInProgress);

        glTranslated(x + width / 2.0, y + height / 2.0, 0);
        glScaled(smoothFadeIn, smoothFadeIn, 1);
        glTranslated(-(x + width / 2.0), -(y + height / 2.0), 0);

        if (smoothFadeIn >= 1.0)
            this.onHandleDragging(mouseX, mouseY);

        // Draw background
        {
            final double pageSelectorWidth = this.pageSelector.getWidth();
            final double separatorWidth = 1;

            // Draw page selector background
            if (smoothFadeIn >= 1.0)
                BlurUtil.blurArea(x, y, pageSelectorWidth, height);
            DrawUtil.glDrawRoundedRect(x, y, pageSelectorWidth, height,
                                       DrawUtil.RoundingMode.LEFT,
                                       4.f, scaledResolution.getScaleFactor(),
                                       this.getTheme().getPageSelectorBackgroundColour());
            // Draw separator
            DrawUtil.glDrawFilledQuad(x + pageSelectorWidth - separatorWidth, y, separatorWidth, height, this.getTheme().getPageSelectorPageSeparatorColour());

            // Draw canvas background
            DrawUtil.glDrawRoundedRect(x + pageSelectorWidth, y,
                                       width - pageSelectorWidth,
                                       height, DrawUtil.RoundingMode.RIGHT,
                                       4.f, scaledResolution.getScaleFactor(),
                                       this.getTheme().getPageBackgroundColour());
        }

        // Draw watermark
        {
            final String watermarkText = "\247L" + KetamineClient.getInstance().getName().toUpperCase();

            final CustomFontRenderer fontRenderer = KetamineClient.getInstance().getFontRenderer();

            final double watermarkTextX = x + this.pageSelector.getWidth() / 2.0 - fontRenderer.getWidth(watermarkText, 0.5) / 2.0;
            final double watermarkTextY = y + this.header.getHeight() / 2.0 - fontRenderer.getHeight(watermarkText, 0.5) / 2.0;

            fontRenderer.draw(watermarkText, watermarkTextX - 0.5, watermarkTextY, 0.5, 900, this.getTheme().getWatermarkTextColour());
        }

        // Draw all the children on top of everything
        super.onDraw(scaledResolution, mouseX, mouseY);

        if (this.currentPage != null) {
            // reposition current page
            this.currentPage.setX(this.pageSelector.getWidth());
            this.currentPage.setY(this.header.getHeight());
            // resize current page
            this.currentPage.setWidth(this.getWidth() - this.pageSelector.getWidth());
            this.currentPage.setHeight(this.getHeight() - this.header.getHeight());
            // draw current page
            this.currentPage.onDraw(scaledResolution, mouseX, mouseY);
        }

        glScaled(-smoothFadeIn, -smoothFadeIn, 1);
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (this.fadeInProgress < 1.0) return;
        for (final Component child : this.getChildren()) {
            if (child.isHovered(mouseX, mouseY)) {
                child.onMouseClick(mouseX, mouseY, button);
                return;
            }
        }

        if (this.currentPage != null) {
            for (final Component child : this.currentPage.getChildren()) {
                if (child.isHovered(mouseX, mouseY)) {
                    child.onMouseClick(mouseX, mouseY, button);
                    return;
                }
            }
        }

        if (button == 0 && this.isHovered(mouseX, mouseY))
            this.onStartDragging(mouseX, mouseY);
    }

    @Override
    public void onMouseRelease(int button) {
        if (button == 0) this.onStopDragging();

        super.onMouseRelease(button);

        if (this.currentPage != null) this.currentPage.onMouseRelease(button);
    }

    @Override
    public void onKeyPress(int keyCode) {
        if (this.currentPage != null) this.currentPage.onKeyPress(keyCode);
    }
}
