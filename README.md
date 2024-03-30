## Библиотека
### 3-й этап задания
Внесены изменения в описание сайта из этапа 1, уточняющие описание сайта:  
<details>
<summary>Изменения описания сайта</summary>  

- переход на "Выдать книгу" со страницы "Книги" удалён, аналогичный переход введёт со страницы "Список экземпляров книги"  

- в описание страницы "Читатели" добавлена возможность отфильтровать  

- в описание страницы "Читатель" добавлена возможность перейти на страницу экземпляра книги или на страницу книг с фильтром по названию  

- в описание страницы "Книги" добавлена возможность фильтрации по ISBN и удаления книги  

- в описание страницы "Список экземпляров книги" добавлена возможность перехода на страницу выдачи некого экземпляра или возврата его в библиотеку, а также на страницу книг с фильтром по тому же автору или издательству  

- в описание страницы "Экземпляр книги" добавлена возможность вернуть экземпляр в библиотеку и перейти на страницу читателя, книги или на страницу книг с фильтром по тому же автору или издательству  

- в описание страницы "История" добавлена возможность переходить по ссылкам из таблицы на страницы читателя и экземпляра книги, а также на страницу книг с фильтром по названию и читателей с фильтром по имени  

</details>
Добавлен пакет `lib`, содержащий вспомогательные классы `Event` и `BookAndStatus`, позволяющие более удобно отображать на страницах данные из БД.
  
Также добавлены вспомогательные функции в `IssueDAO.Filter`.  
  
Реализованы контроллеры, они лежат в пакете `lib`. В отдельном классе - контроллеры для отдельных страниц сайта + в `IssueController` есть обработчик запроса на возврат книги, а в `IndexController` - обработчик запросов к CSS. Общая логика в том, что запросы на получение страницы оформляются в виде GET-запросов, в параметрах URI могут указываться параметры фильтрации; запросы на взаимодействие с БД оформляются в виде POST-запросов, которые обычно возвращают пользователя на ту же страницу, с которой он запрос и отправил.  

Страницы созданы при помощи механизма шаблонов Thymeleaf, он позволяет подставлять переменные из Model, переданные Java-кодом, и сохранять куски кода в шаблоны для повторного использования (так, например, шапка на всех страницах одинаковая, но написана один раз в `index.html`).  
  
Страницы свёрстаны с использованием коллекции CSS от bootstrap, однако фактически оттуда взяты только классы для таблиц, остальные классы написаны вручную (они хранятся в файле `libraryStyles.css`.  
  
Шаблоны html-страниц находятся в папке `resources/templates`, таблицы стилей - в папке `resources/static/styles`.  
  
Для подключения к сайту нужно, запустив сервер, ввести в адресной строке `localhost:8080/`.
### 2-й этап задания
Реализованы классы данных, DAO-интерфейсы и реализации, а также тесты.  
Использована библиотека Lombok для автогенерации геттеров и сеттеров.  
Тесты реализованы через JUnit.  
Весь отчёт с 1-го этапа задания (в т. ч. схема БД и сайта) лежат в папке docs.  

Связывание объектов с БД происходит с помощью аннотаций, настройки подключения к БД описаны в файле ``main/resources/application.properties`` (для тестов - ``test/resources/application.properties``; там указана тестовая БД, но она полностью аналогична обычной, и тесты её не портят, можно смело менять ``library_test`` на ``library``) (тоже подключаются при помощи аннотаций).  

В некоторых DAO-интерфейсах описаны вложенные классы Filter, через которые задаются параметры для отбора объектов в запросе. Покрытие тестами - 92% методов и 98% строк (на самом деле, не покрыты только автоматически созданные Lombok'ом Setter'ы и NoArgsConstructor'ы для Filter'ов, все методы получения объектов покрыты полностью). Покрытие измеряется в Idea.  
![Покрытие](/docs/test_coverage.png)