package net.mostlyoriginal.game.system;

import com.artemis.annotations.Wire;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import net.mostlyoriginal.game.MyGame;
import net.mostlyoriginal.game.component.CastleBlock;

/**
 * Keeps track of the best castle made by the player.
 *
 * @author Daan van Yperen
 */
@Wire
public class BestCastleSystem extends VoidEntitySystem {

    private final MyGame game;
    public CastleBlock.Type bestCastle[][] = new CastleBlock.Type[CastleSystem.H][CastleSystem.W];
    private CastleSystem castleSystem;

    public int value = 0;
    public int bestValue = -1;

    private DirectorSystem directorSystem;

    public BestCastleSystem(MyGame game ) {
        this.game = game;
    }

    @Override
    protected void processSystem() {

        if ( directorSystem.gameOver )
        {
            if ( Gdx.input.isKeyPressed(Input.Keys.SPACE))
            {
                game.restart();
            }
            return;
        }

        calculateCastleScore();

        if (value > bestValue) {
            bestValue=value;
            for (int x = 0; x < CastleSystem.W; x++) {
                for (int y = 0; y < CastleSystem.H; y++) {
                    bestCastle[y][x] = castleSystem.castle[y][x];
                }
            }
        }

        if ( value == 0 )
        {
            // game over!
            restoreBestCastle();
            directorSystem.gameOver();
            world.getSystem(QueenTaxSystem.class).setEnabled(false);
        }
    }

    public void restoreBestCastle()
    {
        for (int x = 0; x < CastleSystem.W; x++) {
            for (int y = 0; y < CastleSystem.H; y++) {
                castleSystem.castle[y][x] = bestCastle[y][x];
            }
        }
        castleSystem.castleDirty=true;
    }

    private void calculateCastleScore() {
        value = 0;
        for (int x = 0; x < CastleSystem.W; x++) {
            for (int y = 0; y < CastleSystem.H; y++) {
                value += castleSystem.castle[y][x].score;
            }
        }
    }
}
