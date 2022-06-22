package com.example.triple.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserProfileImg is a Querydsl query type for UserProfileImg
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserProfileImg extends EntityPathBase<UserProfileImg> {

    private static final long serialVersionUID = -168783152L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserProfileImg userProfileImg = new QUserProfileImg("userProfileImg");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final ComparablePath<java.util.UUID> id = createComparable("id", java.util.UUID.class);

    public final StringPath imgUrls = createString("imgUrls");

    public final StringPath status = createString("status");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final QUser user;

    public QUserProfileImg(String variable) {
        this(UserProfileImg.class, forVariable(variable), INITS);
    }

    public QUserProfileImg(Path<? extends UserProfileImg> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserProfileImg(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserProfileImg(PathMetadata metadata, PathInits inits) {
        this(UserProfileImg.class, metadata, inits);
    }

    public QUserProfileImg(Class<? extends UserProfileImg> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

