package classes;


import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class Game extends Canvas implements Runnable {

    private static final long serialVersionUID = 1L;
    public static boolean running;

    public static String NAME = "TANKS V2.0"; //заголовок окна
    public TankLevel1 myTankLevel1;
    public ArrayList<Obstacle> obstacleList = new ArrayList<>();
    public ArrayList<Armor> armorList = new ArrayList<>();
    public ArrayList<Weapon> weaponList = new ArrayList<>();
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean firePressed = false;
    private String globalMessage = "";
    private String anyString = "";
    int killed = 0;
    int lasted = 0;
    ArrayList<Sprite> allSpites = new ArrayList<>();


    @Override
    public void run() {
        long lastTime = System.currentTimeMillis();
        long delta;
        init();
        while (running) {
            delta = System.currentTimeMillis() - lastTime;
            lastTime = System.currentTimeMillis();
            update(delta);
            render();
        }
    }

    public void start() {
        running = true;
        new Thread(this).start();
    }

    public void update(long delta) {

        Field.eraseField();
        weaponList = new ArrayList<>();
        // очистка поля от старых спрайтов
        ArrayList<Obstacle> tempO = new ArrayList<Obstacle>();
        for (Obstacle o : obstacleList) { // расставить по местам все препятствия
            if (o.dead) continue;
            tempO.add(o);
            o.stand();
        }
        obstacleList = tempO;

        if (upPressed) {
            myTankLevel1.calculateMove(2);
        }
        if (rightPressed) {
            myTankLevel1.calculateMove(1);
        }
        if (downPressed) {
            myTankLevel1.calculateMove(0);
        }
        if (leftPressed) {
            myTankLevel1.calculateMove(3);
        }
        if (firePressed) {
            myTankLevel1.fire();
        }

        if (myTankLevel1.live <= 0) {
            globalMessage = "G A M E  O V E R";
            running = false;
        }
        if (myTankLevel1.weapon != null) {
            anyString = "X=" + myTankLevel1.weapon.X + " Y=" + myTankLevel1.weapon.Y + " d=" + myTankLevel1.weapon.dead + " killed=" + killed + " lasted=" + lasted;
        } else {
            anyString = "weapon = null killed=" + killed + " lasted=" + lasted;
        }
        myTankLevel1.holdMyPosition();
        if (myTankLevel1.weapon != null && myTankLevel1.weapon.dead)
            myTankLevel1.weapon = null; // если пуля уперлась или попала в стену удалить оружие у танка
        if (myTankLevel1.weapon != null)
            weaponList.add(myTankLevel1.weapon); // если оружие у танка есть и летит, то нужно его посчитать

        ArrayList<Armor> tempA = new ArrayList<>();
        for (Armor a : armorList) {
            if (a.dead) {
                killed++;
                continue;
            }
            if (a.weapon != null && a.weapon.dead)
                a.weapon = null; // если пуля уперлась или попала в стену удалить оружие у танка
            if (a.weapon != null) {
                weaponList.add(a.weapon);
            }
            a.calculateMove(-1);
            tempA.add(a);
        }
        lasted = tempA.size();
        if (tempA.size() == 0) {
            globalMessage = "YOU ARE WINNER!";
            running = false;
        }
        armorList = tempA;


        for (Weapon w : weaponList) { // расставить по местам все препятствия
            w.calculateFly();
        }

    }

    public void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(2);
            requestFocus();
            return;
        }
        Graphics g = bs.getDrawGraphics();
        paintField(g);
        paintGrid(g);

        for (Obstacle o : obstacleList) { // расставить по местам все препятствия
            o.drawSquares(g);
        }
        for (Armor a : armorList) {
            a.drawSquares(g);
        }
        myTankLevel1.drawSquares(g);
        for (Weapon w : weaponList) {
            w.drawOneSquare(g);
        }

        outLOG(g);
        g.dispose();
        bs.show();

    }

    public void init() {
        myTankLevel1 = new TankLevel1(40, 40);
        myTankLevel1.manualDriven = true;
        myTankLevel1.host = 0;
        for (int x = 80; x < 700; x += 40) {
            obstacleList.add(new Bricks(x, 80));
        }
        for (int x = 80; x < 860; x += 40) {
            obstacleList.add(new Bricks(x, 400));
        }
        for (int x = 80; x < 1100; x += 40) {
            obstacleList.add(new Bricks(x, 640));
        }
        for (int y = 80; y < 700; y += 40) {
            obstacleList.add(new Bricks(40, y));
        }
        for (int y = 40; y < 740; y += 40) {
            obstacleList.add(new Bricks(1200, y));
        }
        for (int x = 100; x < 900; x += 40) {
            armorList.add(new TankLevel1(x, 120));
        }
        for (int x = 100; x < 900; x += 40) {
            armorList.add(new TankLevel1(x, 180));
        }


        addKeyListener(new KeyInputHandler());
    }

    private void paintField(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    private void paintGrid(Graphics g) {
        g.setColor(Color.GRAY);
        for (int i = 4; i <= Configurator.HEIGHT; i += 4) {
            g.drawLine(0, i, Configurator.WIDTH, i);
        }
        for (int i = 4; i <= Configurator.WIDTH; i += 4) {
            g.drawLine(i, 0, i, Configurator.HEIGHT);
        }
    }

    private void outLOG(Graphics g) {
        int fontSize = 15;
        String log = "Total Occupied cells = " + Field.getTotalOccupiedCells();
        g.setFont(new Font("TimesRoman", Font.BOLD, fontSize));
        g.setColor(Color.RED);
        g.drawString("TANK V2.0 LOG:", 10, 20);
        g.drawString(log, 10, 40);
        g.drawString(anyString, 10, 60);
        g.setFont(new Font("TimesRoman", Font.BOLD, 70));
        g.drawString(globalMessage, 380, 330);
    }


    public class KeyInputHandler extends KeyAdapter {

        public void keyPressed(KeyEvent e) { //клавиша нажата
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                leftPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                rightPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                upPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                downPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                firePressed = true;
            }

        }

        public void keyReleased(KeyEvent e) { //клавиша отпущена
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                leftPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                rightPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                upPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                downPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                firePressed = false;
            }
        }
    }

}
