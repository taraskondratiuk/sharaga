

#include <GL/gl.h>
#include <GL/glut.h>


void reshape(int w, int h) {
    glViewport(0, 0, w, h);

    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    gluOrtho2D(0, w, 0, h);

    glMatrixMode(GL_MODELVIEW);
}

void display() {
    //polygons start
    glClearColor(255, 255, 0, 1);
    glClear(GL_COLOR_BUFFER_BIT);

    glColor3ub(0, 255, 0);

    //head
    glBegin(GL_QUADS);
    glVertex2i(100, 400);
    glVertex2i(100, 500);
    glVertex2i(200, 500);
    glVertex2i(200, 400);
    glEnd();

    glBegin(GL_TRIANGLES);
    glVertex2i(100, 500);
    glVertex2i(200, 600);
    glVertex2i(200, 500);
    glEnd();

    glBegin(GL_TRIANGLES);
    glVertex2i(100, 500);
    glVertex2i(160, 560);
    glVertex2i(100, 620);
    glEnd();

    //body
    glBegin(GL_TRIANGLES);
    glVertex2i(200, 450);
    glVertex2i(200, 250);
    glVertex2i(400, 450);
    glEnd();

    glBegin(GL_TRIANGLES);
    glVertex2i(400, 450);
    glVertex2i(600, 450);
    glVertex2i(600, 250);
    glEnd();

    glBegin(GL_TRIANGLES);
    glVertex2i(400, 450);
    glVertex2i(300, 350);
    glVertex2i(500, 350);
    glEnd();

    //tail
    glBegin(GL_QUADS);
    glVertex2i(600, 450);
    glVertex2i(635, 550);
    glVertex2i(720, 640);
    glVertex2i(695, 540);
    glEnd();
    //polygons end

    //lines start
    glLineWidth(4);
    glColor3ub(0, 0, 0);

    //head
    glBegin(GL_LINE_LOOP);
    glVertex2i(100, 400);
    glVertex2i(100, 500);
    glVertex2i(200, 500);
    glVertex2i(200, 400);
    glEnd();

    glBegin(GL_LINE_LOOP);
    glVertex2i(100, 500);
    glVertex2i(200, 600);
    glVertex2i(200, 500);
    glEnd();

    glBegin(GL_LINE_LOOP);
    glVertex2i(100, 500);
    glVertex2i(160, 560);
    glVertex2i(100, 620);
    glEnd();

    //body
    glBegin(GL_LINE_LOOP);
    glVertex2i(200, 450);
    glVertex2i(200, 250);
    glVertex2i(400, 450);
    glEnd();

    glBegin(GL_LINE_LOOP);
    glVertex2i(400, 450);
    glVertex2i(600, 450);
    glVertex2i(600, 250);
    glEnd();

    glBegin(GL_LINE_LOOP);
    glVertex2i(400, 450);
    glVertex2i(300, 350);
    glVertex2i(500, 350);
    glEnd();

    //tail
    glBegin(GL_LINE_LOOP);
    glVertex2i(600, 450);
    glVertex2i(635, 550);
    glVertex2i(720, 640);
    glVertex2i(695, 540);
    glEnd();
    //lines end

    glutSwapBuffers();
}

void moveFigure(unsigned char key, int x, int y) {
    switch (key) {
        case '=': {
            glTranslatef(-5, 5, 0);
            break;
        }
        case '-': {
            glTranslatef(5, -5, 0);
            break;
        }
    }
    glutPostRedisplay();
}

int main(int argc, char * argv[]) {
    glutInit(&argc, argv);
    glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB);

    glutInitWindowSize(800, 800);
    glutCreateWindow("lab1");

    glutDisplayFunc(display);
    glutReshapeFunc(reshape);

    glutKeyboardFunc(moveFigure);

    glutMainLoop();

    return 0;
}
