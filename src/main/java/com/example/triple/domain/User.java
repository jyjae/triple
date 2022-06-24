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
@NoArgsConstructor
@Table(name = "users")
@DynamicInsert
@EntityListeners(AuditingEntityListener.class)
@Entity
public class User {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "user")
    private List<UserProfileImg> userProfileImgs;

    @ToString.Exclude
    @OneToMany(mappedBy = "user")
    private List<Review> reviews = new LinkedList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "user")
    private List<Point> points = new LinkedList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "user")
    private List<RecentImg> recentImgs = new LinkedList<>();

    @Column(nullable = false)
    @ColumnDefault("'ACTIVE'")
    private String status;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public User(String name) {
        this.name = name;
    }


    public static User of(String name) {
        return new User(name);
    }
}
