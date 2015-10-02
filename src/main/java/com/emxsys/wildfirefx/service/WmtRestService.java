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

import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

/**
 *
 * @author Bruce Schubert
 */
public class WmtRestService {

    /**
     * Fuel model categories.
     */
    public enum FuelModelCategory {

        ALL,
        STANDARD,
        ORIGINAL
    }

    /**
     * Fuel moisture conditions.
     */
    public enum Conditions {

        HOT_AND_DRY,
        BETWEEN_HOT_AND_COOL,
        COOL_AND_WET
    }

    /**
     * Multi-part enabled client.
     */
    private static final Client client = ClientBuilder.newBuilder()
            .register(MultiPartFeature.class)
            .build();

    /**
     * WMT REST server URL.
     */
    private static final String WMT_REST_SERVER = "http://emxsys.azurewebsites.net/wmt-rest/rs";

    /**
     * Hidden constructor
     */
    private WmtRestService() {
    }

    /**
     * Gets an array of fuel model JsonObjects.
     *
     * @return An array of type JsonObject.
     */
    public static JsonArray getFuelModels(FuelModelCategory category) {

        String entity = client.target(WMT_REST_SERVER)
                .path("fuelmodels")
                .queryParam("category", category.toString().toLowerCase())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(String.class);

        return toJsonObject(entity).getJsonArray("fuelModel");
    }

    /**
     * Gets a fuel model JsonObject.
     *
     * @param fuelModelNo The fuel model number.
     * @return A fuel model.
     */
    public static JsonObject getFuelModel(String fuelModelNo) {

        String entity = client.target(WMT_REST_SERVER)
                .path("fuelmodels/" + fuelModelNo)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(String.class);
        if (entity.isEmpty()) {
            throw new IllegalStateException("getFuelModel(" + fuelModelNo + ") returned an empty string.");
        }
        return toJsonObject(entity);
    }

    /**
     * Gets a predefined fuel moisture object.
     *
     * @param conditions Weather conditions.
     * @return A fuel moisture tuple.
     */
    public static JsonObject getFuelMoisture(Conditions conditions) {

        Response response = client.target(WMT_REST_SERVER)
                .path("fuelmoisture")
                .queryParam("conditions", conditions.toString().toLowerCase())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        if (response.getStatus() != Status.OK.getStatusCode()) {
            throw new IllegalStateException("getFuelMoisture(" + conditions.toString() + ") returned " + response.getStatusInfo().toString());
        }
        return toJsonObject(response.readEntity(String.class));
    }

    public static JsonObject getSurfaceFuel(JsonObject fuelModel, JsonObject fuelMoisture) {

        FormDataMultiPart multipart = new FormDataMultiPart()
                .field("fuelModel", fuelModel.toString(), MediaType.APPLICATION_JSON_TYPE)
                .field("fuelMoisture", fuelMoisture.toString(), MediaType.APPLICATION_JSON_TYPE);

        Response response = client.target(WMT_REST_SERVER)
                .path("surfacefuel")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(multipart, multipart.getMediaType()));

        if (response.getStatus() != Status.OK.getStatusCode()) {
            throw new IllegalStateException("getSurfaceFuel(\n" + fuelModel + ",\n" + fuelMoisture + ")\n returned " + response.getStatusInfo().toString());
        }
        return toJsonObject(response.readEntity(String.class));
    }

    public static JsonObject getSurfaceFire(JsonObject fuel, JsonObject weather, JsonObject terrain) {

        FormDataMultiPart multipart = new FormDataMultiPart()
                .field("fuel", fuel.toString(), MediaType.APPLICATION_JSON_TYPE)
                .field("weather", weather.toString(), MediaType.APPLICATION_JSON_TYPE)
                .field("terrain", terrain.toString(), MediaType.APPLICATION_JSON_TYPE);

        Response response = client.target(WMT_REST_SERVER)
                .path("surfacefire")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(multipart, multipart.getMediaType()));

        if (response.getStatus() != Status.OK.getStatusCode()) {
            throw new IllegalStateException("getSurfaceFire(\n"
                    + fuel + ",\n"
                    + weather + "\n"
                    + terrain + ")\n returned " + response.getStatusInfo().toString());
        }
        return toJsonObject(response.readEntity(String.class));
    }

    public static JsonObject getTerrain(double aspect, double slope, double elevation) {

        Response response = client.target(WMT_REST_SERVER)
                .path("terrain")
                .queryParam("aspect", Double.toString(aspect))
                .queryParam("slope", Double.toString(slope))
                .queryParam("elevation", Double.toString(elevation))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        if (response.getStatus() != Status.OK.getStatusCode()) {
            throw new IllegalStateException("getTerrain(...) returned " + response.getStatusInfo().toString());
        }
        return toJsonObject(response.readEntity(String.class));
    }

    public static JsonObject getWeather(double airTemperature, double relativeHumidity, double windSpeed, double windDirection, double cloudCover) {

        Response response = client.target(WMT_REST_SERVER)
                .path("weather")
                .queryParam("airTemperature", Double.toString(airTemperature))
                .queryParam("relativeHumidity", Double.toString(relativeHumidity))
                .queryParam("windSpeed", Double.toString(windSpeed))
                .queryParam("windDirection", Double.toString(windDirection))
                .queryParam("cloudCover", Double.toString(cloudCover))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        if (response.getStatus() != Status.OK.getStatusCode()) {
            throw new IllegalStateException("getTerrain(...) returned " + response.getStatusInfo().toString());
        }
        return toJsonObject(response.readEntity(String.class));
    }

    public static JsonObject toJsonObject(String jsonString) {
        // Get the JsonObject structure from JsonReader.
        try (JsonReader reader = Json.createReader(new StringReader(jsonString))) {
            return reader.readObject();
        }
    }
}
