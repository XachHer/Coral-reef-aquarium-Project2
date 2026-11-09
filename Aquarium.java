import edu.cymbal.graphics.CanvasAPI;
import edu.cymbal.graphics.Graphics;
import edu.cymbal.graphics.PainterAPI;
import java.awt.Color;

public class Aquarium {

    static PainterAPI painter;

    public static void main(String[] args) {
        CanvasAPI canvas = Graphics.createCanvas(800, 600, "Coral Reef Aquarium");
        painter = canvas.getPainter();
        painter.setSpeed(0);

        // water + sand
        canvas.setBackgroundColor(new Color(150, 210, 240));
        drawRectangle(-400, -300, 800, 100, new Color(225, 200, 150));

        // rocks
        drawRock(-285, -200, 55, 28);
        drawRock(-225, -205, 35, 18);
        drawRock(260, -200, 60, 25);

        // random coral heights, but each one stays close to the one before it
        int height = 110 + (int) (Math.random() * 41);   // first coral: 110 to 150

        // loop 1: a row of coral that switches between red and purple
        for (int i = 0; i < 4; i++) {
            Color coralColor;
            if (i % 2 == 0) {
                coralColor = new Color(210, 50, 60);
            } else {
                coralColor = new Color(140, 60, 170);
            }
            drawCoral(-140 + i * 75, -210, height, coralColor);

            // next coral is up to 30 taller or shorter, but stays between 90 and 170
            height = height - 30 + (int) (Math.random() * 61);
            if (height < 90) {
                height = 90;
            }
            if (height > 170) {
                height = 170;
            }
        }

        // EXTRA CREDIT: starfish get a random spot on the sand (big one on the left half,
        // small one on the right half, so they never overlap or go off the canvas)
        int bigStarX = -360 + (int) (Math.random() * 281);    // -360 to -80
        int smallStarX = 80 + (int) (Math.random() * 291);    // 80 to 370
        drawStarfish(bigStarX, -255, 40, new Color(240, 200, 40));
        drawStarfish(smallStarX, -260, 22, new Color(240, 130, 170));

        // EXTRA CREDIT: fish get random spots in the open water above the coral.
        // The x and y ranges keep the whole fish (tail included) on the canvas.
        int bigX = -330 + (int) (Math.random() * 551);        // -330 to 220
        int bigY = 30 + (int) (Math.random() * 211);          // 30 to 240
        drawFish(bigX, bigY, 170, 95, new Color(245, 140, 30));

        // keep picking a spot for the small fish until it doesn't overlap the big fish
        int smallX = 0;
        int smallY = 0;
        boolean overlaps = true;
        while (overlaps) {
            smallX = -350 + (int) (Math.random() * 651);      // -350 to 300
            smallY = 0 + (int) (Math.random() * 266);         // 0 to 265

            // compare the boxes around each fish (tail to nose, top to bottom) with a 20 pixel gap
            boolean apartSideways = smallX + 85 + 20 < bigX - 60 || bigX + 170 + 20 < smallX - 30;
            boolean apartUpDown = smallY + 24 + 20 < bigY - 48 || bigY + 48 + 20 < smallY - 24;
            overlaps = !(apartSideways || apartUpDown);
        }
        drawFish(smallX, smallY, 85, 48, new Color(60, 110, 220));

        // EXTRA CREDIT: a random number of bubbles (4 to 7) in a random spot
        int bubbleCount = 4 + (int) (Math.random() * 4);
        int bubbleX = 250 + (int) (Math.random() * 111);      // 250 to 360

        // loop 2: bubbles that get bigger as they float up
        for (int i = 0; i < bubbleCount; i++) {
            int x = bubbleX - (i % 2) * 12;
            int y = -140 + i * 55;
            drawBubble(x, y, 6 + i * 2);
        }

        canvas.awaitClose();
    }

    //shape methods

    // Filled rectangle. (x, y) is the bottom-left corner. 
    public static void drawRectangle(double x, double y, double width, double height, Color color) {
        painter.liftPen();
        painter.moveTo(x, y);
        painter.lowerPen();
        painter.setPenColor(color);
        painter.startFill(color);
        painter.moveTo(x + width, y);
        painter.moveTo(x + width, y + height);
        painter.moveTo(x, y + height);
        painter.moveTo(x, y);
        painter.finishFill();
    }

    // Filled circle. (x, y) is the center. 
    public static void drawCircle(double x, double y, double radius, Color color) {
        painter.liftPen();
        painter.moveTo(x, y - radius);
        painter.lowerPen();
        painter.setPenColor(color);
        painter.startFill(color);
        painter.circle(radius);
        painter.finishFill();
    }

    // Circle outline only (no fill). (x, y) is the center. 
    public static void drawRing(double x, double y, double radius, Color color, float thickness) {
        painter.liftPen();
        painter.moveTo(x, y - radius);
        painter.lowerPen();
        painter.setPenColor(color);
        painter.setPenWidth(thickness);
        painter.circle(radius);
        painter.setPenWidth(1);
    }

    // Filled triangle from three corner points. 
    public static void drawTriangle(double x1, double y1, double x2, double y2,
                                    double x3, double y3, Color color) {
        painter.liftPen();
        painter.moveTo(x1, y1);
        painter.lowerPen();
        painter.setPenColor(color);
        painter.startFill(color);
        painter.moveTo(x2, y2);
        painter.moveTo(x3, y3);
        painter.moveTo(x1, y1);
        painter.finishFill();
    }

    // Filled oval. (x, y) is the center, width and height are the full size.
    public static void drawOval(double x, double y, double width, double height, Color color) {
        painter.liftPen();
        painter.moveTo(x + width / 2, y);
        painter.lowerPen();
        painter.setPenColor(color);
        painter.startFill(color);
        // walk around the oval in small steps
        for (int angle = 0; angle <= 360; angle += 10) {
            double radians = Math.toRadians(angle);
            painter.moveTo(x + width / 2 * Math.cos(radians), y + height / 2 * Math.sin(radians));
        }
        painter.finishFill();
    }

    // Filled 5-point star. (x, y) is the center, size is the length of each point. 
    public static void drawStar(double x, double y, double size, Color color) {
        painter.liftPen();
        painter.moveTo(x, y + size);
        painter.lowerPen();
        painter.setPenColor(color);
        painter.startFill(color);
        // 10 corners: switch between the tip of a point and the inside corner
        for (int i = 1; i <= 10; i++) {
            double radius = size;
            if (i % 2 == 1) {
                radius = size * 0.42;
            }
            double radians = Math.toRadians(90 + i * 36);
            painter.moveTo(x + radius * Math.cos(radians), y + radius * Math.sin(radians));
        }
        painter.finishFill();
    }

    //sea creatures

    // Oval fish facing right. (x, y) is the left edge of the body (where the tail attaches).
    public static void drawFish(double x, double y, double length, double height, Color color) {
        // tail
        drawTriangle(x, y, x - length * 0.35, y + height * 0.45,
                     x - length * 0.35, y - height * 0.45, color);
        // body
        drawOval(x + length / 2, y, length, height, color);
        // eye
        double eyeSize = height * 0.12;
        double eyeX = x + length * 0.75;
        double eyeY = y + height * 0.12;
        drawCircle(eyeX, eyeY, eyeSize, Color.WHITE);
        drawCircle(eyeX, eyeY, eyeSize / 2, Color.BLACK);
    }

    // Starfish. (x, y) is the center, size is how long the arms are.
    public static void drawStarfish(double x, double y, double size, Color color) {
        drawStar(x, y, size, color);
    }

    //decorations

    // One piece of branching coral. (x, y) is the bottom of the main stem.
    public static void drawCoral(double x, double y, double height, Color color) {
        // main stem
        drawRectangle(x, y, 12, height, color);
        // left branch
        drawRectangle(x - 18, y + height * 0.3, 10, height * 0.3, color);
        drawRectangle(x - 18, y + height * 0.3, 18, 8, color);
        // right branch
        drawRectangle(x + 20, y + height * 0.45, 10, height * 0.3, color);
        drawRectangle(x + 12, y + height * 0.45, 18, 8, color);
    }

    // Gray rock. (x, y) is the center. 
    public static void drawRock(double x, double y, double width, double height) {
        drawOval(x, y, width * 2, height * 2, new Color(130, 130, 135));
    }

    // Bubble. (x, y) is the center. 
    public static void drawBubble(double x, double y, double radius) {
        drawRing(x, y, radius, Color.WHITE, 2);
    }
}
