import sys
import matplotlib.pyplot as plt
from correct_percentage_counter import count_correct_percentage_for_given_train_input_limit as count_percent

train_dir = sys.argv[1]
test_dir = sys.argv[2]

results = []
train_used = []
for percent_train_used in range(10, 101, 10):
    train_used.append(percent_train_used)
    results.append(count_percent(train_dir, test_dir, percent_train_used))

plt.scatter(train_used, results)
plt.xlabel("percentage of train data used")
plt.ylabel("correct class percentage")
plt.show()
