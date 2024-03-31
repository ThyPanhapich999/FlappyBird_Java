import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;


public class FlappyBird extends JPanel implements ActionListener, KeyListener{
    int boardwidth = 864;
    int boardheight = 768;

    //Images
    Image backgroundImg;
    Image groundImg;
    Image birdImg1;
    Image birdImg2;
    Image birdImg3;
    Image pipeImg;
    Image restartImg;

    //Bird
    int birdX = boardwidth / 2 - 300;
    int birdY = boardheight / 2; // Change this line
    int birthWidth = 34;
    int birdHeight = 24;

    class Bird{
        int x = birdX;
        int y = birdY; // This will now be updated to the new birdY value
        int width = birthWidth;
        int height = birdHeight;
        Image img;

        Bird(Image img){
            this.img = img;
        }

        // Method to get the current image
        public Image getCurrentBirdImage() {
            switch (currentImage) {
                case 1:
                    return birdImg1;
                case 2:
                    return birdImg2;
                case 3:
                    return birdImg3;
                default:
                    return birdImg1;
            }
            
        }
    }

    //Pipes
    class Pipe {
        int pipeX = boardwidth;
        int pipeY = 0;
        int pipeWidth = 64;
        int pipeHeight = 520;
        int gap = 200;
        Image img;
        boolean flipped; // Added variable to indicate pipe orientation

        Pipe(Image img) {
            this.img = img;
            this.flipped = false; // Default orientation is not flipped
        }

        Pipe(int x, int y, int width, int height, Image img) {
            this.pipeX = x;
            this.pipeY = y;
            this.pipeWidth = width;
            this.pipeHeight = height;
            this.img = img;
            this.flipped = false; // Default orientation is not flipped
        }

        public void flip() {
            flipped = !flipped;
        }
    }

    //game logic
    Bird bird;
    int velocityX = -4; //Move to the left
    int velocityY = 0;
    int gravity = 1;

    ArrayList<Pipe> pipes;

    Timer gameLoop;
    Timer placePipesTimer;

    FlappyBird() {
        setPreferredSize(new Dimension(boardwidth, boardheight));
        setFocusable(true);
        addKeyListener(this); 

        // Load images
        backgroundImg = new ImageIcon("./src/bg.png").getImage();
        groundImg = new ImageIcon("./src/ground.png").getImage();
        birdImg1 = new ImageIcon("./src/bird1.png").getImage();
        birdImg2 = new ImageIcon("./src/bird2.png").getImage();
        birdImg3 = new ImageIcon("./src/bird3.png").getImage();
        pipeImg = new ImageIcon("./src/pipe.png").getImage();
        restartImg = new ImageIcon("./src/restart.png").getImage();

        //bird
        bird = new Bird(birdImg1);
        pipes = new ArrayList<Pipe>();

        //place pipes Timer
        placePipesTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });
        placePipesTimer.start();

        //game loop
        gameLoop = new Timer(1000/60, this);
        gameLoop.start();

        // Timer to update the screen every 50 milliseconds
        Timer timer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update();
            }
        });
        timer.start();

        }

        // Variable to keep track of the current image
        int currentImage = 1;

        // Method to advance to the next image
        public void nextBirdImage() {
            currentImage = (currentImage % 3) + 1;
        }

        public void placePipes(){
            Pipe toPipe = new Pipe(pipeImg);
            pipes.add(toPipe);
            

            }


        // Method to paint the components
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImg, 0, 0, boardwidth, boardheight, null);
            g.drawImage(groundImg, 0, boardheight - 150, null);
            g.drawImage(bird.getCurrentBirdImage(), bird.x, bird.y, null); // Use the current bird image
            for (int i = 0; i < pipes.size(); i++) {
                Pipe pipe = pipes.get(i);
                g.drawImage(pipe.img, pipe.pipeX, pipe.pipeY, pipe.pipeWidth, pipe.pipeHeight, null);
            }
        
        }


        // Method to update the screen
        public void update() {
            nextBirdImage();
            repaint();
        }

        // Method to handle the action 
        public void move() {
            velocityY += gravity;
            bird.y += velocityY;
            bird.y = Math.max(bird.y, 0);

            // Move the pipes
            for (int i = 0; i < pipes.size(); i++) {
                Pipe pipe = pipes.get(i);
                pipe.pipeX += velocityX;
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            move();
            repaint();
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                velocityY = -10;
            }
        }
        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyReleased(KeyEvent e) {}
    }
    
