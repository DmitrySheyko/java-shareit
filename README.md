# java-shareit
Микросервисное приложение для шеринга вещей. 

### Программа состоит из 3 частей: 
- Gateway для валидации запросов от frontEnd;  
- Server для реализации бизнес логики; 
- Database на основе docker образа PostgreSQL.  

### Основной функционал: 
- создание аккаунтов пользователей;  
- создание карточек вещей для шеринга;
- возможность поиска и бронирования вещей;
- возможность одобрения и отклонения заявок на бронирование;
- возможность создания заявок на вещи, отсутсвующие в базе.

### Стек технологий:
- Java 11;
- Spring boot, Srping web, Spring Data, Spring Validation, Hibernate;
- Lombock;
- PostgresSQL and H2:
- Docker, Docker-compose

### Запуск программы:
- сборака: _mvn clean package_;
- запуск через Docker-compose: _docker-compose up_.

Автор: Dmitry Sheyko.
