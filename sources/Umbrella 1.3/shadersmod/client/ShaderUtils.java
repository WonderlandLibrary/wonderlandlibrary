/*
 * Decompiled with CFR 0.150.
 */
package shadersmod.client;

import optifine.Config;
import shadersmod.client.ShaderOption;
import shadersmod.client.ShaderProfile;

public class ShaderUtils {
    public static ShaderOption getShaderOption(String name, ShaderOption[] opts) {
        if (opts == null) {
            return null;
        }
        for (int i = 0; i < opts.length; ++i) {
            ShaderOption so = opts[i];
            if (!so.getName().equals(name)) continue;
            return so;
        }
        return null;
    }

    public static ShaderProfile detectProfile(ShaderProfile[] profs, ShaderOption[] opts, boolean def) {
        if (profs == null) {
            return null;
        }
        for (int i = 0; i < profs.length; ++i) {
            ShaderProfile prof = profs[i];
            if (!ShaderUtils.matchProfile(prof, opts, def)) continue;
            return prof;
        }
        return null;
    }

    public static boolean matchProfile(ShaderProfile prof, ShaderOption[] opts, boolean def) {
        if (prof == null) {
            return false;
        }
        if (opts == null) {
            return false;
        }
        String[] optsProf = prof.getOptions();
        for (int p = 0; p < optsProf.length; ++p) {
            String profVal;
            String optVal;
            String opt = optsProf[p];
            ShaderOption so = ShaderUtils.getShaderOption(opt, opts);
            if (so == null || Config.equals(optVal = def ? so.getValueDefault() : so.getValue(), profVal = prof.getValue(opt))) continue;
            return false;
        }
        return true;
    }
}

