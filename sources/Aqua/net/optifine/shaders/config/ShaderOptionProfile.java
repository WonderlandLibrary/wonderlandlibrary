package net.optifine.shaders.config;

import java.util.ArrayList;
import net.optifine.Lang;
import net.optifine.shaders.ShaderUtils;
import net.optifine.shaders.Shaders;
import net.optifine.shaders.config.ShaderOption;
import net.optifine.shaders.config.ShaderProfile;

public class ShaderOptionProfile
extends ShaderOption {
    private ShaderProfile[] profiles = null;
    private ShaderOption[] options = null;
    private static final String NAME_PROFILE = "<profile>";
    private static final String VALUE_CUSTOM = "<custom>";

    public ShaderOptionProfile(ShaderProfile[] profiles, ShaderOption[] options) {
        super(NAME_PROFILE, "", ShaderOptionProfile.detectProfileName(profiles, options), ShaderOptionProfile.getProfileNames(profiles), ShaderOptionProfile.detectProfileName(profiles, options, true), (String)null);
        this.profiles = profiles;
        this.options = options;
    }

    public void nextValue() {
        super.nextValue();
        if (this.getValue().equals((Object)VALUE_CUSTOM)) {
            super.nextValue();
        }
        this.applyProfileOptions();
    }

    public void updateProfile() {
        ShaderProfile shaderprofile = this.getProfile(this.getValue());
        if (shaderprofile == null || !ShaderUtils.matchProfile((ShaderProfile)shaderprofile, (ShaderOption[])this.options, (boolean)false)) {
            String s = ShaderOptionProfile.detectProfileName(this.profiles, this.options);
            this.setValue(s);
        }
    }

    private void applyProfileOptions() {
        ShaderProfile shaderprofile = this.getProfile(this.getValue());
        if (shaderprofile != null) {
            String[] astring = shaderprofile.getOptions();
            for (int i = 0; i < astring.length; ++i) {
                String s = astring[i];
                ShaderOption shaderoption = this.getOption(s);
                if (shaderoption == null) continue;
                String s1 = shaderprofile.getValue(s);
                shaderoption.setValue(s1);
            }
        }
    }

    private ShaderOption getOption(String name) {
        for (int i = 0; i < this.options.length; ++i) {
            ShaderOption shaderoption = this.options[i];
            if (!shaderoption.getName().equals((Object)name)) continue;
            return shaderoption;
        }
        return null;
    }

    private ShaderProfile getProfile(String name) {
        for (int i = 0; i < this.profiles.length; ++i) {
            ShaderProfile shaderprofile = this.profiles[i];
            if (!shaderprofile.getName().equals((Object)name)) continue;
            return shaderprofile;
        }
        return null;
    }

    public String getNameText() {
        return Lang.get((String)"of.shaders.profile");
    }

    public String getValueText(String val) {
        return val.equals((Object)VALUE_CUSTOM) ? Lang.get((String)"of.general.custom", (String)VALUE_CUSTOM) : Shaders.translate((String)("profile." + val), (String)val);
    }

    public String getValueColor(String val) {
        return val.equals((Object)VALUE_CUSTOM) ? "\u00a7c" : "\u00a7a";
    }

    public String getDescriptionText() {
        String s = Shaders.translate((String)"profile.comment", (String)null);
        if (s != null) {
            return s;
        }
        StringBuffer stringbuffer = new StringBuffer();
        for (int i = 0; i < this.profiles.length; ++i) {
            String s2;
            String s1 = this.profiles[i].getName();
            if (s1 == null || (s2 = Shaders.translate((String)("profile." + s1 + ".comment"), (String)null)) == null) continue;
            stringbuffer.append(s2);
            if (s2.endsWith(". ")) continue;
            stringbuffer.append(". ");
        }
        return stringbuffer.toString();
    }

    private static String detectProfileName(ShaderProfile[] profs, ShaderOption[] opts) {
        return ShaderOptionProfile.detectProfileName(profs, opts, false);
    }

    private static String detectProfileName(ShaderProfile[] profs, ShaderOption[] opts, boolean def) {
        ShaderProfile shaderprofile = ShaderUtils.detectProfile((ShaderProfile[])profs, (ShaderOption[])opts, (boolean)def);
        return shaderprofile == null ? VALUE_CUSTOM : shaderprofile.getName();
    }

    private static String[] getProfileNames(ShaderProfile[] profs) {
        ArrayList list = new ArrayList();
        for (int i = 0; i < profs.length; ++i) {
            ShaderProfile shaderprofile = profs[i];
            list.add((Object)shaderprofile.getName());
        }
        list.add((Object)VALUE_CUSTOM);
        String[] astring = (String[])list.toArray((Object[])new String[list.size()]);
        return astring;
    }
}
