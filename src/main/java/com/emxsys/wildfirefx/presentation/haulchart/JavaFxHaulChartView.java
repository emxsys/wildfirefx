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
import com.emxsys.chart.extension.LogarithmicAxis;
import com.emxsys.chart.extension.ValueMarker;
import com.emxsys.chart.extension.XYAnnotations.Layer;
import com.emxsys.chart.extension.XYImageAnnotation;
import com.emxsys.chart.extension.XYLineAnnotation;
import com.emxsys.chart.extension.XYPolygonAnnotation;
import com.emxsys.chart.extension.XYTextAnnotation;
import com.emxsys.wildfirefx.model.FireBehavior;
import com.emxsys.wildfirefx.model.FireBehaviorUtil;
import com.emxsys.wildfirefx.model.FuelBed;
import com.emxsys.wildfirefx.presentation.View;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
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
    private final double minBtu = 10;     // Heat release
    private final double maxBtu = 11000;  // Heat release
    private final double minRos = 1;      // ROS ft/min
    private final double maxRos = 1100;   // ROS ft/min

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
        chart.getAnnotations().clearTextAnnotations(Layer.FOREGROUND);
        if (fire == null) {
            chart.setSubtitle(null);
            return;
        }

        FuelBed fuel = fire.getFuelBed();
        String modelName = fuel.getFuelModel().getModelName();
        double heat = fuel.getHeatRelease();
        double rosMax = fire.getRateOfSpreadMax();
        double rosFlank = fire.getRateOfSpreadFlanking();
        double flameLen = fire.getFlameLength();

        // Updating the subtitle with the fuel model name
        chart.setSubtitle(modelName);
        chart.layout();

        // Update the plot with our x,y points for max and flanking fire behavior
        seriesMax.getData().add(new XYChart.Data(heat, rosMax));
        seriesFlank.getData().add(new XYChart.Data(heat, rosFlank));
        // Show the flame len at the intersection of heat and rate-of-spread
        XYTextAnnotation flAnno = new XYTextAnnotation(String.format("%1$.1f ft Flame", flameLen), heat, rosMax);
        flAnno.setTextAnchor(Pos.BOTTOM_RIGHT);
        flAnno.getNode().getStyleClass().add("flame-length-label");
        chart.getAnnotations().add(flAnno, Layer.FOREGROUND);

        // Add range (ros) and domain (heat) markers
        chart.getMarkers().addRangeMarker(new ValueMarker(rosMax, String.format("%1$.1f ft/m ROS-Max", rosMax), Pos.TOP_LEFT));
        chart.getMarkers().addRangeMarker(new ValueMarker(rosFlank, String.format("%1$.1f ft/m ROS-Flank", rosFlank), Pos.BOTTOM_LEFT));
        // HACK: There's a bug in the domain marker pos - its the opposite of what's specified
        chart.getMarkers().addDomainMarker(new ValueMarker(heat, String.format("%1$.1f Btu/ft^2 HPA", heat), Pos.TOP_LEFT)); 

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

        xAxis = new LogarithmicAxis(xAxisTitle, minBtu, maxBtu, 1.0d);
        yAxis = new LogarithmicAxis(yAxisTitle, minRos, maxRos, 1.0d);

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

        // Create the background for the Haul Chart
        layoutFireBehaviorThresholds();
        layoutFlameLengthDivisions();
        layoutFireBehaviorImages();
    }

    private void layoutFlameLengthDivisions() {
        // draw flame length divisions
        final double[] flameLens = {1.0, 2.0, 4.0, 8.0, 11.0, 15.0, 20.0};    // [ft]
        for (double flameLen : flameLens) {

            // Draw flame length division from bottom of chart (given flame length 
            // and min ros) to left edge of chart for (given flame length and min btu)
            double btu = FireBehaviorUtil.computeHeatAreaBtus(flameLen, minRos);
            double ros = FireBehaviorUtil.computeRateOfSpread(flameLen, minBtu);
            chart.getAnnotations().add(new XYLineAnnotation(
                    btu, minRos, // start
                    minBtu, ros, // end
                    1.5, Color.GRAY), Layer.BACKGROUND);

            // Draw flame length labels in the lower diagonal half.
            // Compute new btu and ros to represent x,y values for label placement
            btu = FireBehaviorUtil.computeHeatAreaBtus(flameLen, flameLen * 3);
            ros = FireBehaviorUtil.computeRateOfSpread(flameLen, btu);
            chart.getAnnotations().add(new XYTextAnnotation(
                    Integer.toString((int) flameLen) + "\'", btu, ros),
                    Layer.BACKGROUND);

            // Draw fireline intensity labels in the upper in the diagonal half
            // Compute new btu and ros to represent x,y values for label placement
            btu = FireBehaviorUtil.computeHeatAreaBtus(flameLen, flameLen * 15);
            ros = FireBehaviorUtil.computeRateOfSpread(flameLen, btu);
            int fli = (int) Math.round(FireBehaviorUtil.computeFirelineIntensity(btu, ros));
            if (fli > 1000) {
                fli = (int) Math.round((double) fli / 1000) * 1000;
            } else if (fli > 100) {
                fli = (int) Math.round((double) fli / 100) * 100;
            }
            chart.getAnnotations().add(new XYTextAnnotation(
                    Integer.toString((int) fli), btu, ros),
                    Layer.BACKGROUND);
        }
        // Draw textual legend for flame length and fireline intensity values
        final double flameLen = 25;
        double btu = FireBehaviorUtil.computeHeatAreaBtus(flameLen, flameLen * 3);
        double ros = FireBehaviorUtil.computeRateOfSpread(flameLen, btu);
        XYTextAnnotation flAnno = new XYTextAnnotation("Flame Length, ft", btu, ros);
        flAnno.setTextAnchor(Pos.BOTTOM_CENTER);
        chart.getAnnotations().add(flAnno, Layer.BACKGROUND);
        // Draw fireline intensity values
        btu = FireBehaviorUtil.computeHeatAreaBtus(flameLen, flameLen * 15);
        ros = FireBehaviorUtil.computeRateOfSpread(flameLen, btu);
        XYTextAnnotation fliAnno = new XYTextAnnotation("Fireline Intensity, Btu/ft/sec", btu, ros);
        fliAnno.setTextAnchor(Pos.BOTTOM_CENTER);
        chart.getAnnotations().add(fliAnno, Layer.BACKGROUND);

    }

    private void layoutFireBehaviorThresholds() {

        // Compute heat release Btu thresholds
        double lowBtu = FireBehaviorUtil.computeHeatAreaBtus(FL_THRESHOLD_LOW_TO_MODERATE, minRos);
        double moderateBtu = FireBehaviorUtil.computeHeatAreaBtus(FL_THRESHOLD_MODERATE_TO_ACTIVE, minRos);
        double activeBtu = FireBehaviorUtil.computeHeatAreaBtus(FL_THRESHOLD_ACTIVE_TO_VERY_ACTIVE, minRos);
        double veryActiveBtu = FireBehaviorUtil.computeHeatAreaBtus(FL_THRESHOLD_VERY_ACTIVE_TO_EXTREME, minRos);
        // Compute rate of spread thresholds
        double lowRos = FireBehaviorUtil.computeRateOfSpread(FL_THRESHOLD_LOW_TO_MODERATE, minBtu);
        double moderateRos = FireBehaviorUtil.computeRateOfSpread(FL_THRESHOLD_MODERATE_TO_ACTIVE, minBtu);
        double activeRos = FireBehaviorUtil.computeRateOfSpread(FL_THRESHOLD_ACTIVE_TO_VERY_ACTIVE, minBtu);
        double veryActiveRos = FireBehaviorUtil.computeRateOfSpread(FL_THRESHOLD_VERY_ACTIVE_TO_EXTREME, minBtu);

        // Create polygons that represent the threshold regions.
        XYPolygonAnnotation lowBkgnd = new XYPolygonAnnotation(
                new double[]{minBtu, minRos, lowBtu, minRos, minBtu, lowRos});
        XYPolygonAnnotation modBkgnd = new XYPolygonAnnotation(
                new double[]{lowBtu, minRos, minBtu, lowRos, minBtu, moderateRos, moderateBtu, minRos});
        XYPolygonAnnotation activeBkgnd = new XYPolygonAnnotation(
                new double[]{moderateBtu, minRos, minBtu, moderateRos, minBtu, activeRos, activeBtu, minRos});
        XYPolygonAnnotation veryActiveBkgnd = new XYPolygonAnnotation(
                new double[]{activeBtu, minRos, minBtu, activeRos, minBtu, veryActiveRos, veryActiveBtu, minRos});
        XYPolygonAnnotation extremeBkgnd = new XYPolygonAnnotation(
                new double[]{veryActiveBtu, minRos, minBtu, veryActiveRos, maxBtu * 10, maxRos * 10});
        lowBkgnd.getNode().getStyleClass().add("low-fire-behavior");
        modBkgnd.getNode().getStyleClass().add("moderate-fire-behavior");
        activeBkgnd.getNode().getStyleClass().add("active-fire-behavior");
        veryActiveBkgnd.getNode().getStyleClass().add("very-active-fire-behavior");
        extremeBkgnd.getNode().getStyleClass().add("extreme-fire-behavior");

        chart.getAnnotations().add(lowBkgnd, Layer.BACKGROUND);
        chart.getAnnotations().add(modBkgnd, Layer.BACKGROUND);
        chart.getAnnotations().add(activeBkgnd, Layer.BACKGROUND);
        chart.getAnnotations().add(veryActiveBkgnd, Layer.BACKGROUND);
        chart.getAnnotations().add(extremeBkgnd, Layer.BACKGROUND);

        lowBkgnd.setTooltipText(
                "LOW: Fire will burn and will spread however \n"
                + "it presents very little resistance to \n"
                + "control and direct attack with \n"
                + "firefighters is possible ");
        modBkgnd.setTooltipText(
                "MODERATE : Fire spreads rapidly presenting \n"
                + "moderate resistance to control but can \n"
                + "be countered with direct attack by \n"
                + "firefighters ");
        activeBkgnd.setTooltipText(
                "ACTIVE: Fire spreads very rapidly presenting\n"
                + "substantial resistance to control.\n" 
                + "Direct attack with firefighters must be \n"
                + "supplemented with equipment and/or \n"
                + "air support. ");
        veryActiveBkgnd.setTooltipText(
                "VERY ACTIVE: Fire spreads very rapidly\n"
                + "presenting extreme resistance to control.\n"
                + "Indirect attack may be effective. Safety of \n"
                + "firefighters in the area becomes a concern. ");
        extremeBkgnd.setTooltipText(
                "EXTREME: Fire spreads very rapidly\n"
                + "presenting extreme resistance to control.\n"
                + "extreme resistance to control.  Any \n"
                + "form of attack will probably not be \n"
                + "effective.  Safety of firefighters in the \n"
                + "area is of critical concern. ");
    }

    private void layoutFireBehaviorImages() {
        // Setup background
        Image imgFireBehaviorLow = new Image(getClass().getResourceAsStream("/images/fire-behavior-low.gif"));
        Image imgFireBehaviorModerate = new Image(getClass().getResourceAsStream("/images/fire-behavior-moderate.gif"));
        Image imgFireBehaviorActive = new Image(getClass().getResourceAsStream("/images/fire-behavior-active.gif"));
        Image imgFireBehaviorVeryActive = new Image(getClass().getResourceAsStream("/images/fire-behavior-very-active.gif"));
        Image imgFireBehaviorExtreme = new Image(getClass().getResourceAsStream("/images/fire-behavior-extreme.gif"));

        chart.getAnnotations().add(new XYImageAnnotation(imgFireBehaviorLow, 30, 3, Pos.CENTER), Layer.BACKGROUND);
        chart.getAnnotations().add(new XYImageAnnotation(imgFireBehaviorModerate, 110, 11, Pos.CENTER), Layer.BACKGROUND);
        chart.getAnnotations().add(new XYImageAnnotation(imgFireBehaviorActive, 300, 30, Pos.CENTER), Layer.BACKGROUND);
        chart.getAnnotations().add(new XYImageAnnotation(imgFireBehaviorVeryActive, 600, 60, Pos.CENTER), Layer.BACKGROUND);
        chart.getAnnotations().add(new XYImageAnnotation(imgFireBehaviorExtreme, 1200, 120, Pos.CENTER), Layer.BACKGROUND);
    }

}
