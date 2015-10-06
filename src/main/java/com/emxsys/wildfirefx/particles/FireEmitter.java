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
import com.emxsys.wildfirefx.model.FuelModel;
import com.emxsys.wildfirefx.particles.Emitter;
import com.emxsys.wildfirefx.particles.Particle;
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
    private ObjectProperty innerColorProperty = new SimpleObjectProperty(Color.YELLOW);
    private ObjectProperty outerColorProperty = new SimpleObjectProperty(Color.RED);

    private ObjectProperty<FireBehavior> fireBehaviorProperty = new SimpleObjectProperty();

    /**
     * The spline defines the shape of the flame by defining the x velocity as a
     * function of the y velocity.
     */
    private Interpolator xSpline = Interpolator.SPLINE(0.0000, 0.8004, 0.8000, 1.0000);
    private Interpolator ySpline = Interpolator.SPLINE(0.2000, 0.8000, 0.8000, 0.2000);

    private Interpolator windwardSpline = Interpolator.SPLINE(0.2000, 0.8000, 0.8000, 0.2000);
    private Interpolator leewardSpline = Interpolator.SPLINE(0.8000, 0.2000, 0.2000, 0.8000);

    /**
     * Constructs an emitter.
     */
    public FireEmitter() {
        //flameLengthProperty.addListener(observable -> {
        fireBehaviorProperty.addListener((observable, oldValue, newValue) -> {

            FireBehavior fire = (FireBehavior) newValue;
            FuelBed fuel = fire.getFuelBed();

            double fl = fire.getFlameLength();
            double fi = fire.getFirelineIntensity();
            double sav = fire.getFuelBed().getCharacteristicSAV();
            double frt = fire.getFuelBed().getFlameResidenceTime();
            double rv = fire.getFuelBed().getReactionVelocity();

            yVelocityProperty.setValue(fl);
            xVelocityProperty.setValue(fl / 5);
            //xVelocityProperty.setValue(rv);
            //xVarianceProperty.setValue(fl * 2);
            xVarianceProperty.setValue(frt * 100);
            particleSizeProperty.setValue(fl * 2);
            //particleSizeProperty.setValue(fi / 100);
            numParticlesProperty.setValue(fl * 4);
            //numParticlesProperty.setValue(rv * 4);
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

        long max = this.numParticlesProperty.get() * Math.round(Math.random());
        for (long i = 0; i < max; i++) {

            // Compute origin
            double y0 = Math.random();
            double x0 = (Math.random() - 0.5) * 2;
            double y1 = y + y0 * 20;
            double x1 = x + x0 * xVariance;

            // Compute velocity...
            double y2 = Math.random();  // Creates a pulsing effect
            double x2 = (Math.random() - 0.5) * 2;
            // ... attenuate vy as x0 moves away from x to create wide base.
            double vy = y2 * yVelocity * (1 / (Math.abs(x0) + 1));
//            double vy = ySpline.interpolate(0, yVelocity, y2);
            // ... constrain vx as y2 moves upward to create flame tip.
            double vx = windwardSpline.interpolate(x2 * xVelocity, 0, y2);
//            double vx = x2 * xSpline.interpolate(x2, x1, y2) * xVelocity;
            Point2D velocity = new Point2D(vx, -vy);

//            double expireTime = expireBase + Interpolator.LINEAR.interpolate(0.0, 0.3, y2); // slow down upward particles
            double expireTime = Interpolator.LINEAR.interpolate(0.0, expireBase, y2); //

//            // BDS: testing wind profiles
//            double vx = x2 * xVelocity;
//            if (x2 > 0) {
//                vx = x2 * leewardSpline.interpolate(1.0, 0.0, y2) * xVelocity;
//            }else{
//                vx = x2 * windwardSpline.interpolate(1.0, 0.0, y2) * xVelocity;
//            }
            Particle p = new Particle(
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
