package no.studentmediene.barwebgame.util;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

/**
 * @author Håkon Meyer Tørnquist <haakon.t@gmail.com>
 *         Date: 05.11.2016 17.23.
 */
public class BeerPongInputProcessor implements InputProcessor {

    public boolean ballMoving, throwing, spacePressed, powerRising = true;

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.SPACE && !ballMoving && !throwing) {
            spacePressed = true;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.SPACE && spacePressed) {
            spacePressed = false;
            throwing = true;
            powerRising = true;
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
