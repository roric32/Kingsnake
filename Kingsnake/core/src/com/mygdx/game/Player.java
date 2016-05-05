package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class Player {

    public int score = 0;
    public int number;
    private SpriteBatch batch;
    private TextureAtlas atlas;
    private TextureAtlas maptextures;
    private Sprite upsprite;
    private Sprite leftsprite;
    private Sprite rightsprite;
    private Sprite downsprite;
    private Sprite upleftsprite;
    private Sprite uprightsprite;
    private Sprite downleftsprite;
    private Sprite downrightsprite;
    private Sprite verticalsprite;
    private Sprite horizontalsprite;
    private Sprite blanksprite;
    private Sprite explosionsprite;
    private Sprite gridsprite;
    private Sprite kupsprite;
    private Sprite kleftsprite;
    private Sprite krightsprite;
    private Sprite kdownsprite;
    private Sprite kupleftsprite;
    private Sprite kuprightsprite;
    private Sprite kdownleftsprite;
    private Sprite kdownrightsprite;
    private Sprite kverticalsprite;
    private Sprite khorizontalsprite;
    private Sprite kblanksprite;
    private Sprite kexplosionsprite;
    private TextureAtlas spawnatlas = new TextureAtlas(Gdx.files.internal("images/spawn/spawn.pack"));
    private Animation spawnanimation = new Animation(1/15f, spawnatlas.getRegions());
    private boolean[] playedblip = new boolean[3];
    private Color color;
    public ArrayList<int[]> lastpositions;
    private boolean positionschanged;
    private int snaketosplit;
    private float dt;
    private float bt;
    private double speeddefault = 0.099;
    public double speed = 0.099;
    public boolean hasBoost = false;
    public float boostTime = 10;
    public double kingsnakeDuration = 7;
    public double kingsnakeTimer = 0.00;
    private int startx;
    private int starty;
    public int currentx;
    public int currenty;
    private int nextx;
    private int nexty;
    private int[] coords;
    private ArrayList<int[]> deadspaces = new ArrayList<int[]>();
    private String newdirection;
    private String startdirection;
    public boolean isKingsnake = false;
    private boolean eatenMouse = false;
    public int length;
    public boolean isDead;
    private Grid grid;
    private GridSquare currentsquare;
    private Overlord overlord;
    private Sound explosion;
    private Sound spawnsound;
    private Sound blip = Gdx.audio.newSound(Gdx.files.internal("sounds/blip.wav"));
    private Controls controls;
    public boolean isWinner = false;

    public Player(int number, SpriteBatch batch, Color color, Grid grid, int startx, int starty, String startdirection, Controls controls, Overlord overlord) {
        this.number = number;
        this.batch = batch;
        this.color = color;
        this.grid = grid;
        this.startx = startx;
        this.starty = starty;
        this.startdirection = startdirection;
        this.overlord = overlord;
        this.controls = controls;
        //For use on the spawning countdown later.
        Arrays.fill(this.playedblip, false);

        SetUpSprites(false);

        maptextures = new TextureAtlas(Gdx.files.internal("images/obstacles.pack"));
        this.blanksprite = new Sprite(maptextures.findRegion("blank"));
        this.gridsprite = new Sprite(maptextures.findRegion("grid"));

        //Set up sounds.
        this.explosion = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        this.spawnsound = Gdx.audio.newSound(Gdx.files.internal("sounds/spawn.wav"));

        Spawn();

    }

    public void render() {


        //Delta time for calculating time between frames
        float dt = Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f);

        if(!this.isDead) {

            if(this.isKingsnake) {
                this.kingsnakeTimer = this.kingsnakeTimer + dt;
                //System.out.println("Snake number" + this.number + " is a kingsnake! Timer: " + this.kingsnakeTimer + " Duration: " + this.kingsnakeDuration);
                if(this.kingsnakeTimer > this.kingsnakeDuration) {
                    this.kingsnakeTimer = 0;
                    this.isKingsnake = false;
                }
            }

            if (this.positionschanged) {

                for (int x = 0; x < this.lastpositions.size(); x++) {
                    if (x == 0) {
                        if(!this.eatenMouse) {
                            this.grid.gridlist[this.lastpositions.get(0)[0]][this.lastpositions.get(0)[1]].sprite = this.blanksprite;
                        }
                    } else {

                        //In order to know what to display in each square, we need to see what direction came before it and
                        //what direction will be after it.

                        //Get the previous square's info.

                        String previousdirection;

                        int previousx = this.lastpositions.get(x - 1)[0];
                        int previousy = this.lastpositions.get(x - 1)[1];

                        //System.out.println("X: " + x);
                        //System.out.println("Previous x: " + previousx + " Previous Y: " + previousy);
                        //System.out.println("Current iterator x: " + this.lastpositions.get(x)[0] + " Current iterator Y: " + this.lastpositions.get(x)[1]);

                        if (previousx > this.lastpositions.get(x)[0] && previousy == this.lastpositions.get(x)[1]) {
                            previousdirection = "LEFT";
                        } else if (previousx < this.lastpositions.get(x)[0] && previousy == this.lastpositions.get(x)[1]) {
                            previousdirection = "RIGHT";
                        } else if (previousy > this.lastpositions.get(x)[1] && previousx == this.lastpositions.get(x)[0]) {
                            previousdirection = "DOWN";
                        } else if (previousy < this.lastpositions.get(x)[1] && previousx == this.lastpositions.get(x)[0]) {
                            previousdirection = "UP";
                        } else {
                            previousdirection = "NONE";
                        }

                        //Get the next in the sequence

                        String futuredirection;

                        if(x < (this.lastpositions.size() - 1)) {

                            int nextx = this.lastpositions.get(x + 1)[0];
                            int nexty = this.lastpositions.get(x + 1)[1];

                            if (nextx > this.lastpositions.get(x)[0] && nexty == this.lastpositions.get(x)[1]) {
                                futuredirection = "RIGHT";
                            } else if (nextx < this.lastpositions.get(x)[0] && nexty == this.lastpositions.get(x)[1]) {
                                futuredirection = "LEFT";
                            } else if (nexty > this.lastpositions.get(x)[1] && nextx == this.lastpositions.get(x)[0]) {
                                futuredirection = "UP";
                            } else if (nexty < this.lastpositions.get(x)[1] && nextx == this.lastpositions.get(x)[0]) {
                                futuredirection = "DOWN";
                            } else {
                                futuredirection = "NONE";
                            }

                        } else {
                            futuredirection = this.newdirection;
                        }

                        // System.out.println(previousdirection + " " + futuredirection);
                        // I know this code sucks, but I'm writing this with a fever right now and DGAF.
                        // Tried to make it work with setting the this.atlas in the SetUpSprites function to a texturemap with the kingsnake sprites.
                        // Worked for the initial transition, but then it wouldn't change the snake back to the original color.

                        if(previousdirection == "UP" && futuredirection == "LEFT") {
                            if(this.isKingsnake) {
                                this.grid.gridlist[this.lastpositions.get(x)[0]][this.lastpositions.get(x)[1]].sprite = this.kupleftsprite;
                            } else {
                                this.grid.gridlist[this.lastpositions.get(x)[0]][this.lastpositions.get(x)[1]].sprite = this.upleftsprite;
                            }
                        } else if(previousdirection == "UP" && futuredirection == "RIGHT") {
                            if(this.isKingsnake) {
                                this.grid.gridlist[this.lastpositions.get(x)[0]][this.lastpositions.get(x)[1]].sprite = this.kuprightsprite;
                            } else {
                                this.grid.gridlist[this.lastpositions.get(x)[0]][this.lastpositions.get(x)[1]].sprite = this.uprightsprite;
                            }
                        } else if(previousdirection == "LEFT" && futuredirection == "DOWN") {
                            if(this.isKingsnake) {
                                this.grid.gridlist[this.lastpositions.get(x)[0]][this.lastpositions.get(x)[1]].sprite = this.kuprightsprite;
                            } else {
                                this.grid.gridlist[this.lastpositions.get(x)[0]][this.lastpositions.get(x)[1]].sprite = this.uprightsprite;
                            }
                        } else if(previousdirection == "LEFT" && futuredirection == "UP") {
                            if(this.isKingsnake) {
                                this.grid.gridlist[this.lastpositions.get(x)[0]][this.lastpositions.get(x)[1]].sprite = this.kdownrightsprite;
                            } else {
                                this.grid.gridlist[this.lastpositions.get(x)[0]][this.lastpositions.get(x)[1]].sprite = this.downrightsprite;
                            }
                        } else if(previousdirection == "RIGHT" && futuredirection == "UP") {
                            if(this.isKingsnake) {
                                this.grid.gridlist[this.lastpositions.get(x)[0]][this.lastpositions.get(x)[1]].sprite = this.kdownleftsprite;
                            } else {
                                this.grid.gridlist[this.lastpositions.get(x)[0]][this.lastpositions.get(x)[1]].sprite = this.downleftsprite;
                            }
                        } else if(previousdirection == "RIGHT" && futuredirection == "DOWN") {
                            if(this.isKingsnake) {
                                this.grid.gridlist[this.lastpositions.get(x)[0]][this.lastpositions.get(x)[1]].sprite = this.kupleftsprite;
                            } else {
                                this.grid.gridlist[this.lastpositions.get(x)[0]][this.lastpositions.get(x)[1]].sprite = this.upleftsprite;
                            }
                        } else if(previousdirection == "DOWN" && futuredirection == "LEFT") {
                            if(this.isKingsnake) {
                                this.grid.gridlist[this.lastpositions.get(x)[0]][this.lastpositions.get(x)[1]].sprite = this.kdownleftsprite;
                            } else {
                                this.grid.gridlist[this.lastpositions.get(x)[0]][this.lastpositions.get(x)[1]].sprite = this.downleftsprite;
                            }
                        } else if(previousdirection == "DOWN" && futuredirection == "RIGHT") {
                            if(this.isKingsnake) {
                                this.grid.gridlist[this.lastpositions.get(x)[0]][this.lastpositions.get(x)[1]].sprite = this.kdownrightsprite;
                            } else {
                                this.grid.gridlist[this.lastpositions.get(x)[0]][this.lastpositions.get(x)[1]].sprite = this.downrightsprite;
                            }
                        } else if(previousdirection == "DOWN" && futuredirection == "DOWN" || previousdirection == "UP" && futuredirection == "UP") {
                            if(this.isKingsnake) {
                                this.grid.gridlist[this.lastpositions.get(x)[0]][this.lastpositions.get(x)[1]].sprite = this.kverticalsprite;
                            } else {
                                this.grid.gridlist[this.lastpositions.get(x)[0]][this.lastpositions.get(x)[1]].sprite = this.verticalsprite;
                            }
                        } else if(previousdirection == "LEFT" && futuredirection == "LEFT" || previousdirection == "RIGHT" && futuredirection == "RIGHT") {
                            if(this.isKingsnake) {
                                this.grid.gridlist[this.lastpositions.get(x)[0]][this.lastpositions.get(x)[1]].sprite = this.khorizontalsprite;
                            } else {
                                this.grid.gridlist[this.lastpositions.get(x)[0]][this.lastpositions.get(x)[1]].sprite = this.horizontalsprite;
                            }
                        }

                    }

                }

                if(!this.eatenMouse) {
                    for (int x = 0; x < this.lastpositions.size(); x++) {

                        //Update the lastposition with the new last position.
                        if (lastpositions.size() - 1 > x) {
                            this.lastpositions.set(x, this.lastpositions.get(x + 1));
                        } else {
                            this.lastpositions.remove(x);
                        }

                    }
                }

                this.eatenMouse = false;

            }

            this.positionschanged = false;

            this.dt = this.dt + dt;

            //Check for boost powerup
            if(this.hasBoost) {
                float bt = Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f);
                this.bt = this.bt + bt;
                if (this.bt > this.boostTime) {
                    this.bt = 0;
                    this.hasBoost = false;
                    this.speed = this.speeddefault;
                }
            }

            //POLL FOR INPUT

            if (this.dt > this.speed) {
                this.dt = 0;

                if (Gdx.input.isKeyPressed(this.controls.UpKey)) {

                    if(this.newdirection != "DOWN") {

                        this.newdirection = "UP";
                        this.nextx = this.currentx;
                        this.nexty = this.currenty + 1;

                        //Move the snake up!
                        if (this.nexty < this.grid.gridlist.length) {
                            Move();
                        }

                    }

                    //System.out.println("UP pressed.");

                } else if (Gdx.input.isKeyPressed(this.controls.DownKey)) {

                    if(this.newdirection != "UP") {

                        this.newdirection = "DOWN";
                        this.nextx = this.currentx;
                        this.nexty = this.currenty - 1;

                        //Move the snake down!
                        if (this.nexty >= 0) {
                            Move();
                        }

                    }

                    //System.out.println("DOWN pressed.");

                } else if (Gdx.input.isKeyPressed(this.controls.LeftKey)) {

                    if(this.newdirection != "RIGHT") {
                        this.newdirection = "LEFT";
                        this.nextx = this.currentx - 1;
                        this.nexty = this.currenty;

                        //Move the snake left!
                        if (this.nextx >= 0) {
                            Move();
                        }
                    }

                    //System.out.println("LEFT pressed.");

                } else if (Gdx.input.isKeyPressed(this.controls.RightKey)) {

                    if(this.newdirection != "LEFT") {
                        this.newdirection = "RIGHT";
                        this.nextx = this.currentx + 1;
                        this.nexty = this.currenty;

                        //Move the snake right!
                        if (this.nextx < this.grid.gridlist.length) {
                            Move();
                        }
                    }

                    //System.out.println("RIGHT pressed.");

                }
            }

        } else {

            this.dt = this.dt + dt;

            /*
            Spawn countdown. Proved to be kind of irritating, so commenting out. Maybe I can find a better sound someday.

            if(this.dt > 1 && !this.playedblip[0]) {
                this.blip.play();
                this.playedblip[0] = true;
            }

            if(this.dt > 2 && !this.playedblip[1]) {
                this.blip.play();
                this.playedblip[1] = true;
            }
            */


            if (this.dt > 1) {

                //Show the grid sprite while waiting for Snake to spawn.
                ArrayList<int[]> spawnspaces = GetStartingSquares();
                for(int[] spaces : spawnspaces) {
                    this.grid.gridlist[spaces[0]][spaces[1]].animation = this.spawnanimation;
                }

            }

            if (this.dt > 3) {

                this.dt = 0;

                for(int z = 0; z < this.deadspaces.size(); z++) {
                    //Set the spaces where the snake exploded back to blank once snake has spawned.
                    this.grid.gridlist[this.deadspaces.get(z)[0]][this.deadspaces.get(z)[1]].sprite = blanksprite;
                }

                //Turn off the animation for spawning.
                for(int[] spaces : GetStartingSquares()) {
                    this.grid.gridlist[spaces[0]][spaces[1]].animation = null;
                    this.grid.gridlist[spaces[0]][spaces[1]].sprite = blanksprite;
                }

                Spawn();
            }

        }
    }

    public void dispose() {
        this.atlas.dispose();
    }


    private boolean HaveWeBeenHereBefore(int x, int y) {

        boolean result = false;
        int[] coordinates = new int[] {x, y};

        for(int z = 0; z < this.lastpositions.size(); z++) {
            if(this.lastpositions.get(z)[0] == coordinates[0]) {
                if(this.lastpositions.get(z)[1] == coordinates[1]) {
                    result = true;
                }
            }
        }
        //System.out.println(result);

        return result;
    }

    public void KillMe() {

        this.explosion.play();
        //TODO: Explosion animation
        for(int[] coordinates : this.lastpositions) {
            this.grid.gridlist[coordinates[0]][coordinates[1]].sprite = explosionsprite;
        }

        this.grid.gridlist[this.currentx][this.currenty].sprite = explosionsprite;

        for(int i = 0; i < this.lastpositions.size(); i++) {
            this.deadspaces.add(new int[] {this.lastpositions.get(i)[0], this.lastpositions.get(i)[1]});
        }

        this.deadspaces.add(new int[] { this.currentx, this.currenty });

        this.lastpositions.clear();
        this.isDead = true;

    }

    public void Move() {

        if(!HaveWeBeenHereBefore(this.nextx, this.nexty)) {

            this.snaketosplit = this.overlord.SnakeIsHere(this.nextx, this.nexty);

            if(this.snaketosplit == -1) {

                if (!SquareIsObstacle(this.nextx, this.nexty)) {

                    //Check if there's a mouse!
                    int mouseposition = this.overlord.MouseIsHere(this.nextx, this.nexty);

                    if (mouseposition != -1) {
                        EatMouse(mouseposition);
                    }

                    int powerupposition = this.overlord.PowerupIsHere(this.nextx, this.nexty);

                    if (powerupposition != -1) {
                        InvokePowerup(powerupposition);
                    }

                    //Put the snake where he'll be next.
                    if (this.newdirection == "UP") {
                        this.grid.gridlist[this.currentx][this.currenty + 1].sprite = this.upsprite;
                    } else if (this.newdirection == "DOWN") {
                        this.grid.gridlist[this.currentx][this.currenty - 1].sprite = this.downsprite;
                    } else if (this.newdirection == "LEFT") {
                        this.grid.gridlist[this.currentx - 1][this.currenty].sprite = this.leftsprite;
                    } else if (this.newdirection == "RIGHT") {
                        this.grid.gridlist[this.currentx + 1][this.currenty].sprite = this.rightsprite;
                    } else {
                        //System.out.println("FUCKED UP");
                    }

                    //Add the snake's position to the list of last positions.
                    this.lastpositions.add(new int[]{this.currentx, this.currenty});

                    //Update our snake's current position.
                    this.currentx = this.nextx;
                    this.currenty = this.nexty;

                    this.positionschanged = true;

                }

            } else {

                if(!this.isKingsnake) {
                    KillMe();
                } else {
                    Player player = this.overlord.getPlayer(this.snaketosplit);

                    //If you hit the other snake head on, they'll DiE and not you!
                    if(this.nextx == player.currentx && this.nexty == player.currenty) {
                        player.KillMe();
                    } else {
                        player.SplitSnake(this.nextx, this.nexty);
                    }
                }
            }

        } else {
            KillMe();
        }

    }

    public void SplitSnake(int x, int y) {

        this.explosion.play();
        int index = 0;
        //TODO: Explosion animation

        //Find the starting position of the hit.
        for(int z = 0; z < this.lastpositions.size(); z++) {
            if(this.lastpositions.get(z)[0] == x && this.lastpositions.get(z)[1] == y)  {
                index = z;
                break;
            }
        }

        for(int i = index; i < this.lastpositions.size(); i++) {
            this.deadspaces.add(new int[] {this.lastpositions.get(i)[0], this.lastpositions.get(i)[1]});
            this.lastpositions.remove(i);
        }

    }

    private void Spawn() {

        //Kill any snakes that dare to occupy this spawn area while you're spawning!
        int snakeoccupyingthissquare = this.overlord.SnakeIsHere(this.startx, this.starty);
        if(snakeoccupyingthissquare != -1) {
            this.overlord.getPlayer(snakeoccupyingthissquare).KillMe();
        }

        this.isDead = false;
        this.isKingsnake = false;
        this.length = 3;
        this.speed = this.speeddefault;
        this.currentx = startx;
        this.currenty = starty;
        this.newdirection = this.startdirection;
        this.kingsnakeTimer = 0;
        Arrays.fill(this.playedblip, false);

        //Set up last positions array.
        this.lastpositions = new ArrayList<int[]>();
        int[] coords = new int[2];
        coords[0] = startx;
        if(this.newdirection == "UP") {
            coords[1] = starty - 2;
        } else if(this.newdirection == "DOWN") {
            coords[1] = starty + 2;
        }
        this.lastpositions.add(0, coords);
        int[] differentcoords = new int[2];
        differentcoords[0] = startx;
        if(this.newdirection == "UP") {
            differentcoords[1] = starty - 1;
        } else if(this.newdirection == "DOWN") {
            differentcoords[1] = starty + 1;
        }
        this.lastpositions.add(1, differentcoords);
        this.positionschanged = false;

        //Set up origin position.
        //TODO for different players, different positions
        if(this.newdirection == "UP") {
            this.grid.gridlist[this.currentx][this.currenty].sprite = this.upsprite;
        } else if(this.newdirection == "DOWN") {
            this.grid.gridlist[this.currentx][this.currenty].sprite = this.downsprite;
        }
        this.grid.gridlist[this.lastpositions.get(0)[0]][this.lastpositions.get(0)[1]].sprite = this.verticalsprite;
        this.grid.gridlist[this.lastpositions.get(1)[0]][this.lastpositions.get(1)[1]].sprite = this.verticalsprite;

        this.spawnsound.play();
    }

    private boolean SquareIsObstacle(int x, int y) {

        boolean result = false;

        if(this.grid.gridlist[x][y].isObstacle) {
            result = true;
        }

        return result;

    }

    private void EatMouse(int index) {
        Sound munch = Gdx.audio.newSound(Gdx.files.internal("sounds/munch.wav"));
        munch.play();

        this.overlord.destroyMouse(index);
        this.length++;
        this.score++;
        this.eatenMouse = true;
    }

    private void InvokePowerup(int index) {
        this.overlord.InvokePowerup(index, this);
    }

    private void SetUpSprites(boolean ForKingsnake) {

        //Set up sprites.
        if(this.color == Color.BLACK) {
            this.atlas = new TextureAtlas(Gdx.files.internal("images/players/black.pack"));
        } else if(this.color == Color.ORANGE) {
            this.atlas = new TextureAtlas(Gdx.files.internal("images/players/orange.pack"));
        } else if(this.color == Color.RED) {
            this.atlas = new TextureAtlas(Gdx.files.internal("images/players/red.pack"));
        } else if(this.color == Color.YELLOW) {
            this.atlas = new TextureAtlas(Gdx.files.internal("images/players/yellow.pack"));
        }

        this.upsprite = new Sprite(this.atlas.findRegion("up"));
        this.upleftsprite = new Sprite(this.atlas.findRegion("upleft"));
        this.uprightsprite = new Sprite(this.atlas.findRegion("upright"));
        this.downsprite = new Sprite(this.atlas.findRegion("down"));
        this.downleftsprite = new Sprite(this.atlas.findRegion("downleft"));
        this.downrightsprite = new Sprite(this.atlas.findRegion("downright"));
        this.rightsprite = new Sprite(this.atlas.findRegion("right"));
        this.leftsprite = new Sprite(this.atlas.findRegion("left"));
        this.verticalsprite = new Sprite(this.atlas.findRegion("vertical"));
        this.horizontalsprite = new Sprite(this.atlas.findRegion("horizontal"));
        this.explosionsprite = new Sprite(this.atlas.findRegion("explosion"));
        this.kupsprite = new Sprite(this.atlas.findRegion("kup"));
        this.kupleftsprite = new Sprite(this.atlas.findRegion("kupleft"));
        this.kuprightsprite = new Sprite(this.atlas.findRegion("kupright"));
        this.kdownsprite = new Sprite(this.atlas.findRegion("kdown"));
        this.kdownleftsprite = new Sprite(this.atlas.findRegion("kdownleft"));
        this.kdownrightsprite = new Sprite(this.atlas.findRegion("kdownright"));
        this.krightsprite = new Sprite(this.atlas.findRegion("kright"));
        this.kleftsprite = new Sprite(this.atlas.findRegion("kleft"));
        this.kverticalsprite = new Sprite(this.atlas.findRegion("kvertical"));
        this.khorizontalsprite = new Sprite(this.atlas.findRegion("khorizontal"));
        this.kexplosionsprite = new Sprite(this.atlas.findRegion("kexplosion"));

    }

    private ArrayList<int[]> GetStartingSquares() {

        ArrayList<int[]> StartingSquares = new ArrayList<int[]>();

        int[] startcoords = new int[2];
        startcoords[0] = startx;
        startcoords[1] = starty;

        int[] coords = new int[2];
        coords[0] = startx;
        if(this.startdirection == "UP") {
            coords[1] = starty - 2;
        } else if(this.startdirection == "DOWN") {
            coords[1] = starty + 2;
        }
        this.lastpositions.add(0, coords);
        int[] differentcoords = new int[2];
        differentcoords[0] = startx;
        if(this.startdirection == "UP") {
            differentcoords[1] = starty - 1;
        } else if(this.startdirection == "DOWN") {
            differentcoords[1] = starty + 1;
        }

        StartingSquares.add(startcoords);
        StartingSquares.add(coords);
        StartingSquares.add(differentcoords);

        return StartingSquares;
    }

}
