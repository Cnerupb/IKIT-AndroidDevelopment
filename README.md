# Книга Рецептов

Android-приложение для хранения и просмотра кулинарных рецептов с погодой и картой офисов.

---

## Инструкция по сборке APK средствами CLI

### Предварительные требования

| Инструмент | Версия |
|---|---|
| JDK | 11 или выше |
| Android SDK | API 36 |

Убедитесь, что переменная окружения `ANDROID_HOME` указывает на папку Android SDK, либо что в файле `local.properties` прописан путь:

```
sdk.dir=C\:\\Users\\<username>\\AppData\\Local\\Android\\Sdk
```

---

### Подготовка

Проект требует API-ключ Яндекс Карт. Без него не будет грузиться карта в одном из экранов. В корне проекта в файле `local.properties` должна быть строка:

```
MAPKIT_API_KEY=ваш_ключ
```

---

### Сборка debug-APK

Откройте терминал в корне проекта и выполните:

```bash
./gradlew assembleDebug
```

На Windows:

```bat
gradlew.bat assembleDebug
```

Готовый файл будет по пути:

```
app/build/outputs/apk/debug/app-debug.apk
```

---

### Сборка release-APK

Откройте терминал в корне проекта и выполните:

```bash
./gradlew assembleRelease
```

Готовый файл будет по пути:

```
app/build/outputs/apk/release/app-release-unsigned.apk
```

> Release-сборка без подписи не может быть установлена на устройство. Для подписи требуется keystore и дополнительная конфигурация в `build.gradle.kts`.

---

### Полезные команды

```bash
# Очистить артефакты предыдущей сборки
./gradlew clean

# Очистить и собрать заново
./gradlew clean assembleDebug
```

---

### Установка на устройство (опционально)

При подключённом устройстве с включённой отладкой по USB:

```bash
./gradlew installDebug
```
