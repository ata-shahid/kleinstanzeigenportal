import csv

resource_dir = "src/main/resources/"

with open("uebersetzungen.csv", "r", encoding="utf-8") as csvfile:
    reader = csv.reader(csvfile, delimiter=";")
    header = next(reader)
    data = list(reader)

default_language = header[1]
translations = {lang: [] for lang in header[1:]}

for row in data:
    if not row or len(row) < len(header):
        continue
    key = row[0]
    if key.startswith("#"):
        continue
    for i, lang in enumerate(header[1:], start=1):
        translations[lang].append(f"{key}={row[i]}")

for lang, lines in translations.items():
    content = "\n".join(lines)

    # Every language gets its own messages_xx.properties
    with open(f"{resource_dir}messages_{lang}.properties", "w", encoding="utf-8") as file:
        file.write(content)

    # The default language(fallback)
    if lang == default_language:
        with open(f"{resource_dir}messages.properties", "w", encoding="utf-8") as file:
            file.write(content)
