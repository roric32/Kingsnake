package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.*;

public class GameSetup implements Screen {

    private Stage stage;
    private Music bgmusic;
    private int screenwidth;
    private int screenheight;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private SpriteBatch batch;
    private BackgroundActor background;
    public boolean timetostart = false;
    private boolean playeronejoined = false;
    private boolean playertwojoined = false;
    private boolean playerthreejoined = false;
    private boolean playerfourjoined = false;
    private int numplayers = 0;
    private SnakeImage player1image = new SnakeImage(20, 255, "blacksnake");
    private SnakeImage player2image = new SnakeImage(280, 255, "redsnake");
    private SnakeImage player3image = new SnakeImage(540, 255, "orangesnake");
    private SnakeImage player4image = new SnakeImage(794, 255, "yellowsnake");
    private Sound selected = Gdx.audio.newSound(Gdx.files.internal("sounds/selected.wav"));
    private Sound randomsound = Gdx.audio.newSound(Gdx.files.internal("sounds/randomsound.wav"));
    private Kingsnake game;

    public GameSetup(Kingsnake gamecontroller) {
        this.game = gamecontroller;
    }

    public void show() {

        this.timetostart = false;
        Gdx.input.setInputProcessor(null);
        this.stage = new Stage();

        this.bgmusic = Gdx.audio.newMusic(Gdx.files.internal("music/gamesetup.mp3"));
        this.bgmusic.setLooping(true);
        this.bgmusic.play();

        //Set up screen, camera and viewports.
        this.screenwidth = Gdx.graphics.getWidth();
        this.screenheight = Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1024, 768);
        this.viewport = new FitViewport(1024, 768, camera);

        this.batch = new SpriteBatch();
        //this.background = new Background(1024, 768, Gdx.files.internal("images/gamesetup/gamejoinin.png"), this.batch, false);
        Gdx.input.setInputProcessor(this.stage);

        this.background = new BackgroundActor(new Texture(Gdx.files.internal("images/gamesetup/gamejoinin.png")));
        this.stage.addActor(this.background);

        Clock clock = new Clock(454, 500, this);
        this.stage.addActor(clock);

    }

    public void pause() {

    }

    public void resume() {

    }

    public void hide() {
        this.bgmusic.stop();
        this.playeronejoined = false;
        this.playertwojoined = false;
        this.playerthreejoined = false;
        this.playerfourjoined = false;
        this.numplayers = 0;
        this.stage.clear();
    }

    public void render(float delta) {

        if(!timetostart) {
            Gdx.gl.glClearColor(255, 255, 255, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            this.stage.draw();
            if(Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                if(!this.playeronejoined) {
                    this.playeronejoined = true;
                    this.stage.addActor(player1image);
                    this.game.gameController.playercolors.add(Color.BLACK);
                    this.numplayers++;
                    selected.play();
                }
            }
            if(Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                if(!this.playertwojoined) {
                    this.playertwojoined = true;
                    this.stage.addActor(player2image);
                    this.game.gameController.playercolors.add(Color.RED);
                    this.numplayers++;
                    selected.play();
                }
            }
            if(Gdx.input.isKeyPressed(Input.Keys.O) || Gdx.input.isKeyPressed(Input.Keys.L) || Gdx.input.isKeyPressed(Input.Keys.SEMICOLON) || Gdx.input.isKeyJustPressed(Input.Keys.K)) {
                if(!this.playerthreejoined) {
                    this.playerthreejoined = true;
                    this.stage.addActor(player3image);
                    this.game.gameController.playercolors.add(Color.ORANGE);
                    this.numplayers++;
                    selected.play();
                }
            }
            if(Gdx.input.isKeyPressed(Input.Keys.Y) || Gdx.input.isKeyPressed(Input.Keys.H) || Gdx.input.isKeyPressed(Input.Keys.G) || Gdx.input.isKeyJustPressed(Input.Keys.J)) {
                if(!this.playerfourjoined) {
                    this.playerfourjoined = true;
                    this.stage.addActor(player4image);
                    this.game.gameController.playercolors.add(Color.YELLOW);
                    this.numplayers++;
                    selected.play();
                }
            }
        /*
        if (Gdx.input.isTouched()) {
            System.out.println("Input occurred at x=" + Gdx.input.getX() + ", y=" + Gdx.input.getY());
        }
        */
        } else {
            if(!this.playeronejoined && !this.playertwojoined && !this.playerthreejoined && !this.playerfourjoined)  {
                this.randomsound.play();
                this.game.setScreen(this.game.titleScreen);
            } else {
                //Start the game!
                System.out.println(this.numplayers);
                this.game.gameController.numplayers = this.numplayers;
                this.game.setScreen(this.game.gameController);
            }
        }
    }

    public void resize(int x, int y) {

    }

    public void dispose() {
        this.stage.dispose();
    }

}
