#include <cstdio>
#include <cstdlib>
#include <cstring>
#include <dirent.h>
#include <cctype>
#include <vector>
#include <algorithm>
#include <string>
#include <unistd.h>


#define WITHOUT_SORT_KEY 0
#define SORT_BY_NAME_KEY 1
#define SORT_BY_ID_KEY 2


int system_uptime;
long int cpu_hertz = sysconf(_SC_CLK_TCK);
unsigned long long var;
char string [PATH_MAX];
long pagesize = sysconf(_SC_PAGE_SIZE);


struct CpuTimeInfo {
    unsigned long proc_utime;
    unsigned long proc_stime;
    long proc_cutime;
    long proc_cstime;
    unsigned long long proc_starttime;
};

class MemInfo {
public:
    unsigned long mem_total;
    unsigned long mem_free;
    unsigned long swap_free;

     void printMemInfo() {
        printf("%d Mb   %d Mb  %d Mb      %d Kb\n",
               mem_total / 1024,
               mem_free / 1024,
               swap_free / 1024,
               pagesize / 1024);
    }

    static void printTableHeader() {
         printf("Memory: \n\n");
        printf("%5s     %4s     %9s   %9s\n\n",
               "TOTAL",
               "FREE",
               "SWAP_FREE",
               "PAGE_SIZE"
        );
    }


};

class ProcessInfo {
public:
    std::string name;
    int process_id;
    char state;
    int size;
    int rsize;
    int parent_id;
    int cpu_usage;


     void printProcInfo() {
        printf("%35s%6d%4c %7d Kb%7d Kb %6d \t%d %% \n",
               name.c_str(),
               process_id,
               state,
               size,
               rsize,
               parent_id,
               cpu_usage
        );
    }

    static void printTableHeader() {
        printf("%35s%6s%4s %10s%10s%9s %s\n",
               "NAME",
               "PID",
               "S",
               "VIRT",
               "RSS",
               "PARENT",
               "CPU"
        );
    }
};

int countCpuUsage(CpuTimeInfo info);

bool CompareProcessesByName(const ProcessInfo &a, const ProcessInfo &b) {
    return a.name < b.name;
}

bool CompareProcessesById(const ProcessInfo &a, const ProcessInfo &b) {
    return a.process_id < b.process_id;
}

MemInfo getMemInfo() {
    MemInfo memInfo{};
    DIR *directory = opendir("/proc");
    FILE *file = fopen("/proc/meminfo", "r");

    fscanf(file, "%s", string);
    fscanf(file, "%lu", &memInfo.mem_total);
    fscanf(file, "%s", string);

    fscanf(file, "%s", string);
    fscanf(file, "%lu", &memInfo.mem_free);
    fscanf(file, "%s", string);


    for (int i = 0; i < 18; ++i) {
        fscanf(file, "%s", string);
        fscanf(file, "%lu", &var);
        fscanf(file, "%s", string);

    }
    fscanf(file, "%s", string);
    fscanf(file, "%lu", &memInfo.swap_free);
    fclose(file);
    return memInfo;
}


void getProcessesInfo(std::vector<ProcessInfo> &processes) {
    processes.clear();


    DIR *directory;
    FILE *file;
    struct dirent *direntry;
    char path[20], command[30];


    directory = opendir("/proc");


    file = fopen("/proc/uptime", "r");
    fscanf(file, "%d", &system_uptime);


    while (direntry = readdir(directory)) {
        ProcessInfo process_info;
        if (!isdigit(direntry->d_name[0])) {
            continue;
        }

        strcpy(path, "/proc/");
        strcat(path, direntry->d_name);
        strcat(path, "/stat");

        file = fopen(path, "r");


        fscanf(file, "%d", &process_info.process_id);

        fscanf(file, "%s", command);

        process_info.name = std::string(command);
        fscanf(file, "%c", &process_info.state);
        fscanf(file, "%c", &process_info.state);
        fscanf(file, "%d", &process_info.parent_id);

        fscanf(file, "%d", &var);
        fscanf(file, "%d", &var);
        fscanf(file, "%d", &var);
        fscanf(file, "%d", &var);
        fscanf(file, "%u", &var);
        fscanf(file, "%lu", &var);
        fscanf(file, "%lu", &var);
        fscanf(file, "%lu", &var);
        fscanf(file, "%lu", &var);


        CpuTimeInfo cpu_time_info;

        fscanf(file, "%lu", &cpu_time_info.proc_utime);
        fscanf(file, "%lu", &cpu_time_info.proc_stime);
        fscanf(file, "%ld", &cpu_time_info.proc_cutime);
        fscanf(file, "%ld", &cpu_time_info.proc_cstime);

        fscanf(file, "%ld", &var);
        fscanf(file, "%ld", &var);
        fscanf(file, "%ld", &var);
        fscanf(file, "%ld", &var);

        fscanf(file, "%llu", &cpu_time_info.proc_starttime);

        fclose(file);

        strcpy(path, "/proc/");
        strcat(path, direntry->d_name);
        strcat(path, "/statm");

        file = fopen(path, "r");


        fscanf(file, "%d", &process_info.size);
        fscanf(file, "%d", &process_info.rsize);

        process_info.cpu_usage = countCpuUsage(cpu_time_info);

        processes.push_back(process_info);


        fclose(file);
    }
    closedir(directory);
}

int countCpuUsage(CpuTimeInfo info) {
    long double total_time = info.proc_utime + info.proc_stime;
    long seconds_since_start = (long double) system_uptime - ((long double) info.proc_starttime / cpu_hertz);
    return std::abs(25 * ((total_time / cpu_hertz) / (seconds_since_start + 1)));
}
void printMemory() {
    MemInfo::printTableHeader();
    MemInfo memInfo = getMemInfo();

    memInfo.printMemInfo();

}


void printProcesses(const int sort) {
    ProcessInfo::printTableHeader();
    std::vector<ProcessInfo> processes;
    getProcessesInfo(processes);

    switch (sort) {
        case SORT_BY_NAME_KEY:
            std::sort(processes.begin(), processes.end(),
                      CompareProcessesByName);
            break;
        case SORT_BY_ID_KEY:
            std::sort(processes.begin(), processes.end(),
                      CompareProcessesById);
            break;
    }

    for (auto &p : processes) {
        p.printProcInfo();
    }
}

int main(int argc, char *argv[]) {
    system("clear");
    if (argc == 3) {
        std::string sort(argv[1]), timeout(argv[2]);

        for (int i = 0; i < 1000; ++i) {


            printf("\n\n\n\n\n");
            if (sort == "-sortbyname") {
                printProcesses(SORT_BY_NAME_KEY);
            } else if (sort == "-sortbyid") {
                printProcesses(SORT_BY_ID_KEY);
            } else {
                printf("Undefined sort: %s\n", argv[1]);
            }

            printMemory();
            sleep(stoi(timeout));

        }
    } else {
        printProcesses(WITHOUT_SORT_KEY);
    }



    return 0;
}