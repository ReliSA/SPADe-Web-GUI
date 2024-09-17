package cz.zcu.fav.kiv.antipatterndetectionapp.v2.model;

import java.util.List;

/**
 *
 * Dto for deserialization of the obtained data from the dbo.app_metadata table, respectively for the "basics" key value
 *
 * @author Petr Urban
 * @since 2023-04-26
 * @version 1.0.0
 */
public class AboutPageDto {

    private int version;

    private List<String> authors;

    private String description;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
