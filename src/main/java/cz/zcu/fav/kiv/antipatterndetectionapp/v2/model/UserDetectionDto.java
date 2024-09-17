package cz.zcu.fav.kiv.antipatterndetectionapp.v2.model;

public class UserDetectionDto {

    private int configurationId;
    private String userName;



    private String[] selectedProjects;
    private String[] selectedAntipatterns;

    public UserDetectionDto(String userName,int configurationId, String[] selectedProjects, String[] selectedAntipatterns) {
        this.configurationId = configurationId;
        this.selectedProjects = selectedProjects;
        this.selectedAntipatterns = selectedAntipatterns;
        this.userName = userName;
    }
    public String getUserName(){
        return this.userName;
    }
    public UserDetectionDto() {
    }

    public void setConfigurationId(int configurationId) {
        this.configurationId = configurationId;
    }

    public void setSelectedProjects(String[] selectedProjects) {
        this.selectedProjects = selectedProjects;
    }

    public void setSelectedAntipatterns(String[] selectedAntipatterns) {
        this.selectedAntipatterns = selectedAntipatterns;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getConfigurationId() {
        return configurationId;
    }

    public String[] getSelectedProjects() {
        return selectedProjects;
    }

    public String[] getSelectedAntipatterns() {
        return selectedAntipatterns;
    }

}
