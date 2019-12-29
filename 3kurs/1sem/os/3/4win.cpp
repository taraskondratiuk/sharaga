#include <windows.h>
#include <iostream>
#include <string>
#include <vector>

using namespace std;

struct FileInfo {
    string name;
    long size{};
    bool is_hidden{};
    bool is_readonly{};
};

void getFilesInfo(basic_string<char> path, vector<FileInfo> &info);

void printInfo(const vector<FileInfo> &info);

FileInfo getFileInfoNode(WIN32_FIND_DATA data, basic_string<char> path);

int main(int argc, char **argv) {
    vector<FileInfo> info;

    getFilesInfo(argv[1], info);
    printInfo(info);

    return 0;
}


void getFilesInfo(basic_string<char> path, vector<FileInfo> &info) {

    WIN32_FIND_DATA file_data;
    HANDLE file_handle = FindFirstFile((path).c_str(), &file_data);

    if (file_data.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY && path.back() != '*') {
        getFilesInfo(path + "/*", info);
        return;
    }

    if (path.back() == '*') {
        path.pop_back();
        do {
            string file_name(file_data.cFileName);
            if (file_name == ".." || file_name == ".") {
                continue;
            }
            if (file_data.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY) {
                getFilesInfo(path + file_name + "/*", info);
                continue;
            }
            info.push_back(getFileInfoNode(file_data, path + file_name));
        } while (FindNextFile(file_handle, &file_data) != 0);
    } else {
        info.push_back(getFileInfoNode(file_data, path));
    }
}

FileInfo getFileInfoNode(WIN32_FIND_DATA data, basic_string<char> path) {
    FileInfo current;

    current.name = path;
    current.size = (data.nFileSizeHigh * (MAXDWORD + 1)) + data.nFileSizeLow;
    current.is_readonly = data.dwFileAttributes & FILE_ATTRIBUTE_READONLY;
    current.is_hidden = data.dwFileAttributes & FILE_ATTRIBUTE_HIDDEN;
    return  current;
}

void printInfo(const vector<FileInfo> &info) {
    for (auto &i : info) {
        cout << i.name << endl;
        cout << "\t Size: " << i.size << " bytes" << endl;
        cout << "\t Hidden: " << (i.is_readonly ? "+" : "-") << endl;
        cout << "\t Readonly: " << (i.is_hidden ? "+" : "-") << endl;
        cout << endl;
    }
    cout << "Total num of files : " << info.size();
}
