package net.mostlyoriginal.game.manager;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.utils.EntityUtil;

/**
 * @todo Split game logic and library logic.
 * @author Daan van Yperen
 */
@Wire
public class AssetSystem extends net.mostlyoriginal.api.manager.AbstractAssetSystem {

    private TagManager tagManager;

    ComponentMapper<Pos> pm;

    public AssetSystem() {
        super();

        add("background", 0, 340, 284, 160,1);
        add("hills", 78, 296, 284, 36,1);
        add("radar", 1, 193, 68, 130,1);

        add("lift-frame", 0, 0, 23, 25,1);
        add("lift-cage", 23, 4, 17, 21,1);
        add("lift-dot", 156, 13, 4, 4,1);

        add("hint-bg", 130,68,10,10,1);

        add("particle-coin",96,21,4,4,1);
        add("particle-debris", 65, 85, 6, 5,2);

        add("queen", 0, 34, 13, 17,1);
        add("knight", 13, 34, 13, 17, 1);
        add("spelunker", 26, 34, 13, 17,1);
        add("mage", 39, 32, 13, 19,1);
        add("indicator", 102,66, 17, 28,1);
        add("queen-hurt", 78, 34, 13, 17,1);
        add("knight-hurt", 78+13, 34, 13, 17, 1);
        add("spelunker-hurt", 78+26, 34, 13, 17,1);
        add("mage-hurt", 78+39, 32, 13, 19,1);

        add("buy-knight", 156, 36, 13, 15,1);
        add("buy-spelunker", 169, 36, 13, 15,1);
        add("buy-mage", 182, 36, 13, 15,1);
        add("buy-wall", 221, 36, 13, 15,1);

        add("marker-monster", 66, 4, 11, 10,1);
        add("marker-gem", 79, 5,  9, 8,1);
        add("marker-gold", 92, 6, 10, 7,1);
        add("marker-dungeon", 67, 16, 9, 9,1);
        add("marker-portal", 79, 16, 10, 10,1);

        add("bird", 111, 6, 5, 3,2);
        add("cloud", 108, 11, 13, 7,2);

        add("building-hammer", 0,51, 13, 17,1);
        add("building-flag", 26,51, 13, 17,2);
        add("building-spelunker", 65,68, 13, 17,1);
        add("building-barracks", 0,68, 13, 17,1);
        add("building-wall",13,68, 13, 17,3);
        add("building-tower", 52,68, 13, 17,1);
        add("building-trimming-on-tower",0,85, 13, 17,1);
        add("building-trimming-on-wall-left",13,85, 13, 17,1);
        add("building-trimming-on-wall",26,85, 13, 17,1);
        add("building-trimming-on-wall-right",39,85, 13, 17,1);
        add("building-trimming-top-wall-left",13,102, 13, 17,1);
        add("building-trimming-top-wall-right",39,102, 13, 17,1);
        add("building-trimming-bottom-wall-left",13,119, 13, 17,1);
        add("building-trimming-bottom-wall-right",39,119, 13, 17,1);

        loadSounds(new String[] {
                "music",
                "sfx_click",
                "sfx_coins",
                "sfx_dududi",
                "sfx_rattle",
                "sfx_squeekytoy",
                "sfx_stoneonstone",
                "sfx_tape",
                "sfx_foryou",
                "sfx_hocuspocus",
                "sfx_treasure"
        });

        playMusic("music");
    }


    public void playSfx(String name, Entity origin) {
        if (sfxVolume > 0 )
        {
            Entity player = tagManager.getEntity("player");
            float distance = EntityUtil.distance(origin, player);

            float volume = sfxVolume - (distance / 2000f);
            if ( volume > 0.01f )
            {
                float balanceX = pm.has(origin) && pm.has(player) ? MathUtils.clamp((pm.get(origin).x - pm.get(player).x) / 100f, -1f, 1f) : 0;
                Sound sfx = getSfx(name);
                sfx.stop();
                sfx.play(volume, MathUtils.random(1f, 1.04f), balanceX);
            }
        }
    }

}
