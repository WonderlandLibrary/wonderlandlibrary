/*
 * Decompiled with CFR 0.152.
 */
package joptsimple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import joptsimple.OptionSpec;
import joptsimple.UnrecognizedOptionException;
import joptsimple.internal.Messages;
import joptsimple.internal.Strings;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public abstract class OptionException
extends RuntimeException {
    private static final long serialVersionUID = -1L;
    private final List<String> options = new ArrayList<String>();

    protected OptionException(List<String> options) {
        this.options.addAll(options);
    }

    protected OptionException(Collection<? extends OptionSpec<?>> options) {
        this.options.addAll(this.specsToStrings(options));
    }

    protected OptionException(Collection<? extends OptionSpec<?>> options, Throwable cause) {
        super(cause);
        this.options.addAll(this.specsToStrings(options));
    }

    private List<String> specsToStrings(Collection<? extends OptionSpec<?>> options) {
        ArrayList<String> strings = new ArrayList<String>();
        for (OptionSpec<?> each : options) {
            strings.add(this.specToString(each));
        }
        return strings;
    }

    private String specToString(OptionSpec<?> option) {
        return Strings.join(new ArrayList<String>(option.options()), "/");
    }

    public List<String> options() {
        return Collections.unmodifiableList(this.options);
    }

    protected final String singleOptionString() {
        return this.singleOptionString(this.options.get(0));
    }

    protected final String singleOptionString(String option) {
        return option;
    }

    protected final String multipleOptionString() {
        StringBuilder buffer = new StringBuilder("[");
        LinkedHashSet<String> asSet = new LinkedHashSet<String>(this.options);
        Iterator iter = asSet.iterator();
        while (iter.hasNext()) {
            buffer.append(this.singleOptionString((String)iter.next()));
            if (!iter.hasNext()) continue;
            buffer.append(", ");
        }
        buffer.append(']');
        return buffer.toString();
    }

    static OptionException unrecognizedOption(String option) {
        return new UnrecognizedOptionException(option);
    }

    @Override
    public final String getMessage() {
        return this.localizedMessage(Locale.getDefault());
    }

    final String localizedMessage(Locale locale) {
        return this.formattedMessage(locale);
    }

    private String formattedMessage(Locale locale) {
        return Messages.message(locale, "joptsimple.ExceptionMessages", this.getClass(), "message", this.messageArguments());
    }

    abstract Object[] messageArguments();
}

