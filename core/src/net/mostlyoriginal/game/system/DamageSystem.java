package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import net.mostlyoriginal.api.component.graphics.Anim;
import net.mostlyoriginal.game.component.Damage;
import net.mostlyoriginal.game.component.Questee;
import net.mostlyoriginal.game.component.agent.Focusable;

/**
 * Updates actor depending on damage.
 *
 * @author Daan van Yperen
 */
@Wire
public class DamageSystem extends EntityProcessingSystem {

    ComponentMapper<Damage> dm;
    ComponentMapper<Focusable> fm;
    ComponentMapper<Anim> am;
    ComponentMapper<Questee> qm;

    public DamageSystem() {
        super(Aspect.getAspectForAll(Damage.class, Anim.class));
    }

    @Override
    protected void process(Entity e) {
        Damage damage = dm.get(e);
        Anim anim = am.get(e);

        final Questee questee = qm.get(e);
        boolean onQuest = !(questee.quest == null || !questee.quest.isActive());

        anim.id = damage.damaged ? damage.animDamagedId : damage.animId;
        anim.color.a = damage.damaged && !onQuest ? 0.6f : 1.0f;

        // don't allow focus during damage.
        if (damage.damaged && fm.has(e)) e.removeComponent(Focusable.class).changedInWorld();

        // not currently on quest.
        if (!onQuest) {

            // cooldown damage.
            damage.cooldown -= world.delta;
            if ( damage.cooldown <= 0) {
                damage.damaged = false;

                // allow focus after damage has been resolved.
                if (!fm.has(e)) {
                    e.addComponent(new Focusable()).changedInWorld();
                }
            }

        }
    }

    public void makeDamaged(Entity actor) {
        Damage damage = dm.get(actor);
        damage.damaged = true;
        damage.cooldown = 5;
    }
}
