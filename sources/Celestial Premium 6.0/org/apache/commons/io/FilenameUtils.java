/*
 * Decompiled with CFR 0.150.
 */
package org.apache.commons.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;
import org.apache.commons.io.IOCase;

public class FilenameUtils {
    private static final int NOT_FOUND = -1;
    public static final char EXTENSION_SEPARATOR = '.';
    public static final String EXTENSION_SEPARATOR_STR = Character.toString('.');
    private static final char UNIX_SEPARATOR = '/';
    private static final char WINDOWS_SEPARATOR = '\\';
    private static final char SYSTEM_SEPARATOR = File.separatorChar;
    private static final char OTHER_SEPARATOR = FilenameUtils.isSystemWindows() ? (char)47 : (char)92;

    static boolean isSystemWindows() {
        return SYSTEM_SEPARATOR == '\\';
    }

    private static boolean isSeparator(char ch) {
        return ch == '/' || ch == '\\';
    }

    public static String normalize(String filename) {
        return FilenameUtils.doNormalize(filename, SYSTEM_SEPARATOR, true);
    }

    public static String normalize(String filename, boolean unixSeparator) {
        char separator = unixSeparator ? (char)'/' : '\\';
        return FilenameUtils.doNormalize(filename, separator, true);
    }

    public static String normalizeNoEndSeparator(String filename) {
        return FilenameUtils.doNormalize(filename, SYSTEM_SEPARATOR, false);
    }

    public static String normalizeNoEndSeparator(String filename, boolean unixSeparator) {
        char separator = unixSeparator ? (char)'/' : '\\';
        return FilenameUtils.doNormalize(filename, separator, false);
    }

    private static String doNormalize(String filename, char separator, boolean keepSeparator) {
        int i;
        if (filename == null) {
            return null;
        }
        FilenameUtils.failIfNullBytePresent(filename);
        int size = filename.length();
        if (size == 0) {
            return filename;
        }
        int prefix = FilenameUtils.getPrefixLength(filename);
        if (prefix < 0) {
            return null;
        }
        char[] array = new char[size + 2];
        filename.getChars(0, filename.length(), array, 0);
        char otherSeparator = separator == SYSTEM_SEPARATOR ? OTHER_SEPARATOR : SYSTEM_SEPARATOR;
        for (int i2 = 0; i2 < array.length; ++i2) {
            if (array[i2] != otherSeparator) continue;
            array[i2] = separator;
        }
        boolean lastIsDirectory = true;
        if (array[size - 1] != separator) {
            array[size++] = separator;
            lastIsDirectory = false;
        }
        for (i = prefix + 1; i < size; ++i) {
            if (array[i] != separator || array[i - 1] != separator) continue;
            System.arraycopy(array, i, array, i - 1, size - i);
            --size;
            --i;
        }
        for (i = prefix + 1; i < size; ++i) {
            if (array[i] != separator || array[i - 1] != '.' || i != prefix + 1 && array[i - 2] != separator) continue;
            if (i == size - 1) {
                lastIsDirectory = true;
            }
            System.arraycopy(array, i + 1, array, i - 1, size - i);
            size -= 2;
            --i;
        }
        block3: for (i = prefix + 2; i < size; ++i) {
            if (array[i] != separator || array[i - 1] != '.' || array[i - 2] != '.' || i != prefix + 2 && array[i - 3] != separator) continue;
            if (i == prefix + 2) {
                return null;
            }
            if (i == size - 1) {
                lastIsDirectory = true;
            }
            for (int j = i - 4; j >= prefix; --j) {
                if (array[j] != separator) continue;
                System.arraycopy(array, i + 1, array, j + 1, size - i);
                size -= i - j;
                i = j + 1;
                continue block3;
            }
            System.arraycopy(array, i + 1, array, prefix, size - i);
            size -= i + 1 - prefix;
            i = prefix + 1;
        }
        if (size <= 0) {
            return "";
        }
        if (size <= prefix) {
            return new String(array, 0, size);
        }
        if (lastIsDirectory && keepSeparator) {
            return new String(array, 0, size);
        }
        return new String(array, 0, size - 1);
    }

    public static String concat(String basePath, String fullFilenameToAdd) {
        int prefix = FilenameUtils.getPrefixLength(fullFilenameToAdd);
        if (prefix < 0) {
            return null;
        }
        if (prefix > 0) {
            return FilenameUtils.normalize(fullFilenameToAdd);
        }
        if (basePath == null) {
            return null;
        }
        int len = basePath.length();
        if (len == 0) {
            return FilenameUtils.normalize(fullFilenameToAdd);
        }
        char ch = basePath.charAt(len - 1);
        if (FilenameUtils.isSeparator(ch)) {
            return FilenameUtils.normalize(basePath + fullFilenameToAdd);
        }
        return FilenameUtils.normalize(basePath + '/' + fullFilenameToAdd);
    }

    public static boolean directoryContains(String canonicalParent, String canonicalChild) throws IOException {
        if (canonicalParent == null) {
            throw new IllegalArgumentException("Directory must not be null");
        }
        if (canonicalChild == null) {
            return false;
        }
        if (IOCase.SYSTEM.checkEquals(canonicalParent, canonicalChild)) {
            return false;
        }
        return IOCase.SYSTEM.checkStartsWith(canonicalChild, canonicalParent);
    }

    public static String separatorsToUnix(String path) {
        if (path == null || path.indexOf(92) == -1) {
            return path;
        }
        return path.replace('\\', '/');
    }

    public static String separatorsToWindows(String path) {
        if (path == null || path.indexOf(47) == -1) {
            return path;
        }
        return path.replace('/', '\\');
    }

    public static String separatorsToSystem(String path) {
        if (path == null) {
            return null;
        }
        if (FilenameUtils.isSystemWindows()) {
            return FilenameUtils.separatorsToWindows(path);
        }
        return FilenameUtils.separatorsToUnix(path);
    }

    public static int getPrefixLength(String filename) {
        if (filename == null) {
            return -1;
        }
        int len = filename.length();
        if (len == 0) {
            return 0;
        }
        char ch0 = filename.charAt(0);
        if (ch0 == ':') {
            return -1;
        }
        if (len == 1) {
            if (ch0 == '~') {
                return 2;
            }
            return FilenameUtils.isSeparator(ch0) ? 1 : 0;
        }
        if (ch0 == '~') {
            int posUnix = filename.indexOf(47, 1);
            int posWin = filename.indexOf(92, 1);
            if (posUnix == -1 && posWin == -1) {
                return len + 1;
            }
            posUnix = posUnix == -1 ? posWin : posUnix;
            posWin = posWin == -1 ? posUnix : posWin;
            return Math.min(posUnix, posWin) + 1;
        }
        char ch1 = filename.charAt(1);
        if (ch1 == ':') {
            if ((ch0 = Character.toUpperCase(ch0)) >= 'A' && ch0 <= 'Z') {
                if (len == 2 || !FilenameUtils.isSeparator(filename.charAt(2))) {
                    return 2;
                }
                return 3;
            }
            return -1;
        }
        if (FilenameUtils.isSeparator(ch0) && FilenameUtils.isSeparator(ch1)) {
            int posUnix = filename.indexOf(47, 2);
            int posWin = filename.indexOf(92, 2);
            if (posUnix == -1 && posWin == -1 || posUnix == 2 || posWin == 2) {
                return -1;
            }
            posUnix = posUnix == -1 ? posWin : posUnix;
            posWin = posWin == -1 ? posUnix : posWin;
            return Math.min(posUnix, posWin) + 1;
        }
        return FilenameUtils.isSeparator(ch0) ? 1 : 0;
    }

    public static int indexOfLastSeparator(String filename) {
        if (filename == null) {
            return -1;
        }
        int lastUnixPos = filename.lastIndexOf(47);
        int lastWindowsPos = filename.lastIndexOf(92);
        return Math.max(lastUnixPos, lastWindowsPos);
    }

    public static int indexOfExtension(String filename) {
        if (filename == null) {
            return -1;
        }
        int extensionPos = filename.lastIndexOf(46);
        int lastSeparator = FilenameUtils.indexOfLastSeparator(filename);
        return lastSeparator > extensionPos ? -1 : extensionPos;
    }

    public static String getPrefix(String filename) {
        if (filename == null) {
            return null;
        }
        int len = FilenameUtils.getPrefixLength(filename);
        if (len < 0) {
            return null;
        }
        if (len > filename.length()) {
            FilenameUtils.failIfNullBytePresent(filename + '/');
            return filename + '/';
        }
        String path = filename.substring(0, len);
        FilenameUtils.failIfNullBytePresent(path);
        return path;
    }

    public static String getPath(String filename) {
        return FilenameUtils.doGetPath(filename, 1);
    }

    public static String getPathNoEndSeparator(String filename) {
        return FilenameUtils.doGetPath(filename, 0);
    }

    private static String doGetPath(String filename, int separatorAdd) {
        if (filename == null) {
            return null;
        }
        int prefix = FilenameUtils.getPrefixLength(filename);
        if (prefix < 0) {
            return null;
        }
        int index = FilenameUtils.indexOfLastSeparator(filename);
        int endIndex = index + separatorAdd;
        if (prefix >= filename.length() || index < 0 || prefix >= endIndex) {
            return "";
        }
        String path = filename.substring(prefix, endIndex);
        FilenameUtils.failIfNullBytePresent(path);
        return path;
    }

    public static String getFullPath(String filename) {
        return FilenameUtils.doGetFullPath(filename, true);
    }

    public static String getFullPathNoEndSeparator(String filename) {
        return FilenameUtils.doGetFullPath(filename, false);
    }

    private static String doGetFullPath(String filename, boolean includeSeparator) {
        if (filename == null) {
            return null;
        }
        int prefix = FilenameUtils.getPrefixLength(filename);
        if (prefix < 0) {
            return null;
        }
        if (prefix >= filename.length()) {
            if (includeSeparator) {
                return FilenameUtils.getPrefix(filename);
            }
            return filename;
        }
        int index = FilenameUtils.indexOfLastSeparator(filename);
        if (index < 0) {
            return filename.substring(0, prefix);
        }
        int end = index + (includeSeparator ? 1 : 0);
        if (end == 0) {
            ++end;
        }
        return filename.substring(0, end);
    }

    public static String getName(String filename) {
        if (filename == null) {
            return null;
        }
        FilenameUtils.failIfNullBytePresent(filename);
        int index = FilenameUtils.indexOfLastSeparator(filename);
        return filename.substring(index + 1);
    }

    private static void failIfNullBytePresent(String path) {
        int len = path.length();
        for (int i = 0; i < len; ++i) {
            if (path.charAt(i) != '\u0000') continue;
            throw new IllegalArgumentException("Null byte present in file/path name. There are no known legitimate use cases for such data, but several injection attacks may use it");
        }
    }

    public static String getBaseName(String filename) {
        return FilenameUtils.removeExtension(FilenameUtils.getName(filename));
    }

    public static String getExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int index = FilenameUtils.indexOfExtension(filename);
        if (index == -1) {
            return "";
        }
        return filename.substring(index + 1);
    }

    public static String removeExtension(String filename) {
        if (filename == null) {
            return null;
        }
        FilenameUtils.failIfNullBytePresent(filename);
        int index = FilenameUtils.indexOfExtension(filename);
        if (index == -1) {
            return filename;
        }
        return filename.substring(0, index);
    }

    public static boolean equals(String filename1, String filename2) {
        return FilenameUtils.equals(filename1, filename2, false, IOCase.SENSITIVE);
    }

    public static boolean equalsOnSystem(String filename1, String filename2) {
        return FilenameUtils.equals(filename1, filename2, false, IOCase.SYSTEM);
    }

    public static boolean equalsNormalized(String filename1, String filename2) {
        return FilenameUtils.equals(filename1, filename2, true, IOCase.SENSITIVE);
    }

    public static boolean equalsNormalizedOnSystem(String filename1, String filename2) {
        return FilenameUtils.equals(filename1, filename2, true, IOCase.SYSTEM);
    }

    public static boolean equals(String filename1, String filename2, boolean normalized, IOCase caseSensitivity) {
        if (filename1 == null || filename2 == null) {
            return filename1 == null && filename2 == null;
        }
        if (normalized) {
            filename1 = FilenameUtils.normalize(filename1);
            filename2 = FilenameUtils.normalize(filename2);
            if (filename1 == null || filename2 == null) {
                throw new NullPointerException("Error normalizing one or both of the file names");
            }
        }
        if (caseSensitivity == null) {
            caseSensitivity = IOCase.SENSITIVE;
        }
        return caseSensitivity.checkEquals(filename1, filename2);
    }

    public static boolean isExtension(String filename, String extension) {
        if (filename == null) {
            return false;
        }
        FilenameUtils.failIfNullBytePresent(filename);
        if (extension == null || extension.isEmpty()) {
            return FilenameUtils.indexOfExtension(filename) == -1;
        }
        String fileExt = FilenameUtils.getExtension(filename);
        return fileExt.equals(extension);
    }

    public static boolean isExtension(String filename, String[] extensions) {
        if (filename == null) {
            return false;
        }
        FilenameUtils.failIfNullBytePresent(filename);
        if (extensions == null || extensions.length == 0) {
            return FilenameUtils.indexOfExtension(filename) == -1;
        }
        String fileExt = FilenameUtils.getExtension(filename);
        for (String extension : extensions) {
            if (!fileExt.equals(extension)) continue;
            return true;
        }
        return false;
    }

    public static boolean isExtension(String filename, Collection<String> extensions) {
        if (filename == null) {
            return false;
        }
        FilenameUtils.failIfNullBytePresent(filename);
        if (extensions == null || extensions.isEmpty()) {
            return FilenameUtils.indexOfExtension(filename) == -1;
        }
        String fileExt = FilenameUtils.getExtension(filename);
        for (String extension : extensions) {
            if (!fileExt.equals(extension)) continue;
            return true;
        }
        return false;
    }

    public static boolean wildcardMatch(String filename, String wildcardMatcher) {
        return FilenameUtils.wildcardMatch(filename, wildcardMatcher, IOCase.SENSITIVE);
    }

    public static boolean wildcardMatchOnSystem(String filename, String wildcardMatcher) {
        return FilenameUtils.wildcardMatch(filename, wildcardMatcher, IOCase.SYSTEM);
    }

    public static boolean wildcardMatch(String filename, String wildcardMatcher, IOCase caseSensitivity) {
        if (filename == null && wildcardMatcher == null) {
            return true;
        }
        if (filename == null || wildcardMatcher == null) {
            return false;
        }
        if (caseSensitivity == null) {
            caseSensitivity = IOCase.SENSITIVE;
        }
        String[] wcs = FilenameUtils.splitOnTokens(wildcardMatcher);
        boolean anyChars = false;
        int textIdx = 0;
        int wcsIdx = 0;
        Stack<int[]> backtrack = new Stack<int[]>();
        do {
            if (backtrack.size() > 0) {
                int[] array = (int[])backtrack.pop();
                wcsIdx = array[0];
                textIdx = array[1];
                anyChars = true;
            }
            while (wcsIdx < wcs.length) {
                if (wcs[wcsIdx].equals("?")) {
                    if (++textIdx > filename.length()) break;
                    anyChars = false;
                } else if (wcs[wcsIdx].equals("*")) {
                    anyChars = true;
                    if (wcsIdx == wcs.length - 1) {
                        textIdx = filename.length();
                    }
                } else {
                    if (anyChars) {
                        if ((textIdx = caseSensitivity.checkIndexOf(filename, textIdx, wcs[wcsIdx])) == -1) break;
                        int repeat = caseSensitivity.checkIndexOf(filename, textIdx + 1, wcs[wcsIdx]);
                        if (repeat >= 0) {
                            backtrack.push(new int[]{wcsIdx, repeat});
                        }
                    } else if (!caseSensitivity.checkRegionMatches(filename, textIdx, wcs[wcsIdx])) break;
                    textIdx += wcs[wcsIdx].length();
                    anyChars = false;
                }
                ++wcsIdx;
            }
            if (wcsIdx != wcs.length || textIdx != filename.length()) continue;
            return true;
        } while (backtrack.size() > 0);
        return false;
    }

    static String[] splitOnTokens(String text) {
        if (text.indexOf(63) == -1 && text.indexOf(42) == -1) {
            return new String[]{text};
        }
        char[] array = text.toCharArray();
        ArrayList<String> list = new ArrayList<String>();
        StringBuilder buffer = new StringBuilder();
        char prevChar = '\u0000';
        for (char ch : array) {
            if (ch == '?' || ch == '*') {
                if (buffer.length() != 0) {
                    list.add(buffer.toString());
                    buffer.setLength(0);
                }
                if (ch == '?') {
                    list.add("?");
                } else if (prevChar != '*') {
                    list.add("*");
                }
            } else {
                buffer.append(ch);
            }
            prevChar = ch;
        }
        if (buffer.length() != 0) {
            list.add(buffer.toString());
        }
        return list.toArray(new String[list.size()]);
    }
}

