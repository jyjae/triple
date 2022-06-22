package com.example.triple.dto;

import com.example.triple.domain.Review;
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
public class UpdateReviewResponse {
    private String content;
    private List<ReviewImgDto> reviewImgs;

    public static UpdateReviewResponse of(
            String content,
            List<ReviewImg> reviewImgs) {
        List<ReviewImgDto> reviewImgDtos  = new ArrayList<>();

        for(ReviewImg reviewImg : reviewImgs) {
            reviewImgDtos.add(
                    ReviewImgDto.of(
                            reviewImg.getId(),
                            reviewImg.getReview().getId(),
                            reviewImg.getRecentImg().getImgUrl())
            );
        }

        return new UpdateReviewResponse(content, reviewImgDtos);
    }
}
