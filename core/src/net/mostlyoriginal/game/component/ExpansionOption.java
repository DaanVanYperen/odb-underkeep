package net.mostlyoriginal.game.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class ExpansionOption extends Component {
    public final CastleBlock.Type type;
    public ExpansionOption(CastleBlock.Type type) {
        this.type = type;
    }
}
