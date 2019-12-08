#include <iostream>
#include <pthread.h>
#include <cstdlib>
#include <semaphore.h>
#include <zconf.h>

#define VISITORS_NUM 1000
#define CHAIRS_NUM 5

using namespace std;

struct Node {
    int key;
    struct Node *next;
};

struct Queue {
    struct Node *front, *rear;
};

struct Node *newNode(int k) {
    struct Node *temp = (struct Node *) malloc(sizeof(struct Node));
    temp->key = k;
    temp->next = NULL;
    return temp;
}

struct Queue *createQueue() {
    struct Queue *q = (struct Queue *) malloc(sizeof(struct Queue));
    q->front = q->rear = NULL;
    return q;
}

void enqueue(struct Queue *q, int k) {
    struct Node *temp = newNode(k);

    if (q->rear == NULL) {
        q->front = q->rear = temp;
        return;
    }

    q->rear->next = temp;
    q->rear = temp;
}

int dequeue(struct Queue *q) {
    if (q->front == NULL)
        return NULL;

    struct Node *temp = q->front;
    int val = temp->key;

    q->front = q->front->next;

    if (q->front == NULL) {
        q->rear = NULL;
    }
    free(temp);
    return val;
}

void *functionVisitor(void *arg);

void *functionBarber(void *arg);

int getFreeChairsNum();

int getVisitorsNum();

string getQueueString(Queue *items);

sem_t visitor_sem, output_sem, barber_sem, visitor_left_sem;

struct Queue *q;

int main() {
    pthread_t barberThread;
    pthread_t visitorThreads[VISITORS_NUM];


    q = createQueue();

    sem_init(&visitor_sem, 0, 0);
    sem_init(&output_sem, 0, 1);
    sem_init(&barber_sem, 0, 0);
    sem_init(&visitor_left_sem, 0, 1);

    pthread_create(&barberThread, nullptr, functionBarber, nullptr);
    sleep(1);
    for (unsigned long long int visitorThread : visitorThreads) {
        pthread_create(&visitorThread, nullptr, functionVisitor, nullptr);
        sleep(2);
    }

    pthread_join(barberThread, nullptr);

    sem_destroy(&output_sem);
    sem_destroy(&visitor_sem);
    return 0;
}

void *functionVisitor(void *arg) {
    int clientId = rand() % 20;

    if (getFreeChairsNum() > 0) {

        enqueue(q, clientId);

        sem_wait(&output_sem);
        cout << "client " << clientId << " came" << endl;
        cout << getQueueString(q);
        sem_post(&output_sem);

        sem_post(&visitor_sem);
        sem_wait(&barber_sem);

        sem_wait(&output_sem);
        cout << "barber finished work, client " << clientId << " leaving" << endl;
        sem_post(&output_sem);

        sem_post(&visitor_left_sem);

    } else {
        sem_wait(&output_sem);
        cout << "no free chairs, client " << clientId << " leaving" << endl;
        sem_post(&output_sem);
    }

    return nullptr;
}

void *functionBarber(void *arg) {
    for (int k = 0; k < 1000000; k++) {
        if (getVisitorsNum() == 0) {
            sem_wait(&output_sem);
            cout << "barber sleep" << endl;
            sem_post(&output_sem);
        }
        sem_wait(&visitor_sem);

        int num = dequeue(q);

        sem_wait(&output_sem);
        cout << "barber shaves client " << num << endl;
        cout << getQueueString(q);
        sem_post(&output_sem);

        sleep((rand() % 2) + 3);

        sem_post(&barber_sem);

        sem_wait(&visitor_left_sem);

    }
    return nullptr;

}

string getQueueString(Queue *items) {
    char buffer[25];
    string output;
    output.append("queue : ");
    Node *cur = items->front;
    while (cur != NULL) {
        itoa(cur->key, buffer, 10);
        output.append(buffer);
        output.append(" ");
        cur = cur->next;
    }
    output.append("\n");
    return output;
}

int getFreeChairsNum() {
    int semVal;
    sem_getvalue(&visitor_sem, &semVal);
    return CHAIRS_NUM - semVal;
}

int getVisitorsNum() {
    int visNum;
    sem_getvalue(&visitor_sem, &visNum);
    return visNum;
}