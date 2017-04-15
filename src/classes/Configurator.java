package classes;

public class Configurator {
    public static int WIDTH = 1400; //ширина
    public static int HEIGHT = 800; //высота
    public static int tankCounter = 0;
    public static int weaponCounter = 0;
    public static final int fieldX = 350;
    public static final int fieldY = 200;
    public static final int pixels = 4; // количество пикселей в одной клекте

    public static String getUniqueTankIdent() {
        String s = "Tank " + tankCounter;
        tankCounter++;
        return s;
    }

    public static String getUniqueWeaponIdent() {
        String s = "Weapon " + weaponCounter;
        weaponCounter++;
        return s;
    }
}
