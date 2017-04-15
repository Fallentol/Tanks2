package classes;

public class Obstacle extends Sprite {

    public Obstacle(int x, int y) {
        X = x;
        Y = y;
        type = Type.OBSTACLE;
        live = 300;
        host = -1;
    }

    public void stand() {
        int wishX = X / 4;
        int wishY = Y / 4;
        Field.takePosition(wishX, wishY, spriteSize, this);
    }

    public void handleInterception(Sprite sprite) { // в аргументе вращеский спрайт, препятствие или пуля
        if (sprite.type == Type.WEAPON) {
            int solution = live + sprite.live;
            live = solution;
            sprite.live = solution;
            if (sprite.live>=0) sprite.dead = true;
            if (live<=0) dead = true;
        }
    }

}
