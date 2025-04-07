package com.example.strategy.trello.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Card {
    public String id;
    public String name;
    public String desc;
    public String url;
    public List<String> idLabels;
}