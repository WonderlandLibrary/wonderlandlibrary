/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.io.TextStreamsKt
 *  kotlin.text.Charsets
 *  kotlin.text.StringsKt
 */
package net.ccbluex.liquidinstruction;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import javax.swing.JFrame;
import javax.swing.JLabel;
import kotlin.io.TextStreamsKt;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;

public final class LiquidInstructionKt {
    public static final void main() {
        JFrame frame = new JFrame("LiquidBounce | Installation");
        frame.setDefaultCloseOperation(3);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        frame.setAlwaysOnTop(true);
        InputStream inputStream = LiquidBounce.class.getResourceAsStream("/instructions.html");
        Charset charset = Charsets.UTF_8;
        boolean bl = false;
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, charset);
        String string = StringsKt.replace$default((String)TextStreamsKt.readText((Reader)inputStreamReader), (String)"{assets}", (String)LiquidBounce.INSTANCE.getClass().getClassLoader().getResource("assets").toString(), (boolean)false, (int)4, null);
        JLabel label = new JLabel(string);
        frame.add((Component)label, "Center");
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static /* synthetic */ void main(String[] stringArray) {
        LiquidInstructionKt.main();
    }
}

