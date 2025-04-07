package com.example.strategy.trello.controller;

import com.example.strategy.trello.model.Card;
import com.example.strategy.trello.model.Label;
import com.example.strategy.trello.model.ListOfCards;
import com.example.strategy.trello.service.TrelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trello")
public class TrelloController {
    @Autowired
    private TrelloService trelloService;

    @GetMapping("/cards")
    public List<Card> getCardsByLabel(@RequestParam String boardId, @RequestParam String labelId) {
        return trelloService.getCardsByLabel(boardId, labelId);
    }

    @GetMapping("/labels")
    public Label[] getLabelsByBoard(@RequestParam String boardId) {
        return trelloService.getLabelsByBoardId(boardId);
    }

    @GetMapping("/listsOfCards")
    public List<ListOfCards> getListOfCards(@RequestParam String boardId) {
        return trelloService.getListOfCards(boardId);
    }
}
