package com.example.triple.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class RecentImgResponse {
    private UUID id;
    private UUID userId;
}
