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
package com.emxsys.wildfirefx.presentation.main;

import com.emxsys.wildfirefx.WildfireFxApp;
import com.emxsys.wildfirefx.model.FireBehavior;
import com.emxsys.wildfirefx.presentation.BasicController;
import com.emxsys.wildfirefx.presentation.haulchart.HaulChartView;
import com.emxsys.wildfirefx.presentation.simulation.SimView;
import com.emxsys.wildfirefx.presentation.simulation.SimController;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

/**
 * FXML presenter for MainView.
 *
 * @author Bruce Schubert
 */
public class MainController extends BasicController<FireBehavior, MainView> implements Initializable {

    @FXML
    private AnchorPane centerPane;

    @FXML
    private HBox centerLayout;

    @FXML
    private AnchorPane haulChartPane;

    @FXML
    private Slider sliderNumParticles;

    @FXML
    private Label labelNumParticles;

    @FXML
    private Slider sliderParticleSize;

    @FXML
    private Label labelParticleSize;

    @FXML
    private Slider sliderExpireTime;

    @FXML
    private Label labelExpireTime;

    @FXML
    private Label labelXVelocity;

    @FXML
    private Slider sliderXVelocity;

    @FXML
    private Label labelYVelocity;

    @FXML
    private Slider sliderYVelocity;

    private static String NUM_PARTICLES = "num_particles";
    private static String PARTICLE_SIZE = "particle_size";
    private static String EXPIRE_TIME = "expire_time";
    private static String X_VELOCITY = "x_velocity";
    private static String Y_VELOCITY = "y_velocity";

    /**
     * Initializes the child views.
     * 
     * @param location Not used.
     * @param resources Not used.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initializing MainPresenter");
        assert centerLayout != null : "fx:id=\"centerLayout\" was not injected: check your FXML file 'Scene.fxml'.";
        assert haulChartPane != null : "fx:id=\"haulChartPane\" was not injected: check your FXML file 'Scene.fxml'.";
        assert centerPane != null : "fx:id=\"centerPane\" was not injected: check your FXML file 'Scene.fxml'.";

        // Populate panes with model views.
        SimView simView = new SimView();
        SimController simController = simView.getController();

        centerPane.getChildren().add(fitToParent(simView.getRoot()));
        haulChartPane.getChildren().add(fitToParent(new HaulChartView().getRoot()));

        sliderNumParticles.valueProperty().addListener((observable, oldValue, newValue) -> {
            simController.getEmitter().numParticlesProperty().set(newValue.intValue());
            labelNumParticles.setText("Num Particles: " + newValue.intValue());
        });

        sliderParticleSize.valueProperty().addListener((observable, oldValue, newValue) -> {
            simController.getEmitter().particleSizeProperty().set(newValue.intValue());
            labelParticleSize.setText("Particle Size: " + newValue.intValue());
        });

        sliderExpireTime.valueProperty().addListener((observable, oldValue, newValue) -> {
            simController.getEmitter().expireTimeProperty().set(newValue.intValue() / 10.0);
            labelExpireTime.setText("ExpireTime: " + newValue.intValue() / 10.);
        });

        sliderXVelocity.valueProperty().addListener((observable, oldValue, newValue) -> {
            simController.getEmitter().xVelocityProperty().set(newValue.intValue() / 10.0);
            labelXVelocity.setText("X Velocity: " + newValue.intValue() / 10.);
        });
        sliderYVelocity.valueProperty().addListener((observable, oldValue, newValue) -> {
            simController.getEmitter().yVelocityProperty().set(newValue.intValue() / 10.0);
            labelYVelocity.setText("Y Velocity: " + newValue.intValue() / 10.);
        });

        loadPreferences();
    }

    /**
     * Initializes the resources dependent on the view creation. 
     */
    @Override
    protected void postInitialize() {
        
        WildfireFxApp.getPrimaryStage().setOnCloseRequest(event -> {
            savePreferences();
        });
    }

    private void loadPreferences() {

        Preferences prefs = Preferences.userNodeForPackage(this.getClass());
        sliderNumParticles.setValue(prefs.getDouble(NUM_PARTICLES, 40));
        sliderParticleSize.setValue(prefs.getDouble(PARTICLE_SIZE, 30));
        sliderExpireTime.setValue(prefs.getDouble(EXPIRE_TIME, 15));
        sliderXVelocity.setValue(prefs.getDouble(X_VELOCITY, 30));
        sliderYVelocity.setValue(prefs.getDouble(Y_VELOCITY, 50));
    }

    private void savePreferences() {

        Preferences prefs = Preferences.userNodeForPackage(this.getClass());
        prefs.putDouble(NUM_PARTICLES, sliderNumParticles.getValue());
        prefs.putDouble(PARTICLE_SIZE, sliderParticleSize.getValue());
        prefs.putDouble(EXPIRE_TIME, sliderExpireTime.getValue());
        prefs.putDouble(X_VELOCITY, sliderXVelocity.getValue());
        prefs.putDouble(Y_VELOCITY, sliderYVelocity.getValue());
    }

    private static Node fitToParent(Node child) {
        AnchorPane.setTopAnchor(child, 0.0);
        AnchorPane.setBottomAnchor(child, 0.0);
        AnchorPane.setLeftAnchor(child, 0.0);
        AnchorPane.setRightAnchor(child, 0.0);
        return child;
    }
}
