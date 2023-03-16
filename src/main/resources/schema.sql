CREATE TABLE IF NOT EXISTS Film (
    FilmID int  NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    Name varchar(200)   NOT NULL,
    Description varchar(200)   NOT NULL,
    ReleaseDate date  NOT NULL,
    Duration int   NOT NULL,
    Rate int,
    RatingID int   NOT NULL,
    CONSTRAINT pk_Film PRIMARY KEY (FilmID)
);

CREATE TABLE IF NOT EXISTS Users (
    UserID int NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    Email varchar(200) NOT NULL,
    Login varchar(50) NOT NULL,
    Name varchar(200) NOT NULL,
    Birthday date NOT NULL,
    CONSTRAINT pk_User PRIMARY KEY (UserID),
    CONSTRAINT uc_User_Email UNIQUE (Email)
);

CREATE TABLE IF NOT EXISTS Likes (
    LikeID int NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    UserID int NOT NULL,
    FilmID int NOT NULL,
    CONSTRAINT pk_Like PRIMARY KEY (LikeID)
);

CREATE TABLE IF NOT EXISTS Friends (
    FriendsID int NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    UserID int NOT NULL,
    FriendID int NOT NULL,
    Confirmed bool NOT NULL,
    CONSTRAINT pk_Friends PRIMARY KEY (FriendsID)
);

CREATE TABLE IF NOT EXISTS GenresGroup (
    GenresGroupID int NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    FilmID int NOT NULL,
    GenreID int NOT NULL,
    CONSTRAINT pk_GenresGroup PRIMARY KEY (GenresGroupID)
);

CREATE TABLE IF NOT EXISTS Genres (
    GenreID int NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    Name varchar(200) NOT NULL,
    CONSTRAINT pk_Genre PRIMARY KEY (GenreID),
    CONSTRAINT uc_Genre_Name UNIQUE (Name)
);

CREATE TABLE IF NOT EXISTS RatingMPA (
    RatingID int NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    Name varchar(10) NOT NULL,
    Description varchar(200) NOT NULL,
    CONSTRAINT pk_RatingMPA PRIMARY KEY (RatingID),
    CONSTRAINT uc_RatingMPA_Name UNIQUE (Name)
);

ALTER TABLE Film ADD CONSTRAINT IF NOT EXISTS fk_Film_RatingID FOREIGN KEY(RatingID)
    REFERENCES RatingMPA (RatingID) ON DELETE RESTRICT;

ALTER TABLE Likes ADD CONSTRAINT IF NOT EXISTS fk_Like_UserID FOREIGN KEY(UserID)
    REFERENCES Users (UserID);

ALTER TABLE Likes ADD CONSTRAINT IF NOT EXISTS fk_Like_FilmID FOREIGN KEY(FilmID)
    REFERENCES Film (FilmID) ON DELETE CASCADE;

ALTER TABLE Friends ADD CONSTRAINT IF NOT EXISTS fk_Friends_UserID FOREIGN KEY(UserID)
    REFERENCES Users (UserID);

ALTER TABLE Friends ADD CONSTRAINT IF NOT EXISTS fk_Friends_FriendID FOREIGN KEY(FriendID)
    REFERENCES Users (UserID);

ALTER TABLE GenresGroup ADD CONSTRAINT IF NOT EXISTS fk_GenresGroup_FilmID FOREIGN KEY(FilmID)
    REFERENCES Film (FilmID) ON DELETE CASCADE;

ALTER TABLE GenresGroup ADD CONSTRAINT IF NOT EXISTS fk_GenresGroup_GenreID FOREIGN KEY(GenreID)
    REFERENCES Genres (GenreID) ON DELETE RESTRICT;

ALTER TABLE GENRESGROUP ADD CONSTRAINT IF NOT EXISTS UC_GenresGroup_GenreID_FilmID UNIQUE (GenreID, FilmID)
