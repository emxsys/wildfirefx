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
package com.emxsys.jfree;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.PlotState;
import org.jfree.chart.plot.dial.ArcDialFrame;
import org.jfree.chart.plot.dial.DialBackground;
import org.jfree.chart.plot.dial.DialPlot;
import org.jfree.chart.plot.dial.DialPointer;
import org.jfree.chart.plot.dial.StandardDialScale;
import org.jfree.data.general.ValueDataset;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.StandardGradientPaintTransformer;

/**
 * A dial plot used in a to display terrain slope.
 *
 * @author Bruce Schubert
 */
public class SlopePlot extends DialPlot {

    public SlopePlot(ValueDataset dataset) {
        super(dataset);
        // Set the viewport of the circular dial;
        // Set to upper right quadrant of circle with extra left and bottom for labels
        setView(0.4, 0.0, 0.6, 0.6);
        setInsets(RectangleInsets.ZERO_INSETS);

        // Frame
        ArcDialFrame dialFrame = new ArcDialFrame(-2.0, 94.0);
        dialFrame.setInnerRadius(0.40);
        dialFrame.setOuterRadius(0.90);
        dialFrame.setForegroundPaint(Color.darkGray);
        dialFrame.setStroke(new BasicStroke(2.0f));
        dialFrame.setVisible(true);
        setDialFrame(dialFrame);

        // Dial Background 
        GradientPaint gp = new GradientPaint(
                new Point(), new Color(180, 180, 180),
                new Point(), new Color(255, 255, 255));
        DialBackground db = new DialBackground(gp);
        db.setGradientPaintTransformer(new StandardGradientPaintTransformer(GradientPaintTransformType.CENTER_VERTICAL));
        addLayer(db);

        // Scale
        double MIN_SLOPE = 0;
        double MAX_SLOPE = 90;
        StandardDialScale scale = new StandardDialScale(MIN_SLOPE, MAX_SLOPE, 0, 90.0, 10.0, 4);
        scale.setTickRadius(0.6);
        scale.setTickLabelFormatter(new DecimalFormat("#0°"));
        scale.setTickLabelOffset(-0.1);
        scale.setMajorTickIncrement(10.0);  // Labeled increments
        scale.setTickLabelFont(new Font("Dialog", Font.PLAIN, 14));
        addScale(0, scale);

        // Needle
        DialPointer needle = new DialPointer.Pin();
        needle.setRadius(0.84);
        addLayer(needle);
    }

    @Override
    public void draw(Graphics2D g2, Rectangle2D area, Point2D anchor, PlotState parentState, PlotRenderingInfo info) {
        super.draw(g2, area, anchor, parentState, info); //To change body of generated methods, choose Tools | Templates.
    }

}
