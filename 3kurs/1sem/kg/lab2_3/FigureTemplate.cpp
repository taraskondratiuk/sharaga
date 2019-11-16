#pragma once
#include <GL/gl.h>

class FigureTemplate {
public:
    int *listId;

    explicit FigureTemplate(int *listId) {
        this->listId = listId;
    }

    virtual ~FigureTemplate() = default;

    void draw(int r, int g, int b, int x, int y, int z, int figureParams[]) {
        glNewList(*listId, GL_COMPILE);
        (*listId)++;
        glPushMatrix();
        glColor3ub(r, g, b);
        glTranslatef(x, y, z);
        drawSpecificFigure(figureParams);
        glPopMatrix();
        glEndList();
    }

    virtual void drawSpecificFigure(int figureParams[]) = 0;


};