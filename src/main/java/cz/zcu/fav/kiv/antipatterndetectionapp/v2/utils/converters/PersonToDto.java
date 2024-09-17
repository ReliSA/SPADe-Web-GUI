package cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.converters;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Person;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.IdentityDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.PersonDto;

import java.util.ArrayList;
import java.util.List;

public class PersonToDto implements ClassToDto<Person, PersonDto> {
    @Override
    public PersonDto convert(Person source) {
        if (source == null) {
            return null;
        }

        IdentityToDto identityToDto = new IdentityToDto();
        List<IdentityDto> identityDtos = identityToDto.convert(source.getIdentities());

        return PersonDto.builder()
                .id(source.getId())
                .name(source.getName())
                .identities(identityDtos)
                .build();
    }

    @Override
    public List<PersonDto> convert(List<Person> source) {
        List<PersonDto> convertedList = new ArrayList<>();

        if(source == null){
            return convertedList;
        }
        source.stream().forEach(sourceItem->convertedList.add(this.convert(sourceItem)));
        return convertedList;
    }
}
