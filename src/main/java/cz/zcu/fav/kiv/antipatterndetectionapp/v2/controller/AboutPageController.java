package cz.zcu.fav.kiv.antipatterndetectionapp.v2.controller;

import com.google.gson.Gson;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.AboutPageDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.Metadata;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.about.AboutPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for dealing with requests that wants to obtain specific data related to the project.
 * Those data are mostly metadata to provide basic information, so there is no need for authentication at most cases.
 *
 * @author Petr Urban
 * @since 2023-04-26
 * @version 1.0.0
 */

@RestController
@RequestMapping("/v2/app/metadata")
public class AboutPageController {

    @Autowired
    private AboutPageService aboutPageService;

    private final String BASIC_INFO_COLUMN_KEY_VALUE = "basics";

    @GetMapping("/about")
    public ResponseEntity<String> getAboutPageData() {
        List<Metadata> data = aboutPageService.findAll();
        return ResponseEntity.status(data.size() > 0 ? HttpStatus.OK : HttpStatus.NO_CONTENT).body(new Gson().toJson(findAboutData(data)));
    }

    private List<AboutPageDto> findAboutData(List<Metadata> metadata) {
        if (metadata == null || metadata.isEmpty()) {
            return new ArrayList<>();
        }

        for (Metadata cursor : metadata) {
            if (cursor.getAppDataKey().equals(BASIC_INFO_COLUMN_KEY_VALUE)) {
                return new Gson().fromJson(cursor.getAppDataValue(), List.class);
            }
        }

        return new ArrayList<>();
    }

}
