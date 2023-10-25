package joptsimple;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import joptsimple.internal.Reflection;

public class NonOptionArgumentSpec<V> extends AbstractOptionSpec<V> {
  static final String NAME = "[arguments]";
  
  private ValueConverter<V> converter;
  
  private String argumentDescription = "";
  
  NonOptionArgumentSpec() {
    this("");
  }
  
  NonOptionArgumentSpec(String description) {
    super(Arrays.asList(new String[] { "[arguments]" }, ), description);
  }
  
  public <T> NonOptionArgumentSpec<T> ofType(Class<T> argumentType) {
    this.converter = Reflection.findConverter(argumentType);
    return this;
  }
  
  public final <T> NonOptionArgumentSpec<T> withValuesConvertedBy(ValueConverter<T> aConverter) {
    if (aConverter == null)
      throw new NullPointerException("illegal null converter"); 
    this.converter = aConverter;
    return this;
  }
  
  public NonOptionArgumentSpec<V> describedAs(String description) {
    this.argumentDescription = description;
    return this;
  }
  
  protected final V convert(String argument) {
    return convertWith(this.converter, argument);
  }
  
  void handleOption(OptionParser parser, ArgumentList arguments, OptionSet detectedOptions, String detectedArgument) {
    detectedOptions.addWithArgument(this, detectedArgument);
  }
  
  public List<?> defaultValues() {
    return Collections.emptyList();
  }
  
  public boolean isRequired() {
    return false;
  }
  
  public boolean acceptsArguments() {
    return false;
  }
  
  public boolean requiresArgument() {
    return false;
  }
  
  public String argumentDescription() {
    return this.argumentDescription;
  }
  
  public String argumentTypeIndicator() {
    return argumentTypeIndicatorFrom(this.converter);
  }
  
  public boolean representsNonOptions() {
    return true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\joptsimple\NonOptionArgumentSpec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */