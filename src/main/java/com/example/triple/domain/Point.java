package com.example.triple.domain;

import com.example.triple.constant.PointAction;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
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
@Table(name = "points")
@DynamicInsert
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Point {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    private Integer pointCnt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PointAction action;
//
//    @Column(nullable = false)
//    @Enumerated(EnumType.STRING)
//    private PointType pointType;

    @Column(nullable = false)
    @ColumnDefault("'ACTIVE'")
    private String status;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Point(User user, Review review, PointAction action, Integer pointCnt) {
        this.user = user;
        this.review = review;
        this.action = action;
        this.pointCnt = pointCnt;
    }

    public static Point of(
            User user,
            Review review,
            PointAction action,
            Integer pointCnt)
    {
        return new Point(
                user,
                review,
                action,
                pointCnt
        );
    }
}
