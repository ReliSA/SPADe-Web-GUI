package cz.zcu.fav.kiv.antipatterndetectionapp.v2.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdentityDto {

    private Long id;
    private String name;

    private String description;

    private String email;


    public static IdentityDtoBuilder builder() {return new IdentityDtoBuilder();}
}
