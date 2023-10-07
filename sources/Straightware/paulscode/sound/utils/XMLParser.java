package paulscode.sound.utils;

import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemLogger;

import java.io.*;
import java.net.URL;

/**
 * The XMLParser class provides a somewhat limited interface for reading
 * from and processing XML files.
 * <br><br>
 * <b><i>   XMLParser License:</b></i><br><b><br>
 * You are free to use this class for any purpose, commercial or otherwise.
 * You may modify this class or source code, and distribute it any way you
 * like, provided the following conditions are met:
 * <br>
 * 1) You may not falsely claim to be the author of this class or any
 * unmodified portion of it.
 * <br>
 * 2) You may not copyright this class or a modified version of it and then
 * sue me for copyright infringement.
 * <br>
 * 3) If you modify the source code, you must clearly document the changes
 * made before redistributing the modified source code, so other users know
 * it is not the original code.
 * <br>
 * 4) You are not required to give me credit for this class in any derived
 * work, but if you do, you must also mention my website:
 * http://www.paulscode.com
 * <br>
 * 5) I the author will not be responsible for any damages (physical,
 * financial, or otherwise) caused by the use if this class or any portion
 * of it.
 * <br>
 * 6) I the author do not guarantee, warrant, or make any representations,
 * either expressed or implied, regarding the use of this class or any
 * portion of it.
 * <br><br>
 * Author: Paul Lamb
 * <br>
 * http://www.paulscode.com
 * </b>
 */
public class XMLParser {
    /**
     * Processes status messages, warnings, and error messages.
     */
    private static SoundSystemLogger logger;

    /**
     * Parses the XML data contained in the specified file, and returns a list of
     * XML nodes.
     *
     * @param xmlFile URL to an XML file.
     * @return Handle to the first node.
     */
    public static XMLNode parseXML(URL xmlFile) {
        return new XMLNode(trimSpaces(readString(xmlFile)));
    }

    /**
     * Returns a string containing the raw XML that was read and processed from the
     * specified file.  It longer has any newlines or leading and trailing spaces,
     * and all words are seperated by single spaces.
     *
     * @param xmlFile URL to an XML file.
     * @return Raw XML.
     */
    public static String getRawXML(URL xmlFile) {
        return trimSpaces(readString(xmlFile));
    }

    /**
     * Reads a text file's contents.
     *
     * @param file URL to a text file.
     * @return String containing the file's contents.
     */
    private static String readString(URL file) {
        // Open a stream to read from:
        InputStream in = null;
        try {
            in = file.openStream();
        } catch (IOException ioe) {
            errorMessage("Unable to open inputstream in method 'readString'");
            return null;
        }

        // Set up a buffered reader interface (for reading lines of ASCII):
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ByteArrayInputStream bin = null;
        BufferedReader bufRead = null;

        String fullString = "";

        byte buffer[] = new byte[4096];
        int read = 0;
        try {
            // Read raw data into the buffer:
            do {
                read = in.read(buffer);
                if (read != -1)
                    bout.write(buffer, 0, read);
            } while (read != -1);

            // Copy binary to an input stream:
            bin = new ByteArrayInputStream(bout.toByteArray());
            bufRead = new BufferedReader(new InputStreamReader(bin));

            // Read the lines of ASCII:
            String line = "";
            do {
                line = bufRead.readLine();

                // TODO: Is this necessary? (Is newline already there?)
                if (line != null)
                    fullString = fullString + "\n" + line;
            } while (line != null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Close all streams:

        if (in != null) {
            try {
                in.close();
            } catch (Exception e) {
            }
        }
        if (bout != null) {
            try {
                bout.close();
            } catch (Exception e) {
            }
        }
        if (bin != null) {
            try {
                bin.close();
            } catch (Exception e) {
            }
        }
        if (bufRead != null) {
            try {
                bufRead.close();
            } catch (Exception e) {
            }
        }

        in = null;
        bout = null;
        bin = null;
        bufRead = null;

        // Return the result:
        return fullString;
    }

    /**
     * Trims spaces from a string.  The result no longer has any newlines or
     * leading and trailing spaces, and all words are seperated by single spaces.
     *
     * @param text String to process.
     * @return String with trimmed spaces.
     */
    public static String trimSpaces(String text) {
        // Split the string into words:
        String[] splitText = seperateWords(text);

        // Make sure there are some words:
        if (splitText == null || splitText.length == 0)
            return "";

        // Seperate each word by a single space:
        String parsedText = splitText[0];
        for (int x = 1; x < splitText.length; x++) {
            parsedText = parsedText + " " + splitText[x];
        }

        // Return the result:
        return parsedText;
    }

    /**
     * Splits a string on whitespace to generate an array of words.
     *
     * @param text String to process.
     * @return String array containing the words.
     */
    public static String[] seperateWords(String text) {
        // Make sure there is something to process:
        if (text == null)
            return null;

        // Remove all leading spaces:
        while (text.length() > 0 && text.substring(0, 1).matches("\\s")) {
            text = text.substring(1);
        }

        // Make sure there is still something left to process:
        if (text.length() == 0)
            return null;

        // Split the string on whitespace:
        String[] splitText = text.split("\\s+");

        // Return the result:
        return splitText;
    }

    /**
     * Prints an error message.
     *
     * @param message Error message to print.
     */
    protected static void errorMessage(String message) {
        // Grab a handle to the logger if we don't already have one:
        if (logger == null)
            logger = SoundSystemConfig.getLogger();
        // If a logger doesn't exist, make one:
        if (logger == null) {
            logger = new SoundSystemLogger();
            SoundSystemConfig.setLogger(logger);
        }
        logger.errorMessage("XMLParser", message, 0);
    }
}
