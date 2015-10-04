/*
 * Copyright (c) 2015, Bruce Schubert <bruce@emxsys.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     - Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *
 *     - Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *
 *     - Neither the name of Bruce Schubert, Emxsys nor the names of its 
 *       contributors may be used to endorse or promote products derived
 *       from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.emxsys.wildfirefx.particles;

import com.emxsys.wildfirefx.particles.Emitter;
import com.emxsys.wildfirefx.particles.Particle;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.Interpolator;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;

/**
 * An emitter of fire particles in our particle system.
 *
 * Based on "JavaFX Game Development: Particle System" by Almas Baimagambetov.
 *
 * @see <a href="https://youtu.be/vLcJRm6Y72U">JavaFX Game Development: Particle
 * System</a>
 *
 * @author Bruce Schubert
 */
public class FireEmitter implements Emitter {

    private SimpleIntegerProperty numParticlesProperty = new SimpleIntegerProperty(10);
    private SimpleIntegerProperty particleSizeProperty = new SimpleIntegerProperty(10);
    private SimpleDoubleProperty expireTimeProperty = new SimpleDoubleProperty(2);
    private SimpleDoubleProperty xVelocityProperty = new SimpleDoubleProperty(2);
    private SimpleDoubleProperty yVelocityProperty = new SimpleDoubleProperty(4);
    private SimpleObjectProperty innerColorProperty = new SimpleObjectProperty(Color.YELLOW);
    private SimpleObjectProperty outerColorProperty = new SimpleObjectProperty(Color.RED);

    private Interpolator spline = Interpolator.SPLINE(0.0000, 0.3000, 1.0000, 0.5000);

    public SimpleIntegerProperty numParticlesProperty() {
        return numParticlesProperty;
    }

    public SimpleIntegerProperty particleSizeProperty() {
        return particleSizeProperty;
    }

    public SimpleDoubleProperty expireTimeProperty() {
        return expireTimeProperty;
    }

    public SimpleDoubleProperty xVelocityProperty() {
        return xVelocityProperty;
    }

    public SimpleDoubleProperty yVelocityProperty() {
        return yVelocityProperty;
    }

    @Override
    public List<Particle> emit(double x, double y) {
        List<Particle> particles = new ArrayList<>();
        Color innerColor = (Color) innerColorProperty.get();
        Color outerColor = (Color) outerColorProperty.get();
        double radius = particleSizeProperty.get();
        double expireTime = expireTimeProperty.get();
        double xVelocity = this.xVelocityProperty.get();
        double yVelocity = this.yVelocityProperty.get() * -1;

        long max = this.numParticlesProperty.get() * Math.round(Math.random());
        for (long i = 0; i < max; i++) {
            // Compute origin
            double x1 = x + (Math.random() - 0.5) * 40; // vary x left/right
            double y1 = y + Math.random() * 20;         // vary y upwards
            // Compute velocity
            double x2 = Math.random() - 0.5;
            double y2 = Math.random();
            double vx = x2 * spline.interpolate(1.0, 0.0, y2) * xVelocity;
            double vy = y2 * yVelocity;

            Particle p = new Particle(
                    x1, y1,
                    new Point2D(vx, vy),
                    radius,
                    expireTime,
                    innerColor,
                    outerColor,
                    BlendMode.SRC_OVER);

            particles.add(p);
        }
        return particles;
    }

}
