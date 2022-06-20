package com.example.triple.domain;

import com.example.triple.constant.EventAction;
import com.example.triple.constant.PointAction;
import com.example.triple.constant.PointType;
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
@Table(name = "points")
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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PointAction action;
//
//    @Column(nullable = false)
//    @Enumerated(EnumType.STRING)
//    private PointType pointType;

    @Column(nullable = false, columnDefinition = "varchar(10) default 'ACTIVE'")
    @Enumerated(EnumType.STRING)
    private Status status;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Point(User user, Review review, PointAction action) {
        this.user = user;
        this.review = review;
        this.action = action;
    }

    public static Point of(
            User user,
            Review review,
            PointAction action)
    {
        return new Point(
                user,
                review,
                action
        );
    }
}
