package classes;

public class Weapon extends Sprite {


    public Weapon(int x, int y, int direction, int h) {
        this.X = x;
        this.Y = y;
        this.host = h;
        position = direction;
        dead = false;
        type = Type.WEAPON;
    }

    public void calculateFly() {
        int wishX = X / 4; /// X Y - физические координаты в пикселях
        int wishY = Y / 4; /// wishX - координаты клетки. Если физически X=40 то wishX = 10
        int oldX = X;
        int oldY = Y;
        if (position == 0) {
            wishY += 1;
        }
        if (position == 1) {
            wishX += 1;
        }
        if (position == 2) {
            wishY -= 1;
        }
        if (position == 3) {
            wishX -= 1;
        }
        if (Field.clarifyPosition(wishX, wishY, spriteSize, this)) {
            X = wishX * 4;
            Y = wishY * 4;
            if (X <= 1 || Y <= 1 || X + 4 >= Configurator.fieldX * Configurator.pixels || Y + 4 >= Configurator.fieldY * Configurator.pixels) {
                dead = true;
            }
        }
        if (oldX == X && oldY == Y) {
            System.out.println("ЗАЛИП");
            dead = true;
        }
    }

    public void handleInterception(Sprite sprite) { // в аргументе вращеский спрайт, препятствие или пуля

        if (host == sprite.host) return;
        if (sprite.type == Type.WEAPON) {
            sprite.dead = this.dead = true;
            return;
        }
        int solution = live + sprite.live;
        live = solution;
        sprite.live = solution;
        if (sprite.live <= 0) sprite.dead = true;
        if (live >= 0) dead = true;
    }

}
