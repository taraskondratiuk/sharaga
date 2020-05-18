from statistics import mode

candidates_names = ["a", "b", "c", "d"]

voting_results = [
    (9, [candidates_names[2], candidates_names[3], candidates_names[1], candidates_names[0]]),
    (6, [candidates_names[0], candidates_names[3], candidates_names[1], candidates_names[2]]),
    (5, [candidates_names[1], candidates_names[2], candidates_names[3], candidates_names[0]]),
    (2, [candidates_names[0], candidates_names[3], candidates_names[2], candidates_names[1]])
]


def relative_majority(results, candidates):
    results_map = {el: 0 for el in candidates}
    for r in results:
        results_map[r[1][0]] += r[0]

    return max(results_map, key=results_map.get)


def two_tours_relative_majority(results, candidates):
    results_map = {el: 0 for el in candidates}
    total_votes = 0
    for r in results:
        total_votes += r[0]
        results_map[r[1][0]] += r[0]

    majority_votes = (total_votes // 2) + 1
    max_first_tour = max(results_map.values())

    if majority_votes <= max_first_tour:
        return max(results_map, key=results_map.get)
    else:
        return 'need second tour'


def borda(results, candidates):
    results_map = {el: 0 for el in candidates}
    num_candidates = len(candidates)
    for r in results:
        for i in range(num_candidates):
            results_map[r[1][i]] += r[0] * (num_candidates - i)

    return max(results_map, key=results_map.get)


def copland(results, candidates):
    candidates_pairs = []
    for c1 in candidates:
        for c2 in candidates:
            if c1 < c2:
                candidates_pairs.append((c1, c2))

    results_map = {el: 0 for el in candidates}

    for c1, c2 in candidates_pairs:
        c1_score = 0
        c2_score = 0
        for num_votes, preference_list in results:
            if preference_list.index(c1) < preference_list.index(c2):
                c1_score += num_votes
            else:
                c2_score += num_votes

        if c1_score > c2_score:
            results_map[c1] += 1
            results_map[c2] -= 1
        else:
            results_map[c1] -= 1
            results_map[c2] += 1

    return max(results_map, key=results_map.get)


def condorcet(results, candidates):
    candidates_pairs = []
    for c1 in candidates:
        for c2 in candidates:
            if c1 < c2:
                candidates_pairs.append((c1, c2))

    pair_winners = []
    for c1, c2 in candidates_pairs:
        c1_score = 0
        c2_score = 0
        for num_votes, preference_list in results:
            if preference_list.index(c1) < preference_list.index(c2):
                c1_score += num_votes
            else:
                c2_score += num_votes

        if c1_score > c2_score:
            pair_winners.append(c1)
        else:
            pair_winners.append(c2)

    return mode(pair_winners)


def simpson(results, candidates):
    candidates_pairs = []

    for c1 in candidates:
        for c2 in candidates:
            if c1 < c2:
                candidates_pairs.append((c1, c2))

    results_map = {el: 0 for el in candidates}
    for c1, c2 in candidates_pairs:
        c1_score = 0
        c2_score = 0
        for num_votes, preference_list in results:
            if preference_list.index(c1) < preference_list.index(c2):
                c1_score += num_votes
            else:
                c2_score += num_votes

        if results_map[c1] == 0 or results_map[c1] > c1_score:
            results_map[c1] = c1_score
        if results_map[c2] == 0 or results_map[c2] > c2_score:
            results_map[c2] = c2_score

    return max(results_map, key=results_map.get)


def instant_runoff(results, candidates):
    copied_results = results.copy()
    copied_candidates = candidates.copy()
    half_votes = sum(i for i, _ in copied_results) / 2

    results_map = {el: 0 for el in copied_candidates}
    for r in results:
        results_map[r[1][0]] += r[0]
    if max(results_map.values()) > half_votes:
        return max(results_map, key=results_map.get)
    else:
        min_votes_candidate = min(results_map, key=results_map.get)
        copied_candidates.remove(min_votes_candidate)
        for _, lst in copied_results:
            lst.remove(min_votes_candidate)
        return instant_runoff(copied_results, copied_candidates)


print("voting results: ")
print(voting_results)
print("\nrelative majority winner: " + relative_majority(voting_results, candidates_names))
print("\nborda winner: " + borda(voting_results, candidates_names))
print("\ncondorcet winner: " + condorcet(voting_results, candidates_names))
print("\ncopland winner: " + copland(voting_results, candidates_names))
print("\nsimpson winner: " + simpson(voting_results, candidates_names))
print("\ninstant runoff winner: " + instant_runoff(voting_results, candidates_names))
