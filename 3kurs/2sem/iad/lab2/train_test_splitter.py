import sys
import os
from utility import file_len


input_files = sys.argv[1:]

for f in input_files:
    flen = file_len(f)
    train = open("train_data/" + os.path.basename(f), "w", encoding="utf-8")
    test = open("test_data/" + os.path.basename(f), "w", encoding="utf-8")

    input = open(f, "r", encoding="utf-8")

    for i in range(flen * 2 // 3):
        train.write(input.readline())

    for i in range(flen * 2 // 3, flen):
        test.write(input.readline())

    train.close()
    test.close()
    input.close()
