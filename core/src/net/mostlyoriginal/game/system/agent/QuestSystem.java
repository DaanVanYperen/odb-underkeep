package net.mostlyoriginal.game.system.agent;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.artemis.systems.EntityProcessingSystem;
import net.mostlyoriginal.api.component.basic.Bounds;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.utils.SafeEntityReference;
import net.mostlyoriginal.game.component.Quest;
import net.mostlyoriginal.game.component.Questee;
import net.mostlyoriginal.game.component.agent.Clickable;
import net.mostlyoriginal.game.system.UIWalletSystem;

/**
 * Sends selected Qeestee on quest after the quest is clicked.
 *
 * @author Daan van Yperen
 */
@Wire
public class QuestSystem extends EntityProcessingSystem {

    ComponentMapper<Clickable> cm;
    ComponentMapper<Questee> qm;
    ComponentMapper<Quest> qum;
    TagManager tagManager;

    UIWalletSystem uiWalletSystem;

    public QuestSystem() {
        super(Aspect.getAspectForAll(Clickable.class, Pos.class, Quest.class, Bounds.class));
    }

    @Override
    protected void process(Entity e) {

        if ( cm.get(e).clicked && tagManager.isRegistered("focus") )
        {
            activateQuestFor( e, tagManager.getEntity("focus"));
        }

        Quest quest = qum.get(e);
        if ( quest.workRemaining <= 0 )
        {
            payoutQuest(e);
            e.deleteFromWorld();
        }
    }

    private void payoutQuest(Entity e) {
        final Quest quest = qum.get(e);

        if ( quest.gold > 0 )
        {
            uiWalletSystem.add(quest.gold);
        }
    }

    private void activateQuestFor(Entity quest, Entity actor) {
        if ( qm.has(actor) )
        {
            final Questee questee = qm.get(actor);
            if ( questee.quest == null || !questee.quest.isActive() )
            {
                tagManager.unregister("focus");
                questee.quest = new SafeEntityReference(quest);
            }
        }


    }
}
