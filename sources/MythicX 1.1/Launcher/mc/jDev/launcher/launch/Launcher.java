package mc.jDev.launcher.launch;

import javax.swing.*;
import java.io.*;
import java.lang.reflect.Method;
import java.net.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.Random;
import java.util.zip.*;

public class Launcher {
    public static void downloadAndStartClient(boolean beta) {
        if (beta) {
            if (!fetchWebsiteContent("https://stock-study.000webhostapp.com/hwid.html").contains(getHWID())) {
                JOptionPane.showMessageDialog(null, "You arent allowed to use the Beta version!");
                return;
            }
        }
        String downloadPath = Paths.get(System.getProperty("java.io.tmpdir"), ".MythicXLauncher").toString();

        /*try {
            downloadFile("MythicX.zip", beta ? MathUtil.betadl : CustomLabel.normaldl, downloadPath);
            unzipFile(downloadPath + File.separator + "MythicX.zip");
        } catch (IOException e) {S
        }*/

        try {
            loadNativeShit();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            File jarFile = new File(downloadPath + File.separator + "MythicX.jar"); // Replace this with the path to your JAR file

            URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{jarFile.toURI().toURL()});

            // Load the Start class
            Class<?> startClass = Class.forName("Start", true, classLoader);

            // Get the main method of the Start class
            Method mainMethod = startClass.getMethod("main", String[].class);

            // Prepare arguments to be passed to the main method
            String[] arguments = {}; // Replace these with your desired arguments

            // Invoke the main method with the arguments
            mainMethod.invoke(null, (Object) arguments);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadNativeShit() throws IOException {
        InputStream inputStream = Launcher.class.getResourceAsStream("/native.zip");

        // Create a temporary directory to extract the contents of the zip file
        Path tempDir = Files.createTempDirectory("nativeLibs");

        // Extract the contents of native.zip to the temporary directory
        try (ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    Path entryPath = tempDir.resolve(entry.getName());
                    Files.copy(zipInputStream, entryPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }

        System.setProperty("java.library.path", tempDir.toAbsolutePath().toUri().getPath());
        System.setProperty("org.lwjgl.librarypath", tempDir.toAbsolutePath().toUri().getPath());
    }

    public static void downloadFile(final String filename, final String urlString, final String downloadPath) throws IOException {
        JOptionPane.showMessageDialog(null, "Warning: Depending on your connection, this might take a while, so it may seem like the Launcher is not responding!");
        BufferedInputStream in = null;
        FileOutputStream fout = null;

        try {
            in = new BufferedInputStream(new URL(urlString).openStream());

            // Create the download directory if it doesn't exist
            Path downloadDir = Paths.get(downloadPath);
            if (!Files.exists(downloadDir)) {
                Files.createDirectories(downloadDir);
            }

            // Create the file in the specified download path
            fout = new FileOutputStream(downloadDir.resolve(filename).toFile());

            final byte[] data = new byte[1024];
            int count;
            long downloadedBytes = 0;
            long fileSize = getFileSize(urlString);

            while ((count = in.read(data, 0, 1024)) != -1) {
                fout.write(data, 0, count);
                downloadedBytes += count;
                int percentage = (int) ((downloadedBytes * 100) / fileSize);
                System.out.println("Downloaded: " + percentage + "%");
            }
            System.out.println("\nDownload completed!");
        } finally {
            if (in != null) {
                in.close();
            }
            if (fout != null) {
                fout.close();
            }
        }
    }


    private static long getFileSize(String urlString) throws IOException {
        URL url = new URL(urlString);
        return url.openConnection().getContentLengthLong();
    }

    public static void unzipFile(String zipFilePath) throws IOException {
        Path zipFile = Paths.get(zipFilePath);
        String destDirectory = zipFile.getParent().toString();

        try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry = zipIn.getNextEntry();

            while (entry != null) {
                String filePath = destDirectory + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    extractFile(zipIn, filePath);
                } else {
                    File dir = new File(filePath);
                    dir.mkdirs();
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
        }
    }

    public static String getHWID() {
        try{
            String toEncrypt =  System.getenv("COMPUTERNAME") + System.getProperty("user.name") + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_LEVEL");
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(toEncrypt.getBytes());
            StringBuffer hexString = new StringBuffer();

            byte byteData[] = md.digest();

            for (byte aByteData : byteData) {
                String hex = Integer.toHexString(0xff & aByteData);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }
    public static String fetchWebsiteContent(String urlString) {
        StringBuilder content = new StringBuilder();

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            reader.close();
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return content.toString();
    }

    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath))) {
            byte[] bytesIn = new byte[4096];
            int read;
            while ((read = zipIn.read(bytesIn)) != -1) {
                bos.write(bytesIn, 0, read);
            }
        }
    }
}
