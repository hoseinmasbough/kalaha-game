package com.bol.assignment.game.mapper;

import com.bol.assignment.game.dto.PitOutput;
import com.bol.assignment.game.entity.Pit;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PitMapper {

    PitMapper INSTANCE = Mappers.getMapper(PitMapper.class);

    PitOutput entityToOutput(Pit pit);
}
