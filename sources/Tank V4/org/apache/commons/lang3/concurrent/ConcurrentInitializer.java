package org.apache.commons.lang3.concurrent;

public interface ConcurrentInitializer {
   Object get() throws ConcurrentException;
}
