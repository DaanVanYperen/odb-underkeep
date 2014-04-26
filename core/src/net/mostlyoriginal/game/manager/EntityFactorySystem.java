package net.mostlyoriginal.game.manager;

import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.graphics.Anim;
import net.mostlyoriginal.api.component.physics.Clamped;
import net.mostlyoriginal.api.component.physics.Physics;
import net.mostlyoriginal.api.manager.AbstractAssetSystem;
import net.mostlyoriginal.api.manager.AbstractEntityFactorySystem;
import net.mostlyoriginal.game.G;

/**
 * Game specific entity factory.
 *
 * @todo transform this into a manager.
 * @author Daan van Yperen
 */
@Wire
public class EntityFactorySystem extends AbstractEntityFactorySystem {

    private TagManager tagManager;
    private AbstractAssetSystem abstractAssetSystem;

    public Entity createEntity(String entity, int cx, int cy) {
        return createEntity(entity,cx,cy, null);
    }

    @Override
    public Entity createEntity(String entity, int cx, int cy, MapProperties properties) {
        switch (entity) {
            case "background" : return createBackground();
            case "hills" : return createHills(cx, cy);
            case "cloud" : return createCloud(cx, cy);
            /** @todo Add your entities here */
            default: throw new RuntimeException("No idea how to spawn " + entity);
        }
    }

    private Entity createCloud(int cx, int cy) {
        Physics physics = new Physics();
        physics.friction=0;
        physics.vx=MathUtils.random(0.5f,4f);

        // clamp with wrap so clouds can swoop around
        Clamped clamped = new Clamped(-40, 0, Gdx.graphics.getWidth() / G.CAMERA_ZOOM_FACTOR +10, Gdx.graphics.getHeight() / G.CAMERA_ZOOM_FACTOR);
        clamped.wrap = true;
        Anim anim = staticRandomizedAnim("cloud");
        anim.scale=MathUtils.random(1f,6f);
        anim.color.a= 0.1f + (MathUtils.random(0.1f, 0.2f) * (1f/anim.scale));
        anim.layer = -98;
        return world.createEntity()
                .addComponent(new Pos(cx, cy))
                .addComponent(anim)
                .addComponent(physics)
                .addComponent(clamped);
    }

    private Anim staticRandomizedAnim(String animId) {
        Anim anim = new Anim(animId, 1);
        anim.speed=0;
        anim.age= MathUtils.random(0, abstractAssetSystem.get(animId).animationDuration); // start at random cell.
        return anim;
    }

    private Entity createHills(int cx, int cy) {
        return world.createEntity().addComponent(new Pos(cx,cy)).addComponent(new Anim("hills", 1));
    }

    private Entity createBackground() {
        return world.createEntity().addComponent(new Pos(0,0)).addComponent(new Anim("background", -99));
    }

    @Override
    protected void initialize() {
        super.initialize();
        createEntity("background",0,0).addToWorld();
        createEntity("hills",0,26).addToWorld();

        for ( int i=0; i<20; i++)
        {
            createEntity("cloud",
                    (int)MathUtils.random(-40,Gdx.graphics.getWidth() / G.CAMERA_ZOOM_FACTOR+10),
                    (int)MathUtils.random((Gdx.graphics.getHeight() / G.CAMERA_ZOOM_FACTOR) * 0.4f,(Gdx.graphics.getHeight() / G.CAMERA_ZOOM_FACTOR)* 0.9f)).addToWorld();
        }
    }
}
