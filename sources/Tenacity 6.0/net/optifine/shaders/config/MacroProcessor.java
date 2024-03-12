// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.shaders.config;

import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.StringReader;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import net.optifine.shaders.Shaders;
import net.minecraft.src.Config;
import java.io.InputStream;

public class MacroProcessor
{
    public static InputStream process(final InputStream in, final String path) throws IOException {
        String s = Config.readInputStream(in, "ASCII");
        final String s2 = getMacroHeader(s);
        if (!s2.isEmpty()) {
            s = s2 + s;
            if (Shaders.saveFinalShaders) {
                final String s3 = path.replace(':', '/') + ".pre";
                Shaders.saveShader(s3, s);
            }
            s = process(s);
        }
        if (Shaders.saveFinalShaders) {
            final String s4 = path.replace(':', '/');
            Shaders.saveShader(s4, s);
        }
        final byte[] abyte = s.getBytes("ASCII");
        final ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(abyte);
        return bytearrayinputstream;
    }
    
    public static String process(final String strIn) throws IOException {
        final StringReader stringreader = new StringReader(strIn);
        final BufferedReader bufferedreader = new BufferedReader(stringreader);
        final MacroState macrostate = new MacroState();
        final StringBuilder stringbuilder = new StringBuilder();
        while (true) {
            final String s = bufferedreader.readLine();
            if (s == null) {
                break;
            }
            if (!macrostate.processLine(s) || MacroState.isMacroLine(s)) {
                continue;
            }
            stringbuilder.append(s);
            stringbuilder.append("\n");
        }
        final String s = stringbuilder.toString();
        return s;
    }
    
    private static String getMacroHeader(final String str) throws IOException {
        final StringBuilder stringbuilder = new StringBuilder();
        final List<ShaderOption> list = null;
        List<ShaderMacro> list2 = null;
        final StringReader stringreader = new StringReader(str);
        final BufferedReader bufferedreader = new BufferedReader(stringreader);
        while (true) {
            final String s = bufferedreader.readLine();
            if (s == null) {
                break;
            }
            if (!MacroState.isMacroLine(s)) {
                continue;
            }
            if (stringbuilder.length() == 0) {
                stringbuilder.append(ShaderMacros.getFixedMacroLines());
            }
            if (list2 == null) {
                list2 = new ArrayList<ShaderMacro>(Arrays.asList(ShaderMacros.getExtensions()));
            }
            final Iterator iterator = list2.iterator();
            while (iterator.hasNext()) {
                final ShaderMacro shadermacro = iterator.next();
                if (s.contains(shadermacro.getName())) {
                    stringbuilder.append(shadermacro.getSourceLine());
                    stringbuilder.append("\n");
                    iterator.remove();
                }
            }
        }
        return stringbuilder.toString();
    }
    
    private static List<ShaderOption> getMacroOptions() {
        final List<ShaderOption> list = new ArrayList<ShaderOption>();
        final ShaderOption[] ashaderoption = Shaders.getShaderPackOptions();
        for (int i = 0; i < ashaderoption.length; ++i) {
            final ShaderOption shaderoption = ashaderoption[i];
            final String s = shaderoption.getSourceLine();
            if (s != null && s.startsWith("#")) {
                list.add(shaderoption);
            }
        }
        return list;
    }
}
