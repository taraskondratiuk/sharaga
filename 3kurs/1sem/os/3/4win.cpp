#include <windows.h>
#include <iostream>
#include <deque>
#include <string>

using namespace std;

struct FileInfo {
    string name;
    long size{};
    bool is_hidden{};
    bool is_readonly{};
};

void getFilesInfo(const string &directory, const string &filter, deque<FileInfo> &info);

void printInfo(const deque<FileInfo> &info);

int main(int argc, char **argv) {
    deque<FileInfo> info;

    getFilesInfo("C:/Users/Taras/Desktop/io/", "*", info);
    printInfo(info);

    return 0;
}

void getFilesInfo(const string &directory, const string &filter, deque<FileInfo> &info) {
    deque<FileInfo> file_info;

    WIN32_FIND_DATA file_data;
    WIN32_FIND_DATA dir_data;
    HANDLE file_handle = FindFirstFile((directory + filter).c_str(), &file_data);

    HANDLE dir_file_handle = FindFirstFile((directory + "*" ).c_str(), &dir_data);
    if (file_handle != INVALID_HANDLE_VALUE) {

        do {
            string file_name(file_data.cFileName);
            if (file_name == ".." || file_name == ".") {
                continue;
            }
            if (file_data.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY) {
                getFilesInfo((directory + file_name + "/"), filter, info);
                continue;
            }

            FileInfo current;

            current.name = directory + file_name;
            current.size = (file_data.nFileSizeHigh * (MAXDWORD + 1)) + file_data.nFileSizeLow;
            current.is_readonly = file_data.dwFileAttributes & FILE_ATTRIBUTE_READONLY;
            current.is_hidden = file_data.dwFileAttributes & FILE_ATTRIBUTE_HIDDEN;

            info.push_back(current);
        } while (FindNextFile(file_handle, &file_data) != 0);

        FindClose(file_handle);
    } else if (dir_file_handle != INVALID_HANDLE_VALUE) {
        do {
            string file_name(dir_data.cFileName);
            if (file_name == ".." || file_name == ".") {
                continue;
            }
            if (dir_data.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY) {
                getFilesInfo((directory + file_name + "/"), filter, info);
                continue;
            }

        } while (FindNextFile(dir_file_handle, &dir_data) != 0);
    }
}


void printInfo(const deque<FileInfo> &info) {
    for (auto &i : info) {
        cout << i.name << endl;
        cout << "\t Size: " << i.size << " bytes" << endl;
        cout << "\t Hidden: " << (i.is_readonly ? "+" : "-") << endl;
        cout << "\t Readonly: " << (i.is_hidden ? "+" : "-") << endl;
        cout << endl;
    }
    cout << "Total num of files : " << info.size();
}
