import sys
from fractions import Fraction

from item_sets import *

files = sys.argv
files.pop(0)

temps = {}
for line in open("v8_temperatures.txt"):
    (key, val) = line.split()
    temps[int(key)] = int(val)

print("average temperatures for months:")
print(temps)
print()

for f in files:
    probabilities = {}
    for line in open(f):
        (key, val) = line.split()
        probabilities[int(key)] = float(Fraction(val))

    print(f.split(".")[0])	
    best_strategy = get_best_strategy(probabilities, temps, "special" in f)
    print("cheapest items set: " + str(best_strategy[1]) + "\nProfit: " + str(best_strategy[0]) + "\n")
