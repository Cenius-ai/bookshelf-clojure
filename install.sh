#!/usr/bin/env bash
# ---------------------------------------------------------------------------
# Bookshelf — install script
# Fetches Clojure dependencies + self-hosts Google Fonts. Exits when done.
# ---------------------------------------------------------------------------
set -euo pipefail
cd "$(dirname "$0")"

echo "==> Fetching Clojure dependencies …"
clojure -P

echo "==> Downloading web fonts (Outfit + Inter) …"
FONTS_DIR="resources/public/fonts"
mkdir -p "$FONTS_DIR"

FONT_CSS_URL="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600&family=Outfit:wght@400;500;600;700&display=swap"

python3 -c "
import urllib.request, re, os, sys

css_url = '${FONT_CSS_URL}'
fonts_dir = '${FONTS_DIR}'

try:
    req = urllib.request.Request(css_url, headers={'User-Agent': 'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36'})
    css = urllib.request.urlopen(req, timeout=15).read().decode('utf-8')
except Exception as e:
    print(f'WARNING: could not fetch Google Fonts CSS: {e}', file=sys.stderr)
    with open(os.path.join(fonts_dir, 'fonts.css'), 'w') as f:
        f.write('/* Fonts unavailable — using system fallbacks */')
    sys.exit(0)

urls = list(dict.fromkeys(re.findall(r'url\((https://[^)]+\.(?:woff2|ttf))\)', css)))
if not urls:
    print('WARNING: no font URLs in CSS — using system fonts', file=sys.stderr)
    with open(os.path.join(fonts_dir, 'fonts.css'), 'w') as f:
        f.write('/* No fonts downloaded — using system fallbacks */')
    sys.exit(0)

for i, url in enumerate(urls):
    ext = url.rsplit('.', 1)[-1].split('?')[0]
    local = f'font-{i}.{ext}'
    print(f'  -> {local}')
    try:
        req2 = urllib.request.Request(url, headers={'User-Agent': 'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36'})
        with open(os.path.join(fonts_dir, local), 'wb') as f:
            f.write(urllib.request.urlopen(req2, timeout=15).read())
    except Exception as e:
        print(f'  WARNING: failed to download {local}: {e}', file=sys.stderr)
    css = css.replace(url, local)

with open(os.path.join(fonts_dir, 'fonts.css'), 'w') as f:
    f.write(css)
print('  -> wrote fonts.css')
" 2>&1 || echo "  Font download had issues — system fonts will be used (app still works)."

echo ""
echo "Install complete."
echo "  Seed the database:  clojure -M:seed"
echo "  Start the server:   clojure -M:run"
