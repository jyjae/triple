package com.example.triple.dto;


import com.example.triple.domain.ReviewImg;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ReviewImgDto {
    private UUID id;
    private UUID review_id;
    private String imgUrl;

    public ReviewImgDto(UUID id, UUID review_id) {
        this.id = id;
        this.review_id = review_id;
    }

    public static ReviewImgDto of(
            UUID id,
            UUID reviewId,
            String imgUrl
    ) {
        return new ReviewImgDto(
                id,
                reviewId,
                imgUrl
        );
    }

//    public static List<ReviewImgDto> idOnly(UUID reviewId, List<UUID> attachedPhotoIds) {
//        List<ReviewImgDto> reviewImgDtos = new ArrayList<>();
//        for(UUID imgId : attachedPhotoIds) {
//            reviewImgDtos.add(ReviewImgDto.of(imgId, reviewId, null));
//        }
//        return reviewImgDtos;
//    }

}
