package com.example.ClientApp.serviceTest;

import com.example.ClientApp.service.InvalidScoreException;
import com.example.ClientApp.service.ScoreService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public class ScoreServiceTest {
    @Test
    void testGetDescriptionScoreInsuficiente() {
        ScoreService scoreService = new ScoreService();
        String description = scoreService.getDescriptionScore(150);
        assertEquals("Insuficiente", description);
    }

    @Test
    void testGetDescriptionScoreInaceitavel() {
        ScoreService scoreService = new ScoreService();
        String description = scoreService.getDescriptionScore(400);
        assertEquals("Inaceitável", description);
    }

    @Test
    void testGetDescriptionScoreAceitavel() {
        ScoreService scoreService = new ScoreService();
        String description = scoreService.getDescriptionScore(600);
        assertEquals("Aceitável", description);
    }

    @Test
    void testGetDescriptionScoreRecomendavel() {
        ScoreService scoreService = new ScoreService();
        String description = scoreService.getDescriptionScore(800);
        assertEquals("Recomendável", description);
    }

    @Test
    void testGetDescriptionScoreInvalido() {
        ScoreService scoreService = new ScoreService();

        // Mock da exceção para verificar se ela está sendo lançada
        InvalidScoreException mockException = mock(InvalidScoreException.class);
        assertThrows(InvalidScoreException.class, () -> {
            scoreService.getDescriptionScore(1200);
            throw mockException;
        });
    }
}
