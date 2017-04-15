package classes;

public class Field {


    public static Sprite[][] field = new Sprite[Configurator.fieldX][Configurator.fieldY];

    private static void prepareField() {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                field[i][j] = null;
            }
        }
    }

    public static Sprite[][] eraseField() {
        prepareField();
        return field;
    }

    public static boolean clarifyPosition(int x, int y, int size, Sprite sprite) { // в аргумент ложиться перспективная координата, для которой нужно найти свободное место.
        if (x > Configurator.fieldX - size - 1 || y > Configurator.fieldY - size - 1
                || y - size < 0 || x - size < 0) {
            return false;
        }
        try {
            for (int i = x - size; i <= x + size; i++) {
                for (int j = y - size; j <= y + size; j++) {
                    if (field[i][j] != null) {
                        sendInterceptDetected(sprite, field[i][j]);
                        //System.out.println("INTERCEPT DETECTED!! ");
                        return false;
                    }
                }
            }
            takePosition(x, y, size, sprite);
        } catch (Exception e) {
            System.out.println("EXCEPTION x=" + x + " y=" + y);
            return false;
        }

        return true;
    }

    public static void takePosition(int x, int y, int size, Sprite sprite) {
        if (x > Configurator.fieldX - size - 1 || y > Configurator.fieldY - size - 1
                || y - size < 0 || x - size < 0) {
            return;
        }
        for (int i = x - size; i <= x + size; i++) {
            for (int j = y - size; j <= y + size; j++) {
                field[i][j] = sprite;
                //System.out.println("Position x=" + i + " y=" + j);
            }
        }
    }

    private static void sendInterceptDetected(Sprite sf, Sprite ss) {
        sf.handleInterception(ss); // пусть один обработает другого
        //ss.handleInterception(sf); // а другой первого
    }

    public static int getTotalOccupiedCells() {
        int result = 0;
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j] != null) result++;
            }
        }
        return result;
    }

}
