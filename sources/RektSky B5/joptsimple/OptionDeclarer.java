/*
 * Decompiled with CFR 0.152.
 */
package joptsimple;

import java.util.List;
import joptsimple.NonOptionArgumentSpec;
import joptsimple.OptionSpecBuilder;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public interface OptionDeclarer {
    public OptionSpecBuilder accepts(String var1);

    public OptionSpecBuilder accepts(String var1, String var2);

    public OptionSpecBuilder acceptsAll(List<String> var1);

    public OptionSpecBuilder acceptsAll(List<String> var1, String var2);

    public NonOptionArgumentSpec<String> nonOptions();

    public NonOptionArgumentSpec<String> nonOptions(String var1);

    public void posixlyCorrect(boolean var1);

    public void allowsUnrecognizedOptions();

    public void recognizeAlternativeLongOptions(boolean var1);
}

