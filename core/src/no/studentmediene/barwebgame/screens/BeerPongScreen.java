package no.studentmediene.barwebgame.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import no.studentmediene.barwebgame.BarwebGame;
import no.studentmediene.barwebgame.models.Cup;
import no.studentmediene.barwebgame.util.BeerPongInputProcessor;
import no.studentmediene.barwebgame.util.ProjectileEquation;
import no.studentmediene.barwebgame.util.SpriteSheetAnimation;

import java.util.Iterator;

/**
 * @author Håkon Meyer Tørnquist <haakon.t@gmail.com>
 *         Date: 06.11.2016 13.35.
 */
public class BeerPongScreen extends ScreenAdapter {
    private SpriteBatch batch = new SpriteBatch();

    private SpriteSheetAnimation
            throwAnimation = new SpriteSheetAnimation(
            0.1f, 5, 1, new Texture("animation/throw.png")),
            beerAnimation = new SpriteSheetAnimation(
                    1, 8, 8, new Texture("animation/power-bar.png"));

    private float throwingStateTime = 0f;

    private Array<Cup> cups = new Array<>();

    private Texture
            ball = new Texture("ball.png"),
            mug = new Texture("mug.png"),
            table = new Texture("table.png");

    private Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/game-music.mp3"));

    private Sprite background = new Sprite(new Texture("background.png"));

    private BeerPongInputProcessor inputProcessor = new BeerPongInputProcessor();

    private ProjectileEquation ballTrajectory;

    private double currentPowerLevel = 1, ballAirTime = 0;

    private final Game game;

    public BeerPongScreen(final Game game) {
        this.game = game;

        int x = 420;
        for (int i = 0; i < 10; i++) {
            cups.add(new Cup(x, 50));
            x += Cup.WIDTH;
        }
        Gdx.input.setInputProcessor(inputProcessor);
    }

    @Override
    public void render (float delta) {
        if (throwAnimation.isAnimationFinished(throwingStateTime)) {
            inputProcessor.throwing = false;
            throwingStateTime = 0;
            releaseBall();
        }

        if(inputProcessor.spacePressed) {
            changePowerLevel();
        }

        if (inputProcessor.throwing) {
            throwingStateTime += delta;
        }

        batch.begin();
        background.draw(batch);
        batch.draw(beerAnimation.getKeyFrame((float) (currentPowerLevel - 0.5f)), 45, 385);
        batch.draw(table, 375, -90);
        batch.draw(mug, 40, 375, 110, 65);
        boolean won = renderBall();
        cups.forEach(cup -> batch.draw(cup, cup.getX(), cup.getY(), Cup.WIDTH, Cup.HEIGHT));
        batch.draw(throwAnimation.getKeyFrame(throwingStateTime), 50, 50);
        batch.end();
        if (won) dispose();
    }

    private boolean renderBall() {
        boolean won = false;
        if (inputProcessor.ballMoving) {
            ballAirTime += (10 * Gdx.graphics.getDeltaTime());
            if (getCurrentBallY() > 50) {
                batch.draw(ball, getCurrentBallX(), getCurrentBallY(), 15, 15);
            } else {
                throwCount++;
                won = detectCupCollision();
                currentPowerLevel = 1;
                ballAirTime = 0;
                inputProcessor.ballMoving = false;
            }
        }
        return won;
    }

    private int throwCount = 0;
    private int cupCount = 10;

    private boolean detectCupCollision() {
        for (Iterator<Cup> iterator = cups.iterator(); iterator.hasNext();) {
            Cup cup = iterator.next();
            if (getCurrentBallX() > cup.getX()
                    && getCurrentBallX() < cup.getX() +
                    Cup.WIDTH) {
                cupCount--;
                iterator.remove();
                if (cupCount == 0) {
                    if (BarwebGame.HIGH_SCORE == -1 ||
                            throwCount < BarwebGame.HIGH_SCORE) {
                        BarwebGame.HIGH_SCORE = throwCount;
                    }
                    game.setScreen(new MenuScreen(game));
                    return true;
                }
                break;
            }
        }
        return false;
    }

    private float getCurrentBallY() {
        return ballTrajectory.getY((float) ballAirTime);
    }

    private float getCurrentBallX() {
        return ballTrajectory.getX((float) ballAirTime);
    }

    private void releaseBall() {
        float totalSpeed = (float) (currentPowerLevel + 16);
        ballTrajectory = new ProjectileEquation(160, 120,
                (float) (totalSpeed*Math.cos(inputProcessor.currentAngle)),
                (float) (totalSpeed*Math.sin(inputProcessor.currentAngle)));
        inputProcessor.ballMoving = true;
    }

    private void changePowerLevel() {
        if (inputProcessor.powerRising) {
            currentPowerLevel++;
            if (currentPowerLevel >= 64)
                inputProcessor.powerRising = false;
        }
        else {
            currentPowerLevel--;
            if (currentPowerLevel <= 1)
                inputProcessor.powerRising = true;
        }
    }

    @Override
    public void dispose () {
        batch.dispose();
        throwAnimation.dispose();
        beerAnimation.dispose();
        ball.dispose();
        table.dispose();
        cups.forEach(Texture::dispose);
        backgroundMusic.dispose();
    }

    @Override
    public void show() {
        backgroundMusic.play();
    }
}
