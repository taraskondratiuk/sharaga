#include <GL/glut.h>
#include "FigureTemplate.cpp"


class SolidCube : public FigureTemplate {
public:
    explicit SolidCube(int *listId) : FigureTemplate(listId) {}


private:
    void drawSpecificFigure(int figureParams[]) override {
        glutSolidCube(figureParams[0]);
    }
};
