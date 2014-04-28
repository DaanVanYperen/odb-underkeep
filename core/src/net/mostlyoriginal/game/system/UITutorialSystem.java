package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.mostlyoriginal.api.component.basic.Bounds;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.game.component.Tutorial;
import net.mostlyoriginal.game.component.agent.Clickable;
import net.mostlyoriginal.game.manager.AssetSystem;
import net.mostlyoriginal.game.manager.FontManager;

/**
 * @author Daan van Yperen
 */
@Wire

public class UITutorialSystem extends EntityProcessingSystem {

    private SpriteBatch batch = new SpriteBatch();
    public ComponentMapper<Tutorial> tm;
    public ComponentMapper<Pos> pm;
    public ComponentMapper<Bounds> bm;
    public ComponentMapper<Clickable> cm;
    public int activeStep = 0;
    private FontManager fontManager;
    private CameraSystem cameraSystem;
    private AssetSystem assetSystem;
    private float age;

    public UITutorialSystem() {
        super(Aspect.getAspectForAll(Clickable.class, Tutorial.class, Pos.class, Bounds.class));
    }

    @Override
    protected void begin() {
        age+= world.delta;
    }

    @Override
    protected void process(Entity e) {
        Tutorial tutorial = tm.get(e);
        if ( tutorial.step == activeStep )
        {
            String hint = tutorial.hint;

            float a = 1;

            Pos pos = pm.get(e);

            BitmapFont.TextBounds bounds = fontManager.font.getBounds(hint);

            float x=pos.x+ bm.get(e).cx() - (bounds.width/2);
            float y=pos.y + bm.get(e).maxy + 15;

            // clamp to screen.
            if ( x + bounds.width >= cameraSystem.getPixelWidth() ) x = cameraSystem.getPixelWidth()-4-bounds.width;
            if ( y + bounds.height >= cameraSystem.getPixelHeight() ) x = cameraSystem.getPixelHeight()-4-bounds.height;
            if ( y < 0 ) y =0;
            if ( x < 0 ) x =0;

            batch.setProjectionMatrix(cameraSystem.guiCamera.combined);
            batch.begin();
            batch.setColor(1f, 1f, 1f, a);
            batch.draw(assetSystem.get("hint-bg").getKeyFrame(0), x - 2, y - 2, (int) (bounds.width + 4), (int) (bounds.height + 4));
            fontManager.font.setColor(1f, 1f, 1f, a);
            fontManager.font.draw(batch, hint, (int)x,(int)y + 5);
            batch.draw(assetSystem.get("clickme").getKeyFrame(age*2f,true), pos.x - 4 + bm.get(e).cx(), pos.y  + bm.get(e).cy() + 4);

            batch.end();

            Clickable clickable = cm.get(e);
            if ( clickable.clicked ) {
                activeStep++;
            }
        }
    }
}
