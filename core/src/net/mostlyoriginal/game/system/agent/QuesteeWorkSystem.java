package net.mostlyoriginal.game.system.agent;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.artemis.systems.EntityProcessingSystem;
import net.mostlyoriginal.game.component.Quest;
import net.mostlyoriginal.game.component.Questee;
import net.mostlyoriginal.game.component.agent.Clickable;
import net.mostlyoriginal.game.component.agent.Focusable;

/**
 * Questee entities work towards assigned quests.
 *
 * @author Daan van Yperen
 */
@Wire
public class QuesteeWorkSystem extends EntityProcessingSystem {

    ComponentMapper<Clickable> cm;
    ComponentMapper<Focusable> fm;
    ComponentMapper<Questee> qm;
    ComponentMapper<Quest> qum;
    TagManager tagManager;

    public QuesteeWorkSystem() {
        super(Aspect.getAspectForAll(Questee.class));
    }

    @Override
    protected void process(Entity e) {

        Questee questee = qm.get(e);
        if ( questee.quest != null )
        {
            if ( questee.quest.isActive() ) {
                Entity questEntity = questee.quest.get();
                Quest quest = qum.get(questEntity);
                if (quest.workRemaining > 0) {
                    workOnQuest(e, quest);
                }

                // forbid focus while working.
                if ( fm.has(e))
                {

                    e.removeComponent(Focusable.class).changedInWorld();
                }
            } else {
                // restore focus when done working.
                if ( !fm.has(e))
                {
                    e.addComponent(new Focusable()).changedInWorld();
                }
            }
        }
    }

    private void workOnQuest(Entity actor, Quest quest) {
        quest.workRemaining -= world.delta;
    }
}
