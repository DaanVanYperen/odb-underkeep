package net.mostlyoriginal.game.manager;

import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.api.component.basic.Bounds;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.graphics.Anim;
import net.mostlyoriginal.api.component.mouse.MouseCursor;
import net.mostlyoriginal.api.component.physics.Clamped;
import net.mostlyoriginal.api.component.physics.Physics;
import net.mostlyoriginal.api.manager.AbstractAssetSystem;
import net.mostlyoriginal.api.manager.AbstractEntityFactorySystem;
import net.mostlyoriginal.game.G;
import net.mostlyoriginal.game.component.CastleBlock;
import net.mostlyoriginal.game.component.Quest;
import net.mostlyoriginal.game.component.Questee;
import net.mostlyoriginal.game.component.agent.Clickable;
import net.mostlyoriginal.game.component.agent.Focusable;

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
    public Entity createEntity(String entity) {
        return createEntity(entity,0,0, null);
    }

    @Override
    public Entity createEntity(String entity, int cx, int cy, MapProperties properties) {
        switch (entity) {
            case "background" : return createBackground();
            case "hills" : return createHills(cx, cy);
            case "cloud" : return createCloud(cx, cy);
            case "radar" : return createRadar(cx, cy);
            case "lift" : return createElevator(cx, cy);
            case "queen" : return createAgent(cx, cy, "queen");
            case "knight" : return createAgent(cx, cy, "knight");
            case "mage" : return createAgent(cx, cy, "mage");
            case "spelunker" : return createAgent(cx, cy, "spelunker");
            case "indicator" : return createIndicator(cx, cy);
            case "mouse" : return createMouse();
            case "marker-monster":
            case "marker-gem":
            case "marker-gold":
            case "marker-dungeon":
            case "marker-portal": return createQuest(entity,cx,cy);
            case "building-tower":
            case "building-wall":
            case "building-barracks":
                    return createBlock(entity, cx, cy);
            case "building-flag":
            case "building-trimming-on-tower":
            case "building-trimming-on-wall-left":
            case "building-trimming-on-wall":
            case "building-trimming-on-wall-right":
            case "building-trimming-top-wall-left":
            case "building-trimming-top-wall-right":
            case "building-trimming-bottom-wall-left":
            case "building-trimming-bottom-wall-right":
                return createTrimming(entity,cx,cy);

            /** @todo Add your entities here */
            default: throw new RuntimeException("No idea how to spawn " + entity);
        }
    }

    private Entity createTrimming(String entity, int cx, int cy) {
        return world.createEntity()
                .addComponent(new Pos(cx, cy))
                .addComponent(new Bounds(17,13))
                .addComponent(new CastleBlock())
                .addComponent(new Anim(entity, -5));
    }

    private Entity createBlock(String entity, int cx, int cy) {
        Anim anim = staticRandomizedAnim(entity);
        anim.layer=-10;
        return world.createEntity()
                .addComponent(new Pos(cx, cy))
                .addComponent(new Bounds(17,13))
                .addComponent(new CastleBlock())
                .addComponent(anim);


        //add("building-hammer", 0,51, 13, 17,1);
       // add("building-flag", 26,51, 13, 17,2);
       // add("building-barracks", 0,68, 13, 17,1);
       // add("building-wall",13,68, 13, 17,3);
       // add("building-tower", 52,68, 13, 17,1);
      //  add("building-trimming-on-tower",0,85, 13, 17,1);
      //  add("building-trimming-on-wall-left",13,85, 13, 17,1);
       // add("building-trimming-on-wall",26,85, 13, 17,1);
       // add("building-trimming-on-wall-right",39,85, 13, 17,1);
      //  add("building-trimming-top-wall-left",13,102, 13, 17,1);
      //  add("building-trimming-top-wall-right",39,102, 13, 17,1);
       // add("building-trimming-bottom-wall-left",13,119, 13, 17,1);
        //add("building-trimming-bottom-wall-right",39,119, 13, 17,1);

    }

    private Entity createQuest(String entity, int cx, int cy) {
        return world.createEntity()
                .addComponent(new Pos(cx, cy))
                .addComponent(new Bounds(10,10))
                .addComponent(new Clickable())
                .addComponent(new Quest(entity))
                .addComponent(new Anim(entity, 51));
    }

    private Entity createRadar(int cx, int cy) {
        return world.createEntity()
                .addComponent(new Pos(cx, cy))
                .addComponent(new Anim("radar", 50));
    }

    private Entity createMouse() {
        Entity mouse = world.createEntity()
                .addComponent(new Pos())
                .addComponent(new Bounds(-2, -2, 2, 2))
                .addComponent(new MouseCursor());
        tagManager.register("mouse", mouse);
        return mouse;
    }

    private Entity createIndicator(int cx, int cy) {
        Anim anim = new Anim("indicator");
        anim.visible=false;
        Entity indicator = world.createEntity()
                .addComponent(new Pos(cx, cy))
                .addComponent(anim);
        tagManager.register("indicator",indicator);
        return indicator;
    }

    private Entity createAgent(int cx, int cy, String type) {

        Entity entity = world.createEntity()
                .addComponent(new Pos(cx, cy))
                .addComponent(new Bounds(17,13))
                .addComponent(new Clickable())
                .addComponent(new Questee())
                .addComponent(new Focusable());

        switch(type)
        {
            case "queen": entity.addComponent(new Anim("queen")); break;
            case "knight": entity.addComponent(new Anim("knight")); break;
            case "mage": entity.addComponent(new Anim("mage")); break;
            case "spelunker": entity.addComponent(new Anim("spelunker")); break;
            default: throw new RuntimeException("unknown agent type " + type);
        }

        return entity;
    }

    private Entity createCloud(int cx, int cy) {
        Physics physics = new Physics();
        physics.friction=0;
        physics.vx=MathUtils.random(0.2f,2f);

        // clamp with wrap so clouds can swoop around
        Clamped clamped = new Clamped(-40, 0, Gdx.graphics.getWidth() / G.CAMERA_ZOOM_FACTOR +10, Gdx.graphics.getHeight() / G.CAMERA_ZOOM_FACTOR);
        clamped.wrap = true;
        Anim anim = staticRandomizedAnim("cloud");
        anim.scale=MathUtils.random(1f,4f);
        anim.color.a= 0.1f + (MathUtils.random(0.1f, 0.2f) * (1f/anim.scale));
        anim.layer = -98;
        return world.createEntity()
                .addComponent(new Pos(cx, cy))
                .addComponent(anim)
                .addComponent(physics)
                .addComponent(clamped);
    }

    private Entity createElevator(int cx, int cy) {
        world.createEntity()
                .addComponent(new Pos(cx, cy))
                .addComponent(new Anim("lift-frame", -80)).addToWorld();
        Entity cage = world.createEntity()
                .addComponent(new Pos(cx+3, cy))
                .addComponent(new Anim("lift-cage", -79));
        return cage;
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
        createEntity("lift",211,30).addToWorld();

        createEntity("queen",5, 5).addToWorld();
        createEntity("knight",25,5).addToWorld();
        createEntity("mage",45,5).addToWorld();
        createEntity("spelunker",65,5).addToWorld();

        for ( int i=0; i<20; i++)
        {
            createEntity("cloud",
                    (int)MathUtils.random(-40,Gdx.graphics.getWidth() / G.CAMERA_ZOOM_FACTOR+10),
                    (int)MathUtils.random((Gdx.graphics.getHeight() / G.CAMERA_ZOOM_FACTOR) * 0.4f,(Gdx.graphics.getHeight() / G.CAMERA_ZOOM_FACTOR)* 0.9f)).addToWorld();
        }

        createEntity("indicator").addToWorld();
        createEntity("mouse").addToWorld();
        createEntity("radar",240,26).addToWorld();

        createEntity("marker-monster", 245, 30).addToWorld();
        createEntity("marker-gem", 245, 40).addToWorld();
        createEntity("marker-gold", 245, 50).addToWorld();
        createEntity("marker-dungeon", 245, 60).addToWorld();
        createEntity("marker-portal", 245, 70).addToWorld();
    }
}
