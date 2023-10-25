package joptsimple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import joptsimple.internal.Objects;

public class OptionSet {
  private final List<OptionSpec<?>> detectedSpecs;
  
  private final Map<String, AbstractOptionSpec<?>> detectedOptions;
  
  private final Map<AbstractOptionSpec<?>, List<String>> optionsToArguments;
  
  private final Map<String, AbstractOptionSpec<?>> recognizedSpecs;
  
  private final Map<String, List<?>> defaultValues;
  
  OptionSet(Map<String, AbstractOptionSpec<?>> recognizedSpecs) {
    this.detectedSpecs = new ArrayList<OptionSpec<?>>();
    this.detectedOptions = new HashMap<String, AbstractOptionSpec<?>>();
    this.optionsToArguments = new IdentityHashMap<AbstractOptionSpec<?>, List<String>>();
    this.defaultValues = defaultValues(recognizedSpecs);
    this.recognizedSpecs = recognizedSpecs;
  }
  
  public boolean hasOptions() {
    return !this.detectedOptions.isEmpty();
  }
  
  public boolean has(String option) {
    return this.detectedOptions.containsKey(option);
  }
  
  public boolean has(OptionSpec<?> option) {
    return this.optionsToArguments.containsKey(option);
  }
  
  public boolean hasArgument(String option) {
    AbstractOptionSpec<?> spec = this.detectedOptions.get(option);
    return (spec != null && hasArgument(spec));
  }
  
  public boolean hasArgument(OptionSpec<?> option) {
    Objects.ensureNotNull(option);
    List<String> values = this.optionsToArguments.get(option);
    return (values != null && !values.isEmpty());
  }
  
  public Object valueOf(String option) {
    Objects.ensureNotNull(option);
    AbstractOptionSpec<?> spec = this.detectedOptions.get(option);
    if (spec == null) {
      List<?> defaults = defaultValuesFor(option);
      return defaults.isEmpty() ? null : defaults.get(0);
    } 
    return valueOf(spec);
  }
  
  public <V> V valueOf(OptionSpec<V> option) {
    Objects.ensureNotNull(option);
    List<V> values = valuesOf(option);
    switch (values.size()) {
      case 0:
        return null;
      case 1:
        return values.get(0);
    } 
    throw new MultipleArgumentsForOptionException(option.options());
  }
  
  public List<?> valuesOf(String option) {
    Objects.ensureNotNull(option);
    AbstractOptionSpec<?> spec = this.detectedOptions.get(option);
    return (spec == null) ? defaultValuesFor(option) : valuesOf(spec);
  }
  
  public <V> List<V> valuesOf(OptionSpec<V> option) {
    Objects.ensureNotNull(option);
    List<String> values = this.optionsToArguments.get(option);
    if (values == null || values.isEmpty())
      return defaultValueFor(option); 
    AbstractOptionSpec<V> spec = (AbstractOptionSpec<V>)option;
    List<V> convertedValues = new ArrayList<V>();
    for (String each : values)
      convertedValues.add(spec.convert(each)); 
    return Collections.unmodifiableList(convertedValues);
  }
  
  public List<OptionSpec<?>> specs() {
    List<OptionSpec<?>> specs = this.detectedSpecs;
    specs.remove(this.detectedOptions.get("[arguments]"));
    return Collections.unmodifiableList(specs);
  }
  
  public Map<OptionSpec<?>, List<?>> asMap() {
    Map<OptionSpec<?>, List<?>> map = new HashMap<OptionSpec<?>, List<?>>();
    for (AbstractOptionSpec<?> spec : this.recognizedSpecs.values()) {
      if (!spec.representsNonOptions())
        map.put(spec, valuesOf(spec)); 
    } 
    return Collections.unmodifiableMap(map);
  }
  
  public List<?> nonOptionArguments() {
    return Collections.unmodifiableList(valuesOf(this.detectedOptions.get("[arguments]")));
  }
  
  void add(AbstractOptionSpec<?> spec) {
    addWithArgument(spec, null);
  }
  
  void addWithArgument(AbstractOptionSpec<?> spec, String argument) {
    this.detectedSpecs.add(spec);
    for (String each : spec.options())
      this.detectedOptions.put(each, spec); 
    List<String> optionArguments = this.optionsToArguments.get(spec);
    if (optionArguments == null) {
      optionArguments = new ArrayList<String>();
      this.optionsToArguments.put(spec, optionArguments);
    } 
    if (argument != null)
      optionArguments.add(argument); 
  }
  
  public boolean equals(Object that) {
    if (this == that)
      return true; 
    if (that == null || !getClass().equals(that.getClass()))
      return false; 
    OptionSet other = (OptionSet)that;
    Map<AbstractOptionSpec<?>, List<String>> thisOptionsToArguments = new HashMap<AbstractOptionSpec<?>, List<String>>(this.optionsToArguments);
    Map<AbstractOptionSpec<?>, List<String>> otherOptionsToArguments = new HashMap<AbstractOptionSpec<?>, List<String>>(other.optionsToArguments);
    return (this.detectedOptions.equals(other.detectedOptions) && thisOptionsToArguments.equals(otherOptionsToArguments));
  }
  
  public int hashCode() {
    Map<AbstractOptionSpec<?>, List<String>> thisOptionsToArguments = new HashMap<AbstractOptionSpec<?>, List<String>>(this.optionsToArguments);
    return this.detectedOptions.hashCode() ^ thisOptionsToArguments.hashCode();
  }
  
  private <V> List<V> defaultValuesFor(String option) {
    if (this.defaultValues.containsKey(option))
      return (List<V>)this.defaultValues.get(option); 
    return Collections.emptyList();
  }
  
  private <V> List<V> defaultValueFor(OptionSpec<V> option) {
    return defaultValuesFor(option.options().iterator().next());
  }
  
  private static Map<String, List<?>> defaultValues(Map<String, AbstractOptionSpec<?>> recognizedSpecs) {
    Map<String, List<?>> defaults = new HashMap<String, List<?>>();
    for (Map.Entry<String, AbstractOptionSpec<?>> each : recognizedSpecs.entrySet())
      defaults.put(each.getKey(), ((AbstractOptionSpec)each.getValue()).defaultValues()); 
    return defaults;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\joptsimple\OptionSet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */