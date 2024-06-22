# Video Paylaşım Sitesi Backend

Bu proje, bir video paylaşım sitesi için backend API sağlar. Kullanıcılar video yükleyebilir, videoları izleyebilir, yorum yapabilir ve beğenebilir. Proje Spring Boot ve Hibernate kullanılarak geliştirilmiştir.

## İçindekiler

1. [Özellikler](#özellikler)
2. [Teknolojiler](#teknolojiler)
3. [Gereksinimler](#gereksinimler)
4. [Kurulum](#kurulum)
5. [API Kullanımı](#api-kullanımı)
6. [Diğer](#diğer)
7. [İletişim](#i̇letişim)
8. [Katkıda Bulunanlar](#katkıda-bulunanlar)

## Özellikler

- Kullanıcı kaydı ve girişi
- Admin kullanıcı tarafından video yükleme
- Yorum yapma ve yorumları beğenme
- Videoları beğenme
- Playlist oluşturma video ekleme
- Kategorilere göre video arama ve filtreleme

## Teknolojiler

- Java
- Spring Boot
- MySQL
- Redis
- Docker

## Gereksinimler

- Java 21
- Maven
- Docker

## Kurulum

Projeyi yerel ortamınızda çalıştırmak için aşağıdaki adımları izleyin:

1. Bu repoyu klonlayın:

    ```bash
    git clone https://github.com/SelahattinGngr/video-sharing-site
    ```

2. Proje dizinine gidin:

    ```bash
    cd video-sharing-site
    ```

3. Docker Compose ile gerekli hizmetleri başlatın:

    ```bash
    docker-compose up -d
    ```

    Bu adım, MySQL, Redis ve phpMyAdmin hizmetlerini başlatacaktır.
4. Gerekli bağımlılıkları yükleyin:

    ```bash
    mvn install
    ```

5. Uygulamayı başlatın:

    ```bash
    mvn spring-boot:run
    ```

Uygulama varsayılan olarak `http://localhost:5353` adresinde çalışacaktır.

## API Kullanımı

### Kullanıcı Auth İşlemleri

- **POST /auth/signup**: Kullanıcı kayıt
- **POST /auth/signin**: Kullanıcı giriş
- **POST /auth/signout**: Kullanıcı çıkış
- **POST /auth/refresh-token**: Token yenileme

### Kategori İşlemleri

- **POST /categories**: Yeni kategori oluştur
- **GET /categories**: Tüm kategorileri getir

### Takip İşlemleri

- **GET /followers/follow/{username}**: Takip et
- **GET /followers/following/{username}**: Takip eden kullanıcılar
- **GET /followers/unfollow/{username}**: Takipten çık

### Oynatma Listesi İşlemleri

- **POST /playlists**: Oynatma listesi oluştur
- **POST /playlists/add-video/{id}**: Videoyu oynatma listesine ekle
- **GET /playlists/public**: Tüm herkese açık oynatma listeleri
- **GET /playlists/user-playlists**: Kullanıcının oynatma listeleri
- **GET /playlists/{id}**: Oynatma listesi ID'ye göre
- **GET /playlists/follow/{id}**: Oynatma listesini takip et
- **PUT /playlists/{id}**: Oynatma listesini güncelle

### Kullanıcı İşlemleri

- **GET /users/{username}**: Kullanıcı bilgisi getir
- **GET /users**: Tüm kullanıcıları getir

### Video Yorumları

- **POST /video-comments/{videoId}**: Videoya yorum ekle
- **GET /video-comments/{videoId}**: Videoya ait yorumları getir
- **GET /video-comments/like/{commentId}**: Yorumu beğen
- **GET /video-comments/likes/{commentId}**: Yorumu beğenen kullanıcılar
- **PUT /video-comments/{commentId}**: Yorumu güncelle
- **DELETE /video-comments/{commentId}**: Yorumu sil

### Video İşlemleri

- **POST /videos**: Yeni video yükle
- **GET /videos?page={page}&size={size}**: Tüm videoları getir
- **GET /videos/{id}**: Video ID'ye göre getir
- **GET /videos/like/{id}**: Videoyu beğen
- **PUT /videos/{id}**: Videoyu güncelle

## Diğer

- [HELP.md](/HELP.md)
- [CHANGELOG.md](/CHANGELOG.md)
- [LICENSE](/LICENSE)

## İletişim

Herhangi bir soru veya geri bildiriminiz için lütfen bizimle iletişime geçin:

- E-posta: <selahattin_gungor53@hotmail.com>
- GitHub Issues: [https://github.com/SelahattinGngr/video-sharing-site/issues](https://github.com/SelahattinGngr/video-sharing-site/issues)

## Katkıda Bulunanlar

### Proje Sahibi

- [**SELAHATTİN GÜNGÖR**](https://github.com/SelahattinGngr)
