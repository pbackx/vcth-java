package com.peated.valhack.model;

import org.springframework.data.annotation.Id;

public record Agent(@Id Integer id, String guid, Integer roleId) {

}
