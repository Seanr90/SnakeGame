import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    
    static final int WINDOW_WIDTH = 500;       // Game Window Size
    static final int WINDOW_HEIGHT = 500;     // Game Window Size
    static final int UNIT_SIZE = 25;         // Grid Size
    static final int GAME_UNITS = (WINDOW_WIDTH*WINDOW_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 75;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 3;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;


    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(WINDOW_HEIGHT,WINDOW_WIDTH));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();

    }
    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();

    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
        }
    public void draw(Graphics g) {
        if(running) {
    /*      for(int i = 0 ; i < WINDOW_HEIGHT / UNIT_SIZE; i++) {                        // Create grid in game
                g.drawLine( i * UNIT_SIZE,0, i * UNIT_SIZE, WINDOW_HEIGHT);             
                g.drawLine( 0, i * UNIT_SIZE, WINDOW_WIDTH, i * UNIT_SIZE);           
            }          */
            g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));  // Apple color                                                // Snake apple color
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);                           // Apple shape


            for(int i = 0; i < bodyParts; i++) {                                           // Create Snake Body
                if(i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else {
                    //g.setColor(new Color(45,180,0));        // Snake Color
                    g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.red);
            g.setFont( new Font("Arial",Font.BOLD, 30));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: "+applesEaten, (WINDOW_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
        }
        else {
            gameOver(g);
        }
        }

    public void newApple() {
        appleX = random.nextInt((int)(WINDOW_WIDTH/UNIT_SIZE))*UNIT_SIZE;           // Randomly place starting apple
        appleY = random.nextInt((int)(WINDOW_HEIGHT/UNIT_SIZE))*UNIT_SIZE;         // Randomly place starting apple
    }
    public void move(){
		for(int i = bodyParts;i>0;i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
		
	}
	public void checkApple() {
		if((x[0] == appleX) && (y[0] == appleY)) {   // Check if apple has been eaten, grow snake body
			bodyParts++;                                
			applesEaten++;
			newApple();
		}
	}
	public void checkCollisions() {

		for(int i = bodyParts;i>0;i--) {		//check if head collides with body
			if((x[0] == x[i])&& (y[0] == y[i])) {
				running = false;
			}
		}

		if(x[0] < 0) {		//check if head touches left border
			running = false;
		}

		if(x[0] > WINDOW_WIDTH) {		//check if head touches right border
			running = false;
		}

		if(y[0] < 0) {		//check if head touches top border
			running = false;
		}

		if(y[0] > WINDOW_HEIGHT) {		//check if head touches bottom border
			running = false;
		}
		
		if(!running) {
			timer.stop();
		}
	}
	public void gameOver(Graphics g) {
		// Display players score
		g.setColor(Color.red);
		g.setFont( new Font("Arial",Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Final Score: "+applesEaten, (WINDOW_WIDTH - metrics1.stringWidth("Final Score: "+applesEaten))/2, g.getFont().getSize());
		// Display Game Over
		g.setColor(Color.red);
		g.setFont( new Font("Arial",Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("GAME OVER", (WINDOW_WIDTH - metrics2.stringWidth("GAME OVER"))/2, WINDOW_HEIGHT/2);
    }
    @Override
    public void actionPerformed(ActionEvent e) {

        if(running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {       // Allow arrow keys to change directions
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
            }
        }
    }
}  
