# java-filmorate
Template repository for Filmorate project.
![sqlDB](https://user-images.githubusercontent.com/112032648/225884961-437c23dd-d080-4357-a33e-cb780d87f62d.png)


```
Table film {
  film_id int [pk]
  name varchar
  description varchar
  release_date date 
  duration long 
  rate int
  rating_id int [ref: > rating_mpa.rating_id]
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
  film_id int [ref: > film.film_id]    
}

Table friends {
  id int [pk, increment]
  user_id int [ref: > users.user_id]
  friend_id int [ref: > users.user_id]
  confirmed bool
}

Table genres_group {
  genres_group_id int [pk]
  film_id int [ref: > film.film_id]
  genre_id int [ref: > genres.genre_id]
}

Table genres {
  genre_id int [pk, increment]
  name varchar   
}

Table rating_mpa {
  rating_id int 
  name varchar 
  description varchar
}
```
