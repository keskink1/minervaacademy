package com.keskin.minerva.services;

public interface ISecurityService {
    boolean currentUserHasRoleAdmin();

    boolean currentUserHasRoleTeacher();

    boolean currentUserHasRoleStudent();
}
