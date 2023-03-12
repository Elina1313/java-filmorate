# java-filmorate
Template repository for Filmorate project.
![BD](https://user-images.githubusercontent.com/112032648/224507281-b7f6b4b6-704e-4a63-acfe-db1a0e3200df.png)

Table friends {
  id int [pk, increment]
  user1_id int [ref: > users.user_id]
  user2_id int [ref: > users.user_id]
  confirmed bool
}

Table users {
  user_id int [pk, increment]
  email varchar
  login varchar
  name varchar
  birthday date  
}

Table likes {
  like_id int [pk, increment]
  user_id int [ref: > users.user_id]
  film_id int [ref: > films.film_id]    
}

Table films {
  film_id int [pk]
  name varchar
  mpa_id int [ref: > mpa.mpa_id]
  description varchar
  release_date date 
  duration long 
}

Table genres_group {
  group_id int [pk]
  film_id int [ref: > films.film_id]
  genre_id int [ref: > genres.genre_id]
}

Table genres {
  genre_id int [pk, increment]
  name varchar   
}

Table mpa_group as mpa {
  mpa_id int 
  category varchar 
}
