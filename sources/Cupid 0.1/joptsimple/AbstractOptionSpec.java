package joptsimple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import joptsimple.internal.Reflection;
import joptsimple.internal.ReflectionException;

abstract class AbstractOptionSpec<V> implements OptionSpec<V>, OptionDescriptor {
  private final List<String> options = new ArrayList<String>();
  
  private final String description;
  
  private boolean forHelp;
  
  protected AbstractOptionSpec(String option) {
    this(Collections.singletonList(option), "");
  }
  
  protected AbstractOptionSpec(Collection<String> options, String description) {
    arrangeOptions(options);
    this.description = description;
  }
  
  public final Collection<String> options() {
    return Collections.unmodifiableList(this.options);
  }
  
  public final List<V> values(OptionSet detectedOptions) {
    return detectedOptions.valuesOf(this);
  }
  
  public final V value(OptionSet detectedOptions) {
    return detectedOptions.valueOf(this);
  }
  
  public String description() {
    return this.description;
  }
  
  public final AbstractOptionSpec<V> forHelp() {
    this.forHelp = true;
    return this;
  }
  
  public final boolean isForHelp() {
    return this.forHelp;
  }
  
  public boolean representsNonOptions() {
    return false;
  }
  
  protected abstract V convert(String paramString);
  
  protected V convertWith(ValueConverter<V> converter, String argument) {
    try {
      return (V)Reflection.convertWith(converter, argument);
    } catch (ReflectionException ex) {
      throw new OptionArgumentConversionException(options(), argument, ex);
    } catch (ValueConversionException ex) {
      throw new OptionArgumentConversionException(options(), argument, ex);
    } 
  }
  
  protected String argumentTypeIndicatorFrom(ValueConverter<V> converter) {
    if (converter == null)
      return null; 
    String pattern = converter.valuePattern();
    return (pattern == null) ? converter.valueType().getName() : pattern;
  }
  
  abstract void handleOption(OptionParser paramOptionParser, ArgumentList paramArgumentList, OptionSet paramOptionSet, String paramString);
  
  private void arrangeOptions(Collection<String> unarranged) {
    if (unarranged.size() == 1) {
      this.options.addAll(unarranged);
      return;
    } 
    List<String> shortOptions = new ArrayList<String>();
    List<String> longOptions = new ArrayList<String>();
    for (String each : unarranged) {
      if (each.length() == 1) {
        shortOptions.add(each);
        continue;
      } 
      longOptions.add(each);
    } 
    Collections.sort(shortOptions);
    Collections.sort(longOptions);
    this.options.addAll(shortOptions);
    this.options.addAll(longOptions);
  }
  
  public boolean equals(Object that) {
    if (!(that instanceof AbstractOptionSpec))
      return false; 
    AbstractOptionSpec<?> other = (AbstractOptionSpec)that;
    return this.options.equals(other.options);
  }
  
  public int hashCode() {
    return this.options.hashCode();
  }
  
  public String toString() {
    return this.options.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\joptsimple\AbstractOptionSpec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */