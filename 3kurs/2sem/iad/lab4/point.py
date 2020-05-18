from statistics import median


class Point:
    def __init__(self, word, position):
        self.words = [word]
        self.p1 = [position]
        self.p2 = len(word)
        self.counter = 1

    def __str__(self):
        return str(self.words)

    def __repr__(self):
        return str(self.words)

    def __hash__(self):
        return hash(self.words[0])

    def __eq__(self, other):
        return self.words[0] == other

    def __lt__(self, other):
        return self.words[0] < other.words[0]

    def update(self, other):
        self.counter += 1
        self.p1.append(other.p1[0])

    def is_one(self):
        return self.counter == 1

    def merge(self, other):
        self.words += other.words
        self.p1 = (self.p1 + other.p1) / 2
        self.p2 = (self.p2 + other.p2) / 2

    def count_median_for_p1(self):
        self.p1 = float(median(self.p1))
