package me.betterfps;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Consumer;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000*\n\n\n\b\n\n\b\n\n\b\n\n\u0000\n\n\b\bÆ\u000020B\b¢J'H\"\b\u00002H2\f\bH0¢\bJ\b\t0\nJ\b0\nJ\f0\r20J\f0\r2020J0\r20¨"}, d2={"Lme/betterfps/MiscUtils;", "Lnet/ccbluex/liquidbounce/utils/MinecraftInstance;", "()V", "make", "T", "object", "consumer", "Ljava/util/function/Consumer;", "(Ljava/lang/Object;Ljava/util/function/Consumer;)Ljava/lang/Object;", "openFileChooser", "Ljava/io/File;", "saveFileChooser", "showErrorPopup", "", "message", "", "title", "showURL", "url", "Pride"})
public final class MiscUtils
extends MinecraftInstance {
    public static final MiscUtils INSTANCE;

    public final void showErrorPopup(@NotNull String message) {
        Intrinsics.checkParameterIsNotNull(message, "message");
        JOptionPane.showMessageDialog(null, message, "Alert", 0);
    }

    public final void showErrorPopup(@NotNull String title, @NotNull String message) {
        Intrinsics.checkParameterIsNotNull(title, "title");
        Intrinsics.checkParameterIsNotNull(message, "message");
        JOptionPane.showMessageDialog(null, message, title, 0);
    }

    public final void showURL(@NotNull String url) {
        Intrinsics.checkParameterIsNotNull(url, "url");
        try {
            Desktop.getDesktop().browse(new URI(url));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    public final File openFileChooser() {
        if (MinecraftInstance.mc.isFullScreen()) {
            MinecraftInstance.mc.toggleFullscreen();
        }
        JFileChooser fileChooser = new JFileChooser();
        JFrame frame = new JFrame();
        fileChooser.setFileSelectionMode(0);
        frame.setVisible(true);
        frame.toFront();
        frame.setVisible(false);
        int action = fileChooser.showOpenDialog(frame);
        frame.dispose();
        return action == 0 ? fileChooser.getSelectedFile() : null;
    }

    @Nullable
    public final File saveFileChooser() {
        if (MinecraftInstance.mc.isFullScreen()) {
            MinecraftInstance.mc.toggleFullscreen();
        }
        JFileChooser fileChooser = new JFileChooser();
        JFrame frame = new JFrame();
        fileChooser.setFileSelectionMode(0);
        frame.setVisible(true);
        frame.toFront();
        frame.setVisible(false);
        int action = fileChooser.showSaveDialog(frame);
        frame.dispose();
        return action == 0 ? fileChooser.getSelectedFile() : null;
    }

    public final <T> T make(T object, @NotNull Consumer<T> consumer) {
        Intrinsics.checkParameterIsNotNull(consumer, "consumer");
        consumer.accept(object);
        return object;
    }

    private MiscUtils() {
    }

    static {
        MiscUtils miscUtils;
        INSTANCE = miscUtils = new MiscUtils();
    }
}
