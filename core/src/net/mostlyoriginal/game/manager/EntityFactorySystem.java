package net.mostlyoriginal.game.manager;

import com.artemis.Entity;
import com.artemis.EntityEdit;
import com.artemis.annotations.Wire;
import com.artemis.managers.GroupManager;
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

    public static final int RADAR_X = 5;
    public static final int RADAR_Y = 26;
    public static final int SERVANT_Y = 14;
    private TagManager tagManager;
    private AbstractAssetSystem abstractAssetSystem;
    private CameraSystem cameraSystem;
    private GroupManager groupManager;

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
            case "tracker" : return createTracker(RADAR_X, RADAR_Y + 122);
            case "mouse" : return createMouse();
            case "particle-debris":
                return createParticleDebris(cx, cy);
            case "particle-coin":
                return createParticleCoin(cx, cy);
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
                return createExpansionOption(cx, cy, "buy-knight", CastleBlock.Type.BARRACKS)
                        .add(new Hint("Barracks, extra knight.")).getEntity();
            case "expand-mage":
                return createExpansionOption(cx, cy, "buy-mage", CastleBlock.Type.TOWER)
                        .add(new Hint("Mage tower, extra mage.")).getEntity();
            case "expand-spelunker":
                return createExpansionOption(cx, cy, "buy-spelunker", CastleBlock.Type.SPELUNKER)
                        .add(new Hint("Workshop, extra spelunker.")).getEntity();
            case "expand-wall":
                return createExpansionOption(cx, cy, "buy-wall", CastleBlock.Type.WALL)
                        .add(new Hint("Wall, cheap reinforcement")).getEntity();
            case "building-hammer":
                return createExpansionPoint(entity,cx,cy);

            /** @todo Add your entities here */
            default: throw new RuntimeException("No idea how to spawn " + entity);
        }
    }

    private Entity createTracker(int cx, int cy) {
        return world.createEntity().edit().add(new Pos(cx, cy))
                .add(new Anim("lift-dot", 51))
                .add(new Physics())
                .add(new Bounds(2, 2)).getEntity();
    }

    private Entity createJumpingImp(int cx, int cy) {

        Physics phys = new Physics();
        phys.vx = MathUtils.random(25f,80f) * 3f;
        if ( MathUtils.randomBoolean() ) phys.vx = -phys.vx;
        phys.vy = MathUtils.random(100f,120f) * 3f;
        phys.vr = MathUtils.random(-90,90);
        phys.friction = 1f;

        return world.createEntity()
		        .edit()
                        .add(new Pos(cx, cy))
                        .add(new Bounds(10, 10))
                        .add(new Angle())
                        .add(new Gravity())
                        .add(new ColorAnimation(new Color(1, 1, 1, 1), new Color(1, 1, 1, 0), Interpolation.linear, 1f, 1f))
                        .add(new Schedule().wait(1f).deleteFromWorld())
                        .add(phys)
                        .add(new Anim("marker-monster", 9)).getEntity();
    }

    private Entity createParticleDebris(int cx, int cy) {

        Physics phys = new Physics();
        phys.vx = MathUtils.random(25f,80f) * 3f;
        if ( MathUtils.randomBoolean() ) phys.vx = -phys.vx;
        phys.vy = MathUtils.random(100f,120f) * 2f;
        phys.vr = MathUtils.random(-90,90);
        phys.friction = 3f;

        Anim anim = staticRandomizedAnim("particle-debris");
        anim.layer=9;
        return world.createEntity()
		        .edit()
                        .add(new Pos(cx, cy))
                        .add(new Bounds(6, 5))
                        .add(new Angle())
                        .add(new Gravity())
                        .add(new Schedule()
		                        .wait(MathUtils.random(0.1f, 0.25f))
		                        .add(new ColorAnimation(new Color(1, 1, 1, 1), new Color(1, 1, 1, 0), Interpolation.linear, 0.5f, 0.5f))
		                        .wait(1f)
		                        .deleteFromWorld())
                        .add(phys)
                        .add(anim).getEntity();
    }

    private Entity createParticleCoin(int cx, int cy) {

        Physics phys = new Physics();
        phys.vx = MathUtils.random(25f,80f) * 3f;
        if ( MathUtils.randomBoolean() ) phys.vx = -phys.vx;
        phys.vy = MathUtils.random(100f,120f) * 2f;
        phys.vr = MathUtils.random(-90,90);
        phys.friction = 3f;

        Anim anim = staticRandomizedAnim("particle-coin");
        anim.layer=200;
        return world.createEntity().edit()
                        .add(new Pos(cx, cy))
                        .add(new Bounds(6,5))
                        .add(new Angle())
                        .add(new Gravity())
                        .add(new Schedule()
                                .wait(MathUtils.random(0.1f, 0.25f))
                                .add(new ColorAnimation(new Color(1, 1, 1, 1), new Color(1, 1, 1, 0), Interpolation.linear, 0.5f, 0.5f))
                                .wait(1f)
                                .deleteFromWorld())
                        .add(phys)
                        .add(anim).getEntity();
    }

    private EntityEdit createExpansionOption(int cx, int cy, String animId, CastleBlock.Type type) {
        return world.createEntity()
		        .edit()
                .add(new Pos(cx, cy))
                .add(new Bounds(17, 13))
                .add(new Clickable())
                .add(new Focusable())
                .add(new ExpansionOption(type))
                .add(new Anim(animId, 13));
    }

    private Entity createTrimming(String entity, int cx, int cy) {
        return world.createEntity()
		        .edit()
                .add(new Pos(cx, cy))
                .add(new Bounds(17, 13))
                .add(new CastleBlock())
                .add(new Anim(entity, -5)).getEntity();
    }

    private Entity createExpansionPoint(String entity, int cx, int cy) {
        return world.createEntity()
		        .edit()
                .add(new Pos(cx, cy))
                .add(new Bounds(17, 13))
                .add(new CastleBlock())
                .add(new Clickable())
                .add(new ExpansionPoint())
                .add(new Anim(entity, -6)).getEntity();
    }

    private Entity createBlock(String entity, int cx, int cy) {
        Anim anim = staticRandomizedAnim(entity);
        anim.layer=-10;
        return world.createEntity()
		        .edit()
                .add(new Pos(cx, cy))
                .add(new Bounds(17, 13))
                .add(new CastleBlock())
                .add(anim).getEntity();


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
        Entity questEntity = world.createEntity().edit()
                .add(new Pos(cx, cy))
                .add(new Bounds(10, 10))
                .add(new Clickable())
                .add(new ColorAnimation(new Color(1,1,1,0), new Color(1,1,1,1), Interpolation.linear,1f, 1f))
                .add(questComp)
                .add(new Anim(entity, entity.equals("marker-monster") ? 53 : 52)).getEntity();;

        switch(entity)
        {
            case "marker-monster":
                questEntity.edit().add(new Hint("Approaching Imp."));
                // monsters slowly ascend.
                questComp.dangerous = true;
                Physics physics = new Physics();
                physics.vy= 2;
                physics.friction=0;
                questEntity.edit().add(physics).add(new Erupt(cameraSystem.getPixelHeight() - 8));
                questComp.gold = MathUtils.random(2,4);
                break;
            case "marker-gem":
                questEntity.edit().add(new Hint("Valuable gem"));
                questComp.gold = 30;
                break;
            case "marker-gold":
                questComp.gold = 10;
                questEntity.edit().add(new Hint("Heap of gold!"));
                break;
            case "marker-dungeon":
                questEntity.edit().add(new Hint("Dungeon"));
                questComp.dangerous = true;
                questComp.freeForAll = true;
                questComp.spawnGold = true; // dungeons explode into gold!
                break;
            case "marker-portal":
                questEntity.edit().add(new Hint("Portal"));
                questComp.workable=false;
                questComp.dangerous = true;
                questComp.spawnMonsters = true;
                break;
        }

        return questEntity;
    }

    private Entity createRadar(int cx, int cy) {
        return world.createEntity()
		        .edit().add(new Pos(cx, cy))
                .add(new Anim("radar", 50)).getEntity();
    }

    private Entity createMouse() {
        Entity mouse = world.createEntity()
		        .edit().add(new Pos())
                .add(new Bounds(1, 1))
                .add(new MouseCursor()).getEntity();
        tagManager.register("mouse", mouse);
        return mouse;
    }

    private Entity createIndicator(int cx, int cy) {
        Anim anim = new Anim("indicator",12);
        anim.visible=false;
        Entity indicator = world.createEntity()
		        .edit().add(new Pos(cx, cy))
                .add(anim).getEntity();
        tagManager.register("indicator",indicator);
        return indicator;
    }

    private Entity createAgent(int cx, int cy, String type) {

        Questee questee = new Questee();
        Entity entity = world.createEntity()
		        .edit().add(new Pos(cx, cy))
                .add(new Bounds(17, 13))
                .add(new Clickable())
                .add(questee)
                .add(new Level(1, type))
                .add(new Focusable()).getEntity();

        groupManager.add(entity, type);

        switch(type)
        {
            case "queen":
                questee.workSpeed *= 2f;
                questee.actionSfx = null;
                entity
		                .edit().add(new Tutorial(0, "click"))
                        .add(new Taxing())
                        .add(new Incappable("queen", "queen-hurt", 3.5f))
                        .add(new Hint("The queen, generates coins when undamaged."))
                        .add(new Anim("queen", 13)); break;
            case "knight":
                    questee.travelSpeed *= 0.35f;
                    questee.workSpeed *= 1.5f;
                questee.actionSfx = "sfx_foryou";
                    entity
		                    .edit().add(new Hint("The knight, heals fast, moves slow."))
                    .add(new Incappable("knight", "knight-hurt", 2))
                    .add(new Anim("knight", 13)); break;
            case "mage":
                    questee.travelSpeed *= 4f;
                    questee.workSpeed *= 4f;
                    questee.actionSfx = "sfx_hocuspocus";
                    entity
		                    .edit().add(new Incappable("mage", "mage-hurt", 20))
                       .add(new Hint("The mage, slow healer, fast mover!"))
                       .add(new Anim("mage", 13)); break;
            case "spelunker":
                    questee.actionSfx = "sfx_treasure";
                    questee.canFight = false;
                    questee.canTreasure = true;
                    entity
		                    .edit().add(new Incappable("spelunker", "spelunker-hurt", 5))
                       .add(new Hint("Spelunker. Gathers treasures!"))
                       .add(new Anim("spelunker", 13)); break;
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
		        .edit().add(new Pos(cx, cy))
                .add(anim)
                .add(physics)
                .add(clamped).getEntity();
    }

    private Entity createBird(int cx, int cy) {

        // clamp with wrap so clouds can swoop around
        CastleBlock castleBlock = new CastleBlock();
        castleBlock.fadeoutOnReplace=true;
        return world.createEntity()
		        .edit().add(new Pos(cx, cy))
                .add(castleBlock)
                .add(new ColorAnimation(new Color(1, 1, 1, 0), new Color(1, 1, 1, 1), Interpolation.linear, 1f, 1f))
                .add(new Anim("bird", 11)).getEntity();
    }

    private Entity createElevator(int cx, int cy) {
        world.createEntity()
		        .edit().add(new Pos(cx, cy))
                .add(new Bounds(0, 0, 23, 25))
                .add(new Hint("Mining elevator"))
                .add(new Anim("lift-frame", -80));
        Entity cage = world.createEntity()
		        .edit().add(new Pos(cx + 3, cy))
                .add(new Anim("lift-cage", -79)).getEntity();
        return cage;
    }

    private Anim staticRandomizedAnim(String animId) {
        Anim anim = new Anim(animId, 1);
        anim.speed=0;
        anim.age= MathUtils.random(0, abstractAssetSystem.get(animId).getAnimationDuration()); // start at random cell.
        return anim;
    }

    private Entity createHills(int cx, int cy) {
        return world.createEntity().edit().add(new Pos(cx, cy)).add(new Anim("hills", 10)).getEntity();
    }

    private Entity createBackground() {
        return world.createEntity().edit().add(new Pos(0, 0)).add(new Anim("background", -99)).getEntity();
    }

    @Override
    protected void initialize() {
        super.initialize();
        createEntity("background", 0, 0);
        createEntity("hills", 0, 0);
        Entity lift = createEntity("lift", 251, 30).edit().add(new Physics()).add(new Gravity(-5.0f)).getEntity();
        tagManager.register("lift", lift);

        createEntity("queen", 0, SERVANT_Y);

        createEntity("marker-monster", RADAR_X+10, RADAR_Y +60)
		        .edit()
                .add(new Tutorial(1, "click"));

        for ( int i=0; i<20; i++)
        {
            createEntity("cloud",
		            (int) MathUtils.random(-40, Gdx.graphics.getWidth() / G.CAMERA_ZOOM_FACTOR + 10),
		            (int) MathUtils.random((Gdx.graphics.getHeight() / G.CAMERA_ZOOM_FACTOR) * 0.4f, (Gdx.graphics.getHeight() / G.CAMERA_ZOOM_FACTOR) * 0.9f));
        }

        createEntity("indicator");
        createEntity("mouse");
        createEntity("radar", RADAR_X, RADAR_Y);

        int tmpY = (int)(cameraSystem.getPixelHeight() - 22);
        int tmpX = 140;
        int stepSize = 16;
        createEntity("expand-wall", tmpX, tmpY)
		        .edit().add(new Tutorial(2, "click")).add(new Cost(8, 1));
        createEntity("expand-spelunker", tmpX + stepSize, tmpY).edit().add(new Cost(25, 2));
        createEntity("expand-knight", tmpX + stepSize * 2, tmpY).edit().add(new Cost(40, 20));
        createEntity("expand-mage", tmpX + stepSize * 3, tmpY).edit().add(new Cost(80, 80));

    }
}
