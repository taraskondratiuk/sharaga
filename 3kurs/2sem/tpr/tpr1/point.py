class Point:
    def __init__(self, q1, q2, index):
        self.q1 = q1
        self.q2 = q2
        self.index = index

    def __hash__(self):
        return 10 * self.q1 + self.q2

    def dominant_slater(self, other):
        return (
            self.__class__ == other.__class__ and
            self.q1 > other.q1 and
            self.q2 > other.q2

        )

    def dominant_pareto(self, other):
        return (
            self.__class__ == other.__class__ and (
                (self.q1 >= other.q1 and self.q2 > other.q2) or
                (self.q1 > other.q1 and self.q2 >= other.q2)
            )
        )

    def __eq__(self, other):
        return (
                self.__class__ == other.__class__ and
                self.q1 == other.q1 and
                self.q2 == other.q2 and
                self.index == other.index
        )

    def __str__(self):
        return "[" + str(self.index) + "]: {" + str(self.q1) + ", " + str(self.q2) + "}"
