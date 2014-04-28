package net.mostlyoriginal.game.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class Cost  extends Component{
    public int cost;
    public final int increasePerBuy;

    public Cost( int cost, int increasePerBuy) {
        this.cost = cost;
        this.increasePerBuy = increasePerBuy;
    }
}
