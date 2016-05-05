package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;

public class TitleScreen implements Screen{

    public Kingsnake game;
    private int numplayers;
    private Music bgmusic;
    private int screenwidth;
    private int screenheight;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private SpriteBatch batch;
    private TextureAtlas atlas;
    private Background background;
    private Stage stage;
    private Sprite pushspace;
    private float delta;

    public TitleScreen(Kingsnake game) {

        this.game = game;

    }

    public void show() {

        Gdx.input.setInputProcessor(null);
        this.stage = new Stage();

        this.bgmusic = Gdx.audio.newMusic(Gdx.files.internal("music/kingsnake.mp3"));
        this.bgmusic.setLooping(true);
        this.bgmusic.play();

        //Set up screen, camera and viewports.
        this.screenwidth = Gdx.graphics.getWidth();
        this.screenheight = Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1024, 768);
        this.viewport = new FitViewport(1024, 768, camera);
        this.stage.setViewport(this.viewport);

        this.batch = new SpriteBatch();
        this.background = new Background(1024, 768, Gdx.files.internal("images/title/title.jpg"), this.batch, false);
        this.atlas = new TextureAtlas(Gdx.files.internal("images/gamesetup/gamesetup.pack"));
        this.pushspace = new Sprite(this.atlas.findRegion("Press Space"));
        Gdx.input.setInputProcessor(this.stage);
    }

    public void hide() {
        this.bgmusic.stop();
    }

    public void pause() {
        this.bgmusic.stop();
    }

    public void resume() {
        this.bgmusic.play();
    }

    public void resize(int width, int height) {
        this.stage.getViewport().update(width, height, true);
        this.stage.getViewport().apply();
    }

    public void render(float dt) {
        this.batch.begin();
        this.stage.getViewport().apply();
        this.background.render();
        this.delta = this.delta + dt;
        if(this.delta > 2) {
            this.batch.draw(this.pushspace, 770, 700);
            if(this.delta > 3) {
                this.delta = 0;
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            this.game.setScreen(this.game.gameSetup);
        }

        this.batch.end();
    }

    public void dispose() {
        this.batch.dispose();
        this.bgmusic.dispose();
        this.atlas.dispose();
    }


}
