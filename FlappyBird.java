import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBird extends JPanel implements  ActionListener ,KeyListener{
    int boardwidth=360;
    int boardheight=640;

    Image backgroundImg;
    Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg;

    int birdX= boardwidth/8;
    int birdY= boardheight/2;
    int birdwidth=34;
    int birdheight=24;



    class Bird{
        int x = birdX;
        int y= birdY;
        int width= birdwidth;
        int height=birdheight;
        Image img;
        Bird (Image img){
            this.img = img;
        }

    }

    //pipes
    int pipeX = boardwidth;
    int pipeY = 0;
    int pipewidth = 64;
    int pipeheight = 512;

    class Pipe{
        int x = pipeX;
        int y = pipeY;
        int width = pipewidth;
        int height = pipeheight;
        Image img;
        boolean passed = false;
        Pipe (Image img){
            this.img = img;
        }
    }
    //game logic
    Bird bird;
    int velocityX= -4;      //moves the pipes left
    int velocityY=0;//moves the bird
    int gravity = 1  ;


    ArrayList<Pipe> pipes;
    Random random = new Random();

    Timer gameLoop;
    Timer placepipestimer;

    boolean gameover= false;

    double score = 0;

    FlappyBird(){
        setPreferredSize(new Dimension(boardwidth,boardheight));
        //setBackground(Color.blue);
        setFocusable(true);     //this jpanel takes the event
        addKeyListener(this);       //will triger the keyevent

        //load images
        backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

        bird =new Bird(birdImg);
        pipes = new ArrayList<Pipe>();
        placepipestimer= new Timer(1500,new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                placePipes();
            }
        });

        placepipestimer.start();
        //timer
        gameLoop = new Timer(1000/60,this); //1000/60=16.6 thsi refer to the flappy bird class
        gameLoop.start();
    }

    public void placePipes(){
        //(0-1) * pipeheight/2 -> (0-256)
        //0-128 - (0-256)  -> pipeheight/4 -- 3/4 pipeheight
        int randompipey =(int) (pipeY-pipeheight/4 - Math.random()*(pipeheight/2));
         int openingSpace = boardheight/6;

        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y=randompipey ;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y= topPipe.y + pipeheight + openingSpace;
        pipes.add(bottomPipe);
    }

    public  void paintComponent(Graphics g){
        super.paintComponent(g);//call parent class methods access the super class constructor
        draw(g);
    }
    public void draw(Graphics g){
       //loops 60 times a second
       // System.out.println("draw");
        //draw the backgroudn iimage inthe laybout

        g.drawImage(backgroundImg,0,0,boardwidth,boardheight,null);
        //draw the bird
        g.drawImage(bird.img,bird.x,bird.y,bird.width,bird.height,null);
        //pipes
        for(int i=0; i<pipes.size();i++){
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img,pipe.x,pipe.y,pipe.width,pipe.height,null);
        }

        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN,32));
        if(gameover){
            g.drawString("Game Over: "+ String.valueOf((int) score),80,boardheight/3);
            g.drawString("Highest Score: "+ String.valueOf((int) score),60,270);
            g.drawString("Play Again? ",95,325);
        }
        else{
            g.drawString(String.valueOf((int) score),10,35);
        }
    }
    public void move(){
        velocityY += gravity;
        bird.y+=velocityY;
        bird.y=Math.max(bird.y,0);

        //pipes
        for(int i=0; i<pipes.size();i++){
            Pipe pipe = pipes.get(i);
            pipe.x +=velocityX;
            if(!pipe.passed && bird.x > pipe.x + pipe.width){
                 pipe.passed = true;
                 score +=0.5;
            }
            if(collision(bird,pipe)){
                gameover = true;
            }
        }
        if (bird.y> boardheight){
            gameover=true;
        }

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();// this wil call paintcomponent
        if(gameover){
            placepipestimer.stop();
            gameLoop.stop();
        }
    }

    public boolean collision(Bird a,Pipe b){
        return a.x < b.x + b.width &&       //
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;

    }

    @Override
    public void keyPressed(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
        velocityY = -9;
        if(gameover){
            bird.y= birdY;
            velocityY=0;
            pipes.clear();
            score=0;
            gameover=false;
            gameLoop.start();
            placepipestimer.start();
        }
    }
    }
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}



}
