package io.github.nevalackin.client.ui.nl.components.page.groupBox.components;

import io.github.nevalackin.client.binding.Bind;
import io.github.nevalackin.client.binding.BindType;
import io.github.nevalackin.client.notification.NotificationType;
import io.github.nevalackin.client.ui.cfont.CustomFontRenderer;
import io.github.nevalackin.client.ui.framework.Animated;
import io.github.nevalackin.client.ui.framework.Component;
import io.github.nevalackin.client.ui.framework.ExpandState;
import io.github.nevalackin.client.ui.framework.Expandable;
import io.github.nevalackin.client.core.KetamineClient;
import io.github.nevalackin.client.event.game.input.InputType;
import io.github.nevalackin.client.util.render.DrawUtil;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.lwjgl.opengl.GL11.*;

public final class GroupBoxHeaderComponent extends Component implements Expandable, Animated {

    private final String name;

    private final Unbind unbindFunc;

    private final boolean bindable;
    private boolean binding;

    private final Consumer<Bind> bindSetter;
    private final Supplier<Bind> bindGetter;

    private double clickedX, clickedY;

    private ExpandState state;
    private double progress;

    public GroupBoxHeaderComponent(Component parent, String name, Consumer<Boolean> enabledSetter, Supplier<Boolean> enabledGetter, Consumer<Bind> bindSetter, Supplier<Bind> bindGetter, Unbind unbindFunc, double height) {
        super(parent, 0, 0, 0, height);

        this.name = name;

        this.bindable = bindSetter != null && bindGetter != null && unbindFunc != null;

        this.bindSetter = bindSetter;
        this.bindGetter = bindGetter;
        this.unbindFunc = unbindFunc;

        final CheckBoxComponent checkBox = new CheckBoxComponent(this, null, enabledGetter, enabledSetter, null, height - 4) {
            @Override
            public double getWidth() {
                return this.getParent().getWidth();
            }
        };

        this.addChild(checkBox);
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        final double x = this.getX();
        final double y = this.getY();
        final double width = this.getWidth();
        final double height = this.getHeight();

        final CustomFontRenderer fontRenderer = KetamineClient.getInstance().getFontRenderer();

        final String groupBoxTitle = this.binding ? "Binding..." : this.name;

        fontRenderer.draw(groupBoxTitle, x, y + (height - 4) / 2.0 - fontRenderer.getHeight(groupBoxTitle, 0.275) / 2.0, 0.275, this.getTheme().getHighlightedTextColour());

        final Bind bind;
        if (this.bindable && (bind = this.bindGetter.get()) != null) {
            final String buttonName = String.format("%s [%s]", bind.getCodeName(), bind.getBindType());
            fontRenderer.draw(buttonName, x + fontRenderer.getWidth(groupBoxTitle, 0.275) + 4, y + (height - 4) / 2.0 - fontRenderer.getHeight(buttonName, 0.22) / 2.0, 0.22, 700, this.getTheme().getTextColour());
        }

        final double lineThickness = 1;

        DrawUtil.glDrawFilledQuad(x, y + (height - 2) - lineThickness, width, lineThickness, this.getTheme().getGroupBoxHeaderSeparatorColour());

        super.onDraw(scaledResolution, mouseX, mouseY);

        if (this.state != ExpandState.CLOSED) {
            glPushMatrix();

            switch (this.state) {
                case EXPANDING:
                    this.progress = DrawUtil.animateProgress(this.progress, 1.0, 4);
                    if (this.progress >= 1.0) {
                        this.state = ExpandState.EXPANDED;
                        this.progress = 1.0;
                    }
                    break;
                case CONTRACTING:
                    this.progress = DrawUtil.animateProgress(this.progress, 0.0, 4);
                    if (this.progress <= 0.0) {
                        this.progress = 0.0;
                        this.state = ExpandState.CLOSED;
                        return;
                    }
                    break;
            }

            // Animation
            glTranslated(x, y + (height - 4) / 2.0, 2.0);
            glScaled(this.progress, this.progress, 1.0);
            glTranslated(-x, -(y + (height - 4) / 2.0), 0.0);

            final double ex = this.getExpandedX();
            final double ey = this.getExpandedY();
            final double ew = this.getExpandedWidth();
            final double eh = this.getExpandedHeight();

            DrawUtil.glDrawFilledQuad(ex, ey, ew, eh,
                                        this.getTheme().getGroupBoxBackgroundColour());
            DrawUtil.glDrawOutlinedQuad(ex, ey, ew, eh,
                                      1.f, this.getTheme().getMainColour());

            final BindType[] types = BindType.values();

            double yOffset = ey + 1;

            for (final BindType type : types) {
                final boolean hovered = mouseX > ex && mouseX < ex + ew && mouseY > yOffset && mouseY < yOffset + this.buttonHeight;
                fontRenderer.draw(type.toString(), ex + ew / 2.0 - fontRenderer.getWidth(type.toString(), this.fontScale, 700) / 2.0,
                                  yOffset + this.buttonHeight / 2.0 - fontRenderer.getHeight(type.toString(), this.fontScale, 700) / 2.0, this.fontScale, 700,
                                  hovered ? this.getTheme().getHighlightedTextColour() : this.getTheme().getTextColour());

                yOffset += this.buttonHeight;
            }

            glPopMatrix();
        }
    }

    private BindType selectedBindType;

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (this.binding) {
            this.bindSetter.accept(new Bind(InputType.MOUSE, button, this.selectedBindType));
            KetamineClient.getInstance().getNotificationManager().add(NotificationType.INFO, String.format("Bound %s", this.name),
                                                                      String.format("Set %s to %s on %s", this.name, this.selectedBindType.toString(), Mouse.getButtonName(button)),
                                                                      2000L);
            this.binding = false;
            return;
        }

        if (button == 0) {
            if (this.isHoveredExpanded(mouseX, mouseY)) {
                final double ex = this.getExpandedX();
                final double ey = this.getExpandedY();
                final double ew = this.getExpandedWidth();

                final BindType[] types = BindType.values();

                double yOffset = ey + 1;

                for (final BindType type : types) {
                    if (mouseX > ex && mouseX < ex + ew && mouseY > yOffset && mouseY < yOffset + this.buttonHeight) {
                        this.selectedBindType = type;
                        this.setState(ExpandState.CONTRACTING);
                        if (this.selectedBindType == BindType.NONE) {
                            this.unbindFunc.unbind();
                            return;
                        }
                        this.binding = true;
                    }

                    yOffset += this.buttonHeight;
                }

                return;
            }

            super.onMouseClick(mouseX, mouseY, button);
        } else if (button == 1 && this.bindable) {
            switch (this.state) {
                case CONTRACTING:
                case CLOSED:
                    this.setState(ExpandState.EXPANDING);
                    this.clickedX = mouseX;
                    this.clickedY = mouseY;
                    break;
                case EXPANDING:
                case EXPANDED:
                    this.setState(ExpandState.CONTRACTING);
                    break;
            }
        }
    }

    @Override
    public void onKeyPress(int keyCode) {
        if (this.binding) {
            this.bindSetter.accept(new Bind(InputType.KEYBOARD, keyCode, this.selectedBindType));
            KetamineClient.getInstance().getNotificationManager().add(NotificationType.INFO, String.format("Bound %s", this.name),
                                                                      String.format("Set %s to %s on %s", this.name, this.selectedBindType.toString(), Keyboard.getKeyName(keyCode)),
                                                                      2000L);
            this.binding = false;
        }
    }

    @Override
    public void onMouseRelease(int button) {

    }

    @Override
    public void setState(ExpandState state) {
        this.state = state;
    }

    @Override
    public ExpandState getState() {
        return this.state;
    }

    @Override
    public double getExpandedX() {
        return this.clickedX;
    }

    @Override
    public double getExpandedY() {
        return this.clickedY;
    }

    private double expandedWidth, lastExpandedHeight, fontScale, buttonHeight;

    @Override
    public double getExpandedWidth() {
        final double eh = this.calculateExpandedHeight();

        if (this.lastExpandedHeight != eh) {
            final BindType[] types = BindType.values();
            this.buttonHeight = eh / types.length;
            this.fontScale = (this.buttonHeight - 2) / CustomFontRenderer.BASE_FONT;

            double longest = 0;

            final CustomFontRenderer fontRenderer = KetamineClient.getInstance().getFontRenderer();

            for (final BindType type : types) {
                final double length = fontRenderer.getWidth(type.toString(), this.fontScale, 700) + 4;
                if (length > longest) {
                    longest = length;
                }
            }

            this.expandedWidth = longest;
            this.lastExpandedHeight = eh;
        }

        return this.expandedWidth;
    }

    @Override
    public double calculateExpandedHeight() {
        return this.getHeight() + 4;
    }

    @Override
    public double getExpandProgress() {
        return this.progress;
    }

    @Override
    public void resetAnimationState() {
        this.setState(ExpandState.CLOSED);
        this.progress = 0.0;
    }

    @FunctionalInterface
    public interface Unbind {
        void unbind();
    }
}
