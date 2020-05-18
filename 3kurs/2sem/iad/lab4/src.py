import sys
import os
import math
import matplotlib.pyplot as plt
from point import Point

train_dir = sys.argv[1]

files = os.listdir(train_dir)

words = {}

whitelist = set('abcdefghijklmnopqrstuvwxyz ABCDEFGHIJKLMNOPQRSTUVWXYZ')

for f in files:
    file = open(train_dir + f, "r", encoding="utf-8")
    for _ in range(100):
        line = file.readline()

        line = "".join(filter(whitelist.__contains__, line)).lower()

        if not line:
            break

        position = 0
        for word in line.split():
            point = Point(word, position)
            if point not in words:
                words[word] = point
            else:
                words[word].update(point)

            position += 1

words = {k: v for k, v in words.items() if not v.is_one()}

for k in words.keys():
    words[k].count_median_for_p1()

keys = []
values = []

for v in words.values():
    keys.append(v.p1)
    values.append(v.p2)

for _ in range(len(words) - len(files)):
    min_dist = 1000000
    min_tuple1 = None
    min_tuple2 = None
    for k1, v1 in words.items():
        for k2, v2 in words.items():
            dist = math.sqrt((v1.p1 - v2.p1) ** 2 + (v1.p2 - v2.p2) ** 2)
            if v1 < v2 and dist < min_dist:
                min_dist = dist
                min_tuple1 = k1, v1
                min_tuple2 = k2, v2

    del(words[min_tuple1[0]])
    words[min_tuple2[0]].merge(min_tuple1[1])


clusters = {index: value for index, (_, value) in enumerate(words.items())}
print(clusters)

plt.figure()
plt.ylim(0, 15)
plt.xlim(0, 15)
plt.scatter(keys, values, color="red")
plt.title("input points")

plt.figure()
plt.ylim(0, 15)
plt.xlim(0, 15)
plt.scatter(list(map(lambda x: x.p1, clusters.values())), list(map(lambda x: x.p2, clusters.values())), color="red")
plt.title("centroids of the clusters")
plt.show()
