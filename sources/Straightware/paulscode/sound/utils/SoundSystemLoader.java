package paulscode.sound.utils;

import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.SoundSystemLogger;

import java.net.URL;

/**
 * The SoundSystemLoader class allows the SoundSystem to be set up and loaded
 * via external setup files.
 * <br><br>
 * <b><i>   SoundSystemLoader License:</b></i><br><b><br>
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
public class SoundSystemLoader {
    /**
     * Processes status messages, warnings, and error messages.
     */
    private static SoundSystemLogger logger;

    /**
     * If true, debug messages are printed out.
     */
    private static boolean verbose = false;

    /**
     * Parses an XML file and executes the commands it contains.  If a SoundSystem
     * instance is specified, it will be used, otherwise this method will create
     * a SoundSystem instance and return a handle to it if the XML file contains
     * a CREATE tag.  XML tags are NOT case sensitive.  Parameters must be placed
     * inside whichever tag they belong to (nested PARAMETER tags are not
     * recognized).  Parameter values must NOT contain whitespace (this includes
     * filenames and sourcenames).  Parameter values must NOT be inside quotes.
     * Parameter names are NOT case sensetive, but parameter values ARE case
     * sensitive.  The supported tags and their parameters are listed below.
     * Parameter names in  brackets indicate they are optional.  Parameter names
     * without brackets are not optional.  Whenever optional parameters are not
     * defined, default values will be used instead.                        <br><br>
     * !--                                                                      <br>
     * &nbsp;&nbsp; XML Comment                                             <br><br>
     * ADDLIBRARY                                                               <br>
     * &nbsp;&nbsp; Adds a library plug-in.                                     <br>
     * &nbsp;&nbsp; Parameter:                                                  <br>
     * &nbsp;&nbsp; &nbsp; &nbsp; CLASSNAME                                 <br><br>
     * SETCODEC    Adds a codec plug-in.  Parameters: EXTENSION and CLASSNAME   <br>
     * &nbsp;&nbsp; Adds a codec plug-in.                                       <br>
     * &nbsp;&nbsp; Parameters:                                                 <br>
     * &nbsp;&nbsp; &nbsp; &nbsp; EXTENSION                                     <br>
     * &nbsp;&nbsp; &nbsp; &nbsp; CLASSNAME                                 <br><br>
     * CREATE                                                                   <br>
     * &nbsp;&nbsp; Instantiates the SoundSystem.                               <br>
     * &nbsp;&nbsp; Parameter:                                                  <br>
     * &nbsp;&nbsp; &nbsp; &nbsp; [CLASSNAME]                               <br><br>
     * LOADSOUND                                                                <br>
     * &nbsp;&nbsp; Loads a clip into memory.  SoundSystem MUST be instantiated.<br>
     * &nbsp;&nbsp; Parameter:                                                  <br>
     * &nbsp;&nbsp; &nbsp; &nbsp; FILENAME                                  <br><br>
     * NEWSOURCE                                                                <br>
     * &nbsp;&nbsp; Creates a new source.  SoundSystem MUST be instantiated.    <br>
     * &nbsp;&nbsp; Parameters:                                                 <br>
     * &nbsp;&nbsp; &nbsp; &nbsp; [PRIORITY]                                    <br>
     * &nbsp;&nbsp; &nbsp; &nbsp; [TOSTREAM]                                    <br>
     * &nbsp;&nbsp; &nbsp; &nbsp; [TOLOOP]                                      <br>
     * &nbsp;&nbsp; &nbsp; &nbsp; SOURCENAME                                    <br>
     * &nbsp;&nbsp; &nbsp; &nbsp; FILENAME                                      <br>
     * &nbsp;&nbsp; &nbsp; &nbsp; [X]                                           <br>
     * &nbsp;&nbsp; &nbsp; &nbsp; [Y]                                           <br>
     * &nbsp;&nbsp; &nbsp; &nbsp; [Z]                                           <br>
     * &nbsp;&nbsp; &nbsp; &nbsp; [ATTMODEL]                                    <br>
     * &nbsp;&nbsp; &nbsp; &nbsp; [DISTORROLL]                                  <br>
     * <br>
     * Note: Class names and file names ARE case sensitive and can NOT contain
     * spaces.
     *
     * @param xmlFile URL to an XML file.
     * @param s       Handle to a SoundSystem instance, or null to create one.
     * @return Handle to SoundSystem, or null if none was initially specified and a CREATE command was not found in the XML file.
     */
    public static SoundSystem loadXML(URL xmlFile, SoundSystem s) {
        // Make sure the file exists:
        if (xmlFile == null) {
            errorMessage("Parameter 'xmlFile' null in method 'loadXML'");
            return null;
        }

        // Parse the file to get the commands:
        XMLNode commands = XMLParser.parseXML(xmlFile);

        // Check if there are any commands:
        if (commands == null) {
            warningMessage("No commands found in XML file");
            return null;
        }

        // Process all commands:
        Class c = null;
        String command;
        String parameter;
        while (commands != null) {
            // Get the command name:
            command = commands.name().toUpperCase();

            // Check that the name exists, and that it isn't a "close tag":
            if (command != null && !command.substring(0, 1).equals("/")) {
                // See what command it is:
                if (command.equals("ADDLIBRARY")) {
                    // Add a library plug-in
                    if (verbose) {
                        message("SoundSystemLoader.loadXML:  addLibrary", 0);
                        message("Class name:  " + commands.get("CLASSNAME"),
                                1);
                    }
                    try {
                        // Get the class name
                        c = Class.forName(commands.get("CLASSNAME"));
                        if (verbose)
                            message("Command:  SoundSystemConfig.addLibrary( "
                                    + commands.get("CLASSNAME") +
                                    ".class );", 1);
                        // Add the library
                        SoundSystemConfig.addLibrary(c);
                    } catch (ClassNotFoundException cnfe) {
                        errorMessage("Unable to add library plug-in in " +
                                "method 'loadXML':  Class name '" +
                                commands.get("CLASSNAME") +
                                "' not found.");
                    } catch (SoundSystemException sse) {
                        printStackTrace(sse);
                    }
                } else if (command.equals("SETCODEC")) {
                    // Add a codec plug-in
                    if (verbose) {
                        message("SoundSystemLoader.loadXML:  setCodec", 0);
                        message("Extension: " + commands.get("EXTENSION"),
                                1);
                        message("Class name: " + commands.get("CLASSNAME"),
                                1);
                    }
                    try {
                        // Get the class name
                        c = Class.forName(commands.get("CLASSNAME"));
                        if (verbose)
                            message("Command:  SoundSystemConfig.setCodec( \""
                                            + commands.get("EXTENSION") + "\", " +
                                            commands.get("CLASSNAME") + ".class );",
                                    1);
                        // Associate the codec with the correct extension:
                        SoundSystemConfig.setCodec(
                                commands.get("EXTENSION"), c);
                    } catch (ClassNotFoundException cnfe) {
                        errorMessage("Unable to set codec plug-in for " +
                                "extension '" +
                                commands.get("EXTENSION") +
                                "' in method 'loadXML':  Class name '" +
                                commands.get("CLASSNAME") +
                                "' not found.");
                    } catch (SoundSystemException sse) {
                        printStackTrace(sse);
                    }
                } else if (command.equals("CREATE")) {
                    // Instantiate the SoundSystem
                    if (verbose)
                        message("SoundSystemLoader.loadXML:  create", 0);
                    if (s != null)
                        s.cleanup();
                    s = null;
                    parameter = commands.get("CLASSNAME");
                    if (parameter != null && !parameter.equals("")) {
                        try {
                            c = Class.forName(parameter);
                            if (verbose) {
                                message("Command:  s = (SoundSystem) new " +
                                        parameter + "();", 1);
                                s = (SoundSystem) c.newInstance();
                            }
                        } catch (ClassNotFoundException cnfe) {
                            printStackTrace(cnfe);
                        } catch (InstantiationException ie) {
                            printStackTrace(ie);
                        } catch (IllegalAccessException iae) {
                            printStackTrace(iae);
                        }
                        if (verbose) {
                            message("Unable to instantiate the Sound System " +
                                    "in method 'loadXML'  Returning null.", 1);
                            if (s != null)
                                s.cleanup();
                            s = null;
                            return null;
                        }
                    } else {
                        if (verbose)
                            message("Command:  s = new SoundSystem();", 1);
                        s = new SoundSystem();
                    }
                } else if (command.equals("LOADSOUND")) {
                    // Load a clip into memory
                    if (verbose) {
                        message("SoundSystemLoader.loadXML:  loadSound", 0);
                        message("Filename: " + commands.get("FILENAME"), 1);
                    }
                    // Make sure the SoundSystem has been instantiated
                    if (s == null) {
                        errorMessage("Encountered 'loadSound' command " +
                                "before SoundSystem was instantiated " +
                                "in method 'loadXML'.  returning null.");
                        return null;
                    }
                    if (verbose)
                        message("Command:  s.loadSound( \""
                                + commands.get("FILENAME") + "\" );", 1);
                    // Load the clip into memory
                    s.loadSound(commands.get("FILENAME"));
                } else if (command.equals("NEWSOURCE")) {
                    // Create a new Source
                    if (verbose)
                        message("SoundSystemLoader.loadXML:  newSource", 0);

                    // Make sure the SoundSystem has been instantiated
                    if (s == null) {
                        errorMessage("Encountered 'newSource' command " +
                                "before SoundSystem was instantiated " +
                                "in method 'loadXML'.  returning null.");
                        return null;
                    }
                    boolean priority = false;
                    boolean toStream = false;
                    boolean toLoop = false;
                    String sourcename = "";
                    String filename = "";
                    float x = 0;
                    float y = 0;
                    float z = 0;
                    int attModel = SoundSystemConfig.getDefaultAttenuation();
                    float distOrRoll = 0;
                    parameter = commands.get("PRIORITY");
                    if (parameter != null && !parameter.equals("")) {
                        if (verbose)
                            message("PRIORITY: " + parameter, 1);
                        if (parameter.toUpperCase().equals("TRUE"))
                            priority = true;
                    }
                    parameter = commands.get("TOSTREAM");
                    if (parameter != null && !parameter.equals("")) {
                        if (verbose)
                            message("TOSTREAM: " + parameter, 1);
                        if (parameter.toUpperCase().equals("TRUE"))
                            toStream = true;
                    }
                    parameter = commands.get("TOLOOP");
                    if (parameter != null && !parameter.equals("")) {
                        if (verbose)
                            message("TOLOOP: " + parameter, 1);
                        if (parameter.toUpperCase().equals("TRUE"))
                            toLoop = true;
                    }
                    parameter = commands.get("SOURCENAME");
                    if (parameter != null && !parameter.equals("")) {
                        if (verbose)
                            message("SOURCENAME: " + parameter, 1);
                        sourcename = parameter;
                    }
                    parameter = commands.get("FILENAME");
                    if (parameter != null && !parameter.equals("")) {
                        if (verbose)
                            message("FILENAME: " + parameter, 1);
                        filename = parameter;
                    }
                    parameter = commands.get("X");
                    if (parameter != null && !parameter.equals("")) {
                        if (verbose)
                            message("X: " + parameter, 1);
                        try {
                            x = Float.parseFloat(parameter);
                        } catch (NumberFormatException nfe) {
                            errorMessage("Error parsing float 'X' from " +
                                    "String '" + parameter + "' in " +
                                    "method 'loadXML'.  Using x=0");
                            x = 0;
                        }
                    }
                    parameter = commands.get("Y");
                    if (parameter != null && !parameter.equals("")) {
                        if (verbose)
                            message("Y: " + parameter, 1);
                        try {
                            y = Float.parseFloat(parameter);
                        } catch (NumberFormatException nfe) {
                            errorMessage("Error parsing float 'Y' from " +
                                    "String '" + parameter + "' in " +
                                    "method 'loadXML'.  Using y=0");
                            y = 0;
                        }
                    }
                    parameter = commands.get("Z");
                    if (parameter != null && !parameter.equals("")) {
                        if (verbose)
                            message("Z: " + parameter, 1);
                        try {
                            x = Float.parseFloat(parameter);
                        } catch (NumberFormatException nfe) {
                            errorMessage("Error parsing float 'Z' from " +
                                    "String '" + parameter + "' in " +
                                    "method 'loadXML'.  Using z=0");
                            z = 0;
                        }
                    }
                    parameter = commands.get("ATTMODEL");
                    if (parameter != null && !parameter.equals("")) {
                        if (verbose)
                            message("ATTMODEL: " + parameter, 1);
                        if (parameter.toUpperCase().contains("NONE"))
                            attModel = SoundSystemConfig.ATTENUATION_NONE;
                        else if (parameter.toUpperCase().contains("LINEAR"))
                            attModel = SoundSystemConfig.ATTENUATION_LINEAR;
                        else if (parameter.toUpperCase().contains("ROLLOFF"))
                            attModel = SoundSystemConfig.ATTENUATION_ROLLOFF;
                    }
                    parameter = commands.get("DISTORROLL");
                    if (parameter != null && !parameter.equals("")) {
                        if (verbose)
                            message("DISTORROLL: " + parameter, 1);
                        try {
                            distOrRoll = Float.parseFloat(parameter);
                        } catch (NumberFormatException nfe) {
                            errorMessage("Error parsing float " +
                                    "'DISTORROLL' from String '" +
                                    parameter + "' in method " +
                                    "'loadXML'.  Using default value.");
                            distOrRoll = 0;
                            if (attModel ==
                                    SoundSystemConfig.ATTENUATION_LINEAR)
                                distOrRoll =
                                        SoundSystemConfig.getDefaultFadeDistance();
                            else if (attModel ==
                                    SoundSystemConfig.ATTENUATION_ROLLOFF)
                                distOrRoll =
                                        SoundSystemConfig.getDefaultAttenuation();
                        }
                    } else {
                        if (attModel == SoundSystemConfig.ATTENUATION_LINEAR)
                            distOrRoll =
                                    SoundSystemConfig.getDefaultFadeDistance();
                        else if (attModel ==
                                SoundSystemConfig.ATTENUATION_ROLLOFF)
                            distOrRoll =
                                    SoundSystemConfig.getDefaultAttenuation();
                    }
                    if (sourcename.equals("")) {
                        errorMessage("Parameter 'SOURCENAME' not " +
                                "specified for 'NEWSOURCE' tag in " +
                                "method 'loadXML.  Unable to create " +
                                "new source.");
                    } else if (filename.equals("")) {
                        errorMessage("Parameter 'FILENAME' not " +
                                "specified for 'NEWSOURCE' tag in " +
                                "method 'loadXML.  Unable to create " +
                                "new source.");
                    } else {
                        if (verbose)
                            message("Command:  s.CommandQueue( new " +
                                    "paulscode.sound.CommandObject( " +
                                    "paulscode.sound.CommandObject." +
                                    "NEW_SOURCE, " + priority + ", " +
                                    toStream + ", " + toLoop + ", \"" +
                                    sourcename + "\", " +
                                    "new paulscode.sound.FilenameURL( \"" +
                                    filename + "\" ), " + x + ", " + y + ", " +
                                    z + ", " + attModel + ", " + distOrRoll +
                                    " ) );", 1);
                        s.CommandQueue(new paulscode.sound.CommandObject(
                                paulscode.sound.CommandObject.NEW_SOURCE, priority,
                                toStream, toLoop, sourcename,
                                new paulscode.sound.FilenameURL(filename), x, y,
                                z, attModel, distOrRoll));
                        if (verbose)
                            message("Command:  s.interruptCommandThread();",
                                    1);
                        s.interruptCommandThread();
                    }
                } else if (command.length() >= 3 &&
                        command.substring(0, 3).equals("!--")) {
                    // XML comment
                    if (verbose) {
                        message("SoundSystemLoader.loadXML:  comment", 0);
                        if (commands.contents().length() > 6)
                            message(commands.contents().substring(3,
                                    commands.contents().length() - 2), 1);
                    }
                    // Just a comment, ignore.
                } else {
                    // Tag not recognized.
                    if (verbose) {
                        message("SoundSystemLoader.loadXML:  " + command, 0);
                        message("Unrecognized tag.", 1);
                    } else {
                        warningMessage("Command '" + command + "' not " +
                                "recognized in method 'loadXML'");
                    }
                }
            }
            // Move on to the next command:
            commands = commands.next();
        }

        // Return a handle to the SoundSystem instance:
        return s;
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
     * Prints a message.
     *
     * @param message Message to print.
     */
    protected static void message(String message, int indent) {
        // Grab a handle to the logger if we don't already have one:
        if (logger == null)
            logger = SoundSystemConfig.getLogger();
        // If a logger doesn't exist, make one:
        if (logger == null) {
            logger = new SoundSystemLogger();
            SoundSystemConfig.setLogger(logger);
        }
        logger.message(message, indent);
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
        logger.errorMessage("SoundSystemLoader", message, 0);
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
        logger.importantMessage("Warning in class 'SoundSystemLoader': " +
                message, 0);
    }

    /**
     * Prints an exception's error message followed by the stack trace.
     *
     * @param e Exception containing the information to print.
     */
    protected static void printStackTrace(Exception e) {
        // Grab a handle to the logger if we don't already have one:
        if (logger == null)
            logger = SoundSystemConfig.getLogger();
        // If a logger doesn't exist, make one:
        if (logger == null) {
            logger = new SoundSystemLogger();
            SoundSystemConfig.setLogger(logger);
        }
        logger.printStackTrace(e, 1);
    }
}
