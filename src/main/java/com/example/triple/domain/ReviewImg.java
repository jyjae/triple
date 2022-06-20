package com.example.triple.domain;

import com.example.triple.constant.Status;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@Table(name = "review_imgs")
@EntityListeners(AuditingEntityListener.class)
@Entity
public class ReviewImg {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    @OneToOne
    @JoinColumn(name = "recent_img_id")
    private RecentImg recentImg;

    @Column(nullable = false, columnDefinition = "varchar(10) default 'ACTIVE'")
    @Enumerated(EnumType.STRING)
    private Status status;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public ReviewImg(Review review, RecentImg recentImg) {
        this.review = review;
        this.recentImg = recentImg;
    }

    public static ReviewImg of(
            Review review,
            RecentImg recentImg) {
        return new ReviewImg(
                review,
                recentImg
        );
    }
}
