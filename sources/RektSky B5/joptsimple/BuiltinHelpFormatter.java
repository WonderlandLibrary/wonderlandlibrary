/*
 * Decompiled with CFR 0.152.
 */
package joptsimple;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeSet;
import joptsimple.HelpFormatter;
import joptsimple.OptionDescriptor;
import joptsimple.ParserRules;
import joptsimple.internal.Classes;
import joptsimple.internal.Messages;
import joptsimple.internal.Rows;
import joptsimple.internal.Strings;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class BuiltinHelpFormatter
implements HelpFormatter {
    private final Rows nonOptionRows;
    private final Rows optionRows;

    BuiltinHelpFormatter() {
        this(80, 2);
    }

    public BuiltinHelpFormatter(int desiredOverallWidth, int desiredColumnSeparatorWidth) {
        this.nonOptionRows = new Rows(desiredOverallWidth * 2, 0);
        this.optionRows = new Rows(desiredOverallWidth, desiredColumnSeparatorWidth);
    }

    @Override
    public String format(Map<String, ? extends OptionDescriptor> options) {
        Comparator<OptionDescriptor> comparator = new Comparator<OptionDescriptor>(){

            @Override
            public int compare(OptionDescriptor first, OptionDescriptor second) {
                return first.options().iterator().next().compareTo(second.options().iterator().next());
            }
        };
        TreeSet<OptionDescriptor> sorted = new TreeSet<OptionDescriptor>(comparator);
        sorted.addAll(options.values());
        this.addRows(sorted);
        return this.formattedHelpOutput();
    }

    protected void addOptionRow(String single) {
        this.addOptionRow(single, "");
    }

    protected void addOptionRow(String left, String right) {
        this.optionRows.add(left, right);
    }

    protected void addNonOptionRow(String single) {
        this.nonOptionRows.add(single, "");
    }

    protected void fitRowsToWidth() {
        this.nonOptionRows.fitToWidth();
        this.optionRows.fitToWidth();
    }

    protected String nonOptionOutput() {
        return this.nonOptionRows.render();
    }

    protected String optionOutput() {
        return this.optionRows.render();
    }

    protected String formattedHelpOutput() {
        StringBuilder formatted = new StringBuilder();
        String nonOptionDisplay = this.nonOptionOutput();
        if (!Strings.isNullOrEmpty(nonOptionDisplay)) {
            formatted.append(nonOptionDisplay).append(Strings.LINE_SEPARATOR);
        }
        formatted.append(this.optionOutput());
        return formatted.toString();
    }

    protected void addRows(Collection<? extends OptionDescriptor> options) {
        this.addNonOptionsDescription(options);
        if (options.isEmpty()) {
            this.addOptionRow(this.message("no.options.specified", new Object[0]));
        } else {
            this.addHeaders(options);
            this.addOptions(options);
        }
        this.fitRowsToWidth();
    }

    protected void addNonOptionsDescription(Collection<? extends OptionDescriptor> options) {
        OptionDescriptor nonOptions = this.findAndRemoveNonOptionsSpec(options);
        if (this.shouldShowNonOptionArgumentDisplay(nonOptions)) {
            this.addNonOptionRow(this.message("non.option.arguments.header", new Object[0]));
            this.addNonOptionRow(this.createNonOptionArgumentsDisplay(nonOptions));
        }
    }

    protected boolean shouldShowNonOptionArgumentDisplay(OptionDescriptor nonOptionDescriptor) {
        return !Strings.isNullOrEmpty(nonOptionDescriptor.description()) || !Strings.isNullOrEmpty(nonOptionDescriptor.argumentTypeIndicator()) || !Strings.isNullOrEmpty(nonOptionDescriptor.argumentDescription());
    }

    protected String createNonOptionArgumentsDisplay(OptionDescriptor nonOptionDescriptor) {
        StringBuilder buffer = new StringBuilder();
        this.maybeAppendOptionInfo(buffer, nonOptionDescriptor);
        this.maybeAppendNonOptionsDescription(buffer, nonOptionDescriptor);
        return buffer.toString();
    }

    protected void maybeAppendNonOptionsDescription(StringBuilder buffer, OptionDescriptor nonOptions) {
        buffer.append(buffer.length() > 0 && !Strings.isNullOrEmpty(nonOptions.description()) ? " -- " : "").append(nonOptions.description());
    }

    protected OptionDescriptor findAndRemoveNonOptionsSpec(Collection<? extends OptionDescriptor> options) {
        Iterator<? extends OptionDescriptor> it = options.iterator();
        while (it.hasNext()) {
            OptionDescriptor next = it.next();
            if (!next.representsNonOptions()) continue;
            it.remove();
            return next;
        }
        throw new AssertionError((Object)"no non-options argument spec");
    }

    protected void addHeaders(Collection<? extends OptionDescriptor> options) {
        if (this.hasRequiredOption(options)) {
            this.addOptionRow(this.message("option.header.with.required.indicator", new Object[0]), this.message("description.header", new Object[0]));
            this.addOptionRow(this.message("option.divider.with.required.indicator", new Object[0]), this.message("description.divider", new Object[0]));
        } else {
            this.addOptionRow(this.message("option.header", new Object[0]), this.message("description.header", new Object[0]));
            this.addOptionRow(this.message("option.divider", new Object[0]), this.message("description.divider", new Object[0]));
        }
    }

    protected final boolean hasRequiredOption(Collection<? extends OptionDescriptor> options) {
        for (OptionDescriptor optionDescriptor : options) {
            if (!optionDescriptor.isRequired()) continue;
            return true;
        }
        return false;
    }

    protected void addOptions(Collection<? extends OptionDescriptor> options) {
        for (OptionDescriptor optionDescriptor : options) {
            if (optionDescriptor.representsNonOptions()) continue;
            this.addOptionRow(this.createOptionDisplay(optionDescriptor), this.createDescriptionDisplay(optionDescriptor));
        }
    }

    protected String createOptionDisplay(OptionDescriptor descriptor) {
        StringBuilder buffer = new StringBuilder(descriptor.isRequired() ? "* " : "");
        Iterator<String> i2 = descriptor.options().iterator();
        while (i2.hasNext()) {
            String option = i2.next();
            buffer.append(this.optionLeader(option));
            buffer.append(option);
            if (!i2.hasNext()) continue;
            buffer.append(", ");
        }
        this.maybeAppendOptionInfo(buffer, descriptor);
        return buffer.toString();
    }

    protected String optionLeader(String option) {
        return option.length() > 1 ? "--" : ParserRules.HYPHEN;
    }

    protected void maybeAppendOptionInfo(StringBuilder buffer, OptionDescriptor descriptor) {
        String indicator = this.extractTypeIndicator(descriptor);
        String description = descriptor.argumentDescription();
        if (indicator != null || !Strings.isNullOrEmpty(description)) {
            this.appendOptionHelp(buffer, indicator, description, descriptor.requiresArgument());
        }
    }

    protected String extractTypeIndicator(OptionDescriptor descriptor) {
        String indicator = descriptor.argumentTypeIndicator();
        if (!Strings.isNullOrEmpty(indicator) && !String.class.getName().equals(indicator)) {
            return Classes.shortNameOf(indicator);
        }
        return null;
    }

    protected void appendOptionHelp(StringBuilder buffer, String typeIndicator, String description, boolean required) {
        if (required) {
            this.appendTypeIndicator(buffer, typeIndicator, description, '<', '>');
        } else {
            this.appendTypeIndicator(buffer, typeIndicator, description, '[', ']');
        }
    }

    protected void appendTypeIndicator(StringBuilder buffer, String typeIndicator, String description, char start, char end) {
        buffer.append(' ').append(start);
        if (typeIndicator != null) {
            buffer.append(typeIndicator);
        }
        if (!Strings.isNullOrEmpty(description)) {
            if (typeIndicator != null) {
                buffer.append(": ");
            }
            buffer.append(description);
        }
        buffer.append(end);
    }

    protected String createDescriptionDisplay(OptionDescriptor descriptor) {
        List<?> defaultValues = descriptor.defaultValues();
        if (defaultValues.isEmpty()) {
            return descriptor.description();
        }
        String defaultValuesDisplay = this.createDefaultValuesDisplay(defaultValues);
        return (descriptor.description() + ' ' + Strings.surround(this.message("default.value.header", new Object[0]) + ' ' + defaultValuesDisplay, '(', ')')).trim();
    }

    protected String createDefaultValuesDisplay(List<?> defaultValues) {
        return defaultValues.size() == 1 ? defaultValues.get(0).toString() : defaultValues.toString();
    }

    protected String message(String keySuffix, Object ... args) {
        return Messages.message(Locale.getDefault(), "joptsimple.HelpFormatterMessages", BuiltinHelpFormatter.class, keySuffix, args);
    }
}

