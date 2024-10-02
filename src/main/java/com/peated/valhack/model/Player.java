package com.peated.valhack.model;

import org.springframework.data.annotation.Id;

public record Player(@Id Integer id, String name, String mappingDataId) {

}
