/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ScriptRuntime;

public final class ShellFunctions {
    public static final MethodHandle INPUT = ShellFunctions.findOwnMH("input", Object.class, Object.class, Object.class, Object.class);
    public static final MethodHandle EVALINPUT = ShellFunctions.findOwnMH("evalinput", Object.class, Object.class, Object.class, Object.class);

    private ShellFunctions() {
    }

    public static Object input(Object self, Object endMarker, Object prompt) throws IOException {
        String endMarkerStr = endMarker != ScriptRuntime.UNDEFINED ? JSType.toString(endMarker) : "";
        String promptStr = prompt != ScriptRuntime.UNDEFINED ? JSType.toString(prompt) : ">> ";
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder buf = new StringBuilder();
        while (true) {
            System.out.print(promptStr);
            String line = reader.readLine();
            if (line == null || line.equals(endMarkerStr)) break;
            buf.append(line);
            buf.append('\n');
        }
        return buf.toString();
    }

    public static Object evalinput(Object self, Object endMarker, Object prompt) throws IOException {
        return Global.eval(self, ShellFunctions.input(self, endMarker, prompt));
    }

    private static MethodHandle findOwnMH(String name, Class<?> rtype, Class<?> ... types) {
        return Lookup.MH.findStatic(MethodHandles.lookup(), ShellFunctions.class, name, Lookup.MH.type(rtype, types));
    }
}

