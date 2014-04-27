package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import net.mostlyoriginal.api.component.graphics.Anim;
import net.mostlyoriginal.game.component.Incappable;
import net.mostlyoriginal.game.component.Questee;
import net.mostlyoriginal.game.component.agent.Focusable;

/**
 * Updates actor depending on damage.
 *
 * @author Daan van Yperen
 */
@Wire
public class IncapacitateSystem extends EntityProcessingSystem {

    ComponentMapper<Incappable> dm;
    ComponentMapper<Focusable> fm;
    ComponentMapper<Anim> am;
    ComponentMapper<Questee> qm;

    public IncapacitateSystem() {
        super(Aspect.getAspectForAll(Incappable.class, Anim.class));
    }

    @Override
    protected void process(Entity e) {
        Incappable incappable = dm.get(e);
        Anim anim = am.get(e);

        final Questee questee = qm.get(e);
        boolean onQuest = !(questee.quest == null || !questee.quest.isActive());

        anim.id = incappable.incapacitated ? incappable.animIncappedId : incappable.animId;
        anim.color.a = incappable.incapacitated && !onQuest ? 0.6f : 1.0f;

        // don't allow focus during damage.
        if (incappable.incapacitated && fm.has(e)) e.removeComponent(Focusable.class).changedInWorld();

        // not currently on quest.
        if (!onQuest) {

            // cooldown damage.
            incappable.cooldown -= world.delta;
            if ( incappable.cooldown <= 0) {
                incappable.incapacitated = false;

                // allow focus after damage has been resolved.
                if (!fm.has(e)) {
                    e.addComponent(new Focusable()).changedInWorld();
                }
            }

        }
    }

    public void makeDamaged(Entity actor) {
        Incappable incappable = dm.get(actor);
        incappable.incapacitated = true;
        incappable.cooldown = incappable.normalIncapDuration;
    }
}
