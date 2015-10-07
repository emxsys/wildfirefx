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

import com.emxsys.chartext.LogScatterChart;
import com.emxsys.chartext.axis.LogarithmicAxis;
import com.emxsys.chartext.axis.NumericAxis;
import com.emxsys.wildfirefx.presentation.View;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.ValueAxis;
import javafx.scene.chart.XYChart;

/**
 *
 * @author Bruce Schubert
 */
public class HaulChartView implements View<HaulChartController> {

    private ScatterChart chart;
    private ValueAxis xAxis;
    private ValueAxis yAxis;

    public HaulChartView() {
        createContent();
    }

    @Override
    public HaulChartController getController() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Node getRoot() {
        return chart;
    }

    @SuppressWarnings("unchecked")
    private void createContent() {
        
        xAxis = new LogarithmicAxis("X-Log Axis", 0.1d, 100.0d, 1.0d);
        yAxis = new NumericAxis("Y-Axis", 0.0d, 10.0d, 1.0d);
        
//        xAxis = new com.emxsys.jfxchartext.axis.NumericAxis("X-Axis", 0d, 8.0d, 2.d);
//        yAxis = new com.emxsys.jfxchartext.axis.NumericAxis("Y-Axis", 0.0d, 5.0d, 1.0d);
//        xAxis = new javafx.scene.chart.NumberAxis("X-Axis", 0d, 8.0d, 2.d);
//        yAxis = javafx.scene.chart.new NumberAxis("Y-Axis", 0.0d, 5.0d, 1.0d);
        
        ObservableList<XYChart.Series> data = FXCollections.observableArrayList(
                new ScatterChart.Series("Series 1",
                        FXCollections.<ScatterChart.Data>observableArrayList(
                                new XYChart.Data(1, 1),
                                new XYChart.Data(2, 2),
                                new XYChart.Data(3, 3),
                                new XYChart.Data(4, 4),
                                new XYChart.Data(5, 5),
                                new XYChart.Data(6, 6),
                                new XYChart.Data(7, 7),
                                new XYChart.Data(8, 8),
                                new XYChart.Data(9, 9),
                                new XYChart.Data(10, 10)
                        //                    new XYChart.Data(0.2, 3.5),
                        //                    new XYChart.Data(0.7, 4.6),
                        //                    new XYChart.Data(1.8, 1.7),
                        //                    new XYChart.Data(2.1, 2.8),
                        //                    new XYChart.Data(4.0, 2.2),
                        //                    new XYChart.Data(4.1, 2.6),
                        //                    new XYChart.Data(4.5, 2.0),
                        //                    new XYChart.Data(6.0, 3.0),
                        //                    new XYChart.Data(7.0, 2.0),
                        //                    new XYChart.Data(7.8, 4.0)
                        ))
        );
        chart = new LogScatterChart(xAxis, yAxis, data);
    }
}
