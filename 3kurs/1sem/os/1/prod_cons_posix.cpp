#include <iostream>
#include <pthread.h>
#include <cstdlib>
#include <semaphore.h>
#include <zconf.h>

#define CONSUMERS_NUM 5
#define PRODUCERS_NUM 2
#define QUEUE_SIZE 6

using namespace std;

sem_t sem_single_prod, sem_single_cons, sem_min_queue_size, sem_max_queue_size, sem_output, sem_one_item;


struct Node  {
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
void print_queue(struct Queue *q) {
    cout<< "queue : ";
    if (q->front == NULL)
        return;

    struct Node *temp = q->front;
    do {
        cout<<temp->key<< " ";
    }while (temp-> next != NULL);



}

Queue *que = createQueue();

void *functionConsume(void *);

void *functionProduce(void *);

string getQueueString(Queue *items, char *type, int num);


int queueCreated = 0;

int main() {
    pthread_t consumersThreads[CONSUMERS_NUM], producersThreads[PRODUCERS_NUM];




    sem_init(&sem_single_prod, 0, 1);
    sem_init(&sem_single_cons, 0, 1);
    sem_init(&sem_min_queue_size, 0, 0);
    sem_init(&sem_max_queue_size, 0, QUEUE_SIZE);
    sem_init(&sem_output, 0, 1);
    sem_init(&sem_queue_created, 0, 0);
    sem_init(&sem_one_item, 0, 1);



    for (unsigned int &producerThread : producersThreads) {
        pthread_create(&producerThread, nullptr, functionProduce, nullptr);
    }
    sleep(2);

    for (unsigned int &consumerThread : consumersThreads) {
        pthread_create(&consumerThread, nullptr, functionConsume, nullptr);
    }
    for (unsigned int &consumerThread : consumersThreads) {
        pthread_join(consumerThread, nullptr);
    }

    for (unsigned int &producerThread : producersThreads) {
        pthread_join(producerThread, nullptr);
    }

    sem_destroy(&sem_single_prod);
    return 0;
}

void *functionConsume(void *arg) {



    for (int k = 0; k < 10; k++) {
        sem_wait(&sem_min_queue_size);
        sem_wait(&sem_single_cons);

        int size;
        sem_getvalue(&sem_min_queue_size, &size);

        sem_wait(&sem_one_item);
        if (size == 1) {
            int num = dequeue(que);

            sem_wait(&sem_output);
            cout << "consumed : "<< num<<endl;
            print_queue(que);
            cout<<endl;
            sem_post(&sem_output);

            sem_post(&sem_one_item);
        } else {
            sem_post(&sem_one_item);
            int num = dequeue(que);


            sem_wait(&sem_output);
            cout <<"consumed : "<< num<<endl;
            print_queue(que);
            cout<<endl;
            sem_post(&sem_output);
        }


        sem_post(&sem_max_queue_size);
        sem_post(&sem_single_cons);
        sleep(rand() % 3);
    }
    return nullptr;
}

void *functionProduce(void *arg) {


    for (int k = 0; k < 100; k++) {
        sem_wait(&sem_max_queue_size);
        sem_wait(&sem_single_prod);

        int num = rand() % 20;


        sem_getvalue(&sem_queue_created, &queueCreated);

        int size;
        sem_getvalue(&sem_min_queue_size, &size);

        sem_wait(&sem_one_item);
        if (size > 1) {
            sem_post(&sem_one_item);

            enqueue(que, num);

            sem_wait(&sem_output);
            cout <<"produced : "<< num<<endl;
            print_queue(que);
            cout<<endl;
            sem_post(&sem_output);
        } else {

            enqueue(que, num);

            sem_wait(&sem_output);
            cout <<"produced : "<< num<<endl;
            print_queue(que);
            cout<<endl;
            sem_post(&sem_output);
            sem_post(&sem_one_item);

        }


        sem_post(&sem_min_queue_size);
        sem_post(&sem_single_prod);
        sleep(rand() % 3);
    }
    return nullptr;

}
