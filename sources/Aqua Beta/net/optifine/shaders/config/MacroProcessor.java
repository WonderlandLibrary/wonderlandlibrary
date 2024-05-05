package net.optifine.shaders.config;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import net.minecraft.src.Config;
import net.optifine.shaders.Shaders;
import net.optifine.shaders.config.MacroState;
import net.optifine.shaders.config.ShaderMacro;
import net.optifine.shaders.config.ShaderMacros;
import net.optifine.shaders.config.ShaderOption;

public class MacroProcessor {
    public static InputStream process(InputStream in, String path) throws IOException {
        String s = Config.readInputStream((InputStream)in, (String)"ASCII");
        String s1 = MacroProcessor.getMacroHeader(s);
        if (!s1.isEmpty()) {
            s = s1 + s;
            if (Shaders.saveFinalShaders) {
                String s2 = path.replace(':', '/') + ".pre";
                Shaders.saveShader((String)s2, (String)s);
            }
            s = MacroProcessor.process(s);
        }
        if (Shaders.saveFinalShaders) {
            String s3 = path.replace(':', '/');
            Shaders.saveShader((String)s3, (String)s);
        }
        byte[] abyte = s.getBytes("ASCII");
        ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(abyte);
        return bytearrayinputstream;
    }

    public static String process(String strIn) throws IOException {
        StringReader stringreader = new StringReader(strIn);
        BufferedReader bufferedreader = new BufferedReader((Reader)stringreader);
        MacroState macrostate = new MacroState();
        StringBuilder stringbuilder = new StringBuilder();
        while (true) {
            String s;
            if ((s = bufferedreader.readLine()) == null) {
                s = stringbuilder.toString();
                return s;
            }
            if (!macrostate.processLine(s) || MacroState.isMacroLine((String)s)) continue;
            stringbuilder.append(s);
            stringbuilder.append("\n");
        }
    }

    /*
     * Unable to fully structure code
     */
    private static String getMacroHeader(String str) throws IOException {
        stringbuilder = new StringBuilder();
        list = null;
        list1 = null;
        stringreader = new StringReader(str);
        bufferedreader = new BufferedReader((Reader)stringreader);
        block0: while (true) {
            if ((s = bufferedreader.readLine()) == null) {
                return stringbuilder.toString();
            }
            if (!MacroState.isMacroLine((String)s)) continue;
            if (stringbuilder.length() == 0) {
                stringbuilder.append(ShaderMacros.getFixedMacroLines());
            }
            if (list1 == null) {
                list1 = new ArrayList((Collection)Arrays.asList((Object[])ShaderMacros.getExtensions()));
            }
            iterator = list1.iterator();
            while (true) {
                if (iterator.hasNext()) ** break;
                continue block0;
                shadermacro = (ShaderMacro)iterator.next();
                if (!s.contains((CharSequence)shadermacro.getName())) continue;
                stringbuilder.append(shadermacro.getSourceLine());
                stringbuilder.append("\n");
                iterator.remove();
            }
            break;
        }
    }

    private static List<ShaderOption> getMacroOptions() {
        ArrayList list = new ArrayList();
        ShaderOption[] ashaderoption = Shaders.getShaderPackOptions();
        for (int i = 0; i < ashaderoption.length; ++i) {
            ShaderOption shaderoption = ashaderoption[i];
            String s = shaderoption.getSourceLine();
            if (s == null || !s.startsWith("#")) continue;
            list.add((Object)shaderoption);
        }
        return list;
    }
}
