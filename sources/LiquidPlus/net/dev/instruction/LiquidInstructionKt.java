/*
 * Decompiled with CFR 0.152.
 */
package net.dev.instruction;

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
import net.dev.important.Client;

@Metadata(mv={1, 6, 0}, k=2, xi=48, d1={"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u001a\u0006\u0010\u0000\u001a\u00020\u0001\u00a8\u0006\u0002"}, d2={"main", "", "LiquidBounce"})
public final class LiquidInstructionKt {
    public static final void main() {
        JFrame frame = new JFrame("Install");
        frame.setDefaultCloseOperation(3);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        frame.setAlwaysOnTop(true);
        Object object = Client.class.getResourceAsStream("/instructions.html");
        Intrinsics.checkNotNullExpressionValue(object, "Client::class.java.getRe\u2026eam(\"/instructions.html\")");
        Charset charset = Charsets.UTF_8;
        String string = TextStreamsKt.readText(new InputStreamReader((InputStream)object, charset));
        object = Client.INSTANCE.getClass().getClassLoader().getResource("assets").toString();
        Intrinsics.checkNotNullExpressionValue(object, "Client.javaClass.classLo\u2026urce(\"assets\").toString()");
        JLabel label = new JLabel(StringsKt.replace$default(string, "{assets}", (String)object, false, 4, null));
        frame.add((Component)label, "Center");
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static /* synthetic */ void main(String[] args2) {
        LiquidInstructionKt.main();
    }
}

