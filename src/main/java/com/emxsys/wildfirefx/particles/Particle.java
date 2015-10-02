package com.emxsys.wildfirefx.particles;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Paint;


/**
 * A particle in our particle system.
 *
 * Based on "JavaFX Game Development: Particle System" by Almas Baimagambetov on YouTube.
 *
 * @see <a href="https://youtu.be/vLcJRm6Y72U">JavaFX Game Development: Particle System</a>
 *
 * @author Bruce Schubert
 */
public class Particle {

    private double x;
    private double y;
    private Point2D velocity;
    private double radius;
    private double life = 1.0;
    private double decay;
    private Paint color;
    private BlendMode blendMode;

    /**
     *
     * @param x Position.
     * @param y Position.
     * @param velocity A vector describing the speed and direction of the particle.
     * @param radius The particle size.
     * @param expireTime The the lifetime of the particle in seconds.
     * @param color The particle color.
     * @param blendMode
     */
    public Particle(double x, double y, Point2D velocity, double radius, double expireTime,
                    Paint color,
                    BlendMode blendMode) {
        this.x = x;
        this.y = y;
        this.velocity = velocity;
        this.radius = radius;
        this.color = color;
        this.blendMode = blendMode;

        this.decay = 0.016 / expireTime; // 60 htz = 1/60 = 0.016
    }

    /**
     * @return True if the particle has not expired.
     */
    public boolean isAlive() {
        return life > 0;
    }

    /**
     * Updates the particle's position and life (age). The age of the particle determines the color.
     */
    public void update() {
        x += velocity.getX();
        y += velocity.getY();

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
        g.setFill(color);
        g.fillOval(x, y, radius, radius);
    }
}
