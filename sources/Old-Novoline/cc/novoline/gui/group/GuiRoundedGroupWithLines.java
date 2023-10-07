package cc.novoline.gui.group;

import cc.novoline.utils.fonts.api.FontRenderer;
import static cc.novoline.utils.fonts.impl.Fonts.SFTHIN.SFTHIN_28.SFTHIN_28;
import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;

/**
 * @author xDelsy
 */
public class GuiRoundedGroupWithLines<T> extends GuiRoundedGroup {

    protected final CopyOnWriteArrayList<GroupLine<T>> lines = new CopyOnWriteArrayList<>();
    protected final List<GroupLine<T>> linesView = Collections.unmodifiableList(this.lines);
    protected final Supplier<T> supplier;
    protected final FontRenderer lineFontRenderer;

    public GuiRoundedGroupWithLines(String title, int xPosition, int yPosition, int width, int height, int radius,
                                    Supplier<T> supplier, FontRenderer lineFontRenderer,
                                    FontRenderer titleFontRenderer) {
        super(title, xPosition, yPosition, width, height, radius, titleFontRenderer);
        this.supplier = supplier;
        this.lineFontRenderer = lineFontRenderer;
    }

    public GuiRoundedGroupWithLines(String title, int xPosition, int yPosition, int width, int height, int radius,
                                    Supplier<T> supplier, FontRenderer lineFontRenderer) {
        super(title, xPosition, yPosition, width, height, radius);
        this.supplier = supplier;
        this.lineFontRenderer = lineFontRenderer;
    }

    public GuiRoundedGroupWithLines(String title, int xPosition, int yPosition, int width, int height, int radius,
                                    Supplier<T> supplier) {
        super(title, xPosition, yPosition, width, height, radius);
        this.supplier = supplier;
        this.lineFontRenderer = SFTHIN_28;
    }

    @Override
    public void drawGroup(Minecraft mc, int mouseX, int mouseY) {
        super.drawGroup(mc, mouseX, mouseY);
        drawLines();
    }

    protected void superDrawGroup(Minecraft mc, int mouseX, int mouseY) {
        super.drawGroup(mc, mouseX, mouseY);
    }

    protected void drawLines() {
        final int perLine = this.lineFontRenderer.getHeight() + 4;
        int yOffset = this.yPosition + 4;

        T t = this.supplier.get();

        for (GroupLine<T> line : this.lines) {
            final String text = line.getText(t);

            if (!(line instanceof GroupNullableLine) || text != null) {
                yOffset += perLine;
                this.lineFontRenderer
                        .drawString(text, this.xPosition + 6, this.yPosition + yOffset, new Color(198, 198, 198).getRGB());
            }
        }
    }

    public void addNullableLine(Function<T, String> stringFunction) {
        this.lines.add(GroupNullableLine.of(stringFunction));
    }

    public void addLine(Function<T, String> stringFunction) {
        this.lines.add(GroupSupplierLine.of(stringFunction));
    }

    @SuppressWarnings("unchecked")
    public void addLine(String s) {
        this.lines.add((GroupLine<T>) GroupConstantLine.of(s));
    }

    //region Lombok
    public List<GroupLine<T>> getLines() {
        return this.linesView;
    }

    public FontRenderer getLineFontRenderer() {
        return this.lineFontRenderer;
    }
    //endregion

}
