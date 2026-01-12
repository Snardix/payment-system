import csv
import uuid

def generate_csv(filename: str, count: int):
    with open(filename, mode='w', newline='') as f:
        writer = csv.writer(f)
        writer.writerow(['key'])  # заголовок
        for _ in range(count):
            writer.writerow([str(uuid.uuid4())])

if __name__ == "__main__":
    generate_csv("idempotency_keys.csv", 25000)  # создаём 10000 уникальных ключей
