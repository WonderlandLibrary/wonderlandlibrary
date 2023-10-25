/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Window;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommonDialogs {
    private CommonDialogs() {
    }

    public static FileChooserResult showFileChooser(Window window, File file, String string, String string2, int n, boolean bl, List<ExtensionFilter> list, int n2) {
        Application.checkEventThread();
        String string3 = CommonDialogs.convertFolder(file);
        if (string == null) {
            string = "";
        }
        if (n != 0 && n != 1) {
            throw new IllegalArgumentException("Type parameter must be equal to one of the constants from Type");
        }
        ExtensionFilter[] arrextensionFilter = null;
        if (list != null) {
            arrextensionFilter = list.toArray(new ExtensionFilter[list.size()]);
        }
        if (list == null || list.isEmpty() || n2 < 0 || n2 >= list.size()) {
            n2 = 0;
        }
        return Application.GetApplication().staticCommonDialogs_showFileChooser(window, string3, string, CommonDialogs.convertTitle(string2), n, bl, arrextensionFilter, n2);
    }

    public static File showFolderChooser(Window window, File file, String string) {
        Application.checkEventThread();
        return Application.GetApplication().staticCommonDialogs_showFolderChooser(window, CommonDialogs.convertFolder(file), CommonDialogs.convertTitle(string));
    }

    private static String convertFolder(File file) {
        if (file != null) {
            if (file.isDirectory()) {
                try {
                    return file.getCanonicalPath();
                }
                catch (IOException iOException) {
                    throw new IllegalArgumentException("Unable to get a canonical path for folder", iOException);
                }
            }
            throw new IllegalArgumentException("Folder parameter must be a valid folder");
        }
        return "";
    }

    private static String convertTitle(String string) {
        return string != null ? string : "";
    }

    protected static FileChooserResult createFileChooserResult(String[] arrstring, ExtensionFilter[] arrextensionFilter, int n) {
        ArrayList<File> arrayList = new ArrayList<File>();
        for (String string : arrstring) {
            if (string == null) continue;
            arrayList.add(new File(string));
        }
        return new FileChooserResult(arrayList, arrextensionFilter == null || n < 0 || n >= arrextensionFilter.length ? null : arrextensionFilter[n]);
    }

    public static final class Type {
        public static final int OPEN = 0;
        public static final int SAVE = 1;
    }

    public static final class ExtensionFilter {
        private final String description;
        private final List<String> extensions;

        public ExtensionFilter(String string, List<String> list) {
            Application.checkEventThread();
            if (string == null || string.trim().isEmpty()) {
                throw new IllegalArgumentException("Description parameter must be non-null and not empty");
            }
            if (list == null || list.isEmpty()) {
                throw new IllegalArgumentException("Extensions parameter must be non-null and not empty");
            }
            for (String string2 : list) {
                if (string2 != null && string2.length() != 0) continue;
                throw new IllegalArgumentException("Each extension must be non-null and not empty");
            }
            this.description = string;
            this.extensions = list;
        }

        public String getDescription() {
            Application.checkEventThread();
            return this.description;
        }

        public List<String> getExtensions() {
            Application.checkEventThread();
            return this.extensions;
        }

        private String[] extensionsToArray() {
            Application.checkEventThread();
            return this.extensions.toArray(new String[this.extensions.size()]);
        }
    }

    public static final class FileChooserResult {
        private final List<File> files;
        private final ExtensionFilter filter;

        public FileChooserResult(List<File> list, ExtensionFilter extensionFilter) {
            if (list == null) {
                throw new NullPointerException("files should not be null");
            }
            this.files = list;
            this.filter = extensionFilter;
        }

        public FileChooserResult() {
            this(new ArrayList<File>(), null);
        }

        public List<File> getFiles() {
            return this.files;
        }

        public ExtensionFilter getExtensionFilter() {
            return this.filter;
        }
    }
}

