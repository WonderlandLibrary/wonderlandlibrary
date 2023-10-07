package cc.novoline.gui;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.gui.GuiScreen;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.awt.*;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cc.novoline.Novoline.getLogger;

/**
 * @author xDelsy
 */
public abstract class NovoGuiScreen extends GuiScreen {

    /* fields */
    @NonNull
    private final List<Element> elements;

    /* constructors */
    public NovoGuiScreen(@Nullable Collection<Element> elements) {
        this.elements = elements == null ? new ObjectArrayList<>() : new ObjectArrayList<>(elements);
    }

    public NovoGuiScreen() {
        this(null);
    }

    /* methods */
    protected void register(@NonNull Element element) {
        elements.add(element);
        // System.out.println("Registered an element: " + element.getClass().getSimpleName());
    }

    private long lastMessageTimestamp;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawGradientRect(0, 0, width, height, new Color(100, 200, 200).getRGB(),
                new Color(100, 100, 200).getRGB());

        for (int i = 0; i < elements.size(); i++) {
            elements.get(i).draw(mouseX, mouseY);
        }

        if (System.currentTimeMillis() - lastMessageTimestamp >= 1_000) {
            getLogger().info("Drew: {}", elements.stream()
                    .map(element -> element.getClass().getSimpleName())
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting())));
            this.lastMessageTimestamp = System.currentTimeMillis();
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void onInitialize() {
    }

    protected void onClosing() {
    }

    @Override
    public final void initGui() {
        elements.clear();

        onInitialize();
        super.initGui();
    }

    @Override
    public void onGuiClosed() {
        onClosing();
        super.onGuiClosed();
    }
}
