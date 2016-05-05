package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.*;

public class GameEnd implements Screen{

    private Stage stage;
    private Music bgmusic;
    private Sound boom = Gdx.audio.newSound(Gdx.files.internal("sounds/selected.wav"));
    private int screenwidth;
    private int screenheight;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private BackgroundActor background;
    private SnakeImage player1image = new SnakeImage(751, 530, "blacksnake");
    private SnakeImage player2image = new SnakeImage(751, 530, "redsnake");
    private SnakeImage player3image = new SnakeImage(751, 530, "orangesnake");
    private SnakeImage player4image = new SnakeImage(751, 530, "yellowsnake");
    private SnakeImage winnerimage;
    private Kingsnake game;
    public ArrayList<Player> players;
    private java.util.Map<Integer, Integer> playerscores = new TreeMap<Integer, Integer>();
    private java.util.Map<Integer, Integer> playerlengths = new TreeMap<Integer, Integer>();
    private ShapeRenderer renderer = new ShapeRenderer();
    private int shape_length = 0;
    private double[] max_lengths = new double[4];
    private float barlength1 = 0;
    private float barlength2 = 0;
    private float barlength3 = 0;
    private float barlength4 = 0;

    private float total_time = 0f;
    private ArrayList<Color> color_order = new ArrayList<Color>();

    public GameEnd(Kingsnake game) {
        this.game = game;
    }

    public void show() {

        this.boom.play();
        Gdx.input.setInputProcessor(null);
        this.stage = new Stage();

        this.bgmusic = Gdx.audio.newMusic(Gdx.files.internal("music/gameend.mp3"));
        this.bgmusic.setLooping(true);
        this.bgmusic.play();

        //Set up screen, camera and viewports.
        this.screenwidth = Gdx.graphics.getWidth();
        this.screenheight = Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1024, 768);
        this.viewport = new FitViewport(1024, 768, camera);

        Gdx.input.setInputProcessor(this.stage);

        this.background = new BackgroundActor(new Texture(Gdx.files.internal("images/gameend/endbackground.png")));
        this.stage.addActor(this.background);

        //We'll use this later for the scores.
        java.util.Map<Integer, Integer> scores = new HashMap<Integer, Integer>();
        java.util.Map<Integer, Integer> lengths = new HashMap<Integer, Integer>();

        //Place the winning snake picture on the top right corner.

        for(int x = 0; x < this.players.size(); x++) {
            scores.put(this.players.get(x).number, this.players.get(x).score);
            lengths.put(this.players.get(x).number, this.players.get(x).length);
            //System.out.println(this.players.get(x).number);
            if(this.players.get(x).isWinner) {
                if(this.players.get(x).number == 1) {
                    this.winnerimage = this.player1image;
                } else if(this.players.get(x).number == 2) {
                    this.winnerimage = this.player2image;
                } else if(this.players.get(x).number == 3) {
                    this.winnerimage = this.player3image;
                } else if(this.players.get(x).number == 4) {
                    this.winnerimage = this.player4image;
                }
            }
        }

        //Sort the lengths
        this.playerlengths = sortByComparator(lengths);

        //Lay out the scoreboards.
        int z = 1;
        int y = 500;

        for (java.util.Map.Entry<Integer, Integer> entry : this.playerlengths.entrySet()) {
            //System.out.println("Key : " + entry.getKey()
             //       + " Value : " + entry.getValue());

            //Figure out the math behind this.

            if(z == 1) {
                y = 500;
                this.max_lengths[0] = (entry.getValue() * 39.25);
                System.out.println(this.max_lengths[0]);
            } else if(z == 2) {
                y = 362;
                this.max_lengths[1] = (entry.getValue() * 39.25);
                System.out.println(this.max_lengths[1]);
            } else if(z == 3) {
                y = 221;
                this.max_lengths[2] = (entry.getValue() * 39.25);
                System.out.println(this.max_lengths[2]);
            } else if(z == 4) {
                this.max_lengths[3] = (entry.getValue() * 39.25);
                System.out.println(this.max_lengths[3]);
                y = 80;
            }

            if(entry.getKey() == 1) {
                ScoreBox p1score = new ScoreBox(0, y, "blackbox");
                this.color_order.add(Color.BLACK);
                this.stage.addActor(p1score);
            } else if(entry.getKey() == 2) {
                ScoreBox p2score = new ScoreBox(0, y, "redbox");
                this.color_order.add(Color.RED);
                this.stage.addActor(p2score);
            } else if(entry.getKey() == 3) {
                ScoreBox p3score = new ScoreBox(0, y, "orangebox");
                this.color_order.add(Color.ORANGE);
                this.stage.addActor(p3score);
            } else if(entry.getKey() == 4) {
                ScoreBox p4score = new ScoreBox(0, y, "yellowbox");
                this.color_order.add(Color.YELLOW);
                this.stage.addActor(p4score);
            }
            z++;
        }

    }

    public void hide() {
        this.bgmusic.stop();
        this.stage.clear();
        this.color_order.clear();
        this.max_lengths = new double[4];
        this.barlength1 = 0;
        this.barlength2 = 0;
        this.barlength3 = 0;
        this.barlength4 = 0;
        this.total_time = 0;
        this.shape_length = 0;
    }

    public void pause() {

    }

    public void resume() {

    }

    public void resize(int x, int y) {

    }

    public void render(float dt) {

        Gdx.gl.glClearColor(255, 255, 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.stage.addActor(this.winnerimage);
        this.stage.draw();
        renderer.begin(ShapeRenderer.ShapeType.Filled);

        total_time += dt;
        if (total_time >= 0.0001f) {
            shape_length += 3;
            total_time = 0.f;
        }

        //Score bars
        int x = 0;
        for(Color color : this.color_order) {

            renderer.setColor(color);
            switch(x) {
                case 0: {
                    if(shape_length < this.max_lengths[0]) {
                        this.barlength1 = shape_length;
                    }
                    renderer.rect(124, 505, barlength1, 103);
                    break;
                }
                case 1: {
                    if(shape_length < this.max_lengths[1]) {
                        this.barlength2 = shape_length;
                    }
                    renderer.rect(124, 367, barlength2, 103);
                    break;
                }
                case 2: {
                    if(shape_length < this.max_lengths[2]) {
                        this.barlength3 = shape_length;
                    }
                    renderer.rect(124, 226, this.barlength3, 103);
                    break;
                }
                case 3: {
                    if(shape_length < this.max_lengths[3]) {
                        this.barlength4 = shape_length;
                    }
                    renderer.rect(124, 85, this.barlength4, 103);
                    break;
                }
            }
            x++;
        }

        renderer.end();

        //Restart game.
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            this.game.setScreen(this.game.titleScreen);
        }
    }

    public void dispose() {
        this.background.dispose();
        this.bgmusic.dispose();
        this.boom.dispose();
    }

    private static java.util.Map<Integer, Integer> sortByComparator(java.util.Map<Integer, Integer> unsortMap) {

        // Convert Map to List
        List<java.util.Map.Entry<Integer, Integer>> list =
                new LinkedList<java.util.Map.Entry<Integer, Integer>>(unsortMap.entrySet());

        // Sort list with comparator, to compare the Map values
        Collections.sort(list, new Comparator<java.util.Map.Entry<Integer, Integer>>() {
            public int compare(java.util.Map.Entry<Integer, Integer> o1,
                               java.util.Map.Entry<Integer, Integer> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        // Convert sorted map back to a Map
        java.util.Map<Integer, Integer> sortedMap = new LinkedHashMap<Integer, Integer>();
        for (Iterator<java.util.Map.Entry<Integer, Integer>> it = list.iterator(); it.hasNext();) {
            java.util.Map.Entry<Integer, Integer> entry = it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

}
