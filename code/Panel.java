package code;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Panel extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	//window constants
		private final int WINDOW_WIDTH = 600;	//600
	    private final int WINDOW_HEIGHT = 600;	//600
	    private final int DOT_SIZE = 10;
	    private final int GRID_WIDTH = WINDOW_WIDTH/DOT_SIZE;
	  	private final int GRID_HEIGHT = WINDOW_HEIGHT/DOT_SIZE;
	    private final int RAND_POS = GRID_WIDTH-2;	//28
	    private final int DELAY = 250;	//140 timer speed, ms between ticks, reduce to speed up game
	    //button constants
	    private final int BUTTON_WIDTH = 120;	//150
	    private final int BUTTON_HEIGHT = 50;	//50
	    private final int BUTTON_FONTSIZE = 14;
	    //player constants
	    private final int SNAKE_STARTING_LENGTH = 2;
	    private final int SNAKE_HUNGER = 99;
	    private final int WINNING_APPLES = 30;
	    private final int STONE_COUNTER = 100;	//100
	    private final int STONE_COL_TIMER = 30;
	    //controls
	    private final Integer[] CONTROLS_1 = {37, 38, 39, 40};	//left up right down
	    private final Integer[] CONTROLS_2 = {65, 87, 68, 83};	//a w d s
	    //tile map
	    private Tiles tiles = new Tiles();
	    private int tileMap[][] = tiles.tileMaps[0];

	    private int tileMapWidth = 12;	//length of rows and columns
	    private int tileMapHeight = 12;
	    private int tileGridWidth = WINDOW_WIDTH/DOT_SIZE/tileMapWidth;	//in grid units, not pixels	
	    private int tileGridHeight = WINDOW_HEIGHT/DOT_SIZE/tileMapHeight;	//600/10/6=10
	    
	    //game mode
	    private boolean onePlayer = true;
	    private boolean inGame = false;
	    private Integer loser = null;
	    private Integer winner = null;
	    private boolean draw = false;
	    //object arrays
	    ArrayList<Snake> snakes = new ArrayList<Snake>();
	    ArrayList<Apple> apples = new ArrayList<Apple>();
	    ArrayList<Stone> stones = new ArrayList<Stone>();
	    //sound
	    Sound gameMusic = new Sound();
	    Sound menuMusic = new Sound();
	    Sound soundEffect = new Sound();
	    //game variables
	    private Input input1 = new Input(CONTROLS_1);
	    private Input input2 = new Input(CONTROLS_2);
	    JButton playButton = new JButton();
	    JButton playersButton = new JButton();
	    private int stoneCounter = STONE_COUNTER;
	    private Timer timer;
	    private Image ball;
	    private Image ballp;
	    private Image apple;
	    private Image head;
    

    public Panel() {
    	//initiate button settings
    	playButton.setBounds(WINDOW_WIDTH*2/4, (WINDOW_HEIGHT - 2*BUTTON_HEIGHT), BUTTON_WIDTH, BUTTON_HEIGHT);	//start x, start y, width, height
		playButton.setText("Play");
		playButton.setFont(new Font("Arial", Font.ITALIC, BUTTON_FONTSIZE));
		playButton.setForeground(Color.white);
		playButton.setBackground(Color.black);
		playButton.addActionListener(this);
		playersButton.setBounds(WINDOW_WIDTH*1/4, (WINDOW_HEIGHT - 2*BUTTON_HEIGHT), BUTTON_WIDTH, BUTTON_HEIGHT);	//start x, start y, width, height
		playersButton.setText("1/2 Players");
		playersButton.setFont(new Font("Arial", Font.ITALIC, BUTTON_FONTSIZE));
		playersButton.setForeground(Color.white);
		playersButton.setBackground(Color.black);
		playersButton.addActionListener(this);
        initBoard();
    }
    
    
    private void initBoard() {
    	addKeyListener(input1);
    	addKeyListener(input2);
        setBackground(Color.black);
        setFocusable(true);
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        loadImages();
        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void loadImages() {
        
    	ImageIcon iid = new ImageIcon(getClass().getResource("/resources/dot.png"));
        ball = iid.getImage();
        ImageIcon iip = new ImageIcon(getClass().getResource("/resources/dot-purp.png"));
        ballp = iip.getImage();
        ImageIcon iia = new ImageIcon(getClass().getResource("/resources/apple.png"));
        apple = iia.getImage();
        ImageIcon iih = new ImageIcon(getClass().getResource("/resources/head.png"));
        head = iih.getImage();
    }

    private void initGame() {
    	//reset losers/winners
    	loser = null;
    	winner = null;
    	draw = false;
    	
    	//reset stones
    	resetStones();
    	//create snakes
    	snakes.add(new Snake(GRID_WIDTH/2, GRID_HEIGHT/2-1, 1, 0, SNAKE_STARTING_LENGTH, input1, SNAKE_HUNGER));
    	//extra snake for 1-vs-1 mode
    	if (!onePlayer) {
    		snakes.add(new Snake(GRID_WIDTH/2, GRID_WIDTH/2+1, 1, 0, SNAKE_STARTING_LENGTH, input2, SNAKE_HUNGER));
    	}
    	//add apple
    	addApple();
    	//play game music
    	playMusic(gameMusic, 0);
    }
    
    
  //add stones as a tile map
    private void addStones() {
    	playSoundEffect(7);
    	int tileGridX;
    	int tileGridY;
    	for(int i=0; i < tileMap.length; i++) {
			for(int j=0; j < tileMap[i].length; j++) {
				if(tileMap[i][j] == 1) {
					tileGridX = i * tileGridWidth;
					tileGridY = j * tileGridHeight;
					Stone stone = new Stone(tileGridX, tileGridY, tileGridWidth, tileGridHeight, STONE_COL_TIMER);
					stones.add(stone);
				}
			}
    	}
    }
  //reset stones
    private void resetStones() {
    	tiles.tileCounter = 0;
    	tileMap = tiles.tileMaps[0];
    	stones = new ArrayList<Stone>();
    }
  //add apples at random locations
    private void addApple() {
    	Apple apple = new Apple(RAND_POS);
    	boolean appleCollision = false;
    	do {
    		for(int i=0; i < stones.size(); i++) {
    			appleCollision = stones.get(i).checkCollisionWithObject(apple);
    			if (appleCollision==true) {
    				apple = new Apple(RAND_POS);
    				break;
    			}
    		}
    		if(!appleCollision) {
    			for(int i=0; i < snakes.size(); i++) {
        			appleCollision = snakes.get(i).body.get(0).checkCollisionWithObject(apple);
        			if (appleCollision==true) {
        				apple = new Apple(RAND_POS);
        				break;
        			}
        		}
    		}
    	} while(appleCollision == true);
    	apples.add(apple);
    }
  //sound methods
    private void playMusic(Sound music, int i) {
    	music.setFile(i);
    	music.play();
    	music.loop();
    }
    private void stopMusic(Sound music) {
    	music.stop();
    }
    private void playSoundEffect(int i) {
    	soundEffect.setFile(i);
    	soundEffect.play();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }
    
    private void doDrawing(Graphics g) {
        
        if (inGame) {
        	//draw stones
        	for(int i=0; i < stones.size(); i++) {
        		Stone stone = stones.get(i);
        		if (stone.colTimer > 0) {
        			g.setColor(Color.DARK_GRAY);
        		} else {
        			g.setColor(Color.GRAY);
        		}
                g.fillRect(stone.x * DOT_SIZE, stone.y * DOT_SIZE, stone.width*DOT_SIZE, stone.height*DOT_SIZE);
        	}
        	//draw apples
        	for(int i=0; i < apples.size(); i++) {
        		Apple appleObject = apples.get(i);
        		g.drawImage(apple, appleObject.x*DOT_SIZE, appleObject.y*DOT_SIZE, this);
        	}
        	//draw snakes
        	for(int i=0; i < snakes.size(); i++) {
        		Image bodyImage = ball;
        		if(i==1) {
        			bodyImage = ballp;
        		}
        		for(int j=0; j < snakes.get(i).body.size(); j++) {
        			if(j==0) {
        				g.drawImage(head, snakes.get(i).body.get(0).x*DOT_SIZE, snakes.get(i).body.get(0).y*DOT_SIZE, this);
        			} else {
        				g.drawImage(bodyImage, snakes.get(i).body.get(j).x*DOT_SIZE, snakes.get(i).body.get(j).y*DOT_SIZE, this);
        			}
        		}
        	}
        	
            Toolkit.getDefaultToolkit().sync();
          //draw hunger
            for(int i=0; i < snakes.size(); i++) {
            	Snake snake = snakes.get(i);
            	Integer hunger = snake.hunger;
            	if (hunger < 33) {
            		drawMessage(g, hunger.toString(), (WINDOW_WIDTH * 2 - 90) * (1-i) + 45, WINDOW_HEIGHT / 2, 40, Color.red);
            	} else if (i==0) {
            		drawMessage(g, hunger.toString(), (WINDOW_WIDTH * 2 - 65) * (1-i) + 35, WINDOW_HEIGHT / 2, 30, Color.green);
            	} else {
            		drawMessage(g, hunger.toString(), (WINDOW_WIDTH * 2 - 65) * (1-i) + 35, WINDOW_HEIGHT / 2, 30, Color.MAGENTA);
            	}
            	//draw eaten apples
            	drawMessage(g, ""+snake.applesEaten+"", (WINDOW_WIDTH * 2 - 65) * (1-i) + 35, WINDOW_HEIGHT / 10, 30, Color.yellow);
            	
            }
            
            
        } else {
        	//MENU MESSAGES
        	//win/lose messages
        	if(draw) {
        		drawMessage(g, "IT'S A DRAW", WINDOW_WIDTH, WINDOW_HEIGHT / 5, 14, Color.green);
        	} else
        	if(loser!=null) {
            	drawMessage(g, "Player "+loser.toString()+" LOST THE GAME", WINDOW_WIDTH, WINDOW_HEIGHT / 4, 14, Color.red);
        	}
        	if(winner!=null) {
        		drawMessage(g, "Player "+winner.toString()+" WON THE GAME", WINDOW_WIDTH, WINDOW_HEIGHT / 5, 14, Color.green);
        	}
        	
        	//game-play instructions
            drawMessage(g, "Eat an Apple Before the Timer Runs Out", WINDOW_WIDTH, WINDOW_HEIGHT / 3, 14, Color.yellow);
            drawMessage(g, "Eat "+WINNING_APPLES+" apples to win.", WINDOW_WIDTH, WINDOW_HEIGHT / 5 * 2, 14, Color.yellow);
            drawMessage(g, "Use the Arrows or WASD to move, double press to Speed Up", WINDOW_WIDTH, WINDOW_HEIGHT / 3 * 2, 14, Color.white);
            
            //game mode messages
            if(onePlayer) {
            	drawMessage(g, "Single-player mode", 
                		WINDOW_WIDTH/2, WINDOW_HEIGHT * 3 / 4, 14, Color.white);
            } else {
            	drawMessage(g, "1-vs-1 mode", 
                		WINDOW_WIDTH/2, WINDOW_HEIGHT * 3 / 4, 14, Color.white);
            }
        }        
    }

    private void drawMessage(Graphics g, String msg, int width, int height, int size, java.awt.Color color) {
        Font small = new Font("Helvetica", Font.BOLD, size);
        FontMetrics metr = getFontMetrics(small);
        g.setColor(color);
        g.setFont(small);
        g.drawString(msg, (width - metr.stringWidth(msg)) / 2, height);
    }
    
    private void addButtons() {
    	if(playButton.getParent() == null) {	//getParent == null means the buttons has no parent, it is not in panel
    		add(playButton);
    	}
    	if(playersButton.getParent() == null) {
    		add(playersButton);
    	}
		
    }
    
    //GAME LOOP: update all objects and check and resolve collisions
    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {	//game loop
    	//add stones
        	stoneCounter -= 1;
        	if (stoneCounter == 0) {
	        	tiles.tileCounter += 1;
	        	
	        	tileMap = tiles.tileMaps[tiles.tileCounter];
	        	addStones();
	        	
	        	stoneCounter = STONE_COUNTER;
        	}	
    	//reduce stone collision timers for each stone and decide to play sound
        	boolean stonesBecameCollidable = false;
        	for(int k=0; k < stones.size(); k++) {
    			Stone stone = stones.get(k);
    			if(stone.colTimer > 0) {
    				stone.colTimer -= 1;
    				if(stone.colTimer == 0) {
    					stonesBecameCollidable = true;
    				}
    			}
        	}
        	if(stonesBecameCollidable) {
        		playSoundEffect(8);
        	}
        //check stone-apple collisions
        	for(int i=0; i < stones.size(); i++) {
    			Stone stone = stones.get(i);
    			if(stone.colTimer <= 0) {
    				for(int k=0; k<apples.size(); k++) {
    					Apple apple = apples.get(k);
    					if(stone.checkCollisionWithObject(apple)) {
    						apples.remove(apple);
    						addApple();
    					}
    				}
    			}
        	}		
    	
        //check stone-apple collisions
        	for(int i=0; i < stones.size(); i++) {
    			Stone stone = stones.get(i);
    			if(stone.colTimer <= 0) {
    				for(int k=0; k<apples.size(); k++) {
    					Apple apple = apples.get(k);
    					if(stone.checkCollisionWithObject(apple)) {
    						apples.remove(apple);
    						addApple();
    					}
    				}
    			}
        	}
    	//get snakes, move snakes ALSO reduce hunger
        	if(input1.action) {
        		playSoundEffect(2);
        		input1.action = false;
        	}
        	if(input2.action) {
        		playSoundEffect(2);
        		input2.action = false;
        	}
        	for(int i=1; i <= snakes.size(); i++) {
        		Snake snake = snakes.get(i-1);
        		Object head = snake.body.get(0);
        		
        		if (snake.input.left) { head.vx = -1; head.vy = 0; }
    			else if (snake.input.right) { head.vx = 1; head.vy = 0; }
    			else if (snake.input.up) { head.vy = -1; head.vx = 0; }
    			else if (snake.input.down) { head.vy = 1; head.vx = 0; }
        		if(snake.input.speed == true) {
        			snake.speed = 3;
        		} else {
        			snake.speed = 1;
        		}
        		for(int j=0; j < snake.speed; j++) {
        			snake.move();	//moves j times depending on speed
        		}
        		//hunger
        		snake.hunger -= 1;
        		if(snake.hunger < 0) {
        			playSoundEffect(4);
        			inGame = false;
    				if(loser==null) {
    					loser = i;
    				} else {
    					draw = true;
    					loser = null;
    				}
        			
    				break;
        		}
        	}	

    	//check snake-wall collisions
        	for(int i=1; i <= snakes.size(); i++) {
    			if (snakes.get(i-1).body.get(0).checkCollisionWithWindow(GRID_WIDTH, GRID_HEIGHT)) {
    				playSoundEffect(4);
    				inGame = false;
    				loser = i;
    				break;
    			}
        	}
        	
    	//check snake-stone collisions
        	for(int i=1; i <= snakes.size(); i++) {
        		Snake snake = snakes.get(i-1);
            	for(int j=0; j < snake.body.size(); j++) {
	        		Object part = snake.body.get(j);
	        		for(int k=0; k < stones.size(); k++) {
	        			Stone stone = stones.get(k);
	        			if(stone.colTimer <= 0 && stone.checkCollisionWithObject(part)) {
	        				playSoundEffect(4);
	        				inGame = false;
	        				loser = i;
	        				break;
	        			};
	        		}	
        		}	
        	}
        	
    	//check snake-snake collisions
        	if(!onePlayer) {
	        	Snake snake1 = snakes.get(0);
	        	Snake snake2 = snakes.get(1);
	        	Object snake1head = snake1.body.get(0);
	        	Object snake2head = snake2.body.get(0);
	        	for(int i=1; i < snake2.body.size(); i++) {
	        		if(snake1head.checkCollisionWithObject(snake2.body.get(i))) {
	        			playSoundEffect(4);
	        			inGame = false;
	        			loser = 1;
        				break;
	        		}
	        	}
	        	for(int i=1; i < snake1.body.size(); i++) {
	        		if(snake2head.checkCollisionWithObject(snake1.body.get(i))) {
	        			playSoundEffect(4);
	        			inGame = false;
	        			loser = 2;
        				break;
	        		}
	        	}
        	} else {
        		Snake snake1 = snakes.get(0);
        		Object snake1head = snake1.body.get(0);
        		for(int i=1; i < snake1.body.size(); i++) {
	        		if(snake1head.checkCollisionWithObject(snake1.body.get(i))) {
	        			playSoundEffect(4);
	        			inGame = false;
	        			loser = 1;
        				break;
	        		}
	        	}
        	}
        	
    	//check snake-apple collisions
        		outerloop:
            	for(int i=0; i < snakes.size(); i++) {
            		Snake snake = snakes.get(i);
        			for(int j=0; j < apples.size(); j++) {
        				Apple apple = apples.get(j);
        				for(int k = 0; k < snake.body.size(); k++) {
    	    				if(snake.body.get(k).checkCollisionWithObject(apple)) {
    	    					//if true means there was a collision between 
    	    					//a snake body part and an apple
    	    					playSoundEffect(3);
    	    					snake.applesEaten += 1;
    	    					if(snake.applesEaten >= WINNING_APPLES) {
    	    						inGame = false;
    	    	        			winner = i+1;
    	    	        			break outerloop;
    	    					}
    	    					//get the last body part(tail) and grow it
    	    					Object lastBodyPart = snake.body.get(snake.length-1);
    	    					snake.body.add(
    	    							//add a tail part the first time
    	    							new Object(lastBodyPart.x-lastBodyPart.vx, lastBodyPart.y-lastBodyPart.vy, 
    	    									lastBodyPart.vx, lastBodyPart.vy));	//added opposite to velocity (behind)
    	    					lastBodyPart = snake.body.get(snake.length-1);
    	    					//add a new tail part a second time
    	    					snake.body.add(
    	    							new Object(lastBodyPart.x-lastBodyPart.vx, lastBodyPart.y-lastBodyPart.vy, 
    	    									lastBodyPart.vx, lastBodyPart.vy));
    	    					//remove the eaten apple
    	    					apples.remove(j);
    	    					if(snake.hunger < SNAKE_HUNGER/3) {
    	    						addApple();
    	    					}
    	    					addApple();	//new apple every time an apple is eaten
    	    					snake.length += 2;	//a number showing the length of the snake
    	    					snake.hunger = SNAKE_HUNGER;	//snake's hunger filled
    	    				}
        				}	
        			}
        		}
            	
            //check if there was a WINNER
            	if(loser!= null && !onePlayer) {	//if one of two players lost
            		winner = loser % 2 + 1;	//if 1 lost, then 2 is the winner
            	}
        	
        	
    //game over
        } else {	//menu
        	//change game music to menu music
        	if(gameMusic.clip!=null) {
        		stopMusic(gameMusic);
        	}
        	if(menuMusic.clip==null) {
        		playMusic(menuMusic, 1);
        	}
        	//remove all snakes and apples and stones
        	for(int i=0; i < snakes.size(); i++) {
        		snakes.remove(i);
        	}
        	for(int i=0; i < apples.size(); i++) {
        		apples.remove(i);
        	}
        	//add buttons
        	addButtons();

        	//check buttons and resume game
        	if(e.getSource()==playersButton && this.onePlayer) {
        		playSoundEffect(6);
    			//if changing players mode - switch number of players
    			this.onePlayer = false;
    		} else if(e.getSource()==playersButton && !this.onePlayer) {
    			//make one player mode if it was two players
    			this.onePlayer = true;
    		}
        	if(e.getSource()==playButton) {
        		playSoundEffect(5);
        		//back to game loop
        		this.remove(playButton);
        		this.remove(playersButton);	//remove all the buttons
        		inGame = true;
        		initGame();
        		input1.resetControls();
        		input2.resetControls();	//reset all input controls
        		//change menu music
        		stopMusic(menuMusic);
        		menuMusic.clip = null;
        		
        	}
        }
        
        repaint();
    }
}
