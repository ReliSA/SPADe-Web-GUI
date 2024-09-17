package cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.converters;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Iteration;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Person;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.IterationDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.PersonDto;

import java.util.ArrayList;
import java.util.List;

public class IterationToDto implements ClassToDto<Iteration, IterationDto> {

    @Override
    public IterationDto convert(Iteration source) {
        if (source == null) {
            return null;
        }

        return IterationDto.builder()
                .externalId(source.getExternalId())
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .build();
    }

    @Override
    public List<IterationDto> convert(List<Iteration> source) {
        if (source == null) {
            return null;
        }

        ArrayList<IterationDto> iterationDtos = new ArrayList<>();

        for (Iteration it: source) {
            iterationDtos.add(this.convert(it));
        }
        return iterationDtos;
    }
}
