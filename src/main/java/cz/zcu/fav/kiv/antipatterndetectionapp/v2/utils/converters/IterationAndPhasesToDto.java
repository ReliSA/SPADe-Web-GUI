package cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.converters;

import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.IterationAndPhasesDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.IterationDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.PhaseDto;

import java.util.List;

public class IterationAndPhasesToDto {

    public IterationAndPhasesDto convert (List<IterationDto> iterationDto, List<PhaseDto> phaseDtos) {
        if (iterationDto == null && phaseDtos == null) {
            return null;
        }

        return IterationAndPhasesDto.builder()
                .iterationDtos(iterationDto)
                .phaseDtos(phaseDtos)
                .build();
    }
}
