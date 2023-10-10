/*
 * Decompiled with CFR 0.152.
 */
package joptsimple;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import joptsimple.AbstractOptionSpec;
import joptsimple.AlternativeLongOptionSpec;
import joptsimple.ArgumentList;
import joptsimple.BuiltinHelpFormatter;
import joptsimple.HelpFormatter;
import joptsimple.MissingRequiredOptionsException;
import joptsimple.NonOptionArgumentSpec;
import joptsimple.OptionDeclarer;
import joptsimple.OptionException;
import joptsimple.OptionParserState;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import joptsimple.OptionSpecBuilder;
import joptsimple.OptionSpecTokenizer;
import joptsimple.ParserRules;
import joptsimple.UnconfiguredOptionException;
import joptsimple.internal.AbbreviationMap;
import joptsimple.util.KeyValuePair;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class OptionParser
implements OptionDeclarer {
    private final AbbreviationMap<AbstractOptionSpec<?>> recognizedOptions;
    private final ArrayList<AbstractOptionSpec<?>> trainingOrder;
    private final Map<List<String>, Set<OptionSpec<?>>> requiredIf;
    private final Map<List<String>, Set<OptionSpec<?>>> requiredUnless;
    private OptionParserState state;
    private boolean posixlyCorrect;
    private boolean allowsUnrecognizedOptions;
    private HelpFormatter helpFormatter = new BuiltinHelpFormatter();

    public OptionParser() {
        this.recognizedOptions = new AbbreviationMap();
        this.trainingOrder = new ArrayList();
        this.requiredIf = new HashMap();
        this.requiredUnless = new HashMap();
        this.state = OptionParserState.moreOptions(false);
        this.recognize(new NonOptionArgumentSpec());
    }

    public OptionParser(String optionSpecification) {
        this();
        new OptionSpecTokenizer(optionSpecification).configure(this);
    }

    @Override
    public OptionSpecBuilder accepts(String option) {
        return this.acceptsAll(Collections.singletonList(option));
    }

    @Override
    public OptionSpecBuilder accepts(String option, String description) {
        return this.acceptsAll(Collections.singletonList(option), description);
    }

    @Override
    public OptionSpecBuilder acceptsAll(List<String> options) {
        return this.acceptsAll(options, "");
    }

    @Override
    public OptionSpecBuilder acceptsAll(List<String> options, String description) {
        if (options.isEmpty()) {
            throw new IllegalArgumentException("need at least one option");
        }
        ParserRules.ensureLegalOptions(options);
        return new OptionSpecBuilder(this, options, description);
    }

    @Override
    public NonOptionArgumentSpec<String> nonOptions() {
        NonOptionArgumentSpec<String> spec = new NonOptionArgumentSpec<String>();
        this.recognize(spec);
        return spec;
    }

    @Override
    public NonOptionArgumentSpec<String> nonOptions(String description) {
        NonOptionArgumentSpec<String> spec = new NonOptionArgumentSpec<String>(description);
        this.recognize(spec);
        return spec;
    }

    @Override
    public void posixlyCorrect(boolean setting) {
        this.posixlyCorrect = setting;
        this.state = OptionParserState.moreOptions(setting);
    }

    boolean posixlyCorrect() {
        return this.posixlyCorrect;
    }

    @Override
    public void allowsUnrecognizedOptions() {
        this.allowsUnrecognizedOptions = true;
    }

    boolean doesAllowsUnrecognizedOptions() {
        return this.allowsUnrecognizedOptions;
    }

    @Override
    public void recognizeAlternativeLongOptions(boolean recognize) {
        if (recognize) {
            this.recognize(new AlternativeLongOptionSpec());
        } else {
            this.recognizedOptions.remove(String.valueOf("W"));
        }
    }

    void recognize(AbstractOptionSpec<?> spec) {
        this.recognizedOptions.putAll(spec.options(), spec);
        this.trainingOrder.add(spec);
    }

    public void printHelpOn(OutputStream sink) throws IOException {
        this.printHelpOn(new OutputStreamWriter(sink));
    }

    public void printHelpOn(Writer sink) throws IOException {
        sink.write(this.helpFormatter.format(this._recognizedOptions()));
        sink.flush();
    }

    public void formatHelpWith(HelpFormatter formatter) {
        if (formatter == null) {
            throw new NullPointerException();
        }
        this.helpFormatter = formatter;
    }

    public Map<String, OptionSpec<?>> recognizedOptions() {
        return new LinkedHashMap(this._recognizedOptions());
    }

    private Map<String, AbstractOptionSpec<?>> _recognizedOptions() {
        LinkedHashMap options = new LinkedHashMap();
        for (AbstractOptionSpec<?> spec : this.trainingOrder) {
            for (String option : spec.options()) {
                options.put(option, spec);
            }
        }
        return options;
    }

    public OptionSet parse(String ... arguments) {
        ArgumentList argumentList = new ArgumentList(arguments);
        OptionSet detected = new OptionSet(this.recognizedOptions.toJavaUtilMap());
        detected.add(this.recognizedOptions.get("[arguments]"));
        while (argumentList.hasMore()) {
            this.state.handleArgument(this, argumentList, detected);
        }
        this.reset();
        this.ensureRequiredOptions(detected);
        return detected;
    }

    private void ensureRequiredOptions(OptionSet options) {
        List<AbstractOptionSpec<?>> missingRequiredOptions = this.missingRequiredOptions(options);
        boolean helpOptionPresent = this.isHelpOptionPresent(options);
        if (!missingRequiredOptions.isEmpty() && !helpOptionPresent) {
            throw new MissingRequiredOptionsException((List<? extends OptionSpec<?>>)missingRequiredOptions);
        }
    }

    private List<AbstractOptionSpec<?>> missingRequiredOptions(OptionSet options) {
        AbstractOptionSpec<?> required;
        ArrayList missingRequiredOptions = new ArrayList();
        for (AbstractOptionSpec<?> abstractOptionSpec : this.recognizedOptions.toJavaUtilMap().values()) {
            if (!abstractOptionSpec.isRequired() || options.has(abstractOptionSpec)) continue;
            missingRequiredOptions.add(abstractOptionSpec);
        }
        for (Map.Entry entry : this.requiredIf.entrySet()) {
            required = this.specFor((String)((List)entry.getKey()).iterator().next());
            if (!this.optionsHasAnyOf(options, (Collection)entry.getValue()) || options.has(required)) continue;
            missingRequiredOptions.add(required);
        }
        for (Map.Entry entry : this.requiredUnless.entrySet()) {
            required = this.specFor((String)((List)entry.getKey()).iterator().next());
            if (this.optionsHasAnyOf(options, (Collection)entry.getValue()) || options.has(required)) continue;
            missingRequiredOptions.add(required);
        }
        return missingRequiredOptions;
    }

    private boolean optionsHasAnyOf(OptionSet options, Collection<OptionSpec<?>> specs) {
        for (OptionSpec<?> each : specs) {
            if (!options.has(each)) continue;
            return true;
        }
        return false;
    }

    private boolean isHelpOptionPresent(OptionSet options) {
        boolean helpOptionPresent = false;
        for (AbstractOptionSpec<?> each : this.recognizedOptions.toJavaUtilMap().values()) {
            if (!each.isForHelp() || !options.has(each)) continue;
            helpOptionPresent = true;
            break;
        }
        return helpOptionPresent;
    }

    void handleLongOptionToken(String candidate, ArgumentList arguments, OptionSet detected) {
        KeyValuePair optionAndArgument = OptionParser.parseLongOptionWithArgument(candidate);
        if (!this.isRecognized(optionAndArgument.key)) {
            throw OptionException.unrecognizedOption(optionAndArgument.key);
        }
        AbstractOptionSpec<?> optionSpec = this.specFor(optionAndArgument.key);
        optionSpec.handleOption(this, arguments, detected, optionAndArgument.value);
    }

    void handleShortOptionToken(String candidate, ArgumentList arguments, OptionSet detected) {
        KeyValuePair optionAndArgument = OptionParser.parseShortOptionWithArgument(candidate);
        if (this.isRecognized(optionAndArgument.key)) {
            this.specFor(optionAndArgument.key).handleOption(this, arguments, detected, optionAndArgument.value);
        } else {
            this.handleShortOptionCluster(candidate, arguments, detected);
        }
    }

    private void handleShortOptionCluster(String candidate, ArgumentList arguments, OptionSet detected) {
        char[] options = OptionParser.extractShortOptionsFrom(candidate);
        this.validateOptionCharacters(options);
        for (int i2 = 0; i2 < options.length; ++i2) {
            AbstractOptionSpec<?> optionSpec = this.specFor(options[i2]);
            if (optionSpec.acceptsArguments() && options.length > i2 + 1) {
                String detectedArgument = String.valueOf(options, i2 + 1, options.length - 1 - i2);
                optionSpec.handleOption(this, arguments, detected, detectedArgument);
                break;
            }
            optionSpec.handleOption(this, arguments, detected, null);
        }
    }

    void handleNonOptionArgument(String candidate, ArgumentList arguments, OptionSet detectedOptions) {
        this.specFor("[arguments]").handleOption(this, arguments, detectedOptions, candidate);
    }

    void noMoreOptions() {
        this.state = OptionParserState.noMoreOptions();
    }

    boolean looksLikeAnOption(String argument) {
        return ParserRules.isShortOptionToken(argument) || ParserRules.isLongOptionToken(argument);
    }

    boolean isRecognized(String option) {
        return this.recognizedOptions.contains(option);
    }

    void requiredIf(List<String> precedentSynonyms, String required) {
        this.requiredIf(precedentSynonyms, this.specFor(required));
    }

    void requiredIf(List<String> precedentSynonyms, OptionSpec<?> required) {
        this.putRequiredOption(precedentSynonyms, required, this.requiredIf);
    }

    void requiredUnless(List<String> precedentSynonyms, String required) {
        this.requiredUnless(precedentSynonyms, this.specFor(required));
    }

    void requiredUnless(List<String> precedentSynonyms, OptionSpec<?> required) {
        this.putRequiredOption(precedentSynonyms, required, this.requiredUnless);
    }

    private void putRequiredOption(List<String> precedentSynonyms, OptionSpec<?> required, Map<List<String>, Set<OptionSpec<?>>> target) {
        for (String each : precedentSynonyms) {
            AbstractOptionSpec<?> spec = this.specFor(each);
            if (spec != null) continue;
            throw new UnconfiguredOptionException(precedentSynonyms);
        }
        Set<OptionSpec<?>> associated = target.get(precedentSynonyms);
        if (associated == null) {
            associated = new HashSet();
            target.put(precedentSynonyms, associated);
        }
        associated.add(required);
    }

    private AbstractOptionSpec<?> specFor(char option) {
        return this.specFor(String.valueOf(option));
    }

    private AbstractOptionSpec<?> specFor(String option) {
        return this.recognizedOptions.get(option);
    }

    private void reset() {
        this.state = OptionParserState.moreOptions(this.posixlyCorrect);
    }

    private static char[] extractShortOptionsFrom(String argument) {
        char[] options = new char[argument.length() - 1];
        argument.getChars(1, argument.length(), options, 0);
        return options;
    }

    private void validateOptionCharacters(char[] options) {
        for (char each : options) {
            String option = String.valueOf(each);
            if (!this.isRecognized(option)) {
                throw OptionException.unrecognizedOption(option);
            }
            if (!this.specFor(option).acceptsArguments()) continue;
            return;
        }
    }

    private static KeyValuePair parseLongOptionWithArgument(String argument) {
        return KeyValuePair.valueOf(argument.substring(2));
    }

    private static KeyValuePair parseShortOptionWithArgument(String argument) {
        return KeyValuePair.valueOf(argument.substring(1));
    }
}

