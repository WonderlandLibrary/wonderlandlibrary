package tech.atani.client.feature.value.impl.slider;

public final class NumberParser {

    public static <T extends Number> T parse(final String input, final Class<T> numberType) throws NumberFormatException {
        Double parsed = Double.parseDouble(input);
        if (numberType == Byte.class) {
            return (T) (Number) parsed.byteValue();
        } else if (numberType == Short.class) {
            return (T) (Number) parsed.shortValue();
        } else if (numberType == Integer.class) {
            return (T) (Number) parsed.intValue();
        } else if (numberType == Long.class) {
            return (T) (Number) parsed.longValue();
        } else if (numberType == Float.class) {
            return (T) (Number) parsed.floatValue();
        } else {
            return (T) (Number) parsed.doubleValue();
        }
    }

}
