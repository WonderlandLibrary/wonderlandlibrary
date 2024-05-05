package graphite.impl.gui.click.component;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import graphite.api.property.Property;
import graphite.api.property.impl.*;
import graphite.impl.Graphite;
import graphite.impl.gui.click.component.tabcontrol.page.groupbox.GroupBoxComponent;
import graphite.impl.gui.click.component.tabcontrol.page.groupbox.components.*;

import graphite.api.module.util.Category;
import graphite.api.util.render.RenderUtil;
import graphite.impl.gui.click.component.tabcontrol.page.PageComponent;
import graphite.impl.gui.click.component.tabcontrol.selector.PageSelector;
import graphite.impl.gui.click.component.tabcontrol.selector.PageSelectorComponent;
import graphite.impl.gui.click.framework.Component;
import graphite.impl.notifications.dev.DevNotifications;
import net.minecraft.client.gui.ScaledResolution;

public final class RootComponent extends WindowComponent implements PageSelector {
    private final PageSelectorComponent pageSelector;

    private PageComponent currentPage;
    private int currentIdx = 0;
    private final List<PageComponent> pageComponents = new ArrayList<>();

    private static final double WIDTH = 306.0;
    private static final double HEIGHT = 208.0;

    public RootComponent(final ScaledResolution scaledResolution) {
        super(null, 0, 0, WIDTH, HEIGHT);

        this.setDraggable(true);

        this.setX(scaledResolution.getScaledWidth() / 2.0 - this.getWidth() / 2.0);
        this.setY(scaledResolution.getScaledHeight() / 2.0 - this.getHeight() / 2.0);

        this.pageSelector = new PageSelectorComponent(this);
        this.addChild(this.pageSelector);

        // Category pages
        for (final Category category : Category.values()) {
            this.pageComponents.add(new PageComponent(this) {
                @Override
                public void onInit() {
                    Graphite.INSTANCE.getModuleManager().getModules().stream()
                            .filter((module -> module.getCategory() == category))
                            .forEach(module -> this.addChild(new GroupBoxComponent(this, module.getName()) {
                                @Override
                                public void onInit() {
                                    this.addChild(new CheckBoxComponent(this, "Enabled", module::isEnabled, module::setEnabled, null));
                                    this.addChild(new CheckBoxComponent(this, "Hidden", module::isHidden, module::setHidden, null));

                                    for (final Property<?> property : module.getProperties()) {
                                        if (property instanceof BooleanProperty) {
                                            final BooleanProperty booleanProperty = (BooleanProperty) property;
                                            this.addChild(new CheckBoxComponent(this, property.getName(),
                                                    booleanProperty::getValue, booleanProperty::setValue,
                                                    booleanProperty::check));
                                        } else if (property instanceof DoubleProperty) {
                                            final DoubleProperty doubleProperty = (DoubleProperty) property;
                                            this.addChild(new SliderComponent(this, property.getName(),
                                                    doubleProperty::getDisplayString,
                                                    doubleProperty::setValue, doubleProperty::getValue, doubleProperty::getMin,
                                                    doubleProperty::getMax,
                                                    doubleProperty::check, doubleProperty::getRepresentation));
                                        } else if (property instanceof ColourProperty) {
                                            final ColourProperty colourProperty = (ColourProperty) property;
                                            this.addChild(new ColorPickerComponent(this, property.getName(), colourProperty::getValue,
                                                    colourProperty::setValue, colourProperty::check));
                                        } else if (property instanceof EnumProperty) {
                                            final EnumProperty<?> enumProperty = (EnumProperty<?>) property;
                                            this.addChild(new ComboBoxComponent(this, property.getName(), enumProperty::setValue, null,
                                                    null, () -> enumProperty.getValue().ordinal(), enumProperty::getValueNames,
                                                    enumProperty::check, false));
                                        } else if (property instanceof MultiSelectionEnumProperty) {
                                            final MultiSelectionEnumProperty<?> enumProperty = (MultiSelectionEnumProperty<?>) property;
                                            this.addChild(new ComboBoxComponent(this, property.getName(), enumProperty::select, enumProperty::unselect,
                                                    enumProperty::getValueIndices, null, enumProperty::getValueNames,
                                                    enumProperty::check, true));
                                        }
                                    }
                                }

                                @Override
                                public double getMarginX() {
                                    return 6.5;
                                }
                            }));
                }
            });
        }
        
        // Configuration page
        this.pageComponents.add(new PageComponent(this) {
            @Override
            public void onInit() {
                this.addChild(new GroupBoxComponent(this, "Profiles") {
                    @Override
                    public void onInit() {
                        this.addChild(new ListBoxComponent(this, Graphite.INSTANCE.getConfigManager()::getConfigs) {
                            @Override
                            public double getHeight() {
                                return pageSelector.getHeight() + HEIGHT - 68;
                            }
                        });
                    }

                    @Override
                    public double getMarginX() {
                        return 6.5;
                    }

                    @Override
                    public double getHeight() {
                        return pageSelector.getHeight() + HEIGHT - 57;
                    }
                });

                this.addChild(new GroupBoxComponent(this, "Configurations") {
                    @Override
                    public void onInit() {
                        this.addChild(new TextBoxComponent(this, null, null, 80, 10));

                        this.addChild(new ButtonComponent(this, "New", 80, 10) {
                            @Override
                            public void onPress() {
                                DevNotifications.getManager().post("You pressed button NEW");
                            }
                        });

                        this.addChild(new ButtonComponent(this, "Save", 80, 10) {
                            @Override
                            public void onPress() {
                                DevNotifications.getManager().post("You pressed button SAVE");
                            }
                        });

                        this.addChild(new ButtonComponent(this, "Load", 80, 10) {
                            @Override
                            public void onPress() {
                                DevNotifications.getManager().post("You pressed button LOAD");
                            }
                        });

                        this.addChild(new ButtonComponent(this, "Reset", 80, 10) {
                            @Override
                            public void onPress() {
                                DevNotifications.getManager().post("You pressed button RESET");
                            }
                        });

                        this.addChild(new ButtonComponent(this, "Delete", 80, 10) {
                            @Override
                            public void onPress() {
                                DevNotifications.getManager().post("You pressed button DELETE");
                            }
                        });

                        this.addChild(new ButtonComponent(this, "Import", 80, 10) {
                            @Override
                            public void onPress() {
                                DevNotifications.getManager().post("You pressed button IMPORT");
                            }
                        });

                        this.addChild(new ButtonComponent(this, "Export", 80, 10) {
                            @Override
                            public void onPress() {
                                DevNotifications.getManager().post("You pressed button EXPORT");
                            }
                        });
                    }

                    @Override
                    public double getMarginX() {
                        return 25;
                    }

                    @Override
                    public double getHeight() {
                        return pageSelector.getHeight() + HEIGHT - 57;
                    }
                });
            }
        });
        
        if (currentIdx != -1)
            this.currentPage = this.pageComponents.get(currentIdx);
    }
    
    @Override
    public void onPageSelect(int idx) {
        if (this.pageComponents.isEmpty()) return;
        final int currentIdx = Math.min(this.pageComponents.size() - 1, Math.max(0, idx));
        if (this.currentIdx != currentIdx) {
            this.currentIdx = currentIdx;
            this.currentPage = this.pageComponents.get(currentIdx);
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

        this.pageSelector.setX(9);
        this.pageSelector.setY(9);
        this.pageSelector.setHeight(13);
                
        this.onHandleDragging(scaledResolution, mouseX, mouseY);
                
        {
            // form
            RenderUtil.drawRect(x, y, x + width, y + height, new Color(23, 23, 30).getRGB());
            RenderUtil.drawBorderedRect(x + .5, y + .5, x + width - .5, y + height - .5, .5, new Color(39, 39, 47).getRGB(), new Color(62, 62, 72).getRGB());
            RenderUtil.drawBorderedRect(x + 3, y + 3, x + width - 3, y + height - 3, .5, new Color(23, 23, 30).getRGB(), new Color(62, 62, 72).getRGB());
            RenderUtil.drawGradientRect(x + 3.5, y + 3.5, x + width - 3.5, y + 12, false, new Color(14, 14, 22).getRGB(), new Color(23, 23, 30).getRGB());

            // colored line
            RenderUtil.drawRect(x + 4, y + 4, x + width - 4, y + 5, Theme.getUIColor());
            // volumetric line effect (as in skeet)
            //RenderUtil.drawRect(x + 4, y + 4.5, x + width - 4, y + 5, new Color(0, 0, 0, 110).getRGB());
        }
        
        super.onDraw(scaledResolution, mouseX, mouseY);
       
        if (this.currentPage != null) {
            this.currentPage.setX(9);
            this.currentPage.setY(21.5);
            this.currentPage.setWidth(this.pageSelector.getWidth() - 2);
            this.currentPage.setHeight(this.pageSelector.getHeight() + height - 44);
            this.currentPage.onDraw(scaledResolution, mouseX, mouseY);
        }
    }
    
    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
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