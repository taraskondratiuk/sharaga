import functools
import operator


class Item:
    def __init__(self, name, weight, price):
        self.name = name
        self.price = price
        self.weight = weight

    def __repr__(self):
        return "[" + self.name + "]"

    def __str__(self):
        return "[" + self.name + "]"


items = {
    "blazer": Item("blazer", 0.5, 6),
    "bushlat": Item("bushlat", 4, 48),
    "vata": Item("vata", 2, 24),
    "vietnamky": Item("vietnamky", 0.5, 6),
    "jynsy": Item("jynsy", 1, 12),
    "kepka": Item("kepka", 0.5, 6),
    "krossy": Item("krossy", 1, 12),
    "kurtka": Item("kurtka", 2, 24),
    "palto": Item("palto", 3, 36),
    "rukavychky": Item("rukavychky", 0.5, 6),
    "svetr": Item("svetr", 1, 12),
    "sorochka": Item("sorochka", 0.5, 6),
    "futbolka": Item("futbolka", 0.5, 6),
    "cherevyky": Item("cherevyky", 1.5, 18),
    "choboty": Item("choboty", 2, 24),
    "shapka": Item("shapka", 1, 12),
    "shorty": Item("shorty", 0.5, 6)
}

special_items = {
    "blazer": Item("blazer", 0.5, 6),
    "bushlat": Item("bushlat", 4, 16),
    "vata": Item("vata", 2, 24),
    "vietnamky": Item("vietnamky", 0.5, 2),
    "jynsy": Item("jynsy", 1, 12),
    "kepka": Item("kepka", 0.5, 6),
    "krossy": Item("krossy", 1, 12),
    "kurtka": Item("kurtka", 2, 24),
    "palto": Item("palto", 3, 12),
    "rukavychky": Item("rukavychky", 0.5, 6),
    "svetr": Item("svetr", 1, 12),
    "sorochka": Item("sorochka", 0.5, 6),
    "futbolka": Item("futbolka", 0.5, 6),
    "cherevyky": Item("cherevyky", 1.5, 18),
    "choboty": Item("choboty", 2, 8),
    "shapka": Item("shapka", 1, 4),
    "shorty": Item("shorty", 0.5, 6)
}

clothes_sets = [
    {items["shapka"], items["bushlat"], items["rukavychky"], items["vata"], items["choboty"]},
    {items["shapka"], items["palto"], items["rukavychky"], items["jynsy"], items["choboty"]},
    {items["kepka"], items["kurtka"], items["jynsy"], items["cherevyky"]},
    {items["svetr"], items["jynsy"], items["krossy"]},
    {items["blazer"], items["sorochka"], items["jynsy"], items["krossy"]},
    {items["blazer"], items["futbolka"], items["shorty"], items["vietnamky"]}
]

special_clothes_sets = [
    {special_items["shapka"], special_items["bushlat"], special_items["rukavychky"], special_items["vata"],
     special_items["choboty"]},
    {special_items["shapka"], special_items["palto"], special_items["rukavychky"], special_items["jynsy"],
     special_items["choboty"]},
    {special_items["kepka"], special_items["kurtka"], special_items["jynsy"], special_items["cherevyky"]},
    {special_items["svetr"], special_items["jynsy"], special_items["krossy"]},
    {special_items["blazer"], special_items["sorochka"], special_items["jynsy"], special_items["krossy"]},
    {special_items["blazer"], special_items["futbolka"], special_items["shorty"], special_items["vietnamky"]}
]


def get_special_clothes_set(temp):
    if temp < -10:
        return special_clothes_sets[0]
    elif -10 <= temp < 0:
        return special_clothes_sets[1]
    elif 0 <= temp < 10:
        return special_clothes_sets[2]
    elif 10 <= temp < 20:
        return special_clothes_sets[3]
    elif 20 <= temp < 30:
        return special_clothes_sets[4]
    else:
        return special_clothes_sets[5]


def get_clothes_set(temp, is_special):
    if is_special:
        return get_special_clothes_set(temp)
    if temp < -10:
        return clothes_sets[0]
    elif -10 <= temp < 0:
        return clothes_sets[1]
    elif 0 <= temp < 10:
        return clothes_sets[2]
    elif 10 <= temp < 20:
        return clothes_sets[3]
    elif 20 <= temp < 30:
        return clothes_sets[4]
    else:
        return clothes_sets[5]


def count_price(initial_set, temp, is_special):
    price = functools.reduce(operator.add, map(lambda v: v.weight, initial_set)) * 10
    new_clothes = set(get_clothes_set(temp, is_special) - initial_set)
    if new_clothes:
        price += functools.reduce(operator.add, map(lambda v: v.price + 2, new_clothes))
    return price


def get_best_strategy(probabilities, temps, is_special=False):
    expenses = []
    clothes = clothes_sets
    if is_special:
        clothes = special_clothes_sets
    for c in clothes:
        price_for_set = 0
        for i in range(1, 13):
            price_for_set -= probabilities[i] * count_price(c, temps[i], is_special)
        expenses.append(price_for_set)

    return max(expenses), \
        clothes_sets[expenses.index(max(expenses))] if not is_special \
        else special_clothes_sets[expenses.index(max(expenses))]
