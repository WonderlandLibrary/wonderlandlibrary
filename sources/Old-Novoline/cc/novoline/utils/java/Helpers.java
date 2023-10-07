package cc.novoline.utils.java;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

/**
 * @author xDelsy
 */
public final class Helpers {

    private Helpers() {
        throw new java.lang.UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static boolean isEmpty(final CharSequence seq) {
        return seq == null || seq.length() == 0;
    }

    public static boolean containsWhitespace(final CharSequence seq) {
        if (isEmpty(seq)) return false;

        for (int i = 0; i < seq.length(); i++) {
            if (Character.isWhitespace(seq.charAt(i))) return true;
        }

        return false;
    }

    public static boolean isBlank(final CharSequence seq) {
        if (isEmpty(seq)) return true;

        for (int i = 0; i < seq.length(); i++) {
            if (!Character.isWhitespace(seq.charAt(i))) return false;
        }

        return true;
    }

    public static int countMatches(final CharSequence seq, final char c) {
        if (isEmpty(seq)) return 0;
        int count = 0;

        for (int i = 0; i < seq.length(); i++) {
            if (seq.charAt(i) == c) count++;
        }

        return count;
    }

    public static String truncate(final String input, final int maxWidth) {
        if (input == null) return null;

        Checks.notNegative(maxWidth, "maxWidth");

        if (input.length() <= maxWidth) return input;
        if (maxWidth == 0) return "";

        return input.substring(0, maxWidth);
    }

    public static boolean isNumeric(final String input) {
        if (isEmpty(input)) return false;

        for (char c : input.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }

        return true;
    }

    public static boolean deepEquals(Collection<?> first, Collection<?> second) {
        if (first == second) return true;
        if (first == null || second == null || first.size() != second.size()) return false;

        for (Iterator<?> itFirst = first.iterator(), itSecond = second.iterator(); itFirst.hasNext(); ) {
            final Object elementFirst = itFirst.next();
            final Object elementSecond = itSecond.next();

            if (!Objects.equals(elementFirst, elementSecond)) return false;
        }

        return true;
    }

    public static boolean deepEqualsUnordered(Collection<?> first, Collection<?> second) {
        if (first == second) return true;
        if (first == null || second == null) return false;

        return first.size() == second.size() && second.containsAll(first);
    }

}
