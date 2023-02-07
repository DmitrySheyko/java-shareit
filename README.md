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

### Запуск программы:
- сборака: _mvn clean package_;
- запуск через Docker-compose: _docker-compose up_.

Автор: Dmitry Sheyko.
