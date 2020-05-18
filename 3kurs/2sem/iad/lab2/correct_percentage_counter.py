import os
from utility import file_len


def count_correct_percentage_for_given_train_input_limit(train_dir, test_dir, train_usage_percent):
    if train_usage_percent < 1 or train_usage_percent > 100:
        raise Exception("percent should be [1; 100], the value was: {}".format(train_usage_percent))

    words_counts = {"all": {}}
    total_words = 0
    topics = os.listdir(train_dir)

    for topic in topics:
        words_counts[topic] = {}

    for file in topics:
        flen = file_len(train_dir + file)
        limit = flen * train_usage_percent // 100
        f = open(train_dir + file, "r", encoding="utf-8")
        for _ in range(limit):
            line = f.readline()

            if not line:
                break

            for word in line.rstrip().split():
                letters = "".join(filter(str.isalpha, word)).lower()

                if not letters:
                    continue

                total_words += 1

                if letters in words_counts["all"]:
                    words_counts["all"][letters] += 1
                else:
                    words_counts["all"][letters] = 1

                if letters in words_counts[file]:
                    words_counts[file][letters] += 1
                else:
                    words_counts[file][letters] = 1

                for topic in topics:
                    if letters not in words_counts[topic]:
                        words_counts[topic][letters] = 0

    class_prob = 1 / len(topics)

    for topic in topics:
        for item in words_counts[topic].items():
            counter = words_counts["all"][item[0]]
            num_repetitions = item[1]
            prob = num_repetitions / counter
            words_counts[topic][item[0]] = (num_repetitions * prob + class_prob) / (num_repetitions + 1)

    correct_counter = 0
    all_counter = 0

    for file in topics:
        f = open(test_dir + file, "r", encoding="utf-8")
        while True:
            line = f.readline()

            if not line:
                break

            all_counter += 1
            words = []

            for word in line.rstrip().split():
                letters = "".join(filter(str.isalpha, word)).lower()

                if not letters or letters not in words_counts["all"]:
                    continue

                words.append(letters)

            probabilities_by_topic = {}
            for topic in topics:
                probabilities_by_topic[topic] = 1

                for word in words:
                    probabilities_by_topic[topic] *= words_counts[topic][word]

            if max(probabilities_by_topic, key=probabilities_by_topic.get) == file:
                correct_counter += 1

        f.close()

    return correct_counter / all_counter * 100
