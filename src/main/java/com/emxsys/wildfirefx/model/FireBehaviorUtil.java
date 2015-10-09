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
package com.emxsys.wildfirefx.model;

/**
 * Utility class for computing fire behavior outputs. Useful for generating Haul
 * Chart indices.
 *
 * @author Bruce Schubert
 */
public class FireBehaviorUtil {

    /**
     * Compute fireline intensity (to be used as color key) from "Fire as a
     * Physical Process" page 47.
     *
     * @param heatAreaBtus [btus / ft^2 ]
     * @param rosChainsPerHour [chains per hour]
     * @return fireline intensity [btu / ft / sec]
     */
    public static double computeFirelineIntensity(double heatAreaBtus, double rosChainsPerHour) {
        // convert chains per hour to feet per minute
        double rosFtPerMin = rosChainsPerHour * 1.100;
        double fli = (heatAreaBtus * rosFtPerMin) / 60d;
        return fli;
    }

    /**
     * Computes the flame length from fireline intensity from Andrews and
     * Rothermel GTR-INT-131.
     *
     * @param fliBtuPerFtPerSec - fireline intensity [btu / ft / sec]
     * @return flame length [ft]
     */
    public static double computeFlameLength(double fliBtuPerFtPerSec) {
        double fl = 0.45 * Math.pow(fliBtuPerFtPerSec, 0.46);
        return fl;
    }

    /**
     * Computes heat/area BTUs from chart flame length and ROS.
     *
     * @param flameLen [ft]
     * @param rosChainsPerHour [chains/hour]
     * @return [btu / ft^2]
     */
    public static double computeHeatAreaBtus(double flameLen, double rosChainsPerHour) {
        double fliBtuFtSec = 5.67 * Math.pow(flameLen, 2.17);
        double rosFtPerMin = rosChainsPerHour * 1.100;
        double heatArea = (60 * fliBtuFtSec) / rosFtPerMin;
        return heatArea;
    }

    /**
     * Computes rate of spread from chart flame length and heat area.
     *
     * @param flameLen [ft]
     * @param heatAreaBtus btu / ft^2]
     * @return [chains/hour]
     */
    public static double computeRosChainsPerHour(double flameLen, double heatAreaBtus) {
        double fliBtuFtSec = 5.67 * Math.pow(flameLen, 2.17);
        double rosFtPerMin = (60 * fliBtuFtSec) / heatAreaBtus;
        double rosChainsPerHour = rosFtPerMin * 0.9091;
        return rosChainsPerHour;
    }

}
