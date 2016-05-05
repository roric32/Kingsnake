package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;

import javax.swing.*;
import java.util.ArrayList;

public class GameController implements Screen{

    SpriteBatch batch;
    Background background;
    private Kingsnake game;
    public int numplayers;
    public ArrayList<Color> playercolors = new ArrayList<Color>();
    private int winner;
    public boolean gameisover = false;
    private int screenwidth;
    private int screenheight;
    private OrthographicCamera camera;
    private Overlord overlord;
    private Music bgmusic;
    public FitViewport viewport;
    private Map map;
    private Grid grid;
    private float delta;

    public GameController(int numplayers, Kingsnake gameController) {

        this.game = gameController;
        this.numplayers = numplayers;

    }

    public void pause() {

    }

    public void resume() {

    }

    public void resize(int width, int height) {
        this.viewport.update(width, height);
    }

    public void hide() {
        Gdx.input.setInputProcessor(null);
        this.bgmusic.stop();
        this.playercolors.clear();
        this.dispose();
    }

    public void show() {

        Gdx.input.setInputProcessor(null);
        //Set up music and sounds
        this.bgmusic = Gdx.audio.newMusic(Gdx.files.internal("music/bgmusic.mp3"));
        this.bgmusic.setLooping(true);
        this.bgmusic.play();

        //Set up screen, camera and viewports.
        this.screenwidth = Gdx.graphics.getWidth();
        this.screenheight = Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1024, 768);
        this.viewport = new FitViewport(1024, 768, camera);

        //Set up background images.
        batch = new SpriteBatch();
        background = new Background(this.screenwidth, this.screenheight, Gdx.files.internal("badlands.jpg"), batch, true);

        //IMPORTANT
        this.grid = new Grid(camera, batch);

        //The overlord handles the positions of all players, obstacles, powerups, and non-background sprites.
        overlord = new Overlord(this.grid, this.batch);

        //The map handles generating the random terrain.
        this.map = new Map(this.grid, 14, batch, camera, this.overlord);

        //Create players!

        for(int x = 0; x < this.playercolors.size(); x++) {

            if(this.playercolors.get(x) == Color.BLACK) {
                int startx = 0;
                int starty = 2;
                Controls p1Controls = new Controls(Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT);
                overlord.createPlayer(1, this.playercolors.get(x), startx, starty, "UP", p1Controls);
            } else if(this.playercolors.get(x) == Color.RED) {
                int startx = 0;
                int starty = 13;
                Controls p2Controls = new Controls(Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D);
                overlord.createPlayer(2, this.playercolors.get(x), startx, starty, "DOWN", p2Controls);
            } else if(this.playercolors.get(x) == Color.ORANGE) {
                int startx = 15;
                int starty = 13;
                Controls p3Controls = new Controls(Input.Keys.O, Input.Keys.L, Input.Keys.K, Input.Keys.SEMICOLON);
                overlord.createPlayer(3, this.playercolors.get(x), startx, starty, "DOWN", p3Controls);
            } else if(this.playercolors.get(x) == Color.YELLOW) {
                int startx = 15;
                int starty = 2;
                Controls p4Controls = new Controls(Input.Keys.Y, Input.Keys.H, Input.Keys.G, Input.Keys.J);
                overlord.createPlayer(4, this.playercolors.get(x), startx, starty, "UP", p4Controls);
            }

        }

    }

    public void render(float wak) {
        if(!GameWon()) {
            Gdx.gl.glClearColor(255, 255, 255, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            camera.update();
            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            background.render();
            grid.render();
            map.render();
            overlord.render();
        /*
        if (Gdx.input.isTouched()) {
            System.out.println("Input occurred at x=" + Gdx.input.getX() + ", y=" + Gdx.input.getY());
        }
        */
            batch.end();
        } else {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            //JOptionPane.showMessageDialog(null, "Game over! WINNER: " + this.winner);
            camera.update();
            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            this.bgmusic.stop();
            Gdx.input.setInputProcessor(null);
            this.map.ShowGameWinAnimation();
            grid.render();
            map.render();
            batch.end();
            if(this.map.animationCompleted) {
                this.gameisover = true;
                this.game.gameEnd.players = this.overlord.players;
                this.game.setScreen(this.game.gameEnd);
            }

        }
    }

    public void dispose() {
        batch.dispose();
        background.dispose();
        overlord.dispose();
    }

    public boolean GameWon() {

        boolean result = false;
        int winner = this.overlord.WinningPlayer();

        if(winner != -1) {
            this.winner = winner;
            result = true;
        }

        return result;
    }


}
