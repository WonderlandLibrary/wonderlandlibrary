// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.tutorial;

import java.util.function.Function;

public enum TutorialSteps
{
    MOVEMENT("movement", (Function<Tutorial, T>)MovementStep::new), 
    FIND_TREE("find_tree", (Function<Tutorial, T>)FindTreeStep::new), 
    PUNCH_TREE("punch_tree", (Function<Tutorial, T>)PunchTreeStep::new), 
    OPEN_INVENTORY("open_inventory", (Function<Tutorial, T>)OpenInventoryStep::new), 
    CRAFT_PLANKS("craft_planks", (Function<Tutorial, T>)CraftPlanksStep::new), 
    NONE("none", (Function<Tutorial, T>)CompletedTutorialStep::new);
    
    private final String name;
    private final Function<Tutorial, ? extends ITutorialStep> tutorial;
    
    private <T extends ITutorialStep> TutorialSteps(final String nameIn, final Function<Tutorial, T> constructor) {
        this.name = nameIn;
        this.tutorial = constructor;
    }
    
    public ITutorialStep create(final Tutorial tutorial) {
        return (ITutorialStep)this.tutorial.apply(tutorial);
    }
    
    public String getName() {
        return this.name;
    }
    
    public static TutorialSteps getTutorial(final String tutorialName) {
        for (final TutorialSteps tutorialsteps : values()) {
            if (tutorialsteps.name.equals(tutorialName)) {
                return tutorialsteps;
            }
        }
        return TutorialSteps.NONE;
    }
}
