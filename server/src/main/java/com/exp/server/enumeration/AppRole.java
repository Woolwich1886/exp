package com.exp.server.enumeration;

// На самом деле тут может быть реализована вложенность типа:
// ADMIN(Set.of(ADMIN_READ, ADMIN_EDIT...
// USER(Set.of(USER_READ...
// READER(Collections.emptySet())
public enum AppRole {
    ADMIN,
    USER,
    READER
}
