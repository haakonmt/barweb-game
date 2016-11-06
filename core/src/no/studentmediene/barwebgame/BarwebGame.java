package no.studentmediene.barwebgame;

import com.badlogic.gdx.Game;
import no.studentmediene.barwebgame.screens.MenuScreen;

public class BarwebGame extends Game {

    public static final int WIDTH = 800, HEIGHT = 480;
    public static int HIGH_SCORE = -1;

    @Override
    public void create () {
        setScreen(new MenuScreen(this));
    }

    @Override
    public void render () {
        super.render();
    }
}
