package com.emxsys.wildfirefx.particles;

import javafx.animation.Interpolator;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;

/**
 * A particle in our particle system.
 *
 * Based on "JavaFX Game Development: Particle System" by Almas Baimagambetov on
 * YouTube.
 *
 * @see <a href="https://youtu.be/vLcJRm6Y72U">JavaFX Game Development: Particle
 * System</a>
 *
 * @author Bruce Schubert
 */
public class Particle {

    private double x;
    private double y;
    private Point2D velocity;
    private double radius;
    private double life = 1.0;
    private double expireTime;
    private Color startColor;
    private Color endColor;
    private BlendMode blendMode;

    /**
     * Constructor.
     *
     * @param x Position.
     * @param y Position.
     * @param velocity A vector describing the speed and direction of the
     * particle.
     * @param radius The particle size.
     * @param expireTime The the lifetime of the particle in seconds.
     * @param color The particle color.
     * @param blendMode
     */
    public Particle(double x, double y, Point2D velocity, double radius, double expireTime,
            Color startColor, Color endColor, BlendMode blendMode) {

        this.x = x;
        this.y = y;
        this.velocity = velocity;
        this.radius = radius;
        this.expireTime = expireTime;
        this.startColor = startColor;
        this.endColor = endColor;
        this.blendMode = blendMode;
    }

    /**
     * @return True if the particle has not expired.
     */
    public boolean isAlive() {
        return life > 0;
    }

    /**
     * Updates the particle's position and life (age). The age of the particle
     * determines the color.
     */
    public void update(double frameRate) {
        double decay = 1 / (expireTime * frameRate);    // decay in secs per frame
        double scale = 60 / frameRate;                  // 1x == 60 hz
        
        // BDS: testing wind profile\
        //double mag = velocity.magnitude();
        //velocity = velocity.add(0.2 + (velocity.getY()*-0.01) , 0);
        
        x += velocity.getX() * scale;
        y += velocity.getY() * scale;
        life -= decay;
    }

    /**
     * Renders the particle on the given graphics context.
     *
     * @param g
     */
    public void render(GraphicsContext g) {
        g.setGlobalAlpha(life);
        g.setGlobalBlendMode(blendMode);
        if (endColor == null || startColor.equals(endColor)) {
            g.setFill(startColor);
        } else {
            Color color = (Color) Interpolator.EASE_IN.interpolate(endColor, startColor, life);
            g.setFill(color);
        }
        double r = radius;// * Math.sin(Math.toRadians(life * 90));
        g.fillOval(x, y, r, r);
    }
}
