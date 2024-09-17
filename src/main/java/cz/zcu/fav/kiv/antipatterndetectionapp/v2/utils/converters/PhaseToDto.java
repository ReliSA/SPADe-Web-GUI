package cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.converters;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Iteration;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Phase;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.IterationDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.PhaseDto;

import java.util.ArrayList;
import java.util.List;

public class PhaseToDto implements ClassToDto<Phase, PhaseDto> {
    @Override
    public PhaseDto convert(Phase source) {
        if (source == null) {
            return null;
        }

        return PhaseDto.builder()
                .externalId(source.getExternalId())
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .build();
    }

    @Override
    public List<PhaseDto> convert(List<Phase> source) {
        if (source == null) {
            return null;
        }

        ArrayList<PhaseDto> phasesDtos = new ArrayList<>();

        for (Phase ph: source) {
            phasesDtos.add(this.convert(ph));
        }
        return phasesDtos;
    }
}
