package net.mostlyoriginal.game.system;

import com.artemis.annotations.Wire;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.api.component.graphics.ColorAnimation;
import net.mostlyoriginal.api.component.script.Schedule;
import net.mostlyoriginal.game.manager.EntityFactorySystem;

/**
 * Spawns in monsters and treasures. Provides a scaling challenge.
 *
 *
 *
 *
 * @author Daan van Yperen
 */
@Wire
public class DirectorSystem  extends VoidEntitySystem {


    public static final int STARTING_PORTAL_INTERVAL = 20;
    public float dungeonCooldown = 2;
    public float portalCooldown = 5;

    public float dungeonInterval = 25;
    public float portalInterval = STARTING_PORTAL_INTERVAL;

    public boolean gameOver =false;

    private EntityFactorySystem entityFactory;

    @Override
    protected void processSystem() {

        if ( gameOver ) return;

        dungeonCooldown -= world.delta;
        if (dungeonCooldown <= 0)
        {
            dungeonCooldown = dungeonInterval;
            // slowly increase difficulty.

            if ( MathUtils.random(100) < 25)
            {
                entityFactory.createEntity("marker-gem", randomRadarX(), randomRadarY()).addToWorld();
            } else {
                entityFactory.createEntity("marker-dungeon", randomRadarX(), randomRadarY()).addToWorld();
            }
        }

        portalCooldown -= world.delta;
        if (portalCooldown <= 0)
        {
            portalCooldown = portalInterval;
            // slowly increase difficulty.
            portalInterval = MathUtils.clamp((portalInterval * 0.95f)-1f, 5, 9999);
            entityFactory.createEntity("marker-portal", randomRadarX(), randomRadarY())
                    .addComponent(new Schedule().wait(STARTING_PORTAL_INTERVAL+5f)
                            .add(new ColorAnimation(new Color(1, 1, 1, 1), new Color(1, 1, 1, 0), Interpolation.linear, 1f, 1f)).deleteFromWorld()).addToWorld();
        }
    }

    private int randomRadarY() {
        return entityFactory.RADAR_Y + 26 + MathUtils.random(90);
    }

    private int randomRadarX() {
        return entityFactory.RADAR_X + 3 + MathUtils.random(20);
    }

    public void gameOver() {
        gameOver =true;
    }
}
