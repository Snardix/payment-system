import csv

COUNT = 500
OUTPUT_FILE = "users.csv"

with open(OUTPUT_FILE, mode="w", newline="", encoding="utf-8") as file:
    writer = csv.writer(file)
    writer.writerow(["email", "password"])

    for i in range(1, COUNT + 1):
        email = f"user{i}@loadtest.local"
        password = "1234"
        writer.writerow([email, password])

print(f"Created {COUNT} users in {OUTPUT_FILE}")
