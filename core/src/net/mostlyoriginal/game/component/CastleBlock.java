package net.mostlyoriginal.game.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class CastleBlock extends Component {

    public static enum SubType {
        NONE,
        WALL,
        TOWER
    };

    public boolean fadeoutOnReplace;

    public static enum Type {
        EMPTY(SubType.NONE,0),
        TOWER(SubType.TOWER, 6),
        WALL(SubType.WALL, 1),
        BARRACKS(SubType.WALL, 4),
        SPELUNKER(SubType.WALL, 2);

        public final SubType subType;
        public int score;

        Type(SubType subType, int score) {


            this.subType = subType;
            this.score = score;
        }
    };

    public Type type;
}
