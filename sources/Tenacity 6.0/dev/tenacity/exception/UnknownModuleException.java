package dev.tenacity.exception;

import dev.tenacity.util.misc.ChatUtil;

public final class UnknownModuleException extends RuntimeException {

    private final String enteredModule;

    public UnknownModuleException(final String enteredModule) {
        super(enteredModule + " not found!");
        this.enteredModule = enteredModule;
    }

    public void printErrorToChat() {
        ChatUtil.error(enteredModule + " not found!");
    }

}
