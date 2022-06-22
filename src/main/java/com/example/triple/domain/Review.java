package com.example.triple.domain;

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
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@Table(name = "reviews")
@DynamicInsert
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Review {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;

    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY)
    private List<ReviewImg> reviewImgs = new LinkedList<>();

    @Column(nullable = false)
    @ColumnDefault("'ACTIVE'")
    private String status;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Review(String content, User user, Place place) {
        this.content = content;
        this.user = user;
        this.place = place;
    }

    public static Review of(
            String content,
            User user,
            Place place) {
        return new Review(
                content,
                user,
                place
        );
    }
}
