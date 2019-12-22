#include <windows.h>
#include <cstdio>

#define NUM_READERS 5

#define NUM_WRITERS 2

CRITICAL_SECTION single_writer, output, writers_queue, library, readers_queue;

int file, writer_counter = 0, reader_counter = 0;

DWORD WINAPI functionRead(LPVOID arg);

DWORD WINAPI functionWrite(LPVOID arg);


int main() {
    DWORD threadId;

    HANDLE readersThreads[NUM_READERS];
    HANDLE writersThreads[NUM_WRITERS];

    int readerIds[NUM_READERS];
    int writerIds[NUM_WRITERS];

    InitializeCriticalSection(&single_writer);
    InitializeCriticalSection(&output);
    InitializeCriticalSection(&writers_queue);
    InitializeCriticalSection(&readers_queue);
    InitializeCriticalSection(&library);

    for (int j = 0; j < NUM_WRITERS; ++j) {
        writerIds[j] = j;
        writersThreads[j] = CreateThread(nullptr, 0, &functionWrite, &writerIds[j], 0, &threadId);
    }

    Sleep(1000);
    for (int i = 0; i < NUM_READERS; ++i) {
        readerIds[i] = i;
        readersThreads[i] = CreateThread(nullptr, 0, &functionRead, &readerIds[i], 0, &threadId);
    }


    WaitForMultipleObjects(NUM_WRITERS, writersThreads, true, INFINITE);
    WaitForMultipleObjects(NUM_WRITERS, readersThreads, true, INFINITE);

    DeleteCriticalSection(&single_writer);
    return 0;

}

DWORD WINAPI functionRead(LPVOID arg) {
    int id = *static_cast<int *>(arg) + 1;
    for (int i = 0; i < 100; ++i) {
        EnterCriticalSection(&readers_queue);


        EnterCriticalSection(&output);
        printf("reader %d came to queue\n", id);
        LeaveCriticalSection(&output);

        reader_counter++;
        if (reader_counter == 1) {
            EnterCriticalSection(&library);
            EnterCriticalSection(&output);
            printf("reader %d locked lib\n", id);
            LeaveCriticalSection(&output);
        }

        LeaveCriticalSection(&readers_queue);

        EnterCriticalSection(&output);
        printf("reader %d reading : %d\n", id, file);
        LeaveCriticalSection(&output);

        Sleep(1000 + rand() % 1000);


        EnterCriticalSection(&output);
        printf("reader %d left\n", id);
        LeaveCriticalSection(&output);

        EnterCriticalSection(&readers_queue);

        reader_counter--;
        if (reader_counter == 0) {
            EnterCriticalSection(&output);
            printf("reader %d unlocked lib\n", id);
            LeaveCriticalSection(&output);
            LeaveCriticalSection(&library);
        }
        LeaveCriticalSection(&readers_queue);

        Sleep(5000 + rand() % 5000);
    }


    return 0;
}

DWORD WINAPI functionWrite(LPVOID arg) {
    int id = *static_cast<int *>(arg) + 1;
    for (int i = 0; i < 10; ++i) {


        EnterCriticalSection(&writers_queue);
        writer_counter++;

        EnterCriticalSection(&output);
        printf("writer %d came to queue\n", id);
        LeaveCriticalSection(&output);


        if (writer_counter == 1) {
            EnterCriticalSection(&library);
            EnterCriticalSection(&output);
            printf("writer %d locked lib\n", id);
            LeaveCriticalSection(&output);
        }


        LeaveCriticalSection(&writers_queue);


        EnterCriticalSection(&single_writer);

        EnterCriticalSection(&output);
        printf("writer %d writing\n", id);
        LeaveCriticalSection(&output);

        Sleep(1000);
        file = rand() % 1000;

        EnterCriticalSection(&output);
        printf("new file : %d\n", file);

        printf("writer %d left\n", id);
        LeaveCriticalSection(&output);

        LeaveCriticalSection(&single_writer);

        EnterCriticalSection(&writers_queue);
        writer_counter--;
        if (writer_counter == 0) {
            EnterCriticalSection(&output);
            printf("writer %d unlocked lib\n", id);
            LeaveCriticalSection(&output);
            LeaveCriticalSection(&library);
        }


        LeaveCriticalSection(&writers_queue);

        Sleep(4000 + rand() % 2000);
    }
    return 0;
}