/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidinstruction;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import javax.swing.JFrame;
import javax.swing.JLabel;
import kotlin.Metadata;
import kotlin.io.TextStreamsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=2, xi=2, d1={"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u001a\u0006\u0010\u0000\u001a\u00020\u0001\u00a8\u0006\u0002"}, d2={"main", "", "KyinoClient"})
public final class LiquidInstructionKt {
    public static final void main() {
        JFrame frame = new JFrame("KyinoSense | Installation");
        frame.setDefaultCloseOperation(3);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        frame.setAlwaysOnTop(true);
        InputStream inputStream = LiquidBounce.class.getResourceAsStream("/instructions.html");
        Intrinsics.checkExpressionValueIsNotNull(inputStream, "LiquidBounce::class.java\u2026eam(\"/instructions.html\")");
        InputStream inputStream2 = inputStream;
        Charset charset = Charsets.UTF_8;
        boolean bl = false;
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream2, charset);
        String string = TextStreamsKt.readText(inputStreamReader);
        String string2 = LiquidBounce.INSTANCE.getClass().getClassLoader().getResource("assets").toString();
        Intrinsics.checkExpressionValueIsNotNull(string2, "LiquidBounce.javaClass.c\u2026urce(\"assets\").toString()");
        String string3 = StringsKt.replace$default(string, "{assets}", string2, false, 4, null);
        JLabel label = new JLabel(string3);
        frame.add((Component)label, "Center");
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static /* synthetic */ void main(String[] stringArray) {
        LiquidInstructionKt.main();
    }
}

