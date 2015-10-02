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
package com.emxsys.wildfirefx.presentation.fire;

import com.emxsys.wildfirefx.particles.FireEmitter;
import com.emxsys.wildfirefx.model.FireBehavior;
import com.emxsys.wildfirefx.particles.Emitter;
import com.emxsys.wildfirefx.particles.Particle;
import com.emxsys.wildfirefx.presentation.BasicPresenter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;


/**
 * FirePresenter is an off-canvas particle system for rendering flames based on "JavaFX Game
 * Development: Particle System" by Almas Baimagambetov.
 *
 * @see <a href="https://youtu.be/vLcJRm6Y72U">JavaFX Game Development: Particle System</a>
 *
 * @author Bruce Schubert
 */
public class FirePresenter extends BasicPresenter<FireBehavior, FireView> implements
    Initializable {

    /** Particle emitter/generator */
    private Emitter emitter = new FireEmitter();

    /** Particles to be rendered */
    private List<Particle> particles = new ArrayList<>();

    private AnimationTimer timer;

    @FXML
    private Canvas canvas;

    private GraphicsContext g;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initializing AnimationPresenter");

        setModel(new FireBehavior());

        this.g = canvas.getGraphicsContext2D();

        // Create the timer that render the particles on each frame.
        this.timer = new AnimationTimer() {

            @Override
            public void handle(long now) {
                onUpdate();
            }
        };
        timer.start();
    }

    /**
     * Updates the particle system on each timer frame.
     */
    private void onUpdate() {

        g = canvas.getGraphicsContext2D();

        double w = canvas.getWidth();
        double h = canvas.getHeight();
        double x = w / 2;
        double y = h - 10;

        // Clear the canvas
        g.setGlobalAlpha(1.0);
        g.setGlobalBlendMode(BlendMode.SRC_OVER);
        g.setFill(Color.BLACK);
        g.fillRect(0, 0, w, h);

        // Generate particles
        particles.addAll(emitter.emit(x, y));

        for (Iterator<Particle> it = particles.iterator(); it.hasNext();) {
            Particle p = it.next();
            
            // Update the particle's position and color
            p.update();

            // Remove particles that have decayed.
            if (!p.isAlive()) {
                it.remove();
                continue;
            }

            p.render(g);
        }
    }

}
