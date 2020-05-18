from container import Container


def next_fit(weights, num_comparisons=0):
    containers = [Container()]
    current_container = containers[0]
    for w in weights:
        num_comparisons += 1
        if not current_container.put(w):
            current_container = Container()
            current_container.put(w)
            containers.append(current_container)

    return len(containers), num_comparisons


def first_fit(weights, num_comparisons=0):
    containers = [Container()]
    current_container = containers[0]
    was_found = False
    for w in weights:
        num_comparisons += 1
        if not current_container.put(w):
            for c in containers:
                num_comparisons += 1
                if c.put(w):
                    was_found = True
                    break
            num_comparisons += 1
            if not was_found:
                current_container = Container()
                current_container.put(w)
                containers.append(current_container)
            was_found = False

    return len(containers), num_comparisons


def worst_fit(weights, num_comparisons=0):
    containers = [Container()]
    current_container = containers[0]
    for w in weights:
        num_comparisons += 1
        if not current_container.put(w):
            num_comparisons += 1
            num_comparisons += len(containers)  # max
            if not max(containers).put(w):
                current_container = Container()
                current_container.put(w)
                containers.append(current_container)

    return len(containers), num_comparisons


def best_fit(weights, num_comparisons=0):
    containers = [Container()]
    current_container = containers[0]
    for w in weights:
        num_comparisons += 1
        if not current_container.put(w):
            num_comparisons += len(containers)  # filtering
            num_comparisons += len(containers)  # sorting, but in fact getting min, so len
            best_fit_containers = list(sorted(filter(lambda v: v.size >= w, containers)))
            num_comparisons += 1
            if best_fit_containers:
                best_fit_containers[0].put(w)
            else:
                current_container = Container()
                current_container.put(w)
                containers.append(current_container)

    return len(containers), num_comparisons


def next_fit_sorted_asc(weights, num_comparisons=0):
    w = weights.copy()
    quick_sort(w, 0, len(w) - 1, num_comparisons)
    return next_fit(w, num_comparisons)


def first_fit_sorted_asc(weights, num_comparisons=0):
    w = weights.copy()
    quick_sort(w, 0, len(w) - 1, num_comparisons)
    return first_fit(w, num_comparisons)


def worst_fit_sorted_asc(weights, num_comparisons=0):
    w = weights.copy()
    quick_sort(w, 0, len(w) - 1, num_comparisons)
    return worst_fit(w, num_comparisons)


def best_fit_sorted_asc(weights, num_comparisons=0):
    w = weights.copy()
    quick_sort(w, 0, len(w) - 1, num_comparisons)
    return best_fit(w, num_comparisons)


def next_fit_sorted_desc(weights, num_comparisons=0):
    w = weights.copy()
    quick_sort(w, 0, len(w) - 1, num_comparisons)
    w.reverse()
    return next_fit(w, num_comparisons)


def first_fit_sorted_desc(weights, num_comparisons=0):
    w = weights.copy()
    quick_sort(w, 0, len(w) - 1, num_comparisons)
    w.reverse()
    return first_fit(w, num_comparisons)


def worst_fit_sorted_desc(weights, num_comparisons=0):
    w = weights.copy()
    quick_sort(w, 0, len(w) - 1, num_comparisons)
    w.reverse()
    return worst_fit(w, num_comparisons)


def best_fit_sorted_desc(weights, num_comparisons=0):
    w = weights.copy()
    quick_sort(w, 0, len(w) - 1, num_comparisons)
    w.reverse()
    return best_fit(w, num_comparisons)


def partition(arr, low, high, num_comparisons):
    i = (low - 1)
    pivot = arr[high]

    for j in range(low, high):
        num_comparisons += 1
        if arr[j] <= pivot:
            i = i + 1
            arr[i], arr[j] = arr[j], arr[i]

    arr[i + 1], arr[high] = arr[high], arr[i + 1]
    return i + 1


def quick_sort(arr, low, high, num_comparisons):
    num_comparisons += 1
    if low < high:
        pi = partition(arr, low, high, num_comparisons)

        quick_sort(arr, low, pi - 1, num_comparisons)
        quick_sort(arr, pi + 1, high, num_comparisons)
