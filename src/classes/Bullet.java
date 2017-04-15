package classes;


public class Bullet extends Weapon {

    public Bullet(int x, int y, int direction, int host) {
        super(x, y, direction, host);
        live = -110;
    }

}
