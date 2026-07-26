# Installation

## 1. Prerequisites

- Clojure (with tools.deps). Install via the [official Clojure CLI guide](https://clojure.org/guides/install_clojure).
- SQLite3 (the project uses a local SQLite database file).

## 2. Getting the Code

Clone the repository:

```bash
git clone <repository-url>
cd bookshelf
```

Alternatively, download and extract the source archive.

## 3. Install Dependencies

Run the provided install script, which only fetches dependencies and exits:

```bash
./install.sh
```

The script executes `clojure -P` under the hood. You may also run that command directly.

## 4. Environment Configuration

The application respects the following environment variables (all optional):

- `FONTS_DIR` – Path to a directory containing font files.
- `FONT_CSS_URL` – URL to a CSS file for fonts.

Set them if needed (e.g., `export FONTS_DIR=/path/to/fonts`). There is no `.env.example`; adjust as required.

## 5. Running the Development Server

Start the application with:

```bash
clojure -M:run
```

The server binds to `0.0.0.0:8080` and serves the bookshelf web interface.

No test command is provided for this project.

## 6. Building for Production

To create a production build, run:

```bash
clojure -T:build
```

Consult the build tooling documentation for output details.

## 7. Troubleshooting

- **`clojure` command not found** – Ensure Clojure CLI is installed and available in your `PATH`.
- **Port already in use** – Stop any process using port 8080 or configure a different port (modify `src/bookshelf/core.clj` if needed).
- **SQLite errors** – Verify the `bookshelf.db` file exists and is writable. The application creates or seeds the database automatically on first run.