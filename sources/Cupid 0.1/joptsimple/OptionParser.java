package joptsimple;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import joptsimple.internal.AbbreviationMap;
import joptsimple.util.KeyValuePair;

public class OptionParser implements OptionDeclarer {
  private final AbbreviationMap<AbstractOptionSpec<?>> recognizedOptions;
  
  private final Map<Collection<String>, Set<OptionSpec<?>>> requiredIf;
  
  private final Map<Collection<String>, Set<OptionSpec<?>>> requiredUnless;
  
  private OptionParserState state;
  
  private boolean posixlyCorrect;
  
  private boolean allowsUnrecognizedOptions;
  
  private HelpFormatter helpFormatter = new BuiltinHelpFormatter();
  
  public OptionParser() {
    this.recognizedOptions = new AbbreviationMap();
    this.requiredIf = new HashMap<Collection<String>, Set<OptionSpec<?>>>();
    this.requiredUnless = new HashMap<Collection<String>, Set<OptionSpec<?>>>();
    this.state = OptionParserState.moreOptions(false);
    recognize(new NonOptionArgumentSpec());
  }
  
  public OptionParser(String optionSpecification) {
    this();
    (new OptionSpecTokenizer(optionSpecification)).configure(this);
  }
  
  public OptionSpecBuilder accepts(String option) {
    return acceptsAll(Collections.singletonList(option));
  }
  
  public OptionSpecBuilder accepts(String option, String description) {
    return acceptsAll(Collections.singletonList(option), description);
  }
  
  public OptionSpecBuilder acceptsAll(Collection<String> options) {
    return acceptsAll(options, "");
  }
  
  public OptionSpecBuilder acceptsAll(Collection<String> options, String description) {
    if (options.isEmpty())
      throw new IllegalArgumentException("need at least one option"); 
    ParserRules.ensureLegalOptions(options);
    return new OptionSpecBuilder(this, options, description);
  }
  
  public NonOptionArgumentSpec<String> nonOptions() {
    NonOptionArgumentSpec<String> spec = new NonOptionArgumentSpec<String>();
    recognize(spec);
    return spec;
  }
  
  public NonOptionArgumentSpec<String> nonOptions(String description) {
    NonOptionArgumentSpec<String> spec = new NonOptionArgumentSpec<String>(description);
    recognize(spec);
    return spec;
  }
  
  public void posixlyCorrect(boolean setting) {
    this.posixlyCorrect = setting;
    this.state = OptionParserState.moreOptions(setting);
  }
  
  boolean posixlyCorrect() {
    return this.posixlyCorrect;
  }
  
  public void allowsUnrecognizedOptions() {
    this.allowsUnrecognizedOptions = true;
  }
  
  boolean doesAllowsUnrecognizedOptions() {
    return this.allowsUnrecognizedOptions;
  }
  
  public void recognizeAlternativeLongOptions(boolean recognize) {
    if (recognize) {
      recognize(new AlternativeLongOptionSpec());
    } else {
      this.recognizedOptions.remove(String.valueOf("W"));
    } 
  }
  
  void recognize(AbstractOptionSpec<?> spec) {
    this.recognizedOptions.putAll(spec.options(), spec);
  }
  
  public void printHelpOn(OutputStream sink) throws IOException {
    printHelpOn(new OutputStreamWriter(sink));
  }
  
  public void printHelpOn(Writer sink) throws IOException {
    sink.write(this.helpFormatter.format(this.recognizedOptions.toJavaUtilMap()));
    sink.flush();
  }
  
  public void formatHelpWith(HelpFormatter formatter) {
    if (formatter == null)
      throw new NullPointerException(); 
    this.helpFormatter = formatter;
  }
  
  public Map<String, OptionSpec<?>> recognizedOptions() {
    return new HashMap<String, OptionSpec<?>>(this.recognizedOptions.toJavaUtilMap());
  }
  
  public OptionSet parse(String... arguments) {
    ArgumentList argumentList = new ArgumentList(arguments);
    OptionSet detected = new OptionSet(this.recognizedOptions.toJavaUtilMap());
    detected.add((AbstractOptionSpec)this.recognizedOptions.get("[arguments]"));
    while (argumentList.hasMore())
      this.state.handleArgument(this, argumentList, detected); 
    reset();
    ensureRequiredOptions(detected);
    return detected;
  }
  
  private void ensureRequiredOptions(OptionSet options) {
    Collection<String> missingRequiredOptions = missingRequiredOptions(options);
    boolean helpOptionPresent = isHelpOptionPresent(options);
    if (!missingRequiredOptions.isEmpty() && !helpOptionPresent)
      throw new MissingRequiredOptionException(missingRequiredOptions); 
  }
  
  private Collection<String> missingRequiredOptions(OptionSet options) {
    Collection<String> missingRequiredOptions = new HashSet<String>();
    for (AbstractOptionSpec<?> each : (Iterable<AbstractOptionSpec<?>>)this.recognizedOptions.toJavaUtilMap().values()) {
      if (each.isRequired() && !options.has(each))
        missingRequiredOptions.addAll(each.options()); 
    } 
    for (Map.Entry<Collection<String>, Set<OptionSpec<?>>> eachEntry : this.requiredIf.entrySet()) {
      AbstractOptionSpec<?> required = specFor(((Collection<String>)eachEntry.getKey()).iterator().next());
      if (optionsHasAnyOf(options, eachEntry.getValue()) && !options.has(required))
        missingRequiredOptions.addAll(required.options()); 
    } 
    for (Map.Entry<Collection<String>, Set<OptionSpec<?>>> eachEntry : this.requiredUnless.entrySet()) {
      AbstractOptionSpec<?> required = specFor(((Collection<String>)eachEntry.getKey()).iterator().next());
      if (!optionsHasAnyOf(options, eachEntry.getValue()) && !options.has(required))
        missingRequiredOptions.addAll(required.options()); 
    } 
    return missingRequiredOptions;
  }
  
  private boolean optionsHasAnyOf(OptionSet options, Collection<OptionSpec<?>> specs) {
    for (OptionSpec<?> each : specs) {
      if (options.has(each))
        return true; 
    } 
    return false;
  }
  
  private boolean isHelpOptionPresent(OptionSet options) {
    boolean helpOptionPresent = false;
    for (AbstractOptionSpec<?> each : (Iterable<AbstractOptionSpec<?>>)this.recognizedOptions.toJavaUtilMap().values()) {
      if (each.isForHelp() && options.has(each)) {
        helpOptionPresent = true;
        break;
      } 
    } 
    return helpOptionPresent;
  }
  
  void handleLongOptionToken(String candidate, ArgumentList arguments, OptionSet detected) {
    KeyValuePair optionAndArgument = parseLongOptionWithArgument(candidate);
    if (!isRecognized(optionAndArgument.key))
      throw OptionException.unrecognizedOption(optionAndArgument.key); 
    AbstractOptionSpec<?> optionSpec = specFor(optionAndArgument.key);
    optionSpec.handleOption(this, arguments, detected, optionAndArgument.value);
  }
  
  void handleShortOptionToken(String candidate, ArgumentList arguments, OptionSet detected) {
    KeyValuePair optionAndArgument = parseShortOptionWithArgument(candidate);
    if (isRecognized(optionAndArgument.key)) {
      specFor(optionAndArgument.key).handleOption(this, arguments, detected, optionAndArgument.value);
    } else {
      handleShortOptionCluster(candidate, arguments, detected);
    } 
  }
  
  private void handleShortOptionCluster(String candidate, ArgumentList arguments, OptionSet detected) {
    char[] options = extractShortOptionsFrom(candidate);
    validateOptionCharacters(options);
    for (int i = 0; i < options.length; i++) {
      AbstractOptionSpec<?> optionSpec = specFor(options[i]);
      if (optionSpec.acceptsArguments() && options.length > i + 1) {
        String detectedArgument = String.valueOf(options, i + 1, options.length - 1 - i);
        optionSpec.handleOption(this, arguments, detected, detectedArgument);
        break;
      } 
      optionSpec.handleOption(this, arguments, detected, (String)null);
    } 
  }
  
  void handleNonOptionArgument(String candidate, ArgumentList arguments, OptionSet detectedOptions) {
    specFor("[arguments]").handleOption(this, arguments, detectedOptions, candidate);
  }
  
  void noMoreOptions() {
    this.state = OptionParserState.noMoreOptions();
  }
  
  boolean looksLikeAnOption(String argument) {
    return (ParserRules.isShortOptionToken(argument) || ParserRules.isLongOptionToken(argument));
  }
  
  boolean isRecognized(String option) {
    return this.recognizedOptions.contains(option);
  }
  
  void requiredIf(Collection<String> precedentSynonyms, String required) {
    requiredIf(precedentSynonyms, specFor(required));
  }
  
  void requiredIf(Collection<String> precedentSynonyms, OptionSpec<?> required) {
    putRequiredOption(precedentSynonyms, required, this.requiredIf);
  }
  
  void requiredUnless(Collection<String> precedentSynonyms, String required) {
    requiredUnless(precedentSynonyms, specFor(required));
  }
  
  void requiredUnless(Collection<String> precedentSynonyms, OptionSpec<?> required) {
    putRequiredOption(precedentSynonyms, required, this.requiredUnless);
  }
  
  private void putRequiredOption(Collection<String> precedentSynonyms, OptionSpec<?> required, Map<Collection<String>, Set<OptionSpec<?>>> target) {
    for (String each : precedentSynonyms) {
      AbstractOptionSpec<?> spec = specFor(each);
      if (spec == null)
        throw new UnconfiguredOptionException(precedentSynonyms); 
    } 
    Set<OptionSpec<?>> associated = target.get(precedentSynonyms);
    if (associated == null) {
      associated = new HashSet<OptionSpec<?>>();
      target.put(precedentSynonyms, associated);
    } 
    associated.add(required);
  }
  
  private AbstractOptionSpec<?> specFor(char option) {
    return specFor(String.valueOf(option));
  }
  
  private AbstractOptionSpec<?> specFor(String option) {
    return (AbstractOptionSpec)this.recognizedOptions.get(option);
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
      if (!isRecognized(option))
        throw OptionException.unrecognizedOption(option); 
      if (specFor(option).acceptsArguments())
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


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\joptsimple\OptionParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */