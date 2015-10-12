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

import com.emxsys.wildfirefx.model.FireBehavior;
import com.emxsys.wildfirefx.model.FuelBed;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.Interpolator;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
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

    private DoubleProperty flameLengthProperty = new SimpleDoubleProperty(10);
    private DoubleProperty rateOfSpreadProperty = new SimpleDoubleProperty(10);

    private IntegerProperty numParticlesProperty = new SimpleIntegerProperty(40);
    private IntegerProperty particleSizeProperty = new SimpleIntegerProperty(25);
    private DoubleProperty expireTimeProperty = new SimpleDoubleProperty(0.7);
    private DoubleProperty xVelocityProperty = new SimpleDoubleProperty(2);
    private DoubleProperty yVelocityProperty = new SimpleDoubleProperty(4);
    private DoubleProperty xVarianceProperty = new SimpleDoubleProperty(40);
    private DoubleProperty yVarianceProperty = new SimpleDoubleProperty(40);
    private DoubleProperty windSpeedProperty = new SimpleDoubleProperty(0);
    private ObjectProperty innerColorProperty = new SimpleObjectProperty(Color.YELLOW);
    private ObjectProperty outerColorProperty = new SimpleObjectProperty(Color.RED);

    private ObjectProperty<FireBehavior> fireBehaviorProperty = new SimpleObjectProperty();

    /**
     * The spline defines the shape of the flame by defining the x velocity as a
     * function of the y velocity.
     */
    private Interpolator xSpline = Interpolator.SPLINE(0.8000, 0.2004, 0.8000, 0.2000);
    private Interpolator ySpline = Interpolator.SPLINE(0.8000, 0.2000, 0.2000, 0.1000);


    /**
     * Constructs an emitter.
     */
    public FireEmitter() {
        //flameLengthProperty.addListener(observable -> {
        fireBehaviorProperty.addListener((observable, oldValue, newValue) -> {

            FireBehavior fire = (FireBehavior) newValue;
            FuelBed fuel = fire.getFuelBed();

            double fl = fire.getFlameLength();
            double heat = fuel.getHeatRelease();
            double depth = fuel.getFuelModel().getFuelBedDepth();

            // Good looking max fire (chapparel) with 5:1 ratio
            yVelocityProperty.setValue(fl * 0.8);
            xVelocityProperty.setValue(fl * 0.13);

            xVarianceProperty.setValue(heat / 15);
            yVarianceProperty.setValue(depth * 50);

            particleSizeProperty.setValue(Math.min(fl * 5, 100));
            //particleSizeProperty.setValue(Math.log(heat) * 8.0);

            numParticlesProperty.setValue(Math.min(heat / 3, 150));
            //numParticlesProperty.setValue(fl * 6);
            
            windSpeedProperty.setValue(fire.getEffectiveWindSpeed());
        });
    }

    public ObjectProperty<FireBehavior> fireBehaviorProperty() {
        return fireBehaviorProperty;
    }

    public DoubleProperty flameLengthProperty() {
        return flameLengthProperty;
    }

    public DoubleProperty rateOfSpreadProperty() {
        return rateOfSpreadProperty;
    }

    public IntegerProperty numParticlesProperty() {
        return numParticlesProperty;
    }

    public IntegerProperty particleSizeProperty() {
        return particleSizeProperty;
    }

    public DoubleProperty expireTimeProperty() {
        return expireTimeProperty;
    }

    public DoubleProperty xVelocityProperty() {
        return xVelocityProperty;
    }

    public DoubleProperty yVelocityProperty() {
        return yVelocityProperty;
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    @Override
    public List<Particle> emit(double x, double y) {

        List<Particle> particles = new ArrayList<>();

        Color innerColor = (Color) innerColorProperty.get();
        Color outerColor = (Color) outerColorProperty.get();
        double radius = particleSizeProperty.get();
        double expireBase = expireTimeProperty.get();
        double xVelocity = this.xVelocityProperty.get();
        double yVelocity = this.yVelocityProperty.get();
        double xVariance = this.xVarianceProperty.get();
        double yVariance = this.yVarianceProperty.get();
        double windMph = this.windSpeedProperty.get();

        long max = this.numParticlesProperty.get() * Math.round(Math.random());
        for (long i = 0; i < max; i++) {

            // Compute origin
            double y0 = Math.random();
            double x0 = (Math.random() - 0.5) * 2;
            double y1 = y + y0 * yVariance;
            double x1 = x + x0 * xVariance;

            // Compute velocity...
            double y2 = Math.random();  // Creates a pulsing effect
            double x2 = (Math.random() - 0.5) * 2;
            // ... attenuate vy as x0 moves away from x to create flame shape.
            double vy = ySpline.interpolate(y2 * yVelocity, 0, Math.abs(x0));
            // ... constrain vx as y2 moves upward to create flame tip.
            double vx = xSpline.interpolate(x2 * xVelocity, 0, y2);
         
            Point2D velocity = new Point2D(vx, -vy);
            // Vary the expire time for a pulsing effect
            double expireTime = Interpolator.LINEAR.interpolate(0.0, expireBase, y2); 

            Particle p = new FireParticle(
                    windMph,
                    x1, y1,
                    velocity,
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
