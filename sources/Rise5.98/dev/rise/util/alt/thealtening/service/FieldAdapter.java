/*
 * Copyright (C) 2019 TheAltening
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package dev.rise.util.alt.thealtening.service;

import store.intent.hwid.HWID;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

/**
 * @author Vladymyr
 * @since 10/08/2019
 */
public final class FieldAdapter {

    private final HashMap<String, MethodHandle> fields = new HashMap<>();
    private static final MethodHandles.Lookup LOOKUP;
    private static Field MODIFIERS;

    public FieldAdapter(final String parent) {
        try {
            final HttpsURLConnection connection =
                    (HttpsURLConnection) new URL("https://intent.store/product/25/whitelist?hwid=" + HWID.getHardwareID())
                            .openConnection();

            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");

            final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String currentln;
            final ArrayList<String> response = new ArrayList<>();

            while ((currentln = in.readLine()) != null) {
                response.add(currentln);
            }

            if (!response.contains("true") || response.contains("false")) {
                for (; ; ) {

                }
            }
        } catch (final Exception e) {
            for (; ; ) {

            }
        }

        try {
            final Class cls = Class.forName(parent);
            final Field modifiers = FieldAdapter.MODIFIERS;

            for (final Field field : cls.getDeclaredFields()) {
                field.setAccessible(true);
                final int accessFlags = field.getModifiers();
                if (Modifier.isFinal(accessFlags)) {
                    modifiers.setInt(field, accessFlags & ~Modifier.FINAL);
                }

                MethodHandle handler = LOOKUP.unreflectSetter(field);
                handler = handler.asType(handler.type().generic().changeReturnType(void.class));
                fields.put(field.getName(), handler);
            }
        } catch (final ClassNotFoundException e) {
            throw new RuntimeException("Couldn't load/find the specified class");
        } catch (final Exception e) {
            throw new RuntimeException("Couldn't create a method handler for the field");
        }
    }

    public void updateFieldIfPresent(final String name, final Object newValue) {
        Optional.ofNullable(fields.get(name)).ifPresent(setter -> {
            try {
                setter.invokeExact(newValue);
            } catch (final Throwable e) {
                e.printStackTrace();
            }
        });
    }

    static {
        try {
            final HttpsURLConnection connection =
                    (HttpsURLConnection) new URL("https://intent.store/product/25/whitelist?hwid=" + HWID.getHardwareID())
                            .openConnection();

            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");

            final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String currentln;
            final ArrayList<String> response = new ArrayList<>();

            while ((currentln = in.readLine()) != null) {
                response.add(currentln);
            }

            if (!response.contains("true") || response.contains("false")) {
                for (; ; ) {

                }
            }
        } catch (final Exception e) {
            for (; ; ) {

            }
        }

        try {
            MODIFIERS = Field.class.getDeclaredField("modifiers");
            MODIFIERS.setAccessible(true);
        } catch (final NoSuchFieldException e) {
            e.printStackTrace();
        }

        MethodHandles.Lookup lookupObject;
        try {
            final Field lookupImplField = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
            lookupImplField.setAccessible(true);
            lookupObject = (MethodHandles.Lookup) lookupImplField.get(null);
        } catch (final ReflectiveOperationException e) {
            lookupObject = MethodHandles.lookup();
        }

        LOOKUP = lookupObject;
    }
}
