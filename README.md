# Приложение Flight Service

Это Java-приложение позволяющее фильтровать полеты на основании различных критериев. Ниже приведено описание методов и их функциональности.

## Методы

### `filterFlightsByAllRules`

Этот метод фильтрует полеты на основе нескольких правил, включая время отправления, время прибытия, время транзитных рейсов и длительность полета.

### `checkDepartureTimeLaterOrBefore`

Этот метод фильтрует полеты на основе времени отправления, позволяя фильтровать полеты, отправляющиеся после или до указанной даты.

### `checkArrivalTimeLaterDepartureTime`

Этот метод фильтрует полеты, где время прибытия на всех сегментах идет после времени отправления.

### `checkTransitFlightTimeForSumHours`

Этот метод фильтрует полеты на основе времени транзита или длительности полета в зависимости от типов состояний полета и указанного количества часов.

### Другие вспомогательные методы

- `removeFlightsDepartureTimeLaterArrivalTime`: Удаляет полеты, где время отправления позже времени прибытия на хотя бы одном сегменте.
- `checkTransitTimeForSumHours`: Фильтрует полеты на основе времени транзита для указанного количества часов.
- `hasSufficientSegments`: Проверяет, достаточно ли сегментов в полете для фильтрации на основе времени транзита.
- `calculateTotalTransitTime`: Вычисляет общее время транзита для списка сегментов.
- `checkFlightTimeForSumHours`: Фильтрует полеты на основе длительности полета для указанного количества часов.
- `calculateTotalFlightTime`: Вычисляет общую длительность полета для списка сегментов.

## Использование

Вы можете использовать класс `FlightServiceImp` для фильтрации списка полетов в соответствии с вашими требованиями.

## Примечание
Работоспособность приложения протестирована, тесты расположены в папке [test](./src/test/java/com/gridnine/testing/flightcontrol).

Приложение подготовлено в соответствии с [техническим заданием](./test-instructions.odt).

Дополнительные [ответы](./questions.odt) на вопросы .
