package cz.zcu.fav.kiv.antipatterndetectionapp.service;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.types.Node;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.ProjectDto;

import java.util.ArrayList;
import java.util.List;

public interface ProjectService {

    /**
     * Method obtaining all projects sorted by name
     * @return  List of all existing projects
     */
    List<Project> getAllProjects();

    /**
     * Method obtaining project based on id
     * @param id    Identifier of project
     * @return      Project for given id or null
     */
    Project getProjectById(Long id);

    /**
     * Method obtaining projects for given ids
     * @param ids   Array of project's identifiers
     * @return      List of projects
     */
    List<Project> getAllProjectsForGivenIds(Long[] ids);

    /**
     * Method saving project into database
     * @param project   Project for saving
     * @return          Saved project
     */
    Project saveProject(Project project);

    /**
     * Method obtaining all projects without superproject
     * @return  List of parent projects
     */
    List<Project> getParentProjects();

    /**
     * Method obtaining all subordinate projects
     * to project defined by identifier
     * @param id    Identifier of project
     * @return      List of subordinate projects
     */
    List<Project> getSubordinateProjectsTo(Long id);

    /**
     * Method creating the hierarchy of projects by recursion
     * @param p Project for which the hierarchy will be generated
     * @return  List of project's children with their hierarchy
     */
    ArrayList<Node> calculate(ProjectDto p);

    /**
     * Method saving new structure of the projects and superprojects
     * @param nodes List of the nodes with projects to save
     * @param superProject Parent of the projects in the list
     * @return true if successful
     */
    boolean saveProjectsStructure(List<Node> nodes, Project superProject);
}
