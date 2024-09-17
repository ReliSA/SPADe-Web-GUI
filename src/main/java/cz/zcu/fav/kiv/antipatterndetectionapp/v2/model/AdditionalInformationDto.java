package cz.zcu.fav.kiv.antipatterndetectionapp.v2.model;

import lombok.Data;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

@Data
public class AdditionalInformationDto<T> {

    Map<String, T> additionalFields;

    public AdditionalInformationDto() {
        this.additionalFields = new HashMap<>();
    }

    public void addIntoCollection(@NonNull String key,
                                  @NonNull T value) {
        this.additionalFields.putIfAbsent(key, value);
    }

}
