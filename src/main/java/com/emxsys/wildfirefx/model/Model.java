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
import com.emxsys.wildfirefx.service.WmtRestService.FuelModelCategory;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.json.JsonArray;
import javax.json.JsonObject;

/**
 * The MVC model class.
 *
 * @author Bruce Schubert
 */
public class Model {

    private final List<FuelModel> originalFuelModels = new ArrayList<>();
    private final List<FuelModel> standardFuelModels = new ArrayList<>();
    private final ObjectProperty<FuelModel> fuelModel = new SimpleObjectProperty<>();
    private final ObjectProperty<FireBehavior> fireBehavior = new SimpleObjectProperty<>();

    public Model() {
        initialize();
    }

    private void initialize() {
        initializeFuelModels();

        fuelModel.addListener((observable, oldValue, newValue) -> {
            System.out.println("Computing fire behavior for " + newValue);
            
            JsonObject model = newValue.getJsonObject();
            JsonObject moisture = WmtRestService.getFuelMoisture(WmtRestService.Conditions.HOT_AND_DRY);
            JsonObject fuel = WmtRestService.getSurfaceFuel(model, moisture);
            JsonObject weather = WmtRestService.getWeather(80, 25, 10, 135, 10);
            JsonObject terrain = WmtRestService.getTerrain(270, 30, 100);
            JsonObject fire = WmtRestService.getSurfaceFire(fuel, weather, terrain);
            fireBehavior.setValue(new FireBehavior(fire));
            
            System.out.println("Fire Behavior: " + fire.toString());
        });
    }

    /**
     * Loads the available fuel models from the WMT-REST server.
     */
    private void initializeFuelModels() {

        JsonArray original = WmtRestService.getFuelModels(FuelModelCategory.ORIGINAL);
        JsonArray standard = WmtRestService.getFuelModels(FuelModelCategory.STANDARD);

        original.stream().forEach((jsonFuelModel) -> {
            this.originalFuelModels.add(new FuelModel((JsonObject) jsonFuelModel));
        });
        standard.stream().forEach((jsonFuelModel) -> {
            this.standardFuelModels.add(new FuelModel((JsonObject) jsonFuelModel));
        });
    }

    public List<FuelModel> getOriginalFuelModels() {
        return originalFuelModels;
    }

    public List<FuelModel> getStandardFuelModels() {
        return standardFuelModels;
    }

    public FuelModel getFuelModel() {
        return fuelModel.get();
    }

    public void setFuelModel(FuelModel value) {
        fuelModel.set(value);
    }

    public ObjectProperty fuelModelProperty() {
        return fuelModel;
    }

    public FireBehavior getFireBehavior() {
        return fireBehavior.get();
    }

    public void setFireBehavior(FireBehavior value) {
        fireBehavior.set(value);
    }

    public ObjectProperty fireBehaviorProperty() {
        return fireBehavior;
    }

}
