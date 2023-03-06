# java-filmorate
Template repository for Filmorate project.
![Untitled](https://user-images.githubusercontent.com/112032648/223231983-6f2ebb10-2711-4f93-941d-b8bd318905d9.png)
Запрос получения всех фильмов, пользователей. 
SELECT film_id,
       name
FROM films;
---------------------
SELECT user_id,
       login,
       name
FROM users;

Запрос получения топ 5 самых длинных фильмов, аренда которых составляет больше 2 долларов. Выведите на экран:
название фильма (поле title); цену аренды (поле rental_rate); длительность фильма (поле length); возрастной рейтинг (поле rating).
      
SELECT top.title, top.rental_rate, top.length, top.rating
FROM (
      SELECT *
      FROM films AS f
      WHERE rental_rate > 2     
      ORDER BY length DESC
      Limit 5
        ) AS top;

Запрос получения списка общих друзей с другим пользователем.
SELECT user_id
FROM friends
WHERE friendship = true
FROM friends;
