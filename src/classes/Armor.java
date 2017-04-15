package classes;

import java.util.concurrent.ThreadLocalRandom;

public class Armor extends Sprite {

    int delayCounter = 0;
    boolean manualDriven = false;
    public Weapon weapon;

    public Armor(int x, int y) {
        super(x, y);
        delay = 20;
        type = Type.ARMOR;
    }

    public Weapon fire() {
        if (weapon != null) return weapon; // если пуля летит, то новую не выпустить
        if (position == 0) {
            weapon = new Bullet(X, Y + spriteSize * Configurator.pixels + Configurator.pixels, position, host);
        }
        if (position == 1) {
            weapon = new Bullet(X + spriteSize * Configurator.pixels + Configurator.pixels, Y, position, host);
        }
        if (position == 2) {
            weapon = new Bullet(X, Y - spriteSize * Configurator.pixels - Configurator.pixels, position, host);
        }
        if (position == 3) {
            weapon = new Bullet(X - spriteSize * Configurator.pixels - Configurator.pixels, Y, position, host);
        }
        return weapon;
    }

    public void move(int i) {
        position = i;
        int wishX = X / 4; /// X Y - физические координаты в пикселях
        int wishY = Y / 4; /// wishX - координаты клетки. Если физически X=40 то wishX = 10
        if (i == 0) {
            wishY += 1;
        }
        if (i == 1) {
            wishX += 1;
        }
        if (i == 2) {
            wishY -= 1;
        }
        if (i == 3) {
            wishX -= 1;
        }
        if (Field.clarifyPosition(wishX, wishY, spriteSize, this)) {
            //System.out.println("Движение разрешено");
            X = wishX * 4;
            Y = wishY * 4;
        } else {
            //System.out.println("Движение запрещено");
            Field.takePosition(wishX, wishY, spriteSize, this); // если походить не удалось - просто займи старую позицию на поле
        }
    }

    public void holdMyPosition() { // если такн с ручным управлением не ходит - пусть просто займет позицию
        //System.out.println("X=" + X + " Y=" + Y + " wX=" + X / 4 + " wY=" + Y / 4);
        Field.takePosition((int) X / 4, (int) Y / 4, spriteSize, this);
    }

    private int similarSteps = 0;
    private int stepOfChanging = 100; // через сколько шагов менять направление двжения
    private int dir = 0;

    public void calculateMove(int direction) {



        if (delayCounter < delay) {
            delayCounter++;
            return;
        } else {
            delayCounter = 0;
        }

        if (manualDriven) { // если управление ручное, то упралять машиной не нужно
            move(direction);
            return;
        }
        autoFire();

        float oldX = X;
        float oldY = Y;
        if (similarSteps > stepOfChanging) {
            dir = (int) (Math.random() * 4);
            stepOfChanging = ThreadLocalRandom.current().nextInt(15, 50);
            similarSteps = 0;
            //System.out.println("Изменил направление теперь шагов " + stepOfChanging);
        } else {
            //System.out.println("Шаг "+similarSteps);
        }
        move(dir);
        if (X == oldX && Y == oldY) {
            stepOfChanging = -1;
            //System.out.println("Застрял");
        } // признак того что такн застраял
        similarSteps++;
    }

    private void autoFire() {
        if (((int) (Math.random() * 10)) == 0) {
            fire();
        }
    }

    public void handleInterception(Sprite sprite) { // в аргументе вращеский спрайт, препятствие или пуля
        if (sprite.host == this.host) return;
        if (sprite.type == Type.WEAPON) {
            int solution = live + sprite.live;
            live = solution;
            sprite.live = solution;
            if (sprite.live >= 0) sprite.dead = true;
            if (live <= 0) dead = true;
        }
    }

}
