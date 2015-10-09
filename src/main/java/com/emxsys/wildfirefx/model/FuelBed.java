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
package com.emxsys.wildfirefx.model;

import javax.json.JsonObject;

/**
 *
 * @author Bruce Schubert
 */
public class FuelBed {

    private final JsonObject jsonFuelBed;
    private final FuelModel fuelModel;
    private final FuelMoisture fuelMoisture;

    /**
     *
     * <pre>
     * fuelBed : {
     *    "fuelModel": {
     *        "modelNo": "4",
     *        "modelCode": "#4",
     *       "modelName": "Chaparral (6 feet)",
     *       "modelGroup": "Original 13",
     *        "dynamic": "false",
     *        "dead1HrFuelLoad": {
     *           "type": "fuel_load:kg/m2",
     *           "value": "1.1230883661399034"
     *       },
     *       "dead10HrFuelLoad": {
     *           "type": "fuel_load:kg/m2",
     *           "value": "0.8989190315810404"
     *       },
     *       "dead100HrFuelLoad": {
     *           "type": "fuel_load:kg/m2",
     *           "value": "0.4483386691177259"
     *       },
     *       "liveHerbFuelLoad": {
     *           "type": "fuel_load:kg/m2",
     *           "value": "0.0"
     *       },
     *       "liveWoodyFuelLoad": {
     *           "type": "fuel_load:kg/m2",
     *          "value": "1.1230883661399034"
     *       },
     *       "dead1HrSAVRatio": {
     *           "type": "surface_to_volume:m2/m3",
     *           "value": "6561.679790026247"
     *       },
     *       "dead10HrSAVRatio": {
     *           "type": "surface_to_volume:m2/m3",
     *           "value": "357.6115485564305"
     *       },
     *       "dead100HrSAVRatio": {
     *           "type": "surface_to_volume:m2/m3",
     *           "value": "98.4251968503937"
     *       },
     *       "liveHerbSAVRatio": {
     *           "type": "surface_to_volume:m2/m3",
     *           "value": "0.0"
     *       },
     *       "liveWoodySAVRatio": {
     *           "type": "surface_to_volume:m2/m3",
     *           "value": "4921.259842519685"
     *       },
     *       "fuelBedDepth": {
     *           "type": "fuel_depth:m",
     *           "value": "1.8287999999999998",
     *           "unit": "m"
     *       },
     *       "moistureOfExtinction": {
     *           "type": "moisture_of_extinction:%",
     *           "value": "20.0",
     *           "unit": "%"
     *       },
     *       "lowHeatContent": {
     *           "type": "heat_content:kJ/kg",
     *           "value": "18608.0"
     *       },
     *       "burnable": "true"
     *   },
     *   "fuelMoisture": {
     *       "dead1HrFuelMoisture": {
     *           "type": "fuel_moisture_1h:%",
     *           "value": "6.0",
     *           "unit": "%"
     *       },
     *       "dead10HrFuelMoisture": {
     *           "type": "fuel_moisture_10h:%",
     *           "value": "7.0",
     *           "unit": "%"
     *       },
     *       "dead100HrFuelMoisture": {
     *           "type": "fuel_moisture_100h:%",
     *           "value": "8.0",
     *           "unit": "%"
     *       },
     *       "liveHerbFuelMoisture": {
     *           "type": "fuel_moisture_herb:%",
     *           "value": "70.0",
     *           "unit": "%"
     *       },
     *       "liveWoodyFuelMoisture": {
     *           "type": "fuel_moisture_woody:%",
     *           "value": "70.0",
     *           "unit": "%"
     *       }
     *   },
     *   "fuelTemperature": {
     *       "type": "GENERIC_REAL",
     *       "value": "NaN",
     *       "unit": "UniversalUnit"
     *   },
     *   "meanBulkDensity": {
     *       "type": "bulk_density:lb/ft3",
     *       "value": "0.12266556382050901"
     *   },
     *   "fuelParticleDensity": {
     *       "type": "particle_density:lb/ft3",
     *       "value": "32.0"
     *   },
     *   "meanPackingRatio": {
     *       "type": "mean_packing_ratio",
     *       "value": "0.0038332988693909067",
     *       "unit": ""
     *   },
     *   "optimalPackingRatio": {
     *       "type": "optimal_packing_ratio",
     *       "value": "0.007434592860248393",
     *       "unit": ""
     *   },
     *   "relativePackingRatio": {
     *       "type": "relative_packing_ratio",
     *       "value": "0.5156030654868752",
     *       "unit": ""
     *   },
     *   "characteristicSAV": {
     *       "type": "fuel_complex:ft2/ft3",
     *       "value": "1739.2294964144478"
     *   },
     *   "liveMoistureOfExt": {
     *       "type": "live_moisture_of_ext:%",
     *       "value": "302.1611722537434",
     *       "unit": "%"
     *   },
     *   "mineralDamping": {
     *       "type": "mineral_damping_coefficient",
     *       "value": "0.4173969279093913",
     *       "unit": ""
     *   },
     *   "moistureDamping": {
     *       "type": "moisture_damping_coefficient",
     *       "value": "0.26306971101211224",
     *       "unit": ""
     *   },
     *   "lowHeatContent": {
     *       "type": "heat_content:Btu/lb",
     *       "value": "8000.0"
     *   },
     *   "reactionVelocity": {
     *       "type": "reaction_velocity:1/min",
     *       "value": "14.155533132296872",
     *       "unit": ""
     *   },
     *   "reactionIntensity": {
     *       "type": "reaction_intensity:BTU/ft2/min",
     *       "value": "12434.728679843649"
     *   },
     *   "flameResidenceTime": {
     *       "type": "GENERIC_REAL",
     *       "value": "0.22078742385156463",
     *       "unit": "UniversalUnit"
     *   },
     *   "heatRelease": {
     *       "type": "GENERIC_REAL",
     *       "value": "2745.4317115158465",
     *       "unit": "UniversalUnit"
     *   },
     *   "propagatingFluxRatio": {
     *       "type": "GENERIC_REAL",
     *       "value": "0.03220906883716744",
     *       "unit": "UniversalUnit"
     *   },
     *   "heatSink": {
     *       "type": "GENERIC_REAL",
     *       "value": "68.52393024377115",
     *       "unit": "UniversalUnit"
     *   },
     *   "burnable": "true"
     *
     * </pre>
     *
     * @param jsonFuelBed
     */
    public FuelBed(JsonObject jsonFuelBed) {
        this.jsonFuelBed = jsonFuelBed;

        this.fuelModel = new FuelModel(jsonFuelBed.getJsonObject("fuelModel"));
        this.fuelMoisture = new FuelMoisture(jsonFuelBed.getJsonObject("fuelMoisture"));
    }

    /**
     * Gets the fuel model that this fuel bed is based upon.
     *
     * @return The fuel model.
     */
    public FuelModel getFuelModel() {
        return fuelModel;
    }

    /**
     * Gets the fuel moisture that this fuel bed is based upon.
     *
     * @return The fuel moisture.
     */
    public FuelMoisture getFuelMoisture() {
        return fuelMoisture;
    }

    /**
     * @return [fuel_complex:ft2/ft3]
     */
    public double getCharacteristicSAV() {
        return Double.parseDouble(jsonFuelBed.getJsonObject("characteristicSAV").getString("value"));
    }
    /**
     * @return
     */
    public double getFlameResidenceTime() {
        return Double.parseDouble(jsonFuelBed.getJsonObject("flameResidenceTime").getString("value"));
    }
    /**
     * Gets the heat release per unit area.
     *
     * @return hpa [Btu/ft2]     
     */
    public double getHeatRelease() {
        return Double.parseDouble(jsonFuelBed.getJsonObject("heatRelease").getString("value"));
    }
    /**
     * @return
     */
    public double getReactionVelocity() {
        return Double.parseDouble(jsonFuelBed.getJsonObject("reactionVelocity").getString("value"));
    }

}
