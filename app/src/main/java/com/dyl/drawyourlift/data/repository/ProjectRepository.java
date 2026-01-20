package com.dyl.drawyourlift.data.repository;

import com.dyl.drawyourlift.data.model.LiftProject;

public class ProjectRepository {

    private static ProjectRepository instance;
    private LiftProject currentProject;

    private ProjectRepository() {
        currentProject = new LiftProject();
    }

    public static ProjectRepository getInstance() {
        if (instance == null) {
            instance = new ProjectRepository();
        }
        return instance;
    }

    public LiftProject getProject() {
        return currentProject;
    }

    public void resetProject() {
        currentProject = new LiftProject();
    }
}
