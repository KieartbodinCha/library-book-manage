package com.bookorder.management.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseModel implements Serializable {

    private String status;

    private Object data;

    public ResponseModel() {

    }

    public ResponseModel(String message, Object data) {
        this.status = message;
        this.data = data;
    }
}
