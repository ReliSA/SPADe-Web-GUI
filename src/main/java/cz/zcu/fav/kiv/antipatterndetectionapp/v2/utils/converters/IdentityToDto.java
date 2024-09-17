package cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.converters;


import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Identity;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.IdentityDto;

import java.util.ArrayList;
import java.util.List;

public class IdentityToDto implements ClassToDto<Identity, IdentityDto> {

    @Override
    public IdentityDto convert(Identity source) {
        if (source == null) {
            return null;
        }

        return IdentityDto.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .email(source.getEmail())
                .build();
    }

    @Override
    public List<IdentityDto> convert(List<Identity> source) {
        List<IdentityDto> convertedList = new ArrayList<>();

        if(source == null){
            return convertedList;
        }
        source.stream().forEach(sourceItem->convertedList.add(this.convert(sourceItem)));
        return convertedList;
    }
}
