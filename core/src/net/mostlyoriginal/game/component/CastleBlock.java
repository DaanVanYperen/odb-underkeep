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
        EMPTY(SubType.NONE),
        TOWER(SubType.TOWER),
        WALL(SubType.WALL),
        BARRACKS(SubType.WALL),
        SPELUNKER(SubType.WALL);

        public final SubType subType;

        Type(SubType subType) {


            this.subType = subType;
        }
    };

    public Type type;
}
