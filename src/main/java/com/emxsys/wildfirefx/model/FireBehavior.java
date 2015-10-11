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

import javax.json.JsonObject;

/**
 *
 *
 * @author Bruce Schubert
 */
public class FireBehavior {

    private final JsonObject jsonObject;

    /**
     * Constructs a FuelBehavior object from a JSON representation obtained from
     * the WMT-REST server.
     *
     * @param jsonObject JSON fire behavior. <pre>
     * {
     *  aspect : {"type":"aspect:deg","value":"270.0","unit":"deg"}
     *  directionMaxSpread : {"type":"dir_of_spread:deg","value":"335.7639305636446","unit":"deg"}
     *  effectiveWindSpeed : {"type":"wind_speed:mph","value":"5.260550905405299"}
     *  firelineIntensity : {"type":"fire_line_intensity:Btu/ft/s","value":"5246.0434298657"}
     *  flameLength : {"type":"flame_length:ft","value":"23.138498817644386","unit":"international foot"}
     *  flameLengthBacking : {"type":"flame_length:ft","value":"5.916587884266262","unit":"international foot"}
     *  flameLengthFlanking : {"type":"flame_length:ft","value":"7.952394393604833","unit":"international foot"}
     *  fuelBed : {...}
     *  midFlameWindSpeed : {"type":"wind_speed:mph","value":"6.292451634296434"}
     *  rateOfSpreadBacking : {"type":"rate_of_spread:ft/min","value":"5.9134694426877665"}
     *  rateOfSpreadFlanking : {"type":"rate_of_spread:ft/min","value":"11.246842087522502"}
     *  rateOfSpreadMax : {"type":"rate_of_spread:ft/min","value":"114.64958478903517"}
     *  rateOfSpreadNoWindNoSlope : {"type":"rate_of_spread:ft/min","value":"5.844834506657489"}
     *  slope : {"type":"slope:deg","value":"30.0","unit":"deg"}
     *  windDirection : {"type":"wind_dir:deg","value":"135.0","unit":"deg"}
     * }
     * </pre>
     */
    public FireBehavior(JsonObject jsonObject) {
        this.jsonObject = jsonObject;

    }

    /**
     * Gets the flame length.
     *
     * @return [flame_length:ft]
     */
    public double getFlameLength() {
        return Double.parseDouble(jsonObject.getJsonObject("flameLength").getString("value"));
    }

    /**
     * Gets Byram's intensity.
     *
     * @return [fire_line_intensity:Btu/ft/s]
     */
    public double getFirelineIntensity() {
        return Double.parseDouble(jsonObject.getJsonObject("firelineIntensity").getString("value"));
    }

    /**
     * Gets the rate of spread.
     *
     * @return [rate_of_spread:ft/min]
     */
    public double getRateOfSpreadMax() {
        return Double.parseDouble(jsonObject.getJsonObject("rateOfSpreadMax").getString("value"));
    }

    /**
     * Gets the rate of spread.
     *
     * @return [rate_of_spread:ft/min]
     */
    public double getRateOfSpreadFlanking() {
        return Double.parseDouble(jsonObject.getJsonObject("rateOfSpreadFlanking").getString("value"));
    }

    /**
     *
     * @return [mph]
     */
    public double getEffectiveWindSpeed() {
        return Double.parseDouble(jsonObject.getJsonObject("effectiveWindSpeed").getString("value"));
    }

    /**
     * Gets the conditioned fuel bed.
     *
     * @return The fuel bed including the FuelModel and FuelMoisture.
     */
    public FuelBed getFuelBed() {
        return new FuelBed(jsonObject.getJsonObject("fuelBed"));
    }
}
