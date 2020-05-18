import json
import sys

input_file = open(sys.argv[1], "r")

output_files = {}

while True:
    line = input_file.readline()

    if not line:
        break

    json_line = json.loads(line)
    category = json_line["category"]
    headline = json_line["headline"]

    if category not in output_files:
        output_files[category] = open("topics_data/" + category + ".txt", "w", encoding="utf-8")

    output_files[category].write(headline + "\n")

for f in output_files.values():
    f.close()

input_file.close()
