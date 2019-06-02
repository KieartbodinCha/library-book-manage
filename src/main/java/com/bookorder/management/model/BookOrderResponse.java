package com.bookorder.management.model;

import com.bookorder.management.utils.MoneySerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookOrderResponse implements Serializable {

    @JsonProperty("price")
    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal price;

    public BookOrderResponse() {
        price = BigDecimal.ZERO;
    }
}
