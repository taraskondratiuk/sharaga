#include <iostream>
#include <dirent.h>
#include <sys/stat.h>
#include <cstring>
#include <map>
#include <vector>

using namespace std;

int total_files_counter = 0, two_or_more_links_counter = 0;
const char slash[PATH_MAX] = "/";

struct FileInfo {
    FileInfo(const struct stat &stats, char *path) {
        this->stats = stats;
        strcpy(this->path, path);
    }

    struct stat stats{};
    char path[PATH_MAX]{};
};


string getPermissions(mode_t perm) {

    char modeval[10];

    modeval[0] = (perm & S_IRUSR) ? 'r' : '-';
    modeval[1] = (perm & S_IWUSR) ? 'w' : '-';
    modeval[2] = (perm & S_IXUSR) ? 'x' : '-';
    modeval[3] = (perm & S_IRGRP) ? 'r' : '-';
    modeval[4] = (perm & S_IWGRP) ? 'w' : '-';
    modeval[5] = (perm & S_IXGRP) ? 'x' : '-';
    modeval[6] = (perm & S_IROTH) ? 'r' : '-';
    modeval[7] = (perm & S_IWOTH) ? 'w' : '-';
    modeval[8] = (perm & S_IXOTH) ? 'x' : '-';
    modeval[9] = '\0';
    return string(modeval);

}

void printInfo(const map<int, vector<FileInfo>> &info) {
    for (auto &i : info) {
        cout << "Inode id: " << i.first << endl << endl;
        for (auto &j : i.second) {
            cout << "\t Path: " << j.path << endl;
            cout << "\t Size: " << j.stats.st_size << " bytes" << endl;
            cout << "\t User id: " << j.stats.st_uid << endl;
            cout << "\t File mode: " << getPermissions(j.stats.st_mode) << endl << endl;
        }

    }
    cout << "Total files : " << total_files_counter << endl;
    cout << "Total files with two or more hard links : " << two_or_more_links_counter << endl << endl;
}

void addInodesInfoToMap(char *path, map<int, vector<FileInfo>> &info_map) {
    total_files_counter++;
    struct stat stats{};
    if (stat(path, &stats) < 0) {
        return;
    }


    if (S_ISDIR(stats.st_mode)) {
        DIR *dir = opendir(path);
        strcat(path, slash);
        struct dirent *ent;
        while ((ent = readdir(dir)) != NULL) {
            if (ent->d_name[0] == '.') {
                continue;
            }
            char path_buffer[PATH_MAX];
            strcpy(path_buffer, path);
            strcat(path_buffer, ent->d_name);

            addInodesInfoToMap(path_buffer, info_map);
        }
        closedir(dir);

    }

    if (stats.st_nlink > 1) {
        two_or_more_links_counter++;

        std::map<int, vector<FileInfo>>::iterator it;
        it = info_map.find(stats.st_ino);

        if (it != info_map.end()) {

            it->second.emplace_back(stats, path);
        } else {
            vector<FileInfo> vec;
            vec.emplace_back(stats, path);
            info_map.insert(pair<int, vector<FileInfo> >(stats.st_ino, vec));
        }
    }

}


int main() {
    char path[PATH_MAX] = "/home/gladiator";
    map<int, vector<FileInfo>> info_map;

    addInodesInfoToMap(path, info_map);
    printInfo(info_map);
    return 0;
}