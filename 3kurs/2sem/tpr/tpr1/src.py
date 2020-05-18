import matplotlib.pyplot as plt
import sys

from point import Point

files = sys.argv
files.pop(0)

for f in files:
    numsFromFile = [int(line.rstrip('\n')) for line in open(f)]

    points = [Point(num // 10, num % 10, i + 1) for i, num in enumerate(numsFromFile)]
    print('\nfile: ' + f)
    print('\nall points:')

    for p in points:
        print(p)

    pareto = points.copy()
    slater = points.copy()

    for p in points:
        pareto_remove = set()
        slater_remove = set()
        for par in pareto:
            if p.dominant_pareto(par):
                pareto_remove.add(par)
        for sla in slater:
            if p.dominant_slater(sla):
                slater_remove.add(sla)
        pareto = list(filter(lambda v: v not in pareto_remove, pareto))
        slater = list(filter(lambda v: v not in slater_remove, slater))
    print('\noptimal pareto:')

    for p in pareto:
        print(p)

    print('\noptimal slater:')

    for s in slater:
        print(s)

    plt.figure()
    plt.scatter(list(map(lambda v: v.q1, points)), list(map(lambda v: v.q2, points)))
    plt.xlim(0, 10)
    plt.ylim(0, 10)


plt.show()
