package net.mostlyoriginal.game.component;


import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class Tutorial extends Component {
    public int step;
    public String hint;
    public Tutorial(int step, String hint) {
        this.step = step; this.hint = hint;
    }
}
