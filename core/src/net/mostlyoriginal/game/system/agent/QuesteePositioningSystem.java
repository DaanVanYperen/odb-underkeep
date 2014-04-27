package net.mostlyoriginal.game.system.agent;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import net.mostlyoriginal.api.component.basic.Bounds;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.graphics.Anim;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.game.component.Questee;

/**
 * Places Questees where they need to be on the screen, depending if they are questing or not.
 *
 * @author Daan van Yperen
 */
@Wire
public class QuesteePositioningSystem extends EntityProcessingSystem {

    public static final int OFFSET_X = 45;
    ComponentMapper<Questee> qm;
    ComponentMapper<Pos> pm;
    ComponentMapper<Anim> am;
    CameraSystem cameraSystem;
    private int countLeft;
    private int countRight;

    public QuesteePositioningSystem() {
        super(Aspect.getAspectForAll(Questee.class, Pos.class, Bounds.class));
    }

    @Override
    protected void begin() {
        countLeft=countRight=0;
    }

    @Override
    protected void process(Entity e) {

        // don't position invisible mobs.
        if ( am.has(e) && !am.get(e).visible )
            return;

        Questee questee = qm.get(e);
        boolean placeRight = ( questee.quest != null && questee.quest.isActive());

        Pos pos = pm.get(e);
        if ( placeRight) {
            pos.x = cameraSystem.getPixelWidth() - 10 - 13 - 18 * countRight;
            countRight++;
        } else {
            pos.x = (cameraSystem.getPixelWidth() / 2 + 22) - (18 * countLeft);
            countLeft++;
        }

    }
}
