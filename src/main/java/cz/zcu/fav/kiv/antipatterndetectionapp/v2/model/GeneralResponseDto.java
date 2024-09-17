package cz.zcu.fav.kiv.antipatterndetectionapp.v2.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GeneralResponseDto<T,S> {
    private String message;
    private List<T> responseBody;
    private S additionalInformation;
    public static <T, S> GeneralResponseDtoBuilder<T, S> builder() {
        return new GeneralResponseDtoBuilder<T, S>();
    }

}
