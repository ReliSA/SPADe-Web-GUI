package cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.converters;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Activity;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.ActivityDto;

import java.util.ArrayList;
import java.util.List;

public class ActivityToDto implements ClassToDto<Activity, ActivityDto> {

    @Override
    public ActivityDto convert(Activity source) {
        if (source == null) {
            return null;
        }

        return ActivityDto.builder()
                .externalId(Long.parseLong(source.getExternalId()))
                .endDate(source.getEndDate())
                .startDate(source.getStartDate())
                .name(source.getName())
                .description(source.getDescription())
                .id(source.getId())
                .build();
    }

    @Override
    public List<ActivityDto> convert(List<Activity> source) {
        List<ActivityDto> convertedList = new ArrayList<>();

        if(source == null){
            return convertedList;
        }
        source.stream().forEach(sourceItem->convertedList.add(this.convert(sourceItem)));
        return convertedList;
    }
}
