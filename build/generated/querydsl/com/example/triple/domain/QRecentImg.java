package com.example.triple.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecentImg is a Querydsl query type for RecentImg
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecentImg extends EntityPathBase<RecentImg> {

    private static final long serialVersionUID = 2115850557L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecentImg recentImg = new QRecentImg("recentImg");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final ComparablePath<java.util.UUID> id = createComparable("id", java.util.UUID.class);

    public final StringPath imgUrl = createString("imgUrl");

    public final StringPath status = createString("status");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final QUser user;

    public QRecentImg(String variable) {
        this(RecentImg.class, forVariable(variable), INITS);
    }

    public QRecentImg(Path<? extends RecentImg> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecentImg(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecentImg(PathMetadata metadata, PathInits inits) {
        this(RecentImg.class, metadata, inits);
    }

    public QRecentImg(Class<? extends RecentImg> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

