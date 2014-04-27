package net.mostlyoriginal.game.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class Incappable extends Component {
    public boolean incapacitated;
    public String animId;
    public String animIncappedId;
    public float cooldown;

    // incap duration for this entity.
    public float normalIncapDuration = 5;

    public Incappable(String animId, String animIncappedId, float normalIncapDuration) {
        this.animId = animId;
        this.animIncappedId = animIncappedId;
        this.normalIncapDuration = normalIncapDuration;
    }
}
