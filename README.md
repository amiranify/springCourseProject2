# Приложения для цифрового учета книг 2
## Техническое задание
### Задачи:
1. Переписать [Приложения для цифрового учета книг](https://github.com/amiranify/SpringCrudProject1) с использованием Hibernate и Spring Data JPA. В вашем
проекте не должно быть ни одного SQL запроса. Должны быть реализованы
сущности (@Entity) Книга и Человек, репозитории и сервисы. PersonDAO и
BookDAO должны быть пустыми и не должны использоваться, вся работа с БД
через сервисы.

### Стэк: Spring DataJPA, Hibernate, Spring Validator, Thymeleaf

#### Новый функционал (кратко, подробности далее):
1. Добавить пагинацию для книг.
Книг может быть много и они могут не помещаться на одной странице в
браузере. Чтобы решить эту проблему, метод контроллера должен уметь
выдавать не только все книги разом, но и разбивать выдачу на страницы.
2. Добавить сортировку книг по году. Метод контроллера должен уметь
выдавать книги в отсортированном порядке.
3. Создать страницу поиска книг. Вводим в поле на странице начальные буквы
названия книги, получаем полное название книги и имя автора. Также, если
книга сейчас находится у кого-то, получаем имя этого человека.
4. Добавить автоматическую проверку на то, что человек просрочил возврат
книги.

## Пагинация
Метод index() в BooksController должен уметь принимать в адресной строке два
ключа: page и books_per_page. Первый ключ сообщает, какую страницу мы
запрашиваем, а второй ключ сообщает, сколько книг должно быть на одной странице.
Нумерация страниц стартует с 0. Если в адресной строке не передаются эти ключи, то
возвращаются как обычно все книги.

Например (обычный запрос на http://localhost:8080/books):
1. Тайные виды на гору Фудзи, Владимир Пелевин, 2018
2. Бытие и время, Мартин Хайдеггер, 1927
3. Над пропастью во ржи, Джером Сэлинджер, 1951
4. Философия Java, Брюс Эккель, 2018
5. Психопатология обыденной жизни, Фрейд Зигмунд, 1904
6. День опричника, Владимир Сорокин, 2006
7. Игра в бисер, Герман Гессе, 1943

Делаем запрос http://localhost:8080/books?page=1&books_per_page=3

Получаем: 

1. Философия Java, Брюс Эккель, 2018
2. Психопатология обыденной жизни, Фрейд Зигмунд, 1904
3. День опричника, Владимир Сорокин, 2006

## Сортировка
Метод index() в BooksController должен уметь принимать в адресной строке ключ
sort_by_year. Если он имеет значение true, то выдача должна быть отсортирована
по году. Если в адресной строке не передается этот ключ, то книги возвращаются в
обычном порядке.

Например (обычный запрос на http://localhost:8080/books):
1. Тайные виды на гору Фудзи, Владимир Пелевин, 2018
2. Бытие и время, Мартин Хайдеггер, 1927
3. Над пропастью во ржи, Джером Сэлинджер, 1951
4. Философия Java, Брюс Эккель, 2018
5. Психопатология обыденной жизни, Фрейд Зигмунд, 1904
6. День опричника, Владимир Сорокин, 2006
7. Игра в бисер, Герман Гессе, 1943

Делаем запрос http://localhost:8080/books?sort_by_year=true

Получаем: 
1. Психопатология обыденной жизни, Фрейд Зигмунд, 1904
2. Бытие и время, Мартин Хайдеггер, 1927
3. Игра в бисер, Герман Гессе, 1943
4. Над пропастью во ржи, Джером Сэлинджер, 1951
5. День опричника, Владимир Сорокин, 2006
6. Философия Java, Брюс Эккель, 2018
7. Тайные виды на гору Фудзи, Владимир Пелевин, 2018

(Книги отсортированы по году)

Пагинация и сортировка могут
работать одновременно (если
передаются сразу три параметра в
запросе).

Делаем запрос http://localhost:8080/books?sort_by_year=true&page=0&books_per_page=3

Получаем:
1. Психопаталогия обыденной жизни, Фрейд Зигмунд, 1904
2. Бытие и время, Мартин Хайдеггер, 1927
3. Игра в бисер, Герман Гессе, 1943


## Страница поиска
### /books/search

• 
На странице поиска должна выдаваться найденная книга и
текущий хозяин книги, если он есть. Если такой книги не было
найдено, то должно выдаваться сообщение о том, что "Книг не
найдено"

• 
Поиск должен производиться по начальным буквам названия
книги (с помощью JPA репозитория)

Делаем запрос http://localhost:8080/books/search

При вводе "Декамерон" - Книг не найдено.

При вводе "Псих" - Психопаталогия обыденной жизни, Фрейд Зигмунд, 1904 

Книга свободна


## Проверка просрочки книги
### /people/id

Если человек взял книгу более 10 дней назад и до сих пор не
вернул ее, эта книга на странице этого человека должна
подсвечиваться красным цветом.

• 
При взятии книги человеком, должно сохраняться текущее точное время. 

• 
Вся логика по определению просрочки не должна лежать в контроллерее, или что
еще хуже, в представлении. Вся логика лежит в сервисе, а на представление
отправляется только одно булево значение - просрочена книга (True) или не
просрочена (False).

• 
Подумайте, где хранить и как отправлять булево значение на представление.
Возможно, здесь придется кстати аннотация @Transient?

• 
Изучите атрибут `th:style` в Thymeleaf, который позволяет динамически
изменять стиль на основании условия (понадобится для того, чтобы красить
красным цветом просроченные книги). Можно реализовать и без th:style,
любой способ подойдет.

• 
Для того, чтобы проверить, что все работает, вручную положите в таблицу
значение времени, которое было раньше, чем 10 дней назад.

• 
Изучите, как в PostgreSQL класть в таблицу значение в колонку типа `timestamp`
