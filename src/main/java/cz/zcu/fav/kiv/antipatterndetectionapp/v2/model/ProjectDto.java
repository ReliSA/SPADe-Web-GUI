package cz.zcu.fav.kiv.antipatterndetectionapp.v2.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {
    private Long id;
    private String name;

    private String description;

    public static ProjectDtoBuilder builder() {return new ProjectDtoBuilder();}
}