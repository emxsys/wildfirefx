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

import static com.emxsys.jfree.ClockCompassPlot.CLOCK_HAND_NEEDLE;
import static com.emxsys.jfree.ClockCompassPlot.WIND_NEEDLE;
import java.awt.Color;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.data.general.ValueDataset;

/**
 *
 * @author Bruce Schubert
 */
public class SolarPlot extends ClockCompassPlot {

    // SolarChart/SolarPlot members
    private static final int SOLAR_AZIMUTH_SERIES = 0;
    private static final int SOLAR_HOUR_SERIES = 1;
    private DateTimeFormatter titleFormatter = DateTimeFormatter.ofPattern("dd-MMM, HH:mm z");

    boolean shaded = false;
    boolean night = false;

    public SolarPlot() {
        setRosePaint(Color.orange);
        setRoseHighlightPaint(Color.gray);
        setRoseCenterPaint(Color.white);
        setDrawBorder(false);

        // The first (default) dataset is the direction of Solar Radiation
        setSeriesNeedle(SOLAR_AZIMUTH_SERIES, WIND_NEEDLE);
        setSeriesPaint(SOLAR_AZIMUTH_SERIES, Color.red);        // arrow heads
        setSeriesOutlinePaint(SOLAR_AZIMUTH_SERIES, Color.red); // arrow shafts and arrow head outline

        // The second  dataset is the Time / Clock
        ValueDataset dataset = new DefaultValueDataset(new Double(0.0));
        addDataset(dataset, null);
        setSeriesNeedle(SOLAR_HOUR_SERIES, CLOCK_HAND_NEEDLE);
        setSeriesPaint(SOLAR_HOUR_SERIES, Color.black);        // clock hands
        setSeriesOutlinePaint(SOLAR_HOUR_SERIES, Color.black); //        
    }

    /**
     * Updates the solar vectors.
     *
     * @param aziumthAngleDeg
     */
    public void setAzimuthAngle(double aziumthAngleDeg) {
        // Update the Azimuth Plot (solar vectors)
        DefaultValueDataset compassData = (DefaultValueDataset) getDatasets()[SOLAR_AZIMUTH_SERIES];
        double A = aziumthAngleDeg;
        compassData.setValue(A);
    }

    /**
     * Updates the clock hands.
     *
     * @param localTime
     */
    public void setClockTime(LocalTime localTime) {
        // Update the Time Plot (clock hands)
        DefaultValueDataset hourData = (DefaultValueDataset) getDatasets()[SOLAR_HOUR_SERIES];

        final double DEG_PER_HOUR_12 = 360 / 12.0;
        double hour = localTime.get(ChronoField.MINUTE_OF_DAY) / 60.0;
        double hourDegrees = (hour % 12.0) * DEG_PER_HOUR_12;
        hourData.setValue(hourDegrees);
    }

}
