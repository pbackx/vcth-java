package com.peated.valhack.model;

public record DataFile(String fileName, DataFileStatus status, Tournament tournament, String year) {
}
