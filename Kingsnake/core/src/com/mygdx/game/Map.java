package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.RandomXS128;

import java.util.ArrayList;

public class Map {

    public int obstacle_count;
    private SpriteBatch batch;
    private TextureAtlas textureAtlas;
    private Sprite cactussprite;
    private Sprite rocksprite;
    private Sprite cowskullsprite;
    private Sprite outlinesprite;
    private Sprite blank;
    private Sprite purple;
    private Sound winboom;
    private Sound gameendsound = Gdx.audio.newSound(Gdx.files.internal("sounds/gameendsound.wav"));
    private boolean gameendsoundplayed = false;
    private Grid grid;
    private GridSquare square;
    private Overlord overlord;
    private float dt = 0;
    public boolean animationCompleted = false;

    public Map(Grid grid, int obstacle_count, SpriteBatch batch, Camera camera, Overlord overlord) {
        this.grid = grid;
        this.batch = batch;
        this.overlord = overlord;

        //TODO: obstacle count determines number of obstacles
        this.obstacle_count = obstacle_count;

        //Get sprites for possible terrain
        textureAtlas = new TextureAtlas(Gdx.files.internal("images/obstacles.pack"));
        cactussprite = new Sprite(textureAtlas.findRegion("cactus"));
        rocksprite = new Sprite(textureAtlas.findRegion("rock"));
        cowskullsprite = new Sprite(textureAtlas.findRegion("cowskull"));
        outlinesprite = new Sprite(textureAtlas.findRegion("grid"));
        blank = new Sprite(textureAtlas.findRegion("blank"));
        purple = new Sprite(textureAtlas.findRegion("black"));

        //Sounds...Just for the win.
        this.winboom = Gdx.audio.newSound(Gdx.files.internal("sounds/winboom.wav"));

        //Assemble a list of possible obstacles to randomly generate on the map.
        Sprite[] possibleObstacles = new Sprite[] {cactussprite, rocksprite, cowskullsprite};

        //Randomly put sprites on the board!
        for(int x = 0; x < this.obstacle_count; x++) {
            RandomXS128 random = new RandomXS128();
            int selectedindex = random.nextInt(possibleObstacles.length);

            int randomx;
            int randomy;
            boolean squareisspawnpoint = false;

            do {
                randomx = random.nextInt(16);
                randomy = random.nextInt(16);
                squareisspawnpoint = this.overlord.SquareIsSpawnPoint(randomx, randomy);
            } while (squareisspawnpoint);

            this.overlord.createObstacle(possibleObstacles[selectedindex], randomx, randomy);

        }


    }

    public void render() {

    }

    public void dispose() {
        this.textureAtlas.dispose();
        this.winboom.dispose();
    }

    public void ShowGameWinAnimation() {

        this.dt = this.dt + Gdx.graphics.getDeltaTime();

        if(!this.gameendsoundplayed) {
            this.gameendsound.play();
            this.gameendsoundplayed = true;
        }

        for(int x = 0; x < this.grid.gridlist.length; x++) {
            for(int y = 0; y < this.grid.gridlist.length; y++) {
                    this.grid.gridlist[x][y].sprite = this.purple;
                    if(this.grid.gridlist[x][y].animation != null) {
                        this.grid.gridlist[x][y].animation = null;
                    }
            }
        }

        if(this.dt > 2) {
            this.dt = 0;
            this.winboom.play();
            this.animationCompleted = true;
        }


    }
}
