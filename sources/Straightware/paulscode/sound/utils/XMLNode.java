package paulscode.sound.utils;

import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemLogger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * The XMLNode class contains an XML tag's name, its parameters, and a handle
 * to the next XMLNode.
 * <br><br>
 * <b><i>   XMLNode License:</b></i><br><b><br>
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
public class XMLNode {
    /**
     * Processes status messages, warnings, and error messages.
     */
    private static SoundSystemLogger logger;

    /**
     * If true, debug messages are printed out.
     */
    private static boolean verbose = false;

    /**
     * Name of the XML tag.
     */
    protected String name = "";
    /**
     * Entire contents of the XML tag.
     */
    protected String contents = "";
    /**
     * Hashmap containing parameter/value pairs for this tag.
     */
    protected HashMap<String, String> parameters = null;
    /**
     * Handle to the next XMLNode.
     */
    protected XMLNode next = null;

    /**
     * Constructor: Parses the specified XML text and generates a list of nodes.
     * NOTE: The text must have been processed, and longer have any newlines or
     * leading and trailing spaces, and all words must be seperated by single
     * spaces.
     *
     * @param XMLText String containing the XML.
     */
    public XMLNode(String XMLText) {
        // Figure out where the next tag begins and ends:
        int lBracket = XMLText.indexOf("<");
        int rBracket = XMLText.indexOf(">");

        // If we reached the end of the file, we are done:
        if (lBracket == -1 || rBracket == -1)
            return;

        // Sanity check: Is this really XML we're looking at?)
        if (rBracket <= lBracket) {
            errorMessage("Invalid XML syntax: '>' before '<'");
            return;
        }

        // Create a hash map for the parameters:
        parameters = new HashMap<String, String>();

        // Process everything inside this tag:
        contents = XMLText.substring(lBracket + 1, rBracket);
        processTagContents(contents);

        if (verbose)
            displayParameters();

        // Check if there is more after this tag:
        if (rBracket + 1 >= XMLText.length())
            return;
        String XMLRemainder = XMLText.substring(rBracket + 1);

        // If there are no more tags, we are done:
        lBracket = XMLRemainder.indexOf("<");
        if (lBracket == -1)
            return;

        // Create the next node:
        next = new XMLNode(XMLRemainder.substring(lBracket));
    }

    /**
     * Sets whether or not to print out messages while loading (default = false).
     *
     * @param val True to show debug messages.
     */
    public static void setVerbose(boolean val) {
        verbose = val;
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
        logger.errorMessage("XMLNode", message, 0);
    }

    /**
     * Prints a warning message.
     *
     * @param message Warning message to print.
     */
    protected static void warningMessage(String message) {
        // Grab a handle to the logger if we don't already have one:
        if (logger == null)
            logger = SoundSystemConfig.getLogger();
        // If a logger doesn't exist, make one:
        if (logger == null) {
            logger = new SoundSystemLogger();
            SoundSystemConfig.setLogger(logger);
        }
        logger.importantMessage("Warning in class 'XMLNode': " + message, 0);
    }

    /**
     * Returns the name of this tag.
     *
     * @return Tag name, or "" for none.
     */
    public String name() {
        if (name == null)
            return "";

        return name;
    }

    /**
     * Returns the entire contents of this tag.
     *
     * @return Tag contents, or "" for none.
     */
    public String contents() {
        if (contents == null)
            return "";

        return contents;
    }

    /**
     * Checks if there is another XMLNode after this one.
     *
     * @return True if there are more nodes.
     */
    public boolean hasNext() {
        return (next != null);
    }

    /**
     * Returns the next XMLNode.
     *
     * @return Next node, or null if this is the last one.
     */
    public XMLNode next() {
        return next;
    }

    /**
     * Returns the value of the specified parameter.
     *
     * @return Parameter's value, or "" for none.
     */
    public String get(String parameter) {
        if (parameters == null)
            return "";

        return parameters.get(parameter.toUpperCase());
    }

    /**
     * Returns a hashmap containing all parameters and their values.
     *
     * @return String, String pairs.
     */
    public HashMap<String, String> parameters() {
        return parameters;
    }

    /**
     * Prints out the name of this tag and all of its parameters and their values.
     */
    public void displayParameters() {
        // Print the tag's name:
        System.out.println("Parameters for " + name + ":");

        // Get the parameters keyset:
        Set<String> keys = parameters.keySet();
        Iterator<String> iter = keys.iterator();
        String par;
        String val;

        // Print "(none)" if there aren't any parameters:
        if (!iter.hasNext())
            System.out.println("    (none)");

        // Loop through and print each parameter:
        while (iter.hasNext()) {
            par = iter.next();
            val = parameters.get(par);
            System.out.println("    " + par + " = " + val);
        }
    }

    /**
     * Processes a tag's contents, extracting the name and each parameter/value
     * pair.
     *
     * @param tagContents String containing everything between the XML brackets.
     */
    protected void processTagContents(String tagContents) {
        String[] splitTag = XMLParser.seperateWords(tagContents);
        if (splitTag.length > 0) {
            // Store the tag name:
            name = splitTag[0];

            // If it is a comment tag, do nothing:
            if (name.length() >= 3 && name.substring(0, 3).equals("!--"))
                return;

            // Get the parameters:
            for (int x = 1; x < splitTag.length; ) {
                String paramText = splitTag[x];
                if (paramText.equals("/"))
                    break;
                if (paramText.contains("=")) {
                    String[] pair = paramText.split("=");
                    if (pair == null || pair.length == 0) {
                        errorMessage("Invalid XML syntax: paramater null");
                        return;
                    }
                    if (pair.length == 1) {
                        if (x + 1 >= splitTag.length) {
                            warningMessage("Value not specified for " +
                                    "parameter '" + pair[0] + "'");
                            parameters.put(pair[0].toUpperCase(), "");
                            x++;
                        } else {
                            parameters.put(pair[0].toUpperCase(),
                                    splitTag[x + 1]);
                            x = x + 2;
                        }
                    } else {
                        parameters.put(pair[0].toUpperCase(), pair[1]);
                        x++;
                    }
                } else {
                    if (x + 1 >= splitTag.length) {
                        warningMessage("Value not specified for " +
                                "parameter '" + splitTag[x] + "'");
                        parameters.put(splitTag[x].toUpperCase(), "");
                        x++;
                    } else {
                        if (splitTag[x + 1].equals("=")) {
                            if (x + 2 >= splitTag.length) {
                                warningMessage("Value not specified for " +
                                        "parameter '" + splitTag[x] +
                                        "'");
                                parameters.put(splitTag[x].toUpperCase(), "");
                                x = x + 2;
                            } else {
                                parameters.put(splitTag[x].toUpperCase(),
                                        splitTag[x + 2]);
                                x = x + 3;
                            }
                        } else if (splitTag[x + 1].contains("=")) {
                            String[] eq = splitTag[x + 1].split("=");
                            if (eq == null || eq.length < 1) {
                                warningMessage("Value not specified for " +
                                        "parameter '" + splitTag[x] +
                                        "'");
                                parameters.put(splitTag[x].toUpperCase(), "");
                                x = x + 2;
                            } else {
                                parameters.put(splitTag[x].toUpperCase(),
                                        eq[1]);
                                x = x + 2;
                            }
                        } else {
                            warningMessage("Value not specified for " +
                                    "parameter '" + splitTag[x] + "'");
                            parameters.put(splitTag[x].toUpperCase(), "");
                            x++;
                        }
                    }
                }
            }
        }
    }
}
