package com.testehan.openliberty.chpt5;

import jakarta.ws.rs.FormParam;

public class CarInput {
    @FormParam("model")
    private String model;
    @FormParam("year")
    private String year;

    public String getModel() {
        return model;
    }

    public String getYear() {
        return year;
    }
}
