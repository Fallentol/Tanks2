package classes;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class Sprite {

    Image image; //изображение
    HashMap<Integer, Image> mapOfImages;
    int position = 0; //0-up  1-right 2-bottom 3-left
    int imageWidth = 0;
    int imageHeight = 0;
    int X = 0;
    int Y = 0;
    int delay = 0;

    boolean dead = false; // признак того что спрайт убит
    boolean intercept = false; // признако того что спрайт в пересечении.
    public int host = 0; // принадлежность 0- свой 1-чужой
    public int live = 0; // количесвто жизней, у теники +100 у пули -100
    public int spriteSize = 0;
    enum Type { OBSTACLE, ARMOR, WEAPON }
    public Type type;

    public int getPosition() {
        return position;
    }

    public float getX() {
        return X;
    }

    public float getY() {
        return Y;
    }

    public Sprite(int x, int y) {
        X = x;
        Y = y;
    }

    public Sprite() {
    }

    public Sprite(String path) {
        BufferedImage sourceImage = null;
        try {
            URL url = this.getClass().getClassLoader().getResource(path);
            sourceImage = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.image = Toolkit.getDefaultToolkit().createImage(sourceImage.getSource());
    }

    public void handleInterception(Sprite sprite) { // в аргументе вращеский спрайт, препятствие или пуля
        System.out.println("Sprite method");
    }

    public Image getImageFromString(String path) {
        BufferedImage sourceImage = null;
        try {
            URL url = this.getClass().getClassLoader().getResource(path);
            sourceImage = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Toolkit.getDefaultToolkit().createImage(sourceImage.getSource());
    }

    public int getWidth() { //получаем ширину картинки
        return image.getWidth(null);
    }

    public int getHeight() { //получаем высоту картинки
        return image.getHeight(null);
    }

    public void draw(Graphics g, int x, int y) {
        g.drawImage(image, x, y, imageWidth, imageHeight, null);
    }

    public void draw(Graphics g) {
        Image currentImage = mapOfImages.get(position);
        g.drawImage(currentImage, X, Y, imageWidth, imageHeight, null);
    }

    public void drawSimple(Graphics g) {
        g.drawImage(image, X, Y, imageWidth, imageHeight, null);
    }

    public void drawSquares(Graphics g) {

        g.setColor(Color.YELLOW);
        if (host == 0) g.setColor(Color.BLUE);
        if (host == -1) g.setColor(Color.ORANGE);
        g.fillRect(X-spriteSize*4, Y-spriteSize*4, Configurator.pixels*2*spriteSize + Configurator.pixels, Configurator.pixels*2*spriteSize + Configurator.pixels);
        g.setColor(Color.RED);
        g.fillRect(X, Y, Configurator.pixels, Configurator.pixels);
    }

    public void drawOneSquare(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(X, Y, Configurator.pixels, Configurator.pixels);
    }
}
