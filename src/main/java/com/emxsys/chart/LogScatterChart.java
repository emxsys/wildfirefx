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
package com.emxsys.chart;

import com.emxsys.chart.axis.LogarithmicAxis;
import java.util.Iterator;
import javafx.beans.NamedArg;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;

/**
 * A logarithmic scatter chart.
 *
 * @author Bruce Schubert
 * @param <X>
 * @param <Y>
 */
public class LogScatterChart<X, Y> extends EnhancedScatterChart<X, Y> {

    private Group plotArea = null;
    // References to XYChart grid lines
    private Path horzGridLines = null;
    private Path vertGridLines = null;
    // Major log grid lines added to XYChart
    private Path majorHorzGridLines = new Path();
    private Path majorVertGridLines = new Path();

    /**
     * Constructs a logarithmic scatter chart.
     * @param xAxis
     * @param yAxis 
     */
    public LogScatterChart(@NamedArg("xAxis") Axis<X> xAxis, @NamedArg("yAxis") Axis<Y> yAxis) {
        this(xAxis, yAxis, FXCollections.<Series<X, Y>>observableArrayList());
    }

    /**
     * Constructs a logarithmic scatter chart.
     * @param xAxis
     * @param yAxis
     * @param data 
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public LogScatterChart(@NamedArg("xAxis") Axis<X> xAxis, @NamedArg("yAxis") Axis<Y> yAxis, @NamedArg("data") ObservableList<Series<X, Y>> data) {
        super(xAxis, yAxis, data);
        
        majorHorzGridLines.getStyleClass().setAll("chart-horizontal-zero-line");
        majorVertGridLines.getStyleClass().setAll("chart-vertical-zero-line");

        // Find the gridlines contained in the plotArea of the XYChart.
        // The chart children include:
        //  plotBackground
        //  plotArea
        //  xAxis
        //  yAxis
        @SuppressWarnings("OverridableMethodCallInConstructor")
        ObservableList<Node> chartChildren = getChartChildren();
        Iterator<Node> chartIt = chartChildren.iterator();
        while (chartIt.hasNext() && plotArea == null) {

            Node chartChild = chartIt.next();

            // Find the "plotArea" which is a Group instance
            if (chartChild instanceof Group) {

                plotArea = (Group) chartChild;
                // Insert our custom background annotations before 
                // the plotContent found at the end of the plotArea children
                ObservableList<Node> plotAreaChildren = plotArea.getChildren();
                plotAreaChildren.add(plotAreaChildren.size() - 1, majorHorzGridLines);
                plotAreaChildren.add(plotAreaChildren.size() - 1, majorVertGridLines);

                // Obtain references to the factory "grid lines" which are Path instances
                for (Node plotAreaChild : plotAreaChildren) {
                    if (plotAreaChild instanceof Path) {
                        // We can identify the horizontal and vertical grid lines by the CSS.
                        ObservableList<String> styles = plotAreaChild.getStyleClass();

                        if (horzGridLines == null && styles.contains("chart-horizontal-grid-lines")) {
                            horzGridLines = (Path) plotAreaChild;
                        } else if (vertGridLines == null && styles.contains("chart-vertical-grid-lines")) {
                            vertGridLines = (Path) plotAreaChild;
                        }
                        if (horzGridLines != null && vertGridLines != null) {
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void layoutChildren() {
        // Invoked during the layout pass to layout this chart and all its content.
        super.layoutChildren();
        
        final Rectangle clip = (Rectangle) plotArea.getClip();
        final Axis<X> xa = getXAxis();
        final Axis<Y> ya = getYAxis();
        double top = clip.getY();
        double left = clip.getX();
        double xAxisWidth = xa.getWidth();
        double yAxisHeight = ya.getHeight();

        // Find the grid lines
        if (horzGridLines != null) {

        }

        majorVertGridLines.getElements().clear();
        if (vertGridLines != null && getVerticalGridLinesVisible()) {
            if (xa instanceof LogarithmicAxis) {
                // Move the major grid lines from the default Path to our custom Path
                final ObservableList<Axis.TickMark<X>> tickMarks = xa.getTickMarks();
                for (int i = 0; i < tickMarks.size(); i++) {
                    Axis.TickMark<X> tick = tickMarks.get(i);
                    Double value = (Double) tick.getValue();
                    double log = ((LogarithmicAxis) xa).calculateLog(value);
                    if (log % 1 == 0) {
                        double pixelOffset = (i == (tickMarks.size() - 1)) ? -0.5 : 0.5;
                        double x = xa.getDisplayPosition(tick.getValue());
                        //if ((x != xAxisZero || !isVerticalZeroLineVisible()) && x > 0 && x <= xAxisWidth) {
                        if (x > 0 && x <= xAxisWidth) {
                            majorVertGridLines.getElements().add(new MoveTo(left + x + pixelOffset, top));
                            majorVertGridLines.getElements().add(new LineTo(left + x + pixelOffset, top + yAxisHeight));
                        }
                    }
                }
            }
        }

    }


    /**
     * Modifiable and observable list of all content in the plot. This is where
     * implementations of XYChart should add any nodes they use to draw their
     * plot.
     *
     * @return Observable list of plot children
     */
    @Override
    protected ObservableList<Node> getPlotChildren() {
        ObservableList<Node> list = super.getPlotChildren();

        // TODO: Inject major plot lines into list
        return list;
    }

}
