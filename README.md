# java-filmorate
Template repository for Filmorate project.

# filmorate_db

Проект базы данных для приложения Filmorate проектируемого в Яндекс Практикуме

<!-- Структура каталогов репозитория -->
<details>
<summary> Структура проекта </summary>

- :file_folder: dbml_src - исходный код в формате .dbml для dbdiagram.io
- :file_folder: diagrams - изображения .png .svg диаграмм базы данных проекта
- :file_folder: sql_scripts - сгенерированные sql-скрипты

</details>

---

## Модель данных приложения Filmorate
<!-- Описание модели приложения -->
>Модель данных состоит из основных классов представляющих собой Пользователей и Фильмы, а также дополнительных: Жанр фильма, MPA-рейтинг, "Лайк"-рейтинг, Друзья и Статус запроса пользователей на добавление в друзья.
>
<details>
<summary> Визуализация диаграммы классов</summary>

```mermaid
---
title: Filmorate data model
---
classDiagram
    Film *-- Genre
    Film *-- MPARating
    Film *-- LikesRating
    User *-- Friends
    Friends *-- FriendshipStatus
    class Film {
        - Long id
        - String name
        - LocalDate releaseDate
        - String description
        - long duration
        - Set~Genre~ genres
        - MPARating mpaRating
        - LikesRating likesRating
    }
    class Genre{
        - Long id
        - String name
    }
    class MPARating{
        - Long id
        - String name
    }
    class LikesRating{
        - Long filmId
        - Set~Long~ usersId
    }
    class User{
        - Long id
        - String login
        - String email
        - String name
        - LocalDate birthday
        - Friends friends
    }
    class Friends{
        - Long userId
        - Map~Long, FriendshipStatus~
    }
    class FriendshipStatus{
        <<enum>>
        CONFIRMED
        REQUESTED
    }
```

</details>

---

## Диаграмма базы данных
<!-- Описание схемы базы данных -->
>Исходя из модели данных спроектированы основные таблицы:

- фильмы(films);
- жанр фильма(film_genre);
- жанры(genres);
- MPA-рейтинг(mpa_rating);
- лайк-рейтинг(likes_rating);
- пользователи(users);
- друзья(friends).

>Дополнительные таблицы позволяют реализовать связи между свойствами моделей.

*Таблица соответствия свойств модели и базы данных*

|Свойство модели     |Тип связи        |Таблица      |
|:---                |:--:             |:---:        |
|МРА-рейтинг фильма  |один ко многим   |MPA-рейтинг  |
|Лайк-рейтинг        |многие ко многим |лайк-рейтинг |
|жанры фильма        |многие ко многим |жанр фильма  |
|друзья пользователей|многие ко многим |дружба       |

<details>
<summary> Визуализация диаграммы </summary>

<!-- Диаграмма экспортирована из dbdiagram.io -->
![Diagram from simple database visualizer dbdiagram.io http://dbdiagram.io](/db/diagrams/Filmorate.svg)

</details>
