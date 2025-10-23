# Course Management Platform

Платформа для управления курсами с функциональностью тестирования (Quiz) на Spring Boot.

## Функциональность

### Основные возможности
- ✅ Регистрация и аутентификация пользователей (JWT)
- ✅ Управление курсами (создание, просмотр, редактирование)
- ✅ Создание и прохождение тестов (Quiz)
- ✅ Разграничение доступа по ролям (STUDENT, TEACHER, ADMIN)
- ✅ Автоматическое оценивание тестов
- ✅ Отслеживание попыток прохождения тестов
- ✅ REST API с документацией Swagger
- ✅ Мониторинг через Spring Actuator

### Роли пользователей
- **STUDENT** - может просматривать курсы и проходить тесты
- **TEACHER** - может создавать курсы и тесты, просматривать результаты студентов
- **ADMIN** - полный доступ ко всем функциям

### Типы вопросов в тестах
- **SINGLE_CHOICE** - выбор одного правильного ответа
- **MULTIPLE_CHOICE** - выбор нескольких правильных ответов
- **TEXT** - текстовый ответ (требует ручной проверки)

## Технологический стек

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security** (JWT аутентификация)
- **Spring Data JPA** (Hibernate)
- **PostgreSQL** (база данных)
- **Flyway** (миграции БД)
- **MapStruct** (маппинг DTO)
- **SpringDoc OpenAPI** (Swagger документация)
- **Spring Actuator** (мониторинг)
- **Lombok** (генерация кода)

## Требования

- Java 17 или выше
- PostgreSQL 12 или выше
- Maven 3.6 или выше

## Настройка и запуск

### 1. Настройка базы данных

Создайте базу данных PostgreSQL:

```sql
CREATE DATABASE course_platform;
CREATE USER course_user WITH PASSWORD 'course_password';
GRANT ALL PRIVILEGES ON DATABASE course_platform TO course_user;
```

### 2. Настройка конфигурации

Обновите файл `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/course_platform
    username: course_user
    password: course_password
```

### 3. Сборка и запуск

```bash
# Сборка проекта
mvn clean compile

# Запуск приложения
mvn spring-boot:run
```

Приложение будет доступно по адресу: http://localhost:8080

### 4. Доступ к документации API

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

### 5. Мониторинг

- **Health Check**: http://localhost:8080/actuator/health
- **Application Info**: http://localhost:8080/actuator/info
- **Metrics**: http://localhost:8080/actuator/metrics

## Тестирование API

### Использование Postman

1. Импортируйте коллекцию `Course_Platform_API.postman_collection.json` в Postman
2. Создайте окружение со следующими переменными:
   - `baseUrl`: http://localhost:8080
3. Выполните запросы в следующем порядке:
   1. Регистрация пользователей (Teacher и Student)
   2. Аутентификация
   3. Создание курса
   4. Создание теста
   5. Прохождение теста

### Тестовые данные

Для тестирования создайте пользователей через API регистрации или используйте готовые:
- **admin@platform.edu** / **password** (ADMIN)
- **teacher@platform.edu** / **password** (TEACHER)  
- **student@platform.edu** / **password** (STUDENT)

**Примечание:** Тестовые пользователи создаются автоматически при первом запуске через API регистрацию.

## Структура проекта

```
src/main/java/edu/platform/
├── config/           # Конфигурация (Security, OpenAPI)
├── controller/       # REST контроллеры
├── dto/             # DTO классы (request/response)
├── entity/          # JPA сущности
├── exception/       # Исключения и обработчики
├── mapper/          # MapStruct мапперы
├── repository/      # JPA репозитории
├── security/        # Компоненты безопасности
├── service/         # Бизнес-логика
└── util/            # Утилиты (JWT)
```

## Основные эндпоинты API

### Аутентификация
- `POST /api/auth/register` - Регистрация пользователя
- `POST /api/auth/login` - Вход в систему

### Пользователи
- `GET /api/users/me` - Информация о текущем пользователе
- `GET /api/users/{id}` - Информация о пользователе по ID

### Курсы
- `POST /api/courses` - Создание курса (TEACHER, ADMIN)
- `GET /api/courses` - Список всех курсов
- `GET /api/courses/{id}` - Детали курса
- `PUT /api/courses/{id}` - Обновление курса (владелец)
- `DELETE /api/courses/{id}` - Удаление курса (владелец)
- `GET /api/courses/my` - Мои курсы (TEACHER)

### Тесты
- `POST /api/quizzes` - Создание теста (TEACHER, ADMIN)
- `GET /api/quizzes/course/{courseId}` - Тесты курса
- `GET /api/quizzes/{id}` - Детали теста
- `POST /api/quizzes/{id}/start` - Начать прохождение теста (STUDENT)
- `POST /api/quizzes/submit` - Отправить ответы (STUDENT)
- `GET /api/quizzes/attempts/my` - Мои попытки (STUDENT)
- `GET /api/quizzes/{id}/attempts` - Попытки теста (TEACHER)

## Особенности реализации

### Транзакции
- Используются различные уровни изоляции (`READ_COMMITTED`, `REPEATABLE_READ`, `SERIALIZABLE`)
- Демонстрируется работа с `Propagation.REQUIRED`, `Propagation.REQUIRES_NEW`
- Оптимистические блокировки через `@Version` в сущности Course
- Пессимистические блокировки через `@Lock` в репозиториях

### Безопасность
- JWT токены для аутентификации
- Разграничение доступа по ролям через `@PreAuthorize`
- Защита от CSRF атак
- CORS конфигурация

### Валидация
- Валидация входных данных через Bean Validation
- Глобальная обработка ошибок через `@ControllerAdvice`
- Детальные сообщения об ошибках валидации

### Логирование
- Структурированное логирование на уровнях INFO и DEBUG
- Отдельная настройка для пакета приложения
- Логирование SQL запросов в режиме DEBUG

## Развитие проекта

Возможные улучшения:
- Добавление файловых вложений к курсам
- Система уведомлений
- Расширенная аналитика и отчеты
- Интеграция с внешними LMS системами
- Кэширование часто используемых данных
- Поддержка мультимедиа контента
## Дополнения (Лабораторная №2)

### Модульный монолит
- Организация по пакетам верхнего уровня: `edu.platform.media`, `edu.platform.entity` (включая `Lesson`, `MediaAsset`), `service`, `controller`.
- Взаимодействие модулей через интерфейсы: `FileStorage`, `ResultEvaluationService`.

### Новые сущности
- `Lesson` — элементы курса с полями: id, courseId, title, content, orderIndex, createdAt, updatedAt, `@Version`.
- `MediaAsset` — медиафайлы: id, ownerId, fileName, filePath, fileSize, contentType, checksum, createdAt; связи с `User` и опционально `Course`.
- В `QuizAttempt` добавлены поля `status` (PASSED/FAILED) и `duration_seconds`.

### Миграции БД (Flyway)
- `V2__create_lessons.sql` — таблица `lessons` с оптимистической блокировкой.
- `V3__create_media_assets.sql` — таблица `media_assets`, уникальный индекс по `checksum`.
- `V4__alter_quiz_attempts_add_result.sql` — добавлены `status` и `duration_seconds` в `quiz_attempts`.

### Рабочая с файловой системой (media)
- Интерфейс `FileStorage` и реализация `LocalFileStorage` (детерминированные имена по SHA-256, хранение на локальном диске).
- Декоратор `ResilientFileStorage`: Timeout + Retry + Circuit Breaker для операций save/load/delete.
- `MediaService`: валидация (PNG/JPG/WEBP, до 5 МБ), сохранение файла, запись метаданных в `media_assets`.

### Надёжность (Resilience4j)
- Файловые операции: TimeLimiter (3s), Retry (3 попытки), CircuitBreaker.
- Вычисление результата квиза: `ResultEvaluationService` с TimeLimiter + CircuitBreaker и безопасным fallback → FAILED.

### REST API: Lessons и Media
- Lessons: `POST /api/lessons` (TEACHER/ADMIN), `GET /api/lessons/{id}`, `PUT /api/lessons/{id}`.
- Media: `POST /api/media` (multipart/form-data: поле `file`, опционально `courseId`), `GET /api/media/{id}`, `GET /api/media/{id}/download`.

### Конфигурация
- `application.yml`: лимиты multipart, `media.storage.base-dir`, `media.storage.allowed-content-types`.
- Resilience4j: при желании можно перевести конфигурацию на `CircuitBreakerRegistry/RetryRegistry/TimeLimiterRegistry` и аннотации.

### Примеры запросов (PowerShell)
```powershell
# Регистрация и логин
$reg = @{ email='user1@example.com'; password='secret123'; fullName='User One'; role='TEACHER' } | ConvertTo-Json
Invoke-RestMethod -Method Post -Uri http://localhost:8080/api/auth/register -ContentType 'application/json' -Body $reg
$login = @{ email='user1@example.com'; password='secret123' } | ConvertTo-Json
$resp = Invoke-RestMethod -Method Post -Uri http://localhost:8080/api/auth/login -ContentType 'application/json' -Body $login
$token = $resp.accessToken

# Создать урок (TEACHER)
$lesson = @{ courseId=1; title='Intro'; content='...' ; orderIndex=1 } | ConvertTo-Json
Invoke-RestMethod -Method Post -Uri http://localhost:8080/api/lessons -Headers @{ Authorization = "Bearer $token" } -ContentType 'application/json' -Body $lesson

# Загрузка изображения (multipart)
Invoke-RestMethod -Method Post -Uri http://localhost:8080/api/media -Headers @{ Authorization = "Bearer $token" } -Form @{ file = Get-Item .\image.png }
```

### Swagger и порт
- Если приложение запущено на другом порту (например, 8081), открывайте Swagger по фактическому адресу (например, `http://localhost:8081/swagger-ui.html`).
- В конфигурации OpenAPI можно убрать жёстко заданный сервер `http://localhost:8080` и указать относительный `url: /`, чтобы Swagger всегда использовал текущий хост/порт.
