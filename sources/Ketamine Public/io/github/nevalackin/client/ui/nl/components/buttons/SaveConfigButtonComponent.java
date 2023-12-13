package io.github.nevalackin.client.ui.nl.components.buttons;

import io.github.nevalackin.client.config.ConfigManager;
import io.github.nevalackin.client.ui.cfont.CustomFontRenderer;
import io.github.nevalackin.client.ui.cfont.StaticallySizedImage;
import io.github.nevalackin.client.ui.framework.Component;
import io.github.nevalackin.client.config.Config;
import io.github.nevalackin.client.core.KetamineClient;
import io.github.nevalackin.client.util.misc.ResourceUtil;
import io.github.nevalackin.client.util.render.ColourUtil;
import io.github.nevalackin.client.util.render.DrawUtil;
import net.minecraft.client.gui.ScaledResolution;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.function.Supplier;

public final class SaveConfigButtonComponent extends Component {

    private static StaticallySizedImage icon;

    static {
        try {
            icon = new StaticallySizedImage(ImageIO.read(ResourceUtil.getResourceStream("icons/ui/save.png")), true, 2);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private final Supplier<Config> configSupplier;

    private double hoveredFadeInProgress;

    public SaveConfigButtonComponent(Component parent,
                                     Supplier<Config> configSupplier,
                                     double x, double y, double width, double height) {
        super(parent, x, y, width, height);

        this.configSupplier = configSupplier;
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        final double x = this.getX();
        final double y = this.getY();
        final double width = this.getWidth();
        final double height = this.getHeight();

        // Hovered animation logic
        {
            this.hoveredFadeInProgress = DrawUtil.animateProgress(this.hoveredFadeInProgress, this.isHovered(mouseX, mouseY) ? 1.0 : 0.0, 4);
        }

        // Draw button outline & background
        {
            DrawUtil.glDrawRoundedOutline(x, y, width, height, 2.f, DrawUtil.RoundingMode.FULL, 4,
                                          ColourUtil.fadeBetween(this.getTheme().getComponentOutlineColour(), this.getTheme().getMainColour(),
                                                                 DrawUtil.bezierBlendAnimation(this.hoveredFadeInProgress)));
        }

        // Draw icon & label
        {
            final CustomFontRenderer cFontRenderer = KetamineClient.getInstance().getFontRenderer();
            final String label = "Save";
            final double iconSize = this.getHeight() - 4;

            final double labelWidth = cFontRenderer.getWidth(label);

            final int textColour = this.getTheme().getTextColour();

            icon.draw(x + width / 2.0 - labelWidth / 2.0 - (iconSize + 4) / 2.0, y + height / 2.0 - iconSize / 2.0, iconSize, iconSize, textColour);

            cFontRenderer.draw(label, x + width / 2.0 - labelWidth / 2.0 + (iconSize + 4) / 2.0, y + height / 2.0 - cFontRenderer.getHeight(label) / 2.0, textColour);
        }
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (button == 0 && this.isHovered(mouseX, mouseY)) {
            final ConfigManager configManager = KetamineClient.getInstance().getConfigManager();

            if (this.configSupplier == null) configManager.saveCurrent();
            else configManager.save(this.configSupplier.get());
        }
    }

    @Override
    public void onMouseRelease(int button) {

    }

    @Override
    public void onKeyPress(int keyCode) {

    }
}
