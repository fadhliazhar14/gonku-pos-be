package com.gonku.pos_be.constant;

public class ResponseMessages {

    private ResponseMessages() {
        // private constructor to prevent instantiation
    }

    public static final String SUCCESS = "Success";
    public static final String FAILED = "Failed";

    public static String created(String entity) {
        return String.format("%s has been created successfully", entity);
    }

    public static String granted(String entity) {
        return String.format("%s has been granted successfully", entity);
    }

    public static String revoked(String roleName) {
        return String.format("%s role has been revoked successfully", roleName);
    }

    public static String updated(String entity) {
        return String.format("%s has been updated successfully", entity);
    }

    public static String deleted(String entity) {
        return String.format("%s has been deleted successfully", entity);
    }

    public static String notFound(String entity) {
        return String.format("%s not found", entity);
    }

    public static String alreadyExists(String entity) {
        return String.format("%s is already exists", entity);
    }

    public static String taken(String field) {
        return String.format("%s is already taken", field);
    }
}
