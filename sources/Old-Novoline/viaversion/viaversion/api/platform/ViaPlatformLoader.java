package viaversion.viaversion.api.platform;

public interface ViaPlatformLoader {

    /**
     * Initialise the loading for a platform, eg. registering listeners / providers / events etc.
     */
    void load();

    void unload();
}
