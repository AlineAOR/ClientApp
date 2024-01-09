package com.example.ClientApp.service;

import org.springframework.stereotype.Service;


@Service
public class ScoreService {
    public String getDescriptionScore(int punctuation) {
        try {
            if (punctuation >= 0 && punctuation <= 200) {
                return "Insuficiente";
            } else if (punctuation >= 201 && punctuation <= 500) {
                return "Inaceitável";
            } else if (punctuation >= 501 && punctuation <= 700) {
                return "Aceitável";
            } else if (punctuation >= 701 && punctuation <= 1000) {
                return "Recomendável";
            } else {
                throw new InvalidScoreException("Pontuação inválida");
            }
        } catch (InvalidScoreException e) {
            return e.getMessage();
        }
    }
}
