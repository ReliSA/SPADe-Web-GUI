package cz.zcu.fav.kiv.antipatterndetectionapp.v2.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDto {

    private String name;
    private String description;
    private long externalId;
    private Date startDate;
    private Date endDate;
    private long id;


    public static ActivityDto.ActivityDtoBuilder builder() {return new ActivityDto.ActivityDtoBuilder();}

}
