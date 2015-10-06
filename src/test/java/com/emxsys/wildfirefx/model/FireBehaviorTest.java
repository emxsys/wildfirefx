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

import com.emxsys.wildfirefx.service.WmtRestService;
import javax.json.JsonObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Bruce Schubert
 */
public class FireBehaviorTest {
    static JsonObject fire;
    
    public FireBehaviorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        JsonObject model = WmtRestService.getFuelModel("4");
        JsonObject moisture = WmtRestService.getFuelMoisture(WmtRestService.Conditions.HOT_AND_DRY);
        JsonObject fuel = WmtRestService.getSurfaceFuel(model, moisture);
        JsonObject weather = WmtRestService.getWeather(80, 25, 10, 135, 10);
        JsonObject terrain = WmtRestService.getTerrain(270, 30, 100);
        fire = WmtRestService.getSurfaceFire(fuel, weather, terrain);    
        
        System.out.println("FireBehaviorTest values:");
        fire.entrySet().stream().forEach((entry) -> {
            System.out.println("\t" + entry.getKey() + " : " + entry.getValue().toString());
        });
    }
    
    @Before
    public void setUp() {
    }

    @Test
    public void testGetFlameLength() {
        System.out.println("getFlameLength");
        FireBehavior instance = new FireBehavior(fire);
        double expResult = 23.138498817644386;
        double result = instance.getFlameLength();
        assertEquals(expResult, result, 0.01);
    }

    @Test
    public void testGetRateOfSpreadMax() {
        System.out.println("getRateOfSpreadMax");
        FireBehavior instance = new FireBehavior(fire);
        double expResult = 114.64958478903517;
        double result = instance.getRateOfSpreadMax();
        assertEquals(expResult, result, 0.01);
    }
    
}
