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
 * The FuelModel class provides various components of a fuel type used in the
 * computation of wildland fire behavior using the Rothermel fire spread
 * algorithms.
 *
 * @author Bruce Schubert
 */
public class FuelModel {

    public static double METERS_TO_FEET = 3.28084;
    
    private final JsonObject jsonObject;

    /**
     * Constructs a FuelModel object from a JSON representation obtained from
     * the WMT-REST server.
     * <pre>
     * "fuelModel": {
     *   "modelNo": "4",
     *   "modelCode": "#4",
     *   "modelName": "Chaparral (6 feet)",
     *   "modelGroup": "Original 13",
     *   "dynamic": "false",
     *   "dead1HrFuelLoad": {
     *       "type": "fuel_load:kg/m2",
     *       "value": "1.1230883661399034"
     *   },
     *   "dead10HrFuelLoad": {
     *       "type": "fuel_load:kg/m2",
     *       "value": "0.8989190315810404"
     *   },
     *   "dead100HrFuelLoad": {
     *       "type": "fuel_load:kg/m2",
     *       "value": "0.4483386691177259"
     *   },
     *   "liveHerbFuelLoad": {
     *       "type": "fuel_load:kg/m2",
     *       "value": "0.0"
     *   },
     *   "liveWoodyFuelLoad": {
     *       "type": "fuel_load:kg/m2",
     *       "value": "1.1230883661399034"
     *   },
     *   "dead1HrSAVRatio": {
     *       "type": "surface_to_volume:m2/m3",
     *       "value": "6561.679790026247"
     *   },
     *   "dead10HrSAVRatio": {
     *       "type": "surface_to_volume:m2/m3",
     *       "value": "357.6115485564305"
     *   },
     *   "dead100HrSAVRatio": {
     *       "type": "surface_to_volume:m2/m3",
     *       "value": "98.4251968503937"
     *   },
     *   "liveHerbSAVRatio": {
     *       "type": "surface_to_volume:m2/m3",
     *       "value": "0.0"
     *   },
     *   "liveWoodySAVRatio": {
     *       "type": "surface_to_volume:m2/m3",
     *       "value": "4921.259842519685"
     *   },
     *   "fuelBedDepth": {
     *       "type": "fuel_depth:m",
     *       "value": "1.8287999999999998",
     *       "unit": "m"
     *   },
     *   "moistureOfExtinction": {
     *       "type": "moisture_of_extinction:%",
     *       "value": "20.0",
     *       "unit": "%"
     *   },
     *   "lowHeatContent": {
     *       "type": "heat_content:kJ/kg",
     *       "value": "18608.0"
     *   },
     *   "burnable": "true"
     * }
     * </pre>
     *
     * @param jsonObject
     */
    public FuelModel(JsonObject jsonObject) {
        this.jsonObject = jsonObject;

    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public String getModelName() {
        return this.jsonObject.getString("modelName");
    }

    public String getModelNo() {
        return this.jsonObject.getString("modelNo");
    }

    public String getModelCode() {
        return this.jsonObject.getString("modelCode");
    }

    public double getLowHeatContent() {
        return Double.parseDouble(jsonObject.getJsonObject("lowHeatContent").getString("value"));
    }
    
    /**
     * @return [ft]
     */
    public double getFuelBedDepth() {
        return Double.parseDouble(jsonObject.getJsonObject("fuelBedDepth").getString("value")) * METERS_TO_FEET; 
    }

    public boolean isBurnable() {
        return this.jsonObject.getBoolean("burnable");
    }

    @Override
    public String toString() {
        return getModelCode() + " : " + getModelName();
    }

}
