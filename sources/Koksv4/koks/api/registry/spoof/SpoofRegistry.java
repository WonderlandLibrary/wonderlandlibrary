package koks.api.registry.spoof;

import koks.api.registry.Registry;
import koks.spoof.Badlion;
import koks.spoof.Lunar;
import koks.spoof.Vanilla;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

/**
 * Copyright 2021, Koks Team
 * Please don't use the code
 */

@Getter @Setter
public class SpoofRegistry implements Registry {

    @Getter @Setter
    public static Spoof currentSpoofed;
    @Getter
    private static final ArrayList<Spoof> SPOOFS = new ArrayList<>();

    @Override
    public void initialize() {
        addSpoof(new Vanilla());
        addSpoof(new Lunar());
        //addSpoof(new Badlion());
        setCurrentSpoofed(getSpoof(Vanilla.class));
    }

    public void addSpoof(Spoof spoof) {
        SPOOFS.add(spoof);
    }

    public Spoof getSpoof(Class<?> clazz) {
        return SPOOFS.stream().filter(spoof -> spoof.getClass().equals(clazz)).findAny().orElse(null);
    }

    public void handleSpoof() {
        currentSpoofed.handleSpoof();
    }
}
