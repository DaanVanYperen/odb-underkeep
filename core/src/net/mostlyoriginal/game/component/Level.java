package net.mostlyoriginal.game.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class Level extends Component{

    public int level;
    public final String type;

    public Level(int level, String type) {
        this.level = level;
        this.type = type;
    }
}
