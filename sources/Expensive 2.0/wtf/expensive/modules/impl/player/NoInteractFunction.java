package wtf.expensive.modules.impl.player;

import wtf.expensive.events.Event;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.BooleanOption;
import wtf.expensive.modules.settings.imp.MultiBoxSetting;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author dedinside
 * @since 26.06.2023
 */
@FunctionAnnotation(name = "NoInteract", type = Type.Player)
public class NoInteractFunction extends Function {
    public BooleanOption allBlocks = new BooleanOption("Все блоки", false);
    public MultiBoxSetting ignoreInteract = new MultiBoxSetting("Обьекты",
            new BooleanOption("Стойки", true),
            new BooleanOption("Сундуки", true),
            new BooleanOption("Двери", true),
            new BooleanOption("Кнопки", true),
            new BooleanOption("Воронки", true),
            new BooleanOption("Раздатчики", true),
            new BooleanOption("Нотные блоки", true),
            new BooleanOption("Верстаки", true),
            new BooleanOption("Люки", true),
            new BooleanOption("Печки", true),
            new BooleanOption("Калитки", true),
            new BooleanOption("Наковальни", true),
            new BooleanOption("Рычаги", true)).setVisible(() -> !allBlocks.get());

    public NoInteractFunction() {
        addSettings(allBlocks, ignoreInteract);
    }


    public Set<Integer> getBlocks() {
        Set<Integer> blocks = new HashSet<>();
        addBlocksForInteractionType(blocks, 1, 147, 329, 270);
        addBlocksForInteractionType(blocks, 2, 173, 161, 485, 486, 487, 488, 489, 720, 721);
        addBlocksForInteractionType(blocks, 3, 183, 308, 309, 310, 311, 312, 313, 718, 719, 758);
        addBlocksForInteractionType(blocks, 4, 336);
        addBlocksForInteractionType(blocks, 5, 70, 342, 508);
        addBlocksForInteractionType(blocks, 6, 74);
        addBlocksForInteractionType(blocks, 7, 151);
        addBlocksForInteractionType(blocks, 8, 222, 223, 224, 225, 226, 227, 712, 713, 379);
        addBlocksForInteractionType(blocks, 9, 154, 670);
        addBlocksForInteractionType(blocks, 10, 250, 475, 476, 477, 478, 479, 714, 715);
        addBlocksForInteractionType(blocks, 11, 328, 327, 326);
        addBlocksForInteractionType(blocks, 12, 171);
        return blocks;
    }

    private void addBlocksForInteractionType(Set<Integer> blocks, int interactionType, Integer... blockIds) {
        if (ignoreInteract.get(interactionType)) {
            blocks.addAll(Arrays.asList(blockIds));
        }
    }

    @Override
    public void onEvent(Event event) {

    }
}
