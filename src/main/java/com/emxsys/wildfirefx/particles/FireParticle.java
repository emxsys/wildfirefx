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

import javafx.animation.Interpolator;
import javafx.geometry.Point2D;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;

/**
 *
 * @author Bruce Schubert
 */
public class FireParticle extends Particle {

    double wind;

    public FireParticle(double wind, double x, double y, Point2D velocity, double radius, double expireTime, Color startColor, Color endColor, BlendMode blendMode) {
        super(x, y, velocity, radius, expireTime, startColor, endColor, blendMode);

        this.wind = wind;
    }

    /**
     * Updates the particle's position and life (age). The age of the particle
     * determines the color.
     *
     * @param frameRate [frames per sec]
     */
    @Override
    public void update(double frameRate) {
//        double scale = 60 / frameRate;                  // 1x == 60 hz
//
//        double windSpeed = wind * 1.46667; // mph to ft/s
//        double wx = windSpeed / frameRate;
//        double vx = velocity.getX() / frameRate;
//        double dx = vx + wx;
//        // Attenuate delta based on proximity to ground
//        dx *= Interpolator.LINEAR.interpolate(0, 1, Math.min(y * -0.01, 1));
//        velocity = velocity.add(dx, 0);

        super.update(frameRate);
    }
}
