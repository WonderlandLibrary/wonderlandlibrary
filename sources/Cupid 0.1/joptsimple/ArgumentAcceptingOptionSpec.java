package joptsimple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import joptsimple.internal.Objects;
import joptsimple.internal.Reflection;
import joptsimple.internal.Strings;

public abstract class ArgumentAcceptingOptionSpec<V> extends AbstractOptionSpec<V> {
  private static final char NIL_VALUE_SEPARATOR = '\000';
  
  private boolean optionRequired;
  
  private final boolean argumentRequired;
  
  private ValueConverter<V> converter;
  
  private String argumentDescription = "";
  
  private String valueSeparator = String.valueOf(false);
  
  private final List<V> defaultValues = new ArrayList<V>();
  
  ArgumentAcceptingOptionSpec(String option, boolean argumentRequired) {
    super(option);
    this.argumentRequired = argumentRequired;
  }
  
  ArgumentAcceptingOptionSpec(Collection<String> options, boolean argumentRequired, String description) {
    super(options, description);
    this.argumentRequired = argumentRequired;
  }
  
  public final <T> ArgumentAcceptingOptionSpec<T> ofType(Class<T> argumentType) {
    return withValuesConvertedBy(Reflection.findConverter(argumentType));
  }
  
  public final <T> ArgumentAcceptingOptionSpec<T> withValuesConvertedBy(ValueConverter<T> aConverter) {
    if (aConverter == null)
      throw new NullPointerException("illegal null converter"); 
    this.converter = aConverter;
    return this;
  }
  
  public final ArgumentAcceptingOptionSpec<V> describedAs(String description) {
    this.argumentDescription = description;
    return this;
  }
  
  public final ArgumentAcceptingOptionSpec<V> withValuesSeparatedBy(char separator) {
    if (separator == '\000')
      throw new IllegalArgumentException("cannot use U+0000 as separator"); 
    this.valueSeparator = String.valueOf(separator);
    return this;
  }
  
  public final ArgumentAcceptingOptionSpec<V> withValuesSeparatedBy(String separator) {
    if (separator.indexOf(false) != -1)
      throw new IllegalArgumentException("cannot use U+0000 in separator"); 
    this.valueSeparator = separator;
    return this;
  }
  
  public ArgumentAcceptingOptionSpec<V> defaultsTo(V value, V... values) {
    addDefaultValue(value);
    defaultsTo(values);
    return this;
  }
  
  public ArgumentAcceptingOptionSpec<V> defaultsTo(V[] values) {
    for (V each : values)
      addDefaultValue(each); 
    return this;
  }
  
  public ArgumentAcceptingOptionSpec<V> required() {
    this.optionRequired = true;
    return this;
  }
  
  public boolean isRequired() {
    return this.optionRequired;
  }
  
  private void addDefaultValue(V value) {
    Objects.ensureNotNull(value);
    this.defaultValues.add(value);
  }
  
  final void handleOption(OptionParser parser, ArgumentList arguments, OptionSet detectedOptions, String detectedArgument) {
    if (Strings.isNullOrEmpty(detectedArgument)) {
      detectOptionArgument(parser, arguments, detectedOptions);
    } else {
      addArguments(detectedOptions, detectedArgument);
    } 
  }
  
  protected void addArguments(OptionSet detectedOptions, String detectedArgument) {
    StringTokenizer lexer = new StringTokenizer(detectedArgument, this.valueSeparator);
    if (!lexer.hasMoreTokens()) {
      detectedOptions.addWithArgument(this, detectedArgument);
    } else {
      while (lexer.hasMoreTokens())
        detectedOptions.addWithArgument(this, lexer.nextToken()); 
    } 
  }
  
  protected final V convert(String argument) {
    return convertWith(this.converter, argument);
  }
  
  protected boolean canConvertArgument(String argument) {
    StringTokenizer lexer = new StringTokenizer(argument, this.valueSeparator);
    try {
      while (lexer.hasMoreTokens())
        convert(lexer.nextToken()); 
      return true;
    } catch (OptionException ignored) {
      return false;
    } 
  }
  
  protected boolean isArgumentOfNumberType() {
    return (this.converter != null && Number.class.isAssignableFrom(this.converter.valueType()));
  }
  
  public boolean acceptsArguments() {
    return true;
  }
  
  public boolean requiresArgument() {
    return this.argumentRequired;
  }
  
  public String argumentDescription() {
    return this.argumentDescription;
  }
  
  public String argumentTypeIndicator() {
    return argumentTypeIndicatorFrom(this.converter);
  }
  
  public List<V> defaultValues() {
    return Collections.unmodifiableList(this.defaultValues);
  }
  
  public boolean equals(Object that) {
    if (!super.equals(that))
      return false; 
    ArgumentAcceptingOptionSpec<?> other = (ArgumentAcceptingOptionSpec)that;
    return (requiresArgument() == other.requiresArgument());
  }
  
  public int hashCode() {
    return super.hashCode() ^ (this.argumentRequired ? 0 : 1);
  }
  
  protected abstract void detectOptionArgument(OptionParser paramOptionParser, ArgumentList paramArgumentList, OptionSet paramOptionSet);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\joptsimple\ArgumentAcceptingOptionSpec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */