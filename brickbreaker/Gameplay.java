package brickbreaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Gameplay extends JPanel implements KeyListener, ActionListener {
    private boolean play=false;
    private int score=0;
    private int totalBricks=21;
    private Timer time;
    private int delay=0;
    private int playerX=310;
    private int ballposX=120;
    private int ballposY=350;
    private int ballXdir=-1;
    private int ballYdir=-2;
    private Timer timer;  // Declare timer as an instance variable
    private Mapgenerator map;
    public Gameplay(){
        map= new Mapgenerator(3,7);
        map.generateRandomPattern();
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
        totalBricks = calculateTotalBricks();
    }

    // Calculate the total number of bricks in the map
    private int calculateTotalBricks() {
        int totalBricksInMap = 0;
        for (int i = 0; i < map.map.length; i++) {
            for (int j = 0; j < map.map[0].length; j++) {
                if (map.map[i][j] == 1) { // Only count actual bricks (value 1)
                    totalBricksInMap++;
                }
            }
        }
        return totalBricksInMap;
    }


    public void paint(Graphics g) {
        //background
        g.setColor(Color.blue);
        g.fillRect(1, 1, 692, 592);
        //borders
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);
        //drawing map
        map.draw((Graphics2D) g);
        //scores
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("" + score, 590, 30);

        //thepeddle
        g.setColor(Color.RED);
        g.fillRect(playerX, 550, 100, 8);
        //theball
        g.setColor(Color.BLACK);
        g.fillOval(ballposX, ballposY, 20, 20);

        if (totalBricks <= 0) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("YOU WON", 270, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("PRESS ENTER TO RESTART", 230, 350);
        }

        if (ballposY > 570) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("GAME OVER YOU SCORED: " + score, 170, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("PRESS ENTER TO RESTART", 230, 350);
        }
        g.dispose();


    }
    private void printMap() {
        for (int i = 0; i < map.map.length; i++) {
            for (int j = 0; j < map.map[0].length; j++) {
                System.out.print(map.map[i][j] + " ");
            }
            System.out.println();
        }
    }


    @Override
    public void actionPerformed(ActionEvent e){
            timer.start();
            if (play) {
                if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
                    ballYdir = -ballYdir;
                }
                A:
                for (int i = 0; i < map.map.length; i++) {
                    for (int j = 0; j < map.map[0].length; j++) {
                        if (map.map[i][j] > 0) {
                            int brickX = j * map.brickWidth + 80;
                            int brickY = i * map.brickHeight + 50;
                            int brickWidth = map.brickWidth;
                            int brickHeight = map.brickHeight;

                            Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                            Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
                            Rectangle brickRect = rect;
                            if (ballRect.intersects(brickRect)) {
                                int brickValue = map.map[i][j]; // Get the value of the brick

                                if (brickValue == 1) { // Regular brick
                                    map.setBrickValue(0, i, j);
                                    totalBricks--;
                                    score += 5;

                                    if (ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width) {
                                        ballXdir = -ballXdir;
                                    } else {
                                        ballYdir = -ballYdir;
                                    }

                                    break A;
                                } else if (brickValue == 2) { // Generated brick
                                    map.setBrickValue(0, i, j);
                                    totalBricks--;
                                    score += 10; // Adjust the score for generated bricks

                                    if (ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width) {
                                        ballXdir = -ballXdir;
                                    } else {
                                        ballYdir = -ballYdir;
                                    }
                                }

                                    break A;
                                }
                            }
                          }

                        }

                ballposX += ballXdir;
                ballposY += ballYdir;

                if (ballposX < 0) {
                    ballXdir = -ballXdir;
                }
                if (ballposY < 0) {
                    ballYdir = -ballYdir;
                }
                if (ballposX > 670) {
                    ballXdir = -ballXdir;
                }

                repaint();
            }}

        @Override
        public void keyTyped(KeyEvent e) {}
        @Override
        public void keyReleased(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_RIGHT){
            if(playerX>=600) {
                playerX = 600;
            }
            else {
                moveRight();

            }
        }
        if(e.getKeyCode()==KeyEvent.VK_LEFT){
            if(playerX<10) {
                playerX = 10;
            }
            else{
                moveLeft();
            }
        }
        if(e.getKeyCode()==KeyEvent.VK_ENTER){
            if(!play){
                play=true;
                ballposX=120;
                ballposY=350;
                ballXdir=-1;
                ballYdir=-2;
                playerX=310;
                score=0;
                totalBricks=21;
                map=new Mapgenerator(3,7);
                map.generateRandomPattern();
                repaint();
            }
        }
    }
    public void moveRight(){
        play=true;
        playerX+=20;
    }
    public void moveLeft(){
        play=true;
        playerX-=20;
    }


}
