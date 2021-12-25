package com.bol.assignment.game.mapper;

import com.bol.assignment.game.dto.GameStatusOutput;
import com.bol.assignment.game.entity.GameStatus;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = PitMapper.class)
public interface GameStatusMapper {

    GameStatusMapper INSTANCE = Mappers.getMapper(GameStatusMapper.class);

    GameStatusOutput entityToOutput(GameStatus gameStatus);
}
