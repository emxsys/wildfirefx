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
package com.emxsys.wildfirefx.service;

import com.emxsys.wildfirefx.service.WmtRestService.Conditions;
import com.emxsys.wildfirefx.service.WmtRestService.FuelModelCategory;
import javax.json.JsonArray;
import javax.json.JsonObject;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 *
 * @author Bruce Schubert
 */
@Ignore
public class WmtRestServiceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testGetFuelModels() {
        System.out.println("getFuelModels(ALL)");
        JsonArray result = WmtRestService.getFuelModels(FuelModelCategory.ALL);
        assertTrue(result.size() > 0);
        
        result.stream().map((value) -> (JsonObject) value).forEach((fuelModel) -> {
            System.out.println("\t" + fuelModel.getString("modelNo") + " : " + fuelModel.getString("modelName"));
        });
    }

    @Test
    public void testGetFuelModel() {
        System.out.println("getFuelModel(4)");
        String fuelModelNo = "4";
        JsonObject result = WmtRestService.getFuelModel(fuelModelNo);
        assertEquals(fuelModelNo, result.getString("modelNo"));

        result.entrySet().stream().forEach((entry) -> {
            System.out.println("\t" + entry.getKey() + " : " + entry.getValue().toString());
        });
    }

    @Test
    public void testGetFuelMoisture() {
        System.out.println("getFuelMoisture(HOT_AND_DRY)");
        JsonObject result = WmtRestService.getFuelMoisture(Conditions.HOT_AND_DRY);
        
        result.entrySet().stream().forEach((entry) -> {
            System.out.println("\t" + entry.getKey() + " : " + entry.getValue().toString());
        });
    }

    @Test
    public void testGetSurfaceFuel() {
        System.out.println("testGetSurfaceFuel(4,HOT_AND_DRY)");
        JsonObject model = WmtRestService.getFuelModel("4");
        JsonObject moisture = WmtRestService.getFuelMoisture(Conditions.HOT_AND_DRY);
        JsonObject fuel = WmtRestService.getSurfaceFuel(model, moisture);

        fuel.entrySet().stream().forEach((entry) -> {
            System.out.println("\t" + entry.getKey() + " : " + entry.getValue().toString());
        });
    }
    @Test
    public void testGetSurfaceFire() {
        JsonObject model = WmtRestService.getFuelModel("4");
        JsonObject moisture = WmtRestService.getFuelMoisture(Conditions.HOT_AND_DRY);
        JsonObject fuel = WmtRestService.getSurfaceFuel(model, moisture);
        JsonObject weather = WmtRestService.getWeather(80, 25, 10, 135, 10);
        JsonObject terrain = WmtRestService.getTerrain(270, 30, 100);
        JsonObject fire = WmtRestService.getSurfaceFire(fuel, weather, terrain);
        
        System.out.println("testGetSurfaceFire(");
        System.out.println("   " + fuel.toString()+ ",");
        System.out.println("   " + weather.toString()+ ",");
        System.out.println("   " + terrain.toString() + ")");
        fire.entrySet().stream().forEach((entry) -> {
            System.out.println("\t" + entry.getKey() + " : " + entry.getValue().toString());
        });
    }

}
