package dev.tenacity.exception;

public class InvalidConfigException extends Exception {

    @Override
    public String getMessage() {
        return "The specified config could not be found!";
    }

}
