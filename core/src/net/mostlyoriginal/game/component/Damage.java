package net.mostlyoriginal.game.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class Damage extends Component {
    public boolean damaged;
    public String animId;
    public String animDamagedId;
    public float cooldown;

    public Damage( String animId, String animDamagedId) {
        this.animId = animId;
        this.animDamagedId = animDamagedId;
    }
}
