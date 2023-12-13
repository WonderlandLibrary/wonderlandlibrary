package koks.api.exceptions;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

public class CastNotPossibleException extends RuntimeException {
    public CastNotPossibleException(String type, String otherType) {
        super("Cannot cast ValueType: " + type + " to an: " + otherType);
    }
}