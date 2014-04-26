package net.mostlyoriginal.game.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class ExpansionPoint extends Component {
    public int x, y;
    public boolean allowWalls;
    public boolean allowTowers;

    public ExpansionPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public ExpansionPoint() {
    }
}
