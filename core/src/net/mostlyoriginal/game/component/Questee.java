package net.mostlyoriginal.game.component;

import com.artemis.Component;
import net.mostlyoriginal.api.utils.EntityReference;

import java.util.UUID;

/**
 * For quest takers.
 *
 * @author Daan van Yperen
 */
public class Questee extends Component {
    public EntityReference quest;
    public UUID tracker;
    public float travelSpeed=1;
    public float workSpeed=1;
}
