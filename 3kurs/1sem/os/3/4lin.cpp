#include <iostream>
#include <dirent.h>
#include <sys/stat.h>
#include <cstring>
#include <deque>
#include <string>

using namespace std;

struct FileInfo {
    FileInfo(const struct stat &stats, char *path) {
        this->stats = stats;
        strcpy(this->path, path);
    }

    struct stat stats{};
    char path[PATH_MAX]{};
};

void printInfo(const deque<FileInfo> &info) {
    for (auto &i : info) {
        cout << i.path << endl;
        cout << "\t Size: " << i.stats.st_size << " bytes" << endl;
        cout << "\t User id: " << i.stats.st_uid << endl;
        cout << "\t File mode: " << i.stats.st_mode << endl << endl;
    }
    cout << "Total num of files : " << info.size() << endl << endl;
}

void addFilesInfoToQueue(char path[], deque<FileInfo> &info_queue) {

    struct stat stats{};

    if (stat(path, &stats) < 0) {
        return;
    }


    if (S_ISDIR(stats.st_mode)) {
        DIR *dir = opendir(path);
        strcat(path, new char('/'));
        struct dirent *ent;
        while ((ent = readdir(dir)) != NULL) {
            if (ent->d_name[0] == '.') {
                continue;
            }
            char path_buffer[PATH_MAX];
            strcpy(path_buffer, path);
            strcat(path_buffer, ent->d_name);

            addFilesInfoToQueue(path_buffer, info_queue);
        }
        closedir(dir);

    } else {
        info_queue.emplace_back(stats, path);

    }

}

int main() {
    char path[PATH_MAX] = "/home/gladiator/Desktop/settings.zip";
    deque<FileInfo> info_queue;
    addFilesInfoToQueue(path, info_queue);
    printInfo(info_queue);

    return 0;
}