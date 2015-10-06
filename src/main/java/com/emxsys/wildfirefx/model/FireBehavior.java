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
 * @author Bruce Schubert
 */
public class FireBehavior {

    private final JsonObject jsonObject;

    /**
     * Constructs a FuelBehavior object from a JSON representation obtained from
     * the WMT-REST server.
     *
     * @param jsonObject JSON fire behavior.
     */
    public FireBehavior(JsonObject jsonObject) {
        this.jsonObject = jsonObject;

    }

    /**
     * Gets the flame length.
     * @return [flame_length:ft]
     */
    public double getFlameLength() {
        return Double.parseDouble(jsonObject.getJsonObject("flameLength").getString("value"));
    }

    /**
     * Gets Byram's intensity.
     * @return [fire_line_intensity:Btu/ft/s]
     */
    public double getFirelineIntensity() {
        return Double.parseDouble(jsonObject.getJsonObject("firelineIntensity").getString("value"));
    }

    /**
     * Gets the rate of spread.
     * @return [rate_of_spread:ft/min]
     */
    public double getRateOfSpreadMax() {
        return Double.parseDouble(jsonObject.getJsonObject("rateOfSpreadMax").getString("value"));
    }

    /**
     * Gets the conditioned fuel bed.
     * @return The fuel bed including the FuelModel and FuelMoisture.
     */
    public FuelBed getFuelBed() {
        return new FuelBed(jsonObject.getJsonObject("fuelBed"));
    }
}
