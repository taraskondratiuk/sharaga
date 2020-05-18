def file_len(fname):
    with open(fname, "r", encoding="utf-8") as f:
        for i, _ in enumerate(f):
            pass
    return i