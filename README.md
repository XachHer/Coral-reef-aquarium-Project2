# Coral Reef Aquarium

CSCI 1720G Project 2. Draws a coral reef scene inside an aquarium using the `edu.cymbal.graphics` Painter library.

## What's in the scene
- Two kinds of sea creatures: oval fish facing right (big orange, small blue) and starfish (big yellow, small pink)
- Decorations: red and purple coral, gray rocks, bubbles, sand, and a light blue water background

## Methods
- Shapes: `drawRectangle`, `drawCircle`, `drawRing`, `drawTriangle`, `drawOval`, `drawStar`
- Sea creatures: `drawFish`, `drawStarfish`
- Decorations: `drawCoral`, `drawRock`, `drawBubble`

Loops are used to draw the row of coral and the column of bubbles.

## How to run
```
javac -cp lib/painter-canvas-17.jar Aquarium.java
java -cp lib/painter-canvas-17.jar;. Aquarium
```
