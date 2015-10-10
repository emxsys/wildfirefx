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
 *     - Neither the name of Bruce Schubert,  nor the names of its 
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
package com.emxsys.chart.extension;

import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.chart.ValueAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Region;
import javafx.scene.shape.Line;

/**
 *
 * @author Bruce Schubert
 */
public class XYAnnotations<X, Y> {

    private final XYChart chart;
    private final ObservableList<Node> chartChildren;
    private final ObservableList<Node> plotChildren;

    private final Region plotBackground;
    private final Region plotForeground = new Region();

    private final ObservableList<XYLineAnnotation> lines;
    //private final ObservableList<Node> polygons;
    //private final ObservableList<Node> labels;

    public XYAnnotations(XYChart chart, ObservableList<Node> chartChildren, ObservableList<Node> plotChildren) {
        this.chart = chart;
        this.chartChildren = chartChildren;
        this.plotChildren = plotChildren;

        // Get a reference to the plot backgound region
        this.plotBackground = (Region) plotChildren.get(0);
        if (!plotBackground.getStyleClass().contains("chart-plot-background")) {
            throw new IllegalStateException("Unable to assert that we found the plotBackground based on the styleClass.");
        }

        // Add a new plot foreground region to the plot area after the plot contents.
        this.plotForeground.getStyleClass().add("chart-plot-foreground");
        Group plotArea = (Group) plotChildren.get(1);
        plotArea.getChildren().add(plotForeground);

        // Create lists that notify on changes
        lines = FXCollections.observableArrayList();

        // Listen to list changes and re-plot
        lines.addListener((InvalidationListener) observable -> layoutLines());
    }

    public void add(XYLineAnnotation annotation) {
        //this.plotForeground.s
        this.lines.add(annotation);
        
    }
    public void layoutAnnotations() {
        layoutLines();
    }

    private void layoutLines() {

        ValueAxis xAxis = (ValueAxis) chart.getXAxis();
        ValueAxis yAxis = (ValueAxis) chart.getYAxis();
        for (XYLineAnnotation annotation : lines) {

            // Convert line values to axis values (e.g., log values)
            X x1 = (X) xAxis.toRealValue(annotation.getX1());
            X x2 = (X) xAxis.toRealValue(annotation.getX2());
            Y y1 = (Y) yAxis.toRealValue(annotation.getY1());
            Y y2 = (Y) yAxis.toRealValue(annotation.getY2());

            // Plot the values
            Line line = (Line) annotation.getNode();
            line.setStartX(xAxis.getDisplayPosition(x1));
            line.setStartY(yAxis.getDisplayPosition(y1));
            line.setEndX(xAxis.getDisplayPosition(x2));
            line.setEndY(yAxis.getDisplayPosition(y2));
        }
    }

}
