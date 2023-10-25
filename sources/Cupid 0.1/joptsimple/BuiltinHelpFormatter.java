package joptsimple;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import joptsimple.internal.Classes;
import joptsimple.internal.Rows;
import joptsimple.internal.Strings;

public class BuiltinHelpFormatter implements HelpFormatter {
  private final Rows nonOptionRows;
  
  private final Rows optionRows;
  
  BuiltinHelpFormatter() {
    this(80, 2);
  }
  
  public BuiltinHelpFormatter(int desiredOverallWidth, int desiredColumnSeparatorWidth) {
    this.nonOptionRows = new Rows(desiredOverallWidth * 2, 0);
    this.optionRows = new Rows(desiredOverallWidth, desiredColumnSeparatorWidth);
  }
  
  public String format(Map<String, ? extends OptionDescriptor> options) {
    Comparator<OptionDescriptor> comparator = new Comparator<OptionDescriptor>() {
        public int compare(OptionDescriptor first, OptionDescriptor second) {
          return ((String)first.options().iterator().next()).compareTo(second.options().iterator().next());
        }
      };
    Set<OptionDescriptor> sorted = new TreeSet<OptionDescriptor>(comparator);
    sorted.addAll(options.values());
    addRows(sorted);
    return formattedHelpOutput();
  }
  
  private String formattedHelpOutput() {
    StringBuilder formatted = new StringBuilder();
    String nonOptionDisplay = this.nonOptionRows.render();
    if (!Strings.isNullOrEmpty(nonOptionDisplay))
      formatted.append(nonOptionDisplay).append(Strings.LINE_SEPARATOR); 
    formatted.append(this.optionRows.render());
    return formatted.toString();
  }
  
  private void addRows(Collection<? extends OptionDescriptor> options) {
    addNonOptionsDescription(options);
    if (options.isEmpty()) {
      this.optionRows.add("No options specified", "");
    } else {
      addHeaders(options);
      addOptions(options);
    } 
    fitRowsToWidth();
  }
  
  private void addNonOptionsDescription(Collection<? extends OptionDescriptor> options) {
    OptionDescriptor nonOptions = findAndRemoveNonOptionsSpec(options);
    if (shouldShowNonOptionArgumentDisplay(nonOptions)) {
      this.nonOptionRows.add("Non-option arguments:", "");
      this.nonOptionRows.add(createNonOptionArgumentsDisplay(nonOptions), "");
    } 
  }
  
  private boolean shouldShowNonOptionArgumentDisplay(OptionDescriptor nonOptions) {
    return (!Strings.isNullOrEmpty(nonOptions.description()) || !Strings.isNullOrEmpty(nonOptions.argumentTypeIndicator()) || !Strings.isNullOrEmpty(nonOptions.argumentDescription()));
  }
  
  private String createNonOptionArgumentsDisplay(OptionDescriptor nonOptions) {
    StringBuilder buffer = new StringBuilder();
    maybeAppendOptionInfo(buffer, nonOptions);
    maybeAppendNonOptionsDescription(buffer, nonOptions);
    return buffer.toString();
  }
  
  private void maybeAppendNonOptionsDescription(StringBuilder buffer, OptionDescriptor nonOptions) {
    buffer.append((buffer.length() > 0 && !Strings.isNullOrEmpty(nonOptions.description())) ? " -- " : "").append(nonOptions.description());
  }
  
  private OptionDescriptor findAndRemoveNonOptionsSpec(Collection<? extends OptionDescriptor> options) {
    for (Iterator<? extends OptionDescriptor> it = options.iterator(); it.hasNext(); ) {
      OptionDescriptor next = it.next();
      if (next.representsNonOptions()) {
        it.remove();
        return next;
      } 
    } 
    throw new AssertionError("no non-options argument spec");
  }
  
  private void addHeaders(Collection<? extends OptionDescriptor> options) {
    if (hasRequiredOption(options)) {
      this.optionRows.add("Option (* = required)", "Description");
      this.optionRows.add("---------------------", "-----------");
    } else {
      this.optionRows.add("Option", "Description");
      this.optionRows.add("------", "-----------");
    } 
  }
  
  private boolean hasRequiredOption(Collection<? extends OptionDescriptor> options) {
    for (OptionDescriptor each : options) {
      if (each.isRequired())
        return true; 
    } 
    return false;
  }
  
  private void addOptions(Collection<? extends OptionDescriptor> options) {
    for (OptionDescriptor each : options) {
      if (!each.representsNonOptions())
        this.optionRows.add(createOptionDisplay(each), createDescriptionDisplay(each)); 
    } 
  }
  
  private String createOptionDisplay(OptionDescriptor descriptor) {
    StringBuilder buffer = new StringBuilder(descriptor.isRequired() ? "* " : "");
    for (Iterator<String> i = descriptor.options().iterator(); i.hasNext(); ) {
      String option = i.next();
      buffer.append((option.length() > 1) ? "--" : ParserRules.HYPHEN);
      buffer.append(option);
      if (i.hasNext())
        buffer.append(", "); 
    } 
    maybeAppendOptionInfo(buffer, descriptor);
    return buffer.toString();
  }
  
  private void maybeAppendOptionInfo(StringBuilder buffer, OptionDescriptor descriptor) {
    String indicator = extractTypeIndicator(descriptor);
    String description = descriptor.argumentDescription();
    if (indicator != null || !Strings.isNullOrEmpty(description))
      appendOptionHelp(buffer, indicator, description, descriptor.requiresArgument()); 
  }
  
  private String extractTypeIndicator(OptionDescriptor descriptor) {
    String indicator = descriptor.argumentTypeIndicator();
    if (!Strings.isNullOrEmpty(indicator) && !String.class.getName().equals(indicator))
      return Classes.shortNameOf(indicator); 
    return null;
  }
  
  private void appendOptionHelp(StringBuilder buffer, String typeIndicator, String description, boolean required) {
    if (required) {
      appendTypeIndicator(buffer, typeIndicator, description, '<', '>');
    } else {
      appendTypeIndicator(buffer, typeIndicator, description, '[', ']');
    } 
  }
  
  private void appendTypeIndicator(StringBuilder buffer, String typeIndicator, String description, char start, char end) {
    buffer.append(' ').append(start);
    if (typeIndicator != null)
      buffer.append(typeIndicator); 
    if (!Strings.isNullOrEmpty(description)) {
      if (typeIndicator != null)
        buffer.append(": "); 
      buffer.append(description);
    } 
    buffer.append(end);
  }
  
  private String createDescriptionDisplay(OptionDescriptor descriptor) {
    List<?> defaultValues = descriptor.defaultValues();
    if (defaultValues.isEmpty())
      return descriptor.description(); 
    String defaultValuesDisplay = createDefaultValuesDisplay(defaultValues);
    return (descriptor.description() + ' ' + Strings.surround("default: " + defaultValuesDisplay, '(', ')')).trim();
  }
  
  private String createDefaultValuesDisplay(List<?> defaultValues) {
    return (defaultValues.size() == 1) ? defaultValues.get(0).toString() : defaultValues.toString();
  }
  
  private void fitRowsToWidth() {
    this.nonOptionRows.fitToWidth();
    this.optionRows.fitToWidth();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\joptsimple\BuiltinHelpFormatter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */