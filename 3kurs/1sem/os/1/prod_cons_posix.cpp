#include <iostream>
#include <semaphore.h>
#include <zconf.h>


#define CONSUMERS_NUM 5
#define PRODUCERS_NUM 5
#define QUEUE_SIZE 6


using namespace std;

struct Node {
    int key;
    Node *next;
};


struct Queue {
    Node *front, *rear;
};

Node *newNode(int k) {
    Node *temp = new Node();
    temp->key = k;
    temp->next = nullptr;
    return temp;
}

Queue *createQueue() {
    Queue *q = new Queue();
    q->front = q->rear = nullptr;
    return q;
}

void enqueue(Queue *q, int k) {

    Node *temp = newNode(k);

    if (q->rear == nullptr) {
        q->front = q->rear = temp;
        return;
    }

    q->rear->next = temp;
    q->rear = temp;
}


int deque(Queue *q) {

    if (q->front == nullptr) {
        return 0;
    }

    Node *temp = q->front;
    int res = temp->key;

    q->front = q->front->next;

    if (q->front == nullptr) {
        q->rear = nullptr;
    }
    free(temp);

    return res;
}

void print_queue(struct Queue *q) {
    cout << "queue : ";
    if (q->front == NULL) {
        return;
    }

    struct Node *temp = q->front;
    do {
        cout << temp->key << " ";
        temp = temp->next;
    } while (temp != nullptr);
}


Queue *que = createQueue();

void *functionConsume(void *);
void *functionProduce(void *);

sem_t sem_single_prod, sem_single_cons, sem_min_queue_size, sem_max_queue_size, sem_output, sem_one_item;

int main() {
    pthread_t consumersThreads[CONSUMERS_NUM], producersThreads[PRODUCERS_NUM];


    sem_init(&sem_single_prod, 0, 1);
    sem_init(&sem_single_cons, 0, 1);
    sem_init(&sem_min_queue_size, 0, 0);
    sem_init(&sem_max_queue_size, 0, QUEUE_SIZE);
    sem_init(&sem_output, 0, 1);
    sem_init(&sem_one_item, 0, 1);


    for (int i = 0; i < PRODUCERS_NUM; i++) {
        pthread_create(&producersThreads[i], nullptr, functionProduce, nullptr);
    }
    sleep(2);
    for (int i = 0; i < CONSUMERS_NUM; i++) {
        pthread_create(&consumersThreads[i], nullptr, functionConsume, nullptr);
    }

    for (int i = 0; i < CONSUMERS_NUM; i++) {
        pthread_join(consumersThreads[i], nullptr);
    }
    for (int i = 0; i < PRODUCERS_NUM; i++) {
        pthread_join(producersThreads[i], nullptr);
    }

    sem_destroy(&sem_single_prod);
    sem_destroy(&sem_single_cons);
    sem_destroy(&sem_min_queue_size);
    sem_destroy(&sem_max_queue_size);
    sem_destroy(&sem_output);
    sem_destroy(&sem_one_item);
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
            int num = deque(que);

            sem_wait(&sem_output);
            cout << "consumed : " << num << endl;
            print_queue(que);
            cout << endl;
            sem_post(&sem_output);

            sem_post(&sem_one_item);
        } else {
            sem_post(&sem_one_item);

            int num = deque(que);
            sem_wait(&sem_output);
            cout << "consumed : " << num << endl;
            print_queue(que);
            cout << endl;
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

        int size;
        sem_getvalue(&sem_min_queue_size, &size);

        sem_wait(&sem_one_item);
        if (size > 1) {
            sem_post(&sem_one_item);

            enqueue(que, num);

            sem_wait(&sem_output);
            cout << "produced : " << num << endl;
            print_queue(que);
            cout << endl;
            sem_post(&sem_output);
        } else {
            enqueue(que, num);

            sem_wait(&sem_output);
            cout << "produced : " << num << endl;
            print_queue(que);
            cout << endl;
            sem_post(&sem_output);
            sem_post(&sem_one_item);
        }

        sem_post(&sem_min_queue_size);
        sem_post(&sem_single_prod);
        sleep(rand() % 3);
    }
    return nullptr;
}
