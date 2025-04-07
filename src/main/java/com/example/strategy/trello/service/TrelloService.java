package com.example.strategy.trello.service;

import com.example.strategy.trello.model.Card;
import com.example.strategy.trello.model.Label;
import com.example.strategy.trello.model.ListOfCards;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrelloService {
    @Value("${trello.api.key}")
    private String apiKey;

    @Value("${trello.api.token}")
    private String token;

    @Value("${trello.api.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<ListOfCards> getListOfCards(String boardId) {
        String url = baseUrl + "/boards/" + boardId + "/lists?key=" + apiKey + "&token=" + token;
        System.out.println("🔍 Request URL: " + url);

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            String responseBody = response.getBody();

            ObjectMapper mapper = new ObjectMapper();

            ListOfCards[] allLists = mapper.readValue(responseBody, ListOfCards[].class);

            return Arrays.asList(allLists);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<Card> getCardsByLabel(String boardId, String labelId) {
        String url = baseUrl + "/boards/" + boardId + "/cards?key=" + apiKey + "&token=" + token;
        System.out.println("🔍 Request URL: " + url);

        try {
            ResponseEntity<String> cards = restTemplate.getForEntity(url, String.class);
            String responseBody = cards.getBody();

            System.out.println("🔍 Request URL: " + url);

            // Se a resposta começa com "<", é HTML e não JSON (provável erro)
            if (responseBody != null && responseBody.trim().startsWith("<")) {
                System.err.println("⚠️ Resposta inválida (HTML): Verifique chave, token, boardId e construção de URL.");
                return List.of();
            }

            // Responsável pela conversão de JSON para objeto Java
            ObjectMapper mapper = new ObjectMapper();
            Card[] allCards = mapper.readValue(responseBody, Card[].class);

            return Arrays.stream(allCards)
                    .filter(card -> card != null && card.id != null && cardHasLabel(card, labelId))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    private boolean cardHasLabel(Card card, String labelId) {
        return card != null && card.idLabels != null && card.idLabels.contains(labelId);
    }

    public Label[] getLabelsByBoardId(String boardId) {
        String url = "https://api.trello.com/1/boards/" + boardId + "/labels?key=" + apiKey + "&token=" + token;
        System.out.println("🔍 Request URL: " + url);

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            System.out.println("🔁 Raw Response: " + response.getBody());

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.getBody(), Label[].class);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new Label[0];
        }
    }

}