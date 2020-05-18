import sys
import math

from methods import *

files = sys.argv
files.pop(0)

for f in files:
    nums_from_file = [int(line.rstrip('\n')) for line in open(f)]
    # nums_from_file = [94, 59, 58, 52, 51, 51, 42, 25, 23, 20, 18, 12, 7, 4, 3]

    print(f)
    print("min possible containers: " + str(math.ceil(sum(nums_from_file) / 100)))

    nf = next_fit(nums_from_file)
    ff = first_fit(nums_from_file)
    wf = worst_fit(nums_from_file)
    bf = best_fit(nums_from_file)

    nfa = next_fit_sorted_asc(nums_from_file)
    ffa = first_fit_sorted_asc(nums_from_file)
    wfa = worst_fit_sorted_asc(nums_from_file)
    bfa = best_fit_sorted_asc(nums_from_file)

    nfd = next_fit_sorted_desc(nums_from_file)
    ffd = first_fit_sorted_desc(nums_from_file)
    wfd = worst_fit_sorted_desc(nums_from_file)
    bfd = best_fit_sorted_desc(nums_from_file)

    print("\nunsorted")
    print("NFA: " + str(nf[0]) + " containers, " + str(nf[1]) + " comparisons")
    print("FFA: " + str(ff[0]) + " containers, " + str(ff[1]) + " comparisons")
    print("WFA: " + str(wf[0]) + " containers, " + str(wf[1]) + " comparisons")
    print("BFA: " + str(bf[0]) + " containers, " + str(bf[1]) + " comparisons")

    print("\nsorted ascending")
    print("NFA: " + str(nfa[0]) + " containers, " + str(nfa[1]) + " comparisons")
    print("FFA: " + str(ffa[0]) + " containers, " + str(ffa[1]) + " comparisons")
    print("WFA: " + str(wfa[0]) + " containers, " + str(wfa[1]) + " comparisons")
    print("BFA: " + str(bfa[0]) + " containers, " + str(bfa[1]) + " comparisons")

    print("\nsorted descending")
    print("NFA: " + str(nfd[0]) + " containers, " + str(nfd[1]) + " comparisons")
    print("FFA: " + str(ffd[0]) + " containers, " + str(ffd[1]) + " comparisons")
    print("WFA: " + str(wfd[0]) + " containers, " + str(wfd[1]) + " comparisons")
    print("BFA: " + str(bfd[0]) + " containers, " + str(bfd[1]) + " comparisons")
    print()
