/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.util;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.GroupPrincipal;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.UserPrincipal;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.Objects;
import java.util.Set;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;

public final class FileUtils {
    private static final String PROTOCOL_FILE = "file";
    private static final String JBOSS_FILE = "vfsfile";
    private static final Logger LOGGER = StatusLogger.getLogger();

    private FileUtils() {
    }

    public static File fileFromUri(URI uri) {
        if (uri == null) {
            return null;
        }
        if (uri.isAbsolute()) {
            if (JBOSS_FILE.equals(uri.getScheme())) {
                try {
                    uri = new URI(PROTOCOL_FILE, uri.getSchemeSpecificPart(), uri.getFragment());
                }
                catch (URISyntaxException uRISyntaxException) {
                    // empty catch block
                }
            }
            try {
                if (PROTOCOL_FILE.equals(uri.getScheme())) {
                    return new File(uri);
                }
            }
            catch (Exception ex) {
                LOGGER.warn("Invalid URI {}", uri);
            }
        } else {
            File file = new File(uri.toString());
            try {
                if (file.exists()) {
                    return file;
                }
                String path = uri.getPath();
                return new File(path);
            }
            catch (Exception ex) {
                LOGGER.warn("Invalid URI {}", uri);
            }
        }
        return null;
    }

    public static boolean isFile(URL url) {
        return url != null && (url.getProtocol().equals(PROTOCOL_FILE) || url.getProtocol().equals(JBOSS_FILE));
    }

    public static String getFileExtension(File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return null;
    }

    public static void mkdir(File dir, boolean createDirectoryIfNotExisting) throws IOException {
        if (!dir.exists()) {
            if (!createDirectoryIfNotExisting) {
                throw new IOException("The directory " + dir.getAbsolutePath() + " does not exist.");
            }
            if (!dir.mkdirs()) {
                throw new IOException("Could not create directory " + dir.getAbsolutePath());
            }
        }
        if (!dir.isDirectory()) {
            throw new IOException("File " + dir + " exists and is not a directory. Unable to create directory.");
        }
    }

    public static void makeParentDirs(File file) throws IOException {
        File parent = Objects.requireNonNull(file, PROTOCOL_FILE).getCanonicalFile().getParentFile();
        if (parent != null) {
            FileUtils.mkdir(parent, true);
        }
    }

    public static void defineFilePosixAttributeView(Path path, Set<PosixFilePermission> filePermissions, String fileOwner, String fileGroup) throws IOException {
        PosixFileAttributeView view = Files.getFileAttributeView(path, PosixFileAttributeView.class, new LinkOption[0]);
        if (view != null) {
            GroupPrincipal groupPrincipal;
            UserPrincipal userPrincipal;
            UserPrincipalLookupService lookupService = FileSystems.getDefault().getUserPrincipalLookupService();
            if (fileOwner != null && (userPrincipal = lookupService.lookupPrincipalByName(fileOwner)) != null) {
                view.setOwner(userPrincipal);
            }
            if (fileGroup != null && (groupPrincipal = lookupService.lookupPrincipalByGroupName(fileGroup)) != null) {
                view.setGroup(groupPrincipal);
            }
            if (filePermissions != null) {
                view.setPermissions(filePermissions);
            }
        }
    }

    public static boolean isFilePosixAttributeViewSupported() {
        return FileSystems.getDefault().supportedFileAttributeViews().contains("posix");
    }
}

