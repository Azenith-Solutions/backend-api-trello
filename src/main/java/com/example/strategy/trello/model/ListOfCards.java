package com.example.strategy.trello.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListOfCards {
    public String id;
    private String name;
}
