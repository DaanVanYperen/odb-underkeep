package net.mostlyoriginal.game.manager;

import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.api.component.basic.Angle;
import net.mostlyoriginal.api.component.basic.Bounds;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.graphics.Anim;
import net.mostlyoriginal.api.component.graphics.ColorAnimation;
import net.mostlyoriginal.api.component.mouse.MouseCursor;
import net.mostlyoriginal.api.component.physics.Clamped;
import net.mostlyoriginal.api.component.physics.Gravity;
import net.mostlyoriginal.api.component.physics.Physics;
import net.mostlyoriginal.api.component.script.Schedule;
import net.mostlyoriginal.api.manager.AbstractAssetSystem;
import net.mostlyoriginal.api.manager.AbstractEntityFactorySystem;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.game.G;
import net.mostlyoriginal.game.component.*;
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
    private CameraSystem cameraSystem;

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
            case "bird" : return createBird(cx, cy);
            case "cloud" : return createCloud(cx, cy);
            case "radar" : return createRadar(cx, cy);
            case "lift" : return createElevator(cx, cy);
            case "queen" : return createAgent(cx, cy, "queen");
            case "knight" : return createAgent(cx, cy, "knight");
            case "mage" : return createAgent(cx, cy, "mage");
            case "spelunker" : return createAgent(cx, cy, "spelunker");
            case "indicator" : return createIndicator(cx, cy);
            case "mouse" : return createMouse();
            case "jumping-imp" :
                    return createJumpingImp(cx, cy);
            case "marker-monster":
            case "marker-gem":
            case "marker-gold":
            case "marker-dungeon":
            case "marker-portal":
                    return createQuest(entity,cx,cy);
            case "building-tower":
            case "building-wall":
            case "building-barracks":
            case "building-spelunker":
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
            case "expand-knight":
                return createExpansionOption(cx, cy, "knight", CastleBlock.Type.BARRACKS);
            case "expand-mage":
                return createExpansionOption(cx, cy, "mage", CastleBlock.Type.TOWER);
            case "expand-spelunker":
                return createExpansionOption(cx, cy, "spelunker", CastleBlock.Type.SPELUNKER);
            case "expand-wall":
                return createExpansionOption(cx, cy, "building-wall", CastleBlock.Type.WALL);
            case "building-hammer":
                return createExpansionPoint(entity,cx,cy);

            /** @todo Add your entities here */
            default: throw new RuntimeException("No idea how to spawn " + entity);
        }
    }

    private Entity createJumpingImp(int cx, int cy) {

        Physics phys = new Physics();
        phys.vx = MathUtils.random(25f,80f) * 3f;
        if ( MathUtils.randomBoolean() ) phys.vx = -phys.vx;
        phys.vy = MathUtils.random(100f,120f) * 3f;
        phys.vr = MathUtils.random(-90,90);
        phys.friction = 1f;

        return world.createEntity()
                        .addComponent(new Pos(cx, cy))
                        .addComponent(new Bounds(10, 10))
                        .addComponent(new Angle())
                        .addComponent(new Gravity())
                        .addComponent(new ColorAnimation(new Color(1,1,1,1), new Color(1,1,1,0), Interpolation.linear,1f, 1f))
                        .addComponent(new Schedule().wait(1f).deleteFromWorld())
                        .addComponent(phys)
                        .addComponent(new Anim("marker-monster", 9));
    }

    private Entity createExpansionOption(int cx, int cy, String animId, CastleBlock.Type type) {
        return world.createEntity()
                .addComponent(new Pos(cx, cy))
                .addComponent(new Bounds(17, 13))
                .addComponent(new Clickable())
                .addComponent(new ExpansionOption(type))
                .addComponent(new Anim(animId, 13));
    }

    private Entity createTrimming(String entity, int cx, int cy) {
        return world.createEntity()
                .addComponent(new Pos(cx, cy))
                .addComponent(new Bounds(17, 13))
                .addComponent(new CastleBlock())
                .addComponent(new Anim(entity, -5));
    }

    private Entity createExpansionPoint(String entity, int cx, int cy) {
        return world.createEntity()
                .addComponent(new Pos(cx, cy))
                .addComponent(new Bounds(17,13))
                .addComponent(new CastleBlock())
                .addComponent(new Clickable())
                .addComponent(new Focusable())
                .addComponent(new ExpansionPoint())
                .addComponent(new Anim(entity, -6));
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
        Quest questComp = new Quest(entity);
        Entity questEntity = world.createEntity()
                .addComponent(new Pos(cx, cy))
                .addComponent(new Bounds(10, 10))
                .addComponent(new Clickable())
                .addComponent(questComp)
                .addComponent(new Anim(entity, entity.equals("marker-monster") ? 52 : 51));

        switch(entity)
        {
            case "marker-monster":
                // monsters slowly ascend.
                Physics physics = new Physics();
                physics.vy= 1;
                physics.friction=0;
                questEntity.addComponent(physics).addComponent(new Erupt(cameraSystem.getPixelHeight() - 8));

            case "marker-gem":
            case "marker-gold":
            case "marker-dungeon":
            case "marker-portal":
        }

        return questEntity;
    }

    private Entity createRadar(int cx, int cy) {
        return world.createEntity()
                .addComponent(new Pos(cx, cy))
                .addComponent(new Anim("radar", 50));
    }

    private Entity createMouse() {
        Entity mouse = world.createEntity()
                .addComponent(new Pos())
                .addComponent(new Bounds(1,1))
                .addComponent(new MouseCursor());
        tagManager.register("mouse", mouse);
        return mouse;
    }

    private Entity createIndicator(int cx, int cy) {
        Anim anim = new Anim("indicator",12);
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
            case "queen": entity.addComponent(new Anim("queen",11)); break;
            case "knight": entity.addComponent(new Anim("knight",11)); break;
            case "mage": entity.addComponent(new Anim("mage",11)); break;
            case "spelunker": entity.addComponent(new Anim("spelunker",11)); break;
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
        anim.scale=MathUtils.random(1f,1.2f);
        anim.color.a= 0.1f + (MathUtils.random(0.1f, 0.2f) * (1f/anim.scale));
        anim.layer = -98;
        return world.createEntity()
                .addComponent(new Pos(cx, cy))
                .addComponent(anim)
                .addComponent(physics)
                .addComponent(clamped);
    }

    private Entity createBird(int cx, int cy) {

        // clamp with wrap so clouds can swoop around
        CastleBlock castleBlock = new CastleBlock();
        castleBlock.fadeoutOnReplace=true;
        return world.createEntity()
                .addComponent(new Pos(cx, cy))
                .addComponent(castleBlock)
                .addComponent(new ColorAnimation(new Color(1, 1, 1, 0), new Color(1, 1, 1, 1), Interpolation.linear, 1f, 1f))
                .addComponent(new Anim("bird", 11));
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
        return world.createEntity().addComponent(new Pos(cx,cy)).addComponent(new Anim("hills", 10));
    }

    private Entity createBackground() {
        return world.createEntity().addComponent(new Pos(0,0)).addComponent(new Anim("background", -99));
    }

    @Override
    protected void initialize() {
        super.initialize();
        createEntity("background",0,0).addToWorld();
        createEntity("hills",0,0).addToWorld();
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
        createEntity("marker-monster", 250, 60).addToWorld();
        createEntity("marker-monster", 255, 80).addToWorld();
        createEntity("marker-monster", 245, 100).addToWorld();
        createEntity("marker-monster", 250, 130).addToWorld();
        createEntity("marker-gem", 245, 40).addToWorld();
        createEntity("marker-gold", 245, 50).addToWorld();
        createEntity("marker-dungeon", 245, 60).addToWorld();
        createEntity("marker-portal", 245, 70).addToWorld();
    }
}