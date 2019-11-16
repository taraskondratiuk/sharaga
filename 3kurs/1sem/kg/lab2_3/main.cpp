#include <GL/gl.h>
#include <GL/glut.h>
#include <cmath>
#include <iostream>
#include "FigureTemplate.cpp"
#include "SolidCube.cpp"
#include "SolidTorus.cpp"
#include "WireTorus.cpp"
#include "WireCube.cpp"

#define ESCAPE '\033'

bool vChange = false;

double cx = 0.0, cy = 0.0, cz = 0.0, hValue = 0.0, vValue = 0.0, upY = 1.0,
        scaleCoef = 1.0, cameraSpeed = 0.03, rotateSpeed = 0.3,
        rotateX = 0.0, rotateY = 0.0, rotateZ = 0.0,
        lightSpeed = 0.3, lightCoord = 0.0;

int listId = 1, transX = 0, transY = 0, transZ = 0;


void changeCamera() {

    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();

    cx = cos(hValue) * cos(vValue);
    cz = cos(vValue) * sin(hValue);
    cy = sin(vValue);

    if (vChange) {
        if (cx > 0.0) {
            upY = 1.0;
        } else {
            upY = -1.0;
        }
    }

    gluLookAt(cx, cy, cz, 0.0, 0.0, 0.0, 0.0, upY, 0.0);
}

void displayGraphic() {
    GLdouble points[1200];
    GLint count = 0;
    GLint x = 0;
    for (GLint y = 0; y < 20; y++) {
        for (GLint x = 0; x < 20; x++) {

            points[count++] = x * 3;
            points[count++] = y * 3;
            points[count++] = sin(x) * cos(y) * 3;
        }
    }

    glEnableClientState(GL_VERTEX_ARRAY);
    glColor3f(1, 0, 0);
    glVertexPointer(3, GL_DOUBLE, 0, points);

    glDrawArrays(GL_POINTS, 0, 400);

    glDisableClientState(GL_VERTEX_ARRAY);
}

void scale() {

    GLdouble scaleMatrix[] = {
            scaleCoef, 0, 0, 0,
            0, scaleCoef, 0, 0,
            0, 0, scaleCoef, 0,
            0, 0, 0, 1};
    glMultMatrixd(scaleMatrix);

}

void translate() {
    GLdouble translationMatrix[] = {
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            static_cast<GLdouble>(transX),
            static_cast<GLdouble>(transY), static_cast<GLdouble>(transZ), 1};
    glMultMatrixd(translationMatrix);
}

void rotate() {


    GLdouble rotationMatrixX[] = {
            1, 0, 0, 0,
            0, cos(rotateX), sin(rotateX), 0,
            0, -sin(rotateX), cos(rotateX), 0,
            0, 0, 0, 1};
    glMultMatrixd(rotationMatrixX);


    GLdouble rotationMatrixY[] = {
            cos(rotateY), 0, -sin(rotateY), 0,
            0, 1, 0, 0,
            sin(rotateY), 0, cos(rotateY), 0,
            0, 0, 0, 1};
    glMultMatrixd(rotationMatrixY);


    GLdouble rotationMatrixZ[] = {
            cos(rotateZ), sin(rotateZ), 0, 0,
            -sin(rotateZ), cos(rotateZ), 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1};
    glMultMatrixd(rotationMatrixZ);

}


void printAllLists(int x, int y, int z) {
    glTranslatef(x, y, z);
    GLint listOfLists[listId];

    for (int i = 0; i < listId; ++i) {
        listOfLists[i] = i;
    }
    glCallLists(listId, GL_INT, listOfLists);

}

void light() {
    GLfloat lightAmbient[] = {1, 0, 0, 1.0};
    GLfloat lightDiffuse[] = {1.0, 1.0, 1.0, 1.0};
    GLfloat lightSpecular[] = {1.0, 1.0, 1.0, 1.0};
    GLfloat lightPosition[] = {(float) cos(lightCoord) * 100, (float) sin(lightCoord) * 100, 0.0, 0.0};


    glLightfv(GL_LIGHT0, GL_AMBIENT, lightAmbient);
    glLightfv(GL_LIGHT0, GL_DIFFUSE, lightDiffuse);
    glLightfv(GL_LIGHT0, GL_SPECULAR, lightSpecular);
    glLightfv(GL_LIGHT0, GL_POSITION, lightPosition);

    glEnable(GL_LIGHTING);
    glEnable(GL_LIGHT0);

    lightCoord += lightSpeed;
}

void display(void) {

    glClearColor(0, 0, 0, 1);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    changeCamera();
    scale();
    translate();
    rotate();
    light();

    int cubeParams[] = {35};

    FigureTemplate *figure = new SolidCube(&listId);
    figure->draw(255, 0, 0, 100, 55, 50, cubeParams);
    delete figure;

    figure = new WireCube(&listId);
    figure->draw(0, 255, 0, 100, 55, 10, cubeParams);
    delete figure;

    int torusParams[] = {10, 20, 300, 300};

    figure = new SolidTorus(&listId);
    figure->draw(0, 0, 255, 100, 55, -50, torusParams);
    delete figure;


    figure = new WireTorus(&listId);
    figure->draw(212, 111, 0, 100, 55, -150, torusParams);
    delete figure;


    printAllLists(0, 0, 0);
    printAllLists(100, 0, 0);
    listId = 1;


    displayGraphic();

    glutSwapBuffers();

}


void reshape(GLint w, GLint h) {
    glViewport(0, 0, w, h);
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    glOrtho(-w / 2, w / 2, -h / 2, h / 2, -10000, 10000);
}

void arrows(int key, int x, int y) {

    switch (key) {
        case GLUT_KEY_UP:
            vChange = true;
            vValue += cameraSpeed;
            break;
        case GLUT_KEY_DOWN:
            vChange = true;
            vValue -= cameraSpeed;
            break;
        case GLUT_KEY_LEFT:
            vChange = false;
            hValue -= cameraSpeed;
            break;
        case GLUT_KEY_RIGHT:
            vChange = false;
            hValue += cameraSpeed;
            break;
        default:
            std::cout << "unsupported button" << std::endl;
    }

    glutPostRedisplay();
}

void keyboard(unsigned char key, int x, int y) {
    switch (key) {
        case ESCAPE:
            exit(0);
        case '5':
            scaleCoef += 0.5;
            break;
        case '6':
            scaleCoef -= 0.5;
            break;
        case '1':
            transY -= 1;
            break;
        case '2':
            transY += 1;
            break;
        case '3':
            transX -= 1;
            break;
        case '4':
            transX += 1;
            break;
        case '8':
            rotateZ += rotateSpeed;
            break;
        case '7':
            rotateX += rotateSpeed;
            break;
        case '9':
            rotateY += rotateSpeed;
            break;
        default:
            std::cout << "unsupported button" << std::endl;

    }
    glutPostRedisplay();
}

void update(int value) {

    hValue += cameraSpeed;
    glutPostRedisplay();
    glutTimerFunc(100, update, 0);
}

int main(int argc, char *argv[]) {
    glutInit(&argc, argv);
    glutInitWindowSize(800, 800);
    glutCreateWindow("lab2");

    glutDisplayFunc(display);
    glutReshapeFunc(reshape);
    glutKeyboardFunc(keyboard);

    glutSpecialFunc(arrows);
    glEnable(GL_DEPTH_TEST);

//    glutTimerFunc(100, update, 0);
    glutMainLoop();
}
