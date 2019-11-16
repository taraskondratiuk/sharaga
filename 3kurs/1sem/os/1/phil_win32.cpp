#include "iostream"
#include "windows.h"

#define PHIL_NUM 5

using namespace std;

HANDLE events[PHIL_NUM];

HANDLE output_mutex;

DWORD WINAPI eat(void *);

int main() {

    DWORD dwThreadId;
    output_mutex = CreateMutex(nullptr, false, NULL);

    for (auto &event : events) {
        event = CreateEvent(nullptr, false, true, nullptr);
    }

    int phils[PHIL_NUM];
    HANDLE threads[PHIL_NUM];

    for (int i = 0; i < PHIL_NUM; ++i) {
        phils[i] = i + 1;
        threads[i] = CreateThread(NULL, 0, eat, &phils[i], 0, &dwThreadId);
    }

    for (auto &thread : threads) {
        WaitForSingleObject(thread, INFINITE);
    }
}


DWORD WINAPI eat(void *arg) {
    int *philNum = (int *) arg;

    int lForkIndex = *philNum - 1;
    int rForkIndex = *philNum;
    if (rForkIndex == PHIL_NUM) {
        rForkIndex = 0;
    }

    for (int j = 0; j < 10; j++) {

        if (WaitForSingleObject(events[lForkIndex], 0) == WAIT_OBJECT_0) {
            if (WaitForSingleObject(events[rForkIndex], 0) == WAIT_OBJECT_0) {

                WaitForSingleObject(output_mutex, INFINITE);
                printf("philosopher %d eating\n", *philNum);
                ReleaseMutex(output_mutex);

                Sleep(rand() % 2000);

                WaitForSingleObject(output_mutex, INFINITE);
                printf("philosopher %d thinking\n", *philNum);
                ReleaseMutex(output_mutex);

                SetEvent(events[rForkIndex]);
            }
            SetEvent(events[lForkIndex]);
        }
        Sleep(rand() % 2000);
    }
}