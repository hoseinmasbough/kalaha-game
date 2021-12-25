package com.bol.assignment.game.controller;

import com.bol.assignment.game.core.PlayerType;
import com.bol.assignment.game.dto.GameStatusOutput;
import com.bol.assignment.game.service.GameManagerService;
import com.bol.assignment.game.service.GameValidation;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameController.class)
class GameControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Value("${game.kalaha.pit.stones}")
    private int stones;

    @MockBean
    GameManagerService gameManagerService;

    @MockBean
    GameValidation gameValidation;

    @Test
    void should_getInitGameStats_when_createGame() throws Exception
    {
        GameStatusOutput gameStatusOutput = new GameStatusOutput();
        gameStatusOutput.setId("blahblah");
        gameStatusOutput.setActivePlayer(PlayerType.PLAYER_1.toString());
        gameStatusOutput.setPits(new ArrayList<>());

        Mockito.when(gameManagerService.createGame()).thenReturn(gameStatusOutput);

        mockMvc.perform(MockMvcRequestBuilders.post("/kalaha")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.is("blahblah")))
                .andExpect(jsonPath("$.activePlayer", Matchers.is(PlayerType.PLAYER_1.toString())))
                .andExpect(jsonPath("$.reward").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.winner").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.pits", Matchers.hasSize(0)));
    }


}
