package cz.zcu.fav.kiv.antipatterndetectionapp.v2.model;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkUnitDto {

    private String assignee;
    private String type;
    private Set<String> category;
    private long id;
    private Date startDate;
    private Date endDate;
    private String activity;

    public static WorkUnitDto.WorkUnitDtoBuilder builder() {return new WorkUnitDto.WorkUnitDtoBuilder();}
}
