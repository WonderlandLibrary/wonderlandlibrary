package cc.novoline.events;

/**
 * Main class for the API.
 * Contains various information about the API.
 *
 * @author DarkMagician6
 * @since July 31, 2013
 */
public final class EventAPI {

    /**
     * No need to create an Object of this class as all Methods are static.
     */
    private EventAPI() {
    }

    /**
     * The current version of the API.
     */
    public static final String VERSION = String.format("%s-%s", "0.7", "beta");

    /**
     * Array containing the authors of the API.
     */
    public static final String[] AUTHORS = {"DarkMagician6"};

}
