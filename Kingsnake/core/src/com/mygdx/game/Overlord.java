package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.badlogic.gdx.math.RandomXS128;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Overlord {

    private Grid grid;
    private SpriteBatch batch;
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;
    private int winninglength = 12;
    public ArrayList<Player> players;
    private ArrayList<Mouse> mice;
    private ArrayList<Powerup> powerups;
    private ArrayList<Obstacle> obstacles;
    private ArrayList<int[]> spawnpoints;
    private float delta;
    private float poweruptimer;
    private int mousenumber = 0;

    public Overlord(Grid grid, SpriteBatch batch) {
        this.grid = grid;
        this.batch = batch;
        this.players = new ArrayList<Player>();
        this.mice = new ArrayList<Mouse>();
        this.powerups = new ArrayList<Powerup>();
        this.obstacles = new ArrayList<Obstacle>();

        createSpawnPoints();
    }

    public void createPlayer(int number, Color color, int startx, int starty, String direction, Controls controls) {

        switch(number) {
            case 1:
                this.player1 = new Player(number, this.batch, color, this.grid, startx, starty, direction, controls, this);
                addPlayer(this.player1);
                break;
            case 2:
                this.player2 = new Player(number, this.batch, color, this.grid, startx, starty, direction, controls, this);
                addPlayer(this.player2);
                break;
            case 3:
                this.player3 = new Player(number,this.batch, color, this.grid, startx, starty, direction, controls, this);
                addPlayer(this.player3);
                break;
            case 4:
                this.player4 = new Player(number, this.batch, color, this.grid, startx, starty, direction, controls, this);
                addPlayer(this.player4);
                break;
        }

    }

    public void createMouse(int number, int startx, int starty) {
        Mouse newmouse = new Mouse(number, this.batch, this.grid, startx, starty, this);
        addMouse(newmouse, number);
    }

    public void createObstacle(Sprite sprite, int x, int y) {
        Obstacle newobstacle = new Obstacle(this.grid, sprite, x, y);
        addObstacle(newobstacle);
    }

    public void createPowerup(int number, int startx, int starty) {

        String[] powerups = new String[] {"SKATES", "CROWN", "GEM"};

        RandomXS128 rand = new RandomXS128();

        int index = rand.nextInt(powerups.length);

        Powerup powerup;

        if(index == 0) {
            powerup = new Skates(number, this.batch, this.grid, startx, starty, this);
        } else if(index == 1){

            if(NobodyIsAKingsnake()) {
                powerup = new Crown(number, this.batch, this.grid, startx, starty, this);
            } else {
                powerup = new Skates(number, this.batch, this.grid, startx, starty, this);
            }

        } else {
            powerup = new Gem(number, this.batch, this.grid, startx, starty, this);
        }

        addPowerup(powerup);
    }

    public Player getPlayer(int playernumber) {

        int index = -1;

        for(int x = 0; x < this.players.size(); x++) {
            if(this.players.get(x).number == playernumber) {
                index = x;
            }
        }

        return this.players.get(index);
    }

    public void destroyMouse(int index) {
        System.out.println("Destroying mouse with number: " + index);
        int iterator = 0;
        int actualIndex = -1;

        for(Mouse mouse : this.mice) {
            if(mouse.number == index) {
                actualIndex = iterator;
                System.out.println("Found mouse with number: " + index + " at position: " + actualIndex);
            }
            iterator++;
        }

        if(actualIndex != -1) {
            this.mice.get(actualIndex).isDead = true;
            this.mice.remove(actualIndex);
            this.delta = 0;
        }
    }

    private void addPlayer(Player player) {
        this.players.add(player);
    }

    private void addMouse(Mouse mouse, int number) {
        this.mice.add(mouse);
    }

    private void addObstacle(Obstacle obstacle) {
        this.obstacles.add(obstacle);
    }

    private void addPowerup(Powerup powerup) {
        this.powerups.add(powerup);
    }

    private int[] findEmptyPoint() {

        RandomXS128 random = new RandomXS128();
        int randomx;
        int randomy;

        do {
            randomx = random.nextInt(15);
            randomy = random.nextInt(15);
        } while(SquareIsSpawnPoint(randomx, randomy) || this.grid.gridlist[randomx][randomy].isObstacle || SnakeIsHere(randomx, randomy) != -1 || MouseIsHere(randomx, randomy) != -1 || PowerupIsHere(randomx, randomy) != -1);

        return new int[] {randomx, randomy};

    }

    public int SnakeIsHere(int x, int y) {

        int result = -1;

        for(Player player : this.players) {

            if(!player.isDead) {

                if(player.currentx == x && player.currenty == y) {
                    result = player.number;
                }

                for (int[] coords : player.lastpositions) {
                    if (coords[0] == x && coords[1] == y) {
                        result = player.number;
                    }
                }

            }
        }

        return result;

    }

    public int MouseIsHere(int x, int y) {

        int result = -1;

        for(Mouse mouse : this.mice) {
            if(mouse.currentx == x && mouse.currenty == y) {
                result = mouse.number;
            }
        }

        return result;
    }

    public int PowerupIsHere(int x, int y) {

        int result = -1;

        for(Powerup powerup : this.powerups) {
            if(powerup.getCurrentX() == x && powerup.getCurrentY() == y) {
                result = powerup.getNumber();
            }
        }

        return result;
    }

    public boolean NobodyIsAKingsnake() {

        boolean result = true;

        for(Player player : this.players) {
            if(player.isKingsnake == true) {
                result = false;
            }
        }

        return result;

    }

    public boolean SquareIsPassable(int x, int y) {

        boolean result = true;

        if(x > 15) {
            //System.out.println("DiE at 126");
            result = false;
            return result;
        }

        if(y > 15) {
            //System.out.println("DiE at 132");
            result = false;
            return result;
        }

        if(x < 0) {
            //System.out.println("DiE at 138. Danzig approves!");
            result = false;
            return result;
        }

        if(y < 0) {
            //System.out.println("DiE at 144");
            result = false;
            return result;
        }

        if(this.grid.gridlist[x][y].isObstacle) {
            result = false;
        }

        if(this.SquareIsSpawnPoint(x, y)) {
            result = false;
        }

        for(Player snake : this.players) {

            for(int[] coords : snake.lastpositions) {
                if(coords[0] == x && coords[1] == y) {
                    result = false;
                }
            }

            if(snake.currentx == x && snake.currenty == y) {
                result = false;
            }

        }

        return result;

    }

    private void createSpawnPoints() {

        this.spawnpoints = new ArrayList<int[]>();
        spawnpoints.add(new int[] {0, 0});
        spawnpoints.add(new int[] {0, 1});
        spawnpoints.add(new int[] {0, 2});
        spawnpoints.add(new int[] {1, 0});
        spawnpoints.add(new int[] {1, 1});
        spawnpoints.add(new int[] {1, 2});
        spawnpoints.add(new int[] {0, 13});
        spawnpoints.add(new int[] {0, 14});
        spawnpoints.add(new int[] {0, 15});
        spawnpoints.add(new int[] {1, 13});
        spawnpoints.add(new int[] {1, 14});
        spawnpoints.add(new int[] {1, 15});
        spawnpoints.add(new int[] {14, 13});
        spawnpoints.add(new int[] {14, 14});
        spawnpoints.add(new int[] {14, 15});
        spawnpoints.add(new int[] {15, 13});
        spawnpoints.add(new int[] {15, 14});
        spawnpoints.add(new int[] {15, 15});
        spawnpoints.add(new int[] {14, 0});
        spawnpoints.add(new int[] {14, 1});
        spawnpoints.add(new int[] {14, 2});
        spawnpoints.add(new int[] {15, 0});
        spawnpoints.add(new int[] {15, 1});
        spawnpoints.add(new int[] {15, 2});

    }

    public boolean SquareIsSpawnPoint(int x, int y) {

        boolean result = false;

        for(int z = 0; z < this.spawnpoints.size(); z++)  {

            if(this.spawnpoints.get(z)[0] == x) {
                if(this.spawnpoints.get(z)[1] == y) {
                    result = true;
                }
            }

        }

        return result;
    }

    public void InvokePowerup(int index, Player player) {

        this.powerups.get(index).Invoke(player);

    }


    public void render() {

        for(Player player : this.players) {
            player.render();
        }

        for(Obstacle obstacle : this.obstacles) {
            obstacle.render();
        }

        for(Mouse mouse : this.mice) {
            if(mouse.isDead) {
                destroyMouse(mouse.number);
            } else {
                mouse.render();
            }
        }

        Iterator<Powerup> iter = this.powerups.iterator();
        while (iter.hasNext()) {
            Powerup powerup = iter.next();

            if (powerup.getisCollected()) {
                iter.remove();
            } else {
                powerup.render();
            }
        }

        //Decide whether a mouse needs to get spawned!
        float delta = Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f);
        this.delta = this.delta + delta;
        this.poweruptimer = this.poweruptimer + delta;

        if(this.delta > 1 && this.mice.size() < 4) {
            this.delta = 0;
            int[] emptypoint = findEmptyPoint();

            //Find the first available index.

            System.out.println("New Mouse index: " + mousenumber);
            createMouse(mousenumber, emptypoint[0], emptypoint[1]);
            this.mousenumber++;
        }

        //Decide whether a powerup needs to get spawned!
        if(this.poweruptimer > 11 && this.powerups.size() < 1) {
            this.poweruptimer = 0;
            int[] emptypoint = findEmptyPoint();
            createPowerup(this.powerups.size(), emptypoint[0], emptypoint[1]);
        }

    }

    public void dispose() {

        for(Player player : this.players) {
            player.dispose();
        }

        for(Mouse mouse : this.mice) {
            mouse.dispose();
        }

        for(Powerup powerup : this.powerups) {
            powerup.dispose();
        }

    }

    public int WinningPlayer() {

        int result = -1;
        for(Player player : this.players) {
            if(player.length == this.winninglength) {
                result = player.number;
                player.isWinner = true;
            }
        }

        return result;

    }

}
