package club.dortware.client.property.impl;

import club.dortware.client.property.Property;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class StringProperty<T1> extends Property<T1, String> {

    private final List<String> choices;
    private final List<String> choicesLowerCase;

    public StringProperty(String name, T1 owner, String... values) {
        super(name, owner, values[0]);
        choices = Arrays.asList(values);
        choicesLowerCase = new ArrayList<>();
        for (String choice : values) {
            choicesLowerCase.add(choice.toLowerCase());
        }
    }

    @Override
    public void setValue(String value) {
        if (choicesLowerCase.contains(value.toLowerCase())) {
            super.setValue(value);
        }
    }

    public void forceSetValue(String value) {
        super.setValue(value);
    }

    public List<String> getChoices() {
        return choices;
    }

    public List<String> getChoicesLowerCase() {
        return choicesLowerCase;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || getValue().equals(o);
    }

}
