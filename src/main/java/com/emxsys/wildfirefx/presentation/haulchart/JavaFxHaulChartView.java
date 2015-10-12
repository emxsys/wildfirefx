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
package com.emxsys.wildfirefx.presentation.haulchart;

import com.emxsys.chart.LogScatterChart;
import com.emxsys.chart.axis.LogarithmicAxis;
import com.emxsys.chart.extension.ValueMarker;
import com.emxsys.chart.extension.XYAnnotations.Layer;
import com.emxsys.chart.extension.XYLineAnnotation;
import com.emxsys.chart.extension.XYPolygonAnnotation;
import com.emxsys.wildfirefx.model.FireBehavior;
import com.emxsys.wildfirefx.model.FireBehaviorUtil;
import com.emxsys.wildfirefx.model.FuelBed;
import com.emxsys.wildfirefx.presentation.View;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;

/**
 * A JavaFX version of the "Haul Chart".
 *
 * @author Bruce Schubert
 */
public class JavaFxHaulChartView implements View<JavaFxHaulChartController> {

    // Color constants
    private static final double ALPHA = 0.7;
    public static final Color COLOR_LOW = Color.rgb(128, 127, 255, ALPHA);         // blue
    public static final Color COLOR_MODERATE = Color.rgb(127, 193, 151, ALPHA);    // green
    public static final Color COLOR_ACTIVE = Color.rgb(255, 179, 130, ALPHA);      // tan
    public static final Color COLOR_VERY_ACTIVE = Color.rgb(255, 128, 255, ALPHA); // magenta
    public static final Color COLOR_EXTREME = Color.rgb(253, 128, 124, ALPHA);     // orange
    // Flame Length thresholds
    public static final double FL_THRESHOLD_LOW_TO_MODERATE = 1D;
    public static final double FL_THRESHOLD_MODERATE_TO_ACTIVE = 3D;
    public static final double FL_THRESHOLD_ACTIVE_TO_VERY_ACTIVE = 7D;
    public static final double FL_THRESHOLD_VERY_ACTIVE_TO_EXTREME = 15D;
    // X and Y axis properties
    private final double xMin = 10;
    private final double yMin = 1;
    private final double xMax = 11000;
    private final double yMax = 1100;

    private LogarithmicAxis xAxis;
    private LogarithmicAxis yAxis;

    // Plottable values
    private ScatterChart.Series seriesMax;
    private ScatterChart.Series seriesFlank;
    private ObservableList<XYChart.Series> dataset;

    /**
     * The chart is root node of this view.
     */
    private LogScatterChart<Double, Double> chart;
    /**
     * The MVC view controller.
     */
    private final JavaFxHaulChartController controller;

    /**
     * Constructs an MVC view (and controller) hosting a JavaFX-based "Haul
     * Chart".
     */
    public JavaFxHaulChartView() {
        createChart();
        controller = new JavaFxHaulChartController(this);
    }

    /**
     * @return The view's controller.
     */
    @Override
    public JavaFxHaulChartController getController() {
        return controller;
    }

    /**
     * @return A LogScatterChart.
     */
    @Override
    public Node getRoot() {
        return chart;
    }

    /**
     * Plots the fire behavior. Called by the controller.
     */
    void plotFireBehavior(FireBehavior fire) {
        // Resetting the chart so we don't display stale data if we don't have a valid fire.
        seriesMax.getData().clear();
        seriesFlank.getData().clear();
        chart.getMarkers().clearDomainMarkers();
        chart.getMarkers().clearRangeMarkers();
        if (fire == null) {
            chart.setSubtitle(null);
            return;
        }

        FuelBed fuel = fire.getFuelBed();
        String modelName = fuel.getFuelModel().getModelName();
        double heat = fuel.getHeatRelease();
        double rosMax = fire.getRateOfSpreadMax();
        double rosFlank = fire.getRateOfSpreadFlanking();

        // Updating the subtitle with the fuel model name
        chart.setSubtitle(modelName);
        chart.layout();

        // Update the plot with our x,y points for max and flanking fire behavior
        seriesMax.getData().add(new XYChart.Data(heat, rosMax));
        seriesFlank.getData().add(new XYChart.Data(heat, rosFlank));

        // Add range (ros) and domain (heat) markers
        chart.getMarkers().addRangeMarker(new ValueMarker(rosMax));
        chart.getMarkers().addRangeMarker(new ValueMarker(rosFlank));
        chart.getMarkers().addDomainMarker(new ValueMarker(heat));

    }

    /**
     * Creates a LogScatterChart representing a "Haul Chart".
     */
    @SuppressWarnings("unchecked")
    private void createChart() {
        String title = "JavaFX Haul Chart";
        String xAxisTitle = "Heat per Unit Area (HPA) Btu/ft^2"; // + heatStr;
        String yAxisTitle = "Rate of Spread (ROS) ft/min"; // + rosStr;
        String seriesMaxName = "Max Spread";
        String seriesFlankName = "Flanking Spread";

        xAxis = new LogarithmicAxis(xAxisTitle, xMin, xMax, 1.0d);
        yAxis = new LogarithmicAxis(yAxisTitle, yMin, yMax, 1.0d);

        seriesMax = new ScatterChart.Series();
        seriesMax.setName(seriesMaxName);

        seriesFlank = new ScatterChart.Series();
        seriesFlank.setName(seriesFlankName);

        // HACK: Addinng a dummy value to our series to provide the chart with
        // the necessary data to generate a legend.  Without inital values the
        // legend will not contain a symbol, only the name. 
        seriesMax.getData().add(new XYChart.Data(0, 0));
        seriesFlank.getData().add(new XYChart.Data(0, 0));

        dataset = FXCollections.observableArrayList(seriesMax, seriesFlank);

        chart = new LogScatterChart(xAxis, yAxis, dataset);
        chart.setTitle(title);
        // Set the drawing style for the symbols
        chart.getStylesheets().add("/styles/HaulChart.css");

        // Clear out the dummy data now that the legend has been created.
        seriesMax.getData().clear();
        seriesFlank.getData().clear();

        layoutFireBehaviorThresholds();
        layoutFlameLengthDivisions();
    }

    private void layoutFlameLengthDivisions() {
        // draw flame length divisions
        final double[] flameLens = {1.0, 2.0, 4.0, 8.0, 11.0, 15.0, 20.0};    // [ft]
        for (double flameLen : flameLens) {

            // get BTU value at bottom of chart for given flame length and 1 ch/hr
            double btu = FireBehaviorUtil.computeHeatAreaBtus(flameLen, yMin);

            // ... and get the  ROS value on the left edge of chart for 10 btu/ft^2
            double ros = FireBehaviorUtil.computeRateOfSpread(flameLen, xMin);

            chart.getAnnotations().add(new XYLineAnnotation(
                    btu, yMin, // start
                    xMin, ros, // end
                    1.5, Color.GRAY), Layer.BACKGROUND);

        }
    }

    private void layoutFireBehaviorThresholds() {
        //
        double xEndLow = FireBehaviorUtil.computeHeatAreaBtus(FL_THRESHOLD_LOW_TO_MODERATE, yMin);
        double xEndModerate = FireBehaviorUtil.computeHeatAreaBtus(FL_THRESHOLD_MODERATE_TO_ACTIVE, yMin);
        double xEndActive = FireBehaviorUtil.computeHeatAreaBtus(FL_THRESHOLD_ACTIVE_TO_VERY_ACTIVE, yMin);
        double xEndVeryActive = FireBehaviorUtil.computeHeatAreaBtus(FL_THRESHOLD_VERY_ACTIVE_TO_EXTREME, yMin);
        //
        double yEndLow = FireBehaviorUtil.computeRateOfSpread(FL_THRESHOLD_LOW_TO_MODERATE, xMin);
        double yEndModerate = FireBehaviorUtil.computeRateOfSpread(FL_THRESHOLD_MODERATE_TO_ACTIVE, xMin);
        double yEndActive = FireBehaviorUtil.computeRateOfSpread(FL_THRESHOLD_ACTIVE_TO_VERY_ACTIVE, xMin);
        double yEndVeryActive = FireBehaviorUtil.computeRateOfSpread(FL_THRESHOLD_VERY_ACTIVE_TO_EXTREME, xMin);
        //
        XYPolygonAnnotation lowBkgnd = new XYPolygonAnnotation(
                new double[]{xMin, yMin, xEndLow, yMin, xMin, yEndLow});
        XYPolygonAnnotation modBkgnd = new XYPolygonAnnotation(
                new double[]{xEndLow, yMin, xMin, yEndLow, xMin, yEndModerate, xEndModerate, yMin});
        XYPolygonAnnotation activeBkgnd = new XYPolygonAnnotation(
                new double[]{xEndModerate, yMin, xMin, yEndModerate, xMin, yEndActive, xEndActive, yMin});
        XYPolygonAnnotation veryActiveBkgnd = new XYPolygonAnnotation(
                new double[]{xEndActive, yMin, xMin, yEndActive, xMin, yEndVeryActive, xEndVeryActive, yMin});
        XYPolygonAnnotation extremeBkgnd = new XYPolygonAnnotation(
                new double[]{xEndVeryActive, yMin, xMin, yEndVeryActive, xMax * 10, yMax * 10});
        
        lowBkgnd.getNode().getStyleClass().add("low-fire-behavior");
        modBkgnd.getNode().getStyleClass().add("moderate-fire-behavior");
        activeBkgnd.getNode().getStyleClass().add("active-fire-behavior");
        veryActiveBkgnd.getNode().getStyleClass().add("very-active-fire-behavior");
        extremeBkgnd.getNode().getStyleClass().add("extreme-fire-behavior");
        //
//        lowBkgnd.setToolTipText("LOW");
//        modBkgnd.setToolTipText("MODERATE");
//        activeBkgnd.setToolTipText("ACTIVE");
//        veryActiveBkgnd.setToolTipText("VERY ACTIVE");
//        extremeBkgnd.setToolTipText("EXTREME");
        //
        chart.getAnnotations().add(lowBkgnd, Layer.BACKGROUND);
        chart.getAnnotations().add(modBkgnd, Layer.BACKGROUND);
        chart.getAnnotations().add(activeBkgnd, Layer.BACKGROUND);
        chart.getAnnotations().add(veryActiveBkgnd, Layer.BACKGROUND);
        chart.getAnnotations().add(extremeBkgnd, Layer.BACKGROUND);
    }

}
