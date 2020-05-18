class Container:
    size = 100

    def put(self, weight):
        if self.size - weight >= 0:
            self.size -= weight
            return True
        else:
            return False

    def __eq__(self, other):
        return other.size == self.size

    def __lt__(self, other):
        return self.size < other.size

    def __str__(self):
        return "[" + str(self.size) + "]"

    def __repr__(self):
        return "[" + str(self.size) + "]"
