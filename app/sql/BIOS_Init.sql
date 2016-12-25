drop schema if exists bios;
create schema bios;
use bios;

create table Student (
    UserID varchar(128) not null, /* ada.goh.2012 etc */
    UserPassword varchar(128) not null, /* qwerty128 etc */
    NameOfUser varchar(100) not null, /* Ada GOH etc */
    School varchar(5) not null, /* SIS etc */
    EDollar double not null, /* 200.0 etc */

    primary key (UserID)
);

create table Course (
    CourseID varchar(10) not null, /* IS200 etc */
    School varchar(3) not null, /* SIS etc */
    Title varchar(100) not null, /* Calculus etc */
    Description text not null, /* Super awesome long description here etc */
    ExamDate varchar(40) not null, /* NOTE: supposed to return 20101119, but we will be storing datetime etc */
    ExamStart varchar(40) not null, /* 830 etc */
    ExamEnd varchar(40) not null, /* 1145 etc */

    primary key (CourseID)
);

create table CourseCompleted (
    UserID varchar(128) not null, /* ada.goh.2012 etc */
    CourseID varchar(10) not null, /* IS100 etc */
    
	primary key (UserID, CourseID),
    constraint completed_course_fk1 foreign key (UserID) references Student(UserID),
    constraint completed_course_fk2 foreign key (CourseID) references Course(CourseID)  
);

create table Prerequisite (
    CourseID varchar (10) not null,
    PrerequisiteID varchar(6) not null,
    
	primary key (CourseID, PrerequisiteID),
    constraint prerequisite_fk1 foreign key (CourseID) references Course(CourseID),
    constraint prerequisite_fk2 foreign key (PrerequisiteID) references Course(courseID)
);

create table Section (
    CourseID varchar(10) not null, /* IS200 etc */
    SectionID varchar(3) not null, /* S1, s2 etc */
    DayOfWeek int not null, /* Monday, Tuesday, Wednesday etc*/
    StartTime varchar(40) not null, /* 830 etc */
    EndTime varchar(40) not null, /* 1145 etc */
    Instructor varchar(100) not null, /* Albert KHOO etc */
    Venue varchar(100) not null, /* Seminar Rm 2-1 etc */
    Size int not null, /* 1,2,3,4 etc */

	primary key (CourseID, SectionID),
    constraint section_fk foreign key (CourseID) References Course(CourseID)
);

-- This entity is used to store all the bids made by students
create table Bid (
    UserID varchar(128) not null, /* joyce.hsu.2011 etc */
    Amount double not null, /* 12.0 etc */
    CourseID varchar(10) not null, /* S1 etc */
    SectionID varchar(3) not null, /* IS100 etc */
    Status varchar(7) not null, /* success, pending, fail */

	PRIMARY KEY (UserID, CourseID),
    constraint bid_fk1 foreign key (CourseID, SectionID) references Section(CourseID, SectionID),
    constraint bid_fk2 foreign key (UserID) references Student(UserID)
);

-- We can presume that this is used to store the successful bids where students are already placed into sections
create table SectionStudent (
    CourseID varchar(10) not null, /* IS100 etc */
    UserID varchar(128) not null, /* ada.goh.2012 etc */
    SectionID varchar(3) not null, /* S1 etc */
    Amount double not null, /* 12.0 etc */

    primary key (CourseID, UserID),
    constraint section_student_fk1 foreign key (CourseID, SectionID) references Section(CourseID, SectionID),
    constraint section_student_fk2 foreign key (UserID) references Student(UserID)
);



-- This is a weird design
-- All we have to do, is to ensure that we only have one bidding round at any one time
-- We shall give it a unique ID of 'bid_round'
-- Round is used to store the number of round that the 'bid_round' is currently on
create table BiddingRound(
	BidRoundID varchar(20) not null, /* NOTE: we will be storing one and only one fixed ID here, "bid_round" etc */
	Round int not null, /* 1,2,3 etc */
        Status varchar(10) not null,

    constraint bidding_round_pk  primary key (BidRoundID)
);

-- This is the creation of a bidding round with a unique id
insert into BiddingRound(BidRoundID, Round, Status) values('bid_round', 1, 'started');



