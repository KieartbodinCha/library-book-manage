package com.bookorder.management.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "books")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookModel implements Serializable {


    @Id
    @JsonProperty("id")
    private Long id;

    @JsonProperty("author_name")
    private String authorName;

    @JsonProperty("book_name")
    private String bookName;

    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("is_recommended")
    private boolean recommended;


}
