package no.studentmediene.barwebgame.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import no.studentmediene.barwebgame.BarwebGame;

/**
 * @author Håkon Meyer Tørnquist <haakon.t@gmail.com>
 *         Date: 05.11.2016 11.29.
 */
public class MenuScreen extends ScreenAdapter {

    private final SpriteBatch batch = new SpriteBatch();
    private final Sprite background = new Sprite(new Texture("background.png"));
    private final Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/menu-music.mp3"));
    private final Game game;
    private final Button startSinglePlayerGame, startMultiPlayerGame;

    private final Stage stage = new Stage(new FillViewport(BarwebGame.WIDTH, BarwebGame.HEIGHT), batch);

    private Table table;

    public MenuScreen(final Game game) {
        this.game = game;
        backgroundMusic.setLooping(true);
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        startSinglePlayerGame = new TextButton("En spiller", skin);
        startSinglePlayerGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new BeerPongScreen(game));
                dispose();
            }
        });
        startMultiPlayerGame = new TextButton("To spillere", skin);

        table = new Table(skin);
        table.setFillParent(true);
        if (BarwebGame.HIGH_SCORE != -1) {
            table.add("Highscore: " + BarwebGame.HIGH_SCORE);
            table.row().pad(20);
        }

        table.add(startSinglePlayerGame).width(250).height(75);
        table.row().pad(10);
        table.add(startMultiPlayerGame).width(250).height(75);

        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render (float delta) {
        batch.begin();
        background.draw(batch);
        batch.end();
        stage.draw();
    }

    @Override
    public void show() {
        backgroundMusic.play();
    }

    @Override
    public void dispose() {
        batch.dispose();
        backgroundMusic.dispose();
    }
}
