# Usage

## 1. Starting the Server

Run the application:

```bash
clojure -M:run
```

Once started, the server listens on `http://localhost:8080`.

## 2. Viewing the Bookshelf

Open a browser and navigate to `http://localhost:8080`. The home page displays a list of all books with their titles and authors. A few sample books are pre-seeded.

Example with `curl`:

```bash
curl http://localhost:8080
```

Returns an HTML document containing the book list.

## 3. Adding a Book

The form on the same page allows adding a new book. Submit a POST request with `title` and `author` fields:

```bash
curl -X POST http://localhost:8080 \
     -d "title=The Hobbit&author=J.R.R. Tolkien"
```

After adding, the book appears on the home page.

## 4. Accessing Static Assets

CSS and font files are served from `resources/public/`:

- CSS: `http://localhost:8080/css/style.css`
- Font files (if `FONTS_DIR` is set): `http://localhost:8080/fonts/...`

A font CSS file is available at `/fonts/fonts.css` when `FONT_CSS_URL` is configured.