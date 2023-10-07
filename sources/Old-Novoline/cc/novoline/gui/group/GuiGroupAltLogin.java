package cc.novoline.gui.group;

import cc.novoline.gui.screen.alt.repository.GuiAddAlt;
import cc.novoline.utils.RenderUtils;
import static cc.novoline.utils.fonts.impl.Fonts.SFBOLD.SFBOLD_22.SFBOLD_22;
import static cc.novoline.utils.fonts.impl.Fonts.SFTHIN.SFTHIN_20.SFTHIN_20;
import java.awt.*;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

/**
 * @author xDelsy
 */
public final class GuiGroupAltLogin extends GuiRoundedGroupWithLines<Object> {

    private String status = EnumChatFormatting.GRAY + "Idling...";

    public GuiGroupAltLogin(final GuiAddAlt gui, final String text) {
        super(text, 0, 15, 200, 30, 15, () -> null, SFTHIN_20, SFBOLD_22);

        this.xPosition = (int) ((gui.width - this.width) / 2F);
        addLine(t -> this.status);
    }

    @Override
    public void drawGroup(final Minecraft mc, final int mouseX, final int mouseY) {
        if (this.hidden) return;

        RenderUtils.drawRoundedRect(this.xPosition, this.yPosition, this.width, this.height, this.radius,
                new Color(48, 49, 54).getRGB());

        if (this.title != null) {
            this.titleFontRenderer.drawString(this.title, // @off
                    this.xPosition + (this.width - this.titleFontRenderer.stringWidth(this.title)) / 2.0F,
                    this.yPosition + 5,
                    new Color(198, 198, 198).getRGB());
        } // @on

        drawLines();
    }

    @Override
    protected void drawLines() {
        final String text = this.lines.get(0).getText(null);
        this.lineFontRenderer.drawString(text, // @off
                this.xPosition + (this.width - this.lineFontRenderer.stringWidth(text)) / 2F,
                this.yPosition + this.lineFontRenderer.getHeight() + 11,
                new Color(198, 198, 198).getRGB()); // @on
    }

    public void updateStatus(String status) {
        this.status = status;
    }

}
