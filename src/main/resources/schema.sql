use triple;

drop table if exists points;
drop table if exists review_imgs;
drop table if exists recent_imgs;
drop table if exists reviews;
drop table if exists places;
drop table if exists user_profile_imgs;
drop table if exists users;

create table users (
	id binary(16) primary key,
    name varchar(20) not null,
    status varchar(10) default 'ACTIVE',
    created_at timestamp DEFAULT current_timestamp,
    updated_at timestamp DEFAULT current_timestamp
);

create table user_profile_imgs (
	id binary(16) primary key,
    img_urls text,
    user_id binary(16),
    status varchar(10) default 'ACTIVE',
    created_at timestamp DEFAULT current_timestamp,
    updated_at timestamp DEFAULT current_timestamp,
    foreign key(user_id) references users(id) on update cascade on delete cascade
);

create table places (
	id binary(16) primary key,
    name varchar(50) not null,
    status varchar(10) default 'ACTIVE',
    created_at timestamp DEFAULT current_timestamp,
    updated_at timestamp DEFAULT current_timestamp
);

create table reviews (
	id binary(16) primary key,
    user_id binary(16) not null,
    place_id binary(16) not null,
    content text,
    status varchar(10) default 'ACTIVE',
    created_at timestamp DEFAULT current_timestamp,
    updated_at timestamp DEFAULT current_timestamp,
    foreign key(user_id) references users(id) on update cascade on delete cascade,
    foreign key(place_id) references places(id) on update cascade on delete cascade
);

create table recent_imgs(
	id binary(16) primary key,
    user_id binary(16) not null,
    img_url text not null,
    status varchar(10) default 'ACTIVE',
    created_at timestamp DEFAULT current_timestamp,
    updated_at timestamp DEFAULT current_timestamp,
    foreign key(user_id) references users(id) on update cascade on delete cascade
);
create table review_imgs(
	id binary(16) primary key,
    review_id binary(16) not null,
    recent_img_id binary(16) not null,
    status varchar(10) default 'ACTIVE',
    created_at timestamp DEFAULT current_timestamp,
    updated_at timestamp DEFAULT current_timestamp,
    foreign key(review_id) references reviews(id) on update cascade on delete cascade,
    foreign key(recent_img_id) references recent_imgs(id) on update cascade on delete cascade
);


create table points (
	id binary(16) primary key,
    user_id binary(16) not null,
    review_id binary(16) not null,
    point_cnt int not null,
    action varchar(20) not null,
    status varchar(10) default 'ACTIVE',
    created_at timestamp DEFAULT current_timestamp,
    updated_at timestamp DEFAULT current_timestamp,
    foreign key(user_id) references users(id) on update cascade on delete cascade,
    foreign key(review_id) references reviews(id) on update cascade on delete cascade
);


create index idx_recent_imgs_id_user_id on recent_imgs(id, user_id);
create index idx_review_id_user_id on reviews(id, user_id);
create index idx_review_user_id_place_id on reviews(user_id, place_id);