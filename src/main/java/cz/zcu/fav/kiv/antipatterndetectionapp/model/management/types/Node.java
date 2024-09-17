package cz.zcu.fav.kiv.antipatterndetectionapp.model.management.types;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.ProjectDto;

import java.util.ArrayList;

public class Node {
    public ProjectDto project;
    public ArrayList<Node> children;
}